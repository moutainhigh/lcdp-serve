package com.redxun.msgsend.mq;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * 日志接收消息队列。
 */
public interface MsgInput {

    /**
     * MessageModel的消息收发。
     */
    String INPUT = "msgInput";



    @Input(INPUT)
    SubscribableChannel msgInput();
}
