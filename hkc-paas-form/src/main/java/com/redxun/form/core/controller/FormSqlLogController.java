
package com.redxun.form.core.controller;

import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.KeyValEnt;
import com.redxun.common.base.search.QueryFilter;
import com.redxun.common.base.search.SortParam;
import com.redxun.form.core.entity.FormSqlLog;
import com.redxun.form.core.service.FormSqlLogServiceImpl;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.tool.StringUtils;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/form/core/formSqlLog")
@Api(tags = "SQL执行日志")
public class FormSqlLogController extends BaseController<FormSqlLog> {

    @Autowired
    FormSqlLogServiceImpl formSqlLogService;


    @Override
    public BaseService getBaseService() {
        return formSqlLogService;
    }

    @Override
    public String getComment() {
        return "SQL执行日志";
    }

    @Override
    protected void handleFilter(QueryFilter filter) {
        filter.addSortParam(new SortParam("CREATE_TIME_","DESC"));
        super.handleFilter(filter);
    }

    @GetMapping("/getTypeList")
    public List<KeyValEnt> getTypeList(){
        List<KeyValEnt> list=new ArrayList<>();
        list.add(new KeyValEnt(FormSqlLog.TYPE_FORM_BO_LIST,"数据列表"));
        list.add(new KeyValEnt(FormSqlLog.TYPE_FORM_TABLE_FORMULA,"表间公式"));
        return list;
    }

    @GetMapping("/delByType")
    public JsonResult delByType(@RequestParam(value = "type") String type) {
        formSqlLogService.delByType(type);
        return JsonResult.getSuccessResult("清空日志成功！");
    }

}



