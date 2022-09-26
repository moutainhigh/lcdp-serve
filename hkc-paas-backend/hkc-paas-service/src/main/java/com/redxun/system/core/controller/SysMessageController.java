
package com.redxun.system.core.controller;

import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.mq.MessageModel;
import com.redxun.system.core.entity.SysLog;
import com.redxun.system.core.service.SysMessageServiceImpl;
import com.redxun.system.messagehandler.MessageHandlerContext;
import com.redxun.system.messagehandler.MessageType;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/system/core/message")
@ClassDefine(title = "统一消息管理",alias = "sysMessageController",path = "/system/core/message",packages = "system",packageName = "系统管理")
@Api(tags = "系统统一消息")
public class SysMessageController extends BaseController<SysLog> {

    @Autowired
    SysMessageServiceImpl sysMessageService;


    @Override
    public BaseService getBaseService() {
    return sysMessageService;
    }

    @Override
    public String getComment() {
    return "系统日志";
    }


    /**
     * 获取消息处理器
     * @return
     */
    @GetMapping("/getMessageHandler")
    public List<MessageType> getMessageHandler() {
        List<MessageType> list= MessageHandlerContext.getTypes();

        return list;
    }

    @MethodDefine(title = "统一消息接口", path = "/sendMessage", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "消息体", varName = "model")})
    @ApiOperation(value = "统一消息接口")
    @PostMapping("/sendMessage")
    public JsonResult sendMessage(@RequestBody MessageModel model){
        sysMessageService.sendMessage(model);
        return JsonResult.Success("统一消息成功！");
    }

}

