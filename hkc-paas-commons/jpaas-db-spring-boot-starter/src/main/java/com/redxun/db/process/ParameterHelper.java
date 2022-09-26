package com.redxun.db.process;

import java.util.ArrayList;
import java.util.List;

/**
 * 构造参数类。
 */
public class ParameterHelper {

    List<ProcParameter> parameterList=new ArrayList<>();

    public ParameterHelper addInParameter(ParameterType parameterType,Object val){
        ProcParameter parameter=new ProcParameter(parameterType,val);
        parameterList.add(parameter);
        return  this;
    }

    public ParameterHelper addOutParameter(int  outType){
        ProcParameter parameter=new ProcParameter();
        parameter.setInput(false);
        parameter.setSqlType(outType);
        parameterList.add(parameter);
        return  this;
    }

    public List<ProcParameter> getParameterList(){
        return parameterList;
    }
}
