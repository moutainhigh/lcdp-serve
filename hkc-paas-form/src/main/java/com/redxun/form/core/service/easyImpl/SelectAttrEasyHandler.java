package com.redxun.form.core.service.easyImpl;

import com.alibaba.fastjson.JSONObject;
import com.redxun.form.bo.entity.FormBoAttr;
import org.springframework.stereotype.Component;

@Component("easySelectAttrHandler")
public class SelectAttrEasyHandler extends BaseAttrEasyHandler {

    @Override
    public String getPluginName() {
        return "select";
    }

    @Override
    public String getDescription() {
        return "下拉选择器";
    }

    @Override
    public FormBoAttr parse(JSONObject jsonObject) {
        FormBoAttr attr = super.parse(jsonObject);
        //设置值模式
        setValMode(jsonObject,attr);

        return attr;
    }

    @Override
    protected void setInitData(FormBoAttr attr, JSONObject setting, JSONObject jsonObject) {

    }
}
