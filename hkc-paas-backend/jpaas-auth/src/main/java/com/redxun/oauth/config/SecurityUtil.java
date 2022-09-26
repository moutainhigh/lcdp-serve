package com.redxun.oauth.config;

import com.redxun.api.org.impl.UserServiceImpl;
import com.redxun.common.context.TenantContextHolder;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.constant.CommonConstant;
import com.redxun.common.utils.SpringUtil;
import com.redxun.common.utils.SysPropertiesUtil;
import com.redxun.oauth2.common.config.AppConfig;
import com.redxun.oauth2.common.properties.AppProperties;
import com.redxun.oauth2.common.util.PasswordUtil;
import org.apache.commons.collections.MapUtils;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.UnapprovedClientAuthenticationException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/***
 * 安全框架处理工具类，提供登录认证实现
 */
public class SecurityUtil {

    /**
     * 登录认证
     * @param request
     * @param token 安全令牌
     * @param ignorePwd 是否忽略密码
     * @return
     * @throws IOException
     */
    public static OAuth2AccessToken login(HttpServletRequest request,AbstractAuthenticationToken token,Boolean ignorePwd ) throws IOException {
        PasswordUtil.setIgnore(ignorePwd);
        AuthenticationManager authenticationManager= SpringUtil.getBean(AuthenticationManager.class);
        AuthorizationServerTokenServices authorizationServerTokenServices= SpringUtil.getBean(AuthorizationServerTokenServices.class);
        AppProperties appProperties= SpringUtil.getBean(AppProperties.class);
        /**
         * 获取客户端ID与密钥
         */
        String clientId = appProperties.getClientId();
        String clientSecret = appProperties.getClientSecret();
        ClientDetails clientDetails = getClient(clientId, clientSecret);

        //保存租户id
        TenantContextHolder.setTenant(clientId);
        TokenRequest tokenRequest = new TokenRequest(MapUtils.EMPTY_MAP, clientId, clientDetails.getScope(), "customer");
        //创建客户端请求
        OAuth2Request oAuth2Request = tokenRequest.createOAuth2Request(clientDetails);
        //根据令处理认证
        Authentication authentication = authenticationManager.authenticate(token);

        UserServiceImpl userService =  SpringUtil.getBean(UserServiceImpl.class);
        //密码过期导致账号被锁定
        if(userService.isLockedByPasswordExpire((String)token.getPrincipal(), getTenantIdByUsername((String)token.getPrincipal()))){
            throw new InternalAuthenticationServiceException("密码过期导致账号被锁定，请联系管理员进行解锁。");
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(oAuth2Request, authentication);
        //是否允许多地登录
        Boolean multipleLogin = SysPropertiesUtil.getBoolean("multipleLogin");
        if(!multipleLogin){
            //删除已存在的token
            OAuth2AccessToken accessToken = authorizationServerTokenServices.getAccessToken(oAuth2Authentication);
            if(BeanUtil.isNotEmpty(accessToken)){
                TokenStore tokenStore= SpringUtil.getBean(TokenStore.class);
                tokenStore.removeAccessToken(accessToken);
            }
        }
        OAuth2AccessToken oAuth2AccessToken = authorizationServerTokenServices.createAccessToken(oAuth2Authentication);
        oAuth2Authentication.setAuthenticated(true);
        return oAuth2AccessToken;

    }

    /**
     * 根据ClientID 与密钥获取客户认证信息
     * @param clientId
     * @param clientSecret
     * @return
     */
    public static ClientDetails getClient(String clientId, String clientSecret) {
        ClientDetailsService clientDetailsService= SpringUtil.getBean(ClientDetailsService.class);
        AppConfig appConfig= SpringUtil.getBean(AppConfig.class);
        ClientDetails clientDetails = appConfig.getByApp();
        if (clientDetails == null) {
            throw new UnapprovedClientAuthenticationException("clientId对应的信息不存在");
        } else if (!clientSecret.equals( clientDetails.getClientSecret())) {
            throw new UnapprovedClientAuthenticationException("clientSecret不匹配");
        }
        return clientDetails;
    }

    /**
     * 根据用户名获取租户id
     * @param username
     * @return
     */
    public static String getTenantIdByUsername(String username){
        UserServiceImpl userService =  SpringUtil.getBean(UserServiceImpl.class);
        String tenantId = null;
        int index = username.indexOf("@");
        if(index > -1){ //带有@，表示登录的用户名称是以 用户名@机构域名 组成的值。
            //机构域名
            String domain = username.substring(index + 1);
            //机构ID
            tenantId = userService.findTenanIdByDomain(domain);
        } else if(username.indexOf("#")!=-1) {//带有#，表示登录的用户名是以 用户名#机构编码 组成的值
            String[]arrs=username.split("#");
            //机构名称
            tenantId=userService.findTenantIdByInstNo(arrs[1]);
        } else{
            tenantId = CommonConstant.PLATFORM_TENANT_ID_VAL;
        }

        return tenantId;

    }

}
