
/**
 * <pre>
 *
 * 描述：手机表单实体类定义
 * 表:form_mobile
 * 作者：csx
 * 邮箱: csx@redxun.cn
 * 日期:2020-05-18 09:44:12
 * 版权：广州红迅软件
 * </pre>
 */
package com.redxun.dto.form;

import com.redxun.common.dto.BaseDto;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 * 表单APP单据DTO
 */
@Setter
@Getter
public class FormMobileDto extends BaseDto {
    //主键
	private String id;
    //名称
    private String name;
    //分类ID
    private String categoryId;
    //ALIAS_
    private String alias;
    //表单HTML
    private String formHtml;
    //表单脚本
    private String script;
    //是否发布
    private Integer deployed;
    private String metadata;
    //业务模型ID
    private String bodefId;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FormMobileDto that = (FormMobileDto) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}



