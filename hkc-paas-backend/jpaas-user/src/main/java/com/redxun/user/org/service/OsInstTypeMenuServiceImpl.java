package com.redxun.user.org.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.dto.SysMenuDto;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.DbLogicDelete;
import com.redxun.user.org.entity.OsGroupMenu;
import com.redxun.user.org.entity.OsInst;
import com.redxun.user.org.entity.OsInstTypeMenu;
import com.redxun.user.org.mapper.OsGroupMenuMapper;
import com.redxun.user.org.mapper.OsInstTypeMenuMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
* [机构类型授权菜单]业务服务类
*/
@Service
public class OsInstTypeMenuServiceImpl extends SuperServiceImpl<OsInstTypeMenuMapper, OsInstTypeMenu> implements BaseService<OsInstTypeMenu> {

    @Resource
    private OsInstTypeMenuMapper osInstTypeMenuMapper;
    @Resource
    private OsInstServiceImpl osInstService;
    @Resource
    private OsGroupMenuMapper osGroupMenuMapper;

    @Override
    public BaseDao<OsInstTypeMenu> getRepository() {
        return osInstTypeMenuMapper;
    }

    /**
     * 根据实例类型获取菜单数据。
     * @param instTypeId
     * @param appId
     * @return
     */
    public List<OsInstTypeMenu> getByInstTypeId(String instTypeId,String appId){

        QueryWrapper<OsInstTypeMenu> queryWrapper = new QueryWrapper<OsInstTypeMenu>().eq("INST_TYPE_ID_", instTypeId);
        queryWrapper.in("MENU_TYPE_","F","C");
        if(StringUtils.isNotEmpty(appId)){
            queryWrapper.eq("APP_ID_",appId);
        }
        //逻辑删除
        if (DbLogicDelete.getLogicDelete()) {
            queryWrapper.eq("DELETED_","0");
        }

        List<OsInstTypeMenu> instTypeMenus  =  osInstTypeMenuMapper.selectList(queryWrapper);

        return instTypeMenus;
    }

    private List<SysMenuDto> convertInstMenu(List<OsInstTypeMenu> menuList){
        List<SysMenuDto> dtos=new ArrayList<>();
        for(OsInstTypeMenu menu:menuList){
            SysMenuDto menuDto=new SysMenuDto();
            BeanUtil.copyProperties(menuDto,menu);
            menuDto.setId(menu.getMenuId());
            dtos.add(menuDto);
        }
        return dtos;
    }

    public List<SysMenuDto> getMenuDaoByInstTypeId(String instTypeId,String appId){
        List<OsInstTypeMenu> menus = getByInstTypeId(instTypeId, appId);
        List<SysMenuDto> dtos= convertInstMenu(menus);
        return dtos;
    }



    /**
     * 根据机构类型查找菜单及接口信息
     * <pre>
     *     1.根据租户类型ID获取租户类型的菜单。
     *     2.获取APPID同时获取应用列表。
     *     3.遍历用户类型菜单
     *          3.1 如果菜单的父ID为0,那么设置父ID为 appId.
     *          3.2 设置菜单ID.
     * </pre>
     * @param instTypeId
     * @return
     */
    public List<SysMenuDto> findMenusByInstTypeId(String instTypeId){
        QueryWrapper<OsInstTypeMenu> menuWraper = new QueryWrapper<OsInstTypeMenu>().eq("INST_TYPE_ID_", instTypeId);
        menuWraper.in("MENU_TYPE_","F","C");
        //逻辑删除
        if (DbLogicDelete.getLogicDelete()) {
            menuWraper.eq("DELETED_","0");
        }
        //菜单信息
        List<OsInstTypeMenu> menuList  =  osInstTypeMenuMapper.selectList(menuWraper);

        List<String> menuIdList = menuList.stream()
                .filter(item -> "C".equals(item.getMenuType()))
                .map(item -> item.getMenuId()).collect(Collectors.toList());


        if(BeanUtil.isNotEmpty(menuIdList)){
            //获取菜单下的接口
            QueryWrapper wrapper=new QueryWrapper();
            wrapper.in("PARENT_ID_",menuIdList);
            wrapper.eq("MENU_TYPE_","I");
            //逻辑删除
            if (DbLogicDelete.getLogicDelete()) {
                wrapper.eq("DELETED_","0");
            }
            List<OsInstTypeMenu> interFaceList  =  osInstTypeMenuMapper.selectList(wrapper);
            menuList.addAll(interFaceList);
        }

        List<SysMenuDto> authList = Lists.newArrayList();

        for(OsInstTypeMenu menu:menuList){
            SysMenuDto menuDto=new SysMenuDto();
            cn.hutool.core.bean.BeanUtil.copyProperties(menu,menuDto);
            menuDto.setId(menu.getMenuId());
            authList.add(menuDto);

        }

        return authList;
    }


    @Transactional(rollbackFor = Exception.class)
    public boolean saveOrUpdate(List<OsInstTypeMenu> objs) {
        //用户组与用户如何动态生成关系实例
        if (!CollectionUtils.isEmpty(objs)) {

            Set<String> queryId= objs.parallelStream()
                    .map(OsInstTypeMenu::getInstTypeId).collect(Collectors.toSet());

            Map m = Maps.newHashMap();
            m.put("INST_TYPE_ID_", queryId.stream().findAny().get());
            removeByMap(m);

            objs.forEach(e-> e.setId(IdGenerator.getIdStr()));

            saveBatch(objs);
        }
        return true;
    }

    /**
     * 根据机构Id 查找机构类型授权的菜单资源ID。
     * @param tenantId
     * @return
     */
    public List<String> getInstTypeMenusByTenantId(String tenantId) {
        OsInst osInst = osInstService.get(tenantId);
        List<OsInstTypeMenu> menuList  =  osInstTypeMenuMapper.selectList(new QueryWrapper<OsInstTypeMenu>().eq("INST_TYPE_ID_", osInst.getInstType()));
        return  menuList.stream().map(item -> item.getMenuId()).collect(Collectors.toList()) ;
    }

    /**
     * 更新菜单权限
     * @param sysMenuDto
     */
    public void updateMenuControl(SysMenuDto sysMenuDto) {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("MENU_ID_",sysMenuDto.getId());
        List<OsInstTypeMenu> osInstTypeMenus = osInstTypeMenuMapper.selectList(queryWrapper);
        List<OsGroupMenu>  osGroupMenus= osGroupMenuMapper.selectList(queryWrapper);
        //机构菜单
        for (OsInstTypeMenu osInstTypeMenu : osInstTypeMenus) {
            String id = osInstTypeMenu.getId();
            cn.hutool.core.bean.BeanUtil.copyProperties(sysMenuDto,osInstTypeMenu);
            String parentId="0".equals(sysMenuDto.getParentId())?sysMenuDto.getAppId():sysMenuDto.getParentId();
            osInstTypeMenu.setId(id);
            osInstTypeMenu.setParentId(parentId);
            osInstTypeMenu.setMenuId(sysMenuDto.getId());
            osInstTypeMenuMapper.updateById(osInstTypeMenu);
        }
        //组织菜单
        for (OsGroupMenu osGroupMenu : osGroupMenus) {
            String id = osGroupMenu.getId();
            cn.hutool.core.bean.BeanUtil.copyProperties(sysMenuDto,osGroupMenu);
            String parentId="0".equals(sysMenuDto.getParentId())?sysMenuDto.getAppId():sysMenuDto.getParentId();
            osGroupMenu.setId(id);
            osGroupMenu.setParentId(parentId);
            osGroupMenu.setMenuId(sysMenuDto.getId());
            osGroupMenuMapper.updateById(osGroupMenu);
        }
    }

    /**
     * 删除菜单权限
     * @param idList
     */
    public void delMenuControl(List<String> idList) {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.in("MENU_ID_",idList);

        osInstTypeMenuMapper.delete(queryWrapper);
        osGroupMenuMapper.delete(queryWrapper);

    }

    public void deleteByInstTypeId(String instTypeId){
        osInstTypeMenuMapper.deleteByInstTypeId(instTypeId);
    }
}


