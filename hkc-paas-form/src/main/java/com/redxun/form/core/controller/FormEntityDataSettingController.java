
package com.redxun.form.core.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.JsonPageResult;
import com.redxun.common.base.entity.QueryData;
import com.redxun.common.base.search.QueryFilter;
import com.redxun.common.base.search.QueryFilterBuilder;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.form.core.entity.FormEntityDataSetting;
import com.redxun.form.core.entity.FormEntityDataSettingDic;
import com.redxun.form.core.service.FormEntityDataSettingDicServiceImpl;
import com.redxun.form.core.service.FormEntityDataSettingServiceImpl;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.tool.StringUtils;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/form/core/formEntityDataSetting")
@Api(tags = "业务实体数据配置")
@ClassDefine(title = "业务实体数据配置",alias = "FormEntityDataSettingController",path = "/form/core/formEntityDataSetting",packages = "core",packageName = "表单管理")
public class FormEntityDataSettingController extends BaseController<FormEntityDataSetting> {

    @Autowired
    FormEntityDataSettingServiceImpl formEntityDataSettingService;
    @Autowired
    FormEntityDataSettingDicServiceImpl formEntityDataSettingDicService;


    @Override
    public BaseService getBaseService() {
        return formEntityDataSettingService;
    }

    @Override
    protected JsonResult beforeRemove(List<String> list) {
        List<String> settingIdList=new ArrayList<>();
        for (String roleId:list) {
            FormEntityDataSetting formEntityDataSetting=formEntityDataSettingService.getByRoleId(roleId);
            String[] dataTypeAry=formEntityDataSetting.getDataTypeId().split(",");
            for(String dataTypeId:dataTypeAry){
                formEntityDataSetting=formEntityDataSettingService.getByRoleType(roleId,dataTypeId);
                formEntityDataSettingDicService.deleteBySettingId(formEntityDataSetting.getId());
                settingIdList.add(formEntityDataSetting.getId());
            }
        }
        return super.beforeRemove(settingIdList);
    }

    @Override
    public String getComment() {
        return "业务实体数据配置";
    }
    @MethodDefine(title = "根据类型id查询所查看权限数据", path = "/queryData_*", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "类型id", varName = "id")})
    @ApiOperation(value="查看权限数据", notes="根据类型id查询所查看权限数据")
    @GetMapping("/queryData_{id}")
    public JsonResult queryData( @PathVariable(value = "id")String id) throws Exception{
        if(StringUtils.isEmpty(id)){
            return JsonResult.getFailResult("请输入类型ID");
        }
        return formEntityDataSettingService.queryData(id);
    }

    @PostMapping("queryRole")
    public JsonPageResult queryRole(@RequestBody QueryData queryData){
        JsonPageResult result=JsonPageResult.getSuccess("返回数据成功！");
        QueryFilter filter = QueryFilterBuilder.createQueryFilter(queryData);
        this.handleFilter(filter);
        IPage page = formEntityDataSettingService.queryRole(filter);
        this.handlePage(page);
        result.setPageData(page);
        return result;
    }

    @GetMapping("getByRoleId")
    public JsonResult getByRoleId(@RequestParam(value = "roleId")String roleId){
        JsonResult result = JsonResult.Success();
        result.setShow(false);
        if (ObjectUtils.isEmpty(roleId)) {
            return result.setData(new Object());
        } else {
            FormEntityDataSetting ent=formEntityDataSettingService.getByRoleId(roleId);
            String dataTypeId=ent.getDataTypeId();
            if(StringUtils.isNotEmpty(dataTypeId)){
                String[] dataTypeIdAry=dataTypeId.split(",");
                for(String id:dataTypeIdAry){
                    ent.getDataTypeDicMap().put(id,formEntityDataSettingDicService.getByRoleIdDataTypeId(roleId,id));
                }
            }
            return result.setData(ent);
        }
    }

    @GetMapping("validRole")
    public JsonResult validRole(@RequestParam("roleId")String roleId){
        FormEntityDataSetting formEntityDataSetting=formEntityDataSettingService.getByRoleId(roleId);
        if(formEntityDataSetting!=null){
            return JsonResult.Fail();
        }
        return JsonResult.Success();
    }

    @PostMapping("saveRole")
    public JsonResult saveRole(@RequestBody JSONObject json){
        String roleId=json.getString("roleId");
        String roleName=json.getString("roleName");
        JSONArray dataTypeList=json.getJSONArray("dataTypeList");
        return formEntityDataSettingService.saveRole(roleId,roleName,dataTypeList);
    }
}

