package com.redxun.web.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.common.annotation.ContextQuerySupport;
import com.redxun.common.base.entity.IUser;
import com.redxun.common.base.search.QueryFilter;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.tool.constant.CommonConstant;
import com.redxun.common.utils.ContextUtil;
import com.redxun.common.utils.DbLogicDelete;
import com.redxun.common.utils.SysPropertiesUtil;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * 功能: web请求工具类。
 *
 * @author ASUS
 * @date 2022/6/14 16:52
 */
public class WebUtil {


    /**
     * 获取当前租户。
     * @return
     */
    public static String getCurrentTenantId(){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String tenantId=request.getParameter("tenantId");
        if(StringUtils.isEmpty(tenantId)){
            tenantId= ContextUtil.getCurrentTenantId();
        }
        return tenantId;
    }

    /**
     * 处理filter。
     * @param filter
     * @param annotation
     */
    public static void handFilter(QueryFilter filter,ContextQuerySupport annotation){
        if(annotation==null){
            return;
        }
        IUser user=ContextUtil.getCurrentUser();
        if(user==null){
            return;
        }
        //处理租户上下文
        handTenant(filter,annotation.tenant(),user);
        //处理公司上下文
        handCompany(filter,annotation.company(),user);
        //处理逻辑删除
        handLogicDel(filter);
    }

    /**
     * 处理filter。
     * @param filter
     * @param tenantType
     * @param companyType
     */
    public static void handFilter(QueryFilter filter,String tenantType,String companyType){
        IUser user=ContextUtil.getCurrentUser();
        //处理租户上下文
        handTenant(filter,tenantType,user);
        //处理公司上下文
        handCompany(filter,companyType,user);
    }

    /**
     * 根据注解处理公司上下文查询。
     * @param filter
     * @param type
     * @param user
     */
    private static void handCompany(QueryFilter filter,String type,IUser user){

        //如果为None那么不适用
        if(ContextQuerySupport.NONE.equals(type)){
            return;
        }
        Boolean supportGrade = SysPropertiesUtil.getSupportGradeConfig();
        if(!supportGrade){
            return;
        }
        String companyId= user.getCompanyId();
        if(StringUtils.isEmpty(companyId) ){
            return;
        }

        String companyVal=handContextString(type,companyId);
        String companyPrefix="";
        if(filter.getParams().containsKey(CommonConstant.COMPANY_PREFIX)){
            companyPrefix=(String)filter.getParams().get(CommonConstant.COMPANY_PREFIX);
        }
        filter.addQueryParam("Q_"+companyPrefix+"COMPANY_ID__S_IN",companyVal);
    }

    /**
     * 逻辑删除上下文查询。
     * @param filter
     */
    private static void handLogicDel(QueryFilter filter){
        //逻辑删除
        if (!DbLogicDelete.getLogicDelete()) {
            return;
        }
        String tenantPrefix="";
        if(filter.getParams().containsKey(CommonConstant.DELETED_PREFIX)){
            tenantPrefix=(String)filter.getParams().get(CommonConstant.DELETED_PREFIX);
        }
        filter.addQueryParam("Q_"+tenantPrefix+"DELETED__S_EQ","0");
    }

    /**
     * 处理租户上下文查询。
     * @param filter
     * @param type
     * @param user
     */
    private static void handTenant(QueryFilter filter,String type,IUser user){
        String tenantId= user.getTenantId();
        if(StringUtils.isEmpty(tenantId)){
            return;
        }

        if(ContextQuerySupport.NONE.equals(type)){
            return;
        }
        String tenantVal=handContextString(type,tenantId);

        String tenantPrefix="";
        if(filter.getParams().containsKey(CommonConstant.TENANT_PREFIX)){
            tenantPrefix=(String)filter.getParams().get(CommonConstant.TENANT_PREFIX);
        }

        filter.addQueryParam("Q_"+tenantPrefix+"TENANT_ID__S_IN",tenantVal);

    }

    /**
     * 根据上下文查询注解，构造上下文 wrapper对象。
     * @param annotation
     * @param wrapper
     */
    public static void handContextWrapper(ContextQuerySupport annotation,QueryWrapper wrapper){
        if(annotation==null){
            return ;
        }

        if(!annotation.tenant().equals(ContextQuerySupport.NONE)){
            String tenantId=getCurrentTenantId();
            List<String> tenantIds=handContext(annotation.tenant(),tenantId);
            wrapper.in(CommonConstant.TENANT_ID,tenantIds);
        }


        IUser user=ContextUtil.getCurrentUser();
        if(user==null){
            return;
        }

        if(!annotation.company().equals(ContextQuerySupport.NONE)){
            Boolean supportGrade = SysPropertiesUtil.getSupportGradeConfig();
            if(supportGrade!=null && supportGrade){
                List<String> companys=handContext(annotation.company(),user.getCompanyId());
                wrapper.in(CommonConstant.COMPANY_ID,companys);
            }
        }

    }

    /**
     * 根据类型返回集合对象。
     * @param type
     * @param curId
     * @return
     */
    private static List<String> handContext(String type, String curId){

        List<String> list=new ArrayList<>();
        if(type.equals(ContextQuerySupport.NONE)){
            return list;
        }

        if(type.equals(ContextQuerySupport.CURRENT)){
            list.add(curId);
        }
        else if(type.equals(ContextQuerySupport.BOTH)){
            list.add("0");
            if(!list.contains(curId)){
                list.add(curId);
            }
        }
        else {
            list.add("0");
        }
        return  list;
    }


    private static String handContextString(String type,String curId){
        String tmp="0";
        if(type.equals(ContextQuerySupport.CURRENT)){
            tmp=curId ;
        }
        else if(type.equals(ContextQuerySupport.BOTH)){
            tmp=curId +",0";
        }
        else {
            tmp="0";
        }
        return  tmp;
    }


    /**
     * 返回公司ID
     * 如果公司不为0，则返回 0 + ,"公司ID"
     * @param companyId
     * @return
     */
    public static String getCompanyId(String companyId){
        if(CommonConstant.COMPANY_ZERO.equals(companyId)){
            return  "'" + companyId +"'";
        }
        return "'0','"+ companyId +"'";
    }
}
