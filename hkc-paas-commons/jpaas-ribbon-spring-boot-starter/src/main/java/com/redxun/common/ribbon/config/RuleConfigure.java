package com.redxun.common.ribbon.config;

import com.redxun.common.ribbon.rule.CustomIsolationRule;
import com.netflix.loadbalancer.IRule;
import org.springframework.context.annotation.Bean;

/**
 * @author yjy
 * @date 2019/9/3
 */
public class RuleConfigure {
    @Bean
    public IRule isolationRule() {
        return new CustomIsolationRule();
    }
}
