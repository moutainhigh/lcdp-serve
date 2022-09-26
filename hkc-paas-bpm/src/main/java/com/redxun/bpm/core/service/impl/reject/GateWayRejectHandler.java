package com.redxun.bpm.core.service.impl.reject;

import com.redxun.bpm.core.entity.BpmRuPath;
import com.redxun.bpm.core.mapper.BpmRuPathMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class GateWayRejectHandler extends BaseRejectHandler {

    @Resource
    private BpmRuPathMapper bpmRuPathMapper;

    @Override
    public boolean canHandler(BpmRuPath parentPath) {
        String nodeType= parentPath.getNodeType();
        if("parallelGateway".equals( nodeType) ||
                "inclusiveGateway".equals( nodeType)){
            return true;
        }
        return false;
    }

    @Override
    public BpmRuPath getBackNode(BpmRuPath parentPath) {
        while (!"userTask".equals( parentPath.getNodeType()) ){
            parentPath=bpmRuPathMapper.selectById(parentPath.getParentId());
        }
        return parentPath;
    }


}
