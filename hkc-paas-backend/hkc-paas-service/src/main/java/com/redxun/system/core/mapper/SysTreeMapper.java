package com.redxun.system.core.mapper;

import com.redxun.common.base.db.BaseDao;
import com.redxun.system.core.entity.SysTree;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* 系统分类树
数据库访问层
*/
@Mapper
public interface SysTreeMapper extends BaseDao<SysTree> {
    /**
     * 查找该分类一级节点下的所有树
     * @param catKey
     * @return
     */
    List<SysTree> getTopNodesByCatKey(@Param(value = "catKey") String catKey,@Param(value = "tenantId") String tenantId
    ,@Param(value = "appId") String appId,@Param(value = "companyId") String companyId);

    /**
     * 查找某个树节点下的所有的节点列表
     * @param parentId
     * @return
     */
    List<SysTree> getByParentId(String parentId,String companyId);

    /**
     * 按树路径进行数据查询
     * @param path
     * @return
     */
    List<SysTree> getByLikePath(String path);

    /**
     * 按树路径删除数据
     * @param path
     */
    void delByLeftPath(String path);

    /**
     * 取得其下所有子节点的数量
     * @param parentId
     * @return
     */
    Long getChildCounts(String parentId);


    /**
     * 通过主键获取实体数据
     * @param treeId
     * @return
     */
    SysTree getById(String treeId);

    /**
     * 通过分类Key获取分类树集合
     * @param catKey
     * @return
     */
    List<SysTree> getByCatKey(String catKey);



    /**
     * 查找公司级别的该分类一级节点下的所有树
     * @param catKey
     * @param tenantId
     * @param companyId
     * @return
     */
    List<SysTree> getCompanyTopNodesByCatKey(@Param(value = "catKey") String catKey,@Param(value = "tenantId") String tenantId, @Param(value = "companyId")String companyId
            ,@Param(value = "appId") String appId);

    /**
     * 根据父id和catKey获取公司级别的数据
     * @param parentId
     * @param catKey
     * @param companyId
     * @return
     */
    List<SysTree> getCompanyTreeByPidAndCatKey(@Param("parentId") String parentId, @Param("catKey")String catKey, @Param(value = "companyId")String companyId);

    /**
     * 查找公司级别的某个树节点下的所有的节点列表
     * @param parentId
     * @param companyId
     * @return
     */
    List<SysTree> getCompanyTreeByParentId(@Param("parentId") String parentId, @Param(value = "companyId")String companyId);

    /**
     * 取得公司级别的catKey下节点
     * @param catKey
     * @param tenantId
     * @param companyId
     * @return
     */
    List<SysTree> getCompanyTreeByCatKey(@Param("catKey") String catKey, @Param("tenantId") String tenantId, @Param("companyId") String companyId,@Param(value = "appId") String appId);

    /**
     * 查找该分类一级节点下的所有树
     * @param catKey
     * @return
     */
    List<SysTree> getTopNodesByReadTreeId(@Param(value = "catKey") String catKey,@Param(value = "tenantId") String tenantId
            ,@Param(value = "appId") String appId,@Param(value = "readTreeId") String readTreeId);

    /**
     * 按树路径删除数据-逻辑删除
     * @param path
     */
    void updateByLeftPath(@Param("path")String path);


    /**
     * 查找该分类一级节点下的所有树
     * @param catKey
     * @return
     */
    List<SysTree> getNodesByCatKey(@Param(value = "catKey") String catKey,@Param(value = "tenantId") String tenantId
            ,@Param(value = "appId") String appId,@Param(value = "companyId") String companyId);
}
