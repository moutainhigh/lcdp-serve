package com.redxun.bpm.script;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.redxun.bpm.activiti.ActInstService;
import com.redxun.bpm.activiti.config.SignConfig;
import com.redxun.bpm.core.entity.*;
import com.redxun.bpm.core.service.*;
import com.redxun.bpm.feign.SystemClient;
import com.redxun.bpm.script.cls.*;
import com.redxun.bpm.util.ProcessHandleUtil;
import com.redxun.common.base.entity.IUser;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ContextUtil;
import com.redxun.db.CommonDao;
import com.redxun.dto.bpm.TaskExecutor;
import com.redxun.dto.user.OsUserDto;
import com.redxun.feign.org.OrgClient;
import org.activiti.engine.RuntimeService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 流程变量函数
 */
@ClassScriptType(type="ProcessApi",description = "流程函数")
@Component("ProcessApi")
public class ProcessScript implements IScript {

    @Resource
    CommonDao commonDao;
    @Resource
    OrgClient orgClient;
    @Resource
    SystemClient systemClient;

    @Resource
    RuntimeService runtimeService;
    @Resource
    ActInstService actInstService;
    @Resource
    BpmRuPathServiceImpl bpmRuPathService;
    @Resource
    BpmCheckHistoryServiceImpl bpmCheckHistoryService;
    @Resource
    BpmInstServiceImpl bpmInstService;
    @Resource
    BpmTaskService bpmTaskService;
    @Resource
    BpmTaskUserServiceImpl bpmTaskUserService;

    @MethodDefine(title = "执行第三方接口")
    public JsonResult invokeInterface(@ParamDefine(varName = "apiId",description = "接口ID")String apiId,@ParamDefine(varName = "params",description = "接口参数")JSONObject params){
        return systemClient.executeApi(apiId,params);
    }

    @MethodDefine(title ="让流程继续往下执行")
    public void trigger(@ParamDefine(varName = "actInstId",description = "流程实例ID")String actInstId,@ParamDefine(varName = "jumpType",description = "意见类型")String jumpType,@ParamDefine(varName = "opinion",description = "审批意见")String opinion){
        BpmInst bpmInst=bpmInstService.getByActInstId(actInstId);
        if(bpmInst==null){
            return;
        }
        BpmRuPath bpmRuPath=bpmRuPathService.getFarestPath(bpmInst.getInstId());
        if(bpmRuPath==null){
            return;
        }
        actInstService.trigger(bpmRuPath.getExecutionId(),jumpType,opinion);
    }

    @MethodDefine(title="根据任务ID获取是否有执行人")
    public boolean hasApprover(@ParamDefine(varName = "taskId",description = "任务ID")String taskId){
        BpmTask bpmTask=bpmTaskService.get(taskId);
        if(StringUtils.isNotEmpty( bpmTask.getAssignee())){
            return true;
        }
        List<BpmTaskUser> taskUsers = bpmTaskUserService.getByTaskId(taskId);
        if(BeanUtil.isNotEmpty(taskUsers)){
            return  true;
        }
        return  false;
    }

    @MethodDefine(title="根据流程实例ID获取上个节点的执行人ID")
    public String getFirstExecutor(@ParamDefine(varName = "instId",description = "流程实例ID")String instId){
        String  curUserId = ContextUtil.getCurrentUserId();
        List<BpmTask> listTask=bpmTaskService.getByInstId(instId);
        if(listTask==null || listTask.size()!=1){
            return curUserId;
        }
        String userId=listTask.get(0).getOwner();
        return userId;
    }

    @MethodDefine(title="获取流程状态")
    public String getStatusByInstId(@ParamDefine(varName = "instId",description = "流程实例Id")String instId){
        if(StringUtils.isEmpty(instId)){
            return null;
        }
            BpmInst bpmInst = bpmInstService.getById(instId);
        if(BeanUtil.isEmpty(bpmInst)){
            return null;
        }
        return bpmInst.getStatus();
    }

    @MethodDefine(title="获取流程状态集合")
    public List<String> getStatusByInstIds(@ParamDefine(varName = "instIds",description = "流程实例Id")String instIds){
        List<String> status=new ArrayList<>();
        if(StringUtils.isEmpty(instIds)){
            return status;
        }
        String[] ids=instIds.split(",");
        for(int i=0;i<ids.length;i++) {
            BpmInst bpmInst = bpmInstService.getById(ids[i]);
            if (BeanUtil.isEmpty(bpmInst)) {
                status.add(null);
            }else{
                status.add(bpmInst.getStatus());
            }
        }
        return status;
    }

    @MethodDefine(title="获取流程变量")
    public Object getVar(@ParamDefine(varName = "actInstId",description = "Act流程实例Id") String actInstId,
                         @ParamDefine(varName = "varKey",description = "变量Key") String varKey){
        return runtimeService.getVariable(actInstId,varKey);
    }

    @MethodDefine(title="设置变量值")
    public void setVar(@ParamDefine(varName = "actInstId",description = "Act流程实例Id") String actInstId,
                       @ParamDefine(varName = "varKey",description = "变量Key") String varKey,
                       @ParamDefine(varName = "varVal",description = "变量值") String varVal){
        runtimeService.setVariable(actInstId,varKey,varVal);
    }

    @MethodDefine(title="获取会签结果")
    public boolean getSignResult(){
         IExecutionCmd cmd= ProcessHandleUtil.getProcessCmd();
         Object  result = cmd.getTransientVar(SignConfig.SIGN_RESULT);
         if(result==null){
             return false;
         }
         if(  result instanceof  Boolean){
             return (Boolean)result;
         }
         return false;
    }

    @MethodDefine(title="往表中插入数据")
    public int insert(@ParamDefine(varName = "tableName",description = "表名") String tableName,
                       @ParamDefine(varName = "pkField",description = "主键字段") String pkField,
                       @ParamDefine(varName = "jsonFields",description = "字段数据") String jsonFields){

        int rtn= this.insert("",tableName,pkField,"",jsonFields);
        return rtn;

    }

    @MethodDefine(title="往表中插入数据")
    public int insert(@ParamDefine(varName = "tableName",description = "表名") String tableName,
                       @ParamDefine(varName = "jsonFields",description = "字段数据") String jsonFields){
        int rtn= this.insert("",tableName,"ID_","",jsonFields);
        return rtn;
    }

    @MethodDefine(title="往表中插入数据")
    public int insert(@ParamDefine(varName = "dsName",description = "数据源名称") String dsName,
                       @ParamDefine(varName = "tableName",description = "表名") String tableName,
                       @ParamDefine(varName = "pkField",description = "主键字段") String pkField,
                       @ParamDefine(varName = "pk",description = "主键") String pk,
                       @ParamDefine(varName = "jsonFields",description = "字段数据") String jsonFields){
        DruidDataSource ds;

        JSONObject json=JSONObject.parseObject(jsonFields);

        String sql="insert into " +tableName ;

        List<String> names=new ArrayList<>();
        List<String> vals=new ArrayList<>();
        Map<String,Object> vMap=new HashMap<>();

        names.add(pkField);
        vals.add("#{"+ pkField +"}");
        if(StringUtils.isEmpty(pk)){
            vMap.put(pkField, IdGenerator.getIdStr());
        }
        else{
            vMap.put(pkField, pk);
        }

        for(String key :json.keySet()){
            names.add(key);
            vals.add("#{"+ key +"}");
            vMap.put(key,json.get(key));
        }

        sql +="("+ StringUtils.join(names) +") values ("+  StringUtils.join(vals) +")";

        int rtn=0;

        if(StringUtils.isEmpty(dsName)){
            rtn=commonDao.execute(sql,vMap);
        }
        else {
            rtn=commonDao.execute(dsName,sql,vMap);
        }
        return rtn;
    }

    @MethodDefine(title="获取流程所有审批人")
    public Collection<TaskExecutor> getAllApprovers(){
        List<String> list=new ArrayList<>();
        IExecutionCmd cmd= ProcessHandleUtil.getProcessCmd();
        String instId = cmd.getInstId();
        List<BpmCheckHistory> bpmCheckHistories = bpmCheckHistoryService.getByInstId(instId);
        if(BeanUtil.isNotEmpty(bpmCheckHistories)){
            for (BpmCheckHistory bpmCheckHistory : bpmCheckHistories) {
                list.add(bpmCheckHistory.getHandlerId());
            }
        }
        //去重
        HashSet set = new HashSet(list);
        list.clear();
        list.addAll(set);

        String approverStr = StringUtils.join(list.toArray(), ",");
        List<OsUserDto> users = orgClient.getUsersByIds(approverStr);
        Collection<TaskExecutor> approvers=new ArrayList<>();
        for (OsUserDto user : users) {
            TaskExecutor taskExecutor=TaskExecutor.getUser(user);
            approvers.add(taskExecutor);
        }
        return approvers;
    }

}
