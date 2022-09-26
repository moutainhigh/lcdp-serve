package com.redxun.portal.core.controller;

import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.portal.core.entity.InsAppCollect;
import com.redxun.portal.core.service.InsAppCollectServiceImpl;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("/portal/core/insAppCollect")
@ClassDefine(title = "常用应用",alias = "insAppCollectController",path = "/portal/core/insAppCollect",packages = "core",packageName = "门户管理")
@Api(tags = "常用应用")
public class InsAppCollectController extends BaseController<InsAppCollect> {

    @Autowired
    InsAppCollectServiceImpl insAppCollectService;

    @Override
    public BaseService getBaseService() {
        return insAppCollectService;
    }

    @Override
    public String getComment() {
        return "常用应用";
    }

}