package com.redxun.util.feishu;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.HttpClientUtil;
import com.redxun.util.feishu.constants.Keys;
import com.redxun.util.feishu.entity.*;
import com.redxun.util.feishu.enums.DepartmentIdTypeEnum;
import com.redxun.util.feishu.enums.UserIdTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 飞书工具类。
 *
 * @author ycs
 */
@Component
@Slf4j
public class FeiShuClient {

    private static final String FEISHU_BASE_ADDR="https://open.feishu.cn";

    private static String defaultCharset="UTF-8";

    private static String getBaseAddr(){
        String feishuAddr = null;

        if(StringUtils.isEmpty(feishuAddr)){
            feishuAddr=FEISHU_BASE_ADDR;
        }
        return feishuAddr;
    }


    /**
     * 获取飞书app_access_token
     * @param appid
     * @param secret
     * @return app_access_token
     *
     * @throws Exception
     */
    public String getAppAccessToken(String appid,String secret) throws Exception{
        //获取token
        String url= getBaseAddr()+"/open-apis/auth/v3/app_access_token/internal";
        FeiShuTokenReq req=new FeiShuTokenReq();
        req.setAppId(appid);
        req.setAppSecret(secret);
        String rtnCode= HttpClientUtil.postJson(url, JSON.toJSONString(req));
        FeiShuAppToken feiShuAppToken= JSON.parseObject(rtnCode,FeiShuAppToken.class);
        if(feiShuAppToken!=null && feiShuAppToken.getCode()==0){
            return feiShuAppToken.getAppAccessToken();
        }
        return null;
    }
    /**
     * 获取飞书tenant_access_token
     * @param appid
     * @param secret
     * @return tenant_access_token
     * @throws Exception
     */
    public String getTenantAccessToken(String appid,String secret) throws Exception{
        //获取token
        String url= getBaseAddr()+"/open-apis/auth/v3/tenant_access_token/internal";
        FeiShuTokenReq req=new FeiShuTokenReq();
        req.setAppId(appid);
        req.setAppSecret(secret);
        String rtnCode= HttpClientUtil.postJson(url, JSON.toJSONString(req));
        FeiShuTenantToken feiShuAppToken= JSON.parseObject(rtnCode, FeiShuTenantToken.class);
        if(feiShuAppToken!=null && feiShuAppToken.getCode()==0){
            return feiShuAppToken.getTenantAccessToken();
        }
        return null;
    }

    /**
     * 获取飞书授权url
     * @param appid
     * @param redirectUri
     * @param state
     * @return url
     * @throws Exception
     */
    public String getFeishuAuthUrl(String appid,String redirectUri,String state){
       String url= getBaseAddr()+"/open-apis/authen/v1/index?redirect_uri="+redirectUri+"&app_id="+appid+"&state="+state;
        return url;
    }

    /**
     * 获取飞书用户信息
     * @param req
     * @param appAccessToken 调用FeiShuClient.getAppAccessToken()获取
     * @return FeiShuUserInfoResp
     * @throws Exception
     */
    public FeiShuUserAccessTokenInfo getFeiShuUserAccessTokenInfo(String appAccessToken , FeiShuUserAccessTokenInfoReq req) throws Exception{
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "Bearer "+appAccessToken);
        //获取用户信息
        String url= getBaseAddr()+"/open-apis/authen/v1/access_token";
        HttpClientUtil.HttpRtnModel httpRtnModel = HttpClientUtil.postJson(url, headers, JSON.toJSONString(req), defaultCharset);
        log.info("getFeiShuUserAccessTokenInfo httpRtnModel:{}",httpRtnModel.getContent());
        if(200!=httpRtnModel.getStatusCode()){
            return null;
        }
        FeiShuResp<FeiShuUserAccessTokenInfo> feiShuResp= JSON.parseObject(httpRtnModel.getContent(), new TypeReference<FeiShuResp<FeiShuUserAccessTokenInfo>>(){});
        if(feiShuResp!=null && feiShuResp.getCode()==0){
            return feiShuResp.getData();
        }
        return null;
    }
    /**
     * 获取飞书用户信息
     * @param userAccessToken 调用FeiShuClient.getFeiShuUserAccessTokenInfo()获取
     * @return FeiShuUserInfoResp
     * @throws Exception
     */
    public FeiShuUserInfo getFeiShuUserInfo(String userAccessToken) throws Exception{
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "Bearer "+userAccessToken);
        //获取用户信息
        String url= getBaseAddr()+"/open-apis/authen/v1/user_info";
        HttpClientUtil.HttpRtnModel httpRtnModel = HttpClientUtil.getFromUrlHreader(url, headers, defaultCharset);
        if(200!=httpRtnModel.getStatusCode()){
            return null;
        }
        FeiShuResp<FeiShuUserInfo> feiShuResp= JSON.parseObject(httpRtnModel.getContent(), new TypeReference<FeiShuResp<FeiShuUserInfo>>(){});
        if(feiShuResp!=null && feiShuResp.getCode()==0){
            return feiShuResp.getData();
        }
        return null;
    }

    /**
     * 发送飞书消息
     * @param req 消息内容参数类
     * @return FeiShuUserInfoResp
     * @throws Exception
     */
    public FeiShuSendMessage sendMessage(String tenantAccessToken, UserIdTypeEnum userIdTypeEnum, FeiShuSendMessageReq req) throws Exception{
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "Bearer "+tenantAccessToken);
        //获取用户信息
        String url= getBaseAddr()+"/open-apis/im/v1/messages?receive_id_type="+(userIdTypeEnum==null?UserIdTypeEnum.OPEN_ID.getValue():userIdTypeEnum.getValue());

        HttpClientUtil.HttpRtnModel httpRtnModel = HttpClientUtil.postJson(url, headers, JSON.toJSONString(req), defaultCharset);
        log.info("sendMessage httpRtnModel:{}",httpRtnModel.getContent());
        if(200!=httpRtnModel.getStatusCode()){
            return null;
        }
        FeiShuResp<FeiShuSendMessage> feiShuResp= JSON.parseObject(httpRtnModel.getContent(), new TypeReference<FeiShuResp<FeiShuSendMessage>>(){});
        if(feiShuResp!=null && feiShuResp.getCode()==0){
            return feiShuResp.getData();
        }
        return null;
    }

    /**
     * 创建飞书部门
     * @param tenantAccessToken
     * @param req
     * @param userIdTypeEnum
     * @param departmentIdTypeEnum
     * @return FeiShuDepartment
     * @throws Exception
     */
    public FeiShuDepartment createDepartment(String tenantAccessToken, FeiShuDepartmentsEditReq req, UserIdTypeEnum userIdTypeEnum, DepartmentIdTypeEnum departmentIdTypeEnum) throws Exception{
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "Bearer "+tenantAccessToken);
        //获取用户信息
        String url= getBaseAddr()+"/open-apis/contact/v3/departments?user_id_type="+(userIdTypeEnum==null?"":userIdTypeEnum.getValue())+"&department_id_type="+(departmentIdTypeEnum==null?"":departmentIdTypeEnum.getValue());

        HttpClientUtil.HttpRtnModel httpRtnModel = HttpClientUtil.postJson(url, headers, JSON.toJSONString(req), defaultCharset);
        if(200!=httpRtnModel.getStatusCode()){
            return null;
        }
        FeiShuResp<Map<String,FeiShuDepartment>> feiShuResp= JSON.parseObject(httpRtnModel.getContent(), new TypeReference<FeiShuResp<Map<String,FeiShuDepartment>>>(){});
        if(feiShuResp!=null && feiShuResp.getCode()==0){
            return feiShuResp.getData().get("department");
        }
        return null;
    }

    /**
     * 查询单个部门
     * @param tenantAccessToken
     * @param departmentId
     * @param userIdTypeEnum
     * @param departmentIdTypeEnum
     * @return FeiShuDepartment
     * @throws Exception
     */
    public FeiShuDepartment getDepartment(String tenantAccessToken,String departmentId,UserIdTypeEnum userIdTypeEnum, DepartmentIdTypeEnum departmentIdTypeEnum ) throws Exception{
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "Bearer "+tenantAccessToken);
        //获取用户信息
        String url= getBaseAddr()+"/open-apis/contact/v3/departments/"+departmentId+"?user_id_type="+(userIdTypeEnum==null?"":userIdTypeEnum.getValue())+"&department_id_type="+(departmentIdTypeEnum==null?"":departmentIdTypeEnum.getValue());

        String json = HttpClientUtil.getFromUrlByHeaders(url, headers, null);
        if(StringUtils.isEmpty(json)){
            return null;
        }
        FeiShuResp<Map<String,FeiShuDepartment>> feiShuResp= JSON.parseObject(json, new TypeReference<FeiShuResp<Map<String,FeiShuDepartment>>>(){});
        if(feiShuResp!=null && feiShuResp.getCode()==0){
            return feiShuResp.getData().get("department");
        }
        return null;
    }

    /**
     * 更新部门信息
     * @param tenantAccessToken
     * @param req
     * @param userIdTypeEnum
     * @param departmentIdTypeEnum
     * @return FeiShuDepartment
     * @throws Exception
     */
    public FeiShuDepartment updateDepartment(String tenantAccessToken, FeiShuDepartmentsEditReq req, UserIdTypeEnum userIdTypeEnum, DepartmentIdTypeEnum departmentIdTypeEnum) throws Exception{
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "Bearer "+tenantAccessToken);
        //获取用户信息
        String url= getBaseAddr()+"/open-apis/contact/v3/departments/"+req.getDepartmentId()+"?user_id_type="+(userIdTypeEnum==null?"":userIdTypeEnum.getValue())+"&department_id_type="+(departmentIdTypeEnum==null?"":departmentIdTypeEnum.getValue());

        HttpClientUtil.HttpRtnModel httpRtnModel = HttpClientUtil.patchJson(url, headers, JSON.toJSONString(req), defaultCharset);
        if(200!=httpRtnModel.getStatusCode()){
            return null;
        }
        FeiShuResp<Map<String,FeiShuDepartment>> feiShuResp= JSON.parseObject(httpRtnModel.getContent(), new TypeReference<FeiShuResp<Map<String,FeiShuDepartment>>>(){});
        if(feiShuResp!=null && feiShuResp.getCode()==0){
            return feiShuResp.getData().get("department");
        }
        return null;
    }
    /**
     * 删除部门信息
     * @param tenantAccessToken
     * @param departmentId
     * @param departmentIdTypeEnum DepartmentIdTypeEnum
     * @return FeiShuDepartment
     * @throws Exception
     */
    public Boolean delDepartment(String tenantAccessToken,String departmentId, DepartmentIdTypeEnum departmentIdTypeEnum) throws Exception{
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "Bearer "+tenantAccessToken);
        //获取用户信息
        String url= getBaseAddr()+"/open-apis/contact/v3/departments/"+departmentId+"?department_id_type="+(departmentIdTypeEnum==null?"":departmentIdTypeEnum.getValue());

        HttpClientUtil.HttpRtnModel httpRtnModel = HttpClientUtil.delFromUrl(url, headers, defaultCharset);
        if(200!=httpRtnModel.getStatusCode()){
            return false;
        }
        FeiShuResp<Object> feiShuResp= JSON.parseObject(httpRtnModel.getContent(), new TypeReference<FeiShuResp<Object>>(){});
        if(feiShuResp!=null && feiShuResp.getCode()==0){
            return true;
        }
        return false;
    }
    /**
     * 创建飞书用户
     * @param tenantAccessToken
     * @param req
     * @param userIdTypeEnum
     * @param departmentIdTypeEnum
     * @return FeiShuDepartment
     * @throws Exception
     */
    public FeiShuUser createUser(String tenantAccessToken,FeiShuUserEditReq req,UserIdTypeEnum userIdTypeEnum, DepartmentIdTypeEnum departmentIdTypeEnum) throws Exception{
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "Bearer "+tenantAccessToken);
        //获取用户信息
        String url= getBaseAddr()+"/open-apis/contact/v3/users?user_id_type="+(userIdTypeEnum==null?"":userIdTypeEnum.getValue())+"&department_id_type="+(departmentIdTypeEnum==null?"":departmentIdTypeEnum.getValue());

        HttpClientUtil.HttpRtnModel httpRtnModel = HttpClientUtil.postJson(url, headers, JSON.toJSONString(req), defaultCharset);
        if(200!=httpRtnModel.getStatusCode()){
            return null;
        }
        FeiShuResp<Map<String,FeiShuUser>> feiShuResp= JSON.parseObject(httpRtnModel.getContent(), new TypeReference<FeiShuResp<Map<String,FeiShuUser>>>(){});
        if(feiShuResp!=null && feiShuResp.getCode()==0){
            return feiShuResp.getData().get("user");
        }
        return null;
    }

    /**
     * 查询单个用户
     * @param tenantAccessToken
     * @param userId
     * @param userIdTypeEnum
     * @param departmentIdTypeEnum
     * @return FeiShuDepartment
     * @throws Exception
     */
    public FeiShuUser getUser(String tenantAccessToken,String userId,UserIdTypeEnum userIdTypeEnum, DepartmentIdTypeEnum departmentIdTypeEnum ) throws Exception{
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "Bearer "+tenantAccessToken);
        //获取用户信息
        String url= getBaseAddr()+"/open-apis/contact/v3/users/"+userId+"?user_id_type="+(userIdTypeEnum==null?"":userIdTypeEnum.getValue())+"&department_id_type="+(departmentIdTypeEnum==null?"":departmentIdTypeEnum.getValue());

        String json = HttpClientUtil.getFromUrlByHeaders(url, headers, null);
        if(StringUtils.isEmpty(json)){
            return null;
        }
        FeiShuResp<Map<String,FeiShuUser>> feiShuResp= JSON.parseObject(json, new TypeReference<FeiShuResp<Map<String,FeiShuUser>>>(){});
        if(feiShuResp!=null && feiShuResp.getCode()==0){
            return feiShuResp.getData().get("user");
        }
        return null;
    }

    /**
     * 更新用户
     * @param tenantAccessToken
     * @param req
     * @param userIdTypeEnum
     * @param departmentIdTypeEnum
     * @return FeiShuDepartment
     * @throws Exception
     */
    public FeiShuUser updateUser(String tenantAccessToken,FeiShuUserEditReq req,UserIdTypeEnum userIdTypeEnum, DepartmentIdTypeEnum departmentIdTypeEnum) throws Exception{
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "Bearer "+tenantAccessToken);
        //获取用户信息
        String url= getBaseAddr()+"/open-apis/contact/v3/users/"+req.getUserId()+"?user_id_type="+(userIdTypeEnum==null?"":userIdTypeEnum.getValue())+"&department_id_type="+(departmentIdTypeEnum==null?"":departmentIdTypeEnum.getValue());

        HttpClientUtil.HttpRtnModel httpRtnModel = HttpClientUtil.patchJson(url, headers, JSON.toJSONString(req), defaultCharset);
        if(200!=httpRtnModel.getStatusCode()){
            return null;
        }
        FeiShuResp<Map<String,FeiShuUser>> feiShuResp= JSON.parseObject(httpRtnModel.getContent(), new TypeReference<FeiShuResp<Map<String,FeiShuUser>>>(){});
        if(feiShuResp!=null && feiShuResp.getCode()==0){
            return feiShuResp.getData().get("user");
        }
        return null;
    }
    /**
     * 删除用户
     * @param tenantAccessToken
     * @param userId
     * @param userIdTypeEnum UserIdTypeEnum
     * @return FeiShuDepartment
     * @throws Exception
     */
    public Boolean delUser(String tenantAccessToken,String userId, UserIdTypeEnum userIdTypeEnum) throws Exception{
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "Bearer "+tenantAccessToken);
        //获取用户信息
        String url= getBaseAddr()+"/open-apis/contact/v3/users/"+userId+"?user_id_type="+(userIdTypeEnum==null?"":userIdTypeEnum.getValue());

        HttpClientUtil.HttpRtnModel httpRtnModel = HttpClientUtil.delFromUrl(url, headers, defaultCharset);
        if(200!=httpRtnModel.getStatusCode()){
            return false;
        }
        FeiShuResp<Object> feiShuResp= JSON.parseObject(httpRtnModel.getContent(), new TypeReference<FeiShuResp<Object>>(){});
        if(feiShuResp!=null && feiShuResp.getCode()==0){
            return true;
        }
        return false;
    }


}
