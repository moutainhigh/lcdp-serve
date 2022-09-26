package com.redxun.user.org.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.redxun.common.base.db.BaseDao;
import com.redxun.user.org.entity.OsRankType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 用户组等级分类
 *
 * @author yjy
 * @date 2019-10-29 17:31:18
 */
@Mapper
public interface OsRankTypeMapper extends BaseDao<OsRankType> {
    /**
     * 分页查询用户列表
     * @param page
     * @param params
     * @return
     */
    List<OsRankType> findList(Page<OsRankType> page, @Param("p") Map<String, Object> params);

    /**
     * 是否存在
     * @param params
     * @return
     */
    int isExist(@Param("p") Map<String, Object> params);
}
