package com.redxun.bpm.core.controller;

import com.redxun.bpm.core.entity.BpmInstLog;
import com.redxun.bpm.core.service.BpmInstLogServiceImpl;
import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.JsonResult;
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
@RequestMapping("/bpm/core/bpmInstLog")
@ClassDefine(title = "流程活动日志",alias = "bpmInstLogController",path = "/bpm/core/bpmInstLog",packages = "core",packageName = "流程管理")
@Api(tags = "流程活动日志")
public class BpmInstLogController extends BaseController<BpmInstLog> {

    @Autowired
    BpmInstLogServiceImpl bpmInstLogService;

    @Override
    public BaseService getBaseService() {
        return bpmInstLogService;
    }

    @Override
    public String getComment() {
        return "流程活动日志";
    }

    /**
     * 通过流程获得流程活动列表
     * @param instId
     * @return
     */
    @MethodDefine(title = "通过流程获得流程活动列表", path = "/getByInstId", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "实例ID", varName = "instId")})
    @ApiOperation("通过流程获得流程活动列表")
    @GetMapping("getByInstId")
    public JsonResult getByInstId(@ApiParam  @RequestParam("instId") String instId){
        List list= bpmInstLogService.getByInstId(instId);
        return new JsonResult(true,list,"");
    }

}