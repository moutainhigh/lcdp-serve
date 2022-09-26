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
<template>
  <rx-dialog @handOk="handleSubmit" @cancel="cancel">
    <a-form :form="form">
      <a-form-item style="display:none">
        <a-input v-decorator="['${pkVar}']"/>
      </a-form-item>	  
	  <#list commonList as col>
		<#assign colName=func.convertUnderLine(col.columnName)>
			<#if func.isExcludeField(colName) >
			  <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="${col.comment}">
				  <#assign isNotNull=col.getIsNotNull()?string("true","false") >
				  <#if (col.colType=="java.util.Date")>
					<a-date-picker placeholder="${col.comment}" v-decorator="['${colName}', {rules: [{required: ${isNotNull}, message: '请输入${col.comment}'}]}]" format="YYYY-MM-DD"/>
				  <#elseif (col.colType=="String" && col.length >=512 )>
                    <a-textarea placeholder="${col.comment}" v-decorator="['${colName}', {rules: [{required: ${isNotNull}, message: '请输入${col.comment}'}]}]" :rows="4" />
                  <#elseif (col.colType=="Integer")>
                    <a-input-number :min="0" :max="1000000" placeholder="${col.comment}" v-decorator="['${colName}', {rules: [{required: ${isNotNull}, message: '请输入${col.comment}'}]}]" />
                  <#else>
					<a-input placeholder="${col.comment}" v-decorator="['${colName}', {rules: [{required: ${isNotNull}, message: '请输入${col.comment}'}]}]"/>
				  </#if>
			  </a-form-item>
		  </#if>
		 </#list>
    </a-form>
  </rx-dialog>
</template>
<script>
  import ${class}Api from '@/api/${system}/${package}/${classVar}'
  import {BaseForm,RxDialog} from 'jpaas-common-lib';
  export default {
    name: '${class}Edit',
    mixins:[BaseForm],
    components: {
      RxDialog,
    },
    methods: {
      get(id){
        return ${class}Api.get(id);
      },
      save(values){
        return ${class}Api.save(values);
      }

    }
  }
</script>
