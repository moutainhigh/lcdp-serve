package com.redxun.bpm.core.entity;

import com.alibaba.fastjson.JSONObject;
import com.redxun.dto.bpm.TaskExecutor;

import java.beans.Transient;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 流程执行中的命令参数接口
 */
public interface IExecutionCmd {



    /**
     * 获取流程变量
     * @return
     */
    Map<String,Object> getVars();


    /**
     * 临时变量，这个不会被流程变量存储。
     * @return
     */
    Map<String,Object> getTransientVars();

    /**
     * 根据KEY获取临时变量。
     * @param key
     * @return
     */
    Object getTransientVar(String key);

    /**
     * 添加临时变量。
     * @param key
     * @param obj
     */
    void addTransientVar(String key,Object obj);


    /**
     * 获取表单数据
     * @return
     */
    JSONObject getFormData();

    /**
     * 键就是BODEF名称
     * 值就是表单的JSON.
     * @return
     */
    @Transient
    JSONObject getBoDataMap();


    void setBoDataMap(JSONObject json);

    /**
     * 获取流程定义ID
     * @return
     */
    String getDefId();

    /**
     * 获取流程实例Id
     * @return
     */
    String getInstId();

    /**
     * 前一流程节点Id
     * @return
     */
    String getPreNodeId();

    /**
     * 获取后续节点的人员映射
     * @return
     */
    Map<String, LinkedHashSet<TaskExecutor>> getNodeExecutors();

    /**
     *
     *
     * @return
     */
    Set<TaskExecutor> getExcutors();



    /**
     * 设置前一流程节点Id
     */
    void setPreNodeId(String preNodeId);

    /**
     * 审批类型
     * @return
     */
    String getCheckType();

    /**
     * 意见。
     * @return
     */
    String getOpinion();

    /**
     * 获取抄送人账号。
     * @return
     */
    String getCopyUserAccounts ();

    /**
     * 设置抄送人账号。
     * @return
     */
    void setCopyUserAccounts (String copyUserAccounts);


    /**
     * 获取消息类型。
     * @return
     */
    String getMsgTypes();

    /**
     * 新产生出来的任务。
     * @return
     */
    List<BpmTask> getTasks();

    /**
     * 添加任务。
     * @param bpmTask
     */
    void addTask(BpmTask bpmTask);

    /**
     * 清理任务。
     */
    void clearTask();

    /**
     * 获取节点用户。
     *  数据格式。
     * {userTask1:'1,2,3',userTask2:'2,3,4'}
     * @return
     */
    String getNodeUserIds();

    /**
     *  获取是否系统处理，默认为true。
     * @return
     */
    boolean getSystemHand();

    /**
     * 设置人工处理。
     * @param systemHand
     */
    void setSystemHand(boolean systemHand);


}
