package com.redxun.bpm.activiti.config;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Setter
@Getter
public class FormConfig implements Serializable {

    private List<Form> formpc;

    private List<Form> mobile;
}
