package com.redxun.system.feign;

import com.alibaba.fastjson.JSONObject;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.dto.form.FormSolutionDto;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "jpaas-form")
public interface FormClient {
    /**
     * @Description: 删除本数据库中应用相关的数据
     * @param appId 应用ID
     **/
    @PostMapping( "/form/core/appdata/delete")
    void delete( @RequestParam("appId")  String appId);

    /**
     * @Description:  查询本数据库中应用相关的数据
     * @param appId 应用ID
     **/
    @GetMapping( "/form/core/appdata/query")
    Map<String,String> query(@RequestParam("appId") String appId,@RequestParam("exportCustomTables") boolean exportCustomTables);

    /**
     * @Description: 导入本数据库中应用相关的数据
     * @param data 应用相关数据
     **/
    @PostMapping( value = "/form/core/appdata/install",consumes = "application/json;charset=UTF-8")
    JsonResult install( @RequestBody String data);

    /**
     * @Description: 应用导入前数据校验
     * @param array 应用相关表记录数组
     **/
    @PostMapping( value = "/form/core/appdata/importCheck",consumes = "application/json;charset=UTF-8",produces = {"application/json;charset=UTF-8"})
    JsonResult importCheck(@ApiParam @RequestBody Object[]  array);

    @PostMapping(value = "/form/core/formSolution/saveForm")
    JsonResult saveForm(@RequestBody JSONObject jsonObject);

    @PostMapping(value = "/form/core/formSolution/removeById")
    JsonResult removeById(@RequestBody JSONObject jsonObject);

    @PostMapping(value = "/form/core/formSolution/getByAlias")
    JsonResult getByAlias(@RequestParam("alias") String alias,@RequestParam(value = "pk",required = false) String pk,@RequestBody(required = false) JSONObject jsonParams);

    @GetMapping(value = "/form/core/formSolution/getFormSolutionByAlias")
    FormSolutionDto getFormSolutionByAlias(@RequestParam("alias") String alias);

    @PostMapping(value = "/form/core/formDataSourceDef/executeSql")
    JsonResult executeSql(@RequestParam(value="alias")String alias, @RequestParam(value="sql")String sql,@RequestBody Map<String, Object> resultMap);

    @PostMapping(value = "/form/core/formExcelGenTask/updateExcelGenTask")
    JsonResult updateExcelGenTask(@RequestBody JSONObject jsonObject);
}
