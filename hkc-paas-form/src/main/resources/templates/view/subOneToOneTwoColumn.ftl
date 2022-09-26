<table class="table-form four-column" style="width:100%;" id="${util.randomId()}" type="onetoone" ctltype="rx-table-onetoone" tableid="${ent.id}" tablename="${ent.alias}">
    <caption>
        ${ent.name}
    </caption>
    <colgroup>
        <col width="15%"/>
        <col width="35%"/>
        <col width="15%"/>
        <col width="35%"/>
    </colgroup>
    <tbody>
    <#list ent.boAttrList as field>
        <#if (field_index % 2==0)>
            <tr <#if (field_index==0)>class="firstRow"</#if>>
        </#if>
        <td style="word-break: break-all;">
            ${field.comment}
        </td>
        <td>
            <@fieldCtrl field=field type='onetoone' entName=ent.alias />
        </td>

        <#if field_index % 2 == 0 && !field_has_next>
            <td></td>
            <td></td>
        </#if>
        <#if field_index % 2 == 1 || !field_has_next>
            </tr>
        </#if>
    </#list>
    </tbody>
</table>