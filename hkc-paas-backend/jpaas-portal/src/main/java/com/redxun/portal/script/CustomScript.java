package com.redxun.portal.script;

import com.redxun.common.model.JPaasUser;
import com.redxun.common.script.GroovyScript;
import com.redxun.common.utils.ContextUtil;
import org.springframework.stereotype.Component;

/**
 * 自定义脚本处理器
 */
@Component
public class CustomScript  implements GroovyScript {

    /**
     * customScript.getCurentUser();
     * @return
     */
    public JPaasUser getCurentUser(){
        JPaasUser jPaasUser= (JPaasUser) ContextUtil.getCurrentUser();
        return jPaasUser;
    }
}
