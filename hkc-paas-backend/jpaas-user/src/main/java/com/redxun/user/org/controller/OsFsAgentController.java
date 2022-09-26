
package com.redxun.user.org.controller;

import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.utils.ContextUtil;
import com.redxun.dto.user.OsFsAgentDto;
import com.redxun.user.org.entity.OsFsAgent;
import com.redxun.user.org.service.OsFsAgentServiceImpl;
import com.redxun.util.BeanCopyUtils;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/user/org/osFsAgent")
@Api(tags = "飞书应用表")
@ClassDefine(title = "飞书应用表", alias = "OsFsAgentController", path = "/user/org/osFsAgent", packages = "org", packageName = "子系统名称")

public class OsFsAgentController extends BaseController<OsFsAgent> {

    @Autowired
    OsFsAgentServiceImpl osFsAgentService;


    @Override
    public BaseService getBaseService() {
        return osFsAgentService;
    }

    @Override
    public String getComment() {
        return "飞书应用表";
    }
    @Override
    protected JsonResult beforeSave(OsFsAgent ent)  {
        String tenantId= ContextUtil.getCurrentTenantId();
        //更新其他是否默认为否
        if("1".equals(Integer.toString(ent.getIsDefault()))){
            osFsAgentService.updateNotDefault(tenantId);
        }
        return super.beforeSave(ent);
    }

    @MethodDefine(title = "获取默认的飞书应用", path = "/getDefaultAgent", method = HttpMethodConstants.GET)
    @ApiOperation(value = "获取默认的飞书应用")
    @GetMapping("getDefaultAgent")
    public OsFsAgentDto getDefaultAgent(@RequestParam(value = "tenantId") String tenantId) {
        OsFsAgentDto osFsAgentDto = null;
        try {
            OsFsAgent osFsAgent = osFsAgentService.getDefaultAgent(tenantId);
            if (BeanUtil.isEmpty(osFsAgent)) {
                return null;
            }
            osFsAgentDto = BeanCopyUtils.map(osFsAgent, OsFsAgentDto.class);
        } catch (Exception e) {
            log.error("OsFsAgentController.getDefaultAgent is error :" + e.getMessage());
        }

        return osFsAgentDto;
    }

}

