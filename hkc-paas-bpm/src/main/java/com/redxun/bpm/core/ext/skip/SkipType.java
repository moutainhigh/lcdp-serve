package com.redxun.bpm.core.ext.skip;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SkipType {

    public SkipType(){
    }

    public SkipType(String type,String name){
        this.name=name;
        this.type=type;
    }

    private String type="";
    private String name="";

}
