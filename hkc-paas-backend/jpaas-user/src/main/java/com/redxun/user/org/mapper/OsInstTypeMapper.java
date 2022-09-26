package com.redxun.user.org.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.redxun.common.base.db.BaseDao;
import com.redxun.user.org.entity.OsInstType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * 机构类型
 * 
 * @author yjy
 * @date 2019-10-29 17:31:21
 */
@Mapper
public interface OsInstTypeMapper extends BaseDao<OsInstType> {

    /**
     * 分页查询
     * @param page
     * @param params
     * @return
     */
    @Override
    IPage<OsInstType> query(IPage<OsInstType> page, @Param("w") Map<String, Object> params);
}
