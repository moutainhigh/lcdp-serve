<#import "function.ftl" as func>
<#assign package=model.variables.package>
<#assign class=model.variables.class>
<#assign classVar=model.variables.classVar>
<#assign comment=model.tabComment>
<#assign system=vars.system>
<#assign domain=vars.domain>
<#assign sub=model.sub>
<#assign foreignKey=func.convertUnderLine(model.foreignKey)>
<#assign pkType=func.getPkType(model)>
<#assign fkType=func.getFkType(model)>
package ${domain}.${system}.${package}.mapper;

import ${domain}.${system}.${package}.entity.${class};
import org.apache.ibatis.annotations.Mapper;
import ${domain}.common.base.db.BaseDao;

/**
* ${comment}数据库访问层
*/
@Mapper
public interface ${class}Mapper extends BaseDao<${class}> {

}
