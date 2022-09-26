package com.redxun.system.core.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.constant.MBoolean;
import com.redxun.common.constant.SysConstant;
import com.redxun.common.http.HttpFactory;
import com.redxun.common.http.HttpRtnModel;
import com.redxun.common.http.IHttp;
import com.redxun.common.script.GroovyEngine;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ExceptionUtil;
import com.redxun.constvar.ConstVarContext;
import com.redxun.log.model.Audit;
import com.redxun.log.model.LogContext;
import com.redxun.system.core.entity.SysHttpTask;
import com.redxun.system.core.entity.SysHttpTaskLog;
import com.redxun.system.core.entity.SysWebReqDef;
import com.redxun.system.core.mapper.SysWebReqDefMapper;
import com.redxun.util.SysUtil;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * [WEB请求调用定义]业务服务类
 */
@Service
public class SysWebReqDefServiceImpl extends SuperServiceImpl<SysWebReqDefMapper, SysWebReqDef> implements BaseService<SysWebReqDef> {

    /**
     * 常量
     */
    private static final String S_CONSTANT = "constantVar";
    /**
     * 脚本
     */
    private static final String S_SCRIPT = "script";
    /**
     * 表单数据
     */
    private static final String S_FORM_PARAM = "formParam";
    /**
     * 流程变量数据
     */
    private static final String S_BPM_PARAM = "bpmParam";
    /**
     * 固定值
     */
    private static final String S_FIXED_VAR = "fixedVar";

    @Resource
    private SysWebReqDefMapper sysWebReqDefMapper;
    @Resource
    GroovyEngine groovyEngine;
    @Resource
    HttpFactory httpFactory;
    @Resource
    ConstVarContext constVarContext;
    @Resource
    private SysInvokeScriptServiceImpl sysInvokeScriptService;
    @Resource
    SysHttpTaskLogServiceImpl sysHttpTaskLogService;

    @Override
    public BaseDao<SysWebReqDef> getRepository() {
        return sysWebReqDefMapper;
    }

    /**
     * [{key:"",value:""}]
     *
     * @param json
     * @return
     */
    public Map parseParamsMap(String json) {
        JSONArray ary = JSONArray.parseArray(json);
        Map<String, String> map = new HashMap<>(ary.size());
        for (int i = 0; i < ary.size(); i++) {
            JSONObject jsonObj = ary.getJSONObject(i);
            String key = jsonObj.getString("key");
            String value = "";
            String from = jsonObj.getString("valueSource");
            if (StringUtils.isNotEmpty(from)) {
                if (S_SCRIPT.equals(from)) {
                    //来自脚本
                    String scriptText = jsonObj.getString("value");
                    Object obj = groovyEngine.executeScripts(scriptText, null);
                    value = String.valueOf(obj);
                } else if (S_CONSTANT.equals(from)) {
                    //来自常量
                    value = String.valueOf(SysUtil.replaceConstant(jsonObj.getString("value")));
                } else {
                    //其他：如传入参数
                    value = jsonObj.getString("value");
                }
            } else {
                value = jsonObj.getString("value");
            }

            map.put(key, value);
        }
        return map;
    }

    @Recover
    public JsonResult recover(Exception e,String url, String type, HashMap headerMap, HashMap params, String template,String alias,String batId) {
        String message=e.getMessage();
        SysHttpTaskLog log=new SysHttpTaskLog();
        log.setResponseState("500");
        log.setResult("0");
        log.setTaskId(batId);
        if(StringUtils.isNotEmpty(message)){
            JSONObject json=JSONObject.parseObject(message);
            log=JSONObject.toJavaObject(json,SysHttpTaskLog.class);
        }
        JsonResult result=new JsonResult();
        result.setSuccess(false);
        result.setCode(Integer.parseInt(log.getResponseState()));
        result.setMessage(log.getErrorMessage());
        SysWebReqDef sysWebReqDef = getByAlias(alias);
        if(MBoolean.YES.name().equals(sysWebReqDef.getIsLog())) {
            sysHttpTaskLogService.createLog(batId, SysHttpTask.TYPE_WEBREQ, sysWebReqDef.getId(), sysWebReqDef.getName(), "sysWebReqDefServiceImpl", "execute", log, false, url, type, headerMap, params, template, alias);
        }
        return result;
    }

    @Retryable(value = Exception.class, maxAttemptsExpression = "${retry.maxAttempts:3}", backoff = @Backoff(delayExpression = "${retry.delay:2000}", multiplierExpression = "${retry.multiplier:1.5}"))
    public JsonResult execute(String url, String type, HashMap headerMap, HashMap params, String template,String alias,String batId) throws Exception {
        return executeReq(url,type,headerMap,params,template,alias,batId);
    }

    /**
     * 执行http请求
     *
     * @param url       访问地址
     * @param type      请求类型 POST,GET,DELETE
     * @param headerMap 请求头
     * @param params    请求参数定义
     * @param template  报文模版
     * @param alias
     * @param batId
     * @return
     */
    public JsonResult executeReq(String url, String type, Map headerMap, Map params, String template,String alias,String batId) throws Exception {
        //使用HttpClient方式
        IHttp httpClient = httpFactory.getHttpByResfulTemplate();
        // 格式化URL
        url = StringUtils.format(url, params);
        // 格式化请求报文
        template = StringUtils.format(template, params);
        JsonResult result = new JsonResult();
        SysHttpTaskLog log=new SysHttpTaskLog();
        log.setTaskId(batId).setLogUrl(url).setLogHeaders(JSONArray.toJSONString(headerMap));
        HttpRtnModel htm = new HttpRtnModel();
        try {
            Long start=System.currentTimeMillis();
            switch (type.toUpperCase()) {
                case "GET":
                    if (StringUtils.isEmpty(template)) {
                        log.setLogQuery(JSONArray.toJSONString(params));
                        htm = httpClient.getFromUrl(url, headerMap, params);
                    } else {
                        log.setLogBody(template);
                        htm = httpClient.getFromUrl(url, headerMap, template);
                    }
                    result.setData(htm.getContent());
                    break;
                case "POST":
                    if (StringUtils.isEmpty(template)) {
                        log.setLogBody(JSONArray.toJSONString(params));
                        htm = httpClient.postFromUrl(url, headerMap,new HashMap<>(), params);
                    } else {
                        log.setLogBody(template);
                        htm = httpClient.postFromUrl(url, headerMap,new HashMap<>(), template);
                    }
                    break;
                default:
                    result = new JsonResult();
                    break;
            }
            Long end=System.currentTimeMillis();
            Long timeConsuming = (end-start);
            log.setTimeConsuming(String.valueOf(timeConsuming));
            log.setResponseState(String.valueOf(htm.getStatusCode()));
            if (htm.getStatusCode() == 200) {
                log.setResult("1");
                result.setSuccess(true);
                log.setResponseData(htm.getContent());
                result.setData(htm.getContent());
            } else {
                log.setResult("0");
                log.setErrorMessage(htm.getContent());
                throw new RuntimeException(JSONObject.toJSONString(log));
            }
        } catch (Exception e) {
            log.setResponseState("500");
            log.setResult("0");
            log.setErrorMessage(ExceptionUtil.getExceptionMessage(e));
            throw new RuntimeException(JSONObject.toJSONString(log));
        }
        if(StringUtils.isNotEmpty(batId)) {
            SysWebReqDef sysWebReqDef = getByAlias(alias);
            if(MBoolean.YES.name().equals(sysWebReqDef.getIsLog())) {
                sysHttpTaskLogService.createLog(batId, SysHttpTask.TYPE_WEBREQ, sysWebReqDef.getId(), sysWebReqDef.getName(), "sysWebReqDefServiceImpl", "execute", log, true, url, type, headerMap, params, template, alias);
            }
        }
        return result;
    }

    /**
     * 通过别名获取
     *
     * @param alias
     * @return
     */
    public SysWebReqDef getByAlias(String alias) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("ALIAS_", alias);
        return sysWebReqDefMapper.selectOne(queryWrapper);
    }

    public boolean isExist(SysWebReqDef ent) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("ALIAS_", ent.getAlias());
        if (StringUtils.isNotEmpty(ent.getId())) {
            wrapper.ne("ID_", ent.getId());
        }
        Integer rtn = sysWebReqDefMapper.selectCount(wrapper);
        return rtn > 0;
    }

    /**
     * 执行脚本
     *
     * @param scriptText
     * @param alias
     * @param headerMap
     * @param paramsMap
     * @param body
     * @return
     */
    public Object executeScript(String scriptText, String alias, Map<String, String> headerMap, Map<String, String> paramsMap, String body) {
        Object obj = null;
        try {
            Map<String, Object> vars = new HashMap<>(SysConstant.INIT_CAPACITY_16);
            if (StringUtils.isNotEmpty(alias)) {
                SysWebReqDef webReqDef = getByAlias(alias);
                JSONArray paramsSet = JSONArray.parseArray(webReqDef.getParamsSet());
                Map<String, String> header = new HashMap<>(paramsSet.size());
                for (int i = 0; i < paramsSet.size(); i++) {
                    JSONObject head = paramsSet.getJSONObject(i);
                    String headKey = head.getString("key");
                    header.put(headKey, headerMap.get(headKey));
                }
                JSONArray data = JSONArray.parseArray(webReqDef.getData());
                Map<String, String> params = new HashMap<>(data.size());
                for (int i = 0; i < data.size(); i++) {
                    JSONObject param = data.getJSONObject(i);
                    String paramKey = param.getString("key");
                    params.put(paramKey, paramsMap.get(paramKey));
                }
                vars.put("$header", header);
                vars.put("$params", params);
                vars.put("$body", body);
            }
            obj = groovyEngine.executeScripts(scriptText, vars);
        } catch (Exception e) {
            obj = e.getMessage();
        }
        return obj;
    }

    /**
     * 返回结果
     *
     * @param alias
     * @param headerMap
     * @param paramsMap
     * @param body
     * @return
     * @throws Exception
     */
    public JsonResult previewReturn(String alias, Map<String, String> headerMap, Map<String, String> paramsMap, String body) throws Exception {
        SysWebReqDef webReqDef = getByAlias(alias);
        String dataType = webReqDef.getDataType();
        headerMap.put("Content-Type", dataType + ";charset=UTF-8");

        JsonResult<String> result = executeReq(webReqDef.getUrl(),
                ("SOAP".equals(webReqDef.getMode()) ? "POST" : webReqDef.getType()),
                headerMap, paramsMap, body,null,null);
        return result;
    }


    /**
     * @param alias
     * @param header
     * @param params
     * @return
     */
    public JsonResult executeStart(String alias, String header, String params, Map map) throws Exception {
        SysWebReqDef sysWebReqDef = getByAlias(alias);
        if (BeanUtil.isEmpty(sysWebReqDef)) {
            return new JsonResult<String>(false, "未设置服务");
        }

        HashMap<String, String> headerMap = new HashMap<>(SysConstant.INIT_CAPACITY_16);
        if (StringUtils.isNotEmpty(header)) {
            JSONArray ary = JSONArray.parseArray(header);
            headerMap = new HashMap<>(ary.size());
            Iterator<Object> it = ary.iterator();
            while (it.hasNext()) {
                JSONObject obj = (JSONObject) it.next();
                //获取配置结果
                Object value = getValue(map, obj);
                headerMap.put(obj.getString("key"), (String) value);
            }
        }
        HashMap<String, String> paramsMap = new HashMap<>(SysConstant.INIT_CAPACITY_16);
        if (StringUtils.isNotEmpty(params)) {
            JSONArray ary = JSONArray.parseArray(params);
            paramsMap = new HashMap<>(ary.size());
            Iterator<Object> it = ary.iterator();
            while (it.hasNext()) {
                JSONObject obj = (JSONObject) it.next();
                //获取配置结果
                Object value = getValue(map, obj);
                paramsMap.put(obj.getString("key"), (String) value);
            }
        }

        //请求URL
        String url = sysWebReqDef.getUrl();
        //报文模板
        String template = sysWebReqDef.getTemp();
        //调用类型(soap,restful)
        String type = sysWebReqDef.getType();
        if ("SOAP".equals(sysWebReqDef.getMode())) {
            type = "POST";
        }
        JsonResult result = execute(url, type, headerMap, paramsMap, template,alias, IdGenerator.getIdStr());

        return result;
    }

    /**
     * 获取参数的值。
     *
     * @param params
     * @param jsonObject
     * @return
     */
    private Object getValue(Map<String, Object> params, JSONObject jsonObject) {
        String valSource = (String) jsonObject.get("valueSource");
        String valueDef = (String) jsonObject.get("valueDef");
        Object val = null;
        //表单数据
        if (S_FORM_PARAM.equals(valSource)) {
            Map<String, Object> map = (Map<String, Object>) params.get("formParam");
            if (BeanUtil.isNotEmpty(map)) {
                val = map.get(valueDef);
            }
        }
        //流程变量数据
        else if (S_BPM_PARAM.equals(valSource)) {
            Map<String, Object> map = (Map<String, Object>) params.get("bpmParam");
            val = map.get(valueDef);
        }
        // 固定值
        else if (S_FIXED_VAR.equals(valSource)) {
            val = valueDef;
        }
        // 脚本
        else if (S_SCRIPT.equals(valSource)) {
            val = (String) groovyEngine.executeScripts(valueDef, params);
        }
        //常量
        else if (S_CONSTANT.equals(valSource)) {
            val = constVarContext.getValByKey(valueDef, params);
        }
        //url参数
        else {
            val = jsonObject.get("value");
        }
        return val;
    }

    public void importWebReq(MultipartFile file, String treeId,String appId) {
        StringBuilder sb=new StringBuilder();
        sb.append("导入WEB请求:");
        JSONArray sysWebArray  = sysInvokeScriptService.readZipFile(file);
        for (Object obj:sysWebArray) {
            JSONObject webObj = (JSONObject)obj;
            JSONObject sysWebReq = webObj.getJSONObject("sysWebReq");
            if(BeanUtil.isEmpty(sysWebReq)){
                continue;
            }

            String sysWebStr = sysWebReq.toJSONString();
            SysWebReqDef sysWebReqDef = JSONObject.parseObject(sysWebStr,SysWebReqDef.class);
            sb.append(sysWebReqDef.getName() +"("+sysWebReqDef.getId()+"),");

            String id = sysWebReqDef.getId();
            SysWebReqDef oldWeb = get(id);
            sysWebReqDef.setAppId(appId);
            if(BeanUtil.isNotEmpty(oldWeb)) {
                update(sysWebReqDef);
            }
            else{
                insert(sysWebReqDef);
            }
        }
        sb.append(",导入到分类:" + treeId);

        LogContext.put(Audit.DETAIL,sb.toString());
    }


}
