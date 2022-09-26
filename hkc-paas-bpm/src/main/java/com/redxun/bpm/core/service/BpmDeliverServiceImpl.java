
package com.redxun.bpm.core.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.bpm.core.entity.BpmDeliverLog;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.bpm.core.entity.BpmDeliver;
import com.redxun.bpm.core.mapper.BpmDeliverMapper;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ContextUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import javax.annotation.Resource;

/**
* [流程待办任务交接]业务服务类
*/
@Service
public class BpmDeliverServiceImpl extends SuperServiceImpl<BpmDeliverMapper, BpmDeliver> implements BaseService<BpmDeliver> {

    @Resource
    private BpmDeliverMapper bpmDeliverMapper;

    @Resource
    private BpmTaskService bpmTaskService;

    @Resource
    private BpmTaskUserServiceImpl bpmTaskUserService;

    @Resource
    BpmDeliverLogServiceImpl bpmDeliverLogService;

    @Resource
    BpmSignDataServiceImpl bpmSignDataService;

    @Override
    public BaseDao<BpmDeliver> getRepository() {
        return bpmDeliverMapper;
    }


    /**
     * 新增或者修改时，同步将移交人的待办任务交接给接管人
     * @param entity
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public JsonResult dealDeliver(BpmDeliver entity) {
        JsonResult jsonResult= JsonResult.Success("保存成功");

        if(StringUtils.isEmpty(entity.getId())){
            entity.setId(IdGenerator.getIdStr());
        }

        dealAgentTask(entity);

        super.saveOrUpdate(entity);
        return jsonResult;
    }

    /**
     * 处理待办任务
     * @param entity
     */
    private void dealAgentTask(BpmDeliver entity){
        //流程待办任务交接
        bpmTaskService.updateAssignee(entity.getReceiptUserId(), entity.getDeliverUserId());
        bpmTaskUserService.updateUserId(entity.getReceiptUserId(), entity.getDeliverUserId());

        //记录交接日志
        BpmDeliverLog bpmDeliverLog = new BpmDeliverLog();
        cn.hutool.core.bean.BeanUtil.copyProperties(entity, bpmDeliverLog);
        bpmDeliverLog.setId(IdGenerator.getIdStr());
        bpmDeliverLog.setCreateByName(ContextUtil.getCurrentUser().getFullName());
        bpmDeliverLogService.save(bpmDeliverLog);
    }


    /**
     * 根据移交人获取交接信息
     *
     * @param deliverUserId
     * @return
     */
    public BpmDeliver getByDeliverUserId(String deliverUserId){
        BpmDeliver entity = null;
        if(StringUtils.isEmpty(deliverUserId)){
            return null;
        }
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("DELIVER_USER_ID_",deliverUserId);
        List<BpmDeliver> list = bpmDeliverMapper.selectList(queryWrapper);
        if(com.redxun.common.tool.BeanUtil.isEmpty(list) ||  list.size() <= 0){
            return null;
        }

        for(BpmDeliver bpmDeliver : list){
            if(com.redxun.common.tool.BeanUtil.isNotEmpty(entity)){
                //多个，用最新的
                if(entity.getCreateTime().before(bpmDeliver.getCreateTime())){
                    entity = bpmDeliver;
                }
            }else {
                entity = bpmDeliver;
            }

        }

        return entity;
    }

}
