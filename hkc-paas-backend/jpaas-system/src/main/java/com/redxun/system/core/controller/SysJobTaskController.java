
package com.redxun.system.core.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.utils.SpringUtil;
import com.redxun.system.core.entity.SysJobTask;
import com.redxun.system.core.service.SysJobTaskServiceImpl;
import com.redxun.system.core.service.job.ISysJobHandler;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/system/core/sysJobTask")
@Api(tags = "定时任务定义")
@ClassDefine(title = "定时任务定义",alias = "sysJobTaskController",path = "/system/core/sysJobTask",packages = "core",packageName = "系统管理")
public class SysJobTaskController extends BaseController<SysJobTask> {

    @Autowired
    SysJobTaskServiceImpl sysJobTaskService;


    @Override
    public BaseService getBaseService() {
    return sysJobTaskService;
    }

    @Override
    public String getComment() {
    return "定时任务定义";
    }


    @GetMapping("getJobHandlers")
    public JSONArray getJobHandlers(){

        Collection<Object> beans = SpringUtil.getBeans(ISysJobHandler.class);
        JSONArray ary=new JSONArray();

        for(Object obj:beans){
            ISysJobHandler handler=(ISysJobHandler)obj;
            JSONObject json=new JSONObject();
            Class cls=obj.getClass();
            json.put("value",cls.getName());
            json.put("label",handler.getName());
            ary.add(json);
        }

        return ary;
    }

}

