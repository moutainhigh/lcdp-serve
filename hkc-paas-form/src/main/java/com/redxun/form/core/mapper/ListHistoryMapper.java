package com.redxun.form.core.mapper;

import com.redxun.form.core.entity.ListHistory;
import org.apache.ibatis.annotations.Mapper;
import com.redxun.common.base.db.BaseDao;
import org.apache.ibatis.annotations.Param;

/**
* form_bo_list_history数据库访问层
*/
@Mapper
public interface ListHistoryMapper extends BaseDao<ListHistory> {

    Integer getMaxVersion(String listID);

    ListHistory selectByNum(@Param("listId") String listId, @Param("num")int num);
}
