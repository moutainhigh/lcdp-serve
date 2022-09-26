package com.redxun.form.core.service.export;

import com.alibaba.fastjson.JSONObject;
import com.redxun.form.core.entity.FormMobile;
import com.redxun.form.core.service.FormMobileServiceImpl;
import com.redxun.web.controller.IExport;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class FormMobileExport implements IExport {
    @Resource
    FormMobileServiceImpl formMobileServiceImpl;
    @Override
    public JSONObject doExportById(String id, StringBuilder sb) {
        JSONObject json = new JSONObject();
        FormMobile formMobile =formMobileServiceImpl. get(id);
        //添加日志
        sb.append(formMobile.getName() +"("+id+"),");

        json.put("formMobile", formMobile);
        return json;
    };
}
