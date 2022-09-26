package com.redxun.system.core.export;

import com.alibaba.fastjson.JSONObject;
import com.redxun.system.core.entity.SysInvokeScript;
import com.redxun.system.core.service.SysInvokeScriptServiceImpl;
import com.redxun.web.controller.IExport;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class SysInvokeExport implements IExport {
    @Resource
    SysInvokeScriptServiceImpl sysInvokeScriptService;

    @Override
    public JSONObject doExportById(String id, StringBuilder sb) {

        JSONObject json = new JSONObject();
        SysInvokeScript sysInvokeScript =sysInvokeScriptService.get(id);

        sb.append(sysInvokeScript.getName()+"["+ sysInvokeScript.getId()+"]");

        json.put("sysInvokeScript", sysInvokeScript);
        return json;

    }
}
