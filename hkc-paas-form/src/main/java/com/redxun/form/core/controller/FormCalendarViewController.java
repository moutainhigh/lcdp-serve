
package com.redxun.form.core.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.StringUtils;
import com.redxun.form.core.entity.FormBoPmt;
import com.redxun.form.core.entity.FormCalendarView;
import com.redxun.form.core.service.FormBoPmtServiceImpl;
import com.redxun.form.core.service.FormCalendarViewServiceImpl;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.redxun.common.annotation.ClassDefine;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/form/core/formCalendarView")
@Api(tags = "表单日历视图")
@ClassDefine(title = "表单日历视图", alias = "FormCalendarViewController", path = "/form/core/formCalendarView", packages = "core", packageName = "子系统名称")

public class FormCalendarViewController extends BaseController<FormCalendarView> {

    @Autowired
    FormCalendarViewServiceImpl formCalendarViewService;
    @Autowired
    FormBoPmtServiceImpl formBoPmtService;


    @Override
    public BaseService getBaseService() {
        return formCalendarViewService;
    }

    @Override
    public String getComment() {
        return "表单日历视图";
    }

    @MethodDefine(title = "根据主键获取表单日历视图", path = "/getById", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "主键", varName = "pkId"),@ParamDefine(title = "获取字段", varName = "getFields")})
    @ApiOperation(value = "根据主键获取表单日历视图", notes = "根据主键获取表单日历视图")
    @GetMapping("/getById")
    public JsonResult getById(HttpServletRequest request, @ApiParam(value = "pkId") String pkId,
                              @ApiParam(value = "getFields",required = false,defaultValue = "false") Boolean getFields) {
        FormCalendarView formCalendarView = formCalendarViewService.getById(pkId);
        try {
            if(getFields){
               formCalendarViewService.getColumns(request, formCalendarView);
            }
       }catch (Exception ex){
            ex.printStackTrace();
            return new JsonResult(false,ex.getMessage()).setShow(false);
       }
        return new JsonResult(true,"获取成功").setShow(false).setData(formCalendarView);
    }

    @MethodDefine(title = "根据标识键获取表单日历视图数据", path = "/getDataByKey", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "标识键", varName = "key"),@ParamDefine(title = "参数", varName = "params")})
    @ApiOperation(value = "根据标识键获取表单日历视图数据", notes = "根据标识键获取表单日历视图数据")
    @PostMapping("/getDataByKey")
    public JsonResult getDataByKey(HttpServletRequest request, @RequestParam(value = "key") String key,@RequestParam(value = "params",required = false) String params) {
        JSONObject resObj=new JSONObject();
        FormCalendarView formCalendarView = formCalendarViewService.getByKey(key);
        if(BeanUtil.isEmpty(formCalendarView)){
            return new JsonResult(false,"未找到标识键【"+key+"】的日历视图!").setData(resObj);
        }
        Map<String, Object> requestParams = formCalendarViewService.getParams(request);
        String menuId = (String) requestParams.get("menuId");
        FormBoPmt formBoPmt=formBoPmtService.getByBoListIdMenuId(formCalendarView.getId(),menuId);
        JSONArray buttons=formCalendarViewService.getPmtButtons(formCalendarView,menuId,formBoPmt);
        formCalendarView.setButtonConf(buttons);
        resObj.put("calendarView",formCalendarView);
        //查询数据
        JSONObject data = formCalendarViewService.getData(formCalendarView, request,params,formBoPmt);
        resObj.put("calendarData",data);
        resObj.put("formBoPmt",formBoPmt);
        return new JsonResult(true,"获取成功").setShow(false).setData(resObj);
    }


    @Override
    protected JsonResult beforeSave(FormCalendarView formCalendarView) {
        boolean isExist = formCalendarViewService.isExist(formCalendarView);
        if (isExist) {
            return JsonResult.Fail("标识键已存在!");
        }
        //数据权限配置
        if(StringUtils.isNotEmpty(formCalendarView.getId()) && StringUtils.isNotEmpty(formCalendarView.getPermissionConf())){
            formBoPmtService.saveConfig(formCalendarView.getPermissionConf(),formCalendarView.getId(),formCalendarView.getKey());
        }
        return JsonResult.Success();
    }


}

