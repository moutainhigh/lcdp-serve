package com.redxun.bpm.core.entity;

import com.alibaba.fastjson.JSONObject;
import com.redxun.common.constant.SysConstant;
import com.redxun.common.tool.StringUtils;
import com.redxun.dto.bpm.TaskExecutor;
import lombok.Data;

import java.util.*;

@Data
public abstract class AbstractExecutionCmd implements IExecutionCmd{

    public  static String OPERATE_LIVE="live";

    public  static String OPERATE_START="start";

    /**
     * 审批类型
     */
    protected String checkType;

    /**
     * 审批意见名称。
     */
    protected String opinionName;
    /**
     * 审批意见
     */
    protected String opinion;
    /**
     * 审批附件ID
     * [{fileId:"",name:""},{fileId:"",name:""}]
     */
    protected String opFiles;


    /**
     * 关联流程。
     * [{id:"",name:""},{id:"",name:""}]
     */
    protected  String relInsts;

    /**
     * 目标节点Id
     */
    protected String destNodeId;

    private List<BpmTask> tasks=new ArrayList<>();

    public void setFormJson(String formJson) {
        if(StringUtils.isNotEmpty(formJson)){
            this.formData=JSONObject.parseObject(formJson);
        }
    }

    public void setVars(String vars){
        if(StringUtils.isNotEmpty(vars)){
            this.vars= JSONObject.parseObject(vars).getInnerMap();
        }
    }

    public void setVars(Map<String,Object> vars){
        this.vars=vars;
    }

    public void setBoDataJson(String boDataMapJson) {
        if(StringUtils.isNotEmpty(boDataMapJson)){
            this.boDataMap=JSONObject.parseObject(boDataMapJson);
        }
    }

    /**
     * 流程变量
     */
    protected Map<String,Object> vars=new HashMap<>();


    /**
     * 单据数据，格式如：formData:{
     *             form1:{
     *               field1:'a',
     *               field2:'b'
     *             },
     *             form2:{
     *               field3:'c',
     *               field4:'d'
     *             }
     *           }
     */
    protected JSONObject formData=new JSONObject();
    
    /**
     * 由内部生成，不需要传入
     */
    protected String defId;

    /**
     * 流程实例Id,不需要传入
     */
    protected String instId;
    /**
     * 前一任务节点，不需要传入
     */
    protected String preNodeId;


    /**
     * 节点任务执行者(后面的节点的人员计算，优先从这里获取人员）
     * {"UserTask_0clful0": [{"type": "user", "id": "1", "name": "管理员", "calcType": "none"}]}
     */
    private Map<String, LinkedHashSet<TaskExecutor>> nodeExecutors=new LinkedHashMap<>();

    /**
     * [{"type": "user", "id": "1"}]
     */
    private  Set<TaskExecutor> excutors=new LinkedHashSet<>();

    /**
     * 抄送用户账号
     */
    private String copyUserAccounts;
    /**
     * 消息类型
     */
    private String msgTypes;

    protected JSONObject boDataMap=new JSONObject();

    private Map<String,Object> transientVars=new HashMap<>(SysConstant.INIT_CAPACITY_16);

    /**
     * 系统自己处理。
     */
    private boolean systemHand=true;

    /**
     * 添加变量。
     * @param key
     * @param obj
     */
    @Override
    public void addTransientVar(String key, Object obj){
        this.transientVars.put(key,obj);
    }

    /**
     * 根据key 获取变量。
     * @param key
     * @return
     */
    @Override
    public Object getTransientVar(String key){
        return transientVars.get(key);
    }



    @Override
    public void addTask(BpmTask bpmTask) {
        this.tasks.add(bpmTask);
    }

    @Override
    public void clearTask() {
        this.tasks.clear();
    }

    /**
     * {userTask1:'1,2,3',userTask2:'2,3,4'}
     */
    protected String nodeUserIds = "";




    /**
     * 设置节点的后续执行人
     * @param nodeId 节点Id
     * @param userIds 多个用户Id 格式如 1,2
     */
    public void setNodeUserIds(String nodeId,String userIds){
        if(StringUtils.isEmpty(userIds)){
            return;
        }
        String[] uIds=userIds.split("[,]");
        LinkedHashSet<TaskExecutor> userExeSet=new LinkedHashSet<>();
        for(String uId:uIds){
            userExeSet.add(new TaskExecutor(TaskExecutor.TYPE_USER,uId));
        }
        nodeExecutors.put(nodeId,userExeSet);
    }


    @Override
    public boolean getSystemHand() {
        return this.systemHand;
    }

    @Override
    public void setSystemHand(boolean systemHand) {
        this.systemHand=systemHand;
    }
}
