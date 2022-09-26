package com.redxun.system.script;

import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.script.GroovyScript;
import com.redxun.common.utils.SpringUtil;
import com.redxun.system.core.service.SysWebReqDefServiceImpl;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author lenovo
 */
@Component
public class SqlScript implements GroovyScript {

    public String doExecuteWebReq(String alias, Map<String, String> headerMap, Map<String, String> paramsMap, String body) {
        try {
            SysWebReqDefServiceImpl manager = SpringUtil.getBean(SysWebReqDefServiceImpl.class);
            JsonResult jsonResult = manager.previewReturn(alias, headerMap, paramsMap, body);
            return (String) jsonResult.getData();
        } catch (Exception e) {
            return "";
        }
    }

}
