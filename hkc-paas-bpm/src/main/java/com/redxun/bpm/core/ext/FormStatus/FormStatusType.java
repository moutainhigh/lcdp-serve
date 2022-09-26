package com.redxun.bpm.core.ext.FormStatus;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FormStatusType {

    public FormStatusType(){
    }

    public FormStatusType(String type, String name){
        this.name=name;
        this.type=type;
    }

    private String type="";
    private String name="";

}
