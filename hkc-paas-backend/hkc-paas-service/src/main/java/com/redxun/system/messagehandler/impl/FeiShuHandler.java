package com.redxun.system.messagehandler.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.redxun.api.org.IOrgService;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ExceptionUtil;
import com.redxun.common.utils.SysPropertiesUtil;
import com.redxun.dto.user.OsFsAgentDto;
import com.redxun.dto.user.OsUserDto;
import com.redxun.mq.MessageModel;
import com.redxun.system.messagehandler.IMessageHandler;
import com.redxun.system.messagehandler.MessageType;
import com.redxun.system.messagehandler.MessageUtil;
import com.redxun.util.feishu.FeiShuClient;
import com.redxun.util.feishu.entity.FeiShuSendMessageReq;
import com.redxun.util.feishu.enums.UserIdTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 微信发送处理器
 * @author ray
 */
@Component
@Slf4j
public class FeiShuHandler implements IMessageHandler {

    @Autowired
    IOrgService orgService;
    @Autowired
    FeiShuClient feiShuClient;

    @Override
    public MessageType getType() {
        return new MessageType("feishu", "飞书");
    }

    @Override
    public void handMessage(MessageModel messageModel) {

        String tenantId=messageModel.getTenantId();
        if(StringUtils.isEmpty(tenantId)){
            return ;
        }
        OsFsAgentDto agent=orgService.getFsDefaultAgent(tenantId);
        if(agent==null){
            return;
        }
        try {
            for (OsUserDto receiver : messageModel.getReceivers()) {
                if (receiver == null || StringUtils.isEmpty(receiver.getAccount())) {
                    continue;
                }
                String fsOpenId = receiver.getFsOpenId();
                if (StringUtils.isEmpty(fsOpenId)) {
                    continue;
                }
                try {
                    String token=feiShuClient.getTenantAccessToken(agent.getAppId(),agent.getSecret());
                    FeiShuSendMessageReq req = getCardMsg(messageModel, receiver);
                    feiShuClient.sendMessage(token, UserIdTypeEnum.OPEN_ID,req);
                   /* FeiShuSendMessageReq req2 = getMsg(messageModel, receiver);
                    feiShuClient.sendMessage(token, UserIdTypeEnum.OPEN_ID,req2);*/
                } catch (Exception ex) {
                    String message = ExceptionUtil.getExceptionMessage(ex);
                    log.error("--FeiShuHandler.handMessage is error openId="+fsOpenId+" ---message=:" + message);
                }
            }
        } catch (Exception ex) {
            String message = ExceptionUtil.getExceptionMessage(ex);
            log.error("--FeiShuHandler.handMessage is error message=:" + message);
        }
    }


    /**
     * 获取消息。
     *
     * @param messageModel
     * @param receiver
     * @return
     * @throws Exception
     */
    private FeiShuSendMessageReq getMsg(MessageModel messageModel, OsUserDto receiver) throws Exception {
        String content = MessageUtil.getContent(messageModel, receiver, this.getType().getType());
        String mobile_url= SysPropertiesUtil.getString("mobileUrl");
        FeiShuSendMessageReq req = new FeiShuSendMessageReq();
        req.setMsgType("post");

        req.setReceiveId(receiver.getFsOpenId());
        /**
         *{
         * 	"zh_cn": {
         * 		"title": "我是一个标题",
         * 		"content": [
         * 			[{
         * 					"tag": "text",
         * 					"text": "第一行 :"
         *                                },
         *                {
         * 					"tag": "a",
         * 					"href": "http://www.feishu.cn",
         * 					"text": "超链接"
         *                },
         *                {
         * 					"tag": "at",
         * 					"user_id": "ou_1avnmsbv3k45jnk34j5",
         * 					"user_name": "tom"
         *                }
         * 			]
         * 		]
         * 	}
         * }
         */
        JSONObject jsonObject = new JSONObject();
        JSONObject zhCnJsonObject = new JSONObject();
        JSONArray contentJsonArray = new JSONArray();
        JSONArray firstJsonArray = new JSONArray();
        JSONObject obj1 = new JSONObject();
        obj1.put("tag", "text");
        obj1.put("text", content);
        JSONObject obj2 = new JSONObject();
        obj2.put("tag", "a");
        obj2.put("href", messageModel.getUrl());
        obj2.put("text", "点击处理");
        JSONObject obj3 = new JSONObject();
        obj3.put("tag", "at");
        obj3.put("user_id", receiver.getFsOpenId());
        firstJsonArray.add(obj1);
        firstJsonArray.add(obj2);
        firstJsonArray.add(obj3);
        contentJsonArray.add(firstJsonArray);
        zhCnJsonObject.put("title", "通知");
        zhCnJsonObject.put("content", contentJsonArray);
        jsonObject.put("zh_cn", zhCnJsonObject);

        req.setContent(jsonObject.toJSONString());

        return req;
    }
    /**
     * 获取卡片消息。
     *
     * @param messageModel
     * @param receiver
     * @return
     * @throws Exception
     */
    private FeiShuSendMessageReq getCardMsg(MessageModel messageModel, OsUserDto receiver) throws Exception {
        String content = MessageUtil.getContent(messageModel, receiver, this.getType().getType());
        FeiShuSendMessageReq req = new FeiShuSendMessageReq();
        req.setMsgType("interactive");
        req.setReceiveId(receiver.getFsOpenId());
        /**
         *{
         * 	"config": {
         * 		"wide_screen_mode": true
         *        },
         * 	"elements": [{
         * 		"alt": {
         * 			"content": "",
         * 			"tag": "plain_text"
         *        },
         * 		"img_key": "img_7ea74629-9191-4176-998c-2e603c9c5e8g",
         * 		"tag": "img"
         *    }, {
         * 		"tag": "div",
         * 		"text": {
         * 			"content": "你是否曾因为一本书而产生心灵共振，开始感悟人生？\n你有哪些想极力推荐给他人的珍藏书单？\n\n加入 **4·23 飞书读书节**，分享你的**挚爱书单**及**读书笔记**，**赢取千元读书礼**！\n\n📬 填写问卷，晒出你的珍藏好书\n😍 想知道其他人都推荐了哪些好书？马上[入群围观](https://open.feishu.cn/)\n📝 用[读书笔记模板](https://open.feishu.cn/)（桌面端打开），记录你的心得体会\n🙌 更有惊喜特邀嘉宾 4月12日起带你共读",
         * 			"tag": "lark_md"
         *        }
         *    }, {
         * 		"actions": [{
         * 			"tag": "button",
         * 			"text": {
         * 				"content": "立即推荐好书",
         * 				"tag": "plain_text"
         *            },
         * 			"type": "primary",
         * 			"url": "https://open.feishu.cn/"
         *        }, {
         * 			"tag": "button",
         * 			"text": {
         * 				"content": "查看活动指南",
         * 				"tag": "plain_text"
         *            },
         * 			"type": "default",
         * 			"url": "https://open.feishu.cn/"
         *        }],
         * 		"tag": "action"
         *    }],
         * 	"header": {
         * 		"template": "turquoise",
         * 		"title": {
         * 			"content": "📚晒挚爱好书，赢读书礼金",
         * 			"tag": "plain_text"
         *        }
         *    }
         * }
         */
        JSONObject jsonObject = new JSONObject();
        //配置部分
        /*JSONObject configJsonObj = new JSONObject();
        configJsonObj.put("wide_screen_mode", true);
        jsonObject.put("config", configJsonObj);*/
        //内容部分
        JSONArray elementsJsonArray = new JSONArray();
        JSONObject elementsJsonObj = new JSONObject();
        elementsJsonObj.put("tag", "div");
        JSONObject elementsJsonObjText = new JSONObject();
        elementsJsonObjText.put("content", content +"<a href='"+messageModel.getUrl()+"'>点击处理</a>");
        elementsJsonObjText.put("tag", "lark_md");
        elementsJsonObj.put("text", elementsJsonObjText);
        elementsJsonArray.add(elementsJsonObj);
        jsonObject.put("elements", elementsJsonArray);
        //内容部分
        /*JSONArray elementsJsonArray2 = new JSONArray();
        JSONObject elementsJsonObj2 = new JSONObject();
        elementsJsonObj2.put("tag", "div");
        JSONObject elementsJsonObjText2 = new JSONObject();
        elementsJsonObjText2.put("content", content);
        elementsJsonObjText2.put("tag", "lark_md");
        elementsJsonObj.put("text", elementsJsonObjText2);
        elementsJsonArray.add(elementsJsonObj2);
        jsonObject.put("elements", elementsJsonArray2);*/


        //actions部分
       /* JSONArray actionsJsonArray = new JSONArray();
        JSONObject actionsJsonObj = new JSONObject();
        actionsJsonObj.put("tag", "button");
        actionsJsonObj.put("type", "primary");
        actionsJsonObj.put("url", messageModel.getUrl());
        JSONObject actionsJsonObjText = new JSONObject();
        actionsJsonObjText.put("content", "立即处理");
        actionsJsonObjText.put("tag", "plain_text");
        actionsJsonObj.put("text", actionsJsonObjText);
        actionsJsonArray.add(actionsJsonObj);
        jsonObject.put("actions", actionsJsonArray);
        jsonObject.put("tag", "action");*/
        //header部分
        JSONObject headerJsonObj = new JSONObject();
        headerJsonObj.put("template", "turquoise");
        JSONObject headerJsonObjTitle = new JSONObject();
        headerJsonObjTitle.put("content", "通知");
        headerJsonObjTitle.put("tag", "plain_text");
        headerJsonObj.put("title", headerJsonObjTitle);
        jsonObject.put("header", headerJsonObj);

        req.setContent(jsonObject.toJSONString());
        return req;
    }
}
