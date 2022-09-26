package com.redxun.util.wechat;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 签名参数模型
 */
public class SignParamModel {

	private int type=3;
	//开始时间
	private Date startDate;
	//结束时间
	private Date endDate;
	//用户Id列表
	private List<String> userIds=new ArrayList<String>();
	
	
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public List<String> getUserIds() {
		return userIds;
	}
	public void setUserIds(List<String> userIds) {
		this.userIds = userIds;
	}
	
	public void addUser(String userId) {
		this.userIds.add(userId);
	}
	
	
	@Override
	public String toString() {
		JSONObject jsonObj=new JSONObject();
		jsonObj.put("opencheckindatatype",this.type);
		jsonObj.put("starttime", this.startDate.getTime() /1000);
		jsonObj.put("endtime", this.endDate.getTime() /1000);
		jsonObj.put("useridlist", this.userIds);
	
		return jsonObj.toJSONString();
		
	}
	
	
	
}
