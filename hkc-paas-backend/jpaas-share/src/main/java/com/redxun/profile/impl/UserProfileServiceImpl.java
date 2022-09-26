package com.redxun.profile.impl;

import com.redxun.common.utils.ContextUtil;
import com.redxun.profile.IProfileService;
import com.redxun.profile.ProfileType;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * 用户策略实现
 */
@Service
public class UserProfileServiceImpl implements IProfileService {
	/**
	 * 类型
	 * @return
	 */
	@Override
	public ProfileType getType() {
		return new ProfileType("user","用户") ;
	}

	/**
	 * 返回当前用户ID
	 * @return
	 */
	@Override
	public Set<String> getCurrentProfile() {
		String userId= ContextUtil.getCurrentUserId();
		Set<String> set=new HashSet<String>();
		set.add(userId);
		return set;
	}

}
