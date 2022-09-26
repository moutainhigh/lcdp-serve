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
    <rx-dialog @handOk="handleSubmit" @cancel="cancel">
        <rx-layout>
            <div slot="center">
                <a-form-model ref="form"  :model="mdl" :rules="rules" :label-col="labelCol" :wrapper-col="wrapperCol">
                    <#list commonList as col>
                        <#assign colName=func.convertUnderLine(col.columnName)>
                        <#if func.isExcludeField(colName) >
                            <#if colName?if_exists?string=="name">
                                <a-form-model-item  label="${col.comment}" prop = "${colName}">
                                    <a-input placeholder="请输入${col.comment}" v-model="mdl.${colName}"/>
                                </a-form-model-item>
                            <#else>
                                <a-form-model-item  label="${col.comment}" prop = "${colName}">
                                    <#if (col.colType=="java.util.Date")>
                                        <a-date-picker placeholder="请输入${col.comment}" v-model="mdl.${colName}"
                                                       format="YYYY-MM-DD"/>至
                                        <a-date-picker placeholder="请输入${col.comment}" v-model="mdl.${colName}"
                                                       format="YYYY-MM-DD"/>
                                    <#elseif (col.colType=="Integer")>
                                        <a-input-number :min="0" :max="1000000" placeholder="请输入${colName}"
                                                        v-model="mdl.${colName}"/>
                                    <#else>
                                        <a-input placeholder="请输入${col.comment}" v-model="mdl.${colName}"/>
                                    </#if>
                                </a-form-model-item>
                            </#if>
                        </#if>
                    </#list>
                </a-form-model>


                <#if (subSize?number  gt 0) >
                    <#assign subtables=model.subTableList>
                    <#list subtables as subTable>
                        <a-button class="editable-add-btn" @click="handleAdd('${subTable.variables.classVar}')">
                            新增
                        </a-button>
                        <a-table :columns=subTable.${subTable.variables.classVar}.columns :data-source="mdl['${subTable.variables.classVar}']" :pagination = false  bordered>
                            <template
                                    v-for="col in subTable['${subTable.variables.classVar}'].fields"
                                    :slot="col"
                                    slot-scope="text, record, index">
                                <div :key="col">
                                    <a-input
                                            v-if="record.editable"
                                            style="margin: -5px 0"
                                            :value="text"
                                            @change="e => handleChange(e.target.value, record.uid, col,'${subTable.variables.classVar}')"/>
                                    <template v-else>
                                        {{ text }}
                                    </template>
                                </div>
                            </template>
                            <template slot="operation" slot-scope="text, record, index">
                                <div class="editable-row-operations">
                            <span v-if="record.editable">
                                <a @click="saveChild(record.uid,'${subTable.variables.classVar}')">保存</a>
                            </span>
                                    <span v-else class="relick">
                                <a @click="edit(record.uid,'${subTable.variables.classVar}')">编辑</a>
                                <a @click="onDelete(record.uid,'${subTable.variables.classVar}')">删除</a>
                            </span>
                                </div>
                            </template>
                        </a-table>


                    </#list>
                </#if>
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
                /**
                 * 子表列表，以及表格属性
                 */
                subTable:{
                    <#if (subSize?number  gt 0) >
                    <#assign subtables=model.subTableList>
                    <#list subtables as subTable>
                    "${subTable.variables.classVar}": {
                        row:{
            <#list subTable.columnList as col>
            <#assign colName=func.convertUnderLine(col.columnName)>
            <#if func.isExcludeField(colName) >
            <#if colName?if_exists?string!="id" && colName?if_exists?string!="fkid" >
            ${colName}:'',
                </#if>
                </#if>
                </#list>
                editable: true,
        },
            columns:[
                <#list subTable.columnList as col>
                <#assign colName=func.convertUnderLine(col.columnName)>
                <#if func.isExcludeField(colName) >
                {
                    title: '${col.comment}',
                    dataIndex: '${colName}',
                    width: '20%',
                    scopedSlots: { customRender: '${colName}' },
                },
                </#if>
                </#list>
                {
                    title: 'operation',
                    dataIndex: 'operation',
                    scopedSlots: { customRender: 'operation' },
                },
            ],
                fields:[
                <#list subTable.columnList as col>
                <#assign colName=func.convertUnderLine(col.columnName)>
                <#if func.isExcludeField(colName) >
                '${colName}',
                </#if>
                </#list>
            ]
        },
            </#list>
            </#if>
        },
            rules: {
                <#list commonList as col>
                <#assign colName=func.convertUnderLine(col.columnName)>
                <#if func.isExcludeField(colName) >
                <#assign isNotNull=col.getIsNotNull()?string("true","false") >
                ${colName}: [{ required:${isNotNull},message:"请输入${col.comment}", trigger: 'change' }],
                </#if>
                </#list>
            }
        }

        },
        methods: {
            get(id){
                return ${class}Api.get(id);
            },

            save(){
                return ${class}Api.save(this.mdl);
            },

        }
    }
</script>
<style>
    .relick a{
        margin-right: 10px;
    }

</style>
