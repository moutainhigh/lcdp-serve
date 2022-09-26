INSERT INTO FORM_RULE(ID_,NAME_,PROMPT_,ALIAS_,REGULAR_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_)
values(         '1275596976204800002',         '正整数',         '请输入正整数！',         'positiveinteger',         '^[1-9]d*$',         '0',         '2',         '1',         to_date('2020-6-24 9:09:53','yyyy-MM-dd HH24:mi:ss'),         '1',         to_date('2020-12-20 21:23:59','yyyy-MM-dd HH24:mi:ss'));

INSERT INTO FORM_RULE(ID_,NAME_,PROMPT_,ALIAS_,REGULAR_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_)
values(         '1275669328796049409',         '手机号',         '请输入手机号!',         'phone',         '^1[3456789]d{9}$',         '0',         '2',         '1',         to_date('2020-6-24 13:57:23','yyyy-MM-dd HH24:mi:ss'),         '1',         to_date('2020-12-20 21:23:52','yyyy-MM-dd HH24:mi:ss'));

INSERT INTO FORM_RULE(ID_,NAME_,PROMPT_,ALIAS_,REGULAR_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_)
values(         '1277545456632532994',         '合法Email',         '必须输入合法Email地址!',         'email',         '^(w)+(.w+)*@(w)+((.w+)+)$',         '0',         '1',         '1',         to_date('2020-6-29 18:12:27','yyyy-MM-dd HH24:mi:ss'),         '1',         to_date('2020-12-20 21:04:47','yyyy-MM-dd HH24:mi:ss'));

INSERT INTO FORM_RULE(ID_,NAME_,PROMPT_,ALIAS_,REGULAR_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_)
values(         '1277545684584566786',         '身份证',         '必须输入15-18位数字',         'idcard',         '(^d{15}$)|(^d{18}$)|(^d{17}(d|X|x)$)',         '0',         '1',         '1',         to_date('2020-6-29 18:13:22','yyyy-MM-dd HH24:mi:ss'),         '1',         to_date('2020-12-20 21:23:37','yyyy-MM-dd HH24:mi:ss'));

INSERT INTO FORM_RULE(ID_,NAME_,PROMPT_,ALIAS_,REGULAR_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_)
values(         '1277545966160777217',         '英文或数字',         '必须输入英文或数字',         'isenglishandnumber',         '^[0-9a-zA-Z\_]+$',         '0',         '1',         '1',         to_date('2020-6-29 18:14:29','yyyy-MM-dd HH24:mi:ss'),         '',         to_date('2020-12-20 21:08:48','yyyy-MM-dd HH24:mi:ss'));

INSERT INTO FORM_RULE(ID_,NAME_,PROMPT_,ALIAS_,REGULAR_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_)
values(         '1277546396345372673',         '数字',         '必须输入数字',         'number',         '^[-+]?[0-9]+(.[0-9]+)?$',         '0',         '1',         '1',         to_date('2020-6-29 18:16:11','yyyy-MM-dd HH24:mi:ss'),         '',         to_date('2020-12-20 21:08:10','yyyy-MM-dd HH24:mi:ss'));

INSERT INTO FORM_RULE(ID_,NAME_,PROMPT_,ALIAS_,REGULAR_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_)
values(         '1277546588067008514',         '浮点数',         '必须输入浮点数',         'float',         '^-?([1-9]d*.d*|0.d*[1-9]d*|0?.0+|0|d*)$',         '0',         '1',         '1',         to_date('2020-6-29 18:16:57','yyyy-MM-dd HH24:mi:ss'),         '',         to_date('2020-12-20 21:07:36','yyyy-MM-dd HH24:mi:ss'));

INSERT INTO FORM_RULE(ID_,NAME_,PROMPT_,ALIAS_,REGULAR_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_)
values(         '1277547652346482689',         'ip',         '必须输入合法IP地址',         'ip',         '^d+.d+.d+.d+$',         '0',         '1',         '1',         to_date('2020-6-29 18:21:11','yyyy-MM-dd HH24:mi:ss'),         '',         to_date('2020-12-20 21:06:30','yyyy-MM-dd HH24:mi:ss'));


INSERT INTO FORM_TEMPLATE(ID_,NAME_,ALIAS_,TEMPLATE_,TYPE_,CATEGORY_,TENANT_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_,CREATE_DEP_ID_)
values(         '1275710436348100609',         '主表单列模板',         'oneColumn',         '',         'pc',         'main',         '0',         '1',         to_date('2020-6-24 16:40:44','yyyy-MM-dd HH24:mi:ss'),         '1',         to_date('2020-12-11 15:20:50','yyyy-MM-dd HH24:mi:ss'),         '2');

DECLARE
content CLOB:='<#setting number_format="#">
<table class="table-form two-column" style="width:100%;">
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
                <@fieldCtrl field=field type=''main'' />
            </td>
        </tr>
        </#list>
    </tbody>
</table>';
BEGIN
update FORM_TEMPLATE set TEMPLATE_=content where id_='1275710436348100609';
commit;
end ;
/

INSERT INTO FORM_TEMPLATE(ID_,NAME_,ALIAS_,TEMPLATE_,TYPE_,CATEGORY_,TENANT_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_,CREATE_DEP_ID_)
       values(         '1277077170606948354',         '主表双列模版',         'twocolumn',         '',         'pc',         'main',         '0',         '1',         to_date('2020-6-28 11:11:39','yyyy-MM-dd HH24:mi:ss'),         '1',         to_date('2020-12-11 15:22:02','yyyy-MM-dd HH24:mi:ss'),         '2');

            DECLARE
content CLOB:='<#setting number_format="#">
<table class="table-form four-column" style="width:100%;">
    <caption>
        ${ent.name}
    </caption>
   <col width="15%"/>
            <col width="35%"/>
            <col width="15%"/>
            <col width="35%"/>
    <tbody>
    <#list ent.boAttrList as field>
        <#if (field_index % 2==0)>
        <tr <#if (field_index==0)>class="firstRow"</#if>>
        </#if>
            <td style="word-break: break-all;">
                ${field.comment}
            </td>
            <td>
                <@fieldCtrl field=field type=''main'' />
            </td>

            <#if field_index % 2 == 0 ' || '&' || '' || '&' || ' !field_has_next>
                <td></td>
                <td></td>
            </#if>
            <#if field_index % 2 == 1 || !field_has_next>
                </tr>
            </#if>
    </#list>
    </tbody>
</table>';
BEGIN
update FORM_TEMPLATE set TEMPLATE_=content where id_='1277077170606948354';
commit;
end ;
/

INSERT INTO FORM_TEMPLATE(ID_,NAME_,ALIAS_,TEMPLATE_,TYPE_,CATEGORY_,TENANT_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_,CREATE_DEP_ID_)
       values(         '1277080670808162306',         '字段控件模版',         'fieldCtrl',         '',         'pc',         'field',         '0',         '1',         to_date('2020-6-28 11:25:34','yyyy-MM-dd HH24:mi:ss'),         '',         to_date('2020-12-11 17:57:31','yyyy-MM-dd HH24:mi:ss'),         '2');

            DECLARE
content CLOB:='<#setting number_format="#">
<#macro fieldCtrl field type entName readonly isAttr>
    <${field.control} class="${field.control}" isattr="${isAttr}" type="${type}" ctltype="${field.control}" id="${util.randomId()}"
                        <@attrJson field=field type=type entName=entName readonly=readonly/>
            ></${field.control}>
</#macro>

<#macro attrJson field type entName readonly>
    :readonly="${readonly???string(readonly,''readonly'')}" ctlid="${field.id}"
    <#if (type==''main'')>
        :permission="permission.${field.name}" v-model="data.${field.name}"
        :valid="validFunc(''main'',''${field.name}'')"
        v-on:enter="enter(''main'',''${field.name}'')"
        v-on:valuechange="valuechange(''main'',''${field.name}'')"
    <#elseif (type==''onetoone'')>
        :permission="permission.sub__${entName}.${field.name}"
        :valid="validFunc(''${entName}'',''${field.name}'',item)"
        v-model="data.sub__${entName}.${field.name}"
        v-on:enter="enter(''${entName}'',''${field.name}'',item)"
        v-on:valuechange="valuechange(''${entName}'',''${field.name}'',item)"
    <#else>
        :permission="permission.sub__${entName}.${field.name}" v-model="item.${field.name}" :data="item"
        :valid="validFunc(''${entName}'',''${field.name}'',index)"
        v-on:enter="enter(''${entName}'',''${field.name}'',item)"
        v-on:valuechange="valuechange(''${entName}'',''${field.name}'',item)"
        length="${field.length}" from="input"
    </#if>
</#macro>


';
BEGIN
update FORM_TEMPLATE set TEMPLATE_=content where id_='1277080670808162306';
commit;
end ;
/

INSERT INTO FORM_TEMPLATE(ID_,NAME_,ALIAS_,TEMPLATE_,TYPE_,CATEGORY_,TENANT_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_,CREATE_DEP_ID_)
       values(         '1277081573669847041',         '子表行模版',         'subOneToMany',         '',         'pc',         'onetomany',         '0',         '1',         to_date('2020-6-28 11:29:09','yyyy-MM-dd HH24:mi:ss'),         '',         to_date('2020-12-11 15:55:03','yyyy-MM-dd HH24:mi:ss'),         '2');

            DECLARE
content CLOB:='<#setting number_format="#">

            <div class="rx-table-body active" ctltype="rx-table" tableid="${ent.id}" type="onetomany"
                 id="${util.randomId()}" tablename="${ent.alias}">
                <div class="table-header-tool">
                    <div class="title">
                        <div>
                        ${ent.name}
                        </div>
                    </div>
                    <a-button-group>
                        <a-button v-on:click="add(''${ent.alias}'',''inner'')">
                            添加
                        </a-button>
                        <a-button v-on:click="remove(''${ent.alias}'',''inner'')">
                            删除
                        </a-button>
                        <a-button v-on:click="up(''${ent.alias}'',''inner'')">
                            上移
                        </a-button>
                        <a-button v-on:click="down(''${ent.alias}'',''inner'')">
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
                            <td><a-checkbox :checked="item.selected" @change=''item=>item.selected==!item.selected''></a-checkbox></td>
                            <#list ent.boAttrList as field>
                                <td>
                                    <@fieldCtrl field=field type=''onetomany'' entName=ent.alias />
                                </td>
                            </#list>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
';
BEGIN
update FORM_TEMPLATE set TEMPLATE_=content where id_='1277081573669847041';
commit;
end ;
/

INSERT INTO FORM_TEMPLATE(ID_,NAME_,ALIAS_,TEMPLATE_,TYPE_,CATEGORY_,TENANT_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_,CREATE_DEP_ID_)
       values(         '1277085691964669953',         '子表双列模版',         'subOneToOneTwoColumn',         '',         'pc',         'onetoone',         '0',         '1',         to_date('2020-6-28 11:45:31','yyyy-MM-dd HH24:mi:ss'),         '1',         to_date('2020-12-11 17:50:13','yyyy-MM-dd HH24:mi:ss'),         '2');

            DECLARE
content CLOB:='<table class="table-form four-column" style="width:100%;" id="${util.randomId()}" type="onetoone" ctltype="rx-table-onetoone" tableid="${ent.id}" tablename="${ent.alias}">
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
            <@fieldCtrl field=field type=''onetoone'' entName=ent.alias />
        </td>

        <#if field_index % 2 == 0 ' || '&' || '' || '&' || ' !field_has_next>
            <td></td>
            <td></td>
        </#if>
        <#if field_index % 2 == 1 || !field_has_next>
            </tr>
        </#if>
    </#list>
    </tbody>
</table>';
BEGIN
update FORM_TEMPLATE set TEMPLATE_=content where id_='1277085691964669953';
commit;
end ;
/

INSERT INTO FORM_TEMPLATE(ID_,NAME_,ALIAS_,TEMPLATE_,TYPE_,CATEGORY_,TENANT_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_,CREATE_DEP_ID_)
       values(         '1277085945392906241',         '子表单列模版',         'subWinOneColumn',         '',         'pc',         'onetomany',         '0',         '1',         to_date('2020-6-28 11:46:31','yyyy-MM-dd HH24:mi:ss'),         '1',         to_date('2020-12-11 18:17:39','yyyy-MM-dd HH24:mi:ss'),         '2');

            DECLARE
content CLOB:='<#setting number_format="#">
<div class="rx-table-body" ctltype="rx-table" type="onetomany" id="${util.randomId()}" tableid="${ent.id}" tablename="${ent.alias}" tabname="${ent.alias}" style="width:100%">
        <div class="table-header-tool">
            <div class="title">
                <div>
                    ${ent.name}
                </div>
            </div>
            <a-button-group v-if="!getReadonly()">
                 <a-button  v-if="getSubTablePermission(''${ent.alias}'',''add'')" v-on:click="add(''${ent.alias}'',''window'',''${ent.name}'')">
                    添加
                </a-button>
  				<a-button v-if="getSubTablePermission(''${ent.alias}'',''edit'')" v-on:click="edit(''${ent.alias}'',''window'',''${ent.name}'')">
                    编辑
                </a-button>
                <a-button  v-if="getSubTablePermission(''${ent.alias}'',''remove'')" v-on:click="remove(''${ent.alias}'',''window'',''${ent.name}'')">
                    删除
                </a-button>
                <a-button v-if="getSubTablePermission(''${ent.alias}'',''up'')" v-on:click="up(''${ent.alias}'',''window'',''${ent.name}'')">
                    上移
                </a-button>
                <a-button v-if="getSubTablePermission(''${ent.alias}'',''down'')" v-on:click="down(''${ent.alias}'',''window'',''${ent.name}'')">
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
                    <tr v-if="item" v-for="(item,index) in data.sub__${ent.alias}" :key="item.index_" v-select="item" :class="{active:item.selected}" dblclick="edit(''${ent.alias}'')">
                        <td>${index + 1}</td>
						<td style="text-align:center;"><a-checkbox :checked="item.selected" @change=''item=>item.selected==!item.selected''></a-checkbox></td>
                        <#list ent.boAttrList as field>
                            <td>
                                <@fieldCtrl field=field type=''onetomany'' entName=ent.alias readonly=true />
                            </td>
                        </#list>
                    </tr>
                </tbody>
            </table>
            <div class="tableEmpty" v-if="data.sub__${ent.alias} ' || '&' || '' || '&' || ' data.sub__${ent.alias}.length <= 0">
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
                            <@fieldCtrl field=field type=''onetomany'' entName=ent.alias isAttr=''false''/>
                        </td>
                      </tr>
                    </#list>
                </tbody>
            </table>
        </div>
    </div>
';
BEGIN
update FORM_TEMPLATE set TEMPLATE_=content where id_='1277085945392906241';
commit;
end ;
/

INSERT INTO FORM_TEMPLATE(ID_,NAME_,ALIAS_,TEMPLATE_,TYPE_,CATEGORY_,TENANT_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_,CREATE_DEP_ID_)
       values(         '1277086151886880770',         '子表双列模版',         'subWinTwoColumn',         '',         'pc',         'onetomany',         '0',         '1',         to_date('2020-6-28 11:47:20','yyyy-MM-dd HH24:mi:ss'),         '1',         to_date('2020-12-11 18:14:24','yyyy-MM-dd HH24:mi:ss'),         '2');

            DECLARE
content CLOB:='<#setting number_format="#">
<div class="rx-table-body" ctltype="rx-table" type="onetomany" id="${util.randomId()}" tableid="${ent.id}" tablename="${ent.alias}" tabname="${ent.alias}" style="width:100%">
        <div class="table-header-tool">
            <div class="title">
                <div>
                    ${ent.name}
                </div>
            </div>
            <a-button-group v-if="!getReadonly()">
                 <a-button  v-if="getSubTablePermission(''${ent.alias}'',''add'')" v-on:click="add(''${ent.alias}'',''window'',''${ent.name}'')">
                    添加
                </a-button>
  				<a-button v-if="getSubTablePermission(''${ent.alias}'',''edit'')" v-on:click="edit(''${ent.alias}'',''window'',''${ent.name}'')">
                    编辑
                </a-button>
                <a-button  v-if="getSubTablePermission(''${ent.alias}'',''remove'')" v-on:click="remove(''${ent.alias}'',''window'',''${ent.name}'')">
                    删除
                </a-button>
                <a-button v-if="getSubTablePermission(''${ent.alias}'',''up'')" v-on:click="up(''${ent.alias}'',''window'',''${ent.name}'')">
                    上移
                </a-button>
                <a-button v-if="getSubTablePermission(''${ent.alias}'',''down'')" v-on:click="down(''${ent.alias}'',''window'',''${ent.name}'')">
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
                    <tr v-if="item" v-for="(item,index) in data.sub__${ent.alias}" :key="item.index_" v-select="item" :class="{active:item.selected}" dblclick="edit(''${ent.alias}'')">
                        <td>${index + 1}</td>
   						<td style="text-align:center;"><a-checkbox :checked="item.selected" @change=''item=>item.selected==!item.selected''></a-checkbox></td>
                        <#list ent.boAttrList as field>
                            <td>
                                <@fieldCtrl field=field type=''onetomany'' entName=ent.alias readonly=true />
                            </td>
                        </#list>
                    </tr>
                </tbody>
            </table>
            <div class="tableEmpty" v-if="data.sub__${ent.alias} ' || '&' || '' || '&' || ' data.sub__${ent.alias}.length <= 0">
                <a-empty></a-empty>
            </div>
        </div>
        <div class="rx-table-dialog"  v-if="0"   id="dialog_${ent.alias}" >
            <div class="dialog-header">
                ${ent.name}
            </div>

            <table class="table-detail column-four table-align" style="width:100%;">
                <colgroup>
                    <col width="15%"/>
                    <col width="35%"/>
                    <col width="15%"/>
                    <col width="35%"/>
                </colgroup>
                <tbody>
                   <#list ent.boAttrList as field>
                        <#if (field_index % 2==0)>
                            <tr>
                        </#if>
                        <td style="word-break: break-all;">
                            ${field.comment}
                        </td>
                        <td>
                            <@fieldCtrl field=field type=''onetomany'' entName=ent.alias isAttr=''false'' />
                        </td>

                        <#if field_index % 2 == 0 ' || '&' || '' || '&' || ' !field_has_next>
                            <td></td>
                            <td></td>
                        </#if>
                        <#if field_index % 2 == 1 || !field_has_next>
                            </tr>
                        </#if>
                    </#list>
                </tbody>
            </table>
        </div>
    </div>';
BEGIN
update FORM_TEMPLATE set TEMPLATE_=content where id_='1277086151886880770';
commit;
end ;
/

INSERT INTO FORM_TEMPLATE(ID_,NAME_,ALIAS_,TEMPLATE_,TYPE_,CATEGORY_,TENANT_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_,CREATE_DEP_ID_)
       values(         '1277087373637304321',         '子表单列模版',         'subOneToOne',         '',         'pc',         'onetoone',         '0',         '1',         to_date('2020-6-28 11:52:12','yyyy-MM-dd HH24:mi:ss'),         '1',         to_date('2020-12-11 17:51:11','yyyy-MM-dd HH24:mi:ss'),         '2');

            DECLARE
content CLOB:='<table class="table-form two-column" style="width:100%;" id="${util.randomId()}" type="onetoone" ctltype="rx-table-onetoone" tableid="${ent.id}" tablename="${ent.alias}">
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
            <@fieldCtrl field=field type=''onetoone'' entName=ent.alias />
        </td>
    </tr>
    </#list>
    </tbody>
</table>
';
BEGIN
update FORM_TEMPLATE set TEMPLATE_=content where id_='1277087373637304321';
commit;
end ;
/

INSERT INTO FORM_TEMPLATE(ID_,NAME_,ALIAS_,TEMPLATE_,TYPE_,CATEGORY_,TENANT_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_,CREATE_DEP_ID_)
        values(         '1278256114231779329',         '字段控件模板',         'fieldCtrl',         '',         'print',         'field',         '0',         '1',         to_date('2020-7-1 17:16:21','yyyy-MM-dd HH24:mi:ss'),         '1',         to_date('2021-12-16 16:34:22','yyyy-MM-dd HH24:mi:ss'),         '');

            DECLARE
content CLOB:='<#function getPre pre>
 	<#assign rtn><#if pre=="">data.<#elseif pre="item_">item.<#else>data.SUB_${pre}.</#if></#assign>

	<#return rtn>
</#function>

<#macro getField attr pre>
	<#switch attr.control>
		<#default>
			<#if (attr.isSingle==1)>
			 <#if (attr.control=="rx-barcode")>
			 	  <img src="/api/api-form/form/core/formPdfTemplate/getQrCode?content=<#noparse>${</#noparse>${getPre(pre)}${attr.name}<#noparse>}</#noparse>"></img>
			 <#else>
				<#noparse>${</#noparse>${getPre(pre)}${attr.name}<#noparse>}</#noparse>
			 </#if>

			<#else>
                <#if (attr.control=="rx-address")>
                    <#noparse>${("${(</#noparse>${getPre(pre)}${attr.name}<#noparse>?? ' || '&' || '' || '&' || ' </#noparse>${getPre(pre)}${attr.name}<#noparse>!='''')?then(</#noparse>${getPre(pre)}${attr.name}<#noparse>,''{}'')}"?eval).province}</#noparse>
                    <#noparse>${("${(</#noparse>${getPre(pre)}${attr.name}<#noparse>?? ' || '&' || '' || '&' || ' </#noparse>${getPre(pre)}${attr.name}<#noparse>!='''')?then(</#noparse>${getPre(pre)}${attr.name}<#noparse>,''{}'')}"?eval).city}</#noparse>
                    <#noparse>${("${(</#noparse>${getPre(pre)}${attr.name}<#noparse>?? ' || '&' || '' || '&' || ' </#noparse>${getPre(pre)}${attr.name}<#noparse>!='''')?then(</#noparse>${getPre(pre)}${attr.name}<#noparse>,''{}'')}"?eval).county}</#noparse>
                    <#noparse>${("${(</#noparse>${getPre(pre)}${attr.name}<#noparse>?? ' || '&' || '' || '&' || ' </#noparse>${getPre(pre)}${attr.name}<#noparse>!='''')?then(</#noparse>${getPre(pre)}${attr.name}<#noparse>,''{}'')}"?eval).address}</#noparse>
                <#else>
                	<#noparse>${("${(</#noparse>${getPre(pre)}${attr.name}<#noparse>?? ' || '&' || '' || '&' || ' </#noparse>${getPre(pre)}${attr.name}<#noparse>!='''')?then(</#noparse>${getPre(pre)}${attr.name}<#noparse>,''{}'')}"?eval).label}</#noparse>
                </#if>
			</#if>
	</#switch>
</#macro>';
BEGIN
update FORM_TEMPLATE set TEMPLATE_=content where id_='1278256114231779329';
commit;
end ;
/

INSERT INTO FORM_TEMPLATE(ID_,NAME_,ALIAS_,TEMPLATE_,TYPE_,CATEGORY_,TENANT_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_,CREATE_DEP_ID_)
       values(         '1278258262659145730',         '子表单列模版',         'subOneToOne',         '',         'print',         'onetoone',         '0',         '1',         to_date('2020-7-1 17:24:53','yyyy-MM-dd HH24:mi:ss'),         '1',         to_date('2020-7-1 17:38:37','yyyy-MM-dd HH24:mi:ss'),         '');

            DECLARE
content CLOB:='<table class="table-form two-column" style="width:100%;" id="${util.randomId()}" type="onetoone" ctltype="rx-table-onetoone" tableid="${ent.id}" tablename="${ent.alias}">
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
            <@getField attr=field pre=ent.name />
        </td>
    </tr>
    </#list>
    </tbody>
</table>';
BEGIN
update FORM_TEMPLATE set TEMPLATE_=content where id_='1278258262659145730';
commit;
end ;
/

INSERT INTO FORM_TEMPLATE(ID_,NAME_,ALIAS_,TEMPLATE_,TYPE_,CATEGORY_,TENANT_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_,CREATE_DEP_ID_)
       values(         '1278258334415298562',         '子表双列模版',         'subOneToOneTwoColumn',         '',         'print',         'onetoone',         '0',         '1',         to_date('2020-7-1 17:25:10','yyyy-MM-dd HH24:mi:ss'),         '1',         to_date('2020-7-1 17:38:32','yyyy-MM-dd HH24:mi:ss'),         '');

            DECLARE
content CLOB:='<table class="table-form four-column" style="width:100%;" id="${util.randomId()}" type="onetoone" ctltype="rx-table-onetoone" tableid="${ent.id}" tablename="${ent.alias}">
    <caption>
        ${ent.name}
    </caption>
    <tbody>
    <#list ent.boAttrList as field>
        <#if (field_index % 2==0)>
            <tr <#if (field_index==0)>class="firstRow"</#if>>
        </#if>
        <td style="word-break: break-all;">
            ${field.comment}
        </td>
        <td>
            <@getField attr=field pre=ent.name />
        </td>

        <#if field_index % 2 == 0 ' || '&' || '' || '&' || ' !field_has_next>
            <td></td>
            <td></td>
        </#if>
        <#if field_index % 2 == 1 || !field_has_next>
            </tr>
        </#if>
    </#list>
    </tbody>
</table>';
BEGIN
update FORM_TEMPLATE set TEMPLATE_=content where id_='1278258334415298562';
commit;
end ;
/

INSERT INTO FORM_TEMPLATE(ID_,NAME_,ALIAS_,TEMPLATE_,TYPE_,CATEGORY_,TENANT_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_,CREATE_DEP_ID_)
       values(         '1278258401062789122',         '主表双列模版',         'twoColumn',         '',         'print',         'main',         '0',         '1',         to_date('2020-7-1 17:25:26','yyyy-MM-dd HH24:mi:ss'),         '1',         to_date('2020-7-1 17:44:29','yyyy-MM-dd HH24:mi:ss'),         '');

            DECLARE
content CLOB:='<#setting number_format="#">
<table class="table-form four-column" style="width:100%;">
    <caption>
        ${ent.name}
    </caption>
    <tbody>
    <#list ent.boAttrList as field>
        <#if (field_index % 2==0)>
        <tr <#if (field_index==0)>class="firstRow"</#if>>
        </#if>
            <td style="word-break: break-all;">
                ${field.comment}
            </td>
            <td>
                <@getField attr=field pre="" />
            </td>

            <#if field_index % 2 == 0 ' || '&' || '' || '&' || ' !field_has_next>
                <td></td>
                <td></td>
            </#if>
            <#if field_index % 2 == 1 || !field_has_next>
                </tr>
            </#if>
    </#list>
    </tbody>
</table>';
BEGIN
update FORM_TEMPLATE set TEMPLATE_=content where id_='1278258401062789122';
commit;
end ;
/

INSERT INTO FORM_TEMPLATE(ID_,NAME_,ALIAS_,TEMPLATE_,TYPE_,CATEGORY_,TENANT_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_,CREATE_DEP_ID_)
       values(         '1278258462500954113',         '主表单列模板',         'oneColumn',         '',         'print',         'main',         '0',         '1',         to_date('2020-7-1 17:25:41','yyyy-MM-dd HH24:mi:ss'),         '1',         to_date('2020-7-1 17:44:16','yyyy-MM-dd HH24:mi:ss'),         '');

            DECLARE
content CLOB:='<#setting number_format="#">
<table class="table-form two-column" style="width:100%;">
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
                <@getField attr=field pre="" />
            </td>
        </tr>
        </#list>
    </tbody>
</table>';
BEGIN
update FORM_TEMPLATE set TEMPLATE_=content where id_='1278258462500954113';
commit;
end ;
/

INSERT INTO FORM_TEMPLATE(ID_,NAME_,ALIAS_,TEMPLATE_,TYPE_,CATEGORY_,TENANT_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_,CREATE_DEP_ID_)
       values(         '1278260208514191361',         '子表行模式',         'dOneColumn',         '',         'print',         'onetomany',         '0',         '1',         to_date('2020-7-1 17:32:37','yyyy-MM-dd HH24:mi:ss'),         '1',         to_date('2021-7-29 11:25:00','yyyy-MM-dd HH24:mi:ss'),         '');

            DECLARE
content CLOB:='<#setting number_format="#">
<table class="table-form two-column" style="width:100%;" data-sort="sortDisabled">
    <tr class="firstRow">
        <td style="word-break: break-all;" rowspan="1" colspan="1">
            <div class="rx-table-body active" ctltype="rx-table" tableid="${ent.id}" type="onetomany"
                 id="${util.randomId()}" tablename="${ent.alias}">
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
                        <tr rx-list="sub__${ent.alias}" :key="item.index_" v-select="item"
                            :class="{active:item.selected}">
                            <td><#noparse>${item_index+1}</#noparse></td>
                            <#list ent.boAttrList as field>
                                <td>
                                    <@getField attr=field pre="item_"/>
                                </td>
                            </#list>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </td>
    </tr>
</table>';
BEGIN
update FORM_TEMPLATE set TEMPLATE_=content where id_='1278260208514191361';
commit;
end ;
/


INSERT INTO FORM_TEMPLATE(ID_,NAME_,ALIAS_,TEMPLATE_,TYPE_,CATEGORY_,TENANT_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_,CREATE_DEP_ID_)
        values(         '1471759491079692289',         '审批历史',         'approvalHistory',         '',         'print',         'field',         '1',         '1',         to_date('2021-12-17 16:29:41','yyyy-MM-dd HH24:mi:ss'),         '1',         to_date('2021-12-17 17:54:59','yyyy-MM-dd HH24:mi:ss'),         '1');

            DECLARE
content CLOB:='<table class="rx-table">
	<thead>
		<tr class="firstRow">
		<th>创建时间</th>
		<th>处理时间</th>
		<th>停留时间</th>
		<th>审批节点</th>
		<th>操作人</th>
		<th>类型</th>
		<th>意见</th>
		</tr>
		</thead>
		<tbody>
		  <tr rx-list="history" :key="item.index_">
		   <td>${item.createTime}</td>
		   <td>${item.completeTime}</td>
			<td>${item.duration}秒</td>
			<td>${item.nodeName}</td>
			<td>${item.createBy}</td>
			<td>${item.checkStatus}</td>
			<td>${item.remark}</td>
		  </tr>
		  </#list>
	</tbody>
 </table>';
BEGIN
update FORM_TEMPLATE set TEMPLATE_=content where id_='1471759491079692289';
commit;
end ;
/


INSERT INTO FORM_CUSTOM(ID_, CATEGORY_ID_, NAME_, ALIAS_, TYPE_, JSON_, TENANT_ID_, CREATE_DEP_ID_, CREATE_BY_, CREATE_TIME_, UPDATE_BY_, UPDATE_TIME_, APP_ID_, HOME_PAGE_)
       values(         '1426059877513220098',         '1273175507561000961',         '门户',         'portal',         'drag',         '[{"x":17,"y":11,"w":7,"h":66,"i":1628834541030,"id":"mjqxstv541030","config":{"colId":"1262555793443348482","name":"我的日程"},"defConf":"column","active":false,"main":false,"moved":false},{"x":0,"y":0,"w":24,"h":11,"i":1628834651902,"id":"kqbunwx651902","config":{"colId":"1406144016241942530","name":"消息盒子-门户"},"defConf":"column","active":false,"main":false,"moved":false},{"x":0,"y":11,"w":9,"h":31,"i":1628840670696,"id":"zzwckmh670696","config":{"colId":"1406061558150025218","name":"待办事项-门户"},"defConf":"column","active":false,"main":false,"moved":false},{"x":9,"y":11,"w":8,"h":31,"i":1628841387176,"id":"zuivgli387176","config":{"colId":"1426090204717772802","name":"已办事项-门户"},"defConf":"column","active":false,"main":false,"moved":false},{"x":0,"y":42,"w":9,"h":35,"i":1628842581367,"id":"iramkmx581367","config":{"colId":"1392738215320346625","name":"柱状图"},"defConf":"column","active":false,"main":false,"moved":false},{"x":9,"y":42,"w":8,"h":35,"i":1628843171614,"id":"bkvcctt171615","config":{"colId":"1392739711713796097","name":"漏斗图"},"defConf":"column","active":false,"main":false,"moved":false}]',         '1',         '1',         '1',         to_date('2021-8-13 13:55:44','yyyy-MM-dd HH24:mi:ss'),         '1',         to_date('2021-8-13 16:26:20','yyyy-MM-dd HH24:mi:ss'),         '',         '0');

INSERT INTO FORM_CODEGEN_TEMPLATE(ID_, NAME_, FILE_NAME_, PATH_, SINGLE_, ENABLED_, CONTENT_, TENANT_ID_, CREATE_DEP_ID_, CREATE_BY_, CREATE_TIME_, UPDATE_BY_, UPDATE_TIME_)
values(         '1424623584797523970',         '控制器模板',         '{class}Controller.java',       'src/main/java/com/redxun/{system}/{package}/controller',      'NO',      'YES',         '',       '1',      '1',       '1',       to_date('2021-8-9','yyyy-MM-dd HH24:mi:ss'),     '1',        to_date('2021-8-19','yyyy-MM-dd HH24:mi:ss'));

DECLARE
content CLOB:='<#assign package=model.vars.package>
<#assign class=model.vars.class>
<#assign classVar=model.vars.classVar>
<#assign comment=model.name>
<#assign system=vars.system>
<#assign domain=vars.domain>
<#assign tableName=model.tableName>
<#assign colList=model.boAttrList>

package ${domain}.${system}.${package}.controller;

import com.redxun.common.base.db.BaseService;
import com.redxun.form.codegen.controller.CodeGenBaseController;
import ${domain}.${system}.${package}.entity.${class};
import ${domain}.${system}.${package}.service.${class}ServiceImpl;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.redxun.common.annotation.ClassDefine;


/**
* [${model.name}] 控制器代码
* @author: ${vars.developer}
* @company: ${vars.company}
* @date: ${vars.date}
*/
@Slf4j
@RestController
@RequestMapping("/${system}/${package}/${classVar}")
@Api(tags = "${comment}")
@ClassDefine(title = "${comment}",alias = "${class}Controller",path = "/${system}/${package}/${classVar}",packages = "${package}",packageName = "子系统名称")
public class ${class}Controller extends CodeGenBaseController<${class}> {

	@Autowired
	${class}ServiceImpl ${classVar}Service;



	@Override
	public BaseService getBaseService() {
		return ${classVar}Service;
	}

	@Override
	public String getComment() {
		return "${comment}";
	}

	@Override
	public String getFormAlias() {
		return "${formAlias}";
	}


}';
BEGIN
update FORM_CODEGEN_TEMPLATE set CONTENT_=content where id_='1424623584797523970';
commit;
end ;
/

INSERT INTO FORM_CODEGEN_TEMPLATE(ID_, NAME_, FILE_NAME_, PATH_, SINGLE_, ENABLED_, CONTENT_, TENANT_ID_, CREATE_DEP_ID_, CREATE_BY_, CREATE_TIME_, UPDATE_BY_, UPDATE_TIME_)
       values(         '1424627869878845442',         '实体模板',         '{class}.java',       'src/main/java/com/redxun/{system}/{package}/entity',      'NO',      'YES',         '',       '1',      '1',       '1',       to_date('2021-8-9','yyyy-MM-dd HH24:mi:ss'),     '1',        to_date('2021-8-19','yyyy-MM-dd HH24:mi:ss'));

            DECLARE
content CLOB:='<#assign package=model.vars.package>
<#assign class=model.vars.class>
<#assign classVar=model.vars.classVar>
<#assign comment=model.name>
<#assign system=vars.system>
<#assign domain=vars.domain>
<#assign tableName=model.tableName>
<#assign colList=model.boAttrList>
<#assign idField=convertUnderLine(model.idField)>


/**
* [${model.name}]实体类定义
* @author: ${vars.developer}
* @company: ${vars.company}
* @date: ${vars.date}
*/
package ${domain}.${system}.${package}.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonCreator;
import ${domain}.common.base.entity.BaseExtEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import java.util.ArrayList;
import java.util.List;
import org.apache.ibatis.type.JdbcType;
import java.util.Date;


@Setter
@Getter
@Accessors(chain = true)
@TableName(value = "${tableName}")
public class ${class}  extends BaseExtEntity<java.lang.String> {

    @JsonCreator
    public ${class}() {
    }

	//主键
    @TableId(value = "${model.idField}",type = IdType.INPUT)
	private String ${idField};

	@Override
    public String getPkId() {
        return this.${idField};
    }

    @Override
    public void setPkId(String pkId) {
        this.${idField}=pkId;
    }


    <#list colList as col>
    //${col.comment}
    @TableField(value = "${col.fieldName}",jdbcType=JdbcType.${getJdbcType(col)})
    private ${getJavaType(col)} ${col.name};
    </#list>


	<#if model.boEntityList?size gt 0 >
  <#list model.boEntityList as subEnt>
    @TableField(exist=false)
	private List<${subEnt.vars.class}> ${subEnt.vars.classVar}List=new ArrayList<>();
		</#list>
	</#if>

}


<#function getJdbcType col>
<#assign dbtype=col.dataType?lower_case>
<#assign rtn>
	<#if  dbtype=="number"  >
		NUMERIC
	<#elseif (dbtype=="date")>
		DATE
	<#else>
		VARCHAR
	</#if>
</#assign>
<#return rtn?trim>
</#function>


<#function getJavaType col>
<#assign dbtype=col.dataType?lower_case>
<#assign rtn>
<#if  dbtype=="number"  >
    <#if col.decimalLength==0>
         <#if col.length gt 10 >
            long
         <#else>
            int
         </#if>
    <#else>
         double
    </#if>
<#elseif (dbtype=="date")>
	Date
<#else>
	String
</#if>
</#assign>
 <#return rtn?trim>
</#function>


<#function convertUnderLine field>
	<#if field?index_of("_")==-1>
		<#assign rtn>${field?lower_case?trim}</#assign>
		<#return rtn>
	</#if>

	<#if field?index_of("F_")==0>
		<#assign rtn><#list (field?substring(2))?split("_") as x><#if (x_index==0)><#if x?length==1>${x?upper_case?trim}<#else>${x?lower_case?trim}</#if><#else>${x?lower_case?cap_first?trim}</#if></#list></#assign>
		<#return rtn>
	</#if>

	<#assign rtn><#list field?split("_") as x><#if (x_index==0)><#if x?length==1>${x?upper_case?trim}<#else>${x?lower_case?trim}</#if><#else>${x?lower_case?cap_first?trim}</#if></#list></#assign>
 <#return rtn>
</#function>

';
BEGIN
update FORM_CODEGEN_TEMPLATE set CONTENT_=content where id_='1424627869878845442';
commit;
end ;
/

INSERT INTO FORM_CODEGEN_TEMPLATE(ID_, NAME_, FILE_NAME_, PATH_, SINGLE_, ENABLED_, CONTENT_, TENANT_ID_, CREATE_DEP_ID_, CREATE_BY_, CREATE_TIME_, UPDATE_BY_, UPDATE_TIME_)
       values(         '1424629106896535554',         'Mapping文件',         '{class}Mapper.xml',       'src/main/resources/mapper/{system}/{package}',      'NO',      'YES',         '',       '1',      '1',       '1',       to_date('2021-8-9','yyyy-MM-dd HH24:mi:ss'),     '1',        to_date('2021-8-18','yyyy-MM-dd HH24:mi:ss'));

            DECLARE
content CLOB:='<#assign package=model.vars.package>
<#assign class=model.vars.class>
<#assign system=vars.system>
<#assign domain=vars.domain>
<#assign type="${domain}."+system+"."+ package +".entity." +class>
<#assign tableName=model.tableName>
<#assign colList=model.boAttrList>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${domain}.${system}.${package}.mapper.${class}Mapper">

    <resultMap id="${class}" type="${type}">
            <#assign idField=convertUnderLine(model.idField)>
            <id property="${idField}" column="${model.idField}" jdbcType="VARCHAR"/>
		        <#list colList as col>
    	        <#assign colName=convertUnderLine(col.fieldName)>
      	      <result property="${colName}" column="${col.fieldName}" jdbcType="${getJdbcType(col)}"/>
		        </#list>

		      <#if model.genMode=="form">
		      <result property="tenantId" column="TENANT_ID_" jdbcType="VARCHAR"/>
		      <result property="createBy" column="CREATE_BY_" jdbcType="VARCHAR"/>
		      <result property="createTime" column="CREATE_TIME_" jdbcType="DATE"/>
		      <result property="updateBy" column="UPDATE_BY_" jdbcType="VARCHAR"/>
		      <result property="updateTime" column="UPDATE_TIME_" jdbcType="DATE"/>
		      <result property="createDepId" column="CREATE_DEP_ID_" jdbcType="VARCHAR"/>
		      </#if>
    </resultMap>


    <select id="query" resultType="${type}" parameterType="java.util.Map">
        select ${model.idField},<#list colList as col>${col.fieldName}<#if col_has_next>,</#if></#list><#if model.genMode=="form">,TENANT_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_,CREATE_DEP_ID_</#if> from ${tableName}
        <#noparse>
        <where>
            <if test="@rx.Ognl@isNotEmpty(w.whereSql)">
                ${w.whereSql}
            </if>
        </where>
        <if test="@rx.Ognl@isNotEmpty(w.orderBySql)">
            ORDER BY ${w.orderBySql}
        </if>
        <if test="@rx.Ognl@isEmpty(w.orderBySql)"></#noparse>
            ORDER BY  ${model.idField} DESC
        </if>
    </select>

</mapper>

<#function getJdbcType col>
<#assign dbtype=col.dataType?lower_case>
<#assign rtn>
	<#if  dbtype=="number"  >
		NUMERIC
	<#elseif (dbtype=="date")>
		DATE
	<#else>
		VARCHAR
	</#if>
</#assign>
<#return rtn?trim>
</#function>


<#function convertUnderLine field>
	<#if field?index_of("_")==-1>
		<#assign rtn>${field?lower_case?trim}</#assign>
		<#return rtn>
	</#if>

	<#if field?index_of("F_")==0>
		<#assign rtn><#list (field?substring(2))?split("_") as x><#if (x_index==0)><#if x?length==1>${x?upper_case?trim}<#else>${x?lower_case?trim}</#if><#else>${x?lower_case?cap_first?trim}</#if></#list></#assign>
		<#return rtn>
	</#if>

	<#assign rtn><#list field?split("_") as x><#if (x_index==0)><#if x?length==1>${x?upper_case?trim}<#else>${x?lower_case?trim}</#if><#else>${x?lower_case?cap_first?trim}</#if></#list></#assign>
 <#return rtn>
</#function>

';
BEGIN
update FORM_CODEGEN_TEMPLATE set CONTENT_=content where id_='1424629106896535554';
commit;
end ;
/

INSERT INTO FORM_CODEGEN_TEMPLATE(ID_, NAME_, FILE_NAME_, PATH_, SINGLE_, ENABLED_, CONTENT_, TENANT_ID_, CREATE_DEP_ID_, CREATE_BY_, CREATE_TIME_, UPDATE_BY_, UPDATE_TIME_)
       values(         '1424629731029942274',         '数据库访问类',         '{class}Mapper.java',       'src/main/java/com/redxun/{system}/{package}/mapper',      'NO',      'YES',         '',       '1',      '1',       '1',       to_date('2021-8-9','yyyy-MM-dd HH24:mi:ss'),     '1',        to_date('2021-8-19','yyyy-MM-dd HH24:mi:ss'));

            DECLARE
content CLOB:='<#assign package=model.vars.package>
<#assign class=model.vars.class>
<#assign system=vars.system>
<#assign domain=vars.domain>
<#assign type="${domain}."+system+"."+ package +".entity." +class>
<#assign tableName=model.tableName>

package ${domain}.${system}.${package}.mapper;

import ${domain}.${system}.${package}.entity.${class};
import org.apache.ibatis.annotations.Mapper;
import com.redxun.common.base.db.BaseDao;

/**
* [${model.name}]据库访问层
* @author: ${vars.developer}
* @company: ${vars.company}
* @date: ${vars.date}
*/
@Mapper
public interface ${class}Mapper extends BaseDao<${class}> {

}
';
BEGIN
update FORM_CODEGEN_TEMPLATE set CONTENT_=content where id_='1424629731029942274';
commit;
end ;
/

INSERT INTO FORM_CODEGEN_TEMPLATE(ID_, NAME_, FILE_NAME_, PATH_, SINGLE_, ENABLED_, CONTENT_, TENANT_ID_, CREATE_DEP_ID_, CREATE_BY_, CREATE_TIME_, UPDATE_BY_, UPDATE_TIME_)
       values(         '1424630163097780225',         '服务层代码',         '{class}ServiceImpl.java',       'src/main/java/com/redxun/{system}/{package}/service',      'YES',      'YES',         '',       '1',      '1',       '1',       to_date('2021-8-9','yyyy-MM-dd HH24:mi:ss'),     '1',        to_date('2021-8-19','yyyy-MM-dd HH24:mi:ss'));

            DECLARE
content CLOB:='<#assign package=model.vars.package>
<#assign class=model.vars.class>
<#assign classVar=model.vars.classVar>
<#assign system=vars.system>
<#assign domain=vars.domain>

package ${domain}.${system}.${package}.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.service.impl.SuperServiceImpl;
import ${domain}.${system}.${package}.entity.${class};
import ${domain}.${system}.${package}.mapper.${class}Mapper;
<#if model.boEntityList?size gt 0 >
<#list model.boEntityList as subEnt>
import ${domain}.${system}.${package}.mapper.${subEnt.vars.class}Mapper;
</#list>
</#if>
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Collection;


/**
* [${model.name}]业务服务类
* @author: ${vars.developer}
* @company: ${vars.company}
* @date: ${vars.date}
*/
@Service
public class ${class}ServiceImpl extends SuperServiceImpl<${class}Mapper, ${class}> implements BaseService<${class}> {

    @Resource
    private ${class}Mapper ${classVar}Mapper;

    <#if model.boEntityList?size gt 0 >
    <#list model.boEntityList as subEnt>
    @Resource
    private ${subEnt.vars.class}Mapper ${subEnt.vars.classVar}Mapper;
    </#list>
    </#if>

    @Override
    public BaseDao<${class}> getRepository() {
        return ${classVar}Mapper;
    }


	<#if model.boEntityList?size gt 0 >
	@Override
    public void delete(Collection<Serializable> entities) {

        for(Serializable id:entities){
            //根据ID删除${model.name}
            ${classVar}Mapper.deleteById(id);
            <#list model.boEntityList as subEnt>
            //根据ID删除${subEnt.name}
    		QueryWrapper wrapper=new QueryWrapper();
            wrapper.eq("${subEnt.boRelation.fkField}",id);
            ${subEnt.vars.classVar}Mapper.delete(wrapper);
    		</#list>
        }
    }
    </#if>


}
';
BEGIN
update FORM_CODEGEN_TEMPLATE set CONTENT_=content where id_='1424630163097780225';
commit;
end ;
/

INSERT INTO FORM_CODEGEN_TEMPLATE(ID_, NAME_, FILE_NAME_, PATH_, SINGLE_, ENABLED_, CONTENT_, TENANT_ID_, CREATE_DEP_ID_, CREATE_BY_, CREATE_TIME_, UPDATE_BY_, UPDATE_TIME_)
       values(         '1424906913904091137',         '列表文件',         '{class}List.vue',       'src/views/modules/{system}/{package}',      'YES',      'YES',         '',       '1',      '1',       '1',       to_date('2021-8-10','yyyy-MM-dd HH24:mi:ss'),     '1',        to_date('2021-8-19','yyyy-MM-dd HH24:mi:ss'));

            DECLARE
content CLOB:='<#assign package=model.vars.package>
<#assign class=model.vars.class>
<#assign classVar=model.vars.classVar>
<#assign comment=model.name>
<#assign pk=model.idField>
<#assign system=vars.system>
<#assign domain=vars.domain>
<#assign commonList=model.boAttrList>
<template>
  <rx-layout>
    <div slot="center" style>
      <rx-fit>
        <div slot="toolheader" foldheader="true" foldbtn="false" border="false">
          <div class="table-operator">
            <rx-button alias="${classVar}Add" :butn-icon="''plus''" :butn-type="''primary''" :show-no-right="true" @click="add">新建</rx-button>
            <a-dropdown v-if="editEnable ' || '&&' || ' selectedRowKeys.length > 0 || removeEnable ' || '&&' || ' selectedRowKeys.length > 0">
              <a-button style="margin-left: 8px"> 更多 <a-icon type="down" /> </a-button>
              <a-menu slot="overlay">
                <a-menu-item key="1" v-if="editEnable ' || '&&' || ' selectedRowKeys.length > 0" alias="${classVar}Edit" :butn-icon="''edit''"
                             :butn-type="''primary''" :show-no-right="false" @click="editOne"><a-icon type="edit"  />编辑</a-menu-item>
                <a-menu-item key="2" v-if="removeEnable ' || '&&' || ' selectedRowKeys.length > 0" alias="${classVar}Delete" :butn-icon="''delete''" :butn-type="''danger''"
                             :show-no-right="false" @click="delByIds(selectedRowKeys)"><a-icon type="delete" />删除</a-menu-item>
              </a-menu>
            </a-dropdown>
          </div>
          <span class="search-btn-box">
                <span class="search-btn" @click="searchshow"><i class="iconfont iconguolv"></i>过滤</span>
           </span>
        </div>
        <div slot="searchbar" btnalign="right"  v-if="fitSearch" @search="search" @cancel="cancel" @close="close">
          <a-form layout="vertical">
            <#list commonList as col>
              <a-form-item label="${col.comment}">
                <#if (col.dataType=="date")>
                  从
                  <a-date-picker placeholder="请输入${col.comment}" v-model="queryParam.Q_${col.fieldName}_D_GT"
                                 format="YYYY-MM-DD"/>至
                  <a-date-picker placeholder="请输入${col.comment}" v-model="queryParam.Q_${col.fieldName}_D_LT"
                                 format="YYYY-MM-DD"/>
                <#elseif (col.dataType=="number")>
                  <a-input-number :min="0" :max="1000000" placeholder="请输入${col.comment}"
                                  v-model="queryParam.Q_${col.fieldName}_I_EQ"/>
                <#else>
                  <a-input placeholder="请输入${col.comment}" v-model="queryParam.Q_${col.fieldName}_S_LK"/>
                </#if>
              </a-form-item>
            </#list>
          </a-form>
        </div>
        <#assign idField=convertUnderLine(pk)>
        <rx-grid
                ref="table"
                :allowRowSelect="true"
                :multiSelect="true"
                :columns="columns"
                :defaultPageSize="10"
                url="/api-${system}/${system}/${package}/${classVar}/query"
                :queryParam="queryParam"
                data-field="result.data"
                :pageSizeOptions="[''10'',''30'',''40'']"
                id-field="${idField}"
                @selectChange="onSelectChange"
        >
          <span slot="action" slot-scope="{ text, record }" class="actionIcons">
                <span  @click.stop="edit(record)">编辑</span>
				<span  @click.stop="detail(record)">明细</span>
                <span  @click.stop="delByIds([record.${idField}])">删除</span>
            </span>
        </rx-grid>
      </rx-fit>
    </div>
  </rx-layout>
</template>

<script>
  import ${class}Api from ''@/api/${system}/${package}/${classVar}''
  import ${class}Edit from ''./${class}Edit''
  import {BaseList,RxFit,RxGrid } from ''jpaas-common-lib'';
  import RxButton from "@/views/modules/share/rx-button";
  import RxSpan from "@/views/modules/share/rx-span";
  export default {
    name: ''${class}List'',
    mixins: [BaseList],
    components: {
      RxButton,
      RxSpan,
      RxFit
    },
    data() {
      return {
        columns: [
          {
            title: ''序号'',
            type: ''indexColumn'',
            width: 100,
            dataIndex: ''serial'',
            scopedSlots: {customRender: ''serial''}
          },
          <#list commonList as col>
          {title: ''${col.comment}'', dataIndex: ''${col.name}'', width: 100},
          </#list>
          {title: ''操作'', width: 100, dataIndex: ''action'', scopedSlots: {customRender: ''action''}}
        ],
        component:${class}Edit,
        comment:"${comment}",
        widthHeight:[''800px'',''600px''],
        fitSearch:false,
      }
    },
    methods:{
      delById(ids){
        return ${class}Api.del({ ids: ids.join('','') });
      },
      getListByParams(parameter){
        return ${class}Api.query(parameter)
      }

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

<#function convertUnderLine field>
  <#if field?index_of("_")==-1>
    <#assign rtn>${field?lower_case?trim}</#assign>
    <#return rtn>
  </#if>

  <#if field?index_of("F_")==0>
    <#assign rtn><#list (field?substring(2))?split("_") as x><#if (x_index==0)><#if x?length==1>${x?upper_case?trim}<#else>${x?lower_case?trim}</#if><#else>${x?lower_case?cap_first?trim}</#if></#list></#assign>
    <#return rtn>
  </#if>

  <#assign rtn><#list field?split("_") as x><#if (x_index==0)><#if x?length==1>${x?upper_case?trim}<#else>${x?lower_case?trim}</#if><#else>${x?lower_case?cap_first?trim}</#if></#list></#assign>
  <#return rtn>
</#function>

';
BEGIN
update FORM_CODEGEN_TEMPLATE set CONTENT_=content where id_='1424906913904091137';
commit;
end ;
/

INSERT INTO FORM_CODEGEN_TEMPLATE(ID_, NAME_, FILE_NAME_, PATH_, SINGLE_, ENABLED_, CONTENT_, TENANT_ID_, CREATE_DEP_ID_, CREATE_BY_, CREATE_TIME_, UPDATE_BY_, UPDATE_TIME_)
       values(         '1424907493359771649',         'Apijs代码',         '{classVar}.js',       'src/api/{system}/{package}',      'YES',      'YES',         '',       '1',      '1',       '1',       to_date('2021-8-10','yyyy-MM-dd HH24:mi:ss'),     '1',        to_date('2021-8-18','yyyy-MM-dd HH24:mi:ss'));

            DECLARE
content CLOB:='<#assign package=model.vars.package>
<#assign class=model.vars.class>
<#assign classVar=model.vars.classVar>
<#assign developer=vars.developer>
<#assign system=vars.system>
<#assign name=model.name>
<#assign domain=vars.domain>
import rxAjax from ''@/assets/js/ajax.js'';

/*
 概要说明:${name} api接口
 开发人员:${developer}
*/
export const ${class}Api = {};

${class}Api.baseUrl= ''/api-${system}/${system}/${package}/${classVar}'';
${class}Api.exportUrl= ${class}Api.baseUrl + ''/export'';

//查询列表
${class}Api.query=function (parameter) {
  var url= ${class}Api.baseUrl + ''/query'';
  return rxAjax.postJson(url,parameter).then (res => {
    return res.result
  })
}


/**
 * 获取表单数据
 * @param pkId
 * @returns {*}
 */
${class}Api.getById =function(pkId) {
    var url= HtglApi.baseUrl + ''/getById?pkId='' + pkId;
    return rxAjax.get(url);
}

/**
 * 保存表单数据
 * @param formdata
 * @returns {*}
 */
${class}Api.saveData =function(formdata) {
    var url= HtglApi.baseUrl + ''/saveData'';
    return rxAjax.postJson(url,formdata);
}



//删除数据
${class}Api.del =function(parameter) {
  var url= ${class}Api.baseUrl + ''/del'';
  return rxAjax.postUrl(url,parameter);
}

export  default ${class}Api;

';
BEGIN
update FORM_CODEGEN_TEMPLATE set CONTENT_=content where id_='1424907493359771649';
commit;
end ;
/

INSERT INTO FORM_CODEGEN_TEMPLATE(ID_, NAME_, FILE_NAME_, PATH_, SINGLE_, ENABLED_, CONTENT_, TENANT_ID_, CREATE_DEP_ID_, CREATE_BY_, CREATE_TIME_, UPDATE_BY_, UPDATE_TIME_)
       values(         '1424907787665694721',         '编辑页面',         '{class}Edit.vue',       'src/views/modules/{system}/{package}',      'YES',      'YES',         '',       '1',      '1',       '1',       to_date('2021-8-10','yyyy-MM-dd HH24:mi:ss'),     '1',        to_date('2021-8-18','yyyy-MM-dd HH24:mi:ss'));

            DECLARE
content CLOB:='${vueTemplate}';
BEGIN
update FORM_CODEGEN_TEMPLATE set CONTENT_=content where id_='1424907787665694721';
commit;
end ;
/

INSERT INTO FORM_CODEGEN_TEMPLATE(ID_, NAME_, FILE_NAME_, PATH_, SINGLE_, ENABLED_, CONTENT_, TENANT_ID_, CREATE_DEP_ID_, CREATE_BY_, CREATE_TIME_, UPDATE_BY_, UPDATE_TIME_)
       values(         '1427176661270401026',         '编辑JS',         '{class}Data.js',       'src/views/modules/{system}/{package}',      'YES',      'YES',         '',       '1',      '1',       '1',       to_date('2021-8-16','yyyy-MM-dd HH24:mi:ss'),     '1',        to_date('2021-8-18','yyyy-MM-dd HH24:mi:ss'));

            DECLARE
content CLOB:='${vueJs}';
BEGIN
update FORM_CODEGEN_TEMPLATE set CONTENT_=content where id_='1427176661270401026';
commit;
end ;
/

commit;