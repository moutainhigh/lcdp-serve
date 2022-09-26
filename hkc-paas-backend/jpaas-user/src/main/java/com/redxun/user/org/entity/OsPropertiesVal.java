package com.redxun.user.org.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.redxun.common.base.entity.BaseExtEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <pre>
 *
 * 描述：扩展属性值实体类定义
 * 表:os_properties_val
 * 作者：gjh
 * 邮箱: gjh@redxun.cn
 * 日期:2020-08-06 17:49:55
 * 版权：广州红迅软件
 * </pre>
 */
@Setter
@Getter
@Accessors(chain = true)
@TableName(value = "os_properties_val")
public class OsPropertiesVal  extends BaseExtEntity<String> {

    @JsonCreator
    public OsPropertiesVal() {
    }

    //主键
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;

    //DATA_VAL
    @TableField(value = "DATE_VAL")
    private java.util.Date dateVal;
    //DIM_ID_
    @TableField(value = "DIM_ID_")
    private String dimId;
    //分组ID
    @TableField(value = "GROUP_ID_")
    private String groupId;
    //数据值
    @TableField(value = "NUM_VAL_")
    private Double numVal;
    //所有者
    @TableField(value = "OWNER_ID_")
    private String ownerId;
    //属性ID
    @TableField(value = "PROPERY_ID_")
    private String properyId;
    //TXT_VAL
    @TableField(value = "TXT_VAL")
    private String txtVal;

    @Override
    public String getPkId() {
        return id;
    }

    @Override
    public void setPkId(String pkId) {
        this.id=pkId;
    }
}



