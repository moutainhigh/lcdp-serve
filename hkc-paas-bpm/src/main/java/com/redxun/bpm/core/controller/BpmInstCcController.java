package com.redxun.bpm.core.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.redxun.bpm.core.entity.BpmInstCc;
import com.redxun.bpm.core.service.BpmInstCcServiceImpl;
import com.redxun.bpm.core.service.BpmInstCpServiceImpl;
import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.ContextQuerySupport;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.IUser;
import com.redxun.common.base.entity.JsonPageResult;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.base.entity.QueryData;
import com.redxun.common.base.search.QueryFilter;
import com.redxun.common.base.search.QueryFilterBuilder;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.tool.constant.CommonConstant;
import com.redxun.common.utils.ContextUtil;
import com.redxun.common.utils.ExceptionUtil;
import com.redxun.util.QueryFilterUtil;
import com.redxun.web.controller.BaseController;
import com.redxun.web.util.WebUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/bpm/core/bpmInstCc")
@ClassDefine(title = "抄送处理",alias = "bpmInstCcController",path = "/bpm/core/bpmInstCc",packages = "core",packageName = "流程管理")
@Api(tags = "抄送处理")
public class BpmInstCcController extends BaseController<BpmInstCc> {

    @Autowired
    BpmInstCcServiceImpl bpmInstCcService;
    @Autowired
    BpmInstCpServiceImpl bpmInstCpService;

    @Override
    public BaseService getBaseService() {
        return bpmInstCcService;
    }

    @Override
    public String getComment() {
        return "代理配置";
    }

    @MethodDefine(title = "获取我转出的抄送", path = "/getMyTurnTo", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "查询数据", varName = "queryData")})
    @ApiOperation("获取我转出的抄送")
    @PostMapping("getMyTurnTo")
    public JsonPageResult listVars(@ApiParam @RequestBody QueryData queryData)
            throws Exception {
        JsonPageResult jsonResult=JsonPageResult.getSuccess("返回数据成功!");

        QueryFilter filter= QueryFilterBuilder.createQueryFilter(queryData);

        IUser user= ContextUtil.getCurrentUser();
        filter.addParam("userId",user.getUserId());

        WebUtil.handFilter(filter, ContextQuerySupport.CURRENT,ContextQuerySupport.NONE);

        IPage page= bpmInstCcService.getMyTurnTo(filter);

        jsonResult.setPageData(page);

        return jsonResult;

    }

    @MethodDefine(title = "获取我收到的抄送", path = "/myRecevieTurn", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "查询数据", varName = "queryData")})
    @ApiOperation(value="获取我收到的抄送", notes="根据条件查询所有记录")
    @PostMapping(value="/myRecevieTurn")
    public JsonPageResult myRecevieTurn(@RequestBody QueryData queryData) throws Exception{
        JsonPageResult jsonResult=JsonPageResult.getSuccess("返回数据成功!");

        QueryFilter filter= QueryFilterBuilder.createQueryFilter(queryData);

        IUser user= ContextUtil.getCurrentUser();
        filter.addParam("userId",user.getUserId());

        filter.addParam(CommonConstant.TENANT_PREFIX,"A.");
        WebUtil.handFilter(filter,ContextQuerySupport.CURRENT,ContextQuerySupport.NONE);

        IPage page= bpmInstCcService.getMyReceiveTurn(filter);

        jsonResult.setPageData(page);

        return jsonResult;
    }


    /**
     * 更新抄送为已读。
     * @param cpId
     */
    @MethodDefine(title = "更新为已读", path = "/updRead", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "主键", varName = "cpId")})
    @ApiOperation("更新抄送为已读")
    @PostMapping(value="/updRead")
    public void updRead(@RequestParam(value = "cpId")String cpId){
        bpmInstCpService.updReaded(cpId);
    }

    /**
     * 根据实例ID获取抄送情况。
     * @param instId
     * @return
     */
    @MethodDefine(title = "根据实例ID获取抄送情况", path = "/getByInstId", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "实例ID", varName = "instId")})
    @ApiOperation("根据实例ID获取抄送情况")
    @PostMapping(value="/getByInstId")
    public List<BpmInstCc> getByInstId(@RequestParam(value = "instId")String instId){
        List<BpmInstCc> list= bpmInstCcService.getByInstId(instId);
        return list;
    }


    @MethodDefine(title = "转发流程实例", path = "/transfer", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "json", varName = "json")})
    @ApiOperation(value = "转发流程实例")
    @PostMapping("transfer")
    public JsonResult transfer(@RequestBody JSONObject json){
        JsonResult result=bpmInstCcService.transfer(json);
        return result;
    }

    /**
     * 根据实例ID获取抄送记录。（抄送记录控件）
     * @param instId
     * @return
     */
    @MethodDefine(title = "根据实例ID获取抄送记录(抄送记录控件)", path = "/getCcRecordByInstId", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "实例ID", varName = "instId")})
    @ApiOperation("根据实例ID获取抄送记录(抄送记录控件)")
    @PostMapping(value="/getCcRecordByInstId")
    public JSONArray getCcRecordByInstId(@RequestParam(value = "instId")String instId){
        JSONArray jsonArray=bpmInstCcService.getCcRecordByInstId(instId);
        return jsonArray;
    }




}
