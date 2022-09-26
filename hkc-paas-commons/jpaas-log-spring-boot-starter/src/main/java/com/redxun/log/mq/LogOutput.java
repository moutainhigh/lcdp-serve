package com.redxun.log.mq;


import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * 日志发送消息。
 */
public interface LogOutput {

        /**
         * MessageModel的消息收发。
         */
        String OUTPUT = "logOutput";

        @Output(OUTPUT)
        MessageChannel logOutput();


}
