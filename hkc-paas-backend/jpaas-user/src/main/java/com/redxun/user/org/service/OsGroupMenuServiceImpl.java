package com.redxun.user.org.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import com.redxun.api.sys.RemoteMenuService;
import com.redxun.cache.CacheUtil;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.dto.SysMenuDto;
import com.redxun.common.model.PageResult;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.tool.constant.CommonConstant;
import com.redxun.common.utils.DbLogicDelete;
import com.redxun.user.org.entity.OsGroupMenu;
import com.redxun.user.org.mapper.OsGroupMenuMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 用户组下的授权菜单Service业务层处理
 * 
 * @author yjy
 * @date 2019-11-14
 */
@Slf4j
@Service
public class OsGroupMenuServiceImpl extends SuperServiceImpl<OsGroupMenuMapper, OsGroupMenu> implements BaseService<OsGroupMenu> {

    @Resource
    private OsGroupMenuMapper osGroupMenuMapper;



    @Resource
    private RemoteMenuService remoteMenuService;


    @Override
    public BaseDao<OsGroupMenu> getRepository() {
        return osGroupMenuMapper;
    }

    /**'
     * 根据用户组ID，应用ID删除所有的用户组菜单
     * @param groupId
     * @param appId
     */
    public void removeByGroupId(String groupId, String appId){
        Map m = Maps.newHashMap();
        m.put("GROUP_ID_", groupId);
        if (StringUtils.isNotEmpty(appId)) {
            m.put("APP_ID_", appId);
        }
        removeByMap(m);

        this.removeApiCache();

    }


    public void removeApiCache(){
        //清除接口授权缓存
        CacheUtil.remove(CommonConstant.API_REGION, CommonConstant.API_KEY);
    }

    /**
     * 通过组用户组ID获取所有的用户组授权的菜单
     * @param groupId
     * @return
     */
    public List<OsGroupMenu> getGrantMenusByGroupId(String groupId){
        return osGroupMenuMapper.getGrantMenusByGroupId(groupId);
    }




    /**
     * 通过用户组ID获取所有的
     * @param groupId
     * @return
     */
    public PageResult<OsGroupMenu> findList(Map<String, Object> params){
        Page<OsGroupMenu> page = new Page<>(MapUtils.getInteger(params, "pageNum"), MapUtils.getInteger(params, "pageSize"));
        List<OsGroupMenu> list  =  baseMapper.findList(page, params);
        return PageResult.<OsGroupMenu>builder().data(list).code(0).count(page.getTotal()).build();
    }

    public List<String> findMenuListByGroupId(String groupId){
        //菜单信息
        List<OsGroupMenu> menuList  =  osGroupMenuMapper.selectList(new QueryWrapper<OsGroupMenu>().eq("GROUP_ID_", groupId));
        List<String> menuIds=menuList.stream().map(OsGroupMenu::getMenuId).collect(Collectors.toList());
        //获取应用ID，在前端选中第一级节点。
        List<String> appIds=menuList.stream().map(OsGroupMenu::getAppId).distinct().collect(Collectors.toList());

        menuIds.addAll(appIds);

        return menuIds;
    }

    /**
     * 保存更新
     * @param osGroupMenus
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean saveOrUpdate(List<OsGroupMenu> osGroupMenus) {

        if (!CollectionUtils.isEmpty(osGroupMenus)) {
            osGroupMenus.forEach(e-> e.setId(IdGenerator.getIdStr()));
            saveBatch(osGroupMenus);
        }
         return true;
    }


    /**
     * 根据用户获取菜单授权情况。
     * @param userId
     * @param tenantId
     * @return
     */
    public List<OsGroupMenu> getResourceByGrade(String userId,String tenantId){
        List list=osGroupMenuMapper.getResourceByGrade(userId,tenantId);
        return list;
    }


    /**
     * 根据菜单ids获取菜单授权情况
     * @param menuIds
     * @return
     */
    public List<OsGroupMenu> getGrantMenusByMenuIds(String menuIds){
        return osGroupMenuMapper.getGrantMenusByMenuIds(menuIds);
    }

    /**
     * 根据用户组获取授权菜单。
     * @param groupId
     * @param appId
     * @return
     */
    public List<String> getResourceByAppIdAndGroupId(String appId, String groupId) {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("APP_ID_",appId);
        queryWrapper.eq("GROUP_ID_",groupId);
        List<OsGroupMenu> list =  osGroupMenuMapper.selectList(queryWrapper);
        List<String> result = list.stream().map(e -> e.getMenuId()).collect(Collectors.toList());
        return result;
    }

    /**
     * 根据用户组ID获取授权的菜单id列表数据
     * @param groupId
     * @return
     */
    public List<String> getResourceByGroupId(String groupId) {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("GROUP_ID_",groupId);
        List<OsGroupMenu> list =  osGroupMenuMapper.selectList(queryWrapper);
        List<String> result = list.stream().map(e -> e.getMenuId()).collect(Collectors.toList());
        return result;
    }

    /**
     * 根据用户组ID获取授权的菜单id列表数据
     * @param groupIds
     * @return
     */
    public List<String> getResourceByGroupIdList(String groupIds) {
        List<String> groupIdList = Arrays.asList(groupIds.split(","));
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.in("GROUP_ID_",groupIdList);
        List<OsGroupMenu> list =  osGroupMenuMapper.selectList(queryWrapper);
        List<String> result = list.stream().map(e -> e.getMenuId()).collect(Collectors.toList());
        return result;
    }

    public List<String> getAppIdGroupIdList(String groupId){
        List<String> groupIdList = Arrays.asList(groupId.split(","));
        List<OsGroupMenu> groupMenus = osGroupMenuMapper.selectList(new QueryWrapper<OsGroupMenu>().in("GROUP_ID_", groupIdList));
        List<String> result = groupMenus.stream().map(e -> e.getAppId()).collect(Collectors.toList());
        return result;
    }

    /**
     * 根据用户组获取授权菜单。
     * @param groupIds
     * @param appId
     * @return
     */
    public List<OsGroupMenu> getByGroupIds(List<String> groupIds,String appId){
        QueryWrapper<OsGroupMenu> queryWrapper = new QueryWrapper<OsGroupMenu>().in("GROUP_ID_", groupIds);
        queryWrapper.and(w -> w.in("MENU_TYPE_","F","C").or().isNull("MENU_TYPE_"));
        if(StringUtils.isNotEmpty(appId)){
            queryWrapper.eq("APP_ID_",appId);
        }
        //逻辑删除
        if (DbLogicDelete.getLogicDelete()) {
            queryWrapper.eq("DELETED_","0");
        }
        List<OsGroupMenu> groupMenus = osGroupMenuMapper.selectList(queryWrapper);

        return groupMenus;
    }

    /**
     * 根据用户组和应用ID获取菜单授权。
     * @param groupIds
     * @param appId
     * @return
     */
    public List<SysMenuDto> getSysMenuByGroupIds(List<String> groupIds, String appId){
        List<OsGroupMenu> menuList= getByGroupIds(groupIds, appId);
        List<SysMenuDto> dtoList=new ArrayList<>();
        for(OsGroupMenu menu:menuList){
            SysMenuDto dto=new SysMenuDto();
            BeanUtil.copyProperties(dto,menu);
            dto.setId(menu.getMenuId());
            dtoList.add(dto);
        }
        dtoList=dtoList.stream().distinct().collect(Collectors.toList());

        return dtoList;
    }

    /**
     * 根据ID获取
     * @param groupIds
     * @return
     */
    public List<OsGroupMenu> getAppsByGroups(List<String> groupIds){

        QueryWrapper wrapper=new QueryWrapper();
        wrapper.in("GROUP_ID_",groupIds);
        wrapper.apply(" APP_ID_=MENU_ID_" ,"");


        List<OsGroupMenu> groupMenus= osGroupMenuMapper.selectList(wrapper);

        return groupMenus;
    }

}
