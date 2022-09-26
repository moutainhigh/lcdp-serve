package com.redxun.db.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.redxun.common.base.entity.IUser;
import com.redxun.common.utils.ContextUtil;
import org.apache.ibatis.reflection.MetaObject;

import java.util.Date;

/**
 * 自定义填充公共 name 字段
 *
 * @author yjy
 * @date 2018/12/11
 */
public class DateMetaObjectHandler implements MetaObjectHandler {
    private final static String UPDATE_TIME = "updateTime";
    private final static String CREATE_TIME = "createTime";

    private final static String CREATE_BY = "createBy";
    private final static String UPDATE_BY = "updateBy";

    private final static String CREATE_DEPT_ID = "createDepId";

    private final static String TENANT_ID = "tenantId";

    private final static String COMPANY_ID = "companyId";



    /**
     * 插入填充，字段为空自动填充
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        Object createTime = getFieldValByName(CREATE_TIME, metaObject);
        Object updateTime = getFieldValByName(UPDATE_TIME, metaObject);
        if (createTime == null || updateTime == null) {
            Date date = new Date();
            if (createTime == null) {
                setFieldValByName(CREATE_TIME, date, metaObject);
            }
            if (updateTime == null) {
                setFieldValByName(UPDATE_TIME, date, metaObject);
            }
        }
        IUser user= ContextUtil.getCurrentUser();


        Object createBy = getFieldValByName(CREATE_BY, metaObject);
        if(createBy == null && user!=null){
            setFieldValByName(CREATE_BY, user.getUserId(), metaObject);
        }

        Object createDepId = getFieldValByName(CREATE_DEPT_ID, metaObject);
        if(createDepId == null && user!=null){
            setFieldValByName(CREATE_DEPT_ID, user.getDeptId(), metaObject);
        }

        /**
         * 租户ID
         */
        Object tenantId = getFieldValByName(TENANT_ID, metaObject);
        if(tenantId == null && user!=null){
            setFieldValByName(TENANT_ID, user.getTenantId(), metaObject);
        }

        /**
         * 添加公司ID
         */
        Object companyId = getFieldValByName(COMPANY_ID, metaObject);
        if(companyId == null && user!=null){
            setFieldValByName(COMPANY_ID, user.getCompanyId(), metaObject);
        }

    }

    /**
     * 更新填充
     *
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        //mybatis-plus版本2.0.9+
        setFieldValByName(UPDATE_TIME, new Date(), metaObject);

        IUser user= ContextUtil.getCurrentUser();
        Object updateBy = getFieldValByName(UPDATE_BY, metaObject);
        if(updateBy == null && user!=null){
            setFieldValByName(UPDATE_BY, user.getUserId(), metaObject);
        }
    }
}
