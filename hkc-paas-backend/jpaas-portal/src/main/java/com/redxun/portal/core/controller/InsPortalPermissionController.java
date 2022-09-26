package com.redxun.portal.core.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.redxun.api.org.IOrgService;
import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.tool.StringUtils;
import com.redxun.log.annotation.AuditLog;
import com.redxun.log.model.Audit;
import com.redxun.log.model.LogContext;
import com.redxun.portal.core.entity.InsPortalPermission;
import com.redxun.portal.core.service.InsPortalPermissionServiceImpl;
import com.redxun.portal.feign.OsGroupClient;
import com.redxun.portal.feign.OsUserClient;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("/portal/core/insPortalPermission")
@ClassDefine(title = "门户权限",alias = "insPortalPermissionController",path = "/portal/core/insPortalPermission",packages = "core",packageName = "门户管理")
@Api(tags = "门户权限")
public class InsPortalPermissionController extends BaseController<InsPortalPermission> {

    @Autowired
    InsPortalPermissionServiceImpl insPortalPermissionService;

    @Autowired
    OsUserClient osUserClient;

    @Autowired
    OsGroupClient osGroupClient;
    @Autowired
    IOrgService orgService;

    @Override
    public BaseService getBaseService() {
        return insPortalPermissionService;
    }

    @Override
    public String getComment() {
        return "布局权限设置";
    }

    /**
     * 根据门户ID获取对应权限设计数据
     *
     * @param layoutId
     * @return
     */
    @MethodDefine(title = "根据门户ID获取门户权限", path = "/getListByLayoutId", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "门户ID", varName = "layoutId")})
    @ApiOperation(value = "根据门户ID获取门户权限", notes = "根据门户ID获取门户权限")
    @GetMapping("/getListByLayoutId")
    public JsonResult<JSONObject> getListByLayoutId(@RequestParam(value = "layoutId") String layoutId) {

        JSONObject resultObj = new JSONObject();
        resultObj.put("type", "ALL");
        JSONArray userGroupHanderList = orgService.getOsUserGroupHanderList();
        if (StringUtils.isEmpty(layoutId)) {
            resultObj.put("typeList", userGroupHanderList);
            return JsonResult.Success().setData(resultObj);
        }
        List<InsPortalPermission> list = insPortalPermissionService.getListByLayoutId(layoutId);
        if (BeanUtil.isEmpty(list)) {
            resultObj.put("typeList", userGroupHanderList);
            return JsonResult.Success().setData(resultObj);
        }

        if (list.size() == 1 && InsPortalPermission.ALL_TYPE.equals(list.get(0).getType())) {
            resultObj.put("typeList", userGroupHanderList);
            return JsonResult.Success().setData(resultObj);
        }

        resultObj.put("type", "custom");
        getValuesAndNames(list, resultObj, userGroupHanderList);
        return JsonResult.Success().setData(resultObj);
    }


    private void getValuesAndNames(List<InsPortalPermission> list,
                                   JSONObject resultObj, JSONArray userGroupHanderList) {
        resultObj.put("typeList", userGroupHanderList);

        for (int i = 0; i < userGroupHanderList.size(); i++) {
            JSONObject jsonObject = userGroupHanderList.getJSONObject(i);
            List<String> values = new ArrayList<>();
            List<String> names = new ArrayList<>();
            for (InsPortalPermission permission : list) {
                if (jsonObject.get("type").equals(permission.getType())) {
                    values.add(permission.getOwnerId());
                    names.add(permission.getOwnerName());
                }
            }
            jsonObject.put("values", StringUtils.join(values));
            jsonObject.put("names", StringUtils.join(names));
        }
    }

    @MethodDefine(title = "保存授权数据", path = "/saveAll", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "数据", varName = "params")})
    @ApiOperation(value = "保存授权数据", notes = "保存授权数据")
    @AuditLog(operation = "保存授权数据")
    @PostMapping("/saveAll")
    public JsonResult<String> saveAll(@RequestBody JSONObject params) {
        String layoutId = params.getString("layoutId");
        String menuType = params.getString("menuType");
        String type = params.getString("type");
        if (StringUtils.isEmpty(layoutId)) {
            LogContext.addError("布局ID为空!");
            return JsonResult.Success().setData("布局ID为空!");
        }

        //删除所有，再添加
        insPortalPermissionService.delByLayoutId(layoutId);

        LogContext.put(Audit.PK,layoutId);
        LogContext.put(Audit.DETAIL,"保存布局ID:" +layoutId +"的权限数据!");

        if (InsPortalPermission.ALL_TYPE.equals(type)) {
            JSONObject object = new JSONObject();
            object.put("layoutId", layoutId);
            object.put("menuType", menuType);
            object.put("type", type);
            object.put("values", InsPortalPermission.ALL_TYPE);
            creatInsPortalPermission(object);
        } else {
            JSONArray jsonArray = params.getJSONArray("typeList");
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                if (StringUtils.isNotEmpty(object.getString("values"))) {
                    object.put("layoutId", layoutId);
                    object.put("menuType", menuType);
                    creatInsPortalPermission(object);
                }
            }
        }
        return JsonResult.Success().setData("保存成功！");
    }

    private void creatInsPortalPermission(JSONObject object) {
        String values = object.getString("values");
        String menuType = object.getString("menuType");
        String type = object.getString("type");
        String[] valueList = values.split("[,]");
        String names = object.getString("names");
        String[] nameList = null;
        if(!InsPortalPermission.ALL_TYPE.equals(type)){
            nameList = names.split("[,]");
        }

        for (int i = 0; i < valueList.length; i++) {
            InsPortalPermission portalPermission = new InsPortalPermission();
            portalPermission.setId(IdGenerator.getIdStr());
            portalPermission.setMenuType(menuType);
            portalPermission.setLayoutId(object.getString("layoutId"));
            portalPermission.setType(type);
            portalPermission.setOwnerId(valueList[i]);
            if(!InsPortalPermission.ALL_TYPE.equals(type)){
                portalPermission.setOwnerName(nameList[i]);
            }
            insPortalPermissionService.insert(portalPermission);
        }
    }
}
