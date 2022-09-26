package com.redxun.portal.core.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.redxun.portal.core.entity.InfInnerMsg;
import org.apache.ibatis.annotations.Mapper;
import com.redxun.common.base.db.BaseDao;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
* 内部短消息数据库访问层
*/
@Mapper
public interface InfInnerMsgMapper extends BaseDao<InfInnerMsg> {

    /**
     * 分页查询 个人消息
     * @param page
     * @param params
     * @return
     */
    IPage<InfInnerMsg> queryMsg(IPage<InfInnerMsg> page, @Param("w") Map<String, Object> params,@Param("recType")String recType,@Param("recUserId")String recUserId,@Param("curUserId")String curUserId);

    /**
     * 分页查询 发送的消息
     * @param page
     * @param params
     * @return
     */
    IPage querySentMsg(IPage page,@Param("w") Map<String, Object> params);

    /**
     * 根据接收人与接收人组查询我的消息总数
     * @param recUserId
     * @param groupIds
     * @return
     */
    Integer getCountByRecUserId(@Param("curUserId") String recUserId, @Param("groupIds") List<String> groupIds,@Param("tenantId")String tenantId);

    /**
     * 根据接收人Id及接收人组查询我的消息
     * @param page
     * @param recUserId
     * @param groupIds
     * @return
     */
    IPage getByRecUserId(IPage page,@Param("curUserId") String recUserId,@Param("groupIds") List<String> groupIds);

    /**
     * 更新发送的消息的删除标记
     * @param msgId
     */
    void updateDelFlag(String msgId);
}
