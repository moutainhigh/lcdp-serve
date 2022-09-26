package com.redxun.common.base.db;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 基础DAO类，继承BaseMapper实现常用的CRUD功能，同步加上分页查询
 * @param <T>
 */
public interface BaseDao<T>  extends BaseMapper<T> {

    /**
     * 分页查询
     * @param page
     * @param params
     * @return
     */
    IPage<T> query(IPage<T> page, @Param("w") Map<String, Object> params);


    /**
     * 根据条件查询
     * @param params
     * @return
     */
    List<T> query( @Param("w") Map<String, Object> params);


}
