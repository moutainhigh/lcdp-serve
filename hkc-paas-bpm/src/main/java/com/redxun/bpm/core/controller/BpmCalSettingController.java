package com.redxun.bpm.core.controller;

import com.redxun.bpm.core.entity.BpmCalSetting;
import com.redxun.bpm.core.service.BpmCalSettingServiceImpl;
import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.tool.BeanUtil;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("/bpm/core/bpmCalSetting")
@ClassDefine(title = "日历设定",alias = "bpmCalSettingController",path = "/bpm/core/bpmCalSetting",packages = "core",packageName = "流程管理")
@Api(tags = "日历设定")
public class BpmCalSettingController extends BaseController<BpmCalSetting> {

    @Autowired
    BpmCalSettingServiceImpl bpmCalSettingService;

    @Override
    public BaseService getBaseService() {
        return bpmCalSettingService;
    }

    @Override
    public String getComment() {
        return "日历设定";
    }

    @Override
    protected JsonResult beforeSave(BpmCalSetting ent){
        if(BpmCalSetting.IS_COMMON_YES.equals(ent.getIsCommon())){
            BpmCalSetting bpmCalSetting = bpmCalSettingService.getIsCommon(BpmCalSetting.IS_COMMON_YES);
            if(BeanUtil.isNotEmpty(bpmCalSetting)){
                bpmCalSetting.setIsCommon(BpmCalSetting.IS_COMMON_NO);
                bpmCalSettingService.update(bpmCalSetting);
            }
        }
        return JsonResult.Success();
    }
}