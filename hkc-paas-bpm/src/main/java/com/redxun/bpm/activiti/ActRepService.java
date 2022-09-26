package com.redxun.bpm.activiti;

import com.redxun.bpm.activiti.cmd.DeleteTaskCmd;
import com.redxun.bpm.activiti.cmd.JumpToTargetCmd;
import com.redxun.bpm.activiti.cmd.SetFLowNodeAndGoCmd;
import com.redxun.bpm.core.entity.BpmRuPath;
import com.redxun.bpm.core.entity.ProcessNextCmd;
import com.redxun.bpm.core.service.BpmRuPathServiceImpl;
import com.redxun.bpm.util.ProcessHandleUtil;
import com.redxun.common.constant.SysConstant;
import com.redxun.common.tool.BeanUtil;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.*;
import org.activiti.engine.ManagementService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.AbstractLobCreatingPreparedStatementCallback;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobCreator;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * 流程库服务类
 * @author csx
 * @Email: chshxuan@163.com
 * @Copyright (c) 2014-2022 广州红迅软件有限公司（http://www.redxun.cn）
 * 本源代码受软件著作法保护，请在授权允许范围内使用。
 */
@Service
public class ActRepService {

	@Resource
    RepositoryService repositoryService;



	@Resource
	JdbcTemplate jdbcTemplate;




	/**
	 * 获得开始节点后面的节点
	 * @param actDefId
	 * @return
	 */
	public FlowNode getNodeAfterStart(String actDefId){
		BpmnModel bpmnModel=repositoryService.getBpmnModel(actDefId);
		Process process=bpmnModel.getMainProcess();
		//返回流程的开始节点
		return (FlowNode)process.getInitialFlowElement();
	}

	/**
	 * 根据流程定义ID获取第一个任务节点。
	 * @param actDefId
	 * @return
	 */
	public FlowNode getFirstUserTaskNode(String actDefId){
		FlowNode startNode=getNodeAfterStart(actDefId);
		FlowNode flowNode=null;
		if(startNode!=null){
			List<SequenceFlow> list=startNode.getOutgoingFlows();
			if(BeanUtil.isNotEmpty(list)){
				flowNode= (FlowNode) list.get(0).getTargetFlowElement();
			}
		}
		return flowNode;
	}

	/**
	 * 获取流程定义的FlowNode
	 * @param actDefId
	 * @param nodeId
	 * @return
	 */
	public FlowNode getFlowNode(String actDefId,String nodeId){
		BpmnModel bpmnModel=repositoryService.getBpmnModel(actDefId);
		if(bpmnModel==null){
			return null;
		}
		Process process=bpmnModel.getMainProcess();

		Collection<FlowElement> flowElements = process.getFlowElements();
		for(FlowElement el:flowElements){
			FlowElement element=getByFlow(el,nodeId);
			if(element!=null){
				return (FlowNode)element;
			}
		}


		return null;
	}

	/**
	 * 获取流程下的指定节点
	 * @param el
	 * @param nodeId
	 * @return
	 */
	private FlowElement getByFlow(FlowElement el ,String nodeId){
		if(el.getId().equals(nodeId)){
			return  el;
		}
		if(el instanceof  SubProcess){
			SubProcess subProcess=(SubProcess)el;
			Collection<FlowElement> els= subProcess.getFlowElements();
			for(FlowElement elsub:els){
				FlowElement rtn= getByFlow(elsub,nodeId);
				if(rtn!=null){
					return  rtn;
				}
			}
		}

		return null;
	}

	/**
	 * 获取流程定义中所有的待办任务
	 * @param actDefId
	 * @return
	 */
	public Collection<FlowNode> getUserNodes(String actDefId){
		return getUserNodes(actDefId,false);
	}

	/**
	 * 获取流程定义中所有的待办任务
	 * @param actDefId
	 * @return
	 */
	public Collection<FlowNode> getUserNodes(String actDefId,Boolean isGetEnd){
		Set<FlowNode> flowNodes = new HashSet<>();

		BpmnModel bpmnModel=repositoryService.getBpmnModel(actDefId);
		Process process=bpmnModel.getMainProcess();
		Map<String,FlowElement> flowElementMap=process.getFlowElementMap();
		for (FlowElement el: flowElementMap.values()){
			if(el instanceof UserTask){
				flowNodes.add((FlowNode)el);
				continue;
			}
			if(isGetEnd &&el instanceof EndEvent){
				flowNodes.add((FlowNode)el);
			}
		}
		return flowNodes;
	}

	/**
	 * 根据 predicate 获取节点集合。
	 * @param actDefId
	 * @param predicate
	 * @return
	 */
	public Collection<FlowNode> getNodesByPredicate(String actDefId, Predicate<FlowElement> predicate){
		Set<FlowNode> flowNodes = new HashSet<>();

		BpmnModel bpmnModel=repositoryService.getBpmnModel(actDefId);
		Process process=bpmnModel.getMainProcess();
		Map<String,FlowElement> flowElementMap=process.getFlowElementMap();
		for (FlowElement el: flowElementMap.values()){
			boolean isNode=predicate.test(el);
			if(isNode){
				flowNodes.add((FlowNode)el);
				continue;
			}

		}
		return flowNodes;
	}

	/**
	 * 获取网关节点。
	 * @param actDefId
	 * @return
	 */
	public Collection<FlowNode> getByExclusiveGateway(String actDefId){
		Collection<FlowNode> nodes=getNodesByPredicate(actDefId,item->{
			return item instanceof ExclusiveGateway;
		});
		return nodes;
	}

	/**
	 * 获取所有的网关节点。
	 * @param actDefId
	 * @return
	 */
	public Collection<FlowNode> getByGateway(String actDefId){
		Collection<FlowNode> nodes=getNodesByPredicate(actDefId,item->{
			return item instanceof Gateway;
		});
		return nodes;
	}



	/**
	 * 获取子流程的开始节点
	 * @param subProcess
	 * @return
	 */
	public FlowNode getInitNode(SubProcess subProcess){
		FlowNode node= (FlowNode) subProcess.getFlowElements().stream().filter(p-> {
			return 	p instanceof StartEvent ;
		}).findFirst().get();
		return node;
	}

	/**
	 * 获取结束节点。
	 * @param subProcess
	 * @return
	 */
	public FlowNode getEndNode(SubProcess subProcess){
		FlowNode node= (FlowNode) subProcess.getFlowElements().stream().filter(p-> {
			return 	p instanceof EndEvent ;
		}).findFirst().get();
		return node;
	}

	/**
	 * 判断节点是否再多实例子流程内部。
	 * @param flowNode
	 * @return
	 */
	public boolean isSubProcessMulti(FlowNode flowNode) {
		FlowElementsContainer container = flowNode.getParentContainer();
		if (container instanceof Process) {
			return false;
		}
		if (container instanceof SubProcess) {
			SubProcess subProcess = (SubProcess) container;
			if (BeanUtil.isNotEmpty(subProcess.getLoopCharacteristics())) {
				return true;
			}
		}
		return false;
	}


	/**
	 * 根据流程定义ID获取节点Map数据。
	 * @param actDefId
	 * @return
	 */
	public Map<String,FlowNode> getFlowNodes(String actDefId){
		BpmnModel bpmnModel=repositoryService.getBpmnModel(actDefId);
		if(bpmnModel==null){
			return null;
		}
		Process process=bpmnModel.getMainProcess();

		Map<String,FlowNode> map =new HashMap<>(SysConstant.INIT_CAPACITY_16);

		FlowNode startEl = (FlowNode) process.getInitialFlowElement();

		handFlowNode(startEl,map);

		return map;
	}

	/**
	 * 产生当前节点与后续节点的集合
	 * @param flowNode
	 * @param map
	 */
	private void handFlowNode(FlowNode flowNode,Map<String,FlowNode> map){

		if(map.containsKey(flowNode.getId())){
			return;
		}

		map.put(flowNode.getId(),flowNode);

		if(flowNode instanceof SubProcess){
			SubProcess subProcess=(SubProcess)flowNode;
			FlowNode initNode=getInitNode(subProcess);
			handFlowNode(initNode,map);
		}

		List<SequenceFlow> sequenceFlows= flowNode.getOutgoingFlows();
		if(BeanUtil.isEmpty( sequenceFlows)){
			return;
		}
		for(SequenceFlow flow:sequenceFlows){
			FlowNode node= (FlowNode) flow.getTargetFlowElement();
			handFlowNode(node,map);
		}
	}


	/**
	 * 是否为并行网关/条件包含网关的入口节点
	 * @param actDefId
	 * @param nodeId
	 * @return
	 */
	public boolean isGatewayStart(String actDefId, String nodeId){
		BpmnModel bpmnModel=repositoryService.getBpmnModel(actDefId);
		FlowNode actNode = (FlowNode)bpmnModel.getMainProcess().getFlowElement(nodeId);

		if(actNode.getClass().getTypeName().indexOf("InclusiveGateway")!=-1
				|| actNode.getClass().getTypeName().indexOf("ParallelGateway")!=-1
		){
			return actNode.getIncomingFlows().size()==1;
		}
		return false;
	}

	/**
	 * 检查是在网关内的流程节点
	 * @param actDefId
	 * @param nodeId
	 * @return
	 */
	public boolean isInGateway(String actDefId, String nodeId){
		FlowNode actNode = getFlowNode(actDefId,nodeId);
		return findStartGateway(actNode)!=null;
	}

	/**
	 * 检查节点是否在网关内
	 * @param flowNode
	 * @return
	 */
	public boolean isInGateway(FlowNode flowNode){
		return findStartGateway(flowNode)!=null;
	}

	/**
	 * 根据网关内部的节点id来获取外部网关的起始节点
	 * @param actDefId
	 * @param nodeId
	 * @return
	 */
	public FlowNode getStartGateway(String actDefId,String nodeId){
		BpmnModel bpmnModel=repositoryService.getBpmnModel(actDefId);
		FlowNode actNode = (FlowNode)bpmnModel.getMainProcess().getFlowElement(nodeId);
		return findStartGateway(actNode);
	}

	/**
	 *  获取当前节点前面的网关节点，若没有，则返回空节点.
	 * @param node  为任务节点
	 */
	public FlowNode findStartGateway(FlowNode node){
		List<SequenceFlow> seqFlows= node.getIncomingFlows();
		for(SequenceFlow flow : seqFlows){
			FlowNode flowNode=(FlowNode)flow.getSourceFlowElement();
			if(flowNode.getClass().getTypeName().indexOf("InclusiveGateway")!=-1
				|| flowNode.getClass().getTypeName().indexOf("ParallelGateway")!=-1){
				if(flowNode.getIncomingFlows().size()==1){
					return flowNode;
				}
			}
			//查找上一节点是否为网关
			return findStartGateway(flowNode);
		}
		return null;
	}

	/**
	 * 查找后续的结束网关节点
	 * @param node
	 * @return
	 */
	public FlowNode getEndGateway(FlowNode node){
		List<SequenceFlow> seqFlows= node.getOutgoingFlows();
		for(SequenceFlow flow : seqFlows){
			FlowNode flowNode=(FlowNode)flow.getTargetFlowElement();
			if(flowNode.getClass().getTypeName().indexOf("InclusiveGateway")!=-1
					|| flowNode.getClass().getTypeName().indexOf("ParallelGateway")!=-1){
				return flowNode;
			}
			//查找上一节点是否为网关
			return getEndGateway(flowNode);
		}
		return null;
	}

	/**
	 * 根据网关内部的节点id来获取外部网关的起始节点
	 * @param actDefId
	 * @param nodeId
	 * @return
	 */
	public FlowNode getEndGateway(String actDefId,String nodeId){
		BpmnModel bpmnModel=repositoryService.getBpmnModel(actDefId);
		FlowNode actNode = (FlowNode)bpmnModel.getMainProcess().getFlowElement(nodeId);
		return getEndGateway(actNode);
	}

	/**
	 * 获取网关节点里的节点Id
	 * @param actDefId
	 * @param startGatewayNodeId
	 * @return
	 */
	public Set<FlowNode> getGatewayOutNodes(String actDefId,String startGatewayNodeId){
		BpmnModel bpmnModel=repositoryService.getBpmnModel(actDefId);
		FlowNode actNode = (FlowNode)bpmnModel.getMainProcess().getFlowElement(startGatewayNodeId);
		Set<FlowNode> outNodes=new HashSet<FlowNode>();
		findNodesBeforeGateway(actNode,outNodes);
		return  outNodes;
	}

	/**
	 * 找到网关之前的节点集合
	 * @param node
	 * @param nodeSet
	 */
	private void findNodesBeforeGateway(FlowNode node,Set<FlowNode> nodeSet){
		List<SequenceFlow> outFlows=node.getOutgoingFlows();
		for(SequenceFlow flow:outFlows){
			FlowNode targetNode=(FlowNode)flow.getTargetFlowElement();
			if(targetNode.getClass().getTypeName().indexOf("InclusiveGateway")!=-1
					|| targetNode.getClass().getTypeName().indexOf("ParallelGateway")!=-1){
				break;
			}
			nodeSet.add(targetNode);
			findNodesBeforeGateway(targetNode,nodeSet);
		}
	}




	/**
	 * 查找流程定义中的结束节点
	 * @param actDefId
	 * @return
	 */
	public FlowNode getEndNode(String actDefId){
		BpmnModel bpmnModel=repositoryService.getBpmnModel(actDefId);
		Collection<FlowElement> els=bpmnModel.getMainProcess().getFlowElements();
		for(FlowElement el:els){
			if(el.getClass().getName().indexOf("EndEvent")!=-1){
				return (FlowNode) el;
			}
		}
		return null;
	}

	/**
	 * 通过流程任务节点获取其后续任务节点所有跳转节点
	 * @param actDefId
	 * @param nodeId
	 * @return
	 */
	public Set<FlowNode> getOutcomeCascadeUserTasks(String actDefId,String nodeId){
		FlowNode flowNode=getFlowNode(actDefId,nodeId);
		return getOutcomeCascadeUserTasks(flowNode);
	}

	/**
	 * 通过流程任务节点获取其后续任务节点所有跳转节点
	 * @param flowNode
	 * @return
	 */
	public Set<FlowNode> getOutcomeCascadeUserTasks(FlowNode flowNode){
		Set<FlowNode> nodes=new LinkedHashSet<>();
		getFlowUserTasks(flowNode,nodes);
		return nodes;
	}


	/**
	 * 获取后续任务节点的
	 * @param actDefId
	 * @param nodeId
	 * @return
	 */
	public Set<FlowNode> getOutcomeNodes(String actDefId,String nodeId){
		Set<FlowNode> nodes=new LinkedHashSet<>();
		FlowNode flowNode=getFlowNode(actDefId,nodeId);
		List<SequenceFlow> flows=flowNode.getOutgoingFlows();
		for(SequenceFlow flow:flows){
			nodes.add((FlowNode) flow.getTargetFlowElement());
		}
		return nodes;
	}

	/**
	 * 获取这个节点的后续任务节点，若后续节点为网关，则再取网关后的任务节点
	 * @param flowNode
	 * @param userNodeSet
	 */
	private void getFlowUserTasks(FlowNode flowNode,Set<FlowNode> userNodeSet){
		List<SequenceFlow> sequenceFlows=flowNode.getOutgoingFlows();
		for(SequenceFlow seq : sequenceFlows){
			FlowNode targetNode = (FlowNode) seq.getTargetFlowElement();
			if(targetNode.getClass().getName().indexOf("UserTask")!=-1){
				userNodeSet.add(targetNode);
			}else{
				getFlowUserTasks(targetNode,userNodeSet);
			}
		}
	}



	/**
	 * 把修改过的xml更新至回流程定义中
	 *
	 * @param deployId
	 * @param defXml
	 */
	public void writeDefXml(final String deployId, String defXml) {
		try {
			LobHandler lobHandler = new DefaultLobHandler();
			final byte[] btyesXml = defXml.getBytes("UTF-8");
			String sql = "update ACT_GE_BYTEARRAY set BYTES_=? where NAME_ LIKE '%bpmn20.xml' and DEPLOYMENT_ID_= ? ";
			jdbcTemplate.execute(sql, new AbstractLobCreatingPreparedStatementCallback(lobHandler) {
				@Override
				protected void setValues(PreparedStatement ps, LobCreator lobCreator) throws SQLException, DataAccessException {
					lobCreator.setBlobAsBytes(ps, 1, btyesXml);
					ps.setString(2, deployId);
				}
			});
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 *  查看前面的节点 是否为网关节点。
	 * @param actDefId		流程定义ID
	 * @param nodeId		节点ID
	 * @return
	 */
	public boolean isPreGateway(String actDefId,String nodeId){
		FlowNode flowNode=getFlowNode(actDefId,nodeId);
		List<SequenceFlow> incomingFlows = flowNode.getIncomingFlows();
		SequenceFlow sequenceFlow=incomingFlows.get(0);
		FlowElement sourceNode = sequenceFlow.getSourceFlowElement();

		if(sourceNode.getClass().getTypeName().indexOf("InclusiveGateway")!=-1
				|| sourceNode.getClass().getTypeName().indexOf("ParallelGateway")!=-1){
			return true;
		}

		return false;
	}

	/**
	 * 支持发起流程时选择路径。
	 *
	 * 发起节点-->任务节点-->后面跟着多个节点。
	 *
	 * @param actDefId
	 * @return
	 */
	public boolean allowStartSelectPath(String actDefId){
		//获取第一个任务节点。
		FlowNode firstNode= getFirstUserTaskNode(actDefId);

		int rtn= firstNode.getOutgoingFlows().size();

		return rtn>1;
	}






}
