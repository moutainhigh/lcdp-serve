package com.redxun.user.org.controller;

import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.user.org.entity.OsPropertiesVal;
import com.redxun.user.org.service.OsPropertiesValServiceImpl;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 用户属性值定义Controller
 */
@Slf4j
@RestController
@RequestMapping("/user/org/osPropertiesVal")
@ClassDefine(title = "扩展属性值",alias = "osPropertiesValController",path = "/user/org/osPropertiesVal",packages = "org",packageName = "组织架构")
@Api(tags = "扩展属性值")
public class OsPropertiesValController extends BaseController<OsPropertiesVal> {

    @Autowired
    OsPropertiesValServiceImpl osPropertiesValService;

    @Override
    public BaseService getBaseService() {
        return osPropertiesValService;
    }

    @Override
    public String getComment() {
        return "扩展属性值";
    }

    @MethodDefine(title = "根据条件获取所属人id", path = "/getOwnerIdsByCondition", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "维度ID",varName = "dimId"),@ParamDefine(title = "条件",varName = "condition")})
    @ApiOperation(value = "根据条件获取所属人id")
    @PostMapping("/getOwnerIdsByCondition")
    public List<String> getOwnerIdsByCondition(@RequestParam("dimId")String dimId, @RequestParam("condition")String condition) {
        return osPropertiesValService.getOwnerIdsByCondition(dimId,condition);
    }

}