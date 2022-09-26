package com.redxun.system.messagehandler.impl;

import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ExceptionUtil;
import com.redxun.dto.user.OsUserDto;
import com.redxun.mq.MessageModel;
import com.redxun.system.feign.BpmClient;
import com.redxun.system.messagehandler.IMessageHandler;
import com.redxun.system.messagehandler.MessageType;
import com.redxun.system.messagehandler.MessageUtil;
import com.redxun.util.InfInnerMsgUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 内部消息推送
 */
@Component
@Slf4j
public class InfInnerMsgHandler implements IMessageHandler {

    @Autowired
    BpmClient bpmClient;
    @Override
    public MessageType getType() {
        return new MessageType( "inner","内部消息");
    }

    @Override
    public void handMessage(MessageModel messageModel) {
        //发送人Id
        String fromUserId= messageModel.getSender().getUserId();
        if(StringUtils.isEmpty(fromUserId)){
            return;
        }
        //发送内容
        String content="";
        //接收人
        for(OsUserDto receiver:messageModel.getReceivers()){
            if(receiver==null || StringUtils.isEmpty(receiver.getUserId())){
                continue;
            }
            try{
                String subject=messageModel.getSubject();
                if(StringUtils.isEmpty(subject)){
                    subject= MessageUtil.getInformSubject(messageModel, receiver);
                }
                if(StringUtils.isNotEmpty(subject)){
                    messageModel.setSubject(subject);
                }else{
                    messageModel.setSubject("");
                }
                content  = MessageUtil.getContent(messageModel, receiver, getType().getType());
                InfInnerMsgUtil.sendSystemMsg(receiver,content,messageModel.getSubject());
            }
            catch (Exception ex){
                String message= ExceptionUtil.getExceptionMessage(ex);
                log.error(message);
            }
        }

    }
}
