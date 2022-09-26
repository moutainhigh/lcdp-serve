package com.redxun.common.constant;

/**
 * 流程状态
 * @author csx
 *
 */
public enum ProcessStatus {
	/**运行中*/
	RUNNING("运行中"),
	/**草稿*/
	DRAFTED("草稿"),
	/**成功结束*/
	SUCCESS_END("成功结束"),
	/**作废*/
    CANCEL("作废"),
	/**删除*/
	DELETE("删除"),
	/**异常中止结束*/
	ABORT_END("异常结束"),
	ERROR_END("异常结束"),
	/**
	 提交
	 **/
	SUBMIT("提交"),
	/**
	 锁定
	 **/
	LOCKED("锁定"),
	/**
	 暂停
	**/
	SUPSPEND("暂停"),
	/**挂起*/
	PENDING("挂起");

	String statusLabel;
	ProcessStatus(String statusLabel){
		this.statusLabel=statusLabel;
	}
	public String getStatusLabel() {
		return statusLabel;
	}
}
