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
    <rx-layout class="rxLayout">
        <#if (tabTreeColumns?size > 0)>。
        <div slot="left" width="300"  >
            <a-tabs v-model="activeKey">
                <#list tabTreeColumns as tab>
                <a-tab-pane forceRender tab="${tab.treeId}" key="${tab.tabName}" :closable="false">
                    <rx-tree ref="${tab.tabName}"
                             url="${ctxPath}/api-form/form/core/formBoList/${tab.tabName}/getTreeJson<#noparse>${query}</#noparse>"
                             :showTreeIcon="false"
                             ajax-type="post"
                             data-field="result.data"
                             :async="'${tab.isLazy}'=='YES'"
                             <#if tab.isLazy=='YES'>
                             :loadByParent="loadTreeByParent"
                             </#if>
                             text-field="${tab.textField}"
                             id-field="${tab.idField}"
                             <#if tab.parentField??>
                                parent-field="${tab.parentField}"
                             </#if>
                             :expandOnLoad="true"
                            <#if tab.onlySelLeaf??>
                                :onlySelLeaf="'${tab.onlySelLeaf}'=='YES'"
                            </#if>
                             @select="${tab.onnodeclick}"
                    />
                </a-tab-pane>
                </#list>
            </a-tabs>
        </div>
        </#if>
		<#if formBoList.isSearchView=="YES">
            <div v-if="formBoList.isShowView=='YES'" slot="header" class="search-btn-box">
                <ul class="search-btn-ul">
                    <li key="all" @click="handSearchClick('all')" :class="{active:searchCommonActive == 'all' ? true:false}">全部</li>
                    <li v-for="(item,index) of [...searchCommonList,...searchUserList]" :key="item.key" @click="handSearchClick(item.key)" :class="{active:searchCommonActive == item.key ? true:false}">
                        <a-dropdown v-if="item.viewType=='USER'" :trigger="['contextmenu']">
                                        <span>
                                          {{item.label}}
                                       </span>
                            <a-menu slot="overlay">
                                <a-menu-item key="edit" @click="saveEdit(item.key)">
                                    编辑
                                </a-menu-item>
                                <a-menu-item key="del" @click="itemClose(item.key)">
                                    删除
                                </a-menu-item>
                            </a-menu>
                        </a-dropdown>
                        <span v-else>{{item.label}}</span>
                    </li>
                </ul>
            </div>
        </#if>
        <div slot="center" style="">
            <rx-fit :isheader="isheader" :ref="'rxfit_'+formBoList.key">
                <div slot="toolheader" foldbtn="false" border="false">
                    <div class="list-toolbar">
                        <div class="table-operator">
                            <rx-buttons :btns="topButtonColumns" :btnSize="<#if formBoList.buttonMax??>${formBoList.buttonMax}<#else>1</#if>"
                                        :custom="true" :btnRight="btnRight"  :hasAllRight="hasAllRight"
                            ></rx-buttons>
                        </div>
                        <span class="search-btn-box">
                                    <#if formBoList.isSearchView=="NO">
                                        <span class="rx-dropdown-span">
                                            <a-dropdown v-if="formBoList.isShowView=='YES'" v-model="visible" :getPopupContainer="p=>p.parentNode">
                                                <span class="rx-ant-dropdown-text">
                                                    <span class="ant-dropdown-link">{{queryStrategy}}</span>
                                                    <my-icon :type="'iconicon-test40'"></my-icon>
                                                </span>
                                                <a-menu slot="overlay" @click="handSearchCommonClick">
                                                    <template v-if="searchCommonList.length == 0">
                                                        <a-menu-item class="rx-dropdown-titile-disabled" :disabled="true">系统视图</a-menu-item>
                                                        <a-menu-item key="all" :class="{'rx-dropdown-active':activeAll}">全部</a-menu-item>
                                                    </template>

                                                    <template v-if="searchCommonList.length > 0">
                                                        <a-menu-item class="rx-dropdown-titile-disabled" :disabled="true">系统视图</a-menu-item>
                                                        <a-menu-item key="all" :class="{'rx-dropdown-active':activeAll}">全部</a-menu-item>
                                                        <a-menu-item :key="item.key" v-for="item of searchCommonList" :class="{'rx-dropdown-item-common':true,'rx-dropdown-active':item.active}">
                                                            <div class="rx-itme">
                                                                <span class="rx-text">{{ item.label }}</span>
                                                            </div>
                                                        </a-menu-item>
                                                    </template>
                                                    <template v-if="searchUserList.length > 0">
                                                         <a-menu-item class="rx-dropdown-titile-disabled" :disabled="true">个人视图</a-menu-item>
                                                         <a-menu-item :key="item.key" v-for="item of searchUserList" :class="{'rx-dropdown-item-user':true,'rx-dropdown-active':item.active}">
                                                            <div class="rx-itme">
                                                                <span class="rx-text">{{ item.label }}</span>
                                                                <span v-if="item.viewType=='USER'" class="rx-edit">
                                                                    <span class="rx-edit-icon" @click.stop="saveEdit(item.key)" title="编辑"><my-icon type="iconanniu_bianji_20px"></my-icon></span>
                                                                    <span class="rx-edit-icon del" @click.stop="itemClose(item.key)" title="删除"><my-icon type="icontrash"></my-icon></span>
                                                                </span>
                                                            </div>
                                                        </a-menu-item>
                                                    </template>
                                                </a-menu>
                                            </a-dropdown>
                                        </span>
                                    </#if>

                                    <span v-if="formBoList.isShowView=='YES'"  style="display: inline-block;">
                                        <rx-popover :comboBox="{'width':'700px'}">
                                            <span slot="title"><a-icon type="eye" />查询视图</span>
                                            <div slot="content">
                                                <div class="list-search">
                                                    <search-view-content ref="findContent" :quickSearchData="quickSearchData" />
                                                    <div class="list-search-toolbar">
                                                        <a-button @click="listResetSearch()">重置</a-button>
                                                        <a-button @click="saves()" type="primary">保存</a-button>
                                                        <a-button @click="listSearchAll()" type="primary">搜索</a-button>
                                                    </div>
                                                </div>
                                            </div>
                                        </rx-popover>
                                    </span>
                                    <span v-if="formBoList.isFieldShow=='YES'" class="listSearch" @click="openFieldVisiBle">
                                        <a-icon type="setting" /> 字段显藏
                                    </span>
                                    <#if (searchColumns?size > 0)>
                                    <#-- <span class="search-btn" @click.stop="searchShow" v-if="!isheader && formBoList.isShowSearch=='YES'">
                                         <a-icon type="search"/>搜索
                                     </span>-->
                                        <rx-popover :comboBox="{'width':'700px'}">
                                            <span slot="title"><a-icon type="search"/>搜索</span>
                                            <div slot="content">
                                                <div class="list-search">
                                                    <a-form :layout="islayout?'vertical':'inline'">
                                                        <a-row :gutter="24">
                                                        <#list searchColumns as search>
                                                            <#if (search.type=='query' && search_index<formBoList.searchMax)>
                                                                <a-col :span="12">
                                                                    <a-form-item label="${search.fieldLabel}">
                                                                        <#if (!search.fc?? || search.fc=='textbox')>
                                                                            <a-input <#if search.emptytext??>placeholder="${search.emptytext}"</#if>
                                                                                    style="width: 100%"
                                                                                     v-model.trim="queryParam['<#if search.autoFilter=='YES'>Q_<#if search.tablePre?? && search.tablePre!="">${search.tablePre}.</#if>${search.fieldName}_S_${search.fieldOp}<#else>${search.fieldName}</#if>']"/>
                                                                        <#elseif (search.fc=='datepicker')>
                                                                            <#if search.format??>
                                                                                <#assign searchDateFormat=search.format>
                                                                            <#else>
                                                                                <#assign searchDateFormat='YYYY-MM-DD'>
                                                                            </#if>
                                                                            <a-date-picker <#if search.emptytext??>placeholder="${search.emptytext}"</#if>
                                                                                           v-model="queryParam['queryField_${search.fieldName}']"
                                                                                           format="${searchDateFormat}"
                                                                                    <#if searchDateFormat=='YYYY-MM-DD HH:mm:ss'>
                                                                                        show-time
                                                                                    </#if>
                                                                                           @change="searchDateChange($event,$event,{queryField:'<#if search.autoFilter=='YES'>Q_<#if search.tablePre?? && search.tablePre!="">${search.tablePre}.</#if>${search.fieldName}_D_${search.fieldOp}<#else>${search.fieldName}</#if>',format:'${searchDateFormat}'})"/>
                                                                        <#elseif (search.fc=='month')>
                                                                            <a-month-picker  v-model="queryParam['queryField_${search.fieldName}']" <#if search.emptytext??>placeholder="${search.emptytext}"</#if>  @change="searchMonthChange($event,$event,{queryField:'<#if search.tablePre?? && search.tablePre!="">${search.tablePre}.</#if>${search.fieldName}'<#if search.format??>,format:'${search.format}'</#if>,autoFilter:'${search.autoFilter}'})" />
                                                                        <#elseif (search.fc=='rangepicker')>
                                                                            <#if search.format??>
                                                                                <#assign searchRangeFormat=search.format>
                                                                            <#else>
                                                                                <#assign searchRangeFormat='YYYY-MM-DD HH:mm:ss'>
                                                                            </#if>
                                                                            <a-range-picker  ref="queryField_${search.fieldName}"  v-model="params['queryField_${search.fieldName}']" <#if search.emptytext??>placeholder="${search.emptytext}"</#if> show-time format="${searchRangeFormat}"  @change="searchRangeDateChange($event,'${searchRangeFormat}',{queryField:'<#if search.tablePre?? && search.tablePre!="">${search.tablePre}.</#if>${search.fieldName}',autoFilter:'${search.autoFilter}'})" />
                                                                        <#elseif (search.fc=='select')>
                                                                            <rx-select ref="Q_<#if search.tablePre?? && search.tablePre!="">${search.tablePre}_</#if>${search.fieldName}_S_${search.fieldOp}" v-model="queryParam['<#if search.autoFilter=='YES'>Q_<#if search.tablePre?? && search.tablePre!="">${search.tablePre}.</#if>${search.fieldName}_S_${search.fieldOp}<#else>${search.fieldName}</#if>']"
                                                                                       :multiValue="true" :labelInValue="false" :multiSelect="<#if search.multiSelect??>${search.multiSelect?c}<#else>false</#if>"
                                                                                       <#if (!search.from?? || search.from=='self')>:dataSource="selectOptions${search_index}" value-field="key" text-field="name"
                                                                                       <#elseif (search.from=='url')>url="${search.url}" value-field="${search.url_valuefield}" text-field="${search.url_textfield}"
                                                                                       <#elseif (search.from=='dic')>url="/api-system/system/core/sysDic/treeByKey?key=${search.dicKey}" value-field="value" text-field="name"
                                                                                       <#elseif (search.from=='sql')>url="/api-form/form/core/formCustomQuery/queryForJson_${search.sql}" data-field="data" value-field="${search.sql_valuefield}" text-field="${search.sql_textfield}"
                                                                                       @focus="selectFocus('Q_<#if search.tablePre?? && search.tablePre!="">${search.tablePre}_</#if>${search.fieldName}_S_${search.fieldOp}',selectParams${search_index})" :loadDataOnstart="true"
                                                                                       @onChange="searchChange('${search.fieldName}','Q_<#if search.tablePre?? && search.tablePre!="">${search.tablePre}_</#if>${search.fieldName}_S_${search.fieldOp}')" ajaxType="post"
                                                                                       <#elseif (search.from=='grant')>url="/api-form/form/core/formEntityDataSetting/queryData_${search.grant}" data-field="data"
                                                                                       @focus="selectFocus('Q_<#if search.tablePre?? && search.tablePre!="">${search.tablePre}_</#if>${search.fieldName}_S_${search.fieldOp}')" :loadDataOnstart="true"
                                                                                    </#if>/>
                                                                        <#elseif (search.fc=='treeselect')>
                                                                            <rx-tree-select-ctl ref="Q_<#if search.tablePre?? && search.tablePre!="">${search.tablePre}_</#if>${search.fieldName}_S_${search.fieldOp}" v-model="queryParam['<#if search.autoFilter=='YES'>Q_<#if search.tablePre?? && search.tablePre!="">${search.tablePre}.</#if>${search.fieldName}_S_${search.fieldOp}<#else>${search.fieldName}</#if>']"
                                                                                                :multiValue="true" :labelInValue="false" :multiSelect="<#if search.multiSelect??>${search.multiSelect?c}<#else>false</#if>" :showSearch="<#if search.showSearch??>${search.showSearch?c}<#else>false</#if>"
                                                                                                <#if (!search.from?? || search.from=='self')>:dataSource="selectOptions${search_index}" value-field="key" text-field="name"
                                                                                                <#elseif (search.from=='url')>url="${search.url}" value-field="${search.url_valuefield}" text-field="${search.url_textfield}" parent-field="${search.url_parentfield}"
                                                                                                <#elseif (search.from=='dic')>url="/api-system/system/core/sysDic/treeByKey?key=${search.dicKey}" value-field="value" text-field="name" :toTree="false"
                                                                                                <#elseif (search.from=='sql')>url="/api-form/form/core/formCustomQuery/queryForJson_${search.sql}" data-field="data" value-field="${search.sql_valuefield}" text-field="${search.sql_textfield}" parent-field="${search.sql_parentfield}"
                                                                                                @focus="selectFocus('Q_<#if search.tablePre?? && search.tablePre!="">${search.tablePre}_</#if>${search.fieldName}_S_${search.fieldOp}',selectParams${search_index})" :loadDataOnstart="true"
                                                                                                @onChange="searchChange('${search.fieldName}','Q_<#if search.tablePre?? && search.tablePre!="">${search.tablePre}_</#if>${search.fieldName}_S_${search.fieldOp}')" ajaxType="post"
                                                                                                <#elseif (search.from=='grant')>url="/api-form/form/core/formEntityDataSetting/queryData_${search.grant}" data-field="data"
                                                                                                @focus="selectFocus('Q_<#if search.tablePre?? && search.tablePre!="">${search.tablePre}_</#if>${search.fieldName}_S_${search.fieldOp}')" :loadDataOnstart="true"
                                                                                    </#if>/>
                                                                        <#elseif (search.fc=='dialog')>
                                                                            <rx-input-button v-model="params['<#if search.autoFilter=='YES'>Q_<#if search.tablePre?? && search.tablePre!="">${search.tablePre}.</#if>${search.fieldName}_S_${search.fieldOp}<#else>${search.fieldName}</#if>']" :multiValue="true"
                                                                                             :single="false" :config="{fieldName:'${search.fieldName}',queryField:'<#if search.autoFilter=='YES'>Q_<#if search.tablePre?? && search.tablePre!="">${search.tablePre}.</#if>${search.fieldName}_S_${search.fieldOp}<#else>${search.fieldName}</#if>',dialog:'<#if search.dialog??>${search.dialog}</#if>',dialog_name:'<#if search.dialog_name??>${search.dialog_name}</#if>',valueField:'<#if search.valueField??>${search.valueField}</#if>',textField:'<#if search.textField??>${search.textField}</#if>',single:!<#if search.multiSelect??>${search.multiSelect?c}<#else>true</#if>}"
                                                                                             @click="_OnDialogShow"/>
                                                                        </#if>
                                                                    </a-form-item>
                                                                </a-col>
                                                            </#if>
                                                        </#list>

                                                            <#list searchColumns as search>
                                                                <#if (search.type=='query' && search_index>=formBoList.searchMax)>
                                                                    <a-col :span="12">
                                                                        <a-form-item label="${search.fieldLabel}">
                                                                            <#if (!search.fc?? || search.fc=='textbox')>
                                                                                <a-input <#if search.emptytext??>placeholder="${search.emptytext}"</#if>
                                                                                         :allowClear="true"
                                                                                         style="width: 100%"
                                                                                         v-model.trim="queryParam['<#if search.autoFilter=='YES'>Q_<#if search.tablePre?? && search.tablePre!="">${search.tablePre}.</#if>${search.fieldName}_S_${search.fieldOp}<#else>${search.fieldName}</#if>']"/>
                                                                            <#elseif (search.fc=='datepicker')>
                                                                                <#if search.format??>
                                                                                    <#assign searchDateFormat=search.format>
                                                                                <#else>
                                                                                    <#assign searchDateFormat='YYYY-MM-DD'>
                                                                                </#if>
                                                                                <a-date-picker <#if search.emptytext??>placeholder="${search.emptytext}"</#if>
                                                                                               v-model="queryParam['queryField_${search.fieldName}']"
                                                                                               format="${searchDateFormat}"
                                                                                        <#if searchDateFormat=='YYYY-MM-DD HH:mm:ss'>
                                                                                            show-time
                                                                                        </#if>
                                                                                               @change="searchDateChange($event,$event,{queryField:'<#if search.autoFilter=='YES'>Q_<#if search.tablePre?? && search.tablePre!="">${search.tablePre}.</#if>${search.fieldName}_D_${search.fieldOp}<#else>${search.fieldName}</#if>',format:'${searchDateFormat}'})"/>
                                                                            <#elseif (search.fc=='month')>
                                                                                <a-month-picker  v-model="queryParam['queryField_${search.fieldName}']" <#if search.emptytext??>placeholder="${search.emptytext}"</#if>  @change="searchMonthChange($event,$event,{queryField:'<#if search.tablePre?? && search.tablePre!="">${search.tablePre}.</#if>${search.fieldName}'<#if search.format??>,format:'${search.format}'</#if>,autoFilter:'${search.autoFilter}'})" />
                                                                            <#elseif (search.fc=='rangepicker')>
                                                                                <#if search.format??>
                                                                                    <#assign searchRangeFormat=search.format>
                                                                                <#else>
                                                                                    <#assign searchRangeFormat='YYYY-MM-DD HH:mm:ss'>
                                                                                </#if>
                                                                                <a-range-picker ref="queryField_${search.fieldName}"  v-model="params['queryField_${search.fieldName}']" <#if search.emptytext??>placeholder="${search.emptytext}"</#if> show-time format="${searchRangeFormat}"  @change="searchRangeDateChange($event,'${searchRangeFormat}',{queryField:'<#if search.tablePre?? && search.tablePre!="">${search.tablePre}.</#if>${search.fieldName}',autoFilter:'${search.autoFilter}'})" />
                                                                            <#elseif (search.fc=='select')>
                                                                                <rx-select ref="Q_<#if search.tablePre?? && search.tablePre!="">${search.tablePre}_</#if>${search.fieldName}_S_${search.fieldOp}" v-model="queryParam['<#if search.autoFilter=='YES'>Q_<#if search.tablePre?? && search.tablePre!="">${search.tablePre}.</#if>${search.fieldName}_S_${search.fieldOp}<#else>${search.fieldName}</#if>']"
                                                                                           :multiValue="true" :labelInValue="false" :multiSelect="<#if search.multiSelect??>${search.multiSelect?c}<#else>false</#if>"
                                                                                           <#if (!search.from?? || search.from=='self')>:dataSource="selectOptions${search_index}" value-field="key" text-field="name"
                                                                                           <#elseif (search.from=='url')>url="${search.url}" value-field="${search.url_valuefield}" text-field="${search.url_textfield}"
                                                                                           <#elseif (search.from=='dic')>url="/api-system/system/core/sysDic/treeByKey?key=${search.dicKey}" value-field="value" text-field="name"
                                                                                           <#elseif (search.from=='sql')>url="/api-form/form/core/formCustomQuery/queryForJson_${search.sql}" data-field="data" value-field="${search.sql_valuefield}" text-field="${search.sql_textfield}"
                                                                                           @focus="selectFocus('Q_<#if search.tablePre?? && search.tablePre!="">${search.tablePre}_</#if>${search.fieldName}_S_${search.fieldOp}',selectParams${search_index})" :loadDataOnstart="true"
                                                                                           @onChange="searchChange('${search.fieldName}','Q_<#if search.tablePre?? && search.tablePre!="">${search.tablePre}_</#if>${search.fieldName}_S_${search.fieldOp}')" ajaxType="post"
                                                                                           <#elseif (search.from=='grant')>url="/api-form/form/core/formEntityDataSetting/queryData_${search.grant}" data-field="data"
                                                                                           @focus="selectFocus('Q_<#if search.tablePre?? && search.tablePre!="">${search.tablePre}_</#if>${search.fieldName}_S_${search.fieldOp}')" :loadDataOnstart="true"
                                                                                        </#if>/>
                                                                            <#elseif (search.fc=='treeselect')>
                                                                                <rx-tree-select-ctl ref="Q_<#if search.tablePre?? && search.tablePre!="">${search.tablePre}_</#if>${search.fieldName}_S_${search.fieldOp}" v-model="queryParam['<#if search.autoFilter=='YES'>Q_<#if search.tablePre?? && search.tablePre!="">${search.tablePre}.</#if>${search.fieldName}_S_${search.fieldOp}<#else>${search.fieldName}</#if>']"
                                                                                                    :multiValue="true" :labelInValue="false" :multiSelect="<#if search.multiSelect??>${search.multiSelect?c}<#else>false</#if>" :showSearch="<#if search.showSearch??>${search.showSearch?c}<#else>false</#if>"
                                                                                                    <#if (!search.from?? || search.from=='self')>:dataSource="selectOptions${search_index}" value-field="key" text-field="name"
                                                                                                    <#elseif (search.from=='url')>url="${search.url}" value-field="${search.url_valuefield}" text-field="${search.url_textfield}" parent-field="${search.url_parentfield}"
                                                                                                    <#elseif (search.from=='dic')>url="/api-system/system/core/sysDic/treeByKey?key=${search.dicKey}" value-field="value" text-field="name" :toTree="false"
                                                                                                    <#elseif (search.from=='sql')>url="/api-form/form/core/formCustomQuery/queryForJson_${search.sql}" data-field="data" value-field="${search.sql_valuefield}" text-field="${search.sql_textfield}" parent-field="${search.sql_parentfield}"
                                                                                                    @focus="selectFocus('Q_<#if search.tablePre?? && search.tablePre!="">${search.tablePre}_</#if>${search.fieldName}_S_${search.fieldOp}',selectParams${search_index})" :loadDataOnstart="true"
                                                                                                    @onChange="searchChange('${search.fieldName}','Q_<#if search.tablePre?? && search.tablePre!="">${search.tablePre}_</#if>${search.fieldName}_S_${search.fieldOp}')" ajaxType="post"
                                                                                                    <#elseif (search.from=='grant')>url="/api-form/form/core/formEntityDataSetting/queryData_${search.grant}" data-field="data"
                                                                                                    @focus="selectFocus('Q_<#if search.tablePre?? && search.tablePre!="">${search.tablePre}_</#if>${search.fieldName}_S_${search.fieldOp}')" :loadDataOnstart="true"
                                                                                        </#if>/>
                                                                            <#elseif (search.fc=='dialog')>
                                                                                <rx-input-button v-model="params['<#if search.autoFilter=='YES'>Q_<#if search.tablePre?? && search.tablePre!="">${search.tablePre}.</#if>${search.fieldName}_S_${search.fieldOp}<#else>${search.fieldName}</#if>']" :multiValue="true"
                                                                                                 :single="false" :config="{fieldName:'${search.fieldName}',queryField:'<#if search.autoFilter=='YES'>Q_<#if search.tablePre?? && search.tablePre!="">${search.tablePre}.</#if>${search.fieldName}_S_${search.fieldOp}<#else>${search.fieldName}</#if>',dialog:'<#if search.dialog??>${search.dialog}</#if>',dialog_name:'<#if search.dialog_name??>${search.dialog_name}</#if>',valueField:'<#if search.valueField??>${search.valueField}</#if>',textField:'<#if search.textField??>${search.textField}</#if>',single:!<#if search.multiSelect??>${search.multiSelect?c}<#else>true</#if>}"
                                                                                                 @click="_OnDialogShow"/>
                                                                            </#if>
                                                                        </a-form-item>
                                                                    </a-col>
                                                                </#if>
                                                            </#list>

                                                        </a-row>
                                                    </a-form>
                                                    <div class="list-search-toolbar">
                                                        <#if (searchColumns?size > 0)>
                                                            <a-button type="primary" @click="searchAll" icon="search">查询</a-button>
                                                            <a-button style="margin-left: 4px" @click="resetSearch" >重置</a-button>
                                                        </#if>
                                                    </div>
                                                </div>
                                            </div>
                                        </rx-popover>
                                    </#if>
                                </span>
                    </div>
                </div>
        <!--点击搜索的弹框end-->

        <!--点击 过滤 时 的弹框-->
                <div slot="searchbar" btnalign="right" v-if="fitSearch" @search="searchAll" @cancel="resetSearch" @close="closeSearchbar">
                    <a-form :layout="islayout?'vertical':'inline'">
                        <a-row :gutter="24">
                            <#list searchColumns as search>
                                <#if (search.type=='query' && search_index<formBoList.searchMax)>
                                    <a-col :span="colSpan">
                                        <a-form-item label="${search.fieldLabel}">
                                            <#if (!search.fc?? || search.fc=='textbox')>
                                                <a-input <#if search.emptytext??>placeholder="${search.emptytext}"</#if>
                                                         style="width: 100%"
                                                         v-model.trim="queryParam['<#if search.autoFilter=='YES'>Q_<#if search.tablePre?? && search.tablePre!="">${search.tablePre}.</#if>${search.fieldName}_S_${search.fieldOp}<#else>${search.fieldName}</#if>']"/>
                                            <#elseif (search.fc=='datepicker')>
                                                <#if search.format??>
                                                    <#assign searchDateFormat=search.format>
                                                <#else>
                                                    <#assign searchDateFormat='YYYY-MM-DD'>
                                                </#if>
                                                <a-date-picker <#if search.emptytext??>placeholder="${search.emptytext}"</#if>
                                                               v-model="queryParam['queryField_${search.fieldName}']"
                                                               format="${searchDateFormat}"
                                                        <#if searchDateFormat=='YYYY-MM-DD HH:mm:ss'>
                                                            show-time
                                                        </#if>
                                                               @change="searchDateChange($event,$event,{queryField:'<#if search.autoFilter=='YES'>Q_<#if search.tablePre?? && search.tablePre!="">${search.tablePre}.</#if>${search.fieldName}_D_${search.fieldOp}<#else>${search.fieldName}</#if>',format:'${searchDateFormat}'})"/>
                                            <#elseif (search.fc=='month')>
                                                <a-month-picker  v-model="queryParam['queryField_${search.fieldName}']" <#if search.emptytext??>placeholder="${search.emptytext}"</#if>  @change="searchMonthChange($event,$event,{queryField:'<#if search.tablePre?? && search.tablePre!="">${search.tablePre}.</#if>${search.fieldName}'<#if search.format??>,format:'${search.format}'</#if>,autoFilter:'${search.autoFilter}'})" />
                                            <#elseif (search.fc=='rangepicker')>
                                                <#if search.format??>
                                                    <#assign searchRangeFormat=search.format>
                                                <#else>
                                                    <#assign searchRangeFormat='YYYY-MM-DD HH:mm:ss'>
                                                </#if>
                                                <a-range-picker  ref="queryField_${search.fieldName}"  v-model="params['queryField_${search.fieldName}']" <#if search.emptytext??>placeholder="${search.emptytext}"</#if> show-time format="${searchRangeFormat}"  @change="searchRangeDateChange($event,'${searchRangeFormat}',{queryField:'<#if search.tablePre?? && search.tablePre!="">${search.tablePre}.</#if>${search.fieldName}',autoFilter:'${search.autoFilter}'})" />
                                            <#elseif (search.fc=='select')>
                                                <rx-select ref="Q_<#if search.tablePre?? && search.tablePre!="">${search.tablePre}_</#if>${search.fieldName}_S_${search.fieldOp}" v-model="queryParam['<#if search.autoFilter=='YES'>Q_<#if search.tablePre?? && search.tablePre!="">${search.tablePre}.</#if>${search.fieldName}_S_${search.fieldOp}<#else>${search.fieldName}</#if>']"
                                                           :multiValue="true" :labelInValue="false" :multiSelect="<#if search.multiSelect??>${search.multiSelect?c}<#else>false</#if>"
                                                           <#if (!search.from?? || search.from=='self')>:dataSource="selectOptions${search_index}" value-field="key" text-field="name"
                                                           <#elseif (search.from=='url')>url="${search.url}" value-field="${search.url_valuefield}" text-field="${search.url_textfield}"
                                                           <#elseif (search.from=='dic')>url="/api-system/system/core/sysDic/treeByKey?key=${search.dicKey}" value-field="value" text-field="name"
                                                           <#elseif (search.from=='sql')>url="/api-form/form/core/formCustomQuery/queryForJson_${search.sql}" data-field="data" value-field="${search.sql_valuefield}" text-field="${search.sql_textfield}"
                                                           @focus="selectFocus('Q_<#if search.tablePre?? && search.tablePre!="">${search.tablePre}_</#if>${search.fieldName}_S_${search.fieldOp}',selectParams${search_index})" :loadDataOnstart="true"
                                                           @onChange="searchChange('${search.fieldName}','Q_<#if search.tablePre?? && search.tablePre!="">${search.tablePre}_</#if>${search.fieldName}_S_${search.fieldOp}')" ajaxType="post"
                                                           <#elseif (search.from=='grant')>url="/api-form/form/core/formEntityDataSetting/queryData_${search.grant}" data-field="data"
                                                           @focus="selectFocus('Q_<#if search.tablePre?? && search.tablePre!="">${search.tablePre}_</#if>${search.fieldName}_S_${search.fieldOp}')" :loadDataOnstart="true"
                                                        </#if>/>
                                            <#elseif (search.fc=='treeselect')>
                                                <rx-tree-select-ctl ref="Q_<#if search.tablePre?? && search.tablePre!="">${search.tablePre}_</#if>${search.fieldName}_S_${search.fieldOp}" v-model="queryParam['<#if search.autoFilter=='YES'>Q_<#if search.tablePre?? && search.tablePre!="">${search.tablePre}.</#if>${search.fieldName}_S_${search.fieldOp}<#else>${search.fieldName}</#if>']"
                                                                    :multiValue="true" :labelInValue="false" :multiSelect="<#if search.multiSelect??>${search.multiSelect?c}<#else>false</#if>" :showSearch="<#if search.showSearch??>${search.showSearch?c}<#else>false</#if>"
                                                                    <#if (!search.from?? || search.from=='self')>:dataSource="selectOptions${search_index}" value-field="key" text-field="name"
                                                                    <#elseif (search.from=='url')>url="${search.url}" value-field="${search.url_valuefield}" text-field="${search.url_textfield}" parent-field="${search.url_parentfield}"
                                                                    <#elseif (search.from=='dic')>url="/api-system/system/core/sysDic/treeByKey?key=${search.dicKey}" value-field="value" text-field="name" :toTree="false"
                                                                    <#elseif (search.from=='sql')>url="/api-form/form/core/formCustomQuery/queryForJson_${search.sql}" data-field="data" value-field="${search.sql_valuefield}" text-field="${search.sql_textfield}" parent-field="${search.sql_parentfield}"
                                                                    @focus="selectFocus('Q_<#if search.tablePre?? && search.tablePre!="">${search.tablePre}_</#if>${search.fieldName}_S_${search.fieldOp}',selectParams${search_index})" :loadDataOnstart="true"
                                                                    @onChange="searchChange('${search.fieldName}','Q_<#if search.tablePre?? && search.tablePre!="">${search.tablePre}_</#if>${search.fieldName}_S_${search.fieldOp}')" ajaxType="post"
                                                                    <#elseif (search.from=='grant')>url="/api-form/form/core/formEntityDataSetting/queryData_${search.grant}" data-field="data"
                                                                    @focus="selectFocus('Q_<#if search.tablePre?? && search.tablePre!="">${search.tablePre}_</#if>${search.fieldName}_S_${search.fieldOp}')" :loadDataOnstart="true"
                                                        </#if>/>
                                            <#elseif (search.fc=='dialog')>
                                                <rx-input-button v-model="params['<#if search.autoFilter=='YES'>Q_<#if search.tablePre?? && search.tablePre!="">${search.tablePre}.</#if>${search.fieldName}_S_${search.fieldOp}<#else>${search.fieldName}</#if>']" :multiValue="true"
                                                                 :single="false" :config="{fieldName:'${search.fieldName}',queryField:'<#if search.autoFilter=='YES'>Q_<#if search.tablePre?? && search.tablePre!="">${search.tablePre}.</#if>${search.fieldName}_S_${search.fieldOp}<#else>${search.fieldName}</#if>',dialog:'<#if search.dialog??>${search.dialog}</#if>',dialog_name:'<#if search.dialog_name??>${search.dialog_name}</#if>',valueField:'<#if search.valueField??>${search.valueField}</#if>',textField:'<#if search.textField??>${search.textField}</#if>',single:!<#if search.multiSelect??>${search.multiSelect?c}<#else>true</#if>}"
                                                                 @click="_OnDialogShow"/>
                                            </#if>
                                        </a-form-item>
                                    </a-col>
                                </#if>
                            </#list>
                            <#if (searchColumns?size > 0)>
                                <a-col :span="colSpan" v-if="isheader">
                                        <span class="table-page-search-submitButtons">
                                            <a-button type="primary" @click="searchAll" icon="search">查询</a-button>
                                            <a-button style="margin-left: 4px" @click="resetSearch" >重置</a-button>
                                            <#if (searchColumns?size > formBoList.searchMax)>
                                                <a @click="toggleAdvanced" style="margin-left: 8px">
                                                                {{ advanced ? '收起' : '展开' }}
                                                    <a-icon :type="advanced ? 'up' : 'down'" />
                                                </a>
                                            </#if>
                                        </span>
                                </a-col>
                            </#if>
                            <template v-if="advanced">
                                <#list searchColumns as search>
                                    <#if (search.type=='query' && search_index>=formBoList.searchMax)>
                                        <a-col :span="colSpan">
                                            <a-form-item label="${search.fieldLabel}">
                                                <#if (!search.fc?? || search.fc=='textbox')>
                                                    <a-input <#if search.emptytext??>placeholder="${search.emptytext}"</#if>
                                                             :allowClear="true"
                                                             v-model.trim="queryParam['<#if search.autoFilter=='YES'>Q_<#if search.tablePre?? && search.tablePre!="">${search.tablePre}.</#if>${search.fieldName}_S_${search.fieldOp}<#else>${search.fieldName}</#if>']"/>
                                                <#elseif (search.fc=='datepicker')>
                                                    <#if search.format??>
                                                        <#assign searchDateFormat=search.format>
                                                    <#else>
                                                        <#assign searchDateFormat='YYYY-MM-DD'>
                                                    </#if>
                                                    <a-date-picker <#if search.emptytext??>placeholder="${search.emptytext}"</#if>
                                                                   v-model="queryParam['queryField_${search.fieldName}']"
                                                                   format="${searchDateFormat}"
                                                            <#if searchDateFormat=='YYYY-MM-DD HH:mm:ss'>
                                                                show-time
                                                            </#if>
                                                                   @change="searchDateChange($event,$event,{queryField:'<#if search.autoFilter=='YES'>Q_<#if search.tablePre?? && search.tablePre!="">${search.tablePre}.</#if>${search.fieldName}_D_${search.fieldOp}<#else>${search.fieldName}</#if>',format:'${searchDateFormat}'})"/>
                                                <#elseif (search.fc=='month')>
                                                    <a-month-picker  v-model="queryParam['queryField_${search.fieldName}']" <#if search.emptytext??>placeholder="${search.emptytext}"</#if>  @change="searchMonthChange($event,$event,{queryField:'<#if search.tablePre?? && search.tablePre!="">${search.tablePre}.</#if>${search.fieldName}'<#if search.format??>,format:'${search.format}'</#if>,autoFilter:'${search.autoFilter}'})" />
                                                <#elseif (search.fc=='rangepicker')>
                                                    <#if search.format??>
                                                        <#assign searchRangeFormat=search.format>
                                                    <#else>
                                                        <#assign searchRangeFormat='YYYY-MM-DD HH:mm:ss'>
                                                    </#if>
                                                    <a-range-picker ref="queryField_${search.fieldName}"  v-model="params['queryField_${search.fieldName}']" <#if search.emptytext??>placeholder="${search.emptytext}"</#if> show-time format="${searchRangeFormat}"  @change="searchRangeDateChange($event,'${searchRangeFormat}',{queryField:'<#if search.tablePre?? && search.tablePre!="">${search.tablePre}.</#if>${search.fieldName}',autoFilter:'${search.autoFilter}'})" />
                                                <#elseif (search.fc=='select')>
                                                    <rx-select ref="Q_<#if search.tablePre?? && search.tablePre!="">${search.tablePre}_</#if>${search.fieldName}_S_${search.fieldOp}" v-model="queryParam['<#if search.autoFilter=='YES'>Q_<#if search.tablePre?? && search.tablePre!="">${search.tablePre}.</#if>${search.fieldName}_S_${search.fieldOp}<#else>${search.fieldName}</#if>']"
                                                               :multiValue="true" :labelInValue="false" :multiSelect="<#if search.multiSelect??>${search.multiSelect?c}<#else>false</#if>"
                                                               <#if (!search.from?? || search.from=='self')>:dataSource="selectOptions${search_index}" value-field="key" text-field="name"
                                                               <#elseif (search.from=='url')>url="${search.url}" value-field="${search.url_valuefield}" text-field="${search.url_textfield}"
                                                               <#elseif (search.from=='dic')>url="/api-system/system/core/sysDic/treeByKey?key=${search.dicKey}" value-field="value" text-field="name"
                                                               <#elseif (search.from=='sql')>url="/api-form/form/core/formCustomQuery/queryForJson_${search.sql}" data-field="data" value-field="${search.sql_valuefield}" text-field="${search.sql_textfield}"
                                                               @focus="selectFocus('Q_<#if search.tablePre?? && search.tablePre!="">${search.tablePre}_</#if>${search.fieldName}_S_${search.fieldOp}',selectParams${search_index})" :loadDataOnstart="true"
                                                               @onChange="searchChange('${search.fieldName}','Q_<#if search.tablePre?? && search.tablePre!="">${search.tablePre}_</#if>${search.fieldName}_S_${search.fieldOp}')" ajaxType="post"
                                                               <#elseif (search.from=='grant')>url="/api-form/form/core/formEntityDataSetting/queryData_${search.grant}" data-field="data"
                                                               @focus="selectFocus('Q_<#if search.tablePre?? && search.tablePre!="">${search.tablePre}_</#if>${search.fieldName}_S_${search.fieldOp}')" :loadDataOnstart="true"
                                                            </#if>/>
                                                <#elseif (search.fc=='treeselect')>
                                                    <rx-tree-select-ctl ref="Q_<#if search.tablePre?? && search.tablePre!="">${search.tablePre}_</#if>${search.fieldName}_S_${search.fieldOp}" v-model="queryParam['<#if search.autoFilter=='YES'>Q_<#if search.tablePre?? && search.tablePre!="">${search.tablePre}.</#if>${search.fieldName}_S_${search.fieldOp}<#else>${search.fieldName}</#if>']"
                                                                        :multiValue="true" :labelInValue="false" :multiSelect="<#if search.multiSelect??>${search.multiSelect?c}<#else>false</#if>" :showSearch="<#if search.showSearch??>${search.showSearch?c}<#else>false</#if>"
                                                                        <#if (!search.from?? || search.from=='self')>:dataSource="selectOptions${search_index}" value-field="key" text-field="name"
                                                                        <#elseif (search.from=='url')>url="${search.url}" value-field="${search.url_valuefield}" text-field="${search.url_textfield}" parent-field="${search.url_parentfield}"
                                                                        <#elseif (search.from=='dic')>url="/api-system/system/core/sysDic/treeByKey?key=${search.dicKey}" value-field="value" text-field="name" :toTree="false"
                                                                        <#elseif (search.from=='sql')>url="/api-form/form/core/formCustomQuery/queryForJson_${search.sql}" data-field="data" value-field="${search.sql_valuefield}" text-field="${search.sql_textfield}" parent-field="${search.sql_parentfield}"
                                                                        @focus="selectFocus('Q_<#if search.tablePre?? && search.tablePre!="">${search.tablePre}_</#if>${search.fieldName}_S_${search.fieldOp}',selectParams${search_index})" :loadDataOnstart="true"
                                                                        @onChange="searchChange('${search.fieldName}','Q_<#if search.tablePre?? && search.tablePre!="">${search.tablePre}_</#if>${search.fieldName}_S_${search.fieldOp}')" ajaxType="post"
                                                                        <#elseif (search.from=='grant')>url="/api-form/form/core/formEntityDataSetting/queryData_${search.grant}" data-field="data"
                                                                        @focus="selectFocus('Q_<#if search.tablePre?? && search.tablePre!="">${search.tablePre}_</#if>${search.fieldName}_S_${search.fieldOp}')" :loadDataOnstart="true"
                                                            </#if>/>
                                                <#elseif (search.fc=='dialog')>
                                                    <rx-input-button v-model="params['<#if search.autoFilter=='YES'>Q_<#if search.tablePre?? && search.tablePre!="">${search.tablePre}.</#if>${search.fieldName}_S_${search.fieldOp}<#else>${search.fieldName}</#if>']" :multiValue="true"
                                                                     :single="false" :config="{fieldName:'${search.fieldName}',queryField:'<#if search.autoFilter=='YES'>Q_<#if search.tablePre?? && search.tablePre!="">${search.tablePre}.</#if>${search.fieldName}_S_${search.fieldOp}<#else>${search.fieldName}</#if>',dialog:'<#if search.dialog??>${search.dialog}</#if>',dialog_name:'<#if search.dialog_name??>${search.dialog_name}</#if>',valueField:'<#if search.valueField??>${search.valueField}</#if>',textField:'<#if search.textField??>${search.textField}</#if>',single:!<#if search.multiSelect??>${search.multiSelect?c}<#else>true</#if>}"
                                                                     @click="_OnDialogShow"/>
                                                </#if>
                                            </a-form-item>
                                        </a-col>
                                    </#if>
                                </#list>
                            </template>

                        </a-row>
                    </a-form>
                </div>
                <#if formBoList.dataStyle=='list'>
                <rx-grid :showPage="'${formBoList.isPage}'=='YES'"
                </#if>
                <#if formBoList.dataStyle=='tree'>
                <rx-tree-table
                        :resultAsTree="true"
                        <#if formBoList.isLazy=='YES'>
                            @rowExpand="rowExpandMethod"
                        </#if>
                </#if>
                        :bordered="formBoList.tableStyle!=1 && formBoList.tableStyle!=2 && formBoList.tableStyle!=5"
                        :loadDataOnstart="loadDataOnstart"
                        alias="${formBoList.key}<#noparse><#if pmtAlias??>_${pmtAlias}</#if></#noparse>"
                        :defaultPageSize="${formBoList.pageSize}"
                        :pageSizeOptions="[10,20,30,40,50]"
                        :showColumnsMenu="false"
                        :showColumns="showColumns"
                        :defaultShowColumns="defaultShowColumns"
                        :hasAllColumns="hasAllColumns"
                        :showExpandedRowRender="${(formBoList.isExpandRow=='YES')?string('true','false')}"
                        :showFooter="'${formBoList.showSummaryRow}'=='YES'"
                        :columns="columns"
                        :idField="formBoList.idField"
                        :expandIconColumnIndex="${expandIconColumnIndex}"
                        data-field="result.data"
                        :parentField="formBoList.parentField"
                        url="${ctxPath}/api-form/form/core/formBoList/${formBoList.key}/getData<#noparse>${query}</#noparse>"
                        ref="table"
                        @selectChange="selectChange"
                        <#if formBoList.rowDblClick?? && formBoList.rowDblClick!=''>
                            @rowDblClick="${formBoList.rowDblClick}"
                        </#if>
                        <#if formBoList.rowClick?? && formBoList.rowClick!=''>
                            @rowClick="${formBoList.rowClick}"
                        </#if>
                        :getCheckboxProps="getCheckboxPropsFun"

                        :activeEditFun="activeEditFunMethod"
                        :rowSummaryFun="rowSummaryFunMethod"
                        :drawSummaryFun="drawSummaryFunMethod"
                        :queryParam="queryParam"
                        :allowRowSelect="true"
                        :multiSelect="formBoList.multiSelect"
                >

                    <#list cellEditorColumns as cellEditor>
                        <#if cellEditor.type=='customTitle'>
                    <span slot="${cellEditor.field}" slot-scope="text">
                            ${cellEditor.html}
                    </span>
                        <#else>
                        <span slot="<#if formBoList.useCondSql=='WEBREQ' || formBoList.useCondSql=='INTERFACE'>${cellEditor.field}<#else>${cellEditor.field?upper_case}</#if>"
                              slot-scope="{text,record,index,blur}"<#if (cellEditor.type=='render' && cellEditor.renderType=='ACTION')> class="actionIcons" size="<#if cellEditor.renderConf?? && cellEditor.renderConf.maxButton??>${cellEditor.renderConf.maxButton}<#else>3</#if>"</#if>>
                        <#if (cellEditor.type=='control')>
                            <#if (!cellEditor.control?? || cellEditor.control=='input')>
                                <a-input v-model="record['${cellEditor.field}']"
                                <#if cellEditor.controlConf?? && cellEditor.controlConf.rules??>
                                    @blur="validRule(record,'${cellEditor.field}','${cellEditor.controlConf.regular}','${cellEditor.controlConf.prompt}',index,text,blur)"
                                <#else>
                                    @blur="blur(index,'${cellEditor.field}',text,record)"
                                </#if>/>
                            <#elseif (cellEditor.control=='textarea')>
                                <a-textarea v-model="record['${cellEditor.field}']"
                                            @blur="blur(index,'${cellEditor.field}',text,record)"/>
                            <#elseif (cellEditor.control=='inputNumber')>
                                <#if cellEditor.controlConf??>
                                    <a-input-number :min="<#if cellEditor.controlConf.min??>${cellEditor.controlConf.min}<#else>1</#if>"
                                                    :max="<#if cellEditor.controlConf.max??>${cellEditor.controlConf.max}<#else>100</#if>"
                                                    :step="<#if cellEditor.controlConf.step??>${cellEditor.controlConf.step}<#else>1</#if>"
                                                    :formatter="function(value){return <#if cellEditor.controlConf.formatter?? && cellEditor.controlConf.formatter!=''>${cellEditor.controlConf.formatter}<#else>value</#if>}"
                                                    v-model="record['${cellEditor.field}']"
                                                    @blur="blur(index,'${cellEditor.field}',text,record)"/>
                                <#else>
                                    <a-input-number :min="0"
                                                    :max="100"
                                                    :step="1"
                                                    :formatter="function(value){return value;}"
                                                    v-model="record['${cellEditor.field}']"
                                                    @blur="blur(index,'${cellEditor.field}',text,record)"/>
                                </#if>
                            <#elseif (cellEditor.control=='checkbox')>
                                <#if cellEditor.controlConf??>
                                    <rx-form-checkbox v-model="record['${cellEditor.field}']" true-value="${cellEditor.controlConf.trueValue}" false-value="${cellEditor.controlConf.falseValue}" />
                                <#else>
                                    <rx-form-checkbox v-model="record['${cellEditor.field}']"  />
                                </#if>
                            <#elseif (cellEditor.control=='datePicker')>
                                <#if cellEditor.controlConf?? && cellEditor.controlConf.format??>
                                    <#assign dateFormat=cellEditor.controlConf.format>
                                <#else>
                                    <#assign dateFormat='YYYY-MM-DD'>
                                </#if>
                                <#if cellEditor.renderConf?? && cellEditor.renderConf.format??>
                                    <#assign renderDateFormat=cellEditor.renderConf.format>
                                <#else>
                                    <#assign renderDateFormat=''>
                                </#if>
                                <a-date-picker showTime format="${dateFormat}" @change="gridChangeDate($event,$event,{record:record,dataIndex:'${cellEditor.field}',format:'${dateFormat}',renderFormat:'${renderDateFormat}'})" @ok="blur(index,'${cellEditor.field}',text,record)" />
                            <#elseif (cellEditor.control=='select')>
                                <rx-select v-model="record['${cellEditor.field}']" :labelInValue="true" style="width:100%;"
                                           <#if (!cellEditor.controlConf?? || cellEditor.controlConf.from=='self')>:dataSource="cellOptions${cellEditor_index}" value-field="key" text-field="name"
                                           <#elseif (cellEditor.controlConf.from=='url')>url="${cellEditor.controlConf.url}" value-field="${cellEditor.controlConf.url_valuefield}" text-field="${cellEditor.controlConf.url_textfield}"
                                           <#elseif (cellEditor.controlConf.from=='dic')>url="/api-system/system/core/sysDic/treeByKey?key=${cellEditor.controlConf.dicKey}" value-field="value" text-field="name"
                                           <#elseif (cellEditor.controlConf.from=='sql')>url="/api-form/form/core/formCustomQuery/queryForJson_${cellEditor.controlConf.sql}" data-field="data" value-field="${cellEditor.controlConf.sql_valuefield}" text-field="${cellEditor.controlConf.sql_textfield}" ajaxType="post"
                                        </#if> @blur="blur(index,'${cellEditor.field}',text,record)"/>
                            <#elseif (cellEditor.control=='dialog')>
                                <#if cellEditor.controlConf??>
                                    <rx-input-button v-model="record['${cellEditor.field}']"
                                                     :allowInput="<#if cellEditor.controlConf.allowinput??>${cellEditor.controlConf.allowinput}<#else>false</#if>"
                                                     :showClose="<#if cellEditor.controlConf.showclose??>${cellEditor.controlConf.showclose}<#else>false</#if>"
                                                     :single="true" :config="{dialog:'${cellEditor.controlConf.dialog}',dialog_name:'${cellEditor.controlConf.dialog_name}',valueField:'${cellEditor.controlConf.valueField}',returnFields:returnFields${cellEditor_index}}"
                                                     @click="_OnGridDialogShow($event,{field:'${cellEditor.field}',text:text,record:record,index:index,blur:blur})"/>
                                <#else>
                                    <rx-input-button v-model="record['${cellEditor.field}']"
                                                     :allowInput="false"
                                                     :showClose="false"
                                                     :single="true" :config="{dialog:'',dialog_name:'',valueField:'',returnFields:returnFields${cellEditor_index}}"
                                                     @click="_OnGridDialogShow($event,{field:'${cellEditor.field}',text:text,record:record,index:index,blur:blur})"/>
                                </#if>
                            </#if>
                        </#if>
                        <#if (cellEditor.type=='render')>
                            <#if (!cellEditor.renderType?? || cellEditor.renderType=='COMMON')>
                                <#if (cellEditor.renderConf??) && (cellEditor.renderConf.html??)>
                                    ${cellEditor.renderConf.html}
                                <#else>
                                    <span v-html="text"></span>
                                </#if>
                            <#elseif ((cellEditor.renderType=='USER') && (cellEditor.renderConf??))>
<#--                                    <#if (cellEditor.renderConf.showLink!false)>-->
<#--                                    <a href="javascript:void(0);"-->
<#--                                       @click.stop="_ShowUserInfo(text)">{{record['${cellEditor.field}_render']}}</a>-->
<#--                                    <#else>-->
                                {{record['${cellEditor.field}_render']}}
<#--                                    </#if>-->
                            <#elseif (cellEditor.renderType=='GROUP' && (cellEditor.renderConf??))>
<#--                                    <#if (cellEditor.renderConf.showLink!false)>-->
<#--                                        <a href="javascript:void(0);"-->
<#--                                           @click.stop="_ShowGroupInfo(text)">{{record['${cellEditor.field}_render']}}</a>-->
<#--                                    <#else>-->
                                {{record['${cellEditor.field}_render']}}
<#--                                    </#if>-->
                            <#elseif (cellEditor.renderType=='SYSINST' && (cellEditor.renderConf??))>
<#--                                    <#if (cellEditor.renderConf.showLink!false)>-->
<#--                                        <a href="javascript:void(0);"-->
<#--                                           @click.stop="_ShowInstInfo(text)">{{record['${cellEditor.field}_render']}}</a>-->
<#--                                    <#else>-->
                                {{record['${cellEditor.field}_render']}}
<#--                                    </#if>-->
                            <#elseif (cellEditor.renderType=='IMG')>
                                <span v-html="onImgRender(text)"></span>
                            <#elseif (cellEditor.renderType=='APPENDIX')>
                                <span v-html="onFileRender(text)"></span>
                            <#elseif (cellEditor.renderType=='URL')>
                                <a href="javascript:void(0);"
                                   @click.stop="_ShowUrl(record['${cellEditor.field}_render'],'<#if (cellEditor.renderConf?? && cellEditor.renderConf.linkType??)>${cellEditor.renderConf.linkType}</#if>')">{{text}}</a>
                            <#elseif (cellEditor.renderType=='DISPLAY_PERCENT')>
                                <#if cellEditor.renderConf??>
                                    <#if (cellEditor.renderConf.stylename=='circular')>
                                        <a-progress strokeColor="${cellEditor.renderConf.fgcolor}" class="aprogress" type="circle" :percent="text">
                                              <template #format="percent">
                                              <span>{{text}}%</span>
                                              </template>
                                             </a-progress>
                                            <#else >
                                        <a-progress strokeColor="${cellEditor.renderConf.fgcolor}" :percent="text"/>
                                    </#if>
                                </#if>
                            <#elseif (cellEditor.renderType=='LINK_FLOW')>
                                <template v-if="record['${cellEditor.field}_render']">
                                        <span v-show="false">
                                        {{flowData[index]=JSON.parse(record['${cellEditor.field}_render'])}}
                                        {{showTitle=<#if cellEditor.renderConf.showTitle??>${cellEditor.renderConf.showTitle?c}<#else>false</#if>}}
                                        {{showBpmInstLink=<#if cellEditor.renderConf.showBpmInstLink??>${cellEditor.renderConf.showBpmInstLink?c}<#else>false</#if>}}
                                        {{showTask=<#if cellEditor.renderConf.showTask??>${cellEditor.renderConf.showTask?c}<#else>false</#if>}}
                                        {{showTaskHandler=<#if cellEditor.renderConf.showTaskHandler??>${cellEditor.renderConf.showTaskHandler?c}<#else>false</#if>}}
                                        </span>
                                        <a href="javascript:void(0);" v-if="showBpmInstLink"
                                           @click.stop="_ShowBpmInstInfo(flowData[index].instId,flowData[index].subject)">
                                            <template v-if="showTitle">
                                                {{flowData[index].subject}}
                                            </template>
                                        </a>
                                        <template v-if="!showBpmInstLink && showTitle">
                                                {{flowData[index].subject}}
                                        </template>
                                        <template v-if="showTask">
                                            [<template v-for="task in flowData[index].taskAry">
                                                <a v-if="task.taskId" href="javascript:void(0);" @click.stop="_handleTask(flowData[index].instId,task.taskId,task.taskName)">
                                                    {{task.taskName}}
                                                    <span v-if="showTaskHandler && !task.isUsers" style='color:red'>{{'('+task.taskUsers+')'}}</span>
                                                    <span v-else>{{'('+task.taskUsers+')'}}</span>
                                            </a>
                                                <span v-else>
                                                    {{task.taskName}}
                                                    <span v-if="showTaskHandler && !task.isUsers" style='color:red'>{{'('+task.taskUsers+')'}}</span>
                                                    <span v-else>{{'('+task.taskUsers+')'}}</span>
                                                </span>
                                            </template>]
                                        </template>
                                    </template>
                            <#elseif (cellEditor.renderType=='ACTION' && cellEditor.renderConf?? && cellEditor.renderConf.btnList??)>
                               <#list cellEditor.renderConf.btnList as btn>
                                    <span v-show="canActionShow('${btn.alias}',record)" @click.stop="${btn.btnClick}">${btn.btnLabel}</span>
                                </#list>
                             <#elseif (cellEditor.renderType=='LIST' && cellEditor.renderConf??)>
                                    <a @click='openListDialog(record,"${cellEditor.renderConf.alias}","${cellEditor.renderConf.fieldMap?replace("\"","\\\"")}")' >{{text}}</a>
                            <#elseif (cellEditor.renderType=='OPENFORM' && cellEditor.renderConf??)>
                                <a href="javascript:void(0);" @click.stop='openForm("${cellEditor.renderConf.formAlias?replace("\"","\\\"")}",<#if cellEditor.renderConf.pkField??>"${(cellEditor.renderConf.pkField)}"<#else>""</#if>,"${(cellEditor.renderConf.readOnly)?string("true","false")}",record,"${cellEditor.renderConf.fieldMap?replace("\"","\\\"")}","${(cellEditor.renderConf.callback)}")'>{{text}}</a>
                            </#if>
                        </#if>
                    </span>
                    </#if>
                    </#list>
                    <#if (formBoList.isExpandRow=='YES')>
                        <template slot="expandedRowRender" slot-scope="{record,index}">
                            <#if expandRowJsonType=='self'>
                                ${expandRowJsonListHtml}
                            <#elseif expandRowJsonType=='form'>
                                <rx-form-solution-show :config="expandRowJson" :record="record"></rx-form-solution-show>
                            <#elseif expandRowJsonType=='list'>
                                <rx-form-bo-list :config="expandRowJson" :record="record"></rx-form-bo-list>
                            </#if>
                        </template>
                    </#if>
                    <#if formBoList.dataStyle=='list'>
               </rx-grid>
                    </#if>
                    <#if formBoList.dataStyle=='tree'>
               </rx-tree-table>
                    </#if>
                <a-modal v-model="visibleModal" title="提示" @ok="handleOk"
                         :zIndex="9999" width="300px" @cancel="handleCancel">
                    <p>
                        您确定要删除这个搜索条件吗？
                    </p>
                </a-modal>
            </rx-fit>
        </div>
        <div v-if="formBoList.multiSelect && formBoList.isPage=='NO'" slot="right"  width="250">
            <a-table size="small" :rowKey="formBoList.idField" bordered v-bind="selectedTable">
                    <span slot="index" slot-scope="text,record,index">
                       {{index+1}}
  					</span>
                    <#if dialgColumnList ??>
                        <#list dialgColumnList as dialgColumn>
                            <#if dialgColumn.scopedSlots ?? && dialgColumn.dataIndex!="index" >
                                <span slot="${dialgColumn.dataIndex}" slot-scope="text,record,index">
                                   {{record.${dialgColumn.dataIndex}_display}}
                                </span>
                            </#if>
                        </#list>
                    </#if>
                    <span slot="action" slot-scope="text,record,index">
                        <a @click="handleDeleteSelected(record,index)">删除</a>
                    </span>
            </a-table>
        </div>
    </rx-layout>
</template>

<script>
    export default {
        name: "${formBoList.key}",
        data() {
            return {
                loadDataOnstart:${(formBoList.isInitData?? && formBoList.isInitData=='YES')?string('true','false')},
                //rx-fit,搜索栏放在头部或者 弹框 ；
                isheader:<#if (formBoList.isSearchHeader?? && formBoList.isSearchHeader=='YES')>true<#else>false</#if>,
				islayout:<#if (formBoList.isSearchLayout?? && formBoList.isSearchLayout=='YES')>true<#else>false</#if>,
                searches:[],
                <#if (searchColumns?size > 0)>
                <#list searchColumns as search>
                <#if (search.type=='query' && (search.fc=='select' || search.fc=='treeselect') && !search.from??)>
                selectOptions${search_index}:[],
                <#elseif (search.type=='query' && (search.fc=='select' || search.fc=='treeselect') && search.from=='self')>
                selectOptions${search_index}:${search.props},
                <#elseif (search.type=='query' && (search.fc=='select' || search.fc=='treeselect') && search.from=='sql')>
                selectParams${search_index}:${search.params},
                </#if>
                </#list>
                </#if>
                <#list cellEditorColumns as cellEditor>
                <#if (cellEditor.type=='control' && cellEditor.control=='select' && !cellEditor.controlConf??)>
                cellOptions${cellEditor_index}:[],
                <#elseif (cellEditor.type=='control' && cellEditor.control=='dialog' && !cellEditor.controlConf??)>
                returnFields${cellEditor_index}:[],
                <#elseif (cellEditor.type=='control' && cellEditor.control=='select' && cellEditor.controlConf.from=='self')>
                cellOptions${cellEditor_index}:${cellEditor.controlConf.props},
                <#elseif (cellEditor.type=='control' && cellEditor.control=='dialog')>
                returnFields${cellEditor_index}:${cellEditor.controlConf.returnFields},
                </#if>
                </#list>
                <#if (tabTreeColumns?size>0)>activeKey: '${tabTreeColumns[0].tabName}', </#if>
                queryId: '<#noparse><#if params?? && params.queryId??>${params.queryId}</#if></#noparse>',
                topButtonColumns:${topButtonHtml},
                //默认查询条件参数
                <#if (searchColumns?size > 0)>
                tmpQueryParams: {
                    <#list searchColumns as search>
                    <#if (search.type=='query' && search.defaultValConf?? && search.defaultValConf!="")>
                    <#if search.fc=='datepicker' || search.fc=='month' || search.fc=='rangepicker'>queryField_${search.fieldName}<#elseif search.autoFilter=='YES'>Q_<#if search.tablePre?? && search.tablePre!="">${search.tablePre}.</#if>${search.fieldName}_S_${search.fieldOp}<#else>${search.fieldName}</#if>:{autoFilter:'${search.autoFilter}',tablePre:'${search.tablePre}',fieldName:'${search.fieldName}',fieldOp:'${search.fieldOp}',defaultValConf:${search.defaultValConf}},
            </#if>
            </#list>
        },
            <#else >
            tmpQueryParams:{},
            flowData:[],
            </#if>
                showColumns:<#noparse><#if showColumns??>${showColumns}<#else>[]</#if></#noparse>,
                defaultShowColumns:<#noparse><#if defaultShowColumns??>${defaultShowColumns}<#else>[]</#if></#noparse>,
                formBoList: {
                        rowEdit: '${formBoList.rowEdit}',
                        isMax: '${formBoList.isMax}',
                        width: ${formBoList.width},
                        height: ${formBoList.height},
                        isShade: '${formBoList.isShade}',
                        tableStyle:<#if formBoList.tableStyle??>${formBoList.tableStyle}<#else>0</#if>,
                        lineHeight:<#if formBoList.lineHeight??>${formBoList.lineHeight}<#else>0</#if>,
                        isShowView: '${formBoList.isShowView}',
                        isShowSearch: '${formBoList.isShowSearch}',
                        isFieldShow: '${formBoList.isFieldShow}',
                        name: '${formBoList.name}',
                        dataStyle: '${formBoList.dataStyle}',
                        idField: '<#if formBoList.useCondSql=='WEBREQ' || formBoList.useCondSql=='INTERFACE'>${idField}<#else>${idField?upper_case}</#if>',
                        parentField: '<#if formBoList.useCondSql=='WEBREQ' || formBoList.useCondSql=='INTERFACE'>${parentField}<#else>${parentField?upper_case}</#if>',
                        key: '${formBoList.key}',
                        id: '${formBoList.id}',
                        multiSelect:
                    <#noparse>
                    <#if params ?? && params.single ?? && params.single == "false" >
                ${(params.single=="true")?string('false','true')},
        <#elseif params.single == "true" >
            false,
        <#else >
                ${formBoList.multiSelect},
        </#if>
            </#noparse>
                    isDialog:'${formBoList.isDialog}',
                    isTreeDlg:'${isTreeDlg}'
                },
                columns:${gridColumns?replace('"<func>', '')?replace('</func>"', '')},
                rowButtons:${rowButtons},
                btnRight:<#noparse><#if btnRight??>${btnRight}<#else>[]</#if></#noparse>,
                hasAllRight:'<#noparse><#if hasAllRight??>${hasAllRight}</#if></#noparse>',
                hasAllColumns:'<#noparse><#if hasAllColumns??>${hasAllColumns}</#if></#noparse>',
                pmtDatas:<#noparse><#if pmtDatas??>${pmtDatas}<#else>{}</#if></#noparse>,
                //已选择列表
                selectedTable:{
                    pagination: false,
                    scroll: {y:240},
                    columns : [...${dialgColumns},{
                title:'操作',
                    dataIndex:'action',
                    align:'center',
                    width:60,
                    scopedSlots:{customRender:'action'}
            }],
                    dataSource:[]
                }
        }
        },
        created() {
            <#if (searchColumns?size > 0)>
            <#list searchColumns as search>
            <#if (search.type=='query' && (search.fc=='select' || search.fc=='treeselect') && search.from?? && search.from=='sql')>
            this.searches.push({fieldName:'${search.fieldName}',tablePre:'${search.tablePre}',fieldOp:'${search.fieldOp}',params:${search.params}});
            </#if>
            </#list>
            </#if>
        },
        methods: {
        <#if (tabTreeColumns?size > 0)>
        <#list tabTreeColumns as tab>
        <#if tab.onnodeclick??>
            ${tab.onnodeclick}(selectedKeys, info)
    {
        this.handTreeClick(selectedKeys, '<#if tab.paramName??>${tab.paramName}</#if>');
    }
    ,
    </#if>
    </#list>
            </#if>
    <#if (topButtonColumns?size > 0)>
    <#list topButtonColumns as button>
    <#if button.btnType=='scriptBtn'>
    ${button.btnClick}(){
        this.handButtonClick('/api-form/form/core/formBoList/'+this.formBoList.key+'/selfButtonExe/${button.btnName}');
    },
    </#if>
    </#list>
    </#if>
            ${bodyScript}
        }
    }
</script>

<style scoped>
    .rxLayout{
        padding: 0!important;
        background: #f0f2f5!important;
    }
    .listSearch{
        margin-left:16px;
        cursor: pointer;
    }
    .listSearch i{
        margin-right: 4px;
    }
    .actspan{
        color: #BFBFBF !important;
        cursor: not-allowed;
        pointer-events:none
    }
</style>
