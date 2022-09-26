package com.redxun.bpm.core.service.impl.reject;

import com.redxun.bpm.activiti.ActRepService;
import com.redxun.bpm.core.entity.BpmRuPath;
import com.redxun.bpm.core.mapper.BpmRuPathMapper;
import org.activiti.bpmn.model.FlowNode;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class SubMultiRejectOutHanler extends BaseRejectHandler {

    @Resource
    private BpmRuPathMapper bpmRuPathMapper;

    @Resource
    ActRepService actRepService;

    @Override
    public boolean canHandler(BpmRuPath parentPath) {
        if("startEvent".equals(parentPath.getNodeType())){
            FlowNode flowNode=actRepService.getFlowNode(parentPath.getActDefId(),parentPath.getNodeId());

            boolean rtn= actRepService.isSubProcessMulti(flowNode);
            return rtn;
        }
        return  false;
    }

    @Override
    public BpmRuPath getBackNode(BpmRuPath parentPath) {
        while (!"userTask".equals( parentPath.getNodeType()) ){
            parentPath=bpmRuPathMapper.selectById(parentPath.getParentId());
        }
        return parentPath;
    }
}
