package com.redxun.oauth.handler;

import cn.hutool.core.util.StrUtil;
import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.base.entity.IUser;
import com.redxun.common.utils.ContextUtil;
import com.redxun.common.utils.SysPropertiesUtil;
import com.redxun.common.utils.SysUserUtil;
import com.redxun.log.annotation.AuditLog;
import com.redxun.log.model.Audit;
import com.redxun.log.model.LogContext;
import com.redxun.oauth2.common.util.AuthUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Oauth 用户退出处理器
 * @author yjy
 * @date 2018/10/17
 */
@Slf4j
@ClassDefine(title = "用户认证",packageName = "认证中心",alias = "aAuth2Controller",path = "")
public class OauthLogoutHandler implements LogoutHandler {
	@Autowired
	private TokenStore tokenStore;

	@Override
	@AuditLog(operation = "退出登录")
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		Assert.notNull(tokenStore, "tokenStore must be set");
		String token = request.getParameter("token");
		if (StrUtil.isEmpty(token)) {
			token = AuthUtils.extractToken(request);
		}
		if(StrUtil.isNotEmpty(token)){
			OAuth2AccessToken existingAccessToken = tokenStore.readAccessToken(token);
			IUser user= SysUserUtil.getLoginUser(token);
			//方便记录日志
			ContextUtil.setCurrentUser(user);
			LogContext.put(Audit.ACTION,"logout");
			LogContext.put(Audit.PK,user.getUserId());
			String detail=user.getFullName() +"("+user.getUserId()+") 退出登录!";
			LogContext.put(Audit.DETAIL,detail);
			OAuth2RefreshToken refreshToken;
			//是否允许多地登录
			Boolean multipleLogin = SysPropertiesUtil.getBoolean("multipleLogin");
			if (existingAccessToken != null && !multipleLogin) {
				if (existingAccessToken.getRefreshToken() != null) {
					log.info("remove refreshToken!", existingAccessToken.getRefreshToken());
					refreshToken = existingAccessToken.getRefreshToken();
					tokenStore.removeRefreshToken(refreshToken);
				}
				log.info("remove existingAccessToken!", existingAccessToken);
				tokenStore.removeAccessToken(existingAccessToken);
			}
		}
	}
}
