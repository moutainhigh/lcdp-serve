
/**
 * <pre>
 *
 * 描述：业务实体数据配置字典实体类定义
 * 表:FORM_ENTITY_DATA_SETTING_DIC
 * 作者：hj
 * 邮箱: hj@redxun.cn
 * 日期:2021-03-19 14:31:00
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
@TableName(value = "FORM_ENTITY_DATA_SETTING_DIC")
public class FormEntityDataSettingDic  extends BaseExtEntity<java.lang.String> {

    @JsonCreator
    public FormEntityDataSettingDic() {
    }

    //主键
    @TableId(value = "ID_",type = IdType.INPUT)
    private String id;

    //值内容
    @TableField(value = "ID_VALUE_")
    private String idValue;
    //配置ID
    @TableField(value = "SETTING_ID_")
    private String settingId;
    //文本内容
    @TableField(value = "TEXT_VALUE_")
    private String textValue;
    //父内容
    @TableField(value = "PARENT_VALUE_")
    private String parentValue;
    //路径
    @TableField(value = "PATH_")
    private String path;
    //序号
    @TableField(value = "SN_")
    private Integer sn;



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



