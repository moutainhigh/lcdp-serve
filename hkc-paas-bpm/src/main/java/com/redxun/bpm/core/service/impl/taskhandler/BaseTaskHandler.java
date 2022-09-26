package com.redxun.bpm.core.service.impl.taskhandler;

import com.redxun.bpm.core.service.BpmInstServiceImpl;
import com.redxun.bpm.core.service.BpmRuPathServiceImpl;
import com.redxun.bpm.core.service.BpmTaskService;
import com.redxun.bpm.core.service.ITaskHandler;

import javax.annotation.Resource;

public class BaseTaskHandler implements ITaskHandler {

    @Resource
    BpmRuPathServiceImpl bpmRuPathService;
    @Resource
    BpmTaskService bpmTaskService;
    @Resource
    BpmInstServiceImpl bpmInstService;

}
