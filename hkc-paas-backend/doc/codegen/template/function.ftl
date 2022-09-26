<#--获取查询数据类型-->
<#function getDataType colType start>
<#if (colType=="long") > <#return "L">
<#elseif (colType=="int")><#return "N">
<#elseif (colType=="double")><#return "BD">
<#elseif (colType=="Short")><#return "SN">
<#elseif (colType=="Date" && start=="1")><#return "DL">
<#elseif (colType=="Date" && start=="0")><#return "DG">
<#else><#return "SL"></#if>
</#function>

<#--将字符串 user_id 转换为 类似userId-->
<#function convertUnderLine field>
<#if field?index_of("_")==-1>
<#assign rtn>${field?lower_case?trim}</#assign>
<#return rtn>
</#if>

<#if field?index_of("F_")==0>
	<#assign rtn><#list (field?substring(2))?split("_") as x><#if (x_index==0)><#if x?length==1>${x?upper_case?trim}<#else>${x?lower_case?trim}</#if><#else>${x?lower_case?cap_first?trim}</#if></#list></#assign>
	<#return rtn>
</#if>

<#assign rtn><#list field?split("_") as x><#if (x_index==0)><#if x?length==1>${x?upper_case?trim}<#else>${x?lower_case?trim}</#if><#else>${x?lower_case?cap_first?trim}</#if></#list></#assign>
 <#return rtn>
</#function>


<#--判断是否有子表-->
<#function hasSubTable model>
<#assign subtables=model.subTableList>
<#assign rtn><#if (subtables?exists && subtables?size!=0)>1<#else>0</#if></#assign>
 <#return rtn>
</#function>

<#function getPk model>
<#assign rtn><#if (model.pkModel??) >${model.pkModel.columnName}<#else>id</#if></#assign>
 <#return rtn>
</#function>

<#--获取主键类型-->
<#function getPkType model>
<#list model.columnList as col>
<#if col.isPK>
<#if (col.colType=="Integer")><#assign rtn>"Long"</#assign><#return rtn>
<#else><#assign pkType=col.colType ></#if>
</#if>
</#list>
<#assign rtn>${pkType}</#assign>
<#return rtn>
</#function>

<#--获取外键类型 没有则返回Long-->
<#function getFkType model>
<#assign fk=model.foreignKey>
<#list model.columnList as col>
<#if (col.columnName?lower_case)==(fk?lower_case)>
	<#if (col.colType=="Integer")><#assign rtn>Long</#assign><#return rtn><#else><#assign rtn>${col.colType}</#assign><#return rtn></#if>
</#if>
</#list>
<#assign rtn>Long</#assign><#return rtn>
</#function>

<#function getPkVar model>
<#assign rtn><#if (model.pkModel??) ><#noparse>${</#noparse>${convertUnderLine(model.pkModel.columnName)}<#noparse>}</#noparse><#else>id</#if></#assign>
 <#return rtn>
</#function>


<#function getJdbcType dataType>
<#assign dbtype=dataType?lower_case>
<#assign rtn>
<#if  dbtype?ends_with("int") || (dbtype=="double") || (dbtype=="float") || (dbtype=="decimal") || dbtype?ends_with("number")||dbtype?starts_with("numeric") >
NUMERIC
<#elseif (dbtype?index_of("char")>-1)  >
VARCHAR
<#elseif (dbtype=="date")>
DATE
<#elseif (dbtype?index_of("timestamp")>-1)  || (dbtype=="datetime") >
TIMESTAMP
<#elseif (dbtype?ends_with("text") || dbtype?ends_with("clob")) >
VARCHAR
</#if></#assign>
 <#return rtn?trim>
</#function>

<#--是否为需要排除的列-->
<#function isExcludeField colName>
<#if colName!="createBy" && colName!="createTime" && colName!="updateBy" && colName!="updateTime" && colName!="tenantId" && colName!="createDepId" >
	<#return true>
<#else>
	<#return false>
</#if>
</#function>




<#function getField col dbType>
    <#assign rtn>
         <#if (col.columnName=="COMPANY_ID_")><#noparse> <#if </#noparse>${col.columnName}<#noparse>?? > </#noparse><#noparse> <#if </#noparse>${col.columnName}!='0'<#noparse>></#noparse><#noparse>'${</#noparse> ${col.columnName}}'<#noparse><#else></#noparse><#noparse>'0'</#if></#noparse><#noparse><#else>'0'</#if></#noparse><#elseif (col.colType=="String")><#noparse > <#if </#noparse> ${col.columnName}<#noparse >??>'</#noparse><#if dbType=="oracle"><#if col.colDbType=="text" || col.colDbType=="longtext"><#noparse >'<#else>''</#if></#noparse><#else><#noparse >${</#noparse>${col.columnName}}'<#noparse ><#else>''</#if></#noparse></#if><#elseif dbType=="mysql"><#if col.colDbType=="text" || col.colDbType=="longtext"><#noparse >${</#noparse> ${col.columnName}<#noparse >?replace("\n","\\n")?replace("\"","\\\"")?replace("'","''")}'</#noparse><#noparse ><#else>null</#if></#noparse><#else><#noparse >${</#noparse>${col.columnName}}'<#noparse ><#else>null</#if></#noparse></#if></#if><#elseif (col.colType=="Integer")><#noparse > <#if </#noparse> ${col.columnName}<#noparse >??>${</#noparse> ${col.columnName}}<#noparse ><#else>null</#if></#noparse><#else><#noparse > <#if </#noparse> ${col.columnName}<#noparse >??></#noparse><#if dbType=="oracle" && col.colDbType=="datetime"><#noparse >TO_DATE('${</#noparse> ${col.columnName}}', 'yyyy-MM-dd HH24:mi:ss')<#else><#noparse >'${</#noparse> ${col.columnName}}'</#if><#noparse ><#else>null</#if></#noparse></#if></#assign>
    <#return rtn>
</#function>

<#function replaceContent text>
    <#assign rtn>text?replace("\n","\\n")?replace("\"","\\\"")</#assign>
    <#return rtn>
</#function>

<#function replaceColByOracle col colIndex>
    <#assign rtn><#if col.colDbType=="text" || col.colDbType=="longtext"> content_${colIndex} CLOB:='<#noparse >${ </#noparse>${col.columnName}<#noparse > ?replace("\n","")?replace("\\\"","\"")?replace("'","''")?replace("&&","' || '&&' || '")?replace("&amp;&amp;","' || '&&' || '")?replace("&lt;","<")?replace("&gt;",">")}';</#noparse></#if></#assign>
    <#return rtn>
</#function>

<#function updateColByOracle model pkModel col colIndex>
    <#assign rtn>
    <#if col.colDbType=="text" || col.colDbType=="longtext">
    update ${model.tableName} set ${col.columnName}=content_${colIndex} where ${pkModel.columnName}='<#noparse >${ </#noparse>${pkModel.columnName}<#noparse >}';</#noparse></#if></#assign>
<#return rtn>
</#function>


<#--text类型字段是否为需要转换-->
<#function isreplaceAndUpdate columnList>
    <#assign isTrue=false />
    <#list columnList as col>
        <#if col.colDbType=="text" || col.colDbType=="longtext">
        <#assign isTrue=true />
        <#break>
        </#if>
    </#list>
    <#return isTrue>
</#function>


<#function replaceMysql content>
    <#assign content=content.replace("\n","\\n")?replace("\"","\\\"")?replace("'","''") />
    <#return content>
</#function>

<#function replaceOracle content>
    <#assign content=content?replace("\n","")?replace("\\\"","\"")?replace("'","''") />
    <#return content>
</#function>












