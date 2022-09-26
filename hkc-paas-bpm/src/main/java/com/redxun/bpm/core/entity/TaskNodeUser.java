package com.redxun.bpm.core.entity;

import com.redxun.dto.bpm.TaskExecutor;
import lombok.Data;
import org.activiti.bpmn.model.FlowNode;
import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 任务节点的人员
 * @author mansan
 * @Email: chshxuan@163.com
 * @Copyright (c) 2014-2022 广州红迅软件有限公司（http://www.redxun.cn）
 * 本源代码受软件著作法保护，请在授权允许范围内使用。
 */
@Data
public class TaskNodeUser implements Serializable{

	public static String STATUS_INIT="init";

	public static String STATUS_RUNING="runing";

	public static String STATUS_FINISH="finish";

	//任务Id
	private String taskId;
	//节点ID
	private String nodeId;
	//节点名称
	private String nodeText;
	//节点类型
	private String nodeType;
	//并行还是串行
	private String multiInstance=null;

	/**
	 * 流程定义Id
	 */
	private String  actDefId;

	/**
	 * 实例ID
	 */
	private String instId;

	//节点的配置中的执行人员
	private Set<TaskExecutor> configExecutors = new LinkedHashSet<>();
	//节点干预执行优先的执行人员
	private Set<TaskExecutor> operateExecutors=new LinkedHashSet<>();
	//节点已审批的执行人员
	private Set<TaskExecutor> checkExecutors=new LinkedHashSet<>();
	//节点正运行中的执行人员
	private Set<TaskExecutor> runExecutors=new LinkedHashSet<>();

	/**
	 * 节点状态。
	 */
	private String status=STATUS_INIT;
	/**
	 * 为当前用户的任务
	 */
	private boolean isCurUserTask=false;

	public TaskNodeUser() {

	}

	public TaskNodeUser(String nodeId,String nodeText){
		this.nodeId=nodeId;
		this.nodeText=nodeText;
	}

	public TaskNodeUser(FlowNode flowNode){
		this.nodeId=flowNode.getId();
		this.nodeText=flowNode.getName();
	}

	/**
	 * 是否结束节点
	 */
	private boolean isEndNode=false;
}
