package com.redxun.user.org.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.constant.MBoolean;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.StringUtils;
import com.redxun.user.org.entity.OsDimension;
import com.redxun.user.org.entity.OsGroup;
import com.redxun.user.org.entity.OsUser;
import com.redxun.user.org.entity.OsWxEntAgent;
import com.redxun.user.org.service.ISyncService;
import com.redxun.user.org.service.OsUserServiceImpl;
import com.redxun.user.org.service.OsWxEntAgentServiceImpl;
import com.redxun.util.wechat.WxEntUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * 微信同步实现。
 */
public class WeixinSyncServiceImpl implements ISyncService {
    @Autowired
    OsWxEntAgentServiceImpl osWxEntAgentService;
    @Autowired
    WxEntUtil wxEntUtil;
    @Autowired
    OsUserServiceImpl osUserService;

    @Override
    public JsonResult getUsers(String tenantId, List<OsGroup> groups) {

        JsonResult tokenResult=getToken(tenantId);

        if(!tokenResult.isSuccess()){
            return tokenResult;
        }
        String token= (String) tokenResult.getData();
        List<OsUser> osUsers = new ArrayList<>();
        for (OsGroup group : groups) {
            String allUserInfo = wxEntUtil.getAllUserInfo(token,group.getGroupId());
            JSONArray jsonArray = JSONArray.parseArray(allUserInfo);
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject userJosn=jsonArray.getJSONObject(i);
                OsUser osUser = handUser(userJosn);
                if (osUser == null) {
                    continue;
                }
                osUsers.add(osUser);
            }
        }

        /*String allUserInfo = wxEntUtil.getAllUserInfo(token);
        JSONArray jsonArray = JSONArray.parseArray(allUserInfo);
        List<OsUser> osUsers = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject userJosn=jsonArray.getJSONObject(i);
            OsUser osUser = handUser(userJosn);
            if (osUser == null) {
                continue;
            }
            osUsers.add(osUser);
        }*/
        JsonResult result=JsonResult.Success();
        result.setData(osUsers);
        return result;
    }

    private JsonResult getToken(String tenantId){

        //根据租户ID获取企业微信的配置信息
        OsWxEntAgent osWxEntAgent = osWxEntAgentService.getDefaultAgent(tenantId);
        if (osWxEntAgent == null) {
            return JsonResult.Fail("没有默认的企业微信应用配置，请先在后台配置!").setShow(false);
        }
        //获取Token
        String token = wxEntUtil.getToken(osWxEntAgent.getCorpId(), osWxEntAgent.getSecret(), tenantId);
        if (StringUtils.isEmpty(token)) {
            return JsonResult.Fail("获取Token失败，请检查网格连接或安全设置!").setShow(false);
        }
        return JsonResult.Success().setData(token)
                .setShow(false);
    }

    private OsUser handUser(JSONObject userJosn) {
        OsUser osUser = new OsUser();
        String  name = userJosn.getString("name");
        osUser.setFullName(name);

        String address = userJosn.getString("address");
        osUser.setAddress(address);

        String gender = userJosn.getString("gender");
        if (gender != null) {
            if (gender.equals("1")) {
                osUser.setSex(OsUser.MALE);
            } else {
                osUser.setSex(OsUser.FMALE);
            }
        }
        String mobile = userJosn.getString("mobile");
        osUser.setMobile(mobile);

        String userno = userJosn.getString("userid");
        osUser.setUserNo(userno);
        osUser.setWxOpenId(userno);

        String email = userJosn.getString("email");
        osUser.setEmail(email);

        String mainDepartment = userJosn.getString("main_department");
        osUser.setMainDepId(mainDepartment);
        osUser.setFrom(OsUser.WEIXIN);
        //该用户所有的部门
        JSONArray departments = userJosn.getJSONArray("department");
        if(BeanUtil.isEmpty(departments)){
            return osUser;
        }

        //除去主部门
        List<String> groups=new ArrayList<>();
        for(int j=0;j<departments.size();j++){
            String groupId=departments.getString(j);
            if(!groupId.equals(osUser.getMainDepId())){
                groups.add(groupId);
            }
        }

        if(groups.size()>0){
            osUser.setCanGroupIds(StringUtils.join(groups));
        }
        return osUser;
    }

    @Override
    public JsonResult getDepartMent(String tenantId) {
        JsonResult tokenResult=getToken(tenantId);
        if(!tokenResult.isSuccess()){
            return tokenResult;
        }
        String token= (String) tokenResult.getData();

        String department = wxEntUtil.getDepartment(token);
        JSONArray jsonArray = JSONArray.parseArray(department);
        List<OsGroup> osGroups = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject departmentJson=jsonArray.getJSONObject(i);
            OsGroup osGroup = handDepartment(departmentJson);
            osGroups.add(osGroup);
        }
        JsonResult result=JsonResult.Success();
        result.setData(osGroups);
        return result;

    }

    private OsGroup handDepartment(JSONObject departmentJson) {
        OsGroup osGroup = new OsGroup();
        String name = departmentJson.getString("name");
        osGroup.setName(name);

        String id = departmentJson.getString("id");
        osGroup.setGroupId(id);

        String parentid = departmentJson.getString("parentid");
        osGroup.setParentId(parentid);

        String order = departmentJson.getString("order");
        osGroup.setSn(Integer.parseInt(order));
        osGroup.setStatus(MBoolean.ENABLED.val);
        osGroup.setDimId(OsDimension.DIM_ADMIN_ID);
        osGroup.setKey(id);
        osGroup.setForm(OsGroup.WEIXIN);
        return osGroup;
    }
}
