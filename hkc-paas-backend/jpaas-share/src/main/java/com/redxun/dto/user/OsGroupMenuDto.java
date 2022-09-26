package com.redxun.dto.user;

import lombok.Getter;
import lombok.Setter;

/***
 * 系统用户组菜单
 */
@Getter
@Setter
public class OsGroupMenuDto {

    /**
     * 菜单ID
     */
    private String menuId;

    /**
     * 用户组ID
     */
    private String groupId;

    /**
     * 应用ID
     */
    private String appId;
}
