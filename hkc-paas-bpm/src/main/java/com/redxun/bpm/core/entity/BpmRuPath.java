
/**
 * <pre>
 *
 * 描述：流程实例运行路线实体类定义
 * 表:bpm_ru_path
 * 作者：csx
 * 邮箱: csx@redxun.cn
 * 日期:2020-03-14 17:55:42
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
@TableName(value = "bpm_ru_path")
public class BpmRuPath  extends BaseExtEntity<String> {
    /**
     * 正常执行
     */
    public final static String NORMAL = "normal";
    /**
     * 原路返回
     */
    public final static String SOURCE = "source";


    @JsonCreator
    public BpmRuPath() {
    }

    //PATH_ID_
    @TableId(value = "PATH_ID_",type = IdType.INPUT)
	private String pathId;

    //流程实例ID
    @TableField(value = "INST_ID_")
    private String instId;
    //流程定义Id
    @TableField(value = "DEF_ID_")
    private String defId;
    //Act定义ID
    @TableField(value = "ACT_DEF_ID_")
    private String actDefId;
    //Act实例ID
    @TableField(value = "ACT_INST_ID_")
    private String actInstId;
    //节点ID
    @TableField(value = "NODE_ID_")
    private String nodeId;
    //节点名称
    @TableField(value = "NODE_NAME_")
    private String nodeName;
    //节点类型
    @TableField(value = "NODE_TYPE_")
    private String nodeType;
    //开始时间
    @TableField(value = "START_TIME_")
    private java.util.Date startTime;
    //结束时间
    @TableField(value = "END_TIME_")
    private java.util.Date endTime;
    //处理人ID
    @TableField(value = "ASSIGNEE_")
    private String assignee;
    //代理人ID
    @TableField(value = "TO_USER_ID_")
    private String toUserId;
    //原执行人IDS
    @TableField(value = "USER_IDS_")
    private String userIds;
    //是否为多实例
    @TableField(value = "MULTIPLE_TYPE_")
    private String multipleType;
    //活动执行ID
    @TableField(value = "EXECUTION_ID_")
    private String executionId;
    //父ID
    @TableField(value = "PARENT_ID_")
    private String parentId;
    //层次
    @TableField(value = "LEVEL_")
    private Integer level;
    //跳出路线ID
    @TableField(value = "OUT_TRAN_ID_")
    private String outTranId;
    //路线令牌
    @TableField(value = "TOKEN_")
    private String token;
    //跳到该节点的方式
    @TableField(value = "JUMP_TYPE_")
    private String jumpType;

    //下一节点跳转方式
    @TableField(value = "NEXT_JUMP_TYPE_")
    protected String nextJumpType;

    //引用路径ID
    @TableField(value = "REF_PATH_ID_")
    private String refPathId;

    @Override
    public String getPkId() {
        return pathId;
    }

    @Override
    public void setPkId(String pkId) {
        this.pathId=pkId;
    }
}



