package com.redxun.util.feishu.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * 飞书创建部门参数
 * @author ycs
 * @version V1.0
 * @since 2022-06-13 16:20
 */
@Data
public class FeiShuDepartmentsEditReq {
    /**
     * 部门名称
     */
    @JSONField(name = "name")
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
     * 部门主管用户ID
     */
    @JSONField(name = "leader_user_id")
    private String leaderUserId;

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

    @JSONField(name = "create_group_chat")
    private Boolean  createGroupChat;

}
