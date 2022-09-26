package com.redxun.bpm.core.entity;

/**
 * 审批任务的意见类型常量
 * @author csx
 *  @Email: chshxuan@163.com
 * @Copyright (c) 2018-2020 广州红迅软件有限公司（http://www.redxun.cn）
 * 本源代码受软件著作法保护，请在授权允许范围内使用。
 */
public enum TaskOptionType {
	AGREE("通过","icon-agree","true"),
	OVERTIME_AUTO_AGREE("超时审批","icon-agree","true"),
	SKIP("跳过","icon-skip","false"),
	RECOVER("撤回","icon-recorver","false"),
	TIMEOUT_SKIP("超时跳过","timeout-skip","false"),
	REFUSE("不同意","icon-refuse","true"),
	LINKUP("沟通","icon-linkup","true"),
	COMMUNICATE("沟通","icon-communicate","true"),
	REPLY_COMMUNICATE("回复沟通","icon-replaycommunicate","false"),
	CANCEL_COMMUNICATE("取消沟通","icon-cancelcommunicate","false"),
	BACK("驳回","icon-back","true"),
	BACK_TO_STARTOR("驳回发起人","icon-startor","true"),
	INVOKE_TO_STARTOR("撤销到发起人","icon-startor","true"),
	BACK_SPEC("驳回节点","icon-back","true"),
	BACK_GATEWAY("驳回网关","icon-back","true"),
	ABSTAIN("弃权","icon-abstain","true"),
	BACK_CANCEL("驳回撤销","icon-back","true"),
	RECOVER_CANCEL("撤回撤销","icon-back","true"),
	TRANSFER("转办","icon-transfer","true"),
	ROAM_TRANSFER("流转","icon-transfer","true"),
	LIVE("复活","icon-agree","true"),
	INTERPOSE("干预","icon-interpose","false"),
	TRANS("转发","icon-transfer","true"),
	TRANS_REPLY("回复转发","icon-transfer","true");//干预跳转


	 TaskOptionType(String text, String iconCls, String isShow){
		 this.text=text;
		 this.iconCls=iconCls;
		 this.isShow=isShow;
	 }

	 //操作意见
	private String text;
	//显示样式
	private String iconCls;
	//是否显示
	private String isShow;

	public String getText() {
		return text;
	}

	public String getIconCls() {
		return iconCls;
	}

	public String getIsShow() {
		return isShow;
	}


}



