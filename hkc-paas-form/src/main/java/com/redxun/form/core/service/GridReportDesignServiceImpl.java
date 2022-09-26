
package com.redxun.form.core.service;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.GridHeader;
import com.redxun.common.base.entity.JsonPageResult;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.base.search.QueryFilter;
import com.redxun.common.constant.MBoolean;
import com.redxun.common.engine.FtlEngine;
import com.redxun.common.script.GroovyEngine;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.common.tool.StringUtils;
import com.redxun.constvar.ConstVarContext;
import com.redxun.datasource.DataSourceContextHolder;
import com.redxun.db.CommonDao;
import com.redxun.dto.sys.SysWebReqDefDto;
//import com.redxun.feign.SysInterfaceApiClient;
import com.redxun.feign.SysWebReqDefClient;
import com.redxun.form.core.entity.FormSolution;
import com.redxun.form.core.entity.FormSqlLog;
import com.redxun.form.core.entity.GridReportDesign;
import com.redxun.form.core.grid.column.MiniGridColumnRenderConfig;
import com.redxun.form.core.mapper.GridReportDesignMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * [demo]业务服务类
 */
@Service
public class GridReportDesignServiceImpl extends SuperServiceImpl<GridReportDesignMapper, GridReportDesign> implements BaseService<GridReportDesign> {

    @Resource
    private GridReportDesignMapper gridReportDesignMapper;

    @Resource
    SysWebReqDefClient sysWebReqDefClient;

/*    @Resource
    SysInterfaceApiClient sysInterfaceApiClient;*/

    @Resource
    FtlEngine freemarkEngine;

    @Resource
    GroovyEngine groovyEngine;

    @Resource
    ConstVarContext constVarContext;

    @Resource
    CommonDao commonDao;

    @Resource
    FormSqlLogServiceImpl formSqlLogService;

    @Resource
    MiniGridColumnRenderConfig miniGridColumnRenderConfig;

    @Override
    public BaseDao<GridReportDesign> getRepository() {
        return gridReportDesignMapper;
    }

    public String parseFreemarkSql(String useCondSql,String sql,Map<String,Object> params,String reportIncomeParams,QueryFilter queryFilter) throws Exception{
        Map<String,JSONObject> paramMap=new HashMap<>();
        if(StringUtils.isNotEmpty(reportIncomeParams)){
            JSONArray paramsArray=JSONArray.parseArray(reportIncomeParams);
            for(Object obj:paramsArray){
                JSONObject param=(JSONObject)obj;
                paramMap.put(param.getString("fieldName"),param);
            }
        }
        String newSql=null;
        Pattern pattern = Pattern.compile("#\\{([\\w]+)\\}");
        Matcher matcher = pattern.matcher(sql);
        while(matcher.find()) {
            String placeHolder = matcher.group(0);
            String key = matcher.group(1);
//            if(paramMap.containsKey(key)&&queryFilter!=null){
//                String colType=paramMap.get(key).getString("fc");
//                switch (colType){
//                    case "textbox":
//                        sql=sql.replace("#{"+key+"}","<#noparse> AND #{w."+key+"}</#noparse>");
//                        break;
//                    case "select":
//                        sql=sql.replace("#{"+key+"}","<#noparse> AND #{w."+key+"}</#noparse>");
//                        break;
//                    case "datepicker":
//                        sql=sql.replace("#{"+key+"}","<#noparse> AND #{w."+key+"}</#noparse>");
//                        break;
//                    case "rangepicker":
//                        sql=sql.replace("#{"+key+"}","<#noparse> AND #{w."+key+"}</#noparse>");
//                        break;
//                }
//                sql=sql.replace("#{"+key+"}","<#noparse> AND #{w."+key+"}</#noparse>");
//            }else
                sql=sql.replace("#{"+key+"}","<#noparse>#{w."+key+"}</#noparse>");
        }

        if (MBoolean.YES.val.equals(useCondSql)) {
            sql = freemarkEngine.parseByStringTemplate(params, sql);
            newSql = (String) groovyEngine.executeScripts(sql, params);
        } else {
            newSql = freemarkEngine.parseByStringTemplate(params, sql);
        }

        return newSql;
    }

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
    public Map<String,GridHeader> getGridHeaderMap(String colJsons){
        JSONArray columnArr=JSONArray.parseArray(colJsons);
        Map<String,GridHeader> headerMap=new HashMap<>(columnArr.size());
        genGridColumnHeaders(headerMap,columnArr);
        return headerMap;
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

    public String getValidSql(GridReportDesign gridReportDesign,Map<String,Object> params,Map<String,String> oriParams,String reportIncomeParams,QueryFilter queryFilter) throws Exception{

        String newSql=parseFreemarkSql(gridReportDesign.getUseCondSql(),
                MBoolean.YES.val.equals(gridReportDesign.getUseCondSql())?gridReportDesign.getCondSqls():gridReportDesign.getSql(),params,reportIncomeParams,queryFilter);
        return newSql;

    }

    public JsonPageResult executeWebReq(GridReportDesign gridReportDesign, Map<String, Object> params) {
        String scriptText = gridReportDesign.getWebReqScript();
        String key = gridReportDesign.getWebReqKey();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("scriptText", scriptText);
        if(StringUtils.isNotEmpty(key)) {
            SysWebReqDefDto webReqDef = sysWebReqDefClient.getByAlias(key);
            JSONObject mappingJson = JSONObject.parseObject(gridReportDesign.getWebReqMappingJson());

            JSONArray headerMapping = mappingJson.getJSONArray("headerMapping");
            Map<String, String> headerMap = new HashMap<>(headerMapping.size());
            if (headerMapping != null) {
                parseMapping(headerMapping, headerMap, params);
            }

            JSONArray paramsMapping = mappingJson.getJSONArray("paramsMapping");
            Map<String, String> paramsMap = new HashMap<>(paramsMapping.size());
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

/*
    public JsonPageResult executeInterface(GridReportDesign gridReportDesign, Map<String,Object> params){
        String interfaceKey=gridReportDesign.getInterfaceKey();
        JSONObject jsonObject=new JSONObject();
        if(StringUtils.isNotEmpty(interfaceKey)){
            JSONObject mappingJson = JSONObject.parseObject(gridReportDesign.getInterfaceMappingJson());

            JSONArray pathParamsMapping = mappingJson.getJSONArray("pathParamsMapping");
            JSONArray headersMapping = mappingJson.getJSONArray("headersMapping");
            JSONArray queryMapping = mappingJson.getJSONArray("queryMapping");
            JSONArray bodyMapping = mappingJson.getJSONArray("bodyMapping");

            Map<String, String> pathParamsMap = new HashMap<>(pathParamsMapping.size());
            if (pathParamsMapping != null) {
                parseMapping(pathParamsMapping, pathParamsMap, params);
            }
            Map<String, String> headersMap = new HashMap<>(headersMapping.size());
            if (headersMapping != null) {
                parseMapping(headersMapping, headersMap, params);
            }
            Map<String, String> queryMap = new HashMap<>(queryMapping.size());
            if (queryMapping != null) {
                parseMapping(queryMapping, queryMap, params);
            }
            Map<String, String> bodyMap = new HashMap<>(bodyMapping.size());
            if (bodyMapping != null) {
                parseMapping(bodyMapping, bodyMap, params);
            }
            jsonObject.put("pathParams",pathParamsMap);
            jsonObject.put("headers",headersMap);
            jsonObject.put("querys",queryMap);
            jsonObject.put("bodys",bodyMap);
        }
        JsonResult result = sysInterfaceApiClient.executeApi(interfaceKey,jsonObject.toJSONString());
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
*/

    public List getDataBySql(String alias,String sql, QueryFilter filter, String isTest) {
        List list= commonDao.queryForList(alias,sql, filter, null);
        list = this.parseListDate(list);
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

    //日期转字符串
    private List parseListDate(List<Map<String,Object>> list){
        List newList = new ArrayList();
        //更换时间戳为date字符串
        for(Map<String,Object> item: list){
            for(Map.Entry<String,Object> entry : item.entrySet()){
                if(entry.getValue() instanceof Timestamp){
                    Date date = (Date) entry.getValue();
                    item.put(entry.getKey(), date.toString());
                }
            }
            newList.add(item);
        }
        return newList;
    }

    private void parseMapping(JSONArray mapping, Map<String, String> temp, Map<String, Object> params) {
        for (Object obj : mapping) {
            JSONObject json = (JSONObject) obj;
            String key = json.getString("key");
            if(json.containsKey("children") && !json.getJSONArray("children").isEmpty()){
                Map<String,String> map=new HashMap<>();
                parseMapping(json.getJSONArray("children"),map,params);
                temp.put(key,map.toString());
                continue;
            }
            String valSource = json.getString("valueSource");
            String valueDef = json.getString("value");
            String val = "";
            //传入参数
            if ("queryParam".equals(valSource)) {
                val = (String) params.get(valueDef);
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
    /**
     * 判断报表是否存在。
     * @param gridReportDesign
     * @return
     */
    public boolean isExist(GridReportDesign gridReportDesign){
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("KEY_",gridReportDesign.getKey());
        if(StringUtils.isNotEmpty( gridReportDesign.getId())){
            queryWrapper.ne("ID_",gridReportDesign.getId());
        }
        int count=gridReportDesignMapper.selectCount(queryWrapper);

        return count>0;
    }
}
