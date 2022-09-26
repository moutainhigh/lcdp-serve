package com.redxun.form.core.entity;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;

/**
 * 更新JSON
 * @author ray
 *
 */
@Setter
@Getter
public class UpdJsonEnt {
	
	public UpdJsonEnt(){}
	
	

	public UpdJsonEnt(String pk, JSONObject curJson, JSONObject originJson) {
		this.pk = pk;
		this.curJson = curJson;
		this.originJson = originJson;
	}



	private String pk="";

	/**
	 * 当前JSON数据
	 */
	private JSONObject curJson;

	/**
	 * 原JSON数据。
	 */
	private JSONObject originJson;


	
}
