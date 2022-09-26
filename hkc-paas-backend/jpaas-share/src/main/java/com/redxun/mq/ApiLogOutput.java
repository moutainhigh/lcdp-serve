package com.redxun.mq;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * API日志发送消息。
 */
public interface ApiLogOutput {

    /**
     * MessageModel的消息收发。
     */
    String OUTPUT = "apiLogOut";

    @Output(OUTPUT)
    MessageChannel logOutput();
}
