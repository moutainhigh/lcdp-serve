package com.redxun.portal.core.controller;

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
import com.redxun.log.model.Audit;
import com.redxun.log.model.LogContext;
import com.redxun.portal.core.entity.InsMsgboxBox;
import com.redxun.portal.core.entity.InsMsgboxDef;
import com.redxun.portal.core.service.InsMsgboxBoxServiceImpl;
import com.redxun.portal.core.service.InsMsgboxDefServiceImpl;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("/portal/core/insMsgboxDef")
@ClassDefine(title = "消息盒子定义",alias = "insMsgboxDefController",path = "/portal/core/insMsgboxDef",packages = "core",packageName = "门户管理")
@Api(tags = "消息盒子定义")
public class InsMsgboxDefController extends BaseController<InsMsgboxDef> {

    @Autowired
    InsMsgboxDefServiceImpl insMsgboxDefService;
    @Autowired
    InsMsgboxBoxServiceImpl insMsgboxBoxService;

    @Override
    public BaseService getBaseService() {
        return insMsgboxDefService;
    }

    @Override
    public String getComment() {
        return "消息盒子定义";
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

    @MethodDefine(title = "保存消息盒子定义", path = "/saveOne", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "数据对象", varName = "insMsgboxDef")})
    @ApiOperation(value = "保存消息盒子定义", notes = "保存数据")
    @AuditLog(operation = "保存消息盒子定义")
    @PostMapping("/saveOne")
    public JsonResult<String> saveOne(@ApiParam @RequestBody InsMsgboxDef insMsgboxDef) {
        String boxId = insMsgboxDef.getBoxId();
        String msgIds = insMsgboxDef.getColId();
        String appId=insMsgboxDef.getAppId();
        boolean result = insMsgboxDefService.isExist(insMsgboxDef);
        if (result) {
            String detail="数据已经存在key【" + insMsgboxDef.getKey() + "】!";
            LogContext.addError(detail);
            return JsonResult.Fail("数据已经存在key【" + insMsgboxDef.getKey() + "】!").setShow(false);
        }

        if (StringUtils.isEmpty(boxId)) {
            boxId = IdGenerator.getIdStr();
            insMsgboxDef.setBoxId(boxId);
            insMsgboxDef.setColId("");
            insMsgboxDefService.insert(insMsgboxDef);
            creatImsBoxBox(msgIds, boxId,appId);

            String detail="添加消息盒子:" +insMsgboxDef.getName() +"("+boxId+")";
            LogContext.put(Audit.DETAIL,detail);
            LogContext.put(Audit.PK,boxId);

            return JsonResult.Success().setData("保存成功！");
        } else {
            insMsgboxDefService.update(insMsgboxDef);
            insMsgboxBoxService.delByBoxId(boxId);
            creatImsBoxBox(msgIds, boxId,appId);

            String detail="更新消息盒子:" +insMsgboxDef.getName() +"("+boxId+")";
            LogContext.put(Audit.DETAIL,detail);
            LogContext.put(Audit.PK,boxId);
        }
        return JsonResult.Success().setData("保存成功！");
    }

    private void creatImsBoxBox(String msgIds, String boxId,String appId) {
        if (StringUtils.isEmpty(msgIds)) {
            return;
        }
        String[] msgIdsList = msgIds.split("[,]");
        int i = 0;
        List<String> list= Arrays.asList(msgIdsList);
        for (String msgId : msgIdsList) {
            InsMsgboxBox insMsgboxBox = new InsMsgboxBox();
            insMsgboxBox.setId(IdGenerator.getIdStr());
            insMsgboxBox.setBoxId(boxId);
            insMsgboxBox.setMsgId(msgId);
            insMsgboxBox.setAppId(appId);
            insMsgboxBox.setSn(i);
            insMsgboxBoxService.insert(insMsgboxBox);
            i++;
        }
    }
}
