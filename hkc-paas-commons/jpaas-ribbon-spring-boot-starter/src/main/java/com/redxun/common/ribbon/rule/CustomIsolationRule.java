package com.redxun.common.ribbon.rule;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.cloud.nacos.ribbon.NacosServer;
import com.redxun.common.context.LbIsolationContextHolder;
import com.netflix.loadbalancer.*;
import com.redxun.common.tool.constant.CommonConstant;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 自定义隔离随机规则
 *
 * @author yjy
 * @date 2019/9/3
 */
public class CustomIsolationRule extends RoundRobinRule {
    private final static String KEY_DEFAULT = "default";
    /**
     * 优先根据版本号取实例
     */
    @Override
    public Server choose(ILoadBalancer lb, Object key) {
        if (lb == null) {
            return null;
        }
        String developer;
        if (key != null && !KEY_DEFAULT.equals(key)) {
            developer = key.toString();
        } else {
            developer = LbIsolationContextHolder.getDeveloper();
        }

        List<Server> targetList = null;
        List<Server> upList = lb.getReachableServers();
        if (StrUtil.isNotEmpty(developer)) {
            //取指定版本号的实例
            targetList = upList.stream().filter(
                    server -> developer.equals(
                            ((NacosServer) server).getMetadata().get(CommonConstant.DEVELOPER)
                    )
            ).collect(Collectors.toList());
        }

        if (CollUtil.isEmpty(targetList)) {
            //只取无版本号的实例
            targetList = upList.stream().filter(
                    server -> {
                        String metadataVersion = ((NacosServer) server).getMetadata().get(CommonConstant.DEVELOPER);
                        return StrUtil.isEmpty(metadataVersion);
                    }
            ).collect(Collectors.toList());
        }

        if (CollUtil.isNotEmpty(targetList)) {
            return getServer(targetList);
        }
        return super.choose(lb, key);
    }

    /**
     * 随机取一个实例
     */
    private Server getServer(List<Server> upList) {
        int nextInt = RandomUtil.randomInt(upList.size());
        return upList.get(nextInt);
    }
}
