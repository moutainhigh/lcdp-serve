package com.redxun.idempotence;

import com.redxun.common.redis.template.RedisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RedisIdempotence implements Idempotence {
    @Autowired
    private RedisRepository redisRepository;

    @Override
    public boolean check(String idempotenceId) {
        return redisRepository.exists(idempotenceId);
    }

    @Override
    public void record(String idempotenceId) {
        redisRepository.set(idempotenceId,"1");
    }

    @Override
    public void record(String idempotenceId,Integer time) {
        redisRepository.setExpire(idempotenceId,"1",time);
    }

    @Override
    public void delete(String idempotenceId) {
        redisRepository.del(idempotenceId);
    }
}
