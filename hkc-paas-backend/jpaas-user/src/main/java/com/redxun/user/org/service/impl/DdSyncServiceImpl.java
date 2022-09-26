package com.redxun.user.org.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.constant.MBoolean;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.StringUtils;
import com.redxun.user.org.entity.OsDdAgent;
import com.redxun.user.org.entity.OsDimension;
import com.redxun.user.org.entity.OsGroup;
import com.redxun.user.org.entity.OsUser;
import com.redxun.user.org.service.ISyncService;
import com.redxun.user.org.service.OsDdAgentServiceImpl;
import com.redxun.util.dd.DingDingUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * 基于钉钉的组织用户同步服务
 */
public class DdSyncServiceImpl implements ISyncService {
    @Autowired
    OsDdAgentServiceImpl osDdAgentService;
    @Autowired
    DingDingUtil ddUtil;

    /**
     * 获取所有用户
     * @param tenantId
     * @param groups
     * @return
     */
    @Override
    public JsonResult getUsers(String tenantId, List<OsGroup> groups) {
        JsonResult tokenResult = getToken(tenantId);
        if (!tokenResult.isSuccess()) {
            return tokenResult;
        }
        String token = (String) tokenResult.getData();
        //遍历部门下的用户并去重
        Set<OsUser> osUsers = new HashSet<>();
        for (OsGroup o : groups) {
            String userInfo = ddUtil.getUserInfo(token, o.getGroupId());
            JSONArray jsonArray = JSONArray.parseArray(userInfo);
            if (jsonArray == null) {
                continue;
            }
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject userJosn = jsonArray.getJSONObject(i);
                OsUser osUser = handUser(userJosn);
                if (osUser == null) {
                    continue;
                }
                osUsers.add(osUser);
            }
        }
        List<OsUser> users = new ArrayList<>(osUsers);
        JsonResult rtn = JsonResult.Success();
        rtn.setData(users);
        return rtn;
    }

    /**
     * 添加用户
     * @param userJosn
     * @return
     */
    private OsUser handUser(JSONObject userJosn) {
        OsUser osUser = new OsUser();
        String name = userJosn.getString("name");
        osUser.setFullName(name);

        String address = userJosn.getString("work_place");
        osUser.setAddress(address);

        String mobile = userJosn.getString("mobile");
        osUser.setUserNo(mobile);
        osUser.setMobile(mobile);

        String userid = userJosn.getString("userid");
        osUser.setUserId(userid);

        String email = userJosn.getString("email");
        osUser.setEmail(email);

        osUser.setFrom(OsUser.DD);
        //该用户所有的部门
        JSONArray departments = userJosn.getJSONArray("dept_id_list");
        if (BeanUtil.isEmpty(departments)) {
            return osUser;
        }

        //默认第一个为主部门
        osUser.setMainDepId(departments.get(0).toString());
        if (departments.size() <= 1) {
            return osUser;
        }
        List<String> groups = new ArrayList<>();
        for (int j = 1; j < departments.size(); j++) {
            String groupId = departments.getString(j);
            groups.add(groupId);
        }
        if (groups.size() > 0) {
            osUser.setCanGroupIds(StringUtils.join(groups));
        }
        return osUser;

    }

    /**
     * 获取Token
     * @param tenantId
     * @return
     */
    private JsonResult getToken(String tenantId) {
        OsDdAgent osDdAgent = osDdAgentService.getDefault(tenantId);
        if (osDdAgent == null) {
            return JsonResult.Fail("没有默认的钉钉应用配置，请先在后台配置!").setShow(false);
        }
        //获取Token
        String token = ddUtil.getToken(osDdAgent.getAppKey(), osDdAgent.getSecret(), tenantId);
        if (StringUtils.isEmpty(token)) {
            return JsonResult.Fail("获取Token失败，请检查网格连接或安全设置!").setShow(false);
        }
        return JsonResult.Success()
                .setData(token)
                .setShow(false);
    }

    /**
     * 获取部门数据
     * @param tenantId
     * @return
     */
    @Override
    public JsonResult getDepartMent(String tenantId) {

        JsonResult tokenResult = getToken(tenantId);
        if (!tokenResult.isSuccess()) {
            return tokenResult;
        }
        String token = (String) tokenResult.getData();
        String department = ddUtil.getDepartment(token);
        if (department == null) {
            return tokenResult;
        }
        JSONArray jsonArray = JSONArray.parseArray(department);
        List<OsGroup> osGroups = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject departmentJson = jsonArray.getJSONObject(i);
            OsGroup osGroup = handDepartment(departmentJson);
            osGroups.add(osGroup);
        }
        JsonResult result = JsonResult.Success();
        result.setData(osGroups);
        return result;
    }

    /**
     * 处理部门
     * @param departmentJson
     * @return
     */
    private OsGroup handDepartment(JSONObject departmentJson) {
        OsGroup osGroup = new OsGroup();
        String name = departmentJson.getString("name");
        osGroup.setName(name);

        String id = departmentJson.getString("id");
        osGroup.setGroupId(id);

        String parentid = departmentJson.getString("parentid");
        if (parentid == null) {
            osGroup.setParentId("0");
        } else {
            osGroup.setParentId(parentid);
        }

        String order = departmentJson.getString("id");
        osGroup.setSn(Integer.parseInt(order));
        osGroup.setStatus(MBoolean.ENABLED.val);
        osGroup.setDimId(OsDimension.DIM_ADMIN_ID);
        osGroup.setKey(osGroup.getGroupId());
        osGroup.setForm(OsGroup.DD);
        return osGroup;
    }
}
