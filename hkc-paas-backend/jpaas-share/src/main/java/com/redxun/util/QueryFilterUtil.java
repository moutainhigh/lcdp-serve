package com.redxun.util;

import com.redxun.common.base.search.QueryFilter;
import com.redxun.common.base.search.QueryParam;
import com.redxun.common.base.search.WhereParam;
import com.redxun.common.model.JPaasUser;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ContextUtil;
import com.redxun.common.utils.SpringUtil;
import com.redxun.dto.sys.SysTreeDto;
import com.redxun.feign.sys.SystemClient;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * QueryFilter工具类
 */
public class QueryFilterUtil {

    /**
     * 设置分类查询条件
     * @param filter
     * @param fieldName
     * @param catKey
     * @param readKey
     */
    public static void setQueryFilterByTreeId(QueryFilter filter,  String fieldName , String catKey, String readKey){
        setQueryFilterByTreeId(filter,fieldName,fieldName,catKey,readKey);
    }

    /**
     * 设置分类查询条件。
     * @param filter
     * @param paramName
     * @param fieldName
     * @param catKey
     * @param readKey
     */
    public static void setQueryFilterByTreeId(QueryFilter filter,  String paramName,String fieldName , String catKey, String readKey){
        SystemClient systemClient= SpringUtil.getBean(SystemClient.class);
        List<WhereParam> list=filter.getFieldLogic().getWhereParams();
        boolean flag=false;
        for(WhereParam whereParam:list){
            if(((QueryParam)whereParam).getFieldName().contains(paramName)){
                flag=true;
            }
        }
        JPaasUser user=(JPaasUser) ContextUtil.getCurrentUser();
        if(!flag && !user.isAdmin()){
            //不为超级管理员时，进行过滤
            List<SysTreeDto> sysTreeDtoList=systemClient.getByCatKey(catKey,readKey,true,true,"");
            Set<String> treeIds=new HashSet<>();
            for(SysTreeDto sysTreeDto:sysTreeDtoList){
                treeIds.add(sysTreeDto.getTreeId());
            }
            String val=StringUtils.join(treeIds,",");
            if(StringUtils.isEmpty(val)){
                filter.addQueryParam(new QueryParam(fieldName,QueryParam.OP_EQUAL,val));
            }else {
                filter.addQueryParam("Q_" + fieldName + "_S_IN", StringUtils.join(treeIds, ","));
            }
        }
    }
}
