package com.redxun.bpm.core.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.redxun.bpm.core.entity.BpmTaskTransfer;
import com.redxun.common.base.db.BaseDao;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.ss.formula.functions.T;

import java.util.Map;

/**
* 流程任务转移记录数据库访问层
*/
@Mapper
public interface BpmTaskTransferMapper extends BaseDao<BpmTaskTransfer> {


    /**
     * 获取我收到任务。
     * @param page
     * @param params
     * @return
     */
    IPage<T> getMyReceiveTask(IPage<T> page, @Param("w") Map<String, Object> params);

    /**
     * 获取我转出去的任务。
     * @param page
     * @param params
     * @return
     */
    IPage<T> getMyTransOutTask(IPage<T> page, @Param("w") Map<String, Object> params);

}
