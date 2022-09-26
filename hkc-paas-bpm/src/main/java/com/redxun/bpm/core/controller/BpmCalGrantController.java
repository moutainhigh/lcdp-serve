package com.redxun.bpm.core.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.redxun.bpm.core.entity.BpmCalGrant;
import com.redxun.bpm.core.service.BpmCalGrantServiceImpl;
import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.tool.IdGenerator;
import com.redxun.log.annotation.AuditLog;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/bpm/core/bpmCalGrant")
@ClassDefine(title = "日历分配",alias = "bpmCalGrantController",path = "/bpm/core/bpmCalGrant",packages = "core",packageName = "流程管理")
@Api(tags = "日历分配")
public class BpmCalGrantController extends BaseController<BpmCalGrant> {

    @Autowired
    BpmCalGrantServiceImpl bpmCalGrantService;

    @Override
    public BaseService getBaseService() {
        return bpmCalGrantService;
    }

    @Override
    public String getComment() {
        return "日历分配";
    }

    @MethodDefine(title = "批量保存新增的分配用户/用户组", path = "/saveAll", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "保存数据", varName = "data")})
    @ApiOperation(value = "批量保存新增的分配用户/用户组")
    @PostMapping("/saveAll")
    @AuditLog(operation = "日历分配授权")
    @ResponseBody
    public JsonResult saveAll(@RequestBody JSONObject data) {
        JSONArray addList = data.getJSONArray("addList");
        for(int i=0;i<addList.size();i++){
            JSONObject addData = addList.getJSONObject(i);
            addOne(addData);
        }
        return JsonResult.Success();
    }

    private  void addOne(JSONObject addData){
        BpmCalGrant bpmCalGrant = new BpmCalGrant();
        bpmCalGrant.setPkId(IdGenerator.getIdStr());
        bpmCalGrant.setGrantType(addData.getString("grantType"));
        bpmCalGrant.setBelongWho(addData.getString("belongWho"));
        bpmCalGrant.setBelongWhoId(addData.getString("belongWhoId"));
        bpmCalGrant.setSettingId(addData.getString("settingId"));
        bpmCalGrantService.insert(bpmCalGrant);
    }
}
