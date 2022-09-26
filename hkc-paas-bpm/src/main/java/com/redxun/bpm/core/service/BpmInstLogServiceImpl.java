package com.redxun.bpm.core.service;

import com.redxun.bpm.core.entity.BpmInstLog;
import com.redxun.bpm.core.mapper.BpmInstLogMapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.IUser;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.utils.ContextUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* [流程活动日志]业务服务类
 * @author csx
*/
@Service
public class BpmInstLogServiceImpl extends SuperServiceImpl<BpmInstLogMapper, BpmInstLog> implements BaseService<BpmInstLog> {

    @Resource
    private BpmInstLogMapper bpmInstLogMapper;

    @Override
    public BaseDao<BpmInstLog> getRepository() {
        return bpmInstLogMapper;
    }

    /**
     * 添加程活动日志
     * @param instId 流程实例
     * @param opDescp 操作描述
     * @return
     */
    public BpmInstLog addInstLog(String instId,String opDescp){
        IUser curUser= ContextUtil.getCurrentUser();
        BpmInstLog log=new BpmInstLog();
        log.setId(IdGenerator.getIdStr());
        log.setInstId(instId);
        log.setUserName(curUser.getFullName());
        log.setUserId(curUser.getUserId());
        log.setOpDescp(opDescp);
        insert(log);
        return log;
    }

    /**
     * 添加流程的任务操作日志
     * @param instId
     * @param taskId
     * @param taskName
     * @param taskKey
     * @param opDescp
     * @return
     */
    public BpmInstLog addTaskLog(String instId,String taskId,String taskName,String taskKey,String opDescp){
        IUser curUser= ContextUtil.getCurrentUser();
        BpmInstLog log=new BpmInstLog();
        log.setId(IdGenerator.getIdStr());
        log.setInstId(instId);
        log.setTaskId(taskId);
        log.setTaskName(taskName);
        log.setTaskKey(taskKey);
        log.setUserName(curUser.getFullName());
        log.setUserId(curUser.getUserId());
        log.setOpDescp(opDescp);
        insert(log);
        return log;
    }

    /**
     * 流程某流程实例下的活动日志
     * @param instId 流程实例Id
     */
    public void delByInstId(String instId){
        bpmInstLogMapper.delByInstId(instId);
    }

    /**
     * 获得某流程活动列表
     * @param instId 流程实例Id
     * @return
     */
    public List<BpmInstLog> getByInstId(String instId){
        return bpmInstLogMapper.getByInstId(instId);
    }

    /**
     * 删除备份的数据
     * @param instId
     * @param tableId
     */
    public void delArchiveByInstId(String instId,Integer tableId) {
        bpmInstLogMapper.delArchiveByInstId(instId,tableId);
    }
}
