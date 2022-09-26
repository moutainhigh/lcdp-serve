package com.redxun.user.org.controller;

import com.alibaba.fastjson.JSONObject;
import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.utils.ContextUtil;
import com.redxun.dto.user.OsWxEntAgentDto;
import com.redxun.user.org.entity.OsWxEntAgent;
import com.redxun.user.org.service.OsWxEntAgentServiceImpl;
import com.redxun.util.wechat.WeChatTokenModel;
import com.redxun.util.wechat.WeChatTokenUtil;
import com.redxun.util.wechat.WeixinUtil;
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
 * 微信企业应用Controller
 */
@Slf4j
@RestController
@RequestMapping("/user/org/osWxEntAgent")
@ClassDefine(title = "企业微信应用",alias = "osWxEntAgentController",path = "/user/org/osWxEntAgent",packages = "org",packageName = "组织架构")
@Api(tags = "企业微信应用")
public class OsWxEntAgentController extends BaseController<OsWxEntAgent> {

    @Autowired
    OsWxEntAgentServiceImpl osWxEntAgentService;

    @Override
    public BaseService getBaseService() {
        return osWxEntAgentService;
    }

    @Override
    public String getComment() {
        return "企业微信应用";
    }

    @MethodDefine(title = "获取默认的企业微信应用", path = "/getDefaultAgent", method = HttpMethodConstants.GET)
    @ApiOperation(value = "获取默认的企业微信应用")
    @GetMapping("getDefaultAgent")
    public OsWxEntAgentDto getDefaultAgent(@RequestParam(value = "tenantId")String tenantId){
        OsWxEntAgentDto osWxEntAgentDto=null;
        OsWxEntAgent osWxEntAgent= osWxEntAgentService.getDefaultAgent(tenantId);
        if(BeanUtil.isEmpty(osWxEntAgent)){
            return null;
        }

        try {
            String osWxEntAgentStr = JSONObject.toJSONString(osWxEntAgent);
            osWxEntAgentDto= JSONObject.parseObject(osWxEntAgentStr,OsWxEntAgentDto.class);
        }catch (Exception e){
            log.error("OsWxEntAgentController.getDefaultAgent is error :"+e.getMessage());
        }

        return osWxEntAgentDto;
    }

    @Override
    protected JsonResult beforeSave(OsWxEntAgent ent)  {
        WeChatTokenModel tokenModel;
        try{
            tokenModel = WeChatTokenUtil.getEntToken(ent.getCorpId(), ent.getSecret());
        }
        catch (Exception ex){
            return JsonResult.Fail("企业号或应用密钥输入错误");
        }

        try{
            boolean rtn=WeixinUtil.validApp(ent.getAgentId(),tokenModel.getToken());
            if(!rtn){
                return JsonResult.Fail("应用ID无效!");
            }
        }
        catch (Exception ex){
            return JsonResult.Fail("Http请求出错!");
        }



        String tenantId= ContextUtil.getCurrentTenantId();
        //更新其他默认为否
        if("1".equals(Integer.toString(ent.getDefaultAgent()))){
            osWxEntAgentService.updateNotDefault(tenantId);
        }
        return super.beforeSave(ent);
    }


}
