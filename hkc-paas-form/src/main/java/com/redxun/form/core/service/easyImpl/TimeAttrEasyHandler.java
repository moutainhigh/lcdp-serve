package com.redxun.form.core.service.easyImpl;

import com.alibaba.fastjson.JSONObject;
import com.redxun.dboperator.model.Column;
import com.redxun.form.bo.entity.FormBoAttr;
import org.springframework.stereotype.Component;

@Component("easyTimeAttrHandler")
public class TimeAttrEasyHandler extends BaseAttrEasyHandler {
    @Override
    public String getPluginName() {
        return "time";
    }

    @Override
    public String getDescription() {
        return "时间控件";
    }

    @Override
    public FormBoAttr parse(JSONObject jsonObject) {
        FormBoAttr attr = super.parse(jsonObject);
        attr.setDataType(Column.COLUMN_TYPE_VARCHAR);
        attr.setLength(20);
        return attr;
    }

    @Override
    protected void setInitData(FormBoAttr attr, JSONObject setting, JSONObject jsonObject) {

    }
}
