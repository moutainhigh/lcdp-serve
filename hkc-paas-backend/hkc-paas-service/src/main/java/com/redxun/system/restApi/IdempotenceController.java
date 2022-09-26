package com.redxun.system.restApi;

import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.utils.SpringUtil;
import com.redxun.util.IdempotenceUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/system/idempotence")
public class IdempotenceController {
    /**
     * 生成幂等号
     * @return
     */
    @GetMapping("/generateId")
    public JsonResult generateId(){
        IdempotenceUtil idempotenceUtil=SpringUtil.getBean(IdempotenceUtil.class);
        String uId=idempotenceUtil.generateId();
        return JsonResult.getSuccessResult("成功生成！").setData(uId);
    }
}
