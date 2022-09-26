<#assign package=model.vars.package>
<#assign class=model.vars.class>
<#assign classVar=model.vars.classVar>
<#assign system=vars.system>
<#assign domain=vars.domain>
<template>
<rx-dialog @handOk="handleSubmit" @cancel="cancel" order="top" btnalign="right" :showok="!readonly">
    <rx-layout>
        <div slot="center">
            ${vueTemplate}
        </div>
    </rx-layout>
</rx-dialog>
</template>

<script>
    import ${class}Api from "@/api/${system}/${package}/${classVar}.js";
    import ${class}Data from "@/views/modules/${system}/${package}/${class}Data.js";
    import formJs from "@/views/modules/form/core/js-comps/form.js";

    export default{
        mixins: [formJs,${class}Data],
        props:["pkId","layerid","destroy","readonly"],
        data(){
            return {}
        },
        created() {
            //加载表单数据
            this.loadData(${class}Api);
        },
        mounted(){
            this.onload();
        },
        methods:{
            <#if onload??>
            onload()${onload},
            </#if>
            <#if _beforeSubmit??>
            async _beforeSubmit()${_beforeSubmit},
            </#if>
            <#if _afterSubmit??>
            _afterSubmit(result,formJson)${_afterSubmit},
            </#if>
              enter(entName, field){

              },
            <#list funcList as col>
                ${col.name}${col.action},
            </#list>
            handleSubmit(vm){
                this.handSubmit(${class}Api,vm);
            }

        },
        watch:{
            <#list watchList as col>
            "${col.name}":${col.action},
            </#list>
        }
    }
</script>