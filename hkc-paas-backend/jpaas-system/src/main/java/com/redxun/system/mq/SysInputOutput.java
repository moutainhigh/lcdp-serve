package com.redxun.system.mq;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

/**
 * 文件转换消息处理。
 */
public interface SysInputOutput {


    /**
     * 文件转化的消息收发。
     */
    String OUTPUT_FILED = "fileOutput";
    String INPUT_FILED = "fileInput";



    @Output(OUTPUT_FILED)
    MessageChannel filedOutput();
    @Input(INPUT_FILED)
    SubscribableChannel filedInput();

}
