package com.redxun.system.ext.job;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.utils.SpringUtil;
import com.redxun.system.core.entity.SysHttpTask;
import com.redxun.system.core.service.SysHttpTaskServiceImpl;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 执行失败任务
 */
@Slf4j
@Component
public class SysHttpTaskHandler extends IJobHandler {
    @Autowired
    SysHttpTaskServiceImpl sysHttpTaskService;

    @Override
    @XxlJob("sysHttpTaskHandler")
    public void execute() throws Exception {
        //获取所有失败任务
        List<SysHttpTask> list = sysHttpTaskService.getByStatus(SysHttpTask.STATUS_FAIL);
        if (BeanUtil.isEmpty(list)) {
            return;
        }
        //执行失败任务
        for(SysHttpTask task :list){
            sysHttpTaskService.handJob(task);
        }

        return;
    }

}