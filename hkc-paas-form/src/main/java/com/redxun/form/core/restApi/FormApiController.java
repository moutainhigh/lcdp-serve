package com.redxun.form.core.restApi;

import com.alibaba.fastjson.JSONObject;
import com.redxun.annotation.CurrentUser;
import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.model.JPaasUser;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ContextUtil;
import com.redxun.common.utils.RequestUtil;
import com.redxun.controller.RestApiController;
import com.redxun.feign.org.UserClient;
import com.redxun.form.core.service.FormCustomQueryUtil;
import com.redxun.form.core.service.FormSolutionServiceImpl;
import com.redxun.idempotence.IdempotenceRequired;
import com.redxun.log.annotation.AuditLog;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 表单相关外部API
 */
@Slf4j
@RestController
@RequestMapping("/restApi/form")
@ClassDefine(title = "表单相关外部API", alias = "form", path = "/restApi/form")
public class FormApiController implements RestApiController {
    @Autowired
    FormSolutionServiceImpl formSolutionService;


    @MethodDefine(title = "保存单据数据",path = "/save/{alias}/{action}",method = HttpMethodConstants.POST)
    @IdempotenceRequired
    @CurrentUser
    @AuditLog(operation = "保存单据数据")
    @PostMapping(value = "/save/{alias}/{action}")
    public JsonResult saveForm(@PathVariable(name="alias")String alias, @PathVariable(name="action")String action,
                               @RequestBody JSONObject data){
        try {
            JSONObject setting=new JSONObject();
            setting.put("alias",alias);
            setting.put("action",action);
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("setting",setting);
            jsonObject.put("data",data);

            JsonResult jsonResult = formSolutionService.handData(jsonObject);
            return jsonResult;
        } catch (Exception ex) {
            JsonResult rtn = JsonResult.Fail("保存数据失败!");
            return rtn;
        }
    }

    @MethodDefine(title = "删除单据数据",path = "/remove/{alias}",method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "主键",varName = "id")})
    @IdempotenceRequired
    @CurrentUser
    @AuditLog(operation = "删除单据数据")
    @PostMapping(value = "/remove/{alias}")
    public JsonResult removeById(@PathVariable(name = "alias")String alias,@RequestParam("id")String id) {
        try {
            JSONObject json=new JSONObject();

            json.put("id",id);
            json.put("alias",alias);
            json.put("cascade",true);

            JsonResult jsonResult = formSolutionService.removeData(json);
            return jsonResult;
        } catch (Exception ex) {
            JsonResult rtn = JsonResult.Fail("删除数据失败!");
            return rtn;
        }
    }


    @MethodDefine(title = "获取单据数据",path = "/getData/{alias}",method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "业务主键",varName = "pk")})
    @CurrentUser
    @GetMapping (value = "/getData/{alias}")
    public JsonResult getData(@PathVariable(value="alias" )String alias,
                              @RequestParam(value="pk" )String pk){
            JsonResult jsonResult = formSolutionService.getDataByPk(alias,pk);
            return jsonResult;
    }


    @MethodDefine(title = "根据别名查询自定义SQL数据", path = "/query/{alias}", method = HttpMethodConstants.POST)
    @ApiOperation(value="查询JSON", notes="根据别名查询自定义SQL数据")
    @PostMapping("/query/{alias}")
    public JsonResult queryForJson( @PathVariable(value = "alias")String alias, @RequestParam(value="params" )String params,
                                    @RequestParam(value="deploy" ,required = false )String deploy) throws Exception{
        if(StringUtils.isEmpty(alias)){
            return JsonResult.getFailResult("请输入别名");
        }
        JsonResult result = FormCustomQueryUtil.queryForJson(alias,params,deploy);
        return result;
    }


}
