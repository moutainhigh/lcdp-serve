package com.redxun.system.core.mapper;

import com.redxun.system.core.entity.SysAuthRights;
import org.apache.ibatis.annotations.Mapper;
import com.redxun.common.base.db.BaseDao;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* 权限定义表数据库访问层
*/
@Mapper
public interface SysAuthRightsMapper extends BaseDao<SysAuthRights> {

    /**
     * 通过树ID获取权限定义
     * @param treeId
     * @return
     */
    List<SysAuthRights> getByTreeId(@Param("treeId")String treeId);
}
