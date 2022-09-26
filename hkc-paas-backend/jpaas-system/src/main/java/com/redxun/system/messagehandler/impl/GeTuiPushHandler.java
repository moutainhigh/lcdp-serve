package com.redxun.system.messagehandler.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.redxun.common.tool.BeanUtil;
import com.redxun.dto.user.OsUserDto;
import com.redxun.mq.MessageModel;
import com.redxun.system.feign.BpmClient;
import com.redxun.system.messagehandler.IMessageHandler;
import com.redxun.system.messagehandler.MessageType;
import com.redxun.system.util.messageutil.PushMobileMsgUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 个推的消息推送
 */
@Component
public class GeTuiPushHandler implements IMessageHandler {

	@Autowired
    BpmClient bpmClient;

	@Override
	public MessageType getType() {
		return new MessageType( "getui","个推");
	}

	@Override
	public void handMessage(MessageModel messageModel) {
		String subject = messageModel.getSubject();
		String body = messageModel.getContent();

		String userList = bindMobileTypeToCid(messageModel);

		if(StringUtils.isEmpty(userList)) {
			return;
		}

		JSONObject json = new JSONObject();
		Map<String, Object> params= messageModel.getVars();
		if(params.containsKey("action")){
			json.put("action", params.get("action"));
			json.put("subject", subject);
			json.put("body", body);
		}
		try {
			PushMobileMsgUtil.pushMsgToUser(userList, subject, body, json.toJSONString());
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	// 组装Id字符串
	private String bindMobileTypeToCid(MessageModel messageModel){
		List<OsUserDto> userListObj=messageModel.getReceivers();
		if(BeanUtil.isEmpty(userListObj)) {
			return "";
		}
		List<String> userList = new ArrayList<String>(0);
		for(OsUserDto user:userListObj){
			userList.add(user.getUserId());
		}
		return StringUtils.join(userList, ",");
	}

	JSONObject getBycidList(JSONArray list){
		return  bpmClient.getBycidList(list);
	}
}
