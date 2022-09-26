package com.redxun.bpm.core.controller;

import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.bpm.core.entity.BpmCheckFile;
import com.redxun.bpm.core.service.BpmCheckFileService;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/bpm/core/bpmCheckFile")
@ClassDefine(title = "审批意见附件",alias = "bpmCheckFileController",path = "/bpm/core/bpmCheckFile",packages = "core",packageName = "流程管理")
@Api(tags = "审批意见附件")
public class BpmCheckFileController extends BaseController<BpmCheckFile> {

    @Autowired
    BpmCheckFileService bpmCheckFileService;

    @Override
    public BaseService getBaseService() {
        return bpmCheckFileService;
    }

    @Override
    public String getComment() {
        return "审批意见附件";
    }

    @MethodDefine(title = "查看流程审批附件", path = "/getByInstId", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "实例ID", varName = "instId")})
    @ApiOperation("查看流程审批附件")
    @GetMapping("getByInstId")
    public List<BpmCheckFile> getByInstId(@ApiParam @RequestParam(value = "instId") String instId){
        return bpmCheckFileService.getByInstId(instId);
    }

}