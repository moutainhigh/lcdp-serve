
package com.redxun.user.org.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.DateUtils;
import com.redxun.common.tool.IdGenerator;
import com.redxun.user.org.entity.OsPasswordInputError;
import com.redxun.user.org.entity.OsPasswordPolicy;
import com.redxun.user.org.mapper.OsPasswordInputErrorMapper;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
* [用户密码输入错误记录表]业务服务类
*/
@Service
public class OsPasswordInputErrorServiceImpl extends SuperServiceImpl<OsPasswordInputErrorMapper, OsPasswordInputError> implements BaseService<OsPasswordInputError> {

    @Resource
    private OsPasswordInputErrorMapper osPasswordInputErrorMapper;

    @Resource
    private OsPasswordPolicyServiceImpl osPasswordPolicyService;

    @Resource
    private OsUserServiceImpl osUserService;

    @Override
    public BaseDao<OsPasswordInputError> getRepository() {
        return osPasswordInputErrorMapper;
    }

    /**
     * 账号是否被锁住
     * @param username
     * @param tenantId
     * @return  true：账号被锁定，false：账号没有锁定
     */
    public boolean isLocked(String username, String tenantId){
        //没启用的情况下
        if(!isAction(tenantId)){
            return false;
        }

        //无输错记录
        OsPasswordInputError entity = getByUserNoAndTenantId(username, tenantId);
        if(BeanUtil.isEmpty(entity)){
            return false;
        }

        //输错次数达到配置的最大次数
        OsPasswordPolicy configure =  osPasswordPolicyService.getConfig(tenantId).getData();
        if(entity.getErrorTime().intValue() >= configure.getErrorTime().intValue()){
            osUserService.lockAccount(username, tenantId);
            return true;
        }

        return false;
    }

    /**
     * 根据用户编号获取密码错误输入次数记录
     * 如果账号锁定天数没有配置或者配置的值小于1，则账号的锁定策略为永久锁定
     * 否则则按照配置的值进行锁定
     * 只获取该用户当前的错误记录，也就是说，当天的错误数累加，隔天清零
     * @param userNO
     * @param tenantId
     * @return
     */
    private OsPasswordInputError getByUserNoAndTenantId(String userNO, String tenantId){
        QueryWrapper queryWrapper = new QueryWrapper();

        OsPasswordPolicy configure =  osPasswordPolicyService.getConfig(tenantId).getData();
        if(BeanUtil.isNotEmpty(configure.getAccountLockDay()) && configure.getAccountLockDay() > 0){
            Date now = new Date();
            Date preDay = DateUtils.addDays(now, -configure.getAccountLockDay().intValue());
            queryWrapper.between("CREATE_TIME_", preDay, now);
        }


        queryWrapper.eq("USER_NO_", userNO);
        queryWrapper.eq("TENANT_ID_", tenantId);
        List<OsPasswordInputError> list = osPasswordInputErrorMapper.selectList(queryWrapper);
        return BeanUtil.isEmpty(list) ? null : list.get(0);
    }

    /**
     * 密码错误输入次数递增
     * @param username
     * @param tenantId
     */
    public void saveInputErrorIncrement(String username, String tenantId) {
        //没启用的情况下，不做保存
        if(!isAction(tenantId)){
            return;
        }

        OsPasswordInputError entity = getByUserNoAndTenantId(username, tenantId);
        if(BeanUtil.isEmpty(entity)){
            entity = new OsPasswordInputError();
            entity.setId(IdGenerator.getIdStr());
            entity.setUserNo(username);
            entity.setErrorTime(0);
        }

        entity.setErrorTime(entity.getErrorTime().intValue() + 1);
        this.saveOrUpdate(entity);

    }

    /**
     * 是否启用，1、没有密码安全策略管理配置；2、多错误输入密码错误次数没有配置
     * @param tenantId
     * @return true：是，false：否
     */
    private boolean isAction(String tenantId){
        OsPasswordPolicy configure =  osPasswordPolicyService.getConfig(tenantId).getData();
        //没有密码安全策略管理配置的情况
        if(BeanUtil.isEmpty(configure)){
            return false;
        }

        //最多错误输入密码错误次数没有配置
        if(BeanUtil.isEmpty(configure.getErrorTime())){
            return false;
        }

        return true;
    }

    /**
     * 根据账号以及租户id清除密码输入次数记录
     * @param username
     * @param tenantId
     */
    public void removeRecordByUsernameAndTenantId(String username, String tenantId){
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("USER_NO_", username);
        queryWrapper.eq("TENANT_ID_", tenantId);
        this.remove(queryWrapper);
    }

}
