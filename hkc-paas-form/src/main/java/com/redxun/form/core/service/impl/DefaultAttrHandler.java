package com.redxun.form.core.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.redxun.form.bo.entity.FormBoAttr;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

@Component
public class DefaultAttrHandler extends BaseAttrHandler {
    @Override
    protected void setInitData(FormBoAttr attr, JSONObject setting, JSONObject jsonObject) {

    }

    @Override
    protected void parseFormSetting(FormBoAttr field, Element el, JSONObject jsonObject) {

    }

    @Override
    protected void parseDataSetting(FormBoAttr field, Element el, JSONObject jsonObject) {

    }

    @Override
    protected void parseAttrSetting(FormBoAttr field, Element el, JSONObject jsonObject) {

    }

    @Override
    public String getPluginName() {
        return "default";
    }

    @Override
    public String getDescription() {
        return "默认的处理器";
    }
}
