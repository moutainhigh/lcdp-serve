package com.redxun.form.bo.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 通用字段。
 */
@Getter
@Setter
@Accessors(chain = true)
public class CommonField {


    /**
     * 子表为一对多时，这个存放主表的主键ID
     *
     */
    private String refId="0";
    /*
     *  1. 当配置表单配置树形时，这个放数据。
     *  2. 子表为树形时，这个需要设置。
     */
    private String parentId="0";

    /**
     * 是否外部表。
     */
    private boolean external=false;

}
