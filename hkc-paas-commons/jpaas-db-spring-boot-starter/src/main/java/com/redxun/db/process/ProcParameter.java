package com.redxun.db.process;

import lombok.Getter;
import lombok.Setter;

/**
 * 存储过程参数类
 */
@Getter
@Setter
public class ProcParameter {

    ProcParameter(){

    }

    /**
     * 输入参数定义。
     * @param parameterType
     * @param value
     */
    public ProcParameter(ParameterType parameterType, Object value) {
        this.parameterType = parameterType;
        this.value = value;
    }

    /**
     * 输出参数
     * @param parameterType
     */
    public ProcParameter(ParameterType parameterType) {
        this.parameterType = parameterType;
        this.setInput(false);
    }

    /**
     * 参数输入输出类型。
     */
    private boolean input=true;

    /**
     * 输入参数类型。
     */
    private ParameterType parameterType;

    /**
     * 参数值用于输入参数。
     */
    private Object value;

    /**
     * 输出参数类型。
     */
    private int sqlType;


}
