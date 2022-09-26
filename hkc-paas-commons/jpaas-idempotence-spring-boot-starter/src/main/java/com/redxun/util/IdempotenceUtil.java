package com.redxun.util;

import com.redxun.common.redis.template.RedisRepository;
import com.redxun.common.tool.Base64Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * 幂等工具类
 */
@Component
public class IdempotenceUtil {
    @Autowired
    private RedisRepository redisRepository;
    /**
     * 生成幂等号
     * @return
     */
    public String generateId() {
        String uuid = UUID.randomUUID().toString();
        String uId=Base64Util.encode(uuid).toLowerCase();
        redisRepository.setExpire(uId,"1",1800);
        return uId;
    }

    /**
     * 从Header里面获取幂等号
     * @return
     */
    public String getHeaderIdempotenceId(){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String idempotenceId=request.getHeader("idempotenceId");
        return idempotenceId;
    }
}
