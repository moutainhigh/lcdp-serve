package com.redxun.util.wechat;

import com.alibaba.fastjson.JSONObject;
import com.redxun.common.redis.template.RedisRepository;
import com.redxun.common.tool.Base64Util;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;

/**
 * WxEntUtil
 * @Description: 企业微信工具
 * @Author: Elwin ZHANG
 * @Date: 2021/4/14 9:50
 */
@Component
@Slf4j
public class WxEntUtil {

    /**
    * 企业微信token缓存的键前缀
    **/
    private static final String REDIS_TOKEN_KEY="WxEntToken_";

    @Autowired
    private  RedisRepository redisRepository;

    /**
    * @Description: 获取前端调用的网页
    * @Author: Elwin ZHANG
    * @Date: 2021/4/14 11:54
    * @param corpId 企业微信的企业ID
    * @param redirectUrl 回调地址
    * @return: java.lang.String
    **/
    public String getAuthorizeUrl(String corpId, String redirectUrl)  {
        try {
            String encodeUrl = URLEncoder.encode(redirectUrl, "UTF-8");
            String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + corpId + "&redirect_uri=" + encodeUrl
                    + "&response_type=code&scope=snsapi_base&state=0#wechat_redirect";
            return url;
        }catch (Exception e){
            return "";
        }
    }

    /**
    * @Description: 获取企业微信扫码登录地址
    * @param corpId 企业微信的企业ID
    * @param agentId 企业微信的应用ID
    * @param redirectUrl 回调地址
    * @return java.lang.String
    * @Author: Elwin ZHANG  @Date: 2021/4/27 14:26
    **/
    public String getQRConnectUrl(String corpId, String agentId,String redirectUrl){
        try {
            String encodeUrl = URLEncoder.encode(redirectUrl, "UTF-8");
            String url = "https://open.work.weixin.qq.com/wwopen/sso/qrConnect?appid=" + corpId
                    + "&agentid=" + agentId + "&redirect_uri=" + encodeUrl
                    + "&state=wx";
            return url;
        }catch (Exception e){
            return "";
        }
    }

   /**
    * @Description: API获取企业微信用户信息
    * @param token access_token
    * @param code 微信成员授权获取到的code
    * @return com.alibaba.fastjson.JSONObject
    * @Author: Elwin ZHANG @Date: 2021/4/14 17:26
    **/
    public  String getUserInfo(String token,String code){
        String url="https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo?access_token=" + token + "&code=" + code;
        try {
            //缓存中不存在，则通过API获取新的TOKEN
            String content = HttpClientUtil.getFromUrl(url, null);
            log.info("根据Code获取企业微信用户信息结果：" + content);
            JSONObject object= JSONObject.parseObject(content);
            //出错则返回为空
            if(!"0".equals((object.get("errcode").toString()))){
                return null;
            }
            String userId=object.getString("UserId");
            if(StringUtils.isNotEmpty(userId)){
                return  userId;
            }
            return object.getString("OpenId");
        }catch (Exception e){
            log.info("API获取企业微信用户信息：",e);
        }
        return null;
    }


    /**
     * @Description: 获取某租户企业微信token
     * @Author: Elwin ZHANG 2021-4-14
     * @param corpId 企业微信的企业ID
     * @param agentSecret 企业微信的应用密钥
     * @param tenantId 租户ID
     * @return java.lang.String
     */
    public String getToken(String corpId,String agentSecret,String tenantId){
        //先从缓存获取
        String token=getTokeFromCache( tenantId);
        if(StringUtils.isNotEmpty(token)){
            return token;
        }
        //不存在则通过API获取
        return getAccessToken(corpId,agentSecret,tenantId);
    }

    /**
     * @Description: API获取企业微信的TOKEN
     * @Author: Elwin ZHANG
     * @Date: 2021/4/14 9:55
     * @param corpId 企业微信的企业ID
     * @param  agentSecret: 企业微信的应用密钥
     * @param tenantId : 租户ID
     * @return: com.alibaba.fastjson.JSONObject
    **/
    private  String getAccessToken(String corpId,String agentSecret,String tenantId)  {
        String url="https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=" + corpId + "&corpsecret=" + agentSecret;
        try {
            //缓存中不存在，则通过API获取新的TOKEN
            String content = HttpClientUtil.getFromUrl(url, null);
            log.info("获取企业微信Token结果：" + content);
            JSONObject object= JSONObject.parseObject(content);
            return refreshTokenCache(tenantId,object);
        }catch (Exception e){
            log.info("获取企业微信Token出错：",e);
        }
        return null;
    }

    /**
     * @Description: 获取所有部门用户信息
     * @param token : access_token
     * @Date: 2021/5/12 14:55
     * @return: java.lang.String
     **/
    public  String getAllUserInfo(String token, String groupId){
        String url="https://qyapi.weixin.qq.com/cgi-bin/user/list?access_token="+token+"&department_id="+groupId+"&fetch_child=0";
        try {
            String content = HttpClientUtil.getFromUrl(url, null);
            log.info("根据token获取企业微信所有部门的用户信息结果：" + content);
            JSONObject object= JSONObject.parseObject(content);
            //出错则返回为空
            if(!"0".equals((object.get("errcode").toString()))){
                return null;
            }
            return object.getString("userlist");
        }catch (Exception e){
            log.info("API获取企业微信所有部门的用户信息出错：",e);
            return null;
        }
    }
    /**
     * @Description: 获取所有部门信息
     * @param token : access_token
     * @Date: 2021/5/18 16:55
     * @return: java.lang.String
     **/
    public  String getDepartment(String token){
        String url="https://qyapi.weixin.qq.com/cgi-bin/department/list?access_token="+token;
        try {
            String content = HttpClientUtil.getFromUrl(url, null);
            log.info("根据token获取企业微信所有部门信息结果：" + content);
            JSONObject object= JSONObject.parseObject(content);
            //出错则返回为空
            if(!"0".equals((object.get("errcode").toString()))){
                return null;
            }
            return object.getString("department");
        }catch (Exception e){
            log.info("API获取企业微信所有部门信息出错：",e);
            return null;
        }
    }


    /**
    * @Description: 获取某租户缓存的企业微信token
    * @Author: Elwin ZHANG
    * @Date: 2021/4/14 11:21
    * @param tenantId : 租户ID
    * @return: java.lang.String
    **/
    private  String getTokeFromCache(String tenantId){
        String key=REDIS_TOKEN_KEY+tenantId;
        Object value=redisRepository.get(key);
        if(value==null){
            return null;
        }
        return value.toString();
    }
    /**
    * @Description: 根据返回的Token信息缓存
    * @Author: Elwin ZHANG
    * @Date: 2021/4/14 10:49
    *  @param tenantId : 租户ID
    * @param object :Token JSON对象
    **/
    private  String refreshTokenCache(String tenantId,JSONObject object){
        try{
            String key=REDIS_TOKEN_KEY+tenantId;
            if(object==null ){
                return  null;
            }
            if(!"0".equals((object.get("errcode").toString()))){
                return null;
            }
            String token=object.getString("access_token");
            Integer expires=object.getInteger("expires_in");
            redisRepository.setExpire(key,token,expires-1);
            return  token;
        }catch (Exception e){
            log.info("解析微信Token参数值时出错",e);
            return null;
        }
    }
}
