package com.redxun.form.core.service.easyImpl;

import com.alibaba.fastjson.JSONObject;
import com.redxun.dboperator.model.Column;
import com.redxun.form.bo.entity.FormBoAttr;
import org.springframework.stereotype.Component;

@Component("easyMapAttrHandler")
public class MapAttrEasyHandler extends BaseAttrEasyHandler {
    @Override
    public String getPluginName() {
        return "map";
    }

    @Override
    public String getDescription() {
        return "地图控件";
    }

    @Override
    public FormBoAttr parse(JSONObject jsonObject) {
        FormBoAttr attr=super.parse(jsonObject);
        JSONObject options = jsonObject.getJSONObject("options");
        attr.setDataType(Column.COLUMN_TYPE_VARCHAR);
        attr.setLength(options.getInteger("length"));
        attr.setIsSingle(1);
        return attr;
    }

    @Override
    protected void setInitData(FormBoAttr attr, JSONObject setting, JSONObject jsonObject) {

    }
}