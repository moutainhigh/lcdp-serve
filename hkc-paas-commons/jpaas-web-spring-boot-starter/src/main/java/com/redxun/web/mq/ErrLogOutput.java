package com.redxun.web.mq;


import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * 错误日志发送消息。
 */
public interface ErrLogOutput {

        /**
         * MessageModel的消息收发。
         */
        String OUTPUT = "errLogOut";

        @Output(OUTPUT)
        MessageChannel logOutput();


}
