
/**
 * <pre>
 *
 * 描述：流程抄送/转发实体类定义
 * 表:BPM_INST_CC
 * 作者：csx
 * 邮箱: csx@redxun.cn
 * 日期:2020-04-12 17:46:56
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
@TableName(value = "BPM_INST_CC")
public class BpmInstCc  extends BaseExtEntity<String> {

    /**
     * 转办=TURN_TO
     */
    public static final String CC_TYPE_TURN_TO="TURN_TO";
    /**
     * 复制=COPY
     */
    public static final String CC_TYPE_COPY="COPY";

    @JsonCreator
    public BpmInstCc() {
    }

    //ID_
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;

    //抄送标题
    @TableField(value = "SUBJECT_")
    private String subject;
    //节点ID
    @TableField(value = "NODE_ID_")
    private String nodeId;
    //节点名称
    @TableField(value = "NODE_NAME_")
    private String nodeName;
    //抄送人
    @TableField(value = "FROM_USER_")
    private String fromUser;
    //抄送人ID
    @TableField(value = "FROM_USER_ID_")
    private String fromUserId;
    //流程实例ID
    @TableField(value = "INST_ID_")
    private String instId;
    //流程定义Id
    @TableField(value = "DEF_ID_")
    private String defId;
    //类型(0,抄送,1,转发)
    @TableField(value = "CC_TYPE_")
    private String ccType;
    @TableField(value = "TREE_ID_")
    private String treeId;

    @Override
    public String getPkId() {
        return id;
    }

    @Override
    public void setPkId(String pkId) {
        this.id=pkId;
    }


    /**
     * 抄送人
     */
    @TableField(exist = false)
    private BpmInstCp instCp;
}



