package com.redxun.system.mq;


import com.alibaba.fastjson.JSONObject;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ExceptionUtil;
import com.redxun.mq.MessageModel;
import com.redxun.msgsend.mq.MsgInput;
import com.redxun.system.messagehandler.IMessageHandler;
import com.redxun.system.messagehandler.MessageHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MsgConsumer {

    @Autowired
    JdbcTemplate jdbcTemplate;


    /**
     *统一消息发送处理。
     */
    @StreamListener(MsgInput.INPUT)
    public void handLog(String modelStr) {
        try {
            MessageModel messageModel = JSONObject.parseObject(modelStr, MessageModel.class);
            String msgType=messageModel.getMsgType();
            if(StringUtils.isEmpty(msgType)){
                return;
            }
            String[] aryType=msgType.split(",");
            for(String type:aryType){
                IMessageHandler messageHandler = MessageHandlerContext.getMessageHandler(type);
                if(messageHandler==null){
                    continue;
                }
                messageHandler.handMessage(messageModel);
            }
        }catch (Exception e){
            log.error("**** MsgConsumer.handLog is error :message={}", ExceptionUtil.getExceptionMessage(e));
        }

    }
}
