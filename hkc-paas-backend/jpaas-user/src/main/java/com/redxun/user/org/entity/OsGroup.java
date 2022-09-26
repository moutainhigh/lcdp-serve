package com.redxun.user.org.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.redxun.common.base.entity.BaseExtEntity;
import com.redxun.log.annotation.FieldDef;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * 用户组实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("os_group")
public class OsGroup extends BaseExtEntity<String> {
    /**
     * 来自用户类型
     */
    public static String FROM_USER_TYPE="_USER_TYPE";

    public final static String WEIXIN="weixin";
    public final static String DD="dd";

    //单位
    public static Integer TYPE_COMPANY=1;
    //部门
    public static Integer TYPE_DEPARTMENT=2;


    private static final long serialVersionUID = 4497149010220586111L;

    @TableId(value = "GROUP_ID_",type = IdType.INPUT)
    private String groupId;

    @TableField(value = "DIM_ID_")
    private String dimId;
    @FieldDef(comment = "组名")
    @TableField(value = "NAME_")
    private String name;
    @TableField(value = "SHORT_NAME_")
    private String shortName;
    @FieldDef(comment = "编码")
    @TableField(value = "KEY_")
    private String key;
    @TableField(value = "RANK_LEVEL_")
    private Integer rankLevel;
    @TableField(value = "STATUS_")
    private String status;
    @TableField(value = "DESCP_")
    private String descp;
    @TableField(value = "SN_")
    private Integer sn;
    @TableField(value = "PARENT_ID_")
    private String parentId;
    @TableField(value = "PATH_")
    private String path;
    @TableField(value = "AREA_CODE_")
    private String areaCode;
    @TableField(value = "FORM_")
    private String form;
    @TableField(value = "SYNC_WX_")
    private Integer syncWx;
    @TableField(value = "WX_PARENT_PID_")
    private Integer wxParentPid;
    @TableField(value = "WX_PID_")
    private Integer wxPid;
    @TableField(value = "IS_DEFAULT_")
    private String isDefault;
    @TableField(value = "IS_LEAF_")
    private Short isLeaf;
    @TableField(value = "DD_PARENT_ID_")
    private String ddParentId;
    @TableField(value = "DD_ID_")
    private String ddId;

    @TableField(value="IS_MAIN_",exist = false)
    private String isMain;


    @TableField(exist = false)
    private Integer childAmount;
    //用户数量
    @TableField(exist = false)
    private Integer userAmount;
    /**
     *组织类型，1代表公司、2代表部门
     */
    @TableField(value = "TYPE_")
    private Integer type;


    /**
     * 是否公共的组
     */
    @TableField(exist = false)
    private Boolean isPublic=false;


    @Override
    public String getPkId() {
        return groupId;
    }

    @Override
    public void setPkId(String pkId) {
        this.groupId=pkId;
    }
}
