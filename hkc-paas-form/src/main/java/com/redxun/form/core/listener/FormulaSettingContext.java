package com.redxun.form.core.listener;

/**
 * 公式上下文设定。
 */
public class FormulaSettingContext {

    private  static ThreadLocal<FormulaSetting> settingThreadLocal=new ThreadLocal<>();

    public static void setFormulaSetting(FormulaSetting setting){
        settingThreadLocal.set(setting);
    }


    public static FormulaSetting getFormulaSetting(){
        return  settingThreadLocal.get();
    }

    public static void clearSetting(){
        settingThreadLocal.remove();
    }

}
