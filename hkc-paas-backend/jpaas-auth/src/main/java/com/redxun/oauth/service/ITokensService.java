package com.redxun.oauth.service;

import com.redxun.common.model.PageResult;
import com.redxun.oauth.model.TokenVo;

import java.util.Map;

/**
 * Redis身份认证服务类
 * @author yjy
 */
public interface ITokensService {
    /**
     * 查询token列表
     * @param params 请求参数
     * @param clientId 应用id
     */
    PageResult<TokenVo> listTokens(Map<String, Object> params, String clientId);
}
