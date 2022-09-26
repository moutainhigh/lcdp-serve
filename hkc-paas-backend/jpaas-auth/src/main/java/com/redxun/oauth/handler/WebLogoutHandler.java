package com.redxun.oauth.handler;

import com.redxun.common.model.JPaasUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Web端退出处理器
 * @author yjy
 * @date 2018/10/17
 */
@Slf4j
@Component
public class WebLogoutHandler extends SecurityContextLogoutHandler {

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        JPaasUser user = (JPaasUser) authentication.getPrincipal();
        String username = user.getUsername();
        log.info("username: {}  is offline now", username);
        super.logout(request,response,authentication);

    }
}
