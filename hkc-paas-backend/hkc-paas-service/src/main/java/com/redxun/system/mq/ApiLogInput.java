package com.redxun.system.mq;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * API消费日志消息。
 */
public interface ApiLogInput {

    String INPUT_FILED = "apiLogInput";

    @Input(INPUT_FILED)
    SubscribableChannel apiLogInput();
}
