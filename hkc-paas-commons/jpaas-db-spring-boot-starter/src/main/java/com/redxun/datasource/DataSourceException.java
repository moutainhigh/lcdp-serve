package com.redxun.datasource;

/**
 * 数据源异常。
 * <pre> 
 * 作者：ray
 * 日期:2014-4-11-下午2:56:04
 * 版权：广州红迅软件有限公司版权所有 2014-2021
 * </pre>
 */
public class DataSourceException extends RuntimeException {
	
	/**
	 * serialVersionUID
	 * @since 1.0.0
	 */
	private static final long serialVersionUID = 3148019938789322656L;

	public DataSourceException(String msg){
		super(msg);
	}
	
	public DataSourceException(String msg, Throwable throwable)
	{
		super(msg,throwable);
	}
}
