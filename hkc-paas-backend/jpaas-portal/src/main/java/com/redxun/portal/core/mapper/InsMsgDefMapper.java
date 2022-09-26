package com.redxun.portal.core.mapper;

import com.redxun.portal.core.entity.InsMsgDef;
import org.apache.ibatis.annotations.Mapper;
import com.redxun.common.base.db.BaseDao;

import java.util.List;

/**
* INS_MSG_DEF数据库访问层
*/
@Mapper
public interface InsMsgDefMapper extends BaseDao<InsMsgDef> {

    List<InsMsgDef> getDataByBoxId(String boxId);
}
