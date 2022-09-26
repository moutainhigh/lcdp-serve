package com.redxun.form.core.service.easyImpl;

import com.alibaba.fastjson.JSONObject;
import com.redxun.dboperator.model.Column;
import com.redxun.form.bo.entity.FieldEntity;
import com.redxun.form.bo.entity.FormBoAttr;
import com.redxun.form.core.entity.ValueResult;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component("easySignAttrHandler")
public class SignAttrEasyHandler extends BaseAttrEasyHandler {
    @Override
    public String getPluginName() {
        return "sign";
    }

    @Override
    public String getDescription() {
        return "签名控件";
    }

    @Override
    public FormBoAttr parse(JSONObject jsonObject) {
        FormBoAttr attr=super.parse(jsonObject);
        attr.setDataType(Column.COLUMN_TYPE_VARCHAR);
        attr.setLength(50);
        attr.setIsSingle(1);
        return attr;
    }

    @Override
    public ValueResult getValue(FormBoAttr attr, Map<String, Object> rowData, boolean isExternal) {
        return super.getValue(attr, rowData, isExternal);
    }

    @Override
    public List<FieldEntity> getFieldEntity(FormBoAttr attr, JSONObject json) {
        return super.getFieldEntity(attr, json);
    }

    @Override
    protected void setInitData(FormBoAttr attr, JSONObject setting, JSONObject jsonObject) {

    }
}