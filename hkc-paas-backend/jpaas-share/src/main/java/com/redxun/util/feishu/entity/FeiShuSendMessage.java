package com.redxun.util.feishu.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.ArrayList;

/**
 * 飞书发送信息返回实体类
 * @author ycs
 * @version V1.0
 * @since 2022-06-13 16:20
 */
@Data
public class FeiShuSendMessage {
    /**
     * 消息id
     */
    @JSONField(name = "message_id")
    private String messageId;
    /**
     * 根消息id
     */
    @JSONField(name = "root_id")
    private String rootId;
    /**
     * 父消息的id
     */
    @JSONField(name = "parent_id")
    private String parentId;
    /**
     * 消息类型 包括：text、post、image、file、audio、media、sticker、interactive、share_chat、share_user等
     */
    @JSONField(name = "msg_type")
    private String msgType;
    /**
     * 消息生成的时间戳（毫秒）
     */
    @JSONField(name = "create_time")
    private String createTime;
    /**
     * 消息更新的时间戳（毫秒）
     */
    @JSONField(name = "update_time")
    private String updateTime;
    /**
     * 消息是否被撤回
     */
    private Boolean deleted;
    /**
     * 消息是否被更新
     */
    private Boolean updated;
    /**
     * 所属的群
     */
    @JSONField(name = "chat_id")
    private String chatId;
    /**
     * 发送者，可以是用户或应用
     */
    private Sender sender;
    /**
     * 合并转发消息中，上一层级的消息id message_id
     */
    @JSONField(name = "upper_message_id")
    private String upperMessageId;

    /**
     * 消息内容
     */
    private MessageBody  body;

    /**
     * 被@的用户或机器人的id列表
     */
    private ArrayList<Mention> mentions;
    @Data
    public class Sender{
        /**
         * 该字段标识发送者的id
         */
       private String id;

        /**
         * 该字段标识发送者的类型
         */
        @JSONField(name = "sender_type")
        private String senderType;
        /**
         * 为租户在飞书上的唯一标识，用来换取对应的tenant_access_token，也可以用作租户在应用里面的唯一标识
         */
        @JSONField(name = "tenant_key")
        private String tenantKey;

    }
    @Data
    public class MessageBody{
        /**
         * 消息内容，json结构序列化后的字符串。不同msg_type对应不同内容
         */
        private String content;

    }
    @Data
    public class Mention {
        /**
         * 被@的用户或机器人的序号。例如，第3个被@到的成员，值为“@_user_3”
         */
        private String key;

        /**
         * 被@的用户或者机器人的open_id
         */
        private String id;
        /**
         * 被@的用户或机器人 id 类型，目前仅支持 open_id
         */
        @JSONField(name = "id_type")
        private String idType;

        /**
         * 被@的用户或机器人的姓名
         */
        private String name;

        /**
         * 为租户在飞书上的唯一标识，用来换取对应的tenant_access_token，也可以用作租户在应用里面的唯一标识
         */
        @JSONField(name = "tenantKey")
        private String tenant_key;

    }

}
