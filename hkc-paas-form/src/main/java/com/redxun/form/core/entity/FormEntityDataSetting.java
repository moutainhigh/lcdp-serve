
/**
 * <pre>
 *
 * 描述：业务实体数据配置实体类定义
 * 表:FORM_ENTITY_DATA_SETTING
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Setter
@Getter
@Accessors(chain = true)
@TableName(value = "FORM_ENTITY_DATA_SETTING")
public class FormEntityDataSetting  extends BaseExtEntity<java.lang.String> {

    @JsonCreator
    public FormEntityDataSetting() {
    }

    //主键
    @TableId(value = "ID_",type = IdType.INPUT)
    private String id;
    //类型ID
    @TableField(value = "DATA_TYPE_ID_")
    private String dataTypeId;
    //类型名称
    @TableField(value = "DATA_TYPE_NAME_")
    private String dataTypeName;
    //角色ID
    @TableField(value = "ROLE_ID_")
    private String roleId;
    //角色名称
    @TableField(value = "ROLE_NAME_")
    private String roleName;

    @TableField(exist = false)
    private List<FormEntityDataSettingDic> formEntityDataSettingDicList;

    @TableField(exist = false)
    private Map<String,List<FormEntityDataSettingDic>> dataTypeDicMap=new HashMap<>();


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



