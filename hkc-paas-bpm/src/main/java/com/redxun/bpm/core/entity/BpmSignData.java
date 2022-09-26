
/**
 * <pre>
 *
 * 描述：任务会签数据实体类定义
 * 表:bpm_sign_data
 * 作者：csx
 * 邮箱: csx@redxun.cn
 * 日期:2020-04-23 23:17:22
 * 版权：广州红迅软件
 * </pre>
 */
package com.redxun.bpm.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.redxun.common.base.entity.BaseExtEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
@TableName(value = "bpm_sign_data")
public class BpmSignData  extends BaseExtEntity<String> {

    @JsonCreator
    public BpmSignData() {
    }

    public BpmSignData(BpmTask bpmTask,String userId,String voteStatus) {
        this.actDefId=bpmTask.getActDefId();
        this.nodeId=bpmTask.getKey();
        this.actInstId=bpmTask.getActInstId();
        this.userId=userId;
        this.voteStatus=voteStatus;
    }

    //主键
    @TableId(value = "DATA_ID_",type = IdType.INPUT)
	private String dataId;

    //流程定义ID
    @TableField(value = "ACT_DEF_ID_")
    private String actDefId;
    //流程实例ID
    @TableField(value = "ACT_INST_ID_")
    private String actInstId;
    //流程节点Id
    @TableField(value = "NODE_ID_")
    private String nodeId;
    //投票人ID
    @TableField(value = "USER_ID_")
    private String userId;
    //投票状态
    @TableField(value = "VOTE_STATUS_")
    private String voteStatus;


    @Override
    public String getPkId() {
        return dataId;
    }

    @Override
    public void setPkId(String pkId) {
        this.dataId=pkId;
    }
}



