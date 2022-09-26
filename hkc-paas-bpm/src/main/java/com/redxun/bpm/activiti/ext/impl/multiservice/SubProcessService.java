package com.redxun.bpm.activiti.ext.impl.multiservice;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.redxun.bpm.activiti.ActRepService;
import com.redxun.bpm.activiti.config.UserTaskConfig;
import com.redxun.bpm.activiti.ext.IMultiSerice;
import com.redxun.bpm.core.entity.IExecutionCmd;
import com.redxun.bpm.core.service.BpmDefService;
import com.redxun.bpm.core.service.TaskExecutorService;
import com.redxun.bpm.util.ProcessHandleUtil;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.StringUtils;
import com.redxun.dto.bpm.BpmConst;
import com.redxun.dto.bpm.TaskExecutor;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.SubProcess;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 多实例子流程服务
 * @author
 */
@Component(value = "subProcessService")
public class SubProcessService implements IMultiSerice {

    @Resource
    private TaskExecutorService taskExecutorService;
    @Resource
    private BpmDefService bpmDefService;
    @Resource
    private ActRepService actRepService;

    @Override
    public List<TaskExecutor> getExcutors(DelegateExecution execution) {
        UserTask userTask= getFirstUserTask( execution);

        IExecutionCmd cmd= ProcessHandleUtil.getProcessCmd();
        String actDefId=execution.getProcessDefinitionId();
        String nodeId=userTask.getId();
        String varName=BpmConst.SIGN_EXECUTOR_IDS + nodeId;


        //从流程变量中获取
        List<TaskExecutor> users = getExecutors(execution,varName);
        if(BeanUtil.isNotEmpty(users )){
            return users;
        }
        //从上下文获取
        Set<TaskExecutor> userSets= cmd.getNodeExecutors().get(nodeId);
        if(BeanUtil.isNotEmpty(userSets)){
            List<TaskExecutor> userList=new ArrayList<>();
            userList.addAll(userSets);
            //只有并行的时候才会执行
            handExecutorVars( execution, userList,nodeId);
            return userList;
        }
        //从数据库获取。
        List<TaskExecutor> userList=  getFromDb(actDefId,nodeId,execution.getVariables());

        handExecutorVars( execution, userList,nodeId);

        return userList;
    }

    private List<TaskExecutor> getExecutors(DelegateExecution execution,String varName){
        List<TaskExecutor> executors=new ArrayList<>();
        String users = (String) execution.getVariable(varName);
        if(StringUtils.isEmpty(users)){
            return null;
        }
        JSONArray ary=JSONArray.parseArray(users);
        for(int i=0;i<ary.size();i++){
            JSONObject json=ary.getJSONObject(i);
            TaskExecutor executor= JSONObject.toJavaObject(json,TaskExecutor.class);
            executors.add(executor);
        }
        return executors;
    }

    private void handExecutorVars(DelegateExecution execution,List<TaskExecutor> userList,String nodeId){
        String varName=BpmConst.SIGN_EXECUTOR_IDS +nodeId;
        String json= JSONArray.toJSONString(userList);
        execution.setVariable(varName,json);
    }

    private List<TaskExecutor> getFromDb(String actDefId, String nodeId, Map<String,Object> vars){
        //获取第一个节点的人员配置数据。
        UserTaskConfig config= (UserTaskConfig) bpmDefService.getNodeConfig(actDefId,nodeId);
        Set<TaskExecutor> executorSet= taskExecutorService.getTaskExecutors(config.getUserConfigs(),vars);
        List<TaskExecutor> users=new ArrayList<>();
        if(BeanUtil.isNotEmpty(executorSet)){
            users.addAll(executorSet);
        }
        return  users;
    }

    /**
     * 获取子流程的第一个节点。
     * @param execution
     * @return
     */
    private UserTask getFirstUserTask(DelegateExecution execution){
        SubProcess subProcess= (SubProcess) execution.getCurrentFlowElement();
        //获取发起节点
        FlowNode startNode=actRepService.getInitNode(subProcess);
        //获取第一个用户任务节点。
        UserTask userTask = (UserTask) startNode.getOutgoingFlows().get(0).getTargetFlowElement();

        return userTask;
    }

    @Override
    public boolean isComplete(DelegateExecution execution) {

        return false;
    }
}
