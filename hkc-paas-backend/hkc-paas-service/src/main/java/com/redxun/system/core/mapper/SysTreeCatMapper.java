package com.redxun.system.core.mapper;

import com.redxun.common.base.db.BaseDao;
import com.redxun.system.core.entity.SysTreeCat;
import org.apache.ibatis.annotations.Mapper;

/**
 * user数据库访问层
 */
@Mapper
public interface SysTreeCatMapper extends BaseDao<SysTreeCat> {
    /**
     * 根据分类获取系统分类
     * @param key
     * @return
     */
    SysTreeCat getByKey(String key);
}