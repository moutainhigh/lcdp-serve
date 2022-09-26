package com.redxun.common.base.entity;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 列表查询参数对象
 */
@Data
public class QueryData {
    /**
     * 分页序号
     */
    private Integer pageNo;
    /**
     * 分页大小
     */
    private Integer pageSize;
    /**
     * 排序字段
     */
    private String sortField;
    /**
     * 排序
     */
    private String sortOrder;

    /**
     * 映射参数，格式如：ORDER_ID_,Q_ORDER_ID_S_EQ 键值的映射
     */
    private Map<String,String> params=new HashMap<>();
}
