package com.redxun.mq;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

/**
 * 消息队列扩展。
 */
public interface BpmInputOutput {
    /**
     * 流程异步事件消息收发。
     */
    String OUTPUT_EVENT = "eventOutput";
    String INPUT_EVENT = "eventInput";

    @Output(OUTPUT_EVENT)
    MessageChannel eventOutput();
    @Input(INPUT_EVENT)
    SubscribableChannel eventInput();


}
