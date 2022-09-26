package com.redxun.common.script;

import groovy.lang.Binding;

import java.util.HashMap;
import java.util.Map;

/**
 * Groovy脚本运行的上下环境变量
 * @author ray
 *@Email 58133370@qq.com
 * @Copyright (c) 2014-2016 广州红迅软件有限公司（http://www.redxun.cn）
 * 本源代码受软件著作法保护，请在授权允许范围内使用。
 *
 */
public class GroovyBinding extends Binding {
	//每个线程有独立的本地变量
	private static ThreadLocal<Map<String,Object>> localVars=new ThreadLocal<Map<String,Object>>();

	public void setVariables(Map<String,Object> vars){
		Map<String,Object> localMap=localVars.get();
		if(localMap==null){
			localMap=new HashMap<String,Object>();
			localVars.set(localMap);
		}
		localMap.putAll(vars);
	}
	
	public void clearVariables(){
		localVars.remove();
	}
	
	/**
	 * 重写该方法，以使用线程变量中的对象值
	 */
	@Override
	public Object getVariable(String name) {
		Map<String,Object> vars=localVars.get();
		if(vars!=null){
			Object val=vars.get(name);
			if(val!=null){
				return val;
			}
		}
		return super.getVariable(name);
	}

	public boolean hasVar(String name) {
		return localVars.get() != null && localVars.get().containsKey(name);
	}
}
