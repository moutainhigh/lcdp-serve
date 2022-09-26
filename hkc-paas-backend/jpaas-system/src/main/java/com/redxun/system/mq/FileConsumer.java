package com.redxun.system.mq;


import com.redxun.system.ext.messagehandler.IMessageHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class FileConsumer {

    @Resource
    IMessageHandler messageHandler;

    /**
     * 处理openOffice文件转换。
     */
    @StreamListener(SysInputOutput.INPUT_FILED)
    public void handOpenOfficeFile(Object messageObj) {
        messageHandler.handOpenOfficeFile(messageObj);
    }
}
