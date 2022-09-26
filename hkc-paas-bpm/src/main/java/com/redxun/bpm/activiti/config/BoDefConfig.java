package com.redxun.bpm.activiti.config;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Bo定义配置
 */
@Getter
@Setter
public class BoDefConfig implements Serializable {

    /**
     * bo定义设置。
     */
    private  String value="";

    /**
     * bo定义文本
     */
    private  String text="";

}
