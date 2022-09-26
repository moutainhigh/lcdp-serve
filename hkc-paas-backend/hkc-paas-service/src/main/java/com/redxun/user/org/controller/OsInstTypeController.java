package com.redxun.user.org.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.base.search.QueryFilter;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.utils.DbLogicDelete;
import com.redxun.user.org.entity.OsInstType;
import com.redxun.user.org.service.OsInstTypeServiceImpl;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 机构类型Controller
 *
 * @author yjy
 * @date 2019-10-29 17:31:21
 */
@Slf4j
@RestController
@RequestMapping("/user/org/osInstType")
@ClassDefine(title = "机构类型",alias = "osInstTypeController",path = "/user/org/osInstType",packages = "org",packageName = "组织架构")
@Api(tags = "机构类型")
public class OsInstTypeController extends BaseController<OsInstType> {


    @Autowired
    OsInstTypeServiceImpl osInstTypeServiceImpl;

    @Override
    public BaseService getBaseService() {
        return osInstTypeServiceImpl;
    }

    @Override
    public String getComment() {
        return "机构类型";
    }


    @Override
    protected void handleFilter(QueryFilter filter) {
        //super.handleFilter(filter);
        //逻辑删除
        if (DbLogicDelete.getLogicDelete()) {
            filter.addQueryParam("Q_DELETED__S_EQ","0");
        }
    }

    @Override
    protected JsonResult beforeSave(OsInstType ent) {
        boolean isExist= osInstTypeServiceImpl.isExist(ent);
        if(isExist){
            return JsonResult.Fail("该机构类型已存在!");
        }
        return super.beforeSave(ent);
    }

    /**
     * 查询字典列表数据
     */
    @MethodDefine(title = "查询机构类型列表数据", path = "/getOsInstList", method = HttpMethodConstants.GET)
    @ApiOperation(value = "查询机构类型列表数据")
    @GetMapping("/getOsInstList")
    public List<OsInstType> getOsInstList() {
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("ENABLED_","Y");
        wrapper.eq("IS_DEFAULT_","N");
        wrapper.orderByDesc("UPDATE_TIME_");
        //逻辑删除
        if (DbLogicDelete.getLogicDelete()) {
            wrapper.eq("DELETED_","0");
        }
        List<OsInstType> list = osInstTypeServiceImpl.list(wrapper);
        return list;
    }

}
