
/**
 * <pre>
 *
 * 描述：业务实体实体类定义
 * 表:form_bo_relation
 * 作者：csx
 * 邮箱: csx@redxun.cn
 * 日期:2020-01-12 17:03:17
 * 版权：广州红迅软件
 * </pre>
 */
package com.redxun.dto.form;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.redxun.common.base.entity.BaseExtEntity;
import com.redxun.common.dto.BaseDto;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 业务对象关系
 */
@Setter
@Getter
@Accessors(chain = true)
@TableName(value = "form_bo_relation")
public class FormBoRelationDto extends BaseDto {
    //主键
	private String id;
    //业务模型定义
    private String bodefId;
    //实体ID
    private String entId;
    //父实体ID
    private String parentEntId;
    //关系类型(onetoone,onetomany)
    private String type;
    //是否引用
    private Integer isRef=0;
    //关联字段
    private String fkField;
}



