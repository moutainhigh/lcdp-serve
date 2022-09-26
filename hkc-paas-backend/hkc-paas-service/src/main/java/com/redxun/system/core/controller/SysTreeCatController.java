package com.redxun.system.core.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.base.search.QueryFilter;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.utils.ContextUtil;
import com.redxun.common.utils.DbLogicDelete;
import com.redxun.system.core.entity.SysTreeCat;
import com.redxun.system.core.service.SysTreeCatServiceImpl;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/system/core/sysTreeCat")
@ClassDefine(title = "系统分类",alias = "sysTreeCatController",path = "/system/core/sysTreeCat",packages = "core",packageName = "系统管理")
@Api(tags = "系统分类")
public class SysTreeCatController extends BaseController<SysTreeCat> {

    @Autowired
    SysTreeCatServiceImpl sysTreeCatServiceImpl;

    @Override
    protected void handleFilter(QueryFilter filter) {
        String tenantId= ContextUtil.getCurrentUserId();
        filter.addQueryParam("Q_TENANT_ID__S_IN",tenantId+",0");
        //逻辑删除
        if (DbLogicDelete.getLogicDelete()) {
            filter.addQueryParam("Q_DELETED__S_EQ","0");
        }
    }

    @Override
    public BaseService getBaseService() {
        return sysTreeCatServiceImpl;
    }

    @Override
    public String getComment() {
        return "系统分类";
    }

    @Override
    protected JsonResult beforeSave(SysTreeCat ent) {
        boolean result= sysTreeCatServiceImpl.isExist(ent);
        if(result){
            return JsonResult.Fail("数据已经存在key【"+ent.getKey()+"】!");
        }
        return JsonResult.Success();
    }

    @MethodDefine(title = "根据Key获取某一分类树", path = "/getTreeCatByKey", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "分类树KEY", varName = "catKey")})
    @ApiOperation("根据Key获取某一分类树")
    @GetMapping("/getTreeCatByKey")
    public SysTreeCat getTreeCatNameByKey(@ApiParam @RequestParam String catKey){
        SysTreeCat sysTreeCat = sysTreeCatServiceImpl.getByKey(catKey);
        return sysTreeCat;
    }

    @MethodDefine(title = "根据租户获取分类", path = "/getTreeCatByKey", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "分类树KEY", varName = "catKey")})
    @ApiOperation("根据租户获取分类")
    @GetMapping("/getCatList")
    public List<SysTreeCat> getCatList(){
        String tenantId=ContextUtil.getCurrentTenantId();
        QueryWrapper wrapper=new QueryWrapper();

        wrapper.in("TENANT_ID_",new String[]{tenantId,"0"});
        //逻辑删除
        if (DbLogicDelete.getLogicDelete()) {
            wrapper.eq("DELETED_","0");
        }
        List<SysTreeCat> list= sysTreeCatServiceImpl.getRepository().selectList(wrapper);
        return  list;
    }


}
