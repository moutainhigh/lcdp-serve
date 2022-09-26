package com.redxun.common.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 *  用户组菜单
 *
 * @author zfh
 */
@Setter
@Getter
public class GroupMenuDto implements Serializable {

    /**
     * 菜单ID
     */
    private String menuId;
    /**
     * 组ID
     */
    private String groupId;


}
