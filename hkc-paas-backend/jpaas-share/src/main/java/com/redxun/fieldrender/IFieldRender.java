package com.redxun.fieldrender;

import java.util.List;

/**
 * 功能: 字段渲染接口。
 * @author ASUS
 * @date 2022/5/17 9:56
 */
public interface IFieldRender {

    List<String> getControl();


    /**
     * 渲染
     * @param val
     * @return
     */
    String render(String val);


}
