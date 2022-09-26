package com.redxun.system.messagehandler.impl;

import com.redxun.api.org.IOrgService;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ExceptionUtil;
import com.redxun.dto.user.OsUserDto;
import com.redxun.dto.user.OsWxEntAgentDto;
import com.redxun.mq.MessageModel;
import com.redxun.system.messagehandler.IMessageHandler;
import com.redxun.system.messagehandler.MessageType;
import com.redxun.system.messagehandler.MessageUtil;
import com.redxun.util.wechat.WeChatTextCard;
import com.redxun.util.wechat.WeChatTextCardMsg;
import com.redxun.util.wechat.WeixinUtil;
import com.redxun.util.wechat.WxEntUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 微信发送处理器
 * @author ray
 */
@Component
@Slf4j
public class WeixinHandler implements IMessageHandler {

    @Autowired
    IOrgService orgService;
    @Autowired
    WxEntUtil wxEntUtil;

    @Override
    public MessageType getType() {
        return new MessageType("weixin", "微信");
    }

    @Override
    public void handMessage(MessageModel messageModel) {

        String tenantId=messageModel.getTenantId();
        if(StringUtils.isEmpty(tenantId)){
            return ;
        }
        OsWxEntAgentDto agent = orgService.getDefaultAgent(tenantId);
        if(agent==null){
            return;
        }
        try {
            for (OsUserDto receiver : messageModel.getReceivers()) {
                if (receiver == null || StringUtils.isEmpty(receiver.getAccount())) {
                    continue;
                }
                String wxOpenId = receiver.getWxOpenId();
                if (StringUtils.isEmpty(wxOpenId)) {
                    continue;
                }
                try {
                    WeChatTextCardMsg msg = getMsg(messageModel, receiver, agent, wxOpenId);
                    String token=wxEntUtil.getToken(agent.getCorpId(),agent.getSecret(), tenantId);
                    WeixinUtil.sendMsg(token, msg);
                } catch (Exception ex) {
                    String message = ExceptionUtil.getExceptionMessage(ex);
                    log.error("--WeixinHandler.handMessage is error wxOpenId="+wxOpenId+" ---message=:" + message);
                }
            }
        } catch (Exception ex) {
            String message = ExceptionUtil.getExceptionMessage(ex);
            log.error("--WeixinHandler.handMessage is error message=:" + message);
        }
    }


    /**
     * 获取消息。
     *
     * @param messageModel
     * @param agent
     * @return
     * @throws Exception
     */
    private WeChatTextCardMsg getMsg(MessageModel messageModel, OsUserDto receiver, OsWxEntAgentDto agent, String wxAccount) throws Exception {
        String content = MessageUtil.getContent(messageModel, receiver, this.getType().getType());
        WeChatTextCardMsg cardMsg = new WeChatTextCardMsg();
        cardMsg.setAgentid(agent.getAgentId());
        cardMsg.setTouser(wxAccount);
        WeChatTextCard card = new WeChatTextCard();

        card.setTitle(messageModel.getSubject());
        card.setDescription(content);
        card.setBtntxt(messageModel.getBtntxt());

        String url = messageModel.getUrl() ;
        String params="entwxid=" +wxAccount + "&tenantId=" +receiver.getTenantId();
        //带上openId, 第一次打开也不用登录了
        url+=(url.indexOf("?")>-1)? "&" : "?";
        url+=params;

        card.setUrl(url);

        cardMsg.setTextcard(card);
        return cardMsg;
    }
}
