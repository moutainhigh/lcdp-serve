package com.redxun.util;

import com.alibaba.fastjson.JSONObject;
import com.redxun.common.base.entity.IUser;
import com.redxun.common.utils.SpringUtil;
import com.redxun.feign.portal.PortalClient;

/**
 * 内部消息发送工具类
 */
public class InfInnerMsgUtil {

    /**
     * 发送系统内部消息
     * @param receiver 接收人
     * @param content   内容
     * @param msgTitle   标题
     */
    public static void sendSystemMsg(IUser receiver, String content, String msgTitle){
        try {
            PortalClient portalClient=SpringUtil.getBean(PortalClient.class);
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("recUserId",receiver.getUserId());
            jsonObject.put("tenantId",receiver.getTenantId());
            jsonObject.put("fullName",receiver.getFullName());
            jsonObject.put("content",content);
            jsonObject.put("msgTitle",msgTitle);
            portalClient.sendSystemMsg(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
