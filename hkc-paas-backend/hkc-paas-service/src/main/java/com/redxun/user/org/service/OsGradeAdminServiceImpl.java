package com.redxun.user.org.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.tool.constant.CommonConstant;
import com.redxun.user.org.entity.OsGradeAdmin;
import com.redxun.user.org.entity.OsGradeRole;
import com.redxun.user.org.mapper.OsGradeAdminMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
* [分级管理员]业务服务类
*/
@Service
public class OsGradeAdminServiceImpl extends SuperServiceImpl<OsGradeAdminMapper, OsGradeAdmin> implements BaseService<OsGradeAdmin> {

    @Resource
    private OsGradeAdminMapper osGradeAdminMapper;
    @Resource
    private OsGradeRoleServiceImpl osGradeRoleService;

    @Override
    public BaseDao<OsGradeAdmin> getRepository() {
        return osGradeAdminMapper;
    }

    /**
     * 获取某个租户某个用户下的分级管理员
     * @param tenantId
     * @param userId
     * @return
     */
    public List<OsGradeAdmin> getAdminByUserId(String tenantId,String userId) {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("USER_ID_",userId);
        queryWrapper.eq(CommonConstant.TENANT_ID,tenantId);
        return osGradeAdminMapper.selectList(queryWrapper);
    }

    /**
     * 通过用户组获取授权分级管理员的ID
     * @param groupId
     * @return
     */
    public Integer getCountByGroupId(String groupId) {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("GROUP_ID_",groupId);
        return osGradeAdminMapper.selectCount(queryWrapper);
    }

    /**
     * 判断用户是否存在
     * @param admin
     * @return
     */
    public boolean isAdminExist(OsGradeAdmin admin) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("GROUP_ID_",admin.getGroupId());
        queryWrapper.eq("USER_ID_",admin.getUserId());
        Integer rtn=osGradeAdminMapper.selectCount(queryWrapper);
        return rtn>0;
    }

    /**
     * 保存所有
     * @param list
     * @return
     */
    public List<String> saveAll(List<OsGradeAdmin> list) {
        List<String> idList = new ArrayList<>();
        for (OsGradeAdmin osGradeAdmin : list) {
            if(isAdminExist(osGradeAdmin)){
                continue;
            }
            String id = IdGenerator.getIdStr();
            osGradeAdmin.setId(id);
            osGradeAdmin.setPath("0."+id+".");
            insert(osGradeAdmin);
            idList.add(id);
        }
        return idList;
    }

    /**
     * 保存所有的角色
     * @param list
     */
    public void saveAllRole(List<OsGradeRole> list) {
        for (OsGradeRole osGradeRole : list) {
            osGradeRoleService.insert(osGradeRole);
        }
    }

    /**
     * 通过分级授权ID获取授权的所有角色
     * @param adminId
     * @return
     */
    public List<OsGradeRole> getRoleByAdminId(String adminId) {
        return osGradeRoleService.getByAdminId(adminId);
    }


}
