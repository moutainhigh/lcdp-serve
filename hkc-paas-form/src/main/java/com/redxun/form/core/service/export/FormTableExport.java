package com.redxun.form.core.service.export;

import com.alibaba.fastjson.JSONObject;
import com.redxun.form.core.entity.FormMobile;
import com.redxun.form.core.entity.FormTableFormula;
import com.redxun.form.core.service.FormMobileServiceImpl;
import com.redxun.form.core.service.FormTableFormulaServiceImpl;
import com.redxun.web.controller.IExport;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class FormTableExport implements IExport {
    @Resource
    FormTableFormulaServiceImpl formTableFormulaService;
    @Override
    public JSONObject doExportById(String id, StringBuilder sb) {
        JSONObject json = new JSONObject();
        FormTableFormula formTableFormula = formTableFormulaService.get(id);
        //添加日志
        sb.append(formTableFormula.getName() +"("+id+"),");

        json.put("formTableFormula", formTableFormula);
        return json;
    };
}
