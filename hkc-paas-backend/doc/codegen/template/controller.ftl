<#import "function.ftl" as func>
<#assign package=model.variables.package>
<#assign class=model.variables.class>
<#assign classVar=model.variables.classVar>
<#assign comment=model.tabComment>
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
<#assign subSize=func.hasSubTable(model)>

package ${domain}.${system}.${package}.controller;

import com.redxun.common.base.db.BaseService;
import ${domain}.${system}.${package}.entity.${class};
import ${domain}.${system}.${package}.service.${class}ServiceImpl;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import ${domain}.common.base.entity.JsonResult;
import ${domain}.common.tool.StringUtils;
import ${domain}.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.redxun.common.annotation.ClassDefine;
import java.util.Arrays;
import java.util.List;
<#if (subSize?number  gt 0) >
    <#assign subtables=model.subTableList>
    <#list subtables as subTable>
        import ${domain}.${system}.${package}.entity.${subTable.variables.class};
        import ${domain}.${system}.${package}.service.${subTable.variables.class}ServiceImpl;
    </#list>
</#if>

@Slf4j
@RestController
@RequestMapping("/${system}/${package}/${classVar}")
@Api(tags = "${comment}")
@ClassDefine(title = "${comment}",alias = "${class}Controller",path = "/${system}/${package}/${classVar}",packages = "${package}",packageName = "子系统名称")

public class ${class}Controller extends BaseController<${class}> {

@Autowired
${class}ServiceImpl ${classVar}Service;

<#if (subSize?number  gt 0) >
    <#assign subtables=model.subTableList>
    <#list subtables as subTable>
        @Autowired
        ${subTable.variables.class}ServiceImpl ${subTable.variables.classVar}ServiceImpl;
    </#list>
</#if>

@Override
public BaseService getBaseService() {
return ${classVar}Service;
}

@Override
public String getComment() {
return "${comment}";
}

<#if (subSize?number  gt 0) >
    <#assign subtables=model.subTableList>


    /**
    *删除主表数据的时候，同时删除子表数据
    */

    @Override
    @ApiOperation(value="删除实体信息", notes="根据实体Id删除实体信息,parameters is {ids:'1,2'}")
    @PostMapping("del")
    public JsonResult del(@RequestParam String ids){

    if(StringUtils.isEmpty(ids)){
    return new JsonResult(false,"");
    }
    String[] aryId=ids.split(",");
    List list= Arrays.asList(aryId);

    ${class} ent= ${classVar}Service.get(ids);
    <#list subtables as subTable>
        List<${subTable.variables.class}> ${subTable.variables.classVar}list = ${subTable.variables.classVar}ServiceImpl.getByFk(ids);
        ent.set${subTable.variables.class}(${subTable.variables.classVar}list);

        for(${subTable.variables.class} ${subTable.variables.classVar}:ent.get${subTable.variables.class}()){
        ${subTable.variables.classVar}ServiceImpl.removeByFk(ent.getPkId());
        }
    </#list>
    JsonResult result=JsonResult.getSuccessResult("删除"+getComment()+"成功!");

    getBaseService().delete(list);
    return result;
    }

</#if>
<#if (subSize?number  gt 0) >
    <#assign subtables=model.subTableList>

    /**
    * 根据主键查询记录详细信息,同时根据外键调出子表信息
    * @param pkId
    * @return
    */
    @ApiOperation(value="查看单条记录信息", notes="根据主键查询记录详细信息")
    @GetMapping("/get")
    @Override
    public JsonResult<${class}> get(@RequestParam(value="pkId") String pkId){
    JsonResult result=JsonResult.Success();
    result.setShow(false);
    if(ObjectUtils.isEmpty(pkId)){
    return result.setData(new Object());
    }
    ${class} ent= ${classVar}Service.get(pkId);

    <#list subtables as subTable>
        List<${subTable.variables.class}> ${subTable.variables.classVar} = ${subTable.variables.classVar}ServiceImpl.getByFk(pkId);
        ent.set${subTable.variables.class}(${subTable.variables.classVar});
    </#list>
    handleData(ent);

    return result.setData(ent);
    }
</#if>
}

