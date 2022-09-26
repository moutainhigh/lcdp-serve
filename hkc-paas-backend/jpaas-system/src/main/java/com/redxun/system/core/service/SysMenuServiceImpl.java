package com.redxun.system.core.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Maps;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.IUser;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.constant.MBoolean;
import com.redxun.common.dto.AuthDto;
import com.redxun.common.dto.SysMenuDto;
import com.redxun.common.model.JPaasUser;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.tool.constant.CommonConstant;
import com.redxun.common.utils.ContextUtil;
import com.redxun.common.utils.DbLogicDelete;
import com.redxun.common.utils.ExceptionUtil;
import com.redxun.common.utils.SysUserUtil;
import com.redxun.dto.sys.SysAppDto;
import com.redxun.dto.user.OsGradeRoleDto;
import com.redxun.feign.org.OrgClient;
import com.redxun.feign.org.OsGradeRoleClient;
import com.redxun.feign.org.OsGroupMenuClient;
import com.redxun.feign.org.UserClient;
import com.redxun.profile.ProfileContext;
import com.redxun.system.core.entity.SysApp;
import com.redxun.system.core.entity.SysMenu;
import com.redxun.system.core.mapper.SysAppMapper;
import com.redxun.system.core.mapper.SysMenuMapper;
import com.redxun.system.feign.FormBoListClient;
import com.redxun.system.feign.OsUserClient;
import com.redxun.util.SysUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;


/**
 * [系统分类树]业务服务类
 */
@Slf4j
@Service
public class SysMenuServiceImpl extends SuperServiceImpl<SysMenuMapper, SysMenu> implements BaseService<SysMenu> {

    @Resource
    private SysMenuMapper sysMenuMapper;

    @Resource
    private SysAppMapper sysAppMapper;

    @Resource
    private SysMenuReleaseServiceImpl sysMenuReleaseService;

    @Resource
    private FormBoListClient formBoListClient;

    @Resource
    private UserClient userClient;
    @Resource
    private OsUserClient osUserClient;

    @Resource
    private SysAppAuthMenuServiceImpl sysAppAuthMenuService;

    @Resource
    private OrgClient orgClient;

    @Resource
    private OsGroupMenuClient osGroupMenuClient;

    @Resource
    private OsGradeRoleClient osGradeRoleClient;

//    @Resource
//    private OsGroupAppClient osGroupAppClient;

    @Resource
    private SysAppServiceImpl sysAppService;



    @Override
    public BaseDao<SysMenu> getRepository() {
        return sysMenuMapper;
    }

    public JSONObject getButtonsBySysApp(SysApp sysApp) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("MENU_TYPE_", "F");
        queryWrapper.eq("APP_ID_", sysApp.getAppId());

        List<SysMenu> list = sysMenuMapper.selectList(queryWrapper);
        List<String> menuKeys=new ArrayList<>();
        for(SysMenu menu:list){
            menuKeys.add(menu.getMenuKey());
        }
        JSONObject json = new JSONObject();
        json.put(sysApp.getClientCode(),menuKeys);
        return json;
    }

    public JSONObject getAllButtonsByMenuType(String menuType) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("MENU_TYPE_", menuType);
        List<SysMenu> list = sysMenuMapper.selectList(queryWrapper);
        JSONObject appKeys = new JSONObject();
        getButtons(list, appKeys);
        return appKeys;
    }

    private void getButtons(List<SysMenu> menus, JSONObject appKeys) {
        if (BeanUtil.isEmpty(menus)) {
            return;
        }
        QueryWrapper queryWrapper = new QueryWrapper();
        List<SysApp> apps = sysAppMapper.selectList(queryWrapper);

        for (SysMenu menu : menus) {
            String appId = menu.getAppId();
            String appKey = getAppKey(appId, apps);
            if (StringUtils.isEmpty(appKey)) {
                continue;
            }
            JSONArray buttons = appKeys.getJSONArray(appKey);
            if (BeanUtil.isEmpty(buttons)) {
                buttons = new JSONArray();

            }
            buttons.add(menu.getMenuKey());
            appKeys.put(appKey, buttons);
        }
    }

    private String getAppKey(String appId, List<SysApp> apps) {
        String appKey = "";
        if (BeanUtil.isEmpty(apps)) {
            return appKey;
        }
        for (SysApp app : apps) {
            if (appId.equals(app.getAppId())) {
                appKey = app.getClientCode();
            }
        }
        return appKey;
    }


    public List<SysMenu> getByParentId(String menuId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("PARENT_ID_", menuId);
        queryWrapper.orderBy(true, true, "SN_");
        return sysMenuMapper.selectList(queryWrapper);
    }


    public SysMenu getByMenuKey(String menuKey) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("MENU_KEY_", menuKey);
        return sysMenuMapper.selectOne(queryWrapper);
    }

    public JsonResult saveOrUpdateSysMenu(SysMenu entity) {
        StringBuilder sb = new StringBuilder();
        String str = "";
        String pkId = entity.getPkId();
        if (BeanUtil.isEmpty(pkId)) {



            String id = IdGenerator.getIdStr();
            String path = "";
            if (StringUtils.isNotEmpty(entity.getParentId())) {
                SysMenu parentMenu = this.get(entity.getParentId());
                if (parentMenu != null) {
                    path = parentMenu.getPath() + id + ".";
                } else {
                    path = "0." + id + ".";
                    entity.setParentId("0");
                }
            } else {
                path = "0." + id + ".";
                entity.setParentId("0");
            }
            entity.setId(id);
            entity.setPath(path);
            str = "添加成功";
            this.save(entity);

            sb.append("添加菜单:" + entity.getName() + "(" + entity.getId() + ")");

            //新建发布菜单记录
            sysMenuReleaseService.creatSysMenuReleaseId(entity);
            createBoListJoinBtns(entity);
            // 保存已安装应用菜单
            sysAppAuthMenuService.saveAppAuthMenu(entity.getAppId(), id);
        } else {
            sb.append("更新菜单:" + entity.getName() + "(" + entity.getId() + ")");
            str = "更新成功";
            this.update(entity);
        }


        AuthDto authDto = new AuthDto();
        BeanUtil.copyProperties(authDto, entity);

        JsonResult result = JsonResult.getSuccessResult(str);
        result.setData(entity);
        return result;
    }

    private void createBoListJoinBtns(SysMenu menu) {
        String boListKey = menu.getBoListKey();
        if (StringUtils.isEmpty(boListKey) || !menu.getJoinBtns()) {
            return;
        }
        JSONArray btnsArr = formBoListClient.getPcListBtns(boListKey);
        if (BeanUtil.isEmpty(btnsArr)) {
            return;
        }
        syncBtns(menu, btnsArr);
    }

    public String syncBtns(SysMenu parentMenu, JSONArray btnsArr) {
        StringBuffer strBu = new StringBuffer();
        for (int i = 0; i < btnsArr.size(); i++) {
            JSONObject jsonObj = btnsArr.getJSONObject(i);
            String btnLabel = jsonObj.getString("btnLabel");
            String btnName = jsonObj.getString("btnName");
            String url = jsonObj.getString("url");
            if (StringUtils.isNotEmpty(url)) {
                int index = url.indexOf("?");
                if (index != -1) {
                    url = url.substring(0, index);
                }
            }
            SysMenu btnMenu = null;
            if (StringUtils.isNotEmpty(btnName)) {
                btnMenu = getByMenuKey(btnName);
                if (btnMenu == null) {
                    btnMenu = new SysMenu();
                    btnMenu.setName(btnLabel);
                    btnMenu.setMenuKey(btnName);
                    btnMenu.setAppId(parentMenu.getAppId());
                    btnMenu.setMenuType("F");
                    btnMenu.setSn("0");
                    btnMenu.setParentId(parentMenu.getId());
                    insert(btnMenu);
                } else {
                    strBu.append("【" + btnName + "】,");
                }
            }

            JSONArray childs = jsonObj.getJSONArray("children");

            if (childs == null) {
                continue;
            }

            syncBtns(btnMenu, childs);
        }
        return strBu.toString();
    }

    public String getReleaseUrl(String appId, String path) {
        SysApp sysApp = sysAppMapper.selectById(appId);
        String releaseUrl = sysApp.getClientName();

        String[] paths = path.split("[.]");

        QueryWrapper wrapper = new QueryWrapper();
        wrapper.in("MENU_ID_", paths);
        wrapper.orderBy(true, true, "PATH_");
        List<SysMenu> menus = sysMenuMapper.selectList(wrapper);
        for (SysMenu menu : menus) {
            releaseUrl += "-" + menu.getName();
        }
        return releaseUrl;
    }


    /**
     * 根据应用获取菜单和按钮及菜单下的接口。
     *
     * @param appId
     * @return
     */
    public List<SysMenu> getMenusByAppId(String appId) {

        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("APP_ID_", appId);
        wrapper.in("MENU_TYPE_", "F", "C");
        wrapper.orderByAsc("SN_");
        //逻辑删除
        if (DbLogicDelete.getLogicDelete()) {
            wrapper.eq("DELETED_","0");
        }
        List<SysMenu> menus = sysMenuMapper.selectList(wrapper);

        List<String> menuIds = menus.stream()
                .filter(item -> item.getMenuType().equals("C"))
                .map(item -> item.getId())
                .collect(Collectors.toList());
        //返回菜单下的接口。
        if (BeanUtil.isNotEmpty(menuIds)) {
            QueryWrapper iWrapper = new QueryWrapper();
            iWrapper.in("PARENT_ID_", menuIds);
            iWrapper.eq("MENU_TYPE_", "I");
            List<SysMenu> iMenus = sysMenuMapper.selectList(iWrapper);
            menus.addAll(iMenus);
        }


        return menus;



    }

    public List<SysMenu> getAllMenusByAppId(String appId) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("APP_ID_", appId);
        wrapper.orderByAsc("SN_");
        //逻辑删除
        if (DbLogicDelete.getLogicDelete()) {
            wrapper.eq("DELETED_","0");
        }
        return sysMenuMapper.selectList(wrapper);
    }


    /**
     * 查询系统正常显示菜单（不含按钮）
     *
     * @return 菜单列表
     */
    public List<SysMenu> selectMenuNormalAll() {
        return sysMenuMapper.selectMenuNormalAll();
    }

    /**
     * 查询系统正常显示菜单（含按钮）
     *
     * @return 菜单列表
     */
    public List<SysMenu> selectMenuAndPermsNormalAll() {
        return sysMenuMapper.selectMenuAndPermsNormalAll();
    }

    public List<SysMenuDto> getMenusByIdsAndType(String menuIds, String menuType) throws InvocationTargetException, IllegalAccessException {
        List<SysMenuDto> dtos = new ArrayList<>();
        if (StringUtils.isEmpty(menuIds)) {
            return dtos;
        }
        List<SysMenu> list = sysMenuMapper.getMenusByIdsAndType(menuIds, menuType);
        for (SysMenu sysMenu : list) {
            SysMenuDto dto = new SysMenuDto();
            BeanUtil.copyNotNullProperties(dto, sysMenu);
            dtos.add(dto);
        }
        return dtos;
    }

    private void addAllMap(List<Map<String, Object>> result, List<SysMenu> filterList) {
        filterList.forEach(menu -> {
            Map appMap = Maps.newHashMap();
            appMap.put("id", menu.getId());
            appMap.put("name", menu.getName());
            appMap.put("parentId", menu.getParentId());
            appMap.put("appId", menu.getAppId());
            appMap.put("type", menu.getMenuType());
            appMap.put("boListKey", menu.getBoListKey());
            result.add(appMap);
        });
    }


    public boolean isExist(SysMenu ent) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("APP_ID_", ent.getAppId());
        wrapper.eq("menu_key_", ent.getMenuKey());
        if (StringUtils.isNotEmpty(ent.getId())) {
            wrapper.ne("MENU_ID_", ent.getId());
        }
        Integer rtn = sysMenuMapper.selectCount(wrapper);
        return rtn > 0;
    }

    /**
     * 批量删除菜单
     *
     * @param ids
     * @return
     */
    public JsonResult delSysMenus(String[] ids) {
        try {
            for (String menuId : ids) {
                delMenuCascade(menuId);
            }
            //删除授权的菜单
            List<String> list = Arrays.asList(ids);
            osUserClient.delMenuControl(list);
            return JsonResult.getSuccessResult("删除菜单成功!");
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.getFailResult("删除菜单失败!");
        }
    }

    /**
     * 级联删除菜单
     */
    public void delMenuCascade(String menuId) {
        List<SysMenu> childrenMenu = sysMenuMapper.getChildrenList(menuId);
        if (childrenMenu.size() > 0) {
            for (SysMenu menu : childrenMenu) {
                delMenuCascade(menu.getId());
            }
        }
        sysMenuMapper.deleteById(menuId);
        // 删除已安装应用授权菜单
        sysAppAuthMenuService.deleteAppAuthMenuByMenuId(menuId);
    }


    public List<SysMenu> getMenusByType(String menuType) {
        return sysMenuMapper.getMenusByType(menuType);
    }


    /**
     * 根据租户ID获取租户私有的菜单。
     *
     * @param tenantId
     * @return
     */
    public List<SysMenuDto> getMenusByTenantId(String tenantId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq(CommonConstant.TENANT_ID, tenantId);
        //逻辑删除
        if (DbLogicDelete.getLogicDelete()) {
            queryWrapper.eq("DELETED_","0");
        }
        List<SysMenu> list = sysMenuMapper.selectList(queryWrapper);
        List<SysMenuDto> dtos = new ArrayList<>();

        list.stream().forEach(item -> {
            SysMenuDto menuDto = new SysMenuDto();
            cn.hutool.core.bean.BeanUtil.copyProperties(item, menuDto);
            dtos.add(menuDto);
        });
        return dtos;
    }

    public List<SysMenu> getInterfaceByParent(String ParentId) {
        return sysMenuMapper.getInterfaceByParent(ParentId);
    }


    public List<SysMenu> getInterface() {
        return sysMenuMapper.getInterface();
    }


    @Transactional(rollbackFor = Exception.class)
    public boolean saveBatch(String appId, String parentId, List<SysMenu> sysMenus) {

        this.removeByAppIdParentId(appId, parentId);

        if (!CollectionUtils.isEmpty(sysMenus)) {
            sysMenus.forEach(e -> e.setId(IdGenerator.getIdStr()));
            saveBatch(sysMenus);
        }
        return true;
    }

    public void removeByAppIdParentId(String appId, String parentId) {
        Map m = Maps.newHashMap();
        m.put("APP_ID_", appId);
        m.put("PARENT_ID_", parentId);
        removeByMap(m);
    }

    /**
     * 根据应用编码获取应用
     *
     * @param appKey
     * @return
     */
    public JsonResult getMenusByAppKey(String appKey) {
        JsonResult jsonResult = JsonResult.getSuccessResult("获取成功!").setShow(false);
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("CLIENT_CODE_", appKey);
        Integer index = sysAppMapper.selectCount(queryWrapper);
        if (index == 0) {
            return JsonResult.getFailResult("未找到" + appKey + "对应的应用!");
        }
        //根据应用标识获取菜单
        SysApp sysApp = sysAppMapper.getMenusByAppKey(appKey);
        JSONObject jsonObject = new JSONObject();
        JSONObject app = new JSONObject();
        app.put("appId", sysApp.getAppId());
        app.put("appName", sysApp.getClientName());
        jsonObject.put("app", app);
        List<SysMenuDto> sysMenuDtos = new ArrayList<>();
        try {
            JPaasUser jPaasUser = SysUserUtil.getLoginUser();
            jsonObject.put("user", jPaasUser);
            //根租户管理员获取所有菜单
            if (jPaasUser.isRootAdmin()) {
                QueryWrapper wrapper = new QueryWrapper();
                wrapper.eq("APP_ID_", sysApp.getAppId());
                List<SysMenu> sysMenus = sysMenuMapper.selectList(wrapper);
                constructMenu(sysMenuDtos, sysApp, sysMenus);
                jsonObject.put("menus", sysMenuDtos);
            } else {
                //根据当前用户获取菜单
                sysMenuDtos = userClient.getMenusByAppId(sysApp.getAppId());
                jsonObject.put("menus", sysMenuDtos);
            }

        } catch (Exception ex) {
            jsonResult.setSuccess(false);
            jsonResult.setMessage(ExceptionUtil.getExceptionMessage(ex));
            log.error(ExceptionUtil.getExceptionMessage(ex));
            return jsonResult;
        }
        JSONObject allMenuButtons = getButtonsBySysApp( sysApp);
        jsonObject.put("allMenuButtons", allMenuButtons);
        jsonResult.setData(jsonObject);
        return jsonResult;
    }


    public List<SysMenu> listMenusByAppId(String appId) {
        return sysMenuMapper.listMenusByAppId(appId);
    }

    /**
     * 根据选择的menuId集合查询所有关联的资源。
     * <pre>
     *     除了ID代表的资源，还包括关联的接口。
     * </pre>
     *
     * @param menuIds
     * @return
     */
    public List<SysMenu> getAllByMenuIds(String[] menuIds) {
        QueryWrapper wrapper = new QueryWrapper();

        wrapper.in("MENU_ID_", menuIds);
        List<SysMenu> menus = this.sysMenuMapper.selectList(wrapper);
        //获取资源中为按钮的ID数据。
        List<String> buttonIds = menus.stream()
                .filter(menu -> menu.getMenuType().equals("F"))
                .map(SysMenu::getId).collect(Collectors.toList());

        if (BeanUtil.isNotEmpty(buttonIds)) {
            QueryWrapper iwrapper = new QueryWrapper();
            iwrapper.in("PARENT_ID_", buttonIds);
            iwrapper.eq("MENU_TYPE_", "I");
            List<SysMenu> iMenus = this.sysMenuMapper.selectList(iwrapper);

            if (BeanUtil.isNotEmpty(iMenus)) {
                menus.addAll(iMenus);
            }
        }
        return menus;
    }

    /**
     * 构建菜单。
     *
     * @param result
     * @param app
     * @param menus
     */
    private void constructMenu(List<SysMenuDto> result, SysApp app, List<SysMenu> menus) {
        SysMenuDto appMenu = constructMenu(app);
        result.add(appMenu);
        List<SysMenu> menuList = menus.stream().sorted(Comparator.comparing(menu -> {
            String sn = menu.getSn();
            if (sn == null) {
                return 1;
            }
            return Integer.parseInt(sn);
        })).collect(Collectors.toList());
        List<SysMenuDto> menuDtos=new ArrayList<>();
        for (SysMenu sysMenu : menuList) {
            if(app.getAppId().equals(sysMenu.getAppId())){
                //第一层菜单的父ID改为应用ID
                if("0".equals(sysMenu.getParentId())){
                    sysMenu.setParentId(app.getAppId());
                }
                SysMenuDto menuDto=new SysMenuDto();
                BeanUtil.copyProperties(menuDto,sysMenu);
                menuDtos.add(menuDto);
            }
        }
        result.addAll(menuDtos);
    }

    //将app构建为菜单
    private SysMenuDto constructMenu(SysApp app) {
        SysMenuDto appMenu = new SysMenuDto();
        appMenu.setId(app.getAppId());
        appMenu.setAppId(app.getAppId());
        appMenu.setName(app.getClientName());
        appMenu.setParentId("0");
        appMenu.setParams(app.getParams());
        appMenu.setSettingType(app.getHomeType());
        if ("custom".equals(app.getHomeType())) {
            appMenu.setComponent(app.getHomeUrl());
        } else if ("iframe".equals(app.getHomeType())) {
            appMenu.setComponent("UrlView");
            appMenu.setParams(app.getHomeUrl());
        } else {
            appMenu.setComponent("PageView");
        }
        appMenu.setShowType(app.getUrlType());
        appMenu.setMenuKey(app.getClientCode());
        appMenu.setIconPc(app.getIcon());
        appMenu.setMenuType("C");
        if (StringUtils.isNotEmpty(app.getLayout())) {
            appMenu.setLayout(app.getLayout());
        }
        if (StringUtils.isNotEmpty(app.getParentModule())) {
            appMenu.setParentModule(app.getParentModule());
        }
        appMenu.setAppType(app.getAppType() + "");
        appMenu.setAppPath(app.getPath());
        appMenu.setMenuNavType(app.getMenuNavType() + "");
        appMenu.setCategoryId(app.getCategoryId());
        appMenu.setBackColor(app.getBackColor());
        return appMenu;
    }

    public List<SysMenuDto> getAllMenus() {
        List<SysMenuDto> menuDtos=new ArrayList<>();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("MENU_TYPE_","C");
        //逻辑删除
        if (DbLogicDelete.getLogicDelete()) {
            queryWrapper.eq("DELETED_","0");
        }
        List<SysMenu> menus = sysMenuMapper.selectList(queryWrapper);
        QueryWrapper wrapper = new QueryWrapper<>();
        wrapper.eq("STATUS_","1");
        wrapper.ne("APP_TYPE_",2);
        String tenantId = ContextUtil.getCurrentTenantId();
        if(StringUtils.isEmpty(tenantId)){
            tenantId=IUser.ROOT_TENANT_ID;
        }
        wrapper.eq("TENANT_ID_",tenantId);
        //逻辑删除
        if (DbLogicDelete.getLogicDelete()) {
            wrapper.eq("DELETED_","0");
        }
        List<SysApp> sysApps = sysAppMapper.selectList(wrapper);
        List<SysApp> sysAppList= sysApps.stream().sorted(Comparator.comparing(item->{
            Integer sn= item.getSn();
            if(sn==null){
                sn=1;
            }
            return sn;
        })).collect(Collectors.toList());
        for (SysApp sysApp : sysAppList) {
            constructMenu(menuDtos, sysApp, menus);
        }
        return menuDtos;
    }

    //解析应用首页
    private void parseAuthSysApp(SysAppDto app){

        if(!MBoolean.YES.name().equals(app.getIsAuth())){
            return;
        }
        Map<String, Set<String>> profiles = ProfileContext.getCurrentProfile();
        JSONArray setting =JSONArray.parseArray(app.getAuthSetting());
        for(Object obj:setting){
            JSONObject json=(JSONObject)obj;
            //满足条件
            String setVal=json.containsKey("setting")?json.getJSONObject("setting").getString("value"):"";
            boolean flag= SysUtil.hasRights(setVal,profiles);
            if(flag){
                app.setLayout(json.getString("layout"));
                app.setParentModule(json.getString("parentModule"));
                app.setHomeType(json.getString("homeType"));
                app.setHomeUrl(json.getString("homeUrl"));
                app.setParams(json.getString("params"));
                app.setUrlType(json.getString("urlType"));
                break;
            }
        }

    }


    /**
     * 根据菜单IDs获取所有授权菜单信息
     *
     * @param menuIds
     * @return
     */
    public List<SysMenu> getGrantList(List<String> menuIds) {
        if(BeanUtil.isEmpty(menuIds)){
            return new ArrayList<>();
        }
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.in("MENU_ID_",menuIds);
        return sysMenuMapper.selectList(queryWrapper);
    }

    /**
     * 根据companyId集合获取公司级别菜单列表
     * @param companyId
     * @return
     */
    public List<SysMenuDto> getCompanyMenus(String companyId) {

        //获取所有分公司id
        List<String> subCompanyIdsList = orgClient.getAllSubCompanyIds(companyId);
        String subCompanyIds = "";
        if(BeanUtil.isNotEmpty(subCompanyIdsList)){
            subCompanyIds = String.join(",", subCompanyIdsList);
        }
        IUser user = ContextUtil.getCurrentUser();
        String tenantId = ContextUtil.getCurrentTenantId();
        String userId = user.getUserId();
        //根据管理员获取角色列表
        List<OsGradeRoleDto> group = osGradeRoleClient.getGroupByUserId(userId,tenantId);
        List<String> groupIdList = group.stream().map(OsGradeRoleDto::getGroupId).collect(Collectors.toList());
        groupIdList.add(companyId);
        String ids="";
        if(BeanUtil.isNotEmpty(groupIdList)){
            ids = String.join(",", groupIdList);
        }
        //获取管理员的角色菜单和授权给本公司的的菜单
        List<String> menuIdsList = osGroupMenuClient.getResourceByGroupIdList(ids);

        String menuIds = "";
        if(BeanUtil.isNotEmpty(menuIdsList)){
            menuIds = String.join(",", menuIdsList);
        }

        //这里包括授权的，本级建的和  下级共享的，加上配置给管理角色的菜单。
        List<SysMenu> list = sysMenuMapper.getAllGrantMenus(tenantId,companyId, subCompanyIds, menuIds);
        List<SysMenuDto> rtnList=new ArrayList<>();
        list.forEach(p->{
            SysMenuDto sysMenuDto =new SysMenuDto();
            BeanUtil.copyProperties(sysMenuDto,p);
            rtnList.add(sysMenuDto);
        });
        return rtnList;
    }


}


