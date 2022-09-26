package com.redxun.common.ribbon;

import com.redxun.common.constant.ConfigConstants;
import com.redxun.common.ribbon.config.RuleConfigure;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.netflix.ribbon.RibbonClients;

/**
 * Ribbon扩展配置类
 *
 * @author yjy
 * @date 2018/11/17 9:24
 */
@ConditionalOnProperty(value = ConfigConstants.CONFIG_RIBBON_ISOLATION_ENABLED, havingValue = "true" )
@RibbonClients(defaultConfiguration = {RuleConfigure.class})
public class LbIsolationAutoConfigure {

}
