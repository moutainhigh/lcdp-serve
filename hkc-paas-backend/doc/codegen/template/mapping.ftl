<#import "function.ftl" as func>
<#assign package=model.variables.package>
<#assign class=model.variables.class>
<#assign system=vars.system>
<#assign domain=vars.domain>
<#assign type="${domain}."+system+"."+ package +".entity." +class>
<#assign tableName=model.tableName>
<#assign foreignKey=model.foreignKey>
<#assign sub=model.sub>
<#assign colList=model.columnList>
<#assign commonList=model.commonList>
<#assign pk=func.getPk(model) >
<#assign pkModel=model.pkModel >
<#assign pkVar=func.getPkVar(model) >
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${domain}.${system}.${package}.mapper.${class}Mapper">

    <resultMap id="${class}" type="${type}">
        <#list colList as col>
            <#assign colName=func.convertUnderLine(col.columnName)>
            <#if (col.isPK) >
                <id property="${colName}" column="${col.columnName}" jdbcType="${func.getJdbcType(col.colDbType)}"/>
            </#if>
        </#list>
        <#list colList as col>
            <#assign colName=func.convertUnderLine(col.columnName)>
            <#if (!col.isPK) >
                <result property="${colName}" column="${col.columnName}" jdbcType="${func.getJdbcType(col.colDbType)}"/>
            </#if>
        </#list>
    </resultMap>


    <select id="query" resultType="${domain}.${system}.${package}.entity.${class}" parameterType="java.util.Map">
        select <#list colList as col>${col.columnName}<#if col_has_next>,</#if></#list> from ${tableName}
        <#noparse>
        <where>
            <if test="@rx.Ognl@isNotEmpty(w.whereSql)">
                ${w.whereSql}
            </if>
        </where>
        <if test="@rx.Ognl@isNotEmpty(w.orderBySql)">
            ORDER BY ${w.orderBySql}
        </if>
        <if test="@rx.Ognl@isEmpty(w.orderBySql)"></#noparse>
            ORDER BY  ${pk} DESC
        </if>
    </select>

</mapper>


