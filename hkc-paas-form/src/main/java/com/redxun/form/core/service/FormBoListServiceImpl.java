package com.redxun.form.core.service;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.redxun.api.org.IOrgService;
import com.redxun.cache.CacheUtil;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.*;
import com.redxun.common.base.search.*;
import com.redxun.common.constant.MBoolean;
import com.redxun.common.engine.FtlEngine;
import com.redxun.common.script.GroovyEngine;
import com.redxun.common.script.NoMethodException;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.DateUtils;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ContextUtil;
import com.redxun.common.utils.ExcelUtil;
import com.redxun.common.utils.ExceptionUtil;
import com.redxun.common.utils.FileUtil;
import com.redxun.constvar.ConstVarContext;
import com.redxun.datasource.DataSourceContextHolder;
import com.redxun.db.CommonDao;
import com.redxun.dto.sys.SysWebReqDefDto;
import com.redxun.feign.*;
import com.redxun.feign.sys.SystemClient;
import com.redxun.form.bo.entity.FormBoAttr;
import com.redxun.form.bo.entity.FormBoEntity;
import com.redxun.form.core.entity.*;
import com.redxun.form.core.grid.column.MiniGridColumnRender;
import com.redxun.form.core.grid.column.MiniGridColumnRenderConfig;
import com.redxun.form.core.grid.column.MiniGridColumnRenderSearch;
import com.redxun.form.core.grid.enums.MiniGridColumnType;
import com.redxun.form.core.importdatahandler.ImportDataHandlerExecutor;
import com.redxun.form.core.mapper.FormBoListMapper;
import com.redxun.form.util.FormExOrImportHandler;
import com.redxun.form.util.SysFileUtil;
import com.redxun.log.model.Audit;
import com.redxun.log.model.LogContext;
import freemarker.template.TemplateException;
import groovy.lang.Script;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
* [系统自定义业务管理列表]业务服务类
* @author hujun
*/
@Slf4j
@Service
public class FormBoListServiceImpl extends SuperServiceImpl<FormBoListMapper, FormBoList> implements BaseService<FormBoList> {

    public static String AMOUTSUCCESS = "amoutSuccess";
    public static String AMOUTFAIL = "amoutFail";
    public static String ERRORSTR = "errorStr";

    private final static String HAND_ROW = "handRow";
    private final static String BEFORE_INSERT = "beforeInsert";

    @Resource
    private FormBoListMapper formBoListMapper;
    @Resource
    FtlEngine freemarkEngine;
    @Resource
    GroovyEngine groovyEngine;
    @Resource
    CommonDao commonDao;
    @Resource
    MiniGridColumnRenderConfig miniGridColumnRenderConfig;

    @Resource
    SysWebReqDefClient sysWebReqDefClient;
    @Resource
    SysInterfaceApiClient sysInterfaceApiClient;
    @Resource
    ConstVarContext constVarContext;

    @Resource
    IOrgService orgService;
    @Resource
    FormSqlLogServiceImpl formSqlLogService;
    @Resource
    FormEntityDataSettingServiceImpl formEntityDataSettingService;
    @Resource
    MiniGridColumnRenderSearch miniGridColumnRenderSearch;
    @Resource
    BpmInstClient bpmInstClient;
    @Autowired
    ListHistoryServiceImpl listHistoryService;
    @Resource
    ImportDataHandlerExecutor importDataHandlerExecutor;

    @Resource
    private  FormDataService formDataService;
    @Resource
    PermissionSqlService permissionSqlService;
    @Resource
    FormPermissionServiceImpl formPermissionService;
    @Autowired
    SystemClient systemClient;
    @Autowired
    FormExcelGenTaskServiceImpl formExcelGenTaskService;

    @Override
    public BaseDao<FormBoList> getRepository() {
        return formBoListMapper;
    }


    public void genHtml(String id,String ctlPath,String remark) throws Exception {

        Map<String,Object> model=new HashMap<>(16);
        model.put("ctxPath",ctlPath);
        //生成模板时，不替换${ctxPath},解析真正页面时，才替换
        FormBoList formBoList= get(id);
        //清除缓存
        removeCache(formBoList.getKey());

        String[] listHtml= genHtmlPage(formBoList, model);
        formBoList.setListHtml(listHtml[0]);
        formBoList.setMobileHtml(listHtml[1]);
        formBoList.setIsGen(MBoolean.YES.name());
        update(formBoList);
        listHistoryService.addList(formBoList,remark);
    }


    public void removeCache(String key){
        //清除缓存
        CacheUtil.remove(FormBoListServiceImpl.REGION_BOLIST,getListKey(key));
    }



    /**
     * 缓存区域
     */
    public static String REGION_BOLIST="boList";

    /**
     * 产生对应的页面
     * @param formBoList
     * @return
     * @throws TemplateException
     * @throws IOException
     */
    public String[] genHtmlPage(FormBoList formBoList, Map<String,Object> rootParams) throws Exception{
        JSONArray searchArr = JSONArray.parseArray(formBoList.getSearchJson());
        int searchSize=0;
        if(searchArr!=null){
            searchSize=searchArr.size();
        }
        Map<String,Object> params=new HashMap<>(searchSize+rootParams.size());
        Map<String,Object> tmpParams= getGenParams(formBoList, (searchArr==null || searchArr.size() == 0) ? false : true);
        params.putAll(rootParams);
        params.putAll(tmpParams);

        if (MBoolean.YES.val.equals(formBoList.getIsDialog())) {
            formBoList.setTemplateType("dialogListTemplate.ftl");
        } else {
            if (StringUtils.isEmpty(formBoList.getTemplateType())) {
                formBoList.setTemplateType("pageListTemplate.ftl");
            }
        }

        String html=freemarkEngine.mergeTemplateIntoString("/list/" + formBoList.getTemplateType(), params);
        String mobileHtml=freemarkEngine.mergeTemplateIntoString("/mobile/pageListTemplate.ftl", params);

        String[] htmlAry=new String[2];
        htmlAry[0]=html;
        htmlAry[1]=mobileHtml;
        return htmlAry;
    }

    /**
     * 获取行操作按钮。
     * @param jsonAry
     * @return
     */
    private JSONArray getActionButtons(JSONArray jsonAry){
        JSONArray rtnAry=new JSONArray();
        for(int i=0;i<jsonAry.size();i++){
            JSONObject row=jsonAry.getJSONObject(i);
            String renderType=row.getString("renderType");
            JSONObject renderConf =row.getJSONObject("renderConf");
            if("ACTION".equals(renderType) && BeanUtil.isNotEmpty(renderConf)){
                rtnAry=renderConf.getJSONArray("btnList");
                break;
            }
        }
        return rtnAry;
    }


    public Map<String,Object> getGenParams(FormBoList formBoList,boolean needSearch) throws  Exception{
        Map<String,Object> params=new HashMap<>(16);

        params.put("gridName",formBoList.getKey());
        //产生表格的Html的Json
        String colsHtml=getColumnsHtml(formBoList.getColsJson(),params,false,formBoList.getRowEdit(),formBoList);
        params.put("gridColumns", colsHtml);
        //将列头的一些配置合并到返回字段列配置中
        String colsJson=mergeColJsons(formBoList.getFieldsJson(),formBoList.getColsJson());
        colsHtml = getColumnsHtml(colsJson, params, true,formBoList.getRowEdit(),formBoList);
        params.put("dialgColumns", colsHtml);
        if(StringUtils.isNotEmpty(colsHtml)){
            params.put("dialgColumnList", JSONArray.parseArray(colsHtml));
        }
        //产生行编辑的JSON
        JSONArray cellEditorHtml = getCellEditorHtml(formBoList.getColsJson(), params,formBoList.getRowEdit(),formBoList);
        params.put("cellEditorColumns", cellEditorHtml);
        //生成按钮
        JSONArray rowButtons = getActionButtons(cellEditorHtml);
        params.put("rowButtons", rowButtons.toJSONString());
        //产生搜索条件的Html
        JSONArray searchHtml = getSearchColumns(formBoList.getSearchJson(), params);
        params.put("searchColumns", searchHtml);
        //产生导航栏的Html
        String tabTreeHtml = getTabTreeColumns(formBoList.getLeftTreeJson(), params);
        params.put("tabTreeColumns", JSONArray.parseArray(tabTreeHtml));
        String topButtonHtml = getTopButtonHtml(formBoList.getTopBtnsJson(), params);
        params.put("topButtonHtml",topButtonHtml);
        params.put("topButtonColumns", JSONArray.parseArray(topButtonHtml));

        params.put("formBoList", formBoList);
        params.put("expandRowJson",formBoList.getExpandRowJson()==null?"{}":formBoList.getExpandRowJson());
        String expandRowJsonType="self";
        if(StringUtils.isNotEmpty(formBoList.getExpandRowJson())){
            JSONObject expandRowJson=JSONObject.parseObject(formBoList.getExpandRowJson());
            expandRowJsonType=!expandRowJson.containsKey("type")?"self":expandRowJson.getString("type");
        }
        if("self".equals(expandRowJsonType)){
            JSONObject jsonObject=JSONObject.parseObject((String)params.get("expandRowJson"));
            String listHtml=jsonObject.getString("listHtml");
            if(StringUtils.isEmpty(listHtml)){
                listHtml="请配置展示行";
            }
            params.put("expandRowJsonListHtml",listHtml);
        }
        params.put("expandRowJsonType",expandRowJsonType);
        params.put("bodyScript", formBoList.getBodyScript() == null ? "" : formBoList.getBodyScript());
        JSONObject mobileConf=JSONObject.parseObject(formBoList.getMobileConf() == null ? "{}" : formBoList.getMobileConf());
        params.put("mobileStyle", mobileConf.getInteger("mobileStyle"));
        //转换手机端的列
        String mobileCols=mobileConf.getString("mobileCols");
        mobileCols= handMobileRenderTypes(formBoList.getColsJson(),mobileCols);
        params.put("mobileCols",mobileCols);

        params.put("mobileTreeTab", mobileConf.getString("mobileTreeTab"));
        params.put("mobileSearch", mobileConf.getString("mobileSearch"));
        params.put("mobileIsSearchView", mobileConf.getString("mobileIsSearchView"));
        params.put("mobileIsShowView", mobileConf.getString("mobileIsShowView"));
        params.put("mobileIsViewHeader", mobileConf.getString("mobileIsViewHeader"));
        params.put("mobileView", mobileConf.getString("mobileView"));
        params.put("mobileButton", mobileConf.getString("mobileButton"));
        String mobileJs = mobileConf.getString("mobileJs");
        params.put("mobileJs",mobileJs==null? "" : mobileJs);
        params.put("formAlias", formBoList.getFormAlias() == null ? "" : formBoList.getFormAlias());
        params.put("formAddAlias", formBoList.getFormAddAlias() == null ? "" : formBoList.getFormAddAlias());
        params.put("formDetailAlias", formBoList.getFormDetailAlias() == null ? "" : formBoList.getFormDetailAlias());
        params.put("idField", formBoList.getIdField() == null ? "" : formBoList.getIdField());
        params.put("parentField", formBoList.getParentField() == null ? "" : formBoList.getParentField());
        params.put("expandIconColumnIndex",getExpandIconColumnIndex(formBoList));
        params.put("isTreeDlg", formBoList.getIsTreeDlg() == null ? "NO" : formBoList.getIsTreeDlg());
        params.put("busSolution", formBoList.getBusSolution()== null ? "" : formBoList.getBusSolution());

        return params;
    }

    private String handMobileRenderTypes(String colsJson,String mobileCols){
        if(StringUtils.isEmpty(mobileCols)){
            return "[]";
        }
        JSONArray mobileAry=JSONArray.parseArray(mobileCols);
        JSONArray colAry=JSONArray.parseArray(colsJson);

        Map<String,JSONObject> mapJson=new HashMap<>();
        for(int i=0;i<colAry.size();i++){
            JSONObject colJson=colAry.getJSONObject(i);
            String field=colJson.getString("field");
            mapJson.put(field,colJson);
        }

        for(int i=0;i<mobileAry.size();i++){
            JSONObject mobileJson=mobileAry.getJSONObject(i);
            String field=mobileJson.getString("field");
            JSONObject colJson=mapJson.get(field);
            if(colJson.containsKey("renderType")){
                mobileJson.put("renderType",colJson.getString("renderType"));
            }

            if(colJson.containsKey("renderConf")){
                mobileJson.put("renderConf",colJson.getString("renderConf"));
            }
        }
        return mobileAry.toJSONString();



    }

    private int getExpandIconColumnIndex(FormBoList formBoList){
        String colJsons=formBoList.getColsJson();
        String textField=formBoList.getTextField();
        if(StringUtils.isNotEmpty(colJsons)) {
            JSONArray columnJsons = JSONArray.parseArray(colJsons);
            for (int i = 0; i < columnJsons.size(); i++) {
                JSONObject column = columnJsons.getJSONObject(i);
                if (column.getString("field").equals(textField)) {
                    return i + 2;
                }
            }
        }
        //默认第一列
        return 1;
    }

    public JSONArray getSearchColumns(String searchJson,Map<String,Object> params){
        JSONArray array=JSONArray.parseArray(searchJson==null?"[]":searchJson);
        JSONArray search=new JSONArray();
        for(int i=0;i<array.size();i++){
            JSONObject json=array.getJSONObject(i);
            if(!"income".equals(json.getString("type"))){
                search.add(json);
            }
        }
        return search;
    }

    public String getTabTreeColumns(String leftTreeJson,Map<String,Object> params){
        return leftTreeJson==null?"[]":leftTreeJson;
    }

    public String getTopButtonHtml(String topBtnsJson,Map<String,Object> params){
        return topBtnsJson==null?"[]":topBtnsJson;
    }

    private JSONArray getColumnJsons(JSONArray colJsons){
        JSONArray temp=new JSONArray();
        temp.addAll(colJsons);
        for(Object obj:colJsons){
            JSONObject json=(JSONObject)obj;
            if(json.containsKey("children")) {
                temp.addAll(getColumnJsons(json.getJSONArray("children")));
            }
        }
        return temp;
    }

    private JSONArray getCellEditorHtml(String colJsons,Map<String,Object> params,String rowEdit,FormBoList formBoList){
        JSONArray columnJsons=getColumnJsons(JSONArray.parseArray(colJsons));
        JSONArray cellEditorJsons=new JSONArray();
        for(Object obj:columnJsons) {
            JSONObject json = (JSONObject) obj;
            JSONObject cellEditor=new JSONObject();
            String control=json.getString("control");
            String renderType=json.getString("renderType");
            String field=json.getString("field");
            if(StringUtils.isNotEmpty(field) && !"WEBREQ".equals(formBoList.getUseCondSql()) && !"INTERFACE".equals(formBoList.getUseCondSql())){
                field=field.toUpperCase();
            }
            if(MBoolean.YES.val.equals(rowEdit) && StringUtils.isNotEmpty(control)) {
                cellEditor.put("type","control");
                cellEditor.put("field",field);
                cellEditor.put("header",json.getString("header"));
                cellEditor.put("control", control);
                cellEditor.put("controlConf", json.getJSONObject("controlConf"));
                cellEditor.put("renderConf", json.getJSONObject("renderConf"));
                cellEditorJsons.add(cellEditor);
            }else if(StringUtils.isNotEmpty(renderType)){
                cellEditor.put("type","render");
                cellEditor.put("field",field);
                cellEditor.put("header",json.getString("header"));
                cellEditor.put("renderType", renderType);
                cellEditor.put("renderConf", json.getJSONObject("renderConf"));
                //排除不需要生成的渲染类型
                String[] includeStr=new String[]{
                        MiniGridColumnType.TEXT.name(),
                        MiniGridColumnType.DISPLAY_LABEL.name(),
                        MiniGridColumnType.DISPLAY_ITEMS.name(),
                        MiniGridColumnType.DISPLAY_RANGE.name(),
                        MiniGridColumnType.FLOW_STATUS.name(),
                        MiniGridColumnType.OVER_TIME.name()
                };
                if(!Arrays.asList(includeStr).contains(renderType)) {
                    cellEditorJsons.add(cellEditor);
                }
            }
            String customTitle=json.getString("customTitle");
            if(StringUtils.isNotEmpty(customTitle)){
                cellEditor=new JSONObject();
                cellEditor.put("type","customTitle");
                cellEditor.put("field",field+"Title");
                cellEditor.put("html", customTitle);
                cellEditorJsons.add(cellEditor);
            }
        }
        return cellEditorJsons;
    }
    /**
     * 获得栏目的HTML
     * @param colJsons
     * @return
     */
    private String getColumnsHtml(String colJsons,Map<String,Object> params,boolean isDialog,String rowEdit,FormBoList formBoList){
        JSONArray columns=new JSONArray();
        if(StringUtils.isEmpty(colJsons)){
            return columns.toJSONString();
        }
        JSONArray columnJsons=JSONArray.parseArray(colJsons);
        JSONObject index = new JSONObject();
        index.put("title","序号");
        index.put("width",50);
        index.put("dataIndex","index");
        index.put("type","indexColumn");
        index.put("scopedSlots",JSONObject.parseObject("{'customRender':'index'}"));
        columns.add(index);

        getColumnsJson(columns,columnJsons,isDialog,rowEdit,formBoList);

        return columns.toJSONString();
    }

    private void getColumnsJson(JSONArray columns,JSONArray columnJsons,boolean isDialog,String rowEdit,FormBoList formBoList){
        for(Object obj:columnJsons){
            JSONObject json = (JSONObject) obj;
            if(json.getBoolean("visible")==null || json.getBoolean("visible")) {
                JSONObject column = new JSONObject();
                String field=json.getString("field");
                if(StringUtils.isNotEmpty(field) && !"WEBREQ".equals(formBoList.getUseCondSql()) && !"INTERFACE".equals(formBoList.getUseCondSql())){
                    field=field.toUpperCase();
                }
                column.put("dataIndex", field);
                if(json.getInteger("width") !=null){
                    column.put("width", json.getInteger("width"));
                }
                column.put("align", json.getString("headerAlign"));
                String fixed = json.getString("fixed");
                if (StringUtils.isNotEmpty(fixed)) {
                    column.put("fixed", "false".equals(fixed) ? false : fixed);
                }
                if (!isDialog) {
                    column.put("customCell","<func>(row,index)=>{return this.customCellMethod('"+field+"',row,index);}</func>");
                    String control = json.getString("control");
                    String renderType = json.getString("renderType");
                    String format = json.getString("format");
                    if (MBoolean.YES.val.equals(rowEdit) && StringUtils.isNotEmpty(control)) {
                        if("select".equals(control) && json.containsKey("controlConf")){
                            String fieldLabel = json.getJSONObject("controlConf").getString("fieldLabel");
                            if(!json.getString("field").equals(fieldLabel)) {
                                column.put("displayField", fieldLabel);
                            }
                        }
                        if("checkbox".equals(control)){
                            column.put("allowCellEdit", false);
                        }else {
                            column.put("allowCellEdit", true);
                        }
                    }
                    if(MBoolean.YES.val.equals(rowEdit) && StringUtils.isNotEmpty(control) || StringUtils.isNotEmpty(renderType) || StringUtils.isNotEmpty(format)){
                        column.put("scopedSlots", JSONObject.parseObject("{'customRender':'" + field + "'}"));
                    }
                    column.put("sorter",json.getBooleanValue("allowSort"));
                    column.put("sortField",field);
                }else {
                    if( StringUtils.isNotEmpty(json.getString("format"))){
                        column.put("scopedSlots", JSONObject.parseObject("{'customRender':'"+field+"'}"));
                    }
                }
                if(StringUtils.isNotEmpty(json.getString("customTitle"))){
                    column.put("slots", JSONObject.parseObject("{'title':'"+field+"Title'}"));
                }else{
                    column.put("title", json.getString("header"));
                }
                if(json.containsKey("children")){
                    JSONArray children=new JSONArray();
                    getColumnsJson(children,json.getJSONArray("children"),isDialog,rowEdit,formBoList);
                    column.put("children",children);
                }
                columns.add(column);
            }
        }
    }



    public String parseFreemarkSql(String useCondSql,String sql,Map<String,Object> params) throws Exception{
        String newSql=null;
        Pattern pattern = Pattern.compile("#\\{([\\w]+)\\}");
        Matcher matcher = pattern.matcher(sql);
        while(matcher.find()) {
            String placeHolder = matcher.group(0);
            String key = matcher.group(1);
            sql=sql.replace("#{"+key+"}","<#noparse>#{w."+key+"}</#noparse>");
        }
        if (MBoolean.YES.val.equals(useCondSql)) {
            sql = freemarkEngine.parseByStringTemplate(params, sql);
            params.put("params",params);
            newSql = (String) groovyEngine.executeScripts(sql, params);
        } else {
            newSql = freemarkEngine.parseByStringTemplate(params, sql);
        }

        return newSql;
    }

    /**
     * 通过外部参数获得业务实体的SQL
     *
     * select * from table1 where 1=1 order by tmp;
     *
     * @param formBoList
     * @param params
     * @return
     * @throws Exception
     */
    public String getValidSql(FormBoList formBoList,Map<String,Object> params) throws Exception{

        String newSql=parseFreemarkSql(formBoList.getUseCondSql(),
                MBoolean.YES.val.equals(formBoList.getUseCondSql())?formBoList.getCondSqls():formBoList.getSql(),params);

        //newSql=newSql.toUpperCase();
        String menuId=(String)params.get("menuId");
        String pmtAlias=(String)params.get("pmtAlias");
        String permissionSql=permissionSqlService.parsePermissionSql(formBoList.getId(),menuId,pmtAlias);

        return dealSql(newSql, permissionSql);

    }



    private String dealSql(String sql, String permissionSql){
        if(StringUtils.isEmpty(permissionSql) || "()".equals(permissionSql.trim())){
            return sql;
        }
        //表示sql中存在 /*condition*/
        if(sql.indexOf(CommonDao.CONDITION_TAG)!=-1){
            int len=2;
            String[] arySql=sql.split("/\\*CONDITION\\*/");
            String sqlreturn =  arySql[0] + CommonDao.CONDITION_TAG + " and (" +permissionSql +")" ;
            if(arySql.length==len){
                sqlreturn+=" " +arySql[1];
            }
            return  sqlreturn;
        }
        else{
            sql=permissionSqlService. insertWhereSql(sql,permissionSql);
        }
        return  sql;
    }






    /**
     * 返回sql的查询结果
     * @param sql
     * @param filter
     * @return
     */
    public com.github.pagehelper.Page getPageDataBySql(String sql,QueryFilter filter,String isTest){
        com.github.pagehelper.Page page=commonDao.queryDynamicList(sql, filter,null);
        log.debug("--FormBoListServiceImpl.getPageDataBySql is debug :--"+filter.getParams().get("sql"));
        DataSourceContextHolder.setDefaultDataSource();
        if(MBoolean.YES.name().equals(isTest)){
            FormSqlLog formSqlLog=new FormSqlLog();
            formSqlLog.setType(FormSqlLog.TYPE_FORM_BO_LIST);
            formSqlLog.setSql((String)filter.getParams().get("sql"));
            String newSql=(String)filter.getParams().remove("sql");
            formSqlLog.setParams(JSONObject.toJSONString(filter.getParams()));
            formSqlLog.setIsSuccess(MBoolean.YES.name());
            formSqlLog.setRemark("--FormBoListServiceImpl.getPageDataBySql is debug :--"+newSql);
            formSqlLogService.insert(formSqlLog);
        }
        return page;
    }

    public List getDataBySql(String sql, QueryFilter filter,String isTest) {
        List list= commonDao.queryForList(sql, filter, null);
        log.debug("--FormBoListServiceImpl.getDataBySql is debug :--"+filter.getParams().get("sql"));
        DataSourceContextHolder.setDefaultDataSource();
        if(MBoolean.YES.name().equals(isTest)){
            FormSqlLog formSqlLog=new FormSqlLog();
            formSqlLog.setType(FormSqlLog.TYPE_FORM_BO_LIST);
            formSqlLog.setSql((String)filter.getParams().get("sql"));
            String newSql=(String)filter.getParams().remove("sql");
            formSqlLog.setParams(JSONObject.toJSONString(filter.getParams()));
            formSqlLog.setIsSuccess(MBoolean.YES.name());
            formSqlLog.setRemark("--FormBoListServiceImpl.getDataBySql is debug :--"+newSql);
            formSqlLogService.insert(formSqlLog);
        }
        return list;
    }


    public Long getCountDataBySql(String sql, QueryFilter filter,String isTest) {

        Object count=  commonDao.queryOne(sql,filter,null);
        Long count_=0L;
        if(count instanceof Long){
            count_=(Long)count;
        }
        //返回的数据类型如果为Decimal
        if(count instanceof BigDecimal){
            BigDecimal tmp=(BigDecimal) count;
            count_= tmp.longValue();
        }

        return count_;
    }

    public Map<String,GridHeader> getGridHeaderMap(String colJsons){
        JSONArray columnArr=JSONArray.parseArray(colJsons);
        Map<String,GridHeader> headerMap=new HashMap<>(columnArr.size());
        genGridColumnHeaders(headerMap,columnArr);
        return headerMap;
    }

    /**
     * 获得表格的列头
     * @param headerMap
     * @param columnArr
     */
    private void genGridColumnHeaders(Map<String,GridHeader> headerMap,JSONArray columnArr){
        for(int i=0;i<columnArr.size();i++){
            JSONObject jsonObj=columnArr.getJSONObject(i);
            JSONArray children=jsonObj.getJSONArray("children");
            if(children!=null && children.size()>0){
                genGridColumnHeaders(headerMap,children);
                continue;
            }
            String header=jsonObj.getString("header");
            String field=jsonObj.getString("field");
            String renderType=jsonObj.getString("renderType");
            String renderConf=jsonObj.getString("renderConf");
            GridHeader gh=new GridHeader();
            gh.setFieldLabel(header);
            gh.setFieldName(field);
            gh.setRenderType(renderType);
            gh.setFormat(jsonObj.getString("format"));
            gh.setCurDataType(jsonObj.getString("dataType"));
            if(StringUtils.isNotEmpty(renderConf)){
                JSONObject conf=JSONObject.parseObject(renderConf);
                gh.setRenderConfObj(conf);
            }
            headerMap.put(field,gh);
        }
    }

    public void gdRenderColumn(String isMobile,FormBoList formBoList,List list, List showColumns) throws Exception {
        if (MBoolean.TRUE_LOWER.val.equals(isMobile)) {
            return;
        }
        Map<String, GridHeader> gridHeaderMap = getGridHeaderMap(formBoList.getColsJson());
        //需要在vue中渲染方法的
        String[] excludeStr = new String[]{MiniGridColumnType.USER.toString(),
                MiniGridColumnType.GROUP.toString(),
                MiniGridColumnType.SYSINST.toString(),
                MiniGridColumnType.URL.toString(),
                MiniGridColumnType.LINK_FLOW.toString(),
                MiniGridColumnType.DISPLAY_PERCENT.toString()};
        List<String> excludeList = Arrays.asList(excludeStr);

        String idField=formBoList.getIdField().toUpperCase();

        //处理后端的数据格式化及展示的问题
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> newRow = (Map) list.get(i);
            //存放转为大写的数据
            Map<String, Object> row=new HashMap<>();
            Set<String> keys = newRow.keySet();
            for(String key :keys){
                Object val=newRow.get(key);
                if(val instanceof Long){
                    val=val.toString();
                }
                String upKey=key.toUpperCase();
                if("children".equals(key)){
                    upKey="children";
                }

                row.put(upKey,val);
            }
            list.set(i,row);
            for (GridHeader gd : gridHeaderMap.values()) {
                if(gd==null){
                    continue;
                }
                Object val = row.get(gd.getFieldName().toUpperCase());
                MiniGridColumnRender render = miniGridColumnRenderConfig.getColumnRenderMapByType(gd.getRenderType());
                if (render != null) {
                    if(showColumns != null){
                        if(!showColumns.contains(gd.getFieldName())){
                            handleNotRender(gd,val,row);
                            continue;
                        }
                    }
                    Object ival = "";
                    if(gd.getRenderType().equals("DISPLAY_ONLY")){
                        ival = row.get(gd.getFieldName().toUpperCase());
                    }
                    else {
                        ival = render.render(gd, row, val, false);

                        if (excludeList.contains(render.getRenderType())) {
                            row.put(gd.getFieldName().toUpperCase() + "_render", ival);
                        } else {
                            row.put(gd.getFieldName().toUpperCase() + "_display", ival);
                        }
                    }
                }else{
                    handleNotRender(gd,val,row);
                }
            }
        }

    }

    public void handleNotRender(GridHeader gd, Object val, Map<String, Object> row ){
        String format=gd.getFormat();
        String datatype=gd.getCurDataType();
        if(StringUtils.isNotEmpty(format) && StringUtils.isNotEmpty(datatype)){
            if("date".equals(datatype)){
                if(val instanceof Date){
                    SimpleDateFormat df = new SimpleDateFormat(format);
                    row.put(gd.getFieldName().toUpperCase()+"_display",df.format(val));
                }
            }else if("float".equals(datatype) || "int".equals(datatype) || "currency".equals(datatype)) {
                if(val instanceof Number){
                    DecimalFormat df = new DecimalFormat(format);
                    String num=((Number)val).doubleValue()==0?"0":""+df.format(val);
                    row.put(gd.getFieldName().toUpperCase()+"_display",num);
                }
            }
        }
    }

    /**
     *
     * 获取缓存KEY.
     * @param key
     * @return
     */
    public String getListKey(String key){
        return  REGION_BOLIST + key;
    }

    public FormBoList getByKey(String key){
        String cacheKey=getListKey(key);
        Object obj= CacheUtil.get(REGION_BOLIST,cacheKey);
        if(obj!=null){
            return (FormBoList) obj;
        }
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("KEY_",key);
        FormBoList formBoList = formBoListMapper.selectOne(queryWrapper);
        if(formBoList!=null){
            parseSysBoList(formBoList);
            CacheUtil.set(REGION_BOLIST,cacheKey,formBoList);
        }

        return formBoList;
    }

    private void parseSysBoList(FormBoList formBoList){
        //获得列头大小
        if(StringUtils.isNotEmpty(formBoList.getColsJson())){
            Map<String,GridHeader> headerMap=getGridHeaderMap(formBoList.getColsJson());
            formBoList.setColumnHeaderMap(headerMap);
        }
        //在Bo中增加缓存的按钮处理
        if(StringUtils.isNotEmpty(formBoList.getTopBtnsJson())){
            List<FormBoTopButton> buttons=JSONArray.parseArray(formBoList.getTopBtnsJson(), FormBoTopButton.class);
            formBoList.getTopButtonMap().clear();
            if(buttons!=null) {
                for (FormBoTopButton btn : buttons) {
                    formBoList.getTopButtonMap().put(btn.getBtnName(), btn);
                }
            }
        }

        //在Bo中增加左树的缓存处理
        if(StringUtils.isNotEmpty(formBoList.getLeftTreeJson())){
            List<TreeConfig> trees=JSONArray.parseArray(formBoList.getLeftTreeJson(), TreeConfig.class);
            formBoList.getLeftTreeMap().clear();
            if(trees!=null) {
                for (TreeConfig conf : trees) {
                    formBoList.getLeftTreeMap().put(conf.getTreeId(), conf);
                }
            }
        }
    }

    /**
     * 产生对应的页面
     * @param formBoList
     * @return
     * @throws TemplateException
     * @throws IOException
     */
    public String genTreeDlgHtmlPage(FormBoList formBoList,Map<String,Object> params,String templateName) throws IOException, TemplateException{
        params.put("formBoList", formBoList);
        params.put("bodyScript",formBoList.getBodyScript());
        String topButtonHtml = getTopButtonHtml(formBoList.getTopBtnsJson(), params);
        params.put("topButtonColumns", JSONArray.parseArray(topButtonHtml));
        params.put("onlySelLeaf", StringUtils.isEmpty(formBoList.getOnlySelLeaf())?"NO":formBoList.getOnlySelLeaf());
        params.put("formAlias", formBoList.getFormAlias() == null ? "" : formBoList.getFormAlias());
        params.put("formAddAlias", formBoList.getFormAddAlias() == null ? "" : formBoList.getFormAddAlias());
        if(formBoList.getExpandLevel()!=null){
            String expandLevel = formBoList.getExpandLevel();
            if(expandLevel.equals("0")){
                expandLevel = "false";
            }
            else{
                int numberLevel = Integer.parseInt(expandLevel) - 1;
                expandLevel = String.valueOf(numberLevel);
            }
            params.put("expandLevel", expandLevel);
        }else {
            params.put("expandLevel", "false");
        }
        String html=freemarkEngine.mergeTemplateIntoString(templateName, params);
        return html;
    }

    /**
     * 判断是否存在。
     *
     * @param formBoList
     * @return
     */
    public boolean isExist(FormBoList formBoList) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("KEY_", formBoList.getKey());
        if (StringUtils.isNotEmpty(formBoList.getId())) {
            wrapper.ne("ID_", formBoList.getId());
        }
        Integer rtn = formBoListMapper.selectCount(wrapper);
        return rtn > 0;
    }

    public JsonPageResult executeInterface(FormBoList formBoList, Map<String,Object> params){
        String interfaceKey=formBoList.getInterfaceKey();
        JSONObject jsonObject=new JSONObject();
        if(StringUtils.isNotEmpty(interfaceKey)){
            JSONObject mappingJson = JSONObject.parseObject(formBoList.getInterfaceMappingJson());

            JSONArray pathParamsMapping = mappingJson.getJSONArray("pathParamsMapping");
            JSONArray headersMapping = mappingJson.getJSONArray("headersMapping");
            JSONArray queryMapping = mappingJson.getJSONArray("queryMapping");
            JSONArray bodyMapping = mappingJson.getJSONArray("bodyMapping");

            Map<String, Object> pathParamsMap = new HashMap<>(pathParamsMapping.size());
            if (pathParamsMapping != null) {
                parseMapping(pathParamsMapping, pathParamsMap, params);
            }
            Map<String, Object> headersMap = new HashMap<>(headersMapping.size());
            if (headersMapping != null) {
                parseMapping(headersMapping, headersMap, params);
            }
            Map<String, Object> queryMap = new HashMap<>(queryMapping.size());
            if (queryMapping != null) {
                parseMapping(queryMapping, queryMap, params);
            }
            Map<String, Object> bodyMap = new HashMap<>(bodyMapping.size());
            if (bodyMapping != null) {
                parseMapping(bodyMapping, bodyMap, params);
            }
            jsonObject.put("pathParams",pathParamsMap);
            jsonObject.put("headers",headersMap);
            jsonObject.put("querys",queryMap);
            jsonObject.put("bodys",bodyMap);
        }
        JsonResult result = sysInterfaceApiClient.executeApi(interfaceKey,jsonObject);
        if (result.isSuccess()) {
            JsonPageResult jsonPageResult = JsonPageResult.getSuccess("成功！");
            Map data = (Map)result.getData();
            Integer pageSize=Integer.parseInt((String)params.get("pageSize"));
            Integer pageNo=Integer.parseInt((String)params.get("pageNo"));
            IPage page =new Page(pageNo,pageSize,data.size());
            page.setRecords((List) data.get("result")).setTotal(Integer.parseInt(data.get("total").toString()));
            jsonPageResult.setPageData(page);
            return jsonPageResult;
        }
        return JsonPageResult.getFail(result.getMessage());
    }

    public JsonPageResult executeWebReq(FormBoList sysBoList, Map<String, Object> params) {
        String scriptText = sysBoList.getWebreqScript();
        String key = sysBoList.getWebreqKey();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("scriptText", scriptText);
        if(StringUtils.isNotEmpty(key)) {
            SysWebReqDefDto webReqDef = sysWebReqDefClient.getByAlias(key);
            JSONObject mappingJson = JSONObject.parseObject(sysBoList.getWebreqMappingJson());

            JSONArray headerMapping = mappingJson.getJSONArray("headerMapping");
            Map<String, Object> headerMap = new HashMap<>(headerMapping.size());
            if (headerMapping != null) {
                parseMapping(headerMapping, headerMap, params);
            }

            JSONArray paramsMapping = mappingJson.getJSONArray("paramsMapping");
            Map<String, Object> paramsMap = new HashMap<>(paramsMapping.size());
            if (paramsMapping != null) {
                parseMapping(paramsMapping, paramsMap, params);
            }
            String body = webReqDef.getTemp();
            body = StringUtils.format(body, params);

            jsonObject.put("key", key);
            jsonObject.put("header", JSONUtils.toJSONString(headerMap));
            jsonObject.put("params", JSONUtils.toJSONString(paramsMap));
            jsonObject.put("body", body);
        }
        JsonResult result = sysWebReqDefClient.executeScript(jsonObject);
        if (result.isSuccess()) {
            Object data = result.getData();
            String json = JSONUtils.toJSONString(data);
            return JSONObject.toJavaObject(JSONObject.parseObject(json), JsonPageResult.class);
        }
        return JsonPageResult.getFail(result.getMessage());
    }

    private void parseMapping(JSONArray mapping, Map<String, Object> temp, Map<String, Object> params) {
        for (Object obj : mapping) {
            JSONObject json = (JSONObject) obj;
            String key = json.getString("key");
            if(json.containsKey("children") && !json.getJSONArray("children").isEmpty()){
                Map<String,Object> map=new HashMap<>();
                parseMapping(json.getJSONArray("children"),map,params);
                if("root".equals(key)){
                    temp.putAll(map);
                }else {
                    temp.put(key, map);
                }
                continue;
            }
            String valSource = json.getString("valueSource");
            String valueDef = json.getString("value");
            String val = "";
            //传入参数
            if ("queryParam".equals(valSource) || "param".equals(valSource) ) {
                val = (String) params.get(key);
            }
            // 固定值
            else if ("fixedVar".equals(valSource)) {
                val = valueDef;
            }
            // 脚本
            else if ("script".equals(valSource)) {
                val = (String) groovyEngine.executeScripts(valueDef, params);
            }
            //常量
            else if ("constantVar".equals(valSource)) {
                val = (String) constVarContext.getValByKey(valueDef, params);
            }
            temp.put(key, val);
        }
    }


    public List<Map<String,Object>> importExcel(List<FormBoAttr> headers, MultipartFile mFile){
        //获取文件名
        String fileName = mFile.getOriginalFilename();
        if (!ExcelUtil.validateExcel(fileName)) {
            // 验证文件名是否合格
            return null;
        }
        boolean isXls = ExcelUtil.getExcelInfo(fileName);
        try {
            List<Map<String,Object>> dataList = createExcel(headers,mFile.getInputStream(), isXls);
            return dataList;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public List<Map<String,Object>> createExcel(List<FormBoAttr> headers, InputStream is, boolean isXls) {
        List<Map<String,Object>> dataList = new ArrayList<Map<String,Object>>();
        try{
            Workbook wb = null;
            if (isXls) {
                // 当excel是2003时,创建excel2003
                wb = new HSSFWorkbook(is);
            } else {
                // 当excel是2007时,创建excel2007
                wb = new XSSFWorkbook(is);
            }
            dataList = readExcelValue(wb,headers,is, isXls);// 读取Excel里面客户的信息
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dataList;
    }

    public List<Map<String,Object>> readExcelValue(Workbook wb,List<FormBoAttr> headers,InputStream is,boolean isXls){
        List<Map<String, Object>> dataList = new ArrayList<>();
        // 得到第一个shell
        Sheet sheet = wb.getSheetAt(0);
        // 得到Excel的行数
        int totalRows = sheet.getPhysicalNumberOfRows();
        // 得到Excel的列数(前提是有行数)
        int totalCells = 0;
        if (totalRows > 1 && sheet.getRow(0) != null) {
            totalCells = sheet.getRow(1).getPhysicalNumberOfCells();
        }

        //只读取数据行，标题行跟列头行忽略掉
        for (int r = 2; r < totalRows; r++) {
            Row row = sheet.getRow(r);
            if (row == null){
                continue;
            }
            Map<String, Object> map = handRow(headers, totalCells, row);
            // 添加到list
            dataList.add(map);
        }
        return dataList;
    }

    /**
     * 处理一行数据的读取。
     * @param headers
     * @param totalCells
     * @param row
     * @return
     */
    private Map<String, Object> handRow(List<FormBoAttr> headers, int totalCells, Row row) {
        Map<String,Object> map = new HashMap<>(totalCells);
        // 循环Excel的列
        for (int i = 0; i < totalCells; i++) {
            Cell cell = row.getCell(i);
            if(cell==null){
                continue;
            }
            //如果是纯数字,比如你写的是25,cell.getNumericCellValue()获得是25.0,通过截取字符串去掉.0获得25
            FormBoAttr attr = headers.get(i);
            if(attr==null){
                continue;
            }

            if("string".equals(attr.getDataType())) {
                cell.setCellType(CellType.STRING);
            }
            String value= ExcelUtil.getValueByCellType(cell);
            String field = attr.getFieldName();
            if("date".equals(attr.getDataType())){
                String formStr = StringUtils.isNotEmpty(attr.getFormat()) ? attr.getFormat().trim() : DateUtils.getCnDateStr(value);
                Date date = DateUtils.parseDate(value, formStr);
                map.put(field, date);
            }else {
                map.put(field, value);
            }

        }
        return map;
    }

    private String getPkField(FormBoEntity boEntity) {
        if (StringUtils.isNotEmpty(boEntity.getIdField())) {
            return boEntity.getIdField();
        }
        return FormBoEntity.FIELD_PK;
    }

    public Map<String, Object> insertData(List<Map<String,Object>> dataList,FormBoEntity boEnt, FormBoList formBoList){
        groovyEngine.clearVariables();
        String javaCode = null;
        String importDataHandler = null;
        String excelConfJson = formBoList.getExcelConfJson();
        if (StringUtils.isNotEmpty(excelConfJson)) {
            JSONObject jo = JSONObject.parseObject(excelConfJson);
            javaCode = jo.getString("javaCode");
            importDataHandler = jo.getString("importDataHandler");
        }


        JsonResult result = null;
        Script script = null;

        String tableName = boEnt.getTableName();
        Map<String, Object> map = new HashMap<>();
        long amountSuccess = 0 ;
        long amountFail = 0;
        String pkField = getPkField(boEnt);

        //执行对读取数据进行处理的脚本(java脚本)
        if (StringUtils.isNotEmpty(javaCode)) {
            script = groovyEngine.getScript(javaCode);
        }
        if(script != null){
            result = handBeforeInsertScript(script, javaCode, dataList);
            if (!result.isSuccess()) {
                amountFail = dataList.size();
                map.put(AMOUTSUCCESS, amountSuccess);
                map.put(AMOUTFAIL, amountFail);
                return map;
            }
        }

        //执行对读取数据进行处理的脚本(java接口)
        result = importDataHandlerExecutor.beforeInsertHandler(dataList, importDataHandler);
        if (!result.isSuccess()) {
            amountFail = dataList.size();
            map.put(AMOUTSUCCESS, amountSuccess);
            map.put(AMOUTFAIL, amountFail);
            return map;
        }

        int idx_ = 0;
        StringBuffer errorBuf = new StringBuffer();
        for(Map<String,Object> data:dataList){
            idx_+=1;
            //执行每一行进行判断处理的脚本(java脚本)
            if (script != null) {
                result = handRowScript(script, javaCode, data);
                if (!result.isSuccess()) {
                    amountFail++;
                    continue;
                }
            }


            //执行每一行进行判断处理的脚本(java接口)
            result = importDataHandlerExecutor.handRowHandler(data, importDataHandler);
            if (!result.isSuccess()) {
                amountFail++;
                continue;
            }


            StringBuffer keySb = new StringBuffer();
            StringBuffer valueSb = new StringBuffer();Map<String,Object> params=new HashMap<>(data.size()+2);
            try{
                keySb.append(pkField).append(",");
                valueSb.append("#{"+pkField+"},");
                params.put(pkField, IdGenerator.getIdStr());
                for(Map.Entry<String, Object> entry:data.entrySet()){
                    if(pkField.contains(entry.getKey())) {
                        params.put(pkField,entry.getValue());
                        continue;
                    }
                    keySb.append(entry.getKey()).append(",");
                    valueSb.append("#{").append(entry.getKey()).append("},");
                    params.put(entry.getKey(),entry.getValue());

                }
                if(!boEnt.external()){
                    if(keySb.indexOf(FormBoEntity.FIELD_CREATE_BY )==-1) {
                        keySb.append(FormBoEntity.FIELD_CREATE_BY ).append(",");
                        valueSb.append("#{"+FormBoEntity.FIELD_CREATE_BY +"},");
                        params.put(FormBoEntity.FIELD_CREATE_BY ,ContextUtil.getCurrentUserId());
                    }
                    if(keySb.indexOf(FormBoEntity.FIELD_CREATE_TIME )==-1) {
                        keySb.append(FormBoEntity.FIELD_CREATE_TIME).append(",");
                        valueSb.append("#{"+FormBoEntity.FIELD_CREATE_TIME+"},");
                        params.put(FormBoEntity.FIELD_CREATE_TIME,new Date());
                    }
                }
                if(keySb.length()>0){
                    keySb.deleteCharAt(keySb.length()-1);
                    valueSb.deleteCharAt(valueSb.length()-1);
                }
                String sql = "insert into "+tableName+" ("+keySb+") values ("+valueSb+")" ;
                commonDao.execute(boEnt.getDsAlias(),sql, params);
                amountSuccess++;
            }catch (Exception ex){
                ex.printStackTrace();
                if(errorBuf.length()>0){
                    errorBuf.append("，");
                }
                errorBuf.append(idx_);
            }
        }
        String errorStr=errorBuf.toString();
        if(StringUtils.isNotEmpty(errorStr)){
            errorStr=formBoList.getName()+"第"+errorStr+"条数据导入异常！";
        }
        map.put(ERRORSTR, errorStr);
        map.put(AMOUTSUCCESS, amountSuccess);
        map.put(AMOUTFAIL, amountFail);

        groovyEngine.clearVariables();

        return map;
    }

    @Override
    public int update(FormBoList entity) {
        int rtn= formBoListMapper.updateById(entity);
        return rtn;
    }

    public void importBoList(MultipartFile file, String treeId) {
        StringBuilder sb=new StringBuilder();
        sb.append("导入列表数据,导入列表如下:");
        JSONArray formBoListArray  = FormExOrImportHandler.readZipFile(file);
        String appId=formDataService.getAppIdByTreeId(treeId);

        for (Object obj:formBoListArray) {
            JSONObject BoListObj = (JSONObject)obj;
            JSONObject formBoList = BoListObj.getJSONObject("formBoList");
            if(BeanUtil.isNotEmpty(formBoList)){
                String formListStr = formBoList.toJSONString();
                FormBoList formNewList = JSONObject.parseObject(formListStr,FormBoList.class);
                sb.append(formNewList.getName() +"("+ formNewList.getId()+"),");

                String id = formNewList.getId();
                formNewList.setTreeId(treeId);
                formNewList.setAppId(appId);
                FormBoList oldList = get(id);
                if(BeanUtil.isNotEmpty(oldList)) {
                    //应用外，或应用ID相同才更新
                    if(StringUtils.isEmpty(appId) || appId.equals(oldList.getAppId())) {
                        update(formNewList);
                    }else{
                        formNewList.setId(IdGenerator.getIdStr());
                        insert(formNewList);
                    }
                }
                else{
                    insert(formNewList);
                }
            }
        }
        sb.append("导入到分类("+treeId +")下。");

        LogContext.put(Audit.DETAIL,sb);
    }


    /**
     * 根据配置的权限获取手机端列表按钮
     * @param btns
     */
    public JSONArray getMobileListBtns(JSONArray btns) {
        //当前用户信息
        Map<String, Set<String>> profiles =  orgService.getCurrentProfile();
        //新的按鈕
        JSONArray newBtns=new JSONArray();
        for (int i = 0; i <btns.size() ; i++) {
            JSONObject btn =  btns.getJSONObject(i);
            JSONObject permission = btn.getJSONObject("permission");
            if(BeanUtil.isNotEmpty(permission)){
                String value = permission.getString("value");
                boolean right=formPermissionService.hasRights(value,profiles);
                if(right){
                    newBtns.add(btn);
                }
            }
        }
        return newBtns;
    }



    /**
     * 处理对每一行进行判断处理的代码。
     * @param script
     * @param javaCode
     * @param data
     * @return
     */
    private JsonResult handRowScript(Script script, String javaCode, Map<String,Object> data) {
        JsonResult result = JsonResult.Success();
        if (script == null || javaCode.indexOf(HAND_ROW) == -1) {
            return result;
        }
        try {
            JsonResult obj = (JsonResult) script.invokeMethod(HAND_ROW, data);
            if (obj != null && obj instanceof JsonResult && ! obj.isSuccess()) {
                result.setMessage(obj.getMessage());
                result.setSuccess(false);
            }
        }
        catch (NoMethodException ex){
            log.error("没有指定handRow方法");
        }
        return  result;
    }

    /**
     * 执行对读取数据进行处理的脚本。
     * @param script
     * @param javaCode
     * @param data
     */
    private JsonResult handBeforeInsertScript(Script script, String javaCode, List<Map<String,Object>> data){
        JsonResult result = JsonResult.Success();
        if(script==null || javaCode.indexOf(BEFORE_INSERT)==-1){
            return result;
        }

        try {
            JsonResult obj = (JsonResult) script.invokeMethod(BEFORE_INSERT, data);
            if (obj != null && obj instanceof JsonResult && ! obj.isSuccess()) {
                result.setMessage(obj.getMessage());
                result.setSuccess(false);
            }
        }
        catch (NoMethodException ex){
            log.error("没有指定beforeInsert方法");
        }
        return  result;

    }

    public List<FormBoList> getListByIds(List<String> boListIds) {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.in("ID_", boListIds);
        return formBoListMapper.selectList(queryWrapper);
    }

    /**
     * 构建快捷查询参数
     * @param queryFilter
     * @param queryCondition
     */
    public void parseQueryCondition(QueryFilter queryFilter, String queryCondition) {
        if(StringUtils.isEmpty(queryCondition)){
            return;
        }
        JSONObject queryObj=JSONObject.parseObject(queryCondition);
        FieldLogic fieldLogic=queryFilter.getFieldLogic();
        FieldLogic queryFieldLogic=new FieldLogic();
        //设置AND或者OR
        queryFieldLogic.setLogic(queryObj.getString("condition"));
        //设置搜索条件
        queryFieldLogic.setWhereParams(parseQueryRules(queryObj.getJSONArray("rules")));
        fieldLogic.addParams(queryFieldLogic);
    }

    private List<WhereParam> parseQueryRules(JSONArray rules){
        List<WhereParam> list=new ArrayList<>();
        if(BeanUtil.isEmpty(rules)){
            return list;
        }
        for(Object obj:rules){
            JSONObject json=(JSONObject)obj;
            if(!json.containsKey("rules")){
                list.addAll(getQuickFieldValue(json));
            }else{
                FieldLogic queryFieldLogic=new FieldLogic();
                queryFieldLogic.setLogic(json.getString("condition"));
                queryFieldLogic.setWhereParams(parseQueryRules(json.getJSONArray("rules")));
                list.add(queryFieldLogic);
            }
        }
        return list;
    }

    private List<WhereParam> getQuickFieldValue(JSONObject json){
        List<WhereParam> list=new ArrayList<>();
        String fieldName=json.getString("fieldName");
        if(json.containsKey("tablePre") && StringUtils.isNotEmpty(json.getString("tablePre"))){
            fieldName=json.getString("tablePre")+"."+fieldName;
        }
        String dataType=json.getString("dataType");
        String fieldOp=json.getString("fieldOp");
        if(StringUtils.isEmpty(fieldOp)){
            fieldOp=QueryParam.OP_EQUAL;
        }
        QueryParam queryParam=new QueryParam();
        queryParam.setFieldName(fieldName);
        queryParam.setOpType(fieldOp);
        queryParam.setFieldType(dataType);
        if(QueryParam.OP_IS_NULL.equals(fieldOp) || QueryParam.OP_NOTNULL.equals(fieldOp)){
            queryParam.setValue("1");
            list.add(queryParam);
        }else{
            String fc=json.getString("fc");
            String format=json.getString("format");
            if(StringUtils.isEmpty(fc) || "textbox".equals(fc) || "select".equals(fc) || "treeselect".equals(fc) || "dialog".equals(fc)){
                String fieldValue=json.getString("fieldValue");
                if(StringUtils.isNotEmpty(fieldValue)) {
                    queryParam.setValue(fieldValue);
                    list.add(queryParam);
                }
            }else if("datepicker".equals(fc)){
                if(StringUtils.isEmpty(format)){
                    format=DateUtils.YYYY_MM_DD;
                }
                String fieldValue=json.getString("fieldValue");
                if(StringUtils.isNotEmpty(fieldValue)) {
                    queryParam.setValue(DateUtils.parseDateToStr(format,DateUtils.parseDate(fieldValue)));
                    list.add(queryParam);
                }
            }else if("month".equals(fc)){
                FieldLogic fieldLogic=new FieldLogic();
                QueryParam startQuery=BeanUtil.deepCopyBean(queryParam);
                startQuery.setOpType(QueryParam.OP_GREAT_EQUAL);
                QueryParam endQuery=BeanUtil.deepCopyBean(queryParam);
                endQuery.setOpType(QueryParam.OP_LESS_EQUAL);
                String fieldValue=json.getString("fieldValue");
                if(StringUtils.isNotEmpty(fieldValue)) {
                    Date date = DateUtils.parseDate(fieldValue);
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(date);
                    cal.set(Calendar.DAY_OF_MONTH, 1);
                    cal.roll(Calendar.DAY_OF_MONTH, -1);
                    Date lastDate = cal.getTime();
                    String startDate = DateUtils.parseDateToStr(DateUtils.YYYY_MM, lastDate) + "-01";
                    startQuery.setValue(startDate);
                    endQuery.setValue(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD,lastDate));
                    fieldLogic.getWhereParams().add(startQuery);
                    fieldLogic.getWhereParams().add(endQuery);
                    list.add(fieldLogic);
                }
            }else if("rangepicker".equals(fc)){
                FieldLogic fieldLogic=new FieldLogic();
                if(StringUtils.isEmpty(format)){
                    format=DateUtils.YYYY_MM_DD_HH_MM_SS;
                }
                QueryParam startQuery=BeanUtil.deepCopyBean(queryParam);
                startQuery.setOpType(QueryParam.OP_GREAT_EQUAL);
                QueryParam endQuery=BeanUtil.deepCopyBean(queryParam);
                endQuery.setOpType(QueryParam.OP_LESS_EQUAL);
                JSONArray dateAry=json.getJSONArray("fieldValue");
                if(BeanUtil.isNotEmpty(dateAry)) {
                    Date start = DateUtils.parseDate(dateAry.getString(0));
                    Date end = DateUtils.parseDate(dateAry.getString(1));
                    startQuery.setValue(DateUtils.parseDateToStr(format,start));
                    endQuery.setValue(DateUtils.parseDateToStr(format,end));
                    fieldLogic.getWhereParams().add(startQuery);
                    fieldLogic.getWhereParams().add(endQuery);
                    list.add(fieldLogic);
                }
            }
        }
        return list;
    }

    /**
     * 将列头的一些配置合并到返回字段列配置中
     * @param fieldsJson 返回字段列配置
     * @param colsJson 列头配置
     * @return
     */
    private String mergeColJsons(String fieldsJson,String colsJson){
        if(StringUtils.isEmpty(fieldsJson) || StringUtils.isEmpty(colsJson)){
            return fieldsJson;
        }
        JSONArray fieldsJsons=JSONArray.parseArray(fieldsJson);
        JSONArray colsJsons=JSONArray.parseArray(colsJson);
        for (int i = 0; i < fieldsJsons.size(); i++) {
            JSONObject fieldsObject = fieldsJsons.getJSONObject(i);
            for (int j = 0; j < colsJsons.size(); j++) {
                JSONObject colsObject = colsJsons.getJSONObject(j);
                if(fieldsObject.getString("field").equals(colsObject.getString("field"))){
                    //格式
                    fieldsObject.put("format",colsObject.getString("format"));
                    //渲染
//                    fieldsObject.put("renderType",colsObject.getString("renderType"));
//                    fieldsObject.put("renderConf",colsObject.getString("renderConf"));
                    break;
                }
            }
        }
        return fieldsJsons.toJSONString();
    }

    /**
     * 异步生成excel
     * 功能点：
     *  1、异步生成
     *  2、分页查询
     *  3、边查询边写入文件
     * @param user
     */
    @Async(value = "formExportExecutor")
    public void genExcel(JSONObject excelConfig, IUser user) {
        //设置当前用户
        ContextUtil.setCurrentUser(user);

        String queryDataStr=excelConfig.getString("queryDataStr");
        QueryData queryData = JSONObject.parseObject(queryDataStr,QueryData.class);

        String title=excelConfig.getString("title");
        FormExcelGenTask formExcelGenTask=new FormExcelGenTask();
        formExcelGenTask.setId(excelConfig.getString("fileId"));
        formExcelGenTask.setFileName(title+"."+"xlsx");
        //生成状态(0为失败、1为成功、2为正在生成)
        formExcelGenTask.setGenStatus("2");

        FormBoList formBoList = this.getByKey(excelConfig.getString("boListKey"));

        formExcelGenTask.setListId(formBoList.getId());
        formExcelGenTask.setListName(formBoList.getName());
        formExcelGenTask.setCreateByName(user.getFullName());
        formExcelGenTaskService.save(formExcelGenTask);
        log.info("*****************exportExce.genExcel is begin....:title={},userName={}",title,user.getFullName());
        List<List<String>> heads=excelConfig.getObject("heads",List.class);

        Integer totalRowCount=excelConfig.getInteger("totalRowCount");
        if(totalRowCount>1000000 || totalRowCount==0){
            log.info("*****************exportExce.genExcel is end....:title={},userName={},totalRowCount={}",title,user.getFullName(),totalRowCount);
            //不能超过每个工作表的总数一百万
            return;
        }



        Integer pageSize = excelConfig.getInteger("pageSize");
        //计算查询次数
        Integer queryNum = totalRowCount/pageSize;
        int remainder = totalRowCount % pageSize;
        if(remainder>0){
            queryNum+=1;
        }

        //计算进度值
        int firstNumber = (int) (queryNum * 0.25);
        int secondNumber = (int) (queryNum * 0.5);
        int thirdNumber = (int) (queryNum * 0.75);

        String fileId = IdGenerator.getIdStr();
        String path = SysFileUtil.getConfigKey("uploadPath")+"temporary\\"+fileId+"\\";
        String	fileName= path+fileId+".xlsx";
        //创建临时文件夹
        File dirFile = new File(path);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        log.info("*****************exportExce.genExcel is begin_2....:title={},userName={},path={}",title,user.getFullName(),path);
        //创建临时文件
        String extName="xlsx";
        ExcelWriter excelWriter = EasyExcel.write(fileName).build();
        log.info("*****************exportExce.genExcel is begin_3....:title={},userName={}",title,user.getFullName());
        try {
            WriteSheet writeSheet = EasyExcel.writerSheet("模板").head(heads).build();
            for (int rowIndex = 0; rowIndex < queryNum; rowIndex++) {
                queryData.setPageSize(pageSize);
                queryData.setPageNo(rowIndex + 1);
                QueryFilter filter = QueryFilterBuilder.createQueryFilter(queryData);
                List<List<Object>> data = this.getPageDataBySql(excelConfig,filter,formBoList);
                if(BeanUtil.isEmpty(data)){
                    continue;
                }
                excelWriter.write(data, writeSheet);

                //计算导出的进度条信息
                updatEPlannedSpeed(rowIndex,firstNumber,"25",formExcelGenTask);
                updatEPlannedSpeed(rowIndex,secondNumber,"50",formExcelGenTask);
                updatEPlannedSpeed(rowIndex,thirdNumber,"75",formExcelGenTask);

                filter=null;
                data=null;
            }
            excelWriter.finish();
            log.info("*****************exportExce.genExcel is end_1....:excelWriter.finish()  title={},userName={}",title,user.getFullName());
            //写入完成，进行读取文件
            File file = new File(fileName);
            MultipartFile multipartFile = FileUtil.getMultipartFile(file);

            //删除临时文件
            boolean value = file.delete();
            //删除临时文件夹
            dirFile.delete();

            //feign上传文件
            systemClient.uploadFile(multipartFile,fileName,fileId,false);
            log.info("*****************exportExce.genExcel is end_2....:feign上传文件完成  title={},userName={}",title,user.getFullName());
            formExcelGenTask.setFileId(fileId);
            formExcelGenTask.setGenStatus("100");
            formExcelGenTask.setRemark("生成成功");
        }catch (Exception e){
            log.info("*****************exportExce.genExcel is error:....:title={},userName={},message={}",title,user.getFullName(),ExceptionUtil.getExceptionMessage(e));
            formExcelGenTask.setGenStatus("0");
            formExcelGenTask.setRemark("生成失败");
        }finally {
            if(excelWriter!=null){
                excelWriter.finish();
            }
        }
        formExcelGenTaskService.update(formExcelGenTask);
        log.info("*****************exportExce.genExcel is end....:formExcelGenTaskService.update完成  title={},userName={}",title,user.getFullName());
    }

    private void updatEPlannedSpeed(int rowIndex,int updateNumber,String updateNumberStr,FormExcelGenTask formExcelGenTask){
        if(rowIndex == updateNumber) {
            formExcelGenTask.setGenStatus(updateNumberStr);
            formExcelGenTask.setRemark("导出完成" + updateNumberStr + "%");
            formExcelGenTaskService.update(formExcelGenTask);
        }
    }

    /**
     * 全部查询
     * @param excelConfig
     * @param filter
     * @param formBoList
     * @return
     */
    public List<List<Object>> getSqlDataBySql(JSONObject excelConfig,QueryFilter filter,FormBoList formBoList){
        List list = this.getDataBySql(excelConfig.getString("sql"), filter,"false");
        if(BeanUtil.isEmpty(list)){
            return new ArrayList<>();
        }
        List<List<Object>> data= renderColumn(excelConfig,list,formBoList);
        return data;
    }

    /**
     * 分页查询
     * @param excelConfig
     * @param filter
     * @param formBoList
     * @return
     */
    public List<List<Object>> getPageDataBySql(JSONObject excelConfig,QueryFilter filter,FormBoList formBoList){
        List list = this.getPageDataByDbAsAndSql(excelConfig.getString("dbAs"),excelConfig.getString("sql"), filter,"false");
        if(BeanUtil.isEmpty(list)){
            return new ArrayList<>();
        }
        List<List<Object>> data= renderColumn(excelConfig,list,formBoList);
        return data;
    }

    /**
     * 返回sql的查询结果
     * @param sql
     * @param filter
     * @return
     */
    public com.github.pagehelper.Page getPageDataByDbAsAndSql(String dbAs,String sql,QueryFilter filter,String isTest){
        com.github.pagehelper.Page page=commonDao.queryDynamicList(dbAs,sql, filter,null);
        log.debug("--FormBoListServiceImpl.getPageDataByDbAsAndSql is debug :--"+filter.getParams().get("sql"));
        if(MBoolean.YES.name().equals(isTest)){
            FormSqlLog formSqlLog=new FormSqlLog();
            formSqlLog.setType(FormSqlLog.TYPE_FORM_BO_LIST);
            formSqlLog.setSql((String)filter.getParams().get("sql"));
            String newSql=(String)filter.getParams().remove("sql");
            formSqlLog.setParams(JSONObject.toJSONString(filter.getParams()));
            formSqlLog.setIsSuccess(MBoolean.YES.name());
            formSqlLog.setRemark("--FormBoListServiceImpl.getPageDataByDbAsAndSql is debug :--"+newSql);
            formSqlLogService.insert(formSqlLog);
        }
        return page;
    }

    /**
     * 处理后端的数据格式化及展示的问题
     * @param excelConfig
     * @param list
     * @param formBoList
     * @return
     */
    public List<List<Object>> renderColumn(JSONObject excelConfig,List list,FormBoList formBoList){
        List<List<Object>> data = new ArrayList<>();
        try {
            List<List<String>> fields=excelConfig.getObject("fields",List.class);
            List<List<String>> showColumns=excelConfig.getObject("showColumns",List.class);
            //处理后端的数据格式化及展示的问题
            this.gdRenderColumn("false", formBoList, list,showColumns);
            SimpleDateFormat sf  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for (Object object : list) {
                Map<String, Object> map = (Map) object;
                List<Object> json = new ArrayList<>();
                for (List<String> header : fields) {
                    Object headerData = map.get(header.get(header.size() - 1).toUpperCase());
                    if (headerData instanceof Timestamp) {
                        Date d = new Date(((Timestamp) headerData).getTime());
                        json.add(sf.format(d));
                    } else {
                        //取出渲染list里面的参数并与当且header匹配
                        String curRowKey = header.get(1);
                        if(showColumns.contains(curRowKey)){
                            String column=null;
                            column = curRowKey + "_display";
                            if(map.get(column)==null){
                                column=curRowKey+"_render";
                            }
                            json.add(map.get(column));
                        }else {
                            json.add(headerData);
                        }
                    }
                    headerData=null;
                }
                data.add(json);
            }
        }catch (Exception e){
            log.error("---renderColumn() is error  message={}", ExceptionUtil.getExceptionMessage(e));
        }
        return data;
    }


    /**
     * 验证按钮前置条件。
     * @param config
     * @param rows
     * @return
     */
    public JsonResult validPreByButton(String config,JSONArray rows){
        JsonResult result=JsonResult.Success();
        JSONObject json=JSONObject.parseObject(config);
        Map<String,Object> vars=new HashMap<>();


        String mode = json.getString("mode");
        String script = "";
        if ("easy".equals(mode)) {
            if(BeanUtil.isEmpty(rows)){
                return JsonResult.Success();
            }
            JSONObject row=rows.getJSONObject(0);

            JSONArray easyTableData = json.getJSONArray("easyTableData");
            int idx = -1;
            for (int i = 0; i < easyTableData.size(); i++) {
                JSONObject easyField = easyTableData.getJSONObject(i);
                String formField = easyField.getString("formField");
                if (!row.containsKey(formField)) {
                    continue;
                }
                formField="row."+formField;
                String logic = (String) easyField.getOrDefault("logic", "AND");
                String op = (String) easyField.getOrDefault("op", "==");
                String type = easyField.getString("dataType");
                String value = easyField.getString("condition");
                if (!"Number".equals(type) && !"number".equals(type) && !"int".equals(type)) {
                    value = "\"" + value + "\"";
                }
                idx++;
                if (idx == 0) {
                    script += getSettingByOp(formField, op, value);
                    continue;
                }
                if ("AND".equals(logic)) {
                    script += " && " + getSettingByOp(formField, op, value);
                } else if ("OR".equals(logic)) {
                    script += " || " + getSettingByOp(formField, op, value);
                }
            }
            if(BeanUtil.isEmpty(script)){
                return JsonResult.Success();
            }

            vars.put("row",row);

            Object flag = groovyEngine.executeScripts(script, vars);
            if (flag instanceof Boolean && !(Boolean) flag) {
                String validMsg=json.getString("validMsg");
                validMsg=String.valueOf(groovyEngine.executeScripts(validMsg, vars));
                return JsonResult.Fail(validMsg).setShow(false);
            }

        } else if ("hard".equals(mode)) {

            Map<String,Object> contextData=new HashMap<>();
            IUser user=ContextUtil.getCurrentUser();
            contextData.put("curUserId",user.getUserId());
            contextData.put("curUserName",user.getFullName());
            contextData.put("account",user.getAccount());
            contextData.put("deptId",user.getDeptId());
            vars.put("context",contextData);

            vars.put("rows",rows);
            script = json.getString("javaCode");
            if(StringUtils.isEmpty(script)){
                return JsonResult.Success();
            }
            Object flag = groovyEngine.executeScripts(script, vars);
            if (flag instanceof Boolean && !(Boolean) flag) {
                String validMsg=json.getString("validMsg");
                validMsg=String.valueOf(groovyEngine.executeScripts(validMsg, vars));
                return JsonResult.Fail(validMsg).setShow(false);
            }
            return result;
        }
        return JsonResult.Success();

    }


    private String getSettingByOp(String key,String op,String value){
        if("include".equals(op)){
            return key+".indexOf("+value+")!=-1";
        }else if("notInclude".equals(op)){
            return key+".indexOf("+value+")==-1";
        }
        return key + op + value;
    }

}

