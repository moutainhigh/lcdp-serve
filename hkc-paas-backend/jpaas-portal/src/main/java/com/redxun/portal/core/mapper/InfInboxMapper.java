package com.redxun.portal.core.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.redxun.portal.core.entity.InfInbox;
import org.apache.ibatis.annotations.Mapper;
import com.redxun.common.base.db.BaseDao;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
* 内部短消息收件箱数据库访问层
*/
@Mapper
public interface InfInboxMapper extends BaseDao<InfInbox> {

    /**
     * 根据msgId与recUserId获取内部短消息收件箱
     * @param msgId
     * @return
     */
    InfInbox getByMsgIdAndUserId(@Param("msgId")String msgId,@Param("recUserId")String recUserId,@Param("recType")String recType);

    /**
     * 根据发信人id获取内部短消息列表
     * @param page
     * @param params
     * @param senderId
     * @return
     */
    IPage<InfInbox> query(IPage<InfInbox> page, @Param("w") Map<String, Object> params,@Param("senderId")String senderId);

    /**
     * 根据msgId获取列表数据
     * @param msgId
     * @return
     */
    List<InfInbox> getByMsgId(@Param("msgId")String msgId);
}
