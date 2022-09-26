package com.redxun.util.dd;

import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import lombok.Getter;
import lombok.Setter;


/**
 * 钉钉消息。
 */
@Setter
@Getter
public class DdMessage {

    //发送消息时使用的微应用的AgentID
    private String agentId;
    //接收者的userid列表
    private String receivers;

    OapiMessageCorpconversationAsyncsendV2Request.Msg msg;

}
