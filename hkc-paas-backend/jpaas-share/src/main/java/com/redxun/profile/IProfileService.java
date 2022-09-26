package com.redxun.profile;

import java.util.Set;

/**
 * 用户组策略接口类
 */
public interface IProfileService {
    /**
     * 类型
     * @return
     */
    ProfileType getType();

    /**
     * 返回用户组策略数据
     * @return
     */
    Set<String> getCurrentProfile();
}
