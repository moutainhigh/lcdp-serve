package com.redxun.user.org.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.redxun.common.base.db.BaseDao;
import com.redxun.user.org.entity.OsGroupMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 用户组下的授权菜单Mapper接口
 * 
 * @author yjy
 * @date 2019-11-14
 */
@Mapper
public interface OsGroupMenuMapper extends BaseDao<OsGroupMenu> {

    List<OsGroupMenu> findList(Page<OsGroupMenu> page, @Param("p") Map<String, Object> params);

    List<OsGroupMenu> getGrantMenusByGroupId(@Param("groupId")String groupId);

    /**
     * 根据用户获取他有权限的菜单。
     * @param userId
     * @param tenantId
     * @return
     */
    List<OsGroupMenu> getResourceByGrade(@Param("userId") String userId,@Param("tenantId") String tenantId);


    /**
     * 根据菜单ids获取权限
     * @param menuIds
     * @return
     */
    List<OsGroupMenu> getGrantMenusByMenuIds(@Param("menuIds") String menuIds);

    List<OsGroupMenu> getAppsByGroups(@Param("roles")String roles);

}
