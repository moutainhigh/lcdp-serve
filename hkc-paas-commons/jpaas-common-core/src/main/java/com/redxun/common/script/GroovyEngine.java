package com.redxun.common.script;

import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ExceptionUtil;
import com.redxun.common.utils.MessageUtil;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.groovy.runtime.metaclass.MissingMethodExceptionNoStack;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


/**
 * Groovy引擎
 * @author ray
 *@Email 58133370@qq.com
 * @Copyright (c) 2014-2016 广州红迅软件有限公司（http://www.redxun.cn）
 * 本源代码受软件著作法保护，请在授权允许范围内使用。
 */
@Component
@Slf4j
public class GroovyEngine implements BeanPostProcessor {



	GroovyBinding groovyBinding=new GroovyBinding();

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

		boolean isInherit= GroovyScript.class.isAssignableFrom(bean.getClass());
		if (isInherit) {
			groovyBinding.setProperty(beanName, bean);
		}
		return bean;
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}


	/**
	 * 运行脚本
	 * @param scriptText
	 * @param vars
	 * @return
	 */
	public Object executeScripts(String scriptText,Map<String,Object> vars){
		if(StringUtils.isEmpty(scriptText) ||  "".equals(scriptText.trim()) ) {
			return null;
		}
		Object result=null;

		try{
			if(vars!=null){
				vars.put("logger",log);
				groovyBinding.setVariables(vars);
			}
			GroovyShell shell =new GroovyShell(groovyBinding);
			shell.getClassLoader().clearCache();
			result=shell.evaluate(scriptText);
			vars.remove("logger");
		}
		catch(Exception ex){
			String msg=ExceptionUtil.getExceptionMessage(ex);
			MessageUtil.triggerException("执行脚本出错,请检查脚本!","脚本内容:\r\n" +scriptText +"\n" + msg );
		}
		finally{
			groovyBinding.clearVariables();
		}
		return result;
	}

	public Object executeMethod(String scriptText,String method, Object vars){
		if(StringUtils.isEmpty(scriptText) || "".equals(scriptText.trim()) ){
			return null;
		}
		Object result=null;
		try{
			Map<String,Object> params=new HashMap<>();
			params.put("logger",log);
			groovyBinding.setVariables(params);
			GroovyShell shell=new GroovyShell(groovyBinding);
			shell.getClassLoader().clearCache();
			Script script= shell.parse(scriptText);

			result=script.invokeMethod(method,vars);

		}
		catch (MissingMethodExceptionNoStack ex){
			throw new NoMethodException();
		}
		catch(Exception ex){
			String message=ExceptionUtil.getExceptionMessage(ex);
			throw new RuntimeException("执行脚本出错! 脚本内容："+ scriptText +",\n" +message);
		}
		finally {
			groovyBinding.clearVariables();
		}
		return result;
	}

	/**
	 * 获取脚本增加logger
	 * @param scriptText
	 * @return
	 */
	public Script getScript(String scriptText){
		Map<String,Object> params=new HashMap<>();
		params.put("logger",log);
		groovyBinding.setVariables(params);
		GroovyShell shell=new GroovyShell(groovyBinding);
		Script script= shell.parse(scriptText);
		return script;
	}

	/**
	 * 清理线程变量
	 */
	public void clearVariables(){
		groovyBinding.clearVariables();
	}

	public static void main(String[] args) {
//		AfterParams		params=new AfterParams();
//		JSONObject json= JSONObject.parseObject("{age:1}");
//		params.setJsonData(json);
//		GroovyEngine groovyEngine=new GroovyEngine();
//		String str="import com.redxun.sys.customform.manager.AfterParams;public  String hello(AfterParams params){ return \"hello:\" +params.getJsonData().toJSONString()}";
//
//		Object obj=groovyEngine.executeMethod(str,"hello",params);
//
//		System.out.println(obj);


	}
}
