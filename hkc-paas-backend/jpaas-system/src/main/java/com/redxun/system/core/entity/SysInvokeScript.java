
/**
 * <pre>
 *
 * 描述：脚本调用实体类定义
 * 表:sys_invoke_script
 * 作者：csx
 * 邮箱: csx@redxun.cn
 * 日期:2020-06-24 11:43:03
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
@TableName(value = "sys_invoke_script")
public class SysInvokeScript  extends BaseExtEntity<String> {

    @JsonCreator
    public SysInvokeScript() {
    }

    //主键
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;

    //分类ID
    @TableField(value = "TREE_ID_")
    private String treeId;
    //名称
    @FieldDef(comment = "名称")
    @TableField(value = "NAME_")
    private String name;
    //别名
    @FieldDef(comment = "别名")
    @TableField(value = "ALIAS_")
    private String alias;
    //参数定义
    @TableField(value = "PARAMS_")
    private String params;
    //脚本内容
    @TableField(value = "CONTENT_")
    private String content;
    //备注
    @TableField(value = "DESCP_")
    private String descp;

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



