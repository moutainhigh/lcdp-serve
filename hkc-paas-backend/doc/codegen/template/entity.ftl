<#import "function.ftl" as func>
<#assign package=model.variables.package>
<#assign class=model.variables.class>
<#assign classVar=model.variables.classVar>
<#assign comment=model.tabComment>
<#assign subtables=model.subTableList>
<#assign pk=func.getPk(model) >
<#assign pkModel=model.pkModel >
<#assign pkVar=func.convertUnderLine(pk) >
<#assign pkType=func.getPkType(model)>
<#assign fkType=func.getFkType(model)>
<#assign system=vars.system>
<#assign domain=vars.domain>
<#assign tableName=model.tableName>
<#assign colList=model.columnList>
<#assign commonList=model.commonList>
<#assign foreignField="">
<#assign subSize=func.hasSubTable(model)>
<#if (model.sub)>
	<#assign foreignField=model.foreignKey?lower_case>
</#if>

/**
 * <pre>
 *
 * 描述：${comment}实体类定义
 * 表:${tableName}
 * 作者：${vars.developer}
 * 邮箱: ${vars.email}
 * 日期:${date?string("yyyy-MM-dd HH:mm:ss")}
 * 版权：${vars.company}
 * </pre>
 */
package ${domain}.${system}.${package}.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonCreator;
import ${domain}.common.base.entity.BaseExtEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import java.util.ArrayList;
import java.util.List;
import org.apache.ibatis.type.JdbcType;


@Setter
@Getter
@Accessors(chain = true)
@TableName(value = "${tableName}")
public class ${class}  extends BaseExtEntity<java.lang.String> {

    @JsonCreator
    public ${class}() {
    }

    //${pkModel.comment}
    @TableId(value = "${pkModel.columnName}",type = IdType.INPUT)
	private ${pkModel.colType} ${func.convertUnderLine(pkModel.columnName)};

    <#list commonList as col>
    <#assign colName=func.convertUnderLine(col.columnName)>
    <#if func.isExcludeField( colName) >
    //${col.comment}
    @TableField(value = "${col.columnName}",jdbcType=JdbcType.${func.getJdbcType(col.colDbType)})
    private ${col.colType} ${colName};
    </#if>
    </#list>

    <#if (model.sub) >
    @TableField(value = "${model.getForeignKey()}",jdbcType=JdbcType.${func.getJdbcType(col.colDbType)})
    private String ${func.convertUnderLine(model.getForeignKey())?cap_first};
    </#if>


    @Override
    public String getPkId() {
        return ${func.convertUnderLine(pkModel.columnName)};
    }

    @Override
    public void setPkId(String pkId) {
        this.${func.convertUnderLine(pkModel.columnName)}=pkId;
    }


    /**
    生成子表属性的Array List
    */
    <#if (subSize?number  gt 0) >
        <#assign subtables=model.subTableList>
        <#list subtables as subTable>
    @TableField(exist = false)
    private List<${subTable.variables.class}> ${subTable.variables.classVar}=new ArrayList<>();
        </#list>
    </#if>

}



