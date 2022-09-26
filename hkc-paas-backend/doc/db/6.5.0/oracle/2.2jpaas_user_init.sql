INSERT INTO os_user(ADDRESS_,BIRTHDAY_,CREATE_BY_,CREATE_DEP_ID_,CREATE_TIME_,DD_ID_,EMAIL_,ENABLED_,ENTRY_TIME_,FROM_,FULLNAME_,MOBILE_,OPEN_ID_,PHOTO_,PWD_,QQ_,QUIT_TIME_,SEX_,STATUS_,SYNC_WX_,TENANT_ID_,UPDATE_BY_,UPDATE_TIME_,URGENT_,URGENT_MOBILE_,USER_ID_,USER_NO_,USER_TYPE_,WX_OPEN_ID_)
       values(         '',         null,         '',         '',         to_date('2019-11-9 0:00:00','yyyy-MM-dd HH24:mi:ss'),         null,         '994042808@qq.com',         'YES',         to_date('2019-10-30','yyyy-MM-dd HH24:mi:ss'),         '',         '管理员',         '',         '',         '',         '$2a$10$TJkwVdlpbHKnV45.nBxbgeFHmQRmyWlshg94lFu2rKxVtT2OMniDO',         '',         null,         '',         '1',         null,         '1',         '1',         to_date('2020-4-9','yyyy-MM-dd HH24:mi:ss'),         '',         '',         '1',         'admin',         '',         null);

INSERT INTO os_dimension(CODE_,CREATE_BY_,CREATE_DEP_ID_,CREATE_TIME_,DESC_,DIM_ID_,IS_GRANT_,IS_SYSTEM_,NAME_,SHOW_TYPE_,SN_,STATUS_,TENANT_ID_,UPDATE_BY_,UPDATE_TIME_)
       values(         '_ADMIN',         '1',         '2',         to_date('2020-1-16 0:00:00','yyyy-MM-dd HH24:mi:ss'),         '管理用户的公司与部门',         '1',         'YES',         'YES',         '行政组织',         'TREE',         1,         'ENABLED',         '0',         '1',         to_date('2020-1-20','yyyy-MM-dd HH24:mi:ss'));

INSERT INTO os_dimension(CODE_,CREATE_BY_,CREATE_DEP_ID_,CREATE_TIME_,DESC_,DIM_ID_,IS_GRANT_,IS_SYSTEM_,NAME_,SHOW_TYPE_,SN_,STATUS_,TENANT_ID_,UPDATE_BY_,UPDATE_TIME_)
       values(         '_ROLE',         '1',         '',         to_date('2021-1-18 0:00:00','yyyy-MM-dd HH24:mi:ss'),         '系统的用户角色',         '2',         'YES',         'YES',         '角色',         'FLAT',         2,         'ENABLED',         '0',         '1',         to_date('2020-3-17','yyyy-MM-dd HH24:mi:ss'));

INSERT INTO os_dimension(CODE_,CREATE_BY_,CREATE_DEP_ID_,CREATE_TIME_,DESC_,DIM_ID_,IS_GRANT_,IS_SYSTEM_,NAME_,SHOW_TYPE_,SN_,STATUS_,TENANT_ID_,UPDATE_BY_,UPDATE_TIME_)
       values(         '_POS',         '1',         '',         to_date('2021-1-18 0:00:00','yyyy-MM-dd HH24:mi:ss'),         '管理用户的职务',         '3',         'YES',         'YES',         '职务',         'FLAT',         3,         'ENABLED',         '0',         '1',         to_date('2020-3-17','yyyy-MM-dd HH24:mi:ss'));

INSERT INTO os_rel_type(CONST_TYPE_,CREATE_BY_,CREATE_DEP_ID_,CREATE_TIME_,DIM_ID1_,DIM_ID2_,ID_,IS_DEFAULT_,IS_SYSTEM_,IS_TWO_WAY_,KEY_,LEVEL_,MEMO_,NAME_,PARTY1_,PARTY2_,REL_TYPE_,STATUS_,TENANT_ID_,UPDATE_BY_,UPDATE_TIME_)
       values(         'ONE-MANY',         '1',         '1',         to_date('2020-6-18 0:00:00','yyyy-MM-dd HH24:mi:ss'),         '',         '',         '1',         'YES',         'NO',         'YES',         'GROUP-USER-BELONG',         null,         '用户从属于用户组的关系',         '组内用户',         'GROUP',         'USER',         'GROUP-USER',         'ENABLED',         '0',         '1',         to_date('2020-6-18','yyyy-MM-dd HH24:mi:ss'));

INSERT INTO os_rel_type(CONST_TYPE_,CREATE_BY_,CREATE_DEP_ID_,CREATE_TIME_,DIM_ID1_,DIM_ID2_,ID_,IS_DEFAULT_,IS_SYSTEM_,IS_TWO_WAY_,KEY_,LEVEL_,MEMO_,NAME_,PARTY1_,PARTY2_,REL_TYPE_,STATUS_,TENANT_ID_,UPDATE_BY_,UPDATE_TIME_)
       values(         'ONE-MANY',         '',         '',         null,         '1',         '',         '2',         'YES',         'YES',         'NO',         'GROUP-USER-LEADER',         null,         '用户作为组的负责人',         '组负责人',         'GROUP',         'LEADER',         'GROUP-USER',         'ENABLED',         '0',         '1',         null);

INSERT INTO os_rel_type(CONST_TYPE_,CREATE_BY_,CREATE_DEP_ID_,CREATE_TIME_,DIM_ID1_,DIM_ID2_,ID_,IS_DEFAULT_,IS_SYSTEM_,IS_TWO_WAY_,KEY_,LEVEL_,MEMO_,NAME_,PARTY1_,PARTY2_,REL_TYPE_,STATUS_,TENANT_ID_,UPDATE_BY_,UPDATE_TIME_)
       values(         'ONE-ONE',         '',         '',         null,         '',         '',         '3',         'YES',         'YES',         'NO',         'USER-UP-LOWER',         null,         '用户的下属',         '上下级',         'UPPER',         'LOWER',         'USER-USER',         'ENABLED',         '0',         '1',         null);

INSERT INTO os_rel_type(CONST_TYPE_,CREATE_BY_,CREATE_DEP_ID_,CREATE_TIME_,DIM_ID1_,DIM_ID2_,ID_,IS_DEFAULT_,IS_SYSTEM_,IS_TWO_WAY_,KEY_,LEVEL_,MEMO_,NAME_,PARTY1_,PARTY2_,REL_TYPE_,STATUS_,TENANT_ID_,UPDATE_BY_,UPDATE_TIME_)
       values(         'ONE-ONE',         '1',         '2',         to_date('2020-2-25 0:00:00','yyyy-MM-dd HH24:mi:ss'),         '1',         '3',         '4',         'YES',         'YES',         'NO',         'GROUP-DEP-POS',         null,         '部门下的职位',         '岗位',         'DEPARTMENT',         'POSITION',         'GROUP-GROUP',         'ENABLED',         '0',         '',         to_date('2020-2-25','yyyy-MM-dd HH24:mi:ss'));

INSERT INTO os_rel_type(CONST_TYPE_,CREATE_BY_,CREATE_DEP_ID_,CREATE_TIME_,DIM_ID1_,DIM_ID2_,ID_,IS_DEFAULT_,IS_SYSTEM_,IS_TWO_WAY_,KEY_,LEVEL_,MEMO_,NAME_,PARTY1_,PARTY2_,REL_TYPE_,STATUS_,TENANT_ID_,UPDATE_BY_,UPDATE_TIME_)
       values(         'ONE-MANY',         '',         '',         null,         '',         '',         '5',         'YES',         'YES',         'NO',         'REL-USER-BELONG',         null,         '用户组之间关系下的用户',         '组关系用户',         'REL',         'USER',         'REL-USER',         'ENABLED',         '0',         '',         null);

INSERT INTO os_group(AREA_CODE_,CREATE_BY_,CREATE_DEP_ID_,CREATE_TIME_,DD_ID_,DD_PARENT_ID_,DESCP_,DIM_ID_,FORM_,GROUP_ID_,IS_DEFAULT_,IS_LEAF_,KEY_,NAME_,PARENT_ID_,PATH_,RANK_LEVEL_,SN_,STATUS_,SYNC_WX_,TENANT_ID_,UPDATE_BY_,UPDATE_TIME_,WX_PARENT_PID_,WX_PID_)
       values(         '',         '',         '1',         null,         null,         null,         '集团',         '1',         '0.1.',         '1',         '',         '0',         'RX-GROUP',         '智慧集团',         '0',         '0.1.',         1,         1,         'ENABLED',         1,         '1',         '1',         to_date('2020-6-19','yyyy-MM-dd HH24:mi:ss'),         null,         0);

INSERT INTO os_rank_type(CREATE_BY_,CREATE_DEP_ID_,CREATE_TIME_,DIM_ID_,KEY_,LEVEL_,NAME_,RK_ID_,TENANT_ID_,UPDATE_BY_,UPDATE_TIME_)
       values(         '1',         '1',         to_date('2020-6-17 0:00:00','yyyy-MM-dd HH24:mi:ss'),         '1',         'GROUP',         1,         '集团',         '1273143346803912706',         '1',         '',         to_date('2020-6-17','yyyy-MM-dd HH24:mi:ss'));

INSERT INTO os_rank_type(CREATE_BY_,CREATE_DEP_ID_,CREATE_TIME_,DIM_ID_,KEY_,LEVEL_,NAME_,RK_ID_,TENANT_ID_,UPDATE_BY_,UPDATE_TIME_)
       values(         '1',         '1',         to_date('2020-6-17 0:00:00','yyyy-MM-dd HH24:mi:ss'),         '1',         'COMPANY',         2,         '分公司',         '1273143418476179458',         '1',         '',         to_date('2020-6-17','yyyy-MM-dd HH24:mi:ss'));

INSERT INTO os_rank_type(CREATE_BY_,CREATE_DEP_ID_,CREATE_TIME_,DIM_ID_,KEY_,LEVEL_,NAME_,RK_ID_,TENANT_ID_,UPDATE_BY_,UPDATE_TIME_)
       values(         '1',         '1',         to_date('2020-6-17 0:00:00','yyyy-MM-dd HH24:mi:ss'),         '1',         'DEPARTMENT',         3,         '部门',         '1273143492459507713',         '1',         '',         to_date('2020-6-17','yyyy-MM-dd HH24:mi:ss'));

INSERT INTO os_rank_type(CREATE_BY_,CREATE_DEP_ID_,CREATE_TIME_,DIM_ID_,KEY_,LEVEL_,NAME_,RK_ID_,TENANT_ID_,UPDATE_BY_,UPDATE_TIME_)
       values(         '1',         '1',         to_date('2020-6-17 0:00:00','yyyy-MM-dd HH24:mi:ss'),         '1',         'GP',         4,         '组',         '1273143619920211970',         '1',         '',         to_date('2020-6-17','yyyy-MM-dd HH24:mi:ss'));

INSERT INTO os_rel_inst(ALIAS_,CREATE_BY_,CREATE_DEP_ID_,CREATE_TIME_,DIM1_,DIM2_,INST_ID_,IS_MAIN_,PARTY1_,PARTY2_,PATH_,REL_TYPE_,REL_TYPE_ID_,REL_TYPE_KEY_,STATUS_,TENANT_ID_,UPDATE_BY_,UPDATE_TIME_)
       values(         null,         null,         null,         null,         '1',         null,         '1232134235423452233',         'YES',         '1',         '1',         '0.1.1',         'GROUP-USER',         '1',         'GROUP-USER-BELONG',         'ENABLED',         '1',         null,         null);

INSERT INTO os_rel_inst(ALIAS_,CREATE_BY_,CREATE_DEP_ID_,CREATE_TIME_,DIM1_,DIM2_,INST_ID_,IS_MAIN_,PARTY1_,PARTY2_,PATH_,REL_TYPE_,REL_TYPE_ID_,REL_TYPE_KEY_,STATUS_,TENANT_ID_,UPDATE_BY_,UPDATE_TIME_)
       values(         null,         '1',         null,         to_date('2021-1-19 0:00:00','yyyy-MM-dd HH24:mi:ss'),         '1',         null,         '1351466820325920770',         'YES',         '1351466819906490369',         '1351466819931656193',         '0.1351466819906490369.1351466819931656193.',         'GROUP-USER',         '1',         'GROUP-USER-BELONG',         'ENABLED',         '1351466819793244161',         null,         to_date('2021-1-19','yyyy-MM-dd HH24:mi:ss'));

INSERT INTO os_rel_inst(ALIAS_,CREATE_BY_,CREATE_DEP_ID_,CREATE_TIME_,DIM1_,DIM2_,INST_ID_,IS_MAIN_,PARTY1_,PARTY2_,PATH_,REL_TYPE_,REL_TYPE_ID_,REL_TYPE_KEY_,STATUS_,TENANT_ID_,UPDATE_BY_,UPDATE_TIME_)
       values(         null,         '1',         '1',         to_date('2021-7-7 19:13:05','yyyy-MM-dd HH24:mi:ss'),         '1',         null,         '1412731388254785538',         'YES',         '1273526098128740353',         '1273526443793915905',         '0.1273526098128740353.1273526443793915905.',         'GROUP-USER',         '1',         'GROUP-USER-BELONG',         'ENABLED',         '1',         null,         to_date('2021-7-7','yyyy-MM-dd HH24:mi:ss'));

INSERT INTO os_rel_inst(ALIAS_,CREATE_BY_,CREATE_DEP_ID_,CREATE_TIME_,DIM1_,DIM2_,INST_ID_,IS_MAIN_,PARTY1_,PARTY2_,PATH_,REL_TYPE_,REL_TYPE_ID_,REL_TYPE_KEY_,STATUS_,TENANT_ID_,UPDATE_BY_,UPDATE_TIME_)
       values(         null,         '1',         '1',         to_date('2021-7-7 19:13:18','yyyy-MM-dd HH24:mi:ss'),         '1',         null,         '1412731443242110977',         'YES',         '1273526190164353025',         '1273499857644363777',         '0.1273526190164353025.1273499857644363777.',         'GROUP-USER',         '1',         'GROUP-USER-BELONG',         'ENABLED',         '1',         null,         to_date('2021-7-7','yyyy-MM-dd HH24:mi:ss'));

INSERT INTO os_rel_inst(ALIAS_,CREATE_BY_,CREATE_DEP_ID_,CREATE_TIME_,DIM1_,DIM2_,INST_ID_,IS_MAIN_,PARTY1_,PARTY2_,PATH_,REL_TYPE_,REL_TYPE_ID_,REL_TYPE_KEY_,STATUS_,TENANT_ID_,UPDATE_BY_,UPDATE_TIME_)
       values(         null,         '1',         '1',         to_date('2021-9-9 11:25:55','yyyy-MM-dd HH24:mi:ss'),         '1',         null,         '1435806645995786243',         'YES',         '1',         '1435806645895122946',         '0.1.1435806645895122946.',         'GROUP-USER',         '1',         'GROUP-USER-BELONG',         'ENABLED',         '1',         null,         to_date('2021-9-9','yyyy-MM-dd HH24:mi:ss'));

INSERT INTO os_rel_inst(ALIAS_,CREATE_BY_,CREATE_DEP_ID_,CREATE_TIME_,DIM1_,DIM2_,INST_ID_,IS_MAIN_,PARTY1_,PARTY2_,PATH_,REL_TYPE_,REL_TYPE_ID_,REL_TYPE_KEY_,STATUS_,TENANT_ID_,UPDATE_BY_,UPDATE_TIME_)
       values(         null,         '1',         '1',         to_date('2021-9-9 11:34:05','yyyy-MM-dd HH24:mi:ss'),         '1',         null,         '1435808701464793090',         'YES',         '2',         '1435808701401878530',         '0.2.1435808701401878530.',         'GROUP-USER',         '1',         'GROUP-USER-BELONG',         'ENABLED',         '1',         null,         to_date('2021-9-9','yyyy-MM-dd HH24:mi:ss'));

INSERT INTO os_rel_inst(ALIAS_,CREATE_BY_,CREATE_DEP_ID_,CREATE_TIME_,DIM1_,DIM2_,INST_ID_,IS_MAIN_,PARTY1_,PARTY2_,PATH_,REL_TYPE_,REL_TYPE_ID_,REL_TYPE_KEY_,STATUS_,TENANT_ID_,UPDATE_BY_,UPDATE_TIME_)
       values(         null,         '1',         '1',         to_date('2021-9-9 13:49:45','yyyy-MM-dd HH24:mi:ss'),         '1',         null,         '1435842842780585988',         'YES',         '1273526098128740353',         '1435842842780585986',         '0.1273526098128740353.1435842842780585986.',         'GROUP-USER',         '1',         'GROUP-USER-BELONG',         'ENABLED',         '1',         null,         to_date('2021-9-9','yyyy-MM-dd HH24:mi:ss'));


INSERT INTO os_inst(ADDRESS_,BUS_LICE_NO_,CONTRACTOR_,CREATE_BY_,CREATE_DEP_ID_,CREATE_TIME_,DESCP_,DOMAIN_,EMAIL_,FAX_,HOME_URL_,INST_ID_,INST_NO_,INST_TYPE_,LEGAL_MAN_,NAME_CN_,NAME_CN_S_,NAME_EN_,NAME_EN_S_,PARENT_ID_,PATH_,PHONE_,STATUS_,UPDATE_BY_,UPDATE_TIME_)
       values(         '',         'SMART',         '陈先生',         '1',         '1',         to_date('2020-6-18 0:00:00','yyyy-MM-dd HH24:mi:ss'),         '',         'redxun.cn',         'keitch@redxun.cn',         '020-29026352',         '广东省广州市海珠区庭园路163号4楼401',         '1',         '001',         '1',         '张成功',         '智慧集团',         '',         'ZHIHUI',         '',         '',         '',         '020-29026351',         'ENABLED',         '',         to_date('2020-6-18','yyyy-MM-dd HH24:mi:ss'));


INSERT INTO os_inst_type(CREATE_BY_,CREATE_DEP_ID_,CREATE_TIME_,DESCP_,ENABLED_,HOME_URL_,IS_DEFAULT_,TENANT_ID_,TYPE_CODE_,TYPE_ID_,TYPE_NAME_,UPDATE_BY_,UPDATE_TIME_)
       values(         '1',         '1',         to_date('2020-6-18 0:00:00','yyyy-MM-dd HH24:mi:ss'),         '定义机构',         'Y',         '',         'Y',         '1',         'PLATFORM',         '1',         '平台机构',         '',         to_date('2020-6-18','yyyy-MM-dd HH24:mi:ss'));

INSERT INTO os_inst_type(CREATE_BY_,CREATE_DEP_ID_,CREATE_TIME_,DESCP_,ENABLED_,HOME_URL_,IS_DEFAULT_,TENANT_ID_,TYPE_CODE_,TYPE_ID_,TYPE_NAME_,UPDATE_BY_,UPDATE_TIME_)
       values(         '1',         '1',         to_date('2021-1-18 0:00:00','yyyy-MM-dd HH24:mi:ss'),         '通用机构',         'Y',         null,         'N',         '1',         'COMMON',         '1273437052912189443',         '通用机构',         null,         to_date('2021-1-18','yyyy-MM-dd HH24:mi:ss'));

INSERT INTO os_inst_users(CREATE_BY_,CREATE_DEP_ID_,CREATE_TIME_,ID_,IS_ADMIN_,TENANT_ID_,UPDATE_BY_,UPDATE_TIME_,USER_ID_,USER_TYPE_,APPLY_STATUS_)
       values(         '1',         null,         to_date('2021-1-18 0:00:00','yyyy-MM-dd HH24:mi:ss'),         '1',         1,         '1',         '1',         to_date('2021-1-18','yyyy-MM-dd HH24:mi:ss'),         '1',         null,   'ENABLED');


INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1257928508099674114',         '1364462870689144843',         '1',         '1364462564484026369');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980200670081026',         '1364462870689144987',         '1',         '1364462772253069314');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1305412680270073858',         '1364462932626432001',         '1273437052912189443',         '1305412680270073858');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1257928508099674114',         '1364462932626432002',         '1273437052912189443',         '1257928508099674114');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1257928508099674114',         '1364462932626432003',         '1273437052912189443',         '1258697656622858242');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1257928508099674114',         '1364462932626432004',         '1273437052912189443',         '1290200582640492545');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1257928508099674114',         '1364462932626432005',         '1273437052912189443',         '1290200644439367682');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1257928508099674114',         '1364462932626432006',         '1273437052912189443',         '1290200764849446914');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1257928508099674114',         '1364462932626432007',         '1273437052912189443',         '1290200826270834690');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1257928508099674114',         '1364462932626432008',         '1273437052912189443',         '1290201109801590785');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1257928508099674114',         '1364462932626432009',         '1273437052912189443',         '1364462564484026369');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1257928508099674114',         '1364462932626432010',         '1273437052912189443',         '1283422020083683329');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1257928508099674114',         '1364462932626432011',         '1273437052912189443',         '1290200432228556802');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1257928508099674114',         '1364462932626432012',         '1273437052912189443',         '1283427147565797377');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1257928508099674114',         '1364462932626432013',         '1273437052912189443',         '1290201031812702209');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1257928508099674114',         '1364462932626432014',         '1273437052912189443',         '1290200178347335682');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1257928508099674114',         '1364462932626432015',         '1273437052912189443',         '1258697973154398209');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1257928508099674114',         '1364462932626432016',         '1273437052912189443',         '1258698215539032066');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1257928508099674114',         '1364462932626432017',         '1273437052912189443',         '1260784115450310658');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1257928508099674114',         '1364462932626432018',         '1273437052912189443',         '1271372098643738626');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1257928508099674114',         '1364462932626432019',         '1273437052912189443',         '1260854501005103105');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1257928508099674114',         '1364462932626432020',         '1273437052912189443',         '1252503826125074433');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1257928508099674114',         '1364462932626432021',         '1273437052912189443',         '1271372406870556673');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1211584063094243329',         '1364462932626432022',         '1273437052912189443',         '1211584063094243329');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1211584063094243329',         '1364462932626432023',         '1273437052912189443',         '1232217594178830338');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1211584063094243329',         '1364462932626432024',         '1273437052912189443',         '1263004299056492546');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1211584063094243329',         '1364462932626432025',         '1273437052912189443',         '1264835010617008129');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1211584063094243329',         '1364462932626432026',         '1273437052912189443',         '1266292053827657729');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1211584063094243329',         '1364462932626432027',         '1273437052912189443',         '1266316421131673601');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1211584063094243329',         '1364462932626432028',         '1273437052912189443',         '1266316643584974850');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1211584063094243329',         '1364462932626432029',         '1273437052912189443',         '1232223000892538881');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1211584063094243329',         '1364462932626432030',         '1273437052912189443',         '1264835093727141890');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1211584063094243329',         '1364462932626432031',         '1273437052912189443',         '1266292134257631233');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1211584063094243329',         '1364462932626432032',         '1273437052912189443',         '1266316494125146113');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1211584063094243329',         '1364462932626432033',         '1273437052912189443',         '1266316741412921346');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1211584063094243329',         '1364462932626432034',         '1273437052912189443',         '1235770029288288257');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1211584063094243329',         '1364462932626432035',         '1273437052912189443',         '1264835219484958721');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1211584063094243329',         '1364462932626432036',         '1273437052912189443',         '1266292200984813570');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1211584063094243329',         '1364462932626432037',         '1273437052912189443',         '1266316568800534529');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1211584063094243329',         '1364462932626432038',         '1273437052912189443',         '1266317190513827842');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1211584063094243329',         '1364462932626432039',         '1273437052912189443',         '1211585626864992257');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1211584063094243329',         '1364462932626432040',         '1273437052912189443',         '1266210212634546178');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1211584063094243329',         '1364462932626432041',         '1273437052912189443',         '1264834225111949313');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1211584063094243329',         '1364462932626432042',         '1273437052912189443',         '1211861757814603777');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1211584063094243329',         '1364462932626432043',         '1273437052912189443',         '1266291700776312833');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1216010678984933378',         '1364462932626432044',         '1273437052912189443',         '1216010678984933378');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1216010678984933378',         '1364462932626432045',         '1273437052912189443',         '1216012199285915649');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1216010678984933378',         '1364462932626432046',         '1273437052912189443',         '1244447241742184449');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1216010678984933378',         '1364462932626432047',         '1273437052912189443',         '1248505134808322050');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1216010678984933378',         '1364462932626432048',         '1273437052912189443',         '1249889565586513922');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1216010678984933378',         '1364462932626432049',         '1273437052912189443',         '1278596165849227265');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1216010678984933378',         '1364462932626432050',         '1273437052912189443',         '1293362622282776577');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1216010678984933378',         '1364462932626432051',         '1273437052912189443',         '1293363696435642369');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1216010678984933378',         '1364462932626432052',         '1273437052912189443',         '1306045780712865793');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1216010678984933378',         '1364462932626432053',         '1273437052912189443',         '1306045843447070721');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1216010678984933378',         '1364462932626432054',         '1273437052912189443',         '1306045964670844930');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1216010678984933378',         '1364462932626432055',         '1273437052912189443',         '1244447341415624705');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1216010678984933378',         '1364462932626432056',         '1273437052912189443',         '1248505135328415745');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1216010678984933378',         '1364462932626432057',         '1273437052912189443',         '1260755253341421570');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1216010678984933378',         '1364462932626432058',         '1273437052912189443',         '1278596600936964097');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1216010678984933378',         '1364462932626432059',         '1273437052912189443',         '1281238408903122945');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1216010678984933378',         '1364462932626432060',         '1273437052912189443',         '1293363797350596609');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1216010678984933378',         '1364462932626432061',         '1273437052912189443',         '1234752717012910081');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1216010678984933378',         '1364462932626432062',         '1273437052912189443',         '1248505135357775873');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1216010678984933378',         '1364462932626432063',         '1273437052912189443',         '1278596791257702402');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1216010678984933378',         '1364462932626432064',         '1273437052912189443',         '1286853619731222529');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1216010678984933378',         '1364462932626432065',         '1273437052912189443',         '1293363842540027906');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1216010678984933378',         '1364462932626432066',         '1273437052912189443',         '1248505135387136002');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1216010678984933378',         '1364462932626432067',         '1273437052912189443',         '1278593759014985729');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1216010678984933378',         '1364462932626432068',         '1273437052912189443',         '1293844457764896769');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1216010678984933378',         '1364462932626432069',         '1273437052912189443',         '1234753134522318850');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1216010678984933378',         '1364462932626432070',         '1273437052912189443',         '1235736062029025281');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1216010678984933378',         '1364462932626432071',         '1273437052912189443',         '1237568297940049922');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1216010678984933378',         '1364462932626432072',         '1273437052912189443',         '1308231134028992513');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1216010678984933378',         '1364462932626432073',         '1273437052912189443',         '1261227387018747905');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1216010678984933378',         '1364462932626432074',         '1273437052912189443',         '1274947857612177410');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1216010678984933378',         '1364462932626432075',         '1273437052912189443',         '1293362266983284737');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1216010678984933378',         '1364462932626432076',         '1273437052912189443',         '1308654677577383938');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980476072275970',         '1364462932626432077',         '1273437052912189443',         '1208980476072275970');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980476072275970',         '1364462932626432078',         '1273437052912189443',         '1200071564951851010');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980476072275970',         '1364462932626432079',         '1273437052912189443',         '1305330478559612929');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980476072275970',         '1364462932626432080',         '1273437052912189443',         '1305330535048499202');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980476072275970',         '1364462932626432081',         '1273437052912189443',         '1202615098250752002');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980476072275970',         '1364462932626432082',         '1273437052912189443',         '1305330425287757826');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980476072275970',         '1364462932626432083',         '1273437052912189443',         '1285383690846740482');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980476072275970',         '1364462932626432084',         '1273437052912189443',         '1202615504901107713');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980476072275970',         '1364462932626432085',         '1273437052912189443',         '1257962418689892353');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980476072275970',         '1364462932626432086',         '1273437052912189443',         '1258235847351537665');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980200670081026',         '1364462932626432087',         '1273437052912189443',         '4212');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980200670081026',         '1364462932626432088',         '1273437052912189443',         '2112');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980200670081026',         '1364462932626432089',         '1273437052912189443',         '4112');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980200670081026',         '1364462932626432090',         '1273437052912189443',         '4213');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980200670081026',         '1364462932626432091',         '1273437052912189443',         '2113');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980200670081026',         '1364462932626432092',         '1273437052912189443',         '4113');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980200670081026',         '1364462932626432093',         '1273437052912189443',         '4114');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980200670081026',         '1364462932626432094',         '1273437052912189443',         '2114');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980200670081026',         '1364462932626432095',         '1273437052912189443',         '4214');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980200670081026',         '1364462932626432096',         '1273437052912189443',         '2115');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980200670081026',         '1364462932626432097',         '1273437052912189443',         '4215');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980200670081026',         '1364462932626432098',         '1273437052912189443',         '4115');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980200670081026',         '1364462932626432099',         '1273437052912189443',         '1208980200670081026');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980200670081026',         '1364462932626432100',         '1273437052912189443',         '10');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980200670081026',         '1364462932626432101',         '1273437052912189443',         '1192840820951826434');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980200670081026',         '1364462932626432102',         '1273437052912189443',         '1192842980397629441');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980200670081026',         '1364462932626432103',         '1273437052912189443',         '1192843171666280450');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980200670081026',         '1364462932626432104',         '1273437052912189443',         '12');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980200670081026',         '1364462932626432105',         '1273437052912189443',         '13');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980200670081026',         '1364462932626432106',         '1273437052912189443',         '1305323673653694465');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980200670081026',         '1364462932626432107',         '1273437052912189443',         '1305323962142117890');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980200670081026',         '1364462932626432108',         '1273437052912189443',         '1305324650528067585');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980200670081026',         '1364462932626432109',         '1273437052912189443',         '1305325070310789122');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980200670081026',         '1364462932626432110',         '1273437052912189443',         '1305325136597569538');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980200670081026',         '1364462932626432111',         '1273437052912189443',         '1305325192159514626');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980200670081026',         '1364462932626432112',         '1273437052912189443',         '1364462772253069314');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980200670081026',         '1364462932626432113',         '1273437052912189443',         '11');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980200670081026',         '1364462932626432114',         '1273437052912189443',         '1192842689568784386');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980200670081026',         '1364462932626432115',         '1273437052912189443',         '1305323518837739522');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980200670081026',         '1364462932626432116',         '1273437052912189443',         '14');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980200670081026',         '1364462932626432117',         '1273437052912189443',         '1299181110890094594');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980200670081026',         '1364462932626432118',         '1273437052912189443',         '1305324971073556482');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980200670081026',         '1364462932626432119',         '1273437052912189443',         '9');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980200670081026',         '1364462932626432120',         '1273437052912189443',         '1281055642299138050');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980200670081026',         '1364462932626432121',         '1273437052912189443',         '1192840639556567041');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1432269009532760065',         '1433378358229504045',         '1',         '1432269009532760065');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1305412680270073858',         '1435830767526170626',         '1',         '1305412680270073858');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1257928508099674114',         '1435830767526170627',         '1',         '1257928508099674114');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1257928508099674114',         '1435830767526170628',         '1',         '1258697656622858242');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1257928508099674114',         '1435830767526170629',         '1',         '1290200582640492545');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1257928508099674114',         '1435830767526170630',         '1',         '1290200644439367682');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1257928508099674114',         '1435830767526170631',         '1',         '1290200764849446914');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1257928508099674114',         '1435830767526170632',         '1',         '1290200826270834690');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1257928508099674114',         '1435830767526170633',         '1',         '1290201109801590785');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1257928508099674114',         '1435830767526170634',         '1',         '1364462564484026369');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1257928508099674114',         '1435830767526170635',         '1',         '1283422020083683329');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1257928508099674114',         '1435830767526170636',         '1',         '1290200432228556802');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1257928508099674114',         '1435830767526170637',         '1',         '1283427147565797377');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1257928508099674114',         '1435830767526170638',         '1',         '1290201031812702209');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1257928508099674114',         '1435830767526170639',         '1',         '1290200178347335682');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1257928508099674114',         '1435830767526170640',         '1',         '1258697973154398209');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1257928508099674114',         '1435830767526170641',         '1',         '1258698215539032066');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1257928508099674114',         '1435830767526170642',         '1',         '1260784115450310658');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1257928508099674114',         '1435830767526170643',         '1',         '1271372098643738626');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1257928508099674114',         '1435830767526170644',         '1',         '1260854501005103105');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1257928508099674114',         '1435830767526170645',         '1',         '1252503826125074433');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1257928508099674114',         '1435830767526170646',         '1',         '1271372406870556673');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1211584063094243329',         '1435830767526170647',         '1',         '1211584063094243329');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1211584063094243329',         '1435830767526170648',         '1',         '1232217594178830338');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1211584063094243329',         '1435830767526170649',         '1',         '1263004299056492546');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1211584063094243329',         '1435830767526170650',         '1',         '1264835010617008129');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1211584063094243329',         '1435830767526170651',         '1',         '1266292053827657729');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1211584063094243329',         '1435830767526170652',         '1',         '1266316421131673601');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1211584063094243329',         '1435830767526170653',         '1',         '1266316643584974850');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1211584063094243329',         '1435830767526170654',         '1',         '1394197475231150081');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1211584063094243329',         '1435830767526170655',         '1',         '1232223000892538881');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1211584063094243329',         '1435830767526170656',         '1',         '1264835093727141890');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1211584063094243329',         '1435830767526170657',         '1',         '1266292134257631233');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1211584063094243329',         '1435830767526170658',         '1',         '1266316494125146113');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1211584063094243329',         '1435830767526170659',         '1',         '1266316741412921346');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1211584063094243329',         '1435830767526170660',         '1',         '1235770029288288257');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1211584063094243329',         '1435830767526170661',         '1',         '1264835219484958721');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1211584063094243329',         '1435830767526170662',         '1',         '1266292200984813570');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1211584063094243329',         '1435830767526170663',         '1',         '1266316568800534529');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1211584063094243329',         '1435830767526170664',         '1',         '1266317190513827842');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1211584063094243329',         '1435830767526170665',         '1',         '1211585626864992257');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1211584063094243329',         '1435830767526170666',         '1',         '1266210212634546178');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1211584063094243329',         '1435830767526170667',         '1',         '1264834225111949313');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1211584063094243329',         '1435830767526170668',         '1',         '1211861757814603777');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1211584063094243329',         '1435830767526170669',         '1',         '1266291700776312833');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1216010678984933378',         '1435830767526170671',         '1',         '1216010678984933378');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1216010678984933378',         '1435830767526170672',         '1',         '1216012199285915649');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1216010678984933378',         '1435830767526170673',         '1',         '1244447241742184449');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1216010678984933378',         '1435830767526170674',         '1',         '1248505134808322050');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1216010678984933378',         '1435830767526170675',         '1',         '1249889565586513922');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1216010678984933378',         '1435830767526170676',         '1',         '1278596165849227265');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1216010678984933378',         '1435830767526170677',         '1',         '1293362622282776577');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1216010678984933378',         '1435830767526170678',         '1',         '1293363696435642369');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1216010678984933378',         '1435830767526170679',         '1',         '1306045780712865793');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1216010678984933378',         '1435830767526170680',         '1',         '1306045843447070721');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1216010678984933378',         '1435830767526170681',         '1',         '1306045964670844930');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1216010678984933378',         '1435830767526170682',         '1',         '1364513356284854279');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1216010678984933378',         '1435830767526170683',         '1',         '1371080697661370369');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1216010678984933378',         '1435830767526170684',         '1',         '1372067911683969025');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1216010678984933378',         '1435830767526170685',         '1',         '1372068139174629378');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1216010678984933378',         '1435830767526170686',         '1',         '1386621109043757058');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1216010678984933378',         '1435830767526170687',         '1',         '1244447341415624705');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1216010678984933378',         '1435830767526170688',         '1',         '1248505135328415745');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1216010678984933378',         '1435830767526170689',         '1',         '1260755253341421570');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1216010678984933378',         '1435830767526170690',         '1',         '1278596600936964097');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1216010678984933378',         '1435830767526170691',         '1',         '1281238408903122945');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1216010678984933378',         '1435830767526170692',         '1',         '1293363797350596609');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1216010678984933378',         '1435830767526170693',         '1',         '1234752717012910081');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1216010678984933378',         '1435830767526170694',         '1',         '1248505135357775873');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1216010678984933378',         '1435830767526170695',         '1',         '1278596791257702402');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1216010678984933378',         '1435830767526170696',         '1',         '1286853619731222529');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1216010678984933378',         '1435830767526170697',         '1',         '1293363842540027906');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1216010678984933378',         '1435830767526170698',         '1',         '1248505135387136002');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1216010678984933378',         '1435830767526170699',         '1',         '1278593759014985729');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1216010678984933378',         '1435830767526170700',         '1',         '1293844457764896769');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1216010678984933378',         '1435830767526170701',         '1',         '1234753134522318850');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1216010678984933378',         '1435830767526170702',         '1',         '1235736062029025281');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1216010678984933378',         '1435830767526170703',         '1',         '1237568297940049922');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1216010678984933378',         '1435830767526170704',         '1',         '1308231134028992513');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1216010678984933378',         '1435830767526170705',         '1',         '1422736845555679234');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1216010678984933378',         '1435830767526170706',         '1',         '1261227387018747905');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1216010678984933378',         '1435830767526170707',         '1',         '1274947857612177410');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1216010678984933378',         '1435830767526170708',         '1',         '1293362266983284737');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1216010678984933378',         '1435830767526170709',         '1',         '1308654677577383938');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980476072275970',         '1435830767526170710',         '1',         '1208980476072275970');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980476072275970',         '1435830767526170711',         '1',         '1200071564951851010');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980476072275970',         '1435830767526170712',         '1',         '1305330478559612929');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980476072275970',         '1435830767526170713',         '1',         '1305330535048499202');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980476072275970',         '1435830767526170714',         '1',         '1202615098250752002');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980476072275970',         '1435830767526170715',         '1',         '1305330425287757826');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980476072275970',         '1435830767526170716',         '1',         '1285383690846740482');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980476072275970',         '1435830767526170717',         '1',         '1202615504901107713');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980476072275970',         '1435830767526170718',         '1',         '1257962418689892353');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980476072275970',         '1435830767526170719',         '1',         '1258235847351537665');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980374695948289',         '1435830767526170720',         '1',         '1208980374695948289');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980374695948289',         '1435830767526170721',         '1',         '1200063412336365569');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980374695948289',         '1435830767526170722',         '1',         '1207193355854483457');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980374695948289',         '1435830767526170723',         '1',         '1252486733648441345');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980374695948289',         '1435830767526170724',         '1',         '1254981434737713153');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980374695948289',         '1435830767526170725',         '1',         '1263400405615255553');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980374695948289',         '1435830767526170726',         '1',         '1277422036429197313');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980374695948289',         '1435830767526170727',         '1',         '1283642493907161101');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980374695948289',         '1435830767526170728',         '1',         '1286856600102981634');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980374695948289',         '1435830767526170729',         '1',         '1286856698836897794');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980374695948289',         '1435830767526170730',         '1',         '1286856776683180033');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980374695948289',         '1435830767526170731',         '1',         '1353613805231726593');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980374695948289',         '1435830767526170732',         '1',         '1371097248343011329');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980374695948289',         '1435830767526170733',         '1',         '1418090742926475265');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980374695948289',         '1435830767526170734',         '1',         '1423573455319605249');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980374695948289',         '1435830767526170735',         '1',         '1424565450276134913');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980374695948289',         '1435830767526170736',         '1',         '1428261214437294082');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980374695948289',         '1435830767526170737',         '1',         '1429745529077387266');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980374695948289',         '1435830767526170738',         '1',         '1201125270560563202');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980374695948289',         '1435830767526170739',         '1',         '1252486873310375938');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980374695948289',         '1435830767526170740',         '1',         '1254597233039491073');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980374695948289',         '1435830767526170741',         '1',         '1255012367289311233');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980374695948289',         '1435830767526170742',         '1',         '1265136481083518978');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980374695948289',         '1435830767526170743',         '1',         '1283642493907161100');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980374695948289',         '1435830767526170744',         '1',         '1283642493907161102');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980374695948289',         '1435830767526170745',         '1',         '1429745675387293698');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980374695948289',         '1435830767526170746',         '1',         '1203542002503778306');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980374695948289',         '1435830767526170747',         '1',         '1255012498449391618');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980374695948289',         '1435830767526170748',         '1',         '1260748542887034882');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980374695948289',         '1435830767526170749',         '1',         '1283642493907161103');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980374695948289',         '1435830767526170750',         '1',         '1288416919781060609');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980374695948289',         '1435830767526170751',         '1',         '1431195527581294594');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980374695948289',         '1435830767526170752',         '1',         '1226895246382366722');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980374695948289',         '1435830767526170753',         '1',         '1255012627294216194');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980374695948289',         '1435830767526170754',         '1',         '1286854170938265601');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980374695948289',         '1435830767526170755',         '1',         '1288417074123059201');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980374695948289',         '1435830767526170756',         '1',         '1432966054587236353');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980374695948289',         '1435830767526170757',         '1',         '1229238736024264705');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980374695948289',         '1435830767526170758',         '1',         '1262953796867796993');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980374695948289',         '1435830767526170759',         '1',         '1291643382329257985');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980374695948289',         '1435830767526170760',         '1',         '1243419186959482881');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980374695948289',         '1435830767526170761',         '1',         '1253141628290355202');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980374695948289',         '1435830767526170762',         '1',         '1296014599562817538');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980374695948289',         '1435830767526170763',         '1',         '1260751330262089729');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980374695948289',         '1435830767526170764',         '1',         '1261134794188664833');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980374695948289',         '1435830767526170765',         '1',         '1270960174458208258');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980374695948289',         '1435830767526170766',         '1',         '1336224385327091714');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980374695948289',         '1435830767526170767',         '1',         '1208732245116686338');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980374695948289',         '1435830767526170768',         '1',         '1277421271715303426');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980374695948289',         '1435830767526170769',         '1',         '1431191712496943105');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980200670081026',         '1435830767526170770',         '1',         '1208980200670081026');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980200670081026',         '1435830767526170771',         '1',         '10');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980200670081026',         '1435830767526170772',         '1',         '12');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980200670081026',         '1435830767526170773',         '1',         '13');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980200670081026',         '1435830767526170774',         '1',         '2112');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980200670081026',         '1435830767526170775',         '1',         '4112');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980200670081026',         '1435830767526170776',         '1',         '4212');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980200670081026',         '1435830767526170777',         '1',         '1192840820951826434');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980200670081026',         '1435830767526170778',         '1',         '1192842980397629441');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980200670081026',         '1435830767526170779',         '1',         '1192843171666280450');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980200670081026',         '1435830767526170780',         '1',         '1305323673653694465');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980200670081026',         '1435830767526170781',         '1',         '1305323962142117890');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980200670081026',         '1435830767526170782',         '1',         '1305324650528067585');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980200670081026',         '1435830767526170783',         '1',         '1305325070310789122');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980200670081026',         '1435830767526170784',         '1',         '1305325136597569538');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980200670081026',         '1435830767526170785',         '1',         '1305325192159514626');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980200670081026',         '1435830767526170786',         '1',         '1364462772253069314');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980200670081026',         '1435830767526170787',         '1',         '1371092826967781378');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980200670081026',         '1435830767526170788',         '1',         '1371093843239251969');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980200670081026',         '1435830767526170789',         '1',         '1371094328977403905');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980200670081026',         '1435830767526170790',         '1',         '1419944312698310657');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980200670081026',         '1435830767526170791',         '1',         '1420351467654012930');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980200670081026',         '1435830767526170792',         '1',         '1420351744436133889');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980200670081026',         '1435830767526170793',         '1',         '1420948640309522434');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980200670081026',         '1435830767526170794',         '1',         '11');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980200670081026',         '1435830767526170795',         '1',         '14');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980200670081026',         '1435830767526170796',         '1',         '2113');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980200670081026',         '1435830767526170797',         '1',         '4113');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980200670081026',         '1435830767526170798',         '1',         '4213');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980200670081026',         '1435830767526170799',         '1',         '1192842689568784386');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980200670081026',         '1435830767526170800',         '1',         '1305323518837739522');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980200670081026',         '1435830767526170801',         '1',         '9');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980200670081026',         '1435830767526170802',         '1',         '2114');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980200670081026',         '1435830767526170803',         '1',         '4114');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980200670081026',         '1435830767526170804',         '1',         '4214');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980200670081026',         '1435830767526170805',         '1',         '1299181110890094594');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980200670081026',         '1435830767526170806',         '1',         '1305324971073556482');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980200670081026',         '1435830767526170807',         '1',         '2115');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980200670081026',         '1435830767526170808',         '1',         '4115');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980200670081026',         '1435830767526170809',         '1',         '4215');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980200670081026',         '1435830767526170810',         '1',         '1281055642299138050');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1208980200670081026',         '1435830767526170811',         '1',         '1192840639556567041');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1257961844557897730',         '1435830767526170812',         '1',         '1257961844557897730');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1257961844557897730',         '1435830767526170813',         '1',         '1308967447961300994');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1257961844557897730',         '1435830767526170814',         '1',         '1308967867312009217');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1257961844557897730',         '1435830767526170815',         '1',         '1308968817112150018');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1257961844557897730',         '1435830767526170816',         '1',         '1308968012229406722');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1257961844557897730',         '1435830767526170817',         '1',         '1308968502723899394');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1257961844557897730',         '1435830767526170818',         '1',         '1308968968958537729');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1257961844557897730',         '1435830767526170819',         '1',         '1308968164868517889');

INSERT INTO os_inst_type_menu(APP_ID_,ID_,INST_TYPE_ID_,MENU_ID_)
       values(         '1257961844557897730',         '1435830767526170820',         '1',         '1308968278555127810');

commit;