package com.redxun.bpm.core.entity;

/**
 * 返回路径结果对象
 * @author csx
 */
public class PathResult {

	/**
	 * 是否在网关内（并行与条件包含网关）
	 */
	private boolean isInGateway=false;
	/**
	 * 跳转回来。
	 */
	private BpmRuPath bpmRuPath;

	public PathResult() {
		
	}

	public PathResult(BpmRuPath ruPath){
		this.bpmRuPath=ruPath;
	}


	public BpmRuPath getBpmRuPath() {
		return bpmRuPath;
	}

	public void setBpmRuPath(BpmRuPath bpmRuPath) {
		this.bpmRuPath = bpmRuPath;
	}
	

	public boolean isInGateway() {
		return isInGateway;
	}

	public void setInGateway(boolean inGateway) {
		isInGateway = inGateway;
	}
}
