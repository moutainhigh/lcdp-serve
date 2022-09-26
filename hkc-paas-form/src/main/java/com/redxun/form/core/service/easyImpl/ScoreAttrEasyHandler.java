package com.redxun.form.core.service.easyImpl;

import com.alibaba.fastjson.JSONObject;
import com.redxun.dboperator.model.Column;
import com.redxun.form.bo.entity.FormBoAttr;
import org.springframework.stereotype.Component;

@Component("easyScoreAttrHandler")
public class ScoreAttrEasyHandler extends BaseAttrEasyHandler {
    @Override
    public String getPluginName() {
        return "score";
    }

    @Override
    public String getDescription() {
        return "评分控件";
    }

    @Override
    public FormBoAttr parse(JSONObject jsonObject) {
        FormBoAttr attr=super.parse(jsonObject);
        attr.setDataType(Column.COLUMN_TYPE_NUMBER);
        attr.setLength(5);
        attr.setDecimalLength(1);
        attr.setIsSingle(1);
        return attr;
    }

    @Override
    protected void setInitData(FormBoAttr attr, JSONObject setting, JSONObject jsonObject) {

    }
}