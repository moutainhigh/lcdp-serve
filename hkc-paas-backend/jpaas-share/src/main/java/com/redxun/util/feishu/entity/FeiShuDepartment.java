package com.redxun.util.feishu.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * 飞书部门实体
 * @author ycs
 * @version V1.0
 * @since 2022-06-13 16:20
 */
@Data
public class FeiShuDepartment {

    /**
     * 部门名称
     */
    private String name;
    /**
     * 父部门的ID,根部门的话传0
     */
    @JSONField(name = "parent_department_id")
    private String parentDepartmentId;

    /**
     * 本部门的自定义部门ID
     */
    @JSONField(name = "department_id")
    private String departmentId;

    /**
     * 部门的open_id
     */
    @JSONField(name = "open_department_id")
    private String openDepartmentId;

    /**
     * 部门主管用户ID
     */
    @JSONField(name = "leader_user_id")
    private String leaderUserId;
    /**
     * 部门群ID
     */
    @JSONField(name = "chat_id")
    private String chatId;
    /**
     * 部门的排序，即部门在其同级部门的展示顺序
     */
    @JSONField(name = "order")
    private String order;
    /**
     *部门单位自定义ID列表，当前只支持一个
     */
    @JSONField(name = "unit_ids")
    private String[] unitIds;

    /**
     * 部门下用户的个数
     */
    @JSONField(name = "member_count")
    private Boolean  memberCount;


}
