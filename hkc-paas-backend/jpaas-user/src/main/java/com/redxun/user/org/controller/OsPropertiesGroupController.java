package com.redxun.user.org.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.JsonPageResult;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.base.entity.QueryData;
import com.redxun.common.base.search.QueryFilter;
import com.redxun.common.base.search.QueryFilterBuilder;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ContextUtil;
import com.redxun.common.utils.DbLogicDelete;
import com.redxun.common.utils.ExceptionUtil;
import com.redxun.log.annotation.AuditLog;
import com.redxun.log.model.Audit;
import com.redxun.log.model.LogContext;
import com.redxun.log.util.EntityUtil;
import com.redxun.user.org.entity.OsGroup;
import com.redxun.user.org.entity.OsPropertiesGroup;
import com.redxun.user.org.service.OsGroupServiceImpl;
import com.redxun.user.org.service.OsPropertiesDefServiceImpl;
import com.redxun.user.org.service.OsPropertiesGroupServiceImpl;
import com.redxun.user.org.service.OsPropertiesValServiceImpl;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * 用户组属性扩展定义Controller
 */
@Slf4j
@RestController
@RequestMapping("/user/org/osPropertiesGroup")
@ClassDefine(title = "自定义属性分组",alias = "osPropertiesGroupController",path = "/user/org/osPropertiesGroup",packages = "org",packageName = "组织架构")
@Api(tags = "自定义属性分组")
public class OsPropertiesGroupController extends BaseController<OsPropertiesGroup> {

    @Autowired
    OsPropertiesGroupServiceImpl osPropertiesGroupService;

    @Autowired
    OsPropertiesValServiceImpl osPropertiesValService;

    @Autowired
    OsPropertiesDefServiceImpl osPropertiesDefService;

    @Autowired
    OsGroupServiceImpl osGroupService;

    @Override
    public BaseService getBaseService() {
        return osPropertiesGroupService;
    }



    @Override
    public String getComment() {
        return "自定义属性分组";
    }

    @Override
    protected void handleFilter(QueryFilter filter) {
        String tenantId= ContextUtil.getCurrentTenantId();
        filter.addQueryParam("Q_opg.TENANT_ID__S_EQ",tenantId);
        //逻辑删除
        if (DbLogicDelete.getLogicDelete()) {
            filter.addQueryParam("Q_opg.DELETED__S_EQ","0");
        }
    }

    /**
     * 获得所有查询数据列表，不传入条件时，则返回所有的记录
     * @return
     * @throws Exception
     */
    @Override
    @MethodDefine(title = "根据条件查询所有记录", path = "/query", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "查询条件", varName = "queryData")})
    @ApiOperation(value="按条件查询所有记录", notes="根据条件查询所有记录")
    @PostMapping(value="/query")
    public JsonPageResult query(@RequestBody QueryData queryData) throws Exception{
        JsonPageResult jsonResult=JsonPageResult.getSuccess("返回数据成功!");
        try{
            QueryFilter filter= QueryFilterBuilder.createQueryFilter(queryData);
            handleFilter(filter);
            IPage page= getBaseService().query(filter);
            handlePage(page);
            jsonResult.setPageData(page);
        }catch (Exception ex){
            jsonResult.setSuccess(false);
            logger.error(ExceptionUtil.getExceptionMessage(ex));
            jsonResult.setMessage(ExceptionUtil.getExceptionMessage(ex));
        }
        return jsonResult;
    }

    @MethodDefine(title = "保存自定义属性分组", path = "/save", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "自定义属性分组", varName = "entity")})
    @ApiOperation(value="自定义属性分组", notes="保存自定义属性分组")
    @AuditLog(operation = "自定义属性分组")
    @PostMapping("/save")
    public JsonResult save(@Validated @ApiParam @RequestBody OsPropertiesGroup entity, BindingResult validResult) throws Exception{
        JsonResult result=JsonResult.Success();
        if(!result.isSuccess()){
            return result;
        }
        Serializable pkId= entity.getPkId();
        String str="";
        String operation="";
        try{
            result= beforeSave(entity);
            if(!result.isSuccess()){
                return result;
            }
            if(BeanUtil.isEmpty(pkId)){
                String fieldInfo="添加"+getComment()+":"+ EntityUtil.getInfo(entity,true);
                String name = entity.getName();
                QueryData queryData=new QueryData();
                QueryFilter filter= QueryFilterBuilder.createQueryFilter(queryData);
                filter.addQueryParam("Q_opg.NAME__S_EQ",name);
                IPage query = osPropertiesGroupService.query(filter);
                if(query.getRecords().size()>0){
                    return new JsonResult().setSuccess(false).setMessage("分组名称重复!");
                }
                osPropertiesGroupService.insert(entity);
                //处理日志相关。
                LogContext.put(Audit.PK,entity.getPkId());
                LogContext.put(Audit.ACTION,Audit.ACTION_ADD);
                LogContext.put(Audit.DETAIL, fieldInfo);

                str="添加" + getComment() + "成功";
                operation="添加" + getComment();

                LogContext.put(Audit.OPERATION,operation);
            }else{
                OsPropertiesGroup oldEnt=osPropertiesGroupService.get(pkId);

                //处理日志相关。
                String fieldInfo= EntityUtil.getUpdInfo(oldEnt,entity);
                LogContext.put(Audit.ACTION,Audit.ACTION_UPD);
                LogContext.put(Audit.PK,pkId);
                LogContext.put(Audit.DETAIL,"更新"+getComment()+":" +fieldInfo);

                osPropertiesGroupService.update(entity);
                str="更新" + getComment() + "成功";

                operation="更新" + getComment();

                LogContext.put(Audit.OPERATION,operation);
            }
            result=JsonResult.getSuccessResult(str);
            result.setData(entity);
        } catch(Exception ex){
            String errMsg= ExceptionUtil.getExceptionMessage(ex);

            if(BeanUtil.isEmpty(pkId)){
                str="添加" + getComment() +"失败!";
            }else{
                str="更新" + getComment() +"失败!";
            }
            //处理日志相关。
            LogContext.put(Audit.OPERATION,operation);
            LogContext.put(Audit.DETAIL,errMsg);

            result=JsonResult.getFailResult(str);
            result.setData(errMsg);
        }
        return result;
    }

    /**
     * 查询
     */
    @MethodDefine(title = "获取所有的自定义属性组", path = "/getAllProperties", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "维度ID",varName = "dimId"),@ParamDefine(title = "所属人ID",varName = "ownerId"),@ParamDefine(title = "机构ID",varName = "tenantId")})
    @ApiOperation(value = "获取所有的自定义属性组")
    @PostMapping("/getAllProperties")
    public List<OsPropertiesGroup> getAllProperties(@RequestParam String dimId,@RequestParam String ownerId,@RequestParam(required = false)String tenantId) {
        return osPropertiesGroupService.getPropertiesGroups(dimId,ownerId,tenantId);
    }

    @MethodDefine(title = "保存自定义属性", path = "/saveProperties", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "自定义属性",varName = "jsonObject")})
    @ApiOperation(value = "保存自定义属性")
    @AuditLog(operation = "保存自定义属性")
    @PostMapping("/saveProperties")
    public JsonResult saveProperties(@RequestBody JSONObject jsonObject) {
        try {
            String propertiesGroups = jsonObject.getString("propertiesGroups");
            String groupId = jsonObject.getString("groupId");

            OsGroup osGroup= osGroupService.get(groupId);
            String detail="设置用户组：" +osGroup.getName() +"属性!";
            LogContext.put(Audit.DETAIL,detail);


            List<OsPropertiesGroup> propertiesGroupsList = JSONArray.parseArray(propertiesGroups, OsPropertiesGroup.class);
            osPropertiesValService.saveProPerty(propertiesGroupsList,groupId);
            return JsonResult.Success("保存成功");
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.Fail("保存失败!");
        }
    }

    @ApiOperation(value="删除扩展属性分组")
    @AuditLog(operation = "删除扩展属性分组")
    @PostMapping("del")
    @Override
    public JsonResult del(@RequestParam String ids){
        if(StringUtils.isEmpty(ids)){
            return new JsonResult(false,"");
        }
        String[] aryId=ids.split(",");
        List list= Arrays.asList(aryId);

        StringBuilder detail=new StringBuilder( "删除扩展属性分组:");

        list.stream().forEach(item->{
            String groupId=(String)item.toString();
            OsPropertiesGroup osPropertiesGroup = osPropertiesGroupService.get(groupId);
            detail.append(osPropertiesGroup.getName() +",") ;
        });
        LogContext.put(Audit.DETAIL,detail);



        osPropertiesDefService.delByGroupId(aryId);
        JsonResult result=JsonResult.getSuccessResult("删除"+getComment()+"成功!");
        getBaseService().delete(list);
        return result;
    }

}
