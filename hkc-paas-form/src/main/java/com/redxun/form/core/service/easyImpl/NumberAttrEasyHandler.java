package com.redxun.form.core.service.easyImpl;

import com.alibaba.fastjson.JSONObject;
import com.redxun.form.bo.entity.FormBoAttr;
import org.springframework.stereotype.Component;

@Component("easyNumberAttrHandler")
public class NumberAttrEasyHandler extends BaseAttrEasyHandler {

    @Override
    public String getPluginName() {
        return "number";
    }

    @Override
    public String getDescription() {
        return "数字输入框";
    }

    @Override
    public FormBoAttr parse(JSONObject jsonObject) {
        FormBoAttr attr=super.parse(jsonObject);
        attr.setIsSingle(1);
        return attr;
    }

    @Override
    protected void setInitData(FormBoAttr attr, JSONObject setting, JSONObject jsonObject) {

    }
}
