<#import "function.ftl" as func>
<resources xmlns="http://www.redxun.cn/sqlgen"
xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
xsi:schemaLocation="http://www.redxun.cn/sqlgen sqlgen.xsd"
filePath="d:\init.sql" >
<#assign dbType=vars.dbType>


<#list models as model>
<resource>
        <#assign pkModel=model.pkModel >
        <sql>SELECT * FROM ${model.tableName}</sql>
        <template><![CDATA[<#noparse ><#setting number_format="#"></#noparse>INSERT INTO ${model.tableName}(<#list model.columnList as col><#if col_has_next>${col.columnName},<#else>${col.columnName}</#if></#list>)
        values(<#list model.columnList as col><#if col_has_next>${func.getField(col dbType)},<#else>${func.getField(col dbType)}</#if></#list>);
        <#if dbType=="oracle" && func.isreplaceAndUpdate(model.columnList)>

        DECLARE
        <#list model.columnList as col><#if col_has_next>${func.replaceColByOracle(col col_index)}<#else>${func.replaceColByOracle(col col_index)}</#if></#list>
        BEGIN
        <#list model.columnList as col><#if col_has_next>${func.updateColByOracle(model pkModel col col_index)}<#else>${func.updateColByOracle(model pkModel col col_index)}</#if></#list>
        commit;
        end ;
        /
        </#if>
        ]]>
        </template>
</resource>
</#list>

</resources>
