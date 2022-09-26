package com.redxun.form.core.service.easyImpl;

import com.alibaba.fastjson.JSONObject;
import com.redxun.dboperator.model.Column;
import com.redxun.form.bo.entity.FormBoAttr;
import org.springframework.stereotype.Component;

@Component("easyBarcodeAttrHandler")
public class BarcodeAttrEasyHandler extends BaseAttrEasyHandler {
    @Override
    public String getPluginName() {
        return "barcode";
    }

    @Override
    public String getDescription() {
        return "生成二维码";
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
    protected void setInitData(FormBoAttr attr, JSONObject setting, JSONObject jsonObject) {

    }
}