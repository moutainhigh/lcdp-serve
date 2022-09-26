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
<#assign apiProject=vars.apiProject>
<#assign tableName=model.tableName>
<#assign colList=model.columnList>
<#assign commonList=model.commonList>
<template>
  <rx-layout>
    <div slot="center" style>
      <rx-fit>
        <div slot="toolheader" foldheader="true" foldbtn="false" border="false">
          <div class="table-operator">
            <rx-button alias="${classVar}Add" :butn-icon="'plus'" :butn-type="'primary'" :show-no-right="true" @click="add">新建</rx-button>
            <span slot="action" slot-scope="{ text, record }" class="actionIcons">
              <rx-span v-if="editEnable" alias="${classVar}Edit" :show-no-right="false" @click="edit(record)">编辑</rx-span>
              <rx-span v-if="removeEnable" alias="${classVar}Delete" :show-no-right="false" @click="delByIds([record.${pkVar}])">删除</rx-span>
            </span>
          </div>
          <span class="search-btn-box">
                <span class="search-btn" @click="searchshow"><i class="iconfont iconguolv"></i>过滤</span>
           </span>
          <#--          -->
        </div>
        <div slot="searchbar" btnalign="right"  v-if="fitSearch" @search="search" @cancel="cancel" @close="closeSearchbar">
          <a-form layout="vertical">
            <#list commonList as col>
              <#assign colName=func.convertUnderLine(col.columnName)>
              <#if func.isExcludeField(colName) >
                <a-form-item label="${col.comment}">
                  <#assign isNotNull=col.getIsNotNull()?string("true","false") >
                  <#if (col.colType=="java.util.Date")>
                    从
                    <a-date-picker placeholder="请输入${col.comment}" v-model="queryParam.Q_${col.columnName}_D_GT"
                                   format="YYYY-MM-DD"/>至
                    <a-date-picker placeholder="请输入${col.comment}" v-model="queryParam.Q_${col.columnName}_D_LT"
                                   format="YYYY-MM-DD"/>
                  <#elseif (col.colType=="Integer")>
                    <a-input-number :min="0" :max="1000000" placeholder="请输入${col.comment}"
                                    v-model="queryParam.Q_${col.columnName}_I_EQ"/>
                  <#else>
                    <a-input placeholder="请输入${col.comment}" v-model="queryParam.Q_${col.columnName}_S_LK"/>
                  </#if>
                </a-form-item>
              </#if>
            </#list>
          </a-form>
        </div>
        <rx-grid
                ref="table"
                :allowRowSelect="true"
                :multiSelect="true"
                :columns="columns"
                :defaultPageSize="10"
                url="/${apiProject}/${system}/${package}/${classVar}/query"
                :queryParam="queryParam"
                data-field="result.data"
                :pageSizeOptions="['10','30','40']"
                id-field="${pkVar}"
                @selectChange="onSelectChange"
        >
        </rx-grid>
      </rx-fit>
    </div>
  </rx-layout>
</template>

<script>
  import {STable} from '@/components'
  import ${class}Api from '@/api/${system}/${package}/${classVar}'
  import ${class}Edit from './${class}Edit'
  import {BaseList,RxFit} from 'jpaas-common-lib';
  export default {
    name: '${class}List',
    mixins: [BaseList],
    components: {
      STable,
      RxFit
    },
    data() {
      return {
        columns: [
          {
            title: '序号',
            type: 'indexColumn',
            width: 100,
            dataIndex: 'serial',
            scopedSlots: {customRender: 'serial'}
          },
          <#list commonList as col>
          <#assign colName=func.convertUnderLine(col.columnName)>
          <#if func.isExcludeField( colName) >
          {title: '${col.comment}', dataIndex: '${colName}', width: 100},
          </#if>
          </#list>
          {title: '操作', width: 100, dataIndex: 'action', scopedSlots: {customRender: 'action'}}
        ],
        component:${class}Edit,
        comment:"${comment}",
        widthHeight:['800px','600px']
      }
    },
    methods:{
      delById(ids){
        return ${class}Api.del({ ids: ids.join(',') });
      },
      getListByParams(parameter){
        return ${class}Api.query(parameter)
      },
      search(){
        this.$refs.table.loadData()
      },
      cancel(){
        this.resetSearch();
      },
    }
  }
</script>
<style scoped>
  .table-operator {
    padding:4px;
  }
  .table-operator button {
    margin:2px;
  }
</style>

