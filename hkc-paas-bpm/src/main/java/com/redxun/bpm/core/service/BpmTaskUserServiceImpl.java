package com.redxun.bpm.core.service;

import com.redxun.api.org.IOrgService;
import com.redxun.bpm.activiti.event.TaskAssignApplicationEvent;
import com.redxun.bpm.core.entity.BpmInst;
import com.redxun.bpm.core.entity.BpmInstLog;
import com.redxun.bpm.core.entity.BpmTask;
import com.redxun.bpm.core.entity.BpmTaskUser;
import com.redxun.bpm.core.mapper.BpmInstMapper;
import com.redxun.bpm.core.mapper.BpmTaskUserMapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.IUser;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ContextUtil;
import com.redxun.common.utils.SpringUtil;
import com.redxun.dto.bpm.TaskExecutor;
import com.redxun.dto.user.OsGroupDto;
import com.redxun.dto.user.OsUserDto;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
* [任务处理相关人]业务服务类
*/
@Service
public class BpmTaskUserServiceImpl extends SuperServiceImpl<BpmTaskUserMapper, BpmTaskUser> implements BaseService<BpmTaskUser> {

    @Resource
    private BpmTaskUserMapper bpmTaskUserMapper;

    @Resource
    BpmInstLogServiceImpl bpmInstLogService;

    @Resource
    BpmInstMapper bpmInstMapper;


    @Resource
    BpmTaskService bpmTaskService;

    @Resource
    IOrgService orgService;



    @Override
    public BaseDao<BpmTaskUser> getRepository() {
        return bpmTaskUserMapper;
    }

    /**
     * 按任务删除参与人员的数据
     * @param taskId
     */
    public void deleteByTaskId(String taskId){
        bpmTaskUserMapper.deleteByTaskId(taskId);
    }

    /**
     * 按任务Id获取所有的待办参与人员列表
     * @param taskId
     * @return
     */
    public List<BpmTaskUser> getByTaskId(String taskId){
        return bpmTaskUserMapper.getByTaskId(taskId);
    }

    public Set<IUser> getTaskUsers(String taskId) {
        BpmTask bpmTask = bpmTaskService.get(taskId);
        Set<IUser> userSet = new HashSet<>();
        Set<TaskExecutor> taskExecutorSet = getTaskExecutors(bpmTask);

        for(TaskExecutor taskExecutor : taskExecutorSet) {
            if (TaskExecutor.TYPE_GROUP.equals(taskExecutor.getType())) {
                if(StringUtils.isNotEmpty(taskExecutor.getId())) {
                    List<OsUserDto> userList = orgService.getByGroupId(taskExecutor.getId());
                    for (OsUserDto user : userList) {
                        OsUserDto osUserDto = orgService.getUserById(user.getUserId());
                        userSet.add(osUserDto);
                    }
                }
            } else if (TaskExecutor.TYPE_USER.equals(taskExecutor.getType())) {
                OsUserDto osUserDto = orgService.getUserById(taskExecutor.getId());
                userSet.add(osUserDto);
            }
        }

        return userSet;
    }

    /**
     * 获取任务的执行用户
     * @param bpmTask
     * @return
     */
    public Set<TaskExecutor> getTaskExecutors(BpmTask bpmTask){
        Set<TaskExecutor> executors=new LinkedHashSet<>();
        if(StringUtils.isNotEmpty(bpmTask.getAssignee())){
            OsUserDto userDto=orgService.getUserById(bpmTask.getAssignee());
            if(userDto!=null) {
                executors.add(  TaskExecutor.getUser(userDto.getUserId(), userDto.getFullName(),userDto.getAccount()));
                return executors;
            }
        }
        List<BpmTaskUser> taskUserList=getByTaskId(bpmTask.getTaskId());
        for(BpmTaskUser taskUser : taskUserList){
            if(TaskExecutor.TYPE_GROUP.equals(taskUser.getUserType())){
                if(StringUtils.isNotEmpty(taskUser.getGroupId())) {
                    OsGroupDto groupDto = orgService.getGroupById(taskUser.getGroupId());
                    if(groupDto!=null){
                        executors.add(TaskExecutor.getGroup(groupDto.getGroupId(),groupDto.getName()));
                    }
                }
            }else{
                OsUserDto userDto=orgService.getUserById(taskUser.getUserId());
                if(userDto!=null) {
                    executors.add(  TaskExecutor.getUser(userDto.getUserId(), userDto.getFullName(),userDto.getAccount()));
                }
            }
        }
        return executors;
    }

    /**
     * 设置任务执行人。
     * @param bpmTask
     * @param taskExecutors
     */
    public void createUsers(BpmTask bpmTask,Collection<TaskExecutor> taskExecutors){
        if(BeanUtil.isEmpty(taskExecutors)){
            return;
        }
        Iterator<TaskExecutor> it = taskExecutors.iterator();

        //添加任务执行人
        if(taskExecutors.size()==1){
            TaskExecutor executor=it.next();
            if(TaskExecutor.TYPE_USER.equals( executor.getType())){
                bpmTask.setAssignee(executor.getId());
                bpmTask.setOwner(executor.getId());

                //只有一个人的时候发布任务分配事件
                TaskAssignApplicationEvent event=new TaskAssignApplicationEvent(bpmTask);
                SpringUtil.publishEvent(event);


                bpmTaskService.update(bpmTask);

            }
            else {
                handCandidate(bpmTask,executor);
            }
        }
        else{
           while (it.hasNext()){
               TaskExecutor executor=it.next();
               handCandidate(bpmTask,executor);
           }
        }
    }

    /**
     * 给任务分配用户。
     * @param bpmTask
     * @param executor
     */
    public void createUser(BpmTask bpmTask,TaskExecutor executor){

        if(TaskExecutor.TYPE_USER.equals( executor.getType())){
            bpmTask.setAssignee(executor.getId());
            bpmTask.setOwner(executor.getId());

            //发布任务分配事件
            SpringUtil.publishEvent(new TaskAssignApplicationEvent(bpmTask));

            bpmTaskService.update(bpmTask);

        }
        else {
            handCandidate( bpmTask,executor);
        }
    }



    private void handCandidate(BpmTask bpmTask, TaskExecutor executor){
        if(TaskExecutor.CALC_DELAY.equals( executor.getCalcType())){
            if(StringUtils.isNotEmpty(executor.getId())) {
                List<OsUserDto> users = orgService.getByGroupId(executor.getId());
                for (OsUserDto dto : users) {
                    addUser(bpmTask, BpmTaskUser.PART_TYPE_CANDIDATE, TaskExecutor.TYPE_USER, dto.getUserId());
                }
            }
        }
        else{
            addUser(bpmTask,BpmTaskUser.PART_TYPE_CANDIDATE, executor.getType(),executor.getId());
        }
    }


    private void addUser(BpmTask bpmTask,String partType,String type,String id){
        BpmTaskUser bpmTaskUser=new BpmTaskUser();

        bpmTaskUser.setPartType(partType);
        if(TaskExecutor.TYPE_USER.equals(type)){
            bpmTaskUser.setUserId(id);
            //发布任务分配事件
            TaskAssignApplicationEvent event = new TaskAssignApplicationEvent(bpmTask);
            event.setBpmTaskUser(bpmTaskUser);
            SpringUtil.publishEvent(event);
        }
        else{
            bpmTaskUser.setGroupId(id);
        }
        bpmTaskUser.setIsRead(BpmTaskUser.IS_UNREAD);
        bpmTaskUser.setTaskId(bpmTask.getTaskId());
        bpmTaskUser.setUserType(type);
        bpmTaskUser.setInstId(bpmTask.getInstId());
        bpmTaskUser.setId(IdGenerator.getIdStr());
        this.insert(bpmTaskUser);


    }

    /**
     * 设置执行人
     * @param bpmTask
     * @param userIds
     */
    public void setAssignee(BpmTask bpmTask, List<String>  userIds) {

        if(userIds.size()==1){
            bpmTask.setAssignee(userIds.get(0));
            bpmTask.setExecutor("1");
            bpmTaskService.updateById(bpmTask);
        }else {
            for (String userId : userIds) {
                addUser(bpmTask, BpmTaskUser.PART_TYPE_CANDIDATE, TaskExecutor.TYPE_USER, userId);
            }
            bpmTask.setExecutor("1");
            bpmTaskService.updateById(bpmTask);
        }
        //添加流程实例日志
        if(userIds.size()>0){
            IUser currentUser = ContextUtil.getCurrentUser();
            BpmInstLog bpmInstLog=new BpmInstLog();
            bpmInstLog.setInstId(bpmTask.getInstId());
            bpmInstLog.setTaskId(bpmTask.getTaskId());
            bpmInstLog.setUserId(currentUser.getUserId());
            bpmInstLog.setUserName(currentUser.getFullName());
            bpmInstLog.setTaskKey(bpmTask.getKey());
            bpmInstLog.setTaskName(bpmTask.getName());
            bpmInstLog.setOpDescp("设置执行人");
            bpmInstLogService.insert(bpmInstLog);
        }
    }

    /**
     * 获取任务的执行用户(用户组则查询组内用户)
     * @param bpmTask
     * @return
     */
    public Set<TaskExecutor> getExecutors(BpmTask bpmTask){
        //申请人
        BpmInst bpmInst = bpmInstMapper.selectById(bpmTask.getInstId());
        OsUserDto applicant = orgService.getUserById(bpmInst.getCreateBy());
        bpmTask.setApplicantName(applicant.getFullName());
        bpmTask.setApplicantNo(applicant.getAccount());

        //执行人
        Set<TaskExecutor> executors=new LinkedHashSet<>();
        if(StringUtils.isNotEmpty(bpmTask.getAssignee())){
            OsUserDto userDto=orgService.getUserById(bpmTask.getAssignee());
            if(userDto!=null) {
                executors.add(  TaskExecutor.getUser(userDto.getUserId(), userDto.getFullName(),userDto.getAccount()));
                return executors;
            }
        }
        List<BpmTaskUser> taskUserList=getByTaskId(bpmTask.getTaskId());
        for(BpmTaskUser taskUser : taskUserList){
            //用户组则查询组内用户
            if(TaskExecutor.TYPE_GROUP.equals(taskUser.getUserType())){
                if(StringUtils.isNotEmpty(taskUser.getGroupId())) {
                    List<OsUserDto> userDtos = orgService.getByGroupId(taskUser.getGroupId());
                    if(userDtos!=null){
                        for (OsUserDto user : userDtos) {
                            executors.add(  TaskExecutor.getUser(user.getUserId(), user.getFullName(),user.getAccount()));
                        }
                    }
                }
            }else{
                OsUserDto userDto=orgService.getUserById(taskUser.getUserId());
                if(userDto!=null) {
                    executors.add(  TaskExecutor.getUser(userDto.getUserId(), userDto.getFullName(),userDto.getAccount()));
                }
            }
        }
        return executors;
    }

    /**
     * 修改执行人
     * @param receiptUserId
     * @param deliverUserId
     */
    public void updateUserId(String receiptUserId, String deliverUserId) {
        bpmTaskUserMapper.updateUserId(receiptUserId, deliverUserId);
    }
}


