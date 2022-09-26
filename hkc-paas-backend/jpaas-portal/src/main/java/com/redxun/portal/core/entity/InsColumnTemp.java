
/**
 * <pre>
 *
 * 描述：栏目模板实体类定义
 * 表:ins_column_temp
 * 作者：csx
 * 邮箱: csx@redxun.cn
 * 日期:2019-12-26 22:41:59
 * 版权：广州红迅软件
 * </pre>
 */
package com.redxun.portal.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.redxun.common.base.entity.BaseExtEntity;
import com.redxun.log.annotation.FieldDef;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 栏目模板
 */
@Data
@Accessors(chain = true)
@TableName(value = "ins_column_temp")
public class InsColumnTemp  extends BaseExtEntity<java.lang.String> {

    @JsonCreator
    public InsColumnTemp() {
    }

    //ID_
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;

    //名称
    @FieldDef(comment = "名称")
    @TableField(value = "NAME_")
    private String name;
    //编码
    @FieldDef(comment = "栏目别名")
    @TableField(value = "KEY_")
    private String key;
    //模板
    @TableField(value = "TEMPLET_")
    private String templet;
    //是否系统
    @TableField(value = "IS_SYS_")
    private String isSys;
    //状态
    @TableField(value = "STATUS_")
    private String status;
    //类型
    @TableField(value = "TEMP_TYPE_")
    private String tempType;

    @Override
    public String getPkId() {
        return id;
    }

    @Override
    public void setPkId(String pkId) {
        this.id=pkId;
    }
}



