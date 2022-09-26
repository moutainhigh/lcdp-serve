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
    <div slot="center">
        <a-form-model ref="form" :model="mdl" :rules="rules">
            <a-form-model-item style="display: none">
                <a-input v-model="mdl.${pkVar}"/>
            </a-form-model-item>
            <#list commonList as col>
                <#assign colName=func.convertUnderLine(col.columnName)>
                <#if func.isExcludeField(colName) >
                    <a-row>
                        <a-col :span="24">
                            <a-form-model-item :labelCol="labelCol1" :wrapperCol="wrapperCol1"
                                               label="${col.comment}" prop="${colName}">
                                <#assign isNotNull=col.getIsNotNull()?string("true","false") >
                                <#if (col.colType=="java.util.Date")>
                                    <a-date-picker placeholder="${col.comment}" v-model="mdl.${colName}"
                                                   format="YYYY-MM-DD"/>
                                <#elseif (col.colType=="String" && col.length >=512 )>
                                    <a-textarea placeholder="${col.comment}" v-model="mdl.${colName}"
                                                :rows="4"/>
                                <#elseif (col.colType=="Integer")>
                                    <a-input-number :min="0" :max="1000000" placeholder="${col.comment}"
                                                    v-model="mdl.${colName}"/>
                                <#else>
                                    <a-input placeholder="${col.comment}" v-model="mdl.${colName}"/>
                                </#if>
                            </a-form-model-item>
                        </a-col>
                    </a-row>
                </#if>
            </#list>
        </a-form-model>
    </div>
</template>
<script>
    import ${class}Api from '@/api/${system}/${package}/${classVar}'
    import {BaseFormModel} from 'jpaas-common-lib';

    export default {
        name: '${class}Edit',
        mixins: [BaseFormModel],
        components: {
        },
        props: ['pk'],
        data() {
            return {
                proofTest: true,
                rules: {
            <#list commonList as col>
            <#assign colName=func.convertUnderLine(col.columnName)>
            <#if func.isExcludeField(colName) >
            <#assign isNotNull=col.getIsNotNull()?string("true","false") >
            ${colName}:[{required:${isNotNull}, message: "请输入${col.comment}", trigger: 'change'}],
            </#if>
            </#list>
        }
        }
        },
        created() {
            if (this.pk) {
                this.get();
            }
        },
        methods: {
            get() {
                var self_ = this;
                return ${class}Api.get(this.pk).then(res => {
                    self_.mdl = Object.assign(res.data);
                })
            },
            getData() {
                this.mdl.pkId = this.pk;
                this.onSubmit(this.mdl);
                return this.mdl;
            },
            valid() {
                var self_ = this;
                self_.proofTest = true;
                this.$refs['form'].validate(valid => {
                    if (!valid) {
                        self_.proofTest = false;
                    }
                });
                return {success:self_.proofTest,msg:""};
            }
        }
    }
</script>
