package com.redxun.form.core.entity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.redxun.common.tool.BeanUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * 子表数据
 * @author ray
 *
 */
@Getter
@Setter
public class SubDataHolder {
	
	String tableName="";
	
	private boolean isOneToMany=false;

	/**
	 * 新增的数据
	 */
	private List<JSONObject> addList =new ArrayList<JSONObject>();

	/**
	 * 更新当前的数据
	 */
	private List<UpdJsonEnt> updList =new ArrayList<UpdJsonEnt>();
	
	/**
	 * 删除列表。
	 */
	private List<JSONObject> delList= new ArrayList<JSONObject>();

	public void addAddList(JSONObject obj){
		addList.add(obj);
	}

	public void addDelList(JSONObject obj){
		delList.add(obj);
	}
	
	public void addDelJsonAry(JSONArray ary){
		for(Object obj:ary){
			delList.add((JSONObject) obj);
		}
	}

	public void addUpdJsonEnt(UpdJsonEnt ent){
		updList.add(ent);
	}

	/**
	 * 判断新增和更新都为空的情况。
	 * @return
	 */
	public boolean isDataEmpty(){
		return BeanUtil.isEmpty(addList) && BeanUtil.isEmpty(updList);
	}
	

}
