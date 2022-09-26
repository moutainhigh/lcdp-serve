package com.redxun.form.core.entity;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ValueResult {



    private boolean exist=true;

    private Object value;

    public static ValueResult exist(Object value){
        ValueResult result=new ValueResult();
        result.setExist(true);
        result.setValue(value);
        return result;
    }

    public static ValueResult noExist(){
        ValueResult result=new ValueResult();
        result.setExist(false);
        return result;
    }

}
