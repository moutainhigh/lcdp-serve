package com.redxun.profile.impl;

import com.redxun.api.org.IOrgService;
import com.redxun.common.utils.ContextUtil;
import com.redxun.dto.user.OsGroupDto;
import com.redxun.profile.IProfileService;
import com.redxun.profile.ProfileType;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 用户组策略实现类
 */
@Component
public class GroupProfileServiceImpl implements IProfileService {
	
	@Resource
	private IOrgService orgService;


	@Override
	public ProfileType getType() {
		return new ProfileType("group", "用户组") ;
	}

	/**
	 * 获取当前用户所属的所有用户组ID
	 * @return
	 */
	@Override
	public Set<String> getCurrentProfile() {
		String userId= ContextUtil.getCurrentUserId();
		List<OsGroupDto> list= orgService.getBelongGroups(userId);
		if(list==null || list.size()==0) {
			return Collections.emptySet();
		}
		Set<String> set=new HashSet<String>();
		for(OsGroupDto group:list){
			set.add(group.getGroupId());
		}
		return set;
	}

}
