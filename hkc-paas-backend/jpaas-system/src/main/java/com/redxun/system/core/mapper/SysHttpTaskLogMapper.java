package com.redxun.system.core.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.redxun.system.core.entity.SysHttpTaskLog;
import org.apache.ibatis.annotations.Mapper;
import com.redxun.common.base.db.BaseDao;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.ss.formula.functions.T;

import java.util.Map;

/**
* 接口调用日志表数据库访问层
*/
@Mapper
public interface SysHttpTaskLogMapper extends BaseDao<SysHttpTaskLog> {

    void clearAll();

    IPage getAllByTaskId(IPage<T> page, @Param("w") Map<String, Object> params);
}
