
package com.redxun.system.core.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.system.core.entity.SysApp;
import com.redxun.system.core.entity.SysAppFavorites;
import com.redxun.system.core.mapper.SysAppFavoritesMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;

/**
* [平台开发应用收藏夹]业务服务类
*/
@Service
public class SysAppFavoritesServiceImpl extends SuperServiceImpl<SysAppFavoritesMapper, SysAppFavorites> implements BaseService<SysAppFavorites> {

    @Resource
    private SysAppFavoritesMapper sysAppFavoritesMapper;

    @Override
    public BaseDao<SysAppFavorites> getRepository() {
        return sysAppFavoritesMapper;
    }

    /**
    *  功能：收藏应用
     * @param userId 用户ID
     * @param appId 应用ID
    * @return boolean
    * @author  Elwin ZHANG
    * @date 2022/1/14 18:29
    **/
    public boolean addFavorite(String userId,String appId){
        try {
            saveFavorite(userId, appId, (short) 1);
            return true;
        }catch (Exception e){
            return  false;
        }
    }
    /**
     *  功能：取消收藏应用
     * @param userId 用户ID
     * @param appId 应用ID
     * @return boolean
     * @author  Elwin ZHANG
     * @date 2022/1/14 18:29
     **/
    public boolean cancelFavorite(String userId,String appId){
        try {
            saveFavorite(userId, appId, (short) 0);
            return true;
        }catch (Exception e){
            return  false;
        }
    }
    /**
    *  功能：收藏或取消收藏应用
     * @param userId 用户ID
     * @param appId 应用ID
     * @param isFav 是否收藏
    * @author  Elwin ZHANG
    * @date 2022/1/14 18:19
    **/
    private  String saveFavorite(String userId,String appId, short isFav){
        SysAppFavorites favorite =getFavorite(userId,appId);
        // 存在旧的记录则更新
        if(favorite!=null){
            favorite.setIsFav(isFav);
            favorite.setFavTime(new Date());
            sysAppFavoritesMapper.updateById(favorite);
            return favorite.getFavId();
        }
        //新增
        favorite=new SysAppFavorites();
        favorite.setIsFav(isFav);
        favorite.setAppId(appId);
        favorite.setFavTime(new Date());
        favorite.setUserId(userId);
        String newId=IdGenerator.getIdStr();
        favorite.setPkId(newId);
        sysAppFavoritesMapper.insert(favorite);
        return favorite.getFavId();
    }

    /**
     *  功能：收藏或取消收藏应用
     * @param userId 用户ID
     * @param appId 应用ID
     * @param lastUseTime 最近使用时间
     * @author  Elwin ZHANG
     * @date 2022/1/14 18:19
     **/
    public String saveLastUse(String userId,String appId, Date lastUseTime){
        SysAppFavorites favorite =getFavorite(userId,appId);
        // 存在旧的记录则更新
        if(favorite!=null){
            favorite.setLastUseTime(lastUseTime);
            sysAppFavoritesMapper.updateById(favorite);
            return favorite.getFavId();
        }
        //新增
        favorite=new SysAppFavorites();
        favorite.setLastUseTime(lastUseTime);
        favorite.setAppId(appId);
        favorite.setUserId(userId);
        favorite.setIsFav((short)0);
        String newId=IdGenerator.getIdStr();
        favorite.setPkId(newId);
        sysAppFavoritesMapper.insert(favorite);
        return favorite.getFavId();
    }

    /**
    *  功能：查找用户有没有收藏或使用过某个应用
    * @param userId 用户ID
     * @param appId 应用ID
    * @return com.redxun.system.core.entity.SysAppFavorites
    * @author  Elwin ZHANG
    * @date 2022/1/14 17:58
    **/
    public  SysAppFavorites getFavorite(String userId,String appId){
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("APP_ID_ ", appId);
        wrapper.eq("USER_ID_ ", userId);
        return sysAppFavoritesMapper.selectOne(wrapper);
    }

    /**
     *  功能：获取用户收藏 的应用列表
     * @param userId 用户ID
     * @return java.util.List<com.redxun.system.core.entity.SysApp>
     * @author  Elwin ZHANG
     * @date 2022/1/14 17:39
     **/
    public List<SysApp> getFavorites(String userId) {
        return sysAppFavoritesMapper.getFavorites(userId);
    }

    /**
     *  功能：获取用户最近使用的应用列表
     * @param userId 用户ID
     * @return java.util.List<com.redxun.system.core.entity.SysApp>
     * @author  Elwin ZHANG
     * @date 2022/1/14 17:39
     **/
    public List<SysApp> getLastUse( String userId){
        return sysAppFavoritesMapper.getLastUse(userId);
    }

}
