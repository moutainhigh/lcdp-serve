package com.redxun.form.core.service.easyImpl;

import com.alibaba.fastjson.JSONObject;
import com.redxun.common.base.entity.IUser;
import com.redxun.common.utils.ContextUtil;
import com.redxun.form.bo.entity.FormBoAttr;
import org.springframework.stereotype.Component;

@Component("easyGroupAttrHandler")
public class GroupAttrEasyHandler extends BaseAttrEasyHandler {

    @Override
    public String getPluginName() {
        return "group";
    }

    @Override
    public String getDescription() {
        return "部门控件";
    }

    @Override
    public FormBoAttr parse(JSONObject jsonObject) {
        FormBoAttr attr=super.parse(jsonObject);
        attr.setIsSingle(0);
        return attr;
    }

    @Override
    protected void setInitData(FormBoAttr attr, JSONObject setting, JSONObject jsonObject) {
        JSONObject options=setting.getJSONObject("options");
        boolean curgroup   = options.getBoolean("curgroup");
        if(curgroup){
            IUser user= ContextUtil.getCurrentUser();
            JSONObject obj=new JSONObject();
            obj.put("label",user.getDeptName());
            obj.put("value",user.getDeptId());
            jsonObject.put(attr.getName(),obj.toJSONString());
        }
    }
}