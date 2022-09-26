package com.redxun.system.core.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.redxun.api.org.IGroupMenuService;
import com.redxun.cache.CacheUtil;
import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.IUser;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.dto.SysMenuDto;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.tool.constant.CommonConstant;
import com.redxun.common.utils.*;
import com.redxun.dto.user.OsGroupMenuDto;
import com.redxun.dto.user.OsInstDto;
import com.redxun.feign.org.OrgClient;
import com.redxun.feign.org.OsGroupMenuClient;
import com.redxun.log.annotation.AuditLog;
import com.redxun.log.model.Audit;
import com.redxun.log.model.LogContext;
import com.redxun.system.core.entity.SysApp;
import com.redxun.system.core.entity.SysMenu;
import com.redxun.system.core.service.SysAppServiceImpl;
import com.redxun.system.core.service.SysMenuServiceImpl;
import com.redxun.system.feign.OsUserClient;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 作者 yjy
 */
@Slf4j
@RestController
@RequestMapping("/system/core/sysMenu")
@ClassDefine(title = "系统菜单",alias = "sysMenuController",path = "/system/core/sysMenu",packages = "core",packageName = "系统管理")
@Api(tags = "系统菜单")
public class SysMenuController extends BaseController<SysMenu> {

    /**
     * 构建URL_GROUP映射
     */
    public static final String apiUrlGroup   = "apiMap_";

    @Autowired
    private SysMenuServiceImpl sysMenuServiceImpl;
    @Autowired
    private SysAppServiceImpl sysAppServiceImpl;
    @Autowired
    private IGroupMenuService groupMenuService;
    @Autowired
    private OsUserClient osUserClient;

    @Autowired
    private OrgClient orgClient;

    @Autowired
    private OsGroupMenuClient osGroupMenuClient;

    @Override
    public BaseService getBaseService() {
        return sysMenuServiceImpl;
    }

    /**
     * 是否权限分级
     */
    private static final String IS_GRADE="isGrade";
    @Override
    public String getComment() {
        return "系统菜单";
    }


    @MethodDefine(title = "根据菜单类型获取菜单信息", path = "/getMenusByType", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "菜单类型", varName = "menuType")})
    @ApiOperation("根据菜单类型获取菜单信息")
    @GetMapping("/getMenusByType")
    public List<SysMenu> getMenusByType(@ApiParam @RequestParam("menuType") String menuType){
        List<SysMenu> list = sysMenuServiceImpl.getMenusByType(menuType);
        return list;
    }


    @MethodDefine(title = "获取所有按钮菜单列表", path = "/getAllButtonsByMenuType", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "菜单类型", varName = "menuType")})
    @ApiOperation("获取所有按钮菜单列表")
    @GetMapping("/getAllButtonsByMenuType")
    public JSONObject getAllButtonsByMenuType(@ApiParam @RequestParam String menuType){
        return sysMenuServiceImpl.getAllButtonsByMenuType(menuType);
    }

    @MethodDefine(title = "根据菜单ID获取其下级菜单", path = "/getMenusByMenuId", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "菜单ID", varName = "menuId")})
    @ApiOperation("根据菜单ID获取其下级菜单")
    @GetMapping("/getMenusByMenuId")
    public List<SysMenu> getMenusByMenuId(@ApiParam @RequestParam String menuId){
        List<SysMenu> list = sysMenuServiceImpl.getByParentId(menuId);
        return list;
    }

    @MethodDefine(title = "根据应用ID获取所有菜单信息", path = "/getMenusByAppId", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "应用ID", varName = "appId")})
    @ApiOperation("根据应用ID获取所有菜单信息")
    @GetMapping("/getMenusByAppId")
    public List<SysMenu> getMenusByAppId(@ApiParam @RequestParam(value = "appId") String appId){
        List<SysMenu> list = sysMenuServiceImpl.getAllMenusByAppId(appId);
        return list;
    }

    @MethodDefine(title = "根据菜单ID集合与菜单类型获取菜单信息", path = "/getMenusByIdsAndTypes", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "菜单ID集合", varName = "menuIds"),@ParamDefine(title = "菜单类型", varName = "menuType")})
    @ApiOperation("根据菜单ID集合与菜单类型获取菜单信息")
    @PostMapping("/getMenusByIdsAndTypes")
    public List<SysMenuDto> getMenusByIdsAndTypes(@ApiParam @RequestParam(value = "menuIds") String menuIds,
                                           @ApiParam @RequestParam(value = "menuType") String menuType) throws Exception {
        List<SysMenuDto> list = sysMenuServiceImpl.getMenusByIdsAndType(menuIds, menuType);
        return list;
    }



    @MethodDefine(title = "查询系统正常显示菜单(不含按钮)", path = "/selectMenuNormalAll", method = HttpMethodConstants.GET)
    @ApiOperation("查询系统正常显示菜单(不含按钮)")
    @GetMapping("/selectMenuNormalAll")
    public List<SysMenu> selectMenuNormalAll(){
        List<SysMenu> list = sysMenuServiceImpl.selectMenuNormalAll();
        return list;
    }

    @MethodDefine(title = "查询系统正常显示菜单(含按钮)", path = "/selectMenuAndPermsNormalAll", method = HttpMethodConstants.GET)
    @ApiOperation("查询系统正常显示菜单(含按钮)")
    @GetMapping("/selectMenuAndPermsNormalAll")
    public List<SysMenu> selectMenuAndPermsNormalAll(){
        List<SysMenu> list = sysMenuServiceImpl.selectMenuAndPermsNormalAll();
        return list;
    }

    /**
     * 应用的ID为 appId, parentId 为 0。
     * @param sysApp
     * @return
     */
    private JSONObject getMenuByApp(SysApp sysApp){
        JSONObject topMenu=new JSONObject();
        topMenu.put("id", sysApp.getAppId());
        topMenu.put("appId", sysApp.getAppId());
        topMenu.put("name", sysApp.getClientName());
        topMenu.put("parentId", "0");	//根目录值默认为0
        topMenu.put("menuType", "app");
        topMenu.put("boListKey",null);
        return  topMenu;
    }

    /**
     * 当菜单的父ID为0的时候，那么父ID为appId。
     * @param menu
     * @param appId
     * @return
     */
    private JSONObject getBySysMenu(SysMenu menu,String appId){
        JSONObject subMenu=new JSONObject();
        subMenu.put("id", menu.getId());
        subMenu.put("appId", appId);
        subMenu.put("name", menu.getName());
        subMenu.put("parentId", menu.getParentId());
        if("0".equals(menu.getParentId())){
            subMenu.put("parentId", appId);
        }
        subMenu.put("menuType", menu.getMenuType());
        subMenu.put("boListKey",menu.getBoListKey());

        return subMenu;
    }

    /**
     * 根据当前人的身份获取权限。
     * <pre>
     *     1.根据当前人的身份去JPAAS-USER获取当前人被授权的菜单数据。
     *     2.根据这些数据返回菜单的数据结构。
     * </pre>
     * @return
     */
    @MethodDefine(title = "根据当前人的获取分级管理有权限的资源", path = "/getGradeMenu", method = HttpMethodConstants.GET)
    @ApiOperation("根据当前人的获取分级管理有权限的资源")
    @GetMapping("/getGradeMenu")
    public List<JSONObject> getGradeMenu(){
        IUser user= ContextUtil.getCurrentUser();
        JsonResult<List<OsGroupMenuDto>> result= groupMenuService.getResourceByGrade(user.getUserId(),user.getTenantId());
        List<OsGroupMenuDto> menuDtos=result.getData();
        List<String> appIds= menuDtos.stream().map(OsGroupMenuDto::getAppId).distinct().collect(Collectors.toList());
        List<String> menuIds= menuDtos.stream().map(OsGroupMenuDto::getMenuId).collect(Collectors.toList());

        List<SysApp> listApp= sysAppServiceImpl.getByIds(appIds);
        List<SysMenu> menuList = sysMenuServiceImpl.getByIds(menuIds);

        List<SysApp> apps = listApp.stream().sorted(Comparator.comparing(SysApp::getSn)).collect(Collectors.toList());
        Map<String, List<SysMenu>> appMenuMap = menuList.stream()
                .collect(Collectors.groupingBy(SysMenu::getAppId, Collectors.toList()));

        List<JSONObject> list=new ArrayList<>();

        for (SysApp app : apps) {
            JSONObject topMenu = getMenuByApp(app);

            List<SysMenu> tmpList = appMenuMap.get(app.getAppId());
            if(BeanUtil.isNotEmpty(tmpList)){
                list.add(topMenu);
                List<SysMenu> subMenuList = tmpList.stream()
                        .sorted(Comparator.comparing(menu->{
                            String sn=menu.getSn();
                            if(sn==null){
                                return 1;
                            }
                            return Integer.parseInt(sn);
                        })).collect(Collectors.toList());
                subMenuList.forEach(menu -> {
                    JSONObject subMenu = getBySysMenu(menu, app.getAppId());
                    list.add(subMenu);
                });
            }

        }
        return list;
    }



    /**
     * 获取授权菜单。
     * <pre>
     *     1.根租户ID
     *      获取本租户的资源和共享的资源。
     *     2. 非根租户
     *      获根租户的资源和当前租户的应用资源。
     * </pre>
     * @param appId
     * @return
     */
    @MethodDefine(title = "查询应用及其所属的菜单信息(包括应用及菜单和权限)", path = "/selectMenus", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "应用ID", varName = "appId"),@ParamDefine(title = "菜单获取类型", varName = "type")})
    @ApiOperation("查询应用及其所属的菜单信息(包括应用及菜单和权限)")
    @GetMapping("/selectMenus")
    public List<JSONObject> selectMenus(@ApiParam @RequestParam(required = false,value = "appId") String appId,
                                        @ApiParam @RequestParam(required = false ,value = "type") String type,
                                        @ApiParam @RequestParam(required = false) String grant){
        //逻辑删除
        String deleted=null;
        if (DbLogicDelete.getLogicDelete()) {
            deleted="0";
        }
        List<JSONObject> list=new ArrayList<>();
        if(StringUtils.isEmpty(appId)){
            String tenantId=getCurrentTenantId();
            if("group".equals(type)){
                //不启用组织分级管理  || 启用组织分级管理并且是超管的情况
                OsInstDto osInstDto=osUserClient.getByInstId(tenantId);
                List<SysMenuDto> sysMenuDtos = osUserClient.findSysMenuDtoByInstTypeId(osInstDto.getInstType());
                list.addAll(parseSysMenuDto(sysMenuDtos));

            }else {
                //组织机构菜单(全部显示)
                List<SysApp> appList = sysAppServiceImpl.getAppByTenant(tenantId,deleted);
                for (SysApp sysApp : appList) {
                    JSONObject topMenu = getMenuByApp(sysApp);
                    list.add(topMenu);
                    List<SysMenu> subMenus = sysMenuServiceImpl.getMenusByAppId(sysApp.getAppId());
                    for (SysMenu sysMenu : subMenus) {
                        JSONObject menu = getBySysMenu(sysMenu, sysApp.getAppId());
                        list.add(menu);
                    }
                }
            }
        }else{
            SysApp sysApp=sysAppServiceImpl.get(appId);
            JSONObject topMenu = getMenuByApp(sysApp);
            list.add(topMenu);
            List<SysMenu> menus = sysMenuServiceImpl.getMenusByAppId(appId);
            for(SysMenu menu : menus){
                JSONObject subMenu=getBySysMenu(menu,appId);
                list.add(subMenu);
            }
        }
        return list;
    }

    /**
     * 解析菜单结构
     * @param sysMenuDtos
     * @return
     */
    private List<JSONObject> parseSysMenuDto(List<SysMenuDto> sysMenuDtos){
        List<JSONObject> list= new ArrayList<>();
        List<String> appIdList=sysMenuDtos.stream().map(item -> item.getAppId()).distinct().collect(Collectors.toList());
        List<SysMenuDto> menuList=sysMenuDtos.stream().filter(item->!item.getId().equals(item.getAppId())).collect(Collectors.toList());
        List<String> ids = new ArrayList<>();
        for (String s : appIdList) {
            ids.add("'"+s+"'");
        }
        appIdList=ids;
        List<SysApp> appList = sysAppServiceImpl.getAppsByIds(StringUtils.join(appIdList,","));
        for(SysApp app:appList){
            list.add(getMenuByApp(app));
        }
        for(SysMenuDto menu:menuList){
            SysMenu sysMenu=new SysMenu();
            sysMenu.setId(menu.getId());
            sysMenu.setName(menu.getName());
            sysMenu.setParentId(menu.getParentId());
            sysMenu.setMenuType(menu.getMenuType());
            sysMenu.setBoListKey(menu.getBoListKey());
            list.add(getBySysMenu(sysMenu,menu.getAppId()));
        }
        return list;
    }

    /**
     * 迁移菜单
     * @return
     */
    @MethodDefine(title = "迁移菜单", path = "/toMoveMenu", method = HttpMethodConstants.GET)
    @ApiOperation("迁移菜单")
    @AuditLog(operation = "迁移菜单")
    @PostMapping("/toMoveMenu")
    public JsonResult toMoveMenu(){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request=attributes.getRequest();
        String menuIds = RequestUtil.getString(request,"menuId");

        String targetId=RequestUtil.getString(request,"targetId");
        String menuType=RequestUtil.getString(request,"menuType");
        boolean ckSubMenus=RequestUtil.getBoolean(request,"ckSubMenus");

        String type="app".equals( menuType)?"应用":"菜单";

        String detail="将菜单:"+ menuIds +"转移到:" +type +",目标ID下:" + targetId;

        LogContext.put(Audit.DETAIL,detail);

        String[] menuId = menuIds.split(",");
        for(int i=0;i<menuId.length;i++) {
            //获得迁移的菜单
            SysMenu menu = sysMenuServiceImpl.get(menuId[i]);

            //移动至系统下
            String appType = "app";
            if (appType.equals(menuType)) {
                SysApp subsystem = sysAppServiceImpl.get(targetId);
                //仅移动当前菜单下的菜单至子系统下
                if (ckSubMenus) {
                    List<SysMenu> subMenus = sysMenuServiceImpl.getByParentId(menuId[0]);
                    for (SysMenu m : subMenus) {
                        m.setAppId(subsystem.getAppId());
                        m.setParentId("0");
                        m.setPath("0." + m.getId() + ".");
                        sysMenuServiceImpl.update(m);
                        //递归其下所有子菜单，并更新其路径
                        cascadeUpdateSubMenu(m);
                    }
                } else {//把该菜单移到子系统下
                    menu.setAppId(subsystem.getAppId());
                    menu.setParentId("0");
                    menu.setPath("0." + menu.getId() + ".");
                    sysMenuServiceImpl.update(menu);
                    //递归其下所有子菜单，并更新其路径
                    cascadeUpdateSubMenu(menu);
                }
            } else {//迁移到目标菜单下
                SysMenu parentMenu = sysMenuServiceImpl.get(targetId);
                //仅把其下的菜单进行迁移
                if (ckSubMenus) {
                    List<SysMenu> subMenus = sysMenuServiceImpl.getByParentId(menuId[0]);
                    for (SysMenu m : subMenus) {
                        m.setParentId(parentMenu.getId());
                        m.setPath(parentMenu.getPath() + m.getId() + ".");
                        sysMenuServiceImpl.update(m);
                        //递归其下所有子菜单，并更新其路径
                        cascadeUpdateSubMenu(m);
                    }
                } else {//把当前菜单下其下的菜单一起迁移
                    menu.setParentId(parentMenu.getId());
                    menu.setPath(parentMenu.getPath() + menu.getId() + ".");
                    menu.setAppId(parentMenu.getAppId());
                    sysMenuServiceImpl.update(menu);
                    //递归其下所有子菜单，并更新其路径
                    cascadeUpdateSubMenu(menu);
                }
            }
            SysMenuDto menuDto= new SysMenuDto();
            BeanUtil.copyProperties(menuDto,menu);
            osUserClient.updateMenuControl(menuDto);
        }

        removeCache();

        return new JsonResult(true,"修改成功");
    }

    private void cascadeUpdateSubMenu(SysMenu parentMenu){
        List<SysMenu> subMenus=sysMenuServiceImpl.getByParentId(parentMenu.getId());
        for(SysMenu m:subMenus){
            m.setParentId(parentMenu.getId());
            m.setPath(parentMenu.getPath()+m.getId()+".");
            m.setAppId(parentMenu.getAppId());
            sysMenuServiceImpl.update(m);
            //递归其下所有子菜单，并更新其路径
            cascadeUpdateSubMenu(m);
        }
    }

    /**
     * 同步某个功能下的按钮至菜单资源表中以进行权限的控制
     * @param json
     * @return
     * @throws Exception
     */
    @MethodDefine(title = "同步某个功能下的按钮至菜单资源表中以进行权限的控制", path = "/syncMenuBtns", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "菜单数据集合", varName = "json")})
    @ApiOperation("同步某个功能下的按钮至菜单资源表中以进行权限的控制")
    @AuditLog(operation = "同步按钮到某个菜单下")
    @PostMapping("/syncMenuBtns")
    public JsonResult syncMenuBtns(@RequestBody JSONObject json) throws Exception{
        String menuId=json.getString("menuId");
        SysMenu parentMenu= sysMenuServiceImpl.get(menuId);
        if(parentMenu==null){
            SysApp sysApp= sysAppServiceImpl.get(menuId);
            if(sysApp==null){
                return JsonResult.getFailResult("请选择菜单");
            }
            parentMenu=new SysMenu();
            parentMenu.setId(menuId);
            parentMenu.setAppId(menuId);
            parentMenu.setName(sysApp.getClientName());
        }
        JSONArray btnsArr=json.getJSONArray("btns");
        String str=syncBtns(parentMenu,btnsArr);
        if(StringUtils.isNotEmpty(str)) {
            return JsonResult.error(500,"同步菜单失败！",str);
        }
        String detail="成功同步菜单【"+parentMenu.getName()+"】下的按钮资源";
        LogContext.put(Audit.DETAIL,detail);

        removeCache();

        return JsonResult.getSuccessResult(detail);
    }

    private String syncBtns(SysMenu parentMenu,JSONArray btnsArr){
        StringBuffer strBu=new StringBuffer();
        for(int i=0;i<btnsArr.size();i++){
            JSONObject jsonObj=btnsArr.getJSONObject(i);
            String btnLabel=jsonObj.getString("btnLabel");
            String btnName=jsonObj.getString("btnName");
            String url=jsonObj.getString("url");
            if(StringUtils.isNotEmpty(url)){
                int index=url.indexOf("?");
                if(index!=-1){
                    url=url.substring(0,index);
                }
            }
            SysMenu btnMenu=null;
            if(StringUtils.isNotEmpty(btnName)){
                btnMenu = sysMenuServiceImpl.getByMenuKey(btnName);
                if(btnMenu==null){
                    btnMenu=new SysMenu();
                    btnMenu.setName(btnLabel);
                    btnMenu.setMenuKey(btnName);
                    btnMenu.setAppId(parentMenu.getAppId());
                    btnMenu.setMenuType("F");
                    btnMenu.setSn("0");
                    btnMenu.setParentId(parentMenu.getId());
                    sysMenuServiceImpl.insert(btnMenu);
                }else{
                    strBu.append("【"+btnName+"】菜单Key已存在!\n");
                }
            }

            JSONArray childs=jsonObj.getJSONArray("children");

            if(childs==null) {
                continue;
            }

            syncBtns(btnMenu,childs);
        }

        removeCache();
        return strBu.toString();
    }

    /**
     * 重写父类的菜单
     * @param entity
     * @return
     * @throws Exception
     */
    @ApiOperation(value="保存菜单资源", notes="根据提交的业务实体JSON保存实体数据")
    @PostMapping("/save")
    @Override
    public JsonResult save(@ApiParam @RequestBody SysMenu entity,BindingResult bindingResult) throws Exception{
        String pkId=entity.getPkId();
        JsonResult result=null;
        String str="";
        try{
            beforeSave(entity);
            boolean isCotain= sysMenuServiceImpl.isExist(entity);
            if(isCotain){
                SysApp sysApp = sysAppServiceImpl.get(entity.getAppId());
                str= "应用【"+sysApp.getClientName()+"】下已经存在key【"+entity.getMenuKey()+"】!";
                LogContext.addError(str);
                result=JsonResult.getFailResult(str);
                result.setData(str);
                return result;
            }

            result = sysMenuServiceImpl.saveOrUpdateSysMenu(entity);

            //清除接口授权缓存
            removeCache();

        } catch(Exception ex){
            String errMsg= ExceptionUtil.getExceptionMessage(ex);

            LogContext.addError(errMsg);

            if(BeanUtil.isEmpty(pkId)){
                str="添加" + getComment() +"失败!";
            }else{
                str="更新" + getComment() +"失败!";
            }
            result=JsonResult.getFailResult(str);
            result.setData(errMsg);
        }
        return result;
    }

    /**
     * 批量保存授权资源
     * @param appId
     * @param parentId
     * @param sysMenus
     * @return
     * @throws Exception
     */
    @ApiOperation(value="批量保存授权资源", notes="批量保存授权资源")
    @PostMapping("/saveBatch")
    public JsonResult saveBatch(@ApiParam @RequestParam(value = "appId") String appId, @RequestParam(value = "parentId") String parentId,
                                @RequestBody List<SysMenu> sysMenus,BindingResult bindingResult) throws Exception{

        sysMenuServiceImpl.saveBatch(appId, parentId, sysMenus);

        //清除接口授权缓存
        removeCache();

        return JsonResult.getSuccessResult("保存成功");
    }

    @MethodDefine(title = "批量删除菜单", path = "/delSysMenus", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "菜单ID集合", varName = "ids")})
    @ApiOperation(value="批量删除菜单")
    @AuditLog(operation = "批量删除菜单")
    @PostMapping("delSysMenus")
    public JsonResult delSysMenus(@RequestParam(value = "ids") String ids) throws Exception{
        String[] aryId = ids.split(",");
        String detail="批量删除菜单:" + ids;
        LogContext.put(Audit.DETAIL,detail);


        //清除接口授权缓存
        removeCache();

        return sysMenuServiceImpl.delSysMenus(aryId);
    }


    @MethodDefine(title = "根据租户ID查询所有的菜单资源", path = "/getMenusByTenantId", method = HttpMethodConstants.GET)
    @ApiOperation("根据租户ID查询所有的菜单资源")
    @GetMapping("/getMenusByTenantId")
    public List<SysMenuDto> getMenusByTenantId(
            @RequestParam(name = "tenantId") String tenantId){
        return sysMenuServiceImpl.getMenusByTenantId(tenantId);
    }


    @MethodDefine(title = "根据菜单ID集合与菜单类型获取菜单信息", path = "/getMenusByIds", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "菜单ID集合", varName = "menuIds"),
                    @ParamDefine(title = "租户ID", varName = "tenantId")})
    @ApiOperation("根据菜单ID集合与菜单类型获取菜单信息")
    @PostMapping("/getMenuMenuIds")
    public List<SysMenuDto> getMenuMenuIds(@ApiParam @RequestParam("menuIds") String menuIds)  {
        String[] aryMenus=menuIds.split(",");
        List menuList= Arrays.asList(aryMenus);
        List<SysMenu> list = sysMenuServiceImpl.getByIds(menuList);
        List<SysMenuDto> dtos=new ArrayList<>();

        for(SysMenu menu:list){
            SysMenuDto menuDto= new SysMenuDto();
            cn.hutool.core.bean.BeanUtil.copyProperties(menu,menuDto);
            dtos.add(menuDto);
        }

        return dtos;
    }


    @MethodDefine(title = "根据菜单ID集合获取菜单对象和关联的接口对象", path = "/getMenusByIds", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "菜单ID集合", varName = "menuIds"),
                    @ParamDefine(title = "租户ID", varName = "tenantId")})
    @ApiOperation("根据菜单ID集合获取菜单对象和关联的接口对象")
    @PostMapping("/getAllByMenuIds")
    public List<SysMenuDto> getAllByMenuIds(@ApiParam @RequestParam("menuIds") String menuIds)  {

        String[] aryMenuId=menuIds.split(",");

        List<SysMenu> list = sysMenuServiceImpl.getAllByMenuIds(aryMenuId);

        List<SysMenuDto> dtos=new ArrayList<>();

        for(SysMenu menu:list){
            SysMenuDto menuDto= new SysMenuDto();
            cn.hutool.core.bean.BeanUtil.copyProperties(menu,menuDto);
            dtos.add(menuDto);
        }
        return dtos;
    }


    @MethodDefine(title = "根据parentID获取接口", path = "/getInterfaceByParent", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "ParentId", varName = "parentId")})
    @ApiOperation("根据parentID获取接口")
    @GetMapping("/getInterfaceByParent")
    public List<SysMenu> getInterfaceByParent(@RequestParam (value="parentId") String parentId){
        List<SysMenu> list =  sysMenuServiceImpl.getInterfaceByParent(parentId);
        return list;
    }


    /**
     * 获取所有的授权接口
     * <pre>
     *     1.获取所有的授权接口。
     *     2.根据菜单ID获取菜单ID和组的映射关系。
     *     3.遍历所有接口，建立 接口(接口地址 +":" +方法) 和组的映射关系。
     * </pre>
     * @return
     */
    @MethodDefine(title = "获取所有的授权接口（接口授权合并到菜单授权）", path = "/getUrlGroupIdMap", method = HttpMethodConstants.GET)
    @ApiOperation(value = "获取所有的授权接口（接口授权合并到菜单授权）")
    @GetMapping("/getUrlGroupIdMap")
    public Map<String, Set<String>> getUrlGroupIdMap(){
        List<SysMenu> list = sysMenuServiceImpl.getInterface();
        if(BeanUtil.isEmpty(list)){
            return Collections.emptyMap();
        }

        String menuIds = String.join(",", list.stream().map(item -> item.getId()).collect(Collectors.toList()) );
        //获取菜单ID和组的映射关系。
        Map<String, Set<String>> menuGroupMap = osUserClient.findGroupMenuDtoByMenuIds(menuIds);

        Map<String,Set<String>> map=new HashMap<>();
        //建立 接口(接口地址 +":" +方法) 和组的映射关系。
        for(SysMenu menu: list){
            if(StringUtils.isEmpty(menu.getUrl())){
                continue;
            }
            String key=menu.getUrl() +":" + menu.getMethod();
            map.put(key,new HashSet<>());
            if(menuGroupMap.containsKey(menu.getId())){
                map.put(key,menuGroupMap.get(menu.getId()));
            }
        }
        return map;
    }

    @MethodDefine(title = "根据应用编码获取所有菜单信息", path = "/getMenusByAppKey", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "应用编码", varName = "appKey")})
    @ApiOperation("根据应用编码获取所有菜单信息")
    @GetMapping("/getMenusByAppKey")
    public JsonResult getMenusByAppKey(@ApiParam @RequestParam(value = "appKey") String appKey){
        return sysMenuServiceImpl.getMenusByAppKey(appKey);
    }

    @Override
    protected JsonResult beforeSave(SysMenu ent) {
        //清除接口授权缓存
        removeCache();
        //更新授权的菜单
        SysMenuDto menuDto= new SysMenuDto();
        BeanUtil.copyProperties(menuDto,ent);
        osUserClient.updateMenuControl(menuDto);
        return super.beforeSave(ent);
    }

    @Override
    protected JsonResult beforeRemove(List<String> list) {
        //清除接口授权缓存
        removeCache();
        //删除授权的菜单
        osUserClient.delMenuControl(list);
        return super.beforeRemove(list);
    }

    private void removeCache(){
        CacheUtil.remove(CommonConstant.API_REGION, CommonConstant.API_KEY);
    }

    @MethodDefine(title = "获取所有的菜单资源", path = "/getAllMenus", method = HttpMethodConstants.GET)
    @ApiOperation("获取所有的菜单资源")
    @GetMapping("/getAllMenus")
    public List<SysMenuDto> getAllMenus(){
        return sysMenuServiceImpl.getAllMenus();
    }

    @MethodDefine(title = "根据应用ID获取所有授权菜单信息", path = "/getGrantList", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "应用ID", varName = "appId")})
    @ApiOperation("根据应用ID获取所有授权菜单信息")
    @GetMapping("/getGrantList")
    public List<SysMenu> getGrantList(@ApiParam @RequestParam String appId){

        //组织分级管理开关配置
        Boolean supportGrade = SysPropertiesUtil.getSupportGradeConfig();
        IUser user = ContextUtil.getCurrentUser();
        //启用组织分级管理 && 非超管
        if(supportGrade != null && supportGrade.booleanValue() && !user.isAdmin()){
            List<String> menuIdsList = osGroupMenuClient.getResourceByAppIdAndGroupId(appId, user.getCompanyId());
            return sysMenuServiceImpl.getGrantList(menuIdsList);

        }

        return null;
    }



    @MethodDefine(title = "根据companyId集合获取公司级别菜单列表", path = "/getCompanyMenus", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "公司id", varName = "companyId")})
    @ApiOperation("根据companyId集合获取公司级别菜单列表")
    @GetMapping("/getCompanyMenus")
    public List<SysMenuDto> getCompanyMenus(@ApiParam @RequestParam("companyId") String companyId){
        return sysMenuServiceImpl.getCompanyMenus(companyId);
    }


}
