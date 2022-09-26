package com.redxun.form.core.controller;

import com.alibaba.fastjson.JSONArray;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.model.JPaasUser;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ContextUtil;
import com.redxun.feign.BpmDefClient;
import com.redxun.feign.BpmTaskClient;
import com.redxun.feign.org.UserClient;
import com.redxun.form.core.datahandler.IDataHandler;
import com.redxun.form.core.importdatahandler.ImportDataHandlerExecutor;
import com.redxun.form.core.service.FormSolutionServiceImpl;
import com.redxun.util.SysUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/form/core/public")
@Api(tags = "公共API")
public class PublicController {

    @Resource
    FormSolutionServiceImpl formSolutionService;
    @Autowired
    IDataHandler dataHandler;
    @Resource
    UserClient userClient;


    @Resource
    private ImportDataHandlerExecutor importDataHandlerExecutor;

    /**
     * 获取APP的所有服务
     * @return
     */
    @GetMapping("/getAppServices")
    public List<String> getAppServices(){
        return SysUtil.getAppServices();
    }

    @PostMapping("/getParams")
    public Map<String,Object> getParams(@RequestBody String setting){
        if(StringUtils.isEmpty(setting)){
            return new HashMap<>();
        }
        return formSolutionService.getParams(JSONArray.parseArray(setting));
    }



    /**
     * 获取表单数据导入处理器
     * @return
     */
    @GetMapping("/getImportDataHandlers")
    public JSONArray getBpmHandlers() {
        JSONArray list = importDataHandlerExecutor.getHandlers();
        return list;
    }
}
