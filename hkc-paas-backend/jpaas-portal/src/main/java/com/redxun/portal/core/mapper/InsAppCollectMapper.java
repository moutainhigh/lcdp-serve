package com.redxun.portal.core.mapper;

import com.redxun.portal.core.entity.InsAppCollect;
import org.apache.ibatis.annotations.Mapper;
import com.redxun.common.base.db.BaseDao;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* ins_app_collect数据库访问层
*/
@Mapper
public interface InsAppCollectMapper extends BaseDao<InsAppCollect> {

    List<InsAppCollect> getCommonApp(@Param("curUserId")String curUserId);
}
