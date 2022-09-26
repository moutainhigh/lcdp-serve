package com.redxun.bpm.core.service;

import com.redxun.bpm.core.entity.BpmRuPath;
import com.redxun.bpm.core.entity.BpmTask;

public interface IRejectHandler {

    boolean canHandler(BpmRuPath parentPath) ;

    void  handSource(BpmTask bpmTask,BpmRuPath parentPath);

    BpmRuPath getBackNode(BpmRuPath parentPath);

}
