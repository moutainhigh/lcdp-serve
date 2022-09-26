package com.redxun.portal.core.controller;

import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.base.search.QueryFilter;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.script.GroovyEngine;
import com.redxun.common.utils.ContextUtil;
import com.redxun.common.utils.DbLogicDelete;
import com.redxun.portal.context.remind.impl.RemindDataImpl;
import com.redxun.portal.core.entity.InsRemindDef;
import com.redxun.portal.core.service.InsPortalPermissionServiceImpl;
import com.redxun.portal.core.service.InsRemindDefServiceImpl;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/portal/core/insRemindDef")
@ClassDefine(title = "消息提醒",alias = "insRemindDefController",path = "/portal/core/insRemindDef",packages = "core",packageName = "门户管理")
@Api(tags = "消息提醒")
public class InsRemindDefController extends BaseController<InsRemindDef> {

    @Autowired
    InsRemindDefServiceImpl insRemindDefService;
    @Autowired
    RemindDataImpl remindDataServe;
    @Resource
    private GroovyEngine groovyEngine;
    @Resource
    private InsPortalPermissionServiceImpl insPortalPermissionService;

    @Override
    public BaseService getBaseService() {
        return insRemindDefService;
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
        return "消息提醒";
    }

    @MethodDefine(title = "根据栏目Id获取数据", path = "/myRemind", method = HttpMethodConstants.GET)
    @ApiOperation(value = "根据栏目Id获取数据")
    @GetMapping("/myRemind")
    public JsonResult myRemind() {
        List<InsRemindDef> list = insRemindDefService.getOwnInsColumnDef();
        List<InsRemindDef> returnList = new ArrayList<>();
        try {
            for (InsRemindDef remind:list) {
                if(!InsRemindDef.TYPE_ENABLED.equals(remind.getEnabled())){
                    continue;
                }
                int count =0;
                if (InsRemindDef.SQL_TYPE.equals(remind.getType())) {
                    count = remindDataServe.getCountBySql(remind.getSetting(), remind.getDsAlias());
                }
                //支持查找多个数据
                else if (InsRemindDef.FUNCTION_TYPE.equals(remind.getType())) {
                    Map<String, Object> params = new HashMap<String, Object>();
                    String function = remind.getSetting();
                    params.put("colId", InsRemindDef.INS_REMIND_DEF);

                    count =(Integer)  groovyEngine.executeScripts(function, params);

                }
                if(count==0){
                    continue;
                }
                remind.setCount(count);
                String description = remind.getDescription();
                String countStr = "<i class=\"remindCalss\">"+String.valueOf(count)+"</i>";
                description=description.replace("[count]",countStr);
                remind.setDescription(description);
                returnList.add(remind);
            }
        } catch (Exception e) {
            log.error("----InsRemindDefController.myRemind() is error -----:" + e.getMessage());
        }
        return JsonResult.Success().setData(returnList);
    }

    /**
     * 解决删除提醒的同时删除授权。
     * @param list
     * @return
     */
    @Override
    protected JsonResult beforeRemove(List<String> list) {
        for(String layoutId:list){
            insPortalPermissionService.delByLayoutId(layoutId);
        }
        return JsonResult.Success();
    }
}
