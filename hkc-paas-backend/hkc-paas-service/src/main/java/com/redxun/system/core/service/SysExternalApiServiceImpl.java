
package com.redxun.system.core.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.tool.StringUtils;
import com.redxun.dto.form.FormSolutionDto;
import com.redxun.system.core.entity.SysExternalApi;
import com.redxun.system.core.entity.SysInvokeScript;
import com.redxun.system.core.mapper.SysExternalApiMapper;
import com.redxun.system.feign.FormClient;
import com.redxun.system.feign.FormCustomQueryClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * [对外API接口管理]业务服务类
 */
@Service
public class SysExternalApiServiceImpl extends SuperServiceImpl<SysExternalApiMapper, SysExternalApi> implements BaseService<SysExternalApi> {

    @Resource
    private SysExternalApiMapper sysExternalApiMapper;
    @Resource
    private FormClient formClient;
    @Resource
    private FormCustomQueryClient formCustomQueryClient;
    @Resource
    private SysInvokeScriptServiceImpl sysInvokeScriptService;

    @Override
    public BaseDao<SysExternalApi> getRepository() {
        return sysExternalApiMapper;
    }

    private static final String FORM_TYPE = "form";
    private static final String SQL_TYPE = "sql";
    private static final String SCRIPT_TYPE = "script";

    /**
     * 根据类型生成接口
     *
     * @param type  type 类型 form（表单）、sql(自定义查询)
     * @param alias
     */
    public void genExternalApi(String type, String alias) {
        if (StringUtils.isEmpty(type) || StringUtils.isEmpty(alias)) {
            return;
        }
        //表单
        if (FORM_TYPE.equals(type)) {
            genFormExternalApi(alias);
        }
        //自定义查询
        else if (SQL_TYPE.equals(type)) {
            genSqlExternalApi(alias);
        }
        //脚本
        else if (SCRIPT_TYPE.equals(type)) {
            genInvokeScript(alias);
        }
    }

    /**
     * 生成表单的外部接口
     * @param alias
     */
    public void genFormExternalApi(String alias) {
        if (StringUtils.isEmpty(alias)) {
            return;
        }
        FormSolutionDto formSolution = formClient.getFormSolutionByAlias(alias);
        genSaveData(alias,formSolution,"form");
        genSaveData(alias,formSolution,"startFlow");
        genRemove(alias,formSolution);
        genGetData(alias,formSolution);
    }

    /**
     * 生成自定义查询的外部接口
     * @param alias
     */
    public void genSqlExternalApi(String alias) {
        JsonResult jsonResult = formCustomQueryClient.getByKey(alias);
        String apiName="查询自定义SQL数据";
        if(jsonResult.getSuccess() &&  BeanUtil.isNotEmpty(jsonResult.getData())){
            Map data = (Map) jsonResult.getData();
            String name = (String) data.get("name");
            apiName="查询【"+name+"】自定义SQL数据";
        }
        String path="/api/api-form/restApi/form/query/"+alias;
        SysExternalApi sysExternalApi = getByPathAndType(path, SQL_TYPE);
        sysExternalApi.setPath(path);
        sysExternalApi.setApiName(apiName);
        sysExternalApi.setService("jpaas-form");
        sysExternalApi.setType(SQL_TYPE);
        sysExternalApi.setMethod("POST");
        JSONArray header = getHeader(false);
        sysExternalApi.setHeaders(header.toJSONString());
        //参数
        JSONArray params=new JSONArray();
        JSONObject param1 = new JSONObject();
        param1.put("headerName", "params");
        param1.put("paraType", "String");
        param1.put("headerDesc","参数");
        param1.put("required","是");
        params.add(param1);
        JSONObject param2 = new JSONObject();
        param2.put("headerName", "deploy");
        param2.put("paraType", "String");
        param2.put("headerDesc","下拉树格式:{textfield：'',valuefield:'',fatherNode:''}");
        param2.put("required","否");
        params.add(param2);
        sysExternalApi.setParams(params.toJSONString());
        //新增
        if(StringUtils.isEmpty(sysExternalApi.getId())){
            sysExternalApi.setId(IdGenerator.getIdStr());
            insert(sysExternalApi);
        }else {
            update(sysExternalApi);
        }
    }

    /**
     * 生成保存单据数据、启动流程接口
     * @param alias
     * @param formSolution
     * @param alias
     */
    private void genSaveData(String alias,FormSolutionDto formSolution,String action) {
        String path="/api/api-form/restApi/form/"+alias+"/"+action;
        SysExternalApi sysExternalApi = getByPathAndType(path, FORM_TYPE);
        sysExternalApi.setPath(path);
        String apiName="保存单据数据";
        if("startFlow".equals(action)){
            apiName="启动流程";
        }
        sysExternalApi.setApiName(formSolution.getName()+apiName);
        sysExternalApi.setService("jpaas-form");
        sysExternalApi.setType(FORM_TYPE);
        sysExternalApi.setMethod("POST");
        JSONArray header = getHeader(true);
        sysExternalApi.setHeaders(header.toJSONString());
        JsonResult jsonResult = formClient.getByAlias(alias, "", new JSONObject());
        if(jsonResult.getSuccess()){
            String dataStr = JSON.toJSONString(jsonResult.getData());
            sysExternalApi.setBody(dataStr);
        }
        //新增
        if(StringUtils.isEmpty(sysExternalApi.getId())){
            sysExternalApi.setId(IdGenerator.getIdStr());
            insert(sysExternalApi);
        }else {
            update(  sysExternalApi);
        }
    }

    /**
     * 生成删除数据接口
     * @param alias
     */
    private void genRemove(String alias,FormSolutionDto formSolution) {
        String path="/api/api-form/restApi/form/remove/"+alias;
        SysExternalApi sysExternalApi = getByPathAndType(path, FORM_TYPE);
        sysExternalApi.setPath(path);
        sysExternalApi.setApiName(formSolution.getName()+"删除数据");
        sysExternalApi.setService("jpaas-form");
        sysExternalApi.setType(FORM_TYPE);
        sysExternalApi.setMethod("POST");
        JSONArray header = getHeader(true);
        sysExternalApi.setHeaders(header.toJSONString());
        //参数
        JSONArray params=new JSONArray();
        JSONObject param = new JSONObject();
        param.put("headerName", "id");
        param.put("paraType", "String");
        param.put("headerDesc","主键");
        param.put("required","是");
        params.add(param);
        sysExternalApi.setParams(params.toJSONString());

        //新增
        if(StringUtils.isEmpty(sysExternalApi.getId())){
            sysExternalApi.setId(IdGenerator.getIdStr());
            insert(sysExternalApi);
        }else {
            update(  sysExternalApi);
        }
    }

    /**
     * 生成获取数据接口
     * @param alias
     */
    private void genGetData(String alias,FormSolutionDto formSolution) {
        String path="/api/api-form/restApi/form/getData/"+alias;
        SysExternalApi sysExternalApi = getByPathAndType(path, FORM_TYPE);
        sysExternalApi.setPath(path);
        sysExternalApi.setApiName(formSolution.getName()+"获取数据");
        sysExternalApi.setService("jpaas-form");
        sysExternalApi.setType(FORM_TYPE);
        sysExternalApi.setMethod("POST");
        JSONArray header = getHeader(true);
        sysExternalApi.setHeaders(header.toJSONString());
        //参数
        JSONArray params=new JSONArray();
        JSONObject param = new JSONObject();
        param.put("headerName", "pk");
        param.put("paraType", "String");
        param.put("headerDesc","业务主键");
        param.put("required","是");
        params.add(param);
        sysExternalApi.setParams(params.toJSONString());
        //新增
        if(StringUtils.isEmpty(sysExternalApi.getId())){
            sysExternalApi.setId(IdGenerator.getIdStr());
            insert(sysExternalApi);
        }else {
            update(  sysExternalApi);
        }

    }

    /**
     * 获取请求头
     * curUser 是否需要当前用户请求头
     * @return
     */
    private JSONArray getHeader(boolean curUser){
        JSONArray headerParams=new JSONArray();
        JSONObject token = new JSONObject();
        token.put("headerName", "token");
        token.put("paraType", "String");
        token.put("headerDesc","令牌");
        token.put("required","是");
        headerParams.add(token);
        if(curUser){
            JSONObject account = new JSONObject();
            account.put("headerName", "account");
            account.put("paraType", "String");
            account.put("headerDesc","用户账号");
            account.put("required","否(用户账号和用户ID必须设置一个)");
            headerParams.add(account);
            JSONObject userId = new JSONObject();
            userId.put("headerName", "userId");
            userId.put("paraType", "String");
            userId.put("headerDesc","用户ID");
            userId.put("required","否(用户账号和用户ID必须设置一个)");
            headerParams.add(userId);
        }
        return headerParams;
    }

    /**
     * 生成调用脚本接口
     * @param alias
     */
    public void genInvokeScript(String alias) {
        SysInvokeScript sysInvokeScript = sysInvokeScriptService.getByAlias(alias);
        String apiName="脚本调用";
        if(BeanUtil.isNotEmpty(sysInvokeScript)){
            apiName="【"+sysInvokeScript.getName()+"】脚本调用";
        }

        String path="/api/api-system/restApi/system/invokeScript/"+alias;
        SysExternalApi sysExternalApi = getByPathAndType(path, SCRIPT_TYPE);
        sysExternalApi.setPath(path);
        sysExternalApi.setApiName(apiName);
        sysExternalApi.setService("jpaas-system");
        sysExternalApi.setType(SCRIPT_TYPE);
        sysExternalApi.setMethod("POST");
        JSONArray header = getHeader(false);
        sysExternalApi.setHeaders(header.toJSONString());
        //参数
        String params= sysInvokeScript.getParams();
        JSONObject jsonParams=new JSONObject();

        /**
         * 生成参数。
         */
        if(StringUtils.isNotEmpty(params)){
            //[{paramName: "name", paramType: {key: "string", label: "字符"}, paramDescp: "姓名", value: "ddd",…}]
            JSONArray ary=JSONArray.parseArray(params);
            for(int i=0;i<ary.size();i++){
                JSONObject json=ary.getJSONObject(i);
                jsonParams.put(json.getString("paramName"),"");
            }
        }

        //请求体
        sysExternalApi.setBody(jsonParams.toJSONString());
        //新增
        if(StringUtils.isEmpty(sysExternalApi.getId())){
            sysExternalApi.setId(IdGenerator.getIdStr());
            insert(sysExternalApi);
        }else {
            update(sysExternalApi);
        }
    }


    //判断是否已存在 已存在则更新
    private SysExternalApi getByPathAndType(String path,String type){
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("TYPE_",type);
        if(StringUtils.isNotEmpty(path)){
            queryWrapper.eq("PATH_",path);
        }
        SysExternalApi sysExternalApi = sysExternalApiMapper.selectOne(queryWrapper);
        //不存在
        if(BeanUtil.isEmpty(sysExternalApi)){
            sysExternalApi = new SysExternalApi();
        }
        return sysExternalApi;
    }

}
