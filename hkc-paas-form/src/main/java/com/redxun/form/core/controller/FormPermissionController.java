package com.redxun.form.core.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.redxun.api.org.IOrgService;
import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.StringUtils;
import com.redxun.feign.OsGroupClient;
import com.redxun.form.bo.entity.FormBoEntity;
import com.redxun.form.bo.service.FormBoEntityServiceImpl;
import com.redxun.form.core.entity.FormPc;
import com.redxun.form.core.entity.FormPermission;
import com.redxun.form.core.entity.FormSolution;
import com.redxun.form.core.service.FormPcServiceImpl;
import com.redxun.form.core.service.FormPermissionServiceImpl;
import com.redxun.form.core.service.FormSolutionServiceImpl;
import com.redxun.profile.ProfileContext;
import com.redxun.profile.ProfileType;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;


@Slf4j
@RestController
@RequestMapping("/form/core/formPermission")
@ClassDefine(title = "表单权限配置",alias = "formPermissionController",path = "/form/core/formPermission",packages = "core",packageName = "表单管理")
@Api(tags = "表单权限配置")
public class FormPermissionController extends BaseController<FormPermission> {

    @Autowired
    FormPermissionServiceImpl formPermissionServiceImpl;
    @Autowired
    FormSolutionServiceImpl formSolutionService;
    @Autowired
    private FormPcServiceImpl formPcServiceImpl;
    @Autowired
    private FormBoEntityServiceImpl boEntityService;
    @Autowired
    OsGroupClient osGroupClient;
    @Resource
    IOrgService orgService;

    @Override
    public BaseService getBaseService() {
        return formPermissionServiceImpl;
    }

    @Override
    public String getComment() {
        return "表单权限配置";
    }

    @MethodDefine(title = "根据表单方案ID查找权限", path = "/getByFormSolutionId", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "主键", varName = "pkId"),@ParamDefine(title = "类型", varName = "type")})
    @GetMapping(value = "getByFormSolutionId")
    public JsonResult getByFormSolutionId(@RequestParam(value = "pkId",required = true)String pkId,
                                          @RequestParam(value = "type",required = true)String type ){
        FormPc formPc=null;
        if(FormPermission.TYPE_FORM.equals(type)){
            formPc= formPcServiceImpl.getById(pkId);
        }else {
            FormSolution formSolution=formSolutionService.get(pkId);
            formPc= formPcServiceImpl.getById(formSolution.getFormId());
        }

        String boDefId=formPc.getBodefId();
        FormBoEntity boEntity= boEntityService.getWithAttrsByDefId(boDefId);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id","");

        FormPermission permission= formPermissionServiceImpl.getByconfigId(type,pkId);
        JSONObject permissionJson =new JSONObject();
        if(BeanUtil.isEmpty(permission)){
            permissionJson = formPermissionServiceImpl.getFormInitPermission(boEntity,formPc,"",false);
        }else {
            jsonObject.put("id",permission.getPkId());
            permissionJson = formPermissionServiceImpl.getFormInitPermission(boEntity,formPc,permission.getPermission(),false);
        }
        jsonObject.put("permission",permissionJson);

        return JsonResult.Success().setData(jsonObject);
    }

    @MethodDefine(title = "获取表单对应权限配置", path = "/getBpmFormPermissonByFormAlias", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "请求参数", varName = "params")})
    @ApiOperation(value = "获取表单对应权限配置", notes = "获取表单对应权限配置")
    @PostMapping("/getBpmFormPermissonByFormAlias")
    public JsonResult getBpmFormPermissonByFormAlias(@RequestBody JSONObject params) {
        String alias = params.getString("alias");
        String permisson = params.getString("permisson");
        FormPc formPc= formPcServiceImpl.getByAlias(alias);
        String boDefId=formPc.getBodefId();
        FormBoEntity boEntity= boEntityService.getWithAttrsByDefId(boDefId);

        JSONObject jsonObject = new JSONObject();

        JSONObject permissionJson =new JSONObject();
        if(StringUtils.isEmpty(permisson) || "{}".equals(permisson)){
            permissionJson = formPermissionServiceImpl.getFormInitPermission(boEntity,formPc,"",false);
        }else {
            permissionJson = formPermissionServiceImpl.getFormInitPermission(boEntity,formPc,permisson,false);
        }
        jsonObject.put("permission",permissionJson);

        return JsonResult.Success().setData(jsonObject);
    }


    /**
     * 获取权限设置策略列表
     * @return
     */
    @MethodDefine(title = "获取权限设置策略列表", path = "/getPermissionList", method = HttpMethodConstants.GET)
    @ApiOperation(value = "获取权限设置策略列表", notes = "获取权限设置策略列表")
    @GetMapping("/getPermissionList")
    public JsonResult<List<ProfileType>> getPermissionList() {
        List<ProfileType> profileList = ProfileContext.getProfileList();
        return JsonResult.Success().setData(profileList);
    }

    @MethodDefine(title = "重新加载表单方案权限", path = "/reloadFormPermission", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "表单方案id", varName = "solutionId")})
    @GetMapping(value = "reloadFormPermission")
    public JsonResult reloadFormPermission(@RequestParam(value = "solutionId",required = true)String solutionId ){
        JsonResult jsonResult = JsonResult.getSuccessResult("获取成功!").setShow(false);
        FormSolution formSolution = formSolutionService.get(solutionId);
        FormPc formPc=formPcServiceImpl.getById(formSolution.getFormId());
        String boDefId=formPc.getBodefId();
        FormBoEntity boEntity= boEntityService.getWithAttrsByDefId(boDefId);
        JSONObject formInitPermission = formPermissionServiceImpl.getFormInitPermission(boEntity, formPc, "", false);
        jsonResult.setData(formInitPermission);
        return jsonResult;
    }

    @PostMapping("/getResultByPermissions")
    public JsonResult getResultByPermissions(@RequestBody String permissions){
        JsonResult result=new JsonResult();
        if(StringUtils.isEmpty(permissions)){
            return result;
        }
        List<Boolean> flagAry=new ArrayList<>();
        Map<String, Set<String>> profiles =  orgService.getCurrentProfile();
        JSONArray permissionAry=JSONArray.parseArray(permissions);
        for(int i=0;i<permissionAry.size();i++){
            String permission=permissionAry.getString(i);
            boolean flag = formPermissionServiceImpl.hasRights(permission, profiles);
            flagAry.add(flag);
        }
        result.setData(flagAry);
        return result;
    }

}