<#setting number_format="#">
<#macro fieldCtrl field type entName readonly isAttr>
    <${field.control} class="${field.control}" isattr="${isAttr}" type="${type}" ctltype="${field.control}" id="${util.randomId()}"
    <@attrJson field=field type=type entName=entName readonly=readonly style='width:calc( 100% - 2px )'/>
    ></${field.control}>
</#macro>

<#macro attrJson field type entName readonly>
    :readonly="${readonly???string(readonly,'readonly')}" ctlid="${field.id}"
    <#if (type=='main')>
        :permission="permission.${field.name}" v-model="data.${field.name}"
        :valid="validFunc('main','${field.name}')"
        v-on:enter="enter('main','${field.name}')"
        v-on:valuechange="valuechange('main','${field.name}')"
    <#elseif (type=='onetoone')>
        :permission="permission.sub__${entName}.${field.name}"
        :valid="validFunc('${entName}','${field.name}',item)"
        v-model="data.sub__${entName}.${field.name}"
        v-on:enter="enter('${entName}','${field.name}',item)"
        v-on:valuechange="valuechange('${entName}','${field.name}',item)"
    <#else>
        :permission="permission.sub__${entName}.${field.name}" v-model="item.${field.name}" :data="item"
        :valid="validFunc('${entName}','${field.name}',index)"
        v-on:enter="enter('${entName}','${field.name}',item)"
        v-on:valuechange="valuechange('${entName}','${field.name}',item)"
        length="${field.length}" from="input"
    </#if>
</#macro>


