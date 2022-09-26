package com.redxun.user.org.controller;

import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.ContextQuerySupport;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.constant.BooleanConstants;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.model.JPaasUser;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.Result;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ContextUtil;
import com.redxun.dto.user.OsDimensionDto;
import com.redxun.user.org.entity.OsDimension;
import com.redxun.user.org.service.OsDimensionServiceImpl;
import com.redxun.user.org.service.OsGroupServiceImpl;
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

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;


/**
 * 用户组维度 提供者
 *
 * @author yjy
 * @date 2019-11-08
 */
@Slf4j
@RestController
@RequestMapping("/user/org/osDimension")
@ClassDefine(title = "用户组维度",alias = "osDimensionController",path = "/user/org/osDimension",packages = "org",packageName = "组织架构")
@Api(tags = "用户组维度")
@ContextQuerySupport(tenant = ContextQuerySupport.BOTH,company = ContextQuerySupport.BOTH)
public class OsDimensionController extends BaseController<OsDimension> {

    @Autowired
    OsDimensionServiceImpl osDimensionServiceImpl;

    @Autowired
    OsGroupServiceImpl osGroupServiceImpl;

    @Override
    public BaseService getBaseService() {
        return osDimensionServiceImpl;
    }

    @Override
    public String getComment() {
        return "用户组维度";
    }



    /**
     * 查询维度列表
     */
    @MethodDefine(title = "查询维度列表", path = "/getDimList", method = HttpMethodConstants.GET)
    @ApiOperation(value = "查询维度列表")
    @GetMapping("/getDimList")
    public Result getDimList(HttpServletRequest request) {
        ContextQuerySupport support=getContextQuerySupport();
        List<OsDimension> model = osDimensionServiceImpl.findList(support);
        return Result.succeed(model);
    }

    /**
     * 按照维度Code查询维度
     */
    @MethodDefine(title = "按照维度Code查询维度", path = "/getDimensionByCode", method = HttpMethodConstants.GET)
    @ApiOperation(value = "按照维度Code查询维度")
    @GetMapping("/getDimensionByCode")
    public OsDimensionDto getDimensionByCode(@ApiParam @RequestParam(value = "code") String dimCode) {
        JPaasUser curUser = (JPaasUser) ContextUtil.getCurrentUser();
        String tenantId = curUser.getTenantId();
        OsDimension dimension = osDimensionServiceImpl.getByCodeTenantId(dimCode,tenantId);
        OsDimensionDto dto=new OsDimensionDto();
        if(dimension!=null){
            try{
                BeanUtil.copyNotNullProperties(dto,dimension);
            }catch (Exception exception){
                exception.printStackTrace();
            }
        }
        return dto;
    }

    @MethodDefine(title = "查询维度", path = "/getByDimIds", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "维度ID集合", varName = "dimIds")})
    @ApiOperation(value="查询维度")
    @GetMapping("/getByDimIds")
    public Result getByDimIds(@RequestParam(value = "dimIds") String dimIds){
        List<OsDimension> model = osDimensionServiceImpl.getByDimIds(Arrays.asList(dimIds.split(",")));
        return Result.succeed(model);
    }

    @Override
    protected JsonResult beforeSave(OsDimension ent) {
        //数据新增时才进行租户ID变更
        if(StringUtils.isEmpty(ent.getPkId())) {
            String tenantId = ContextUtil.getCurrentTenantId();
            ent.setTenantId(tenantId);
        }
        boolean isExist = osDimensionServiceImpl.isExist(ent);
        if(isExist){
            return JsonResult.Fail("编码存在相同！");
        }
        if(StringUtils.isEmpty(ent.getDimId())){
            ent.setIsSystem(BooleanConstants.YES.name());
        }
        return  JsonResult.Success();
    }
}
