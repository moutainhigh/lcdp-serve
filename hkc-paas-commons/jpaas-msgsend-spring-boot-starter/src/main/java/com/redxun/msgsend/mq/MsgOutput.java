package com.redxun.msgsend.mq;


import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * 日志发送消息。
 */
public interface MsgOutput {

        /**
         * MessageModel的消息收发。
         */
        String OUTPUT = "msgOutput";

        @Output(OUTPUT)
        MessageChannel msgOutput();


}
