
/**
 * <pre>
 *
 * 描述：内部短消息收件箱实体类定义
 * 表:inf_inbox
 * 作者：gjh
 * 邮箱: gjh@redxun.cn
 * 日期:2020-07-21 17:32:57
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
 * 内部消息收件箱
 */
@Setter
@Getter
@Accessors(chain = true)
@TableName(value = "inf_inbox")
public class InfInbox  extends BaseExtEntity<java.lang.String> {

    @JsonCreator
    public InfInbox() {
    }

    //接收ID
    @TableId(value = "REC_ID_",type = IdType.INPUT)
	private String recId;

    //消息ID
    @TableField(value = "MSG_ID_")
    private String msgId;
    //用户=USER 用户组=GROUP
    @TableField(value = "REC_TYPE_")
    private String recType;
    //接收人ID
    @TableField(value = "REC_USER_ID_")
    private String recUserId;
    //接收人名称
    @TableField(value = "REC_USER_NAME_")
    private String recUserName;
    //内容
    @TableField(exist = false)
    private String content;

    @Override
    public String getPkId() {
        return recId;
    }

    @Override
    public void setPkId(String pkId) {
        this.recId=pkId;
    }
}



