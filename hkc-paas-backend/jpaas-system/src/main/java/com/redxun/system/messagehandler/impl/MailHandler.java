package com.redxun.system.messagehandler.impl;


import com.redxun.common.mail.MailUtil;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ExceptionUtil;
import com.redxun.dto.user.OsUserDto;
import com.redxun.mq.MessageModel;
import com.redxun.system.feign.BpmClient;
import com.redxun.system.messagehandler.IMessageHandler;
import com.redxun.system.messagehandler.MessageType;
import com.redxun.system.messagehandler.MessageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 处理邮件消息。
 * @author ray
 */
@Component
@Slf4j
public class MailHandler implements IMessageHandler {

    @Autowired
    BpmClient bpmClient;
    @Resource
    private MailUtil mailUtil;
    @Resource
    private JavaMailSenderImpl mailSender;
    @Value("${spring.mail.username}")
    private String username;


    @Override
    public MessageType getType() {
        return new MessageType( "mail","邮件");
    }

    @Override
    public void handMessage(MessageModel messageModel) {
        if(StringUtils.isEmpty(username)){
            return;
        }
        for(OsUserDto receiver:messageModel.getReceivers()){
            if(receiver==null || StringUtils.isEmpty(receiver.getEmail())){
                continue;
            }
            try{
                String subject= MessageUtil.getInformSubject(messageModel, receiver);
                if(StringUtils.isNotEmpty(subject)){
                    messageModel.setSubject(subject);
                }
                String content  = MessageUtil.getContent(messageModel, receiver, getType().getType());
                mailUtil.sendMail(username,receiver.getEmail(),messageModel.getSubject(),content);
            }
            catch (Exception ex){
                String message= ExceptionUtil.getExceptionMessage(ex);
                log.error("--MailHandler.mailUtil.sendMail is error message=: "+message);
            }
        }
    }
}
