package com.redxun.portal.core.mapper;

import com.redxun.common.base.db.BaseDao;
import com.redxun.portal.core.entity.InsRemindDef;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
* 消息提醒数据库访问层
*/
@Mapper
public interface InsRemindDefMapper extends BaseDao<InsRemindDef> {
    /**
     * 根据solId 获取权限。
     * @return
     */
    List<InsRemindDef> getByOwner(@Param("params") Map<String,Object> params);
}
