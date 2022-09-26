package com.redxun.msgsend.util;

import com.redxun.common.utils.ExceptionUtil;
import com.redxun.common.utils.SpringUtil;
import com.redxun.msgsend.mq.MsgOutput;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.support.MessageBuilder;

/**
 * 消息统一发送操作类。
 */
@Slf4j
public class MesAutoUtil {

    public static void sendMessage(String model){
        try {
            MsgOutput mgsInputOutput = SpringUtil.getBean(MsgOutput.class);
            mgsInputOutput.msgOutput().send(MessageBuilder.withPayload(model).build());
        }catch (Exception e){
            log.error("MesAutoUtil.sendMessage is error. message={}", ExceptionUtil.getExceptionMessage(e));
        }

    }
}
