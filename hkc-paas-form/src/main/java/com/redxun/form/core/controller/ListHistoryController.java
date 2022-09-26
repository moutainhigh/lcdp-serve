
package com.redxun.form.core.controller;

import com.redxun.common.base.db.BaseService;
import com.redxun.form.core.entity.ListHistory;
import com.redxun.form.core.service.ListHistoryServiceImpl;
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
@RequestMapping("/form/core/listHistory")
@Api(tags = "单据实体列表历史")
public class ListHistoryController extends BaseController<ListHistory> {

    @Autowired
    ListHistoryServiceImpl listHistoryService;


    @Override
    public BaseService getBaseService() {
        return listHistoryService;
    }

    @Override
    public String getComment() {
        return "单据实体列表历史";
    }

}

