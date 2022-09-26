package com.redxun.form.core.grid.enums;

/**
 * mini Grid的列类型
 * @author mansan
 *
 */
public enum MiniGridColumnType {
	/*普通*/
	COMMON("普通"),
    /*文本*/
    TEXT("文本"),
	/*用户*/
	USER("用户"),
	/*部门与组*/
	GROUP("部门与组"),
	/*机构*/
	SYSINST("机构"),
	/*日期*/
	DATE("日期"),
	/*数字*/
	NUMBER("数字"),
	/*URL链接*/
	URL("URL链接"),
	/*单色标签*/
	DISPLAY_LABEL("单色标签"),
	/*多色标签*/
	DISPLAY_ITEMS("多色标签"),
	/*百分比标签*/
	DISPLAY_PERCENT("百分比标签"),
	/*值区域标签*/
	DISPLAY_RANGE("值区域标签"),
	/*关联流程*/
	LINK_FLOW("关联流程"),
	/*流程状态*/
	FLOW_STATUS("流程状态"),
	/*当前节点*/
	CUR_NODE("当前节点"),
	/*审批执行人*/
	CUR_ASSIGNEE("审批执行人"),
	/*流程节点超时预警*/
	OVER_TIME("流程节点超时预警"),
	/*脚本计算*/
	SCRIPT("脚本计算"),
	/*数据脱敏*/
	REG_LIB("数据脱敏"),


	/*地址*/
	ADDRESS("地址"),

	/*地图*/
	MAP("地图"),

	/**
	 * 打开列表对话框。
	 */
	LIST("列表"),

	/**
	 * 匹配字段
	 */
	SEARCH("匹配字段"),
	/**
	 * 启动流程
	 */
	STARTFLOW("启动流程");

	
	MiniGridColumnType(String typeName){
		this.typeName=typeName;
	}
	/**
	 * 类型名称
	 */
	private String typeName;

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	
}
