package com.redxun.bpm.core.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.redxun.api.org.IOrgService;
import com.redxun.bpm.core.entity.*;
import com.redxun.bpm.core.mapper.BpmInstCcMapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.db.PageHelper;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.base.search.QueryFilter;
import com.redxun.common.constant.MBoolean;
import com.redxun.common.model.JPaasUser;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.utils.ContextUtil;
import com.redxun.dto.user.OsUserDto;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
* [流程抄送/转发]业务服务类
*/
@Service
public class BpmInstCcServiceImpl extends SuperServiceImpl<BpmInstCcMapper, BpmInstCc> implements BaseService<BpmInstCc> {

    @Resource
    private BpmInstCcMapper bpmInstCcMapper;
    @Resource
    private BpmInstCpServiceImpl bpmInstCpService;
    @Resource
    private BpmInstServiceImpl bpmInstService;
    @Resource
    private IOrgService orgService;
    @Resource
    private BpmCheckHistoryServiceImpl bpmCheckHistoryService;
    @Resource
    private BpmTaskService bpmTaskService;


    @Override
    public BaseDao<BpmInstCc> getRepository() {
        return bpmInstCcMapper;
    }


    /**
     * 获取我转出的抄送(转发)
     * @param queryFilter
     * @return
     */
    public   IPage getMyTurnTo(QueryFilter queryFilter){
        Map<String,Object> params= PageHelper.constructParams(queryFilter);
        return  bpmInstCcMapper.getMyTurnTo(queryFilter.getPage(),params);
    }

    /**
     * 获取我收到的抄送(转发)
     * @param queryFilter
     * @return
     */
    public   IPage getMyReceiveTurn(QueryFilter queryFilter){
        Map<String,Object> params= PageHelper.constructParams(queryFilter);
        return  bpmInstCcMapper.getMyReceiveTurn(queryFilter.getPage(),params);
    }

    /**
     * 根据实例Id获取抄送情况。
     * @param instId
     * @return
     */
    public List<BpmInstCc> getByInstId(String instId){
        List<BpmInstCc> list= bpmInstCcMapper.getByInstId(instId);
        return list;
    }


    /**
     * 删除备份的数据
     * @param instId
     * @param tableId
     */
    public void delArchiveByInstId(String instId,Integer tableId) {
        bpmInstCcMapper.delArchiveByInstId(instId,tableId);
    }


    /**
     * 转发流程实例。
     * <pre>
     *
     * 1.如果是转发
     *  1.添加抄送。
     *  2.发送消息
     *  3.添加意见
     * 2.添加意见
     *
     * </pre>
     * @param json
     * @return
     */
    public JsonResult transfer(JSONObject json){
        JSONArray msgTypes=json.getJSONArray("msgTypes");
        JPaasUser user= (JPaasUser) ContextUtil.getCurrentUser();
        JSONArray users= json.getJSONArray("copyUsers");
        boolean send=json.getBoolean("send");
        String instId=json.getString("instId");
        String opinion=json.getString("opinion");
        String files=json.getString("files");

        String message=send?"转发流程实例成功!":"回复消息成功!";

        BpmInst bpmInst=bpmInstService.get(instId);

        BpmTask bpmTask=getTaskByInstId(bpmInst);

        if(send){
            //添加抄送
            JsonResult result= addCc(bpmInst,users,user,bpmTask);
            if(!result.isSuccess()){
                return result;
            }
            List<String> userList= (List<String>) result.getData();

            String transUser= String.join(",",userList);
            //添加意见
            addCheckHistory(bpmTask, TaskOptionType.TRANS.name(),opinion,files,transUser);
            //发送消息
            sendMessage(user,bpmInst,msgTypes,userList);
        }
        else{
            addCheckHistory(bpmTask,TaskOptionType.TRANS_REPLY.name(),opinion,files,"");
        }
        //返回是否成功
        return JsonResult.Success(message);
    }

    private void sendMessage(JPaasUser user,BpmInst bpmInst,JSONArray msgTypeAry,List<String> userList){
        //没有勾选消息类型
        if(BeanUtil.isEmpty(msgTypeAry) ){
            return;
        }

        if(BeanUtil.isEmpty(userList)){
            return;
        }
        //消息类型
        List<String> msgTypeList=new ArrayList<>();
        for(int i=0;i<msgTypeAry.size();i++){
            msgTypeList.add(msgTypeAry.getString(i));
        }
        String msgTypes= String.join(",",msgTypeList);


        //发送消息通知
        OsUserDto sendUser=new OsUserDto();
        sendUser.setUserId(user.getUserId());
        sendUser.setFullName(user.getFullName());
        sendUser.setTenantId(user.getTenantId());

        String users=String.join(",",userList);

        List<OsUserDto> recievers=orgService.getUsersByIds(users);
        Map vars=new HashMap();
        vars.put("instId",bpmInst.getInstId());

        com.redxun.bpm.core.ext.messagehandler.MessageUtil
                .sendMessage(sendUser,bpmInst.getSubject(),msgTypes,"forward",recievers,vars);
    }

    private void addCheckHistory(BpmTask bpmTask,String checkType,String opinion,String opFiles,String transUsers){
        bpmCheckHistoryService.createHistory(bpmTask,checkType,"",opinion,opFiles,"",transUsers);
    }

    /**
     * 获取活动的任务。
     * @param bpmInst
     * @return
     */
    private BpmTask getTaskByInstId(BpmInst bpmInst){
        String status=bpmInst.getStatus();
        BpmTask bpmTask=getTaskByInst(bpmInst);

        if(BpmInstStatus.SUCCESS_END.name().equals(status) ||
                BpmInstStatus.DRAFTED.name().equals(status) ||
                BpmInstStatus.ERROR_END.name().equals(status) ||
                BpmInstStatus.CANCEL.name().equals(status))
        {
            return bpmTask;
        }
        List<BpmTask> tasks= bpmTaskService.getByInstId(bpmInst.getInstId());
        if(BeanUtil.isEmpty(tasks)){
            return  bpmTask;
        }
        return tasks.get(0);
    }

    /**
     * 返回空的任务。
     * @param bpmInst
     * @return
     */
    private BpmTask getTaskByInst(BpmInst bpmInst){
        BpmTask bpmTask=new BpmTask();

        bpmTask.setInstId(bpmInst.getInstId());
        bpmTask.setSubject(bpmInst.getSubject());
        bpmTask.setActInstId(bpmInst.getActInstId());
        bpmTask.setActDefId(bpmInst.getActDefId());
        bpmTask.setDefId(bpmInst.getDefId());
        bpmTask.setCreateTime(new Date());

        bpmTask.setBillType(bpmInst.getBillType());
        bpmTask.setBillNo(bpmInst.getBillNo());
        bpmTask.setBusKey(bpmInst.getBusKey());

        return bpmTask;
    }

    /**
     * 是否可以发送抄送。
     * @param instId
     * @param users
     * @return
     */
    private JsonResult canAdd(String instId, JSONArray users){
        JsonResult result=JsonResult.Success();

        List<String> userList=new ArrayList<>();

        for(int i=0;i<users.size();i++){
            JSONObject json=users.getJSONObject(i);
            String userId=json.getString("id");
            boolean hasSend=hasSend(userId,instId);
            if(hasSend){
                continue;
            }
            userList.add(userId);
        }
        if(userList.size()==0){
            result.setSuccess(false);
            result.setMessage("选择的人都已转发!");
        }
        else{
            result.setData(userList);
        }
        return result;
    }

    /**
     * 已发送。
     * @param userId
     * @param instId
     * @return
     */
    private boolean hasSend(String userId,String instId){
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("USER_ID_",userId);
        wrapper.eq("INST_ID_",instId);
        Integer count= bpmInstCpService.getRepository().selectCount(wrapper);
        return count>0;
    }


    /**
     * 添加抄送。
     * <pre>
     *     1.添加抄送抄送人。
     * </pre>
     * @param bpmInst
     * @param users
     * @param user
     */
    private JsonResult addCc(BpmInst bpmInst, JSONArray users, JPaasUser user, BpmTask bpmTask){

        JsonResult result= canAdd(bpmInst.getInstId(), users);
        if(!result.isSuccess()){
            return result;
        }

        BpmInstCc instCc=new BpmInstCc();
        instCc.setId(IdGenerator.getIdStr());
        instCc.setSubject(bpmInst.getSubject());
        instCc.setFromUserId(user.getUserId());
        instCc.setCcType(BpmInstCc.CC_TYPE_TURN_TO);
        instCc.setFromUser(user.getFullName());
        instCc.setDefId(bpmInst.getDefId());
        instCc.setInstId(bpmInst.getInstId());
        instCc.setTreeId(bpmInst.getTreeId());

        if(bpmTask!=null){
            instCc.setNodeId(bpmTask.getKey());
            instCc.setNodeName(bpmTask.getName());
        }

        this.insert(instCc);

        List<String> userList=(List<String>) result.getData();

        for(int i=0;i<userList.size();i++){
            String userId=userList.get(i);

            BpmInstCp instCp=new BpmInstCp();
            instCp.setIsRead(MBoolean.NO.name());
            instCp.setUserId(userId);
            instCp.setInstId(bpmInst.getInstId());
            instCp.setCcId(instCc.getId());
            instCp.setId(IdGenerator.getIdStr());

            bpmInstCpService.insert(instCp);
        }
        return result;
    }

    /**
     * 根据InstId获取抄送记录(带人员部门)
     * @param instId
     * @return
     */
    public JSONArray getCcRecordByInstId(String instId) {
        JSONArray jsonArray=new JSONArray();
        List<BpmInstCc> list= bpmInstCcMapper.getByInstId(instId);
        for (BpmInstCc bpmInstCc : list) {
            if(jsonArray.size()==0){
                JSONObject ccRecord = getCcRecord(bpmInstCc);
                jsonArray.add(ccRecord);
            }else {
                //标记是否有相同
                boolean falg=false;
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    if(bpmInstCc.getId().equals(jsonObject.getString("id"))){
                        JSONArray instCps = jsonObject.getJSONArray("instCps");
                        instCps.add(getInstCps(bpmInstCc));
                        falg=true;
                        break;
                    }
                }
                if(!falg){
                    JSONObject ccRecord = getCcRecord(bpmInstCc);
                    jsonArray.add(ccRecord);
                }
            }
        }
        return jsonArray;
    }

    //获取抄送记录
    private JSONObject getCcRecord(BpmInstCc bpmInstCc){
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("id",bpmInstCc.getId());
        //发送人
        jsonObject.put("fromUser",bpmInstCc.getFromUser());
        jsonObject.put("fromUserId",bpmInstCc.getFromUserId());
        //获取发送人主部门名称
        OsUserDto fromUser = orgService.getUserById(bpmInstCc.getFromUserId());
        String deptName = fromUser.getDeptName();
        jsonObject.put("ccDeptName",deptName);
        //发送时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String ccTime = sdf.format(bpmInstCc.getCreateTime());
        jsonObject.put("ccTime",ccTime);
        //发送对象
        JSONArray instCps=new JSONArray();
        instCps.add(getInstCps(bpmInstCc));
        jsonObject.put("instCps",instCps);
        return jsonObject;
    }

    //获取抄送对象
    private JSONObject getInstCps(BpmInstCc bpmInstCc){
        JSONObject instCpObj=new JSONObject();
        BpmInstCp instCp = bpmInstCc.getInstCp();
        instCpObj.put("cpId",instCp.getId());
        instCpObj.put("cpUserId",instCp.getUserId());
        OsUserDto cpUser = orgService.getUserById(instCp.getUserId());
        instCpObj.put("cpUserName",cpUser.getFullName());
        instCpObj.put("cpDeptName",cpUser.getDeptName());
        return instCpObj;
    }


}
