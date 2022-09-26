
package com.redxun.user.org.controller;

import com.redxun.common.base.db.BaseService;
import com.redxun.user.org.entity.OsDdCorp;
import com.redxun.user.org.service.OsDdCorpServiceImpl;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 钉钉公司机构服务Controller
 */
@Slf4j
@RestController
@RequestMapping("/user/org/osDdCorp")
@Api(tags = "os_dd_corp")
public class OsDdCorpController extends BaseController<OsDdCorp> {

    @Autowired
    OsDdCorpServiceImpl osDdCorpService;
    @Override
    public BaseService getBaseService() {
    return osDdCorpService;
    }

    @Override
    public String getComment() {
    return "os_dd_corp";
    }

}

