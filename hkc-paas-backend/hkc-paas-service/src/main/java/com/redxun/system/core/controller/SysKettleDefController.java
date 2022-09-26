
package com.redxun.system.core.controller;

import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.log.annotation.AuditLog;
import com.redxun.system.core.entity.SysKettleDef;
import com.redxun.system.core.service.SysKettleDefServiceImpl;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/system/core/sysKettleDef")
@Api(tags = "KETTLE定义")
@ClassDefine(title = "KETTLE定义",alias = "SysKettleDefController",path = "/system/core/sysKettleDef",packages = "core",packageName = "子系统名称")
public class SysKettleDefController extends BaseController<SysKettleDef> {

    @Autowired
    SysKettleDefServiceImpl sysKettleDefService;


    @Override
    public BaseService getBaseService() {
    return sysKettleDefService;
    }

    @Override
    public String getComment() {
        return "KETTLE定义";
    }

    @MethodDefine(title = "根据资源库Id获取作业", path = "/getJobTree", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "资源库Id", varName = "kettleDbdefId")})
    @ApiOperation("根据资源库Id获取作业")
    @GetMapping("/getJobTree")
    public JsonResult getJobTree(@RequestParam(name = "kettleDbdefId") String kettleDbdefId){
        return  sysKettleDefService.getJobTree(kettleDbdefId);
    }

    @ApiOperation("文件上传")
    @MethodDefine(title = "文件上传", path = "/uploadFile", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "文件上传", varName = "request")})
    @AuditLog(operation = "文件上传")
    @RequestMapping("uploadFile")
    public JsonResult uploadFile(MultipartHttpServletRequest request) throws Exception {
        MultiValueMap<String, MultipartFile> multiFileMap = request.getMultiFileMap();
        List<MultipartFile> files = multiFileMap.get("file");
        JsonResult jsonResult =sysKettleDefService.uploadFile(files);
        return  jsonResult;
    }


    @MethodDefine(title = "运行kettle", path = "/runKettle", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "kettleDefId", varName = "kettleDefId")})
    @ApiOperation("运行kettle")
    @GetMapping("/runKettle")
    public JsonResult runKettle(@RequestParam(name = "kettleDefId") String kettleDefId){
        return  sysKettleDefService.runKettle(kettleDefId,"Debug");
    }


}

