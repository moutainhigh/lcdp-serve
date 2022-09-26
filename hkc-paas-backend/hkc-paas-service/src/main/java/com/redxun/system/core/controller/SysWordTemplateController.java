
package com.redxun.system.core.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.tool.StringUtils;
import com.redxun.system.core.entity.SysWordTemplate;
import com.redxun.system.core.service.SysWordTemplateServiceImpl;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/system/core/sysWordTemplate")
@ClassDefine(title = "文档模板编辑",alias = "sysWordTemplateController",path = "/system/core/sysWordTemplate",packages = "core",packageName = "系统管理")
@Api(tags = "文档模板编辑")
public class SysWordTemplateController extends BaseController<SysWordTemplate> {

    @Autowired
    SysWordTemplateServiceImpl sysWordTemplateService;


    @Override
    public BaseService getBaseService() {
        return sysWordTemplateService;
    }

    @Override
    public String getComment() {
        return "文档模板编辑";
    }

    @MethodDefine(title = "获取业务定义数据的结构", path = "/getFields", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "主键ID", varName = "pkId")})
    @ApiOperation(value="获取业务定义数据的结构")
    @GetMapping("getFields")
    public JSONArray getFields(@RequestParam (value="pkId") String pkId) throws Exception{
        if(StringUtils.isEmpty(pkId)){
            return new JSONArray();
        }
        JSONArray array= sysWordTemplateService.getMetaData(pkId);
        return array;
    }

    @MethodDefine(title = "获取文档模板数据", path = "/getData", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "主键ID", varName = "pkId"),@ParamDefine(title = "业务主键", varName = "id")})
    @ApiOperation(value="获取文档模板数据")
    @GetMapping("getData")
    public JSONObject getData(@RequestParam (value="pkId") String pkId, @RequestParam (value="id") String id) throws Exception{
        if(StringUtils.isEmpty(pkId)){
            return new JSONObject();
        }
        SysWordTemplate sysWordTemplate = sysWordTemplateService.get(pkId);
        JSONObject data = sysWordTemplateService.getData(sysWordTemplate, id);
        return data;
    }

    /**
     * 通过templateId获取文档模板
     * @param templateId
     * @return
     */
    @MethodDefine(title = "根据主键查询记录详细信息", path = "/getByTemplateId", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "模板ID", varName = "templateId")})
    @ApiOperation(value="查看单条记录信息", notes="根据主键查询记录详细信息")
    @GetMapping("/getByTemplateId")
    public JsonResult getByTemplateId(@RequestParam (value="templateId") String templateId){
        JsonResult result=JsonResult.Success();
        result.setShow(false);
        if(ObjectUtils.isEmpty(templateId)){
            return result.setData(new Object());
        }
        SysWordTemplate ent=sysWordTemplateService.getByTemplateId(templateId);
        return result.setData(ent);
    }

}

