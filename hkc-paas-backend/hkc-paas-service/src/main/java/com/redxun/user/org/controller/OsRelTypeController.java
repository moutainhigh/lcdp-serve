package com.redxun.user.org.controller;

import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.base.search.QueryFilter;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.utils.ContextUtil;
import com.redxun.common.utils.DbLogicDelete;
import com.redxun.user.org.entity.OsRelType;
import com.redxun.user.org.service.OsRelTypeServiceImpl;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * 关系类型定义 提供者
 * 
 * @author yjy
 * @date 2019-11-08
 */
@Slf4j
@RestController
@RequestMapping("/user/org/osRelType")
@ClassDefine(title = "关系类型定义",alias = "osRelTypeController",path = "/user/org/osRelType",packages = "org",packageName = "组织架构")
@Api(tags = "关系类型定义")
public class OsRelTypeController extends BaseController<OsRelType> {

    @Autowired
    OsRelTypeServiceImpl osRelTypeServiceImpl;

    @Override
    public BaseService getBaseService() {
        return osRelTypeServiceImpl;
    }

    @Override
    protected void handleFilter(QueryFilter filter) {
        String tenantId=ContextUtil.getCurrentTenantId();
        filter.addQueryParam("Q_TENANT_ID__S_IN",tenantId +",0");
        //逻辑删除
        if (DbLogicDelete.getLogicDelete()) {
            filter.addQueryParam("Q_DELETED__S_EQ","0");
        }
    }

    @Override
    public String getComment() {
        return "关系类型定义";
    }

    @MethodDefine(title = "获得用户间的关系类型定义", path = "/getUserRels", method = HttpMethodConstants.GET)
    @ApiOperation(value="获得用户间的关系类型定义")
    @GetMapping("/getUserRels")
    public List<OsRelType> getUserRels(){
        String tenantId= ContextUtil.getCurrentTenantId();
        return osRelTypeServiceImpl.getUserRelType(tenantId);
    }

    @MethodDefine(title = "获得用户组间的关系类型定义", path = "/getGroupRels", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "维度ID",varName = "dimId")})
    @ApiOperation(value="获得用户组间的关系类型定义")
    @GetMapping("/getGroupRels")
    public List<OsRelType> getGroupRels( @RequestParam(value="dimId")String dimId){
        String tenantId= ContextUtil.getCurrentTenantId();
        return osRelTypeServiceImpl.getGroupRelType(tenantId,dimId);
    }

    @MethodDefine(title = "根据关系类型KEY获取关系类型数据", path = "/getRelTypeByKey", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "关系类型KEY",varName = "relTypeKey")})
    @ApiOperation(value="根据关系类型KEY获取关系类型数据")
    @GetMapping("/getRelTypeByKey")
    public OsRelType getRelTypeByKey(@RequestParam(value="relTypeKey")String relTypeKey){
        String tenantId=ContextUtil.getCurrentTenantId();
        return osRelTypeServiceImpl.getByKey(tenantId,relTypeKey);
    }


    /**
     * 根据维度ID查询组与用户的关系类型
     * @param dimId
     * @return
     */
    @MethodDefine(title = "根据维度ID查询组与用户的关系类型", path = "/getOsRelTypeOfGroupUser", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "维度ID",varName = "dimId"),@ParamDefine(title = "等级",varName = "level")})
    @ApiOperation(value="查询组与用户的关系类型", notes="根据维度ID查询组与用户的关系类型")
    @GetMapping("/getOsRelTypeOfGroupUser")
    public JsonResult<OsRelType> getOsRelTypeOfGroupUser(@RequestParam (value="dimId") String dimId,
                                                         @RequestParam (value="relType",required = false) String relType,
                                                         @RequestParam(value="level",required = false)Integer level){
        if(level==null){
            level=-1;
        }
        if(relType==null){
            relType=OsRelType.REL_TYPE_GROUP_USER;
        }
        //获取当前的租户
        String tenantId= getCurrentTenantId();
        List<OsRelType> data= osRelTypeServiceImpl.getOsRelTypeOfRelType(tenantId, dimId,relType,level);
        return JsonResult.Success("get data success").setData(data).setShow(false);
    }

    /**
     *
     * 根据维度ID查询组与组的关系类型
     * @param dimId
     * @return
     */
    @MethodDefine(title = "根据维度ID查询组与组的关系类型", path = "/getOsRelTypeOfGroupGroup", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "维度ID",varName = "dimId"),@ParamDefine(title = "等级",varName = "level")})
    @ApiOperation(value="查询组与组的关系类型", notes="根据维度ID查询组与组的关系类型")
    @GetMapping("/getOsRelTypeOfGroupGroup")
    public JsonResult<OsRelType> getOsRelTypeOfGroupGroup(@RequestParam (value="dimId") String dimId,@RequestParam(value="level",required = false)Integer level){
        if(level==null){
            level=-1;
        }
        //获取当前的租户
        String tenantId= getCurrentTenantId();
        List<OsRelType> data= osRelTypeServiceImpl.getOsRelTypeOfRelType(tenantId,dimId,"GROUP-GROUP",level);
        return JsonResult.Success("get data success").setData(data).setShow(false);
    }


    @Override
    protected JsonResult beforeSave(OsRelType ent) {

        String tenantId= getCurrentTenantId();

        boolean isExist= osRelTypeServiceImpl.isRelTypeExist(ent.getKey(),tenantId,ent.getId());

        if(isExist){
            return JsonResult.Fail("关系类型已经存在!");
        }

        return super.beforeSave(ent);
    }

    @MethodDefine(title = "获得用户组间的关系类型定义", path = "/getGroupRelsBydimIdAndRelType", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "维度ID",varName = "dimId"),@ParamDefine(title = "关系类型",varName = "relType")})
    @ApiOperation(value="获得用户组间的关系类型定义")
    @GetMapping("/getGroupRelsBydimIdAndRelType")
    public List<OsRelType> getGroupRelsBydimIdAndRelType( @RequestParam(value="dimId")String dimId,@RequestParam(value="relType")String relType){
        String tenantId= ContextUtil.getCurrentTenantId();
        return osRelTypeServiceImpl.getGroupRelsBydimIdAndRelType(tenantId,dimId,relType);
    }
}
