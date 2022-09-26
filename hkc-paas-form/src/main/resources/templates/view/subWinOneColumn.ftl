<#setting number_format="#">
<div class="rx-table-body" ctltype="rx-table" type="onetomany" id="${util.randomId()}" tableid="${ent.id}" tablename="${ent.alias}" tabname="${ent.alias}" style="width:100%">
    <div class="table-header-tool">
        <div class="title">
            <div>
                ${ent.name}
            </div>
        </div>
        <a-button-group v-if="!getReadonly()">
            <a-button  v-if="getSubTablePermission('${ent.alias}','add')" v-on:click="add('${ent.alias}','window','${ent.name}')">
                添加
            </a-button>
            <a-button v-if="getSubTablePermission('${ent.alias}','edit')" v-on:click="edit('${ent.alias}','window','${ent.name}')">
                编辑
            </a-button>
            <a-button  v-if="getSubTablePermission('${ent.alias}','remove')" v-on:click="remove('${ent.alias}','window','${ent.name}')">
                删除
            </a-button>
            <a-button v-if="getSubTablePermission('${ent.alias}','up')" v-on:click="up('${ent.alias}','window','${ent.name}')">
                上移
            </a-button>
            <a-button v-if="getSubTablePermission('${ent.alias}','down')" v-on:click="down('${ent.alias}','window','${ent.name}')">
                下移
            </a-button>
        </a-button-group>
    </div>
    <div class="rx-table-box" style="display: none">
        <table class="rx-table">
            <colgroup>
                <col style="width: 60px;"/>
                <col style="width: 60px;"/>
                <#list ent.boAttrList as field>
                    <col style="width: 160px;"/>
                </#list>

            </colgroup>
            <thead>
            <tr class="firstRow">
                <th>#</th>
                <th style="text-align:center;">选择</th>
                <#list ent.boAttrList as field>
                    <th>
                        ${field.comment}
                    </th>
                </#list>
            </tr>
            </thead>
            <tbody>
            <tr v-if="item" v-for="(item,index) in data.sub__${ent.alias}" :key="item.index_" v-select="item" :class="{active:item.selected}" dblclick="edit('${ent.alias}')">
                <td>${index + 1}</td>
                <td style="text-align:center;"><a-checkbox :checked="item.selected" @change='item=>item.selected==!item.selected'></a-checkbox></td>
                <#list ent.boAttrList as field>
                    <td>
                        <@fieldCtrl field=field type='onetomany' entName=ent.alias readonly=true />
                    </td>
                </#list>
            </tr>
            </tbody>
        </table>
        <div class="tableEmpty" v-if="data.sub__${ent.alias} && data.sub__${ent.alias}.length <= 0">
            <a-empty></a-empty>
        </div>
    </div>
    <div class="rx-table-dialog"  v-if="0"   id="dialog_${ent.alias}" >
        <div class="dialog-header">
            ${ent.name}
        </div>

        <table class="table-detail column-two table-align" style="width:100%;">
            <colgroup>
                <col width="20%"/>
                <col width="80%"/>
            </colgroup>
            <tbody>
            <#list ent.boAttrList as field>
                <tr>
                    <td>${field.comment}</td>
                    <td>
                        <@fieldCtrl field=field type='onetomany' entName=ent.alias isAttr='false'/>
                    </td>
                </tr>
            </#list>
            </tbody>
        </table>
    </div>
</div>
