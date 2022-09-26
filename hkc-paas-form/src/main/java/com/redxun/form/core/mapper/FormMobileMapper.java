package com.redxun.form.core.mapper;

import com.redxun.form.core.entity.FormMobile;
import com.redxun.form.core.entity.FormPc;
import org.apache.ibatis.annotations.Mapper;
import com.redxun.common.base.db.BaseDao;

import java.util.Map;

/**
* 手机表单数据库访问层
*/
@Mapper
public interface FormMobileMapper extends BaseDao<FormMobile> {


    /**
     * 根据主键获取表单
     * @param id
     * @return
     */
    FormMobile getById(String id);

    /**
     * 判断表单是否存在。
     * @param params
     * @return
     */
    Integer isExist(Map<String,Object> params);
}
