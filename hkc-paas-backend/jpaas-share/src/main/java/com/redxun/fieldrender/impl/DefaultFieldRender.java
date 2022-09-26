package com.redxun.fieldrender.impl;

import com.redxun.fieldrender.IFieldRender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 功能: 默认字段渲染器。
 *
 * @author ray
 * @date 2022/5/17 10:01
 */
@Component
@Slf4j
public class DefaultFieldRender implements IFieldRender {


    @Override
    public List<String> getControl() {
        return null;
    }

    @Override
    public String render(String val) {
        return val;
    }
}
