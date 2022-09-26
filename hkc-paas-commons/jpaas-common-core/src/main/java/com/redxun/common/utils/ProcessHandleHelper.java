package com.redxun.common.utils;


/**
 * 流程处理线程辅助类
 * @author mansan
 *@Email: chshxuan@163.com
 * @Copyright (c) 2014-2016 广州红迅软件有限公司（http://www.redxun.cn）
 * 本源代码受软件著作法保护，请在授权允许范围内使用。
 */
public class ProcessHandleHelper {
	/**
	 * 上下文线程变量。
	 */
	private static ThreadLocal<Object> objectLocal=new ThreadLocal<Object>();

	public static void setObjectLocal(Object obj){
		objectLocal.set(obj);
	}
	
	public static Object getObjectLocal(){
		return objectLocal.get();
	}
	
	public static void clearObjectLocal(){
		objectLocal.remove();
	}
}
