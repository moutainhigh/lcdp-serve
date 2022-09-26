package com.redxun.portal.core.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * 自定义栏目返回的数据结构。
 * @author ray
 */
@Getter
@Setter
public class InsColumnEntity {
	//标题
	private String title;
	//更多的URL
	private String url;
	//返回的数量
	private Integer count;
	//返回的结果对象
	private Object obj;
	//对象类型
	private String type;


	
	public InsColumnEntity(){

	}
	
	public InsColumnEntity(String title, String url, Integer count, Object obj) {
		super();
		this.title = title;
		this.url = url;
		this.count = count;
		this.obj = obj;
	}
	public InsColumnEntity(String title, String url, Integer count, Object obj, String type) {
		super();
		this.title = title;
		this.url = url;
		this.count = count;
		this.obj = obj;
		this.type = type;
	}
	
	
}
