
package com.redxun.form.core.controller;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.*;
import com.redxun.common.base.search.QueryFilter;
import com.redxun.common.base.search.QueryFilterBuilder;
import com.redxun.common.base.search.QueryParam;
import com.redxun.common.base.search.WhereParam;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.constant.MBoolean;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.tool.constant.CommonConstant;
import com.redxun.common.utils.ContextUtil;
import com.redxun.common.utils.RequestUtil;
import com.redxun.datasource.DataSourceContextHolder;
import com.redxun.db.CommonDao;
import com.redxun.db.DbUtil;
//import com.redxun.dto.sys.SysInterfaceApiDto;
import com.redxun.dto.sys.SysWebReqDefDto;
//import com.redxun.feign.SysInterfaceApiClient;
import com.redxun.feign.SysWebReqDefClient;
import com.redxun.form.bo.entity.FormBoEntity;
import com.redxun.form.core.entity.*;
import com.redxun.form.core.service.FormSqlLogServiceImpl;
import com.redxun.form.core.service.GridReportDesignServiceImpl;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author 53737
 */
@Slf4j
@RestController
@RequestMapping("/form/core/gridreport")
@Api(tags = "GridReport报表管理")
@ClassDefine(title = "GridReport报表管理", alias = "GridReportDesignController", path = "/form/core/gridreport", packages = "core", packageName = "GridReport报表管理")
public class GridReportDesignController extends BaseController<GridReportDesign> {

    @Autowired
    GridReportDesignServiceImpl gridReportDesignService;

    @Resource
    FormSqlLogServiceImpl formSqlLogService;

/*    @Resource
    SysInterfaceApiClient sysInterfaceApiClient;*/

    @Resource
    SysWebReqDefClient sysWebReqDefClient;

    @Autowired
    CommonDao commonDao;

    private static final String WEBREQ = "WEBREQ";
    private static final String INTERFACE = "INTERFACE";

    @Override
    public BaseService getBaseService() {
        return gridReportDesignService;
    }

    @Override
    public String getComment() {
        return "报表";
    }

    @Override
    protected JsonResult beforeSave(GridReportDesign ent) {
        boolean isExist=gridReportDesignService.isExist(ent);
        if(isExist){
            return JsonResult.Fail("报表已存在!");
        }
         return JsonResult.Success();
    }

    @MethodDefine(title = "获取报表数据", path = "/*/getData", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "报表配置ID", varName = "reportConfigId"), @ParamDefine(title = "查询数据", varName = "queryParam"), @ParamDefine(title = "请求数据", varName = "request")})
    @ApiOperation(value = "获取报表数据")
    @PostMapping("{reportConfigId}/getData")
    public JsonPageResult getData(@PathVariable("reportConfigId") String reportConfigId,@RequestBody GridReportParam queryData, HttpServletRequest request) throws Exception {
        GridReportDesign gridReportDesign = gridReportDesignService.get(reportConfigId);
        if (gridReportDesign == null) {
            throw new RuntimeException("报表配置不存在");
        }
        JsonPageResult result = JsonPageResult.getSuccess("成功！");
        QueryFilter queryFilter = QueryFilterBuilder.createQueryFilter(queryData.getQueryData());


        try {
            Map<String, Object> params = RequestUtil.getParameterValueMap(request, false);
            //处理queryFilter
            handQueryFilter(queryFilter, params);
            //参数合并
            params.putAll(queryFilter.getParams());
            //加上上下文的Context变量
            addContextVar(params);
            //外部数据
            if (WEBREQ.equals(gridReportDesign.getUseCondSql())) {
                return gridReportDesignService.executeWebReq(gridReportDesign, params);
            }else if(INTERFACE.equals(gridReportDesign.getUseCondSql())){
//                return gridReportDesignService.executeInterface(gridReportDesign,params);
            }
            String sql = gridReportDesignService.getValidSql(gridReportDesign, params,queryData.getQueryData().getParams(),queryData.getIncomeParams(),queryFilter);
            String alias="LOCAL";
            if(StringUtils.isNotBlank(gridReportDesign.getDbAs())){
                alias = JSONObject.parseObject(gridReportDesign.getDbAs()).getString("value");
            }
            if(StringUtils.isNotEmpty(queryData.getIncomeParams())){
                Map<String,JSONObject> paramMap=new HashMap<>();
                JSONArray paramsArray=JSONArray.parseArray(queryData.getIncomeParams());
                List<WhereParam> whereParams=new ArrayList<>();
                for(Object obj:paramsArray){
                    JSONObject param=(JSONObject)obj;
                    paramMap.put(param.getString("fieldName"),param);
                }
                for(WhereParam tmpParam:queryFilter.getFieldLogic().getWhereParams()){
                    if(!paramMap.containsKey(((QueryParam)tmpParam).getFieldName()))
                        whereParams.add(tmpParam);
                }
                queryFilter.getFieldLogic().setWhereParams(whereParams);
            }
            List list = gridReportDesignService.getDataBySql(alias,sql, queryFilter, "YES");
            result.setData(list);
        } catch (Exception e) {
            logger.error("--FormBoListController.getData is error :--" + e.getMessage());
            DataSourceContextHolder.setDefaultDataSource();
            FormSqlLog formSqlLog = new FormSqlLog();
            formSqlLog.setType(FormSqlLog.TYPE_GRID_REPORT_DESIGN);
            formSqlLog.setSql((String) queryFilter.getParams().get("sql"));
            queryFilter.getParams().remove("sql");
            formSqlLog.setParams(JSONObject.toJSONString(queryFilter.getParams()));
            formSqlLog.setIsSuccess(MBoolean.NO.name());
            formSqlLog.setRemark("--FormBoListController.getData is error :--" + e.getMessage());
            formSqlLogService.insert(formSqlLog);
        } finally {
            DataSourceContextHolder.setDefaultDataSource();
        }
        return result;
    }

    /**
     * 处理queryFilter
     *
     * @param queryFilter
     * @param params
     */
    private void handQueryFilter(QueryFilter queryFilter, Map<String, Object> params) {
        if (com.redxun.common.tool.BeanUtil.isEmpty(params)) {
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

    private void addContextVar(Map<String, Object> params) {
        IUser curUser = ContextUtil.getCurrentUser();
        if (curUser != null) {
            params.put(FormBoEntity.FIELD_CREATE_BY, curUser.getUserId());
            params.put("DEP_ID_", curUser.getDeptId());
            params.put(FormBoEntity.FIELD_TENANT, curUser.getTenantId());
        }
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

    private void setDefaultFieldObj(JSONObject fieldObj) {
        fieldObj.put("width", 100);
        fieldObj.put("fieldExt", "");
        fieldObj.put("allowSort", false);
        if (!fieldObj.containsKey("format")) {
            fieldObj.put("format", "");
        }
        fieldObj.put("dataTypeExt", "");
        fieldObj.put("headerAlignExt", "");
        fieldObj.put("fixedExt", "");
        fieldObj.put("url", "");
        fieldObj.put("linkType", "");
        fieldObj.put("renderTypeExt", "");
        fieldObj.put("controlExt", "");
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
    /**
     * 获取上下文参数。
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

    private Map<String, Object> getParams(HttpServletRequest request) {
        Map<String, Object> params = getParameterValueMap(request, false);
        IUser curUser = ContextUtil.getCurrentUser();
        //加上上下文的Context变量
        if (curUser != null) {
            params.put(FormBoEntity.FIELD_CREATE_BY, curUser.getUserId());
            params.put(FormBoEntity.FIELD_TENANT, curUser.getTenantId());
            params.put("DEP_ID_", curUser.getDeptId());
        }
        return params;
    }

    private String getHeaderSql(HttpServletRequest request, GridReportDesign gridReportDesign) throws Exception {
        Map<String, Object> params = getParams(request);
        String newSql = gridReportDesignService.parseFreemarkSql(gridReportDesign.getUseCondSql(),
                MBoolean.YES.val.equals(gridReportDesign.getUseCondSql()) ? gridReportDesign.getCondSqls() : gridReportDesign.getSql(), params,"",null);
        newSql = DbUtil.preHandleSql(newSql);
        return newSql;
    }

    @MethodDefine(title = "重新加载表头（列表ID）", path = "/reloadColumns", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "列表ID", varName = "id")})
    @ApiOperation(value = "重新加载表头")
    @GetMapping("/reloadColumns")
    public JsonResult reloadColumns(@RequestBody GridReportDesign gridReportDesign) {
        JsonResult result = new JsonResult(true).setShow(false);
        DataSourceContextHolder.setDataSource(gridReportDesign.getDbAs());
        try {
            if (WEBREQ.equals(gridReportDesign.getUseCondSql())) {

            } else if(INTERFACE.equals(gridReportDesign.getUseCondSql())){
                //若旧的表头已经存在
                JSONArray headerCols = JSONArray.parseArray(gridReportDesign.getColsJson());
                JSONArray fieldCols = new JSONArray();
                if (headerCols == null) {
                    headerCols = new JSONArray();
                }
/*
                if(StringUtils.isNotEmpty(gridReportDesign.getInterfaceKey())) {
                    SysInterfaceApiDto sysInterfaceApiDto=sysInterfaceApiClient.getById(gridReportDesign.getInterfaceKey());
                    Map<String, JSONObject> jsonFieldMap = getFieldJsonMap(gridReportDesign.getFieldsJson());
                    JSONArray returnFileds = JSONArray.parseArray(sysInterfaceApiDto.getApiReturnFields());
                    JSONArray returnColumns=parseReturnColumns(returnFileds);
                    for (Object obj : returnColumns) {
                        JSONObject gh = (JSONObject) obj;
                        JSONObject fieldObj = new JSONObject();
                        String desc = gh.getString("paramDesc");
                        if (StringUtils.isEmpty(desc)) {
                            desc = gh.getString("paramName");
                        }
                        fieldObj.put("header", desc);
                        fieldObj.put("field", gh.getString("paramName"));
                        fieldObj.put("allowSort", false);

                        //为创建时间设置默认的格式
                        JSONObject dataType = JSONObject.parseObject("{'string':'字符','number':'整型','date':'日期型'}");
                        if (gh.getString("paramName").equals(FormBoEntity.FIELD_CREATE_TIME) || gh.getString("paramName").equals(FormBoEntity.FIELD_UPDATE_TIME)) {
                            fieldObj.put("dataType", "date");
                            fieldObj.put("format", CommonConstant.DATE_FORMAT);
                            fieldObj.put("dataType_name", dataType.getString("date"));
                        } else {
                            fieldObj.put("dataType", "number".equals(gh.getString("paramType")) ? "int" : gh.getString("paramType"));
                            fieldObj.put("dataType_name", dataType.getString(gh.getString("paramType")));
                        }
                        setDefaultFieldObj(fieldObj);
                        fieldCols.add(fieldObj);
                        //是否找到
                        boolean isFound = false;
                        for (int i = 0; i < headerCols.size(); i++) {
                            String field = headerCols.getJSONObject(i).getString("field");
                            if (field == null) {
                                continue;
                            }
                            if (field.equals(gh.getString("paramName"))) {
                                isFound = true;
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
*/
                result.setData(headerCols);
                result.setMessage("成功加载数据！");
            } else {
                //若旧的表头已经存在
                JSONArray headerCols = JSONArray.parseArray(gridReportDesign.getColsJson());
                String sql = getHeaderSql(request, gridReportDesign);
                List<GridHeader> headers = DbUtil.getGridHeader(sql);
                JSONArray fieldCols = new JSONArray();
                if (headerCols == null) {
                    headerCols = new JSONArray();
                }
                Map<String, JSONObject> jsonFieldMap = getFieldJsonMap(gridReportDesign.getFieldsJson());
                for (GridHeader gh : headers) {
                    JSONObject fieldObj = new JSONObject();
                    fieldObj.put("header", gh.getFieldLabel());
                    fieldObj.put("field", gh.getFieldName());
                    fieldObj.put("header", gh.getFieldLabel());
                    //为创建时间设置默认的格式
                    JSONObject dataType = JSONObject.parseObject("{'string':'字符','number':'整型','date':'日期型'}");
                    if (gh.getFieldName().equals(FormBoEntity.FIELD_CREATE_TIME) || gh.getFieldName().equals(FormBoEntity.FIELD_UPDATE_TIME)) {
                        fieldObj.put("dataType", "date");
                        fieldObj.put("format", CommonConstant.DATE_FORMAT);
                        fieldObj.put("dataType_name", dataType.getString("date"));
                    } else {
                        fieldObj.put("dataType", "number".equals(gh.getDataType()) ? "int" : gh.getDataType());
                        fieldObj.put("dataType_name", dataType.getString(gh.getDataType()));
                    }
                    fieldObj.put("queryDataType", gh.getQueryDataType());
                    setDefaultFieldObj(fieldObj);
                    fieldCols.add(fieldObj);
                    //是否找到
                    boolean isFound = false;
                    for (int i = 0; i < headerCols.size(); i++) {
                        String field = headerCols.getJSONObject(i).getString("field");
                        if (field == null) {
                            continue;
                        }
                        if (field.equals(gh.getFieldName())) {
                            isFound = true;
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
                result.setMessage("成功加载数据！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setSuccess(false);
            result.setMessage("加载出错");
        } finally {
            DataSourceContextHolder.setDefaultDataSource();
        }
        return result;
    }

    private JSONArray parseInterfaceMappingJson(JSONArray paramsAry,JSONArray paramsMapping,String parentId){
        JSONArray paramsTemp=new JSONArray();
        for(Object obj:paramsAry) {
            JSONObject param = (JSONObject) obj;
            JSONObject val = new JSONObject();
            String idx= IdGenerator.getIdStr();
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

    private void getInterfaceColumns(JSONObject mv, GridReportDesign gridReportDesign, JSONArray headerCols, JSONArray fieldCols, boolean isNeedAd) {
        JSONArray headerColumnsExcel = new JSONArray();
/*
        if(StringUtils.isNotEmpty(gridReportDesign.getInterfaceKey())){
            SysInterfaceApiDto sysInterfaceApiDto=sysInterfaceApiClient.getById(gridReportDesign.getInterfaceKey());
            JSONObject interfaceMapping = JSONObject.parseObject(gridReportDesign.getInterfaceMappingJson());
            */
/**********************接口参数配置************************//*

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
            */
/**********************接口参数配置************************//*

            Map<String, JSONObject> jsonFieldMap = getFieldJsonMap(gridReportDesign.getFieldsJson());
            JSONArray returnFileds=JSONArray.parseArray(sysInterfaceApiDto.getApiReturnFields());
            JSONArray returnColumns=parseReturnColumns(returnFileds);
            for (Object obj : returnColumns) {
                JSONObject gh=(JSONObject)obj;
                JSONObject fieldObj = new JSONObject();
                String desc=gh.getString("paramDesc");
                if(StringUtils.isEmpty(desc)){
                    desc=gh.getString("paramName");
                }
                //若为对话框，为其返回值作显示的原数据作准备
                fieldObj.put("header", desc);
                fieldObj.put("field", gh.getString("paramName"));
                fieldObj.put("allowSort", false);

                //为创建时间设置默认的格式
                JSONObject dataType = JSONObject.parseObject("{'string':'字符','number':'整型','date':'日期型'}");
                if (gh.getString("paramName").equals(FormBoEntity.FIELD_CREATE_TIME) || gh.getString("paramName").equals(FormBoEntity.FIELD_UPDATE_TIME)) {
                    fieldObj.put("dataType", "date");
                    fieldObj.put("format", CommonConstant.DATE_FORMAT);
                    fieldObj.put("dataType_name", dataType.getString("date"));
                } else {
                    fieldObj.put("dataType", "number".equals(gh.getString("paramType")) ? "int" : gh.getString("paramType"));
                    fieldObj.put("dataType_name", dataType.getString(gh.getString("paramType")));
                }
                setDefaultFieldObj(fieldObj);
                fieldCols.add(fieldObj);
                if (isNeedAd) {
                    JSONObject cloneObj = (JSONObject) fieldObj.clone();
                    cloneObj.remove("isReturn");
                    cloneObj.remove("visible");
                    headerCols.add(cloneObj);
                }

            }

        }
*/
        mv.put("headerColumns", headerCols);
        mv.put("fieldColumns", fieldCols);
        mv.put("isAllowDataGrant", false);
        mv.put("returnSet", new JSONArray());
    }

    private void getColumns(JSONObject mv, GridReportDesign gridReportDesign, List<GridHeader> headers, JSONArray fieldCols, JSONArray headerCols,
                            boolean isNeedAd) {
        JSONArray headerColumnsExcel = new JSONArray();


        //是否允许进行权限数据的授权
        Boolean isAllowDataGrant = false;
        //获取其原来的字段映射配置
        Map<String, JSONObject> jsonFieldMap = getFieldJsonMap(gridReportDesign.getFieldsJson());
        for (GridHeader gh : headers) {
            JSONObject fieldObj = new JSONObject();

            //设置为允许数据权限的配置
            if (!isAllowDataGrant && (FormBoEntity.FIELD_CREATE_BY.equalsIgnoreCase(gh.getFieldName()))) {
                isAllowDataGrant = true;
            }
            //若为对话框，为其返回值作显示的原数据作准备
            fieldObj.put("header", gh.getFieldLabel());
            fieldObj.put("field", gh.getFieldName());
            fieldObj.put("allowSort", false);

            //为创建时间设置默认的格式
            JSONObject dataType = JSONObject.parseObject("{'string':'字符','number':'整型','date':'日期型'}");
            if (gh.getFieldName().equals(FormBoEntity.FIELD_CREATE_TIME) || gh.getFieldName().equals(FormBoEntity.FIELD_UPDATE_TIME)) {
                fieldObj.put("dataType", "date");
                fieldObj.put("format", CommonConstant.DATE_FORMAT);
                fieldObj.put("dataType_name", dataType.getString("date"));
            } else {
                fieldObj.put("dataType", "number".equals(gh.getDataType()) ? "int" : gh.getDataType());
                fieldObj.put("dataType_name", dataType.getString(gh.getDataType()));
            }
            fieldObj.put("queryDataType", gh.getQueryDataType());
            setDefaultFieldObj(fieldObj);
            fieldCols.add(fieldObj);
            if (isNeedAd) {
                JSONObject cloneObj = (JSONObject) fieldObj.clone();
                cloneObj.remove("isReturn");
                cloneObj.remove("visible");
                headerCols.add(cloneObj);
            }

        }

        mv.put("headerColumns", headerCols);
        mv.put("fieldColumns", fieldCols);
        mv.put("isAllowDataGrant", isAllowDataGrant);
    }

    @MethodDefine(title = "获取edit2页面相关数据", path = "/getEdit2", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "请求数据", varName = "request")})
    @ApiOperation(value = "edit2页面", notes = "获取edit2页面相关数据")
    @GetMapping("/getEdit2")
    public JSONObject getEdit2(@RequestBody GridReportDesign gridReportDesign) {
        String errorMsg = null;
        JSONObject mv = new JSONObject();
        try {
            if (WEBREQ.equals(gridReportDesign.getUseCondSql())) {

                //若旧的表头已经存在
                JSONArray headerCols = JSONArray.parseArray(gridReportDesign.getColsJson());
                JSONArray fieldCols = JSONArray.parseArray(gridReportDesign.getFieldsJson());
                if (headerCols == null) {
                    headerCols = new JSONArray();
                }
                if (fieldCols == null) {
                    fieldCols = new JSONArray();
                }
                getWebReqColumns(mv, gridReportDesign, headerCols, fieldCols);
                String headerColumns = headerCols.toJSONString();
                String fieldColumns = fieldCols.toJSONString();
                if (StringUtils.isEmpty(gridReportDesign.getColsJson())) {
                    gridReportDesign.setColsJson(headerColumns);
                    gridReportDesign.setFieldsJson(fieldColumns);
                    gridReportDesignService.update(gridReportDesign);
                }
            } else if(INTERFACE.equals(gridReportDesign.getUseCondSql())) {

                //若旧的表头已经存在
                JSONArray headerCols = JSONArray.parseArray(gridReportDesign.getColsJson());
                boolean isNeedAd = (headerCols == null || headerCols.size() == 0) ? true : false;
                JSONArray fieldCols = new JSONArray();
                if (headerCols == null) {
                    headerCols = new JSONArray();
                }
                getInterfaceColumns(mv, gridReportDesign, headerCols, fieldCols,isNeedAd);
                String headerColumns = headerCols.toJSONString();
                String fieldColumns = fieldCols.toJSONString();
                if (StringUtils.isEmpty(gridReportDesign.getColsJson())) {
                    gridReportDesign.setColsJson(headerColumns);
                    gridReportDesign.setFieldsJson(fieldColumns);
                    gridReportDesignService.update(gridReportDesign);
                }
            } else {
                DataSourceContextHolder.setDataSource(gridReportDesign.getDbAs());


                //若旧的表头已经存在
                JSONArray headerCols = JSONArray.parseArray(gridReportDesign.getColsJson());

                List<GridHeader> headers = DbUtil.getGridHeader(getHeaderSql(request, gridReportDesign));
                JSONArray fieldCols = new JSONArray();
                boolean isNeedAd = (headerCols == null || headerCols.size() == 0) ? true : false;

                if (headerCols == null) {
                    headerCols = new JSONArray();
                }
                getColumns(mv, gridReportDesign, headers, fieldCols, headerCols, isNeedAd);

                String headerColumns = headerCols.toJSONString();
                String fieldColumns = fieldCols.toJSONString();

                if (StringUtils.isEmpty(gridReportDesign.getColsJson())) {
                    DataSourceContextHolder.setDefaultDataSource();
                    gridReportDesign.setColsJson(headerColumns);
                    gridReportDesign.setFieldsJson(fieldColumns);
                    gridReportDesignService.update(gridReportDesign);
                }
            }
        } catch (Exception e) {
            errorMsg = e.getMessage();
            mv.put("errorMsg", errorMsg);
        } finally {
            DataSourceContextHolder.setDefaultDataSource();
        }
        return mv;
    }

    @MethodDefine(title = "根据pkId获取全部数据", path = "/getDataByPkId", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "根据pkId获取全部数据", varName = "request")})
    @ApiOperation(value = "根据pkId获取全部数据", notes = "根据pkId获取全部数据")
    @GetMapping("/getDataByPkId/{pkId}")
    public JSONObject getDataByPkId(@PathVariable String pkId){

        GridReportDesign gridReportDesign = gridReportDesignService.get(pkId);
        return (JSONObject) JSON.toJSON(gridReportDesign);
    }

    private void getWebReqColumns(JSONObject mv, GridReportDesign gridReportDesign, JSONArray headerCols, JSONArray fieldCols) {
        if (StringUtils.isNotEmpty(gridReportDesign.getWebReqKey())) {
            SysWebReqDefDto webReqDef = sysWebReqDefClient.getByAlias(gridReportDesign.getWebReqKey());
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
            JSONObject webMapiing = JSONObject.parseObject(gridReportDesign.getWebReqMappingJson());
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

        }
        mv.put("headerColumns", headerCols);
        mv.put("fieldColumns", fieldCols);
        mv.put("isAllowDataGrant", false);
        mv.put("returnSet", new JSONArray());
    }

}



