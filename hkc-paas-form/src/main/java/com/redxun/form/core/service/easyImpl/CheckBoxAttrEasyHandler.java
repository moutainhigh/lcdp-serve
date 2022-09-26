package com.redxun.form.core.service.easyImpl;

import com.alibaba.fastjson.JSONObject;
import com.redxun.form.bo.entity.FormBoAttr;
import org.springframework.stereotype.Component;

@Component("easyCheckBoxAttrHandler")
public class CheckBoxAttrEasyHandler extends BaseAttrEasyHandler {

    @Override
    public String getPluginName() {
        return "checkbox";
    }

    @Override
    public String getDescription() {
        return "复选框";
    }

    @Override
    public FormBoAttr parse(JSONObject jsonObject) {
        FormBoAttr attr=super.parse(jsonObject);
        JSONObject options=jsonObject.getJSONObject("options");
        String ctlType=options.getString("ctlType");
        if("checkbox".equals(ctlType)){
            //单一复选
            attr.setIsSingle(1);
            attr.setLength(20);
        }else if("checkboxlist".equals(ctlType)){
            //设置值模式
            setValMode(jsonObject,attr);
        }
        return attr;
    }

    @Override
    protected void setInitData(FormBoAttr attr, JSONObject setting, JSONObject jsonObject) {

    }
}
