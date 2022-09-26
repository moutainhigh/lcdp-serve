<#setting number_format="#">
<table class="table-form two-column" style="width:100%;" ctltype="rx-two-table" borders="noborder">
    <colgroup>
        <col width="20%"/>
        <col width="80%"/>
    </colgroup>
    <caption>
        ${ent.name}
    </caption>
    <tbody>
    <#list ent.boAttrList as field>
        <tr <#if (field_index==0)>class="firstRow"</#if>>
            <td style="word-break: break-all;">
                ${field.comment}
            </td>
            <td>
                <@fieldCtrl field=field type='main' />
            </td>
        </tr>
    </#list>
    </tbody>
</table>