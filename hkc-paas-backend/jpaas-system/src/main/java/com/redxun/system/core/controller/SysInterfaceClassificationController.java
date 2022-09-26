
package com.redxun.system.core.controller;

import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.system.core.entity.SysInterfaceClassification;
import com.redxun.system.core.service.SysInterfaceApiServiceImpl;
import com.redxun.system.core.service.SysInterfaceClassificationServiceImpl;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.tool.StringUtils;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/system/core/sysInterfaceClassification")
@Api(tags = "接口分类表")
@ClassDefine(title = "接口分类表",alias = "SysInterfaceClassificationController",path = "/system/core/sysInterfaceClassification",packages = "core",packageName = "子系统名称")
public class SysInterfaceClassificationController extends BaseController<SysInterfaceClassification> {

    @Autowired
    SysInterfaceClassificationServiceImpl sysInterfaceClassificationService;
    @Autowired
    SysInterfaceApiServiceImpl sysInterfaceApiService;


    @Override
    public BaseService getBaseService() {
        return sysInterfaceClassificationService;
    }

    @Override
    public String getComment() {
        return "接口分类表";
    }

    @MethodDefine(title = "根据项目ID查询接口分类", path = "/queryByProjectId", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "项目ID", varName = "projectId")})
    @ApiOperation("根据项目ID查询接口分类")
    @GetMapping("/queryByProjectId")
    public List<SysInterfaceClassification> queryByProjectId(@ApiParam @RequestParam String projectId){
        return sysInterfaceClassificationService.getByProjectId(projectId);
    }

    @Override
    protected JsonResult beforeRemove(List<String> list) {
        for(String classificationId:list) {
            sysInterfaceApiService.deleteByClassificationId(classificationId);
        }
        return super.beforeRemove(list);
    }
}

