
/**
 * <pre>
 *
 * 描述：内部短消息实体类定义
 * 表:inf_inner_msg
 * 作者：gjh
 * 邮箱: gjh@redxun.cn
 * 日期:2020-07-20 10:55:40
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
import com.redxun.log.annotation.FieldDef;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

/**
 * 内部消息
 */
@Setter
@Getter
@Accessors(chain = true)
@TableName(value = "inf_inner_msg")
public class InfInnerMsg  extends BaseExtEntity<java.lang.String> {

    @JsonCreator
    public InfInnerMsg() {
    }

    //消息ID
    @TableId(value = "MSG_ID_",type = IdType.INPUT)
	private String msgId;

    //消息标题
    @FieldDef(comment = "消息标题")
    @TableField(value = "MSG_TITLE_")
    private String msgTitle;

    //消息内容
    @TableField(value = "CONTENT_")
    private String content;
    //消息携带连接,

    @TableField(value = "LINK_MSG_")
    private String linkMsg;
    //消息分类
    @TableField(value = "CATEGORY_")
    private String category;
    //SENDER_ID_
    @TableField(value = "SENDER_ID_")
    private String senderId;
    //发送人名
    @TableField(value = "SENDER_")
    private String sender;
    //删除标识
    @TableField(value = "DEL_FLAG_")
    private String delFlag;

    //是否阅读
    @TableField(exist =false)
    private String isRead;
    @TableField(exist =false)
    private String recId;
    @TableField(exist =false)
    private String recType;
    @TableField(exist =false)
    private List<Map<String,String>> userList;
    @TableField(exist =false)
    private List<Map<String,String>> groupList;


    @Override
    public String getPkId() {
        return msgId;
    }

    @Override
    public void setPkId(String pkId) {
        this.msgId=pkId;
    }
}



