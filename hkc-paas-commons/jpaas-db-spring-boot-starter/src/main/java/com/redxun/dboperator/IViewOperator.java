package com.redxun.dboperator;

import com.redxun.dboperator.model.Table;

import java.util.List;

/**
 * 数据视图操作接口
 */
public interface IViewOperator extends IDbType {

    /**
     * 创建或替换视图
     * @param viewName
     * @param sql
     * @throws Exception
     */
    void createOrRep(String viewName,String sql) throws Exception;

    /**
     * 使用模糊匹配，获取系统视图名称
     * @param viewName
     * @return
     * @throws Exception
     */
    List<String> getViews(String viewName) throws Exception;

    /**
     * 根据视图名称，使用精确匹配，获取视图详细信息
     * @param viewName
     * @return
     * @throws Exception
     */
    Table getModelByViewName(String viewName) throws Exception;

    /**
     * 根据视图名，使用模糊匹配，获取视图详细信息
     * @param viewName
     * @return
     * @throws Exception
     */
    List<Table> getViewsByName(String viewName) throws Exception;
}
