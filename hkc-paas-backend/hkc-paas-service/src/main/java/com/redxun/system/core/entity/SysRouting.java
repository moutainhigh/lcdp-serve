
/**
 * <pre>
 *
 * 描述：路由实体类定义
 * 表:sys_routing
 * 作者：gjh
 * 邮箱: gjh@redxun.cn
 * 日期:2020-05-25 15:51:00
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
@TableName(value = "sys_routing")
public class SysRouting  extends BaseExtEntity<String> {

    @JsonCreator
    public SysRouting() {
    }

    //主键
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;

    //路由名称
    @TableField(value = "ROUTING_NAME_")
    private String routingName;
    //路由类型
    @TableField(value = "ROUTE_TYPE_")
    private String routeType;
    //条件
    @TableField(value = "CONDITION_")
    private String condition;
    //条件参数
    @TableField(value = "CONDITION_PARAMETERS_")
    private String conditionParameters;
    //过滤器
    @TableField(value = "FILTER_")
    private String filter;
    //过滤器参数
    @TableField(value = "FILTER_PARAMETERS_")
    private String filterParameters;
    //uri
    @TableField(value = "URI_")
    private String uri;
    //备注
    @TableField(value = "REMARK_")
    private String remark;
    //状态
    @TableField(value = "STATUS_")
    private String status;

    @FieldDef(comment = "应用ID")
    @TableField(value = "APP_ID_")
    private String appId;

    @Override
    public String getPkId() {
        return id;
    }

    @Override
    public void setPkId(String pkId) {
        this.id=pkId;
    }
}



