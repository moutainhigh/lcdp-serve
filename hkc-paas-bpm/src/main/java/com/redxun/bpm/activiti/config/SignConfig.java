package com.redxun.bpm.activiti.config;

import lombok.Data;

import java.io.Serializable;

/**
 * 会签配置
 * @author csx
 * "finishOption": "jumpNext",
 * 			"calOption": "voteCount",
 * 			"voteCount": 2,
 * 		   "voteType":"AGREE",
 * 			"finishCondition": "abc",
 */
@Data
public class SignConfig implements Serializable {

    /**
     * 投票
     */
    public final static String COMPLETE_TICKETS="TICKETS";

    /**
     * 用户
     */
    public final static String COMPLETE_CUSTOM="CUSTOM";

    //投票通过
    public final static String VOTE_TYPE_PASS="AGREE";
    //投票反对
    public final static String VOTE_TYPE_REFUSE="REFUSE";
    //投票直接通过
    public final static String HANDLE_TYPE_DIRECT="jumpNext";
    //投票等待处理
    public final static String HANDLE_TYPE_WAIT_TO="waitAllVoted";
    //投票的计算类型，数字
    public final static String CAL_TYPE_NUMS="voteCount";
    //投票的计算类型，百份比类型
    public final static String CAL_TYPE_PERCENT="votePercent";
    /**
     * 会签结果。
     */
    public final static String SIGN_RESULT="signResult";

    /**
     * 完成类型。
     * <pre>
     *     TICKETS:投票,
     *     CUSTOM :用户自定义
     * </pre>
     */
    private String completeType="";

    /**
     * 会签投票完成的配置
     * jumpNext:跳转到下一环节
     * waitAllVoted:等待所有的投票完成
     */
    private String finishOption;
    /**
     * 统计类型/按票数/按投票百分比
     * voteCount:票数
     * votePercent:百分比
     */
    private String calOption;
    /**
     * 投票的数量或百份比
     */
    private Integer voteCount;

    /**
     * 投票意见类型，AGREE/REFUSE
     *
     */
    private String voteOption;
    /**
     * 完成会签的条件
     */
    private String finishCondition;



}
