package com.redxun.form.core.service.easyImpl;

import com.alibaba.fastjson.JSONObject;
import com.redxun.form.bo.entity.FormBoAttr;
import org.springframework.stereotype.Component;

@Component("easyButtonEditAttrHandler")
public class ButtonEditAttrEasyHandler extends BaseAttrEasyHandler {
    @Override
    public String getPluginName() {
        return "buttonEdit";
    }

    @Override
    public String getDescription() {
        return "编辑型按钮";
    }

    @Override
    public FormBoAttr parse(JSONObject jsonObject) {
        FormBoAttr attr=super.parse(jsonObject);
        attr.setIsSingle(0);
        return attr;
    }

    @Override
    protected void setInitData(FormBoAttr attr, JSONObject setting, JSONObject jsonObject) {

    }
}
