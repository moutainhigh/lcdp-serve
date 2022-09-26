package com.redxun.util.dd;

import com.alibaba.fastjson.JSONObject;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.request.OapiSnsGetuserinfoBycodeRequest;
import com.dingtalk.api.response.OapiSnsGetuserinfoBycodeResponse;
import com.redxun.common.redis.template.RedisRepository;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * @Description:  钉钉工具
 * @Author: Elwin ZHANG
 * @Date: 2021/4/22 17:43
 **/
@Component
@Slf4j
public class DingDingUtil {
    /**
     * 钉钉token缓存的键前缀
     **/
    private static final String REDIS_TOKEN_KEY="DingTalk_Token_";

    @Autowired
    private RedisRepository redisRepository;

    /**
     * @Description: 获取钉钉扫码登录地址
     * @param appKey 钉钉应用I的KEY
     * @param redirectUrl 回调地址
     * @return java.lang.String
     * @Author: Elwin ZHANG  @Date: 2021/4/27 15:01
     **/
    public String getQRConnectUrl(String appKey,String redirectUrl){
        try {
            String encodeUrl = URLEncoder.encode(redirectUrl, "UTF-8");
            String url = "https://oapi.dingtalk.com/connect/qrconnect?appid=" + appKey
                    + "&response_type=code&scope=snsapi_login&state=dd&redirect_uri=" + encodeUrl;
            return url;
        }catch (Exception e){
            return "";
        }
    }

    /**
    * @Description: 生成钉钉API调用时用到的签名
    * @param appSecret 应用密钥
    * @param  timeStamp 当前时间戳参数
    * @return java.lang.String 签名字符串
    * @Author: Elwin ZHANG  @Date: 2021/4/27 15:51
    **/
    public String getSignature( String appSecret,String timeStamp)  {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(appSecret.getBytes("UTF-8"), "HmacSHA256"));
            byte[] signatureBytes = mac.doFinal(timeStamp.getBytes("UTF-8"));
            String signature = new String(Base64.encodeBase64(signatureBytes));
            if ("".equals(signature)) {
                return "";
            }
            String encoded = URLEncoder.encode(signature, "UTF-8");
            String urlEncodeSignature = encoded.replace("+", "%20").replace("*", "%2A").replace("~", "%7E").replace("/", "%2F");
            return  urlEncodeSignature;
        }catch (Exception e){
            log.error("生成钉钉签名失败",e);
            return "";
        }
    }

    /**
    * @Description: 根据扫码获取的Code，取钉钉用户UnionId
    * @param appKey 钉钉应用Key
    * @param secret  钉钉应用密钥
    * @param code   临时Code
    * @return java.lang.String 钉钉用户UnionId
    * @Author: Elwin ZHANG  @Date: 2021/4/27 16:28
    **/
    public String getUnionId(String appKey,String secret,String code){
        String url="https://oapi.dingtalk.com/sns/getuserinfo_bycode" ;
        try{
            DefaultDingTalkClient    client = new DefaultDingTalkClient(url);
            OapiSnsGetuserinfoBycodeRequest req = new OapiSnsGetuserinfoBycodeRequest();
            req.setTmpAuthCode(code);
            OapiSnsGetuserinfoBycodeResponse response = client.execute(req,appKey,secret);
            OapiSnsGetuserinfoBycodeResponse.UserInfo userInfo = response.getUserInfo();
            //出错则返回为空
            if(userInfo==null){
                return null;
            }
            return  userInfo.getUnionid();

        }catch (Exception e){
            log.error("getuserinfo_bycode获取钉钉用户信息出错：",e);
        }
        return null;
    }
    /**
    * @Description:
    * @param token access_token
    * @param unionId 钉钉用户UnionId
    * @return java.lang.String 钉钉用户userId
    * @Author: Elwin ZHANG  @Date: 2021/4/27 17:03
    **/
    public String getUserIdByUnoinId(String token,String unionId){
        String url="https://oapi.dingtalk.com/topapi/user/getbyunionid?access_token=" + token;
        try{

            String body="{\"unionid\":\""+ unionId + "\"}";
            String content = HttpClientUtil.postJson(url,body);
            log.info("getbyunionid获取钉钉用户信息结果：" + content);
            JSONObject object= JSONObject.parseObject(content);
            //出错则返回为空
            if(!"0".equals((object.get("errcode").toString()))){
                return null;
            }
            JSONObject userInfo=object.getJSONObject("result");
            return userInfo.getString("userid");

        }catch (Exception e){
            log.error("getbyunionid获取钉钉用户信息出错：",e);
        }
        return null;
    }
    /**
     * @Description: API获取钉钉用户信息
     * @param token access_token
     * @param code 钉钉授权获取到的code
     * @return java.lang.String 钉钉用户userId
     * @Author: Elwin ZHANG @Date: 2021/4/22 17:26
     **/
    public String getUserId(String token,String code){
        String url="https://oapi.dingtalk.com/user/getuserinfo?access_token=" + token + "&code=" + code;
        try {
            //缓存中不存在，则通过API获取新的TOKEN
            String content = HttpClientUtil.getFromUrl(url, null);
            log.info("getuserinfo获取钉钉用户信息结果：" + content);
            JSONObject object= JSONObject.parseObject(content);
            //出错则返回为空
            if(!"0".equals((object.get("errcode").toString()))){
                return null;
            }
            String userId=object.getString("userid");
            return userId;
        }catch (Exception e){
            log.error("getuserinfo获取钉钉用户信息出错：",e);
        }
        return null;
    }


    /**
     * @Description: 获取某租户钉钉token
     * @Author: Elwin ZHANG 2021-4-22
     * @param appKey
     * @param agentSecret 钉钉的应用密钥
     * @param tenantId 租户ID
     * @return java.lang.String
     */
    public String getToken(String appKey,String agentSecret,String tenantId){
        //先从缓存获取
        String token=getTokeFromCache( tenantId);
        if(StringUtils.isNotEmpty(token)){
            return token;
        }
        //不存在则通过API获取
        return getAccessToken(appKey,agentSecret,tenantId);
    }

    /**
     * @Description: API获取钉钉的TOKEN
     * @Author: Elwin ZHANG
     * @Date: 2021/4/14 9:55
     * @param appKey 应用的唯一标识key
     * @param appSecret: 钉钉的应用密钥
     * @param tenantId : 租户ID
     * @return: com.alibaba.fastjson.JSONObject
     **/
    private  String getAccessToken(String appKey,String appSecret,String tenantId)  {
        String url="https://oapi.dingtalk.com/gettoken?appkey=" + appKey + "&appsecret=" + appSecret;
        try {
            //缓存中不存在，则通过API获取新的TOKEN
            String content = HttpClientUtil.getFromUrl(url, null);
            log.info("获取钉钉Token结果：" + content);
            JSONObject object= JSONObject.parseObject(content);
            return refreshTokenCache(tenantId,object);
        }catch (Exception e){
            log.info("获取钉钉Token出错：",e);
        }
        return null;
    }

    /**
     * @Description: 获取某租户缓存的钉钉token
     * @Author: Elwin ZHANG
     * @Date: 2021/4/22 11:21
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
     * @Date: 2021/4/22 10:49
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
            log.info("解析钉钉Token参数值时出错",e);
            return null;
        }
    }

    /**
     * @Description: 获取部门用户信息
     * @param token : access_token
     * @Date: 2021/5/24 16:55
     * @return: java.lang.String
     **/
    public String getUserInfo(String token,String deptid){
        String url="https://oapi.dingtalk.com/topapi/v2/user/list?access_token="+token;
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("dept_id",deptid);
            map.put("cursor","0");
            map.put("size","30");
            String result = HttpClientUtil.postFromUrl(url,map);
            log.info("根据token获取钉钉部门用户信息结果：" + result);
            JSONObject object= JSONObject.parseObject(result);
            //出错则返回为空
            if(!"0".equals((object.get("errcode").toString()))){
                return null;
            }
            JSONObject list = (JSONObject) object.get("result");
            return list.getString("list");
        }catch (Exception e){
            log.info("API获取企业钉钉部门用户信息出错：",e);
            return null;
        }
    }


    /**
     * @Description: 获取所有部门信息
     * @param token : access_token
     * @Date: 2021/5/21 16:55
     * @return: java.lang.String
     **/
    public String getDepartment(String token){
        String url="https://oapi.dingtalk.com/department/list?access_token="+token;
        try {
            String result = HttpClientUtil.getFromUrl(url,null);
            log.info("根据token获取钉钉所有部门信息结果：" + result);
            JSONObject object= JSONObject.parseObject(result);
            //出错则返回为空
            if(!"0".equals((object.get("errcode").toString()))){
                return null;
            }
            return object.getString("department");
        }catch (Exception e){
            log.info("API获取企业钉钉所有部门的信息出错：",e);
            return null;
        }
    }

}
