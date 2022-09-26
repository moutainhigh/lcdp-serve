package com.redxun.form.core.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.IUser;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ContextUtil;
import com.redxun.feign.OsGroupClient;
import com.redxun.form.core.entity.FormDefPermission;
import com.redxun.form.core.mapper.FormDefPermissionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * [业务模型表单权限表]业务服务类
 *
 * @author hujun
 */
@Service
public class FormDefPermissionServiceImpl extends SuperServiceImpl<FormDefPermissionMapper, FormDefPermission> implements BaseService<FormDefPermission> {

    @Resource
    private FormDefPermissionMapper formDefPermissionMapper;
    @Resource
    private FormPermissionServiceImpl formPermissionServiceImpl;
    @Autowired
    OsGroupClient osGroupClient;

    @Override
    public BaseDao<FormDefPermission> getRepository() {
        return formDefPermissionMapper;
    }

    public boolean isExist(FormDefPermission formDefPermission) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("FORM_ID_", formDefPermission.getFormId());
        wrapper.eq("BO_DEF_ID_", formDefPermission.getBoDefId());
        FormDefPermission obj = formDefPermissionMapper.selectOne(wrapper);
        if (BeanUtil.isNotEmpty(obj)) {
            formDefPermission.setId(obj.getId());
            return true;
        }
        return false;
    }

    /**
     * 根据数据源
     * @param boAlias
     * @return
     */
    public String getFormIdByBoAlias(String boAlias){
        List<FormDefPermission> list = getByBoAlias(boAlias);
        IUser user = ContextUtil.getCurrentUser();
        Map<String, Set<String>> profiles = osGroupClient.getCurrentProfile(user.getUserId());
        for (FormDefPermission formDefPermission : list) {
            String permission = formDefPermission.getPermission();
            if (StringUtils.isNotEmpty(permission)) {
                JSONObject json = JSONObject.parseObject(permission);
                boolean flag = formPermissionServiceImpl.hasRights(json.getString("key"), profiles);
                if (flag) {
                    return formDefPermission.getFormId();
                }
            }
        }
        return "";
    }

    public FormDefPermission getByPermission(String boDefId) {
        List<FormDefPermission> list = getByBoDefIdList(boDefId);
        IUser user = ContextUtil.getCurrentUser();
        Map<String, Set<String>> profiles = osGroupClient.getCurrentProfile(user.getUserId());
        for (FormDefPermission formDefPermission : list) {
            String permission = formDefPermission.getPermission();
            if (StringUtils.isNotEmpty(permission)) {
                JSONObject json = JSONObject.parseObject(permission);
                boolean flag = formPermissionServiceImpl.hasRights(json.getString("key"), profiles);
                if (flag) {
                    return formDefPermission;
                }
            }
        }
        return null;
    }

    public Map<String, FormDefPermission> getByBoDefIdMap(String boDefId) {
        List<FormDefPermission> list = getByBoDefIdList(boDefId);
        Map<String, FormDefPermission> map = new HashMap<>(list.size());
        for (FormDefPermission formDefPermission : list) {
            map.put(formDefPermission.getFormId(), formDefPermission);
        }
        return map;
    }

    public List<FormDefPermission> getByBoDefIdList(String boDefId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("BO_DEF_ID_", boDefId);
        queryWrapper.orderByDesc("LEVEL_");
        return formDefPermissionMapper.selectList(queryWrapper);
    }

    public List<FormDefPermission> getByBoAlias(String boAlias) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("BO_ALIAS_", boAlias);
        queryWrapper.orderByDesc("LEVEL_");
        return formDefPermissionMapper.selectList(queryWrapper);
    }
}
