package com.xxl.job.admin.service;

import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.model.JPaasUser;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.utils.SysUserUtil;
import com.xxl.job.admin.core.model.XxlJobUser;
import com.xxl.job.admin.core.util.CookieUtil;
import com.xxl.job.admin.core.util.I18nUtil;
import com.xxl.job.admin.core.util.JacksonUtil;
import com.xxl.job.admin.dao.XxlJobUserDao;
import com.xxl.job.admin.fegin.UserClient;
import com.xxl.job.core.biz.model.ReturnT;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigInteger;

/**
 * @author xuxueli 2019-05-04 22:13:264
 */
@Configuration
public class LoginService {

    public static final String LOGIN_IDENTITY_KEY = "XXL_JOB_LOGIN_IDENTITY";

    @Resource
    private XxlJobUserDao xxlJobUserDao;

    @Resource
    private UserClient userClient;

    @Resource
    private ApplicationContext applicationContext;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    private String makeToken(XxlJobUser xxlJobUser){
        String tokenJson = JacksonUtil.writeValueAsString(xxlJobUser);
        String tokenHex = new BigInteger(tokenJson.getBytes()).toString(16);
        return tokenHex;
    }
    private XxlJobUser parseToken(String tokenHex){
        XxlJobUser xxlJobUser = null;
        if (tokenHex != null) {
            String tokenJson = new String(new BigInteger(tokenHex, 16).toByteArray());      // username_password(md5)
            xxlJobUser = JacksonUtil.readValue(tokenJson, XxlJobUser.class);
        }
        return xxlJobUser;
    }

    public JPaasUser getJpaasUserByToken(String token){
        JsonResult<JPaasUser> jsonResult= userClient.getLoginUserInfoByToken(token);
        if(jsonResult != null && jsonResult.getData() != null){
            return  jsonResult.getData();
        }
        return null;
    }


    public ReturnT<String> login(HttpServletRequest request, HttpServletResponse response, JPaasUser user, String username, String password, boolean ifRemember, boolean isIgnorePwd){

        // param
        if (username.equals("") || username.trim().length()==0 ){
            return new ReturnT<String>(500, I18nUtil.getString("login_param_empty"));
        }

        // valid passowrd
       /* XxlJobUser xxlJobUser = xxlJobUserDao.loadByUserName(username);
        if (xxlJobUser == null) {
            return new ReturnT<String>(500, I18nUtil.getString("login_param_unvalid"));
        }
        String passwordMd5 = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!passwordMd5.equals(xxlJobUser.getPassword())) {
            return new ReturnT<String>(500, I18nUtil.getString("login_param_unvalid"));
        }

        String loginToken = makeToken(xxlJobUser);*/
        //JPaasUser user = userClient.findByUsername(username);
        if (user == null) {
            return new ReturnT<String>(500, I18nUtil.getString("login_param_unvalid"));
        }
        XxlJobUser xxlJobUser = new XxlJobUser();
        xxlJobUser.setId(user.getUserId());
        xxlJobUser.setUsername(user.getUsername());
        xxlJobUser.setPassword(user.getPassword());
        xxlJobUser.setRole(1);


        String loginToken = makeToken(xxlJobUser);

        // do login
        CookieUtil.set(response, LOGIN_IDENTITY_KEY, loginToken, ifRemember);
        return new ReturnT(xxlJobUser);
    }

    /**
     * logout
     *
     * @param request
     * @param response
     */
    public ReturnT<String> logout(HttpServletRequest request, HttpServletResponse response){
        CookieUtil.remove(request, response, LOGIN_IDENTITY_KEY);
        return ReturnT.SUCCESS;
    }

    /**
     * logout
     *
     * @param request
     * @return
     */
    public XxlJobUser ifLogin(HttpServletRequest request, HttpServletResponse response){
        String cookieToken = CookieUtil.getValue(request, LOGIN_IDENTITY_KEY);
        if (cookieToken != null) {
            XxlJobUser cookieUser = null;
            try {
                cookieUser = parseToken(cookieToken);
            } catch (Exception e) {
                logout(request, response);
            }
            if (cookieUser != null && StringUtils.isNotEmpty( cookieUser.getUsername())) {
                JPaasUser dbUser = userClient.findByUsername(cookieUser.getUsername());
                if (dbUser != null) {
                    XxlJobUser xxlJobUser = new XxlJobUser();
                    xxlJobUser.setId(dbUser.getUserId());
                    xxlJobUser.setUsername(dbUser.getUsername());
                    xxlJobUser.setPassword(dbUser.getPassword());
                    xxlJobUser.setRole(1);
//                    if (cookieUser.getPassword().equals(xxlJobUser.getPassword())) {
                    return xxlJobUser;
//                    }
                }
                /* XxlJobUser dbUser = xxlJobUserDao.loadByUserName(cookieUser.getUsername());
                if (dbUser != null) {
                    if (cookieUser.getPassword().equals(dbUser.getPassword())) {
                        return dbUser;
                    }
                }*/
            }
        }
        return null;
    }


}
