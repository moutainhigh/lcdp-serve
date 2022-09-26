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
<#assign subSize=func.hasSubTable(model)>

<template>
    <rx-dialog  @cancel="cancel">
        <rx-layout>
            <div slot="center">
                <a-form-model ref="form"  :model="mdl"  :label-col="labelCol" :wrapper-col="wrapperCol">
                    <#list commonList as col>
                        <#assign colName=func.convertUnderLine(col.columnName)>
                        <#if func.isExcludeField(colName) >
                            <a-form-model-item  label="${col.comment}" >
                                <span  v-html="mdl.${colName}"></span>
                            </a-form-model-item>
                        </#if>
                    </#list>
                </a-form-model>
            </div>
        </rx-layout>
    </rx-dialog>
</template>
<script>
    import ${class}Api from '@/api/${system}/${package}/${classVar}'
    import {RxDialog,BaseFormModel} from 'jpaas-common-lib';

    export default {
        name: '${class}Edit',
        mixins:[BaseFormModel],
        components: {
            RxDialog,
        },
        data(){
            return {
            }
        },
        methods: {
            get(id){
                return ${class}Api.get(id);
            },
        }
    }
</script>
<style>
    .relick a{
        margin-right: 10px;
    }

</style>
