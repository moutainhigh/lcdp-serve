package com.redxun.bpm.core.ext.messagehandler;

import com.alibaba.fastjson.JSONObject;
import com.redxun.bpm.core.service.BpmMessageTemplateServiceImpl;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ExceptionUtil;
import com.redxun.common.utils.SpringUtil;
import com.redxun.common.utils.SysPropertiesUtil;
import com.redxun.dto.user.OsUserDto;
import com.redxun.mq.MessageModel;
import com.redxun.msgsend.util.MesAutoUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class MessageUtil {



    /**
     * 发送通知消息。
     * @param sendUser      发送人
     * @param subject       标题
     * @param noticeTypes   通知类型 mail,weixin
     * @param reveivers     接收人类型
     * @param vars          变量Map
     * @param templateType  模板类型
     * @param checkType     意见类型
     * @param boDataMap     业务数据
     */
    public static void sendMessage(OsUserDto sendUser, String subject, String noticeTypes,
                                    List<OsUserDto> reveivers, Map<String,Object> vars,String templateType,
                                   String checkType, JSONObject boDataMap){

        if(StringUtils.isEmpty(noticeTypes)){
            return;
        }
        MessageModel messageModel=new MessageModel();
        messageModel.setSender(sendUser);
        messageModel.setMsgType(noticeTypes);
        messageModel.setSubject(subject);

        messageModel.setCheckType(checkType);
        messageModel.setBoDataMap(boDataMap);

        messageModel.addVars(vars);
        messageModel.setReceivers(reveivers);
        messageModel.setTenantId(sendUser.getTenantId());

        String defId= (String) vars.get("defId");
        String nodeId= (String) vars.get("nodeId");

        BpmMessageTemplateServiceImpl bpmMessageTemplateService=SpringUtil.getBean(BpmMessageTemplateServiceImpl.class);
        Map<String, String> templateVars = bpmMessageTemplateService.getByBpmNode(defId, nodeId, templateType, noticeTypes);

        //消息类型和消息模板的映射
        messageModel.addTemplateVars(templateVars);

        getMessageUrl(messageModel,vars);

        MesAutoUtil.sendMessage(JSONObject.toJSONString(messageModel));
    }

    public static  void getMessageUrl(MessageModel messageModel,Map<String,Object> vars){
        //统一消息改造---szw
        String mobileUrl= SysPropertiesUtil.getString("mobileUrl");
        String instId=(String) vars.get("instId");
        String url = mobileUrl+"/pages/bpm/openInst?instId=" + instId;
        messageModel.setBtntxt("流程任务");
        messageModel.setUrl(url);
    }



    /**
     * @param sendUser
     * @param subject
     * @param noticeTypes
     * @param template
     * @param reveivers
     * @param vars
     */

    public static void sendMessage(OsUserDto sendUser, String subject, String noticeTypes,
                                   String template, List<OsUserDto> reveivers, Map<String,Object> vars){
        sendMessage(sendUser,subject,noticeTypes,reveivers,vars,template, "",null);
    }

    /**
     * 发送消息。
     * @param sendUser      发送人
     * @param subject       标题
     * @param noticeTypes   消息类型
     * @param template      模板
     * @param reveiver      接收人
     * @param vars          变量Map
     */
    public static void sendMessage(OsUserDto sendUser, String subject, String noticeTypes,
                                   String template, OsUserDto reveiver, Map<String,Object> vars,String checkType,JSONObject boDataMap){
        List<OsUserDto> reveivers=new ArrayList<>();
        reveivers.add(reveiver);
        sendMessage(sendUser,subject,noticeTypes,reveivers,vars,template,checkType,boDataMap);
    }


    public static void sendMessage(OsUserDto sendUser, String subject, String noticeTypes,
                                   String template, OsUserDto reveiver, Map<String,Object> vars){
        List<OsUserDto> reveivers=new ArrayList<>();
        reveivers.add(reveiver);
        sendMessage(sendUser,subject,noticeTypes,reveivers,vars,template,"",null);
    }

}
