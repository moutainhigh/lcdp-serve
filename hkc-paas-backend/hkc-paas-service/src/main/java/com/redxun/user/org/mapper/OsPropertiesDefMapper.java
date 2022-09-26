package com.redxun.user.org.mapper;

import com.redxun.common.base.db.BaseDao;
import com.redxun.user.org.entity.OsPropertiesDef;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* 自定义属性配置数据库访问层
*/
@Mapper
public interface OsPropertiesDefMapper extends BaseDao<OsPropertiesDef> {

    /**
     * 根据groupId删除分组
     */
    void deleteByGroupId(String groupId);


    /**
     * 根据维度id获取扩展属性值
     * @param dimId
     * @param tenantId
     * @return
     */
    List<OsPropertiesDef> getPropertiesByDimId(@Param(value = "dimId") String dimId, @Param(value = "tenantId")String tenantId);
}
