
package com.redxun.system.core.controller;

import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.system.core.entity.ImportExcelBat;
import com.redxun.system.core.service.ImportExcelBatServiceImpl;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/system/core/importExcelBat")
@Api(tags = "Excel批次管理")
@ClassDefine(title = "Excel批次管理",alias = "importExcelBatController",path = "/system/core/importExcelBat",packages = "core",packageName = "系统管理")
public class ImportExcelBatController extends BaseController<ImportExcelBat> {

    @Autowired
    ImportExcelBatServiceImpl importExcelBatService;


    @Override
    public BaseService getBaseService() {
    return importExcelBatService;
    }

    @Override
    public String getComment() {
    return "Excel批次管理";
    }

}

