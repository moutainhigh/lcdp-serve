package com.redxun.system.core.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.ContextQuerySupport;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.constant.MBoolean;
import com.redxun.common.tool.BeanUtil;
import com.redxun.system.core.entity.SysAppLog;
import com.redxun.system.core.entity.SysAuthManager;
import com.redxun.system.core.service.SysAppLogServiceImpl;
import com.redxun.system.core.service.SysAuthManagerService;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/system/core/sysAppLog")
@ClassDefine(title = "应用授权接口日志表",alias = "sysAppLogController",path = "/system/core/sysAppLog",packages = "core",packageName = "系统管理")
@Api(tags = "应用授权接口日志表")
@ContextQuerySupport(tenant = ContextQuerySupport.NONE,company = ContextQuerySupport.NONE)
public class SysAppLogController extends BaseController<SysAppLog> {

    @Autowired
    SysAppLogServiceImpl sysAppLogService;
    @Autowired
    SysAuthManagerService sysAuthManagerService;

    @Override
    public BaseService getBaseService() {
        return sysAppLogService;
    }

    @Override
    public String getComment() {
        return "应用授权接口日志表";
    }

    @Override
    protected JsonResult beforeSave(SysAppLog ent) {
        SysAuthManager sysAuthManager=sysAuthManagerService.get(ent.getAppId());
        //是否记录日志
        if(sysAuthManager!=null && MBoolean.TRUE_LOWER.val.equals(sysAuthManager.getIsLog())){
            //设置机构ID
            ent.setTenantId(sysAuthManager.getTenantId());
            return super.beforeSave(ent);
        }
        return JsonResult.Fail();
    }

    @Override
    protected void handlePage(IPage page) {
        List<SysAppLog> list = page.getRecords();
        for(SysAppLog sysAppLog : list){
            SysAuthManager sysAuthManager=sysAuthManagerService.get(sysAppLog.getAppId());
            if(BeanUtil.isNotEmpty(sysAuthManager)){
                sysAppLog.setAppName(sysAuthManager.getName());
            }
        }
    }

    @Override
    protected void handleData(SysAppLog ent) {
        SysAuthManager sysAuthManager=sysAuthManagerService.get(ent.getAppId());
        if(BeanUtil.isNotEmpty(sysAuthManager)){
            ent.setAppName(sysAuthManager.getName());
        }
    }
}
