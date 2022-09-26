package com.redxun.common.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 错误信息处理类。
 * @author ray
 * 
 */
public class ExceptionUtil {

	/**
	 * 获取exception的详细错误信息。
	 * 
	 * @param e
	 * @return
	 */
	public static String getExceptionMessage(Throwable e) {

		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw, true));
		String str = sw.toString();

		return str;
	}
}
