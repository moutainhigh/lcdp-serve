package com.redxun.system.core.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.JsonPageResult;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.base.entity.QueryData;
import com.redxun.common.base.search.QueryFilter;
import com.redxun.common.base.search.QueryFilterBuilder;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ContextUtil;
import com.redxun.common.utils.DbLogicDelete;
import com.redxun.common.utils.ExceptionUtil;
import com.redxun.dto.user.OsInstDto;
import com.redxun.system.core.entity.SysApp;
import com.redxun.system.core.entity.SysAppInstall;
import com.redxun.system.core.service.SysAppInstallServiceImpl;
import com.redxun.system.feign.OsUserClient;
import com.redxun.util.QueryFilterUtil;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description 应用市场安装
 * @Author xtk
 * @Date 2021/11/30 18:06
 */
@Slf4j
@RestController
@RequestMapping("/system/core/sysApp/market")
@ClassDefine(title = "系统应用安装",alias = "sysAppInstallController",path = "/system/core/sysApp/market",packages = "core",packageName = "系统应用市场")
@Api(tags = "系统应用安装")
public class SysAppInstallController extends BaseController<SysAppInstall> {

    @Autowired
    SysAppInstallServiceImpl sysAppInstallServiceImpl;

    @Autowired
    OsUserClient osUserClient;

    @Override
    protected void handleFilter(QueryFilter filter) {
        String tenantId= ContextUtil.getCurrentTenantId();
        Boolean isRootTenant= OsInstDto.ROOT_INST.equals(tenantId);

        QueryFilterUtil.setQueryFilterByTreeId(filter,"CATEGORY_ID_","APP","read");

        //非根租户
        if(!isRootTenant){
            super.handleFilter(filter);
        }else if (DbLogicDelete.getLogicDelete()) {
            //逻辑删除
            filter.addQueryParam("Q_DELETED__S_EQ","0");
        }

    }


    @Override
    public BaseService getBaseService() {
        return sysAppInstallServiceImpl;
    }

    @Override
    public String getComment() {
        return "应用安装";
    }

    /**
     * @Description 获取应用市场数据
     * @Author xtk
     * @Date 2021/11/30 18:05
     * @return com.redxun.common.base.entity.JsonResult
     */
    @MethodDefine(title = "获取应用市场数据", path = "/getAppMarketData", method = HttpMethodConstants.POST)
    @PostMapping("/getAppMarketData")
    public  JsonResult getAppMarketData(){
        JsonResult result = JsonResult.Success("");
        try {
            List<SysApp> appMarketData = sysAppInstallServiceImpl.getAppMarketData();
            result.setShow(false);
            result.setData(appMarketData);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setCode(JsonResult.FAIL_CODE);
            result.setMessage(e.getMessage());
            result.setMessage("查询数据出错！");
        }
        return  result;
    }

    /**
     * @Description 获取我的应用数据
     * @Author xtk
     * @Date 2021/11/30 18:05
     * @return com.redxun.common.base.entity.JsonResult
     */
    @MethodDefine(title = "获取我的应用数据", path = "/getMyApp", method = HttpMethodConstants.POST)
    @PostMapping("/getMyApp")
    public  JsonResult getMyApp(@RequestBody SysAppInstall sysAppInstall){
        JsonResult result = JsonResult.Success("");
        // 租户ID
        String tenantId = sysAppInstall.getTenantId();
        try {
            // 获取我的应用
            List<SysApp> appMarketData = sysAppInstallServiceImpl.getMyApp(tenantId);
            result.setShow(false);
            result.setData(appMarketData);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setCode(JsonResult.FAIL_CODE);
            result.setMessage(e.getMessage());
            result.setMessage("查询数据出错！");
        }
        return  result;
    }

    /**
     * @Description 暂停或者启用
     * @Author xtk
     * @Date 2021/11/30 18:05
     * @return com.redxun.common.base.entity.JsonResult
     */
    @MethodDefine(title = "暂停或者启用", path = "/startOrStop", method = HttpMethodConstants.POST)
    @PostMapping("/startOrStop")
    public  JsonResult startOrStop(@RequestBody SysAppInstall sysAppInstall){
        JsonResult result = JsonResult.Success("操作成功！");
        try {
            sysAppInstallServiceImpl.startOrStop(sysAppInstall);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setCode(JsonResult.FAIL_CODE);
            result.setMessage(e.getMessage());
            result.setMessage("操作失败！");
        }
        return  result;
    }

    /**
     * @Description 卸载
     * @Author xtk
     * @Date 2021/11/30 18:05
     * @return com.redxun.common.base.entity.JsonResult
     */
    @MethodDefine(title = "卸载", path = "/uninstall", method = HttpMethodConstants.POST)
    @PostMapping("/uninstall")
    public  JsonResult uninstall(@RequestBody SysAppInstall sysAppInstall){
        JsonResult result = JsonResult.Success("卸载成功！");
        try {
            sysAppInstallServiceImpl.uninstall(sysAppInstall);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setCode(JsonResult.FAIL_CODE);
            result.setMessage("卸载失败！");
        }
        return  result;
    }

    /**
     * @Description 安装成功
     * @Author xtk
     * @Date 2021/11/30 18:05
     * @return com.redxun.common.base.entity.JsonResult
     */
    @MethodDefine(title = "安装", path = "/install", method = HttpMethodConstants.POST)
    @PostMapping("/install")
    public  JsonResult install(@RequestBody SysAppInstall sysAppInstall){
        // 判断是否已安装
        String message = sysAppInstallServiceImpl.isExist(sysAppInstall);
        if(StringUtils.isNotEmpty(message)){
            return JsonResult.Fail(message);
        }
        try {
            sysAppInstallServiceImpl.install(sysAppInstall);
            return JsonResult.Success("安装成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.Fail("安装失败");
        }
    }

    /**
     * @Description 获取机构列表
     * @Author xtk
     * @Date 2021/12/6 12:05
     * @return com.redxun.common.base.entity.JsonResult
     */
    @MethodDefine(title = "获取机构列表", path = "/getInstData", method = HttpMethodConstants.POST)
    @PostMapping("/getInstData")
    public  JsonResult getInstData(){
        try {
            List<OsInstDto> instData = osUserClient.ListInst("");
            JsonResult success = JsonResult.Success("获取机构列表成功！");
            success.setData(instData);
            return success;
        } catch (Exception e) {
            return JsonResult.Fail("安装失败");
        }
    }

    /**
     * @Description 获取应用安装数据
     * @Author xtk
     * @Date 2021/12/6 12:05
     * @return com.redxun.common.base.entity.JsonResult
     */
    @MethodDefine(title = "获取应用安装数据", path = "/getAppInstallData", method = HttpMethodConstants.POST)
    @PostMapping("/getAppInstallData")
    public JsonPageResult getAppInstallData(@RequestBody QueryData queryData) {
        JsonPageResult jsonResult=JsonPageResult.getSuccess("返回数据成功!");
        try{
            QueryFilter filter = QueryFilterBuilder.createQueryFilter(queryData);
            String tenantId = ContextUtil.getCurrentTenantId();
            if (!tenantId.equals("1")) {
                filter.addParam("tenantId", tenantId);
            }
            //超级管理员显示维度
            IPage page = getBaseService().query(filter);
            List<SysAppInstall> appInstallData = page.getRecords();
            List<String> tenantList = appInstallData.stream().map(item -> item.getTenantId()).distinct().collect(Collectors.toList());
            String tenantIds = StringUtils.join(tenantList, ",");
            List<OsInstDto> osInstList = osUserClient.ListInst(tenantIds);
            for (SysAppInstall appInstallDatum : appInstallData) {
                for (OsInstDto osInstDto : osInstList) {
                    if (appInstallDatum.getTenantId().equals(osInstDto.getInstId())) {
                        appInstallDatum.setTenantName(osInstDto.getNameCn());
                        break;
                    }
                }
            }
            handlePage(page);
            jsonResult.setPageData(page);
        }catch (Exception ex){
            jsonResult.setSuccess(false);
            logger.error(ExceptionUtil.getExceptionMessage(ex));
            jsonResult.setMessage(ExceptionUtil.getExceptionMessage(ex));
        }
        return jsonResult;
    }

    /**
     * @Description 根据应用ID删除应用信息
     * @Author xtk
     * @Date 2021/11/30 18:05
     * @return com.redxun.common.base.entity.JsonResult
     */
    @MethodDefine(title = "根据应用ID删除应用信息", path = "/deleteByAppIds", method = HttpMethodConstants.POST)
    @PostMapping("/deleteByTenantIds")
    public  JsonResult deleteByTenantIds(@RequestBody SysAppInstall sysAppInstall){
        JsonResult result = JsonResult.Success("删除成功！");
        try {
            sysAppInstallServiceImpl.deleteByTenantIds(sysAppInstall);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setCode(JsonResult.FAIL_CODE);
            result.setMessage("删除失败！");
        }
        return  result;
    }

}
