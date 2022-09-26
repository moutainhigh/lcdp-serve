package com.redxun.bpm.core.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.redxun.bpm.core.entity.BpmTask;
import com.redxun.bpm.core.entity.BpmTaskTransfer;
import com.redxun.bpm.core.service.BpmTaskService;
import com.redxun.bpm.core.service.BpmTaskTransferServiceImpl;
import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.ContextQuerySupport;
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
import com.redxun.common.utils.MessageUtil;
import com.redxun.dto.sys.SysTreeDto;
import com.redxun.feign.sys.SystemClient;
import com.redxun.util.QueryFilterUtil;
import com.redxun.web.controller.BaseController;
import com.redxun.web.util.WebUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Slf4j
@RestController
@RequestMapping("/bpm/core/bpmTaskTransfer")
@ClassDefine(title = "流程任务转移记录",alias = "bpmTaskTransferController",path = "/bpm/core/bpmTaskTransfer",packages = "core",packageName = "流程管理")
@Api(tags = "流程任务转移记录")
public class BpmTaskTransferController extends BaseController<BpmTaskTransfer> {

    @Autowired
    BpmTaskTransferServiceImpl bpmTaskTransferService;
    @Autowired
    BpmTaskService bpmTaskService;
    @Autowired
    SystemClient systemClient;

    @Override
    public BaseService getBaseService() {
        return bpmTaskTransferService;
    }

    @Override
    public String getComment() {
        return "流程任务转移记录";
    }

    @MethodDefine(title = "按条件查询我收到的任务代办", path = "/getMyReceiveTask", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "查询数据", varName = "queryData")})
    @ApiOperation(value="按条件查询我收到的任务代办", notes="按条件查询我收到的任务代办")
    @PostMapping(value="/getMyReceiveTask")
    public JsonPageResult getMyReceiveTask(@RequestBody QueryData queryData) throws Exception{
        JsonPageResult jsonResult=JsonPageResult.getSuccess("返回数据成功!");

        QueryFilter filter= QueryFilterBuilder.createQueryFilter(queryData);
        String userId= ContextUtil.getCurrentUserId();
        filter.addParam("TO_USER_ID_",userId);

        WebUtil.handFilter(filter, ContextQuerySupport.CURRENT,ContextQuerySupport.NONE);

        IPage page= bpmTaskTransferService.getMyReceiveTasks(filter);
        List<BpmTaskTransfer> list=page.getRecords();
        for(BpmTaskTransfer bpmTaskTransfer:list){
            BpmTask bpmTask=bpmTaskService.get(bpmTaskTransfer.getTaskId());
            //任务已经完成或删除
            if(bpmTask==null){
                bpmTaskTransfer.setTaskId("");
            }
        }
        jsonResult.setPageData(page);

        return jsonResult;
    }

    @MethodDefine(title = "按条件查询我收到的任务代办", path = "/getMyTransOutTask", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "查询数据", varName = "queryData")})
    @ApiOperation(value="按条件查询我收到的任务代办", notes="按条件查询我收到的任务代办")
    @PostMapping(value="/getMyTransOutTask")
    public JsonPageResult getMyTransOutTask(@RequestBody QueryData queryData) throws Exception{
        JsonPageResult jsonResult=JsonPageResult.getSuccess("返回数据成功!");

        QueryFilter filter= QueryFilterBuilder.createQueryFilter(queryData);
        String userId= ContextUtil.getCurrentUserId();
        filter.addParam("OWNER_ID_",userId);

        WebUtil.handFilter(filter, ContextQuerySupport.CURRENT,ContextQuerySupport.NONE);

        IPage page= bpmTaskTransferService.getMyTransOutTask(filter);
        List<BpmTaskTransfer> list=page.getRecords();
        for(BpmTaskTransfer bpmTaskTransfer:list){
            BpmTask bpmTask=bpmTaskService.get(bpmTaskTransfer.getTaskId());
            //任务已经完成或删除
            if(bpmTask==null){
                bpmTaskTransfer.setTaskId("");
            }
        }
        jsonResult.setPageData(page);

        return jsonResult;
    }

    @MethodDefine(title = "取回任务", path = "/doRetrieveTask", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "请求数据", varName = "jsonObject")})
    @ApiOperation(value="取回任务", notes="取回任务")
    @PostMapping(value="/doRetrieveTask")
    public JsonResult doRetrieveTask(@RequestBody JSONObject jsonObject) throws Exception{
        JsonResult jsonResult=JsonResult.Success("取回任务成功!");
        try{
            String taskId=jsonObject.getString("taskId");
            String opinion=jsonObject.getString("opinion");
            String msgTypes=jsonObject.getString("msgTypes");
            return bpmTaskTransferService.doRetrieveTask(taskId,opinion,msgTypes);
        }catch (Exception ex){
            MessageUtil.triggerException("取回任务失败!",ExceptionUtil.getExceptionMessage(ex));
        }
        return jsonResult;
    }


}
