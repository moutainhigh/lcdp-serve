package com.redxun.bpm.core.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.redxun.bpm.core.entity.BpmInst;
import com.redxun.bpm.core.entity.BpmInstTracked;
import org.apache.ibatis.annotations.Mapper;
import com.redxun.common.base.db.BaseDao;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.ss.formula.functions.T;

import java.util.Map;

/**
* 流程实例跟踪数据库访问层
*/
@Mapper
public interface BpmInstTrackedMapper extends BaseDao<BpmInstTracked> {

    IPage<BpmInst> getMyTracked(IPage<T> page, @Param("w") Map<String, Object> params);

}
