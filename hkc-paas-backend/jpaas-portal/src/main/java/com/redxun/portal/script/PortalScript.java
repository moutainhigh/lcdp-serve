package com.redxun.portal.script;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.redxun.api.bpm.IBpmTaskService;
import com.redxun.common.base.entity.*;
import com.redxun.common.model.JPaasUser;
import com.redxun.common.script.GroovyScript;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.utils.ContextUtil;
import com.redxun.dto.bpm.BpmTaskDto;
import com.redxun.portal.context.remind.RemindDataService;
import com.redxun.portal.core.entity.InsAppCollect;
import com.redxun.portal.core.entity.InsColumnDef;
import com.redxun.portal.core.entity.InsColumnEntity;
import com.redxun.portal.core.entity.InsRemindDef;
import com.redxun.portal.core.service.InfInnerMsgServiceImpl;
import com.redxun.portal.core.service.InsAppCollectServiceImpl;
import com.redxun.portal.core.service.InsColumnDefServiceImpl;
import com.redxun.portal.core.service.InsMsgDefServiceImpl;
import com.redxun.portal.feign.BpmTaskClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 流程脚本处理类，放置于脚本运行的环境, 配置@ClassDefine及@MethodDefine目的是
 * 为了可以把系统中自带的API显示出来给配置人员查看及选择使用
 */
@Component
public class PortalScript implements GroovyScript {

    @Resource
    IBpmTaskService bpmTaskService;
    @Resource
    BpmTaskClient bpmTaskClient;
    @Autowired
    InsColumnDefServiceImpl insColumnDefService;
    @Autowired
    InsMsgDefServiceImpl insMsgDefService;
    @Autowired
    InfInnerMsgServiceImpl infInnerMsgService;

    @Resource
    RemindDataService remindDataService;
    @Resource
    InsAppCollectServiceImpl insAppCollectService;

    /**
     * 我的常用应用
     * @return
     */
    public List<InsAppCollect> getMyAppList() {
        List<InsAppCollect> myAppList = insAppCollectService.getCommonApp();
        return myAppList;
    }

    /**
     * 门户消息提醒栏目数据
     * 我的消息提醒
     * @return
     */
    public List<InsRemindDef> getRemindList() {
        List<InsRemindDef> data = remindDataService.getData();
        return data;
    }

    /**
     * 门户待办栏目数据
     * 我的消息
     * @return
     */
    public InsColumnEntity getMyAllInnerMsg(String colId) {
        InsColumnDef col = insColumnDefService.get(colId);
        JSONArray list = new JSONArray();
        IUser curUser=ContextUtil.getCurrentUser();
        IPage page=new Page(1,8);
        IPage msgPage=infInnerMsgService.getByRecUserId(page,curUser.getUserId(),curUser.getRoles());
        list.addAll(msgPage.getRecords());

        InsColumnEntity entity = new InsColumnEntity(col.getName(), "", new Long(msgPage.getTotal()).intValue(), list);
        return entity;
    }

    /**
     * 获取我的内部消息
     * @return
     */
    public Integer getMyInnerMsgCount(){
        IUser curUser= ContextUtil.getCurrentUser();
        Integer msgCounts=infInnerMsgService.getCountByRecUserId(curUser.getUserId(),curUser.getRoles(),ContextUtil.getCurrentTenantId());
        return msgCounts;
    }


    /**
     * 门户待办栏目数据
     * 我的待办栏目
     *
     * @return
     */
    public List getPortalBpmTask() {
        QueryData queryData = new QueryData();
        queryData.setPageNo(1);
        queryData.setPageSize(8);
        queryData.setSortOrder("asc");
        JsonResult result =bpmTaskService.myTasks(queryData);
        List list=new ArrayList();
        if(!result.isSuccess()){
            return list;
        }
        JsonPage pageData =((JsonPageResult) result).getResult();
        List<BpmTaskDto> listData =pageData.getData();
        return listData;
    }

    /**
     * 查询我的待办
     *
     * @return
     */
    public InsColumnEntity getMyTasks(String colId) {
        InsColumnDef col = insColumnDefService.get(colId);
        JSONArray list = new JSONArray();
        QueryData queryData = new QueryData();
        queryData.setPageNo(1);
        queryData.setPageSize(8);
        queryData.setSortOrder("asc");
        JsonPageResult result =bpmTaskService.myTasks(queryData);
        if(result.isSuccess()){
            List<Object> listData =( List<Object>)result.getData();
            InsColumnEntity entity=null;
            if(listData!=null) {
                list.addAll(listData);
                entity = new InsColumnEntity(col.getName(), "", listData.size(), list);
            }else{
                entity = new InsColumnEntity(col.getName(), "", 0, list);
            }
            return entity;
        }
        InsColumnEntity entity = new InsColumnEntity(col.getName(), "", 0, list);
        return entity;
    }

    /**
     * 查询我的待办数
     * @return
     */
    public Integer getMyTaskCount() {
        JsonResult countResult =bpmTaskService.getMyTaskCounts();
        return (Integer)countResult.getData();
    }

    /**
     * 查询我的已办流程实例
     *
     * @return
     */
    public List getMyApprovedInsts() {
        List list = new ArrayList();
        JsonPageResult rs =bpmTaskService.getMyApprovedInsts();
        if(rs.isSuccess()) {
            list = (List<Object>) rs.getResult().getData();
        }
        return list;
    }

    /**
     * 查询我的已办流程实例数
     *
     * @return
     */
    public InsColumnEntity getMyApprovedInstCount(String colId) {
        InsColumnDef col = insColumnDefService.get(colId);
        JSONArray list = new JSONArray();
        JsonResult countResult =bpmTaskService.getMyApprovedInstCount();
        InsColumnEntity   entity = new InsColumnEntity(col.getName(), "", (Integer)countResult.getData(), list);
        return entity;
    }

    /**
     * 查询我的已办流程任务数
     *
     * @return
     */
    public Integer getMyApprovedTaskCount() {
        JsonResult countResult =bpmTaskClient.getMyApprovedTaskCount();
        return (Integer)countResult.getData();

    }

    /**
     * 查询我的已办历史
     * 我的已办
     *
     * @return
     */
    public InsColumnEntity getMyAllApproved(String colId) {
        InsColumnDef col = insColumnDefService.get(colId);
        JSONArray list = new JSONArray();
        JsonResult result =bpmTaskService.getMyAllApproved();
        if(result.isSuccess()){
            List<Object> listData =( List<Object>)result.getData();
            list.addAll(listData);
            InsColumnEntity entity = new InsColumnEntity(col.getName(), "", listData.size(), list);
            return entity;
        }
        InsColumnEntity entity = new InsColumnEntity(col.getName(), "", 0, list);
        return entity;
    }
    /**
     * 查询我创建的草稿
     * 我创建的草稿
     *
     * @return
     */
    public Integer getMyAllDraftBpmInst() {
        JsonResult result =bpmTaskService.getMyAllDraftBpmInst();
        List<Object> listData =( List<Object>)result.getData();
        if(BeanUtil.isEmpty(listData)){
            return 0;
        }
        return listData.size();
    }

    /**
     * 返回当前登录用户测试。
     * @return
     */
    public JPaasUser getCurrentUser(){
        JPaasUser user= (JPaasUser) ContextUtil.getCurrentUser();
        return user;
    }
}
