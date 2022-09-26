package com.redxun.system.core.mapper;

import com.redxun.system.core.entity.SysDic;
import org.apache.ibatis.annotations.Mapper;
import com.redxun.common.base.db.BaseDao;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* 数字字典数据库访问层
*/
@Mapper
public interface SysDicMapper extends BaseDao<SysDic> {
    /**
     * 获取某一分类下的所有分类
     * @param treeId
     * @return
     */
    List<SysDic> getByTreeId(String treeId);

    /**
     * 获取某一分类下的所有数字字典
     * @param treeId
     * @return
     */
    List<SysDic> getTopDicByTreeId(String treeId);

    /**
     * 取得某父类下所有的分类数字项
     * @param parentId
     * @return
     */
    List<SysDic> getByParentId(String parentId);

    /**
     * 按路径取得某一项下下级的所有数字项
     * @param path
     * @return
     */
    List<SysDic> getByLikePath(String path);

    /**
     * 按树路径删除所有下下级的所有数字项
     * @param path
     */
    void delByLeftPath(String path);

    /**
     * 取得某类下一级子节点的数量
     * @param dicId
     * @return
     */
    SysDic getById(String dicId);

    /**
     * 根据id与父id查询数据字典
     * @param treeId
     * @param parentId
     * @return
     */
    List<SysDic> getByPidAndDicId(@Param("treeId")String treeId, @Param("parentId")String parentId);

    List<SysDic> getByPidAndName(@Param("treeId")String treeId, @Param("parentId")String parentId, @Param("name")String name);

    List<SysDic> getDicByDicValue(@Param("treeId")String treeId, @Param("dicValueList")List<String> dicValueList);
}
