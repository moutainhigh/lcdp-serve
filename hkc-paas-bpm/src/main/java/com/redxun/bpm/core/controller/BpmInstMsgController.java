package com.redxun.bpm.core.controller;

import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.bpm.core.entity.BpmInstMsg;
import com.redxun.bpm.core.service.BpmInstMsgServiceImpl;
import com.redxun.common.base.entity.IUser;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ContextUtil;
import com.redxun.log.annotation.AuditLog;
import com.redxun.log.model.Audit;
import com.redxun.log.model.LogContext;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/bpm/core/bpmInstMsg")
@ClassDefine(title = "流程沟通留言板",alias = "bpmInstMsgController",path = "/bpm/core/bpmInstMsg",packages = "core",packageName = "流程管理")
@Api(tags = "流程沟通留言板")
public class BpmInstMsgController extends BaseController<BpmInstMsg> {

    @Autowired
    BpmInstMsgServiceImpl bpmInstMsgService;

    @Override
    public BaseService getBaseService() {
        return bpmInstMsgService;
    }

    @Override
    public String getComment() {
        return "流程沟通留言板";
    }

    @MethodDefine(title = "获得流程实例留言", path = "/getByInstId", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "实例ID", varName = "instId")})
    @ApiOperation("获得流程实例留言")
    @GetMapping("getByInstId")
    public List<BpmInstMsg> getByInstId(@ApiParam @RequestParam("instId") String instId){
        return bpmInstMsgService.getByInstId(instId);
    }

    /**
     * 对流程实例进行留言
     * @param instId
     * @param msg
     * @return
     */
    @MethodDefine(title = "对流程实例进行留言", path = "/addMsg", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "实例ID", varName = "instId"),@ParamDefine(title = "留言信息", varName = "msg")})
    @ApiOperation("对流程实例进行留言")
    @PostMapping("addMsg")
    @AuditLog(operation = "对流程实例进行留言")
    public JsonResult addMsg(@ApiParam @RequestParam("instId") String instId,@ApiParam @RequestParam("msg") String msg){
        if(StringUtils.isEmpty(msg)){
            return new JsonResult(true,"留言信息不能为空");
        }
        IUser user=ContextUtil.getCurrentUser();
        BpmInstMsg mg=new BpmInstMsg();
        mg.setInstId(instId);
        mg.setContent(msg.trim());
        mg.setId(IdGenerator.getIdStr());
        mg.setAuthor(user.getFullName());
        mg.setAuthorId(user.getUserId());
        bpmInstMsgService.insert(mg);

        String detail="对流程实例:"+ instId +"进行留言,内容为:" + msg;
        LogContext.put(Audit.PK,instId);
        LogContext.put(Audit.DETAIL,detail);

        return new JsonResult(true,mg,"成功留言！");
    }

}
