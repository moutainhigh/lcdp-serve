package com.redxun.form.core.service.easyImpl;

import com.alibaba.fastjson.JSONObject;
import com.redxun.form.bo.entity.FormBoAttr;
import org.springframework.stereotype.Component;

@Component("easySwitchAttrHandler")
public class SwitchAttrEasyHandler extends BaseAttrEasyHandler {

    @Override
    public String getPluginName() {
        return "switch";
    }

    @Override
    public String getDescription() {
        return "开关";
    }

    @Override
    public FormBoAttr parse(JSONObject jsonObject) {
        FormBoAttr attr=super.parse(jsonObject);
        attr.setIsSingle(1);
        attr.setLength(20);
        return attr;
    }

    @Override
    protected void setInitData(FormBoAttr attr, JSONObject setting, JSONObject jsonObject) {

    }
}
