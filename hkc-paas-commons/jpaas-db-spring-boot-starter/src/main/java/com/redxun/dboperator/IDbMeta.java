package com.redxun.dboperator;

import com.redxun.dboperator.model.Table;

import java.util.List;

/**
 * DB元数据接口
 */
public interface IDbMeta {

    /**
     * 根据名称模糊获取所有对象
     * @param name
     * @return
     * @throws Exception
     */
    List<Table> getObjectsByName(String name) throws Exception;

    Table getByName(String name);

    /**
     * 根据名称获取表模型对象
     * @param name
     * @return
     * @throws Exception
     */
    Table getModelByName(String name) throws Exception;
}
