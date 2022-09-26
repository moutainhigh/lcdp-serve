package com.redxun.ureport.core.export;

import com.alibaba.fastjson.JSONObject;
import com.redxun.ureport.core.entity.UreportFile;
import com.redxun.ureport.core.service.UreportFileServiceImpl;
import com.redxun.web.controller.IExport;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UreportExport implements IExport {
    @Resource
    UreportFileServiceImpl ureportFileService;
    @Override
    public JSONObject doExportById(String id, StringBuilder sb) {
        JSONObject json = new JSONObject();
        UreportFile ureportFile = ureportFileService.get(id);
        //添加日志
        sb.append(ureportFile.getName() +"("+id+"),");

        json.put("uReport", ureportFile);
        return json;
    }
}
