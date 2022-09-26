
/**
 * <pre>
 *
 * 描述：流程审批流转记录实体类定义
 * 表:bpm_check_history
 * 作者：csx
 * 邮箱: csx@redxun.cn
 * 日期:2020-03-01 17:50:22
 * 版权：广州红迅软件
 * </pre>
 */
package com.redxun.bpm.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.redxun.common.base.entity.BaseExtEntity;
import com.redxun.common.tool.StringUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

@Setter
@Getter
@Accessors(chain = true)
@TableName(value = "bpm_check_history")
public class BpmCheckHistory  extends BaseExtEntity<String> {

    //HIS_ID_
    @TableId(value = "HIS_ID_",type = IdType.INPUT)
	private String hisId;
    //ACT流程定义ID
    @TableField(value = "ACT_DEF_ID_")
    private String actDefId;
    //流程实例ID
    @TableField(value = "INST_ID_")
    private String instId;

    /**
     * 树ID
     */
    @TableField(value = "TREE_ID_")
    private String treeId;

    /**
     * 标题
     */
    @TableField(value = "SUBJECT_")
    private String subject;

    //节点名称
    @TableField(value = "NODE_NAME_")
    private String nodeName;
    //节点Key
    @TableField(value = "NODE_ID_")
    private String nodeId;
    //任务ID
    @TableField(value = "TASK_ID_")
    private String taskId;

    //完成时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @TableField(value = "COMPLETE_TIME_")
    private java.util.Date completeTime;
    //持续时长
    @TableField(value = "DURATION_")
    private Long duration;
    //有效审批时长
    @TableField(value = "DURATION_VAL_")
    private Long durationVal;
    //任务所属人ID
    @TableField(value = "OWNER_ID_")
    private String ownerId;
    //处理人ID
    @TableField(value = "HANDLER_ID_")
    private String handlerId;
    //被代理人
    @TableField(value = "AGENT_USER_ID_")
    private String agentUserId;
    //审批状态
    @TableField(value = "CHECK_STATUS_")
    private String checkStatus;
    //跳转类型
    @TableField(value = "JUMP_TYPE_")
    private String jumpType;
    //处理类型
    @TableField(exist = false)
    private String jumpTypeName;
    //意见备注
    @TableField(value = "REMARK_")
    private String remark;
    //字段意见名称
    @TableField(value = "OPINION_NAME_")
    private String opinionName;
    //处理部门ID
    @TableField(value = "HANDLE_DEP_ID_")
    private String handleDepId;
    //处理部门全名
    @TableField(value = "HANDLE_DEP_FULL_")
    private String handleDepFull;
    //是否支持手机
    @TableField(value = "ENABLE_MOBILE_")
    private Short enableMobile;
    //沟通对象Id
    @TableField(value = "LINK_UP_USER_IDS")
    private String linkUpUserIds;


    @TableField(value = "REL_INSTS_")
    private String relInsts;



    //沟通对象
    @TableField(exist = false)
    private List<Map<String,String>> linkUpUsers;

    //处理人姓名
    @TableField(exist = false)
    private String handlerUserName;
    //处理人账号
    @TableField(exist = false)
    private String handlerUserNo;
    //处理人主部门名称
    @TableField(exist = false)
    private String handlerUserDeptName;
    //申请人姓名
    @TableField(exist = false)
    private String applicantName;
    //申请人账号
    @TableField(exist = false)
    private String applicantNo;
    //附件集合
    @TableField(exist = false)
    private List<BpmCheckFile> opFiles;
    @JsonCreator
    public BpmCheckHistory() {
    }

    @Override
    public String getPkId() {
        return hisId;
    }

    @Override
    public void setPkId(String pkId) {
        this.hisId=pkId;
    }

    public String getJumpTypeName(){
        if(StringUtils.isNotEmpty(this.getCheckStatus())){
            if("CANCEL".equals(this.getCheckStatus())){
                return "作废";
            }else if("RUNNING".equals(this.getCheckStatus())){
                return "恢复";
            }
            else if("SUPSPEND".equals(this.getCheckStatus())){
                return "暂停";
            }
            try {
                TaskOptionType taskOptionType = TaskOptionType.valueOf(this.getCheckStatus());
                if (taskOptionType != null) {
                    return taskOptionType.getText();
                }
            }catch (Exception ex){
                this.getCheckStatus();
            }
        }
        return this.getCheckStatus();
    }
}



