package com.redxun.form.core.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.redxun.common.base.entity.IUser;
import com.redxun.common.utils.ContextUtil;
import com.redxun.form.bo.entity.FormBoAttr;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;


@Component
public class GroupAttrHandler extends  BaseAttrHandler {

    private final String CURGROUP="curgroup";

    @Override
    protected void setInitData(FormBoAttr attr, JSONObject setting, JSONObject jsonObject) {
        JSONObject dataJson= attr.getDataJson();
        boolean curGroup= dataJson.getBoolean(CURGROUP);
        if(curGroup){
            IUser user= ContextUtil.getCurrentUser();
            JSONObject obj=new JSONObject();
            obj.put("label",user.getDeptName());
            obj.put("value",user.getDeptId());
            jsonObject.put(attr.getName(),obj.toJSONString());
        }
    }

    @Override
    protected void parseFormSetting(FormBoAttr field, Element el, JSONObject jsonObject) {

    }

    @Override
    protected void parseDataSetting(FormBoAttr field, Element el, JSONObject jsonObject) {
        String curGroup=jsonObject.getString(CURGROUP);
        JSONObject json=new JSONObject();
        json.put(CURGROUP,curGroup);
        field.setDataJson(json);
    }

    @Override
    protected void parseAttrSetting(FormBoAttr field, Element el, JSONObject jsonObject) {
        field.setIsSingle(0);
    }

    @Override
    public String getPluginName() {
        return "rx-group";
    }

    @Override
    public String getDescription() {
        return "部门控件";
    }
}
