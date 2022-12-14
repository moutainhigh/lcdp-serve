package com.redxun.form.core.controller;

import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.github.pagehelper.Page;
import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.*;
import com.redxun.common.base.search.QueryFilter;
import com.redxun.common.base.search.QueryFilterBuilder;
import com.redxun.common.base.search.QueryParam;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.constant.MBoolean;
import com.redxun.common.engine.FtlEngine;
import com.redxun.common.engine.FtlUtil;
import com.redxun.common.excel.EasyExcelUtil;
import com.redxun.common.script.GroovyEngine;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.tool.constant.CommonConstant;
import com.redxun.common.utils.*;
import com.redxun.datasource.DataSourceContextHolder;
import com.redxun.db.CommonDao;
import com.redxun.db.DbUtil;
import com.redxun.dto.sys.SysInterfaceApiDto;
import com.redxun.dto.sys.SysWebReqDefDto;
import com.redxun.dto.user.OsInstDto;
import com.redxun.feign.OsInstClient;
import com.redxun.feign.OsUserClient;
import com.redxun.feign.SysInterfaceApiClient;
import com.redxun.feign.SysWebReqDefClient;
import com.redxun.form.bo.entity.FormBoAttr;
import com.redxun.form.bo.entity.FormBoEntity;
import com.redxun.form.bo.service.FormBoDefServiceImpl;
import com.redxun.form.bo.service.FormBoEntityServiceImpl;
import com.redxun.form.core.entity.*;
import com.redxun.form.core.service.*;
import com.redxun.form.core.service.export.FormBoListExport;
import com.redxun.log.annotation.AuditLog;
import com.redxun.log.model.Audit;
import com.redxun.log.model.LogContext;
import com.redxun.util.QueryFilterUtil;
import com.redxun.web.controller.BaseController;
import com.redxun.web.controller.IExport;
import freemarker.template.TemplateHashModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.Timestamp;
import java.util.*;


@Slf4j
@RestController
@RequestMapping("/form/core/formBoList")
@ClassDefine(title = "?????????????????????????????????", alias = "formBoListController", path = "/form/core/formBoList", packages = "core", packageName = "????????????")
@Api(tags = "?????????????????????????????????")
public class FormBoListController extends BaseController<FormBoList> {

    private final static int MAX_MILLION=1000000;
    private final static int ONE_HUNDRED_THOUSAND=100000;

    @Autowired
    FormBoListServiceImpl formBoListServiceImpl;
    @Autowired
    FormSolutionServiceImpl formSolutionService;
    @Autowired
    FormPcServiceImpl formPcService;
    @Autowired
    FormBoDefServiceImpl formBoDefService;
    @Autowired
    FormBoEntityServiceImpl formBoEntityService;
    @Autowired
    ListHistoryServiceImpl listHistoryService;
    @Resource
    SysWebReqDefClient sysWebReqDefClient;
    @Resource
    SysInterfaceApiClient sysInterfaceApiClient;
    @Resource
    FtlEngine freemarkEngine;
    @Resource
    GroovyEngine groovyEngine;
    @Resource
    FormBoPmtServiceImpl formBoPmtService;
    @Resource
    FormSqlLogServiceImpl formSqlLogService;
    @Resource
    ParameterHandler parameterHandler;
    @Autowired
    FormBoListExport formBoListExport;
    @Autowired
    SaveExportServiceImpl saveExportService;
    @Autowired
    PermissionSqlService permissionSqlService;
    @Autowired
    FormQueryStrategyServiceImpl formQueryStrategyService;

    @Resource
    OsUserClient osUserClient;
    @Resource
    OsInstClient osInstClient;

    private static final String WEBREQ = "WEBREQ";
    private static final String INTERFACE = "INTERFACE";
    @Override
    protected IExport getExport() {
        return formBoListExport;
    }

    @Override
    public BaseService getBaseService() {
        return formBoListServiceImpl;
    }

    @Override
    public String getComment() {
        return "?????????????????????????????????";
    }

    @Override
    protected void handleFilter(QueryFilter filter) {
        QueryFilterUtil.setQueryFilterByTreeId(filter, "TREE_ID_", "FORM", "read");
        super.handleFilter(filter);
    }


    @MethodDefine(title = "??????EXCEL", path = "/importExcel", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "????????????", varName = "request")})
    @ApiOperation(value = "??????EXCEL")
    @AuditLog(operation = "??????EXCEL")
    @PostMapping("/importExcel")
    public JsonResult importExcel(MultipartHttpServletRequest request) throws Exception {
        JSONObject statusData=new JSONObject();
        statusData.put("status","success");
        String boKey = RequestUtil.getString(request, "boListKey");
        FormBoList formBoList = formBoListServiceImpl.getByKey(boKey);
        if (formBoList == null) {
            String detail = "????????????key?????????!";
            LogContext.put(Audit.STATUS, Audit.STATUS_FAIL);
            LogContext.put(Audit.DETAIL, detail);
            statusData.put("status","error");
            return JsonResult.Success("????????????key?????????!").setData(statusData).setShow(false);
        }
        //????????????????????????
        String formAlias = formBoList.getFormAlias();
        FormSolution solution = formSolutionService.getByAlias(formAlias);
        if (solution == null) {
            String detail = "????????????????????????!";
            LogContext.put(Audit.STATUS, Audit.STATUS_FAIL);
            LogContext.put(Audit.DETAIL, detail);
            statusData.put("status","error");
            return JsonResult.Success("????????????????????????!").setData(statusData).setShow(false);
        }
        FormPc formPc = formPcService.get(solution.getFormId());
        String boDefId = formPc.getBodefId();

        FormBoEntity boEnt = formBoEntityService.getByDefId(boDefId, false);
        List<FormBoAttr> attrs = new ArrayList<>();
        String excelConfJson = formBoList.getExcelConfJson();
        JSONArray ary = JSONObject.parseObject(excelConfJson).getJSONArray("headerColumnsExcel");
        for (Object object : ary) {
            JSONObject obj = (JSONObject) object;
            FormBoAttr attr = new FormBoAttr();
            attr.setComment(obj.getString("header"));
            attr.setFieldName(obj.getString("field"));
            attr.setDataType(obj.getString("dataType"));
            attr.setFormat(obj.getString("format"));
            attrs.add(attr);
        }

        List<MultipartFile> files = request.getFiles("files[]");
        Iterator<MultipartFile> it = files.iterator();
        long amountSuccess = 0;
        long amountFail = 0;
        Map<String, Object> map = null;
        String errorStr="";
        while (it.hasNext()) {
            MultipartFile file = it.next();
            List<Map<String, Object>> dataList = new ArrayList<>();
            List<Map<Integer, String>> maps = EasyExcelUtil.readExcel(file, "2", 0);

            for (int j = 1; j < maps.size(); j++) {
                ArrayList<Object> indexs = new ArrayList<>();
                for (int i = 0; i < maps.get(1).size(); i++) {
                    if (maps.get(j).get(i) != null) {
                        indexs.add(i);
                    }
                }
                HashMap<String, Object> hashMap = new HashMap();
                for (Object index : indexs) {
                    String s = maps.get(0).get(index);
                    for (FormBoAttr attr : attrs) {
                        if (attr.getComment().equals(s)) {
                            hashMap.put(attr.getFieldName(), maps.get(j).get(index));
                        }
                    }
                }
                dataList.add(hashMap);
            }
            map = formBoListServiceImpl.insertData(dataList, boEnt, formBoList);
            amountSuccess +=(Long) map.get(FormBoListServiceImpl.AMOUTSUCCESS);
            amountFail += (Long) map.get(FormBoListServiceImpl.AMOUTFAIL);
            errorStr+= (String) map.get(FormBoListServiceImpl.ERRORSTR);
        }
        String detail = "???EXCEL??????" + boEnt.getTableName() + "???????????????" + amountSuccess + "?????????";
        String result = "????????????" + amountSuccess + "????????????";
        if (amountFail > 0) {
            String append = "?????????" + amountFail + "??????????????????????????????";
            detail = detail + "???" + append;
            result = result + "???" + append;
        }
        if(StringUtils.isNotEmpty(errorStr)){
            errorStr="??????"+errorStr;
            detail = detail + "???" + errorStr;
            result = result + "???" + errorStr;
            statusData.put("status","warning");
        }
        LogContext.put(Audit.DETAIL, detail);


        return JsonResult.Success(result).setData(statusData).setShow(false);
    }

    @MethodDefine(title = "????????????????????????????????????", path = "/getByKey", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "????????????", varName = "listKey")})
    @ApiOperation(value = "????????????????????????????????????")
    @AuditLog(operation = "????????????????????????????????????")
    @GetMapping("/getByKey")
    public JsonResult getByKey(@RequestParam("listKey") String listKey) {
        JsonResult result = JsonResult.Success();
        result.setShow(false);
        if (ObjectUtils.isEmpty(listKey)) {
            return result.setData(new JSONObject());
        } else {
            FormBoList formBoList = formBoListServiceImpl.getByKey(listKey);
            return result.setData(formBoList);
        }
    }

    @MethodDefine(title = "????????????", path = "/downTemp", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "????????????", varName = "request")})
    @ApiOperation(value = "????????????")
    @AuditLog(operation = "????????????")
    @PostMapping("downTemp")
    public void downTemp(HttpServletRequest request) throws Exception {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = attributes.getResponse();
        String boKey = RequestUtil.getString(request, "boListKey");
        FormBoList formBoList = formBoListServiceImpl.getByKey(boKey);
        if (formBoList == null) {
            return;
        }

        JSONArray attrs = JSONObject.parseObject(formBoList.getExcelConfJson()).getJSONArray("headerColumnsExcel");

        List<ExcelExportEntity> gridcolumns = new ArrayList<>();
        for (Object obj : attrs) {
            JSONObject attr = (JSONObject) obj;
            ExcelExportEntity gridcolumn = new ExcelExportEntity();
            gridcolumn.setName(attr.getString("header"));
            gridcolumn.setKey(attr.getString("field"));
            gridcolumns.add(gridcolumn);
        }
        String title = formBoList.getName();
        String detail = "??????Excel??????,??????:" + title + ",BOKEY:" + boKey;
        LogContext.put(Audit.DETAIL, detail);

        ExcelUtil.exportExcel(new JSONArray(), title, "????????????", gridcolumns, title + ".xlsx", response);
    }

    @MethodDefine(title = "????????????EXCEL", path = "/asyncExportExcel", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "????????????", varName = "request")})
    @ApiOperation(value = "????????????EXCEL")
    @AuditLog(operation = "????????????EXCEL")
    @PostMapping("/asyncExportExcel")
    public JsonResult asyncExportExcel(HttpServletRequest request) throws Exception {
        JsonResult result = JsonResult.Fail();
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        JSONObject excelConfig=getExcelConfig(request);
        if(!excelConfig.getBoolean("success")){
            return result.setMessage(excelConfig.getString("message"));
        }
        //???????????????
        String fileId = IdGenerator.getIdStr();
        excelConfig.put("fileId",fileId);
        IUser user = ContextUtil.getCurrentUser();
        formBoListServiceImpl.genExcel(excelConfig, user);
        //?????????????????????
        DataSourceContextHolder.setDefaultDataSource();
        return result.Success().setData(fileId);
    }

    public JSONObject getExcelConfig(HttpServletRequest request) throws Exception {
        JSONObject excelConfig = new JSONObject();
        excelConfig.put("success",true);
        String boListKey = RequestUtil.getString(request, "boListKey");
        String queryCondition = RequestUtil.getString(request,"queryCondition");
        String id=RequestUtil.getString(request, "id");
        Boolean async=RequestUtil.getBoolean(request, "async");
        String columns;
        if(id.equals("")) {
            columns = RequestUtil.getString(request, "columns");
        }else{
            SaveExport saveExport = saveExportService.get(id);
            columns = saveExport.getSetting();
        }

        JSONArray columnsAry=JSONArray.parseArray(columns);

        List<String> showColumns = new ArrayList<>();

        //????????????List
        for(int i = 0; i < columnsAry.size(); i++){
            JSONObject row = columnsAry.getJSONObject(i);
            if(row.getBoolean("showRender") != null && row.getBoolean("showRender") == true){
                showColumns.add((String) row.get("key"));
            }
        }

        boolean isMuti=isMultiHeader(columnsAry);

        FormBoList formBoList = formBoListServiceImpl.getByKey(boListKey);
        if (formBoList == null) {
            excelConfig.put("success",false);
            excelConfig.put("message","???"+boListKey+"???????????????????????????");
            return excelConfig;
        }

        String title = formBoList.getName();
        //??????excel????????????
        List<List<String>> heads = new ArrayList<>();
        List<List<String>> fields = new ArrayList<>();
        if (isMuti) {
            heads = EasyExcelUtil.constructMutiExportFieldColumns(title, columns, "name");
            fields = EasyExcelUtil.constructMutiExportFieldColumns(title, columns, "key");
        } else {
            heads = EasyExcelUtil.constructSingleExportFieldColumns(title, columns, heads, "name");
            fields = EasyExcelUtil.constructSingleExportFieldColumns(title, columns, fields, "key");
        }
        log.info("*****************exportExcel is begin");
        //????????????
        Map<String, Object> params = getParams(request);
        Map<String, Object> newParams = new HashMap<>(params);
        //????????????????????????
        params.remove("boListKey");
        params.remove("async");

        //??????????????????
        QueryData queryData = new QueryData();
        for (String key : params.keySet()) {
            queryData.getParams().put(key, (String) params.get(key));
        }
        QueryFilter queryFilter = QueryFilterBuilder.createQueryFilter(queryData);
        if(StringUtils.isNotEmpty(queryCondition)){
            //????????????
            formBoListServiceImpl.parseQueryCondition(queryFilter,queryCondition);
        }
        String sql = formBoListServiceImpl.getValidSql(formBoList, newParams);
        //???????????????
        DataSourceContextHolder.setDataSource(formBoList.getDbAs());
        //??????????????????
        Integer totalRowCount=getCountByGetData(boListKey,request);
        if(totalRowCount>MAX_MILLION || totalRowCount==0){
            //?????????????????????????????????????????????
            excelConfig.put("success",false);
            excelConfig.put("message","??????????????????1?????????????????????????????????????????????"+totalRowCount);
            return excelConfig;
        }

        excelConfig.put("dbAs",formBoList.getDbAs());
        excelConfig.put("sql",sql);
        excelConfig.put("title",title);
        excelConfig.put("heads",heads);
        excelConfig.put("fields",fields);
        excelConfig.put("totalRowCount",totalRowCount);
        excelConfig.put("showColumns",showColumns);
        excelConfig.put("pageSize",5000);
        excelConfig.put("boListKey",boListKey);
        excelConfig.put("formBoList",JSONObject.toJSONString(formBoList));
        excelConfig.put("queryDataStr",JSONObject.toJSONString(queryData));

        return excelConfig;
    }

    @MethodDefine(title = "??????EXCEL", path = "/exportExcel", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "????????????", varName = "request")})
    @ApiOperation(value = "??????EXCEL")
    @AuditLog(operation = "??????EXCEL")
    @PostMapping("/exportExcel")
    public void exportExcel(HttpServletRequest request) throws Exception {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = attributes.getResponse();

        JSONObject excelConfig=getExcelConfig(request);
        if(!excelConfig.getBoolean("success")){
            return;
        }

        List<List<String>> heads=excelConfig.getObject("heads",List.class);
        FormBoList formBoList = JSONObject.parseObject(excelConfig.getString("formBoList"),FormBoList.class);
        String title = formBoList.getName();
        Integer totalRowCount= excelConfig.getInteger("totalRowCount");
        String queryDataStr=excelConfig.getString("queryDataStr");
        QueryData queryData = JSONObject.parseObject(queryDataStr,QueryData.class);
        //????????????
        queryData.setPageSize(totalRowCount);

        queryData.setPageNo(1);
        QueryFilter filter = QueryFilterBuilder.createQueryFilter(queryData);
        List<List<Object>> data = formBoListServiceImpl.getSqlDataBySql(excelConfig,filter,formBoList);
        filter =null;
        if(BeanUtil.isEmpty(data)){
            return;
        }

        //?????????????????????????????????
        String paramVal = parameterHandler.handParamters(formBoList.getSearchJson(), request);
        log.info("*****************parameterHandler.handParamters is end");

        String detail = "??????[" + title + "]??????,";
        if (StringUtils.isNotEmpty(paramVal)) {
            detail += paramVal + ",";
        }
        detail += ",????????????????????????:" + totalRowCount + "??????";

        LogContext.put(Audit.DETAIL, detail);
        LogContext.put(Audit.ACTION, "exportExcel");
        EasyExcelUtil.writeExcel(title, heads, data, response);
        log.info("*****************EasyExcelUtil.writeExcel is end");
        //?????????????????????
        DataSourceContextHolder.setDefaultDataSource();
        heads = null;
        data = null;
    }


    private int getCountByGetData(String boKey,HttpServletRequest request){
        int countNum=0;
        FormBoList formBoList = formBoListServiceImpl.getByKey(boKey);
        if (formBoList == null) {
            return countNum;
        }
        //???????????????????????????????????????????????????
        //????????????
        Map<String, Object> param = getParams(request);
        Map<String, Object> newParams = new HashMap<>(param);
        //????????????????????????
        param.remove("boListKey");

        QueryData queryData = new QueryData();
        for (String key : param.keySet()) {
            queryData.getParams().put(key, (String) param.get(key));
        }
        queryData.setPageSize(1);
        QueryFilter queryFilter = QueryFilterBuilder.createQueryFilter(queryData);

        try {
            Map<String, Object> params = RequestUtil.getParameterValueMap(request, false);
            //??????queryFilter
            handQueryFilter(queryFilter, params);
            //????????????
            params.putAll(queryFilter.getParams());
            //??????????????????Context??????
            addContextVar(params);
            String webReq = "WEBREQ";
            String tree = "tree";
            //????????????
            JsonPageResult webData=null;
            if (webReq.equals(formBoList.getUseCondSql())) {
                params.put("pageSize", queryData.getPageSize().toString());
                params.put("pageNo", queryData.getPageNo().toString());
                webData=formBoListServiceImpl.executeWebReq(formBoList, params);
            }

            if(BeanUtil.isNotEmpty(webData)){
                return instanceofObj(webData.getData());
            }

            String sql = formBoListServiceImpl.getValidSql(formBoList, params);
            String sql2 = "";
            DataSourceContextHolder.setDataSource(formBoList.getDbAs());
            if (MBoolean.YES.val.equals(formBoList.getIsPage()) && !tree.equals(formBoList.getDataStyle())) {
                Page list = formBoListServiceImpl.getPageDataBySql(sql, queryFilter,"false");
                return (int)list.getTotal();
            } else {
                List list = new ArrayList();
                //??????????????????,???????????????????????????
                if (tree.equals(formBoList.getDataStyle())) {
                    Map<String, String> queryParams = queryData.getParams();
                    if (queryParams == null) {
                        params = new HashMap();
                    }
                    String[] parentIds = queryParams.getOrDefault(formBoList.getIdField(), "0").split(",");
                    String parentId = parentIds[parentIds.length - 1];

                    if ("YES".equals(formBoList.getIsLazy())) {
                        // ?????????
                        int orderBy = sql.lastIndexOf("ORDER BY");
                        String order = "";
                        if (orderBy != -1) {
                            order = sql.substring(orderBy);
                            sql = sql.substring(0, orderBy);
                        }

                        sql = "select * from(" + sql + ") tmp";
                        sql2 = sql;

                        if (!"0".equals(parentId)) {
                            sql += " where " + formBoList.getParentField() + "='" + parentId + "' ";
                            queryFilter.getParams().clear();
                            queryFilter.getFieldLogic().getWhereParams().clear();
                        } else if ("0".equals(parentId)) {
                            if (queryFilter.getFieldLogic().getWhereParams().size() == 0) {
                                sql += " where " + formBoList.getParentField() + "='" + parentId + "' or "
                                        + formBoList.getParentField() + " is null ";
                            }
                        }
                        sql += " " + order;
                        sql2 += " " + order;
                    }
                    list = formBoListServiceImpl.getDataBySql(sql, queryFilter,"false");
                    if (list.size() == 0 & "0".equals(parentId)) {
                        list = formBoListServiceImpl.getDataBySql(sql2, queryFilter,"false");
                    }
                    if ("YES".equals(formBoList.getIsLazy())) {
                        for (Object obj : list) {
                            HashMap map = (HashMap<String, Object>) obj;
                            map.put("children", new JSONArray());
                        }
                    }
                } else {
                    list = formBoListServiceImpl.getDataBySql(sql, queryFilter,"false");
                }
                return (int)list.size();
            }
        } catch (Exception e) {
            return 0;
        }
    }

    private int instanceofObj(Object obj){
        if (obj == null) {
            return 0;
        }
        if (obj instanceof List) {
            return ((List) obj).size();
        }else if (obj instanceof HashMap) {
            return ((HashMap) obj).size();
        }else if (obj instanceof String) {
            return 0;
        } else if (obj instanceof Date) {
            return 0;
        } else if (obj instanceof Calendar) {
            return 0;
        } else if (obj instanceof Timestamp) {
            return 0;
        } else if (obj instanceof Double) {
            return (int)obj;
        }else {
            return 0;
        }
    }


    @MethodDefine(title = "??????EXCEL", path = "/canExportExcel", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "????????????", varName = "request")})
    @ApiOperation(value = "????????????????????????EXCEL")
    @AuditLog(operation = "????????????????????????EXCEL")
    @PostMapping("/canExportExcel")
    public JsonResult canExportExcel(HttpServletRequest request) throws Exception {
        String boListKey = RequestUtil.getString(request, "boListKey");
        String id=RequestUtil.getString(request, "id");
        FormBoList formBoList = formBoListServiceImpl.getByKey(boListKey);
        if (formBoList == null) {
            return JsonResult.Fail("????????????????????????!");
        }
        //????????????
        Map<String, Object> params = getParams(request);
        Map<String, Object> newParams = new HashMap<>(params);
        //????????????????????????
        params.remove("boListKey");

        QueryData queryData = new QueryData();
        for (String key : params.keySet()) {
            queryData.getParams().put(key, (String) params.get(key));
        }
        QueryFilter queryFilter = QueryFilterBuilder.createQueryFilter(queryData);

        String sql = formBoListServiceImpl.getValidSql(formBoList, newParams);
        DataSourceContextHolder.setDataSource(formBoList.getDbAs());

        Long rtn= formBoListServiceImpl.getCountDataBySql(sql, queryFilter, formBoList.getIsTest());
        if(rtn==0){
            return JsonResult.Fail().setShow(true).setMessage("?????????????????????");
        }
        if(rtn>MAX_MILLION){
            //?????????????????????????????????????????????
            return JsonResult.Fail().setShow(true).setMessage("????????????????????????????????????????????????????????????");
        }

        //???????????????????????????????????????????????????????????????
        JSONObject async=new JSONObject();
        Integer restriction = formBoList.getRestriction();
        //??????????????????????????? ????????????-1?????????????????????????????????????????????
        if(BeanUtil.isNotEmpty(restriction) && restriction!=-1 && rtn>restriction){
            async.put("async",true);
        }else {
            async.put("async",false);
        }
        return JsonResult.Success().setShow(false).setData(async);
    }


    /**
     * ????????????????????????
     * @param jsonArray
     * @return
     */
    private boolean isMultiHeader(JSONArray jsonArray){
        for(int i=0;i<jsonArray.size();i++){
            JSONObject json=jsonArray.getJSONObject(i);
            if(json.containsKey("children") ){
                JSONArray childs=json.getJSONArray("children");
                if(childs.size()>0){
                    return true;
                }
            }
        }
        return false;
    }

    private JSONArray parseToArray(String str) {
        JSONArray array = JSONArray.parseArray(str);
        if (array == null) {
            return new JSONArray();
        }
        for (int i = 0; i < array.size(); i++) {
            JSONObject json = array.getJSONObject(i);
            if (!json.containsKey("type")) {
                json.put("type", "field");
            }
        }
        return array;
    }

    @MethodDefine(title = "???????????????????????????KEY???", path = "/reloadColumnsByKey", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "??????KEY", varName = "key")})
    @ApiOperation(value = "??????????????????")
    @GetMapping("/reloadColumnsByKey")
    public JsonResult reloadColumnsByKey(@RequestParam(value = "key") String key) {
        FormBoList formBoList = formBoListServiceImpl.getByKey(key);
        return reloadColumns(formBoList.getId());
    }

    @MethodDefine(title = "???????????????????????????ID???", path = "/reloadColumns", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "??????ID", varName = "id")})
    @ApiOperation(value = "??????????????????")
    @GetMapping("/reloadColumns")
    public JsonResult reloadColumns(@RequestParam(value = "id") String id) {
        FormBoList formBoList = formBoListServiceImpl.get(id);
        JsonResult result = new JsonResult(true).setShow(false);
        DataSourceContextHolder.setDataSource(formBoList.getDbAs());
        try {
            if (WEBREQ.equals(formBoList.getUseCondSql())) {

            } else if(INTERFACE.equals(formBoList.getUseCondSql())){
                //???????????????????????????
                JSONArray headerCols = JSONArray.parseArray(formBoList.getColsJson());
                JSONArray fieldCols = new JSONArray();
                if (headerCols == null) {
                    headerCols = new JSONArray();
                }
                if(StringUtils.isNotEmpty(formBoList.getInterfaceKey())) {
                    SysInterfaceApiDto sysInterfaceApiDto=sysInterfaceApiClient.getById(formBoList.getInterfaceKey());
                    Map<String, JSONObject> jsonFieldMap = getFieldJsonMap(formBoList.getFieldsJson());
                    JSONArray returnFileds = JSONArray.parseArray(sysInterfaceApiDto.getApiReturnFields());
                    JSONArray returnColumns=parseReturnColumns(returnFileds);
                    for (Object obj : returnColumns) {
                        JSONObject gh = (JSONObject) obj;
                        JSONObject fieldObj = new JSONObject();
                        String desc = gh.getString("paramDesc");
                        if (StringUtils.isEmpty(desc)) {
                            desc = gh.getString("paramName");
                        }
                        //???????????????????????????????????????????????????????????????
                        if ("YES".equals(formBoList.getIsDialog())) {
                            JSONObject fieldJson = jsonFieldMap.get(gh.getString("paramName"));
                            if (fieldJson == null) {
                                fieldObj.put("header", desc);
                                fieldObj.put("isReturn", false);
                                fieldObj.put("visible", false);
                            } else {
                                fieldObj.put("header", fieldJson.getString("header"));
                                fieldObj.put("isReturn", fieldJson.getBoolean("isReturn"));
                                fieldObj.put("visible", fieldJson.getBoolean("visible"));
                            }
                        } else {
                            fieldObj.put("header", desc);
                        }
                        fieldObj.put("field", gh.getString("paramName"));
                        fieldObj.put("allowSort", false);

                        //????????????????????????????????????
                        JSONObject dataType = JSONObject.parseObject("{'string':'??????','number':'??????','date':'?????????'}");
                        if (gh.getString("paramName").equals(FormBoEntity.FIELD_CREATE_TIME) || gh.getString("paramName").equals(FormBoEntity.FIELD_UPDATE_TIME)) {
                            fieldObj.put("dataType", "date");
                            fieldObj.put("format", CommonConstant.DATE_FORMAT);
                            fieldObj.put("dataType_name", dataType.getString("date"));
                        } else {
                            fieldObj.put("dataType", "number".equals(gh.getString("paramType")) ? "int" : gh.getString("paramType"));
                            fieldObj.put("dataType_name", dataType.getString(gh.getString("paramType")));
                        }
                        setDefaultFieldObj(fieldObj,formBoList);
                        fieldCols.add(fieldObj);
                        //????????????
                        boolean isFound = false;
                        for (int i = 0; i < headerCols.size(); i++) {
                            JSONObject headerObj=headerCols.getJSONObject(i);
                            String field = headerObj.getString("field");
                            if (field == null) {
                                continue;
                            }
                            if (field.equals(gh.getString("paramName"))) {
                                isFound = true;
                                headerObj.put("headerAlign",fieldObj.getString("headerAlign"));
                                headerObj.put("headerAlign_name",fieldObj.getString("headerAlign_name"));
                                break;
                            }
                        }
                        if (!isFound) {
                            JSONObject cloneObj = (JSONObject) fieldObj.clone();
                            cloneObj.remove("isReturn");
                            cloneObj.remove("visible");
                            headerCols.add(cloneObj);
                        }
                    }
                }
                result.setData(headerCols);
                result.setMessage("?????????????????????");
            } else {
                //???????????????????????????
                JSONArray headerCols = JSONArray.parseArray(formBoList.getColsJson());
                String sql = getHeaderSql(request, formBoList);
                List<GridHeader> headers = DbUtil.getGridHeader(sql);
                JSONArray fieldCols = new JSONArray();
                if (headerCols == null) {
                    headerCols = new JSONArray();
                }
                Map<String, JSONObject> jsonFieldMap = getFieldJsonMap(formBoList.getFieldsJson());
                for (GridHeader gh : headers) {
                    JSONObject fieldObj = new JSONObject();
                    if ("YES".equals(formBoList.getIsDialog())) {
                        JSONObject fieldJson = jsonFieldMap.get(gh.getFieldName());
                        if (fieldJson == null) {
                            fieldObj.put("header", gh.getFieldLabel());
                            fieldObj.put("isReturn", false);
                            fieldObj.put("visible", false);
                        } else {
                            fieldObj.put("header", fieldJson.getString("header"));
                            fieldObj.put("isReturn", fieldJson.getBoolean("isReturn"));
                            fieldObj.put("visible", fieldJson.getBoolean("visible"));
                        }
                    } else {
                        fieldObj.put("header", gh.getFieldLabel());
                    }
                    fieldObj.put("field", gh.getFieldName());
                    fieldObj.put("header", gh.getFieldLabel());
                    //????????????????????????????????????
                    JSONObject dataType = JSONObject.parseObject("{'string':'??????','number':'??????','date':'?????????'}");
                    if (gh.getFieldName().equals(FormBoEntity.FIELD_CREATE_TIME) || gh.getFieldName().equals(FormBoEntity.FIELD_UPDATE_TIME)) {
                        fieldObj.put("dataType", "date");
                        fieldObj.put("format", CommonConstant.DATE_FORMAT);
                        fieldObj.put("dataType_name", dataType.getString("date"));
                    } else {
                        fieldObj.put("dataType", "number".equals(gh.getDataType()) ? "int" : gh.getDataType());
                        fieldObj.put("dataType_name", dataType.getString(gh.getDataType()));
                    }
                    fieldObj.put("queryDataType", gh.getQueryDataType());
                    setDefaultFieldObj(fieldObj,formBoList);
                    fieldCols.add(fieldObj);
                    //????????????
                    boolean isFound = false;
                    for (int i = 0; i < headerCols.size(); i++) {
                        JSONObject headerObj=headerCols.getJSONObject(i);
                        String field = headerObj.getString("field");
                        if (field == null) {
                            continue;
                        }
                        if (field.equals(gh.getFieldName())) {
                            isFound = true;
                            headerObj.put("headerAlign",fieldObj.getString("headerAlign"));
                            headerObj.put("headerAlign_name",fieldObj.getString("headerAlign_name"));
                            break;
                        }
                    }
                    if (!isFound) {
                        JSONObject cloneObj = (JSONObject) fieldObj.clone();
                        cloneObj.remove("isReturn");
                        cloneObj.remove("visible");
                        headerCols.add(cloneObj);
                    }
                }
                result.setData(headerCols);
                result.setMessage("?????????????????????");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setSuccess(false);
            result.setMessage("????????????");
        } finally {
            DataSourceContextHolder.setDefaultDataSource();
        }
        return result;
    }

    @MethodDefine(title = "????????????Excel??????", path = "/reloadColumnsExcel", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "??????ID", varName = "id")})
    @ApiOperation(value = "??????????????????")
    @GetMapping("/reloadColumnsExcel")
    public JsonResult reloadColumnsExcel(@RequestParam(value = "id") String id) {
        FormBoList formBoList = formBoListServiceImpl.get(id);
        JsonResult result = new JsonResult(true);
        DataSourceContextHolder.setDataSource(formBoList.getDbAs());
        try {
            if (WEBREQ.equals(formBoList.getUseCondSql())) {

            } else if(INTERFACE.equals(formBoList.getUseCondSql())){
                //???????????????????????????
                JSONArray headerCols = null;
                String excelConfJson = formBoList.getExcelConfJson();
                if (StringUtils.isEmpty(excelConfJson)) {
                    headerCols = new JSONArray();
                } else {
                    JSONObject jo = JSONObject.parseObject(excelConfJson);
                    if (jo == null) {
                        headerCols = new JSONArray();
                    } else {
                        headerCols = jo.getJSONArray("headerColumnsExcel");
                    }
                }
                JSONArray fieldCols = new JSONArray();
                if (headerCols == null) {
                    headerCols = new JSONArray();
                }
                if(StringUtils.isNotEmpty(formBoList.getInterfaceKey())) {
                    SysInterfaceApiDto sysInterfaceApiDto=sysInterfaceApiClient.getById(formBoList.getInterfaceKey());
                    Map<String, JSONObject> jsonFieldMap = getFieldJsonMap(formBoList.getFieldsJson());
                    JSONArray returnFileds = JSONArray.parseArray(sysInterfaceApiDto.getApiReturnFields());
                    JSONArray returnColumns=parseReturnColumns(returnFileds);
                    for (Object obj : returnColumns) {
                        JSONObject gh = (JSONObject) obj;
                        JSONObject fieldObj = new JSONObject();
                        String desc = gh.getString("paramDesc");
                        if (StringUtils.isEmpty(desc)) {
                            desc = gh.getString("paramName");
                        }
                        //???????????????????????????????????????????????????????????????
                        if ("YES".equals(formBoList.getIsDialog())) {
                            JSONObject fieldJson = jsonFieldMap.get(gh.getString("paramName"));
                            if (fieldJson == null) {
                                fieldObj.put("header", desc);
                                fieldObj.put("isReturn", false);
                                fieldObj.put("visible", false);
                            } else {
                                fieldObj.put("header", fieldJson.getString("header"));
                                fieldObj.put("isReturn", fieldJson.getBoolean("isReturn"));
                                fieldObj.put("visible", fieldJson.getBoolean("visible"));
                            }
                        } else {
                            fieldObj.put("header", desc);
                        }
                        fieldObj.put("field", gh.getString("paramName"));
                        fieldObj.put("allowSort", false);

                        //????????????????????????????????????
                        JSONObject dataType = JSONObject.parseObject("{'string':'??????','number':'??????','date':'?????????'}");
                        if (gh.getString("paramName").equals(FormBoEntity.FIELD_CREATE_TIME) || gh.getString("paramName").equals(FormBoEntity.FIELD_UPDATE_TIME)) {
                            fieldObj.put("dataType", "date");
                            fieldObj.put("format", CommonConstant.DATE_FORMAT);
                            fieldObj.put("dataType_name", dataType.getString("date"));
                        } else {
                            fieldObj.put("dataType", "number".equals(gh.getString("paramType")) ? "int" : gh.getString("paramType"));
                            fieldObj.put("dataType_name", dataType.getString(gh.getString("paramType")));
                        }
                        setDefaultFieldObj(fieldObj,formBoList);
                        fieldCols.add(fieldObj);
                        //????????????
                        boolean isFound = false;
                        for (int i = 0; i < headerCols.size(); i++) {
                            JSONObject headerObj=headerCols.getJSONObject(i);
                            String field = headerObj.getString("field");
                            if (field == null) {
                                continue;
                            }
                            if (field.equals(gh.getString("paramName"))) {
                                isFound = true;
                                headerObj.put("headerAlign",fieldObj.getString("headerAlign"));
                                headerObj.put("headerAlign_name",fieldObj.getString("headerAlign_name"));
                                break;
                            }
                        }
                        if (!isFound) {
                            JSONObject cloneObj = (JSONObject) fieldObj.clone();
                            cloneObj.remove("isReturn");
                            cloneObj.remove("visible");
                            headerCols.add(cloneObj);
                        }
                    }
                }
                result.setData(headerCols);
                result.setMessage("?????????????????????");
            } else {
                //???????????????????????????

                JSONArray headerCols = null;
                String excelConfJson = formBoList.getExcelConfJson();
                if (StringUtils.isEmpty(excelConfJson)) {
                    headerCols = new JSONArray();
                } else {
                    JSONObject jo = JSONObject.parseObject(excelConfJson);
                    if (jo == null) {
                        headerCols = new JSONArray();
                    } else {
                        headerCols = jo.getJSONArray("headerColumnsExcel");
                    }
                }

                String sql = getHeaderSql(request, formBoList);
                List<GridHeader> headers = DbUtil.getGridHeader(sql);
                JSONArray fieldCols = new JSONArray();
                if (headerCols == null) {
                    headerCols = new JSONArray();
                }
                Map<String, JSONObject> jsonFieldMap = getFieldJsonMap(formBoList.getFieldsJson());
                for (GridHeader gh : headers) {
                    JSONObject fieldObj = new JSONObject();
                    if ("YES".equals(formBoList.getIsDialog())) {
                        JSONObject fieldJson = jsonFieldMap.get(gh.getFieldName());
                        if (fieldJson == null) {
                            fieldObj.put("header", gh.getFieldLabel());
                            fieldObj.put("isReturn", false);
                            fieldObj.put("visible", false);
                        } else {
                            fieldObj.put("header", fieldJson.getString("header"));
                            fieldObj.put("isReturn", fieldJson.getBoolean("isReturn"));
                            fieldObj.put("visible", fieldJson.getBoolean("visible"));
                        }
                    } else {
                        fieldObj.put("header", gh.getFieldLabel());
                    }
                    fieldObj.put("field", gh.getFieldName());
                    fieldObj.put("header", gh.getFieldLabel());
                    //????????????????????????????????????
                    JSONObject dataType = JSONObject.parseObject("{'string':'??????','number':'??????','date':'?????????'}");
                    if (gh.getFieldName().equals(FormBoEntity.FIELD_CREATE_TIME) || gh.getFieldName().equals(FormBoEntity.FIELD_UPDATE_TIME)) {
                        fieldObj.put("dataType", "date");
                        fieldObj.put("format", CommonConstant.DATE_FORMAT);
                        fieldObj.put("dataType_name", dataType.getString("date"));
                    } else {
                        fieldObj.put("dataType", "number".equals(gh.getDataType()) ? "int" : gh.getDataType());
                        fieldObj.put("dataType_name", dataType.getString(gh.getDataType()));
                    }
                    fieldObj.put("queryDataType", gh.getQueryDataType());
                    setDefaultFieldObj(fieldObj,formBoList);
                    fieldCols.add(fieldObj);
                    //????????????
                    boolean isFound = false;
                    for (int i = 0; i < headerCols.size(); i++) {
                        JSONObject headerObj=headerCols.getJSONObject(i);
                        String field = headerObj.getString("field");
                        if (field == null) {
                            continue;
                        }
                        if (field.equals(gh.getFieldName())) {
                            isFound = true;
                            headerObj.put("headerAlign",fieldObj.getString("headerAlign"));
                            headerObj.put("headerAlign_name",fieldObj.getString("headerAlign_name"));
                            break;
                        }
                    }
                    if (!isFound) {
                        JSONObject cloneObj = (JSONObject) fieldObj.clone();
                        cloneObj.remove("isReturn");
                        cloneObj.remove("visible");
                        headerCols.add(cloneObj);
                    }
                }
                result.setData(headerCols);
                result.setMessage("?????????????????????");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setSuccess(false);
            result.setMessage("????????????");
        } finally {
            DataSourceContextHolder.setDefaultDataSource();
        }
        return result;
    }

    private void setDefaultFieldObj(JSONObject fieldObj,FormBoList formBoList) {
        JSONObject headerAlign = JSONObject.parseObject("{'left':'??????','center':'??????','right':'??????'}");
        //??????????????????
        String fontPosition=formBoList.getFontPosition();
        fieldObj.put("headerAlign",fontPosition);
        fieldObj.put("headerAlign_name",headerAlign.getString(fontPosition));
        fieldObj.put("width", 100);
        JSONObject json=new JSONObject();
        json.put("value",fieldObj.getString("field"));
        json.put("label",fieldObj.getString("header"));
        json.put("key",fieldObj.getString("field"));
        fieldObj.put("fieldExt", json);
        fieldObj.put("allowSort", false);
        if (!fieldObj.containsKey("format")) {
            fieldObj.put("format", "");
        }
        json=new JSONObject();
        json.put("value",fieldObj.getString("dataType"));
        json.put("label",fieldObj.getString("dataType_name"));
        fieldObj.put("dataTypeExt", json);
        json=new JSONObject();
        json.put("value",fieldObj.getString("headerAlign"));
        json.put("label",fieldObj.getString("headerAlign_name"));
        fieldObj.put("headerAlignExt", json);
        fieldObj.put("fixedExt", "");
        fieldObj.put("url", "");
        fieldObj.put("linkType", "");
        fieldObj.put("renderTypeExt", "");
        fieldObj.put("controlExt", "");
    }


    @MethodDefine(title = "????????????????????????", path = "/saveConfigJson", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "????????????", varName = "json")})
    @ApiOperation(value = "????????????????????????")
    @PostMapping("/saveConfigJson")
    public JsonResult saveConfigJson(@RequestBody JSONObject json) throws Exception {
        String id = json.getString("id");
        String colsJson = json.getString("colsJson");
        String isShowLeft = json.getString("isShowLeft");
        String isExpandRow = json.getString("isExpandRow");
        String isDialog = json.getString("isDialog");
        String isSearchHeader = json.getString("isSearchHeader");
        String isSearchLayout = json.getString("isSearchLayout");
        String isShowSearch = json.getString("isShowSearch");
        String fieldJson = json.getString("fieldJson");
        String searchJson = json.getString("searchJson");
        String topBtnsJson = json.getString("topBtnsJson");
        String dataRightJson = json.getString("dataRightJson");
        String publishConf = json.getString("publishConf");
        String bodyScript = json.getString("bodyScript");
        String idField = json.getString("idField");
        String textField = json.getString("textField");
        String parentField = json.getString("parentField");
        Integer buttonMax = json.getInteger("buttonMax");
        String webreqMappingJson = json.getString("webreqMappingJson");
        String interfaceMappingJson = json.getString("interfaceMappingJson");
        String javaCode = json.getString("javaCode");
        String importDataHandler = json.getString("importDataHandler");
        String importDataHandlerName = json.getString("importDataHandlerName");
        String headerColumnsExcel = json.getString("headerColumnsExcel");
        String excelExport = json.getString("excelExport");
        Integer searchMax = json.getInteger("searchMax");
        String instColumnConfig = json.getString("instColumnConfig");
        String editSearchViewJson = json.getString("editSearchViewJson");
        String queryStrategyViewJson = json.getString("queryStrategyViewJson");
        String isSearchView = json.getString("isSearchView");
        String isShowView = json.getString("isShowView");
        String isFieldShow = json.getString("isFieldShow");
        String mobileConf = json.getString("mobileConf");

        FormBoList formBoList = formBoListServiceImpl.get(id);
        //????????????
        formBoListServiceImpl.removeCache(formBoList.getKey());


        if (handlerColsJson(colsJson)) {
            return JsonResult.getFailResult("???????????????????????????key");
        }
        formBoList.setColsJson(colsJson);
        if (MBoolean.YES.val.equals(isShowLeft)) {
            String leftTreeJson = json.getString("leftTreeJson");
            formBoList.setLeftTreeJson(leftTreeJson);
        }
        if (MBoolean.YES.val.equals(isExpandRow)) {
            String expandRowJson = json.getString("expandRowJson");
            formBoList.setExpandRowJson(expandRowJson);
        }
        formBoList.setTopBtnsJson(topBtnsJson);
        if (MBoolean.YES.val.equals(isDialog)) {
            formBoList.setFieldsJson(fieldJson);
        }
        formBoList.setIsSearchHeader(isSearchHeader);
        formBoList.setIsSearchLayout(isSearchLayout);
        formBoList.setIsShowSearch(isShowSearch);
        formBoList.setIdField(idField);
        formBoList.setTextField(textField);
        formBoList.setParentField(parentField);
        formBoList.setButtonMax(buttonMax);
        formBoList.setSearchJson(searchJson);
        formBoList.setBodyScript(bodyScript);
        formBoList.setPublishConf(publishConf);
        formBoList.setWebreqMappingJson(webreqMappingJson);
        formBoList.setInterfaceMappingJson(interfaceMappingJson);
        formBoList.setSearchMax(searchMax);
        formBoList.setInstColumnConfig(instColumnConfig);
        formBoList.setEditSearchViewJson(editSearchViewJson);
        formBoList.setIsSearchView(isSearchView);
        formBoList.setIsShowView(isShowView);
        formBoList.setIsFieldShow(isFieldShow);
        formBoList.setMobileConf(mobileConf);

        //??????????????????
        formBoPmtService.saveConfig(dataRightJson,formBoList.getId(),formBoList.getKey());
        //??????????????????
        formQueryStrategyService.saveConfig(queryStrategyViewJson,formBoList.getId(),formBoList.getAppId());
        //????????????
        saveExportService.saveConfig(excelExport,formBoList.getKey(),formBoList.getAppId());
        //Excel????????????
        JSONObject excelConf = new JSONObject();
        excelConf.put("javaCode", javaCode);
        excelConf.put("importDataHandler", importDataHandler);
        excelConf.put("importDataHandlerName", importDataHandlerName);
        excelConf.put("headerColumnsExcel", StringUtils.isEmpty(headerColumnsExcel) ? "" : JSONArray.parseArray(headerColumnsExcel));
        formBoList.setExcelConfJson(excelConf.toJSONString());
        formBoListServiceImpl.update(formBoList);
        return JsonResult.getSuccessResult("???????????????????????????");
    }

    private boolean handlerColsJson(String colsJson) {
        JSONArray ary = JSONArray.parseArray(colsJson);
        Map<String, Integer> fieldCount = new HashMap<>(ary.size());
        for (Object object : ary) {
            JSONObject obj = (JSONObject) object;
            Integer count = fieldCount.get(obj.getString("field"));
            if (count == null) {
                count = 0;
            }
            fieldCount.put(obj.getString("field"), count + 1);
        }
        for (Integer count : fieldCount.values()) {
            if (count > 1) {
                return true;
            }
        }
        return false;
    }

    @MethodDefine(title = "??????edit2??????????????????", path = "/getEdit2", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "????????????", varName = "request")})
    @ApiOperation(value = "edit2??????", notes = "??????edit2??????????????????")
    @GetMapping("/getEdit2")
    public JSONObject getEdit2(HttpServletRequest request) {
        String id = RequestUtil.getString(request, "id");
        FormBoList formBoList = formBoListServiceImpl.get(id);
        String formAlias=formBoList.getFormAlias();
        FormSolution solution = formSolutionService.getByAlias(formAlias);
        String errorMsg = null;
        JSONObject mv = new JSONObject();
        try {
            List<FormQueryStrategy> queryList=formQueryStrategyService.getByListId(formBoList.getId(),"SYSTEM");
            mv.put("queryStrategyViewJson",JSONArray.toJSONString(queryList));
            List<SaveExport> saveExportList=saveExportService.getByListPublic(formBoList.getKey());
            mv.put("excelExport",saveExportList);
            if (WEBREQ.equals(formBoList.getUseCondSql())) {

                //excel??????????????????
                JSONObject excelConf = JSONObject.parseObject(formBoList.getExcelConfJson());


                if (excelConf == null) {
                    excelConf = new JSONObject();
                    excelConf.put("javaCode", "");
                    excelConf.put("importDataHandler", "");
                    excelConf.put("importDataHandlerName", "");
                    excelConf.put("headerColumnsExcel", new JSONArray());

                }

                //???????????????????????????
                JSONArray headerCols = JSONArray.parseArray(formBoList.getColsJson());
                JSONArray fieldCols = JSONArray.parseArray(formBoList.getFieldsJson());
                if (headerCols == null) {
                    headerCols = new JSONArray();
                }
                if (fieldCols == null) {
                    fieldCols = new JSONArray();
                }
                getWebReqColumns(mv, formBoList, headerCols, fieldCols, excelConf);
                String headerColumns = headerCols.toJSONString();
                String fieldColumns = fieldCols.toJSONString();
                if (StringUtils.isEmpty(formBoList.getColsJson())) {
                    formBoList.setColsJson(headerColumns);
                    formBoList.setFieldsJson(fieldColumns);
                    formBoListServiceImpl.update(formBoList);
                }
            } else if(INTERFACE.equals(formBoList.getUseCondSql())) {
                //excel??????????????????
                JSONObject excelConf = JSONObject.parseObject(formBoList.getExcelConfJson());

                boolean isNeedAdExcelConf = (excelConf == null || excelConf.size() == 0
                        || excelConf.getJSONArray("headerColumnsExcel") == null
                        || excelConf.getJSONArray("headerColumnsExcel").size() == 0) ? true : false;
                if (excelConf == null) {
                    excelConf = new JSONObject();
                    excelConf.put("javaCode", "");
                    excelConf.put("importDataHandler", "");
                    excelConf.put("importDataHandlerName", "");
                    excelConf.put("headerColumnsExcel", new JSONArray());
                }

                //???????????????????????????
                JSONArray headerCols = JSONArray.parseArray(formBoList.getColsJson());
                boolean isNeedAd = (headerCols == null || headerCols.size() == 0) ? true : false;
                JSONArray fieldCols = new JSONArray();
                if (headerCols == null) {
                    headerCols = new JSONArray();
                }
                getInterfaceColumns(mv, formBoList, headerCols, fieldCols,isNeedAd, excelConf,isNeedAdExcelConf);
                String headerColumns = headerCols.toJSONString();
                String fieldColumns = fieldCols.toJSONString();
                if (StringUtils.isEmpty(formBoList.getColsJson())) {
                    formBoList.setColsJson(headerColumns);
                    formBoList.setFieldsJson(fieldColumns);
                    formBoListServiceImpl.update(formBoList);
                }
            } else {
                DataSourceContextHolder.setDataSource(formBoList.getDbAs());

                //excel??????????????????
                JSONObject excelConf = JSONObject.parseObject(formBoList.getExcelConfJson());

                boolean isNeedAdExcelConf = (excelConf == null || excelConf.size() == 0
                        || excelConf.getJSONArray("headerColumnsExcel") == null
                        || excelConf.getJSONArray("headerColumnsExcel").size() == 0) ? true : false;
                if (excelConf == null) {
                    excelConf = new JSONObject();
                    excelConf.put("javaCode", "");
                    excelConf.put("importDataHandler", "");
                    excelConf.put("importDataHandlerName", "");
                    excelConf.put("headerColumnsExcel", new JSONArray());
                }

                //???????????????????????????
                JSONArray headerCols = JSONArray.parseArray(formBoList.getColsJson());

                List<GridHeader> headers = DbUtil.getGridHeader(getHeaderSql(request, formBoList));
                JSONArray fieldCols = new JSONArray();
                boolean isNeedAd = (headerCols == null || headerCols.size() == 0) ? true : false;

                if (headerCols == null) {
                    headerCols = new JSONArray();
                }
                getColumns(mv, formBoList, headers, fieldCols, headerCols, isNeedAd, excelConf, isNeedAdExcelConf);

                String headerColumns = headerCols.toJSONString();
                String fieldColumns = fieldCols.toJSONString();

                if (StringUtils.isEmpty(formBoList.getColsJson()) || isNeedAdExcelConf) {
                    DataSourceContextHolder.setDefaultDataSource();
                    formBoList.setColsJson(headerColumns);
                    formBoList.setFieldsJson(fieldColumns);
                    formBoList.setExcelConfJson(excelConf.toJSONString());
                    formBoListServiceImpl.update(formBoList);
                }
            }
            if(solution!=null) {
                mv.put("formId", solution.getFormId());
                mv.put("formName", solution.getFormName());
                mv.put("boDefId", solution.getBodefId());
            }
            mv.put("editSearchViewJson",formBoList.getEditSearchViewJson());

        } catch (Exception e) {
            errorMsg = e.getMessage();
            mv.put("errorMsg", errorMsg);
        } finally {
            DataSourceContextHolder.setDefaultDataSource();
        }
        return mv;
    }

    private JSONArray parseInterfaceMappingJson(JSONArray paramsAry,JSONArray paramsMapping,String parentId){
        JSONArray paramsTemp=new JSONArray();
        for(Object obj:paramsAry) {
            JSONObject param = (JSONObject) obj;
            JSONObject val = new JSONObject();
            String idx=IdGenerator.getIdStr();
            val.put("idx_", idx);
            val.put("parentId",parentId);
            String key=param.getString("paramName");
            val.put("key",key);
            String desc=param.getString("paramDesc");
            if(StringUtils.isEmpty(desc)){
                desc=key;
            }
            val.put("description",desc);
            if("object".equals(param.getString("paramType")) || "array".equals(param.getString("paramType"))){
                if (paramsMapping != null && !paramsMapping.isEmpty()) {
                    for (Object json : paramsMapping) {
                        JSONObject head = (JSONObject) json;
                        if (key.equals(head.getString("key"))) {
                            val.put("children",parseInterfaceMappingJson(param.getJSONArray("children"),head.getJSONArray("children"),idx));
                            paramsTemp.add(val);
                        }
                    }
                }else {
                    val.put("children", parseInterfaceMappingJson(param.getJSONArray("children"),null,idx));
                    paramsTemp.add(val);
                }
            }else{
                if("param".equals(param.getString("paramSource"))){
                    if (paramsMapping != null && !paramsMapping.isEmpty()) {
                        for (Object json : paramsMapping) {
                            JSONObject head = (JSONObject) json;
                            if (key.equals(head.getString("key"))) {
                                val = head;
                            }
                        }
                    }
                    paramsTemp.add(val);
                }
            }
        }
        return paramsTemp;
    }

    private JSONArray parseInterfaceMapping(String params,JSONObject mapping,String field,boolean isJson){
        JSONArray paramsTemp=new JSONArray();
        if(StringUtils.isEmpty(params)){
            return paramsTemp;
        }
        JSONArray paramsMapping = null;
        if (mapping != null) {
            paramsMapping = mapping.getJSONArray(field);
        }
        JSONArray paramsAry=JSONArray.parseArray(params);
        if(isJson){
            paramsTemp=parseInterfaceMappingJson(paramsAry,paramsMapping,"0");
        }else{
            for(Object obj:paramsAry) {
                JSONObject param = (JSONObject) obj;
                if("param".equals(param.getString("paramSource"))){
                    JSONObject val = new JSONObject();
                    String key=param.getString("paramName");
                    val.put("key", key);
                    String desc=param.getString("paramDesc");
                    if(StringUtils.isEmpty(desc)){
                        desc=key;
                    }
                    val.put("description",desc);
                    if (paramsMapping != null && !paramsMapping.isEmpty()) {
                        for (Object json : paramsMapping) {
                            JSONObject head = (JSONObject) json;
                            if (key.equals(head.getString("key"))) {
                                val = head;
                            }
                        }
                    }
                    paramsTemp.add(val);
                }
            }
        }
        return paramsTemp;
    }

    private JSONArray parseReturnColumns(JSONArray returnFileds){
        JSONArray returnColumns=new JSONArray();
        JSONArray root=returnFileds.getJSONObject(0).getJSONArray("children");
        for(Object child:root){
            JSONObject childJson=(JSONObject)child;
            if("result".equals(childJson.getString("paramName"))){
                returnColumns=childJson.getJSONArray("children").getJSONObject(0).getJSONArray("children");
            }
        }
        return returnColumns;
    }

    private void getInterfaceColumns(JSONObject mv, FormBoList formBoList, JSONArray headerCols, JSONArray fieldCols, boolean isNeedAd, JSONObject excelConf, boolean isNeedAdExcelConf) {
        JSONArray headerColumnsExcel = new JSONArray();
        if (excelConf.containsKey("headerColumnsExcel") && StringUtils.isNotEmpty(excelConf.getString("headerColumnsExcel"))) {
            headerColumnsExcel = excelConf.getJSONArray("headerColumnsExcel");
        }
        if(StringUtils.isNotEmpty(formBoList.getInterfaceKey())){
            SysInterfaceApiDto sysInterfaceApiDto=sysInterfaceApiClient.getById(formBoList.getInterfaceKey());
            JSONObject interfaceMapping = JSONObject.parseObject(formBoList.getInterfaceMappingJson());
            /**********************??????????????????************************/
            JSONArray pathParams=parseInterfaceMapping(sysInterfaceApiDto.getApiPathParams(),interfaceMapping,"pathParamsMapping",false);
            JSONArray headers=parseInterfaceMapping(sysInterfaceApiDto.getApiHeaders(),interfaceMapping,"headersMapping",false);
            JSONArray query=parseInterfaceMapping(sysInterfaceApiDto.getApiQuery(),interfaceMapping,"queryMapping",false);
            JSONArray body=new JSONArray();
            if("form".equals(sysInterfaceApiDto.getApiDataType()) || "json".equals(sysInterfaceApiDto.getApiDataType())) {
                body=parseInterfaceMapping(sysInterfaceApiDto.getApiBody(),interfaceMapping,"bodyMapping","json".equals(sysInterfaceApiDto.getApiDataType()));
            }
            mv.put("pathParamsMapping",pathParams);
            mv.put("headersMapping",headers);
            mv.put("queryMapping",query);
            mv.put("bodyMapping",body);
            /**********************??????????????????************************/
            Map<String, JSONObject> jsonFieldMap = getFieldJsonMap(formBoList.getFieldsJson());
            JSONArray returnFileds=JSONArray.parseArray(sysInterfaceApiDto.getApiReturnFields());
            JSONArray returnColumns=parseReturnColumns(returnFileds);
            for (Object obj : returnColumns) {
                JSONObject gh=(JSONObject)obj;
                JSONObject fieldObj = new JSONObject();
                String desc=gh.getString("paramDesc");
                if(StringUtils.isEmpty(desc)){
                    desc=gh.getString("paramName");
                }
                //???????????????????????????????????????????????????????????????
                if ("YES".equals(formBoList.getIsDialog())) {
                    JSONObject fieldJson = jsonFieldMap.get(gh.getString("paramName"));
                    if (fieldJson == null) {
                        fieldObj.put("header", desc);
                        fieldObj.put("isReturn", false);
                        fieldObj.put("visible", false);
                    } else {
                        fieldObj.put("header", fieldJson.getString("header"));
                        fieldObj.put("isReturn", fieldJson.getBoolean("isReturn"));
                        fieldObj.put("visible", fieldJson.getBoolean("visible"));
                    }
                } else {
                    fieldObj.put("header", desc);
                }
                fieldObj.put("field", gh.getString("paramName"));
                fieldObj.put("allowSort", false);

                //????????????????????????????????????
                JSONObject dataType = JSONObject.parseObject("{'string':'??????','number':'??????','date':'?????????'}");
                if (gh.getString("paramName").equals(FormBoEntity.FIELD_CREATE_TIME) || gh.getString("paramName").equals(FormBoEntity.FIELD_UPDATE_TIME)) {
                    fieldObj.put("dataType", "date");
                    fieldObj.put("format", CommonConstant.DATE_FORMAT);
                    fieldObj.put("dataType_name", dataType.getString("date"));
                } else {
                    fieldObj.put("dataType", "number".equals(gh.getString("paramType")) ? "int" : gh.getString("paramType"));
                    fieldObj.put("dataType_name", dataType.getString(gh.getString("paramType")));
                }
                setDefaultFieldObj(fieldObj,formBoList);
                fieldCols.add(fieldObj);
                for (int i = 0; i < headerCols.size(); i++) {
                    JSONObject headerObj=headerCols.getJSONObject(i);
                    String field = headerObj.getString("field");
                    if (field == null) {
                        continue;
                    }
                    if (field.equals(gh.getString("paramName"))) {
                        headerObj.put("headerAlign",fieldObj.getString("headerAlign"));
                        headerObj.put("headerAlign_name",fieldObj.getString("headerAlign_name"));
                        break;
                    }
                }
                if (isNeedAd) {
                    JSONObject cloneObj = (JSONObject) fieldObj.clone();
                    cloneObj.remove("isReturn");
                    cloneObj.remove("visible");
                    headerCols.add(cloneObj);
                }

                if (isNeedAdExcelConf) {
                    JSONObject cloneObj = (JSONObject) fieldObj.clone();
                    cloneObj.remove("isReturn");
                    cloneObj.remove("visible");
                    headerColumnsExcel.add(cloneObj);
                }
            }

            excelConf.put("headerColumnsExcel", headerColumnsExcel);
        }
        mv.put("headerColumns", headerCols);
        mv.put("fieldColumns", fieldCols);
        mv.put("isAllowDataGrant", false);
        mv.put("returnSet", new JSONArray());
        mv.put("javaCode", excelConf.getString("javaCode"));
        mv.put("importDataHandler", excelConf.getString("importDataHandler"));
        mv.put("importDataHandlerName", excelConf.getString("importDataHandlerName"));
        mv.put("headerColumnsExcel", excelConf.getString("headerColumnsExcel"));
    }

    private void getWebReqColumns(JSONObject mv, FormBoList formBoList, JSONArray headerCols, JSONArray fieldCols, JSONObject excelConf) {
        if (StringUtils.isNotEmpty(formBoList.getWebreqKey())) {
            SysWebReqDefDto webReqDef = sysWebReqDefClient.getByAlias(formBoList.getWebreqKey());
            JSONArray paramsSet = JSONArray.parseArray(webReqDef.getParamsSet());
            Map<String, String> headerMap = new HashMap<>(paramsSet.size());
            for (int i = 0; i < paramsSet.size(); i++) {
                JSONObject head = paramsSet.getJSONObject(i);
                String headKey = head.getString("key");
                headerMap.put(headKey, null);
            }
            JSONArray data = JSONArray.parseArray(webReqDef.getData());
            Map<String, String> params = new HashMap<>(data.size());
            for (int i = 0; i < data.size(); i++) {
                JSONObject param = data.getJSONObject(i);
                String paramKey = param.getString("key");
                params.put(paramKey, null);
            }
            JSONObject webMapiing = JSONObject.parseObject(formBoList.getWebreqMappingJson());
            JSONArray headerMapping = null;
            if (webMapiing != null) {
                headerMapping = webMapiing.getJSONArray("headerMapping");
            }
            JSONArray headerTemp = new JSONArray();
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                String mapKey = entry.getKey();
                JSONObject val = new JSONObject();
                val.put("key", mapKey);
                if (headerMapping != null) {
                    for (Object json : headerMapping) {
                        JSONObject head = (JSONObject) json;
                        if (mapKey.equals(head.getString("key"))) {
                            val = head;
                        }
                    }
                }
                headerTemp.add(val);
            }
            JSONArray paramsMapping = null;
            if (webMapiing != null) {
                paramsMapping = webMapiing.getJSONArray("paramsMapping");
            }
            JSONArray paramsTemp = new JSONArray();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String mapKey = entry.getKey();
                JSONObject val = new JSONObject();
                val.put("key", mapKey);
                if (paramsMapping != null) {
                    for (Object json : paramsMapping) {
                        JSONObject param = (JSONObject) json;
                        if (mapKey.equals(param.getString("key"))) {
                            val = param;
                        }
                    }
                }
                paramsTemp.add(val);
            }
            String bodyMapping = webReqDef.getTemp();
            if (webMapiing != null) {
                bodyMapping = webMapiing.getString("bodyMapping");
            }

            mv.put("headerMapping", headerTemp);
            mv.put("paramsMapping", paramsTemp);
            mv.put("bodyMapping", bodyMapping);
            mv.put("webreqDataType", webReqDef.getDataType());
            mv.put("javaCode", excelConf.getString("javaCode"));
            mv.put("importDataHandler", excelConf.getString("importDataHandler"));
            mv.put("importDataHandlerName", excelConf.getString("importDataHandlerName"));
            mv.put("headerColumnsExcel", excelConf.getString("headerColumnsExcel"));

        }
        mv.put("headerColumns", headerCols);
        mv.put("fieldColumns", fieldCols);
        mv.put("isAllowDataGrant", false);
        mv.put("returnSet", new JSONArray());
        mv.put("javaCode", excelConf.getString("javaCode"));
        mv.put("importDataHandler", excelConf.getString("importDataHandler"));
        mv.put("importDataHandlerName", excelConf.getString("importDataHandlerName"));
        mv.put("headerColumnsExcel", excelConf.getString("headerColumnsExcel"));
    }

    private void getColumns(JSONObject mv, FormBoList formBoList, List<GridHeader> headers, JSONArray fieldCols, JSONArray headerCols,
                            boolean isNeedAd, JSONObject excelConf, boolean isNeedAdExcelConf) {
        JSONArray headerColumnsExcel = new JSONArray();
        if (excelConf.containsKey("headerColumnsExcel") && StringUtils.isNotEmpty(excelConf.getString("headerColumnsExcel"))) {
            headerColumnsExcel = excelConf.getJSONArray("headerColumnsExcel");
        }


        //???????????????????????????????????????
        Boolean isAllowDataGrant = false;
        //????????????????????????????????????
        Map<String, JSONObject> jsonFieldMap = getFieldJsonMap(formBoList.getFieldsJson());
        for (GridHeader gh : headers) {
            JSONObject fieldObj = new JSONObject();

            //????????????????????????????????????
            if (!isAllowDataGrant && (FormBoEntity.FIELD_CREATE_BY.equalsIgnoreCase(gh.getFieldName()))) {
                isAllowDataGrant = true;
            }
            //???????????????????????????????????????????????????????????????
            if ("YES".equals(formBoList.getIsDialog())) {
                JSONObject fieldJson = jsonFieldMap.get(gh.getFieldName());
                if (fieldJson == null) {
                    fieldObj.put("header", gh.getFieldLabel());
                    fieldObj.put("isReturn", false);
                    fieldObj.put("visible", false);
                } else {
                    fieldObj.put("header", fieldJson.getString("header"));
                    fieldObj.put("isReturn", fieldJson.getBoolean("isReturn"));
                    fieldObj.put("visible", fieldJson.getBoolean("visible"));
                }
            } else {
                fieldObj.put("header", gh.getFieldLabel());
            }
            fieldObj.put("field", gh.getFieldName());
            fieldObj.put("allowSort", false);

            //????????????????????????????????????
            JSONObject dataType = JSONObject.parseObject("{'string':'??????','number':'??????','date':'?????????'}");
            if (gh.getFieldName().equals(FormBoEntity.FIELD_CREATE_TIME) || gh.getFieldName().equals(FormBoEntity.FIELD_UPDATE_TIME)) {
                fieldObj.put("dataType", "date");
                fieldObj.put("format", CommonConstant.DATE_FORMAT);
                fieldObj.put("dataType_name", dataType.getString("date"));
            } else {
                fieldObj.put("dataType", "number".equals(gh.getDataType()) ? "int" : gh.getDataType());
                fieldObj.put("dataType_name", dataType.getString(gh.getDataType()));
            }
            fieldObj.put("queryDataType", gh.getQueryDataType());
            setDefaultFieldObj(fieldObj,formBoList);
            fieldCols.add(fieldObj);
            for (int i = 0; i < headerCols.size(); i++) {
                JSONObject headerObj=headerCols.getJSONObject(i);
                String field = headerObj.getString("field");
                if (field == null) {
                    continue;
                }
                if (field.equals(gh.getFieldName())) {
                    headerObj.put("headerAlign",fieldObj.getString("headerAlign"));
                    headerObj.put("headerAlign_name",fieldObj.getString("headerAlign_name"));
                    break;
                }
            }
            if (isNeedAd) {
                JSONObject cloneObj = (JSONObject) fieldObj.clone();
                cloneObj.remove("isReturn");
                cloneObj.remove("visible");
                headerCols.add(cloneObj);
            }

            if (isNeedAdExcelConf) {
                JSONObject cloneObj = (JSONObject) fieldObj.clone();
                cloneObj.remove("isReturn");
                cloneObj.remove("visible");
                headerColumnsExcel.add(cloneObj);
            }
        }

        excelConf.put("headerColumnsExcel", headerColumnsExcel);

        mv.put("headerColumns", headerCols);
        mv.put("fieldColumns", fieldCols);
        mv.put("isAllowDataGrant", isAllowDataGrant);
        mv.put("javaCode", excelConf.getString("javaCode"));
        mv.put("importDataHandler", excelConf.getString("importDataHandler"));
        mv.put("importDataHandlerName", excelConf.getString("importDataHandlerName"));
        mv.put("headerColumnsExcel", headerColumnsExcel);
    }

    private String getHeaderSql(HttpServletRequest request, FormBoList formBoList) throws Exception {
        Map<String, Object> params = getParams(request);
        String newSql = formBoListServiceImpl.parseFreemarkSql(formBoList.getUseCondSql(),
                MBoolean.YES.val.equals(formBoList.getUseCondSql()) ? formBoList.getCondSqls() : formBoList.getSql(), params);
        newSql = DbUtil.preHandleSql(newSql);
        return newSql;
    }

    private String getCustomHeaderSql(Map<String, Object> params, FormBoList formBoList) throws Exception {
        //??????????????????Context??????
        addContextVar(params);

        String newSql = formBoListServiceImpl.parseFreemarkSql(formBoList.getUseCondSql(),
                MBoolean.YES.val.equals(formBoList.getUseCondSql()) ? formBoList.getCondSqls() : formBoList.getSql(), params);
        return newSql;
    }

    private Map<String, Object> getParams(HttpServletRequest request) {
        Map<String, Object> params = getParameterValueMap(request, false);
        IUser curUser = ContextUtil.getCurrentUser();
        //??????????????????Context??????
        if (curUser != null) {
            params.put(FormBoEntity.FIELD_CREATE_BY, curUser.getUserId());
            params.put(FormBoEntity.FIELD_TENANT, curUser.getTenantId());
            params.put("DEP_ID_", curUser.getDeptId());
        }
        return params;
    }

    /**
     * ????????????????????????
     *
     * @param request
     * @param remainArray
     * @return
     */
    public static Map<String, Object> getParameterValueMap(HttpServletRequest request, boolean remainArray) {
        Map<String, Object> map = new HashMap<>();
        Enumeration params = request.getParameterNames();
        while (params.hasMoreElements()) {
            String key = params.nextElement().toString();
            String[] values = request.getParameterValues(key);
            if (values == null) {
                continue;
            }
            if (values.length == 1) {
                String tmpValue = values[0];
                if (tmpValue == null) {
                    continue;
                }
                tmpValue = tmpValue.trim();
                if (tmpValue.equals("")) {
                    continue;
                }
                map.put(key, tmpValue);
            } else {
                String rtn = getByAry(values);
                if (rtn.length() > 0) {
                    if (remainArray) {
                        map.put(key, rtn.split(","));
                    } else {
                        map.put(key, rtn);
                    }
                }
            }
        }
        return map;
    }

    private static String getByAry(String[] aryTmp) {
        String rtn = "";

        for (int i = 0; i < aryTmp.length; ++i) {
            String str = aryTmp[i].trim();
            if (!str.equals("")) {
                rtn = rtn + str + ",";
            }
        }

        if (rtn.length() > 0) {
            rtn = rtn.substring(0, rtn.length() - 1);
        }

        return rtn;
    }

    private Map<String, JSONObject> getFieldJsonMap(String fieldJson) {
        if (StringUtils.isEmpty(fieldJson)) {
            return new HashMap<>(16);
        }
        JSONArray jsonArray = JSONArray.parseArray(fieldJson);
        Map<String, JSONObject> map = new HashMap<>(jsonArray.size());
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObj = jsonArray.getJSONObject(i);
            String field = jsonObj.getString("field");
            map.put(field, jsonObj);
        }
        return map;
    }

    /**
     * ????????????
     *
     * @param request
     * @return
     * @throws Exception
     */
    @MethodDefine(title = "???????????????????????????", path = "/genTreeHtml", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "????????????", varName = "request")})
    @ApiOperation(value = "???????????????????????????")
    @GetMapping("/genTreeHtml")
    public JsonResult genTreeHtml(HttpServletRequest request) throws Exception {
        String id = RequestUtil.getString(request, "id");
        String remark = request.getParameter("remark");
        FormBoList formBoList = formBoListServiceImpl.get(id);

        Map<String, Object> model = new HashMap<>(16);
        model.put("ctxPath", request.getContextPath());

        String listHtml = formBoListServiceImpl.genTreeDlgHtmlPage(formBoList, model,"/list/treeDlgTemplate.ftl");
        formBoList.setListHtml(listHtml);
        String mobileHtml = formBoListServiceImpl.genTreeDlgHtmlPage(formBoList,model,"/mobile/treeDlgTemplate.ftl");
        formBoList.setMobileHtml(mobileHtml);
        formBoList.setIsGen(MBoolean.YES.name());
        formBoListServiceImpl.update(formBoList);
        listHistoryService.addList(formBoList,remark);
        return new JsonResult(true, "????????????Html");
    }

    /**
     * ????????????
     *
     * @param request
     * @return
     * @throws Exception
     */
    @MethodDefine(title = "????????????", path = "/genHtml", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "????????????", varName = "request")})
    @ApiOperation(value = "????????????")
    @GetMapping("/genHtml")
    public JsonResult genHtml(HttpServletRequest request) throws Exception {
        String id = request.getParameter("id");
        String remark = request.getParameter("remark");
        String ctxPath = request.getContextPath();
        if (id.contains(",")) {
            String[] split = id.split(",");
            for (String s : split) {
                formBoListServiceImpl.genHtml(s, ctxPath,remark);
            }
        } else {
            formBoListServiceImpl.genHtml(id, ctxPath,remark);
        }
        return new JsonResult(true, "????????????Html");
    }

    @MethodDefine(title = "????????????HTML", path = "/saveHtml", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "????????????", varName = "json")})
    @ApiOperation(value = "????????????HTML")
    @PostMapping("/saveHtml")
    public JsonResult saveHtml(@RequestBody JSONObject json) throws Exception {
        String id = json.getString("id");
        String html = json.getString("html");
        String mobileHtml = json.getString("mobileHtml");
        String remark = json.getString("remark");
        String isLog = json.getString("isLog");

        FormBoList formBoList = formBoListServiceImpl.get(id);

        //????????????
        formBoListServiceImpl.removeCache(formBoList.getKey());

        formBoList.setListHtml(html);
        formBoList.setMobileHtml(mobileHtml);
        formBoListServiceImpl.update(formBoList);
        if(!"NO".equals(isLog)) {
            listHistoryService.addList(formBoList, remark);
        }
        return new JsonResult(true, "????????????HTML");
    }

    private void addContextVar(Map<String, Object> params) {
        IUser curUser = ContextUtil.getCurrentUser();
        if (curUser != null) {
            params.put(FormBoEntity.FIELD_CREATE_BY, curUser.getUserId());
            params.put("DEP_ID_", curUser.getDeptId());
            params.put(FormBoEntity.FIELD_TENANT, curUser.getTenantId());
        }
    }

    /**
     * ??????????????????
     *
     * @param boKey
     * @param request
     * @return
     * @throws Exception
     */
    @MethodDefine(title = "??????????????????", path = "/*/getTreeJson", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "??????KEY", varName = "boKey"), @ParamDefine(title = "????????????", varName = "params"), @ParamDefine(title = "????????????", varName = "request")})
    @ApiOperation(value = "??????????????????")
    @PostMapping("{boKey}/getTreeJson")
    public JsonPageResult getTreeJson(@PathVariable("boKey") String boKey, @RequestBody(required = false) Map<String, String> params, HttpServletRequest request) throws Exception {
        FormBoList formBoList = formBoListServiceImpl.getByKey(boKey);
        if (formBoList == null) {
            return JsonPageResult.getFail("?????????Key?????????iao");
        }
        JsonPageResult result = JsonPageResult.getSuccess("?????????");

        try {
            DataSourceContextHolder.setDataSource(formBoList.getDbAs());
            Map<String, Object> requestParams = RequestUtil.getParameterValueMap(request, false);
            QueryFilter queryFilter = QueryFilterBuilder.createQueryFilter(request);
            if (params != null) {
                Iterator<Map.Entry<String, String>> paramsIt = params.entrySet().iterator();
                while (paramsIt.hasNext()) {
                    Map.Entry<String, String> keyVals = paramsIt.next();
                    if (BeanUtil.isEmpty(keyVals.getValue())) {
                        continue;
                    }
                    if (!keyVals.getKey().startsWith("Q_")) {
                        queryFilter.addParam(keyVals.getKey(), keyVals.getValue());
                    } else {
                        QueryParam queryParam = QueryFilter.getQueryParams(keyVals.getKey(), keyVals.getValue());
                        queryFilter.addQueryParam(queryParam);
                    }
                }
            }
            requestParams.putAll(queryFilter.getParams());
            String sql = getCustomHeaderSql(requestParams, formBoList);
            if (MBoolean.YES.val.equals(formBoList.getIsPage())) {

                Page list = formBoListServiceImpl.getPageDataBySql(sql, queryFilter, formBoList.getIsTest());
                IPage page = queryFilter.getPage();
                page.setRecords(list).setTotal(list.getTotal());
                result.setPageData(queryFilter.getPage());
            } else {
                if ("YES".equals(formBoList.getIsLazy())) {
                    if (params == null) {
                        params = new HashMap();
                    }
                    String[] parentIds = params.getOrDefault(formBoList.getIdField(), "0").split(",");
                    String parentId = parentIds[parentIds.length - 1];
                    int inWhere = sql.lastIndexOf("WHERE");
                    int orderBy = sql.lastIndexOf("ORDER BY");
                    String order = "";
                    if (orderBy != -1) {
                        order = sql.substring(orderBy);
                        sql = sql.substring(0, orderBy);
                    }
                    String parentField = formBoList.getParentField();
                    if (BeanUtil.isNotEmpty(formBoList.getParentField())) {
                        parentField = formBoList.getParentField();
                    }
                    String idField = formBoList.getIdField();
                    if (BeanUtil.isNotEmpty(params.get(idField))) {
                        parentId = params.get(idField);
                    }
                    if (inWhere != -1) {//???????????????
                        if (!"0".equals(parentId) && null != parentId) {
                            sql += " and " + parentField + "='" + parentId + "' ";
                            queryFilter.getParams().clear();
                            queryFilter.getFieldLogic().getWhereParams().clear();
                        } else {
                            sql += " and (" + parentField + "='0'" + " or " +
                                    parentField + " is null )";
                        }
                    } else {
                        if ("0".equals(parentId)) {
                            sql += " where " + parentField + "='" + parentId + "' or " +
                                    parentField + " is null ";
                        } else {
                            sql += " where " + parentField + "='" + parentId + "' ";
                            queryFilter.getParams().clear();
                            queryFilter.getFieldLogic().getWhereParams().clear();
                        }
                    }
                    sql += " " + order;
                }
                List list = formBoListServiceImpl.getDataBySql(sql, queryFilter, formBoList.getIsTest());
                //List tempList=BeanUtil.deepCopyBean(list);
                List tempList = deepCopy(list);

                if ("YES".equals(formBoList.getIsLazy())) {
                    for (int i=0;i<list.size();i++) {
                        HashMap map = (HashMap<String, Object>) tempList.get(i);
                        if(map==null){
                            tempList.remove(i);
                            continue;
                        }
                        map.put("isLeaf", false);
                        map.put("expanded", false);
                    }
                }
                IPage page = queryFilter.getPage();
                page.setRecords(tempList).setTotal(list.size());
                result.setPageData(queryFilter.getPage());
            }
        } finally {
            DataSourceContextHolder.setDefaultDataSource();
        }
        return result;
    }

    public static <T> List<T> deepCopy(List<T> src) throws IOException, ClassNotFoundException {

        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteOut);
        out.writeObject(src);

        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
        ObjectInputStream in = new ObjectInputStream(byteIn);
        @SuppressWarnings("unchecked")
        List<T> dest = (List<T>) in.readObject();
        return dest;
    }

    /**
     * ??????queryFilter
     *
     * @param queryFilter
     * @param params
     */
    private void handQueryFilter(QueryFilter queryFilter, Map<String, Object> params) {
        if (BeanUtil.isEmpty(params)) {
            return;
        }

        for (Map.Entry<String, Object> ent : params.entrySet()) {
            String mapKey = ent.getKey();
            Object mapValue = ent.getValue();
            if (mapKey.startsWith("Q_")) {
                queryFilter.addQueryParam(mapKey, mapValue.toString());
            } else {
                queryFilter.addParam(mapKey, mapValue);
            }
        }
    }


    @MethodDefine(title = "????????????", path = "/*/getData", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "??????KEY", varName = "boKey"), @ParamDefine(title = "????????????", varName = "queryData"), @ParamDefine(title = "????????????", varName = "request")})
    @ApiOperation(value = "????????????")
    @PostMapping("{boKey}/getData")
    public JsonPageResult getData(@PathVariable("boKey") String boKey, @RequestBody QueryData queryData, HttpServletRequest request) throws Exception {
        FormBoList formBoList = formBoListServiceImpl.getByKey(boKey);
        if (formBoList == null) {
            return JsonPageResult.getFail("?????????Key?????????");
        }
        JsonPageResult result = JsonPageResult.getSuccess("?????????");
        QueryFilter queryFilter = QueryFilterBuilder.createQueryFilter(queryData);


        try {
            String isMobile = request.getParameter("isMobile");
            Map<String, Object> params = RequestUtil.getParameterValueMap(request, false);
            Map<String, String> queryParams = queryData.getParams();
            if(queryParams.containsKey("queryCondition")){
                //????????????
                formBoListServiceImpl.parseQueryCondition(queryFilter,queryParams.get("queryCondition"));
            }
            //??????queryFilter
            handQueryFilter(queryFilter, params);
            //????????????
            params.putAll(queryFilter.getParams());
            //??????????????????Context??????
            addContextVar(params);
            String tree = "tree";
            //????????????
            if (WEBREQ.equals(formBoList.getUseCondSql())) {
                params.put("pageSize", queryData.getPageSize().toString());
                params.put("pageNo", queryData.getPageNo().toString());
                params.put("sortField",queryData.getSortField());
                return formBoListServiceImpl.executeWebReq(formBoList, params);
            }else if(INTERFACE.equals(formBoList.getUseCondSql())){
                params.put("pageSize", queryData.getPageSize().toString());
                params.put("pageNo", queryData.getPageNo().toString());
                params.put("sortField",queryData.getSortField());
                return formBoListServiceImpl.executeInterface(formBoList,params);
            }

            String sql = formBoListServiceImpl.getValidSql(formBoList, params);
            String sql2 = "";
            String dsAlias= getDsAlias(formBoList);
            DataSourceContextHolder.setDataSource(dsAlias);
            //????????????????????????
            if (MBoolean.YES.val.equals(formBoList.getIsPage()) && !tree.equals(formBoList.getDataStyle())) {

                Page list = formBoListServiceImpl.getPageDataBySql(sql, queryFilter, formBoList.getIsTest());
                formBoListServiceImpl.gdRenderColumn(isMobile, formBoList, list,null);
                IPage page = queryFilter.getPage();
                page.setRecords(list).setTotal(list.getTotal());
                result.setPageData(queryFilter.getPage());
            } else {
                List list = new ArrayList();
                //??????????????????,???????????????????????????
                if (tree.equals(formBoList.getDataStyle())) {
                    if (queryParams == null) {
                        params = new HashMap();
                    }
                    String[] parentIds = queryParams.getOrDefault(formBoList.getIdField(), "0").split(",");
                    String parentId = parentIds[parentIds.length - 1];

                    if ("YES".equals(formBoList.getIsLazy())) {// ?????????
                        int inWhere = sql.lastIndexOf("WHERE");
                        int orderBy = sql.lastIndexOf("ORDER BY");
                        String order = "";
                        if (orderBy != -1) {
                            order = sql.substring(orderBy);
                            sql = sql.substring(0, orderBy);
                        }

                        sql = "select * from(" + sql + ") tmp";
                        sql2 = sql;

                        if (!"0".equals(parentId)) {
                            // sql=sql.substring(0, inWhere);
                            sql += " where " + formBoList.getParentField() + "='" + parentId + "' ";
                            queryFilter.getParams().clear();
                            queryFilter.getFieldLogic().getWhereParams().clear();
                        } else if ("0".equals(parentId)) {
                            if (queryFilter.getFieldLogic().getWhereParams().size() == 0) {
                                sql += " where " + formBoList.getParentField() + "='" + parentId + "' or "
                                        + formBoList.getParentField() + " is null ";

                            }
                        }

                        sql += " " + order;
                        sql2 += " " + order;
                    }
                    list = formBoListServiceImpl.getDataBySql(sql, queryFilter, formBoList.getIsTest());
                    if (list.size() == 0 & "0".equals(parentId)) {
                        list = formBoListServiceImpl.getDataBySql(sql2, queryFilter, formBoList.getIsTest());
                    }
                    if ("YES".equals(formBoList.getIsLazy())) {
                        for (Object obj : list) {
                            HashMap map = (HashMap<String, Object>) obj;
                            map.put("children", new JSONArray());
                        }
                    }
                    IPage page = queryFilter.getPage();
                    page.setRecords(list).setTotal(list.size());
                    result.setPageData(queryFilter.getPage());
                } else {
                    list = formBoListServiceImpl.getDataBySql(sql, queryFilter, formBoList.getIsTest());
                    IPage page = queryFilter.getPage();
                    page.setRecords(list).setTotal(list.size());
                    result.setPageData(queryFilter.getPage());
                }
                formBoListServiceImpl.gdRenderColumn(isMobile, formBoList, list,null);
            }

        } catch (Exception e) {
            logger.error("--FormBoListController.getData is error :--" + e.getMessage());
            DataSourceContextHolder.setDefaultDataSource();
            if (MBoolean.YES.name().equals(formBoList.getIsTest())) {
                FormSqlLog formSqlLog = new FormSqlLog();
                formSqlLog.setType(FormSqlLog.TYPE_FORM_BO_LIST);
                formSqlLog.setSql((String) queryFilter.getParams().get("sql"));
                queryFilter.getParams().remove("sql");
                formSqlLog.setParams(JSONObject.toJSONString(queryFilter.getParams()));
                formSqlLog.setIsSuccess(MBoolean.NO.name());
                formSqlLog.setRemark("--FormBoListController.getData is error :--" + e.getMessage());
                formSqlLogService.insert(formSqlLog);
            }
            result.setPageData(queryFilter.getPage());
        } finally {
            DataSourceContextHolder.setDefaultDataSource();
        }

        return result;
    }

    /**
     * @return
     * @throws Exception
     */
    @MethodDefine(title = "?????????????????????SQL", path = "/onRun", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "????????????", varName = "json")})
    @ApiOperation(value = "?????????????????????SQL")
    @PostMapping("/onRun")
    public JsonResult onRun(@RequestBody JSONObject json,HttpServletRequest request) throws Exception {
        String ds = json.getString("ds");
        String sql = json.getString("sql");
        JSONObject result = new JSONObject();
        try {
            DataSourceContextHolder.setDataSource(ds);

            String newSql = getHeaderSql(request, sql);
            List<GridHeader> headers = DbUtil.getGridHeader(newSql);

            JSONArray fieldCols = new JSONArray();
            for (GridHeader gh : headers) {
                JSONObject fieldObj = new JSONObject();
                fieldObj.put("field", gh.getFieldName());
                fieldObj.put("header", gh.getFieldLabel());
                fieldObj.put("width", 100);
                fieldObj.put("isReturn", true);
                fieldCols.add(fieldObj);
            }
            result.put("headers", headers);
            result.put("fields", fieldCols);
        } catch (Exception e) {
            return new JsonResult(false, "??????SQL?????????" + e.getMessage());
        } finally {
            DataSourceContextHolder.setDefaultDataSource();
        }
        return new JsonResult(true, result, "????????????SQL").setShow(false);
    }

    private String getHeaderSql(HttpServletRequest request, String sql) throws Exception {
        Map<String, Object> params = getParams(request);
        String useCondSql = request.getParameter("useCondSql");
        String newSql = formBoListServiceImpl.parseFreemarkSql(useCondSql, sql, params);
        return newSql;
    }

    @MethodDefine(title = "????????????", path = "/*/config", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "??????KEY", varName = "boKey")})
    @ApiOperation(value = "????????????")
    @GetMapping("{boKey}/config")
    public JsonResult config(@PathVariable("boKey") String boKey) throws Exception {
        JsonResult result = JsonResult.Success();
        if (BeanUtil.isEmpty(boKey)) {
            return result.setSuccess(false).setMessage("?????????????????????????????????");
        }
        result.setShow(false);
        FormBoList formBoList = formBoListServiceImpl.getByKey(boKey);
        FormBoList rtn = new FormBoList();
        rtn.setId(formBoList.getId());
        rtn.setKey(formBoList.getKey());
        rtn.setName(formBoList.getName());
        rtn.setWidth(formBoList.getWidth());
        rtn.setHeight(formBoList.getHeight());
        rtn.setIsDialog(formBoList.getIsDialog());
        rtn.setIsTreeDlg(formBoList.getIsTreeDlg());
        rtn.setMultiSelect(formBoList.getMultiSelect());
        return result.setData(rtn);
    }

    @MethodDefine(title = "???????????????????????????????????????", path = "/*/dialog", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "??????KEY", varName = "boKey"), @ParamDefine(title = "????????????", varName = "request")})
    @ApiOperation(value = "????????????????????????", notes = "???????????????????????????????????????")
    @GetMapping("{boKey}/dialog")
    public JsonResult dialog(@PathVariable("boKey") String boKey, HttpServletRequest request) throws Exception {
        JsonResult result = JsonResult.Success();
        if (BeanUtil.isEmpty(boKey)) {
            return result.setSuccess(false).setMessage("?????????????????????????????????");
        }
        result.setShow(false);

        String isMobile=RequestUtil.getString(request,"isMobile","0");

        //getMobileButtons
        FormBoList formBoList = formBoListServiceImpl.getByKey(boKey);
        if (BeanUtil.isEmpty(formBoList)) {
            return result.setSuccess(false).setMessage("?????????????????????????????????");
        }
        //???????????????????????????
        Map<String, Object> params = RequestUtil.getParameterValueMap(request, false);

        Map<String, Object> model = new HashMap<>(16);
        model.put("ctxPath", request.getContextPath());
        String single = "single";
        if (BeanUtil.isEmpty(params.get(single))) {
            params.put(single, MBoolean.YES.name().equals(formBoList.getMultiSelect()) ? "false" : "true");
        }

        model.put("params", params);
        //??????????????????-??????id
        String menuId=RequestUtil.getString(request,"menuId");
        //??????????????????-????????????
        String pmtAlias=RequestUtil.getString(request,"pmtAlias");
        Map<String,Object> btnRightMap= this.handButtonRights(formBoList,menuId,pmtAlias);
        model.putAll(btnRightMap);

        //??????freemark?????????????????????
        TemplateHashModel sysBoListModel = FtlUtil.generateStaticModel(FormBoListController.class);
        model.put("SysBoListUtil", sysBoListModel);
        String html="";
        try {
            html = freemarkEngine.parseByStringTemplate(model, "1".equals(isMobile)?formBoList.getMobileHtml():formBoList.getListHtml());
        }catch (Exception e){
        }
        //?????????????????????????????????????????????????????????html?????????
        JSONObject formBoListJson = JSONObject.parseObject(JSONObject.toJSONString(formBoList));
        if("1".equals(isMobile)){
            formBoListJson.put("mobileHtml", html);
        }else{
            formBoListJson.put("listHtml", html);
        }
        return result.setData(formBoListJson).setSuccess(StringUtils.isNotEmpty(html));
    }

    private Map<String, Object> handButtonRights(FormBoList formBoList,String menuId,String pmtAlias) {
        Map<String, Object> model = new HashMap<>();
        //????????????
        String hasAllRight = "YES";
        String btnRight = "[";

        //????????????
        String hasAllColumns = "YES";
        String showColumns = "[";
        String defaultShowColumns = "[";
        //????????????
        JSONObject datas=new JSONObject();

        FormBoPmt formBoPmt = null;
        if (StringUtils.isNotEmpty(pmtAlias)) {
            formBoPmt = formBoPmtService.getByAlias(formBoList.getId(), pmtAlias);
        } else {
            if (StringUtils.isNotEmpty(menuId)) {
                formBoPmt = formBoPmtService.getByBoListIdMenuId(formBoList.getId(), menuId);
            }
        }
        if (BeanUtil.isNotEmpty(formBoPmt)) {
            pmtAlias = formBoPmt.getAlias();
            hasAllRight = "NO";
            hasAllColumns = "NO";
            btnRight += this.getButtonRight(formBoPmt);
            Map<String, Set<String>> map = parseShowColumns(formBoPmt, formBoList);
            if (map.containsKey("defaultSet") && BeanUtil.isNotEmpty(map.get("defaultSet"))) {
                for (String field : map.get("defaultSet")) {
                    defaultShowColumns += "'" + field + "',";
                }
                if (map.get("defaultSet").size() > 0) {
                    defaultShowColumns = defaultShowColumns.substring(0, defaultShowColumns.lastIndexOf(","));
                }
            }
            if (map.containsKey("set") && BeanUtil.isNotEmpty(map.get("set"))) {
                for (String field : map.get("set")) {
                    showColumns += "'" + field + "',";
                }
                if (map.get("set").size() > 0) {
                    showColumns = showColumns.substring(0, showColumns.lastIndexOf(","));
                }
            }
            datas=permissionSqlService.parsePermissionJson(formBoPmt);
        }

        showColumns += "]";
        defaultShowColumns += "]";
        model.put("hasAllColumns", hasAllColumns);
        model.put("showColumns", showColumns);
        model.put("defaultShowColumns", defaultShowColumns);
        model.put("pmtAlias", pmtAlias);

        btnRight += "]";
        model.put("hasAllRight", hasAllRight);
        model.put("btnRight", btnRight);
        model.put("pmtDatas",datas.toJSONString());
        return model;

    }

    /**
     * ??????????????????
     * @param formBoPmt
     * @return
     */
    private String getButtonRight(FormBoPmt formBoPmt) {
        String btnRight = "";

        JSONArray buttons = JSONArray.parseArray(formBoPmt.getButtons());
        if (BeanUtil.isEmpty(buttons)) {
            return btnRight;
        }
        for (Object object : buttons) {
            JSONObject json = (JSONObject) object;
            btnRight += "'" + json.getString("btnName") + "',";
        }

        return btnRight;
    }

    private Map<String,Set<String>> parseShowColumns(FormBoPmt formBoPmt,FormBoList formBoList) {
        Map<String,Set<String>> map = new HashMap<>();

        JSONArray conds = JSONArray.parseArray(formBoPmt.getFields());
        if (BeanUtil.isEmpty(conds)) {
            return map;
        }
        Set<String> defaultSet=new HashSet<>();
        Set<String> set=new HashSet<>();
        for (Object object : conds) {
            JSONObject json = (JSONObject) object;
            String field=json.getString("field");
            if(StringUtils.isNotEmpty(field) && !"WEBREQ".equals(formBoList.getUseCondSql()) && !"INTERFACE".equals(formBoList.getUseCondSql())){
                field=field.toUpperCase();
            }
            if(json.containsKey("isDefault") && (StringUtils.isEmpty(json.getString("isDefault")) || json.getBoolean("isDefault"))){
                defaultSet.add(field);
            }
            set.add(field);
        }
        map.put("defaultSet",defaultSet);
        map.put("set",set);
        return map;
    }

    @MethodDefine(title = "??????????????????", path = "/*/selfButtonExe/*", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "????????????", varName = "json"), @ParamDefine(title = "??????KEY", varName = "boKey"), @ParamDefine(title = "??????KEY", varName = "funKey")})
    @ApiOperation(value = "????????????????????????", notes = "???????????????????????????????????????")
    @PostMapping("{boKey}/selfButtonExe/{funKey}")
    public JsonResult selfButtonExe(@RequestBody JSONObject json, @PathVariable("boKey") String boKey, @PathVariable("funKey") String funKey) throws Exception {
        FormBoList formBoList = formBoListServiceImpl.getByKey(boKey);
        if (formBoList == null) {
            return new JsonResult(false, "????????????????????????" + boKey + "????????????????????????");
        }
        FormBoTopButton sysBoTopButton = formBoList.getTopButtonMap().get(funKey);
        if (sysBoTopButton == null || StringUtils.isEmpty(sysBoTopButton.getServerHandleScript())) {
            return new JsonResult(false, "????????????????????????" + funKey + "??????????????????????????????");
        }
        Object result = null;
        String pScript = null;
        try {
            CommonDao dao = SpringUtil.getBean(CommonDao.class);
            Map<String, Object> variables = new HashMap<>(16);
            JSONArray ary = json.getJSONArray("data");

            variables.put("variables", variables);
            variables.put("rows", ary);
            variables.put("dao", dao);
            //??????????????????
            pScript = freemarkEngine.parseByStringTemplate(variables, sysBoTopButton.getServerHandleScript());
            //??????????????????
            result = groovyEngine.executeScripts(pScript, variables);
        } catch (Exception ex) {
            ex.printStackTrace();
            String errMsg = pScript + "?????????????????????????????????" + ex.getMessage();
            return new JsonResult(false, errMsg);
        }

        return JsonResult.getSuccessResult(result, "???????????????").setShow(false);
    }

    @Override
    protected JsonResult beforeSave(FormBoList ent) {
        boolean isExist = formBoListServiceImpl.isExist(ent);
        if (isExist) {
            return JsonResult.Fail("??????????????????!");
        }
        //????????????
        formBoListServiceImpl.removeCache(ent.getKey());
        if(StringUtils.isNotEmpty(ent.getId())) {
            String extJson=ent.getExtJson();
            FormBoList formBoList=formBoListServiceImpl.get(ent.getId());
            ent.setExtJson(formBoList.getExtJson());
            ent.setExtJson(extJson);
        }
        //?????????????????????
        if(MBoolean.YES.name().equals(ent.getIsExpandRow()) && StringUtils.isEmpty(ent.getExpandRowJson())) {
            ent.setExpandRowJson("{}");
        }
        return JsonResult.Success();
    }

    @MethodDefine(title = "??????????????????", path = "/getFormTree", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "??????", varName = "pkId")})
    @ApiOperation(value = "????????????", notes = "??????????????????")
    @GetMapping("/getListHistory")
    public List<ListHistory> listHistory(@RequestParam(value = "pkId") String pkId) {
        List<ListHistory> listHistory = listHistoryService.getByFk(pkId);
        return listHistory;
    }

    @ApiOperation(value = "?????????????????????????????????")
    @GetMapping("getBoListByKey")
    public JsonResult getBoListByKey(@ApiParam @RequestParam("boKey") String boKey) throws Exception {
        FormBoList formBoList = formBoListServiceImpl.getByKey(boKey);
        if (formBoList == null) {
            return new JsonResult(false, "????????????????????????" + boKey + "????????????????????????");
        }
        return JsonResult.getSuccessResult(formBoList, "???????????????").setShow(false);
    }





    @ApiOperation(value = "??????PC????????????")
    @PostMapping("getPcListBtns")
    public JSONArray getPcListBtns(@ApiParam @RequestParam("alias") String alias) {
        FormBoList formBoList = formBoListServiceImpl.getByKey(alias);
        if (formBoList == null) {
            return new JSONArray();
        }
        String pcButton = formBoList.getTopBtnsJson();
        JSONArray jsonArray = JSONArray.parseArray(pcButton);
        return jsonArray;
    }

    @MethodDefine(title = "????????????", path = "/doImport", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "??????ID", varName = "request")})
    @AuditLog(operation = "??????????????????")
    @PostMapping("/doImport")
    @ApiOperation("????????????")
    public JsonResult doImport(MultipartHttpServletRequest request) throws Exception {

        MultipartFile file = request.getFile("zipFile");
        String checkName = file.getOriginalFilename();
        CharSequence formsolution = ".zip";
        if (!checkName.contains(formsolution)) {
            return JsonResult.Fail("???????????????????????????");
        }
        String treeId = request.getParameter("treeId");

        formBoListServiceImpl.importBoList(file, treeId);
        return JsonResult.Success().setMessage("????????????");
    }

    @Override
    protected JsonResult beforeRemove(List<String> list) {
        for (String id : list) {
            FormBoList boList = formBoListServiceImpl.get(id);
            //????????????????????????
            formBoListServiceImpl.removeCache(boList.getKey());
        }
        return super.beforeRemove(list);
    }


    /**
     * ???????????????query??????
     * @return
     * @throws Exception
     */
    @ApiOperation(value="????????????????????????????????????", notes="????????????????????????????????????")
    @PostMapping({"/query"})
    @Override
    public JsonPageResult query(@RequestBody QueryData queryData) throws Exception {
        JsonPageResult jsonResult = JsonPageResult.getSuccess("??????????????????!");

        try {
            QueryFilter filter = QueryFilterBuilder.createQueryFilter(queryData);
            handleFilter(filter);
            IPage<FormBoList> page = formBoListServiceImpl.query(filter);
            if(page != null && page.getSize() > 0) {
                for (FormBoList formBoList : page.getRecords()) {
                    List<FormBoPmt> list = formBoPmtService.getList(formBoList.getId(),ContextUtil.getCurrentTenantId());
                    if(BeanUtil.isEmpty(list)){
                        formBoList.setIsGranted("NO");
                    }else{
                        formBoList.setIsGranted("YES");
                    }

                }
            }
            jsonResult.setPageData(page);

        } catch (Exception e) {
            jsonResult.setSuccess(false);
            this.logger.error(ExceptionUtil.getExceptionMessage(e));
            jsonResult.setMessage(ExceptionUtil.getExceptionMessage(e));
        }

        return jsonResult;
    }

    /**
     * ???????????????
     * @return
     */
    private String getDsAlias(FormBoList formBoList) {
        String dsAlias="";
        String formAlias="";
        //?????????????????????
        if("1".equals(formBoList.getIsTenant())){
            String tenantId = ContextUtil.getCurrentTenantId();
            dsAlias=getDsByTenantId(tenantId);
            return dsAlias;
        }
        if(StringUtils.isNotEmpty(formBoList.getFormAlias())){
            formAlias=formBoList.getFormAlias();
        }else if(StringUtils.isNotEmpty(formBoList.getFormAddAlias())){
            formAlias=formBoList.getFormAddAlias();
        }else if(StringUtils.isNotEmpty(formBoList.getFormDetailAlias())){
            formAlias=formBoList.getFormDetailAlias();
        }
        //?????????????????????????????????????????????????????????
        if(StringUtils.isNotEmpty(formAlias)){
            FormSolution formSolution = formSolutionService.getByAlias(formAlias);
            FormPc formPc = formPcService.get(formSolution.getFormId());
            FormBoEntity boEntity= formBoEntityService.getByDefId(formPc.getBodefId(),true);
            //??????????????????????????????????????????
            if("1".equals(boEntity.getIsTenant())){
                String tenantId = ContextUtil.getCurrentTenantId();
                dsAlias=getDsByTenantId(tenantId);
            }
        }
        if(StringUtils.isEmpty(dsAlias)){
            dsAlias=formBoList.getDbAs();
        }
        return dsAlias;
    }

    /**
     * ????????????Id????????????????????????????????????
     * @param tenantId
     * @return
     */
    private String getDsByTenantId(String tenantId) {
        String dsAlias="";
        OsInstDto osInstDto = osInstClient.getById(tenantId);
        if(BeanUtil.isNotEmpty(osInstDto) && StringUtils.isNotEmpty(osInstDto.getDatasource()) ){
            dsAlias=JSONObject.parseObject(osInstDto.getDatasource()).getString("value");
        }
        return dsAlias;
    }

    /**
     * @return
     * @throws Exception
     */
    @MethodDefine(title = "gridreport??????sql???????????????", path = "/onRunGridreport", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "??????json", varName = "params")})
    @ApiOperation(value = "gridreport??????sql???????????????")
    @PostMapping("/onRunGridreport")
    public JsonResult onRunGridreport(@RequestBody JSONObject params) throws Exception {
        String dataSource = params.getString("ds");
        String sql = params.getString("sql");
        JSONObject result = new JSONObject();
        try {
            DataSourceContextHolder.setDataSource(dataSource);

            String newSql = getHeaderSql(request, sql);
            List<GridHeader> headers = DbUtil.getGridHeader(newSql);

            JSONArray fieldCols = new JSONArray();
            for (GridHeader gh : headers) {
                JSONObject fieldObj = new JSONObject();
                fieldObj.put("field", gh.getFieldName());
                fieldObj.put("header", gh.getFieldLabel());
                fieldObj.put("width", 100);
                fieldObj.put("isReturn", true);
                fieldCols.add(fieldObj);
            }
            result.put("headers", headers);
            result.put("fields", fieldCols);
        } catch (Exception e) {
            return new JsonResult(false, "??????SQL?????????" + e.getMessage());
        } finally {
            DataSourceContextHolder.setDefaultDataSource();
        }
        return new JsonResult(true, result, "????????????SQL").setShow(false);
    }

    @PostMapping(value = "validPreByButton")
    public JsonResult validPreByButton(@RequestBody JSONObject jsonObject){
        String config=jsonObject.getString("config");
        JSONArray rows = jsonObject.getJSONArray("rows");
        JsonResult result=formBoListServiceImpl.validPreByButton(config,rows);
        return result;
    }
}
