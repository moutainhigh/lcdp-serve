package com.redxun.user.org.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.db.PageHelper;
import com.redxun.common.base.search.QueryFilter;
import com.redxun.common.constant.MBoolean;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ContextUtil;
import com.redxun.user.org.entity.*;
import com.redxun.user.org.mapper.OsInstUsersMapper;
import com.redxun.user.org.mapper.OsRelInstMapper;
import com.redxun.user.org.mapper.OsRelTypeMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
* [租户关联用户表]业务服务类
*/
@Service
public class OsInstUsersServiceImpl extends SuperServiceImpl<OsInstUsersMapper, OsInstUsers> implements BaseService<OsInstUsers> {

    @Resource
    private OsInstUsersMapper osInstUsersMapper;
    @Resource
    private OsRelInstMapper osRelInstMapper;
    @Resource
    private OsRelTypeMapper osRelTypeMapper;

    @Override
    public BaseDao<OsInstUsers> getRepository() {
        return osInstUsersMapper;
    }


    /**
     * 初始化用户。
     * @param user
     */
    public void initUser(OsUser user){
        OsInstUsers instUsers=new OsInstUsers();
        instUsers.setId(IdGenerator.getIdStr());
        instUsers.setUserId(user.getUserId());
        instUsers.setTenantId(user.getTenantId());
        instUsers.setStatus(OsUser.STATUS_IN_JOB);
        instUsers.setIsAdmin(1);
        instUsers.setCreateType("CREATE");
        instUsers.setApplyStatus("ENABLED");
        osInstUsersMapper.insert(instUsers);
    }

    /**
     * 根据用户和租户ID获取关联对象。
     * @param userId
     * @param tenantId
     * @return
     */
    public OsInstUsers getByUserTenant(String userId,String tenantId){
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("USER_ID_",userId);
        wrapper.eq("TENANT_ID_",tenantId);

        OsInstUsers osInstUsers=osInstUsersMapper.selectOne(wrapper);
        return osInstUsers;
    }

    /**
     * 删除关联关系。
     * @param userId
     * @param tenantId
     */
    public void removeByUserId(String userId,String tenantId){
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("USER_ID_",userId);
        wrapper.eq("TENANT_ID_",tenantId);
        osInstUsersMapper.delete(wrapper);
    }

    /**
     * 设置管理员
     * @param userId    用户ID
     * @param tenantId  租户ID
     * @param admin     是否管理员
     */
    public void setAdmin(String userId,String tenantId,Integer admin){
        OsInstUsers osInstUsers=new OsInstUsers();
        String[] aryUserId=userId.split(",");

        UpdateWrapper wrapper=new UpdateWrapper();
        wrapper.in("USER_ID_",aryUserId);
        wrapper.eq("TENANT_ID_",tenantId);
        wrapper.set("IS_ADMIN_",admin);
        osInstUsersMapper.update(osInstUsers,wrapper);
    }

    public IPage getByDomain(QueryFilter filter) {
        Map<String,Object> params= PageHelper.constructParams(filter);
        params.put("tenantId", ContextUtil.getCurrentTenantId());
        return osInstUsersMapper.getByDomain(filter.getPage(),params);
    }

    /**
     * 审批机构申请
     * @param osInstUsers
     */
    public void agreeOrRefuse(OsInstUsers osInstUsers,String tenantId) {
        OsInstUsers temp=get(osInstUsers.getId());
        //添加用户主部门关系
        if(StringUtils.isNotEmpty(osInstUsers.getMainDepId())) {
            String relTypeId= OsRelType.REL_CAT_GROUP_USER_BELONG_ID;
            OsRelType osRelType=osRelTypeMapper.selectById(relTypeId);
            OsRelInst inst = new OsRelInst();
            inst.setParty1(osInstUsers.getMainDepId());
            inst.setParty2(temp.getUserId());
            inst.setPath("0." + inst.getParty1() + "." + inst.getParty2() + ".");
            inst.setRelTypeKey(osRelType.getKey());
            inst.setRelType(osRelType.getRelType());
            inst.setRelTypeId(osRelType.getId());
            inst.setDim1(OsDimension.DIM_ADMIN_ID);
            inst.setStatus(MBoolean.ENABLED.toString());
            inst.setPkId(IdGenerator.getIdStr());
            inst.setIsMain(MBoolean.YES.name());
            inst.setTenantId(tenantId);
            osRelInstMapper.insert(inst);
        }
        osInstUsersMapper.updateById(osInstUsers);
    }
}
