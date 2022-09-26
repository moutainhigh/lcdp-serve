package com.redxun.system.core.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.exception.NacosException;
import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.base.search.QueryFilter;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ContextUtil;
import com.redxun.common.utils.DbLogicDelete;
import com.redxun.log.annotation.AuditLog;
import com.redxun.log.model.Audit;
import com.redxun.log.model.LogContext;
import com.redxun.log.util.EntityUtil;
import com.redxun.system.core.entity.SysRouting;
import com.redxun.system.core.service.SysRouteTypeServiceImpl;
import com.redxun.system.core.service.SysRoutingServiceImpl;
import com.redxun.utils.OtherConfigUtils;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("/system/core/sysRouting")
@ClassDefine(title = "路由管理",alias = "sysRoutingController",path = "/system/core/sysRouting",packages = "core",packageName = "系统管理")
@Api(tags = "路由")
public class SysRoutingController extends BaseController<SysRouting> {

    private final String JPAAS_CONFIG = "scg-routes";
    private final String DEFAULT_GROUP = "SCG_GATEWAY";

    @Autowired
    SysRoutingServiceImpl sysRoutingService;

    @Autowired
    SysRouteTypeServiceImpl sysRouteTypeService;

//    @Autowired
//    private ConfigService configService;

    @Override
    public BaseService getBaseService() {
        return sysRoutingService;
    }

    @Override
    public String getComment() {
        return "路由";
    }

    @Override
    protected void handleFilter(QueryFilter filter) {
        String tenantId= ContextUtil.getCurrentTenantId();
        filter.addQueryParam("Q_TENANT_ID__S_IN",tenantId +",0");
        //逻辑删除
        if (DbLogicDelete.getLogicDelete()) {
            filter.addQueryParam("Q_DELETED__S_EQ","0");
        }
    }

    /**
     * 逻辑删除
     * @param ids
     * @return
     */
    @MethodDefine(title = "根据主键ID删除记录", path = "/del", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "实体ID", varName = "ids")})
    @ApiOperation(value="根据主键ID删除记录", notes="根据主键ID删除记录,parameters is {ids:'1,2'}")
    @AuditLog(operation = "根据主键ID删除记录")
    @PostMapping("del")
    @Override
    public JsonResult del(@RequestParam(value = "ids") String ids){

        if(StringUtils.isEmpty(ids)){
            return new JsonResult(false,"");
        }
        String[] aryId=ids.split(",");
        List<String> list= Arrays.asList(aryId);

        JsonResult rtn= beforeRemove(list);
        if(!rtn.isSuccess()){
            return rtn;
        }
        JsonResult result=JsonResult.getSuccessResult("删除"+getComment()+"成功!");
        // 写入日志
        LogContext.put(Audit.OPERATION,"删除"+getComment());

        String detail="";
        LogContext.put(Audit.ACTION,Audit.ACTION_DEL);

        for (Object id: list) {
            String idStr=id.toString();
            SysRouting ent=sysRoutingService.get(idStr);
            if(ent==null){
                continue;
            }
            String fieldInfo= EntityUtil.getInfo(ent,false);
            detail+=fieldInfo +"\r\n";
        }
        LogContext.put(Audit.DETAIL,detail);

        sysRoutingService.deleteByRoutIds(list);
        return result;
    }

    @MethodDefine(title = "新增路由", path = "/saveRouting", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "路由数据", varName = "sysRoutin")})
    @ApiOperation(value = "新增路由")
    @AuditLog(operation = "新增路由")
    @PostMapping("/saveRouting")
    public JsonResult saveRouting(@RequestBody SysRouting sysRoutin, BindingResult validResult) throws Exception {
        String detail=JSONObject.toJSONString(sysRoutin);
        LogContext.put(Audit.DETAIL,detail);
        if (StringUtils.isEmpty(sysRoutin.getId())) {
            return save(sysRoutin,validResult);
        } else {
            getBaseService().insert(sysRoutin);
            JsonResult jsonResult=JsonResult.getSuccessResult("添加"+getComment()+"成功!");
            jsonResult.setData(sysRoutin);
            return jsonResult;
        }
    }

    @MethodDefine(title = "根据类型获取路由列表", path = "/getRoutingByType", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "路由类型", varName = "routeType")})
    @ApiOperation(value = "根据类型获取路由列表")
    @PostMapping("/getRoutingByType")
    public JsonResult getRoutingByType(@RequestBody String routeType) throws Exception {
      try {
          JsonResult jsonResult=JsonResult.getSuccessResult("获取成功").setShow(false).setSuccess(true);;
          List<SysRouting> routingList=sysRoutingService.getRoutingByType(routeType);
          jsonResult.setData(routingList);
          return jsonResult;
      }catch (Exception e){
          return  new JsonResult().setSuccess(false).setMessage("获取失败");
      }
    }

    @MethodDefine(title = "更新路由", path = "/updateRouting", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "路由数据", varName = "sysRoutin")})
    @ApiOperation(value = "更新路由")
    @AuditLog(operation = "更新路由")
    @PostMapping("/updateRouting")
    public JsonResult updateRouting(@RequestBody SysRouting sysRoutin) throws Exception {
        String detail=JSONObject.toJSONString(sysRoutin);
        LogContext.put(Audit.DETAIL,detail);

        getBaseService().update(sysRoutin);
        JsonResult jsonResult=JsonResult.getSuccessResult("更新"+getComment()+"成功!");
        jsonResult.setData(sysRoutin);
        return jsonResult;
    }

    @MethodDefine(title = "获得路由参数", path = "/getConfig", method = HttpMethodConstants.GET)
    @ApiOperation(value = "获得路由参数")
    @GetMapping(value = "getConfig")
    public JsonResult getConfig() throws NacosException {
        String config = OtherConfigUtils.getFileContent(JPAAS_CONFIG);
        JsonResult jsonResult= JsonResult.Success().setData(JSON.parseArray(config));
        jsonResult.setShow(false);
        return jsonResult;
    }

    @MethodDefine(title = "发布路由", path = "/publish", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "路由参数", varName = "jsonObject")})
    @ApiOperation(value = "发布路由")
    @AuditLog(operation = "发布路由")
    @PostMapping(value = "publish")
    public void publish(@RequestBody JSONObject jsonObject) throws NacosException {
        String config=jsonObject.getString("config");
        LogContext.put(Audit.DETAIL,"将路由发布到nacos:"+config);
//        configService.publishConfig(JPAAS_CONFIG, DEFAULT_GROUP, config);
    }

}
