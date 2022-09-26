package com.redxun.common.base.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.redxun.common.model.SuperEntity;
import lombok.Data;
import org.apache.ibatis.type.JdbcType;


/**
 * 带租户ID基础实体。
 */
@Data
public abstract class BaseExtEntity<T> extends SuperEntity<T>  {

    //租户ID
    @TableField(value = "TENANT_ID_",fill = FieldFill.INSERT,jdbcType = JdbcType.VARCHAR)
    private String tenantId;


    /**
     * 分公司ID
     */
    @TableField(value = "COMPANY_ID_",fill = FieldFill.INSERT,jdbcType = JdbcType.VARCHAR)
    private String companyId;

}
