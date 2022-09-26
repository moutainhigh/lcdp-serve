<#list models as model>
ALTER TABLE jpaas_system.${model.tableName} ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
</#list>
