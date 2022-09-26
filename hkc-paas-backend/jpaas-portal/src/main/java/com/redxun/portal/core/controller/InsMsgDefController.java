package com.redxun.portal.core.controller;

import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.search.QueryFilter;
import com.redxun.common.utils.ContextUtil;
import com.redxun.common.utils.DbLogicDelete;
import com.redxun.portal.core.entity.InsMsgDef;
import com.redxun.portal.core.service.InsMsgDefServiceImpl;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("/portal/core/insMsgDef")
@ClassDefine(title = "消息项",alias = "insMsgDefController",path = "/portal/core/insMsgDef",packages = "core",packageName = "门户管理")
@Api(tags = "消息项")
public class InsMsgDefController extends BaseController<InsMsgDef> {

    @Autowired
    InsMsgDefServiceImpl insMsgDefService;

    @Override
    public BaseService getBaseService() {
        return insMsgDefService;
    }

    @Override
    public String getComment() {
        return "消息项";
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

}
