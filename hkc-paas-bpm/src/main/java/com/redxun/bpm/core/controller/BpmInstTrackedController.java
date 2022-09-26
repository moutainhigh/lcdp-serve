
package com.redxun.bpm.core.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.redxun.bpm.core.service.BpmInstServiceImpl;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.bpm.core.entity.BpmInstTracked;
import com.redxun.bpm.core.service.BpmInstTrackedServiceImpl;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.redxun.common.base.entity.IUser;
import com.redxun.common.base.entity.JsonPageResult;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.base.entity.QueryData;
import com.redxun.common.base.search.QueryFilter;
import com.redxun.common.base.search.QueryFilterBuilder;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ContextUtil;
import com.redxun.log.annotation.AuditLog;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.redxun.common.annotation.ClassDefine;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/bpm/core/bpmInstTracked")
@Api(tags = "流程实例跟踪")
@ClassDefine(title = "流程实例跟踪",alias = "BpmInstTrackedController",path = "/bpm/core/bpmInstTracked",packages = "core",packageName = "子系统名称")

public class BpmInstTrackedController extends BaseController<BpmInstTracked> {

    @Autowired
    BpmInstTrackedServiceImpl bpmInstTrackedService;
    @Autowired
    BpmInstServiceImpl bpmInstService;

    @Override
    public BaseService getBaseService() {
        return bpmInstTrackedService;
    }

    @Override
    public String getComment() {
        return "流程实例跟踪";
    }

    @MethodDefine(title = "添加删除跟踪", path = "/doTracked", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "实例ID", varName = "instId")})
    @ApiOperation("添加删除跟踪")
    @AuditLog(operation = "添加删除跟踪")
    @PostMapping("doTracked")
    public JsonResult doTracked(@RequestParam(value="instId") String instId) throws Exception {

        IUser user= ContextUtil.getCurrentUser();
        //处理抄送。
        JsonResult jsonResult =bpmInstTrackedService.doTracked(instId,user.getUserId());

        return jsonResult;
    }


    @MethodDefine(title = "获取我的跟踪的流程", path = "/getMyTracked", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "查询数据", varName = "queryData")})
    @ApiOperation(value="获取我的跟踪的流程", notes="获取我的跟踪的流程")
    @PostMapping(value="/getMyTracked")
    public JsonPageResult getMyTracked(@RequestBody QueryData queryData) throws Exception{
        JsonPageResult jsonResult=JsonPageResult.getSuccess("返回数据成功!");
        QueryFilter filter= QueryFilterBuilder.createQueryFilter(queryData);
        String userId= ContextUtil.getCurrentUserId();
        filter.addParam("createBy",userId);
        IPage page= bpmInstTrackedService.getMyTracked(filter);
        handlePage(page);
        jsonResult.setPageData(page);
        return jsonResult;
    }

    @MethodDefine(title = "删除跟踪", path = "/removeTracked", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "查询数据", varName = "instId")})
    @ApiOperation(value="删除跟踪", notes="删除跟踪")
    @PostMapping(value="/removeTracked")
    public JsonResult removeTracked(@RequestParam(value = "instId") String instId) {
        JsonResult result=JsonResult.Success("删除跟踪成功!");
        String userId= ContextUtil.getCurrentUserId();
        bpmInstTrackedService.removeTracked(instId,userId);
        return result;
    }
}

