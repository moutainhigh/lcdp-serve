package com.redxun.log.mq;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * 日志接收消息队列。
 */
public interface LogInput {

    /**
     * MessageModel的消息收发。
     */
    String INPUT = "logInput";



    @Input(INPUT)
    SubscribableChannel logInput();
}
