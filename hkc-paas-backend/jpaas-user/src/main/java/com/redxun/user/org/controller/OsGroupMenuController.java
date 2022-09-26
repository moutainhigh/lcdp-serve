package com.redxun.user.org.controller;

import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.IUser;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.dto.SysMenuDto;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.tool.Result;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ContextUtil;
import com.redxun.dto.user.OsGroupMenuDto;
import com.redxun.feign.sys.SystemClient;
import com.redxun.log.annotation.AuditLog;
import com.redxun.log.model.Audit;
import com.redxun.log.model.LogContext;
import com.redxun.user.org.entity.OsGroup;
import com.redxun.user.org.entity.OsGroupMenu;
import com.redxun.user.org.service.OsGroupMenuServiceImpl;
import com.redxun.user.org.service.OsGroupServiceImpl;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;


/**
 * 用户组下的授权菜单 提供者
 *
 * @author yjy
 * @date 2019-11-14
 */
@Slf4j
@RestController
@RequestMapping("/user/org/osGroupMenu")
@ClassDefine(title = "用户组下的授权菜单",alias = "osGroupMenuController",path = "/user/org/osGroupMenu",packages = "org",packageName = "用户模块")
@Api(tags = "用户组下的授权菜单")
public class OsGroupMenuController extends BaseController<OsGroupMenu> {
    @Autowired
    OsGroupMenuServiceImpl osGroupMenuServiceImpl;
    @Autowired
    OsGroupServiceImpl osGroupService;
    @Autowired
    SystemClient systemClient;

    @Override
    public BaseService getBaseService() {
        return osGroupMenuServiceImpl;
    }

    @Override
    public String getComment() {
        return "系统用户组";
    }

    @MethodDefine(title = "根据组Id获取授权菜单列表", path = "/getGrantMenusByGroupId", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "用户ID", varName = "userId")})
    @ApiOperation("根据组Id获取授权菜单列表")
    @GetMapping("/getGrantMenusByGroupId")
    public List<OsGroupMenu>  getGrantMenusByGroupId(@ApiParam @RequestParam(value = "groupId") String groupId) {
        List<OsGroupMenu> list = osGroupMenuServiceImpl.getGrantMenusByGroupId(groupId);
        return list;
    }

    /**
     * 新增or更新
     */
    @MethodDefine(title = "保存用户组的菜单授权", path = "/saveOrUpdate", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "权限数据",varName = "osGroupMenus")})
    @ApiOperation(value = "保存用户组的菜单授权")
    @AuditLog(operation = "保存用户组的菜单授权")
    @PostMapping("/saveOrUpdate")
    public JsonResult saveOrUpdate(@RequestParam(value = "groupId") String groupId,
                                   @RequestParam(value = "appId", required = false) String appId,
                                   @RequestBody List<OsGroupMenu> menuList)  {

        osGroupMenuServiceImpl.removeByGroupId(groupId, appId);
        OsGroup osGroup=osGroupService.get(groupId);

        IUser user = ContextUtil.getCurrentUser();


        String detail="对用户组:" + osGroup.getName() +"进行菜单授权!";
        LogContext.put(Audit.DETAIL,detail);
        LogContext.put(Audit.PK,groupId);

        if(BeanUtil.isEmpty(menuList)){
            return JsonResult.getSuccessResult("授权用户组成功!");
        }

        List<OsGroupMenu> appList=menuList.stream().filter(item->item.getMenuId() .equals(item.getAppId()))
                .map(item->item.setParentId("0"))
                .collect(Collectors.toList());

        List<String>  menuIds=menuList.stream()
                .filter(item->!item.getMenuId().equals(item.getAppId()))
                .map(item->item.getMenuId())
                .distinct().collect(Collectors.toList());

        List<OsGroupMenu> groupMenus=new ArrayList<>();

        if(BeanUtil.isNotEmpty(appList)){
            groupMenus.addAll(appList);
        }

        if(BeanUtil.isNotEmpty(menuIds)){
            MultiValueMap valueMap=new LinkedMultiValueMap();
            valueMap.add("menuIds",StringUtils.join(menuIds,",") );

            List<SysMenuDto> sysMenuDtos = systemClient.getAllByMenuIds(valueMap);
            for(SysMenuDto dto:sysMenuDtos){
                OsGroupMenu groupMenu=new OsGroupMenu();
                cn.hutool.core.bean.BeanUtil.copyProperties(dto,groupMenu);
                groupMenu.setMenuId(dto.getId());
                String parentId="0".equals(groupMenu.getParentId())?groupMenu.getAppId():groupMenu.getParentId();
                groupMenu.setParentId(parentId);
                groupMenus.add(groupMenu);
            }
        }

        for(OsGroupMenu groupMenu:groupMenus){
            groupMenu.setId(IdGenerator.getIdStr());
            groupMenu.setGroupId(groupId);
            groupMenu.setCreateBy(user.getUserId());
            groupMenu.setTenantId(user.getTenantId());
        }

        osGroupMenuServiceImpl.saveOrUpdate(groupMenus);
        //清除接口授权缓存
        osGroupMenuServiceImpl.removeApiCache();

        return JsonResult.getSuccessResult("保存成功");
    }


    /**
     * 根据组ID查询菜单列表
     * @param groupId
     * @return
     */
    @MethodDefine(title = "根据组ID查询菜单列表", path = "/findMenuListByGroupId", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "组ID",varName = "groupId")})
    @ApiOperation(value = "根据组ID查询菜单列表")
    @GetMapping("/findMenuListByGroupId")
    public Result findMenuListByGroupId(@RequestParam(value = "groupId") String groupId) {
        //采用数据库
        List<String> model = osGroupMenuServiceImpl.findMenuListByGroupId(groupId);
        //采用Redis缓存
        return Result.succeed(model, "查询成功");
    }


    @MethodDefine(title = "根据用户获取分级管理的资源", path = "/getByUser", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "用户ID",varName = "userId"),
                    @ParamDefine(title = "租户ID",varName = "tenantId")})
    @ApiOperation(value = "根据用户获取分级管理的资源")
    @GetMapping("/getResourceByGrade")
    public JsonResult<List<OsGroupMenuDto>> getResourceByGrade(@RequestParam("userId") String userId, @RequestParam("tenantId") String tenantId){
        JsonResult result=JsonResult.Success();
        List<OsGroupMenu> list=osGroupMenuServiceImpl.getResourceByGrade(userId,tenantId);
        List<OsGroupMenuDto> newList=new ArrayList<>();
        list.forEach(item->{
            OsGroupMenuDto dto=new OsGroupMenuDto();
            dto.setAppId(item.getAppId());
            dto.setGroupId(item.getGroupId());
            dto.setMenuId(item.getMenuId());
            newList.add(dto);
        });
        result.setData(newList);
        return result;
    }


    @MethodDefine(title = "根据菜单ID获取菜单和组的映射关系", path = "/findGroupMenuDtoByMenuIds", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "菜单ids", varName = "menuIds")})
    @ApiOperation("根据菜单ID获取菜单和组的映射关系")
    @GetMapping("/findGroupMenuDtoByMenuIds")
    public Map<String, Set<String>>  findGroupMenuDtoByMenuIds(@ApiParam @RequestParam(value = "menuIds") String menuIds) {
        if(StringUtils.isEmpty(menuIds)){
            return Collections.EMPTY_MAP;
        }
        List<OsGroupMenu> list = osGroupMenuServiceImpl.getGrantMenusByMenuIds(menuIds);
        Map<String, Set<String>> menuGroupMap=new HashMap<>();
        for(OsGroupMenu menu:list){
            String menuId=menu.getMenuId();
            if(menuGroupMap.containsKey(menuId)){
                menuGroupMap.get(menuId).add(menu.getGroupId());
            }
            else{
                Set<String> set=new HashSet<>();
                set.add(menu.getGroupId());
                menuGroupMap.put(menuId,set);
            }
        }
        return menuGroupMap;
    }


    @MethodDefine(title = "根据应用ID以及用户组ID获取授权的菜单id列表数据", path = "/getResourceByAppIdAndGroupId", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "应用ID",varName = "appId"),
                    @ParamDefine(title = "用户组id",varName = "groupId")})
    @ApiOperation(value = "根据应用ID以及用户组ID获取授权的菜单id列表数据")
    @GetMapping("/getResourceByAppIdAndGroupId")
    public List<String> getResourceByAppIdAndGroupId(@RequestParam(value = "appId") String appId,
                                                     @RequestParam("groupId") String groupId){
        return osGroupMenuServiceImpl.getResourceByAppIdAndGroupId(appId, groupId);
    }

    @MethodDefine(title = "根据用户组ID获取授权的菜单id列表数据", path = "/getResourceByGroupId", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "用户组id",varName = "groupId")})
    @ApiOperation(value = "根据用户组ID获取授权的菜单id列表数据")
    @GetMapping("/getResourceByGroupId")
    public List<String> getResourceByGroupId(@RequestParam("groupId") String groupId){
        return osGroupMenuServiceImpl.getResourceByGroupId(groupId);
    }
    @MethodDefine(title = "根据多个用户组ID获取授权的菜单id列表数据", path = "/getResourceByGroupId", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "用户组id",varName = "groupId")})
    @ApiOperation(value = "根据多个用户组ID获取授权的菜单id列表数据")
    @GetMapping("/getResourceByGroupIdList")
    public List<String> getResourceByGroupIdList(@RequestParam("groupIds") String groupIds){
        return osGroupMenuServiceImpl.getResourceByGroupIdList(groupIds);
    }

    @ApiOperation("根据多个用户组ID获取授权的菜单列表数据")
    @GetMapping("/getAppIdGroupIdList")
    List<String> getAppIdGroupIdList(@RequestParam(value = "groupIds") String groupId){
        return osGroupMenuServiceImpl.getAppIdGroupIdList(groupId);
    }
}
