package com.redxun.dto.form;

import lombok.Data;

/**
 * BO属性DTO
 */
@Data
public class FormBoAttrDto {
    /**
     * 主键
     */
    private String id;
    /**
     * 实体对象ID
     */
    private String entId;
    /**
     * 属性名称
     */
    private String name;
    /**
     * 字段名称
     */
    private String fieldName;
    /**
     * 备注
     */
    private String comment;
    /**
     * 数据类型
     */
    private String dataType;
    /**
     * 长度
     */
    private Integer length;
    /**
     * 数值精度长度
     */
    private Integer decimalLength;
    /**
     * 是否单选
     */
    private Integer isSingle;
    /**
     * 控件
     */
    private String control;
}
