package com.redxun.common.utils;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author yjy
 * @date 2019/9/8
 */
@Slf4j
public class AddrUtil {
    private final static String UNKNOWN_STR = "unknown";
    /**
     * 获取客户端IP地址
     */
    public static String getRemoteAddr() {
        HttpServletRequest request= HttpContextUtil.getRequest();
        return request.getRemoteAddr();
    }

    private static boolean isEmptyIP(String ip) {
        return StrUtil.isEmpty(ip) || UNKNOWN_STR.equalsIgnoreCase(ip);
    }

    /**
     * 获取本机的IP地址
     */
    public static String getLocalAddr() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.error("InetAddress.getLocalHost()-error", e);
        }
        return "";
    }
}
