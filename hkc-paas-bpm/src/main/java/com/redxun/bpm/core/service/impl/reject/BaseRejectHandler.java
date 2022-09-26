package com.redxun.bpm.core.service.impl.reject;

import com.redxun.bpm.core.entity.BpmRuPath;
import com.redxun.bpm.core.entity.BpmTask;
import com.redxun.bpm.core.service.IRejectHandler;
import com.redxun.bpm.core.service.impl.RejectSourceService;

import javax.annotation.Resource;

public abstract class BaseRejectHandler implements IRejectHandler {

    @Resource
    RejectSourceService rejectSourceService;

    /**
     * 1.将流程现有的任务进行冻结，修改流程任务的状态为LOCKED。
     * 2.修改BPMINST状态为LOCKED.
     * 3.创建一个新的任务指向第一个节点。
     * 4.更新执行路径为驳回。
     * 5.创建一条执行路径。
     * @param preTask
     */
    @Override
    public void handSource(BpmTask preTask, BpmRuPath parentPath){
        BpmRuPath path=getBackNode(parentPath);
        rejectSourceService.handSource(preTask,path);
    }


}
