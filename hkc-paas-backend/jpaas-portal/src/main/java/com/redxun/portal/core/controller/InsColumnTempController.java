package com.redxun.portal.core.controller;

import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.base.search.QueryFilter;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.utils.ContextUtil;
import com.redxun.common.utils.DbLogicDelete;
import com.redxun.portal.context.InsColumnDefHandlerContext;
import com.redxun.portal.core.entity.InsColumnTemp;
import com.redxun.portal.core.service.InsColumnTempServiceImpl;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/portal/core/insColumnTemp")
@ClassDefine(title = "栏目模板",alias = "insColumnTempController",path = "/portal/core/insColumnTemp",packages = "core",packageName = "门户管理")
@Api(tags = "栏目模板")
public class InsColumnTempController extends BaseController<InsColumnTemp> {

    @Autowired
    InsColumnTempServiceImpl insColumnTempService;

    @Override
    public BaseService getBaseService() {
        return insColumnTempService;
    }

    @Override
    public String getComment() {
        return "栏目模板";
    }

    @Override
    protected void handleFilter(QueryFilter filter) {
        String tenantId= ContextUtil.getCurrentTenantId();
        filter.addQueryParam("Q_TENANT_ID__S_IN",tenantId +",0");
        //逻辑删除
        if (DbLogicDelete.getLogicDelete()) {
            filter.addQueryParam("Q_DELETED__S_EQ","0");
        }
    }

    @Override
    protected JsonResult beforeSave(InsColumnTemp ent){
        boolean result= insColumnTempService.isExist(ent);
        if(result){
            return JsonResult.Fail("数据已经存在key【"+ent.getKey()+"】!");
        }
        return JsonResult.Success();
    }

    /**
     * 获取栏目模板类型
     */
    @MethodDefine(title = "获取栏目模板类型", path = "/getTempTypeList", method = HttpMethodConstants.GET)
    @ApiOperation(value = "获取栏目模板类型")
    @GetMapping("/getTempTypeList")
    public JsonResult getTempTypeList() {
        Map<String, String> map= InsColumnDefHandlerContext.getInsColumnDefMapMap();
        return JsonResult.Success().setData(map);
    }


    @MethodDefine(title = "获取栏目模板列表", path = "/getColumnTempList", method = HttpMethodConstants.GET)
    @ApiOperation(value = "获取栏目模板列表")
    @GetMapping("/getColumnTempList")
    public List<InsColumnTemp> getColumnTempList() {
        String tenantId=ContextUtil.getCurrentTenantId();
        List<InsColumnTemp> list= insColumnTempService.getColumnTempList(tenantId);
        return list;
    }
}
