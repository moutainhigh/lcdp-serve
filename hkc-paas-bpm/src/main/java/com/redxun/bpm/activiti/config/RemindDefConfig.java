package com.redxun.bpm.activiti.config;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 催办配置
 *
 * @author csx
 * "uid": "DJPMKHEOYC4H422W4QY06QU7WXSJPFMB",
 * name: '未定义',
 * action: 'approve',
 * event: 'TASK_CREATE',
 * dateType: 'common',
 * expireDate: {day: '', hour: 0, minute: 0},
 * condition: '',
 * script: '',
 * notifyType: '',
 * timeToSend: {day: '', hour: 0, minute: 0},
 * sendTimes: 1,
 * sendInterval: {day: '', hour: 0, minute: 0},
 * timeLimitHandler: ''
 */
@Data
public class RemindDefConfig implements Serializable {
    private String uid;
    /**
     * 催办名称
     */
    private String name;
    /**
     * 到期动作
     */
    private String action;
    /**
     * 相对节点
     */
    private String nodeId;
    /**
     * 事件
     */
    private String event;
    /**
     * 是否使用日历
     */
    private String dateType;
    /**
     * 日历 默认为 default
     */
    private String calendar;
    /**
     * 期限
     */
    private DateConfig expireDate;
    /**
     * 条件
     */
    private String condition;
    /**
     * 到期执行脚本
     */
    private String script;
    /**
     * 通知类型
     */
    private String notifyType;
    /**
     * 发送消息时间
     */
    private DateConfig timeToSend;
    /**
     * 发送次数
     */
    private Integer sendTimes;
    /**
     * 发送时间间隔
     */
    private DateConfig sendInterval;
    /**
     * 时限计算处理器
     */
    private String timeLimitHandler;
    /**
     * 通知对象 默认default 自定义custom
     */
    private String notifyObj;
    /**
     *催办人员配置
     */
    private List<UserGroupConfig> userCnfs;

}
