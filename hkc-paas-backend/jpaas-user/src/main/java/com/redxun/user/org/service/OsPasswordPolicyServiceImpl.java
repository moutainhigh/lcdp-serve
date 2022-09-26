
package com.redxun.user.org.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.cache.CacheUtil;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.DateUtils;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ContextUtil;
import com.redxun.dto.user.OsUserDto;
import com.redxun.log.model.Audit;
import com.redxun.log.model.LogContext;
import com.redxun.mq.MessageModel;
import com.redxun.msgsend.util.MesAutoUtil;
import com.redxun.user.org.entity.OsPasswordPolicy;
import com.redxun.user.org.entity.OsUser;
import com.redxun.user.org.mapper.OsPasswordPolicyMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;

/**
* [密码安全策略管理配置]业务服务类
*/
@Service
public class OsPasswordPolicyServiceImpl extends SuperServiceImpl<OsPasswordPolicyMapper, OsPasswordPolicy> implements BaseService<OsPasswordPolicy> {

    /**
     * getConfig缓存区域
     */
    private static final String REGION_GET_CONFIG = "getConfig";

    private static final String YES = "YES";

    private static String PW_PATTERN = "^(?![A-Za-z0-9]+$)(?![a-z0-9\\W]+$)(?![A-Za-z\\W]+$)(?![A-Z0-9\\W]+$)[a-zA-Z0-9\\W]{1,}$";

    @Resource
    private OsPasswordPolicyMapper osPasswordPolicyMapper;

    @Resource
    private OsUserServiceImpl osUserService;

    @Override
    public BaseDao<OsPasswordPolicy> getRepository() {
        return osPasswordPolicyMapper;
    }

    public JsonResult<OsPasswordPolicy> getConfig(String tenantId) {
        JsonResult result =  JsonResult.getSuccessResult("");

        //先从缓存取
        OsPasswordPolicy entity =(OsPasswordPolicy) CacheUtil.get(REGION_GET_CONFIG, tenantId);
        if(BeanUtil.isEmpty(entity)){
            entity = getByTenantId(tenantId);
            CacheUtil.set(REGION_GET_CONFIG,tenantId, entity);
        }

        result.setData(entity);
        return result;
    }

    public JsonResult saveOrUpdateConfig(OsPasswordPolicy passwordPolicyConfigure) {
        OsPasswordPolicy old = getByTenantId(ContextUtil.getCurrentTenantId());
        if(BeanUtil.isNotEmpty(old)){
            old.setMinLength(passwordPolicyConfigure.getMinLength());
            old.setErrorTime(passwordPolicyConfigure.getErrorTime());
            old.setAccountLockDay(passwordPolicyConfigure.getAccountLockDay());
            old.setIsMix(passwordPolicyConfigure.getIsMix());
            old.setIsNeverTimeout(passwordPolicyConfigure.getIsNeverTimeout());
            old.setIsFirstLoginUpdate(passwordPolicyConfigure.getIsFirstLoginUpdate());
            old.setIsUsernamePwdConsistent(passwordPolicyConfigure.getIsUsernamePwdConsistent());
            old.setTimeoutDay(passwordPolicyConfigure.getTimeoutDay());
            old.setInformDay(passwordPolicyConfigure.getInformDay());
            old.setInformType(passwordPolicyConfigure.getInformType());
            old.setInformFrequency(passwordPolicyConfigure.getInformFrequency());
            old.setInformContent(passwordPolicyConfigure.getInformContent());
            passwordPolicyConfigure = old;
        }else {
            passwordPolicyConfigure.setId(IdGenerator.getIdStr());
        }

        //清缓存
        CacheUtil.clear(REGION_GET_CONFIG);

        this.saveOrUpdate(passwordPolicyConfigure);
        JsonResult result =  JsonResult.getSuccessResult("保存密码安全策略管理配置成功");
        return result;
    }

    private OsPasswordPolicy getByTenantId(String tenantId){
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("TENANT_ID_", tenantId);
        List<OsPasswordPolicy> list = osPasswordPolicyMapper.selectList(queryWrapper);
        return BeanUtil.isEmpty(list) ? null : list.get(0);
    }

    /**
     * 密码安全策略检查
     * @param userNo
     * @param password
     * @return
     */
    public JsonResult checkPassword(String userNo, String password) {
        JsonResult result =  JsonResult.getSuccessResult("");
        if(StringUtils.isEmpty(password)){
            return result;
        }

        OsPasswordPolicy configure = getConfig(ContextUtil.getCurrentTenantId()).getData();
        //没有密码安全策略管理配置的情况
        if(BeanUtil.isEmpty(configure)){
            return result;
        }

        if(BeanUtil.isNotEmpty(configure.getMinLength()) && password.length() < configure.getMinLength()){
            return JsonResult.getFailResult("最小密码长度不能小于" + configure.getMinLength());
        }

        if(BeanUtil.isNotEmpty(configure.getIsMix()) &&
                YES.equals(configure.getIsMix()) && !password.matches(PW_PATTERN)){
            return JsonResult.getFailResult("密码必须同时由英文大小写特殊字符和数字构成");
        }

        if(BeanUtil.isNotEmpty(configure.getIsUsernamePwdConsistent()) &&
                YES.equals(configure.getIsUsernamePwdConsistent()) && userNo.equals(password)){
            return JsonResult.getFailResult("用户名密码不能一致");
        }

        return result;
    }


    /**
     * 密码过期导致账号被锁定
     * @param username
     * @return  true：账号被锁定，false：账号没有锁定
     */
    public Boolean isLockedByPasswordExpire(String username,  String tenantId) {
        OsPasswordPolicy configure = getConfig(tenantId).getData();
        //密码安全策略没有配置的情况
        if(BeanUtil.isEmpty(configure)){
            return false;
        }
        //密码永不过期
        if(BeanUtil.isNotEmpty(configure.getIsNeverTimeout()) && YES.equals(configure.getIsNeverTimeout())){
            return false;
        }
        //密码过期时间（天）没有设置的情况
        if(BeanUtil.isEmpty(configure.getTimeoutDay())){
            return false;
        }

        OsUser osUser = osUserService.getByUsernameAndTenantId(username, tenantId);
        if(BeanUtil.isEmpty(osUser)){
            return false;
        }

        Date pwdUpdateTime = BeanUtil.isNotEmpty(osUser.getPwdUpdateTime()) ?
                osUser.getPwdUpdateTime() : osUser.getCreateTime();
        //密码修改的时间以及用户创建的时间都为空，则被视为密码已过期
        if(BeanUtil.isEmpty(pwdUpdateTime)){
            return true;
        }
        Date expireDate = DateUtils.addDays(pwdUpdateTime, configure.getTimeoutDay().intValue());
        //密码已过期
        if(expireDate.before(new Date())){
            osUserService.lockAccount(username,tenantId);
            return true;
        }

        return false;
    }


    /**
     * 获取用户是否首次登录
     * @param isFirstLogin
     * @param tenantId
     * @return
     */
    public String getIsFirstLogin(String isFirstLogin, String tenantId) {
        String YES = "YES";
        String NO = "NO";

        //用户非首次登录，直接返回
        if(StringUtils.isNotEmpty(isFirstLogin) && isFirstLogin.equals(NO)){
            return NO;
        }

        OsPasswordPolicy configure = getConfig(tenantId).getData();
        //密码安全策略没有配置的情况
        if(BeanUtil.isEmpty(configure)){
            return NO;
        }

        //用户首次登录必须修改密码没有设置的情况
        if(BeanUtil.isEmpty(configure.getIsFirstLoginUpdate())){
            return NO;
        }

        //用户首次登录必须修改密码
        if(configure.getIsFirstLoginUpdate().equals(YES)){
            return YES;
        }

        return NO;

    }

    /**
     * 通知用户修改密码
     * @param osUser 用户
     * @param date  当前时间
     */
    public void informUserChangePassword(OsUser osUser,  Date date) {

        OsPasswordPolicy configure = getConfig(osUser.getTenantId()).getData();
        //密码安全策略没有配置的情况，不做通知
        if(BeanUtil.isEmpty(configure)){
            return ;
        }
        //密码永不过期，不做通知
        if(BeanUtil.isNotEmpty(configure.getIsNeverTimeout()) && YES.equals(configure.getIsNeverTimeout())){
            return ;
        }
        //密码过期时间（天）没有设置的情况，不做通知
        if(BeanUtil.isEmpty(configure.getTimeoutDay())){
            return ;
        }

        //几天后通知修改密码（天）没有设置的情况，不做通知
        if(BeanUtil.isEmpty(configure.getInformDay())){
            return ;
        }

        //密码修改通知方式 没有设置的情况，不做通知
        if(BeanUtil.isEmpty(configure.getInformType())){
            return ;
        }

        Date pwdUpdateTime = BeanUtil.isNotEmpty(osUser.getPwdUpdateTime()) ?
                osUser.getPwdUpdateTime() : osUser.getCreateTime();

        //密码修改的时间以及用户创建的时间都为空，则被视为密码已过期，不做通知
        if(BeanUtil.isEmpty(pwdUpdateTime)){
            return ;
        }

        //密码过期时间
        Date expireDate = DateUtils.addDays(pwdUpdateTime, configure.getTimeoutDay().intValue());
        //密码已过期，不做通知
        if(expireDate.before(date)){
            return ;
        }

        //当前时间的day（日）
        int curDay = DateUtils.getDay(date);
        Date informDate = DateUtils.addDays(expireDate, configure.getInformDay() - configure.getTimeoutDay());
        //通知用户修改密码时间的day（日）
        int informDay = DateUtils.getDay(informDate);
        //通知频率(每隔多少天通知一次) 没有设置，或者其设置的值小于1，并且几天后通知修改密码（天）配置选项的通知日期不等于当前日期，不做通知
        if((BeanUtil.isEmpty(configure.getInformFrequency()) || configure.getInformFrequency().intValue() < 1)
                && informDay != curDay){
            return;
        }

        //通知频率(每隔多少天通知一次) 处理逻辑
        boolean isMatch = false;
        while (expireDate.after(informDate)){
            if( informDay == curDay){
                isMatch = true;
                break;
            }

            informDate = DateUtils.addDays(informDate, configure.getInformDay().intValue());

        }

        //不在几天后通知修改密码（天）配置选项的以及通知频率(每隔多少天通知一次) 配置选项的通知时间范围，不做通知
        if(!isMatch){
           return;
        }

        String subject = "修改密码";
        String informContent = "您的密码离过期时间还剩" + (DateUtils.getDay(expireDate) - curDay)
                + "天。" + configure.getInformContent();

        //发送通知
        MessageModel model=new MessageModel();
        model.setMsgType(configure.getInformType());
        model.setSubject(subject);
        model.setContent(informContent);

        List<OsUserDto> receivers =new ArrayList<>();
        OsUserDto reveiver = new OsUserDto();
        cn.hutool.core.bean.BeanUtil.copyProperties(osUser, reveiver);
        receivers.add(reveiver);
        model.setReceivers(receivers);

        //发送通知消息。
        MesAutoUtil.sendMessage(JSONObject.toJSONString(model));

        String detail= "通知用户修改密码：用户【" + osUser.getFullName() + "】；"  + "通知内容：" + informContent;
        LogContext.put(Audit.DETAIL,detail);

    }

}
