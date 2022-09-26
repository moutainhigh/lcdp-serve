package com.redxun.portal.core.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.db.PageHelper;
import com.redxun.common.base.entity.IUser;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.base.search.QueryFilter;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ContextUtil;
import com.redxun.log.model.Audit;
import com.redxun.log.model.LogContext;
import com.redxun.portal.core.entity.InfInbox;
import com.redxun.portal.core.entity.InfInnerMsg;
import com.redxun.portal.core.entity.InfInnerMsgLog;
import com.redxun.portal.core.mapper.InfInboxMapper;
import com.redxun.portal.core.mapper.InfInnerMsgMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* [内部短消息]业务服务类
*/
@Service
public class InfInnerMsgServiceImpl extends SuperServiceImpl<InfInnerMsgMapper, InfInnerMsg> implements BaseService<InfInnerMsg> {

    @Resource
    private InfInnerMsgMapper infInnerMsgMapper;
    @Resource
    private InfInboxMapper infInboxMapper;
    @Resource
    private InfInnerMsgLogServiceImpl infInnerMsgLogServiceImpl;

    @Override
    public BaseDao<InfInnerMsg> getRepository() {
        return infInnerMsgMapper;
    }

    /**
     * 发送个人内部消息
     *
     * @param jsonObject
     * @return
     */
    public JsonResult sendMsg(JSONObject jsonObject) {
        StringBuilder sb=new StringBuilder();

        String recUserId = jsonObject.getString("recUserId");
        String fullName = jsonObject.getString("fullName");
        String groupId = jsonObject.getString("groupId");
        String groupName = jsonObject.getString("groupName");
        String content = jsonObject.getString("content");
        String msgTitle = jsonObject.getString("msgTitle");

        JsonResult jsonResult = new JsonResult();
        //内部消息
        InfInnerMsg infInnerMsg = new InfInnerMsg();
        infInnerMsg.setPkId(IdGenerator.getIdStr());
        infInnerMsg.setMsgTitle(msgTitle);
        infInnerMsg.setContent(content);
        IUser user = ContextUtil.getCurrentUser();
        infInnerMsg.setSenderId(user.getUserId());
        infInnerMsg.setSender(user.getFullName());// 发送人名
        infInnerMsg.setCategory("个人消息");
        infInnerMsg.setDelFlag("no");
        infInnerMsgMapper.insert(infInnerMsg);

        sb.append("发送消息:"+ msgTitle +",给用户:");

        //用户
        if (StringUtils.isNotEmpty(recUserId)) {
            String[] recUserIds = recUserId.split(",");
            String[] fullNames = fullName.split(",");
            for (int i = 0; i < recUserIds.length; i++) {
                //内部短消息收件箱
                InfInbox infInbox = new InfInbox();
                infInbox.setPkId(IdGenerator.getIdStr());
                infInbox.setMsgId(infInnerMsg.getMsgId());
                infInbox.setRecType("USER");
                if (StringUtils.isNotEmpty(recUserIds[i])) {
                    infInbox.setRecUserId(recUserIds[i]);
                    infInbox.setRecUserName(fullNames[i]);
                }
                sb.append(fullNames[i] +"("+recUserIds[i]+"),");
                infInboxMapper.insert(infInbox);
            }
        }

        sb.append(",用户组:");

        if (StringUtils.isNotEmpty(groupId)) {
            String[] groupIds = groupId.split(",");
            String[] groupNames = groupName.split(",");
            //用户组
            for (int j = 0; j < groupIds.length; j++) {
                //内部短消息收件箱
                InfInbox infInbox = new InfInbox();
                infInbox.setPkId(IdGenerator.getIdStr());
                infInbox.setMsgId(infInnerMsg.getMsgId());
                infInbox.setRecType("GROUP");
                infInbox.setRecUserId(groupIds[j]);
                infInbox.setRecUserName(groupNames[j]);
                infInboxMapper.insert(infInbox);

                sb.append(groupNames[j] +"("+groupIds[j]+")");

            }
        }

        LogContext.put(Audit.DETAIL,sb.toString());

        return jsonResult.setSuccess(true).setMessage("发送成功!");
    }

    /**
     * 发送系统内部消息
     *
     * @param jsonObject
     * @return
     */
    public JsonResult sendSystemMsg(JSONObject jsonObject) {

        String recUserId = jsonObject.getString("recUserId");
        if(StringUtils.isEmpty(recUserId)){
            return  JsonResult.Fail("接收人为空");
        }

        StringBuilder sb=new StringBuilder();

        sb.append("系统发送消息:");


        String fullName = jsonObject.getString("fullName");
        String msgTitle = jsonObject.getString("msgTitle");
        String content=jsonObject.getString("content");
        String tenantId=jsonObject.getString("tenantId");

        JsonResult jsonResult = new JsonResult();
        //内部消息
        InfInnerMsg infInnerMsg = new InfInnerMsg();
        infInnerMsg.setPkId(IdGenerator.getIdStr());
        infInnerMsg.setMsgTitle(msgTitle);
        infInnerMsg.setContent(content);
        infInnerMsg.setCreateBy("0");
        infInnerMsg.setSenderId("0");
        infInnerMsg.setSender("系统");
        infInnerMsg.setCategory("系统消息");
        infInnerMsg.setDelFlag("no");
        infInnerMsg.setTenantId(tenantId);

        infInnerMsgMapper.insert(infInnerMsg);

        sb.append(",消息标题:" + msgTitle +",收件人:");

        //内部短消息收件箱
        InfInbox infInbox = new InfInbox();
        infInbox.setPkId(IdGenerator.getIdStr());
        infInbox.setMsgId(infInnerMsg.getMsgId());
        infInbox.setRecType("USER");

        infInbox.setRecUserId(recUserId);
        infInbox.setTenantId(tenantId);
        infInbox.setRecUserName(fullName);
        sb.append(fullName +"("+recUserId+")");

        infInbox.setCreateBy("0");
        infInboxMapper.insert(infInbox);

        return jsonResult.setSuccess(true).setMessage("发送成功!");
    }

    /**
     * 分页查询收到的消息
     *
     * @param queryFilter
     * @return
     */
    public IPage queryMsg(QueryFilter queryFilter, String recUserId, String recType) {
        Map<String, Object> params = PageHelper.constructParams(queryFilter);
        String curUserId = ContextUtil.getCurrentUserId();
        IPage iPage = infInnerMsgMapper.queryMsg(queryFilter.getPage(), params,recType,recUserId,curUserId);
        return iPage;
    }

    /**
     * 查看消息
     *
     * @param msgId
     */
    public void readMessage(String msgId) {
        if (StringUtils.isNotEmpty(msgId)) {
            InfInnerMsgLog log = infInnerMsgLogServiceImpl.getByMsgIdAndRecUserId(msgId, ContextUtil.getCurrentUserId());
            if (BeanUtil.isNotEmpty(log)) {
                log.setIsRead("yes");
                infInnerMsgLogServiceImpl.updateById(log);
            } else {
                InfInnerMsgLog infInnerMsgLog = new InfInnerMsgLog();
                infInnerMsgLog.setMsgId(msgId);
                infInnerMsgLog.setRecUserId(ContextUtil.getCurrentUserId());
                infInnerMsgLog.setIsRead("yes");
                infInnerMsgLogServiceImpl.insert(infInnerMsgLog);
            }
        }
    }

    /**
     * 查询发送的消息
     *
     * @param queryFilter
     * @param curUserId
     * @return
     */
    public IPage querySentMsg(QueryFilter queryFilter, String curUserId) {
        queryFilter.addParam("senderId", curUserId);
        Map<String, Object> params = PageHelper.constructParams(queryFilter);
        IPage iPage = infInnerMsgMapper.querySentMsg(queryFilter.getPage(), params);
        return iPage;
    }

    /**
     * 更新发送的消息的删除标记
     *
     * @param msgId
     */

    public void updateDelFlag(String msgId) {
        infInnerMsgMapper.updateDelFlag(msgId);
    }

    /**
     * 获取消息
     * @param recUserId
     * @param groupIds
     * @param tenantId
     * @return
     */
    public Integer getCountByRecUserId(String recUserId,List<String> groupIds,String tenantId){
        return infInnerMsgMapper.getCountByRecUserId(recUserId,groupIds,tenantId);
    }

    /**
     * 根据接收人Id及接收人组查询我的消息
     * @param page
     * @param recUserId
     * @param groupIds
     * @return
     */
    public IPage getByRecUserId(IPage page,String recUserId,List<String> groupIds){
        return infInnerMsgMapper.getByRecUserId(page,recUserId,groupIds);
    }

    public List<InfInnerMsg> getSendObj(List<InfInnerMsg> infInnerMsgs,QueryFilter queryFilter) {
        Map<String, Object> params = queryFilter.getParams();
        for(int i=0;i<infInnerMsgs.size();i++){
            //收信人
            List<Map<String,String>> userList=new ArrayList<>();
            //收信组
            List<Map<String,String>> groupList=new ArrayList<>();
            List<InfInbox> infInboxes= infInboxMapper.getByMsgId(infInnerMsgs.get(i).getMsgId());
            if(infInboxes.size()==0){
                return new ArrayList<>();
            }
            for (InfInbox infInbox : infInboxes) {
                Map<String,String> map=new HashMap<>();
                map.put("id",infInbox.getRecUserId());
                map.put("name",infInbox.getRecUserName());
                if("USER".equals(infInbox.getRecType())){
                    userList.add(map);
                }else {
                    groupList.add(map);
                }
            }
            infInnerMsgs.get(i).setUserList(userList);
            infInnerMsgs.get(i).setGroupList(groupList);

        }
        return infInnerMsgs;
    }
}
