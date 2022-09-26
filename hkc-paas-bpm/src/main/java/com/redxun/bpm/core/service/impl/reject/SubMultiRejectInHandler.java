package com.redxun.bpm.core.service.impl.reject;

import com.redxun.bpm.activiti.ActRepService;
import com.redxun.bpm.core.entity.BpmRuPath;
import com.redxun.bpm.core.service.BpmRuPathServiceImpl;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.SubProcess;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class SubMultiRejectInHandler extends BaseRejectHandler {
    @Resource
    ActRepService actRepService;
    @Resource
    BpmRuPathServiceImpl bpmRuPathService;

    @Override
    public boolean canHandler(BpmRuPath parentPath) {
        if("subProcess".equals(parentPath.getNodeType())){
            FlowNode flowNode=actRepService.getFlowNode(parentPath.getActDefId(),parentPath.getNodeId());
            SubProcess process=(SubProcess)flowNode;
            if(process.getLoopCharacteristics()!=null){
                return true;
            }
        }
        return  false;
    }

    @Override
    public BpmRuPath getBackNode(BpmRuPath parentPath) {
        String nodeId=parentPath.getNodeId();
        String actDefId=parentPath.getActDefId();
        FlowNode flowNode= actRepService.getFlowNode(actDefId,nodeId);
        SubProcess subProcess=(SubProcess)flowNode;
        FlowNode endNode = actRepService.getEndNode(subProcess);
        String endNodeId=endNode.getId();
        //获取最近的
        BpmRuPath path=bpmRuPathService.getLatestByInstIdNodeId(parentPath.getInstId(),endNodeId);

        while (!"userTask".equals(path.getNodeType())){
            path=bpmRuPathService.get(path.getParentId());
        }

        return path;

    }
}
