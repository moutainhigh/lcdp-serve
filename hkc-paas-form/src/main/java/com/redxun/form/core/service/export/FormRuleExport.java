package com.redxun.form.core.service.export;

import com.alibaba.fastjson.JSONObject;
import com.redxun.form.core.entity.FormRule;
import com.redxun.form.core.service.FormRuleServiceImpl;
import com.redxun.web.controller.IExport;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class FormRuleExport implements IExport {

    @Resource
    FormRuleServiceImpl formRuleService;

    @Override
    public JSONObject doExportById(String id,StringBuilder sb) {
        JSONObject json = new JSONObject();
        FormRule formRule = formRuleService.get(id);
        //添加日志
        sb.append(formRule.getName() +"("+id+"),");

        json.put("formRule", formRule);
        return json;
    }
}
