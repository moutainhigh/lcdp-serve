
package com.redxun.form.core.controller;

import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.form.core.entity.FormEntityDataSettingDic;
import com.redxun.form.core.service.FormEntityDataSettingDicServiceImpl;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.tool.StringUtils;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/form/core/formEntityDataSettingDic")
@Api(tags = "业务实体数据配置字典")
@ClassDefine(title = "业务实体数据配置字典",alias = "FormEntityDataSettingDicController",path = "/form/core/formEntityDataSettingDic",packages = "core",packageName = "表单管理")
public class FormEntityDataSettingDicController extends BaseController<FormEntityDataSettingDic> {

@Autowired
FormEntityDataSettingDicServiceImpl formEntityDataSettingDicService;


@Override
public BaseService getBaseService() {
return formEntityDataSettingDicService;
}

@Override
public String getComment() {
return "业务实体数据配置字典";
}

}

