package com.redxun.user.org.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.tool.Result;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.tool.constant.CommonConstant;
import com.redxun.common.utils.ContextUtil;
import com.redxun.common.utils.DbLogicDelete;
import com.redxun.feign.sys.SystemClient;
import com.redxun.log.annotation.AuditLog;
import com.redxun.log.model.Audit;
import com.redxun.log.model.LogContext;
import com.redxun.user.org.entity.OsInstType;
import com.redxun.user.org.entity.OsInstTypeMenu;
import com.redxun.user.org.service.OsInstTypeMenuServiceImpl;
import com.redxun.user.org.service.OsInstTypeServiceImpl;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 机构分类菜单Controller
 */
@Slf4j
@RestController
@RequestMapping("/user/org/osInstTypeMenu")
@ClassDefine(title = "机构类型授权菜单",alias = "osInstTypeMenuController",path = "/user/org/osInstTypeMenu",packages = "org",packageName = "用户模块")
@Api(tags = "机构类型授权菜单")
public class OsInstTypeMenuController extends BaseController<OsInstTypeMenu> {

    @Autowired
    OsInstTypeMenuServiceImpl osInstTypeMenuServiceImpl;
    @Autowired
    OsInstTypeServiceImpl osInstTypeService;
    @Resource
    SystemClient systemClient;

    @Override
    public BaseService getBaseService() {
        return osInstTypeMenuServiceImpl;
    }

    @Override
    public String getComment() {
        return "机构类型授权菜单";
    }

    /**
     * 查询
     */
    @MethodDefine(title = "根据机构类型ID查询菜单列表", path = "/findMenusByInstTypeId", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "机构类型ID",varName = "instTypeId")})
    @ApiOperation(value = "根据机构类型ID查询菜单列表")
    @GetMapping("/findMenusByInstTypeId")
    public Result findMenusByInstTypeId(@RequestParam String instTypeId) {
        List<SysMenuDto> menus = osInstTypeMenuServiceImpl.findMenusByInstTypeId(instTypeId);
        return Result.succeed(menus, "查询成功");
    }

    @GetMapping("/findSysMenuDtoByInstTypeId")
    public List<SysMenuDto> findSysMenuDtoByInstTypeId(@RequestParam(value = "instTypeId") String instTypeId) {
        List<SysMenuDto> menus = osInstTypeMenuServiceImpl.findMenusByInstTypeId(instTypeId);
        return menus;
    }

    @MethodDefine(title = "根据机构类型ID查询菜单ID列表", path = "/getMenusByInstTypeId", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "机构类型ID",varName = "instTypeId")})
    @ApiOperation(value = "根据机构类型ID查询菜单ID列表")
    @GetMapping("/getInstTypeMenusByTenantId")
    public List<String> getInstTypeMenusByTenantId(@RequestParam(value = "tenantId") String tenantId) {
        List<String> menus = osInstTypeMenuServiceImpl.getInstTypeMenusByTenantId(tenantId);
        return menus;
    }


    /**
     * 新增or更新
     * <pre>
     *     根据前端选择Id集合，返回关联的菜单和关联的接口。
     * </pre>
     */
    @MethodDefine(title = "保存", path = "/saveOrUpdate", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "权限数据",varName = "objs")})
    @ApiOperation(value = "保存")
    @AuditLog( operation = "机构类型菜单授权")
    @PostMapping("/saveOrUpdate")
    public JsonResult saveOrUpdate(@RequestParam(value = "osInstTypeId") String osInstTypeId, @RequestBody List<OsInstTypeMenu> typeMenus) {
        OsInstType osInstType=osInstTypeService.getById(osInstTypeId);

        String detail="对机构类型:" +osInstType.getTypeName() +"("+osInstType.getTypeId()+")进行菜单授权";
        LogContext.put(Audit.DETAIL,detail);


        //逻辑删除
        if (DbLogicDelete.getLogicDelete()) {
            osInstTypeMenuServiceImpl.deleteByInstTypeId(osInstTypeId);
        }else {
            QueryWrapper wrapper=new QueryWrapper();
            wrapper.eq("INST_TYPE_ID_",osInstTypeId);
            osInstTypeMenuServiceImpl.getRepository().delete(wrapper);
        }


        if(BeanUtil.isEmpty(typeMenus)){
            return JsonResult.Success("保存授权成功!");
        }
        //获取应用列表
        List<OsInstTypeMenu> appList = typeMenus.stream()
                .filter(item -> {return "0".equals(item.getParentId()); })
                .map(item->item.setMenuType("C"))
                .collect(Collectors.toList());
        //获取菜单列表
        List<String> menuIds = typeMenus.stream()
                .filter(item -> {return !"0".equals(item.getParentId()); })
                .map(item->item.getMenuId())
                .collect(Collectors.toList());

        List<OsInstTypeMenu> menuList=new ArrayList<>();
        //获取菜单对象。
        if(BeanUtil.isNotEmpty(menuIds)){
            MultiValueMap valueMap=new LinkedMultiValueMap();
            valueMap.add("menuIds", StringUtils.join(menuIds,",") );

            List<SysMenuDto> menuDtos = systemClient.getAllByMenuIds(valueMap);
            for(SysMenuDto dto:menuDtos){
                OsInstTypeMenu menu=new OsInstTypeMenu();
                cn.hutool.core.bean.BeanUtil.copyProperties(dto,menu);
                String parentId="0".equals(dto.getParentId())?dto.getAppId():dto.getParentId();
                menu.setParentId(parentId);
                menu.setMenuId(dto.getId());
                menuList.add(menu);
            }
        }
        menuList.addAll(appList);

        IUser user= ContextUtil.getCurrentUser();
        menuList.forEach(menu->{
            menu.setId(IdGenerator.getIdStr());
            menu.setCreateBy(user.getUserId());
            menu.setInstTypeId(osInstTypeId);
            menu.setTenantId(user.getTenantId());
        });
        osInstTypeMenuServiceImpl.saveBatch(menuList);
        //清除接口授权缓存
        removeApiCache();

        return JsonResult.getSuccessResult("保存成功");
    }

    /**
     * 删除缓存。
     */
    private void removeApiCache(){
        CacheUtil.remove(CommonConstant.API_REGION, CommonConstant.API_KEY);
    }

    /**
     * 更新菜单权限
     */
    @MethodDefine(title = "更新菜单权限", path = "/updateMenuControl", method = HttpMethodConstants.POST)
    @ApiOperation(value = "更新菜单权限")
    @AuditLog( operation = "更新菜单权限")
    @PostMapping("/updateMenuControl")
    public void updateMenuControl( @RequestBody SysMenuDto sysMenuDto) {
        removeApiCache();
        osInstTypeMenuServiceImpl.updateMenuControl(sysMenuDto);
    }

    /**
     * 更新菜单权限
     */
    @MethodDefine(title = "删除菜单权限", path = "/delMenuControl", method = HttpMethodConstants.POST)
    @ApiOperation(value = "删除菜单权限")
    @AuditLog( operation = "删除菜单权限")
    @PostMapping("/delMenuControl")
    public void delMenuControl( @RequestBody List<String> idList) {
        removeApiCache();
        osInstTypeMenuServiceImpl.delMenuControl(idList);
    }


}
