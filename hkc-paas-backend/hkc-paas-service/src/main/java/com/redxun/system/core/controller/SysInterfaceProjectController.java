
package com.redxun.system.core.controller;

import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.system.core.entity.SysInterfaceProject;
import com.redxun.system.core.service.SysInterfaceApiServiceImpl;
import com.redxun.system.core.service.SysInterfaceClassificationServiceImpl;
import com.redxun.system.core.service.SysInterfaceProjectServiceImpl;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/system/core/sysInterfaceProject")
@Api(tags = "接口项目表")
@ClassDefine(title = "接口项目表",alias = "SysInterfaceProjectController",path = "/system/core/sysInterfaceProject",packages = "core",packageName = "子系统名称")
public class SysInterfaceProjectController extends BaseController<SysInterfaceProject> {

    @Autowired
    SysInterfaceProjectServiceImpl sysInterfaceProjectService;
    @Autowired
    SysInterfaceClassificationServiceImpl sysInterfaceClassificationService;
    @Autowired
    SysInterfaceApiServiceImpl sysInterfaceApiService;


    @Override
    public BaseService getBaseService() {
        return sysInterfaceProjectService;
    }

    @Override
    public String getComment() {
        return "接口项目表";
    }

    @Override
    protected JsonResult beforeSave(SysInterfaceProject ent) {
        boolean result= sysInterfaceProjectService.isExist( ent);
        if(result){
            return JsonResult.Fail("项目别名或名称已存在!");
        }
        return JsonResult.Success();
    }

    @MethodDefine(title = "查询接口项目", path = "/queryProject", method = HttpMethodConstants.GET)
    @ApiOperation("查询接口项目")
    @GetMapping("/queryProject")
    public List<SysInterfaceProject> queryProject(){
        return sysInterfaceProjectService.queryProject();
    }

    @Override
    protected JsonResult beforeRemove(List<String> list) {
        for(String projectId:list) {
            sysInterfaceApiService.deleteByProjectId(projectId);
            sysInterfaceClassificationService.deleteByProjectId(projectId);
        }
        return super.beforeRemove(list);
    }
}

