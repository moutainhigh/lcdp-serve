package com.redxun.user.org.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.ContextQuerySupport;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.JsonPageResult;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ContextUtil;
import com.redxun.common.utils.DbLogicDelete;
import com.redxun.log.model.Audit;
import com.redxun.log.model.LogContext;
import com.redxun.user.org.entity.OsGroup;
import com.redxun.user.org.entity.OsUserType;
import com.redxun.user.org.service.OsGroupServiceImpl;
import com.redxun.user.org.service.OsUserTypeServiceImpl;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户类型Controller
 */
@Slf4j
@RestController
@RequestMapping("/user/org/osUserType")
@ClassDefine(title = "用户类型",alias = "osUserTypeController",path = "/user/org/osUserType",packages = "org",packageName = "组织架构")
@Api(tags = "用户类型")
@ContextQuerySupport(tenant = ContextQuerySupport.CURRENT,company = ContextQuerySupport.BOTH)
public class OsUserTypeController extends BaseController<OsUserType> {

    @Autowired
    OsUserTypeServiceImpl osUserTypeServiceImpl;

    @Autowired
    OsGroupServiceImpl osGroupServiceImpl;

    @Override
    public BaseService getBaseService() {
        return osUserTypeServiceImpl;
    }

    @Override
    public String getComment() {
        return "用户类型";
    }



    @Override
    protected void handlePage(IPage page) {
        super.handlePage(page);
        List<OsUserType> list=page.getRecords();
        for(OsUserType osUserType:list){
            if(StringUtils.isNotEmpty(osUserType.getGroupId()) && osGroupServiceImpl.get(osUserType.getGroupId())!=null){
                osUserType.setGrant(true);
            }
        }
    }

    @MethodDefine(title = "查询当前业务表的所有数据", path = "/getAllByTenantId", method = HttpMethodConstants.GET,
            params = {})
    @ApiOperation(value="查询当前业务表的所有数据", notes = "查询当前实体表的所有数据。")
    @GetMapping("getAllByTenantId")
    public JsonPageResult getAllByTenantId(@RequestParam(required = false,value = "tenantId")String tenantId) throws Exception {
        QueryWrapper queryWrapper=new QueryWrapper();
        if(StringUtils.isEmpty(tenantId)){
            tenantId=ContextUtil.getCurrentTenantId();
        }
        queryWrapper.eq("TENANT_ID_",tenantId);
        //逻辑删除
        if (DbLogicDelete.getLogicDelete()) {
            queryWrapper.eq("DELETED_","0");
        }
        List data= getBaseService().findAll(queryWrapper);

        JsonPageResult result=new JsonPageResult();
        result.setData(data);
        result.setShow(false);
        return result;
    }

    @Override
    protected JsonResult beforeSave(OsUserType ent) {

        String tenantId=getCurrentTenantId();
        OsUserType osUserType= osUserTypeServiceImpl.getByCode(ent.getCode(),tenantId);
        if(osUserType!=null &&
                !osUserType.getId().equals(ent.getId())){
            return JsonResult.Fail("用户类型编码已存在");
        }

        if(StringUtils.isEmpty(ent.getId())){
            OsGroup osGroup = osGroupServiceImpl.createUserTypeGroup(ent.getCode(),ent.getName());
            //设置关联GroupId
            ent.setGroupId(osGroup.getGroupId());



        }else if(StringUtils.isNotEmpty(ent.getGroupId())){
            OsGroup osGroup= osGroupServiceImpl.get(ent.getGroupId());
            if(osGroup!=null){
                osGroup.setKey(ent.getCode());
                osGroup.setName(ent.getName());
                osGroup.setPath("0."+osGroup.getGroupId()+".");
                osGroupServiceImpl.update(osGroup);
            }else{
                osGroup = osGroupServiceImpl.createUserTypeGroup(ent.getCode(),ent.getName());
                //设置关联GroupId
                ent.setGroupId(osGroup.getGroupId());
            }
        }


        return JsonResult.Success();
    }

    @ApiOperation(value="删除实体信息", notes="根据实体Id删除实体信息,parameters is {ids:'1,2'}")
    @PostMapping("del")
    @Override
    public JsonResult del(@RequestParam(value = "ids") String ids){

        if(StringUtils.isEmpty(ids)){
            return new JsonResult(false,"没有选择关系类型!");
        }

        StringBuilder sb=new StringBuilder();

        sb.append("删除用户类型:");

        String[] aryIds=ids.split(",");
        for(String id:aryIds){
            OsUserType osUserType=osUserTypeServiceImpl.get(id);
            sb.append(osUserType.getName() +"("+osUserType.getCode()+")");
            osUserTypeServiceImpl.delete(id);
        }
        //删除用户类型
        LogContext.put(Audit.DETAIL,sb.toString());
        JsonResult result= JsonResult.getSuccessResult("删除"+getComment()+"成功!");

        return result;
    }


}
