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
	<rx-layout>
		<div slot="center">
			<rx-tree ref="rxTree"
					 url="${ctxPath}/api-form/form/core/formBoList/${formBoList.key}/getTreeJson<#noparse>${query}</#noparse>"
					:showTreeIcon="false"
					 ajax-type="post"
					 data-field="result.data"
					:textField="formBoList.textField"
					:idField="formBoList.idField"
					 <#if formBoList.parentField??>
					 :parentField="formBoList.parentField"
					 </#if>
					:expandOnLoad="${expandLevel}"
					:onlySelLeaf="'${onlySelLeaf}'=='YES'"
					:multiSelect="formBoList.multiSelect"
					 :checkStrictly="dialogVm.checkStrictly"
					:async="'${formBoList.isLazy}'=='YES'"
					<#if formBoList.isLazy=='YES'>
					:loadByParent="loadTreeByParent"
					</#if>
					<#if formBoList.publishConf??>
					@select="handSelect"
					</#if>
					<#if topButtonColumns?? && (topButtonColumns?size>0)>
					@rightClick="rightClick"
					</#if>
			/>
			<div style="height:50px"></div>
			<#if topButtonColumns?? && (topButtonColumns?size>0)>
			<v-contextmenu ref="contextmenu">
				<#list topButtonColumns as button>
					<rx-contextmenu-item alias="${button.btnName}" btn-icon='${button.btnIcon}' @click="${button.btnClick}">${button.btnLabel}</rx-contextmenu-item>
				</#list>
			</v-contextmenu>
			</#if>
		</div>
	</rx-layout>
</template>

<script>
	export default {
		name: "${formBoList.key}",
		data(){
			return {
				formBoList: {
					name: '${formBoList.name}',
					idField: '${formBoList.idField}',
					textField:'${formBoList.textField}',
					formAlias: '${formAlias}',
					formAddAlias: '${formAddAlias}',
					<#if formBoList.parentField??>parentField: '${formBoList.parentField}',</#if>
					key: '${formBoList.key}',
					id: '${formBoList.id}',
					<#if formBoList.publishConf??>
					publishConf:${formBoList.publishConf},
					</#if>
					multiSelect:
					<#noparse>
					<#if params?? && params.single?? && params.single=="false">
					${(params.single=="true")?string('false','true')},
					<#elseif params?? && params.single=="true">
					false,
					<#else>
					${formBoList.multiSelect},
					</#if>
					</#noparse>
					isDialog: '${formBoList.isDialog}',
					isTreeDlg: 'YES'
				}
			}
		},
		computed: {
			table() {
				return this.$refs.rxTree;
			},
		},
		methods:{
			getData(self_,this_){
				return this_.$refs.rxTree.getCheckedNodes(false);
			},
			onExpand(){
				this.$refs.rxTree.expandAll();
			},
			onCollapse(){
				this.$refs.rxTree.collapseAll();
			},
            onRowExpand(){
                var expandedKeys = this.$refs.rxTree.expandedKeys;
                var selKey=this.curRow[this.formBoList.idField];
                if(expandedKeys.indexOf(selKey)==-1){
                    expandedKeys.push(selKey);
                }
                this.$refs.rxTree.onExpand(expandedKeys,{node:{dataRef:this.curRow}});
            },
            onRowCollapse(){
                var expandedKeys = this.$refs.rxTree.expandedKeys;
                var selKey=this.curRow[this.formBoList.idField];
                expandedKeys=expandedKeys.filter(item => item !== selKey)
                this.$refs.rxTree.onExpand(expandedKeys,{node:{dataRef:this.curRow}});
            },
			refreshTree(){
				this.$refs.rxTree.loadData();
			}
			<#if formBoList.publishConf??>
			,handSelect(selectedKeys, info){
				var publishConf=this.formBoList.publishConf;
				if(!publishConf){
					return;
				}
				var row=info.node.dataRef;
				var params={};
				for(var i=0;i<publishConf.length;i++){
					var conf=publishConf[i];
					params[conf.key]=row[conf.key]
				}
				var obj={component: this.formBoList.key,params:params};

				console.info(obj);

				this.$bus.emit('formEvent', obj) ;

			}
			</#if>
			<#if bodyScript??>
			,${bodyScript}
			</#if>
		}
	}
</script>

<style scoped>

</style>
