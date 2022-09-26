package com.redxun.portal.core.controller;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.base.search.QueryFilter;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ContextUtil;
import com.redxun.common.utils.DbLogicDelete;
import com.redxun.portal.core.entity.InsColumnDef;
import com.redxun.portal.core.entity.InsColumnTemp;
import com.redxun.portal.core.service.InsColumnDefServiceImpl;
import com.redxun.portal.core.service.InsColumnTempServiceImpl;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("/portal/core/insColumnDef")
@ClassDefine(title = "栏目定义",alias = "insColumnDefController",path = "/portal/core/insColumnDef",packages = "core",packageName = "门户管理")
@Api(tags = "栏目定义")
public class InsColumnDefController extends BaseController<InsColumnDef> {

    @Autowired
    InsColumnDefServiceImpl insColumnDefService;
    @Autowired
    InsColumnTempServiceImpl insColumnTempService;



    @Override
    public BaseService getBaseService() {
        return insColumnDefService;
    }

    @Override
    protected void handleFilter(QueryFilter filter) {
        String tenantId= ContextUtil.getCurrentTenantId();
        filter.addQueryParam("Q_a.TENANT_ID__S_IN",tenantId +",0");
        //逻辑删除
        if (DbLogicDelete.getLogicDelete()) {
            filter.addQueryParam("Q_a.DELETED__S_EQ","0");
        }
    }

    @Override
    public String getComment() {
        return "栏目定义";
    }

    @Override
    protected JsonResult beforeSave(InsColumnDef ent){
        boolean result= insColumnDefService.isExist(ent);
        if(result){
            return JsonResult.Fail("数据已经存在key【"+ent.getKey()+"】!");
        }
        return JsonResult.Success();
    }


    /**
     * 根据消息盒子ID查询数据
     */
    @MethodDefine(title = "根据栏目类型Id查询数据", path = "/queryByIsNews", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "查询类型ID", varName = "isNews")})
    @ApiOperation(value = "根据栏目类型Id查询数据")
    @GetMapping("/queryByIsNews")
    public JsonResult<List<InsColumnDef>> queryByIsNews(@RequestParam(value = "isNews") String isNews) {
        if (BeanUtil.isEmpty(isNews)) {
            return JsonResult.Success().setData(new ArrayList<>());
        }
        List<InsColumnDef> list = insColumnDefService.queryByIsNews("%\"isNews\":\"" + isNews + "%");
        return JsonResult.Success().setData(list);
    }

    /**
     * 根据主键查询记录详细信息
     * @param pkId
     * @return
     */
    @MethodDefine(title = "根据ID获取门户栏目", path = "/getColumnById", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "主键", varName = "pkId")})
    @ApiOperation(value="根据ID获取门户栏目", notes="根据ID获取门户栏目")
    @GetMapping("/getColumnById")
    public JsonResult<InsColumnDef> getColumnById(@RequestParam (value="pkId") String pkId){
        JsonResult result=JsonResult.Success();
        result.setShow(false);
        if(ObjectUtils.isEmpty(pkId)){
            return result.setData(new Object());
        }
        InsColumnDef insColumnDef=insColumnDefService.get(pkId);
        handleData(insColumnDef);
        InsColumnTemp insColumnTemp = insColumnTempService.get(insColumnDef.getType());
        if (insColumnTemp != null && StringUtils.isNotEmpty(insColumnTemp.getTempType())) {
            insColumnDef.setTypeName(insColumnTemp.getTempType());
        }
        return result.setData(insColumnDef);
    }

}
