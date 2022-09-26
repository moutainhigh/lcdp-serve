
package com.redxun.form.core.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.JsonPageResult;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.tool.BeanUtil;
import com.redxun.dto.form.DataResult;
import com.redxun.form.bo.entity.FormBoAttr;
import com.redxun.form.bo.entity.FormBoDef;
import com.redxun.form.bo.entity.FormBoEntity;
import com.redxun.form.bo.service.FormBoDefServiceImpl;
import com.redxun.form.bo.service.FormBoEntityServiceImpl;
import com.redxun.form.core.entity.FormBusInstData;
import com.redxun.form.core.entity.FormBusinessSolution;
import com.redxun.form.core.entity.FormResult;
import com.redxun.form.core.entity.FormSolution;
import com.redxun.form.core.service.FormBusInstDataServiceImpl;
import com.redxun.form.core.service.FormBusinessSolutionServiceImpl;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.tool.StringUtils;
import com.redxun.form.core.service.FormSolutionServiceImpl;
import com.redxun.log.annotation.AuditLog;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.redxun.common.annotation.ClassDefine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/form/core/formBusinessSolution")
@Api(tags = "表单业务方案")
@ClassDefine(title = "表单业务方案", alias = "FormBusinessSolutionController", path = "/form/core/formBusinessSolution", packages = "core", packageName = "子系统名称")

public class FormBusinessSolutionController extends BaseController<FormBusinessSolution> {

    @Autowired
    FormBusinessSolutionServiceImpl formBusinessSolutionService;
    @Autowired
    FormSolutionServiceImpl formSolutionService;
    @Autowired
    FormBusInstDataServiceImpl formBusInstDataService;
    @Autowired
    FormBoEntityServiceImpl formBoEntityService;
    @Autowired
    FormBoDefServiceImpl formBoDefService;

    @Override
    public BaseService getBaseService() {
        return formBusinessSolutionService;
    }

    @Override
    public String getComment() {
        return "表单业务方案";
    }


    @MethodDefine(title = "获取业务方案", path = "/getByIdAndMainPk", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "业务方案Id", varName = "pkId"),@ParamDefine(title = "主表单Id", varName = "pkId")})
    @ApiOperation("保存业务表单数据")
    @AuditLog(operation = "获取业务方案")
    @GetMapping(value = "getByIdAndMainPk")
    public JsonResult getByIdAndMainPk(@RequestParam(value = "pkId") String pkId,@RequestParam(value = "mainPk") String mainPk){
        JSONObject jsonObject=new JSONObject();
        FormBusinessSolution formBusinessSolution = formBusinessSolutionService.get(pkId);
        jsonObject.put("busSolution",formBusinessSolution);
        if(StringUtils.isNotEmpty(mainPk)){
            String mainFormSolution = formBusinessSolution.getMainFormSolution();
            String mainSolAlias = JSONObject.parseObject(mainFormSolution).getString("value");
            FormResult formResult = formSolutionService.getFormData(mainSolAlias, mainPk, null);
            JSONObject formData = formResult.getData();
            String formSolutions = formBusinessSolution.getFormSolutions();
            JSONArray jsonArray = JSONArray.parseArray(formSolutions);
            JSONObject obj = jsonArray.getJSONObject(0);
            String relFieId = obj.getString("relField");
            String relPk="";
            if(StringUtils.isEmpty(relFieId)){
                relFieId="ID_";
            }
            if(StringUtils.isNotEmpty(formData.getString(relFieId))){
                relPk=formData.getString(relFieId);
            }
            List<FormBusInstData> list=formBusInstDataService.getDataByMainPk(pkId,relPk);
            jsonObject.put("formBusInstData",list);
        }
        JsonPageResult result = new JsonPageResult();
        result.setData(jsonObject);
        result.setShow(false);
        return result;
    }


    @MethodDefine(title = "保存业务表单数据", path = "/saveBusForm", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "业务表单数据", varName = "jsonObject")})
    @ApiOperation("保存业务表单数据")
    @AuditLog(operation = "保存业务表单数据")
    @PostMapping(value = "saveBusForm")
    public JsonResult saveBusForm(@RequestBody JSONObject jsonObject){
        JSONArray data = jsonObject.getJSONArray("data");
        try{
            JsonResult result=new JsonResult().setSuccess(true).setMessage("保存数据成功!");
            List list=new ArrayList();
            for (int i = 0; i < data.size(); i++) {
                JsonResult jsonResult = formSolutionService.handData(data.getJSONObject(i));
                DataResult dataResult = (DataResult) jsonResult.getData();
                FormBoDef formBoDef = formBoDefService.getByAlias(dataResult.getBoAlias());
                FormBoEntity formBoEntity = formBoEntityService.getByDefId(formBoDef.getId(),true);
                List<FormBoAttr> formBoAttrs = formBoEntity.getBoAttrList();
                String relField = data.getJSONObject(i).getString("relField");
                String pk="";
                if(StringUtils.isNotEmpty(relField)){
                    JSONObject formData = data.getJSONObject(i).getJSONObject("data");
                    String value = formData.getString(relField);
                    for (int j = 0; j < formBoAttrs.size(); j++) {
                        FormBoAttr formBoAttr = formBoAttrs.get(j);
                        if(relField.equals(formBoAttr.getName())){
                            if(formBoAttr.getIsSingle()==0){
                                if(StringUtils.isNotEmpty(value)){
                                    value=JSONObject.parseObject(value).getString("value");
                                }
                            }
                            break;
                        }
                    }
                    pk=value;
                }
                //无主表主键值 则为当前
                if(StringUtils.isEmpty(jsonObject.getString("mainPk"))){
                    jsonObject.put("mainPk",pk);
                }
                formBusinessSolutionService.saveInitData(jsonObject,dataResult,pk);
                list.add(jsonResult);
            }
            result.setData(list);
            return result;
        }
        catch (Exception ex){
            formSolutionService.handException(ex);
            return null;
        }
    }

    @MethodDefine(title = "按Id删除", path = "/removeById", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "单据数据", varName = "jsonObject")})
    @ApiOperation("按Id删除")
    @AuditLog(operation = "删除单据数据")
    @PostMapping(value = "removeById")
    public JsonResult removeById(@RequestBody JSONObject jsonObject) {
        return formBusinessSolutionService.delByIds(jsonObject);
    }
}

