package com.redxun.system.mq;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * 错误日志消息读取。
 * @author ray
 */
public interface ErrLogInput {

    String INPUT_FILED = "errLogInput";

    @Input(INPUT_FILED)
    SubscribableChannel errLogInput();
}
