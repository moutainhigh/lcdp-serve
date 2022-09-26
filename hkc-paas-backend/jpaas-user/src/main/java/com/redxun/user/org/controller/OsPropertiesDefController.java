package com.redxun.user.org.controller;

import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.tool.StringUtils;
import com.redxun.log.annotation.AuditLog;
import com.redxun.log.model.Audit;
import com.redxun.log.model.LogContext;
import com.redxun.user.org.entity.OsPropertiesDef;
import com.redxun.user.org.service.OsPropertiesDefServiceImpl;
import com.redxun.user.org.service.OsPropertiesValServiceImpl;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 用户属性定义Controller
 */
@Slf4j
@RestController
@RequestMapping("/user/org/osPropertiesDef")
@ClassDefine(title = "自定义属性配置",alias = "osPropertiesDefController",path = "/user/org/osPropertiesDef",packages = "org",packageName = "组织架构")
@Api(tags = "自定义属性配置")
public class OsPropertiesDefController extends BaseController<OsPropertiesDef> {

    @Autowired
    OsPropertiesDefServiceImpl osPropertiesDefService;
    @Autowired
    OsPropertiesValServiceImpl osPropertiesValService;

    @Override
    public BaseService getBaseService() {
        return osPropertiesDefService;
    }

    @Override
    public String getComment() {
        return "自定义属性配置";
    }

    @ApiOperation(value="删除扩展属性值")
    @PostMapping("del")
    @AuditLog(operation = "删除扩展属性值")
    @Override
    public JsonResult del(@RequestParam(value = "ids") String ids){
        if(StringUtils.isEmpty(ids)){
            return new JsonResult(false,"");
        }
        String[] aryId=ids.split(",");
        List list= Arrays.asList(aryId);

        StringBuilder sb=new StringBuilder("删除属性:");
        list.stream().forEach(item->{
            String id=(String)item;
            OsPropertiesDef def=osPropertiesDefService.get(id);
            sb.append(def.getName()+",");
        });

        LogContext.put(Audit.DETAIL,sb.toString());


        JsonResult result=JsonResult.getSuccessResult("删除"+getComment()+"成功!");
        getBaseService().delete(list);
        //将所有属性值删除
        osPropertiesValService.delByDefId(aryId);
        return result;
    }

    @MethodDefine(title = "根据维度id查询扩展属性", path = "/getPropertiesByDimId", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "维度ID",varName = "dimId")})
    @ApiOperation(value="根据维度id查询扩展属性")
    @GetMapping("getPropertiesByDimId")
    public List<OsPropertiesDef> getPropertiesByDimId(@RequestParam(value = "dimId") String dimId){
        return osPropertiesDefService.getPropertiesByDimId(dimId);
    }
}
