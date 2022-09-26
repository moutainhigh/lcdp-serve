package com.redxun.util.wechat;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.utils.HttpClientUtil;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 企业微信工具类。
 *
 * @author ray
 */
public class WeixinUtil {


    /**
     * 通过公众号ID和应用密钥获取网页授权access_token
     * @param appid
     * @param secret
     * @return {"access_token":"ACCESS_TOKEN","expires_in":7200,"refresh_token":"REFRESH_TOKEN","openid":"OPENID","scope":"SCOPE"}
     * error {"errcode":40029,"errmsg":"invalid code"}
     * @throws Exception
     */
    public static String getGzhWebAccessToken(String appid,String secret,String code) throws Exception{
        String url=WeChatQyApiUrl.getGzhWebAccessToken(appid,secret,code);
        String rtnCode=HttpClientUtil.getFromUrl(url, null);
        return rtnCode;
    }

    /**
     * 通过企业ID和应用密钥获取网页授权access_token
     * @param corpId
     * @param corpsecret
     * @return {"access_token":"ACCESS_TOKEN","expires_in":7200,"refresh_token":"REFRESH_TOKEN","openid":"OPENID","scope":"SCOPE"}
     * error {"errcode":40029,"errmsg":"invalid code"}
     * @throws Exception
     */
    public static String getWebAccessToken(String corpId,String corpsecret) throws Exception{
        String url=WeChatQyApiUrl.getWebAccessToken(corpId,corpsecret);
        String rtnCode=HttpClientUtil.getFromUrl(url, null);
        return rtnCode;
    }
    /**
     * 通过授权ACCESSTOKEN以及code获取用户详细信息
     * @param accessToken
     * @param code
     * @return
     * @throws Exception
     */
    public static String getUserInfoByWebAccessTokenAndCode(String accessToken,String code) throws Exception{
        String url=WeChatQyApiUrl.getUserInfoByWebAccessTokenAndCode(accessToken, code);
        String rtnCode=HttpClientUtil.getFromUrl(url, null);
        return rtnCode;
    }


    /**
     * 根据企业密钥和code获取当前用户信息。
     *
     * @param corpId 企业ID
     * @param secret 密钥
     * @param code   微信提供的code
     * @return 数据格式：
     * {"UserId":"ray","DeviceId":"","errcode":0,"errmsg":"ok"}
     * @throws Exception
     */
    public static String getUserByCode(String corpId, String secret, String code) throws Exception {
        WeChatTokenModel tokenModel = WeChatTokenUtil.getEntToken(corpId, secret);
        String token = tokenModel.getToken();
        String url = WeChatQyApiUrl.getUserId(token, code);
        String json = HttpClientUtil.getFromUrl(url, null);
        return json;
    }

    /**
     * 上传文件到微信。
     *
     * @param folder   上传的文件路径
     * @param fileName 上传的文件名
     * @param token    token
     * @param type     上传类型
     *                 （image）、语音（voice）、视频（video），普通文件(file)
     * @return {
     * "errcode": 0,
     * "errmsg": ""，
     * "type": "image",
     * "media_id": "1G6nrLmr5EC3MMb_-zK1dDdzmd0p7cNliYu9V5w7o8K0",
     * "created_at": "1380000000"
     * }
     * @throws IOException
     */
    /*public static String uploadFile(String folder, String fileName, String token, String type) throws IOException {
        String url = WeChatQyApiUrl.getUploadUrl(token, type);
        Map<String, String> fileMap = new HashMap<String, String>();
        fileMap.put("media", fileName);
        String rtn = HttpClientUtil.uploadFile(url, folder, fileMap);
        return rtn;

    }*/

    /**
     * 下载附件。
     * @param token            token
     * @param id            附件ID
     * @param fileName        存储本地的文件名
     * @throws IOException
     */
	/*public static void downLoad(String token,String id ,String fileName) throws IOException{
		String url=ApiUrl.getDownloadUrl(token, id);
		HttpClientUtil.downLoad(url, fileName);
	}*/

    /**
     * 发送消息。
     * @param token        会话令牌
     * @param baseMsg    消息数据
     * @return
     * {
     *	   "errcode" : 0,
     *	   "errmsg" : "ok",
     *	   "invaliduser" : "UserID1", // 不区分大小写，返回的列表都统一转为小写
     *	   "invalidparty" : "PartyID1",
     *    }
     * @throws Exception
     */
    public static String sendMsg(String token,WeChatBaseMsg baseMsg) throws Exception{
        String url=WeChatQyApiUrl.getSendMsgUrl(token);
        String rtn=  HttpClientUtil.postJson(url, baseMsg.toString());
        return rtn;
    }

    /**
     * 根据企业ID和应用的密钥获取密钥ID信息。
     *
     * @param corpId
     * @param secret
     * @return
     * @throws Exception
     */
    public static JsonResult<Void> getAppId(String corpId, String secret) throws Exception {
        JsonResult<Void> result = new JsonResult<Void>(true);
        try {
            WeChatTokenModel token = WeChatTokenUtil.getEntToken(corpId, secret);
            String url = WeChatQyApiUrl.getAppUrl(token.getToken());
            String json = HttpClientUtil.getFromUrl(url, null);
            JSONObject jsonObj = JSONObject.parseObject(json);
            int errorCode = jsonObj.getIntValue("errcode");
            if (errorCode == 0) {
                JSONArray jsonAry = jsonObj.getJSONArray("agentlist");
                JSONObject rtnObj = jsonAry.getJSONObject(0);
                String agentId = rtnObj.getString("agentid");
                result.setMessage(agentId);
            } else {
                result.setSuccess(false);
                result.setMessage(jsonObj.getString("errmsg"));
            }
            return result;
        } catch (Exception ex) {
            result.setSuccess(false);
            result.setMessage(ex.getMessage());
            return result;
        }
    }

    /**
     * 获取应用明细信息。
     *
     * @param corpId
     * @param secret
     * @return
     * @throws Exception
     */
    public static JsonResult<Void> getAppInfoById(String corpId, String secret) throws Exception {
        JsonResult<Void> result = getAppId(corpId, secret);
        if (!result.isSuccess()) {
            return result;
        }
        String appId = result.getMessage();
        WeChatTokenModel token = WeChatTokenUtil.getEntToken(corpId, secret);

        String url = WeChatQyApiUrl.getAppInfoByIdUrl(token.getToken(), appId);
        String json = HttpClientUtil.getFromUrl(url, null);
        JSONObject jsonObj = JSONObject.parseObject(json);

        int errorCode = jsonObj.getIntValue("errcode");
        if (errorCode == 0) {
            result.setMessage(json);
        } else {
            result.setMessage(jsonObj.getString("errmsg"));
        }
        return result;
    }

    /**
     * 设置APPINFO消息。
     *
     * @param corpId
     * @param secret
     * @param json
     * @return
     * @throws Exception
     */
    public static JSONObject setAppInfo(String corpId, String secret, String json) throws Exception {
        WeChatTokenModel token = WeChatTokenUtil.getEntToken(corpId, secret);
        String url = WeChatQyApiUrl.getAgentSetUrl(token.getToken());
        String rtnJson = HttpClientUtil.postJson(url, json);
        return JSONObject.parseObject(rtnJson);
    }

    /**
     * 验证应用是否有效。
     * @param agentId
     * @param token
     * @return
     * @throws Exception
     */
    public static boolean validApp(String agentId,String token) throws Exception {
        String url = WeChatQyApiUrl.getAppInfoByIdUrl(token, agentId);
        String json = HttpClientUtil.getFromUrl(url, null);
        JSONObject jsonObj = JSONObject.parseObject(json);
        int errorCode = jsonObj.getIntValue("errcode");
        return  errorCode==0;
    }


    /**
     * 获取微信的配置。
     * 这个方法用于对JS SDK进行调用。
     *
     * @param url
     * @param corpId
     * @param secret
     * @return
     * @throws Exception
     */
    public static Map<String, String> getWxConfig(String url, String corpId, String secret) throws Exception {
        WeChatTokenModel tokenModel = WeChatTokenUtil.getEntTicket(corpId, secret);
        // 必填，生成签名的时间戳
        String timestamp = Long.toString(System.currentTimeMillis() / 1000);
        // 必填，生成签名的随机串
        String nonceStr = UUID.randomUUID().toString();
        String ticket = tokenModel.getToken();

        String signature = "";
        // 注意这里参数名必须全部小写，且必须有序
        String sign = "jsapi_ticket=" + ticket + "&noncestr=" + nonceStr + "&timestamp=" + timestamp + "&url=" + url;

        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(sign.getBytes("UTF-8"));
            signature = byteToHex(crypt.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Map<String, String> ret = new HashMap<String, String>();
        ret.put("appId", corpId);
        ret.put("timestamp", timestamp);
        ret.put("nonceStr", nonceStr);
        ret.put("signature", signature);
        return ret;
    }



    /**
     * 返回数据格式。
     * {
         "errcode":0,
         "errmsg":"ok",
         "checkindata": [{
         "userid" : "james",
         "groupname" : "打卡一组",
         "checkin_type" : "上班打卡",
         "exception_type" : "地点异常",
         "checkin_time" : 1492617610,
         "location_title" : "依澜府",
         "location_detail" : "四川省成都市武侯区益州大道中段784号附近",
         "wifiname" : "办公一区",
         "notes" : "路上堵车，迟到了5分钟"
         "wifimac" : "3c:46:d8:0c:7a:70"
         "mediaids":["WWCISP_G8PYgRaOVHjXWUWFqchpBqqqUpGj0OyR9z6WTwhnMZGCPHxyviVstiv_2fTG8YOJq8L8zJT2T2OvTebANV-2MQ"]
         },{
         "userid" : "paul",
         "groupname" : "打卡二组",
         "checkin_type" : "外出打卡",
         "exception_type" : "时间异常",
         "checkin_time" : 1492617620,
         "location_title" : "重庆出口加工区",
         "location_detail" : "重庆市渝北区金渝大道101号金渝大道",
         "wifiname" : "办公室二区",
         "notes" : ""
         "wifimac" : "3c:46:d8:0c:7a:71"
         "mediaids":["WWCISP_G8PYgRaOVHjXWUWFqchpBqqqUpGj0OyR9z6WTwhnMZGCPHxyviVstiv_2fTG8YOJq8L8zJT2T2OvTebANV-2MQ"]
         }]
     }
     * @param corpId
     * @param secret
     * @param model
     * @return
     * @throws Exception
     */
    public static String getSignData(String  corpId,String secret,SignParamModel model) throws Exception{
        WeChatTokenModel token = WeChatTokenUtil.getEntToken(corpId, secret);
        String url=WeChatQyApiUrl.getSignDataUrl(token.getToken());
        String rtnJson= HttpClientUtil.postJson(url, model.toString());

        return rtnJson;
    }


    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    public static void main(String[] args) throws Exception {


    }

}
