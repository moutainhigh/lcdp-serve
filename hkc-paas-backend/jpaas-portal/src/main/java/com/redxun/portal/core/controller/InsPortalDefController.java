package com.redxun.portal.core.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.IUser;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.base.search.QueryFilter;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.DateUtils;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ContextUtil;
import com.redxun.common.utils.DbLogicDelete;
import com.redxun.log.annotation.AuditLog;
import com.redxun.log.model.Audit;
import com.redxun.log.model.LogContext;
import com.redxun.portal.context.IColumnDataService;
import com.redxun.portal.context.InsColumnDefHandlerContext;
import com.redxun.portal.context.calendar.CalendarDataService;
import com.redxun.portal.context.calendar.entity.CalendarData;
import com.redxun.portal.core.entity.InsColumnDef;
import com.redxun.portal.core.entity.InsColumnTemp;
import com.redxun.portal.core.entity.InsPortalDef;
import com.redxun.portal.core.service.InsColumnDefServiceImpl;
import com.redxun.portal.core.service.InsColumnTempServiceImpl;
import com.redxun.portal.core.service.InsMsgboxDefServiceImpl;
import com.redxun.portal.core.service.InsPortalDefServiceImpl;
import com.redxun.portal.feign.OsGroupClient;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@RestController
@RequestMapping("/portal/core/insPortalDef")
@ClassDefine(title = "门户定义",alias = "insPortalDefController",path = "/portal/core/insPortalDef",packages = "core",packageName = "门户管理")
@Api(tags = "门户定义")
public class InsPortalDefController extends BaseController<InsPortalDef> {

    @Autowired
    InsPortalDefServiceImpl insPortalDefService;

    @Autowired
    InsColumnDefServiceImpl insColumnDefService;

    @Autowired
    InsColumnTempServiceImpl insColumnTempService;

    @Autowired
    InsMsgboxDefServiceImpl insMsgboxDefService;

    @Autowired
    OsGroupClient osGroupClient;

    @Autowired
    CalendarDataService calendarDataService;


    @Override
    public BaseService getBaseService() {
        return insPortalDefService;
    }

    @Override
    public String getComment() {
        return "门户定义";
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
     * 根据条件查询月份/日期对应我的日程
     */
    @MethodDefine(title = "根据月份日程数据", path = "/getCalendarMonthData", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "月份日程", varName = "calendarValue")})
    @ApiOperation(value = "根据月份日程数据")
    @GetMapping("/getCalendarMonthData")
    public JsonResult getCalendarMonthData(@RequestParam(value = "calendarValue") String calendarValue) {
        if (StringUtils.isEmpty(calendarValue)) {
            calendarValue= DateUtils.getCurrentMonth();
        }
        List<String> list = calendarDataService.getCalendarMonthData(calendarValue);
        return JsonResult.Success().setData(list);
    }

    /**
     * 根据条件查询月份/日期对应我的日程
     */
    @MethodDefine(title = "根据条件查询月份/日期对应我的日程", path = "/getMonthOrDayData", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "月份/日期", varName = "params")})
    @ApiOperation(value = "根据条件查询月份/日期对应我的日程")
    @PostMapping("/getMonthOrDayData")
    public JsonResult getMonthOrDayData(@RequestBody JSONObject params) {
        String calendarMode = params.getString(CalendarData.CALENDAR_MODE);
        String calendarValue = params.getString(CalendarData.CALENDAR_VALUE);
        if (StringUtils.isEmpty(calendarMode)) {
            return JsonResult.Success().setData(new ArrayList<>());
        }
        List<CalendarData> listData=calendarDataService.getMonthOrDayData(calendarMode,calendarValue);
        return JsonResult.Success().setData(listData);
    }


    /**
     * 根据栏目Id获取数据
     */
    @MethodDefine(title = "根据栏目Id获取数据", path = "/getDataByColId", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "栏目Id", varName = "colId")})
    @ApiOperation(value = "根据栏目Id获取数据")
    @GetMapping("/getDataByColId")
    public JsonResult getDataByColId(@RequestParam(value = "colId") String colId) {
        if (StringUtils.isEmpty(colId)) {
            return JsonResult.Success().setData(new ArrayList<>());
        }
        InsColumnDef insColumnDef = insColumnDefService.get(colId);
        if (insColumnDef == null) {
            return JsonResult.Success().setData(new ArrayList<>());
        }

        InsColumnTemp insColumnTemp = insColumnTempService.get(insColumnDef.getType());
        if (insColumnTemp == null) {
            return JsonResult.Success().setData(new ArrayList<>());
        }

        IColumnDataService dataService =InsColumnDefHandlerContext.getHandler(insColumnTemp.getTempType());
        if (BeanUtil.isEmpty(dataService)) {
            return JsonResult.Success();
        }

        dataService.setSettingValue(insColumnDef.getSetTing(), colId);
        Object data = dataService.getData();
        return JsonResult.Success().setData(data);
    }

    /**
     * 根据门户Id获取门户布局数据
     */
    @MethodDefine(title = "根据门户Id获取门户布局数据", path = "/getLayoutListByPortalId", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "门户Id", varName = "portalId")})
    @ApiOperation(value = "根据门户Id获取门户布局数据")
    @GetMapping("/getLayoutListByPortalId")
    public JsonResult getLayoutListByPortalId(@RequestParam(value = "portalId", required = false) String portalId) {
        if (StringUtils.isEmpty(portalId)) {
            return JsonResult.Success();
        }
        InsPortalDef insPortalDef = insPortalDefService.get(portalId);
        return JsonResult.Success().setData(getByInsPortalDef(insPortalDef));
    }

    @Override
    protected JsonResult beforeSave(InsPortalDef ent) {
        boolean isExist= insPortalDefService.isExist(ent);
        if(isExist){
            return  JsonResult.Fail("标识键已存在!");
        }
        return  JsonResult.Success();
    }

    /**
     * 根据门户Id获取门户布局数据
     */
    @MethodDefine(title = "根据门户Key获取门户布局数据", path = "/getLayoutListByPortalKey", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "门户Key", varName = "portalKey")})
    @ApiOperation(value = "根据门户Key获取门户布局数据")
    @GetMapping("/getLayoutListByPortalKey")
    public JsonResult getLayoutListByPortalKey(@RequestParam(value = "portalKey", required = false) String portalKey) {
        if (StringUtils.isEmpty(portalKey)) {
            return JsonResult.Success();
        }
        InsPortalDef insPortalDef = insPortalDefService.getByKey(portalKey);
        return JsonResult.Success().setData(getByInsPortalDef(insPortalDef));
    }

    /**
     * 根据当前登陆用户获取门户布局数据
     */
    @MethodDefine(title = "根据当前登录用户获取门户数据", path = "/getLayoutListByLoginUser", method = HttpMethodConstants.GET)
    @ApiOperation(value = "根据当前登录用户获取门户数据")
    @GetMapping("/getLayoutListByLoginUser")
    public JsonResult getLayoutListByLoginUser() {
        IUser user = ContextUtil.getCurrentUser();
        if (user == null || BeanUtil.isEmpty(user.getUserId())) {
            return JsonResult.Success();
        }
        InsPortalDef insPortalDef = getOwnInsColumnDef();
        return JsonResult.Success().setData(getByInsPortalDef(insPortalDef));
    }

    /**
     * 获取门户布局数据
     *
     * @return
     */
    private JSONArray getByInsPortalDef(InsPortalDef insPortalDef) {
        if (BeanUtil.isEmpty(insPortalDef) || StringUtils.isEmpty(insPortalDef.getLayoutJson())) {
            return new JSONArray();
        }

        JSONArray newJsonArray = new JSONArray();
        String layouJson = insPortalDef.getLayoutJson();
        JSONArray jsonArray = (JSONArray) JSONArray.parse(layouJson);
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject layout = jsonArray.getJSONObject(i);
            JSONObject config=layout.getJSONObject("config");
            String colId = config.getString("colId");
            String defConf = layout.getString("defConf");
            if(!"column".equals(defConf)){
                newJsonArray.add(layout);
                continue;
            }
            InsColumnDef insColumnDef = insColumnDefService.get(colId);
            if (insColumnDef == null) {
                continue;
            }

            InsColumnTemp insColumnTemp = insColumnTempService.get(insColumnDef.getType());
            if (insColumnTemp == null || StringUtils.isEmpty(insColumnTemp.getTempType())) {
                continue;
            }

            insColumnDef.setTypeName(insColumnTemp.getTempType());
            layout.put("insColumnDef", insColumnDef);
            newJsonArray.add(layout);
        }
        return newJsonArray;
    }

    private InsPortalDef getOwnInsColumnDef() {
        IUser user = ContextUtil.getCurrentUser();
        Map<String, Set<String>> profiles = osGroupClient.getCurrentProfile(user.getUserId());
        List<InsPortalDef> owerAll = insPortalDefService.getByOwner(profiles);
        if (BeanUtil.isNotEmpty(owerAll)) {
            List<InsPortalDef> sortedEmp = owerAll.stream().sorted(
                    Comparator.comparing(InsPortalDef::getPriority).reversed()).collect(Collectors.toList());
            return sortedEmp.get(0);
        }
        return getPriorityPortal();
    }

    /**
     * 计算优先级最高的布局
     *
     * @return
     */
    public InsPortalDef getPriorityPortal() {
        List<InsPortalDef> defaultPortals = insPortalDefService.getListByType(InsPortalDef.IS_NO_MOBILE, InsPortalDef.IS_DEFAULT_,ContextUtil.getCurrentTenantId(),"");
        //没有权限门户  就取默认门户
        if (BeanUtil.isNotEmpty(defaultPortals)) {
            return defaultPortals.get(0);
        } else {
            return new InsPortalDef();
        }
    }

    /**
     * 根据门户ID获取对应布局设计列表
     *
     * @param portId
     * @return
     */
    @MethodDefine(title = "根据门户ID获取对应布局设计列表", path = "/getListByPortalId", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "门户Id", varName = "portId")})
    @ApiOperation(value = "根据门户ID获取对应布局设计列表", notes = "根据门户ID获取对应布局设计列表")
    @GetMapping("/getListByPortalId")
    public JsonResult getListByPortalId(@RequestParam(value = "portId") String portId) {
        if (BeanUtil.isEmpty(portId)) {
            return JsonResult.Success();
        }
        InsPortalDef insPortalDef = insPortalDefService.get(portId);
        if (insPortalDef != null && StringUtils.isNotEmpty(insPortalDef.getLayoutJson())) {
            return JsonResult.Success().setData(insPortalDef.getLayoutJson());
        }
        return JsonResult.Success();
    }

    @MethodDefine(title = "保存布局设计", path = "/saveAll", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "列表数据", varName = "data")})
    @ApiOperation(value = "保存布局设计", notes = "保存列表数据")
    @AuditLog(operation = "保存布局设计")
    @PostMapping("/saveAll")
    public JsonResult<String> saveAll(@RequestBody JSONObject data) {
        String portId = data.getString("portId");
        String name = data.getString("name");
        String alias = data.getString("alias");
        if (StringUtils.isEmpty(portId)) {
            String detail="保存失败：门户ID值为空！";
            LogContext.addError(detail);
            return JsonResult.Fail(detail);
        }


        JSONArray jsonList = data.getJSONArray("layoutList");
        InsPortalDef insPortalDef = insPortalDefService.get(portId);
        if (insPortalDef != null) {
            insPortalDef.setName(name);
            insPortalDef.setKey(alias);
            insPortalDef.setLayoutJson(jsonList.toJSONString());
            insPortalDefService.update(insPortalDef);
        }

        String detail="保存布局设计:"+ insPortalDef.getName() +"("+portId+")";
        LogContext.put(Audit.DETAIL,detail);

        return JsonResult.Success().setMessage("保存成功！");
    }

    @ApiOperation("根据当前登录用户获取手机门户数据")
    @MethodDefine(title = "根据当前登录用户获取手机门户数据", path = "/listMobilePortals", method = HttpMethodConstants.POST)
    @RequestMapping(value = "listMobilePortals",method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public JsonResult<List<InsPortalDef>> mobilePortalHtml() throws Exception {
        IUser user = ContextUtil.getCurrentUser();
        Map<String, Set<String>> profiles = osGroupClient.getCurrentProfile(user.getUserId());
        List<InsPortalDef> owerAll = insPortalDefService.listMobilePortals(profiles);
        if (BeanUtil.isNotEmpty(owerAll)) {
            return JsonResult.Success().setData(owerAll);
        }
        List<InsPortalDef> defaultPortals = insPortalDefService.getListByType(InsPortalDef.IS_MOBILE, "1",ContextUtil.getCurrentTenantId(),"");
        //没有权限门户  就取默认门户
        if (BeanUtil.isNotEmpty(defaultPortals)) {
            return JsonResult.Success().setData(defaultPortals);
        }
        return JsonResult.Success().setData(new ArrayList());
    }

    @MethodDefine(title = "根据应用ID获取当前人门户", path = "/getCurUserPortalByAppId", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "应用ID", varName = "appId")})
    @ApiOperation(value = "根据应用ID获取当前人门户", notes = "根据应用ID获取当前人门户")
    @GetMapping("/getCurUserPortalByAppId")
    public JsonResult getCurUserPortalByAppId(@RequestParam(value = "appId") String appId) {
        JsonResult jsonResult=new JsonResult();
        InsPortalDef insPortalDef=insPortalDefService.getCurUserPortalByAppId(appId);
        jsonResult.setData(insPortalDef);
        return jsonResult.setShow(false);
    }

}
