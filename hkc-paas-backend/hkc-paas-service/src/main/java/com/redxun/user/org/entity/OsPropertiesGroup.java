package com.redxun.user.org.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.redxun.common.base.entity.BaseExtEntity;
import com.redxun.log.annotation.FieldDef;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <pre>
 *
 * 描述：自定义属性分组实体类定义
 * 表:os_properties_group
 * 作者：gjh
 * 邮箱: gjh@redxun.cn
 * 日期:2020-08-05 17:29:50
 * 版权：广州红迅软件
 * </pre>
 */
@Setter
@Getter
@Accessors(chain = true)
@TableName(value = "os_properties_group")
public class OsPropertiesGroup extends BaseExtEntity<String> {

    @JsonCreator
    public OsPropertiesGroup() {
    }

    //主键
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;

    //维度ID
    @TableField(value = "DIM_ID_")
    private String dimId;
    //是否允许
    @TableField(value = "ENABLED_")
    private String enabled;
    //分组名称
    @FieldDef(comment = "分组名称")
    @TableField(value = "NAME_")
    private String name;

    //维度ID
    @TableField(exist = false)
    private String dimName="无";

    @TableField(exist = false)
    private List<OsPropertiesDef> osPropertiesDefList;


    @Override
    public String getPkId() {
        return id;
    }

    @Override
    public void setPkId(String pkId) {
        this.id=pkId;
    }



}



