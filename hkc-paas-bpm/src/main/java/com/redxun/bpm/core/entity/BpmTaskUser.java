
/**
 * <pre>
 *
 * 描述：任务处理相关人实体类定义
 * 表:BPM_TASK_USER
 * 作者：csx
 * 邮箱: csx@redxun.cn
 * 日期:2019-12-18 19:08:11
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

@Getter
@Setter
@Accessors(chain = true)
@TableName(value = "BPM_TASK_USER")
public class BpmTaskUser  extends BaseExtEntity<String> {

    /**
     * 参与类型 ASSIGNEE
     */
    public final static String PART_TYPE_ASSIGNEE="ASSIGNEE";
    /**
     * 参与类型 CANDIDATE
     */
    public final static String PART_TYPE_CANDIDATE="CANDIDATE";

    /**
     * 只读 = read
     */
    public final static  String IS_READ="read";
    /**
     * 未读 = unread
     */
    public final static String IS_UNREAD="unread";

    @JsonCreator
    public BpmTaskUser() {
    }

    //ID_
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;

    //任务ID
    @TableField(value = "TASK_ID_")
    private String taskId;
    //用户ID
    @TableField(value = "USER_ID_")
    private String userId;
    //用户组ID
    @TableField(value = "GROUP_ID_")
    private String groupId;
    //用户类型
    @TableField(value = "USER_TYPE_")
    private String userType;
    //参与类型
    @TableField(value = "PART_TYPE_")
    private String partType;
    //是否已读
    @TableField(value = "IS_READ_")
    private String isRead;

    @TableField(value = "INST_ID_")
    private String instId;

    @Override
    public String getPkId() {
        return id;
    }

    @Override
    public void setPkId(String pkId) {
        this.id=pkId;
    }
}



