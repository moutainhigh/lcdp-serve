package com.redxun.user.org.mapper;

import com.redxun.common.base.db.BaseDao;
import com.redxun.user.org.entity.OsGradeRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
* 分级管理员角色数据库访问层
*/
@Mapper
public interface OsGradeRoleMapper extends BaseDao<OsGradeRole> {
    /**
     * 根据用户获取角色列表
     * @param params
     * @return
     */
    List<OsGradeRole> getGroupByUserId(@Param("p") Map<String, Object> params);
}
