
/**
 * <pre>
 *
 * 描述：路由类型实体类定义
 * 表:sys_route_type
 * 作者：gjh
 * 邮箱: gjh@redxun.cn
 * 日期:2020-06-04 17:41:33
 * 版权：广州红迅软件
 * </pre>
 */
package com.redxun.system.core.entity;

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

@Setter
@Getter
@Accessors(chain = true)
@TableName(value = "sys_route_type")
public class SysRouteType  extends BaseExtEntity<String> {

    @JsonCreator
    public SysRouteType() {
    }

    //主键
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;

    //路由类型名称
    @FieldDef(comment = "路由类型名称")
    @TableField(value = "ROUTE_TYPE_NAME_")
    private String routeTypeName;
    //描述
    @TableField(value = "DESCRIBE_")
    private String describe;

    @Override
    public String getPkId() {
        return id;
    }

    @Override
    public void setPkId(String pkId) {
        this.id=pkId;
    }
}



