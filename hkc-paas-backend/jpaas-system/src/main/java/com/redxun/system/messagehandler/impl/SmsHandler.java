package com.redxun.system.messagehandler.impl;

import com.redxun.api.org.IOrgService;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ExceptionUtil;
import com.redxun.common.utils.SmsVerificationUtil;
import com.redxun.dto.user.OsUserDto;
import com.redxun.mq.MessageModel;
import com.redxun.system.feign.BpmClient;
import com.redxun.system.messagehandler.IMessageHandler;
import com.redxun.system.messagehandler.MessageType;
import com.redxun.system.messagehandler.MessageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 短信发送处理器
 * @author ray
 */
@Component
@Slf4j
public class SmsHandler implements IMessageHandler {

    @Autowired
    BpmClient bpmClient;
    @Autowired
    IOrgService orgService;
    @Resource
    private SmsVerificationUtil smsVerificationUtil;

    @Override
    public MessageType getType() {
        return new MessageType("sms", "短信");
    }

    @Override
    public void handMessage(MessageModel messageModel) {
        log.debug("--SmsHandler.handMessage is begin---");
        try {
            for (OsUserDto receiver : messageModel.getReceivers()) {
                if (receiver == null || StringUtils.isEmpty(receiver.getAccount())) {
                    continue;
                }
                String mobile = receiver.getMobile();
                if (StringUtils.isEmpty(mobile)) {
                    continue;
                }

                String subject= MessageUtil.getInformSubject(messageModel, receiver);
                if(StringUtils.isNotEmpty(subject)){
                    messageModel.setSubject(subject);
                }
                String content  = MessageUtil.getContent(messageModel, receiver, getType().getType());
                //需要定好模板
                String[] params ={content};
                smsVerificationUtil.getSmsByKey("qcloud").sendBpmApproval(mobile,params);
            }
        } catch (Exception ex) {
            String message = ExceptionUtil.getExceptionMessage(ex);
            log.error("--SmsHandler.handMessage is error message=:" + message);
        }

    }
}
