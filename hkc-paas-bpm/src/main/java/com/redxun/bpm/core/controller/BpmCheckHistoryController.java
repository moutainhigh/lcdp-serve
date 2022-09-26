package com.redxun.bpm.core.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.redxun.bpm.core.entity.BpmCheckHistory;
import com.redxun.bpm.core.service.BpmCheckHistoryServiceImpl;
import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.JsonPageResult;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.base.entity.QueryData;
import com.redxun.common.base.search.QueryFilter;
import com.redxun.common.base.search.QueryFilterBuilder;
import com.redxun.common.base.search.QueryParam;
import com.redxun.common.base.search.WhereParam;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ContextUtil;
import com.redxun.common.utils.ExceptionUtil;
import com.redxun.dto.sys.SysTreeDto;
import com.redxun.feign.sys.SystemClient;
import com.redxun.util.QueryFilterUtil;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Slf4j
@RestController
@RequestMapping("/bpm/core/bpmCheckHistory")
@ClassDefine(title = "bpmCheckHistory",alias = "bpmCheckHistoryController",path = "/bpm/core/bpmCheckHistory",packages = "core",packageName = "流程管理")
@Api(tags = "流程审批流转记录")
public class BpmCheckHistoryController extends BaseController<BpmCheckHistory> {

    @Autowired
    BpmCheckHistoryServiceImpl bpmCheckHistoryService;
    @Autowired
    SystemClient systemClient;

    @Override
    public BaseService getBaseService() {
        return bpmCheckHistoryService;
    }

    @Override
    public String getComment() {
        return "流程审批流转记录";
    }

    @MethodDefine(title = "查看流程的审批历史", path = "/getCheckHistorys", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "实例ID", varName = "instId")})
    @ApiOperation("查看流程的审批历史")
    @GetMapping("getCheckHistorys")
    public List<BpmCheckHistory> getCheckHistorys(@ApiParam  @RequestParam(value = "instId") String instId){
        return bpmCheckHistoryService.getByInstId(instId);
    }

    @MethodDefine(title = "根据instId查询OpinionName不为空的审批意见", path = "/getOpinionNameNotEmpty", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "实例ID", varName = "instId")})
    @ApiOperation("根据instId查询OpinionName不为空的审批意见")
    @GetMapping("getOpinionNameNotEmpty")
    public List<BpmCheckHistory> getOpinionNameNotEmpty(@ApiParam @RequestParam(value = "instId") String instId){
        return bpmCheckHistoryService.getOpinionNameNotEmpty(instId);
    }

    @MethodDefine(title = "按条件查询我的所有已办", path = "/getMyApproved", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "查询数据", varName = "queryData")})
    @ApiOperation(value="按条件查询我的所有已办", notes="按条件查询我的所有已办")
    @PostMapping(value="/getMyApproved")
    public JsonPageResult getMyApproved(@RequestBody QueryData queryData) throws Exception{
        JsonPageResult jsonResult=JsonPageResult.getSuccess("返回数据成功!");

        QueryFilter filter= QueryFilterBuilder.createQueryFilter(queryData);
        String userId= ContextUtil.getCurrentUserId();
        filter.addParam("HANDLER_ID_",userId);
        filter.addParam("TENANT_ID_",ContextUtil.getCurrentTenantId());
        IPage page= bpmCheckHistoryService.getMyApproved(filter);
        jsonResult.setPageData(page);

        return jsonResult;
    }

    @MethodDefine(title = "按条件查询我的已办", path = "/getMyAllApproved", method = HttpMethodConstants.GET)
    @ApiOperation(value="按条件查询我的已办", notes="按条件查询我的已办")
    @GetMapping(value="/getMyAllApproved")
    public JsonResult getMyAllApproved() throws Exception{
        JsonResult jsonResult=new JsonResult();

        String userId= ContextUtil.getCurrentUserId();
        List<BpmCheckHistory> list= bpmCheckHistoryService.getMyAllApproved(userId);
        jsonResult.setData(list);

        return jsonResult;
    }

    /**
     * 查询我的已经办的任务数
     * @return
     * @throws Exception
     */
    @MethodDefine(title = "查询我的已经办的任务数", path = "/getMyApprovedTaskCount", method = HttpMethodConstants.GET)
    @ApiOperation(value="查询我的已经办的任务数")
    @GetMapping(value="/getMyApprovedTaskCount")
    public JsonResult getMyApprovedTaskCount() throws Exception{
        Integer counts= bpmCheckHistoryService.getMyApprovedCount(ContextUtil.getCurrentUserId(),ContextUtil.getCurrentTenantId());
        return new JsonResult(true,counts,"");
    }

}
