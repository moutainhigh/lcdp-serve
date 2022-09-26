package com.redxun.bpm.core.mapper;

import com.redxun.bpm.core.entity.MobileTag;
import com.redxun.common.base.db.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
* 手机型号绑定-数据库访问层
*/
@Mapper
public interface MobileTagMapper extends BaseDao<MobileTag> {


}
