package com.redxun.form.core.service.export;

import com.alibaba.fastjson.JSONObject;
import com.redxun.form.core.entity.FormBoList;
import com.redxun.form.core.entity.FormRule;
import com.redxun.form.core.service.FormBoListServiceImpl;
import com.redxun.web.controller.IExport;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class FormBoListExport implements IExport {
    @Resource
    FormBoListServiceImpl formBoListServiceImpl;

    @Override
    public JSONObject doExportById(String id, StringBuilder sb) {
        JSONObject json = new JSONObject();
        FormBoList formBoList = formBoListServiceImpl.get(id);
        //添加日志
        sb.append(formBoList.getName() +"("+id+"),");

        json.put("formBoList", formBoList);
        return json;
    }
}
