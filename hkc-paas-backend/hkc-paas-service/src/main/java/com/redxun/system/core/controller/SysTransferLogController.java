package com.redxun.system.core.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
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
import com.redxun.common.utils.ExceptionUtil;
import com.redxun.system.core.entity.SysTransferLog;
import com.redxun.system.core.service.SysTransferLogServiceImpl;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/system/core/sysTransferLog")
@ClassDefine(title = "权限转移日志表",alias = "sysTransferLogController",path = "/system/core/sysTransferLog",packages = "core",packageName = "系统管理")
@Api(tags = "权限转移日志表")
public class SysTransferLogController extends BaseController<SysTransferLog> {

    @Autowired
    SysTransferLogServiceImpl sysTransferLogServiceImpl;

    @Override
    public BaseService getBaseService() {
        return sysTransferLogServiceImpl;
    }

    @Override
    public String getComment() {
        return "权限转移日志表";
    }

    @MethodDefine(title = "按条件查询所有记录带用户名", path = "/queryUser", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "查询数据", varName = "queryData")})
    @ApiOperation(value = "按条件查询所有记录带用户名")
    @PostMapping(value = "/queryUser")
    public JsonPageResult queryUser(@RequestBody QueryData queryData) throws Exception {
        JsonPageResult jsonResult = JsonPageResult.getSuccess("返回数据成功!");
        try {
            QueryFilter filter = QueryFilterBuilder.createQueryFilter(queryData);
            handleFilter(filter);
            IPage page = sysTransferLogServiceImpl.queryUser(filter);
            jsonResult.setPageData(page);
        } catch (Exception ex) {
            jsonResult.setSuccess(false);
            logger.error(ExceptionUtil.getExceptionMessage(ex));
            jsonResult.setMessage(ExceptionUtil.getExceptionMessage(ex));
        }
        return jsonResult;
    }

    @MethodDefine(title = "查看单条记录信息(用户信息)", path = "/getUser", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "主键ID", varName = "pkId")})
    @ApiOperation(value = "查看单条记录信息(用户信息)")
    @GetMapping("/getUser")
    public JsonResult getUser(@RequestParam(value = "pkId") String pkId) {
        JsonResult result = JsonResult.Success();
        result.setShow(false);
        if (ObjectUtils.isEmpty(pkId)) {
            return result.setData(new Object());
        }
        SysTransferLog ent = sysTransferLogServiceImpl.getUser(pkId);
        return result.setData(ent);
    }

}