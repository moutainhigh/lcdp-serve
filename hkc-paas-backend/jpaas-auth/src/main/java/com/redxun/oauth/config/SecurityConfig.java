package com.redxun.oauth.config;

import com.redxun.common.constant.SecurityConstants;
import com.redxun.oauth.mobile.MobileAuthenticationSecurityConfig;
import com.redxun.oauth.openid.OpenIdAuthenticationSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.annotation.Resource;

/**
 * Spring security 配置入口声明
 * 在WebSecurityConfigurerAdapter不拦截oauth要开放的资源
 * 
 * @author yjy
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	/**
	 * 登录成功后的处理器
	 */
	@Autowired
	private AuthenticationSuccessHandler authenticationSuccessHandler;
	/**
	 * 登录失败后的处理器
	 */
	@Autowired
	private AuthenticationFailureHandler authenticationFailureHandler;
	/**
	 * 声明认证入口点
	 */
	@Autowired(required = false)
	private AuthenticationEntryPoint authenticationEntryPoint;
	/**
	 * 用户认证服务实现类
	 */
	@Resource
	private UserDetailsService userDetailsService;
	/**
	 * 密码加密器
	 */
	@Autowired
	private PasswordEncoder passwordEncoder;
	/**
	 * Oauth2退出处理器
	 */
	@Resource
	private LogoutHandler oauthLogoutHandler;
	/**
	 *
	 */
	@Autowired
	private ValidateCodeSecurityConfig validateCodeSecurityConfig;

	@Autowired
	private OpenIdAuthenticationSecurityConfig openIdAuthenticationSecurityConfig;

	@Autowired
	private MobileAuthenticationSecurityConfig mobileAuthenticationSecurityConfig;

	/**
	 * 这一步的配置是必不可少的，否则SpringBoot会自动配置一个AuthenticationManager,覆盖掉内存中的用户
	 * @return 认证管理对象
	 */
	@Bean
    @Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
					.anyRequest()
					//授权服务器关闭basic认证
                    .permitAll()
                    .and()
                .formLogin()
                    .loginPage(SecurityConstants.LOGIN_PAGE)
                    .loginProcessingUrl(SecurityConstants.OAUTH_LOGIN_PRO_URL)
                    .successHandler(authenticationSuccessHandler)
                    .failureHandler(authenticationFailureHandler)
                    .and()
				.logout()
					.logoutUrl(SecurityConstants.LOGOUT_URL)
					.logoutSuccessUrl(SecurityConstants.LOGIN_PAGE)
					.logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
					.addLogoutHandler(oauthLogoutHandler)
					.clearAuthentication(true)
					.and()
                .apply(validateCodeSecurityConfig)
                    .and()
                .apply(openIdAuthenticationSecurityConfig)
                    .and()
				.apply(mobileAuthenticationSecurityConfig)
					.and()
                .csrf().disable()
				// 解决不允许显示在iframe的问题
				.headers().frameOptions().sameOrigin().cacheControl();

		// 基于密码 等模式可以无session,不支持授权码模式
		if (authenticationEntryPoint != null) {
			http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint);
			http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		} else {
			// 授权码模式单独处理，需要session的支持，此模式可以支持所有oauth2的认证
			http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
		}
	}

	/**
	 * 全局用户信息
	 */
	@Autowired
	public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
	}
}
