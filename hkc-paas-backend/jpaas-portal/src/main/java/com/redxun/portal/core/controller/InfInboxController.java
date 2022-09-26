package com.redxun.portal.core.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.JsonPageResult;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.base.entity.QueryData;
import com.redxun.common.base.search.QueryFilter;
import com.redxun.common.base.search.QueryFilterBuilder;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ExceptionUtil;
import com.redxun.log.annotation.AuditLog;
import com.redxun.portal.core.entity.InfInbox;
import com.redxun.portal.core.service.InfInboxServiceImpl;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("/portal/core/infInbox")
@ClassDefine(title = "内部消息收件箱",alias = "infInboxController",path = "/portal/core/infInbox",packages = "core",packageName = "门户管理")
@Api(tags = "内部消息收件箱")
public class InfInboxController extends BaseController<InfInbox> {

    @Autowired
    InfInboxServiceImpl infInboxServiceImpl;

    @Override
    public BaseService getBaseService() {
        return infInboxServiceImpl;
    }

    @Override
    public String getComment() {
        return "内部短消息收件箱";
    }




}
