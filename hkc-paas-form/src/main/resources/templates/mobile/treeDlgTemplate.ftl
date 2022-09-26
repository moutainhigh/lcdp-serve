<template>
    <#-- 构建外部传入参数 -->
    <#noparse>
    <#assign query="">
    <#if params??&&(params?size>0)>
    <#assign query="?">
    <#assign  keys=params?keys/>
    <#list keys as key>
    <#if (key_index==0)>
    <#assign query=query + key +"=" + params[key] >
    <#else>
    <#assign query=query +"&" +key +"=" + params[key] >
</#if>
</#list>
</#if>
</#noparse>
        <form-bo-list-tree
            ref="table"
            url="${ctxPath}/api-form/form/core/formBoList/${formBoList.key}/getTreeJson<#noparse>${query}</#noparse>"
            ajax-type="post"
            data-field="result.data"
            :textField="formBoList.textField"
            :idField="formBoList.idField"
            <#if formBoList.parentField??>
                :parentField="formBoList.parentField"
            </#if>
            :onlySelLeaf="'${onlySelLeaf}'=='YES'"
            :multiSelect="formBoList.multiSelect"
            :async="'${formBoList.isLazy}'=='YES'"
            <#if formBoList.isLazy=='YES'>
                :loadByParent="loadTreeByParent"
            </#if>
        />
</template>

<script>
    export default {
        data() {
            name: '${formBoList.key}';
            return {
                formBoList: {
                        name: '${formBoList.name}',
                        idField: '${formBoList.idField}',
                        textField:'${formBoList.textField}',
                        <#if formBoList.parentField??>parentField: '${formBoList.parentField}',</#if>
                        key: '${formBoList.key}',
                        id: '${formBoList.id}',
                        multiSelect:
                        <#noparse>
                        <#if params?? && params.single?? && params.single=="false">
                        ${(params.single=="true")?string('false','true')},
                        <#elseif params?? && params.single=="true"> false,
                        <#else>${formBoList.multiSelect},</#if>
                        </#noparse>
                        isDialog: '${formBoList.isDialog}',
                        isTreeDlg: 'YES'
                }
            }
        },
        methods: {
            <#if bodyScript??>
            ${bodyScript}
            </#if>
        }
    }
</script>

<style scoped>

</style>
