package com.redxun.oauth.service;

import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.model.JPaasUser;

import java.util.function.Consumer;

/**
 * @author ycs
 * @date 2022/06/221
 */
public interface IOAuth2Service {
    /**
     * 修改当前登陆信息
     *
     * @param consumer
     * @return
     */
    JsonResult updateCurrentUser(Consumer<JPaasUser> consumer);

}
