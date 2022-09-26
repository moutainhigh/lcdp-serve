package com.redxun.common.model;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @Author: zfh
 * @Date: 2021/10/08 10:32
 *
 * 分公司管理
 */

@Setter
@Getter
public class Company implements Serializable {

    private static final long serialVersionUID = 1001L;

    public Company(){

    }

    public Company(String id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * 公司id
     */
    private String id;


    /**
     * 公司名称
     */
    private String name;

}
