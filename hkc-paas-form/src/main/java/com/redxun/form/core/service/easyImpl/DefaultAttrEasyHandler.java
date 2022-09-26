package com.redxun.form.core.service.easyImpl;

import com.alibaba.fastjson.JSONObject;
import com.redxun.form.bo.entity.FormBoAttr;
import org.springframework.stereotype.Component;

@Component("easyDefaultAttrHandler")
public class DefaultAttrEasyHandler extends BaseAttrEasyHandler {

    @Override
    public String getPluginName() {
        return "default";
    }

    @Override
    public String getDescription() {
        return "默认的处理器";
    }

    @Override
    protected void setInitData(FormBoAttr attr, JSONObject setting, JSONObject jsonObject) {

    }
}
