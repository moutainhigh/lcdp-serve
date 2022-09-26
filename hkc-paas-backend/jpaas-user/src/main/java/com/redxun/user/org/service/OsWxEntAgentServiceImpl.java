package com.redxun.user.org.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.common.tool.BeanUtil;
import com.redxun.user.org.entity.OsWxEntAgent;
import com.redxun.user.org.mapper.OsWxEntAgentMapper;
import com.redxun.util.wechat.WeixinUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* [企业微信应用表]业务服务类
*/
@Service
public class OsWxEntAgentServiceImpl extends SuperServiceImpl<OsWxEntAgentMapper, OsWxEntAgent> implements BaseService<OsWxEntAgent> {

    @Resource
    private OsWxEntAgentMapper osWxEntAgentMapper;

    @Override
    public BaseDao<OsWxEntAgent> getRepository() {
        return osWxEntAgentMapper;
    }

    public void updateNotDefault(String tenantId){

        UpdateWrapper updateWrapper=new UpdateWrapper();
        updateWrapper.eq("TENANT_ID_",tenantId);
        updateWrapper.set("DEFAULT_AGENT_",0);

        osWxEntAgentMapper.update(null,updateWrapper);


    }

    /**
     * 获取租户默认的应用。
     * @param tenantId
     * @return
     */
    public OsWxEntAgent getDefaultAgent(String tenantId){
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("TENANT_ID_",tenantId);
        queryWrapper.eq("DEFAULT_AGENT_",1);
        List<OsWxEntAgent> defaultList= osWxEntAgentMapper.selectList(queryWrapper);
        if(BeanUtil.isNotEmpty(defaultList)){
            return defaultList.get(0);
        }
        return null;
    }

    /**
     * 取得更新微信应用信息的方法。
     * @param agent
     * @return
     * @throws Exception
     */
    public JsonResult<JSONObject> getAppInfo(OsWxEntAgent agent) throws Exception{
        JsonResult<Void> result= WeixinUtil.getAppInfoById(agent.getCorpId(), agent.getSecret());
        JsonResult<JSONObject> rtn=new JsonResult<JSONObject>(true);
        if(result.isSuccess()){
            String json=result.getMessage();
            JSONObject jsonObj=JSONObject.parseObject(json);
            JSONObject newJson=new JSONObject();
            newJson.put("agentid", agent.getAgentId());
            newJson.put("report_location_flag",jsonObj.getInteger("report_location_flag"));

            newJson.put("name", agent.getName());
            newJson.put("description", agent.getDescription());
            newJson.put("redirect_domain", agent.getDomain());
            newJson.put("isreportenter", jsonObj.getInteger("isreportenter"));
            newJson.put("home_url", agent.getHomeUrl());

            rtn.setData(newJson);
        }
        else{
            rtn.setSuccess(false);
            rtn.setMessage(result.getMessage());
        }
        return rtn;
    }
}
