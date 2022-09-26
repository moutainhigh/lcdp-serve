package com.redxun.system.messagehandler;

import com.redxun.mq.MessageModel;

/**
 * 消息处理器接口
 */
public interface IMessageHandler {

    /**
     * 消息类型。
     * @return
     */
    MessageType getType();

    /**
     * 处理消息数据。
     */
    void handMessage(MessageModel messageModel);
}
