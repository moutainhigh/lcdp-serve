package com.redxun.user.org.mapper;

import com.redxun.common.base.db.BaseDao;
import com.redxun.user.org.entity.OsDimension;
import java.util.List;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * 用户组维度Mapper接口
 * 
 * @author yjy
 * @date 2019-11-08
 */
@Mapper
public interface OsDimensionMapper extends BaseDao<OsDimension> {
    /**
     * 分页查询用户列表
     * @param page
     * @param params
     * @return
     */
    List<OsDimension> findList(Page<OsDimension> page, @Param("p") Map<String, Object> params);

    /**
     * 获取所有用户列表
     * @return
     */
    List<OsDimension> findList();

    /**
     * 按Key与供应商ID获取维度信息
     * @param code
     * @param tenantId
     * @return
     */
    OsDimension getByCodeTenantId(@Param("code") String code,@Param("tenantId") String tenantId);

    /**
     * 获取某个租户下的所有用户组维度
     * @param tenantId
     * @return
     */
    List<OsDimension> getTenantDims(@Param("tenantId") String tenantId);
}
