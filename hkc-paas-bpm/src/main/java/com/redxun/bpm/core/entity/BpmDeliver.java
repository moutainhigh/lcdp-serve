
/**
 * <pre>
 *
 * 描述：流程待办任务交接实体类定义
 * 表:bpm_deliver
 * 作者：zfh
 * 邮箱: zfh@redxun.cn
 * 日期:2022-05-11 18:13:08
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
import java.util.ArrayList;
import java.util.List;
import org.apache.ibatis.type.JdbcType;


@Setter
@Getter
@Accessors(chain = true)
@TableName(value = "bpm_deliver")
public class BpmDeliver  extends BaseExtEntity<String> {

    @JsonCreator
    public BpmDeliver() {
    }

    //主键
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;

    //移交人ID
    @TableField(value = "DELIVER_USER_ID_",jdbcType=JdbcType.VARCHAR)
    private String deliverUserId;
    //移交人工号
    @TableField(value = "DELIVER_USER_NO_",jdbcType=JdbcType.VARCHAR)
    private String deliverUserNo;
    //移交人
    @TableField(value = "DELIVER_USER_NAME_",jdbcType=JdbcType.VARCHAR)
    private String deliverUserName;
    //接管人ID
    @TableField(value = "RECEIPT_USER_ID_",jdbcType=JdbcType.VARCHAR)
    private String receiptUserId;
    //接管人工号
    @TableField(value = "RECEIPT_USER_NO_",jdbcType=JdbcType.VARCHAR)
    private String receiptUserNo;
    //接管人
    @TableField(value = "RECEIPT_USER_NAME_",jdbcType=JdbcType.VARCHAR)
    private String receiptUserName;
    //增加备注字段。
    @TableField(value = "COMMENT_",jdbcType=JdbcType.VARCHAR)
    private String comment;






    @Override
    public String getPkId() {
        return id;
    }

    @Override
    public void setPkId(String pkId) {
        this.id=pkId;
    }


    /**
    生成子表属性的Array List
    */

}



