
/**
 * <pre>
 *
 * 描述：流程授权表实体类定义
 * 表:BPM_AUTH
 * 作者：hj
 * 邮箱: hujun@redxun.cn
 * 日期:2022-08-02 09:52:00
 * 版权：广州红迅软件
 * </pre>
 */
package com.redxun.bpm.core.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.redxun.common.base.entity.BaseExtEntity;
import com.redxun.common.constant.MBoolean;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import java.util.ArrayList;
import java.util.List;
import org.apache.ibatis.type.JdbcType;
import org.springframework.format.annotation.DateTimeFormat;


@Setter
@Getter
@Accessors(chain = true)
@TableName(value = "BPM_AUTH")
public class BpmAuth  extends BaseExtEntity<java.lang.String> {

    @JsonCreator
    public BpmAuth() {
    }

    //主键
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;

    //有效期
    @TableField(value = "ACTIVE_TIME_",jdbcType=JdbcType.VARCHAR)
    private String activeTime;
    //应用ID
    @TableField(value = "APP_ID_",jdbcType=JdbcType.VARCHAR)
    private String appId;
    //授权用户id
    @TableField(value = "AUTH_USER_ID_",jdbcType=JdbcType.VARCHAR)
    private String authUserId;
    //授权用户名称
    @TableField(value = "AUTH_USER_NAME_",jdbcType=JdbcType.VARCHAR)
    private String authUserName;
    //删除时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @TableField(value = "DEL_TIME_",jdbcType=JdbcType.TIMESTAMP)
    private java.util.Date delTime;
    //删除用户id
    @TableField(value = "DEL_USER_ID_",jdbcType=JdbcType.VARCHAR)
    private String delUserId;
    //删除用户名称
    @TableField(value = "DEL_USER_NAME_",jdbcType=JdbcType.VARCHAR)
    private String delUserName;
    //流程编号
    @TableField(value = "PROCESS_KEY_",jdbcType=JdbcType.VARCHAR)
    private String processKey;
    //流程名称
    @TableField(value = "PROCESS_NAME_",jdbcType=JdbcType.VARCHAR)
    private String processName;
    //是否有效
    @TableField(value = "STATUS_",jdbcType=JdbcType.VARCHAR)
    private String status= MBoolean.YES.name();
    //被授权用户id
    @TableField(value = "TO_AUTH_USER_ID_",jdbcType=JdbcType.VARCHAR)
    private String toAuthUserId;
    //被授权用户名称
    @TableField(value = "TO_AUTH_USER_NAME_",jdbcType=JdbcType.VARCHAR)
    private String toAuthUserName;

    //被授权人邮箱
    @TableField(exist = false)
    private String email;
    //被授权人联系电话
    @TableField(exist = false)
    private String mobile;
    //被授权人主部门
    @TableField(exist = false)
    private String mainDeptName;

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



