package com.redxun.profile.impl;

import com.redxun.api.org.IOrgService;
import com.redxun.common.model.JPaasUser;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ContextUtil;
import com.redxun.dto.user.OsGroupDto;
import com.redxun.profile.IProfileService;
import com.redxun.profile.ProfileType;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 获取组的下级组信息用户策略实现
 */
@Service
public class SubGroupProfileServiceImpl implements IProfileService {

	@Resource
	private IOrgService orgService;

	@Override
	public ProfileType getType() {
		return new ProfileType("subGroup","组织或以下") ;
	}

	/**
	 * 返回当前用户其所属用户组ID及其子组所有的ID
	 * @return
	 */
	@Override
	public Set<String> getCurrentProfile() {
		JPaasUser user= (JPaasUser) ContextUtil.getCurrentUser();
		Set<OsGroupDto> groupDtos=new HashSet<>();

		if(StringUtils.isNotEmpty(user.getDeptId())){
			OsGroupDto mainGroup=orgService.getGroupById(user.getDeptId());
			if(mainGroup!=null){
				groupDtos.add(mainGroup);
			}
		}

		List<OsGroupDto> deps= orgService. getBelongGroups(user.getUserId() );
		if(BeanUtil.isNotEmpty(deps)) {
			groupDtos.addAll(deps);
		}
		Set<String> set=new HashSet<String>();
		if(BeanUtil.isEmpty(groupDtos)){
			return set;
		}
		for(OsGroupDto group:groupDtos){
			String path=group.getPath();
			if(path==null) {
				continue;
			}
			path= StringUtils.trimPrefix(path, "0.");
			path= StringUtils.trimSuffix(path, ".");
			String[] aryGroup=path.split("[.]");
			for(String groupId:aryGroup){
				set.add(groupId);
			}
		}
		return set;
	}


}
