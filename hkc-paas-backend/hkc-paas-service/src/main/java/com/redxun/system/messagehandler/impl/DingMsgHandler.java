package com.redxun.system.messagehandler.impl;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.dingtalk.api.response.OapiMessageCorpconversationAsyncsendV2Response;
import com.redxun.api.org.IOrgService;
import com.redxun.common.utils.ExceptionUtil;
import com.redxun.common.utils.SysPropertiesUtil;
import com.redxun.dto.user.OsDdAgentDto;
import com.redxun.dto.user.OsUserDto;
import com.redxun.mq.MessageModel;
import com.redxun.system.messagehandler.IMessageHandler;
import com.redxun.system.messagehandler.MessageType;
import com.redxun.system.messagehandler.MessageUtil;
import com.redxun.util.dd.DingDingUtil;
import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 钉钉消息处理器
 */
@Component
@Slf4j
public class DingMsgHandler implements IMessageHandler {

	@Autowired
    IOrgService orgService;
	@Autowired
    DingDingUtil dingDingUtil;

	@Override
	public MessageType getType() {
		return new MessageType("ding", "钉钉");
	}

	/**
	 * 处理钉钉的消息推送
	 * @param messageModel
	 */
	@Override
	public void handMessage(MessageModel messageModel)  {
		Map<String, Object> vars = messageModel.getVars();
		String tenantId=messageModel.getTenantId();
		if(StringUtils.isEmpty(tenantId)){
			return;
		}
		OsDdAgentDto agent=orgService.getDdAgent(tenantId);
		//钉钉移动端放置路径
		String mobile_url= SysPropertiesUtil.getString("mobileUrl");
		for (OsUserDto receiver : messageModel.getReceivers()) {
			if (receiver == null || StringUtils.isEmpty(receiver.getAccount())) {
				continue;
			}

			String ddId = receiver.getDdId();
			if (StringUtils.isEmpty(ddId)) {
				continue;
			}
			try {
				String content = MessageUtil.getContent(messageModel, receiver, this.getType().getType());
				sendMsg(vars,agent,ddId,mobile_url+messageModel.getUrl(),content,messageModel.getSubject(),tenantId);
			} catch (Exception ex) {
				String message = ExceptionUtil.getExceptionMessage(ex);
				log.error("--DingMsgHandler.handMessage is error userNo="+receiver.getUserNo()+" ---message=:" + message);
			}
		}
	}

	/**
	 * 发送钉钉的消息
	 * @param vars 变量
	 * @param agent 代理
	 * @param userStr 发送人
	 * @param moblieProjectUrl 移动端访问URL
	 * @param content 内容
	 * @param subject 标题
	 * @param tenantId 租户ID
	 * @throws ApiException
	 */
	public void sendMsg(Map<String, Object> vars, OsDdAgentDto agent, String userStr, String moblieProjectUrl,
                        String content, String subject, String tenantId) throws ApiException {
		DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/message/corpconversation/asyncsend_v2");
		OapiMessageCorpconversationAsyncsendV2Request request = new OapiMessageCorpconversationAsyncsendV2Request();
		//选择钉钉消息通知人
		request.setUseridList(userStr);
		request.setAgentId(Long.valueOf(agent.getAgentId()));
		request.setToAllUser(false);

		OapiMessageCorpconversationAsyncsendV2Request.Msg msg = new OapiMessageCorpconversationAsyncsendV2Request.Msg();

		//钉钉消息链接
		msg.setMsgtype("link");
		msg.setLink(new OapiMessageCorpconversationAsyncsendV2Request.Link());
		msg.getLink().setTitle(subject);
		msg.getLink().setText(content);
		msg.getLink().setMessageUrl(moblieProjectUrl);
		msg.getLink().setPicUrl("1");
		request.setMsg(msg);
		String token= dingDingUtil.getToken(agent.getAppKey(),agent.getSecret(),tenantId);
		OapiMessageCorpconversationAsyncsendV2Response response = client.execute(request, token);
		if(response.getErrcode()!=0){
			log.error("钉钉消息发送错误:"+response.getErrmsg());
		}else{
			log.debug("钉钉消息推送成功，工号:"+userStr);
		}
	}
}
