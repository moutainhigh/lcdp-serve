
/**
 * <pre>
 *
 * 描述：业务实体数据类型实体类定义
 * 表:FORM_ENTITY_DATA_TYPE
 * 作者：hujun
 * 邮箱: hujun@redxun.cn
 * 日期:2021-03-17 14:04:27
 * 版权：广州红迅软件
 * </pre>
 */
package com.redxun.form.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.redxun.common.base.entity.BaseExtEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import java.util.ArrayList;
import java.util.List;


@Setter
@Getter
@Accessors(chain = true)
@TableName(value = "FORM_ENTITY_DATA_TYPE")
public class FormEntityDataType  extends BaseExtEntity<java.lang.String> {

    @JsonCreator
    public FormEntityDataType() {
    }

    //主键
    @TableId(value = "ID_",type = IdType.INPUT)
    private String id;
    //对话框别名
    @TableField(value = "DIALOG_ALIAS_")
    private String dialogAlias;
    //对话框名称
    @TableField(value = "DIALOG_NAME_")
    private String dialogName;
    //类型名称
    @TableField(value = "NAME_")
    private String name;
    //状态
    @TableField(value = "STATUS_")
    private String status;
    //值字段
    @TableField(value = "ID_FIELD_")
    private String idField;
    //文本字段
    @TableField(value = "TEXT_FIELD_")
    private String textField;
    //展示类型
    @TableField(value = "DATA_SHOW_TYPE_")
    private String dataShowType;



    @Override
    public String getPkId() {
        return id;
    }

    @Override
    public void setPkId(String pkId) {
        this.id=pkId;
    }


    /**
     生成子表属性的Array List
     */

}



