package com.redxun.system.core.mapper;

import com.redxun.common.base.db.BaseDao;
import com.redxun.system.core.entity.SysRouting;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* 路由数据库访问层
*/
@Mapper
public interface SysRoutingMapper extends BaseDao<SysRouting> {

    List<SysRouting> getRoutingByType(String routeType);
    void deleteByRoutIds(@Param("routIds") List<String> routIds);
}
