package com.redxun.form.core.listener;

import com.redxun.form.core.entity.FormTableFormula;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class FormulaSetting {

    /**
     * 流程
     */
    public static String FLOW="flow";

    /**
     * 表单方案
     */
    public static String FORM="form";

    public FormulaSetting(){
        this.extParams.put("mode",FORM);
    }

    public FormulaSetting(List<FormTableFormula> formulas,Map<String,Object> extParams){
        this.formulaList=formulas;
        this.extParams=extParams;
    }

    private List<FormTableFormula> formulaList;

    /**
     * 扩展参数。
     */
    private Map<String,Object> extParams=new HashMap<>();


}
