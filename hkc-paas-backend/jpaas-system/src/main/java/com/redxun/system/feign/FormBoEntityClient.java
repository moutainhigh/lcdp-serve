package com.redxun.system.feign;

import com.alibaba.fastjson.JSONObject;
import com.redxun.dto.form.FormBoEntityDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author hujun
 */
@FeignClient(name = "jpaas-form")
public interface FormBoEntityClient {
    /**
     * 根据BoDefId获取单据实体
     * @param boDefId
     * @return
     */
    @GetMapping("/form/bo/formBoDef/getBoIdFields")
    FormBoEntityDto getBoIdFields(@RequestParam(value="boDefId")String boDefId);

    /**
     * 根据boDefId和主键获取JSON数据
     * @param boDefId
     * @param pk
     * @return
     */
    @GetMapping("/form/bo/formBoDef/getDataByBoDef")
    JSONObject getDataByBoDef(@RequestParam(value="boDefId")String boDefId,@RequestParam(value = "pk")String pk);
}
