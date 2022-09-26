
/**
 * <pre>
 *
 * 描述：流程任务转移记录实体类定义
 * 表:BPM_TASK_TRANSFER
 * 作者：csx
 * 邮箱: csx@redxun.cn
 * 日期:2020-06-09 14:26:11
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
@TableName(value = "BPM_TASK_TRANSFER")
public class BpmTaskTransfer  extends BaseExtEntity<String> {

    public  static final  String TYPE_TRANSFER="transfer";
    public  static final  String TYPE_AGENT="agent";


    @JsonCreator
    public BpmTaskTransfer() {
    }

    //主键
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;
    /**
     * 分类ID
     */
    @TableField(value = "TREE_ID_")
    private String treeId;

    //流程实例ID
    @TableField(value = "INST_ID_")
    private String instId;
    //所有人ID
    @TableField(value = "OWNER_ID_")
    private String ownerId;
    //任务标题
    @TableField(value = "SUBJECT_")
    private String subject;
    //任务ID
    @TableField(value = "TASK_ID_")
    private String taskId;
    //转办人ID
    @TableField(value = "TO_USER_ID_")
    private String toUserId;
    //类型(trans,agent)
    @TableField(value = "TYPE_")
    private String type;

    @Override
    public String getPkId() {
        return id;
    }

    @Override
    public void setPkId(String pkId) {
        this.id=pkId;
    }
}



