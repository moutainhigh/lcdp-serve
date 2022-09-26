package com.redxun.bpm.core.dto;

import com.alibaba.fastjson.JSONObject;
import com.redxun.bpm.util.FlowNodeUtil;
import com.redxun.dto.bpm.TaskExecutor;
import lombok.Data;
import org.activiti.bpmn.model.FlowNode;

import java.beans.Transient;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 后续节点的执行人员
 */
@Data
public class NodeUsersDto {
    //节点Id
    private String nodeId;
    //节点名称
    private String nodeName;

    private String nodeType="";
    //用户过滤配置
    private JSONObject userFilter;
    //用户组过滤配置
    private JSONObject groupFilter;
    //执行用户
    Collection <TaskExecutor> executors;
    /**
     * 流程节点
     */

    private FlowNode flowNode;
    //后续节点的人员映射
    Set<NodeUsersDto> outcomeNodeUsers = new LinkedHashSet();

    public void setFlowNode(FlowNode flowNode){
        this.nodeId=flowNode.getId();
        this.nodeName= FlowNodeUtil.getFlowNodeName(flowNode);
        this.flowNode=flowNode;
        //获取节点类型。
        this.setNodeType(flowNode.getClass().getSimpleName());
    }

    @Transient
    public FlowNode getFlowNode(){
        return this.flowNode;
    }

    public void addNodeUserDto(NodeUsersDto usersDto){
        outcomeNodeUsers.add(usersDto);
    }

    private int level=0;
}
