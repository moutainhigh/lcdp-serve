package com.redxun.form.core.mapper;

import com.redxun.form.core.entity.FormCustomQuery;
import org.apache.ibatis.annotations.Mapper;
import com.redxun.common.base.db.BaseDao;

/**
* 自定查询数据库访问层
 *  @author hujun
*/
@Mapper
public interface FormCustomQueryMapper extends BaseDao<FormCustomQuery> {

}
