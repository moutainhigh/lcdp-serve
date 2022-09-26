package com.redxun.system.core.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.GridHeader;
import com.redxun.common.base.entity.JsonPageResult;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.base.search.QueryFilter;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.constant.SysConstant;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.utils.ContextUtil;
import com.redxun.common.utils.DbLogicDelete;
import com.redxun.common.utils.ExceptionUtil;
import com.redxun.common.utils.RequestUtil;
import com.redxun.datasource.DataSourceContextHolder;
import com.redxun.dto.user.OsUserDto;
import com.redxun.log.annotation.AuditLog;
import com.redxun.log.model.LogContext;
import com.redxun.system.core.entity.SysTransferSetting;
import com.redxun.system.core.service.SysTransferSettingServiceImpl;
import com.redxun.system.feign.OsUserClient;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/system/core/sysTransferSetting")
@ClassDefine(title = "权限转移设置表",alias = "sysTransferSettingController",path = "/system/core/sysTransferSetting",packages = "core",packageName = "系统管理")
@Api(tags = "权限转移设置表")
public class SysTransferSettingController extends BaseController<SysTransferSetting> {
    @Autowired
    SysTransferSettingServiceImpl sysTransferSettingServiceImpl;
    @Autowired
    OsUserClient osUserClient;


    @Override
    protected void handleFilter(QueryFilter filter) {
        String tenantId=ContextUtil.getCurrentTenantId();

        filter.addQueryParam("Q_TENANT_ID__S_IN",tenantId +",0");
        //逻辑删除
        if (DbLogicDelete.getLogicDelete()) {
            filter.addQueryParam("Q_DELETED__S_EQ","0");
        }
    }

    @Override
    public BaseService getBaseService() {
        return sysTransferSettingServiceImpl;
    }

    @Override
    public String getComment() {
        return "权限转移设置表";
    }

    @MethodDefine(title = "获取有效的权限转移设置", path = "/jsonAll", method = HttpMethodConstants.GET)
    @ApiOperation(value = "获取有效的权限转移设置")
    @GetMapping("jsonAll")
    public List<SysTransferSetting> jsonAll() {
        List<SysTransferSetting> setList = sysTransferSettingServiceImpl.getInvailAll();
        return setList;
    }

    @MethodDefine(title = "计算查询SQL，返回表格列", path = "/excuteSelectSql", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "请求数据", varName = "request")})
    @ApiOperation(value = "计算查询SQL，返回表格列")
    @AuditLog(operation = "计算查询SQL，返回表格列")
    @GetMapping("excuteSelectSql")
    public JSONObject excuteSelectSql(HttpServletRequest request) {
        String id = RequestUtil.getString(request, "id");
        String authorId = RequestUtil.getString(request, "authorId");
        if (id == null) {
            LogContext.addError("id 没有传入");
            return null;
        }
        OsUserDto author = osUserClient.getById(authorId);
        JSONObject json = new JSONObject();
        try {
            SysTransferSetting sysTransDef = sysTransferSettingServiceImpl .get(id);


            String ds = sysTransDef.getDsAlias();
            DataSourceContextHolder.setDataSource(ds);
            List<Map<String, Object>> list = sysTransferSettingServiceImpl.excuteSelectSql(sysTransDef, author);

            Map<String, Object> mapList;
            if (BeanUtil.isEmpty(list)) {
                mapList = new HashMap<>(SysConstant.INIT_CAPACITY_16);
            } else {
                mapList = list.get(0);
            }
            json.put("id", sysTransDef.getId());
            json.put("name", sysTransDef.getName());
            JSONArray resultList = new JSONArray();
            for (String dataMap : mapList.keySet()) {
                JSONObject obj = new JSONObject();
                obj.put("dataIndex", dataMap);
                obj.put("title", dataMap.toLowerCase());
                obj.put("width", "120");
                resultList.add(obj);
            }
            json.put("columns", resultList);
        } catch (Exception e) {
            String detail=ExceptionUtil.getExceptionMessage(e);
            LogContext.addError("执行SQL失败:"+ detail);
        } finally {
            DataSourceContextHolder.setDefaultDataSource();
        }
        return json;
    }

    @MethodDefine(title = "计算查询SQL，返回表格数据", path = "/excuteSelectSqlData", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "请求数据", varName = "request")})
    @AuditLog(operation = "计算查询SQL，返回表格数据")
    @ApiOperation(value = "计算查询SQL，返回表格数据")
    @PostMapping("excuteSelectSqlData")
    public JsonPageResult excuteSelectSqlData(HttpServletRequest request) {
        String id = RequestUtil.getString(request, "id");
        String authorId = RequestUtil.getString(request, "authorId");
        JsonPageResult result1 = JsonPageResult.getSuccess("");
        if (id == null) {
            LogContext.addError("id 没有传入");
            return result1;
        }
        OsUserDto author = osUserClient.getById(authorId);
        try {
            SysTransferSetting sysTransDef = sysTransferSettingServiceImpl.get(id);
            String ds = sysTransDef.getDsAlias();
            DataSourceContextHolder.setDataSource(ds);
            List<Map<String, Object>> list = sysTransferSettingServiceImpl.excuteSelectSql(sysTransDef, author);



            /**oracle 字段改成小写 */
            List<Map<String, Object>> result = new ArrayList<>();
            for (Map<String, Object> dataMap : list) {
                Map<String, Object> resultMap = new HashMap<>(SysConstant.INIT_CAPACITY_16);
                for (String key : dataMap.keySet()) {
                    resultMap.put(key, dataMap.get(key) == null ? "" : dataMap.get(key));
                }
                result.add(resultMap);
            }
            result1 = new JsonPageResult(new Page(1, 20).setRecords(result).setTotal(result.size()));
            result1.setSuccess(true);
        } catch (Exception e) {
            result1.setSuccess(false);
            String detail=ExceptionUtil.getExceptionMessage(e);

            LogContext.addError("执行SQL失败:" +detail);

            result1.setMessage("执行SQL失败!");
        } finally {
            DataSourceContextHolder.setDefaultDataSource();
        }
        return result1;
    }

    @MethodDefine(title = "计算更新SQL", path = "/excuteUpdateSql", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "请求数据", varName = "jsonObject")})
    @ApiOperation(value = "计算更新SQL")
    @AuditLog(operation = "计算更新SQL")
    @PostMapping("excuteUpdateSql")
    public JsonResult excuteUpdateSql(@RequestBody JSONObject jsonObject) {
        String id = jsonObject.getString("id");
        String authorId = jsonObject.getString("authorId");
        String targetPersonId = jsonObject.getString("targetPersonId");
        String selectedItem = jsonObject.getString("selectedItem");

        OsUserDto author = osUserClient.getById(authorId);
        OsUserDto targetPerson = osUserClient.getById(targetPersonId);

        SysTransferSetting sysTransDef = sysTransferSettingServiceImpl.get(id);

        try {
            sysTransferSettingServiceImpl.excuteUpdateSql(sysTransDef, author, targetPerson, selectedItem);
            return new JsonResult(true, "操作成功!");
        } catch (Exception e) {
            String message= ExceptionUtil.getExceptionMessage(e);
            LogContext.addError("执行出错:" + message);
            return new JsonResult(false, "操作失败：" +message);
        } finally {
            DataSourceContextHolder.setDefaultDataSource();
        }
    }

    @MethodDefine(title = "执行SQL", path = "/onRun", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "请求数据", varName = "request")})
    @ApiOperation(value = "执行SQL")
    @AuditLog(operation = "执行SQL")
    @PostMapping("/onRun")
    public JsonResult onRun(HttpServletRequest request) throws Exception {
        String ds = RequestUtil.getString(request, "ds");
        String selectSql = RequestUtil.getString(request, "selectSql");
        JSONObject result = new JSONObject();
        try {
            DataSourceContextHolder.setDataSource(ds);

            List<GridHeader> headers = sysTransferSettingServiceImpl.onRun(selectSql);
            result.put("fields", headers);
        } catch (Exception e) {
            String message="执行SQL失败:" + ExceptionUtil.getExceptionMessage(e);
            LogContext.addError(message);
            return new JsonResult(false, message);
        } finally {
            DataSourceContextHolder.setDefaultDataSource();
        }
        return new JsonResult(true, result, "成功执行SQL").setShow(false);
    }

}
