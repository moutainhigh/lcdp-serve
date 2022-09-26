package com.redxun.form.core.service.impl;

import com.alibaba.druid.proxy.jdbc.ClobProxyImpl;
import com.alibaba.fastjson.JSONObject;
import com.redxun.common.utils.FileUtil;
import com.redxun.form.bo.entity.FormBoAttr;
import com.redxun.form.core.entity.ValueResult;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class MarkdownAttrHandler extends BaseAttrHandler {


    @Override
    public String getPluginName() {
        return "rx-markdown";
    }

    @Override
    public String getDescription() {
        return "MARKDOWN";
    }

    @Override
    public ValueResult getValue(FormBoAttr attr, Map<String, Object> rowData, boolean isExternal) {
        String fieldName=attr.getFieldName().toUpperCase();
        if(rowData.containsKey(fieldName)){
            Object val=rowData.get(fieldName);
            if (val instanceof ClobProxyImpl) {
                ClobProxyImpl clob = (ClobProxyImpl) val;
                val = FileUtil.clobToString(clob);
            }
            return  ValueResult.exist(val);
        }
        else{
            return  ValueResult.noExist();
        }
    }

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
}
