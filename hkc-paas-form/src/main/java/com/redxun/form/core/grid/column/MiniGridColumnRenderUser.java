package com.redxun.form.core.grid.column;

import com.redxun.common.base.entity.GridHeader;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ContextUtil;
import com.redxun.dto.user.OsGroupDto;
import com.redxun.dto.user.OsUserDto;
import com.redxun.feign.OsGroupClient;
import com.redxun.feign.OsUserClient;
import com.redxun.form.core.grid.enums.MiniGridColumnType;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 用户类型的映射
 * @author mansan
 *
 */
@Component
public class MiniGridColumnRenderUser implements MiniGridColumnRender{

    @Resource
    OsUserClient osUserManager;
    @Resource
    OsGroupClient osGroupManager;


    @Override
    public String getRenderType() {
        return MiniGridColumnType.USER.name();
    }

    @Override
    public String render(GridHeader gridHeader, Map<String,Object> rowData, Object val, boolean isExport) {
        if(val==null) {
            return "";
        }
        OsUserDto osUser=osUserManager.getById(val.toString());
        if(osUser==null){
            return "";
        }
        String depShowType=gridHeader.getRenderConfObj().getString("depShowType");
        String showField=gridHeader.getRenderConfObj().getString("showField");
        String otherFields=gridHeader.getRenderConfObj().getString("otherFields");
        StringBuffer sb=new StringBuffer();
        String showName="showName";
        String showFullName="showFullName";
        if(showName.equals(depShowType)){
            OsGroupDto osGroup=osGroupManager.getMainDeps(val.toString(), ContextUtil.getCurrentTenantId());
            if(osGroup!=null){
                sb.append(osGroup.getName()).append("/");
            }
        }else if(showFullName.equals(depShowType)){
            OsGroupDto osGroup=osGroupManager.getMainDeps(val.toString(),ContextUtil.getCurrentTenantId());
            sb.append(getMainDepFullNames(osGroup));
        }
        if(StringUtils.isEmpty(showField)){
            showField="fullName";
        }
        String userLabel=(String) BeanUtil.getFieldValueFromObject(osUser, showField);
        sb.append(userLabel);
        if(StringUtils.isNotEmpty(otherFields)){
            String[]fields=otherFields.split(",");
            int i=0;
            if(fields.length>0){
                sb.append("(");
            }
            for(String f:fields){
                String fLabel=(String)BeanUtil.getFieldValueFromObject(osUser, f);
                if(i>0){
                    sb.append(",");
                }
                sb.append(fLabel);
                i++;
            }
            if(fields.length>0){
                sb.append(")");
            }
        }

        return sb.toString();
    }

    private String getMainDepFullNames(OsGroupDto mainDep){
        if(mainDep==null || org.apache.commons.lang.StringUtils.isEmpty(mainDep.getPath())) {
            return "";
        }

        String[] mainDepIds=mainDep.getPath().split("[.]");
        StringBuffer depBuf=new StringBuffer();
        for(String depId:mainDepIds){
            if("0".equals(depId)){
                continue;
            }
            OsGroupDto group=osGroupManager.getById(depId);
            if(group!=null){
                depBuf.append(group.getName()).append("/");
            }
        }
        return depBuf.toString();
    }

}
