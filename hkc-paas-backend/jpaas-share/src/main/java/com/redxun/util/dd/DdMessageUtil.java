package com.redxun.util.dd;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.dingtalk.api.response.OapiMessageCorpconversationAsyncsendV2Response;
import com.taobao.api.ApiException;

/**
 * 钉钉消息工具类。
 */
public class DdMessageUtil {

    /**
     * 发送钉钉消息。
     * @param message
     * @param token
     * @return
     * @throws ApiException
     */
    public static OapiMessageCorpconversationAsyncsendV2Response sendMessage(DdMessage message, String token) throws ApiException {

        //获取访问token。

        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/message/corpconversation/asyncsend_v2");
        OapiMessageCorpconversationAsyncsendV2Request request = new OapiMessageCorpconversationAsyncsendV2Request();
        //选择钉钉消息通知人
        request.setUseridList(message.getReceivers());
        request.setAgentId(Long.valueOf(message.getAgentId()));
        request.setToAllUser(false);


        request.setMsg(message.getMsg());
        OapiMessageCorpconversationAsyncsendV2Response response = client.execute(request, token);
        return response;
    }

    /**
     * 构建连接消息。
     * @param subject
     * @param content
     * @param url
     * @param picUrl
     * @return
     */
    public static OapiMessageCorpconversationAsyncsendV2Request.Msg buildLinkMsg(String subject,String content,String url,String picUrl){
        OapiMessageCorpconversationAsyncsendV2Request.Msg msg=new OapiMessageCorpconversationAsyncsendV2Request.Msg();
            msg.setMsgtype("link");
		msg.setLink(new OapiMessageCorpconversationAsyncsendV2Request.Link());

		msg.getLink().setTitle(subject);
		msg.getLink().setText(content);
		msg.getLink().setMessageUrl(url);
		msg.getLink().setPicUrl(picUrl);

		return msg;
    }


    /**
     * 构建文本消息。
     * @param content
     * @return
     */
    public static OapiMessageCorpconversationAsyncsendV2Request.Msg buildTextMsg(String content){
        OapiMessageCorpconversationAsyncsendV2Request.Msg msg=new OapiMessageCorpconversationAsyncsendV2Request.Msg();
        msg.setMsgtype("text");
        OapiMessageCorpconversationAsyncsendV2Request.Text text= new OapiMessageCorpconversationAsyncsendV2Request.Text();
        text.setContent(content);
        msg.setText(text);

        return msg;
    }

    /**
     * 构建markdown消息。
     * @param title
     * @param content
     * @return
     */
    public static OapiMessageCorpconversationAsyncsendV2Request.Msg buildMarkDownMsg(String title,String content){
        OapiMessageCorpconversationAsyncsendV2Request.Msg msg=new OapiMessageCorpconversationAsyncsendV2Request.Msg();
        msg.setMsgtype("markdown");
        OapiMessageCorpconversationAsyncsendV2Request.Markdown markdown= new OapiMessageCorpconversationAsyncsendV2Request.Markdown();

        markdown.setTitle(title);
        markdown.setText(content);

        msg.setMarkdown(markdown);

        return msg;
    }

    /**
     * 构建card消息。
     * @param title
     * @param content
     * @param singleTitle
     * @param url
     * @return
     */
    public static OapiMessageCorpconversationAsyncsendV2Request.Msg buildCardMsg(String title,String content,String singleTitle,String url){
        OapiMessageCorpconversationAsyncsendV2Request.Msg msg=new OapiMessageCorpconversationAsyncsendV2Request.Msg();
        msg.setMsgtype("action_card");
        OapiMessageCorpconversationAsyncsendV2Request.ActionCard card= new OapiMessageCorpconversationAsyncsendV2Request.ActionCard();

        card.setTitle(title);
        card.setMarkdown(content);
        card.setSingleTitle(singleTitle);
        card.setSingleUrl(url);

        msg.setActionCard(card);


        return msg;
    }



}
