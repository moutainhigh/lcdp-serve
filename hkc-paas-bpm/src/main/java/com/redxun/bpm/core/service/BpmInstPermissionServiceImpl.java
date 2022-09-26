
package com.redxun.bpm.core.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.api.org.IOrgService;
import com.redxun.bpm.core.entity.*;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.IUser;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.constant.MBoolean;
import com.redxun.common.model.JPaasUser;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.DateUtils;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.bpm.core.mapper.BpmInstPermissionMapper;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ContextUtil;
import com.redxun.dto.bpm.TaskExecutor;
import com.redxun.dto.user.OsUserDto;
import org.springframework.stereotype.Service;

import java.util.*;
import javax.annotation.Resource;

/**
* [流程实例权限表]业务服务类
*/
@Service
public class BpmInstPermissionServiceImpl extends SuperServiceImpl<BpmInstPermissionMapper, BpmInstPermission> implements BaseService<BpmInstPermission> {

    @Resource
    private BpmInstPermissionMapper bpmInstPermissionMapper;
    @Resource
    BpmAuthServiceImpl bpmAuthService;
    @Resource
    IOrgService orgService;

    @Override
    public BaseDao<BpmInstPermission> getRepository() {
        return bpmInstPermissionMapper;
    }

    public JsonResult createTaskInfo(BpmInst bpmInst, IExecutionCmd cmd){
        JsonResult result=JsonResult.Success("数据成功!");
        List<BpmTask> tasks = cmd.getTasks();
        if(BeanUtil.isEmpty(tasks)){
            return result;
        }
        for(BpmTask bpmTask:tasks) {
            List<OsUserDto> recievers = getReceiveUser(bpmTask.getTaskExecutors());
            for(OsUserDto osUserDto:recievers){
                IUser user = new JPaasUser();
                user.setUserId(osUserDto.getUserId());
                user.setFullName(osUserDto.getFullName());
                user.setDeptId(osUserDto.getDeptId());
                create(bpmInst,BpmInstPermission.TYPE_HANDLER, bpmTask.getTaskId(), user);
            }
        }
        return result;
    }

    private List<OsUserDto> getReceiveUser(Set<TaskExecutor> executors){
        List<OsUserDto> list=new ArrayList<>();
        for(TaskExecutor executor:executors){
            if(TaskExecutor.TYPE_USER.equals(executor.getType())){
                OsUserDto user=orgService.getUserById(executor.getId());
                list.add(user);
            }
            else{
                if(StringUtils.isNotEmpty(executor.getId())) {
                    List<OsUserDto> dtoList = orgService.getByGroupId(executor.getId());
                    for (OsUserDto user : dtoList) {
                        list.add(user);
                    }
                }
            }
        }
        return list;
    }

    @Override
    public boolean save(BpmInstPermission entity) {
        Integer count=getByInstId(entity.getInstId(),entity.getAuthId(),entity.getTaskId());
        if(count>0){
            return false;
        }
        return super.save(entity);
    }

    public JsonResult create(BpmInst bpmInst, String type, String taskId, IUser user) {
        if(BeanUtil.isEmpty(user)){
            user= ContextUtil.getCurrentUser();
        }

        BpmInstPermission permission=new BpmInstPermission();
        permission.setId(IdGenerator.getIdStr())
                .setAuthId(user.getUserId())
                .setAuthName(user.getFullName())
                .setInstId(bpmInst.getInstId())
                .setDefId(bpmInst.getDefId())
                .setDefKey(bpmInst.getDefCode())
                .setSubject(bpmInst.getSubject())
                .setType(type)
                .setTaskId(taskId);

        permission.setCreateBy(user.getUserId());
        permission.setCreateTime(new Date());
        permission.setCreateDepId(user.getDeptId());

        this.save(permission);

        return JsonResult.Success("数据成功!");
    }

    public void create(BpmDef bpmDef,String id,String type, OsUserDto dto, String desc) {
        IUser user= ContextUtil.getCurrentUser();
        BpmInstPermission permission=new BpmInstPermission();
        permission.setId(id)
                .setAuthId(dto.getUserId())
                .setAuthName(dto.getFullName())
                .setDefId(bpmDef.getDefId())
                .setDefKey(bpmDef.getKey())
                .setType(type).setDesc(desc);

        permission.setCreateBy(user.getUserId());
        permission.setCreateTime(new Date());
        permission.setCreateDepId(user.getDeptId());
        this.insert(permission);

    }

    public void create(BpmInst bpmInst, String type, OsUserDto dto,String desc, String taskId) {
        IUser user= ContextUtil.getCurrentUser();
        BpmInstPermission permission=new BpmInstPermission();
        permission.setId(IdGenerator.getIdStr())
                .setAuthId(dto.getUserId())
                .setAuthName(dto.getFullName())
                .setInstId(bpmInst.getInstId())
                .setDefId(bpmInst.getDefId())
                .setDefKey(bpmInst.getDefCode())
                .setSubject(bpmInst.getSubject())
                .setType(type).setDesc(desc)
                .setTaskId(taskId);

        permission.setCreateBy(user.getUserId());
        permission.setCreateTime(new Date());
        permission.setCreateDepId(user.getDeptId());

        this.save(permission);
    }

    public Integer getByInstIdAuthId(String instId,String authId,List<String> taskIdList){
        if(BeanUtil.isEmpty(taskIdList)){
            return 0;
        }
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("INST_ID_",instId);
        queryWrapper.eq("AUTH_ID_",authId);
        queryWrapper.eq("TYPE_",BpmInstPermission.TYPE_HANDLER);
        queryWrapper.in("TASK_ID_",taskIdList);
        return bpmInstPermissionMapper.selectCount(queryWrapper);
    }

    public Integer getCountByDefCode(String defCode, String userId) {
        int count=0;
        List<BpmInstPermission> list=bpmInstPermissionMapper.getByAdmin(userId,defCode);
        for(BpmInstPermission bpmInstPermission:list){
            BpmAuth bpmAuth=bpmAuthService.getById(bpmInstPermission.getId());
            if(bpmAuth==null){
                continue;
            }
            if(MBoolean.NO.name().equals(bpmAuth.getStatus())){
                continue;
            }
            if(!"长期".equals(bpmAuth.getActiveTime()) && new Date().after(DateUtils.parseDate(bpmAuth.getActiveTime(),DateUtils.YYYY_MM_DD))){
                continue;
            }
            count++;
        }
        return count;
    }

    public Integer getByInstId(String instId,String userId,String taskId){
        Integer rtn=bpmInstPermissionMapper.getByInstId(instId,userId,taskId);
        return  rtn;
    }

    public Integer getByInstId(String instId,String userId) {
        Integer rtn=getByInstId(instId,userId,null);
        return  rtn;
    }

}
