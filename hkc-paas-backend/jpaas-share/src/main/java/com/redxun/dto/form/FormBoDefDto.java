/**
 * <pre>
 *
 * 描述：业务模型实体类定义
 * 表:form_bo_def
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

import java.util.List;

/**
 * BO定义DTO
 */
@Setter
@Getter
@Accessors(chain = true)
@TableName(value = "form_bo_def")
public class FormBoDefDto extends BaseDto {
    //主键
	private String id;
    //名称
    private String name;
    //别名
    private String alias;
    //分类ID
    private String treeId;
    //支持数据库
    private Integer supportDb;
    //描述
    private String description;
    //产生类型，来自数据库或表单
    private String genType="form";
    /**
     * 业务实体定义
     */
    private FormBoEntityDto formBoEntity;
    /**
     * 业务关系
     */
    private List<FormBoRelationDto> relations;
}



