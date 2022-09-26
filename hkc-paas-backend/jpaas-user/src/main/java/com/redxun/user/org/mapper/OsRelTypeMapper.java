package com.redxun.user.org.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.redxun.common.base.db.BaseDao;
import com.redxun.user.org.entity.OsRelType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 关系类型定义Mapper接口
 * 
 * @author yjy
 * @date 2019-11-08
 */
@Mapper
public interface OsRelTypeMapper extends BaseDao<OsRelType> {
    /**
     * 分页查询用户列表
     * @param page
     * @param params
     * @return
     */
    List<OsRelType> findList(Page<OsRelType> page, @Param("p") Map<String, Object> params);

    /**
     * 通过维度ID，关系ID，等级获取关系类型数据
     * @param tenantId
     * @param dimId
     * @param relType
     * @param level
     * @return
     */
    List<OsRelType> getOsRelTypeOfRelTypeLevel(@Param("tenantId")String tenantId,
                                               @Param("dimId")String dimId,
                                               @Param("relType")String relType,
                                               @Param("level")Integer level,
                                               @Param("deleted")String deleted);

    /**
     * 通过维度ID，关系ID获取关系类型数据
     * @param tenantId
     * @param dimId
     * @param relType
     * @return
     */
    List<OsRelType> getOsRelTypeOfRelType(@Param("tenantId")String tenantId,
                                          @Param("dimId")String dimId,
                                          @Param("relType")String relType);
}
