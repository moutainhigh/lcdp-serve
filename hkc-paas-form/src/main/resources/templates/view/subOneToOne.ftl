<table class="table-form two-column" style="width:100%;" id="${util.randomId()}" type="onetoone" ctltype="rx-table-onetoone" tableid="${ent.id}" tablename="${ent.alias}">
    <caption>
        ${ent.name}
    </caption>
    <colgroup>
        <col width="20%"/>
        <col width="80%"/>
    </colgroup>
    <tbody>
    <#list ent.boAttrList as field>
        <tr <#if (field_index==0)>class="firstRow"</#if>>
            <td style="word-break: break-all;">
                ${field.comment}
            </td>
            <td>
                <@fieldCtrl field=field type='onetoone' entName=ent.alias />
            </td>
        </tr>
    </#list>
    </tbody>
</table>
