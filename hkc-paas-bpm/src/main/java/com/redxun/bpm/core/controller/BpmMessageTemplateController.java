
package com.redxun.bpm.core.controller;

import com.redxun.bpm.core.entity.BpmOpinionLib;
import com.redxun.bpm.core.service.BpmDefaultTemplateServiceImpl;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.bpm.core.entity.BpmMessageTemplate;
import com.redxun.bpm.core.service.BpmMessageTemplateServiceImpl;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.constant.MBoolean;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ContextUtil;
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
@RequestMapping("/bpm/core/bpmMessageTemplate")
@Api(tags = "bpm_message_template")
@ClassDefine(title = "bpm_message_template",alias = "BpmMessageTemplateController",path = "/bpm/core/bpmMessageTemplate",packages = "core",packageName = "子系统名称")
public class BpmMessageTemplateController extends BaseController<BpmMessageTemplate> {

    @Autowired
    BpmMessageTemplateServiceImpl bpmMessageTemplateService;
    @Autowired
    BpmDefaultTemplateServiceImpl bpmDefaultTemplateService;



    @Override
    public BaseService getBaseService() {
    return bpmMessageTemplateService;
    }

    @Override
    public String getComment() {
    return "流程消息模板";
    }


    @MethodDefine(title = "获取流程节点模板配置", path = "/getTemplateByDefNode", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "请求数据", varName = "request")})
    @PostMapping("getTemplateByDefNode")
    public List<BpmMessageTemplate> getTemplateByDefNode(@RequestParam(value = "defId")String defId,
                                                         @RequestParam(value = "nodeId")String nodeId){

        List<BpmMessageTemplate> list = bpmMessageTemplateService.getByDefAndNodeId(defId,nodeId);
        return list;
    }


    @MethodDefine(title = "获取流程节点模板配置", path = "/getTemplateById", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "主键ID", varName = "id"),
                    @ParamDefine(title = "是否默认", varName = "isDefault")})
    @PostMapping("getTemplateById")
    public String getTemplateById(@RequestParam(value = "id")String id,
                                  @RequestParam(value = "isDefault")String isDefault){
        String template="";
        if(MBoolean.YES.val.equals(isDefault)){
            template=bpmDefaultTemplateService.get(id).getTemplate();
        }
        else{
            template= bpmMessageTemplateService.get(id).getTemplate();
        }
        return template;
    }

    @MethodDefine(title = "保存模板", path = "/saveTemplate", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "消息对象", varName = "bpmMessageTemplate")})
    @PostMapping("saveTemplate")
    public JsonResult saveTemplate(@RequestBody BpmMessageTemplate template){
        JsonResult result= bpmMessageTemplateService.saveTemplate(template);
        return result;
    }



}

