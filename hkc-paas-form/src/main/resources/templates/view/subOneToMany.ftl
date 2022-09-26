<#setting number_format="#">

<div class="rx-table-body active" ctltype="rx-table" tableid="${ent.id}" type="onetomany"
     id="${util.randomId()}" tablename="${ent.alias}">
    <div class="table-header-tool">
        <div class="title">
            <div>
                ${ent.name}
            </div>
        </div>
        <a-button-group>
            <a-button v-on:click="add('${ent.alias}','inner')">
                添加
            </a-button>
            <a-button v-on:click="remove('${ent.alias}','inner')">
                删除
            </a-button>
            <a-button v-on:click="up('${ent.alias}','inner')">
                上移
            </a-button>
            <a-button v-on:click="down('${ent.alias}','inner')">
                下移
            </a-button>
        </a-button-group>
    </div>
    <div class="rx-table-box">
        <table class="rx-table">
            <col style="width: 60px;"/>
            <#list ent.boAttrList as field>
                <col style="width: 160px;"/>
            </#list>
            <thead>
            <tr class="firstRow">
                <th>#</th>
                <#list ent.boAttrList as field>
                    <th>
                        ${field.comment}
                    </th>
                </#list>
            </tr>
            </thead>
            <tbody>
            <tr v-for="(item,index) in data.sub__${ent.alias}" :key="item.index_" v-select="item"
                :class="{active:item.selected}">
                <td><a-checkbox :checked="item.selected" @change='item=>item.selected==!item.selected'></a-checkbox></td>
                <#list ent.boAttrList as field>
                    <td>
                        <@fieldCtrl field=field type='onetomany' entName=ent.alias />
                    </td>
                </#list>
            </tr>
            </tbody>
        </table>
    </div>
</div>
