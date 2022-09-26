package com.redxun.user.org.controller;

import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.base.search.QueryFilter;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ContextUtil;
import com.redxun.common.utils.DbLogicDelete;
import com.redxun.log.annotation.AuditLog;
import com.redxun.user.org.entity.OsRankType;
import com.redxun.user.org.service.OsRankTypeServiceImpl;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 用户组等级分类
 *
 * @author yjy
 * @date 2019-10-29 17:31:18
 */
@Slf4j
@RestController
@RequestMapping("/user/org/osRankType")
@ClassDefine(title = "用户组等级分类",alias = "osRankTypeController",path = "/user/org/osRankType",packages = "org",packageName = "组织架构")
@Api(tags = "用户组等级分类")
public class OsRankTypeController extends BaseController<OsRankType> {

    @Autowired
    OsRankTypeServiceImpl osRankTypeServiceImpl;

    @Override
    public BaseService getBaseService() {
        return osRankTypeServiceImpl;
    }

    @Override
    public String getComment() {
        return "用户组等级分类";
    }

    @Override
    protected void handleFilter(QueryFilter filter) {
        Map<String, Object> params = filter.getParams();
        String tenantId= ContextUtil.getCurrentTenantId();
        if(params.containsKey("tenantId")){
            tenantId=params.get("tenantId").toString();
        }
        filter.addQueryParam("Q_TENANT_ID__S_EQ",tenantId );
        //逻辑删除
        if (DbLogicDelete.getLogicDelete()) {
            filter.addQueryParam("Q_DELETED__S_EQ","0");
        }
    }

    @MethodDefine(title = "根据维度ID查找等级", path = "/getByDimId", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "维度ID",varName = "dimId")})
    @ApiOperation("根据维度ID查找等级")
    @GetMapping("getByDimId")
    public List<OsRankType> getByDimId(@RequestParam("dimId")String dimId){
        String tenantId=ContextUtil.getCurrentTenantId();
        return osRankTypeServiceImpl.getByDimId(dimId,tenantId);
    }

    @Override
    protected JsonResult beforeSave(OsRankType ent) {
        boolean isExist = osRankTypeServiceImpl.isExist(ent);
        if(isExist){
            return JsonResult.Fail("业务主键或等级不能重复!");
        }
        return super.beforeSave(ent);
    }

    @MethodDefine(title = "保存多条等级数据记录", path = "/saveAll", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "保存多条等级数据记录", varName = "entitys")})
    @ApiOperation(value="保存多条等级数据记录", notes="保存多条等级数据记录")
    @AuditLog(operation = "保存多条等级数据记录")
    @PostMapping("/saveAll")
    public JsonResult saveAll(@ApiParam  @RequestBody List<OsRankType> entitys){
        for(OsRankType type:entitys){
            if(StringUtils.isNotEmpty(type.getRkId())){
                osRankTypeServiceImpl.update(type);
            }else{
                type.setRkId(IdGenerator.getIdStr());
                osRankTypeServiceImpl.save(type);
            }
        }
        return new JsonResult(true,"成功保存多条等级记录！");
    }
}
