package com.redxun.profile;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 用户与用户组策略类型
 */
@Setter
@Getter
public class ProfileType implements Serializable {

    public ProfileType() {
    }

    public ProfileType(String type, String name) {
        this.type = type;
        this.name = name;
    }

    private  String type="";



    private String name="";


}
