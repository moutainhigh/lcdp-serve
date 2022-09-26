package com.redxun.util.wechat;

import com.alibaba.fastjson.JSONObject;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.HttpClientUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * token工具。
 *
 * @author ray
 */
public class WeChatTokenUtil {


    private static String WEIXIN_TOKEN_MAP = "wexin_token_map_";

    private static String WEIXIN_JSAPI_MAP = "wexin_jsapi_map_";

    /**
     * 返回tokenmodel。
     *
     * @param corpId
     * @param secret
     * @return
     * @throws Exception
     */
    public static WeChatTokenModel getToken(String corpId, String secret, boolean isEnt) throws Exception {
        String key = corpId + "_" + secret;
        //获取微信的token缓存maps
        Map<String, WeChatTokenModel> tokenMap = (Map<String, WeChatTokenModel>) WeChatConfigCacheUtil.getCache(WEIXIN_TOKEN_MAP);
        if (tokenMap == null) {
            tokenMap = new HashMap<String, WeChatTokenModel>();
            WeChatTokenModel model = getTokenFromApi(corpId, secret, isEnt);
            if (model.getCode() != 0) {
                throw new RuntimeException("获取token异常:" + model.getMsg());
            }
            tokenMap.put(key, model);
            WeChatConfigCacheUtil.add(WEIXIN_TOKEN_MAP, tokenMap);
            return model;
        } else {
            WeChatTokenModel model = tokenMap.get(key);
            if (model == null || model.isExpire()) {
                //如果没有缓存或者缓存过期
                model = getTokenFromApi(corpId, secret, isEnt);
                tokenMap.put(key, model);
                WeChatConfigCacheUtil.add(WEIXIN_TOKEN_MAP, tokenMap);
            }
            if (model.getCode() != 0) {
                throw new RuntimeException("获取token异常:" + model.getMsg());
            }
            return model;
        }


    }

    /**
     * 获取企业token。
     *
     * @param corpId
     * @param secret
     * @return
     * @throws Exception
     */
    public static WeChatTokenModel getEntToken(String corpId, String secret) throws Exception {
        return getToken(corpId, secret, true);
    }

    /**
     * 获取公众号token。
     *
     * @param corpId
     * @param secret
     * @return
     * @throws Exception
     */
    public static WeChatTokenModel getPublicToken(String corpId, String secret) throws Exception {
        return getToken(corpId, secret, false);
    }


    /**
     * 正常格式
     * {
     * "errcode":0，
     * "errmsg":""，
     * "access_token": "accesstoken000001",
     * "expires_in": 7200
     * }
     * <p>
     * 出错格式
     * {
     * "errcode":40091,
     * "errmsg":"provider_secret is invalid"
     * }
     *
     * @param corpId
     * @param secret
     * @return
     * @throws Exception
     */
    private static WeChatTokenModel getTokenFromApi(String corpId, String secret, boolean isEnt) throws Exception {
        WeChatTokenModel token = new WeChatTokenModel();
        token.setCorpId(corpId);
        token.setSecret(secret);

        String url = "";
        if (isEnt) {
            url = WeChatQyApiUrl.getTokenUrl(corpId, secret);
        } else {
            url = WeChatPublicApiUrl.getAccessTokenUrl(corpId, secret);
        }


        String json = HttpClientUtil.getFromUrl(url, null);
        if (StringUtils.isEmpty(json)) {
            token.setCode(-1);
            token.setMsg("获取token出错了,请检查企业ID和密钥是否有正确!");
            return token;
        }
        JSONObject jsonObj = JSONObject.parseObject(json);

        int code = jsonObj.getIntValue("errcode");
        String errmsg = jsonObj.getString("errmsg");

        if (code == 0) {
            String accessToken = jsonObj.getString("access_token");
            token.setCode(code);
            token.setMsg(errmsg);
            token.setLastUpdateTime(new Date());
            token.setToken(accessToken);
        } else {
            token.setCode(code);
            token.setMsg(errmsg);
        }
        return token;

    }


    private static WeChatTokenModel getJsTicketFromApi(String corpId, String secret, boolean isEnt) throws Exception {
        WeChatTokenModel accessTokenModel = WeChatTokenUtil.getToken(corpId, secret, isEnt);
        if (accessTokenModel.getCode() != 0) {
            return null;
        }


        WeChatTokenModel token = new WeChatTokenModel();
        token.setCorpId(corpId);
        token.setSecret(secret);

        String url = "";
        if (isEnt) {
            url = WeChatQyApiUrl.getJsApiUrl(accessTokenModel.getToken());
        } else {
            url = WeChatPublicApiUrl.getJsApiUrl(accessTokenModel.getToken());
        }

        String json = HttpClientUtil.getFromUrl(url, null);
        if (StringUtils.isEmpty(json)) {
            token.setCode(-1);
            token.setMsg("获取token出错了,请检查APPID和密钥是否有正确!");
            return token;
        }
        JSONObject jsonObj = JSONObject.parseObject(json);

        int code = jsonObj.getIntValue("errcode");
        String errmsg = jsonObj.getString("errmsg");

        if (code == 0) {
            String accessToken = jsonObj.getString("ticket");
            token.setCode(code);
            token.setMsg(errmsg);
            token.setLastUpdateTime(new Date());
            token.setToken(accessToken);
        } else {
            token.setCode(code);
            token.setMsg(errmsg);
        }
        return token;

    }


    /**
     * 获取微信 js ticket。
     *
     * @param corpId 应用ID
     * @param secret 密钥
     * @param isEnt  是否企业微信
     * @return
     * @throws Exception
     */
    public static WeChatTokenModel getJsApiToken(String corpId, String secret, boolean isEnt) throws Exception {
        String key = corpId + "_" + secret;
        //获取微信的token缓存maps
        Map<String, WeChatTokenModel> tokenMap = (Map<String, WeChatTokenModel>) WeChatConfigCacheUtil.getCache(WEIXIN_JSAPI_MAP);
        if (tokenMap == null) {
            tokenMap = new HashMap<String, WeChatTokenModel>();
            WeChatTokenModel model = getJsTicketFromApi(corpId, secret, isEnt);
            if (model.getCode() != 0) {
                throw new RuntimeException("获取token异常:" + model.getMsg());
            }
            tokenMap.put(key, model);
            WeChatConfigCacheUtil.add(WEIXIN_JSAPI_MAP, tokenMap);
            return model;
        } else {
            WeChatTokenModel model = tokenMap.get(key);
            if (model == null || model.isExpire()) {
                //如果没有缓存或者缓存过期
                model = getJsTicketFromApi(corpId, secret, isEnt);
                tokenMap.put(key, model);
                WeChatConfigCacheUtil.add(WEIXIN_JSAPI_MAP, tokenMap);
            }
            if (model.getCode() != 0) {
                throw new RuntimeException("获取JS TICKET异常:" + model.getMsg());
            }
            return model;
        }
    }

    /**
     * 获取企业号ticket
     *
     * @param corpId
     * @param secret
     * @return
     * @throws Exception
     */
    public static WeChatTokenModel getEntTicket(String corpId, String secret) throws Exception {
        return getJsApiToken(corpId, secret, true);
    }

    /**
     * 获取公众号ticket
     *
     * @param corpId
     * @param secret
     * @return
     * @throws Exception
     */
    public static WeChatTokenModel getPuclicTicket(String corpId, String secret) throws Exception {
        return getJsApiToken(corpId, secret, false);
    }

}
