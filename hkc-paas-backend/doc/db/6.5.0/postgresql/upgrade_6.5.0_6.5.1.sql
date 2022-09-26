ALTER TABLE `jpaas_user`.`os_user`
    ADD COLUMN `USER_NUMBER_`  varchar(64) NULL COMMENT '员工号' AFTER `UPDATE_TIME_`;
ALTER TABLE `jpaas_user`.`os_user`
    ADD COLUMN `WORK_PHONE_`  varchar(64) NULL COMMENT '办公电话' AFTER `UPDATE_TIME_`;
ALTER TABLE `jpaas_user`.`os_user`
    ADD COLUMN `CRED_NUMBER_`  varchar(64) NULL COMMENT '证件号码' AFTER `UPDATE_TIME_`;
ALTER TABLE `jpaas_user`.`os_user`
    ADD COLUMN `CRED_TYPE_`  varchar(64) NULL COMMENT '证件类型' AFTER `UPDATE_TIME_`;
ALTER TABLE `jpaas_user`.`os_user`
    ADD COLUMN `COMPANY_ID_`  VARCHAR(64) NULL COMMENT '所属公司ID' AFTER `UPDATE_TIME_`;

-- jpaas-user zfh 2021-10-09 用户组维度增加所属公司ID字段
ALTER TABLE `jpaas_user`.`os_dimension`
    ADD COLUMN `COMPANY_ID_`  varchar(64) NULL COMMENT '所属公司ID' AFTER `UPDATE_TIME_`;

-- jpaas-user lbh 2021-09-23
ALTER TABLE `jpaas_user`.`os_group`
    ADD COLUMN `GROUP_CODE_`  varchar(64) NULL COMMENT '用户组编号' AFTER `UPDATE_TIME_`;
ALTER TABLE `jpaas_user`.`os_group`
    ADD COLUMN `SHORT_NAME_`  varchar(64) NULL COMMENT '简称' AFTER `UPDATE_TIME_`;
ALTER TABLE `jpaas_user`.`os_group`
    ADD COLUMN `GROUP_TYPE_`  varchar(64) NULL COMMENT '组类型' AFTER `UPDATE_TIME_`;
ALTER TABLE `jpaas_user`.`os_group`
    ADD COLUMN `COM_TYPE_`  varchar(64) NULL COMMENT '公司类型' AFTER `UPDATE_TIME_`;
-- jpaas-user zfh 2021-09-28 行政组织增加组织类型字段
ALTER TABLE `jpaas_user`.`os_group`
    ADD COLUMN `TYPE_`  int(11) NULL COMMENT '组织类型，1代表公司、2代表部门' AFTER `COM_TYPE_`;
-- jpaas-user zfh 2021-10-09 系统用户组增加所属公司ID字段
ALTER TABLE `jpaas_user`.`os_group`
    ADD COLUMN `COMPANY_ID_`  varchar(64) NULL COMMENT '所属公司ID' AFTER `UPDATE_TIME_`;
-- jpaas-user zfh 2021-10-09 用户类型增加所属公司ID字段
ALTER TABLE `jpaas_user`.`os_user_type`
    ADD COLUMN `COMPANY_ID_`  varchar(64) NULL COMMENT '所属公司ID' AFTER `UPDATE_TIME_`;
-- jpaas-user lbh 2021-11-01 增加索引 提高加载组织人员速度
-- ALTER TABLE `jpaas_user`.`os_rel_inst`
--     ADD  INDEX `idx_os_rel_inst_party1` (`PARTY1_`);

-- jpaas-system zfh 2021-10-14 系统菜单增加所属公司ID字段
ALTER TABLE `jpaas_system`.`sys_menu`
    ADD COLUMN `COMPANY_ID_`  varchar(64) NULL COMMENT '所属公司ID' AFTER `UPDATE_TIME_`;
-- jpaas-system zfh 2021-10-18 系统菜单增加是否共享字段
ALTER TABLE `jpaas_system`.`sys_menu`
    ADD COLUMN `SHARE_`  varchar(10) NULL COMMENT '是否共享 Y,N' ;
ALTER TABLE `jpaas_system`.`sys_tree`
    ADD COLUMN `COMPANY_ID_`  varchar(64) NULL COMMENT '所属公司ID' AFTER `UPDATE_TIME_`;
-- jpaas-user zfh 2021-10-12 增加用户组应用表
CREATE TABLE `jpaas_user`.`os_group_app`
(
     `ID_` varchar(64) COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
     `GROUP_ID_` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户组Id',
     `APP_ID_` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '应用ID',
     PRIMARY KEY (`ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户组应用';
-- jpaas-system zfh 2021-10-12 应用系统增加所属公司ID字段
ALTER TABLE `jpaas_system`.`sys_app`
    ADD COLUMN `COMPANY_ID_`  varchar(64) NULL COMMENT '所属公司ID' AFTER `UPDATE_TIME_`;
-- jpaas-system zfh 2021-10-12 系统分类树增加所属公司ID字段

-- jpaas_system lbh 2021-11-9 公告接收对象类型初始化数据
INSERT INTO `jpaas_system`.`sys_tree` (`TREE_ID_`, `CODE_`, `NAME_`, `PATH_`, `PARENT_ID_`, `ALIAS_`, `DESCP_`, `CAT_KEY_`, `SN_`, `DATA_SHOW_TYPE_`, `TENANT_ID_`, `CREATE_DEP_ID_`, `CREATE_BY_`, `CREATE_TIME_`, `UPDATE_BY_`, `UPDATE_TIME_`, `COMPANY_ID_`, `APP_ID_`) VALUES ('1456526566008016898', NULL, '新闻公告接收对象类型', '0.1456526566008016898.', '0', 'RECEIVE_PART_TYPE', NULL, 'DIC', '4', 'FLAT', '1', '6193', '1', '2021-11-05 15:39:29', NULL, '2021-11-05 15:39:29', NULL, NULL);
INSERT INTO `jpaas_system`.`sys_dic` (`DIC_ID_`, `TREE_ID_`, `NAME_`, `VALUE_`, `DESCP_`, `SN_`, `PATH_`, `PARENT_ID_`, `TENANT_ID_`, `CREATE_DEP_ID_`, `CREATE_BY_`, `CREATE_TIME_`, `UPDATE_BY_`, `UPDATE_TIME_`, `APP_ID_`) VALUES ('1456526761429028866', '1456526566008016898', '本公司', '1', '', '1', '0.1456526761429028866.', '0', '1', '6193', '1', '2021-11-05 15:40:16', NULL, '2021-11-05 15:40:16', NULL);
INSERT INTO `jpaas_system`.`sys_dic` (`DIC_ID_`, `TREE_ID_`, `NAME_`, `VALUE_`, `DESCP_`, `SN_`, `PATH_`, `PARENT_ID_`, `TENANT_ID_`, `CREATE_DEP_ID_`, `CREATE_BY_`, `CREATE_TIME_`, `UPDATE_BY_`, `UPDATE_TIME_`, `APP_ID_`) VALUES ('1456526762741846018', '1456526566008016898', '指定公司', '2', '', '2', '0.1456526762741846018.', '0', '1', '6193', '1', '2021-11-05 15:40:16', NULL, '2021-11-05 15:40:16', NULL);
-- jpaas_system 修改数字字典
UPDATE `jpaas_system`.`sys_dic` SET `TREE_ID_` = '1280758128721715202', `NAME_` = '新闻公告', `VALUE_` = '新闻公告', `DESCP_` = '公司新闻', `SN_` = 2, `PATH_` = '0.1363005550629212162.', `PARENT_ID_` = '0', `TENANT_ID_` = '1', `CREATE_DEP_ID_` = NULL, `CREATE_BY_` = '1', `CREATE_TIME_` = '2021-02-20 14:00:21', `UPDATE_BY_` = '1', `UPDATE_TIME_` = '2021-09-15 09:15:53', `APP_ID_` = NULL WHERE `DIC_ID_` = '1363005550629212162';
-- jpaas-system zfh 2021-10-25 系统菜单数据变更
-- jpaas-system zfh 2021-10-25 系统菜单数据变更
UPDATE jpaas_system.sys_menu SET MENU_KEY_= 'FormInvokeScriptList', COMPONENT_= 'modules/form/core/FormInvokeScriptList.vue'      WHERE MENU_ID_ = 1281238408903122945 ;



ALTER TABLE `jpaas_portal`.`ins_news`
    ADD COLUMN `RECEIVE_PART_TYPE_`  varchar(64) NULL COMMENT '接收对象类型';
ALTER TABLE `jpaas_portal`.`ins_news`
    ADD COLUMN `RECEIVE_LIST_`  varchar(64) NULL COMMENT '接收公司';
ALTER TABLE `jpaas_portal`.`ins_news`
    ADD COLUMN `COMPANY_ID_`  varchar(64) NULL COMMENT '所属公司ID';
-- jpaas-portal zhl 2021-11-2 增加门户新闻公告公司授权表
CREATE TABLE `jpaas_portal`.`ins_news_company_grant`
(
     `ID_` varchar(64) COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
     `TYPE_` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '授权的业务类型',
     `BUSINESS_ID_` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '被授权的业务ID',
     `GRANT_COMPANY_ID_` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '授权公司ID',
     `GRANT_COMPANY_NAME_` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '授权公司名称',
     `GRANT_TIME_` datetime DEFAULT NULL COMMENT '授权时间',
     `COMPANY_ID_` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '被授权公司ID',
     PRIMARY KEY (`ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='表单公司授权表';

-- jpaas_portal  修改初始化数据
UPDATE `jpaas_portal`.`ins_column_def` SET `NAME_` = '公司公告', `KEY_` = 'newsPortColumn', `IS_DEFAULT_` = '0', `TEMPLET_` = '<div class=\"gridLayoutClass\">\n    <div class=\"headPClass\">\n        <div style=\"display:inline-block;\" >{{insColumnDef.name}}</div>\n        <div style=\"float: right\">\n            <div class=\"journalism_span\">\n             <span @click=\"moreUrl()\">更多</span>\n             <span @click=\"refresh\">刷新</span>\n            </div>\n        </div>\n    </div>\n    <div class=\"bodyDivClass\">\n        <div class=\"aclsty\" v-if=\"hasImage(data)\">\n            <a-carousel arrows autoplay >\n                <div  slot=\"prevArrow\"\n                    slot-scope=\"props\"\n                    class=\"custom-slick-arrow\"\n                    style=\"left: 10px;zIndex: 1\"\n                    >\n                    <a-icon type=\"left-circle\" />\n                </div>\n                <div slot=\"nextArrow\" slot-scope=\"props\" class=\"custom-slick-arrow\" style=\"right: 10px\">\n                    <a-icon type=\"right-circle\" />\n                </div>\n                <div v-for=\"obj of data\"  v-if=\"obj.imgFileId\" class=\"inglist\">\n                    <img v-if=\"obj.imgFileId\" :src=\"getImgPath(obj.imgFileId)\"></img>\n                </div>\n            </a-carousel>\n        </div>\n        <div  >\n            <ul>\n                <li class=\"itmelist\" v-for=\"(obj,index) of data\" :key=\"obj.pkId\" v-if=\"index <=5\">\n                  <p @click=\"getInsNews(obj.pkId)\">{{obj.subject}}</p>\n                  <span>{{obj.createTime}}</span>\n                </li>\n            </ul>\n        </div>\n    </div>\n</div>', `SET_TING_` = '{\"dataUrl\":\"/portal/InsNewsIssueds\",\"function\":\"\",\"newType\":\"wordsList\",\"tabgroups\":\"\",\"newDIc\":\"公司公告\"}', `IS_PUBLIC_` = '0', `TYPE_` = '1230775094247464962', `ICON_` = 'iconbaobiao1', `IS_MOBILE_` = '0', `TENANT_ID_` = '0', `CREATE_DEP_ID_` = '2', `CREATE_BY_` = '1', `CREATE_TIME_` = NULL, `UPDATE_BY_` = '1', `UPDATE_TIME_` = '2021-06-29 18:39:53' WHERE `COL_ID_` = '1239839327900266497';
UPDATE `jpaas_portal`.`ins_column_def` SET `NAME_` = '集团新闻', `KEY_` = 'insNews', `IS_DEFAULT_` = '0', `TEMPLET_` = '<div class=\"gridLayoutClass\">\n    <div class=\"headPClass\">\n        <div style=\"display:inline-block;\" >{{insColumnDef.name}}</div>\n        <div style=\"float: right\">\n            <div class=\"journalism_span\">\n             <span @click=\"moreUrl()\">更多</span>\n             <span @click=\"refresh\">刷新</span>\n            </div>\n        </div>\n    </div>\n    <div class=\"bodyDivClass\">\n        <div class=\"aclsty\" v-if=\"hasImage(data)\">\n            <a-carousel arrows autoplay >\n                <div  slot=\"prevArrow\"\n                    slot-scope=\"props\"\n                    class=\"custom-slick-arrow\"\n                    style=\"left: 10px;zIndex: 1\"\n                    >\n                    <a-icon type=\"left-circle\" />\n                </div>\n                <div slot=\"nextArrow\" slot-scope=\"props\" class=\"custom-slick-arrow\" style=\"right: 10px\">\n                    <a-icon type=\"right-circle\" />\n                </div>\n                <div v-for=\"obj of data\" v-if=\"obj.imgFileId\"  class=\"inglist\">\n                    <img :src=\"getImgPath(obj.imgFileId)\"></img>\n                </div>\n            </a-carousel>\n        </div>\n        <div  >\n            <ul>\n                <li class=\"itmelist\" v-for=\"(obj,index) of data\" :key=\"obj.pkId\" v-if=\"index <=5\">\n                  <p @click=\"getInsNews(obj.pkId)\">{{obj.subject}}</p>\n                  <span>{{obj.createTime}}</span>\n                </li>\n            </ul>\n        </div>\n    </div>\n</div>', `SET_TING_` = '{\"dataUrl\":\"/portal/InsNewsIssueds\",\"function\":\"portalScript.getPortalNews(colId)\",\"isNews\":\"YES\",\"newType\":\"wordsList\",\"tabgroups\":\"\",\"newDIc\":\"新闻公告\"}', `IS_PUBLIC_` = '0', `TYPE_` = '1230775094247464962', `ICON_` = NULL, `IS_MOBILE_` = '0', `TENANT_ID_` = '0', `CREATE_DEP_ID_` = '2', `CREATE_BY_` = '1', `CREATE_TIME_` = NULL, `UPDATE_BY_` = '1', `UPDATE_TIME_` = '2021-06-26 15:52:28' WHERE `COL_ID_` = '1239837443886018562';


-- jpaas-form zfh 2021-10-19 表单设计增加所属公司ID字段、是否共享字段
ALTER TABLE `jpaas_form`.`form_pc`
    ADD COLUMN `COMPANY_ID_`  varchar(64) NULL COMMENT '所属公司ID';
ALTER TABLE `jpaas_form`.`form_pc`
    ADD COLUMN `SHARE_`  varchar(10) NULL COMMENT '是否共享 Y,N' ;
-- jpaas-form zfh 2021-10-20 手机表单定义增加所属公司ID字段、是否共享字段
ALTER TABLE `jpaas_form`.`form_mobile`
    ADD COLUMN `COMPANY_ID_`  varchar(64) NULL COMMENT '所属公司ID';
ALTER TABLE `jpaas_form`.`form_mobile`
    ADD COLUMN `SHARE_`  varchar(10) NULL COMMENT '是否共享 Y,N' ;
-- jpaas-form zfh 2021-10-20 业务实体增加所属公司ID字段、是否共享字段
ALTER TABLE `jpaas_form`.`form_bo_entity`
    ADD COLUMN `COMPANY_ID_`  varchar(64) NULL COMMENT '所属公司ID';
ALTER TABLE `jpaas_form`.`form_bo_entity`
    ADD COLUMN `SHARE_`  varchar(10) NULL COMMENT '是否共享 Y,N' ;
-- jpaas-form zfh 2021-10-20 业务模型增加所属公司ID字段、是否共享字段
ALTER TABLE `jpaas_form`.`form_bo_def`
    ADD COLUMN `COMPANY_ID_`  varchar(64) NULL COMMENT '所属公司ID';
ALTER TABLE `jpaas_form`.`form_bo_def`
    ADD COLUMN `SHARE_`  varchar(10) NULL COMMENT '是否共享 Y,N' ;
-- jpaas-form zfh 2021-10-20 表单方案增加所属公司ID字段、是否共享字段
ALTER TABLE `jpaas_form`.`form_solution`
    ADD COLUMN `COMPANY_ID_`  varchar(64) NULL COMMENT '所属公司ID';
ALTER TABLE `jpaas_form`.`form_solution`
    ADD COLUMN `SHARE_`  varchar(10) NULL COMMENT '是否共享 Y,N' ;
-- jpaas-form zfh 2021-10-21 增加表单公司授权表
CREATE TABLE `jpaas_form`.`form_company_grant`
(
   `ID_` varchar(64) COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
   `TYPE_` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '授权的业务类型',
   `BUSINESS_ID_` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '被授权的业务ID',
   `GRANT_COMPANY_ID_` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '授权公司ID',
   `GRANT_COMPANY_NAME_` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '授权公司名称',
   `GRANT_TIME_` datetime DEFAULT NULL COMMENT '授权时间',
   `COMPANY_ID_` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '被授权公司ID',
   PRIMARY KEY (`ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='表单公司授权表';
-- jpaas-form zfh 2021-10-23 自定义列表管理增加所属公司ID字段、是否共享字段
ALTER TABLE `jpaas_form`.`form_bo_list`
    ADD COLUMN `COMPANY_ID_`  varchar(64) NULL COMMENT '所属公司ID';
ALTER TABLE `jpaas_form`.`form_bo_list`
    ADD COLUMN `SHARE_`  varchar(10) NULL COMMENT '是否共享 Y,N' ;
-- jpaas-form zfh 2021-10-23 表单验证规则增加所属公司ID字段、是否共享字段
ALTER TABLE `jpaas_form`.`form_rule`
    ADD COLUMN `COMPANY_ID_`  varchar(64) NULL COMMENT '所属公司ID';
ALTER TABLE `jpaas_form`.`form_rule`
    ADD COLUMN `SHARE_`  varchar(10) NULL COMMENT '是否共享 Y,N' ;
-- jpaas-form zfh 2021-10-23 表间公式增加所属公司ID字段、是否共享字段
ALTER TABLE `jpaas_form`.`form_table_formula`
    ADD COLUMN `COMPANY_ID_`  varchar(64) NULL COMMENT '所属公司ID';
ALTER TABLE `jpaas_form`.`form_table_formula`
    ADD COLUMN `SHARE_`  varchar(10) NULL COMMENT '是否共享 Y,N' ;
-- jpaas-form zfh 2021-10-23 表间公式日志增加所属公司ID字段
ALTER TABLE `jpaas_form`.`form_sql_log`
    ADD COLUMN `COMPANY_ID_`  varchar(64) NULL COMMENT '所属公司ID';
-- jpaas-form zfh 2021-10-23 业务实体数据类型增加所属公司ID字段
ALTER TABLE `jpaas_form`.`form_entity_data_type`
    ADD COLUMN `COMPANY_ID_`  varchar(64) NULL COMMENT '所属公司ID';
-- jpaas-form zfh 2021-10-23 业务实体数据配置增加所属公司ID字段
ALTER TABLE `jpaas_form`.`form_entity_data_setting`
    ADD COLUMN `COMPANY_ID_`  varchar(64) NULL COMMENT '所属公司ID';
-- jpaas-system zfh 2021-10-23 增加表单调用脚本表
CREATE TABLE `jpaas_form`.`form_invoke_script`
(
       `ID_` varchar(64) COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
       `TREE_ID_` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '分类ID',
       `NAME_` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '名称',
       `ALIAS_` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '别名',
       `PARAMS_` varchar(400) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '参数定义',
       `CONTENT_` varchar(4000) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '脚本定义',
       `DESCP_` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '描述',
       `TENANT_ID_` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '租用用户Id',
       `CREATE_DEP_ID_` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建部门ID',
       `CREATE_BY_` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人ID',
       `CREATE_TIME_` datetime DEFAULT NULL COMMENT '创建时间',
       `UPDATE_BY_` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新人ID',
       `UPDATE_TIME_` datetime DEFAULT NULL COMMENT '更新时间',
       `APP_ID_` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '应用ID',
       `COMPANY_ID_` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '所属公司ID',
       `SHARE_` varchar(10) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '是否共享 Y,N',
       PRIMARY KEY (`ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='表单调用脚本';

-- jpaas-form zfh 2021-10-25 表单模版增加所属公司ID字段、是否共享字段
ALTER TABLE `jpaas_form`.`form_template`    ADD COLUMN `COMPANY_ID_`  varchar(64) NULL COMMENT '所属公司ID';
ALTER TABLE `jpaas_form`.`form_template`    ADD COLUMN `SHARE_`  varchar(10) NULL COMMENT '是否共享 Y,N' ;
-- jpaas-form zfh 2021-10-25 表单布局定制增加所属公司ID字段、是否共享字段
ALTER TABLE `jpaas_form`.`form_custom`    ADD COLUMN `COMPANY_ID_`  varchar(64) NULL COMMENT '所属公司ID';
ALTER TABLE `jpaas_form`.`form_custom`    ADD COLUMN `SHARE_`  varchar(10) NULL COMMENT '是否共享 Y,N' ;
-- jpaas-form zfh 2021-10-25 自定义SQL查询增加所属公司ID字段、是否共享字段
ALTER TABLE `jpaas_form`.`form_custom_query`    ADD COLUMN `COMPANY_ID_`  varchar(64) NULL COMMENT '所属公司ID';
ALTER TABLE `jpaas_form`.`form_custom_query`    ADD COLUMN `SHARE_`  varchar(10) NULL COMMENT '是否共享 Y,N' ;


-- jpaas-bpm zhl 2021-10-28 增加流程公司授权表
CREATE TABLE `jpaas_bpm`.`bpm_company_grant`
(
     `ID_` varchar(64) COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
     `TYPE_` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '授权的业务类型',
     `BUSINESS_ID_` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '被授权的业务ID',
     `GRANT_COMPANY_ID_` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '授权公司ID',
     `GRANT_COMPANY_NAME_` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '授权公司名称',
     `GRANT_TIME_` datetime DEFAULT NULL COMMENT '授权时间',
     `COMPANY_ID_` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '被授权公司ID',
     PRIMARY KEY (`ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='表单公司授权表';

-- jpaas-bpm lbh 2021-10-26 流程定义查询增加所属公司ID字段、是否共享字段
ALTER TABLE `jpaas_bpm`.`bpm_def`    ADD COLUMN `COMPANY_ID_`  varchar(64) NULL COMMENT '所属公司ID';
ALTER TABLE `jpaas_bpm`.`bpm_def`    ADD COLUMN `SHARE_`  varchar(10) NULL COMMENT '是否共享 Y,N' ;

-- 2022-02-23 gjh  jpaas_form  数据列表新增搜索最大显示数字段
ALTER TABLE `jpaas_form`.`form_bo_list` ADD COLUMN SEARCH_MAX int NULL COMMENT '搜索最大显示数';
UPDATE `jpaas_form`.`form_bo_list` SET SEARCH_MAX = 2;


-- 2022-02-24 gjh  jpaas_form  表单设计暂存存
ALTER TABLE jpaas_form.form_pc ADD COLUMN TEMPLATE_TEMP_ text  COMMENT '模版(临时)';
ALTER TABLE jpaas_form.form_pc ADD COLUMN JAVASCRIPT_TEMP_ text  COMMENT '表单脚本(临时)';
ALTER TABLE jpaas_form.form_pc ADD COLUMN METADATA_TEMP_ text  COMMENT '表单数据(临时)';

-- 2022-03-02  jpaas_form   Elwin  表单业务方案加应用ID字段
ALTER TABLE jpaas_form.form_business_solution   ADD COLUMN  APP_ID_ VARCHAR(64)  COMMENT '应用ID';

-- 2022-03-04  jpaas_form   gjh  模板调整
UPDATE `jpaas_form`.`form_template` SET `NAME_` = '主表单列模板', `ALIAS_` = 'oneColumn', `TEMPLATE_` = '<#setting number_format=\"#\">\n<table class=\"table-form two-column\" style=\"width:100%;\">\n    <caption>\n        ${ent.name}\n    </caption>\n 	<colgroup>\n            <col width=\"20%\"/>\n            <col width=\"80%\"/>\n    </colgroup>\n    <tbody>\n        <#list ent.boAttrList as field>\n			<#if (field.control!=\'rx-ref\')>\n				<tr <#if (field_index==0)>class=\"firstRow\"</#if>>\n					<td style=\"word-break: break-all;\">\n						${field.comment}\n					</td>\n					<td>\n						<@fieldCtrl field=field type=\'main\' />\n					</td>\n				</tr>\n			</#if>\n        \n        </#list>\n    </tbody>\n</table>', `TYPE_` = 'pc', `CATEGORY_` = 'main', `TENANT_ID_` = '0', `CREATE_DEP_ID_` = '2', `CREATE_BY_` = '1', `CREATE_TIME_` = '2020-06-24 16:40:44', `UPDATE_BY_` = '1', `UPDATE_TIME_` = '2022-03-04 17:29:01', `APP_ID_` = NULL WHERE `ID_` = '1275710436348100609';
UPDATE `jpaas_form`.`form_template` SET `NAME_` = '主表双列模版', `ALIAS_` = 'twocolumn', `TEMPLATE_` = '<#setting number_format=\"#\">\n<table class=\"table-form four-column\" style=\"width:100%;\">\n    <caption>\n        ${ent.name}\n    </caption>\n   <col width=\"15%\"/>\n            <col width=\"35%\"/>\n            <col width=\"15%\"/>\n            <col width=\"35%\"/>\n    <tbody>\n	<#assign flag=0>\n    <#list ent.boAttrList as field>\n		<#if (field.control!=\'rx-ref\')>\n			<#if (!field.spans || field.spans==1)>\n			<#if (field_index ==0 || flag==0) >\n				<tr <#if (field_index==0)>class=\"firstRow\"</#if>>\n			</#if>\n				<td style=\"word-break: break-all;\">\n					${field.comment}\n				</td>\n				<td>\n					<#assign flag++>\n					<@fieldCtrl field=field type=\'main\' />\n				</td>\n				<#if ((field_index ==0 && !field_has_next && ent.boAttrList[field_index+1].spans==2) || (ent.boAttrList[field_index-1].spans==2 && !field_has_next && ent.boAttrList[field_index+1].spans==2 ) ) >\n					<td></td>\n					<td></td>\n				</#if>\n				<#if !field_has_next || (field_index ==0  && ent.boAttrList[field_index+1].spans==2)||\n				(field_index !=0  && ent.boAttrList[field_index-1].spans==1 && ent.boAttrList[field_index+1].spans==2)||\n				(field_index !=0  && ent.boAttrList[field_index-1].spans==2 && ent.boAttrList[field_index+1].spans==2)|| \n				(field_index !=0 && ent.boAttrList[field_index+1].spans==1 && flag==2) >\n					<#assign flag=0>\n					</tr>\n				</#if>\n		<#else>	\n			<tr class=\"firstRow\">\n				<td style=\"word-break: break-all;\">\n					${field.comment}\n				</td>\n				<td rowspan=\"1\" colspan=\"3\">\n					<@fieldCtrl field=field type=\'main\' />\n				</td>\n			</tr>\n		</#if>\n		</#if>\n    </#list>\n    </tbody>\n</table>\n', `TYPE_` = 'pc', `CATEGORY_` = 'main', `TENANT_ID_` = '0', `CREATE_DEP_ID_` = '2', `CREATE_BY_` = '1', `CREATE_TIME_` = '2020-06-28 11:11:39', `UPDATE_BY_` = '1', `UPDATE_TIME_` = '2022-03-04 17:28:21', `APP_ID_` = NULL WHERE `ID_` = '1277077170606948354';
UPDATE `jpaas_form`.`form_template` SET `NAME_` = '子表行模版', `ALIAS_` = 'subOneToMany', `TEMPLATE_` = '<#setting number_format=\"#\">\n\n            <div class=\"rx-table-body active\" ctltype=\"rx-table\" tableid=\"${ent.id}\" type=\"onetomany\"\n                 id=\"${util.randomId()}\" tablename=\"${ent.alias}\">\n                <div class=\"table-header-tool\">\n                    <div class=\"title\">\n                        <div>\n                        ${ent.name}\n                        </div>\n                    </div>\n                    <a-button-group>\n                        <a-button v-on:click=\"add(\'${ent.alias}\',\'inner\')\">\n                            添加\n                        </a-button>\n                        <a-button v-on:click=\"remove(\'${ent.alias}\',\'inner\')\">\n                            删除\n                        </a-button>\n                        <a-button v-on:click=\"up(\'${ent.alias}\',\'inner\')\">\n                            上移\n                        </a-button>\n                        <a-button v-on:click=\"down(\'${ent.alias}\',\'inner\')\">\n                            下移\n                        </a-button>\n                    </a-button-group>\n                </div>\n                <div class=\"rx-table-box\">\n                    <table class=\"rx-table\">\n                        <col style=\"width: 60px;\"/>\n                         <col style=\"width: 60px;\"/>\n                         <#list ent.boAttrList as field>\n                             <#if (field.control!=\'rx-ref\')>\n                         		<col style=\"width: 160px;\"/>\n 							</#if>\n                         </#list>\n                        <thead>\n                        <tr class=\"firstRow\">\n                            <th>#</th>\n							<th>序号</th>\n                            <#list ent.boAttrList as field>\n							<#if (field.control!=\'rx-ref\')>\n                         		<th>\n                                    ${field.comment}\n                                </th>\n 							</#if>\n                            </#list>\n                        </tr>\n                        </thead>\n                        <tbody>\n                        <tr v-for=\"(item,index) in data.sub__${ent.alias}\" :key=\"item.index_\" v-tableselect=\"{item:item,selmode:&#39;multi&#39;,data:data.sub__${ent.alias}}\"\n                            :class=\"{active:item.selected}\">\n                            <td><a-checkbox :checked=\"item.selected\"></a-checkbox></td>\n							 <td><rx-serial :serial=\"index+1\"></rx-serial></td>\n                            <#list ent.boAttrList as field>\n                                <#if (field.control!=\'rx-ref\')>\n                                     <td>\n                                        <@fieldCtrl field=field type=\'onetomany\' entName=ent.alias />\n                                    </td>\n 								</#if>\n                            </#list>\n                        </tr>\n                        </tbody>\n                    </table>\n					<div class=\"tableEmpty\" v-if=\"data.sub__${ent.alias}&amp;&amp;data.sub__${ent.alias}.length &lt;= 0\">\n                                <a-empty></a-empty>\n                            </div>\n                </div>\n            </div>\n', `TYPE_` = 'pc', `CATEGORY_` = 'onetomany', `TENANT_ID_` = '0', `CREATE_DEP_ID_` = '2', `CREATE_BY_` = '1', `CREATE_TIME_` = '2020-06-28 11:29:09', `UPDATE_BY_` = '', `UPDATE_TIME_` = '2022-03-04 10:36:17', `APP_ID_` = NULL WHERE `ID_` = '1277081573669847041';
UPDATE `jpaas_form`.`form_template` SET `NAME_` = '子表双列模版', `ALIAS_` = 'subOneToOneTwoColumn', `TEMPLATE_` = '<table class=\"table-form four-column\" style=\"width:100%;\" id=\"${util.randomId()}\" type=\"onetoone\" ctltype=\"rx-table-onetoone\" tableid=\"${ent.id}\" tablename=\"${ent.alias}\">\n    <caption>\n        ${ent.name}\n    </caption>\n<colgroup>\n                        <col width=\"15%\"/>\n                        <col width=\"35%\"/>\n                        <col width=\"15%\"/>\n                        <col width=\"35%\"/>\n                    </colgroup>\n    <tbody>\n    <#list ent.boAttrList as field>\n		<#if (field.control!=\'rx-ref\')>\n           <#if (field_index % 2==0)>\n            <tr <#if (field_index==0)>class=\"firstRow\"</#if>>\n        </#if>\n        <td style=\"word-break: break-all;\">\n            ${field.comment}\n        </td>\n        <td>\n            <@fieldCtrl field=field type=\'onetoone\' entName=ent.alias />\n        </td>\n\n        <#if field_index % 2 == 0 && !field_has_next>\n            <td></td>\n            <td></td>\n        </#if>\n        <#if field_index % 2 == 1 || !field_has_next>\n            </tr>\n        </#if>              		\n 		</#if>\n    </#list>\n    </tbody>\n</table>', `TYPE_` = 'pc', `CATEGORY_` = 'onetoone', `TENANT_ID_` = '0', `CREATE_DEP_ID_` = '2', `CREATE_BY_` = '1', `CREATE_TIME_` = '2020-06-28 11:45:31', `UPDATE_BY_` = '1', `UPDATE_TIME_` = '2022-03-04 10:38:23', `APP_ID_` = NULL WHERE `ID_` = '1277085691964669953';
UPDATE `jpaas_form`.`form_template` SET `NAME_` = '子表单列模版', `ALIAS_` = 'subWinOneColumn', `TEMPLATE_` = '<#setting number_format=\"#\">\n<div class=\"rx-table-body\" ctltype=\"rx-table\" type=\"onetomany\" id=\"${util.randomId()}\" tableid=\"${ent.id}\" tablename=\"${ent.alias}\" tabname=\"${ent.alias}\" style=\"width:100%\">\n        <div class=\"table-header-tool\">\n            <div class=\"title\">\n                <div>\n                    ${ent.name}\n                </div>\n            </div>\n            <a-button-group v-if=\"!getReadonly()\">\n                 <a-button  v-if=\"getSubTablePermission(\'${ent.alias}\',\'add\')\" v-on:click=\"add(\'${ent.alias}\',\'window\',\'${ent.name}\')\">\n                    添加\n                </a-button>\n  				<a-button v-if=\"getSubTablePermission(\'${ent.alias}\',\'edit\')\" v-on:click=\"edit(\'${ent.alias}\',\'window\',\'${ent.name}\')\">\n                    编辑\n                </a-button>\n                <a-button  v-if=\"getSubTablePermission(\'${ent.alias}\',\'remove\')\" v-on:click=\"remove(\'${ent.alias}\',\'window\',\'${ent.name}\')\">\n                    删除\n                </a-button>\n                <a-button v-if=\"getSubTablePermission(\'${ent.alias}\',\'up\')\" v-on:click=\"up(\'${ent.alias}\',\'window\',\'${ent.name}\')\">\n                    上移\n                </a-button>\n                <a-button v-if=\"getSubTablePermission(\'${ent.alias}\',\'down\')\" v-on:click=\"down(\'${ent.alias}\',\'window\',\'${ent.name}\')\">\n                    下移\n                </a-button>\n            </a-button-group>\n        </div>\n        <div class=\"rx-table-box\" style=\"display: none\">\n            <table class=\"rx-table\">\n                <colgroup>\n                    <col style=\"width: 60px;\"/>\n            		<col style=\"width: 60px;\"/>\n                   	<#list ent.boAttrList as field>\n                            <col style=\"width: 160px;\"/>\n                    </#list>\n                   \n                </colgroup>\n                <thead>\n                    <tr class=\"firstRow\">\n                        <th>#</th>\n                        <th>序号 </th>\n                       <#list ent.boAttrList as field>\n                            <th>\n                                ${field.comment}\n                            </th>\n                        </#list>\n                    </tr>\n                </thead>\n                <tbody>\n				<tr v-if=\"item\" v-for=\"(item,index) in data.sub__${ent.alias}\" :key=\"item.index_\" v-tableselect=\"{item:item,selmode:&#39;multi&#39;,data:data.sub__${ent.alias}}\" :class=\"{active:item.selected}\" dblclick=\"edit(\'${ent.alias}\')\">\n                         <td><a-checkbox :checked=\"item.selected\"></a-checkbox> </td>\n						 <td>${index + 1}</td>\n                        <#list ent.boAttrList as field>\n                            <td>\n                                <@fieldCtrl field=field type=\'onetomany\' entName=ent.alias readonly=true />\n                            </td>\n                        </#list>\n                    </tr>\n                </tbody>\n            </table>\n            <div class=\"tableEmpty\" v-if=\"data.sub__${ent.alias} && data.sub__${ent.alias}.length <= 0\">\n                <a-empty></a-empty>\n            </div>\n        </div>\n        <div class=\"rx-table-dialog\"  v-if=\"0\"   id=\"dialog_${ent.alias}\" >\n            <div class=\"dialog-header\">\n                ${ent.name}\n            </div>\n\n            <table class=\"table-detail column-two table-align\" style=\"width:100%;\">\n                <colgroup>\n                  	<col width=\"20%\"/>\n                    <col width=\"80%\"/>\n                </colgroup>\n                <tbody>\n                  <#list ent.boAttrList as field>\n                      <tr>\n                        <td>${field.comment}</td>\n                        <td>\n                            <@fieldCtrl field=field type=\'onetomany\' entName=ent.alias isAttr=\'false\'/>\n                        </td>\n                      </tr>\n                    </#list>\n                </tbody>\n            </table>\n        </div>\n    </div>\n', `TYPE_` = 'pc', `CATEGORY_` = 'onetomany', `TENANT_ID_` = '0', `CREATE_DEP_ID_` = '2', `CREATE_BY_` = '1', `CREATE_TIME_` = '2020-06-28 11:46:31', `UPDATE_BY_` = '1', `UPDATE_TIME_` = '2022-02-21 16:42:27', `APP_ID_` = NULL WHERE `ID_` = '1277085945392906241';
UPDATE `jpaas_form`.`form_template` SET `NAME_` = '子表双列模版', `ALIAS_` = 'subWinTwoColumn', `TEMPLATE_` = '<#setting number_format=\"#\">\n<div class=\"rx-table-body\" ctltype=\"rx-table\" type=\"onetomany\" id=\"${util.randomId()}\" tableid=\"${ent.id}\" tablename=\"${ent.alias}\" tabname=\"${ent.alias}\" style=\"width:100%\">\n        <div class=\"table-header-tool\">\n            <div class=\"title\">\n                <div>\n                    ${ent.name}\n                </div>\n            </div>\n            <a-button-group v-if=\"!getReadonly()\">\n                 <a-button  v-if=\"getSubTablePermission(\'${ent.alias}\',\'add\')\" v-on:click=\"add(\'${ent.alias}\',\'window\',\'${ent.name}\')\">\n                    添加\n                </a-button>\n  				<a-button v-if=\"getSubTablePermission(\'${ent.alias}\',\'edit\')\" v-on:click=\"edit(\'${ent.alias}\',\'window\',\'${ent.name}\')\">\n                    编辑\n                </a-button>\n                <a-button  v-if=\"getSubTablePermission(\'${ent.alias}\',\'remove\')\" v-on:click=\"remove(\'${ent.alias}\',\'window\',\'${ent.name}\')\">\n                    删除\n                </a-button>\n                <a-button v-if=\"getSubTablePermission(\'${ent.alias}\',\'up\')\" v-on:click=\"up(\'${ent.alias}\',\'window\',\'${ent.name}\')\">\n                    上移\n                </a-button>\n                <a-button v-if=\"getSubTablePermission(\'${ent.alias}\',\'down\')\" v-on:click=\"down(\'${ent.alias}\',\'window\',\'${ent.name}\')\">\n                    下移\n                </a-button>\n            </a-button-group>\n        </div>\n        <div class=\"rx-table-box\" style=\"display: none\">\n            <table class=\"rx-table\">\n                <colgroup>\n                    <col style=\"width: 60px;\"/>\n            		<col style=\"width: 60px;\"/>\n                   	<#list ent.boAttrList as field>\n						<#if (field.control!=\'rx-ref\')>\n							<col style=\"width: 160px;\"/>\n						</#if> \n                    </#list>\n                   \n                </colgroup>\n                <thead>\n                    <tr class=\"firstRow\">\n                        <th>#</th>\n						<th style=\"text-align:center;\">选择</th>\n                       <#list ent.boAttrList as field>\n						   <#if (field.control!=\'rx-ref\')>\n								 <th>\n									${field.comment}\n								</th>\n							</#if> \n                        </#list>\n                    </tr>\n                </thead>\n                <tbody>\n                    <tr v-if=\"item\" v-for=\"(item,index) in data.sub__${ent.alias}\" :key=\"item.index_\" v-select=\"item\" :class=\"{active:item.selected}\" dblclick=\"edit(\'${ent.alias}\')\">\n                        <td>${index + 1}</td>\n   						<td style=\"text-align:center;\"><a-checkbox :checked=\"item.selected\" @change=\'item=>item.selected==!item.selected\'></a-checkbox></td>\n                        <#list ent.boAttrList as field>\n							<#if (field.control!=\'rx-ref\')>\n								<td>\n									<@fieldCtrl field=field type=\'onetomany\' entName=ent.alias readonly=true />\n								</td>\n							</#if>\n                        </#list>\n                    </tr>\n                </tbody>\n            </table>\n            <div class=\"tableEmpty\" v-if=\"data.sub__${ent.alias} && data.sub__${ent.alias}.length <= 0\">\n                <a-empty></a-empty>\n            </div>\n        </div>\n        <div class=\"rx-table-dialog\"  v-if=\"0\"   id=\"dialog_${ent.alias}\" >\n            <div class=\"dialog-header\">\n                ${ent.name}\n            </div>\n\n            <table class=\"table-detail column-four table-align\" style=\"width:100%;\">\n                <colgroup>\n                    <col width=\"15%\"/>\n                    <col width=\"35%\"/>\n                    <col width=\"15%\"/>\n                    <col width=\"35%\"/>\n                </colgroup>\n                <tbody>\n                   <#list ent.boAttrList as field>\n						<#if (field.control!=\'rx-ref\')>\n								<#if (field_index % 2==0)>\n                            <tr>\n                        </#if>\n                        <td style=\"word-break: break-all;\">\n                            ${field.comment}\n                        </td>\n                        <td>\n                            <@fieldCtrl field=field type=\'onetomany\' entName=ent.alias isAttr=\'false\' />\n                        </td>\n\n                        <#if field_index % 2 == 0 && !field_has_next>\n                            <td></td>\n                            <td></td>\n                        </#if>\n                        <#if field_index % 2 == 1 || !field_has_next>\n                            </tr>\n                        </#if>\n						</#if>\n\n                    </#list>\n                </tbody>\n            </table>\n        </div>\n    </div>', `TYPE_` = 'pc', `CATEGORY_` = 'onetomany', `TENANT_ID_` = '0', `CREATE_DEP_ID_` = '2', `CREATE_BY_` = '1', `CREATE_TIME_` = '2020-06-28 11:47:20', `UPDATE_BY_` = '1', `UPDATE_TIME_` = '2022-03-04 10:40:27', `APP_ID_` = NULL WHERE `ID_` = '1277086151886880770';
UPDATE `jpaas_form`.`form_template` SET `NAME_` = '子表单列模版', `ALIAS_` = 'subOneToOne', `TEMPLATE_` = '<table class=\"table-form two-column\" style=\"width:100%;\" id=\"${util.randomId()}\" type=\"onetoone\" ctltype=\"rx-table-onetoone\" tableid=\"${ent.id}\" tablename=\"${ent.alias}\">\n    <caption>\n        ${ent.name}\n    </caption>\n    <colgroup>\n            <col width=\"20%\"/>\n            <col width=\"80%\"/>\n    </colgroup>\n    <tbody>\n    <#list ent.boAttrList as field>\n    <tr <#if (field_index==0)>class=\"firstRow\"</#if>>\n        <td style=\"word-break: break-all;\">\n            ${field.comment}\n        </td>\n        <td>\n            <@fieldCtrl field=field type=\'onetoone\' entName=ent.alias />\n        </td>\n    </tr>\n    </#list>\n    </tbody>\n</table>\n', `TYPE_` = 'pc', `CATEGORY_` = 'onetoone', `TENANT_ID_` = '0', `CREATE_DEP_ID_` = '2', `CREATE_BY_` = '1', `CREATE_TIME_` = '2020-06-28 11:52:12', `UPDATE_BY_` = '1', `UPDATE_TIME_` = '2020-12-11 17:51:11', `APP_ID_` = NULL WHERE `ID_` = '1277087373637304321';
UPDATE `jpaas_form`.`form_template` SET `NAME_` = '字段控件模版', `ALIAS_` = 'fieldCtrl', `TEMPLATE_` = '<#setting number_format=\"#\">\n<#macro fieldCtrl field type entName readonly isAttr>\n    <${field.control} class=\"${field.control}\" isattr=\"${isAttr}\" type=\"${type}\" ctltype=\"${field.control}\" style=\"width:100%\"\n                        <@attrJson field=field type=type entName=entName readonly=readonly/>\n            ></${field.control}>\n</#macro>\n\n<#macro attrJson field type entName readonly>\n    :readonly=\"${readonly???string(readonly,\'readonly\')}\" ctlid=\"${field.id}\"\n    <#if (type==\'main\')>\n        id=\"${util.randomId()}\" :permission=\"permission.${field.name}\" v-model=\"data.${field.name}\"\n        :valid=\"validFunc(\'main\',\'${field.name}\')\"\n        v-on:enter=\"enter(\'main\',\'${field.name}\')\"\n        v-on:valuechange=\"valuechange(\'main\',\'${field.name}\')\"\n    <#elseif (type==\'onetoone\')>\n		id=\"${field.name}${field.id}\"\n        :permission=\"permission.sub__${entName}.${field.name}\"\n        :valid=\"validFunc(\'${entName}\',\'${field.name}\',item)\"\n        v-model=\"data.sub__${entName}.${field.name}\"\n        v-on:enter=\"enter(\'${entName}\',\'${field.name}\',item)\"\n        v-on:valuechange=\"valuechange(\'${entName}\',\'${field.name}\',item)\"\n    <#else>\n		id=\"${field.name}${field.id}\"\n        :permission=\"permission.sub__${entName}.${field.name}\" v-model=\"item.${field.name}\" :data=\"item\"\n        :valid=\"validFunc(\'${entName}\',\'${field.name}\',index)\"\n        v-on:enter=\"enter(\'${entName}\',\'${field.name}\',item)\"\n        v-on:valuechange=\"valuechange(\'${entName}\',\'${field.name}\',item)\"\n        length=\"${field.length}\" from=\"input\"\n    </#if>\n</#macro>\n\n\n', `TYPE_` = 'pc', `CATEGORY_` = 'field', `TENANT_ID_` = '0', `CREATE_DEP_ID_` = '2', `CREATE_BY_` = '1', `CREATE_TIME_` = '2020-06-28 11:25:34', `UPDATE_BY_` = '', `UPDATE_TIME_` = '2022-03-09 15:07:46', `APP_ID_` = NULL, `COMPANY_ID_` = NULL, `SHARE_` = NULL WHERE `ID_` = '1277080670808162306';

-- 2022-03-09  jpaas_seata   gjh  seata加status字段
-- ALTER TABLE jpaas_seata.lock_table ADD COLUMN  status VARCHAR(64);

-- 2022-03-22  jpaas_system   gjh  新增机构标签数据字典分类
INSERT INTO `jpaas_system`.`sys_tree`(`TREE_ID_`, `CODE_`, `NAME_`, `PATH_`, `PARENT_ID_`, `ALIAS_`, `DESCP_`, `CAT_KEY_`, `SN_`, `DATA_SHOW_TYPE_`, `TENANT_ID_`, `CREATE_DEP_ID_`, `CREATE_BY_`, `CREATE_TIME_`, `UPDATE_BY_`, `UPDATE_TIME_`, `COMPANY_ID_`, `APP_ID_`) VALUES ('1506093829300498434', NULL, '机构标签', '0.1506093829300498434.', '0', 'instLabel', NULL, 'DIC', 1, 'FLAT', '1', '1', '1', '2022-03-22 10:22:05', NULL, '2022-03-22 10:22:05', NULL, '');
-- 2022-03-22  jpaas_user   gjh 新增机构标签字段
ALTER TABLE jpaas_user.OS_INST ADD COLUMN  LABEL_ VARCHAR(64)  COMMENT '标签';
-- 2022-03-22  jpaas_form   gjh 列表新增机构列头配置
ALTER TABLE jpaas_form.FORM_BO_LIST ADD COLUMN  INST_COLUMN_CONFIG_ TEXT COMMENT '机构列头配置';
-- 2022-03-25  jpaas_system   gjh 菜单新增机构个性化配置
ALTER TABLE jpaas_system.sys_menu ADD COLUMN  INST_CONFIG_ TEXT COMMENT '机构个性化配置';

-- 2022-04-08  gjh 新增数据源同步表单
ALTER TABLE jpaas_user.os_inst ADD COLUMN  DATASOURCE_ VARCHAR(64) COMMENT '数据源';
ALTER TABLE jpaas_form.form_bo_entity ADD COLUMN IS_TENANT_ VARCHAR(20) COMMENT '是否为租户使用';
ALTER TABLE jpaas_form.form_datasource_def ADD COLUMN IS_TENANT_ VARCHAR(20) COMMENT '是否为租户使用';
ALTER TABLE jpaas_form.form_datasource_def ADD COLUMN CHANGE_SN_ int(20) COMMENT '变更记录序号';
CREATE TABLE jpaas_form.FORM_CHANGE_LOG
(
    ID_ varchar(64)  NOT NULL COMMENT '主键',
    BO_ALIAS_ varchar(64)  DEFAULT NULL COMMENT 'BO实体别名',
    BO_NAME_ varchar(64)  DEFAULT NULL COMMENT 'BO实体名称',
    SQL_ varchar(1000)  DEFAULT NULL COMMENT 'SQL语句',
    TYPE_ varchar(64)  DEFAULT NULL COMMENT '类型',
    SN_ int(11)  DEFAULT NULL COMMENT '序号',
    TENANT_ID_ varchar(64)  DEFAULT NULL COMMENT '租用用户Id',
    CREATE_DEP_ID_ varchar(64)  DEFAULT NULL COMMENT '创建部门ID',
    CREATE_BY_ varchar(64)  DEFAULT NULL COMMENT '创建人ID',
    CREATE_TIME_ datetime DEFAULT NULL COMMENT '创建时间',
    UPDATE_BY_ varchar(64)  DEFAULT NULL COMMENT '更新人ID',
    UPDATE_TIME_ datetime DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (ID_)
);
ALTER TABLE jpaas_form.FORM_CHANGE_LOG COMMENT '表单变更记录';

CREATE TABLE jpaas_form.FORM_EXECUTE_LOG
(
    ID_ varchar(64)  NOT NULL COMMENT '主键',
    CHANGE_LOG_ID_ varchar(64)  DEFAULT NULL COMMENT '表单变更记录ID',
    DATASOURCE_ varchar(64)  DEFAULT NULL COMMENT '数据源别名',
    SQL_ varchar(1000)  DEFAULT NULL COMMENT '执行的SQL语句',
    STATUS_ varchar(20)  DEFAULT NULL COMMENT '状态(1:成功 0:失败 -1:忽略)',
    RECORD_ text  DEFAULT NULL COMMENT '记录',
    TENANT_ID_ varchar(64)  DEFAULT NULL COMMENT '租用用户Id',
    CREATE_DEP_ID_ varchar(64)  DEFAULT NULL COMMENT '创建部门ID',
    CREATE_BY_ varchar(64)  DEFAULT NULL COMMENT '创建人ID',
    CREATE_BY_NAME_ varchar(64)  DEFAULT NULL COMMENT '创建人名称',
    CREATE_TIME_ datetime DEFAULT NULL COMMENT '创建时间',
    UPDATE_BY_ varchar(64)  DEFAULT NULL COMMENT '更新人ID',
    UPDATE_TIME_ datetime DEFAULT NULL COMMENT '更新时间',
    BATCH_ varchar(64)  DEFAULT NULL COMMENT '批次',
    PRIMARY KEY (ID_)
);
ALTER TABLE jpaas_form.FORM_EXECUTE_LOG COMMENT '租户数据源执行记录';

INSERT INTO `jpaas_system`.`sys_menu`(`MENU_ID_`, `APP_ID_`, `NAME_`, `ICON_PC_`, `ICON_PIC_`, `ICON_APP_`, `PARENT_ID_`, `PATH_`, `SN_`, `SHOW_TYPE_`, `MENU_KEY_`, `MENU_TYPE_`, `COMPONENT_`, `SETTING_TYPE_`, `BO_LIST_KEY_`, `URL_`, `METHOD_`, `PARAMS_`, `TENANT_ID_`, `CREATE_DEP_ID_`, `CREATE_BY_`, `CREATE_TIME_`, `UPDATE_BY_`, `UPDATE_TIME_`, `COMPANY_ID_`, `SHARE_`, `INST_CONFIG_`) VALUES ('1510097722216865794', '1216010678984933378', '同步执行记录', '{\"type\":\"customIcon\",\"icon\":\"iconfile-protect\"}', 'iconfile-protect', NULL, '1511976729900240897', '0.1511976729900240897.1510097722216865794.', 2, 'URL', 'FormExecuteLogList.vue', 'C', 'modules/form/core/FormExecuteLogList.vue', 'custom', NULL, NULL, NULL, NULL, '1', '1', '1', '2022-04-02 11:32:08', '1', '2022-04-07 16:01:42', NULL, 'N', NULL);
INSERT INTO `jpaas_system`.`sys_menu`(`MENU_ID_`, `APP_ID_`, `NAME_`, `ICON_PC_`, `ICON_PIC_`, `ICON_APP_`, `PARENT_ID_`, `PATH_`, `SN_`, `SHOW_TYPE_`, `MENU_KEY_`, `MENU_TYPE_`, `COMPONENT_`, `SETTING_TYPE_`, `BO_LIST_KEY_`, `URL_`, `METHOD_`, `PARAMS_`, `TENANT_ID_`, `CREATE_DEP_ID_`, `CREATE_BY_`, `CREATE_TIME_`, `UPDATE_BY_`, `UPDATE_TIME_`, `COMPANY_ID_`, `SHARE_`, `INST_CONFIG_`) VALUES ('1511893459128832002', '1216010678984933378', '表单同步', '{\"type\":\"customIcon\",\"icon\":\"iconshujudaochu\"}', 'iconshujudaochu', NULL, '1511976729900240897', '0.1511976729900240897.1511893459128832002.', 1, 'URL', 'FormChangeLogs', 'C', 'modules/form/core/FormChangeLogs.vue', 'custom', NULL, NULL, NULL, NULL, '1', '1', '1', '2022-04-07 10:27:45', '1', '2022-04-08 11:27:44', NULL, 'N', NULL);
INSERT INTO `jpaas_system`.`sys_menu`(`MENU_ID_`, `APP_ID_`, `NAME_`, `ICON_PC_`, `ICON_PIC_`, `ICON_APP_`, `PARENT_ID_`, `PATH_`, `SN_`, `SHOW_TYPE_`, `MENU_KEY_`, `MENU_TYPE_`, `COMPONENT_`, `SETTING_TYPE_`, `BO_LIST_KEY_`, `URL_`, `METHOD_`, `PARAMS_`, `TENANT_ID_`, `CREATE_DEP_ID_`, `CREATE_BY_`, `CREATE_TIME_`, `UPDATE_BY_`, `UPDATE_TIME_`, `COMPANY_ID_`, `SHARE_`, `INST_CONFIG_`) VALUES ('1511976729900240897', '1216010678984933378', '数据源表单同步', '{\"type\":\"customIcon\",\"icon\":\"iconxinwendongtaixuanzhong\"}', 'iconxinwendongtaixuanzhong', NULL, '0', '0.1511976729900240897.', 11, 'URL', 'syncForm', 'C', NULL, 'custom', NULL, NULL, NULL, NULL, '1', '1', '1', '2022-04-07 15:58:38', NULL, '2022-04-07 15:58:38', NULL, 'N', NULL);
INSERT INTO `jpaas_user`.`os_inst_type_menu`(`ID_`, `INST_TYPE_ID_`, `APP_ID_`, `MENU_ID_`, `NAME_`, `ICON_PC_`, `ICON_PIC_`, `ICON_APP_`, `PARENT_ID_`, `PATH_`, `SN_`, `SHOW_TYPE_`, `MENU_KEY_`, `MENU_TYPE_`, `COMPONENT_`, `SETTING_TYPE_`, `BO_LIST_KEY_`, `URL_`, `METHOD_`, `PARAMS_`, `TENANT_ID_`, `CREATE_DEP_ID_`, `CREATE_BY_`, `CREATE_TIME_`, `UPDATE_BY_`, `UPDATE_TIME_`) VALUES ('1511977221820817597', '1', '1216010678984933378', '1511976729900240897', '数据源表单同步', '{\"type\":\"customIcon\",\"icon\":\"iconxinwendongtaixuanzhong\"}', 'iconxinwendongtaixuanzhong', NULL, '1216010678984933378', NULL, 11, 'URL', 'syncForm', 'C', NULL, 'custom', NULL, NULL, NULL, NULL, '1', '1', '1', '2022-04-07 07:58:38', NULL, '2022-04-07 07:58:38');
-- 2022-04-06  jpaas-system hj web请求定义新增是否记录日志字段
ALTER TABLE jpaas_system.sys_webreq_def ADD COLUMN  IS_LOG_ varchar(10) COMMENT '是否记录日志';
-- 修改第三方接口日志菜单
UPDATE jpaas_system.sys_menu SET COMPONENT_='modules/system/core/SysHttpTaskList.vue',NAME_='外围系统回调日志' WHERE MENU_ID_='1296014599562817538';
UPDATE jpaas_user.os_inst_type_menu SET COMPONENT_='modules/system/core/SysHttpTaskList.vue',NAME_='外围系统回调日志' WHERE MENU_ID_='1296014599562817538';
UPDATE jpaas_user.os_group_menu SET COMPONENT_='modules/system/core/SysHttpTaskList.vue',NAME_='外围系统回调日志' WHERE MENU_ID_='1296014599562817538';
-- 修改第三方接口日志表名sys_http_task_log
ALTER  TABLE jpaas_system.sys_interface_call_logs RENAME TO jpaas_system.sys_http_task_log;
-- 修改第三方接口日志表字段TASK_ID_
ALTER TABLE jpaas_system.sys_http_task_log CHANGE INTERFACE_ID_ TASK_ID_ VARCHAR(64) COMMENT '任务ID';
-- 增加日志结果字段
ALTER TABLE jpaas_system.sys_http_task_log  ADD COLUMN `RESULT_`  varchar(10) NULL COMMENT '日志结果' ;
-- 创建接口调用表
CREATE TABLE jpaas_system.SYS_HTTP_TASK (
    ID_ varchar(64)  NOT NULL COMMENT '主键',
    TYPE_ varchar(20) DEFAULT NULL COMMENT '类型',
    REL_ID_ varchar(64) DEFAULT NULL COMMENT '关联ID',
    REL_NAME_ varchar(200) DEFAULT NULL COMMENT '关联名称',
    BEAN_NAME_ varchar(64)  DEFAULT NULL COMMENT '类名',
    METHOD_ varchar(64)  DEFAULT NULL COMMENT '方法名',
    PARAMS_ text  DEFAULT NULL COMMENT '方法参数',
    STATUS_ VARCHAR(20) DEFAULT NULL COMMENT '状态',
    EXECUTE_TIMES_ INT DEFAULT NULL COMMENT '执行次数',
    MAX_ATTEMPTS_ INT DEFAULT NULL COMMENT '重试次数',
    DELAY_ INT DEFAULT NULL COMMENT '重试时间间隔',
    MULTIPLIER_ decimal(14,2) DEFAULT NULL COMMENT '延迟倍率',
    TENANT_ID_ varchar(64)  DEFAULT NULL COMMENT '租用用户Id',
    CREATE_DEP_ID_ varchar(64)  DEFAULT NULL COMMENT '创建部门ID',
    CREATE_BY_ varchar(64)  DEFAULT NULL COMMENT '创建人ID',
    CREATE_TIME_ datetime DEFAULT NULL COMMENT '创建时间',
    UPDATE_BY_ varchar(64)  DEFAULT NULL COMMENT '更新人ID',
    UPDATE_TIME_ datetime DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (ID_)
);
ALTER TABLE jpaas_system.SYS_HTTP_TASK COMMENT '接口调用表';

INSERT INTO jpaas_job.xxl_job_group (id, app_name, title, order_, address_type, address_list)
        VALUES ('1385486577859444739', 'system-job', '系统执行器',1, 0, null);

INSERT INTO jpaas_job.xxl_job_info(id, job_group, job_cron, job_desc, add_time, update_time, author, alarm_email, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time)
        VALUES ('1385489751253073923', '1385486577859444739', '* /5 * * * ?', '执行失败任务', '2021-3-2 14:11:40', '2021-3-2 16:57:46', 'admin', '', 'FIRST', 'sysHttpTaskHandler', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2021-3-2 14:11:40', '', 0, 0, 0);

-- 2022-04-08  yl job脚本更新
ALTER TABLE jpaas_job.xxl_job_group ADD COLUMN  update_time datetime COMMENT '更新时间';
ALTER TABLE jpaas_job.xxl_job_info ADD COLUMN  schedule_type varchar(64) COMMENT '调度类型';
ALTER TABLE jpaas_job.xxl_job_info ADD COLUMN  schedule_conf varchar(128) COMMENT '调度配置，值含义取决于调度类型';
ALTER TABLE jpaas_job.xxl_job_info ADD COLUMN  misfire_strategy varchar(64) COMMENT '调度过期策略';

-- 2022-04-13 yl 大屏脚本更新
update jpaas_system.sys_menu set params_="/screen/#/panel/index" where  menu_id_ = '1472821869619146754';
update jpaas_system.sys_menu set params_="/screen/#/chart/index" where  menu_id_ = '1472822224515985409';
update jpaas_system.sys_menu set params_="/screen/#/dataset/index" where  menu_id_ = '1472822342455619586';
update jpaas_system.sys_menu set params_="/screen/#/datasource/index" where  menu_id_ = '1472822555371073537';

-- 2022-04-22 jpaas-form hj 角色数据权限支持树形数据
ALTER TABLE jpaas_form.form_entity_data_type
ADD COLUMN DATA_SHOW_TYPE_ VARCHAR(20)  COMMENT '展示方式';

ALTER TABLE jpaas_form.form_entity_data_setting_dic
ADD COLUMN PARENT_VALUE_ VARCHAR(64)  COMMENT '父内容',
ADD COLUMN PATH_ VARCHAR(256) COMMENT '路径',
ADD COLUMN SN_ int COMMENT '序号';