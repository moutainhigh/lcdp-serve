package com.redxun.oauth.service.impl;

import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.constant.SecurityConstants;
import com.redxun.common.model.JPaasUser;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.HttpContextUtil;
import com.redxun.common.utils.TokenUtil;
import com.redxun.oauth.service.IOAuth2Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.function.Consumer;

/**
 *
 *
 * @author ycs
 */
@Slf4j
@Service
public class OAuth2ServiceImpl implements IOAuth2Service {
    @Resource
    TokenStore tokenStore;

    @Override
    public JsonResult updateCurrentUser(Consumer<JPaasUser> consumer) {
        String token= TokenUtil.getToken();
        if(StringUtils.isEmpty(token)){
            HttpServletRequest request = HttpContextUtil.getRequest();
            token = (String)request.getAttribute(SecurityConstants.Authorization);
        }

        OAuth2Authentication authentication = tokenStore.readAuthentication(token);
        OAuth2AccessToken accessToken = tokenStore.getAccessToken(authentication);
        JPaasUser paasUser=(JPaasUser) authentication.getPrincipal();
        consumer.accept(paasUser);
        tokenStore.storeAccessToken(accessToken,authentication);
        return JsonResult.Success("更新用户信息成功!");
    }


}
