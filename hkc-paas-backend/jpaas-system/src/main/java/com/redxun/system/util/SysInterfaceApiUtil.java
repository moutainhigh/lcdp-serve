package com.redxun.system.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.constant.MBoolean;
import com.redxun.common.http.HttpFactory;
import com.redxun.common.http.HttpRtnModel;
import com.redxun.common.http.IHttp;
import com.redxun.common.script.GroovyEngine;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ExceptionUtil;
import com.redxun.common.utils.SpringUtil;
import com.redxun.constvar.ConstVarContext;
import com.redxun.system.core.entity.SysHttpTask;
import com.redxun.system.core.entity.SysHttpTaskLog;
import com.redxun.system.core.entity.SysInterfaceApi;
import com.redxun.system.core.entity.SysInterfaceProject;
import com.redxun.system.core.service.SysHttpTaskLogServiceImpl;
import com.redxun.system.core.service.SysInterfaceApiServiceImpl;
import com.redxun.system.core.service.SysInterfaceProjectServiceImpl;
import com.redxun.system.ext.handler.ApiReturnDataHandler;
import groovy.lang.Script;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

/**
 * 调用接口API工具类
 */
@Slf4j
public class SysInterfaceApiUtil {

    /**
     * 无
     */
    private static final String NONE = "none";
    /**
     * 常量
     */
    private static final String CONSTANT = "constantVar";
    /**
     * 脚本
     */
    private static final String SCRIPT = "script";
    /**
     * 固定值
     */
    private static final String FIXED_VAR = "fixedVar";
    /**
     * 传入参数
     */
    private static final String PARAM = "param";
    /**
     * xml语法
     */
    private static final String XML_PARSE = "xmlParse";
    /**
     * json语法
     */
    private static final String JSON_PARSE = "jsonParse";

    /**
     * 调用API接口
     * @param apiId             apiId 接口定义ID
     * @param pathParams
     * @param headers
     * @param querys
     * @param bodys
     * @param isTest
     * @param batId
     * @param params
     * @return
     */
    public static JsonResult executeApi(String apiId, Map<String,Object> pathParams, Map<String,Object> headers, Map<String,Object> querys,Object bodys, boolean isTest,String batId,String params) throws Exception {
        SysInterfaceApiServiceImpl sysInterfaceApiService = SpringUtil.getBean(SysInterfaceApiServiceImpl.class);
        SysInterfaceProjectServiceImpl sysInterfaceProjectService = SpringUtil.getBean(SysInterfaceProjectServiceImpl.class);
        SysHttpTaskLogServiceImpl sysHttpTaskLogService = SpringUtil.getBean(SysHttpTaskLogServiceImpl.class);
        HttpFactory httpFactory = SpringUtil.getBean(HttpFactory.class);

        SysInterfaceApi sysInterfaceApi = sysInterfaceApiService.get(apiId);
        if (sysInterfaceApi == null) {
            return JsonResult.getFailResult("此接口不存在，无法调用！");
        }
        SysInterfaceProject sysInterfaceProject = sysInterfaceProjectService.get(sysInterfaceApi.getProjectId());
        if (!isTest && MBoolean.NO.name().equals(sysInterfaceProject.getStatus())) {
            return JsonResult.getFailResult("此接口所属项目未启动，无法调用！");
        }
        if (!isTest && SysInterfaceApi.STATUS_UNDONE.equals(sysInterfaceApi.getStatus())) {
            return JsonResult.getFailResult("接口配置禁用状态，无法调用！");
        }
        //restful/soap
        String apiType = sysInterfaceApi.getApiType();
        //get/post/put/delete
        String apiMethod = sysInterfaceApi.getApiMethod();
        //form/json/xml
        String apiDataType = sysInterfaceApi.getApiDataType();
        String apiBody = sysInterfaceApi.getApiBody();

        //解析请求路径参数
        JsonResult paramsResult = parseApiParamsMap(sysInterfaceApi.getApiPathParams(), pathParams);
        if (!paramsResult.isSuccess()) {
            return paramsResult;
        }
        Map<String, Object> pathParamMap = (Map<String, Object>) paramsResult.getData();
        //解析请求参数
        paramsResult = parseApiParamsMap(sysInterfaceApi.getApiQuery(), querys);
        if (!paramsResult.isSuccess()) {
            return paramsResult;
        }
        Object queryMap = paramsResult.getData();
        //解析请求头参数
        paramsResult = parseApiParamsMap(sysInterfaceApi.getApiHeaders(), headers);
        if (!paramsResult.isSuccess()) {
            return paramsResult;
        }
        Map<String, String> headerMap = (Map<String, String>) paramsResult.getData();
        //解析全局请求头参数
        paramsResult=parseGlobalParamsMap(sysInterfaceProject.getGlobalHeaders(),new HashMap<>());
        if(!paramsResult.isSuccess()){
            return paramsResult;
        }
        Map<String, String> globalHeaderMap = (Map<String, String>) paramsResult.getData();
        headerMap.putAll(globalHeaderMap);

        //接口完整路径
        String fullPath = sysInterfaceProject.getDomainFullPath();
        String url = fullPath + sysInterfaceApi.getApiPath();
        // 格式化URL
        url = StringUtils.format(url, pathParamMap);
        //使用HttpClient方式
        IHttp httpClient = httpFactory.getHttpByHttpClient();
        JsonResult result = new JsonResult();
        SysHttpTaskLog log = new SysHttpTaskLog();
        HttpRtnModel htm = new HttpRtnModel();
        String type = "SOAP".equals(apiType) ? "POST" : apiMethod;
        //判断格式是否正确
        JsonResult bodyResult = new JsonResult();
        if (SysInterfaceApi.DATA_TYPE_FORM.equals(apiDataType)) {
            Map<String, Object> bodysMap = (JSONObject.parseObject(bodys.toString())).getInnerMap();
            bodyResult = parseApiParamsMap(apiBody, bodysMap);
            if (!bodyResult.isSuccess()) {
                return bodyResult;
            }
        } else if (SysInterfaceApi.DATA_TYPE_JSON.equals(apiDataType)) {
            JSONObject json = JSONArray.parseArray(apiBody).getJSONObject(0);
            String paramType = json.getString("paramType");
            String paramValue = json.getString("paramValue");
            String paramName = json.getString("paramName");
            String paramSource = json.getString("paramSource");
            ReadContext context = JsonPath.parse(bodys);
            if ("array".equals(paramType) || "object".equals(paramType)) {
                bodys = getValueByJsonPath(context, "$", json, paramType);
            } else {
                if (NONE.equals(paramSource)) {
                    paramValue = "$." + paramName;
                }
                bodys = getValueByJsonPath(context, paramValue, json, paramType);
            }
        }
        log.setTaskId(batId).setLogUrl(url)
                .setLogHeaders(JSONArray.toJSONString(headerMap))
                .setLogQuery(JSONArray.toJSONString(queryMap));
        try {
            //接口开始调用
            Long start = System.currentTimeMillis();
            switch (type.toUpperCase()) {
                case "GET":
                    htm = httpClient.getFromUrl(url, headerMap, (Map<String, String>) queryMap);
                    break;
                case "POST":
                    if (SysInterfaceApi.DATA_TYPE_FORM.equals(apiDataType)) {
                        Map<String, String> bodyMap = (Map<String, String>) bodyResult.getData();
                        log.setLogBody(JSONArray.toJSONString(bodyMap));
                        htm = httpClient.postFromUrl(url, headerMap, (Map<String, String>) queryMap, bodyMap);
                    } else if (SysInterfaceApi.DATA_TYPE_JSON.equals(apiDataType)) {
                        String bodyJsonStr = "";
                        if (SysInterfaceApi.DATA_TYPE_FORM.equals(apiDataType)) {
                            bodyJsonStr = JSONObject.toJSONString(bodyResult.getData());
                        } else if (SysInterfaceApi.DATA_TYPE_JSON.equals(apiDataType)) {
                            bodyJsonStr = bodys.toString();
                        }
                        log.setLogBody(bodyJsonStr);
                        htm = httpClient.postFromUrl(url, headerMap, (Map<String, String>) queryMap, bodyJsonStr);
                    } else if (SysInterfaceApi.DATA_TYPE_XML.equals(apiDataType)) {
                        apiBody = StringUtils.format(apiBody, (Map<String, Object>) queryMap);
                        log.setLogBody(apiBody);
                        htm = httpClient.postFromUrl(url, headerMap, new HashMap<>(), apiBody);
                    }
                    break;
                case "PUT":
                    if (SysInterfaceApi.DATA_TYPE_FORM.equals(apiDataType)) {
                        Map<String, String> bodyMap = (Map<String, String>) bodyResult.getData();
                        log.setLogBody(JSONArray.toJSONString(bodyMap));
                        htm = httpClient.putFromUrl(url, headerMap, (Map<String, String>) queryMap, bodyMap);
                    } else if (SysInterfaceApi.DATA_TYPE_JSON.equals(apiDataType)) {
                        String bodyJsonStr = "";
                        if (SysInterfaceApi.DATA_TYPE_FORM.equals(apiDataType)) {
                            bodyJsonStr = JSONObject.toJSONString(bodyResult.getData());
                        } else if (SysInterfaceApi.DATA_TYPE_JSON.equals(apiDataType)) {
                            bodyJsonStr = bodys.toString();
                        }
                        log.setLogBody(bodyJsonStr);
                        htm = httpClient.putFromUrl(url, headerMap, (Map<String, String>) queryMap, bodyJsonStr);
                    } else if (SysInterfaceApi.DATA_TYPE_XML.equals(apiDataType)) {
                        apiBody = StringUtils.format(apiBody, (Map<String, Object>) queryMap);
                        log.setLogBody(apiBody);
                        htm = httpClient.putFromUrl(url, headerMap, new HashMap<>(), apiBody);
                    }
                    break;
                case "DELETE":
                    if (SysInterfaceApi.DATA_TYPE_FORM.equals(apiDataType)) {
                        Map<String, String> bodyMap = (Map<String, String>) bodyResult.getData();
                        log.setLogBody(JSONArray.toJSONString(bodyMap));
                        htm = httpClient.deleteFromUrl(url, headerMap, (Map<String, String>) queryMap, bodyMap);
                    } else if (SysInterfaceApi.DATA_TYPE_JSON.equals(apiDataType)) {
                        String bodyJsonStr = "";
                        if (SysInterfaceApi.DATA_TYPE_FORM.equals(apiDataType)) {
                            bodyJsonStr = JSONObject.toJSONString(bodyResult.getData());
                        } else if (SysInterfaceApi.DATA_TYPE_JSON.equals(apiDataType)) {
                            bodyJsonStr = bodys.toString();
                        }
                        log.setLogBody(bodyJsonStr);
                        htm = httpClient.deleteFromUrl(url, headerMap, (Map<String, String>) queryMap, bodyJsonStr);
                    } else if (SysInterfaceApi.DATA_TYPE_XML.equals(apiDataType)) {
                        apiBody = StringUtils.format(apiBody, (Map<String, Object>) queryMap);
                        log.setLogBody(apiBody);
                        htm = httpClient.deleteFromUrl(url, headerMap, new HashMap<>(), apiBody);
                    }
                    break;
                default:
                    result = new JsonResult();
                    break;
            }
            Long end = System.currentTimeMillis();
            Long timeConsuming = (end - start);
            log.setTimeConsuming(String.valueOf(timeConsuming));
            log.setResponseState(String.valueOf(htm.getStatusCode()));
            if (htm.getStatusCode() == 200) {
                log.setResult("1");
                result.setSuccess(true);
                Object returnData = parseApiParamsReturn(sysInterfaceApi, sysInterfaceApi.getApiReturnFields(), htm.getContent());
                log.setResponseData(returnData.toString());
                result.setData(returnData);
            } else {
                log.setResult("0");
                log.setErrorMessage(htm.getContent());
                throw new RuntimeException(JSONObject.toJSONString(log));
            }
        } catch (Exception e) {
            log.setResult("0");
            log.setResponseState("500");
            log.setErrorMessage(ExceptionUtil.getExceptionMessage(e));
            throw new RuntimeException(JSONObject.toJSONString(log));
        }
        if(MBoolean.YES.name().equals(sysInterfaceApi.getIsLog())) {
            sysHttpTaskLogService.createLog(batId, SysHttpTask.TYPE_INTERFACE, apiId,
                    sysInterfaceProject.getProjectName() + "/" + sysInterfaceApi.getApiName(),
                    "sysInterfaceApiServiceImpl", "executeApi", log, true, apiId, params);
        }
        return result;
    }

    /**
     * 解析返回结果
     * @param sysInterfaceApi
     * @param apiParams
     * @param content
     * @return
     */
    private static Object parseApiParamsReturn(SysInterfaceApi sysInterfaceApi,String apiParams,String content) throws Exception{
        if(StringUtils.isEmpty(content)){
            return new JSONObject();
        }
        String result=getReturnResult(sysInterfaceApi,content);

        log.info("___________"+result);
        //return result;
        return getReturnObject(sysInterfaceApi,apiParams,result);
    }

    /**
     * 根据数据处理类型处理返回结果
     * @param sysInterfaceApi
     * @param content
     * @return
     */
    private static String getReturnResult(SysInterfaceApi sysInterfaceApi,String content){
        String result=content;
        //none/script/bean
        String javaType=sysInterfaceApi.getJavaType();
        if(SysInterfaceApi.JAVA_TYPE_SCRIPT.equals(javaType)){
            GroovyEngine groovyEngine=SpringUtil.getBean(GroovyEngine.class);
            //脚本
            String javaCode=sysInterfaceApi.getJavaCode();
            Script script = null;
            if (StringUtils.isNotEmpty(javaCode)) {
                script = groovyEngine.getScript(javaCode);
            }
            if(script!=null && javaCode.indexOf("analysisText")!=-1) {
                result = (String)script.invokeMethod("analysisText", content);
            }
        }else if(SysInterfaceApi.JAVA_TYPE_BEAN.equals(javaType)){
            //处理器
            String javaBean=sysInterfaceApi.getJavaBean();
            ApiReturnDataHandler apiReturnDataHandler=SpringUtil.getBean(javaBean);
            if(apiReturnDataHandler!=null) {
                result = apiReturnDataHandler.analysisText(content);
            }
        }
        return result;
    }

    /**
     * 返回结果json
     * @param sysInterfaceApi
     * @param apiParams
     * @param result
     * @return
     * @throws Exception
     */
    private static Object getReturnObject(SysInterfaceApi sysInterfaceApi,String apiParams,String result) throws Exception {
        JSONArray apiParamArray = JSONArray.parseArray(apiParams);
        //none/json/xml
        String apiReturnType = sysInterfaceApi.getApiReturnType();
        JSONObject json = apiParamArray.getJSONObject(0);
        String paramType = json.getString("paramType");
        String paramValue = json.getString("paramValue");
        String paramName = json.getString("paramName");
        String paramSource= json.getString("paramSource");
        if (SysInterfaceApi.RETURN_TYPE_JSON.equals(apiReturnType)) {
            ReadContext context = JsonPath.parse(result);
            if ("array".equals(paramType) || "object".equals(paramType)) {
                try{
                    return getValueByJsonPath(context,"$",json,paramType);
                }catch (Exception e) {
                    return new JSONArray();
                }
            } else {
                if (NONE.equals(paramSource)) {
                    paramValue = "$." + paramName;
                }
                try{
                    return getValueByJsonPath(context, paramValue, json,paramType);
                }catch (Exception e) {
                    return new JSONObject();
                }
            }
        } else if (SysInterfaceApi.RETURN_TYPE_XML.equals(apiReturnType)) {
            StringReader sr = new StringReader(result);
            InputSource is = new InputSource(sr);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(is);
            XPathFactory xFactory = XPathFactory.newInstance();
            XPath xpath = xFactory.newXPath();
            if ("array".equals(paramType) || "object".equals(paramType)) {
                try{
                    return getValueByXpath(xpath, doc, "", json, paramType);
                }catch (Exception e) {
                    return new JSONArray();
                }
            } else {
                if (NONE.equals(paramSource)) {
                    paramValue = "/" + paramName;
                }
                try {
                    return getValueByXpath(xpath, doc, paramValue, json, paramType);
                }catch (Exception e) {
                    return new JSONObject();
                }
            }
        } else {
            try {
                return getValueByNone(result, json);
            }catch (Exception e){
                return "";
            }
        }
    }

    /**
     * 获取none的值
     * @param result
     * @param json
     * @return
     */
    private static Object getValueByNone(String result,JSONObject json) throws Exception{
        String paramType=json.getString("paramType");
        if("number".equals(paramType)){
            return Double.parseDouble(result);
        }else if("boolean".equals(paramType)){
            return Boolean.valueOf(result);
        }else if("array".equals(paramType)){
            JSONArray array=new JSONArray();
            JSONObject childJson=json.getJSONArray("children").getJSONObject(0);
            try {
                array.add(getValueByNone(result,childJson));
            }catch (Exception e){
            }
            return array;
        }else if("object".equals(paramType)){
            JSONObject jsonObject=new JSONObject();
            if(!json.getJSONArray("children").isEmpty()){
                for(Object obj:json.getJSONArray("children")) {
                    JSONObject childJson = (JSONObject) obj;
                    String childName = childJson.getString("paramName");
                    String childSource = childJson.getString("paramSource");
                    try {
                        if (NONE.equals(childSource)) {
                            jsonObject.put(childName, getValueByNone(result, childJson));
                        } else {
                            jsonObject.put(childName, getValue(new HashMap<>(), childJson));
                        }
                    } catch (Exception e) {
                    }
                }
            }else{
                jsonObject= JSONObject.parseObject(result);
            }

            return jsonObject;
        }else{
            return result;
        }
    }

    /**
     * 获取json对象中的boolean值
     * @param json
     * @param name
     * @return
     */
    private static boolean getJsonBooleanByName(JSONObject json,String name){
        return json.containsKey(name) && json.getBoolean(name);
    }

    /**
     * 获取jsonpath的值
     * @param context
     * @param paramValue
     * @param json
     * @param parentType
     * @return
     */
    private static Object getValueByJsonPath(ReadContext context,String paramValue,JSONObject json,String parentType){
        String paramType=json.getString("paramType");
        if("number".equals(paramType)){
            return context.read(paramValue,Number.class);
        }else if("boolean".equals(paramType)){
            return context.read(paramValue,Boolean.class);
        }else if ("array".equals(paramType)) {
            JSONArray array=new JSONArray();
            JSONObject childJson=json.getJSONArray("children").getJSONObject(0);
            String parentName=json.getString("paramName");
            String parentValue=json.getString("paramValue");
            String parentSource=json.getString("paramSource");
            if(!"object".equals(parentType)) {
                if ((PARAM.equals(parentSource) || NONE.equals(parentSource)) && !getJsonBooleanByName(json,"isRoot") && !getJsonBooleanByName(json,"isChild")) {
                    paramValue = paramValue + "." + parentName;
                } else if (JSON_PARSE.equals(parentSource)) {
                    paramValue = paramValue + parentValue;
                }
            }
            Number length = context.read(paramValue + ".length()", Number.class);
            for (int i = 0; i < length.intValue(); i++) {
                String childName = childJson.getString("paramName");
                String childValue = childJson.getString("paramValue");
                String childSource = childJson.getString("paramSource");
                String childType = childJson.getString("paramType");
                try {
                    if(childJson.containsKey("isChild") && childJson.getBoolean("isChild")
                            || (!"number".equals(childType) && !"boolean".equals(childType) && !"string".equals(childType))) {
                        array.add(getValueByJsonPath(context, paramValue + "[" + i + "]", childJson, paramType));
                    }else {
                        if (PARAM.equals(childSource) || NONE.equals(childSource)) {
                            array.add(getValueByJsonPath(context, paramValue + "[" + i + "]." + childName, childJson, paramType));
                        } else if (JSON_PARSE.equals(childSource)) {
                            array.add(getValueByJsonPath(context, paramValue + "[" + i + "]" + childValue, childJson, paramType));
                        }
                    }
                } catch (Exception e) {
                }
            }
            return array;
        } else if ("object".equals(paramType)) {
            JSONObject jsonObject=new JSONObject();
            String parentName = json.getString("paramName");
            String parentValue = json.getString("paramValue");
            String parentSource = json.getString("paramSource");
            if(!"object".equals(parentType)) {
                if ((PARAM.equals(parentSource) || NONE.equals(parentSource)) && !getJsonBooleanByName(json,"isRoot") && !getJsonBooleanByName(json,"isChild")) {
                    paramValue = paramValue + "." + parentName;
                } else if (JSON_PARSE.equals(parentSource)) {
                    paramValue = paramValue + parentValue;
                }
            }
            for(Object obj:json.getJSONArray("children")) {
                JSONObject childJson = (JSONObject) obj;
                String childName=childJson.getString("paramName");
                String childValue=childJson.getString("paramValue");
                String childSource=childJson.getString("paramSource");
                String childType = childJson.getString("paramType");
                try {
                    if(JSON_PARSE.equals(childSource)){
                        jsonObject.put(childName,getValueByJsonPath(context,paramValue+childValue,childJson,paramType));
                    }else if(PARAM.equals(childSource) || NONE.equals(childSource) || "array".equals(childType) || "object".equals(childType)){
                        jsonObject.put(childName,getValueByJsonPath(context,paramValue+"."+childName,childJson,paramType));
                    }else{
                        jsonObject.put(childName,getValue(new HashMap<>(),childJson));
                    }
                } catch (Exception e) {
                }
            }
            return jsonObject;
        } else {
            return context.read(paramValue, Object.class);
        }
    }


    /**
     * 获取xpath的值
     * @param xpath
     * @param doc
     * @param paramValue
     * @param json
     * @param parentType
     * @return
     * @throws Exception
     */
    private static Object getValueByXpath(XPath xpath,Document doc,String paramValue,JSONObject json,String parentType) throws Exception{
        String paramType=json.getString("paramType");
        if ("number".equals(paramType)) {
            return xpath.evaluate(paramValue,doc,XPathConstants.NUMBER);
        } else if ("boolean".equals(paramType)) {
            return xpath.evaluate(paramValue,doc, XPathConstants.BOOLEAN);
        }else if ("array".equals(paramType)) {
            JSONArray array=new JSONArray();
            JSONObject childJson=json.getJSONArray("children").getJSONObject(0);
            String parentName=json.getString("paramName");
            String parentValue=json.getString("paramValue");
            String parentSource=json.getString("paramSource");
            if(!"object".equals(parentType)) {
                if ((PARAM.equals(parentSource) || NONE.equals(parentSource)) && !getJsonBooleanByName(json,"isRoot") && !getJsonBooleanByName(json,"isChild")) {
                    paramValue = paramValue + "/" + parentName;
                } else if (XML_PARSE.equals(parentSource)) {
                    paramValue = paramValue + parentValue;
                }
            }
            Number length=(Number)xpath.evaluate("count("+paramValue+")",doc, XPathConstants.NUMBER);
            for(int i=1;i<=length.intValue();i++){
                String childName=childJson.getString("paramName");
                String childValue=childJson.getString("paramValue");
                String childSource=childJson.getString("paramSource");
                String childType=childJson.getString("paramType");
                try {
                    if(childJson.containsKey("isChild") && childJson.getBoolean("isChild")
                            || (!"number".equals(childType) && !"boolean".equals(childType) && !"string".equals(childType))) {
                        array.add(getValueByXpath(xpath, doc, paramValue + "[" + i + "]", childJson, paramType));
                    }else{
                        if (PARAM.equals(childSource) || NONE.equals(childSource)) {
                            array.add(getValueByXpath(xpath, doc, paramValue + "[" + i + "]/" + childName, childJson, paramType));
                        } else if (XML_PARSE.equals(childSource)) {
                            array.add(getValueByXpath(xpath, doc, paramValue + "[" + i + "]" + childValue, childJson, paramType));
                        }
                    }
                }catch (Exception e){
                }
            }
            return array;
        } else if ("object".equals(paramType)) {
            JSONObject jsonObject=new JSONObject();
            String parentName = json.getString("paramName");
            String parentValue = json.getString("paramValue");
            String parentSource = json.getString("paramSource");
            if(!"object".equals(parentType)) {
                if ((PARAM.equals(parentSource) || NONE.equals(parentSource)) && !getJsonBooleanByName(json,"isRoot") && !getJsonBooleanByName(json,"isChild")) {
                    paramValue = paramValue + "/" + parentName;
                } else if (XML_PARSE.equals(parentSource)) {
                    paramValue = paramValue + parentValue;
                }
            }
            for(Object obj:json.getJSONArray("children")) {
                JSONObject childJson = (JSONObject) obj;
                String childName=childJson.getString("paramName");
                String childValue=childJson.getString("paramValue");
                String childSource=childJson.getString("paramSource");
                String childType = childJson.getString("paramType");
                try {
                    if(XML_PARSE.equals(childSource)){
                        jsonObject.put(childName, getValueByXpath(xpath, doc, paramValue + childValue, childJson, paramType));
                    }else if(PARAM.equals(childSource) || NONE.equals(childSource) || "array".equals(childType) || "object".equals(childType)){
                        jsonObject.put(childName, getValueByXpath(xpath, doc, paramValue + "/" + childName, childJson, paramType));
                    }else{
                        jsonObject.put(childName, getValue(new HashMap<>(), childJson));
                    }
                } catch (Exception e) {
                }
            }
            return jsonObject;
        } else {
            return xpath.evaluate(paramValue,doc, XPathConstants.STRING);
        }
    }

    private static JsonResult parseGlobalParamsMap(String globalParams,Map<String,Object> params){
        Map<String, String> mapData=new HashMap<>();
        if(StringUtils.isEmpty(globalParams)){
            return JsonResult.getSuccessResult(mapData);
        }
        JSONArray apiParamArray=JSONArray.parseArray(globalParams);
        for(Object obj:apiParamArray){
            JSONObject json=(JSONObject)obj;
            String paramName=json.getString("key");
            String paramValue=json.getString("value");
            String paramSource=json.getString("valueSource");
            JSONObject param=new JSONObject();
            param.put("paramName",paramName);
            param.put("paramValue",paramValue);
            param.put("paramSource",paramSource);

            String value="";
            try {
                value=(String)getValue(params,param);
            }catch (Exception e){
            }
            mapData.put(paramName,value);
        }
        return JsonResult.getSuccessResult(mapData);
    }
    /**
     * 解析MAP对象
     * @param apiParams
     * @param params
     * @return
     */
    private static JsonResult parseApiParamsMap(String apiParams,Map<String,Object> params) {
        Map<String, String> mapData=new HashMap<>();
        if(StringUtils.isEmpty(apiParams)){
            return JsonResult.getSuccessResult(mapData);
        }
        JSONArray apiParamArray=JSONArray.parseArray(apiParams);
        for(Object obj:apiParamArray){
            JSONObject json=(JSONObject)obj;

            JSONArray subJsonArray = json.getJSONArray("children");
            if(subJsonArray != null){
                for(Object subJsonObj : subJsonArray){
                    JSONObject subJson=(JSONObject)subJsonObj;
                    String paramName=subJson.getString("paramName");
                    String paramRequire=subJson.getString("paramRequire");
                    String value="";
                    try {
                        value=(String)getValue(params,subJson);
                    }catch (Exception e){
                    }
                    if(MBoolean.YES.name().equals(paramRequire) && StringUtils.isEmpty(value)){
                        //必填参数未传值
                        return JsonResult.getFailResult("必填参数【"+paramName+"】:未传值！");
                    }
                    mapData.put(paramName,value);
                }
            }


            String paramName=json.getString("paramName");

            if(paramName.equals("root")){
                continue;
            }
            String paramRequire=json.getString("paramRequire");
            String value="";
            try {
                value=(String)getValue(params,json);
            }catch (Exception e){
            }
            if(MBoolean.YES.name().equals(paramRequire) && StringUtils.isEmpty(value)){
                //必填参数未传值
                return JsonResult.getFailResult("必填参数【"+paramName+"】:未传值！");
            }
            mapData.put(paramName,value);
        }
        return JsonResult.getSuccessResult(mapData);
    }

    /**
     * 获取参数的值。
     *
     * @param params
     * @param json
     * @return
     */
    private static Object getValue(Map<String, Object> params, JSONObject json) throws Exception {
        ConstVarContext constVarContext=SpringUtil.getBean(ConstVarContext.class);
        GroovyEngine groovyEngine=SpringUtil.getBean(GroovyEngine.class);
        String paramSource=json.getString("paramSource");
        String paramValue=json.getString("paramValue");
        Object val = null;
        //传入参数
        if (PARAM.equals(paramSource)) {
            val = params.get(json.getString("paramName"));
        }
        // 固定值
        else if (FIXED_VAR.equals(paramSource)) {
            val = paramValue;
        }
        // 脚本
        else if (SCRIPT.equals(paramSource)) {
            val = groovyEngine.executeScripts(paramValue, params);
        }
        //常量
        else if (CONSTANT.equals(paramSource)) {
            val = constVarContext.getValByKey(paramValue, params);
        }
        return val;
    }
}
