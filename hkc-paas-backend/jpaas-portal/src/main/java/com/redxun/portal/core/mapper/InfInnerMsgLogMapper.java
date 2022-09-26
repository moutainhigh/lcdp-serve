package com.redxun.portal.core.mapper;

import com.redxun.portal.core.entity.InfInnerMsgLog;
import org.apache.ibatis.annotations.Mapper;
import com.redxun.common.base.db.BaseDao;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* 内部消息查看记录数据库访问层
*/
@Mapper
public interface InfInnerMsgLogMapper extends BaseDao<InfInnerMsgLog> {

    /**
     * 根据msgId与recUserId获取消息记录
     * @param msgId
     * @return
     */
    InfInnerMsgLog getByMsgIdAndRecUserId(@Param("msgId")String msgId, @Param("recUserId")String recUserId);

    /**
     * 根据id更新删除标记
     * @param id
     */
    void updateIsDel(String id);

}
