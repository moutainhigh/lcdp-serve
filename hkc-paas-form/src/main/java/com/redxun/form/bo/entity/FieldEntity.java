package com.redxun.form.bo.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class FieldEntity {

    private String fieldName="";

    private Object value;

    private String name;

}
