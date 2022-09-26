package com.redxun.dto.user;

import lombok.Data;
import com.redxun.common.dto.BaseDto;

import java.util.Objects;

@Data
public class OsGroupDto extends BaseDto{

    private String groupId;
    private String dimId;
    private String name;
    private String key;
    private Integer rankLevel;
    private String status;
    private String descp;
    private Integer sn;
    private String parentId;
    private String path;
    private String areaCode;
    private String form;
    private Integer syncWx;
    private Integer wxParentPid;
    private Integer wxPid;
    private String isDefault;
    private Short isLeaf;
    private String isMain;
    //OA考勤同步微信信息，需要获取打卡记录的用户ID
    private String cardNumber;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OsGroupDto dto = (OsGroupDto) o;
        return Objects.equals(groupId, dto.groupId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupId);
    }
}
