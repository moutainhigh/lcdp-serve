package com.redxun.bpm.activiti.event.handler;

import com.redxun.bpm.activiti.config.ProcessConfig;
import com.redxun.bpm.activiti.event.EventType;
import com.redxun.bpm.activiti.event.ProcessCompletedEvent;
import com.redxun.bpm.activiti.processhandler.ProcessHandlerExecutor;
import com.redxun.bpm.core.entity.BpmInst;
import com.redxun.bpm.core.entity.BpmInstData;
import com.redxun.bpm.core.entity.BpmInstStatus;
import com.redxun.bpm.core.entity.IExecutionCmd;
import com.redxun.bpm.core.service.BpmDefService;
import com.redxun.bpm.core.service.BpmInstDataServiceImpl;
import com.redxun.bpm.core.service.BpmInstServiceImpl;
import com.redxun.bpm.util.ProcessHandleUtil;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.utils.SpringUtil;
import com.redxun.dto.bpm.BpmConst;
import com.redxun.dto.bpm.BpmInstDataDto;
import com.redxun.feign.form.FormClient;
import org.activiti.engine.delegate.event.ActivitiEntityEvent;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 流程启动事件监听处理器
 *
 */
public class ProcessCompletedEventHandler extends BaseProcessHandler<ActivitiEntityEvent>   {

    /**
     * 事件类型
     * @return
     */
    @Override
    public EventType getEventType() {
        return EventType.PROCESS_COMPLETED;
    }

    @Override
    public void handle(ActivitiEntityEvent eventEntity) {
        BpmDefService bpmDefService=SpringUtil.getBean(BpmDefService.class);
        BpmInstServiceImpl bpmInstService=SpringUtil.getBean(BpmInstServiceImpl.class);
        ProcessHandlerExecutor execute=SpringUtil.getBean(ProcessHandlerExecutor.class);

        IExecutionCmd cmd= ProcessHandleUtil.getProcessCmd();
        //获取流程全局配置
        ProcessConfig processConfig= (ProcessConfig) cmd.getTransientVar(BpmConst.PROCESS_CONFIG);
        if(BeanUtil.isEmpty(processConfig)){
            processConfig = bpmDefService.getProcessConfig(eventEntity.getProcessDefinitionId());
        }
        //获取执行实例对象
        ExecutionEntity entity=(ExecutionEntity) eventEntity.getEntity() ;
        //获取流程实例相关参数
        String instId= (String) entity.getVariable("instId");
        BpmInst bpmInst=bpmInstService.get(instId);
        bpmInst.setStatus(BpmInstStatus.SUCCESS_END.name());
        bpmInst.setEndTime(new Date());
        //处理相关事件
        handEvent((ExecutionEntity) eventEntity.getEntity());
        //处理结束事件。
        execute.handEndHandler(processConfig,bpmInst);
        //发布事件。
        publishEvent(processConfig, (ExecutionEntity) eventEntity.getEntity(),bpmInst);
        //更新流程实例
        bpmInstService.update(bpmInst);

        /**
         * 更新表单数据未终结。
         */
        updDataStatus(bpmInst.getInstId());
    }

    private void publishEvent(ProcessConfig processConfig,ExecutionEntity entity,BpmInst bpmInst){
        ProcessCompletedEvent completedEvent=new ProcessCompletedEvent(processConfig,entity,bpmInst);
        SpringUtil.publishEvent(completedEvent);
    }

    /**
     * 更新表单数据的状态。
     * @param instId
     */
    private void updDataStatus(String instId)  {
        BpmInstDataServiceImpl bpmInstService=SpringUtil.getBean(BpmInstDataServiceImpl.class);

        FormClient  formClient=SpringUtil.getBean(FormClient.class);
        List<BpmInstData> bpmInstDataList= bpmInstService.getByInstId(instId);


        List<BpmInstDataDto> dataDtoList=new ArrayList<>();
        for (BpmInstData data:bpmInstDataList) {
            BpmInstDataDto bpmInstDataDto=new BpmInstDataDto();
            bpmInstDataDto.setBodefAlias(data.getBodefAlias());
            bpmInstDataDto.setInstId(data.getInstId());
            bpmInstDataDto.setPk(data.getPk());
            bpmInstDataDto.setStatus(BpmInstStatus.SUCCESS_END.name());
            dataDtoList.add(bpmInstDataDto);
        }

        formClient.updStatusByInstId(dataDtoList);

    }


}
