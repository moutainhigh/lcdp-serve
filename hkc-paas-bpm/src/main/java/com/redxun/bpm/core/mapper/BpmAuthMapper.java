package com.redxun.bpm.core.mapper;

import com.redxun.bpm.core.entity.BpmAuth;
import org.apache.ibatis.annotations.Mapper;
import com.redxun.common.base.db.BaseDao;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
* 流程授权表数据库访问层
*/
@Mapper
public interface BpmAuthMapper extends BaseDao<BpmAuth> {
    /**
     * 查询当前用户授权的数据
     * @param params
     * @return
     */
    List<BpmAuth> getByToAuthUserId(@Param("w") Map<String, Object> params);

    /**
     * 查询当前用户授权的总数
     * @param params
     * @return
     */
    Integer getCountByToAuthUserId(@Param("w")Map<String, Object> params);
}
