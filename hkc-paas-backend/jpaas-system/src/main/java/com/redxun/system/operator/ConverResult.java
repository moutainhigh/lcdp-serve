package com.redxun.system.operator;

import lombok.Getter;
import lombok.Setter;

/**
 * PDF转换结果对象
 */
@Getter
@Setter
public class ConverResult {

    /**
     * 转换是否成功，true是，false否
     */
    private  Boolean success = false;

    /**
     * 源文件是否存在，true是，false否
     */
    private  Boolean fileNotFind = false;

    /**
     * 原因，如转换失败的原因
     */
    private  String reason;

    /**
     * PDF文件地址
     */
    private String  path;

}
