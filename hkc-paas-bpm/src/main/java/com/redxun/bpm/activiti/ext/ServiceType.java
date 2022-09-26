package com.redxun.bpm.activiti.ext;

import lombok.Data;

/**
 * 服务类型
 */
@Data
public class ServiceType {

    public ServiceType(){

    }

    public ServiceType(String title,String type){
        this.title=title;
        this.type=type;
    }

    /**
     * 类型名称
     */
    String title;
    /**
     * 服务类型
     */
    String type;

}
