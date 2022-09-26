package com.redxun.bpm.core.entity;

import com.alibaba.fastjson.JSONObject;
import com.redxun.dto.bpm.TaskExecutor;
import lombok.Data;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

/**
 * 流程下一步执行命令对象
 */
@Data
public class ProcessNextCmd extends AbstractExecutionCmd{

    /**
     * 下一步执行方式，按流程图执行。
     */
    public final static String NEXT_JUMP_OPTION_NORMAL="normal";
    /**
     * 直接返回驳回人。
     */
    public final static String NEXT_JUMP_OPTION_SOURCE="source";

    /**
     * 流程任务Id
     */
    private String taskId;

    /**
     * 下一步的处理，一般为回退时的选择项动作参数，
     * normal 表示为正常往下执行，
     * source 则表示从哪回退回跳到哪个节点上
     */
    private String nextJumpType;


    /**
     * 任务代理人
     */
    private String agentToUserId;

    /**
     * 是否为回退
     * @return
     */
    public boolean isBack(){
        return TaskOptionType.BACK.name().equalsIgnoreCase(this.checkType)
                || TaskOptionType.BACK_TO_STARTOR.name().equalsIgnoreCase(this.checkType)
                || TaskOptionType.BACK_SPEC.name().equalsIgnoreCase(this.checkType)
                || TaskOptionType.RECOVER.name().equalsIgnoreCase(this.checkType);
    }


    public static void main(String[] args) {
        ProcessNextCmd cmd=new ProcessNextCmd();
        Map<String, LinkedHashSet<TaskExecutor>> executors=new HashMap<>();
        LinkedHashSet<TaskExecutor> executorSet=new LinkedHashSet<>();
        executorSet.add(TaskExecutor.getUser("1","ray",""));
        executorSet.add(TaskExecutor.getUser("2","ludy",""));
        executors.put("nodeId1",executorSet);
        cmd.setNodeExecutors(executors);

        cmd.getVars().put("name","ray");
        cmd.getVars().put("days",1);

        String json=JSONObject.toJSONString(cmd);
        System.err.println(json);
//        String json="{\"back\":false,\"formData\":{},\"nodeExecutors\":{\"nodeId1\":[{\"calcType\":\"none\",\"id\":\"2\",\"name\":\"ludy\",\"type\":\"user\"},{\"calcType\":\"none\",\"id\":\"1\",\"name\":\"ray\",\"type\":\"user\"}]},\"nodeUserIds\":\"\",\"tasks\":[],\"transientVars\":{},\"vars\":{}}";
//        ProcessNextCmd cmd= JSONObject.parseObject(json,ProcessNextCmd.class);
//        System.err.println("ok");
    }

}
