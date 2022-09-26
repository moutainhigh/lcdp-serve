package com.redxun.dto.bpm;


/**
 * 流程常量
 */
public class BpmConst {

    public final static String CACHE_REGION="bpm";

    public final static String TYPE_CONFIG="bpmDefConfig_";

    /**
     * 流程常量
     */
    public static final String LOOP_COUNTS="loopCount";
    /**
     * 流程索引
     */
    public static final String LOOP_INDEX="loopIndex";

    /**
     * 审批完成的次数
     */
    public static final String COMPLETE_COUNTS="completes";

    /**
     * 会签计算类型=按票数统计
     */
    public static final String SIGN_CAL_COUNT="voteCount";
    /**
     * 会签计算类型=按百分比统计
     */
    public static final String SIGN_CAL_PERCENT="votePercent";

    /**
     * 节点类型。
     *parallel并行
     * sequential串行
     * normal普通
     */
    public static final  String NODE_NORMAL="normal";
    public static final  String NODE_PARALLEL="parallel";
    public static final  String NODE_SEQUENTIAL="sequential";

    public static final String SIGN_EXECUTOR_IDS="sigExecutorIds_";

    public static final String MULTI_SUB_PROCESS="multiSubProcess";

    public static final String PROCESS_CONFIG="PROCESS_CONFIG";
    public static final String USERTASK_CONFIG="USERTASK_CONFIG";
    public static final String BPM_INST="BPM_INST";
    /**
     * 需要通知。
     */
    public static final String BPM_INFORM_USERS="BPM_INFORM_USERS";

    public static final String BPM_TASK="BPM_TASK";

    public static final String BPM_APPROVE_TASK="BPM_APPROVE_TASK";

    /**
     * 代理任务。
     */
    public static final String BPM_AGENT_TASK="BPM_AGENT_TASK";

    /**
     * 用户任务。
     */
    public static final String  USER_TASK= "userTask";

}
