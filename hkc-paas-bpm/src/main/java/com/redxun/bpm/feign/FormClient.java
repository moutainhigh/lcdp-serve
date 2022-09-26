package com.redxun.bpm.feign;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.dto.bpm.BpmInstDataDto;
import com.redxun.dto.form.FormBoDefDto;
import com.redxun.dto.form.FormBoEntityDto;
import com.redxun.dto.form.FormSolutionDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "jpaas-form")
public interface FormClient {

    @GetMapping("/form/core/formSolution/getFormSolutionByAlias")
    FormSolutionDto getFormSolutionByAlias(@RequestParam(value = "alias")String alias);

    /**
     * 获取表单数据
     * @param json
     * @return
     */
    @PostMapping("/form/core/formTableFormula/getTableFieldValueHandler")
    JSONObject getTableFieldValueHandler(@RequestBody JSONObject json);

    /**
     * 获取业务实体
     * @param entId
     * @return
     */
    @GetMapping("/form/bo/formBoEntity/getBoEntById")
    FormBoEntityDto getBoEntById(@RequestParam(value = "entId") String entId);

    /**
     * 通过表名获取业务实体
     * @param tableName
     * @return
     */
    @GetMapping("/form/bo/formBoEntity/getBoEntByTableName")
    FormBoEntityDto getBoEntByTableName(@RequestParam(value = "tableName")String tableName);

    /**
     * 删除流程实例修改表单数据流程状态
     * @param jsonArray
     * @return
     */
    @PostMapping("/form/bo/formBoEntity/setFormInstStatus")
    JsonResult setFormInstStatus(@RequestBody JSONArray jsonArray);

    @PostMapping({"/form/core/formPc/updStatusByInstId"})
    JsonResult updStatusByInstId(@RequestBody List<BpmInstDataDto> var1);

    /**
     * 通过主键获取通知模板
     * @param pkId
     * @return
     */
    @GetMapping("/form/bo/formBoDef/getById")
    FormBoDefDto getBoDefById(@RequestParam(value="pkId")String pkId);

}
