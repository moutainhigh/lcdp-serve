package com.redxun.common.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.redxun.common.base.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.JdbcType;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 实体父类
 *
 * @author yjy
 */
@Setter
@Getter
public abstract class SuperEntity<T> implements BaseEntity<T> {
    /**
     * 主键ID
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "CREATE_TIME_", fill = FieldFill.INSERT,jdbcType = JdbcType.TIMESTAMP)
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "UPDATE_TIME_", fill = FieldFill.INSERT_UPDATE,jdbcType = JdbcType.TIMESTAMP)
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date updateTime;

    @TableField(value = "CREATE_BY_", fill = FieldFill.INSERT,jdbcType = JdbcType.VARCHAR)
    private String createBy;

    @TableField(value = "UPDATE_BY_", fill = FieldFill.UPDATE,jdbcType = JdbcType.VARCHAR)
    private String updateBy;

    @TableField(value = "CREATE_DEP_ID_", fill = FieldFill.INSERT,jdbcType = JdbcType.VARCHAR)
    private String createDepId;



}
