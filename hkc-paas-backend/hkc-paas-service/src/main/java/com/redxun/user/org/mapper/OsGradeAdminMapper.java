package com.redxun.user.org.mapper;

import com.redxun.common.base.db.BaseDao;
import com.redxun.user.org.entity.OsGradeAdmin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
* 分级管理员数据库访问层
*/
@Mapper
public interface OsGradeAdminMapper extends BaseDao<OsGradeAdmin> {
    /**
     * 获取授权的机构下的某个机构某个参数下的分级管理员
     * @param params
     * @return
     */
    public List<OsGradeAdmin> getAdminByUserIdAndTenantId(@Param("p") Map<String, Object> params);
}
