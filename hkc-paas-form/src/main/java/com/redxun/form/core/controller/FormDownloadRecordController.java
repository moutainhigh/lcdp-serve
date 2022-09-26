
package com.redxun.form.core.controller;

import com.redxun.common.base.db.BaseService;
import com.redxun.form.core.entity.FormDownloadRecord;
import com.redxun.form.core.service.FormDownloadRecordServiceImpl;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.tool.StringUtils;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.redxun.common.annotation.ClassDefine;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/form/core/formDownloadRecord")
@Api(tags = "Excel下载记录(异步)")
@ClassDefine(title = "Excel下载记录(异步)",alias = "FormDownloadRecordController",path = "/form/core/formDownloadRecord",packages = "core",packageName = "子系统名称")

public class FormDownloadRecordController extends BaseController<FormDownloadRecord> {

@Autowired
FormDownloadRecordServiceImpl formDownloadRecordService;


@Override
public BaseService getBaseService() {
return formDownloadRecordService;
}

@Override
public String getComment() {
return "Excel下载记录(异步)";
}

}

