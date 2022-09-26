package com.redxun.user.org.mapper;

import com.redxun.common.base.db.BaseDao;
import com.redxun.user.org.entity.OsPropertiesGroup;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* 自定义属性分组数据库访问层
*/
@Mapper
public interface OsPropertiesGroupMapper extends BaseDao<OsPropertiesGroup> {
    /**
     * 获取扩展属性组
     * @return
     */
    List<OsPropertiesGroup> getPropertiesGroups(@Param("dimId")String dimId, @Param("tenantId")String tenantId);

}
