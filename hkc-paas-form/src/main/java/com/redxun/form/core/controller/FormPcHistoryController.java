
package com.redxun.form.core.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.StringUtils;
import com.redxun.dto.user.OsUserDto;
import com.redxun.feign.OsUserClient;
import com.redxun.form.core.entity.FormPcHistory;
import com.redxun.form.core.service.FormPcHistoryServiceImpl;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.redxun.common.annotation.ClassDefine;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/form/core/formPcHistory")
@Api(tags = "表单设计历史")
@ClassDefine(title = "表单设计历史", alias = "FormPcHistoryController", path = "/form/core/formPcHistory", packages = "core", packageName = "子系统名称")

public class FormPcHistoryController extends BaseController<FormPcHistory> {

    @Autowired
    FormPcHistoryServiceImpl formPcHistoryService;
    @Autowired
    OsUserClient osUserClient;

    @Override
    public BaseService getBaseService() {
        return formPcHistoryService;
    }

    @Override
    public String getComment() {
        return "表单设计历史";
    }

    @MethodDefine(title = "转换html",path = "/conversionHtml",method = HttpMethodConstants.POST)
    @ApiOperation("转换html")
    @PostMapping(value = "conversionHtml")
    public JsonResult conversionHtml(@RequestBody JSONObject jsonObject){
        JSONObject jsonResult=new JSONObject();
        String oldHtml = jsonObject.getString("oldHtml");
        String curHtml = jsonObject.getString("curHtml");
        try {
            Document oldDoc = Jsoup.parse(oldHtml);
            String oldDocHtml = oldDoc.html();
            jsonResult.put("oldHtml",oldDocHtml);

            Document curDoc = Jsoup.parse(curHtml);
            String curDocHtml = curDoc.html();
            jsonResult.put("curHtml",curDocHtml);
        }catch (Exception e){
            e.printStackTrace();
            return new JsonResult().setShow(false).setSuccess(false).setMessage("转换失败!");
        }
        return new JsonResult().setShow(false).setMessage("转换成功!").setSuccess(true).setData(jsonResult);
    }


    @Override
    protected void handlePage(IPage page) {
        for (int i = 0; i < page.getRecords().size(); i++) {
            FormPcHistory formPcHistory = (FormPcHistory) page.getRecords().get(i);
            OsUserDto osUserDto = osUserClient.getById(formPcHistory.getCreateBy());
            if(BeanUtil.isNotEmpty(osUserDto)){
                formPcHistory.setCreateName(osUserDto.getFullName());
            }
        }
    }

}

