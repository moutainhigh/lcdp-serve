
package com.redxun.bpm.core.controller;

import com.redxun.bpm.core.entity.BpmTask;
import com.redxun.bpm.core.entity.BpmTemporaryOpinion;
import com.redxun.bpm.core.service.BpmTaskService;
import com.redxun.bpm.core.service.BpmTemporaryOpinionServiceImpl;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ContextUtil;
import com.redxun.log.annotation.AuditLog;
import com.redxun.log.model.Audit;
import com.redxun.log.model.LogContext;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/bpm/core/bpmTemporaryOpinion")
@Api(tags = "流程意见暂存")
public class BpmTemporaryOpinionController extends BaseController<BpmTemporaryOpinion> {

    @Autowired
    BpmTemporaryOpinionServiceImpl bpmTemporaryOpinionService;
    @Autowired
    BpmTaskService bpmTaskService;


    @Override
    public BaseService getBaseService() {
        return bpmTemporaryOpinionService;
    }

    @Override
    public String getComment() {
        return "流程意见暂存";
    }

    @MethodDefine(title = "流程意见暂存", path = "/saveOpinion", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "任务ID", varName = "taskId"),@ParamDefine(title = "意见", varName = "opinion"),@ParamDefine(title = "主键", varName = "id")})
    @AuditLog(operation = "流程意见暂存")
    @ApiOperation("流程意见暂存")
    @PostMapping("saveOpinion")
    public JsonResult saveOpinion(@RequestParam(value = "taskId") String taskId,@RequestParam(value = "opinion") String opinion) {
        BpmTask bpmTask=bpmTaskService.get(taskId);
        if(StringUtils.isEmpty(opinion)){
            return JsonResult.Fail("没有传入意见");
        }

        String curUserId = ContextUtil.getCurrentUserId();
        BpmTemporaryOpinion bpmTemporaryOpinion=bpmTemporaryOpinionService.getByTaskIdAndUserId(taskId,curUserId);
        if(bpmTemporaryOpinion==null){
            BpmTemporaryOpinion temporaryOpinion=new BpmTemporaryOpinion();
            temporaryOpinion.setTaskId(taskId);
            temporaryOpinion.setOpinion(opinion);
            bpmTemporaryOpinionService.insert(temporaryOpinion);
        }
        else{
            bpmTemporaryOpinion.setOpinion(opinion);
            bpmTemporaryOpinionService.update(bpmTemporaryOpinion);
        }
        String detail="设置任务("+ bpmTask.getSubject() +")的临时意见:"+opinion;
        LogContext.put(Audit.DETAIL,detail);
        return JsonResult.Success().setMessage("意见暂存成功!");
    }

    @MethodDefine(title = "获取流程意见暂存", path = "/getTemporaryOpinion", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "任务ID", varName = "taskId")})
    @ApiOperation("获取流程意见暂存")
    @GetMapping("getTemporaryOpinion")
    public BpmTemporaryOpinion getTemporaryOpinion(@RequestParam(value = "taskId") String taskId) {
        String curUserId = ContextUtil.getCurrentUserId();
        BpmTemporaryOpinion bpmTemporaryOpinion=bpmTemporaryOpinionService.getByTaskIdAndUserId(taskId,curUserId);
        return bpmTemporaryOpinion;
    }
}

