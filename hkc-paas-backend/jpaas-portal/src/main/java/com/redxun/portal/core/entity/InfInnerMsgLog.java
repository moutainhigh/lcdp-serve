
/**
 * <pre>
 *
 * 描述：内部消息查看记录实体类定义
 * 表:inf_inner_msg_Log
 * 作者：gjh
 * 邮箱: gjh@redxun.cn
 * 日期:2020-07-21 16:52:11
 * 版权：广州红迅软件
 * </pre>
 */
package com.redxun.portal.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.redxun.common.base.entity.BaseExtEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 内部消息查阅记录
 */
@Setter
@Getter
@Accessors(chain = true)
@TableName(value = "inf_inner_msg_Log")
public class InfInnerMsgLog extends BaseExtEntity<java.lang.String> {

    @JsonCreator
    public InfInnerMsgLog() {
    }

    //ID
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;
    //消息ID
    @TableField(value = "MSG_ID_")
    private String msgId;
    //接收人ID
    @TableField(value = "REC_USER_ID_")
    private String recUserId;
    //是否阅读
    @TableField(value = "IS_READ_")
    private String isRead;
    //是否删除
    @TableField(value = "IS_DEL_")
    private String isDel;

    @Override
    public String getPkId() {
        return id;
    }

    @Override
    public void setPkId(String pkId) {
        this.id=pkId;
    }
}



