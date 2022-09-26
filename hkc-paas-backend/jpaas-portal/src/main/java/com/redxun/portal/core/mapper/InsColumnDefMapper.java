package com.redxun.portal.core.mapper;

import com.redxun.portal.core.entity.InsColumnDef;
import org.apache.ibatis.annotations.Mapper;
import com.redxun.common.base.db.BaseDao;

import java.util.List;

/**
 * 栏目定义数据库访问层
 */
@Mapper
public interface InsColumnDefMapper extends BaseDao<InsColumnDef> {
    List<InsColumnDef> queryByIsNews(String isNews);
}
