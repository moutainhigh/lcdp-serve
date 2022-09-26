package com.redxun.form.core.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.redxun.common.base.entity.IUser;
import com.redxun.common.utils.ContextUtil;
import com.redxun.form.bo.entity.FormBoAttr;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

@Component
public class UserAttrHandler extends  BaseAttrHandler  {

    private final String CURUSER="curuser";

    @Override
    protected void setInitData(FormBoAttr attr, JSONObject setting, JSONObject jsonObject) {
        JSONObject dataJson= attr.getDataJson();
        boolean curUser= dataJson.getBoolean(CURUSER);
        if(curUser){
            IUser user= ContextUtil.getCurrentUser();
            JSONObject obj=new JSONObject();
            obj.put("label",user.getFullName());
            obj.put("value",user.getUserId());
            jsonObject.put(attr.getName(),obj.toJSONString());
        }
    }

    @Override
    protected void parseFormSetting(FormBoAttr field, Element el, JSONObject jsonObject) {

    }

    @Override
    protected void parseDataSetting(FormBoAttr field, Element el, JSONObject jsonObject) {
        String curUser=jsonObject.getString(CURUSER);
        JSONObject json=new JSONObject();
        json.put(CURUSER,curUser);
        field.setDataJson(json);
    }

    @Override
    protected void parseAttrSetting(FormBoAttr field, Element el, JSONObject jsonObject) {
        field.setIsSingle(0);
    }

    @Override
    public String getPluginName() {
        return "rx-user";
    }

    @Override
    public String getDescription() {
        return "用户控件";
    }
}
