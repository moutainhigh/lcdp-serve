package com.redxun.common.utils;

import com.redxun.common.sms.SmsHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 短信校验工具类
 */
@Service
public class SmsVerificationUtil {

    private final Map<String, SmsHandler> smsMap = new ConcurrentHashMap<>();

    /**
     * 通过Key获取消息处理器
     * @param key
     * @return
     */
    public SmsHandler getSmsByKey(String key) {
        return smsMap.get(key);
    }
}
