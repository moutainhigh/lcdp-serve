
package com.redxun.user.org.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.db.BaseService;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.tool.StringUtils;
import com.redxun.user.org.entity.OsCompanyAuth;
import com.redxun.user.org.service.OsCompanyAuthServiceImpl;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.redxun.common.annotation.ClassDefine;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/user/core/osCompanyAuth")
@Api(tags = "分公司授权表")
@ClassDefine(title = "分公司授权表",alias = "OsCompanyAuthController",path = "/user/core/osCompanyAuth",packages = "core",packageName = "子系统名称")
public class OsCompanyAuthController extends BaseController<OsCompanyAuth> {

    @Autowired
    OsCompanyAuthServiceImpl osCompanyAuthService;

    @Override
    public BaseService getBaseService() {
    return osCompanyAuthService;
    }

    @Override
    public String getComment() {
    return "分公司授权表";
    }


    @MethodDefine(title = "根据公司查询授权角色列表", path = "/getByCompanyId", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "公司ID", varName = "companyId")})
    @ApiOperation("根据公司查询授权角色列表")
    @PostMapping(value="/getByCompanyId")
    public List<OsCompanyAuth> getByCompanyId(@RequestParam(value = "companyId") String companyId){
        List<OsCompanyAuth> companys = osCompanyAuthService.getByCompanyId(companyId);
        return companys;
    }

    /**
     * 保存公司授权
     * @param json 数据格式为 {companyId:"",groups:[{id:"",name:""}]}
     * @return
     */
    @MethodDefine(title = "保存公司授权", path = "/saveAuth", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "公司ID", varName = "companyId")})
    @ApiOperation("保存公司授权成功")
    @PostMapping(value="/saveAuth")
    public JsonResult saveAuth(@RequestBody JSONObject json){
        JsonResult result= osCompanyAuthService.saveAuth(json);
        return result;
    }



}

