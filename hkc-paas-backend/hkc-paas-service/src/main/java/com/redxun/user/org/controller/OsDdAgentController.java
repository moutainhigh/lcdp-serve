
package com.redxun.user.org.controller;

import com.alibaba.fastjson.JSONObject;
import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.utils.ContextUtil;
import com.redxun.dto.user.OsDdAgentDto;
import com.redxun.user.org.entity.OsDdAgent;
import com.redxun.user.org.service.OsDdAgentServiceImpl;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 钉钉代理服务类
 */
@Slf4j
@RestController
@RequestMapping("/user/org/osDdAgent")
@Api(tags = "钉钉应用管理")
@ClassDefine(title = "钉钉应用管理",alias = "osDdAgentController",path = "/user/org/osDdAgent",packages = "org",packageName = "组织架构")
public class OsDdAgentController extends BaseController<OsDdAgent> {

    @Autowired
    OsDdAgentServiceImpl osDdAgentService;


    @Override
    public BaseService getBaseService() {
    return osDdAgentService;
    }

    @Override
    public String getComment() {
    return "钉钉应用管理";
    }

    @MethodDefine(title = "获取默认的钉钉应用", path = "/getDdAgent", method = HttpMethodConstants.GET)
    @ApiOperation(value = "获取默认的钉钉应用")
    @GetMapping("getDdAgent")
    public OsDdAgentDto getDdAgent(@RequestParam(value = "tenantId")String tenantId){
        OsDdAgentDto osDdAgentDto=null;
        OsDdAgent agent=osDdAgentService.getDefault(tenantId);
        if(BeanUtil.isEmpty(agent)){
            return null;
        }
        try {
            String osDdAgentStr = JSONObject.toJSONString(agent);
            osDdAgentDto= JSONObject.parseObject(osDdAgentStr,OsDdAgentDto.class);
        }catch (Exception e){
            log.error("获取钉钉应用配置出错！",e);
        }
        return osDdAgentDto;
    }

    @Override
    protected JsonResult beforeSave(OsDdAgent ent) {
        String tenantId= ContextUtil.getCurrentTenantId();
        boolean isAgentIdExist=osDdAgentService.isAgentIdExist(ent,tenantId);
        if(isAgentIdExist){
            return JsonResult.Fail("应用ID已存在");
        }

        boolean isAgentKeyExist=osDdAgentService.isAgentKeyExist(ent,tenantId);
        if(isAgentKeyExist){
            return JsonResult.Fail("应用KEY已存在");
        }

        //如果为默认将其他的更新为非默认。
        if(ent.getIsDefault()==1){
            osDdAgentService.updNoDefault(tenantId);
        }

        return super.beforeSave(ent);
    }
}

