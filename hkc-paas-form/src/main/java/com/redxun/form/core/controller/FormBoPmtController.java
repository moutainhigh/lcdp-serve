package com.redxun.form.core.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.JsonPageResult;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.base.entity.QueryData;
import com.redxun.common.base.search.QueryFilter;
import com.redxun.common.base.search.QueryFilterBuilder;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.dto.SysMenuDto;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ContextUtil;
import com.redxun.common.utils.ExceptionUtil;
import com.redxun.feign.SysMenuClient;
import com.redxun.feign.sys.SystemClient;
import com.redxun.form.core.entity.FormBoList;
import com.redxun.form.core.entity.FormBoPmt;
import com.redxun.form.core.entity.FormCalendarView;
import com.redxun.form.core.service.FormBoListServiceImpl;
import com.redxun.form.core.service.FormBoPmtServiceImpl;
import com.redxun.form.core.service.FormCalendarViewServiceImpl;
import com.redxun.log.annotation.AuditLog;
import com.redxun.log.model.Audit;
import com.redxun.log.model.LogContext;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@Slf4j
@RestController
@RequestMapping("/form/core/formBoPmt")
@ClassDefine(title = "业务单据数据权限",alias = "formBoPmtController",path = "/form/core/formBoPmt",packages = "core",packageName = "表单管理")
@Api(tags = "业务单据数据权限")
public class FormBoPmtController extends BaseController<FormBoPmt> {

    @Autowired
    FormBoPmtServiceImpl formBoPmtService;
    @Autowired
    FormBoListServiceImpl formBoListService;
    @Autowired
    FormCalendarViewServiceImpl formCalendarViewService;

    @Override
    public BaseService getBaseService() {
        return formBoPmtService;
    }

    @Override
    public String getComment() {
        return "业务单据数据权限";
    }


    /**
     * 重写父类的get方法
     * @return
     * @throws Exception
     */
    @ApiOperation(value="根据id查询记录", notes="根据id查询记录")
    @GetMapping(value="/get")
    @Override
    public JsonResult get(@RequestParam("pkId") String pkId){
        JsonResult result = JsonResult.Success();
        if (StringUtils.isEmpty(pkId)) {
            return result.setData(new Object());
        } else {
            FormBoPmt fbp = formBoPmtService.get(pkId);
            if(fbp != null){
                FormBoList fbl =  formBoListService.get(fbp.getBoListId());
                if(fbl != null){
                    fbp.setBoListName(fbl.getName());
                }
            }
            return result.setData(fbp);
        }

    }

    /**
     * 重写父类的query方法
     * @return
     * @throws Exception
     */
    @ApiOperation(value="根据条件查询业务数据记录", notes="根据条件查询业务数据记录")
    @PostMapping({"/query"})
    @Override
    public JsonPageResult query(@RequestBody QueryData queryData) throws Exception {
        JsonPageResult jsonResult = JsonPageResult.getSuccess("成功!");

        try {

            QueryFilter filter = QueryFilterBuilder.createQueryFilter(queryData);
            handleFilter(filter);
            IPage<FormBoPmt> page = formBoPmtService.query(filter);
            if(page != null && page.getSize() > 0){
                for(FormBoPmt formBoPmt : page.getRecords()){
                    FormBoList fbl =  formBoListService.get(formBoPmt.getBoListId());
                    if(fbl != null){
                        formBoPmt.setBoListName(fbl.getName());
                    }
                }
            }
            jsonResult.setPageData(page);
        } catch (Exception e) {
            jsonResult.setSuccess(false);
            logger.error(ExceptionUtil.getExceptionMessage(e));
            jsonResult.setMessage(ExceptionUtil.getExceptionMessage(e));

        }

        return jsonResult;
    }

    /**
     * 重写父类的del方法
     * @return
     * @throws Exception
     */
    @ApiOperation(value="根据实体Id删除业务行信息", notes="根据实体Id删除业务行信息")
    @GetMapping({"/del"})
    @Override
    public JsonResult del(@RequestParam("id") String id) {
        if (StringUtils.isEmpty(id)) {
            return new JsonResult(false, "");
        } else {
            JsonResult result = JsonResult.getSuccessResult("删除成功!");
            this.formBoPmtService.deleteById(id);
            return result;
        }
    }


    /**
     * 根据boListId以及groupId获取列表记录
     * @return
     * @throws Exception
     */
    @ApiOperation(value="根据boListId以及groupId获取列表记录", notes="根据boListId以及groupId获取列表记录")
    @GetMapping(value="/getList")
    public JsonResult getList(@RequestParam("boListId") String boListId){
        JsonResult result = JsonResult.Success();
        if (StringUtils.isEmpty(boListId)) {
            return result.setData(new Object());
        } else {
            FormBoList formBoList=formBoListService.get(boListId);
            List<FormBoPmt> list = formBoPmtService.getList(boListId, ContextUtil.getCurrentTenantId());
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("list",list);
            jsonObject.put("boListObj",formBoList);
            return result.setData(jsonObject);
        }
    }

    @GetMapping(value = "/getListByKey")
    public JsonResult getListByKey(@RequestParam("boListKey")String boListKey){
        JsonResult result = JsonResult.Success();
        if (StringUtils.isEmpty(boListKey)) {
            return result.setData(new Object());
        } else {
            FormBoList formBoList=formBoListService.getByKey(boListKey);
            List<FormBoPmt> list = formBoPmtService.getList(formBoList.getId(), ContextUtil.getCurrentTenantId());
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("list",list);
            jsonObject.put("boListObj",formBoList);
            return result.setData(jsonObject);
        }
    }

    /**
     * 根据groupId获取数据列表记录
     * @return
     * @throws Exception
     */
    @ApiOperation(value="根据groupId获取数据列表记录", notes="根据groupId获取数据列表记录")
    @GetMapping(value="/getBoList")
    public JsonResult getBoList(@RequestParam("groupId") String groupId){
        JsonResult result = JsonResult.Success();
        return result;
    }


    /**
     * 根据boListId获取权限数据
     * @return
     * @throws Exception
     */
    @ApiOperation(value="根据boListId获取权限数据", notes="根据boListId获取权限数据")
    @GetMapping(value="/getCalendarPmt")
    public JsonResult getCalendarPmt(@RequestParam("boListId") String boListId){
        JsonResult result = JsonResult.Success();
        if (StringUtils.isEmpty(boListId)) {
            return result.setData(new Object());
        } else {
            FormCalendarView formCalendarView=formCalendarViewService.get(boListId);
            List<FormBoPmt> list = formBoPmtService.getList(boListId, ContextUtil.getCurrentTenantId());
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("list",list);
            jsonObject.put("formCalendarView",formCalendarView);
            return result.setData(jsonObject);
        }

    }
}
