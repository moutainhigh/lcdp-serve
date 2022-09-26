package com.redxun.common.base.search;

import java.io.Serializable;

/**
 * sql 条件接口。
 */
public interface WhereParam  extends Serializable {

    /**
     * 用于构建where的SQL语句片段
     * @return
     */
    public String getSql();
}
