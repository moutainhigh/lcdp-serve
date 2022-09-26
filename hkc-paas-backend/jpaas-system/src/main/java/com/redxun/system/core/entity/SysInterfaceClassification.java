
/**
 * <pre>
 *
 * 描述：接口分类表实体类定义
 * 表:SYS_INTERFACE_CLASSIFICATION
 * 作者：ray
 * 邮箱: ray@redxun.cn
 * 日期:2021-05-18 15:34:23
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
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import java.util.ArrayList;
import java.util.List;


@Setter
@Getter
@Accessors(chain = true)
@TableName(value = "SYS_INTERFACE_CLASS")
public class SysInterfaceClassification  extends BaseExtEntity<String> {

    @JsonCreator
    public SysInterfaceClassification() {
    }

    //分类ID
    @TableId(value = "CLASSIFICATION_ID_",type = IdType.INPUT)
	private String classificationId;

    //分类名称
    @TableField(value = "CLASSIFICATION_NAME_")
    private String classificationName;
    //描述
    @TableField(value = "DESCRIPTION_")
    private String description;
    //项目ID
    @TableField(value = "PROJECT_ID_")
    private String projectId;



    @Override
    public String getPkId() {
        return classificationId;
    }

    @Override
    public void setPkId(String pkId) {
        this.classificationId=pkId;
    }


    /**
    生成子表属性的Array List
    */

}



