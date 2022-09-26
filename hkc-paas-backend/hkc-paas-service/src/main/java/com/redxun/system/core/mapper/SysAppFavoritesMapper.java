package com.redxun.system.core.mapper;

import com.redxun.common.base.db.BaseDao;
import com.redxun.system.core.entity.SysApp;
import com.redxun.system.core.entity.SysAppFavorites;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* 平台开发应用收藏夹数据库访问层
*/
@Mapper
public interface SysAppFavoritesMapper extends BaseDao<SysAppFavorites> {

    /**
    *  功能：获取用户收藏 的应用列表
    * @param userId 用户ID
    * @return java.util.List<com.redxun.system.core.entity.SysApp>
    * @author  Elwin ZHANG
    * @date 2022/1/14 17:39
    **/
    List<SysApp> getFavorites(@Param("userId") String userId);

    /**
     *  功能：获取用户最近使用的应用列表
     * @param userId 用户ID
     * @return java.util.List<com.redxun.system.core.entity.SysApp>
     * @author  Elwin ZHANG
     * @date 2022/1/14 17:39
     **/
    List<SysApp> getLastUse(@Param("userId") String userId);
}
