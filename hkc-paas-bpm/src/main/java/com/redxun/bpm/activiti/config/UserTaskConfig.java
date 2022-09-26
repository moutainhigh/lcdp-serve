package com.redxun.bpm.activiti.config;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 任务节点配置
 */
@Data
public class UserTaskConfig extends AbstractNodeConfig {

    public static final String NEXT_MODE_MANUAL="manual";

    public static final String NEXT_MODE_CONDITION="condition";

    /**
     *  用户配置
     */
    private List<UserGroupConfig> userConfigs=new ArrayList<>();
    /**
     * 按钮配置
     */
    private List<ButtonConfig> buttons=new ArrayList<>();
    /**
     * 事件配置
     */
    private List<Event> events=new ArrayList<>();

    /**
     * "switchOptions": [
     * 			"allowDefButtons", 允许自定义按钮
     * 			"allowSelectPath", 允许选择路径
     * 			"allowSelectExecutor",允许执行执行人
     * 			"allowChangePath",  允许更改执行路径
     * 			"allowExecutorNull" 允许执行人为空
     * 	],
     */
    private List<String> switchOptions = new ArrayList<>();

    /**
     * 执行人过滤配置
     * {user:{},group:{}}
     */
    private JSONObject userFilter;
    /**
     * 回退选择项
     * BACK  回到上一节点
     * BACK_TO_STARTOR 回到发起人
     * BACK_SPEC 回至特定节点
     */
    private List<String> backOptions=new ArrayList<>();
    /**
     * 通知模板
     */
    private KeyValConfig noticeTemplate = new KeyValConfig();
    /**
     * 表间公式
     */
    private KeyValConfig tableFormula = new KeyValConfig();
    /**
     * 跳转规则配置
     */
    private List<JumpRuleConfig> jumpRules = new ArrayList<>();

    /**
     * 催办配置
     */
    private List<RemindDefConfig> remindDefs = new ArrayList<>();

    /**
     * 消息通知类型
     * "InnerMsg",
     * "DingDing"
     * "ShortMsg",
     * "Email",
     * "EntWX"
     */
    private String noticeTypes = "";
    /**
     * 跳转跳过选项
     * "sameFollowNodeUser",
     * 	"noCondition",
     * 	"checkedByFormVars",
     * 	"onceUserCheck"
     */
    private JumpSkipOptions jumpSkipOptions=new JumpSkipOptions();
    /**
     * 会签配置
     */
    private SignConfig signConfig=new SignConfig();

    /**
     * 任务处理前置处理器
     */
    private String taskPreHandler;
    /**
     * 任务处理后置处理器
     */
    private String taskAfterHandler;
    /**
     * 允许审批判断脚本
     */
    private String allowScript;
    /**
     * 提示信息
     */
    private String allowTipInfo;
    /**
     * 流程变理配置
     */
    private List<VariableConfig> varConfigs=new ArrayList<>();

    /**
     * 表单配置
     */
    private FormConfig form=new FormConfig();

    /**
     * 任务循环类型，若等于normal则代表默认类型
     */
    private String multipleType;

    private DataSetting dataSetting=new DataSetting();

    @Override
    public String getType() {
        return BpmNodeTypeEnums.USER_TASK.getTypeName();
    }

//    /**
//     * 任务启动时抄送配置
//     */
//    private List<UserGroupConfig> startCopyConfig=new ArrayList<>();
//
//    /**
//     * 任务完成时抄送配置。
//     */
//    private List<UserGroupConfig> completeCopyConfig=new ArrayList<>();

    /**
     * 节点表单状态配置
     */
    private String formStatus = "default";

    /**
     * 当任务节点之后有多个任务节点时，处理方式
     *  manual：手动选择
     *  condition：条件
     */
    private String nextHandMode=NEXT_MODE_MANUAL;

    /**
     * 输出节点条件配置。
     */
    private List<NodeOutcomeConfig> outs=new ArrayList<>();
}
