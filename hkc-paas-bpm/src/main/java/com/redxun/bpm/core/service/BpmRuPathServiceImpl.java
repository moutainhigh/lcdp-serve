package com.redxun.bpm.core.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.bpm.activiti.ActRepService;
import com.redxun.bpm.core.entity.BpmRuPath;
import com.redxun.bpm.core.entity.BpmTask;
import com.redxun.bpm.core.entity.IExecutionCmd;
import com.redxun.bpm.core.entity.TaskOptionType;
import com.redxun.bpm.core.mapper.BpmRuPathMapper;
import com.redxun.bpm.util.ProcessHandleUtil;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ContextUtil;
import com.redxun.dto.bpm.BpmConst;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.SubProcess;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
* [流程实例运行路线]业务服务类
*/
@Service
public class BpmRuPathServiceImpl extends SuperServiceImpl<BpmRuPathMapper, BpmRuPath> implements BaseService<BpmRuPath> {

    @Resource
    private BpmRuPathMapper bpmRuPathMapper;

    @Resource
    ActRepService actRepService;

    @Override
    public BaseDao<BpmRuPath> getRepository() {
        return bpmRuPathMapper;
    }


    /**
     * 更新路径。
     * @param bpmTask
     */
    public void updRuPath(BpmTask bpmTask){
        IExecutionCmd cmd= ProcessHandleUtil.getProcessCmd();
        BpmRuPath ruPath=getFarestPath(bpmTask.getInstId(),bpmTask.getKey());
        if(ruPath!=null){
            ruPath.setJumpType(cmd.getCheckType());
            ruPath.setEndTime(new Date());
            ruPath.setAssignee(ContextUtil.getCurrentUserId());
            update(ruPath);
        }
    }
    /**
     * 按流程实例获取执行列表
     * @param instId
     * @return
     */
    public List<BpmRuPath> getByInstId(String instId){
        return bpmRuPathMapper.getByInstId(instId);
    }

    /**
     * 获取流程定义的最新节点路径
     * @param instId
     * @param nodeId
     * @return
     */
    public BpmRuPath getLatestByInstIdNodeId(String instId,String nodeId){
        List<BpmRuPath> paths= bpmRuPathMapper.getByInstIdNodeId(instId,nodeId);
        if(paths.size()>0){
            return paths.get(0);
        }
        return null;
    }
    /**
     * 获取流程定义的最新节点路径
     * @param executionId
     * @return
     */
    public BpmRuPath getLatestByExecutionId(String executionId) {
        List<BpmRuPath> paths= bpmRuPathMapper.getByExecutionId(executionId);
        if(paths.size()>0){
            return paths.get(0);
        }
        return null;
    }
    /**
     * 获取流程实例的最大层级数
     * @param instId
     * @return
     */
    public Integer getMaxLevelByInst(String instId){
        return bpmRuPathMapper.getMaxLevelByInst(instId);
    }

    /**
     * 取得某个节点最新的审批路径。
     * @param instId
     * @param nodeId
     * @return
     */
    public BpmRuPath getFarestPath(String instId,String nodeId){
        Integer level=bpmRuPathMapper.getMaxLevelByInstIdNodeId(instId, nodeId);
        if(level==null){
            level=0;
        }
        List<BpmRuPath> bpmRuPaths=bpmRuPathMapper.getByInstIdNodeIdLevel(instId,nodeId,level);
        if(bpmRuPaths.size()>0){
            return bpmRuPaths.get(bpmRuPaths.size()-1);
        }
        return null;
    }

    /**
     * 取得最新的审批路径。
     * @param instId
     * @return
     */
    public BpmRuPath getFarestPath(String instId){
        Integer level=bpmRuPathMapper.getMaxLevelByInst(instId);
        if(level==null){
            level=0;
        }
        return bpmRuPathMapper.getByInstIdLevel(instId,level);
    }


    /**
     * 判断流程是否允许驳回，只有再第一个节点，流程不能驳回。
     * @param actDefId  流程定义ID
     * @param nodeId    节点ID
     * @return
     */
    public boolean canReject(String actDefId,String nodeId){
        FlowNode firstNode = actRepService.getFirstUserTaskNode(actDefId);
        if(firstNode.getId().equals(nodeId)){
            return false;
        }
        return true;

    }


    /**
     * 获取能够驳回的节点。
     * <pre>
     * 1. 获取指定节点最早的驳回节点  。
     * 		start A1 B1 C1 B2 A2 B2 C2 。 从 C1 往前获取。得到节点序列 C1,B1,A1 。
     * 2. 从 C 往回查找。
     * </pre>
     * @param instId
     * @param nodeId
     * @return
     */
    public List<BpmRuPath> getBackNodes(String instId,String nodeId){
        List<BpmRuPath> paths= bpmRuPathMapper.getEarliestByInstId(instId,nodeId);
        BpmRuPath curPath=null;
        for(Iterator<BpmRuPath> it = paths.iterator(); it.hasNext();){
            BpmRuPath path=it.next();
            if(path.getNodeId().equals(nodeId)){
                curPath=path;
                it.remove();
            }
        }

        List<BpmRuPath> ruPaths= new ArrayList<>();
        Map<String,BpmRuPath> pathMap=new HashMap<>();
        for(BpmRuPath path:paths){
            pathMap.put(path.getPathId(), path);
        }
        BpmRuPath parent=pathMap.get(curPath.getParentId());

        if(parent!=null){
            while(true){
                if("startEvent".equals(parent.getNodeType())) {
                    break;
                }
                if(TaskOptionType.AGREE.name().equals(parent.getJumpType()) ||
                        TaskOptionType.ABSTAIN.name().equals(parent.getJumpType()) ||
                        TaskOptionType.REFUSE.name().equals(parent.getJumpType()) ||
                        TaskOptionType.SKIP.name().equals(parent.getJumpType())){
                    ruPaths.add(parent);
                }
                String pid=parent.getParentId();
                if("0".equals(pid)) {
                    break;
                }
                parent=pathMap.get(pid);
            }
        }
        //节点去重
        Map<String,BpmRuPath> nodeMap=new HashMap<>();
        for(BpmRuPath path:ruPaths){
            nodeMap.put(path.getNodeId(), path);
        }
        List<BpmRuPath> tmp=new ArrayList<>();
        for(BpmRuPath bpmRuPath:nodeMap.values()){
            tmp.add(bpmRuPath);
        }
        return tmp;
    }

    /**
     * 获取某任务节点的上级流程的任务节点
     * @param path
     * @return
     */
    public BpmRuPath getParentUserTask(BpmRuPath path){
        if(path==null  || StringUtils.isEmpty(path.getParentId())
                || "0".equals(path.getParentId())){
            return null;
        }
        BpmRuPath parentPath=get(path.getParentId());

        while (parentPath!=null && !isApproveType(parentPath) ){

            parentPath=get(parentPath.getParentId());
        }
        return  parentPath;
    }

    /**
     * 节点路径是审批情况。
     * @param path
     * @return
     */
    private boolean isApproveType(BpmRuPath path){
        if(!BpmConst.USER_TASK.equals(path.getNodeType())){
            return false;
        }
        String jumpType=path.getJumpType();
        if(TaskOptionType.AGREE.name().equals(jumpType)
                || TaskOptionType.REFUSE.name().equals( jumpType)){
            return  true;
        }

        if(TaskOptionType.SKIP.name().equals(jumpType)){
            BpmRuPath ruPath=get(path.getParentId());
            //如果意见类型是跳转，并且前一个是发起节点。
            if("0".equals( ruPath.getParentId())){
                return  true;
            }
        }

        return false;
    }

    /**
     * 根据流程实例Id删除数据
     * @param instId
     */
    public void delByInstId(String instId){
        bpmRuPathMapper.delByInstId(instId);
    }




    public BpmRuPath getByExeutionAndNode(String instId,String nodeId,String executionId){
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("INST_ID_",instId);
        queryWrapper.eq("EXECUTION_ID_",executionId);
        queryWrapper.eq("NODE_ID_",nodeId);
        queryWrapper.orderByDesc("CREATE_TIME_");
        List<BpmRuPath> paths=  bpmRuPathMapper.selectList(queryWrapper);
        if(paths.size()>0){
            return paths.get(0);
        }
        return null;
    }

    public void delByExecutionAndNode(String instId,String nodeId,String executionId){
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("INST_ID_",instId);
        queryWrapper.eq("EXECUTION_ID_",executionId);
        queryWrapper.eq("NODE_ID_",nodeId);

        bpmRuPathMapper.delete(queryWrapper);

    }

    public JsonResult<BpmRuPath> isPreSubProcess(BpmRuPath parentPath){
        if(!"endEvent".equals( parentPath.getNodeType())){
            return JsonResult.Fail("不是子流程节点");
        }
        String nodeId=parentPath.getNodeId();
        String actDefId=parentPath.getActDefId();
        FlowNode flowNode = actRepService.getFlowNode(actDefId, nodeId);
        SubProcess process=(SubProcess) flowNode.getParentContainer();
        while (!"userTask".equals( parentPath.getNodeType())){
            parentPath= this.get(parentPath.getParentId());
        }
        JsonResult result=JsonResult.Success();

        result.setData(parentPath);

        return  result;

    }


    /**
     * 删除备份的数据
     * @param instId
     * @param tableId
     */
    public void delArchiveByInstId(String instId,Integer tableId) {
        bpmRuPathMapper.delArchiveByInstId(instId,tableId);
    }


    /**
     * 获取某人对某个实例最后处理的路径。
     * @param instId
     * @param creator
     * @return
     */
    public BpmRuPath getMyLatestHandle(String instId,String creator){
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("INST_ID_",instId);
        wrapper.eq("ASSIGNEE_",creator);
        wrapper.eq("NODE_TYPE_","userTask");
        wrapper.isNotNull("END_TIME_");
        wrapper.orderByDesc("LEVEL_");

        List<BpmRuPath> list = bpmRuPathMapper.selectList(wrapper);
        if(BeanUtil.isNotEmpty(list)){
            return list.get(0);
        }
        return null;
    }

    /**
     * 根据当前人获取下一步骤的路径。
     * @param
     * @return
     */
    public List getNextPath(BpmRuPath path){
        if(path==null){
            return null;
        }
        List<BpmRuPath> paths=new ArrayList<>();
        getDeepPath(path.getPathId(),paths);
        return  paths;
    }

    /**
     * 根据pathId获取下一步骤的路径。
     * <pre>
     *     1.如果下一步骤不是任务节点则继续往下查找。
     *     2.如果是任务节点则加入到下一的集合中。
     * </pre>
     * @param pathId
     * @param list
     */
    private void getDeepPath(String pathId,List<BpmRuPath> list){
        List<BpmRuPath> paths = getByParentId(pathId);
        if(BeanUtil.isEmpty(paths)){
            return;
        }
        for(BpmRuPath path:paths){
            if(!path.getNodeType().equals("userTask")){
                getDeepPath(path.getPathId(),list);
            }
            else{
                //只有未完成的才加入path。
                if(path.getEndTime()==null){
                    list.add(path);
                }
            }
        }
    }


    /**
     * 根据父ID获取路径。
     * @param parentId
     * @return
     */
    public List<BpmRuPath> getByParentId(String parentId){
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("PARENT_ID_",parentId);
        List<BpmRuPath> list = bpmRuPathMapper.selectList(wrapper);
        return list;
    }

    /**
     *  删除运行路径只保留开始的路径。
     * @param bpmRuPaths
     */
    public void removeRuPath(List<BpmRuPath> bpmRuPaths){
        //获取发起节点路径。
        BpmRuPath startPath = bpmRuPaths.stream().filter(p -> p.getParentId().equals("0")).findFirst().get();
        Iterator<BpmRuPath> it = bpmRuPaths.iterator();
        while (it.hasNext()){
            BpmRuPath path=it.next();
            String parentId=path.getParentId();
            if(!parentId.equals(startPath.getPathId()) && !parentId.equals("0")){
                this.delete(path.getPathId());
                it.remove();
            }
        }
    }




    /**
     * 获取第一条路径。
     * @param bpmRuPaths
     * @return
     */
    public BpmRuPath getFirstPath(List<BpmRuPath> bpmRuPaths){
        BpmRuPath startPath = bpmRuPaths.stream().filter(p -> p.getParentId().equals("0")).findFirst().get();
        BpmRuPath firstPath=bpmRuPaths.stream().filter(p -> p.getParentId().equals(startPath.getPathId())).findFirst().get();
        return firstPath;
    }
}
