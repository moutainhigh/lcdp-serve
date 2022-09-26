package com.redxun.form.core.controller;

import com.alibaba.fastjson.JSONObject;
import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ExceptionUtil;
import com.redxun.form.core.entity.FormBoList;
import com.redxun.form.core.entity.FormQueryStrategy;
import com.redxun.form.core.service.FormBoListServiceImpl;
import com.redxun.form.core.service.FormQueryStrategyServiceImpl;
import com.redxun.log.annotation.AuditLog;
import com.redxun.log.model.LogContext;
import com.redxun.log.util.EntityUtil;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("/form/core/formQueryStrategy")
@ClassDefine(title = "查询策略表",alias = "formQueryStrategyController",path = "/form/core/formQueryStrategy",packages = "core",packageName = "表单管理")
@Api(tags = "查询策略表")
public class FormQueryStrategyController extends BaseController<FormQueryStrategy> {

    @Autowired
    FormQueryStrategyServiceImpl formQueryStrategyServiceImpl;
    @Autowired
    FormBoListServiceImpl formBoListServiceImpl;
    @Override
    public BaseService getBaseService() {
        return formQueryStrategyServiceImpl;
    }

    @Override
    public String getComment() {
        return "查询策略表";
    }

    @MethodDefine(title = "通过列表ID，获取查询策略", path = "/getByListId", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "列表ID", varName = "listId")})
    @ApiOperation(value = "通过列表ID，获取查询策略")
    @GetMapping("getByListId")
    public JsonResult getByListId(@RequestParam("listId") String listId,@RequestParam(value = "isMobile",defaultValue = "0")String isMobile) {
        JsonResult result=JsonResult.Success().setShow(false);
        if("0".equals(isMobile)) {
            List<FormQueryStrategy> list = formQueryStrategyServiceImpl.getByListId(listId);
            result.setData(list);
        }else{
            //手机端视图
            FormBoList formBoList=formBoListServiceImpl.getById(listId);
            String mobileConf=formBoList.getMobileConf();
            if(StringUtils.isNotEmpty(mobileConf)) {
                JSONObject conf=JSONObject.parseObject(mobileConf);
                result.setData(conf.getJSONArray("mobileView"));
            }
        }
        return result;
    }

    @MethodDefine(
            title = "保存业务数据记录",
            path = "/save2",
            method = HttpMethodConstants.POST,
            params = {@ParamDefine(
                    title = "实体数据JSON",
                    varName = "entity"
            )}
    )
    @ApiOperation(            value = "保存业务数据记录",            notes = "根据提交的业务JSON数据保存业务数据记录"    )
    @AuditLog(            operation = "保存业务数据记录"    )
    @PostMapping({"/save2"})
    public JsonResult save2(@Validated @ApiParam @RequestBody FormQueryStrategy entity) throws Exception {
            JsonResult result=JsonResult.Success();
            result.setShow(false);
            String pkId =  entity.getPkId();
            FormBoList boList= formBoListServiceImpl.getById(entity.getListId());
            if(boList!=null){
                entity.setAppId(boList.getAppId());
            }
            try {
                if (BeanUtil.isEmpty(pkId)) {
                    this.getBaseService().insert(entity);
                } else {
                    this.getBaseService().update(entity);
                }
                result.setMessage("保存成功!");
                result.setData(entity);
            } catch (Exception var9) {
                result.setMessage("保存失败!");
            }
            return result;
    }
}