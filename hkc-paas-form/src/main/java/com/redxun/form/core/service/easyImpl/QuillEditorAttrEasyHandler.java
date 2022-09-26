package com.redxun.form.core.service.easyImpl;

import com.alibaba.fastjson.JSONObject;
import com.redxun.dboperator.model.Column;
import com.redxun.form.bo.entity.FormBoAttr;
import org.springframework.stereotype.Component;

@Component("easyQuillEditorAttrHandler")
public class QuillEditorAttrEasyHandler extends BaseAttrEasyHandler {
    @Override
    public String getPluginName() {
        return "quillEditor";
    }

    @Override
    public String getDescription() {
        return "富文本控件";
    }

    @Override
    public FormBoAttr parse(JSONObject jsonObject) {
        FormBoAttr attr=super.parse(jsonObject);
        attr.setDataType(Column.COLUMN_TYPE_CLOB);
        attr.setLength(0);
        attr.setIsSingle(1);
        return attr;
    }

    @Override
    protected void setInitData(FormBoAttr attr, JSONObject setting, JSONObject jsonObject) {

    }
}