package com.redxun.user.org.mapper;

import com.redxun.common.base.db.BaseDao;
import com.redxun.user.org.entity.OsPropertiesVal;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* 扩展属性值数据库访问层
*/
@Mapper
public interface OsPropertiesValMapper extends BaseDao<OsPropertiesVal> {

    /**
     * 根据所属人获取扩展属性值
     * @param ownerId
     * @return
     */
    List<OsPropertiesVal> getByOwnerId(String ownerId);

    /**
     * 根据主键ID来更新属性值
     * @param osPropertiesVal
     * @return
     */
    int updateById(OsPropertiesVal osPropertiesVal);

    /**
     * 根据分组id删除扩展属性值
     * @param groupId
     */
    void deleteByGroupId(String groupId);

    /**
     * 根据属性id删除扩展属性值
     * @param defId
     */
    void deleteByDefId(String defId);

    /**
     * 根据条件获取所属人id
     * @param dimId
     * @param condition
     * @return
     */
    List<String> getOwnerIdsByCondition(@Param("dimId")String dimId, @Param("condition")String condition);
}
