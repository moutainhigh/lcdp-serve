<#import "function.ftl" as func>
<#assign package=model.variables.package>
<#assign comment=model.tabComment>
<#assign class=model.variables.class>
<#assign classVar=model.variables.classVar>
<#assign system=vars.system>
<#assign domain=vars.domain>
<#assign pkType=func.getPkType(model)>
<#assign subtables=model.subTableList>
<#assign sub=model.sub>
<#assign subSize=func.hasSubTable(model)>

package ${domain}.${system}.${package}.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import ${domain}.common.base.db.BaseDao;
import ${domain}.common.base.db.BaseService;
import ${domain}.common.tool.IdGenerator;
import ${domain}.common.service.impl.SuperServiceImpl;
import ${domain}.${system}.${package}.entity.${class};
import ${domain}.${system}.${package}.mapper.${class}Mapper;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
<#if (model.sub) >
import ${domain}.${system}.${package}.entity.${model.getMainModel().variables.class};
</#if>
import javax.annotation.Resource;

/**
* [${comment}]业务服务类
*/
@Service
public class ${class}ServiceImpl extends SuperServiceImpl<${class}Mapper, ${class}> implements BaseService<${class}> {

    @Resource
    private ${class}Mapper ${classVar}Mapper;
    <#if (subSize?number  gt 0) >
        <#assign subtables=model.subTableList>
        <#list subtables as subTable>
    @Resource
    private ${subTable.variables.class}ServiceImpl ${subTable.variables.classVar}ServiceImpl;
        </#list>
    </#if>

    @Override
    public BaseDao<${class}> getRepository() {
        return ${classVar}Mapper;
    }

    <#if (model.sub) >

    /**
    根据子表外键删除子表数据
    */
    public void  removeByFk(String pid){
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("${model.getForeignKey()}",pid);
        ${classVar}Mapper.delete(wrapper);
    }

    /**
    插入子表数据，设定ID和外键
    */
    public void addList(${model.getMainModel().variables.class} entity){
        for(${class} ${classVar}:entity.get${class}()){
            ${classVar}.setId(IdGenerator.getIdStr());
            ${classVar}.set${func.convertUnderLine(model.getForeignKey())?cap_first}(entity.getId());
            ${classVar}Mapper.insert(${classVar});
        }
    }

    /**
    根据子表外键获取数据
    */
    public List<${class}> getByFk(String fk){
        QueryWrapper wrapper=new QueryWrapper<${class}>();
        wrapper.eq("${model.getForeignKey()}",fk);
        return ${classVar}Mapper.selectList(wrapper);
    }

    <#elseif (subSize?number  gt 0)>
    <#assign subtables=model.subTableList>

    /**
    当主表数据为新增的情况下，子表也要根据外键同时添加数据
    */
    @Override
    public int insert(${class} entity) {
        if(BeanUtil.isEmpty(entity.getPkId())) {
            entity.setPkId(IdGenerator.getIdStr());
        }
        <#list subtables as subTable>
        ${subTable.variables.classVar}ServiceImpl.addList(entity);
        </#list>
        return getRepository().insert(entity);
    }

    /**
    更新数据的情况下，先根据外键删除子表的数据，然后直接插入新增的数据
    */
    @Override
    public int update(${class} entity) {
        int rtn= ${classVar}Mapper.updateById(entity);
        <#list subtables as subTable>

        ${subTable.variables.classVar}ServiceImpl.removeByFk(entity.getPkId());
        ${subTable.variables.classVar}ServiceImpl.addList(entity);
        </#list>
        return rtn;
    }
</#if>
}
