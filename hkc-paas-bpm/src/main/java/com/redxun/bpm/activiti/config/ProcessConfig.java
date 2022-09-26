package com.redxun.bpm.activiti.config;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 流程定义的扩展配置
 * @author  csx
 */
@Data
public class ProcessConfig extends AbstractNodeConfig{
    /**
     * 发起必要配置人员的节点Key
     */
    public static final String START_REQ_NODES="startReqNodes";
    /**
     * 发起可选需要配置人员的节点Key
     */
    public static final String START_SEL_NODES="startSelNodes";


    /**
     * 配置标题规则配置数据
     */
    private List<SubjectRuleItem> subjectRuleData=new ArrayList<>();
    /**
     * 通知模板
     */
    private KeyValConfig noticeTemplate = new KeyValConfig();
    /**
     * 表间公式
     */
    private KeyValConfig tableFormula = new KeyValConfig();
    /**
     * 流程绑定的BoIds
     */
    private KeyValConfig boDefs = new KeyValConfig();


    //初始化的流程按钮
    private List<ButtonConfig> buttonConfigs = new ArrayList<>();

    //流程明细按钮
    private List<ButtonConfig> buttonDetailConfigs = new ArrayList<>();

    /**
     * 初始的事件
     */
    private List<Event> events = new ArrayList<>();

    /**
     * 催办配置
     */
    private List<RemindDefConfig> remindDefs = new ArrayList<>();

    /**
     *子流程配置
     */
    private List<SubProcessDefConfig> subProcessDefs = new ArrayList<>();

    /**
     * 开始节点配置项
     * "skipFirstNode",  跳过第一个节点
     * "startConfirm",  提交前确认
     * "assignFlowUsers", 允许调用后续用户配置
     * "fillOpinion",  允许开始填写意见
     * "startCalFlowusers" 允许计算后续流程用户
     */
    private List<String> startNodeOptions = new ArrayList<>();
    /**
     * 开始启动必填的人员节点
     */
    private List<String> startReqNodes=new ArrayList<>();

    /**
     * 开始启动可选的人员节点
     */
    private List<String> startSelNodes=new ArrayList<>();

    /**
     * 消息通知类型
     * "InnerMsg",
     * 	"DingDing"
     * 	"ShortMsg",
     *  "Email",
     * 	"EntWX"
     */
    private String noticeTypes="";
    /**
     * 跳转跳过选项
     * "sameFollowNodeUser",
     * 	"noCondition",
     * 	"checkedByFormVars",
     * 	"onceUserCheck"
     */
    private JumpSkipOptions jumpSkipOptions=new JumpSkipOptions();

    /**
     * 流程变理配置
     */
    private List<VariableConfig> varConfigs=new ArrayList<>();
    /**
     * 流程全局表单配置
     */
    private FormConfig globalForm=new FormConfig();
    /**
     * 流程开始表单配置
     */
    private FormConfig startForm=new FormConfig();
    /**
     * 流程明细配置
     */
    private FormConfig detailForm=new FormConfig();
    /**
     * 流程启动前处理器
     */
    private String processStartPreHandler;
    /**
     * 流程启动后处理器
     */
    private String processStartAfterHandler;
    /**
     * 流程结束处理器
     */
    private String processEndHandler;
    /**
     * 终止流程执行脚本
     */
    private String endProcessScript;
    /**
     * 数据设定配置。
     */
    private DataSetting dataSetting=new DataSetting();


    /**
     *  任务启动的抄送。
     */
    private List<UserGroupConfig> taskStartCopyConfig=new ArrayList<>();


    /**
     *  流程结束时抄送配置。
     */
    private List<UserGroupConfig> taskCompleteCopyConfig=new ArrayList<>();

    /**
     *  流程结束时抄送配置。
     */
    private List<UserGroupConfig> processEndCopyConfig=new ArrayList<>();



    @Override
    public String getType() {
        return BpmNodeTypeEnums.PROCESS.getTypeName();
    }


    /**
     * 是否允许启动时选择路径执行。
     * <pre>
     *     1.流程跳过第一个节点。
     *     2.第一个任务节点后，不止一个出口。
     * </pre>
     */
    private boolean allowSelectPath=false;


    /**
     * 当前的流程定义ID。
     */
    private String defId;

    /**
     * 表单状态配置
     */
    private String formStatus = "default";


    /**
     * 字段设定。
     */
    private TaskFieldConfig taskFields;
}
