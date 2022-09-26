package com.redxun.system.messagehandler;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 消息类型定义
 */
@Setter
@Getter
public class MessageType implements Serializable {

    public MessageType(){

    }

    public MessageType(String type,String name){
        this.type=type;
        this.name=name;
    }

    /**
     * 消息类型
     */
    private String type="";

    /**
     * 消息类型名称
     */
    private String name="";
}
