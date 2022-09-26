package com.redxun.form.bo.mapper;

import com.redxun.form.bo.entity.FormBoDef;
import org.apache.ibatis.annotations.Mapper;
import com.redxun.common.base.db.BaseDao;

/**
* 业务模型数据库访问层
*/
@Mapper
public interface FormBoDefMapper extends BaseDao<FormBoDef> {

    /**
     * 根据表单别名获取BO别名。
     * @param alias
     * @return
     */
    String getAliasByFormAlias(String alias);
}
