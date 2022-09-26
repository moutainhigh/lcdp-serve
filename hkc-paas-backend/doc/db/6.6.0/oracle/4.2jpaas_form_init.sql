INSERT INTO form_rule
  (ID_,
   NAME_,
   PROMPT_,
   ALIAS_,
   REGULAR_,
   DELETED_,
   COMPANY_ID_,
   TENANT_ID_,
   CREATE_DEP_ID_,
   CREATE_BY_,
   CREATE_TIME_,
   UPDATE_BY_,
   UPDATE_TIME_,
   APP_ID_)
VALUES
  ('1275596976204800002',
   '正整数',
   '请输入正整数！',
   'positiveinteger',
   '^[1-9]d*$',
   0,
   '0',
   '0',
   '2',
   '1',
   TO_DATE('2020-6-24 9:09:53', 'yyyy-MM-dd HH24:mi:ss'),
   '1',
   TO_DATE('2020-12-20 21:23:59', 'yyyy-MM-dd HH24:mi:ss'),
   '');

INSERT INTO form_rule
  (ID_,
   NAME_,
   PROMPT_,
   ALIAS_,
   REGULAR_,
   DELETED_,
   COMPANY_ID_,
   TENANT_ID_,
   CREATE_DEP_ID_,
   CREATE_BY_,
   CREATE_TIME_,
   UPDATE_BY_,
   UPDATE_TIME_,
   APP_ID_)
VALUES
  ('1275669328796049409',
   '手机号',
   '请输入手机号!',
   'phone',
   '^1[3456789]d{9}$',
   0,
   '0',
   '0',
   '2',
   '1',
   TO_DATE('2020-6-24 13:57:23', 'yyyy-MM-dd HH24:mi:ss'),
   '1',
   TO_DATE('2020-12-20 21:23:52', 'yyyy-MM-dd HH24:mi:ss'),
   '');

INSERT INTO form_rule
  (ID_,
   NAME_,
   PROMPT_,
   ALIAS_,
   REGULAR_,
   DELETED_,
   COMPANY_ID_,
   TENANT_ID_,
   CREATE_DEP_ID_,
   CREATE_BY_,
   CREATE_TIME_,
   UPDATE_BY_,
   UPDATE_TIME_,
   APP_ID_)
VALUES
  ('1277545456632532994',
   '合法Email',
   '必须输入合法Email地址!',
   'email',
   '^(w)+(.w+)*@(w)+((.w+)+)$',
   0,
   '0',
   '0',
   '1',
   '1',
   TO_DATE('2020-6-29 18:12:27', 'yyyy-MM-dd HH24:mi:ss'),
   '1',
   TO_DATE('2020-12-20 21:04:47', 'yyyy-MM-dd HH24:mi:ss'),
   '');

INSERT INTO form_rule
  (ID_,
   NAME_,
   PROMPT_,
   ALIAS_,
   REGULAR_,
   DELETED_,
   COMPANY_ID_,
   TENANT_ID_,
   CREATE_DEP_ID_,
   CREATE_BY_,
   CREATE_TIME_,
   UPDATE_BY_,
   UPDATE_TIME_,
   APP_ID_)
VALUES
  ('1277545684584566786',
   '身份证',
   '必须输入15-18位数字',
   'idcard',
   '(^d{15}$)|(^d{18}$)|(^d{17}(d|X|x)$)',
   0,
   '0',
   '0',
   '1',
   '1',
   TO_DATE('2020-6-29 18:13:22', 'yyyy-MM-dd HH24:mi:ss'),
   '1',
   TO_DATE('2020-12-20 21:23:37', 'yyyy-MM-dd HH24:mi:ss'),
   '');

INSERT INTO form_rule
  (ID_,
   NAME_,
   PROMPT_,
   ALIAS_,
   REGULAR_,
   DELETED_,
   COMPANY_ID_,
   TENANT_ID_,
   CREATE_DEP_ID_,
   CREATE_BY_,
   CREATE_TIME_,
   UPDATE_BY_,
   UPDATE_TIME_,
   APP_ID_)
VALUES
  ('1277545966160777217',
   '英文或数字',
   '必须输入英文或数字',
   'isenglishandnumber',
   '^[0-9a-zA-Z\_]+$',
   0,
   '0',
   '0',
   '1',
   '1',
   TO_DATE('2020-6-29 18:14:29', 'yyyy-MM-dd HH24:mi:ss'),
   '',
   TO_DATE('2020-12-20 21:08:48', 'yyyy-MM-dd HH24:mi:ss'),
   '');

INSERT INTO form_rule
  (ID_,
   NAME_,
   PROMPT_,
   ALIAS_,
   REGULAR_,
   DELETED_,
   COMPANY_ID_,
   TENANT_ID_,
   CREATE_DEP_ID_,
   CREATE_BY_,
   CREATE_TIME_,
   UPDATE_BY_,
   UPDATE_TIME_,
   APP_ID_)
VALUES
  ('1277546396345372673',
   '数字',
   '必须输入数字',
   'number',
   '^[-+]?[0-9]+(.[0-9]+)?$',
   0,
   '0',
   '0',
   '1',
   '1',
   TO_DATE('2020-6-29 18:16:11', 'yyyy-MM-dd HH24:mi:ss'),
   '',
   TO_DATE('2020-12-20 21:08:10', 'yyyy-MM-dd HH24:mi:ss'),
   '');

INSERT INTO form_rule
  (ID_,
   NAME_,
   PROMPT_,
   ALIAS_,
   REGULAR_,
   DELETED_,
   COMPANY_ID_,
   TENANT_ID_,
   CREATE_DEP_ID_,
   CREATE_BY_,
   CREATE_TIME_,
   UPDATE_BY_,
   UPDATE_TIME_,
   APP_ID_)
VALUES
  ('1277546588067008514',
   '浮点数',
   '必须输入浮点数',
   'float',
   '^-?([1-9]d*.d*|0.d*[1-9]d*|0?.0+|0|d*)$',
   0,
   '0',
   '0',
   '1',
   '1',
   TO_DATE('2020-6-29 18:16:57', 'yyyy-MM-dd HH24:mi:ss'),
   '',
   TO_DATE('2020-12-20 21:07:36', 'yyyy-MM-dd HH24:mi:ss'),
   '');

INSERT INTO form_rule
  (ID_,
   NAME_,
   PROMPT_,
   ALIAS_,
   REGULAR_,
   DELETED_,
   COMPANY_ID_,
   TENANT_ID_,
   CREATE_DEP_ID_,
   CREATE_BY_,
   CREATE_TIME_,
   UPDATE_BY_,
   UPDATE_TIME_,
   APP_ID_)
VALUES
  ('1277547652346482689',
   'ip',
   '必须输入合法IP地址',
   'ip',
   '^d+.d+.d+.d+$',
   0,
   '0',
   '0',
   '1',
   '1',
   TO_DATE('2020-6-29 18:21:11', 'yyyy-MM-dd HH24:mi:ss'),
   '',
   TO_DATE('2020-12-20 21:06:30', 'yyyy-MM-dd HH24:mi:ss'),
   '');

INSERT INTO form_codegen_template
  (ID_,
   NAME_,
   FILE_NAME_,
   PATH_,
   SINGLE_,
   ENABLED_,
   CONTENT_,
   DELETED_,
   COMPANY_ID_,
   TENANT_ID_,
   CREATE_DEP_ID_,
   CREATE_BY_,
   CREATE_TIME_,
   UPDATE_BY_,
   UPDATE_TIME_)
VALUES
  ('1424623584797523970',
   '控制器模板',
   '{class}Controller.java',
   'src/main/java/com/redxun/{system}/{package}/controller',
   'NO',
   'YES',
   '',
   0,
   '0',
   '1',
   '1',
   '1',
   TO_DATE('2021-8-9 0:00:00', 'yyyy-MM-dd HH24:mi:ss'),
   '1',
   TO_DATE('2021-8-19 0:00:00', 'yyyy-MM-dd HH24:mi:ss'));

DECLARE
  content_6 CLOB := '<#assign package=model.vars.package><#assign class=model.vars.class><#assign classVar=model.vars.classVar><#assign comment=model.name><#assign system=vars.system><#assign domain=vars.domain><#assign tableName=model.tableName><#assign colList=model.boAttrList>package ${domain}.${system}.${package}.controller;import com.redxun.common.base.db.BaseService;import com.redxun.form.codegen.controller.CodeGenBaseController;import ${domain}.${system}.${package}.entity.${class};import ${domain}.${system}.${package}.service.${class}ServiceImpl;import io.swagger.annotations.Api;import lombok.extern.slf4j.Slf4j;import org.springframework.beans.factory.annotation.Autowired;import org.springframework.web.bind.annotation.*;import com.redxun.common.annotation.ClassDefine;/*** [${model.name}] 控制器代码* @author: ${vars.developer}* @company: ${vars.company}* @date: ${vars.date}*/@Slf4j@RestController@RequestMapping("/${system}/${package}/${classVar}")@Api(tags = "${comment}")@ClassDefine(title = "${comment}",alias = "${class}Controller",path = "/${system}/${package}/${classVar}",packages = "${package}",packageName = "子系统名称")public class ${class}Controller extends CodeGenBaseController<${class}> { @Autowired  ${class}ServiceImpl ${classVar}Service; @Override public BaseService getBaseService() {   return ${classVar}Service;  } @Override public String getComment() {    return "${comment}";  } @Override public String getFormAlias() {    return "${formAlias}";  }}';
BEGIN
  UPDATE form_codegen_template
     SET CONTENT_ = content_6
   WHERE ID_ = '1424623584797523970';
  COMMIT;
END;
/

INSERT
  INTO form_codegen_template(ID_,
                             NAME_,
                             FILE_NAME_,
                             PATH_,
                             SINGLE_,
                             ENABLED_,
                             CONTENT_,
                             DELETED_,
                             COMPANY_ID_,
                             TENANT_ID_,
                             CREATE_DEP_ID_,
                             CREATE_BY_,
                             CREATE_TIME_,
                             UPDATE_BY_,
                             UPDATE_TIME_) VALUES('1424627869878845442',
                                                  '实体模板',
                                                  '{class}.java',
                                                  'src/main/java/com/redxun/{system}/{package}/entity',
                                                  'NO',
                                                  'YES',
                                                  '',
                                                  0,
                                                  '0',
                                                  '1',
                                                  '1',
                                                  '1',
                                                  TO_DATE('2021-8-9 0:00:00',
                                                          'yyyy-MM-dd HH24:mi:ss'),
                                                  '1',
                                                  TO_DATE('2021-8-19 0:00:00',
                                                          'yyyy-MM-dd HH24:mi:ss'));

DECLARE
  content_6 CLOB := '<#assign package=model.vars.package><#assign class=model.vars.class><#assign classVar=model.vars.classVar><#assign comment=model.name><#assign system=vars.system><#assign domain=vars.domain><#assign tableName=model.tableName><#assign colList=model.boAttrList><#assign idField=convertUnderLine(model.idField)>/*** [${model.name}]实体类定义* @author: ${vars.developer}* @company: ${vars.company}* @date: ${vars.date}*/package ${domain}.${system}.${package}.entity;import com.baomidou.mybatisplus.annotation.IdType;import com.baomidou.mybatisplus.annotation.TableField;import com.baomidou.mybatisplus.annotation.TableId;import com.baomidou.mybatisplus.annotation.TableName;import com.fasterxml.jackson.annotation.JsonCreator;import ${domain}.common.base.entity.BaseExtEntity;import lombok.Getter;import lombok.Setter;import lombok.experimental.Accessors;import java.util.ArrayList;import java.util.List;import org.apache.ibatis.type.JdbcType;import java.util.Date;@Setter@Getter@Accessors(chain = true)@TableName(value = "${tableName}")public class ${class}  extends BaseExtEntity<java.lang.String> {    @JsonCreator    public ${class}() {    }    //主键    @TableId(value = "${model.idField}",type = IdType.INPUT)  private String ${idField};  @Override    public String getPkId() {        return this.${idField};    }    @Override    public void setPkId(String pkId) {        this.${idField}=pkId;    }    <#list colList as col>    //${col.comment}    @TableField(value = "${col.fieldName}",jdbcType=JdbcType.${getJdbcType(col)})    private ${getJavaType(col)} ${col.name};    </#list>    <#if model.boEntityList?size gt 0 >  <#list model.boEntityList as subEnt>    @TableField(exist=false) private List<${subEnt.vars.class}> ${subEnt.vars.classVar}List=new ArrayList<>();   </#list>  </#if> }<#function getJdbcType col><#assign dbtype=col.dataType?lower_case><#assign rtn>  <#if  dbtype=="number"  >   NUMERIC <#elseif (dbtype=="date")>    DATE  <#else>   VARCHAR </#if></#assign><#return rtn?trim></#function><#function getJavaType col><#assign dbtype=col.dataType?lower_case><#assign rtn><#if  dbtype=="number"  >    <#if col.decimalLength==0>         <#if col.length gt 10 >            long         <#else>            int         </#if>    <#else>         double    </#if><#elseif (dbtype=="date")> Date<#else> String</#if></#assign> <#return rtn?trim></#function><#function convertUnderLine field> <#if field?index_of("_")==-1>   <#assign rtn>${field?lower_case?trim}</#assign>   <#return rtn> </#if>  <#if field?index_of("F_")==0>   <#assign rtn><#list (field?substring(2))?split("_") as x><#if (x_index==0)><#if x?length==1>${x?upper_case?trim}<#else>${x?lower_case?trim}</#if><#else>${x?lower_case?cap_first?trim}</#if></#list></#assign>    <#return rtn> </#if>  <#assign rtn><#list field?split("_") as x><#if (x_index==0)><#if x?length==1>${x?upper_case?trim}<#else>${x?lower_case?trim}</#if><#else>${x?lower_case?cap_first?trim}</#if></#list></#assign> <#return rtn></#function>';
BEGIN
  UPDATE form_codegen_template
     SET CONTENT_ = content_6
   WHERE ID_ = '1424627869878845442';
  COMMIT;
END;
/

INSERT
  INTO form_codegen_template(ID_,
                             NAME_,
                             FILE_NAME_,
                             PATH_,
                             SINGLE_,
                             ENABLED_,
                             CONTENT_,
                             DELETED_,
                             COMPANY_ID_,
                             TENANT_ID_,
                             CREATE_DEP_ID_,
                             CREATE_BY_,
                             CREATE_TIME_,
                             UPDATE_BY_,
                             UPDATE_TIME_) VALUES('1424629106896535554',
                                                  'Mapping文件',
                                                  '{class}Mapper.xml',
                                                  'src/main/resources/mapper/{system}/{package}',
                                                  'NO',
                                                  'YES',
                                                  '',
                                                  0,
                                                  '0',
                                                  '1',
                                                  '1',
                                                  '1',
                                                  TO_DATE('2021-8-9 0:00:00',
                                                          'yyyy-MM-dd HH24:mi:ss'),
                                                  '1',
                                                  TO_DATE('2021-8-18 0:00:00',
                                                          'yyyy-MM-dd HH24:mi:ss'));

DECLARE
  content_6 CLOB := '<#assign package=model.vars.package><#assign class=model.vars.class><#assign system=vars.system><#assign domain=vars.domain><#assign type="${domain}."+system+"."+ package +".entity." +class><#assign tableName=model.tableName><#assign colList=model.boAttrList><?xml version="1.0" encoding="UTF-8"?><!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd"><mapper namespace="${domain}.${system}.${package}.mapper.${class}Mapper">    <resultMap id="${class}" type="${type}">            <#assign idField=convertUnderLine(model.idField)>            <id property="${idField}" column="${model.idField}" jdbcType="VARCHAR"/>            <#list colList as col>              <#assign colName=convertUnderLine(col.fieldName)>             <result property="${colName}" column="${col.fieldName}" jdbcType="${getJdbcType(col)}"/>            </#list>                      <#if model.genMode=="form">         <result property="tenantId" column="TENANT_ID_" jdbcType="VARCHAR"/>          <result property="createBy" column="CREATE_BY_" jdbcType="VARCHAR"/>          <result property="createTime" column="CREATE_TIME_" jdbcType="DATE"/>         <result property="updateBy" column="UPDATE_BY_" jdbcType="VARCHAR"/>          <result property="updateTime" column="UPDATE_TIME_" jdbcType="DATE"/>         <result property="createDepId" column="CREATE_DEP_ID_" jdbcType="VARCHAR"/>         </#if>    </resultMap>    <select id="query" resultType="${type}" parameterType="java.util.Map">        select ${model.idField},<#list colList as col>${col.fieldName}<#if col_has_next>,</#if></#list><#if model.genMode=="form">,TENANT_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_,CREATE_DEP_ID_</#if> from ${tableName}        <#noparse>        <where>            <if test="@rx.Ognl@isNotEmpty(w.whereSql)">                ${w.whereSql}            </if>        </where>        <if test="@rx.Ognl@isNotEmpty(w.orderBySql)">            ORDER BY ${w.orderBySql}        </if>        <if test="@rx.Ognl@isEmpty(w.orderBySql)"></#noparse>            ORDER BY  ${model.idField} DESC        </if>    </select></mapper><#function getJdbcType col><#assign dbtype=col.dataType?lower_case><#assign rtn> <#if  dbtype=="number"  >   NUMERIC <#elseif (dbtype=="date")>    DATE  <#else>   VARCHAR </#if></#assign><#return rtn?trim></#function><#function convertUnderLine field>  <#if field?index_of("_")==-1>   <#assign rtn>${field?lower_case?trim}</#assign>   <#return rtn> </#if>  <#if field?index_of("F_")==0>   <#assign rtn><#list (field?substring(2))?split("_") as x><#if (x_index==0)><#if x?length==1>${x?upper_case?trim}<#else>${x?lower_case?trim}</#if><#else>${x?lower_case?cap_first?trim}</#if></#list></#assign>    <#return rtn> </#if>  <#assign rtn><#list field?split("_") as x><#if (x_index==0)><#if x?length==1>${x?upper_case?trim}<#else>${x?lower_case?trim}</#if><#else>${x?lower_case?cap_first?trim}</#if></#list></#assign> <#return rtn></#function>';
BEGIN
  UPDATE form_codegen_template
     SET CONTENT_ = content_6
   WHERE ID_ = '1424629106896535554';
  COMMIT;
END;
/

INSERT
  INTO form_codegen_template(ID_,
                             NAME_,
                             FILE_NAME_,
                             PATH_,
                             SINGLE_,
                             ENABLED_,
                             CONTENT_,
                             DELETED_,
                             COMPANY_ID_,
                             TENANT_ID_,
                             CREATE_DEP_ID_,
                             CREATE_BY_,
                             CREATE_TIME_,
                             UPDATE_BY_,
                             UPDATE_TIME_) VALUES('1424629731029942274',
                                                  '数据库访问类',
                                                  '{class}Mapper.java',
                                                  'src/main/java/com/redxun/{system}/{package}/mapper',
                                                  'NO',
                                                  'YES',
                                                  '',
                                                  0,
                                                  '0',
                                                  '1',
                                                  '1',
                                                  '1',
                                                  TO_DATE('2021-8-9 0:00:00',
                                                          'yyyy-MM-dd HH24:mi:ss'),
                                                  '1',
                                                  TO_DATE('2021-8-19 0:00:00',
                                                          'yyyy-MM-dd HH24:mi:ss'));

DECLARE
  content_6 CLOB := '<#assign package=model.vars.package><#assign class=model.vars.class><#assign system=vars.system><#assign domain=vars.domain><#assign type="${domain}."+system+"."+ package +".entity." +class><#assign tableName=model.tableName>    package ${domain}.${system}.${package}.mapper;import ${domain}.${system}.${package}.entity.${class};import org.apache.ibatis.annotations.Mapper;import com.redxun.common.base.db.BaseDao;/*** [${model.name}]据库访问层* @author: ${vars.developer}* @company: ${vars.company}* @date: ${vars.date}*/@Mapperpublic interface ${class}Mapper extends BaseDao<${class}> {}';
BEGIN
  UPDATE form_codegen_template
     SET CONTENT_ = content_6
   WHERE ID_ = '1424629731029942274';
  COMMIT;
END;
/

INSERT
  INTO form_codegen_template(ID_,
                             NAME_,
                             FILE_NAME_,
                             PATH_,
                             SINGLE_,
                             ENABLED_,
                             CONTENT_,
                             DELETED_,
                             COMPANY_ID_,
                             TENANT_ID_,
                             CREATE_DEP_ID_,
                             CREATE_BY_,
                             CREATE_TIME_,
                             UPDATE_BY_,
                             UPDATE_TIME_) VALUES('1424630163097780225',
                                                  '服务层代码',
                                                  '{class}ServiceImpl.java',
                                                  'src/main/java/com/redxun/{system}/{package}/service',
                                                  'YES',
                                                  'YES',
                                                  '',
                                                  0,
                                                  '0',
                                                  '1',
                                                  '1',
                                                  '1',
                                                  TO_DATE('2021-8-9 0:00:00',
                                                          'yyyy-MM-dd HH24:mi:ss'),
                                                  '1',
                                                  TO_DATE('2021-8-19 0:00:00',
                                                          'yyyy-MM-dd HH24:mi:ss'));

DECLARE
  content_6 CLOB := '<#assign package=model.vars.package><#assign class=model.vars.class><#assign classVar=model.vars.classVar><#assign system=vars.system><#assign domain=vars.domain>package ${domain}.${system}.${package}.service;import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;import com.redxun.common.base.db.BaseDao;import com.redxun.common.base.db.BaseService;import com.redxun.common.service.impl.SuperServiceImpl;import ${domain}.${system}.${package}.entity.${class};import ${domain}.${system}.${package}.mapper.${class}Mapper;<#if model.boEntityList?size gt 0 ><#list model.boEntityList as subEnt>import ${domain}.${system}.${package}.mapper.${subEnt.vars.class}Mapper;</#list></#if>import org.springframework.stereotype.Service;import javax.annotation.Resource;import java.io.Serializable;import java.util.Collection;/*** [${model.name}]业务服务类* @author: ${vars.developer}* @company: ${vars.company}* @date: ${vars.date}*/@Servicepublic class ${class}ServiceImpl extends SuperServiceImpl<${class}Mapper, ${class}> implements BaseService<${class}> {    @Resource    private ${class}Mapper ${classVar}Mapper;        <#if model.boEntityList?size gt 0 >    <#list model.boEntityList as subEnt>    @Resource    private ${subEnt.vars.class}Mapper ${subEnt.vars.classVar}Mapper;    </#list>    </#if>       @Override    public BaseDao<${class}> getRepository() {        return ${classVar}Mapper;    }     <#if model.boEntityList?size gt 0 > @Override    public void delete(Collection<Serializable> entities) {        for(Serializable id:entities){            //根据ID删除${model.name}            ${classVar}Mapper.deleteById(id);            <#list model.boEntityList as subEnt>            //根据ID删除${subEnt.name}            QueryWrapper wrapper=new QueryWrapper();            wrapper.eq("${subEnt.boRelation.fkField}",id);            ${subEnt.vars.classVar}Mapper.delete(wrapper);        </#list>        }    }    </#if>}';
BEGIN
  UPDATE form_codegen_template
     SET CONTENT_ = content_6
   WHERE ID_ = '1424630163097780225';
  COMMIT;
END;
/

INSERT
  INTO form_codegen_template(ID_,
                             NAME_,
                             FILE_NAME_,
                             PATH_,
                             SINGLE_,
                             ENABLED_,
                             CONTENT_,
                             DELETED_,
                             COMPANY_ID_,
                             TENANT_ID_,
                             CREATE_DEP_ID_,
                             CREATE_BY_,
                             CREATE_TIME_,
                             UPDATE_BY_,
                             UPDATE_TIME_) VALUES('1424906913904091137',
                                                  '列表文件',
                                                  '{class}List.vue',
                                                  'src/views/modules/{system}/{package}',
                                                  'YES',
                                                  'YES',
                                                  '',
                                                  0,
                                                  '0',
                                                  '1',
                                                  '1',
                                                  '1',
                                                  TO_DATE('2021-8-10 0:00:00',
                                                          'yyyy-MM-dd HH24:mi:ss'),
                                                  '1',
                                                  TO_DATE('2021-8-19 0:00:00',
                                                          'yyyy-MM-dd HH24:mi:ss'));

DECLARE
  content_6 CLOB := '<#assign package=model.vars.package><#assign class=model.vars.class><#assign classVar=model.vars.classVar><#assign comment=model.name><#assign pk=model.idField><#assign system=vars.system><#assign domain=vars.domain><#assign commonList=model.boAttrList><template>  <rx-layout>    <div slot="center" style>      <rx-fit>        <div slot="toolheader" foldheader="true" foldbtn="false" border="false">          <div class="table-operator">            <rx-button alias="${classVar}Add" :butn-icon="''plus''" :butn-type="''primary''" :show-no-right="true" @click="add">新建</rx-button>            <a-dropdown v-if="editEnable ' || '&&' ||
                    ' selectedRowKeys.length > 0 || removeEnable ' || '&&' ||
                    ' selectedRowKeys.length > 0">              <a-button style="margin-left: 8px"> 更多 <a-icon type="down" /> </a-button>              <a-menu slot="overlay">                <a-menu-item key="1" v-if="editEnable ' || '&&' ||
                    ' selectedRowKeys.length > 0" alias="${classVar}Edit" :butn-icon="''edit''"                             :butn-type="''primary''" :show-no-right="false" @click="editOne"><a-icon type="edit"  />编辑</a-menu-item>                <a-menu-item key="2" v-if="removeEnable ' || '&&' ||
                    ' selectedRowKeys.length > 0" alias="${classVar}Delete" :butn-icon="''delete''" :butn-type="''danger''"                             :show-no-right="false" @click="delByIds(selectedRowKeys)"><a-icon type="delete" />删除</a-menu-item>              </a-menu>            </a-dropdown>          </div>          <span class="search-btn-box">                <span class="search-btn" @click="searchshow"><i class="iconfont iconguolv"></i>过滤</span>           </span>        </div>        <div slot="searchbar" btnalign="right"  v-if="fitSearch" @search="search" @cancel="cancel" @close="close">          <a-form layout="vertical">            <#list commonList as col>              <a-form-item label="${col.comment}">                <#if (col.dataType=="date")>                  从                  <a-date-picker placeholder="请输入${col.comment}" v-model="queryParam.Q_${col.fieldName}_D_GT"                                 format="YYYY-MM-DD"/>至                  <a-date-picker placeholder="请输入${col.comment}" v-model="queryParam.Q_${col.fieldName}_D_LT"                                 format="YYYY-MM-DD"/>                <#elseif (col.dataType=="number")>                  <a-input-number :min="0" :max="1000000" placeholder="请输入${col.comment}"                                  v-model="queryParam.Q_${col.fieldName}_I_EQ"/>                <#else>                  <a-input placeholder="请输入${col.comment}" v-model="queryParam.Q_${col.fieldName}_S_LK"/>                </#if>              </a-form-item>            </#list>          </a-form>        </div>        <#assign idField=convertUnderLine(pk)>        <rx-grid                ref="table"                :allowRowSelect="true"                :multiSelect="true"                :columns="columns"                :defaultPageSize="10"                url="/api-${system}/${system}/${package}/${classVar}/query"                :queryParam="queryParam"                data-field="result.data"                :pageSizeOptions="[''10'',''30'',''40'']"                id-field="${idField}"                @selectChange="onSelectChange"        >          <span slot="action" slot-scope="{ text, record }" class="actionIcons">                <span  @click.stop="edit(record)">编辑</span>       <span  @click.stop="detail(record)">明细</span>                <span  @click.stop="delByIds([record.${idField}])">删除</span>            </span>        </rx-grid>      </rx-fit>    </div>  </rx-layout></template><script>  import ${class}Api from ''@/api/${system}/${package}/${classVar}''  import ${class}Edit from ''./${class}Edit''  import {BaseList,RxFit,RxGrid } from ''jpaas-common-lib'';  import RxButton from "@/views/modules/share/rx-button";  import RxSpan from "@/views/modules/share/rx-span";  export default {    name: ''${class}List'',    mixins: [BaseList],    components: {      RxButton,      RxSpan,      RxFit    },    data() {      return {        columns: [          {            title: ''序号'',            type: ''indexColumn'',            width: 100,            dataIndex: ''serial'',            scopedSlots: {customRender: ''serial''}          },          <#list commonList as col>          {title: ''${col.comment}'', dataIndex: ''${col.name}'', width: 100},          </#list>          {title: ''操作'', width: 100, dataIndex: ''action'', scopedSlots: {customRender: ''action''}}        ],        component:${class}Edit,        comment:"${comment}",        widthHeight:[''800px'',''600px''],        fitSearch:false,      }    },    methods:{      delById(ids){        return ${class}Api.del({ ids: ids.join('','') });      },      getListByParams(parameter){        return ${class}Api.query(parameter)      }    }  }</script><style scoped>  .table-operator {    padding:4px;  }  .table-operator button {    margin:2px;  }</style><#function convertUnderLine field>  <#if field?index_of("_")==-1>    <#assign rtn>${field?lower_case?trim}</#assign>    <#return rtn>  </#if>  <#if field?index_of("F_")==0>    <#assign rtn><#list (field?substring(2))?split("_") as x><#if (x_index==0)><#if x?length==1>${x?upper_case?trim}<#else>${x?lower_case?trim}</#if><#else>${x?lower_case?cap_first?trim}</#if></#list></#assign>    <#return rtn>  </#if>  <#assign rtn><#list field?split("_") as x><#if (x_index==0)><#if x?length==1>${x?upper_case?trim}<#else>${x?lower_case?trim}</#if><#else>${x?lower_case?cap_first?trim}</#if></#list></#assign>  <#return rtn></#function>';
BEGIN
  UPDATE form_codegen_template
     SET CONTENT_ = content_6
   WHERE ID_ = '1424906913904091137';
  COMMIT;
END;
/

INSERT
  INTO form_codegen_template(ID_,
                             NAME_,
                             FILE_NAME_,
                             PATH_,
                             SINGLE_,
                             ENABLED_,
                             CONTENT_,
                             DELETED_,
                             COMPANY_ID_,
                             TENANT_ID_,
                             CREATE_DEP_ID_,
                             CREATE_BY_,
                             CREATE_TIME_,
                             UPDATE_BY_,
                             UPDATE_TIME_) VALUES('1424907493359771649',
                                                  'Apijs代码',
                                                  '{classVar}.js',
                                                  'src/api/{system}/{package}',
                                                  'YES',
                                                  'YES',
                                                  '',
                                                  0,
                                                  '0',
                                                  '1',
                                                  '1',
                                                  '1',
                                                  TO_DATE('2021-8-10 0:00:00',
                                                          'yyyy-MM-dd HH24:mi:ss'),
                                                  '1',
                                                  TO_DATE('2021-8-18 0:00:00',
                                                          'yyyy-MM-dd HH24:mi:ss'));

DECLARE
  content_6 CLOB := '<#assign package=model.vars.package><#assign class=model.vars.class><#assign classVar=model.vars.classVar><#assign developer=vars.developer><#assign system=vars.system><#assign name=model.name><#assign domain=vars.domain>import rxAjax from ''@/assets/js/ajax.js'';/* 概要说明:${name} api接口 开发人员:${developer}*/export const ${class}Api = {};${class}Api.baseUrl= ''/api-${system}/${system}/${package}/${classVar}'';${class}Api.exportUrl= ${class}Api.baseUrl + ''/export'';//查询列表${class}Api.query=function (parameter) {  var url= ${class}Api.baseUrl + ''/query'';  return rxAjax.postJson(url,parameter).then (res => {    return res.result  })}/** * 获取表单数据 * @param pkId * @returns {*} */${class}Api.getById =function(pkId) {    var url= HtglApi.baseUrl + ''/getById?pkId='' + pkId;    return rxAjax.get(url);}/** * 保存表单数据 * @param formdata * @returns {*} */${class}Api.saveData =function(formdata) {    var url= HtglApi.baseUrl + ''/saveData'';    return rxAjax.postJson(url,formdata);}//删除数据${class}Api.del =function(parameter) {  var url= ${class}Api.baseUrl + ''/del'';  return rxAjax.postUrl(url,parameter);}export  default ${class}Api;';
BEGIN
  UPDATE form_codegen_template
     SET CONTENT_ = content_6
   WHERE ID_ = '1424907493359771649';
  COMMIT;
END;
/

INSERT
  INTO form_codegen_template(ID_,
                             NAME_,
                             FILE_NAME_,
                             PATH_,
                             SINGLE_,
                             ENABLED_,
                             CONTENT_,
                             DELETED_,
                             COMPANY_ID_,
                             TENANT_ID_,
                             CREATE_DEP_ID_,
                             CREATE_BY_,
                             CREATE_TIME_,
                             UPDATE_BY_,
                             UPDATE_TIME_) VALUES('1424907493359771650',
                                                  '实体转json映射',
                                                  '{classVar}.json',
                                                  'src/main/resources/entityJson/{package}',
                                                  'YES',
                                                  'YES',
                                                  '',
                                                  0,
                                                  '0',
                                                  '1',
                                                  '1',
                                                  '1',
                                                  TO_DATE('2021-8-10 0:00:00',
                                                          'yyyy-MM-dd HH24:mi:ss'),
                                                  '1',
                                                  TO_DATE('2021-8-18 0:00:00',
                                                          'yyyy-MM-dd HH24:mi:ss'));

DECLARE
  content_6 CLOB := '${jsonFile}';
BEGIN
  UPDATE form_codegen_template
     SET CONTENT_ = content_6
   WHERE ID_ = '1424907493359771650';
  COMMIT;
END;
/

INSERT
  INTO form_codegen_template(ID_,
                             NAME_,
                             FILE_NAME_,
                             PATH_,
                             SINGLE_,
                             ENABLED_,
                             CONTENT_,
                             DELETED_,
                             COMPANY_ID_,
                             TENANT_ID_,
                             CREATE_DEP_ID_,
                             CREATE_BY_,
                             CREATE_TIME_,
                             UPDATE_BY_,
                             UPDATE_TIME_) VALUES('1424907787665694721',
                                                  '编辑页面',
                                                  '{class}Edit.vue',
                                                  'src/views/modules/{system}/{package}',
                                                  'YES',
                                                  'YES',
                                                  '',
                                                  0,
                                                  '0',
                                                  '1',
                                                  '1',
                                                  '1',
                                                  TO_DATE('2021-8-10 0:00:00',
                                                          'yyyy-MM-dd HH24:mi:ss'),
                                                  '1',
                                                  TO_DATE('2021-8-18 0:00:00',
                                                          'yyyy-MM-dd HH24:mi:ss'));

DECLARE
  content_6 CLOB := '${vueTemplate}';
BEGIN
  UPDATE form_codegen_template
     SET CONTENT_ = content_6
   WHERE ID_ = '1424907787665694721';
  COMMIT;
END;
/

INSERT
  INTO form_codegen_template(ID_,
                             NAME_,
                             FILE_NAME_,
                             PATH_,
                             SINGLE_,
                             ENABLED_,
                             CONTENT_,
                             DELETED_,
                             COMPANY_ID_,
                             TENANT_ID_,
                             CREATE_DEP_ID_,
                             CREATE_BY_,
                             CREATE_TIME_,
                             UPDATE_BY_,
                             UPDATE_TIME_) VALUES('1427176661270401026',
                                                  '编辑JS',
                                                  '{class}Data.js',
                                                  'src/views/modules/{system}/{package}',
                                                  'YES',
                                                  'YES',
                                                  '',
                                                  0,
                                                  '0',
                                                  '1',
                                                  '1',
                                                  '1',
                                                  TO_DATE('2021-8-16 0:00:00',
                                                          'yyyy-MM-dd HH24:mi:ss'),
                                                  '1',
                                                  TO_DATE('2021-8-18 0:00:00',
                                                          'yyyy-MM-dd HH24:mi:ss'));

DECLARE
  content_6 CLOB := '${vueJs}';
BEGIN
  UPDATE form_codegen_template
     SET CONTENT_ = content_6
   WHERE ID_ = '1427176661270401026';
  COMMIT;
END;
/

INSERT
  INTO form_template(ID_,
                     NAME_,
                     ALIAS_,
                     TEMPLATE_,
                     TYPE_,
                     CATEGORY_,
                     FILE_NAME_,
                     PATH_,
                     SINGLE_,
                     GEN_MODE_,
                     MAIN_SUB_TYPE_,
                     DELETED_,
                     COMPANY_ID_,
                     TENANT_ID_,
                     CREATE_DEP_ID_,
                     CREATE_BY_,
                     CREATE_TIME_,
                     UPDATE_BY_,
                     UPDATE_TIME_,
                     APP_ID_) VALUES('1275710436348100609',
                                     '主表单列模板',
                                     'oneColumn',
                                     '',
                                     'pc',
                                     'main',
                                     '',
                                     '',
                                     '',
                                     '',
                                     '',
                                     0,
                                     '0',
                                     '0',
                                     '2',
                                     '1',
                                     TO_DATE('2020-6-24 16:40:44',
                                             'yyyy-MM-dd HH24:mi:ss'),
                                     '1',
                                     TO_DATE('2022-3-4 17:29:01',
                                             'yyyy-MM-dd HH24:mi:ss'),
                                     '');

DECLARE
  content_3 CLOB := '<#setting number_format="#"><table class="table-form two-column" style="width:100%;">    <caption>        ${ent.name}    </caption>   <colgroup>            <col width="20%"/>            <col width="80%"/>    </colgroup>    <tbody>        <#list ent.boAttrList as field>     <#if (field.control!=''rx-ref'')>       <tr <#if (field_index==0)>class="firstRow"</#if>>         <td style="word-break: break-all;">           ${field.comment}          </td>         <td>            <@fieldCtrl field=field type=''main'' />          </td>       </tr>     </#if>                </#list>    </tbody></table>';
BEGIN
  UPDATE form_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1275710436348100609';
  COMMIT;
END;
/

INSERT
  INTO form_template(ID_,
                     NAME_,
                     ALIAS_,
                     TEMPLATE_,
                     TYPE_,
                     CATEGORY_,
                     FILE_NAME_,
                     PATH_,
                     SINGLE_,
                     GEN_MODE_,
                     MAIN_SUB_TYPE_,
                     DELETED_,
                     COMPANY_ID_,
                     TENANT_ID_,
                     CREATE_DEP_ID_,
                     CREATE_BY_,
                     CREATE_TIME_,
                     UPDATE_BY_,
                     UPDATE_TIME_,
                     APP_ID_) VALUES('1277077170606948354',
                                     '主表双列模版',
                                     'twocolumn',
                                     '',
                                     'pc',
                                     'main',
                                     '',
                                     '',
                                     '',
                                     '',
                                     '',
                                     0,
                                     '0',
                                     '0',
                                     '2',
                                     '1',
                                     TO_DATE('2020-6-28 11:11:39',
                                             'yyyy-MM-dd HH24:mi:ss'),
                                     '1',
                                     TO_DATE('2022-3-4 17:28:21',
                                             'yyyy-MM-dd HH24:mi:ss'),
                                     '');

DECLARE
  content_3 CLOB := '<#setting number_format="#"><table class="table-form four-column" style="width:100%;">    <caption>        ${ent.name}    </caption>   <col width="15%"/>            <col width="35%"/>            <col width="15%"/>            <col width="35%"/>    <tbody>  <#assign flag=0>    <#list ent.boAttrList as field>   <#if (field.control!=''rx-ref'')>     <#if (!field.spans || field.spans==1)>      <#if (field_index ==0 || flag==0) >       <tr <#if (field_index==0)>class="firstRow"</#if>>     </#if>        <td style="word-break: break-all;">         ${field.comment}        </td>       <td>          <#assign flag++>          <@fieldCtrl field=field type=''main'' />        </td>       <#if ((field_index ==0 ' || '&&' ||
                    ' !field_has_next ' || '&&' ||
                    ' ent.boAttrList[field_index+1].spans==2) || (ent.boAttrList[field_index-1].spans==2 ' || '&&' ||
                    ' !field_has_next ' || '&&' ||
                    ' ent.boAttrList[field_index+1].spans==2 ) ) >          <td></td>         <td></td>       </#if>        <#if !field_has_next || (field_index ==0  ' || '&&' ||
                    ' ent.boAttrList[field_index+1].spans==2)||        (field_index !=0  ' || '&&' ||
                    ' ent.boAttrList[field_index-1].spans==1 ' || '&&' ||
                    ' ent.boAttrList[field_index+1].spans==2)||        (field_index !=0  ' || '&&' ||
                    ' ent.boAttrList[field_index-1].spans==2 ' || '&&' ||
                    ' ent.boAttrList[field_index+1].spans==2)||        (field_index !=0 ' || '&&' ||
                    ' ent.boAttrList[field_index+1].spans==1 ' || '&&' ||
                    ' flag==2) >          <#assign flag=0>          </tr>       </#if>    <#else>       <tr class="firstRow">       <td style="word-break: break-all;">         ${field.comment}        </td>       <td rowspan="1" colspan="3">          <@fieldCtrl field=field type=''main'' />        </td>     </tr>   </#if>    </#if>    </#list>    </tbody></table>';
BEGIN
  UPDATE form_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1277077170606948354';
  COMMIT;
END;
/

INSERT
  INTO form_template(ID_,
                     NAME_,
                     ALIAS_,
                     TEMPLATE_,
                     TYPE_,
                     CATEGORY_,
                     FILE_NAME_,
                     PATH_,
                     SINGLE_,
                     GEN_MODE_,
                     MAIN_SUB_TYPE_,
                     DELETED_,
                     COMPANY_ID_,
                     TENANT_ID_,
                     CREATE_DEP_ID_,
                     CREATE_BY_,
                     CREATE_TIME_,
                     UPDATE_BY_,
                     UPDATE_TIME_,
                     APP_ID_) VALUES('1277080670808162306',
                                     '字段控件模版',
                                     'fieldCtrl',
                                     '',
                                     'pc',
                                     'field',
                                     '',
                                     '',
                                     '',
                                     '',
                                     '',
                                     0,
                                     '0',
                                     '0',
                                     '2',
                                     '1',
                                     TO_DATE('2020-6-28 11:25:34',
                                             'yyyy-MM-dd HH24:mi:ss'),
                                     '',
                                     TO_DATE('2022-3-9 15:07:46',
                                             'yyyy-MM-dd HH24:mi:ss'),
                                     '');

DECLARE
  content_3 CLOB := '<#setting number_format="#"><#macro fieldCtrl field type entName readonly isAttr>    <${field.control} class="${field.control}" isattr="${isAttr}" type="${type}" ctltype="${field.control}" style="width:100%"                        <@attrJson field=field type=type entName=entName readonly=readonly/>            ></${field.control}></#macro><#macro attrJson field type entName readonly>    :readonly="${readonly???string(readonly,''readonly'')}" ctlid="${field.id}"    <#if (type==''main'')>        id="${util.randomId()}" :permission="permission.${field.name}" v-model="data.${field.name}"        :valid="validFunc(''main'',''${field.name}'')"        v-on:enter="enter(''main'',''${field.name}'')"        v-on:valuechange="valuechange(''main'',''${field.name}'')"    <#elseif (type==''onetoone'')>   id="${field.name}${field.id}"        :permission="permission.sub__${entName}.${field.name}"        :valid="validFunc(''${entName}'',''${field.name}'',item)"        v-model="data.sub__${entName}.${field.name}"        v-on:enter="enter(''${entName}'',''${field.name}'',item)"        v-on:valuechange="valuechange(''${entName}'',''${field.name}'',item)"    <#else>   id="${field.name}${field.id}"        :permission="permission.sub__${entName}.${field.name}" v-model="item.${field.name}" :data="item"        :valid="validFunc(''${entName}'',''${field.name}'',index)"        v-on:enter="enter(''${entName}'',''${field.name}'',item)"        v-on:valuechange="valuechange(''${entName}'',''${field.name}'',item)"        length="${field.length}" from="input"    </#if></#macro>';
BEGIN
  UPDATE form_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1277080670808162306';
  COMMIT;
END;
/

INSERT
  INTO form_template(ID_,
                     NAME_,
                     ALIAS_,
                     TEMPLATE_,
                     TYPE_,
                     CATEGORY_,
                     FILE_NAME_,
                     PATH_,
                     SINGLE_,
                     GEN_MODE_,
                     MAIN_SUB_TYPE_,
                     DELETED_,
                     COMPANY_ID_,
                     TENANT_ID_,
                     CREATE_DEP_ID_,
                     CREATE_BY_,
                     CREATE_TIME_,
                     UPDATE_BY_,
                     UPDATE_TIME_,
                     APP_ID_) VALUES('1277081573669847041',
                                     '子表行模版',
                                     'subOneToMany',
                                     '',
                                     'pc',
                                     'onetomany',
                                     '',
                                     '',
                                     '',
                                     '',
                                     '',
                                     0,
                                     '0',
                                     '0',
                                     '2',
                                     '1',
                                     TO_DATE('2020-6-28 11:29:09',
                                             'yyyy-MM-dd HH24:mi:ss'),
                                     '',
                                     TO_DATE('2022-3-4 10:36:17',
                                             'yyyy-MM-dd HH24:mi:ss'),
                                     '');

DECLARE
  content_3 CLOB := '<#setting number_format="#">            <div class="rx-table-body active" ctltype="rx-table" tableid="${ent.id}" type="onetomany"                 id="${util.randomId()}" tablename="${ent.alias}">                <div class="table-header-tool">                    <div class="title">                        <div>                        ${ent.name}                        </div>                    </div>                    <a-button-group>                        <a-button v-on:click="add(''${ent.alias}'',''inner'')">                            添加                        </a-button>                        <a-button v-on:click="remove(''${ent.alias}'',''inner'')">                            删除                        </a-button>                        <a-button v-on:click="up(''${ent.alias}'',''inner'')">                            上移                        </a-button>                        <a-button v-on:click="down(''${ent.alias}'',''inner'')">                            下移                        </a-button>                    </a-button-group>                </div>                <div class="rx-table-box">                    <table class="rx-table">                        <col style="width: 60px;"/>                         <col style="width: 60px;"/>                         <#list ent.boAttrList as field>                             <#if (field.control!=''rx-ref'')>                            <col style="width: 160px;"/>              </#if>                         </#list>                        <thead>                        <tr class="firstRow">                            <th>#</th>             <th>序号</th>                            <#list ent.boAttrList as field>              <#if (field.control!=''rx-ref'')>                             <th>                                    ${field.comment}                                </th>               </#if>                            </#list>                        </tr>                        </thead>                        <tbody>                        <tr v-for="(item,index) in data.sub__${ent.alias}" :key="item.index_" v-tableselect="{item:item,selmode:&#39;multi&#39;,data:data.sub__${ent.alias}}"                            :class="{active:item.selected}">                            <td><a-checkbox :checked="item.selected"></a-checkbox></td>               <td><rx-serial :serial="index+1"></rx-serial></td>                            <#list ent.boAttrList as field>                                <#if (field.control!=''rx-ref'')>                                     <td>                                        <@fieldCtrl field=field type=''onetomany'' entName=ent.alias />                                    </td>                </#if>                            </#list>                        </tr>                        </tbody>                    </table>         <div class="tableEmpty" v-if="data.sub__${ent.alias}' || '&&' ||
                    'data.sub__${ent.alias}.length <= 0">                                <a-empty></a-empty>                            </div>                </div>            </div>';
BEGIN
  UPDATE form_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1277081573669847041';
  COMMIT;
END;
/

INSERT
  INTO form_template(ID_,
                     NAME_,
                     ALIAS_,
                     TEMPLATE_,
                     TYPE_,
                     CATEGORY_,
                     FILE_NAME_,
                     PATH_,
                     SINGLE_,
                     GEN_MODE_,
                     MAIN_SUB_TYPE_,
                     DELETED_,
                     COMPANY_ID_,
                     TENANT_ID_,
                     CREATE_DEP_ID_,
                     CREATE_BY_,
                     CREATE_TIME_,
                     UPDATE_BY_,
                     UPDATE_TIME_,
                     APP_ID_) VALUES('1277085691964669953',
                                     '子表双列模版',
                                     'subOneToOneTwoColumn',
                                     '',
                                     'pc',
                                     'onetoone',
                                     '',
                                     '',
                                     '',
                                     '',
                                     '',
                                     0,
                                     '0',
                                     '0',
                                     '2',
                                     '1',
                                     TO_DATE('2020-6-28 11:45:31',
                                             'yyyy-MM-dd HH24:mi:ss'),
                                     '1',
                                     TO_DATE('2022-3-4 10:38:23',
                                             'yyyy-MM-dd HH24:mi:ss'),
                                     '');

DECLARE
  content_3 CLOB := '<table class="table-form four-column" style="width:100%;" id="${util.randomId()}" type="onetoone" ctltype="rx-table-onetoone" tableid="${ent.id}" tablename="${ent.alias}">    <caption>        ${ent.name}    </caption><colgroup>                        <col width="15%"/>                        <col width="35%"/>                        <col width="15%"/>                        <col width="35%"/>                    </colgroup>    <tbody>    <#list ent.boAttrList as field>    <#if (field.control!=''rx-ref'')>           <#if (field_index % 2==0)>            <tr <#if (field_index==0)>class="firstRow"</#if>>        </#if>        <td style="word-break: break-all;">            ${field.comment}        </td>        <td>            <@fieldCtrl field=field type=''onetoone'' entName=ent.alias />        </td>        <#if field_index % 2 == 0 ' || '&&' ||
                    ' !field_has_next>            <td></td>            <td></td>        </#if>        <#if field_index % 2 == 1 || !field_has_next>            </tr>        </#if>                     </#if>    </#list>    </tbody></table>';
BEGIN
  UPDATE form_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1277085691964669953';
  COMMIT;
END;
/

INSERT
  INTO form_template(ID_,
                     NAME_,
                     ALIAS_,
                     TEMPLATE_,
                     TYPE_,
                     CATEGORY_,
                     FILE_NAME_,
                     PATH_,
                     SINGLE_,
                     GEN_MODE_,
                     MAIN_SUB_TYPE_,
                     DELETED_,
                     COMPANY_ID_,
                     TENANT_ID_,
                     CREATE_DEP_ID_,
                     CREATE_BY_,
                     CREATE_TIME_,
                     UPDATE_BY_,
                     UPDATE_TIME_,
                     APP_ID_) VALUES('1277085945392906241',
                                     '子表单列模版',
                                     'subWinOneColumn',
                                     '',
                                     'pc',
                                     'onetomany',
                                     '',
                                     '',
                                     '',
                                     '',
                                     '',
                                     0,
                                     '0',
                                     '0',
                                     '2',
                                     '1',
                                     TO_DATE('2020-6-28 11:46:31',
                                             'yyyy-MM-dd HH24:mi:ss'),
                                     '1',
                                     TO_DATE('2022-2-21 16:42:27',
                                             'yyyy-MM-dd HH24:mi:ss'),
                                     '');

DECLARE
  content_3 CLOB := '<#setting number_format="#"><div class="rx-table-body" ctltype="rx-table" type="onetomany" id="${util.randomId()}" tableid="${ent.id}" tablename="${ent.alias}" tabname="${ent.alias}" style="width:100%">        <div class="table-header-tool">            <div class="title">                <div>                    ${ent.name}                </div>            </div>            <a-button-group v-if="!getReadonly()">                 <a-button  v-if="getSubTablePermission(''${ent.alias}'',''add'')" v-on:click="add(''${ent.alias}'',''window'',''${ent.name}'')">                    添加                </a-button>          <a-button v-if="getSubTablePermission(''${ent.alias}'',''edit'')" v-on:click="edit(''${ent.alias}'',''window'',''${ent.name}'')">                    编辑                </a-button>                <a-button  v-if="getSubTablePermission(''${ent.alias}'',''remove'')" v-on:click="remove(''${ent.alias}'',''window'',''${ent.name}'')">                    删除                </a-button>                <a-button v-if="getSubTablePermission(''${ent.alias}'',''up'')" v-on:click="up(''${ent.alias}'',''window'',''${ent.name}'')">                    上移                </a-button>                <a-button v-if="getSubTablePermission(''${ent.alias}'',''down'')" v-on:click="down(''${ent.alias}'',''window'',''${ent.name}'')">                    下移                </a-button>            </a-button-group>        </div>        <div class="rx-table-box" style="display: none">            <table class="rx-table">                <colgroup>                    <col style="width: 60px;"/>               <col style="width: 60px;"/>                     <#list ent.boAttrList as field>                            <col style="width: 160px;"/>                    </#list>                                   </colgroup>                <thead>                    <tr class="firstRow">                        <th>#</th>                        <th>序号 </th>                       <#list ent.boAttrList as field>                            <th>                                ${field.comment}                            </th>                        </#list>                    </tr>                </thead>                <tbody>        <tr v-if="item" v-for="(item,index) in data.sub__${ent.alias}" :key="item.index_" v-tableselect="{item:item,selmode:&#39;multi&#39;,data:data.sub__${ent.alias}}" :class="{active:item.selected}" dblclick="edit(''${ent.alias}'')">                         <td><a-checkbox :checked="item.selected"></a-checkbox> </td>            <td>${index + 1}</td>                        <#list ent.boAttrList as field>                            <td>                                <@fieldCtrl field=field type=''onetomany'' entName=ent.alias readonly=true />                            </td>                        </#list>                    </tr>                </tbody>            </table>            <div class="tableEmpty" v-if="data.sub__${ent.alias} ' || '&&' ||
                    ' data.sub__${ent.alias}.length <= 0">                <a-empty></a-empty>            </div>        </div>        <div class="rx-table-dialog"  v-if="0"   id="dialog_${ent.alias}" >            <div class="dialog-header">                ${ent.name}            </div>            <table class="table-detail column-two table-align" style="width:100%;">                <colgroup>                   <col width="20%"/>                    <col width="80%"/>                </colgroup>                <tbody>                  <#list ent.boAttrList as field>                      <tr>                        <td>${field.comment}</td>                        <td>                            <@fieldCtrl field=field type=''onetomany'' entName=ent.alias isAttr=''false''/>                        </td>                      </tr>                    </#list>                </tbody>            </table>        </div>    </div>';
BEGIN
  UPDATE form_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1277085945392906241';
  COMMIT;
END;
/

INSERT
  INTO form_template(ID_,
                     NAME_,
                     ALIAS_,
                     TEMPLATE_,
                     TYPE_,
                     CATEGORY_,
                     FILE_NAME_,
                     PATH_,
                     SINGLE_,
                     GEN_MODE_,
                     MAIN_SUB_TYPE_,
                     DELETED_,
                     COMPANY_ID_,
                     TENANT_ID_,
                     CREATE_DEP_ID_,
                     CREATE_BY_,
                     CREATE_TIME_,
                     UPDATE_BY_,
                     UPDATE_TIME_,
                     APP_ID_) VALUES('1277086151886880770',
                                     '子表双列模版',
                                     'subWinTwoColumn',
                                     '',
                                     'pc',
                                     'onetomany',
                                     '',
                                     '',
                                     '',
                                     '',
                                     '',
                                     0,
                                     '0',
                                     '0',
                                     '2',
                                     '1',
                                     TO_DATE('2020-6-28 11:47:20',
                                             'yyyy-MM-dd HH24:mi:ss'),
                                     '1',
                                     TO_DATE('2022-3-4 10:40:27',
                                             'yyyy-MM-dd HH24:mi:ss'),
                                     '');

DECLARE
  content_3 CLOB := '<#setting number_format="#"><div class="rx-table-body" ctltype="rx-table" type="onetomany" id="${util.randomId()}" tableid="${ent.id}" tablename="${ent.alias}" tabname="${ent.alias}" style="width:100%">        <div class="table-header-tool">            <div class="title">                <div>                    ${ent.name}                </div>            </div>            <a-button-group v-if="!getReadonly()">                 <a-button  v-if="getSubTablePermission(''${ent.alias}'',''add'')" v-on:click="add(''${ent.alias}'',''window'',''${ent.name}'')">                    添加                </a-button>          <a-button v-if="getSubTablePermission(''${ent.alias}'',''edit'')" v-on:click="edit(''${ent.alias}'',''window'',''${ent.name}'')">                    编辑                </a-button>                <a-button  v-if="getSubTablePermission(''${ent.alias}'',''remove'')" v-on:click="remove(''${ent.alias}'',''window'',''${ent.name}'')">                    删除                </a-button>                <a-button v-if="getSubTablePermission(''${ent.alias}'',''up'')" v-on:click="up(''${ent.alias}'',''window'',''${ent.name}'')">                    上移                </a-button>                <a-button v-if="getSubTablePermission(''${ent.alias}'',''down'')" v-on:click="down(''${ent.alias}'',''window'',''${ent.name}'')">                    下移                </a-button>            </a-button-group>        </div>        <div class="rx-table-box" style="display: none">            <table class="rx-table">                <colgroup>                    <col style="width: 60px;"/>               <col style="width: 60px;"/>                     <#list ent.boAttrList as field>           <#if (field.control!=''rx-ref'')>             <col style="width: 160px;"/>            </#if>                     </#list>                                   </colgroup>                <thead>                    <tr class="firstRow">                        <th>#</th>           <th style="text-align:center;">选择</th>                       <#list ent.boAttrList as field>               <#if (field.control!=''rx-ref'')>                 <th>                 ${field.comment}                </th>             </#if>                         </#list>                    </tr>                </thead>                <tbody>                    <tr v-if="item" v-for="(item,index) in data.sub__${ent.alias}" :key="item.index_" v-select="item" :class="{active:item.selected}" dblclick="edit(''${ent.alias}'')">                        <td>${index + 1}</td>              <td style="text-align:center;"><a-checkbox :checked="item.selected" @change=''item=>item.selected==!item.selected''></a-checkbox></td>                        <#list ent.boAttrList as field>             <#if (field.control!=''rx-ref'')>               <td>                  <@fieldCtrl field=field type=''onetomany'' entName=ent.alias readonly=true />               </td>             </#if>                        </#list>                    </tr>                </tbody>            </table>            <div class="tableEmpty" v-if="data.sub__${ent.alias} ' || '&&' ||
                    ' data.sub__${ent.alias}.length <= 0">                <a-empty></a-empty>            </div>        </div>        <div class="rx-table-dialog"  v-if="0"   id="dialog_${ent.alias}" >            <div class="dialog-header">                ${ent.name}            </div>            <table class="table-detail column-four table-align" style="width:100%;">                <colgroup>                    <col width="15%"/>                    <col width="35%"/>                    <col width="15%"/>                    <col width="35%"/>                </colgroup>                <tbody>                   <#list ent.boAttrList as field>           <#if (field.control!=''rx-ref'')>               <#if (field_index % 2==0)>                            <tr>                        </#if>                        <td style="word-break: break-all;">                            ${field.comment}                        </td>                        <td>                            <@fieldCtrl field=field type=''onetomany'' entName=ent.alias isAttr=''false'' />                        </td>                        <#if field_index % 2 == 0 ' || '&&' ||
                    ' !field_has_next>                            <td></td>                            <td></td>                        </#if>                        <#if field_index % 2 == 1 || !field_has_next>                            </tr>                        </#if>            </#if>                    </#list>                </tbody>            </table>        </div>    </div>';
BEGIN
  UPDATE form_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1277086151886880770';
  COMMIT;
END;
/

INSERT
  INTO form_template(ID_,
                     NAME_,
                     ALIAS_,
                     TEMPLATE_,
                     TYPE_,
                     CATEGORY_,
                     FILE_NAME_,
                     PATH_,
                     SINGLE_,
                     GEN_MODE_,
                     MAIN_SUB_TYPE_,
                     DELETED_,
                     COMPANY_ID_,
                     TENANT_ID_,
                     CREATE_DEP_ID_,
                     CREATE_BY_,
                     CREATE_TIME_,
                     UPDATE_BY_,
                     UPDATE_TIME_,
                     APP_ID_) VALUES('1277087373637304321',
                                     '子表单列模版',
                                     'subOneToOne',
                                     '',
                                     'pc',
                                     'onetoone',
                                     '',
                                     '',
                                     '',
                                     '',
                                     '',
                                     0,
                                     '0',
                                     '0',
                                     '2',
                                     '1',
                                     TO_DATE('2020-6-28 11:52:12',
                                             'yyyy-MM-dd HH24:mi:ss'),
                                     '1',
                                     TO_DATE('2020-12-11 17:51:11',
                                             'yyyy-MM-dd HH24:mi:ss'),
                                     '');

DECLARE
  content_3 CLOB := '<table class="table-form two-column" style="width:100%;" id="${util.randomId()}" type="onetoone" ctltype="rx-table-onetoone" tableid="${ent.id}" tablename="${ent.alias}">    <caption>        ${ent.name}    </caption>    <colgroup>            <col width="20%"/>            <col width="80%"/>    </colgroup>    <tbody>    <#list ent.boAttrList as field>    <tr <#if (field_index==0)>class="firstRow"</#if>>        <td style="word-break: break-all;">            ${field.comment}        </td>        <td>            <@fieldCtrl field=field type=''onetoone'' entName=ent.alias />        </td>    </tr>    </#list>    </tbody></table>';
BEGIN
  UPDATE form_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1277087373637304321';
  COMMIT;
END;
/

INSERT
  INTO form_template(ID_,
                     NAME_,
                     ALIAS_,
                     TEMPLATE_,
                     TYPE_,
                     CATEGORY_,
                     FILE_NAME_,
                     PATH_,
                     SINGLE_,
                     GEN_MODE_,
                     MAIN_SUB_TYPE_,
                     DELETED_,
                     COMPANY_ID_,
                     TENANT_ID_,
                     CREATE_DEP_ID_,
                     CREATE_BY_,
                     CREATE_TIME_,
                     UPDATE_BY_,
                     UPDATE_TIME_,
                     APP_ID_) VALUES('1278256114231779329',
                                     '字段控件模板',
                                     'fieldCtrl',
                                     '',
                                     'print',
                                     'field',
                                     '',
                                     '',
                                     '',
                                     '',
                                     '',
                                     0,
                                     '0',
                                     '0',
                                     '',
                                     '1',
                                     TO_DATE('2020-7-1 17:16:21',
                                             'yyyy-MM-dd HH24:mi:ss'),
                                     '1',
                                     TO_DATE('2021-7-29 11:21:29',
                                             'yyyy-MM-dd HH24:mi:ss'),
                                     '');

DECLARE
  content_3 CLOB := '<#function getPre pre>
  <#assign rtn><#if pre=="">data.<#elseif pre="item_">item.<#else>data.SUB_${pre}.</#if></#assign>
  <#return rtn>
</#function>
<#macro getField attr pre>
  <#switch attr.control>
    <#default>
    <#noparse>${util.render(</#noparse>${getPre(pre)}${attr.name}<#noparse>,''</#noparse>${attr.control}<#noparse>'')}</#noparse>
  </#switch>
</#macro>';
BEGIN
  UPDATE form_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1278256114231779329';
  COMMIT;
END;
/

INSERT
  INTO form_template(ID_,
                     NAME_,
                     ALIAS_,
                     TEMPLATE_,
                     TYPE_,
                     CATEGORY_,
                     FILE_NAME_,
                     PATH_,
                     SINGLE_,
                     GEN_MODE_,
                     MAIN_SUB_TYPE_,
                     DELETED_,
                     COMPANY_ID_,
                     TENANT_ID_,
                     CREATE_DEP_ID_,
                     CREATE_BY_,
                     CREATE_TIME_,
                     UPDATE_BY_,
                     UPDATE_TIME_,
                     APP_ID_) VALUES('1278258262659145730',
                                     '子表单列模版',
                                     'subOneToOne',
                                     '',
                                     'print',
                                     'onetoone',
                                     '',
                                     '',
                                     '',
                                     '',
                                     '',
                                     0,
                                     '0',
                                     '0',
                                     '',
                                     '1',
                                     TO_DATE('2020-7-1 17:24:53',
                                             'yyyy-MM-dd HH24:mi:ss'),
                                     '1',
                                     TO_DATE('2020-7-1 17:38:37',
                                             'yyyy-MM-dd HH24:mi:ss'),
                                     '');

DECLARE
  content_3 CLOB := '<table class="table-form two-column" style="width:100%;" id="${util.randomId()}" type="onetoone" ctltype="rx-table-onetoone" tableid="${ent.id}" tablename="${ent.alias}">    <caption>        ${ent.name}    </caption>    <tbody>    <#list ent.boAttrList as field>    <tr <#if (field_index==0)>class="firstRow"</#if>>        <td style="word-break: break-all;">            ${field.comment}        </td>        <td>            <@getField attr=field pre=ent.name />        </td>    </tr>    </#list>    </tbody></table>';
BEGIN
  UPDATE form_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1278258262659145730';
  COMMIT;
END;
/

INSERT
  INTO form_template(ID_,
                     NAME_,
                     ALIAS_,
                     TEMPLATE_,
                     TYPE_,
                     CATEGORY_,
                     FILE_NAME_,
                     PATH_,
                     SINGLE_,
                     GEN_MODE_,
                     MAIN_SUB_TYPE_,
                     DELETED_,
                     COMPANY_ID_,
                     TENANT_ID_,
                     CREATE_DEP_ID_,
                     CREATE_BY_,
                     CREATE_TIME_,
                     UPDATE_BY_,
                     UPDATE_TIME_,
                     APP_ID_) VALUES('1278258334415298562',
                                     '子表双列模版',
                                     'subOneToOneTwoColumn',
                                     '',
                                     'print',
                                     'onetoone',
                                     '',
                                     '',
                                     '',
                                     '',
                                     '',
                                     0,
                                     '0',
                                     '0',
                                     '',
                                     '1',
                                     TO_DATE('2020-7-1 17:25:10',
                                             'yyyy-MM-dd HH24:mi:ss'),
                                     '1',
                                     TO_DATE('2020-7-1 17:38:32',
                                             'yyyy-MM-dd HH24:mi:ss'),
                                     '');

DECLARE
  content_3 CLOB := '<table class="table-form four-column" style="width:100%;" id="${util.randomId()}" type="onetoone" ctltype="rx-table-onetoone" tableid="${ent.id}" tablename="${ent.alias}">    <caption>        ${ent.name}    </caption>    <tbody>    <#list ent.boAttrList as field>        <#if (field_index % 2==0)>            <tr <#if (field_index==0)>class="firstRow"</#if>>        </#if>        <td style="word-break: break-all;">            ${field.comment}        </td>        <td>            <@getField attr=field pre=ent.name />        </td>        <#if field_index % 2 == 0 ' || '&&' ||
                    ' !field_has_next>            <td></td>            <td></td>        </#if>        <#if field_index % 2 == 1 || !field_has_next>            </tr>        </#if>    </#list>    </tbody></table>';
BEGIN
  UPDATE form_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1278258334415298562';
  COMMIT;
END;
/

INSERT
  INTO form_template(ID_,
                     NAME_,
                     ALIAS_,
                     TEMPLATE_,
                     TYPE_,
                     CATEGORY_,
                     FILE_NAME_,
                     PATH_,
                     SINGLE_,
                     GEN_MODE_,
                     MAIN_SUB_TYPE_,
                     DELETED_,
                     COMPANY_ID_,
                     TENANT_ID_,
                     CREATE_DEP_ID_,
                     CREATE_BY_,
                     CREATE_TIME_,
                     UPDATE_BY_,
                     UPDATE_TIME_,
                     APP_ID_) VALUES('1278258401062789122',
                                     '主表双列模版',
                                     'twoColumn',
                                     '',
                                     'print',
                                     'main',
                                     '',
                                     '',
                                     '',
                                     '',
                                     '',
                                     0,
                                     '0',
                                     '0',
                                     '',
                                     '1',
                                     TO_DATE('2020-7-1 17:25:26',
                                             'yyyy-MM-dd HH24:mi:ss'),
                                     '1',
                                     TO_DATE('2022-5-5 15:18:03',
                                             'yyyy-MM-dd HH24:mi:ss'),
                                     '');

DECLARE
  content_3 CLOB := '<#setting number_format="#"><table class="table-form four-column" style="width:100%;">    <caption>        ${ent.name}    </caption>    <tbody>    <#list ent.boAttrList as field>        <#if (field_index % 2==0)>        <tr <#if (field_index==0)>class="firstRow"</#if>>        </#if>            <td style="word-break: break-all;">                <#if ent.isTenant==1>                     <#noparse>${label.</#noparse>${field.name}<#noparse>}</#noparse>                 <#else>                     ${field.comment}                </#if>            </td>            <td>                <@getField attr=field pre="" />            </td>            <#if field_index % 2 == 0 ' || '&&' ||
                    ' !field_has_next>                <td></td>                <td></td>            </#if>            <#if field_index % 2 == 1 || !field_has_next>                </tr>            </#if>    </#list>    </tbody></table>';
BEGIN
  UPDATE form_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1278258401062789122';
  COMMIT;
END;
/

INSERT
  INTO form_template(ID_,
                     NAME_,
                     ALIAS_,
                     TEMPLATE_,
                     TYPE_,
                     CATEGORY_,
                     FILE_NAME_,
                     PATH_,
                     SINGLE_,
                     GEN_MODE_,
                     MAIN_SUB_TYPE_,
                     DELETED_,
                     COMPANY_ID_,
                     TENANT_ID_,
                     CREATE_DEP_ID_,
                     CREATE_BY_,
                     CREATE_TIME_,
                     UPDATE_BY_,
                     UPDATE_TIME_,
                     APP_ID_) VALUES('1278258462500954113',
                                     '主表单列模板',
                                     'oneColumn',
                                     '',
                                     'print',
                                     'main',
                                     '',
                                     '',
                                     '',
                                     '',
                                     '',
                                     0,
                                     '0',
                                     '0',
                                     '',
                                     '1',
                                     TO_DATE('2020-7-1 17:25:41',
                                             'yyyy-MM-dd HH24:mi:ss'),
                                     '1',
                                     TO_DATE('2022-5-5 15:28:33',
                                             'yyyy-MM-dd HH24:mi:ss'),
                                     '');

DECLARE
  content_3 CLOB := '<#setting number_format="#"><table class="table-form two-column" style="width:100%;">    <caption>        ${ent.name}    </caption>    <tbody>        <#list ent.boAttrList as field>        <tr <#if (field_index==0)>class="firstRow"</#if>>            <td style="word-break: break-all;">               <#if ent.isTenant==1>                     <#noparse>${label.</#noparse>${field.name}<#noparse>}</#noparse>                 <#else>                     ${field.comment}                </#if>            </td>            <td>                <@getField attr=field pre="" />            </td>        </tr>        </#list>    </tbody></table>';
BEGIN
  UPDATE form_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1278258462500954113';
  COMMIT;
END;
/

INSERT
  INTO form_template(ID_,
                     NAME_,
                     ALIAS_,
                     TEMPLATE_,
                     TYPE_,
                     CATEGORY_,
                     FILE_NAME_,
                     PATH_,
                     SINGLE_,
                     GEN_MODE_,
                     MAIN_SUB_TYPE_,
                     DELETED_,
                     COMPANY_ID_,
                     TENANT_ID_,
                     CREATE_DEP_ID_,
                     CREATE_BY_,
                     CREATE_TIME_,
                     UPDATE_BY_,
                     UPDATE_TIME_,
                     APP_ID_) VALUES('1278260208514191361',
                                     '子表行模式',
                                     'dOneColumn',
                                     '',
                                     'print',
                                     'onetomany',
                                     '',
                                     '',
                                     '',
                                     '',
                                     '',
                                     0,
                                     '0',
                                     '0',
                                     '',
                                     '1',
                                     TO_DATE('2020-7-1 17:32:37',
                                             'yyyy-MM-dd HH24:mi:ss'),
                                     '1',
                                     TO_DATE('2021-7-29 11:25:00',
                                             'yyyy-MM-dd HH24:mi:ss'),
                                     '');

DECLARE
  content_3 CLOB := '<#setting number_format="#"><table class="table-form two-column" style="width:100%;" data-sort="sortDisabled">    <tr class="firstRow">        <td style="word-break: break-all;" rowspan="1" colspan="1">            <div class="rx-table-body active" ctltype="rx-table" tableid="${ent.id}" type="onetomany"                 id="${util.randomId()}" tablename="${ent.alias}">                <div class="rx-table-box">                    <table class="rx-table">                        <col style="width: 60px;"/>                         <#list ent.boAttrList as field>                         <col style="width: 160px;"/>                         </#list>                        <thead>                        <tr class="firstRow">                            <th>#</th>                            <#list ent.boAttrList as field>                                <th>                                    ${field.comment}                                </th>                            </#list>                        </tr>                        </thead>                        <tbody>                        <tr rx-list="sub__${ent.alias}" :key="item.index_" v-select="item"                            :class="{active:item.selected}">                            <td><#noparse>${item_index+1}</#noparse></td>                            <#list ent.boAttrList as field>                                <td>                                    <@getField attr=field pre="item_"/>                                </td>                            </#list>                        </tr>                        </tbody>                    </table>                </div>            </div>        </td>    </tr></table>';
BEGIN
  UPDATE form_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1278260208514191361';
  COMMIT;
END;
/

INSERT
  INTO form_template(ID_,
                     NAME_,
                     ALIAS_,
                     TEMPLATE_,
                     TYPE_,
                     CATEGORY_,
                     FILE_NAME_,
                     PATH_,
                     SINGLE_,
                     GEN_MODE_,
                     MAIN_SUB_TYPE_,
                     DELETED_,
                     COMPANY_ID_,
                     TENANT_ID_,
                     CREATE_DEP_ID_,
                     CREATE_BY_,
                     CREATE_TIME_,
                     UPDATE_BY_,
                     UPDATE_TIME_,
                     APP_ID_) VALUES('1471759491079692289',
                                     '审批历史',
                                     'approvalHistory',
                                     '',
                                     'print',
                                     'field',
                                     '',
                                     '',
                                     '',
                                     '',
                                     '',
                                     0,
                                     '0',
                                     '1',
                                     '1',
                                     '1',
                                     TO_DATE('2021-12-17 16:29:41',
                                             'yyyy-MM-dd HH24:mi:ss'),
                                     '1',
                                     TO_DATE('2021-12-17 17:54:59',
                                             'yyyy-MM-dd HH24:mi:ss'),
                                     '');

DECLARE
  content_3 CLOB := '<table class="rx-table">  <thead>   <tr class="firstRow">   <th>创建时间</th>   <th>处理时间</th>   <th>停留时间</th>   <th>审批节点</th>   <th>操作人</th>    <th>类型</th>   <th>意见</th>   </tr>   </thead>    <tbody>     <tr rx-list="history" :key="item.index_">      <td>${item.createTime}</td>       <td>${item.completeTime}</td>      <td>${item.duration}秒</td>      <td>${item.nodeName}</td>       <td>${item.createBy}</td>       <td>${item.checkStatus}</td>      <td>${item.remark}</td>     </tr>     </#list>  </tbody> </table> ';
BEGIN
  UPDATE form_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1471759491079692289';
  COMMIT;
END;
/

INSERT
  INTO form_template(ID_,
                     NAME_,
                     ALIAS_,
                     TEMPLATE_,
                     TYPE_,
                     CATEGORY_,
                     FILE_NAME_,
                     PATH_,
                     SINGLE_,
                     GEN_MODE_,
                     MAIN_SUB_TYPE_,
                     DELETED_,
                     COMPANY_ID_,
                     TENANT_ID_,
                     CREATE_DEP_ID_,
                     CREATE_BY_,
                     CREATE_TIME_,
                     UPDATE_BY_,
                     UPDATE_TIME_,
                     APP_ID_) VALUES('1533697561588920321',
                                     '代码生成_正向Apijs代码',
                                     'codeGenApijsW',
                                     '',
                                     'codeGen',
                                     'main',
                                     '{classVar}.js',
                                     'src/api/{system}/{package}',
                                     'YES',
                                     'genCode',
                                     'mainSub',
                                     0,
                                     '0',
                                     '1',
                                     '1',
                                     '1',
                                     TO_DATE('2022-6-6 14:29:28',
                                             'yyyy-MM-dd HH24:mi:ss'),
                                     '',
                                     TO_DATE('2022-6-6 14:29:28',
                                             'yyyy-MM-dd HH24:mi:ss'),
                                     '');

DECLARE
  content_3 CLOB := '<#assign package=model.vars.package><#assign class=model.vars.class><#assign classVar=model.vars.classVar><#assign developer=vars.developer><#assign system=vars.system><#assign name=model.name><#assign domain=vars.domain>import rxAjax from ''@/assets/js/ajax.js'';/* 概要说明:${name} api接口 开发人员:${developer}*/export const ${class}Api = {};${class}Api.baseUrl= ''/api-${system}/${system}/${package}/${classVar}'';${class}Api.exportUrl= ${class}Api.baseUrl + ''/export'';//查询列表${class}Api.query=function (parameter) {  var url= ${class}Api.baseUrl + ''/query'';  return rxAjax.postJson(url,parameter).then (res => {    return res.result  })}/** * 获取表单数据 * @param pkId * @returns {*} */${class}Api.getById =function(pkId) {    var url= ${class}Api.baseUrl + ''/getById?pkId='' + pkId;    return rxAjax.get(url);}/** * 保存表单数据 * @param formdata * @returns {*} */${class}Api.saveData =function(formdata) {    var url= ${class}Api.baseUrl + ''/saveData'';    return rxAjax.postJson(url,formdata);}//删除数据${class}Api.del =function(parameter) {  var url= ${class}Api.baseUrl + ''/del'';  return rxAjax.postUrl(url,parameter);}export  default ${class}Api;';
BEGIN
  UPDATE form_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1533697561588920321';
  COMMIT;
END;
/

INSERT
  INTO form_template(ID_,
                     NAME_,
                     ALIAS_,
                     TEMPLATE_,
                     TYPE_,
                     CATEGORY_,
                     FILE_NAME_,
                     PATH_,
                     SINGLE_,
                     GEN_MODE_,
                     MAIN_SUB_TYPE_,
                     DELETED_,
                     COMPANY_ID_,
                     TENANT_ID_,
                     CREATE_DEP_ID_,
                     CREATE_BY_,
                     CREATE_TIME_,
                     UPDATE_BY_,
                     UPDATE_TIME_,
                     APP_ID_) VALUES('1533697732414533634',
                                     '代码生成_正向Data.js_编辑JS',
                                     'codeGenDatajsW',
                                     '',
                                     'codeGen',
                                     'main',
                                     '{class}Data.js',
                                     'src/views/modules/{system}/{package}',
                                     'YES',
                                     'genCode',
                                     'mainSub',
                                     0,
                                     '0',
                                     '1',
                                     '1',
                                     '1',
                                     TO_DATE('2022-6-6 14:30:09',
                                             'yyyy-MM-dd HH24:mi:ss'),
                                     '1',
                                     TO_DATE('2022-6-13 15:13:21',
                                             'yyyy-MM-dd HH24:mi:ss'),
                                     '');

DECLARE
  content_3 CLOB := 'export default {    data(){        return {            //表单数据            data:${vueJs.data},            //从表单数据            initData:${vueJs.initData},            //公式            formulas:${vueJs.formulas},            //校验函数            rules:${vueJs.rules},            //区域计算            calcAreas:${vueJs.calcAreas},            //校验规则            validRules:${vueJs.validRules},            //自定义查询            customquery:${vueJs.customquery},            //元数据            metadata:${vueJs.metadata},            listSearchType:"${vueJs.listSearchType}",            treeJson:${vueJs.treeJson},            boRelations:${boRelationsStr}        }    }}';
BEGIN
  UPDATE form_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1533697732414533634';
  COMMIT;
END;
/

INSERT
  INTO form_template(ID_,
                     NAME_,
                     ALIAS_,
                     TEMPLATE_,
                     TYPE_,
                     CATEGORY_,
                     FILE_NAME_,
                     PATH_,
                     SINGLE_,
                     GEN_MODE_,
                     MAIN_SUB_TYPE_,
                     DELETED_,
                     COMPANY_ID_,
                     TENANT_ID_,
                     CREATE_DEP_ID_,
                     CREATE_BY_,
                     CREATE_TIME_,
                     UPDATE_BY_,
                     UPDATE_TIME_,
                     APP_ID_) VALUES('1533697966238593025',
                                     '代码生成_正向实体转json映射',
                                     'codeGenfileJsonW',
                                     '',
                                     'codeGen',
                                     'main',
                                     '{classVar}.json',
                                     'src/main/resources/entityJson/{package}',
                                     'YES',
                                     'genCode',
                                     'mainSub',
                                     0,
                                     '0',
                                     '1',
                                     '1',
                                     '1',
                                     TO_DATE('2022-6-6 14:31:05',
                                             'yyyy-MM-dd HH24:mi:ss'),
                                     '',
                                     TO_DATE('2022-6-6 14:31:05',
                                             'yyyy-MM-dd HH24:mi:ss'),
                                     '');

DECLARE
  content_3 CLOB := '${jsonFile}';
BEGIN
  UPDATE form_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1533697966238593025';
  COMMIT;
END;
/

INSERT
  INTO form_template(ID_,
                     NAME_,
                     ALIAS_,
                     TEMPLATE_,
                     TYPE_,
                     CATEGORY_,
                     FILE_NAME_,
                     PATH_,
                     SINGLE_,
                     GEN_MODE_,
                     MAIN_SUB_TYPE_,
                     DELETED_,
                     COMPANY_ID_,
                     TENANT_ID_,
                     CREATE_DEP_ID_,
                     CREATE_BY_,
                     CREATE_TIME_,
                     UPDATE_BY_,
                     UPDATE_TIME_,
                     APP_ID_) VALUES('1533698110191300609',
                                     '代码生成_正向Entity_实体层',
                                     'codeGenEntityW',
                                     '',
                                     'codeGen',
                                     'main',
                                     '{class}.java',
                                     'src/main/java/com/{rootSystem}/{system}/{package}/entity',
                                     'NO',
                                     'genCode',
                                     'mainSub',
                                     0,
                                     '0',
                                     '1',
                                     '1',
                                     '1',
                                     TO_DATE('2022-6-6 14:31:39',
                                             'yyyy-MM-dd HH24:mi:ss'),
                                     '1',
                                     TO_DATE('2022-6-29 18:11:14',
                                             'yyyy-MM-dd HH24:mi:ss'),
                                     '');

DECLARE
  content_3 CLOB := '<#assign package=model.vars.package><#assign class=model.vars.class><#assign classVar=model.vars.classVar><#assign comment=model.name><#assign system=vars.system><#assign domain=vars.domain><#assign tableName=model.tableName><#assign colList=model.boAttrList><#assign idField=convertUnderLine(model.idField)>/*** [${model.name}]实体类定义* @author: ${vars.developer}* @company: ${vars.company}* @date: ${vars.date}*/package ${domain}.${system}.${package}.entity;import com.baomidou.mybatisplus.annotation.IdType;import com.baomidou.mybatisplus.annotation.TableField;import com.baomidou.mybatisplus.annotation.TableId;import com.baomidou.mybatisplus.annotation.TableName;import com.fasterxml.jackson.annotation.JsonCreator;import com.redxun.common.base.entity.BaseExtEntity;import com.redxun.gencode.codegenhander.SubTableDef;import lombok.Getter;import lombok.Setter;import lombok.experimental.Accessors;import java.util.ArrayList;import java.util.List;import org.apache.ibatis.type.JdbcType;import java.util.Date;@Setter@Getter@Accessors(chain = true)@TableName(value = "${tableName}")public class ${class}  extends BaseExtEntity<java.lang.String> {    @JsonCreator    public ${class}() {    }   //主键    @TableId(value = "${model.idField}",type = IdType.INPUT)  private String ${idField};  @Override    public String getPkId() {        return this.${idField};    }    @Override    public void setPkId(String pkId) {        this.${idField}=pkId;    } <#if model.genMode=="form" || model.genMode=="create">  //外键    @TableField(value = "REF_ID_",jdbcType=JdbcType.VARCHAR)    private String refId;    //父键    @TableField(value = "PARENT_ID_",jdbcType=JdbcType.VARCHAR)    private String parentId;  </#if>    <#list colList as col>  <#if col.control=="rx-address"> ${addressFun(col)}  <#else> //${col.comment}    @TableField(value = "${col.fieldName}",jdbcType=JdbcType.${getJdbcType(col)})    private ${getJavaType(col)} ${col.name};   <#if col.isSingle==0> ${colNameFun(col)}  </#if>  </#if>    </#list>    <#if model.boEntityList?size gt 0 >    <#list model.boEntityList as subEnt>    @TableField(exist=false) @SubTableDef(tableName = "${subEnt.vars.class}",className = ${subEnt.vars.class}.class,type = "onetomany")  private List<${subEnt.vars.class}> ${subEnt.vars.classVar}List=new ArrayList<>(); </#list>    </#if> }<#function colNameFun col><#assign extJson=fileUtil.parseStrToJson(col.extJson)><#assign rtn> <#if model.genMode=="form" || model.genMode=="create">    //${col.comment}名称    @TableField(value = "${col.fieldName}_NAME",jdbcType=JdbcType.${getJdbcType(col)})    private ${getJavaType(col)} ${col.name}Name;  <#else>   <#assign colJson=fileUtil.getColByColList(model,extJson.ref,"fieldName")>   //${colJson.comment}名称    @TableField(value = "${colJson.fieldName}",jdbcType=JdbcType.${getJdbcType(colJson)})   private ${getJavaType(colJson)} ${colJson.name};  </#if></#assign><#return rtn?trim></#function><#function addressFun col><#assign extJson=fileUtil.parseStrToJson(col.extJson)><#assign setting=extJson.setting><#assign rtn>  <#if model.genMode=="form" || model.genMode=="create">    //${col.comment}--省   @TableField(value = "${col.fieldName}_PROVINCE",jdbcType=JdbcType.${getJdbcType(col)})    private ${getJavaType(col)} ${col.name}Province;        //${col.comment}--省code   @TableField(value = "${col.fieldName}_P_CODE",jdbcType=JdbcType.${getJdbcType(col)})    private ${getJavaType(col)} ${col.name}Pcode;       <#if setting.isCity=="true">    //${col.comment}--市   @TableField(value = "${col.fieldName}_CITY",jdbcType=JdbcType.${getJdbcType(col)})    private ${getJavaType(col)} ${col.name}City;        //${col.comment}--市Code   @TableField(value = "${col.fieldName}_CITY_CODE",jdbcType=JdbcType.${getJdbcType(col)})   private ${getJavaType(col)} ${col.name}CityCode;    </#if>    <#if setting.isCounty=="true">        //${col.comment}--县(区)    @TableField(value = "${col.fieldName}_COUNTY",jdbcType=JdbcType.${getJdbcType(col)})    private ${getJavaType(col)} ${col.name}County;        //${col.comment}--县(区)Code    @TableField(value = "${col.fieldName}_COUNTY_CODE",jdbcType=JdbcType.${getJdbcType(col)})   private ${getJavaType(col)} ${col.name}CountyCode;    </#if>    <#if setting.isAddress=="true">       //${col.comment}--详细地址    @TableField(value = "${col.fieldName}_ADDRESS",jdbcType=JdbcType.${getJdbcType(col)})   private ${getJavaType(col)} ${col.name}Address;   </#if>  <#else>   <#assign provinceJson=fileUtil.getColByColList(model,extJson.province,"name")>    //${provinceJson.comment}--省    @TableField(value = "${provinceJson.fieldName}",jdbcType=JdbcType.${getJdbcType(provinceJson)})   private ${getJavaType(provinceJson)} ${provinceJson.name};        //${col.comment}--省code   @TableField(value = "${col.fieldName}",jdbcType=JdbcType.${getJdbcType(col)})   private ${getJavaType(col)} ${col.name};        <#if setting.isCity=="true">          <#assign cityJson=fileUtil.getColByColList(model,extJson.city,"name")>      //${cityJson.comment}--市      @TableField(value = "${cityJson.fieldName}",jdbcType=JdbcType.${getJdbcType(cityJson)})     private ${getJavaType(cityJson)} ${cityJson.name};            <#assign cityCodeJson=fileUtil.getColByColList(model,extJson.city_code,"name")>     //${cityCodeJson.comment}--市Code      @TableField(value = "${cityCodeJson.fieldName}",jdbcType=JdbcType.${getJdbcType(cityCodeJson)})     private ${getJavaType(cityCodeJson)} ${cityCodeJson.name};    </#if>    <#if setting.isCounty=="true">          <#assign countyJson=fileUtil.getColByColList(model,extJson.county,"name")>      //${countyJson.comment}--县(区)     @TableField(value = "${countyJson.fieldName}",jdbcType=JdbcType.${getJdbcType(countyJson)})     private ${getJavaType(countyJson)} ${countyJson.name};            <#assign countyCodeJson=fileUtil.getColByColList(model,extJson.county_code,"name")>     //${countyCodeJson.comment}--县(区)Code     @TableField(value = "${countyCodeJson.fieldName}",jdbcType=JdbcType.${getJdbcType(countyCodeJson)})     private ${getJavaType(countyCodeJson)} ${countyCodeJson.name};    </#if>    <#if setting.isAddress=="true">   <#assign addressJson=fileUtil.getColByColList(model,extJson.address,"name")>      //${addressJson.comment}--详细地址      @TableField(value = "${addressJson.fieldName}",jdbcType=JdbcType.${getJdbcType(addressJson)})     private ${getJavaType(addressJson)} ${addressJson.name};    </#if>  </#if></#assign><#return rtn?trim></#function><#function getJdbcType col><#assign dbtype=col.dataType?lower_case><#assign rtn>  <#if  dbtype=="number"  >   NUMERIC <#elseif (dbtype=="date")>    DATE  <#else>   VARCHAR </#if></#assign><#return rtn?trim></#function><#function getJavaType col><#assign dbtype=col.dataType?lower_case><#assign rtn><#if  dbtype=="number"  >    <#if col.decimalLength==0>         <#if col.length gt 10 >            long         <#else>            int         </#if>    <#else>         double    </#if><#elseif (dbtype=="date")> Date<#else> String</#if></#assign> <#return rtn?trim></#function><#function convertUnderLine field> <#if field?index_of("_")==-1>   <#assign rtn>${field?lower_case?trim}</#assign>   <#return rtn> </#if>  <#if field?index_of("F_")==0>   <#assign rtn><#list (field?substring(2))?split("_") as x><#if (x_index==0)><#if x?length==1>${x?upper_case?trim}<#else>${x?lower_case?trim}</#if><#else>${x?lower_case?cap_first?trim}</#if></#list></#assign>    <#return rtn> </#if>  <#assign rtn><#list field?split("_") as x><#if (x_index==0)><#if x?length==1>${x?upper_case?trim}<#else>${x?lower_case?trim}</#if><#else>${x?lower_case?cap_first?trim}</#if></#list></#assign> <#return rtn></#function>';
BEGIN
  UPDATE form_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1533698110191300609';
  COMMIT;
END;
/

INSERT
  INTO form_template(ID_,
                     NAME_,
                     ALIAS_,
                     TEMPLATE_,
                     TYPE_,
                     CATEGORY_,
                     FILE_NAME_,
                     PATH_,
                     SINGLE_,
                     GEN_MODE_,
                     MAIN_SUB_TYPE_,
                     DELETED_,
                     COMPANY_ID_,
                     TENANT_ID_,
                     CREATE_DEP_ID_,
                     CREATE_BY_,
                     CREATE_TIME_,
                     UPDATE_BY_,
                     UPDATE_TIME_,
                     APP_ID_) VALUES('1533698265154056194',
                                     '代码生成_正向Mapper数据库访问类',
                                     'codeGenMapperW',
                                     '',
                                     'codeGen',
                                     'main',
                                     '{class}Mapper.java',
                                     'src/main/java/com/{rootSystem}/{system}/{package}/mapper',
                                     'NO',
                                     'genCode',
                                     'mainSub',
                                     0,
                                     '0',
                                     '1',
                                     '1',
                                     '1',
                                     TO_DATE('2022-6-6 14:32:16',
                                             'yyyy-MM-dd HH24:mi:ss'),
                                     '1',
                                     TO_DATE('2022-6-29 18:08:49',
                                             'yyyy-MM-dd HH24:mi:ss'),
                                     '');

DECLARE
  content_3 CLOB := '<#assign package=model.vars.package><#assign class=model.vars.class><#assign classVar=model.vars.classVar><#assign mainSubClassVar=model.vars.mainSubClassVar><#assign system=vars.system><#assign domain=vars.domain><#assign type="${domain}."+system+"."+ package +".entity." +class><#assign tableName=model.tableName>    package ${domain}.${system}.${package}.mapper;import ${domain}.${system}.${package}.entity.${class};import org.apache.ibatis.annotations.Mapper;import com.redxun.common.base.db.BaseDao;<#if (model.vars.isSub?? ' || '&&' ||
                    ' model.vars.isSub==''YES'') || (model.vars.isSubMain?? ' || '&&' ||
                    ' model.vars.isSubMain==''YES'')>import org.apache.ibatis.annotations.Param;import java.util.List;</#if>/*** [${model.name}]据库访问层* @author: ${vars.developer}* @company: ${vars.company}* @date: ${vars.date}*/@Mapperpublic interface ${class}Mapper extends BaseDao<${class}> {<#if (model.vars.isSub?? ' || '&&' ||
                    ' model.vars.isSub==''YES'') || (model.vars.isSubMain?? ' || '&&' ||
                    ' model.vars.isSubMain==''YES'')>  <#if model.vars.isSubMain?? ' || '&&' ||
                    ' model.vars.isSubMain==''NO''>  <#assign mainSubRelation=fileUtil.getRelation(boRelations,mainSubClassVar)> <#assign subRelation=fileUtil.getRelation(mainSubRelation,classVar)>  List<${class}> getByRefId(@Param("${subRelation.fkFieldName}") String ${subRelation.fkFieldName});    void deleteByRefId(@Param("${subRelation.fkFieldName}") String ${subRelation.fkFieldName}); <#else> <#assign boRelation=fileUtil.getRelation(boRelations,classVar)> List<${class}> getByRefId(@Param("${boRelation.fkFieldName}") String ${boRelation.fkFieldName});    void deleteByRefId(@Param("${boRelation.fkFieldName}") String ${boRelation.fkFieldName}); </#if>    </#if>}';
BEGIN
  UPDATE form_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1533698265154056194';
  COMMIT;
END;
/

INSERT
  INTO form_template(ID_,
                     NAME_,
                     ALIAS_,
                     TEMPLATE_,
                     TYPE_,
                     CATEGORY_,
                     FILE_NAME_,
                     PATH_,
                     SINGLE_,
                     GEN_MODE_,
                     MAIN_SUB_TYPE_,
                     DELETED_,
                     COMPANY_ID_,
                     TENANT_ID_,
                     CREATE_DEP_ID_,
                     CREATE_BY_,
                     CREATE_TIME_,
                     UPDATE_BY_,
                     UPDATE_TIME_,
                     APP_ID_) VALUES('1533698365993512961',
                                     '代码生成_正向ServiceImpl服务层',
                                     'codeGenServiceImpl',
                                     '',
                                     'codeGen',
                                     'main',
                                     '{class}ServiceImpl.java',
                                     'src/main/java/com/{rootSystem}/{system}/{package}/service',
                                     'NO',
                                     'genCode',
                                     'mainSub',
                                     0,
                                     '0',
                                     '1',
                                     '1',
                                     '1',
                                     TO_DATE('2022-6-6 14:32:40',
                                             'yyyy-MM-dd HH24:mi:ss'),
                                     '1',
                                     TO_DATE('2022-6-29 18:08:41',
                                             'yyyy-MM-dd HH24:mi:ss'),
                                     '');

DECLARE
  content_3 CLOB := '<#assign package=model.vars.package><#assign class=model.vars.class><#assign classVar=model.vars.classVar><#assign mainSubClassVar=model.vars.mainSubClassVar><#assign system=vars.system><#assign domain=vars.domain><#assign domain=vars.domain>package ${domain}.${system}.${package}.service;import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;import com.redxun.common.base.db.BaseDao;import com.redxun.common.base.db.BaseService;import com.redxun.common.service.impl.SuperServiceImpl;import com.redxun.common.tool.BeanUtil;import com.redxun.common.tool.StringUtils;import com.redxun.common.utils.ExceptionUtil;import com.redxun.common.tool.IdGenerator;import ${domain}.${system}.${package}.entity.${class};import ${domain}.${system}.${package}.mapper.${class}Mapper;<#if model.boEntityList?size gt 0 ><#list model.boEntityList as subEnt>import ${domain}.${system}.${package}.service.${subEnt.vars.class}ServiceImpl;import ${domain}.${system}.${package}.mapper.${subEnt.vars.class}Mapper;import ${domain}.${system}.${package}.entity.${subEnt.vars.class};</#list></#if>import lombok.extern.slf4j.Slf4j;import org.springframework.stereotype.Service;import javax.annotation.Resource;import java.io.Serializable;import java.util.Collection;import java.util.List;/*** [${model.name}]业务服务类* @author: ${vars.developer}* @company: ${vars.company}* @date: ${vars.date}*/@Slf4j@Servicepublic class ${class}ServiceImpl extends SuperServiceImpl<${class}Mapper, ${class}> implements BaseService<${class}> {    @Resource    private ${class}Mapper ${classVar}Mapper;        <#if model.boEntityList?size gt 0 >    <#list model.boEntityList as subEnt>    @Resource    private ${subEnt.vars.class}ServiceImpl ${subEnt.vars.classVar}Service;  @Resource    private ${subEnt.vars.class}Mapper ${subEnt.vars.classVar}Mapper;    </#list>    </#if>       @Override    public BaseDao<${class}> getRepository() {        return ${classVar}Mapper;    }    <#if (model.vars.isSub?? ' || '&&' ||
                    ' model.vars.isSub==''YES'') || (model.vars.isSubMain?? ' || '&&' ||
                    ' model.vars.isSubMain==''YES'')>  public void saveOrUpdateAll(List<${class}> subList, String pkId){   ${classVar}Mapper.deleteByRefId(pkId);        if(BeanUtil.isEmpty(subList)){            return;        }        for (${class} sub:subList) {    <#if (model.vars.isSub?? ' || '&&' ||
                    ' model.vars.isSub==''YES'') || (model.vars.isSubMain?? ' || '&&' ||
                    ' model.vars.isSubMain==''YES'')>      <#if model.vars.isSubMain?? ' || '&&' ||
                    ' model.vars.isSubMain==''NO''>      <#assign mainSubRelation=fileUtil.getRelation(boRelations,mainSubClassVar)>     <#assign subRelation=fileUtil.getRelation(mainSubRelation,classVar)>      sub.set${subRelation.fkFieldFun}(pkId);     <#else>     <#assign boRelation=fileUtil.getRelation(boRelations,classVar)>     sub.set${boRelation.fkFieldFun}(pkId);      </#if>    </#if>      if(StringUtils.isEmpty(sub.getPkId())){                sub.setPkId(IdGenerator.getIdStr());            }        }        try {            this.saveOrUpdateBatch(subList);        }catch (Exception e){            log.error("saveOrUpdateAll is error: messgae={}", ExceptionUtil.getExceptionMessage(e));        }    } </#if>      <#if model.boEntityList?size gt 0 > @Override    public void delete(Collection<Serializable> entities) {        for(Serializable id:entities){            //根据ID删除${model.name}            ${classVar}Mapper.deleteById(id);            <#list model.boEntityList as subEnt>            //根据ID删除${subEnt.name}            QueryWrapper ${subEnt.vars.classVar}Wrapper=new QueryWrapper();            ${subEnt.vars.classVar}Wrapper.eq("${subEnt.boRelation.fkField}",id);            ${subEnt.vars.classVar}Mapper.delete(${subEnt.vars.classVar}Wrapper);       </#list>        }    }    /**     * 根据主键获取表单数据     * @param pk     * @return     */    @Override    public ${class} get(Serializable pk){        ${class} ${classVar}=this.getById(pk);        if(BeanUtil.isEmpty(${classVar})){            return new ${class}();        }    <#list model.boEntityList as subEnt>    List<${subEnt.vars.class}> ${subEnt.vars.classVar}List =${subEnt.vars.classVar}Mapper.getByRefId((String)pk);        ${classVar}.set${subEnt.vars.class}List(${subEnt.vars.classVar}List);    </#list>        return ${classVar};    }    @Override    public int insert(${class} entity) {        String pkId = entity.getPkId();        if(BeanUtil.isEmpty(pkId)) {            pkId=IdGenerator.getIdStr();            entity.setPkId(pkId);        }        int insert = getRepository().insert(entity);    <#list model.boEntityList as subEnt>    ${subEnt.vars.classVar}Service.saveOrUpdateAll(entity.get${subEnt.vars.class}List(),pkId);    </#list>        return insert;    }    @Override    public int update(${class} entity) {        int insert =  getRepository().updateById(entity);   <#list model.boEntityList as subEnt>    ${subEnt.vars.classVar}Service.saveOrUpdateAll(entity.get${subEnt.vars.class}List(),entity.getPkId());    </#list>        return insert;    }    </#if> }';
BEGIN
  UPDATE form_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1533698365993512961';
  COMMIT;
END;
/

INSERT
  INTO form_template(ID_,
                     NAME_,
                     ALIAS_,
                     TEMPLATE_,
                     TYPE_,
                     CATEGORY_,
                     FILE_NAME_,
                     PATH_,
                     SINGLE_,
                     GEN_MODE_,
                     MAIN_SUB_TYPE_,
                     DELETED_,
                     COMPANY_ID_,
                     TENANT_ID_,
                     CREATE_DEP_ID_,
                     CREATE_BY_,
                     CREATE_TIME_,
                     UPDATE_BY_,
                     UPDATE_TIME_,
                     APP_ID_) VALUES('1533698526207537154',
                                     '代码生成_正向Controller控制层',
                                     'codeGenControllerW',
                                     '',
                                     'codeGen',
                                     'main',
                                     '{class}Controller.java',
                                     'src/main/java/com/{rootSystem}/{system}/{package}/controller',
                                     'NO',
                                     'genCode',
                                     'mainSub',
                                     0,
                                     '0',
                                     '1',
                                     '1',
                                     '1',
                                     TO_DATE('2022-6-6 14:33:18',
                                             'yyyy-MM-dd HH24:mi:ss'),
                                     '1',
                                     TO_DATE('2022-7-7 18:26:27',
                                             'yyyy-MM-dd HH24:mi:ss'),
                                     '');

DECLARE
  content_3 CLOB := '<#assign package=model.vars.package><#assign class=model.vars.class><#assign classVar=model.vars.classVar><#assign entAlias=model.vars.entAlias><#assign comment=model.name><#assign system=vars.system><#assign domain=vars.domain><#assign tableName=model.tableName><#assign colList=model.boAttrList>package ${domain}.${system}.${package}.controller;import com.redxun.common.base.db.BaseService;import com.redxun.gencode.controller.CodeGenBaseController;import ${domain}.${system}.${package}.entity.${class};import ${domain}.${system}.${package}.service.${class}ServiceImpl;import io.swagger.annotations.Api;import lombok.extern.slf4j.Slf4j;import org.springframework.beans.factory.annotation.Autowired;import org.springframework.web.bind.annotation.*;import com.redxun.common.annotation.ClassDefine;import com.redxun.gencode.codegenhander.ClassCodeDef;/*** [${model.name}] 控制器代码* @author: ${vars.developer}* @company: ${vars.company}* @date: ${vars.date}*/@Slf4j@RestController@RequestMapping("/${system}/${package}/${classVar}")@Api(tags = "${comment}")@ClassDefine(title = "${comment}",alias = "${class}Controller",path = "/${system}/${package}/${classVar}",packages = "${package}",packageName = "子系统名称")@ClassCodeDef(alias = "${classVar}",classPackage="${package}",createType="${model.genMode}")public class ${class}Controller extends CodeGenBaseController<${class}> { @Autowired  ${class}ServiceImpl ${classVar}Service; @Override public BaseService getBaseService() {   return ${classVar}Service;  } @Override public String getComment() {    return "${comment}";  } @Override public ${class} getBaseEntity(){    return new ${class}();  }}';
BEGIN
  UPDATE form_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1533698526207537154';
  COMMIT;
END;
/

INSERT
  INTO form_template(ID_,
                     NAME_,
                     ALIAS_,
                     TEMPLATE_,
                     TYPE_,
                     CATEGORY_,
                     FILE_NAME_,
                     PATH_,
                     SINGLE_,
                     GEN_MODE_,
                     MAIN_SUB_TYPE_,
                     DELETED_,
                     COMPANY_ID_,
                     TENANT_ID_,
                     CREATE_DEP_ID_,
                     CREATE_BY_,
                     CREATE_TIME_,
                     UPDATE_BY_,
                     UPDATE_TIME_,
                     APP_ID_) VALUES('1533698647930433538',
                                     '代码生成_正向Mapping文件',
                                     'codeGenMappingW',
                                     '',
                                     'codeGen',
                                     'main',
                                     '{class}Mapper.xml',
                                     'src/main/resources/mapper/{rootSystem}/{system}/{package}',
                                     'NO',
                                     'genCode',
                                     'mainSub',
                                     0,
                                     '0',
                                     '1',
                                     '1',
                                     '1',
                                     TO_DATE('2022-6-6 14:33:47',
                                             'yyyy-MM-dd HH24:mi:ss'),
                                     '1',
                                     TO_DATE('2022-6-30 10:27:04',
                                             'yyyy-MM-dd HH24:mi:ss'),
                                     '');

DECLARE
  content_3 CLOB := '<#assign package=model.vars.package><#assign class=model.vars.class><#assign classVar=model.vars.classVar><#assign mainSubClassVar=model.vars.mainSubClassVar><#assign system=vars.system><#assign domain=vars.domain><#assign type="${domain}."+system+"."+ package +".entity." +class><#assign tableName=model.tableName><#assign colList=model.boAttrList><?xml version="1.0" encoding="UTF-8"?><!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd"><mapper namespace="${domain}.${system}.${package}.mapper.${class}Mapper">    <resultMap id="${class}" type="${type}">            <#assign idField=convertUnderLine(model.idField)>            <id property="${idField}" column="${model.idField}" jdbcType="VARCHAR"/>            <#list colList as col>        ${columnFun(col model.genMode)}           </#list>          <#if model.genMode=="form" || model.genMode=="create" >       <result property="refId" column="REF_ID_" jdbcType="VARCHAR"/>        <result property="parentId" column="PARENT_ID_" jdbcType="VARCHAR"/>          <result property="tenantId" column="TENANT_ID_" jdbcType="VARCHAR"/>          <result property="createBy" column="CREATE_BY_" jdbcType="VARCHAR"/>          <result property="createTime" column="CREATE_TIME_" jdbcType="DATE"/>         <result property="updateBy" column="UPDATE_BY_" jdbcType="VARCHAR"/>          <result property="updateTime" column="UPDATE_TIME_" jdbcType="DATE"/>         <result property="createDepId" column="CREATE_DEP_ID_" jdbcType="VARCHAR"/>         </#if>    </resultMap>    <select id="query" resultType="${type}" parameterType="java.util.Map">        select ${model.idField},<#list colList as col>${selectColumnFun(col model.genMode)}<#if col_has_next>,</#if></#list><#if model.genMode=="form" || model.genMode=="create" >,REF_ID_,PARENT_ID_,TENANT_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_,CREATE_DEP_ID_</#if> from ${tableName}        <#noparse>        <where>            <if test="@rx.Ognl@isNotEmpty(w.whereSql)">                ${w.whereSql}            </if>        </where>        <if test="@rx.Ognl@isNotEmpty(w.orderBySql)">            ORDER BY ${w.orderBySql}        </if>        <if test="@rx.Ognl@isEmpty(w.orderBySql)"></#noparse>            ORDER BY  ${model.idField} DESC        </if>    </select>  <#if (model.vars.isSub?? ' || '&&' ||
                    ' model.vars.isSub==''YES'') || (model.vars.isSubMain?? ' || '&&' ||
                    ' model.vars.isSubMain==''YES'')>  <#if model.vars.isSubMain?? ' || '&&' ||
                    ' model.vars.isSubMain==''NO''>  <#assign mainSubRelation=fileUtil.getRelation(boRelations,mainSubClassVar)> <#assign subRelation=fileUtil.getRelation(mainSubRelation,classVar)>  <select id="getByRefId" resultType="${type}" parameterType="java.util.Map">   select * from ${tableName} where ${subRelation.fkField}=${r"#"}{${subRelation.fkFieldName}} </select>   <delete id="deleteByRefId" parameterType="java.lang.String" > DELETE FROM ${tableName} WHERE ${subRelation.fkField}=${r"#"}{${subRelation.fkFieldName}} </delete> <#else> <#assign boRelation=fileUtil.getRelation(boRelations,classVar)> <select id="getByRefId" resultType="${type}" parameterType="java.util.Map">   select * from ${tableName} where ${boRelation.fkField}=${r"#"}{${boRelation.fkFieldName}} </select>   <delete id="deleteByRefId" parameterType="java.lang.String" > DELETE FROM ${tableName} WHERE ${boRelation.fkField}=${r"#"}{${boRelation.fkFieldName}} </delete> </#if></#if>  </mapper><#function selectColumnFun col genMode><#assign name=col.fieldName><#assign rtn> <#if genMode=="form" || model.genMode=="create" >   <#if col.control=="rx-address">     <#assign extJson=fileUtil.parseAddressFromExtJson(col.extJson)>     ${name}_PROVINCE,${name}_P_CODE     <#if extJson.isCity=="true">      ,${name}_CITY,${name}_CITY_CODE     </#if>      <#if extJson.isCounty=="true">      ,${name}_COUNTY,${name}_COUNTY_CODE     </#if>      <#if extJson.isAddress=="true">     ,${name}_ADDRESS      </#if>    <#elseif col.isSingle==0>     ${name}_NAME,${name}    <#else>     ${name}   </#if>  <#else>   <#assign extJson=fileUtil.parseStrToJson(col.extJson)>    <#if col.control=="rx-address">     <#assign setting=extJson.setting>           <#assign provinceJson=fileUtil.getColByColList(model,extJson.province,"name")>      ${provinceJson.fieldName},${name}     <#if setting.isCity=="true">        <#assign cityJson=fileUtil.getColByColList(model,extJson.city,"name")>        ,${cityJson.fieldName}        <#assign cityCodeJson=fileUtil.getColByColList(model,extJson.city_code,"name")>       ,${cityCodeJson.fieldName}      </#if>      <#if setting.isCounty=="true">        <#assign countyJson=fileUtil.getColByColList(model,extJson.county,"name")>        ,${countyJson.fieldName}        <#assign countyCodeJson=fileUtil.getColByColList(model,extJson.county_code,"name")>       ,${countyCodeJson.fieldName}      </#if>      <#if setting.isAddress=="true">       <#assign addressJson=fileUtil.getColByColList(model,extJson.address,"name")>        ,${addressJson.fieldName}     </#if>    <#elseif col.isSingle==0>     <#assign colJson=fileUtil.getColByColList(model,extJson.ref,"fieldName")>     ${name},${colJson.fieldName}    <#else>     ${name}   </#if>  </#if></#assign><#return rtn?trim></#function><#function columnFun col genMode><#assign colName=convertUnderLine(col.fieldName)><#assign rtn> <#if genMode=="form" || model.genMode=="create" >   <#if col.control=="rx-address">     <#assign extJson=fileUtil.parseStrToJson(col.extJson)>      <#assign setting=extJson.setting>     <result property="${colName}Province" column="${col.fieldName}_PROVINCE" jdbcType="${getJdbcType(col)}"/>     <result property="${colName}Pcode" column="${col.fieldName}_P_CODE" jdbcType="${getJdbcType(col)}"/>      <#if setting.isCity=="true">        <result property="${colName}City" column="${col.fieldName}_CITY" jdbcType="${getJdbcType(col)}"/>       <result property="${colName}CityCode" column="${col.fieldName}_CITY_CODE" jdbcType="${getJdbcType(col)}"/>      </#if>      <#if setting.isCounty=="true">        <result property="${colName}County" column="${col.fieldName}_COUNTY" jdbcType="${getJdbcType(col)}"/>       <result property="${colName}CountyCode" column="${col.fieldName}_COUNTY_CODE" jdbcType="${getJdbcType(col)}"/>      </#if>      <#if setting.isAddress=="true">       <result property="${colName}Address" column="${col.fieldName}_ADDRESS" jdbcType="${getJdbcType(col)}"/>     </#if>    <#elseif col.isSingle==0>     <result property="${colName}" column="${col.fieldName}" jdbcType="${getJdbcType(col)}"/>      <result property="${colName}Name" column="${col.fieldName}_NAME" jdbcType="${getJdbcType(col)}"/>   <#else>     <result property="${colName}" column="${col.fieldName}" jdbcType="${getJdbcType(col)}"/>    </#if>  <#else>   <#assign extJson=fileUtil.parseStrToJson(col.extJson)>    <#if col.control=="rx-address">     <#assign setting=extJson.setting>     <#assign provinceJson=fileUtil.getColByColList(model,extJson.province,"name")>      <result property="${convertUnderLine(provinceJson.fieldName)}" column="${provinceJson.fieldName}" jdbcType="${getJdbcType(provinceJson)}"/>     <result property="${colName}" column="${col.fieldName}" jdbcType="${getJdbcType(col)}"/>      <#if setting.isCity=="true">        <#assign cityJson=fileUtil.getColByColList(model,extJson.city,"name")>        <result property="${convertUnderLine(cityJson.fieldName)}" column="${cityJson.fieldName}" jdbcType="${getJdbcType(cityJson)}"/>       <#assign cityCodeJson=fileUtil.getColByColList(model,extJson.city_code,"name")>       <result property="${convertUnderLine(cityCodeJson.fieldName)}" column="${cityCodeJson.fieldName}" jdbcType="${getJdbcType(cityCodeJson)}"/>     </#if>      <#if setting.isCounty=="true">        <#assign countyJson=fileUtil.getColByColList(model,extJson.county,"name")>        <result property="${convertUnderLine(countyJson.fieldName)}" column="${countyJson.fieldName}" jdbcType="${getJdbcType(countyJson)}"/>       <#assign countyCodeJson=fileUtil.getColByColList(model,extJson.county_code,"name")>       <result property="${convertUnderLine(countyCodeJson.fieldName)}" column="${countyCodeJson.fieldName}" jdbcType="${getJdbcType(countyCodeJson)}"/>     </#if>      <#if setting.isAddress=="true">       <#assign addressJson=fileUtil.getColByColList(model,extJson.address,"name")>        <result property="${convertUnderLine(addressJson.fieldName)}" column="${addressJson.fieldName}" jdbcType="${getJdbcType(addressJson)}"/>      </#if>    <#elseif col.isSingle==0>     <result property="${colName}" column="${col.fieldName}" jdbcType="${getJdbcType(col)}"/>      <#assign colJson=fileUtil.getColByColList(model,extJson.ref,"fieldName")>     <result property="${convertUnderLine(colJson.fieldName)}" column="${colJson.fieldName}" jdbcType="${getJdbcType(colJson)}"/>    <#else>     <result property="${colName}" column="${col.fieldName}" jdbcType="${getJdbcType(col)}"/>    </#if>  </#if></#assign><#return rtn?trim></#function><#function getJdbcType col><#assign dbtype=col.dataType?lower_case><#assign rtn>  <#if  dbtype=="number"  >   NUMERIC <#elseif (dbtype=="date")>    DATE  <#else>   VARCHAR </#if></#assign><#return rtn?trim></#function><#function convertUnderLine field>  <#if field?index_of("_")==-1>   <#assign rtn>${field?lower_case?trim}</#assign>   <#return rtn> </#if>  <#if field?index_of("F_")==0>   <#assign rtn><#list (field?substring(2))?split("_") as x><#if (x_index==0)><#if x?length==1>${x?upper_case?trim}<#else>${x?lower_case?trim}</#if><#else>${x?lower_case?cap_first?trim}</#if></#list></#assign>    <#return rtn> </#if>  <#assign rtn><#list field?split("_") as x><#if (x_index==0)><#if x?length==1>${x?upper_case?trim}<#else>${x?lower_case?trim}</#if><#else>${x?lower_case?cap_first?trim}</#if></#list></#assign> <#return rtn></#function>';
BEGIN
  UPDATE form_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1533698647930433538';
  COMMIT;
END;
/

INSERT
  INTO form_template(ID_,
                     NAME_,
                     ALIAS_,
                     TEMPLATE_,
                     TYPE_,
                     CATEGORY_,
                     FILE_NAME_,
                     PATH_,
                     SINGLE_,
                     GEN_MODE_,
                     MAIN_SUB_TYPE_,
                     DELETED_,
                     COMPANY_ID_,
                     TENANT_ID_,
                     CREATE_DEP_ID_,
                     CREATE_BY_,
                     CREATE_TIME_,
                     UPDATE_BY_,
                     UPDATE_TIME_,
                     APP_ID_) VALUES('1533698815715176450',
                                     '代码生成_正向列表页面',
                                     'codeGenListVueW',
                                     '',
                                     'codeGen',
                                     'main',
                                     '{class}List.vue',
                                     'src/views/modules/{system}/{package}',
                                     'YES',
                                     'genCode',
                                     'mainSub',
                                     0,
                                     '0',
                                     '1',
                                     '1',
                                     '1',
                                     TO_DATE('2022-6-6 14:34:27',
                                             'yyyy-MM-dd HH24:mi:ss'),
                                     '',
                                     TO_DATE('2022-6-6 14:34:27',
                                             'yyyy-MM-dd HH24:mi:ss'),
                                     '');

DECLARE
  content_3 CLOB := '<#assign package=model.vars.package><#assign class=model.vars.class><#assign classVar=model.vars.classVar><#assign comment=model.name><#assign pk=model.idField><#assign system=vars.system><#assign domain=vars.domain><#assign commonList=model.boAttrList><#assign searchFields=settingJson.mainEntity.searchFields><template>  <rx-layout>  ${leftSearchTreeFun()}    <div slot="center" style>      <rx-fit>        <div slot="toolheader" foldheader="true" foldbtn="false" border="false">          <div class="table-operator">            <rx-button alias="${classVar}Add" :butn-icon="''plus''" :butn-type="''primary''" :show-no-right="true" @click="add">新建</rx-button>            <a-dropdown v-if="editEnable ' || '&&' ||
                    ' selectedRowKeys.length > 0 || removeEnable ' || '&&' ||
                    ' selectedRowKeys.length > 0">              <a-button style="margin-left: 8px"> 更多 <a-icon type="down" /> </a-button>              <a-menu slot="overlay">                <a-menu-item key="1" v-if="editEnable ' || '&&' ||
                    ' selectedRowKeys.length > 0" alias="${classVar}Edit" :butn-icon="''edit''"                             :butn-type="''primary''" :show-no-right="false" @click="editOne"><a-icon type="edit"  />编辑</a-menu-item>                <a-menu-item key="2" v-if="removeEnable ' || '&&' ||
                    ' selectedRowKeys.length > 0" alias="${classVar}Delete" :butn-icon="''delete''" :butn-type="''danger''"                             :show-no-right="false" @click="delByIds(selectedRowKeys)"><a-icon type="delete" />删除</a-menu-item>              </a-menu>            </a-dropdown>          </div>          <span class="search-btn-box">                <span class="search-btn" @click="searchshow"><i class="iconfont iconguolv"></i>过滤</span>           </span>        </div>        <div slot="searchbar" btnalign="right"  v-if="fitSearch" @search="search" @cancel="cancel" @close="close">          <a-form layout="vertical">            <#list searchFields as search>            <a-form-item label="${search.fieldLabel}">        <#if (!search.fc?? || search.fc==''textbox'')>          <a-input <#if search.emptytext??>placeholder="${search.emptytext}"</#if>               v-model.trim="queryParam[''<#if search.autoFilter==''YES''>Q_<#if search.tablePre?? ' || '&&' ||
                    ' search.tablePre!="">${search.tablePre}.</#if>${search.fieldName}_S_${search.fieldOp}<#else>${search.fieldName}</#if>'']"/>        <#elseif (search.fc==''datepicker'')>         <#if search.format??>           <#assign searchDateFormat=search.format>          <#else>           <#assign searchDateFormat=''YYYY-MM-DD''>         </#if>          <a-date-picker <#if search.emptytext??>placeholder="${search.emptytext}"</#if>                   v-model="queryParam[''queryField_${search.fieldName}'']"                  format="${searchDateFormat}"             <#if searchDateFormat==''YYYY-MM-DD HH:mm:ss''>               show-time             </#if>                   @change="searchDateChange($event,$event,{queryField:''<#if search.autoFilter==''YES''>Q_<#if search.tablePre?? ' || '&&' ||
                    ' search.tablePre!="">${search.tablePre}.</#if>${search.fieldName}_D_${search.fieldOp}<#else>${search.fieldName}</#if>'',format:''${searchDateFormat}''})"/>       <#elseif (search.fc==''month'')>          <a-month-picker  v-model="queryParam[''queryField_${search.fieldName}'']" <#if search.emptytext??>placeholder="${search.emptytext}"</#if>  @change="searchMonthChange($event,$event,{queryField:''<#if search.tablePre?? ' || '&&' ||
                    ' search.tablePre!="">${search.tablePre}.</#if>${search.fieldName}''<#if search.format??>,format:''${search.format}''</#if>,autoFilter:''${search.autoFilter}''})" />       <#elseif (search.fc==''rangepicker'')>          <#if search.format??>           <#assign searchRangeFormat=search.format>         <#else>           <#assign searchRangeFormat=''YYYY-MM-DD HH:mm:ss''>         </#if>          <a-range-picker  ref="queryField_${search.fieldName}"  v-model="params[''queryField_${search.fieldName}'']" <#if search.emptytext??>placeholder="${search.emptytext}"</#if> show-time format="${searchRangeFormat}"  @change="searchRangeDateChange($event,''${searchRangeFormat}'',{queryField:''<#if search.tablePre?? ' || '&&' ||
                    ' search.tablePre!="">${search.tablePre}.</#if>${search.fieldName}'',autoFilter:''${search.autoFilter}''})" />        <#elseif (search.fc==''select'')>         <rx-select ref="Q_<#if search.tablePre?? ' || '&&' ||
                    ' search.tablePre!="">${search.tablePre}_</#if>${search.fieldName}_S_${search.fieldOp}" v-model="queryParam[''<#if search.autoFilter==''YES''>Q_<#if search.tablePre?? ' || '&&' ||
                    ' search.tablePre!="">${search.tablePre}.</#if>${search.fieldName}_S_${search.fieldOp}<#else>${search.fieldName}</#if>'']"                 :multiValue="true" :labelInValue="false" :multiSelect="<#if search.multiSelect??>${search.multiSelect?c}<#else>false</#if>"                 <#if (!search.from?? || search.from==''self'')>:dataSource="selectOptions${search_index}" value-field="key" text-field="name"                 <#elseif (search.from==''url'')>url="${search.url}" value-field="${search.url_valuefield}" text-field="${search.url_textfield}"                 <#elseif (search.from==''dic'')>url="/api-system/system/core/sysDic/treeByKey?key=${search.dicKey}" value-field="value" text-field="name"                 <#elseif (search.from==''sql'')>url="/api-form/form/core/formCustomQuery/queryForJson_${search.sql}" data-field="data" value-field="${search.sql_valuefield}" text-field="${search.sql_textfield}"                @focus="selectFocus(''Q_<#if search.tablePre?? ' || '&&' ||
                    ' search.tablePre!="">${search.tablePre}_</#if>${search.fieldName}_S_${search.fieldOp}'',selectParams${search_index})" :loadDataOnstart="true"                @onChange="searchChange(''${search.fieldName}'',''Q_<#if search.tablePre?? ' || '&&' ||
                    ' search.tablePre!="">${search.tablePre}_</#if>${search.fieldName}_S_${search.fieldOp}'')" ajaxType="post"                <#elseif (search.from==''grant'')>url="/api-form/form/core/formEntityDataSetting/queryData_${search.grant}" data-field="data"                 @focus="selectFocus(''Q_<#if search.tablePre?? ' || '&&' ||
                    ' search.tablePre!="">${search.tablePre}_</#if>${search.fieldName}_S_${search.fieldOp}'')" :loadDataOnstart="true"             </#if>/>        <#elseif (search.fc==''treeselect'')>         <rx-tree-select-ctl ref="Q_<#if search.tablePre?? ' || '&&' ||
                    ' search.tablePre!="">${search.tablePre}_</#if>${search.fieldName}_S_${search.fieldOp}" v-model="queryParam[''<#if search.autoFilter==''YES''>Q_<#if search.tablePre?? ' || '&&' ||
                    ' search.tablePre!="">${search.tablePre}.</#if>${search.fieldName}_S_${search.fieldOp}<#else>${search.fieldName}</#if>'']"                :multiValue="true" :labelInValue="false" :multiSelect="<#if search.multiSelect??>${search.multiSelect?c}<#else>false</#if>" :showSearch="<#if search.showSearch??>${search.showSearch?c}<#else>false</#if>"                 <#if (!search.from?? || search.from==''self'')>:dataSource="selectOptions${search_index}" value-field="key" text-field="name"                 <#elseif (search.from==''url'')>url="${search.url}" value-field="${search.url_valuefield}" text-field="${search.url_textfield}" parent-field="${search.url_parentfield}"                <#elseif (search.from==''dic'')>url="/api-system/system/core/sysDic/treeByKey?key=${search.dicKey}" value-field="value" text-field="name" :toTree="false"                 <#elseif (search.from==''sql'')>url="/api-form/form/core/formCustomQuery/queryForJson_${search.sql}" data-field="data" value-field="${search.sql_valuefield}" text-field="${search.sql_textfield}" parent-field="${search.sql_parentfield}"                 @focus="selectFocus(''Q_<#if search.tablePre?? ' || '&&' ||
                    ' search.tablePre!="">${search.tablePre}_</#if>${search.fieldName}_S_${search.fieldOp}'',selectParams${search_index})" :loadDataOnstart="true"                @onChange="searchChange(''${search.fieldName}'',''Q_<#if search.tablePre?? ' || '&&' ||
                    ' search.tablePre!="">${search.tablePre}_</#if>${search.fieldName}_S_${search.fieldOp}'')" ajaxType="post"                <#elseif (search.from==''grant'')>url="/api-form/form/core/formEntityDataSetting/queryData_${search.grant}" data-field="data"                 @focus="selectFocus(''Q_<#if search.tablePre?? ' || '&&' ||
                    ' search.tablePre!="">${search.tablePre}_</#if>${search.fieldName}_S_${search.fieldOp}'')" :loadDataOnstart="true"             </#if>/>        <#elseif (search.fc==''dialog'')>         <rx-input-button v-model="params[''<#if search.autoFilter==''YES''>Q_<#if search.tablePre?? ' || '&&' ||
                    ' search.tablePre!="">${search.tablePre}.</#if>${search.fieldName}_S_${search.fieldOp}<#else>${search.fieldName}</#if>'']" :multiValue="true"                   :single="false" :config="{fieldName:''${search.fieldName}'',queryField:''<#if search.autoFilter==''YES''>Q_<#if search.tablePre?? ' || '&&' ||
                    ' search.tablePre!="">${search.tablePre}.</#if>${search.fieldName}_S_${search.fieldOp}<#else>${search.fieldName}</#if>'',dialog:''<#if search.dialog??>${search.dialog}</#if>'',dialog_name:''<#if search.dialog_name??>${search.dialog_name}</#if>'',valueField:''<#if search.valueField??>${search.valueField}</#if>'',textField:''<#if search.textField??>${search.textField}</#if>'',single:!<#if search.multiSelect??>${search.multiSelect?c}<#else>true</#if>}"                  @click="_OnDialogShow"/>       </#if>       </a-form-item>            </#list>          </a-form>        </div>        <#assign idField=convertUnderLine(pk)>        <rx-grid                ref="table"     <#if settingJson.multiSelect==''notSelect''>                :allowRowSelect="false"     <#else>       :allowRowSelect="true"      </#if>      <#if settingJson.multiSelect==''move''>       :multiSelect="true"     <#else>       :multiSelect="false"      </#if>                :columns="columns"      <#if settingJson.isPage==''YES''>       :showPage="true"        :defaultPageSize="${settingJson.pageSize}"        :pageSizeOptions="[''10'',''20'',''30'',''40'',''50'']"     <#else>       :showPage="false"     </#if>                url="/api-${system}/${system}/${package}/${classVar}/query"                :queryParam="queryParam"                data-field="result.data"                id-field="${idField}"                @selectChange="onSelectChange"        >     <#list commonList as col>     ${slotScopeColFun(col)}          </#list>            <span slot="action" slot-scope="{ text, record }" class="actionIcons">                <span  @click.stop="edit(record)">编辑</span>        <span  @click.stop="detail(record)">明细</span>                <span  @click.stop="delByIds([record.${idField}])">删除</span>            </span>        </rx-grid>      </rx-fit>    </div>  </rx-layout></template><script>  import ${class}Api from ''@/api/${system}/${package}/${classVar}''  import ${class}Edit from ''./${class}Edit''  import {BaseList,RxFit,RxGrid,RxButton,RxSpan } from ''jpaas-common-lib'';  import CodeGenlistJs from "@/views/modules/form/core/js-comps/CodeGenlist.js";  import ListRender from ''@/views/modules/form/core/js-comps/ListRender.js'';  import RxCategoryTreeeditor from ''@/components/redxun/rx-category-treeeditor''  import RxParamsTreeeditor from ''@/components/redxun/rx-params-treeeditor''  //import  from "@/views/modules/share/rx-button";  //import  from "@/views/modules/share/rx-span";  export default {    name: ''${class}List'',    mixins: [BaseList,CodeGenlistJs,ListRender],    components: {      RxButton,      RxSpan,      RxFit,   RxCategoryTreeeditor,   RxParamsTreeeditor    },    data() {      return {        columns: [          {            title: ''序号'',            type: ''indexColumn'',            width: 100,            dataIndex: ''serial'',            scopedSlots: {customRender: ''serial''}          },          <#list commonList as col>      <#if col.headShow==''YES''>     {title: ''${col.comment}'', dataIndex: ''${col.name}'', width: ${col.fieldLength}, scopedSlots: {customRender: ''${col.name}''}},     </#if>          </#list>          {title: ''操作'', width: 100, dataIndex: ''action'', scopedSlots: {customRender: ''action''}}        ],        component:${class}Edit,        comment:"${comment}",        fitSearch:false,   curTree:{},   ${searchTreeScriptFun()}      }    },    methods:{    ${leftQeuryParamFun()}      delById(ids){        return ${class}Api.del({ ids: ids.join('','') });      },      getListByParams(parameter){        return ${class}Api.query(parameter)      }    }  }</script><style scoped>  .table-operator {    padding:4px;  }  .table-operator button {    margin:2px;  }</style><#function searchTreeScriptFun >  <#assign listType=settingJson.listType> <#assign treeJson=settingJson.treeJson> <#assign treeKeyJson=treeJson.treeKeyJson>    <#assign rtn>        <#if listType==''tree''>     treeJson:${vueJs.treeJson},   </#if>    <#if treeJson.treeType==''sysDic'' || treeJson.treeType==''formData''>      treeCat:{           name:"<#if treeJson.treeType==''sysDic''>${treeKeyJson.name}<#else>${treeJson.formSolution.name}</#if>",            treeId:"<#if treeJson.treeType==''sysDic''>${treeKeyJson.treeId}<#else>${treeJson.formSolution.alias}</#if>"          },      searchTopNodesUrl:"<#if treeJson.treeType==''sysDic''>/api-system/system/core/sysDic/getByTreeIdParentId<#else>/api-form/form/core/formSolution/getNodesTreeByAlias</#if>",     searchParams:{      <#if treeJson.treeType==''sysDic''>       treeId:"<#if treeJson.treeKey??>${treeKeyJson.treeId}</#if>",       parentId:"0"      <#else>       alias: "${treeJson.formSolution.alias}",                pk: ""      </#if>      },      searchChildrenUrl:"<#if treeJson.treeType==''sysDic''>/api-system/system/core/sysDic/getByParentId<#else>/api-form/form/core/formSolution/getNodesTreeByAlias</#if>",     searchChildrenParams:{      <#if treeJson.treeType==''sysDic''>       parentId: "parentId"      <#else>       alias: "${treeJson.formSolution.alias}",                parentId: "pk"      </#if>      },      treeRelation:{        name:"<#if treeJson.treeType==''sysDic''>name<#else>${treeJson.formSolution.searchLable}</#if>",        treeId:"<#if treeJson.treeType==''sysDic''>dicId<#else>${treeJson.formSolution.searchPkId}</#if>",        parentId:"<#if treeJson.treeType==''sysDic''>parentId<#else>${treeJson.formSolution.searchParentId}</#if>",       key:"<#if treeJson.treeType==''sysDic''>value<#else>${treeJson.formSolution.searchPkId}</#if>",       value:"<#if treeJson.treeType==''sysDic''>value<#else>${treeJson.formSolution.searchPkId}</#if>"      },    </#if>    </#assign>    <#return rtn?trim></#function><#function leftQeuryParamFun >  <#assign listType=settingJson.listType> <#assign treeJson=settingJson.treeJson>    <#assign rtn>        <#if listType==''tree''>      getCurTree(){                return this.curTree;            },     handSelect(curRow) {                this.curTree=curRow;                var treeId = curRow.treeId;                if(treeId!="0"){                    this.queryParam.Q_${searchTreeColFun(treeJson)}_S_EQ=treeId;                }else {                    delete this.queryParam.Q_${searchTreeColFun(treeJson)}_S_EQ;                }                this.loadData(this.queryParam);            },    </#if>    </#assign>    <#return rtn?trim></#function><#function searchTreeColFun treeJson><#assign searchTreeCol=treeJson.searchTreeCol>    <#assign rtn>        <#if searchTreeCol.isSingle==0>     <#if treeJson.searchTreeColKey==''name''>     ${searchTreeCol.fieldName}_NAME     <#else>     ${searchTreeCol.fieldName}      </#if>    <#else>   ${searchTreeCol.fieldName}    </#if>    </#assign>    <#return rtn?trim></#function><#function leftSearchTreeFun >  <#assign listType=settingJson.listType> <#assign treeJson=settingJson.treeJson>    <#assign rtn>        <#if listType==''tree''>      <#if treeJson.treeType==''treeId''>     <div slot="left" width="220" showButton="true">       <rx-fit>          <rx-category-treeeditor           cat-key="${treeJson.treeKey}"           :edit="<#if treeJson.canEdit==''YES''>true<#else>false</#if>"           @handSelect="handSelect"            :async="true"           :readTreeId="<#if treeJson.catSysTreeKey??>''${treeJson.catSysTreeKey}''<#else>''''</#if>"          ></rx-category-treeeditor>        </rx-fit>     </div>      <#elseif treeJson.treeType==''sysDic'' || treeJson.treeType==''formData''>      <div slot="left" width="220" showButton="true">       <rx-fit>          <rx-params-treeeditor         ref="rxParamsTreeeditor"                    :edit="<#if treeJson.canEdit==''YES''>true<#else>false</#if>"                    @handSelect="handSelect"                    @addNode="add<#if treeJson.treeType==''sysDic''>Dic<#else>Form</#if>Node"                    @addSibling="add<#if treeJson.treeType==''sysDic''>Dic<#else>Form</#if>Sibling"                    @editNode="edit<#if treeJson.treeType==''sysDic''>Dic<#else>Form</#if>Node"                    @deleteNode="delete<#if treeJson.treeType==''sysDic''>Dic<#else>Form</#if>Node"                    :treeCat="treeCat"                    :searchTopNodesUrl="searchTopNodesUrl"                    :searchParams="searchParams"                    :treeRelation="treeRelation"                    :searchChildrenUrl="searchChildrenUrl"         :searchChildrenParams="searchChildrenParams"                ></rx-params-treeeditor>        </rx-fit>     </div>      </#if>    </#if>    </#assign>    <#return rtn?trim></#function><#function slotScopeColFun col> <#assign ctlType=col.control>    <#assign rtn>        <#if ctlType==''rx-upload''>      <span :title="text" slot="${col.name}" slot-scope="{text,record,index,blur}">       <span class="upFiles" v-html="onFileRender(text)"></span>            </span>    <#else>     <#if ctlType==''rx-address''>     <span slot="${col.name}" slot-scope="{text,record,index,blur}">       {{onAddressRender(''${model.genMode}'',record,''${col.name}'',${fileUtil.countAddressFromExtJson(col.extJson)})}}     </span>     <#elseif col.isSingle==''0''>     <span slot="${col.name}" slot-scope="{text,record,index,blur}">{{record.${col.name}Name}}</span>      <#else><span slot="${col.name}" slot-scope="{text,record,index,blur}">{{text}}</span></#if>   </#if>    </#assign>    <#return rtn?trim></#function><#function convertUnderLine field>  <#if field?index_of("_")==-1>    <#assign rtn>${field?lower_case?trim}</#assign>    <#return rtn>  </#if>  <#if field?index_of("F_")==0>    <#assign rtn><#list (field?substring(2))?split("_") as x><#if (x_index==0)><#if x?length==1>${x?upper_case?trim}<#else>${x?lower_case?trim}</#if><#else>${x?lower_case?cap_first?trim}</#if></#list></#assign>    <#return rtn>  </#if>  <#assign rtn><#list field?split("_") as x><#if (x_index==0)><#if x?length==1>${x?upper_case?trim}<#else>${x?lower_case?trim}</#if><#else>${x?lower_case?cap_first?trim}</#if></#list></#assign>  <#return rtn></#function>';
BEGIN
  UPDATE form_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1533698815715176450';
  COMMIT;
END;
/

INSERT
  INTO form_template(ID_,
                     NAME_,
                     ALIAS_,
                     TEMPLATE_,
                     TYPE_,
                     CATEGORY_,
                     FILE_NAME_,
                     PATH_,
                     SINGLE_,
                     GEN_MODE_,
                     MAIN_SUB_TYPE_,
                     DELETED_,
                     COMPANY_ID_,
                     TENANT_ID_,
                     CREATE_DEP_ID_,
                     CREATE_BY_,
                     CREATE_TIME_,
                     UPDATE_BY_,
                     UPDATE_TIME_,
                     APP_ID_) VALUES('1533698962918469634',
                                     '代码生成_实体编辑页面',
                                     'bodefEntityVueW',
                                     '',
                                     'codeGen',
                                     'main',
                                     '{class}Edit.vue',
                                     'src/views/modules/{system}/{package}',
                                     'YES',
                                     'genCode',
                                     'mainSub',
                                     0,
                                     '0',
                                     '1',
                                     '1',
                                     '1',
                                     TO_DATE('2022-6-6 14:35:02',
                                             'yyyy-MM-dd HH24:mi:ss'),
                                     '1',
                                     TO_DATE('2022-6-30 10:16:21',
                                             'yyyy-MM-dd HH24:mi:ss'),
                                     '');

DECLARE
  content_3 CLOB := '<#assign package=model.vars.package><#assign class=model.vars.class><#assign classVar=model.vars.classVar><#assign mainClassId=vars.mainClassId><#assign templateType=vars.templateType><#assign system=vars.system><#assign domain=vars.domain><#assign commonList=model.boAttrList><#assign subComList=model.boEntityList><template>    <rx-dialog @handOk="handleSubmit" @cancel="cancel" order="top" btnalign="right" :showok="!readonly">        <rx-layout>            <div slot="center">                <table class="table-form four-column" style="width:100%;" ctltype="rx-four-table" id="${mainClassId}"                       borders="noborder" data-sort="sortDisabled">                    <colgroup>            ${colgroupRowFun(templateType)}                    </colgroup>                    <caption>                        ${model.name}                    </caption>                    <tbody>                        ${firstRowFun(commonList templateType)}                        ${otherRowFun(commonList templateType)}                        <#if (subComList?? ' || '&&' ||
                    ' subComList?size>0)>                        ${subRowFun(subComList templateType)}                        </#if>                    </tbody>                </table>            </div>        </rx-layout>    </rx-dialog></template><script>    import ${class}Api from "@/api/${system}/${package}/${classVar}.js";    import ${class}Data from "@/views/modules/${system}/${package}/${class}Data.js";    import formJs from "@/views/modules/form/core/js-comps/form.js";  import {RxDialog} from ''jpaas-common-lib'';<#if mainSubType==''mainAloneSub''> <#list model.boEntityList as subEnt>  import ${subEnt.vars.class}Edit from ''./${subEnt.vars.class}Edit'' </#list></#if>    export default{        mixins: [formJs,${class}Data],        components:{         RxDialog    },        props:["pkId","layerid","destroy","readonly","refKey","refKeyName"],        data(){            return {       <#if mainSubType==''mainAloneSub''>       components:{          <#list model.boEntityList as subEnt>          "${subEnt.vars.classVar}":${subEnt.vars.class}Edit,         </#list>        },        </#if>      }        },        created() {            //加载表单数据            this.loadData(${class}Api);        },        mounted(){            this.onload();        },        methods:{            onload(){            },            async _beforeSubmit(){            },            _afterSubmit(result,formJson){            },            enter(entName, field){            },            handleSubmit(vm){                this.handSubmit(${class}Api,vm);            }        },        watch:{        }    }</script><#function subRowFun subComList templateType>    <#assign rtn> <#assign colindexNum=templateType?number>        <#list subComList as sub>            <#assign subColList=sub.boAttrList>            <#assign subVars=sub.vars>            <#assign colIndex=sub?index>                <tr class="active2" <#if mainSubType==''mainAloneSub''>v-if="readonly"</#if>>                    <td colspan="<#if colindexNum==4>8<#else>4</#if>" rowspan="1">                        <div class="rx-table-body  " ctltype="rx-table" type="onetomany" id="${sub.id}"                             tablename="${subVars.classVar}" style="width:100%" tabname="${subVars.classVar}"                             tabletype="default" req="no">                            <div class="table-header-tool">                                <div class="title">                                    <div>                                        ${sub.name}                                    </div>                                </div>                <#if mainSubType==''mainAloneSub''>               <a-button-group v-if="readonly">                                    <a-button                                            v-on:click="addSubOne(''${subVars.classVar}'',''inner'',''${subVars.classVar}'','''',''${sub.id}'')">                                        添加                                    </a-button>                 <a-button                                            v-on:click="editSubOne(''${subVars.classVar}'',''inner'',''${subVars.classVar}'','''',''${sub.id}'')">                                        编辑                                    </a-button>                                    <a-button                                            v-on:click="remove(''${subVars.classVar}'',''inner'',''${subVars.classVar}'','''',''${sub.id}'')">                                        删除                                    </a-button>                                    <a-button                                            v-on:click="up(''${subVars.classVar}'',''inner'',''${subVars.classVar}'','''',''${sub.id}'')">                                        上移                                    </a-button>                                    <a-button                                            v-on:click="down(''${subVars.classVar}'',''inner'',''${subVars.classVar}'','''',''${sub.id}'')">                                        下移                                    </a-button>                                </a-button-group>               <#else>               <a-button-group v-if="!getReadonly()">                                    <a-button                                            v-on:click="add(''${subVars.classVar}'',''inner'',''${subVars.classVar}'','''',''${sub.id}'')">                                        添加                                    </a-button>                                    <a-button                                            v-on:click="remove(''${subVars.classVar}'',''inner'',''${subVars.classVar}'','''',''${sub.id}'')">                                        删除                                    </a-button>                                    <a-button                                            v-on:click="up(''${subVars.classVar}'',''inner'',''${subVars.classVar}'','''',''${sub.id}'')">                                        上移                                    </a-button>                                    <a-button                                            v-on:click="down(''${subVars.classVar}'',''inner'',''${subVars.classVar}'','''',''${sub.id}'')">                                        下移                                    </a-button>                                </a-button-group>                </#if>                            </div>                            <div class="rx-table-box">                                <table class="rx-table">                                    <colgroup>                                        <col style="width: 60px;">                                        <col style="width: 60px;">                                        <#list subColList as col>                                        <col style="width: 100px;">                                        </#list>                                    </colgroup>                                    <thead>                                        <tr class="firstRow">                                            <th>#</th>                                            <th>序号</th>                                            <#list subColList as col>                                            <th v-bind:class="{required:required.${subVars.classVar}[''fields''].${col.name}}">${col.comment}</th>                                            </#list>                                        </tr>                                    </thead>                                    <tbody>                                        <tr v-if="''item''" v-for="(item,index) in data.sub__${subVars.classVar}"                                            v-tableselect="{item:item,selmode:''multi'',data:data.sub__${subVars.classVar}}"                                            :key="item.index_" :class="{active:item.selected}">                                            <td><a-checkbox :checked="item.selected"></a-checkbox></td>                                            <td><rx-serial :serial="index+1"></rx-serial></td>                                            <#list subColList as col>                                            <td>                                                <${col.control}                          type="onetomany"                          id="${col.id}"                          :readonly="readonly"                          v-model="item.${col.name}"                          :data="data"                          :row="item"                         :valid="validFunc(''${subVars.classVar}'',''${col.name}'',item)"                          name="${col.name}"                          permission="w"                          ${subColOtherFun(col)} ></${col.control}>                                            </td>                                            </#list>                                        </tr>                                    </tbody>                                </table>                                <div class="tableEmpty"                                     v-if="data.sub__${subVars.classVar}' || '&&' ||
                    'data.sub__${subVars.classVar}.length <= 0">                                    <a-empty></a-empty>                                </div>                            </div>                        </div>                    </td>                </tr>        </#list>    </#assign>    <#return rtn?trim></#function><#function colgroupRowFun templateType>    <#assign colindexNum=templateType?number>    <#assign rtn>   <#if colindexNum==1>    <col width="20%">   <col width="80%">   <#elseif colindexNum==2>    <col width="15%">   <col width="35%">   <col width="15%">   <col width="35%">   <#elseif colindexNum==4>    <col width="8%"/>   <col width="17%"/>    <col width="8%"/>   <col width="17%"/>    <col width="8%"/>   <col width="17%"/>    <col width="8%"/>   <col width="17%"/>    </#if>    </#assign>    <#return rtn?trim></#function><#function subColOtherFun col>    <#assign ctlType=col.control>    <#assign rtn>        <#if ctlType==''rx-number''>            :max="100000000"      :min="0"      :step="1"       valmode="single"        <#elseif ctlType==''rx-textbox''>            :ispassword="false"        <#elseif ctlType==''rx-form-select''>            verifyhide="${col.name}"        <#elseif ctlType==''rx-user''>            valmode="double"     gridname="main"            verifyhide="${col.name}"        <#elseif ctlType==''rx-group''>            valmode="double"      gridname="main"            verifyhide="${col.name}"        <#else >            valmode="single"        </#if>    </#assign>    <#return rtn?trim></#function><#function getRemainderNum templateNum>    <#assign rtn>        <#if templateNum==0 || templateNum==1 || templateNum==2>             1   <#elseif templateNum==3>      2   <#elseif templateNum==4>      3        </#if>    </#assign>    <#return rtn?trim></#function><#function otherRowFun commonList templateType>    <#assign templateNum=templateType?number> <#assign remainderNum=getRemainderNum(templateNum)>    <#assign rtn>        <#list commonList as col>            <#assign colIndex=col?index>            <#if colIndex >= templateNum>                <#if (colIndex % templateNum==0)>                    <tr>                </#if>                <#if (col.control==''rx-tree-select'' || col.control==''rx-form-select'')>                    <td style="word-break: break-all;" :class="{required:required.main.khly}">${col.comment}</td>                <#else >                    <td style="word-break: break-all;">${col.comment}</td>                </#if>                    <td style="word-break: break-all;">                        <${col.control}              type="main"             id="${col.id}"              :readonly="readonly"              v-model="data.${col.name}"              :valid="validFunc(''main'',''${col.name}'')"              permission="w"              ${colOtherFun(col)}></${col.control}>                    </td>                <#if colIndex % templateNum == remainderNum || !col_has_next>                </tr>                </#if>            </#if>        </#list>    </#assign>    <#return rtn?trim></#function><#function firstRowFun commonList templateType>    <#assign colindexNum=templateType?number>    <#assign rtn>        <#list commonList as col>            <#assign colIndex=col?index>            <#if colIndex==0>                <tr class="firstRow">            </#if>            <#if colIndex<colindexNum>                <td style="word-break: break-all;">${col.comment}</td>                <td style="word-break: break-all;">                <${col.control}          type="main"         id="${col.id}"          :readonly="readonly"          v-model="data.${col.name}"          :valid="validFunc(''main'',''${col.name}'')"          permission="w"          ${colOtherFun(col)}></${col.control}>                </td>            </#if>            <#if (colindexNum-colIndex)==1>                </tr>            </#if>        </#list>    </#assign>    <#return rtn?trim></#function><#function colOtherFun col>    <#assign ctlType=col.control>    <#assign rtn>        <#if ctlType==''rx-address''>    <#assign extJson=fileUtil.parseAddressFromExtJson(col.extJson)>           :setting="{isCity:${extJson.isCity},isCounty:${extJson.isCounty},isAddress:${extJson.isAddress}}"        <#elseif ctlType==''rx-radio''>      mode="default"      valmode="<#if col.isSingle==0>double<#else>single</#if>"      :data="data"      verifyhide="${col.name}"      :onlyvalue="false"        <#elseif ctlType==''rx-number''>      :max="1000000"      :min="0"      :step="1"     length="${col.length},${col.decimalLength}"        <#elseif ctlType==''rx-switch''>     mode="custom"     verifyhide="${col.name}"      check="Y"     uncheck="N"        <#elseif ctlType==''rx-tree-select''>      mode="multiple"     valmode="<#if col.isSingle==0>double<#else>single</#if>"      :selectable="false"     :data="data"        <#elseif ctlType==''rx-form-select''>     :data="data"      mode="<#if col.isSingle==0>double<#else>single</#if>"     valmode="single"      :hideselect="false"     verifyhide="${col.name}"        <#elseif ctlType==''rx-date''>            format="YYYY-MM-DD HH:mm:ss"        <#elseif ctlType==''rx-user''>      :data="data"      gridname="main"     :single="false"     verifyhide="${col.name}"        <#elseif ctlType==''rx-group''>     :data="data"      verifyhide="${col.name}"      gridname="main"     :single="false"     :showdeptpath="false"     :deptpathlevel="0"      :showdimid="true"        </#if>    </#assign>    <#return rtn?trim></#function>';
BEGIN
  UPDATE form_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1533698962918469634';
  COMMIT;
END;
/

COMMIT;
