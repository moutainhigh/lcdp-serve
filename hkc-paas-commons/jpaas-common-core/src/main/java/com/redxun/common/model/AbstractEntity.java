package com.redxun.common.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 实体父类
 *
 * @author yjy
 */
@Data
public abstract class AbstractEntity<T extends Model<?>> extends SuperEntity<T> {

    /**
     * 获取主键
     * @return
     */
    public abstract Serializable getPkVal();

    /**
     * 设置主键
     * @param pkVal
     */
    public abstract void setPkVal(Serializable pkVal);

}
