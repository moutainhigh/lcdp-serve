
/**
 * <pre>
 *
 * 描述：form_pdf_template实体类定义
 * 表:form_pdf_template
 * 作者：hjy
 * 邮箱: hjy@redxun.cn
 * 日期:2020-11-22 15:47:59
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
import com.redxun.log.annotation.FieldDef;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import java.util.ArrayList;
import java.util.List;


@Setter
@Getter
@Accessors(chain = true)
@TableName(value = "form_pdf_template")
public class FormPdfTemplate  extends BaseExtEntity<java.lang.String> {

    @JsonCreator
    public FormPdfTemplate() {
    }

    //主键
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;

    //名称
    @TableField(value = "NAME_")
    private String name;
    //标识
    @TableField(value = "KEY_")
    private String key;
    //分类ID
    @TableField(value = "TREE_ID_")
    private String treeId;
    //业务模型ID
    @TableField(value = "BO_DEF_ID_")
    private String boDefId;
    //表单模板
    @TableField(value = "PDF_HTML_")
    private String pdfHtml;

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


    /**
    生成子表属性的Array List
    */

}



