
/**
 * <pre>
 *
 * 描述：act_ru_execution实体类定义
 * 表:act_ru_execution
 * 作者：csx
 * 邮箱: csx@redxun.cn
 * 日期:2020-03-17 16:03:07
 * 版权：广州红迅软件
 * </pre>
 */
package com.redxun.bpm.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.redxun.common.base.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@Accessors(chain = true)
@TableName(value = "act_ru_execution")
public class BpmExecution implements BaseEntity<String> {

    @JsonCreator
    public BpmExecution() {
    }

    //ID_
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;

    //REV_
    @TableField(value = "REV_")
    private Integer rev;
    //PROC_INST_ID_
    @TableField(value = "PROC_INST_ID_")
    private String procInstId;
    //BUSINESS_KEY_
    @TableField(value = "BUSINESS_KEY_")
    private String businessKey;
    //PARENT_ID_
    @TableField(value = "PARENT_ID_")
    private String parentId;
    //PROC_DEF_ID_
    @TableField(value = "PROC_DEF_ID_")
    private String procDefId;
    //SUPER_EXEC_
    @TableField(value = "SUPER_EXEC_")
    private String superExec;
    //ROOT_PROC_INST_ID_
    @TableField(value = "ROOT_PROC_INST_ID_")
    private String rootProcInstId;
    //ACT_ID_
    @TableField(value = "ACT_ID_")
    private String actId;
    //IS_ACTIVE_
    @TableField(value = "IS_ACTIVE_")
    private Short isActive;
    //IS_CONCURRENT_
    @TableField(value = "IS_CONCURRENT_")
    private Short isConcurrent;
    //IS_SCOPE_
    @TableField(value = "IS_SCOPE_")
    private Short isScope;
    //IS_EVENT_SCOPE_
    @TableField(value = "IS_EVENT_SCOPE_")
    private Short isEventScope;
    //IS_MI_ROOT_
    @TableField(value = "IS_MI_ROOT_")
    private Short isMiRoot;
    //SUSPENSION_STATE_
    @TableField(value = "SUSPENSION_STATE_")
    private Integer suspensionState;
    //CACHED_ENT_STATE_
    @TableField(value = "CACHED_ENT_STATE_")
    private Integer cachedEntState;
    //NAME_
    @TableField(value = "NAME_")
    private String name;
    //START_TIME_
    @TableField(value = "START_TIME_")
    private Date startTime;
    //START_USER_ID_
    @TableField(value = "START_USER_ID_")
    private String startUserId;
    //LOCK_TIME_
    @TableField(value = "LOCK_TIME_")
    private Date lockTime;
    //IS_COUNT_ENABLED_
    @TableField(value = "IS_COUNT_ENABLED_")
    private Short isCountEnabled;
    //EVT_SUBSCR_COUNT_
    @TableField(value = "EVT_SUBSCR_COUNT_")
    private Integer evtSubscrCount;
    //TASK_COUNT_
    @TableField(value = "TASK_COUNT_")
    private Integer taskCount;
    //JOB_COUNT_
    @TableField(value = "JOB_COUNT_")
    private Integer jobCount;
    //TIMER_JOB_COUNT_
    @TableField(value = "TIMER_JOB_COUNT_")
    private Integer timerJobCount;
    //SUSP_JOB_COUNT_
    @TableField(value = "SUSP_JOB_COUNT_")
    private Integer suspJobCount;
    //DEADLETTER_JOB_COUNT_
    @TableField(value = "DEADLETTER_JOB_COUNT_")
    private Integer deadletterJobCount;
    //VAR_COUNT_
    @TableField(value = "VAR_COUNT_")
    private Integer varCount;
    //ID_LINK_COUNT_
    @TableField(value = "ID_LINK_COUNT_")
    private Integer idLinkCount;

    @Override
    public String getPkId() {
        return id;
    }

    @Override
    public void setPkId(String pkId) {
        this.id=pkId;
    }

    @TableField(exist = false)
    private List<BpmExecution> children=new ArrayList<>();

    @TableField(exist = false)
    private int level=0;

    public void addChildren(BpmExecution execution){
        this.children.add(execution);
    }

}



