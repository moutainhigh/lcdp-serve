package com.redxun.bpm.core.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.redxun.api.org.IOrgService;
import com.redxun.bpm.core.entity.*;
import com.redxun.bpm.core.mapper.BpmCheckHistoryMapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.db.PageHelper;
import com.redxun.common.base.entity.IUser;
import com.redxun.common.base.search.QueryFilter;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ContextUtil;
import com.redxun.dto.user.OsUserDto;
import org.activiti.engine.RuntimeService;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
* [流程审批流转记录]业务服务类
*/
@Service
public class BpmCheckHistoryServiceImpl extends SuperServiceImpl<BpmCheckHistoryMapper, BpmCheckHistory> implements BaseService<BpmCheckHistory> {

    @Resource
    private BpmCheckHistoryMapper bpmCheckHistoryMapper;

    @Resource
    private BpmDefService bpmDefService;
    @Resource
    IOrgService orgService;

    @Resource
    private BpmInstServiceImpl bpmInstService;
    @Resource
    private BpmCheckFileService bpmCheckFileService;
    @Resource
    private BpmRuPathServiceImpl bpmRuPathService;
    @Resource
    private BpmInstRouterServiceImpl bpmInstRouterService;
    @Resource
    private RuntimeService runtimeService;

    @Override
    public BaseDao<BpmCheckHistory> getRepository() {
        return bpmCheckHistoryMapper;
    }


    /**
     * 创建任务历史
     * @param bpmTask 当前任务
     * @param checkType  审批类型
     * @param opinion 审批意见
     */
    public void createHistory(BpmTask bpmTask,String checkType,String opinionName,String opinion,String opFiles,String relInsts,String transUsers){
        IUser iUser= ContextUtil.getCurrentUser();
        createHistory(bpmTask,checkType,opinionName,opinion,iUser.getUserId(),iUser.getDeptId(),opFiles,relInsts,transUsers);
    }

    /**
     * 创建任务历史
     * @param bpmInst 流程实例
     * @param nodeId 节点ID
     * @param nodeName 节点名称
     * @param checkType 审批类型
     * @param opinion 审批意见
     */
    public void createHistory(BpmInst bpmInst,String nodeId,String nodeName,String checkType,String opinionName,String opinion){
        IUser user= ContextUtil.getCurrentUser();
        BpmCheckHistory history=new BpmCheckHistory();
        BpmDef bpmDef=bpmDefService.getById(bpmInst.getDefId());
        BpmRuPath bpmRuPath=bpmRuPathService.getLatestByInstIdNodeId(bpmInst.getInstId(),nodeId);
        //有效时长，需要根据动态日历中来计算 TODO
        //持续时长
        Long durTimes=System.currentTimeMillis() - bpmRuPath.getCreateTime().getTime();
        history.setHisId(IdGenerator.getIdStr()).setActDefId(bpmInst.getActDefId())
                .setNodeId(nodeId).setNodeName(nodeName)
                .setCheckStatus(checkType).setCompleteTime(new Date())
                .setOwnerId(bpmInst.getCreateBy()).setJumpType(checkType)
                .setHandlerId(user.getUserId()).setHandleDepId(user.getDeptId())
                .setInstId(bpmInst.getInstId()).setRemark(opinion)
                .setDuration(durTimes)
                .setOpinionName(opinionName)
                .setSubject(bpmInst.getSubject())
                .setTreeId(bpmDef.getTreeId())
                .setCreateTime(bpmInst.getCreateTime());
        insert(history);
    }

    /**
     * 创建任务历史
     * @param bpmTask 当前任务
     * @param checkType  审批类型
     * @param opinion 审批意见
     * @param handlerId 处理人Id
     * @param handlerDepId 处理人部门Id
     */
    public void createHistory(BpmTask bpmTask,String checkType,String opinionName,String opinion,String handlerId,
                              String handlerDepId,String opFiles,String relInsts,String transUsers){
        BpmCheckHistory history=new BpmCheckHistory();
        BpmDef bpmDef=bpmDefService.getById(bpmTask.getDefId());
        //有效时长，需要根据动态日历中来计算 TODO
        //持续时长
        Long durTimes=System.currentTimeMillis() - bpmTask.getCreateTime().getTime();
        history.setHisId(IdGenerator.getIdStr()).setActDefId(bpmTask.getActDefId())
                .setNodeId(bpmTask.getKey()).setNodeName(bpmTask.getName())
                .setCheckStatus(checkType).setCompleteTime(new Date())
                .setOwnerId(bpmTask.getOwner()).setJumpType(checkType)
                .setHandlerId(handlerId).setHandleDepId(handlerDepId)
                .setInstId(bpmTask.getInstId()).setRemark(opinion)
                .setTaskId(bpmTask.getTaskId())
                .setDuration(durTimes)
                .setOpinionName(opinionName)
                .setSubject(bpmTask.getSubject())
                .setTreeId(bpmDef.getTreeId())
                .setRelInsts(relInsts)
                .setLinkUpUserIds(transUsers)
                .setCreateTime(bpmTask.getCreateTime());
        //跳过操作，执行人由默认为当前用户改为原任务执行人
        if("SKIP".equals(checkType)){
            String ownerId=bpmTask.getOwner();
            if(StringUtils.isNotEmpty(ownerId)){
                history.setHandlerId(ownerId);
                history.setCreateBy(ownerId);
            }
        }
        insert(history);
        addOpFiles(opFiles,history);
    }

    /**
     * 添加意见附件。
     *
     * @param opFiles [{fileId:"",name:""},{fileId:"",name:""}]
     * @param bpmCheckHistory
     */
    public void addOpFiles(String opFiles, BpmCheckHistory bpmCheckHistory) {
        if (StringUtils.isEmpty(opFiles)) {
            return;
        }

        JSONArray arr = JSONArray.parseArray(opFiles);
        Iterator<Object> it = arr.iterator();
        while (it.hasNext()) {
            JSONObject ob = (JSONObject) it.next();
            String fileName = ob.getString("name");
            String fileId = ob.getString("fileId");
            BpmCheckFile cfile = new BpmCheckFile();
            cfile.setId(IdGenerator.getIdStr());
            cfile.setJumpId(bpmCheckHistory.getHisId());
            cfile.setFileName(fileName);
            cfile.setFileId(fileId);
            cfile.setInstId(bpmCheckHistory.getInstId());
            bpmCheckFileService.insert(cfile);
        }

    }


    /**
     * 创建沟通任务历史
     * @param bpmTask 当前任务
     * @param userId  审批用户
     * @param opinion 审批意见
     * @param linkUpUsers 沟通对象
     */
    public void createLinkupHistory(BpmTask bpmTask, String userId,String opinion,String opFiles,String linkUpUsers){
        BpmCheckHistory history=new BpmCheckHistory();
        IUser iUser= ContextUtil.getCurrentUser();
        history.setHisId(IdGenerator.getIdStr()).setActDefId(bpmTask.getActDefId()).setLinkUpUserIds(linkUpUsers)
                .setNodeId(bpmTask.getKey()).setNodeName(bpmTask.getName())
                .setCheckStatus(BpmTask.STATUS_LINKUP).setCompleteTime(new Date())
                .setOwnerId(bpmTask.getOwner()).setJumpType(BpmTask.STATUS_LINKUP)
                .setHandlerId(iUser.getUserId()).setHandleDepId(iUser.getDeptId())
                .setInstId(bpmTask.getInstId()).setRemark(opinion)
                .setTaskId(bpmTask.getTaskId()).setSubject(bpmTask.getSubject())
                .setCreateTime(bpmTask.getCreateTime());

        insert(history);
        addOpFiles(opFiles,history);
    }

    /**
     * 按流程实例Id查看历史审批
     * @param instId
     * @return
     */
    public List<BpmCheckHistory> getByInstId(String instId){
        List<BpmCheckHistory> bpmCheckHistoryList=new ArrayList<>();
        BpmInst bpmInst=bpmInstService.getById(instId);
        //查询复活前的流程审批记录
        getByLiveInstId(bpmCheckHistoryList,bpmInst.getLiveInstId());
        if (!BpmInstStatus.CANCEL.name().equals(bpmInst.getStatus()) && !BpmInstStatus.DRAFTED.name().equals(bpmInst.getStatus()) && !BpmInstStatus.ERROR_END.name().equals(bpmInst.getStatus()) && !BpmInstStatus.SUCCESS_END.name().equals(bpmInst.getStatus())) {
            getByMainActInstId(bpmCheckHistoryList, bpmInst);
        }
        //查看是否为备份数据
        BpmInstRouter bpmInstRouter = bpmInstRouterService.get(instId);
        if(BeanUtil.isNotEmpty(bpmInstRouter)){
            bpmCheckHistoryList.addAll(bpmCheckHistoryMapper.getByArchiveLog(instId,bpmInstRouter.getTableId()));
        }else {
            bpmCheckHistoryList.addAll(bpmCheckHistoryMapper.getByInstId(instId));
        }
        for (BpmCheckHistory bpmCheckHistory : bpmCheckHistoryList) {
            OsUserDto user = orgService.getUserById(bpmCheckHistory.getHandlerId());
            bpmCheckHistory.setHandlerUserName(user.getFullName());
            List<BpmCheckFile> checkFiles;
            if(BeanUtil.isNotEmpty(bpmInstRouter)){
                checkFiles=bpmCheckFileService.getByArchiveLog(bpmCheckHistory.getHisId(),bpmInstRouter.getTableId());
            }else {
                checkFiles = bpmCheckFileService.getByHisId(bpmCheckHistory.getHisId());
            }
            bpmCheckHistory.setOpFiles(checkFiles);
            if(StringUtils.isNotEmpty(bpmCheckHistory.getLinkUpUserIds())){
                List<Map<String,String>> userList=new ArrayList<>();
                String[] userIds = bpmCheckHistory.getLinkUpUserIds().split(",");
                for (String userId : userIds) {
                    Map<String,String> map=new HashMap<>();
                    OsUserDto osUserDto = orgService.getUserById(userId);
                    map.put("id",userId);
                    map.put("name",osUserDto.getFullName());
                    userList.add(map);
                }
                bpmCheckHistory.setLinkUpUsers(userList);
            }
        }
        return bpmCheckHistoryList;
    }

    private void getByMainActInstId(List<BpmCheckHistory> list,BpmInst bpmInst){
        String mainActInstId=bpmInst.getParentActInstId();
        if (StringUtils.isNotEmpty(mainActInstId)) {
            BpmInst mainBpmInst = bpmInstService.getByActInstId(mainActInstId);
            list.addAll(bpmCheckHistoryMapper.getByInstId(mainBpmInst.getInstId()));
            getByMainActInstId(list, mainBpmInst);
        }

    }

    private void getByLiveInstId(List<BpmCheckHistory> list,String liveInstId){
        if(StringUtils.isNotEmpty(liveInstId)) {
            BpmInst inst = bpmInstService.get(liveInstId);
            list.addAll(bpmCheckHistoryMapper.getByInstId(liveInstId));
            getByLiveInstId(list,inst.getLiveInstId());
        }
    }

    /**
     * 获取流程审批最新的历史信息
     * @param instId
     * @return
     */
    public Map<String,BpmCheckHistory> getLatestHistories(String instId){
        Map<String,BpmCheckHistory> map=new LinkedHashMap<>();

        List<BpmCheckHistory> list = bpmCheckHistoryMapper.getByInstId(instId);
        for(BpmCheckHistory his:list){
            map.put(his.getNodeId(),his);
        }
        return map;
    }



    /**
     * 根据实例和用户ID获取审批历史列表。
     * @param instId
     * @param userId
     * @return
     */
    public List<BpmCheckHistory> getByInstUser(String instId,String userId){
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("INST_ID_",instId);
        wrapper.eq("HANDLER_ID_",userId);
        wrapper.orderByDesc("COMPLETE_TIME_");
        return bpmCheckHistoryMapper.selectList(wrapper);
    }

    public void delByInstId(String instId){
        bpmCheckHistoryMapper.delByInstId(instId);
    }

    public BpmCheckHistory getLatestHistoriesByNodeId(String instId, String nodeId) {
        Map<String,BpmCheckHistory> map=getLatestHistories(instId);
        return map.get(nodeId);
    }


    /**
     * 获取我审批的所有任务。
     * @param userId
     * @return
     */
    public List<BpmCheckHistory> getMyAllApproved(String userId){
        Map<String,Object> params= new HashMap<>();
        params.put("HANDLER_ID_",userId);
        return bpmCheckHistoryMapper.getMyApproved(params);
    }

    /**
     * 获取我审批的任务。
     * @param queryFilter
     * @return
     */
    public IPage getMyApproved(QueryFilter queryFilter){
        Map<String,Object> params= PageHelper.constructParams(queryFilter);
        return bpmCheckHistoryMapper.getMyApproved(queryFilter.getPage(),params);
    }

    public List<BpmCheckHistory> getByTaskIds(List<String> tasks){
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.in("TASK_ID_",tasks);
        List list = bpmCheckHistoryMapper.selectList(wrapper);
        return  list;
    }

    /**
     * 根据任务ID获取历史。
     * @param taskId
     * @return
     */
    public BpmCheckHistory getByTaskId(String taskId){
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("TASK_ID_",taskId);
        wrapper.orderByDesc("CREATE_TIME_");
        List<BpmCheckHistory> list=bpmCheckHistoryMapper.selectList(wrapper);
        if(list==null || list.size()==0){
            return null;
        }
        return  list.get(0);
    }

    public BpmCheckHistory getByTaskIdCheckType(String taskId,String checkType){
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("TASK_ID_",taskId);
        wrapper.eq("CHECK_STATUS_",checkType);
        BpmCheckHistory history = bpmCheckHistoryMapper.selectOne(wrapper);
        return  history;
    }

    /**
     * 根据任务ID删除意见。
     * @param taskId
     */
    public void removeByTaskId(String taskId){
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("TASK_ID_",taskId);
        bpmCheckHistoryMapper.delete(wrapper);
    }

    /**
     * 根据instId查询OpinionName不为空的审批意见
     * @param instId
     * @return
     */
    public List<BpmCheckHistory> getOpinionNameNotEmpty(String instId) {
        List<BpmCheckHistory> bpmCheckHistoryList;
        BpmInstRouter bpmInstRouter = bpmInstRouterService.get(instId);
        if(BeanUtil.isNotEmpty(bpmInstRouter)){
            bpmCheckHistoryList=bpmCheckHistoryMapper.getArchiveByOpinionNameNotEmpty(instId,bpmInstRouter.getTableId());
        }else {
            bpmCheckHistoryList= bpmCheckHistoryMapper.getOpinionNameNotEmpty(instId);
        }
        List<String> userIds=new ArrayList<>();
        boolean flag=true;
        for (BpmCheckHistory bpmCheckHistory : bpmCheckHistoryList) {
            for (int i=0;i<userIds.size();i++){
                if(userIds.get(i).equals(bpmCheckHistory.getHandlerId())){
                    flag=false;
                    break;
                }
            }
            if(flag){
                userIds.add(bpmCheckHistory.getHandlerId());
            }
        }
        String userIdsStr = StringUtils.join(userIds, ",");
        List<OsUserDto> users = orgService.getUsersByIds(userIdsStr);
        for (BpmCheckHistory bpmCheckHistory : bpmCheckHistoryList) {
            for (OsUserDto user : users) {
                if(user.getUserId().equals(bpmCheckHistory.getHandlerId())){
                    bpmCheckHistory.setHandlerUserName(user.getFullName());
                    bpmCheckHistory.setHandlerUserDeptName(user.getDeptName());
                }
            }
            bpmCheckHistory.setOpFiles(bpmCheckFileService.getByHisId(bpmCheckHistory.getHisId()));
        }
        return bpmCheckHistoryList;
    }

    /**
     * 获取某个节点的所有审批历史，并按时间倒序
     *
     * @param instId
     * @param nodeId
     * @return
     */
    public List<BpmCheckHistory> getByInstIdNodeId(@Param("instId") String instId, @Param("nodeId") String nodeId){
        return bpmCheckHistoryMapper.getByInstIdNodeId(instId,nodeId);
    }

    /**
     * 获取某个节点的最新一条审批历史的数据
     * @param instId
     * @param nodeId
     * @return
     */
    public BpmCheckHistory getLatestByInstIdNodeId(@Param("instId") String instId, @Param("nodeId") String nodeId){
        return bpmCheckHistoryMapper.getLatestByInstIdNodeId(instId,nodeId);
    }

    /**
     * 删除备份的数据
     * @param instId
     * @param tableId
     */
    public void delArchiveByInstId(String instId,Integer tableId) {
        bpmCheckHistoryMapper.delArchiveByInstId(instId,tableId);
    }

    /**
     * 获取我的已办任务数
     * @param userId
     * @param tenantId
     * @return
     */
    public Integer getMyApprovedCount(String userId,String tenantId){
        return bpmCheckHistoryMapper.getMyApprovedCount(userId,tenantId);
    }
}
