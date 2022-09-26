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
    <div style="padding: revert;height: 100%">
        <div class="DataList">
            <div class="DataList-head">
                <div @click="dataHead('tree')">
                    <form-bo-list-tab-tree ref="tree" :mobileTreeTab="mobileConf.mobileTreeTab" query="<#noparse>${query}</#noparse>" @search="search"/>
                </div>
                <div @click="dataHead('classify')" v-show="mobileConf.mobileIsShowView=='YES'">
                    <form-bo-list-classify ref="classify" :boListId="formBoList.id" @search="search" />
                </div>
                <div @click="dataHead('search')" v-show="mobileConf.mobileIsSearchView=='YES'">
                    <form-bo-list-search ref="search" :mobileSearch="mobileConf.mobileSearch" @search="search" />
                </div>
            </div>
            <form-bo-list-table :isDialog="formBoList.isDialog"
                                :stylize="mobileConf.mobileStyle"
                                :showPage="${(formBoList.isPage=='YES')?string('true','false')}"
                                :defaultPageSize="${formBoList.pageSize}"
                                :loadDataOnstart="loadDataOnstart"
                                :queryParam="queryParam"
                                :columns="mobileConf.mobileCols"
                                :mobileButton="mobileConf.mobileButton"
                                :idField="formBoList.idField"
                                data-field="result.data"
                                :parentField="formBoList.parentField"
                                url="${ctxPath}/api-form/form/core/formBoList/${formBoList.key}/getData<#noparse>${query}</#noparse>"
                                ref="table"
                                :multiSelect="formBoList.multiSelect"
            />
        </div>
        <div @click="close" class="DataList-shade" v-if="show"></div>
        <form-bo-list-button :mobileButton="mobileConf.mobileButton" />
    </div>
</template>

<script>
    export default {
        name: "${formBoList.key}",
        data(){
            return{
                rootVm:true,
                show:false,
                loadDataOnstart:${(formBoList.isInitData?? && formBoList.isInitData=='YES')?string('true','false')},
                formBoList: {
                    formAlias: '${formAlias}',
                    formAddAlias: '${formAddAlias}',
                    formDetailAlias: '${formDetailAlias}',
                    name: '${formBoList.name}',
                    idField: '${formBoList.idField}',
                    textField:'${formBoList.textField}',
                    <#if formBoList.parentField??>parentField: '${formBoList.parentField}',</#if>
                    key: '${formBoList.key}',
                    id: '${formBoList.id}',
                    multiSelect:<#noparse><#if params ?? && params.single ?? && params.single == "false" >${(params.single=="true")?string('false','true')},<#elseif params ?? && params.single == "true" >false,<#else>${formBoList.multiSelect},</#if></#noparse>
                    isDialog: '${formBoList.isDialog}',
                    isTreeDlg:'${isTreeDlg}'
                },
                mobileConf:{
                    mobileStyle:${mobileStyle},
                    mobileCols:${mobileCols},
                    mobileTreeTab:<#if mobileTreeTab??>${mobileTreeTab}<#else>[]</#if>,
                    mobileSearch:${mobileSearch},
                    mobileIsSearchView:"${mobileIsSearchView}",
                    mobileIsShowView:"${mobileIsShowView}",
                    mobileIsViewHeader:"${mobileIsViewHeader}",
                    mobileView:${mobileView},
                    mobileButton:${mobileButton}
                }
            }
        },
        methods:{
            ${mobileJs}
        }
    }
</script>

<style scoped>
    .rx-fieid>div:nth-child(2){
        text-align:left !important;
    }
    .DataList{
        display: flex;
        flex-direction: column;
        height: 100%;
    }
    .DataList-head{
        display: flex;
        background: white;
        position: relative;
        z-index: 2;
    }
    .DataList-head >div{
        flex: 1;
    }
    .custom-dialog-determine {
        display: flex;
        height: 3rem;
        background: white;
    }

    .custom-dialog-determine>div:nth-child(1) {
        text-align: center;
        background-color: #2e8cff;
        border-radius: 4px;
        margin-left: 1rem;
        height: 2rem;
        line-height: 2rem;
        margin-top: .5rem;
        color: white;
        margin-right: .5rem;
        flex: 1;
    }

    .custom-dialog-determine>div:nth-child(2) {
        text-align: center;
        background-color: #f5f7fa;
        border-radius: 4px;
        height: 2rem;
        line-height: 2rem;
        margin-top: .5rem;
        margin-right: .5rem;
        color: #555555;
        border: solid 1px #dadde0;
        flex: 1;
    }
    .DataList-shade {
        position: fixed;
        top: 0px;
        left: 0px;
        z-index: 1;
        height: 100%;
        width: 100%;
        background: rgba(0, 0, 0, 0.2);
    }
</style>