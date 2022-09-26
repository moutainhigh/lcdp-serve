package com.redxun.util.dd;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.request.OapiGettokenRequest;
import com.dingtalk.api.response.OapiGettokenResponse;
import com.redxun.cache.CacheUtil;
import com.redxun.common.tool.StringUtils;
import com.taobao.api.ApiException;

import java.util.HashMap;
import java.util.Map;

/**
 * 钉钉工具类。
 */
public class DdUtil {

    private static String DING_TOKEN_KEY="ding_token_key";

    //钉钉token失效时间(7200秒)
    private static Integer DING_TOKEN_EXPIRE_TIME=7200000;

    private static  final String REGION="dd";



    /**
     * 根据appKey和密钥获取访问的token。
     *
     * @return
     * @throws Exception
     */
    public static String getAccessToken (String appKey,String secret) throws ApiException {
        String accessToken="";
        Map<String,Object> tokenMapCache=(Map<String, Object>) CacheUtil.get(REGION,DING_TOKEN_KEY);
        if(tokenMapCache!=null){
            accessToken=tokenMapCache.get("accessToken")==null?"":tokenMapCache.get("accessToken").toString();
            Long oldTime=tokenMapCache.get("curTime")==null?0:Long.parseLong(tokenMapCache.get("curTime").toString());
            Long curTime=System.currentTimeMillis();
            //过期时间之内
            if(StringUtils.isNotEmpty(accessToken)&&(curTime-oldTime)<DING_TOKEN_EXPIRE_TIME){
                return accessToken;
            }
        }
        //没有缓存  就发请求获取
        DefaultDingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/gettoken");
        OapiGettokenRequest request = new OapiGettokenRequest();

        if(StringUtils.isEmpty(appKey)|| StringUtils.isEmpty(secret)){
            throw new ApiException("没有配置钉钉应用id或秘钥");
        }
        request.setAppkey(appKey);
        request.setAppsecret(secret);
        request.setHttpMethod("GET");
        OapiGettokenResponse response = client.execute(request);
        if(response.getErrcode()!=0){
            throw new ApiException(response.getErrmsg());
        }
        Long curTime=System.currentTimeMillis();
        //获取响应的Token
        accessToken=response.getAccessToken();
        Map<String,Object> tokenMap=new HashMap<String,Object>();
        tokenMap.put("curTime",curTime);
        tokenMap.put("accessToken",accessToken);
        CacheUtil.set(REGION,DING_TOKEN_KEY,tokenMap);
        return accessToken;
    }

}
