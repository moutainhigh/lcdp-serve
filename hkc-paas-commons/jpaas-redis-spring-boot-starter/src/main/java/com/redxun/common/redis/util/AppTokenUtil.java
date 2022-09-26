package com.redxun.common.redis.util;

import com.redxun.common.redis.template.RedisRepository;
import com.redxun.common.tool.Base64Util;
import com.redxun.common.utils.SpringUtil;

import java.util.UUID;

/**
 * APP Token 工具类
 */
public class AppTokenUtil {
	
	public static String TOKEN_PRE="app_token_";
	
	public static String getTokenKey(String token){
		return TOKEN_PRE + token;
	}

	public static String genSecret(){
		String uuid = UUID.randomUUID().toString();
		return Base64Util.encode(uuid).toLowerCase();
	}
	
	/**
	 * 验证token是否有效。
	 * @param accessToken
	 * @return
	 */
	public static boolean validToken(String accessToken){
		String key=getTokenKey(accessToken);
		RedisRepository redisRepository= SpringUtil.getBean(RedisRepository.class);
		return redisRepository.exists(key);
	}
	
	/**
	 * 根据token获取APPID。
	 * @param accessToken
	 * @return
	 */
	public static String getAppId(String accessToken){
		String key=getTokenKey(accessToken);
		RedisRepository redisRepository=SpringUtil.getBean(RedisRepository.class);
		boolean rtn= redisRepository.exists(key);
		if(rtn){
			return (String) redisRepository.get(key);
		}
		return null;
	}

}
