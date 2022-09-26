package com.redxun.mq;

import com.alibaba.fastjson.JSONObject;
import com.redxun.dto.user.OsUserDto;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter

/**
 * 统一消息消息模型。
 */
public class MessageModel {
    /**
     * 审批类型数据。
     */
    private String checkType="";
    /**
     * 表单数据。
     */
    private JSONObject boDataMap;
    /**
     * 消息发送者。
     */
    private OsUserDto sender;
    /**
     * 消息类型
     */
    private String msgType="";
    /**
     * 接收人。
     */
    private List<OsUserDto> receivers=new ArrayList<>();
    /**
     * 主题
     */
    private String subject="";

    /**
     * 如果内容存在则不考虑消息模板。
     * 否则需要根据消息类型从消息模板中构造消息内容。
     */
    private String content="";

    /**
     * 变量。api-uaa/
     */
    private Map<String, Object> vars=new HashMap<String, Object>();


    public void addVar(String varName,Object value){
        vars.put(varName,value);
    }

    public void addVars(Map<String,Object> vars){
        this.vars.putAll(vars);
    }

    /**
     * 租户ID
     */
    private String tenantId="";

    /**
     * 消息跳转url ,用于钉钉，微信有固定的URL
     */
    private String url="";

    /**
     * 消息按钮名称
     */
    private String btntxt="";

    /**
     * 模板字符串变量。
     * 模板key为 消息类型,value 为消息模板。
     */
    private Map<String, String> templateVars=new HashMap<String, String>();

    public void addTemplateVars(Map<String,String> templateVars){
        this.templateVars.putAll(templateVars);
    }

}
