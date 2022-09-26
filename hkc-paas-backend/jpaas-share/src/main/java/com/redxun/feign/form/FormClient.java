package com.redxun.feign.form;

import com.alibaba.fastjson.JSONObject;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.dto.bpm.BpmInstDataDto;
import com.redxun.dto.form.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 单据微应用中的客户端
 */
@FeignClient(name = "jpaas-form")
public interface FormClient {
    /**
     * 根据别名获取PC单据
     * @param alias
     * @return
     */
    @GetMapping("/form/core/formPc/getFormPcByAlias")
    FormPcDto getFormPcByAlias(@RequestParam(value = "alias") String alias);

    /**
     * 获取手机端的单据
     * @param alias
     * @return
     */
    @GetMapping("/form/core/formMobile/getMobleFormByAlias")
    FormMobileDto getMobleFormByAlias(@RequestParam(value = "alias") String alias);

    /**
     * 通过别名获取单据的业务BO定义
     * @param alias
     * @return
     */
    @GetMapping("/form/bo/formBoDef/getFormBoDefByAlias")
    Object getFormBoDefByAlias(@RequestParam(value = "alias") String alias);

    /**
     * 导入PC单据
     * @param bpmFormJson
     */
    @PostMapping("/form/core/formPc/importFormPc")
    void importFormPc(@RequestBody JSONObject bpmFormJson);

    /**
     * 导入移动单据
     * @param formMobileJson
     */
    @PostMapping("/form/core/formMobile/importMobileForm")
    void importMobileForm(@RequestBody JSONObject formMobileJson);

    /**
     * 导入单据单据BO定义
     * @param sysBoDefJson
     * @return
     */
    @PostMapping("/form/bo/formBoDef/importFormBoDef")
    List<AlterSql> importFormBoDef(@RequestBody JSONObject sysBoDefJson);

    /**
     * 根据表单别名和节点权限获取表单相关数据。
     * @param paramsList
     * @return
     */
    @PostMapping ("/form/core/formPc/getByAliasAndPermisson")
    JsonResult<List<BpmView>> getByAliasAndPermisson(@RequestBody List<FormParams> paramsList);

    /**
     * 通过别名获取移动端单据跟权限
     * @param paramsList
     * @return
     */
    @PostMapping ("/form/core/formPc/getMobileByAliasAndPermisson")
    JsonResult<List<BpmView>> getMobileByAliasAndPermisson(@RequestBody List<FormParams> paramsList);

    /**
     * 表单数据保存。
     * @param data
     * @return
     */
    @PostMapping("/form/core/formPc/saveFormData")
    JsonResult<List<DataResult>> saveFormData(@RequestBody JSONObject data);


    /**
     * 获取单据别名获取所有的单据实体的所有别名
     * @param alias {alias:{boAlias:"",entMap:{alias:'main'}}}
     * @return
     */
    @PostMapping("/form/bo/formBoEntity/getAliasByFormAlias")
    String getAliasByFormAlias(@RequestParam(value = "alias") String alias);

    /**
     * 通过数据库别名跟SQL返回数据
     * @param alias
     * @param sql
     * @return
     */
    @GetMapping("/form/core/formDataSourceDef/getDataByAliasAndSql")
    List getDataByAliasAndSql(@RequestParam(value = "alias") String alias,
                                     @RequestParam(value = "sql") String sql);

    /**
     * 通过Bo定义ID获取所有业务BO实体
     * @param boDefId
     * @return
     */
    @GetMapping("/form/bo/formBoDef/getBosByDefId")
    JsonResult getBosByDefId(@RequestParam(value = "boDefId") String boDefId);

    /**
     * 获取bo及子bo的字段等信息
     * @param boAlias
     * @return
     */
    @GetMapping("/form/bo/formBoDef/getBoFields")
    FormBoEntityDto getBoFields(@RequestParam(value = "boAlias") String boAlias);

    /**
     * 更新表单数据的状态。
     * @param bpmInstDataDtos
     * @return
     */
    @PostMapping("/form/core/formPc/updStatusByInstId")
    JsonResult updStatusByInstId(@RequestBody List<BpmInstDataDto> bpmInstDataDtos);

    /**
     * 通过别名获取所有流程中的所有的单据
     * @param alias 单据别名
     * @param pk 主键
     * @param initPermission 是否初始化权限
     * @return
     */
    @GetMapping("/form/core/formPc/getByAlias")
    JsonResult<BpmView> getByAlias(@RequestParam(value = "alias") String alias,@RequestParam(value = "pk")  String pk,@RequestParam(value = "initPermission") Boolean initPermission);


    /**
     * 通过feign调用自定义查询。
     * @param queryParam 参数为: alias,params,deploy
     * @return
     */
    @RequestMapping("/form/core/formCustomQuery/queryForJson")
    JsonResult queryForJson(@RequestBody MultiValueMap<String,String> queryParam);


    /**
     * 根据bo别名和主键获取业务数据。
     * {boAlias1:pk1,boAlias2:pk2}
     * @return
     */
    @RequestMapping("/form/bo/formBoDef/getByBpmInstData")
    JSONObject getByBpmInstData(@RequestBody JSONObject keyPkJson);

}

