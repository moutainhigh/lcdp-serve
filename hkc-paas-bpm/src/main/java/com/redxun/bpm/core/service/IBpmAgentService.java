package com.redxun.bpm.core.service;

import com.redxun.common.base.entity.JsonResult;

/**
 * 代理人计算接口。
 */
public interface IBpmAgentService {

    /**
     * 根据用户Id和流程定义ID返回代理人。
     * @param userId
     * @param actDefId
     * @return
     */
    JsonResult<String> getAgentUser(String userId, String actDefId);
}
