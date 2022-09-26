package com.redxun.dto.user;

import com.redxun.common.dto.BaseDto;
import lombok.Data;

@Data
public class OsGradeRoleDto extends BaseDto {
    private String id;
    private String adminId;
    private String groupId;
    private String name;
}
