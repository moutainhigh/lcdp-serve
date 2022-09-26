package com.redxun.util.feishu.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * 飞书发送消息参数
 * @author ycs
 * @version V1.0
 * @since 2022-06-13 16:20
 */
@Data
public class FeiShuSendMessageReq {
    /**
     * 接收者id
     */
    @JSONField(name = "receive_id")
    private String receiveId;
    /**
     * 消息内容，json结构序列化后的字符串。不同msg_type对应不同内容
     */
    private String content;

    /**
     * 消息类型包括：text、post、image、file、audio、media、sticker、interactive、share_chat、share_user
     */
    @JSONField(name = "msg_type")
    private String msgType;

}
