package com.redxun.common.utils;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.redxun.common.base.entity.IUser;
import com.redxun.common.constant.SecurityConstants;
import com.redxun.common.model.JPaasUser;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.StringUtils;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

import java.io.IOException;
import java.net.URLDecoder;

/**
 * Token 工具类
 *
 */
public class TokenUtil {

	public static String SIGN_KEY = "RedXun";
	public static String BEARER = "bearer";
	public static Integer AUTH_LENGTH = 7;
	public static String BASE64_SECURITY;

	/**
	 * 获取Token
	 * @return
	 */
	public static String getToken (){
		HttpServletRequest request = HttpContextUtil.getRequest();
		if(BeanUtil.isEmpty(request)){
			return "";
		}

		String header = request.getHeader(SecurityConstants.Authorization) ;
		if(StringUtils.isEmpty(header)) {
			return "";
		}
		String token = StringUtils.isBlank(StringUtils.substringAfter(header, OAuth2AccessToken.BEARER_TYPE+" ")) ? request.getParameter(OAuth2AccessToken.ACCESS_TOKEN) :  StringUtils.substringAfter(header, OAuth2AccessToken.BEARER_TYPE +" ");
		token = StringUtils.isBlank(request.getHeader(SecurityConstants.TOKEN_HEADER)) ? token : request.getHeader(SecurityConstants.TOKEN_HEADER) ;

		return token ;

	}

	/**
	 * 获取用户
	 * @return
	 * @throws IOException
	 */
	public static IUser getUser() throws IOException {
		HttpServletRequest request = HttpContextUtil.getRequest();
		if(BeanUtil.isEmpty(request)){
			return null;
		}

		String curUser = request.getHeader(SecurityConstants.CUR_USER) ;
		if(StringUtils.isEmpty(curUser)) {
			return null;
		}
		curUser = URLDecoder.decode(curUser,"UTF-8");
		return JSONObject.parseObject(curUser,JPaasUser.class);
	}


	
}
