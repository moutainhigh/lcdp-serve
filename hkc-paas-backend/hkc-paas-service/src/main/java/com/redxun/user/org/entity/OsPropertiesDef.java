
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

/**
 * <pre>
 *
 * 描述：自定义属性配置实体类定义
 * 表:os_properties_def
 * 作者：gjh
 * 邮箱: gjh@redxun.cn
 * 日期:2020-08-05 15:52:23
 * 版权：广州红迅软件
 * </pre>
 */
@Setter
@Getter
@Accessors(chain = true)
@TableName(value = "os_properties_def")
public class OsPropertiesDef  extends BaseExtEntity<String> {

    @JsonCreator
    public OsPropertiesDef() {
    }

    //主键
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;

    //控件类型
    @TableField(value = "CTLTYPE_")
    private String ctltype;
    //数据类型

    @TableField(value = "DATA_TYPE_")
    private String dataType;
    //DIM_ID_
    @TableField(value = "DIM_ID_")
    private String dimId;
    //扩展属性
    @TableField(value = "EXT_JSON_")
    private String extJson;
    //分组ID
    @TableField(value = "GROUP_ID_")
    private String groupId;
    //参数名称
    @FieldDef(comment = "参数名称")
    @TableField(value = "NAME_")
    private String name;

    //分组名称
    @TableField(exist = false)
    private String groupName="";
    //扩展属性值Id
    @TableField(exist = false)
    private String propertyId="";
    //扩展属性值
    @TableField(exist = false)
    private Object propertyValue="";
    //是否为组织维度扩展属性
    @TableField(exist = false)
    private boolean isDim=false;

    @Override
    public String getPkId() {
        return id;
    }

    @Override
    public void setPkId(String pkId) {
        this.id=pkId;
    }

    public boolean getIsDim() {
        if("-1".equals(this.dimId)){
            return false;
        }
        return true;
    }

    public void setIsDim() {
        if("-1".equals(this.dimId)){
           this.isDim= false;
        }
        this.isDim=true;
    }

}



