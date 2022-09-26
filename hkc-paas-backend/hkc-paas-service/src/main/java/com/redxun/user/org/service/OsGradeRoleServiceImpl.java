package com.redxun.user.org.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.common.tool.StringUtils;
import com.redxun.user.org.entity.OsGradeRole;
import com.redxun.user.org.mapper.OsGradeAdminMapper;
import com.redxun.user.org.mapper.OsGradeRoleMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* [分级管理员角色]业务服务类
*/
@Service
public class OsGradeRoleServiceImpl extends SuperServiceImpl<OsGradeRoleMapper, OsGradeRole> implements BaseService<OsGradeRole> {

    @Resource
    private OsGradeRoleMapper osGradeRoleMapper;
    @Resource
    private OsGradeAdminMapper osGradeAdminMapper;

    @Override
    public BaseDao<OsGradeRole> getRepository() {
        return osGradeRoleMapper;
    }

    /**
     * 根据分级授权ID获取所有的授权角色列表
     * @param adminId
     * @return
     */
    public List<OsGradeRole> getByAdminId(String adminId) {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("ADMIN_ID_",adminId);
        return osGradeRoleMapper.selectList(queryWrapper);
    }

    /**
     * 根据分级授权ID删除所有的授权角色
     * @param adminId
     */
    public void  delByAdminId(String adminId){
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("ADMIN_ID_",adminId);
        osGradeRoleMapper.delete(queryWrapper);
    }

    /**
     * 根据用户ID和租户ID获取 他授权的角色。
     * @param userId
     * @param tenantId
     * @return
     */
    public List<OsGradeRole> getGroupByUserId(String userId,String tenantId) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId",userId);
        if(StringUtils.isNotEmpty(tenantId)){
            params.put("tenantId",tenantId);
        }
        return osGradeRoleMapper.getGroupByUserId(params);
    }


}
