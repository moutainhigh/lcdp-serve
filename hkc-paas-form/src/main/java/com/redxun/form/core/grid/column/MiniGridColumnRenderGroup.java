package com.redxun.form.core.grid.column;

import com.redxun.common.base.entity.GridHeader;
import com.redxun.common.constant.MBoolean;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.StringUtils;
import com.redxun.dto.user.OsGroupDto;
import com.redxun.feign.OsGroupClient;
import com.redxun.form.core.grid.enums.MiniGridColumnType;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author hujun
 */
@Component
public class MiniGridColumnRenderGroup implements MiniGridColumnRender{

	@Resource
	OsGroupClient osGroupManager;

	@Override
	public String getRenderType() {
		return MiniGridColumnType.GROUP.name();
	}

	@Override
	public String render(GridHeader gridHeader, Map<String,Object> rowData, Object val, boolean isExport) {
		if (BeanUtil.isEmpty(val)) {
			return "";
		}
		OsGroupDto osGroup = osGroupManager.getById(val.toString());
		if (osGroup == null) {
			return "";
		}
		String groupNames = "";
		String showPath = gridHeader.getRenderConfObj().getString("showPath");
		if (!isExport && MBoolean.TRUE_LOWER.val.equals(showPath) && StringUtils.isNotEmpty(osGroup.getPath())) {
			groupNames = getGroupFullPathNames(val.toString());
		} else {
			groupNames = osGroup.getName();
		}
		return groupNames;
	}

	public String getGroupFullPathNames(String groupId){
		OsGroupDto group=osGroupManager.getById(groupId);
		if(BeanUtil.isEmpty(group)) {
			return "";
		}
		if(StringUtils.isEmpty(group.getPath())){
			return group.getName();
		}

		String[]paths=group.getPath().split("[.]");

		StringBuffer sb=new StringBuffer();
		for(String id:paths){
			if("0".equals(id)){
				continue;
			}
			OsGroupDto p=osGroupManager.getById(id);
			if(p==null){
				continue;
			}
			if(sb.length()>0){
				sb.append("/");
			}
			sb.append(p.getName());
		}
		return sb.toString();
	}

}
