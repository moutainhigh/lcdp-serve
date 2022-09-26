package com.redxun.form.core.mapper;

import com.redxun.form.core.entity.FormRule;
import org.apache.ibatis.annotations.Mapper;
import com.redxun.common.base.db.BaseDao;

import java.util.Map;

/**
* 表单校验配置数据库访问层
*/
@Mapper
public interface FormRuleMapper extends BaseDao<FormRule> {
    /**
     * 判断表单是否存在。
     * @param params
     * @return
     */
    Integer isExist(Map<String,Object> params);
}
