package com.redxun.system.ext.handler.impl;

import com.redxun.system.ext.handler.ApiReturnDataHandler;
import org.springframework.stereotype.Component;

/**
 * 默认处理器
 */
@Component
public class DefaultApiReturnDataHandler implements ApiReturnDataHandler {

    @Override
    public String getName() {
        return "默认处理器";
    }

    @Override
    public String analysisText(String data) {
        return data;
    }
}
