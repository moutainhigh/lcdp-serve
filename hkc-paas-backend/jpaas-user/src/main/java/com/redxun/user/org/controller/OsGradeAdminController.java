package com.redxun.user.org.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.JsonPageResult;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.base.entity.QueryData;
import com.redxun.common.base.search.QueryFilter;
import com.redxun.common.base.search.QueryFilterBuilder;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ExceptionUtil;
import com.redxun.dto.user.OsGroupDto;
import com.redxun.log.annotation.AuditLog;
import com.redxun.log.model.Audit;
import com.redxun.log.model.LogContext;
import com.redxun.user.org.entity.OsGradeAdmin;
import com.redxun.user.org.entity.OsGradeRole;
import com.redxun.user.org.entity.OsGroup;
import com.redxun.user.org.entity.OsUser;
import com.redxun.user.org.service.OsGradeAdminServiceImpl;
import com.redxun.user.org.service.OsGradeRoleServiceImpl;
import com.redxun.user.org.service.OsGroupServiceImpl;
import com.redxun.user.org.service.OsUserServiceImpl;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * 分级管理员Controller
 */
@Slf4j
@RestController
@RequestMapping("/user/org/osGradeAdmin")
@ClassDefine(title = "分级管理员",alias = "osGradeAdminController",path = "/user/org/osGradeAdmin",packages = "org",packageName = "组织架构")
@Api(tags = "分级管理员")
public class OsGradeAdminController extends BaseController<OsGradeAdmin> {

    @Autowired
    OsGradeAdminServiceImpl osGradeAdminService;

    @Autowired
    OsGradeRoleServiceImpl osGradeRoleService;

    @Autowired
    OsUserServiceImpl osUserService;

    @Autowired
    OsGroupServiceImpl osGroupService;

    @Override
    public BaseService getBaseService() {
        return osGradeAdminService;
    }

    @Override
    public String getComment() {
        return "分级管理员";
    }

    @MethodDefine(title = "查询分级管理员角色", path = "/queryRole", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "查询数据", varName = "queryData")})
    @ApiOperation("查询分级管理员角色")
    @PostMapping(value="/queryRole")
    public JsonPageResult queryRole(@RequestBody QueryData queryData) throws Exception{
        JsonPageResult jsonResult=JsonPageResult.getSuccess("返回数据成功!");
        try{
            QueryFilter filter= QueryFilterBuilder.createQueryFilter(queryData);
            handleFilter(filter);
            IPage page= osGradeAdminService.query(filter);
            List<OsGradeAdmin> list = page.getRecords();
            for(OsGradeAdmin osGradeAdmin:list){
                List<OsGradeRole> inOsGradeRoleList = osGradeAdminService.getRoleByAdminId(osGradeAdmin.getId());
                OsUser user = osUserService.getById(osGradeAdmin.getUserId());
                if(user != null){
                    osGradeAdmin.setUserNo(user.getUserNo());
                }
                if(BeanUtil.isEmpty(inOsGradeRoleList)){
                    continue;
                }

                String roleName = "";
                osGradeAdmin.setOsGradeRoles(inOsGradeRoleList);
                for(int j=0;j<inOsGradeRoleList.size();j++){
                    if(StringUtils.isEmpty(roleName)){
                        roleName=roleName+inOsGradeRoleList.get(j).getName();
                    }else {
                        roleName = roleName+","+inOsGradeRoleList.get(j).getName();
                    }

                }
                osGradeAdmin.setRoleName(roleName);
            }
            jsonResult.setPageData(page);
        }catch (Exception ex){
            jsonResult.setSuccess(false);
            logger.error(ExceptionUtil.getExceptionMessage(ex));
            jsonResult.setMessage(ExceptionUtil.getExceptionMessage(ex));
        }
        return jsonResult;
    }

    @MethodDefine(title = "保存分级管理员配置", path = "/saveAll", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "保存数据", varName = "jsonObject")})
    @ApiOperation("保存分级管理员配置")
    @PostMapping("/saveAll")
    @AuditLog(operation = "保存分级管理员配置")
    public JsonResult saveAll(@ApiParam @RequestBody JSONObject jsonObject) throws Exception{

        String successName = "[分级管理员]";
        List<OsGradeAdmin> list = jsonObject.getJSONArray("osGradeAdmin").toJavaList(OsGradeAdmin.class);
        List<String> idList =osGradeAdminService.saveAll(list);
        if(BeanUtil.isEmpty(idList)){
            return new JsonResult(false,successName+"数据为空!");
        }
        StringBuilder sb=new StringBuilder();
        sb.append("分配角色:");

        List<OsGradeRole> osGradeRoleList = jsonObject.getJSONArray("osGradeRole").toJavaList(OsGradeRole.class);
        List<OsGradeRole> newOsGradeRole = new ArrayList<OsGradeRole>();
        for(int i=0;osGradeRoleList!=null && i<osGradeRoleList.size();i++){
            OsGradeRole gradeRole=osGradeRoleList.get(i);
            sb.append(gradeRole.getName() +",");
            for(int j=0;j<idList.size();j++){
                String userId=idList.get(j);
                OsGradeRole osGradeRole = new OsGradeRole();
                osGradeRole.setAdminId(userId);
                osGradeRole.setGroupId(gradeRole.getGroupId());
                osGradeRole.setName(gradeRole.getName());
                newOsGradeRole.add(osGradeRole);
            }
        }

        sb.append("给用户:");

        for(int j=0;j<list.size();j++){
            OsGradeAdmin user = list.get(j);
            sb.append(user.getFullname() +"("+user.getUserId()+")");

        }
        LogContext.put(Audit.DETAIL,sb.toString());

        osGradeAdminService.saveAllRole(newOsGradeRole);
        successName =successName+"和[分级管理员角色]";

        return new JsonResult(true,successName+"成功创建");
    }

    @MethodDefine(title = "更新分级管理员角色", path = "/updateRole", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "更新数据", varName = "jsonObject")})
    @PostMapping("/updateRole")
    @ApiOperation("更新分级管理员角色")
    @AuditLog(operation = "更新分级管理员角色")
    public JsonResult updateRole(@ApiParam @RequestBody JSONObject jsonObject) throws Exception{
        String adminId = jsonObject.getString("gradeAdminId");
        List<OsGradeRole> osGradeRoleList =jsonObject.getJSONArray("osGradeRole").toJavaList(OsGradeRole.class);

        osGradeRoleService.delByAdminId(adminId);

        for(OsGradeRole role:osGradeRoleList){
            role.setId(IdGenerator.getIdStr());
            role.setAdminId(adminId);
            osGradeRoleService.insert(role);
        }

        return new JsonResult(true,"成功更新分级管理员角色！");
    }

    @MethodDefine(title = "根据公司管理员用户id获取其管理的所有公司列表", path = "/getCompanyIdsByAdminUserId", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "租户id", varName = "tenantId"), @ParamDefine(title = "公司管理员用户id", varName = "userId")})
    @ApiOperation(value = "根据公司管理员用户id获取其管理的所有公司列表")
    @GetMapping("/getCompanysByAdminId")
    public List<OsGroupDto> getCompanysByAdminId(@ApiParam @RequestParam("tenantId")String tenantId, @ApiParam @RequestParam("userId") String userId) {
        List<OsGradeAdmin> list = osGradeAdminService.getAdminByUserId(tenantId, userId);
        if(BeanUtil.isEmpty(list)){
            return null;
        }
        list = list.stream().collect(Collectors.collectingAndThen(
                Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(OsGradeAdmin :: getGroupId))), ArrayList::new));
        List<OsGroupDto> result = new ArrayList<>();
        OsGroupDto osGroupDto = null;
        for(OsGradeAdmin osGradeAdmin : list){
            OsGroup osGroup = osGroupService.getById(osGradeAdmin.getGroupId());
            osGroupDto = new OsGroupDto();
            osGroupDto.setGroupId(osGroup.getGroupId());
            osGroupDto.setName(osGroup.getName());
            result.add(osGroupDto);
        }
        return result;



    }

}
