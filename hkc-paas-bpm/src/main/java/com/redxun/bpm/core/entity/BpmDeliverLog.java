
/**
 * <pre>
 *
 * 描述：流程待办任务交接日志实体类定义
 * 表:bpm_deliver_log
 * 作者：zfh
 * 邮箱: zfh@redxun.cn
 * 日期:2022-05-16 18:51:16
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
@TableName(value = "bpm_deliver_log")
public class BpmDeliverLog  extends BaseExtEntity<String> {

    @JsonCreator
    public BpmDeliverLog() {
    }

    //主键
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;

    //创建人
    @TableField(value = "CREATE_BY_NAME_",jdbcType=JdbcType.VARCHAR)
    private String createByName;
    //移交人ID
    @TableField(value = "DELIVER_USER_ID_",jdbcType=JdbcType.VARCHAR)
    private String deliverUserId;
    //移交人
    @TableField(value = "DELIVER_USER_NAME_",jdbcType=JdbcType.VARCHAR)
    private String deliverUserName;
    //移交人工号
    @TableField(value = "DELIVER_USER_NO_",jdbcType=JdbcType.VARCHAR)
    private String deliverUserNo;
    //接管人ID
    @TableField(value = "RECEIPT_USER_ID_",jdbcType=JdbcType.VARCHAR)
    private String receiptUserId;
    //接管人
    @TableField(value = "RECEIPT_USER_NAME_",jdbcType=JdbcType.VARCHAR)
    private String receiptUserName;
    //接管人工号
    @TableField(value = "RECEIPT_USER_NO_",jdbcType=JdbcType.VARCHAR)
    private String receiptUserNo;



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



