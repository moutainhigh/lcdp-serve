package com.redxun.form.core.service.easyImpl;

import com.alibaba.fastjson.JSONObject;
import com.redxun.dboperator.model.Column;
import com.redxun.form.bo.entity.FormBoAttr;
import org.springframework.stereotype.Component;

@Component("easyTreeSelectAttrHandler")
public class TreeSelectAttrEasyHandler extends BaseAttrEasyHandler {
    @Override
    public String getPluginName() {
        return "treeSelect";
    }

    @Override
    public String getDescription() {
        return "下拉树控件";
    }

    @Override
    public FormBoAttr parse(JSONObject jsonObject) {
        FormBoAttr attr = super.parse(jsonObject);
        JSONObject options = jsonObject.getJSONObject("options");
        attr.setDataType(Column.COLUMN_TYPE_VARCHAR);
        attr.setLength(options.getInteger("length"));
        //设置值模式
        setValMode(jsonObject,attr);

        return attr;
    }

    @Override
    protected void setInitData(FormBoAttr attr, JSONObject setting, JSONObject jsonObject) {

    }
}
