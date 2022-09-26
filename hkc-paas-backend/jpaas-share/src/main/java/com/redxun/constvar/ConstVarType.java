package com.redxun.constvar;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 用于自定义SQL中的变量类型
 */
@Getter
@Setter
public class ConstVarType implements Serializable {


    /**
     * key名称
     * [USERID]
     * @return
     */
    private String key;

    /**
     * 名称。
     * @return
     */
    private String name;

    public ConstVarType() {
    }

    public ConstVarType(String key, String name) {
        this.key = key;
        this.name = name;
    }
}
