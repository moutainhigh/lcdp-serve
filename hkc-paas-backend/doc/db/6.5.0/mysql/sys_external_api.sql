use jpaas_system;

INSERT INTO `jpaas_system`.`sys_external_api`(`ID_`, `SERVICE_`, `API_NAME_`, `PATH_`, `METHOD_`, `PARAMS_`, `HEADERS_`, `BODY_`, `TYPE_`, `TENANT_ID_`, `CREATE_DEP_ID_`, `CREATE_BY_`, `CREATE_TIME_`, `UPDATE_BY_`, `UPDATE_TIME_`, `COMPANY_ID_`) VALUES ('1525044598775046145', 'jpaas-bpm', '启动流程', '/api/api-bpm/restApi/bpm/startProcess', 'POST', '[{\"headerName\":\"apiId\",\"headerDesc\":\"接口ID\",\"paraType\":\"String\",\"required\":\"是\",\"serial\":1,\"idx_\":\"jfrvekr898641\"}]', '[{\"headerName\":\"token\",\"headerDesc\":\"令牌\",\"paraType\":\"String\",\"required\":\"是\"},{\"headerName\":\"account\",\"headerDesc\":\"用户账号\",\"paraType\":\"String\",\"required\":\"否(用户账号和用户ID必须设置一个)\"},{\"headerName\":\"userId\",\"headerDesc\":\"用户ID\",\"paraType\":\"String\",\"required\":\"否(用户账号和用户ID必须设置一个)\"}]', '{\n    \"checkType\": \"审批类型(必要)\",\n    \"defId\": \"流程定义ID(defId，defKey 必须二选一)\",\n    \"defKey\": \"流程定义key(defId，defKey 必须二选一)\",\n    \"formJson\": \"{\'表单别名\':{\'字段别名\':\'字段值\'}}(必要)\",\n    \"nodeUserIds\": \"发起时指定任务节点执行人({节点ID:\'执行人ID\'})(非必要)\",\n    \"busKey\": \"业务主键(非必要)\",\n    \"boAlias\": \"变量({\'变量\':\'变量值\'})(非必要)\"\n}', 'default', '1', '1', '1', '2022-05-13 17:25:41', '1', '2022-05-13 17:54:08', '0');
INSERT INTO `jpaas_system`.`sys_external_api`(`ID_`, `SERVICE_`, `API_NAME_`, `PATH_`, `METHOD_`, `PARAMS_`, `HEADERS_`, `BODY_`, `TYPE_`, `TENANT_ID_`, `CREATE_DEP_ID_`, `CREATE_BY_`, `CREATE_TIME_`, `UPDATE_BY_`, `UPDATE_TIME_`, `COMPANY_ID_`) VALUES ('1526034707083169793', 'jpaas-bpm', '审批流程', '/api/api-bpm/restApi/bpm/completeTask', 'POST', '[{\"headerName\":\"apiId\",\"headerDesc\":\"接口ID\",\"paraType\":\"String\",\"required\":\"是\",\"serial\":1,\"idx_\":\"jfrvekr898641\"}]', '[{\"headerName\":\"token\",\"headerDesc\":\"令牌\",\"paraType\":\"String\",\"required\":\"是\"},{\"headerName\":\"account\",\"headerDesc\":\"用户账号\",\"paraType\":\"String\",\"required\":\"否(用户账号和用户ID必须设置一个)\"},{\"headerName\":\"userId\",\"headerDesc\":\"用户ID\",\"paraType\":\"String\",\"required\":\"否(用户账号和用户ID必须设置一个)\"}]', '{\n    \"taskId\": \"任务ID(必要)\",\n    \"checkType\": \"审批类型(必要)\",\n    \"opinion\": \"审批意见(必要)\",\n    \"copyUserAccounts\": \"抄送用户账号,用,隔开(非必要)\",\n    \"formJson\": \"表单数据,数据中需要INST_ID_ (非必要)\",\n    \"msgTypes\": \"消息通知(非必要)\",\n    \"nodeExecutors\": \"下一节点审批人配置({\"节点ID\":[{\"calcType\":\"none\",\"id\":\"2\",\"name\":\"ludy\",\"type\":\"user\"}]})(非必要)\",\n    \"opFiles\": \"审批附件(非必要)\",\n    \"opinionName\": \"审批意见名称\",\n    \"destNodeId\": \"目标节点ID\",\n     \"vars\": \"变量({\'变量\':\'变量值\'})(非必要)\",\n    \"nodeExecutors\": \"指定节点的用户({\"节点key\":[{\"calcType\":\"none\",\"id\":\"用户Id\",\"name\":\"用户\",\"type\":\"user\"}]})(非必要)\",\n    \"excutors\": \"直接指定下一节点的执行人(无法与nodeExecutors同时使用)([{\"type\": \"user\", \"id\": \"用户Id\"}])(非必要)\"\n}', 'default', '1', '1', '1', '2022-05-16 11:00:01', '1', '2022-05-16 11:00:42', '0');
INSERT INTO `jpaas_system`.`sys_external_api`(`ID_`, `SERVICE_`, `API_NAME_`, `PATH_`, `METHOD_`, `PARAMS_`, `HEADERS_`, `BODY_`, `TYPE_`, `TENANT_ID_`, `CREATE_DEP_ID_`, `CREATE_BY_`, `CREATE_TIME_`, `UPDATE_BY_`, `UPDATE_TIME_`, `COMPANY_ID_`) VALUES ('1526035254037188610', 'jpaas-bpm', '根据用户账号获取待办列表', '/api/api-bpm/restApi/bpm/getTasksByUserAccount', 'POST', '[{\"headerName\":\"apiId\",\"headerDesc\":\"接口ID\",\"paraType\":\"String\",\"required\":\"是\",\"serial\":1,\"idx_\":\"jfrvekr898641\"}]', '[{\"headerName\":\"token\",\"headerDesc\":\"令牌\",\"paraType\":\"String\",\"required\":\"是\"},{\"headerName\":\"account\",\"headerDesc\":\"用户账号\",\"paraType\":\"String\",\"required\":\"否(用户账号和用户ID必须设置一个)\"},{\"headerName\":\"userId\",\"headerDesc\":\"用户ID\",\"paraType\":\"String\",\"required\":\"否(用户账号和用户ID必须设置一个)\"}]', '{\n    \"pageNo\": 1(当前页码),\n    \"pageSize\": 10(分页大小),\n    \"params\": {}(条件，例：Q_SUBJECT__S_LK	),\n    \"sortField\": \"排序字段\",\n    \"sortOrder\": \"排序（asc/desc）\"\n}', 'default', '1', '1', '1', '2022-05-16 11:02:12', '1', '2022-05-16 11:12:40', '0');
INSERT INTO `jpaas_system`.`sys_external_api`(`ID_`, `SERVICE_`, `API_NAME_`, `PATH_`, `METHOD_`, `PARAMS_`, `HEADERS_`, `BODY_`, `TYPE_`, `TENANT_ID_`, `CREATE_DEP_ID_`, `CREATE_BY_`, `CREATE_TIME_`, `UPDATE_BY_`, `UPDATE_TIME_`, `COMPANY_ID_`) VALUES ('1526035388611432449', 'jpaas-bpm', '获取有权限发起的流程定义', '/api/api-bpm/restApi/bpm/getMySolutions', 'POST', '[{\"headerName\":\"apiId\",\"headerDesc\":\"接口ID\",\"paraType\":\"String\",\"required\":\"是\",\"serial\":1,\"idx_\":\"jfrvekr898641\"}]', '[{\"headerName\":\"token\",\"headerDesc\":\"令牌\",\"paraType\":\"String\",\"required\":\"是\"},{\"headerName\":\"account\",\"headerDesc\":\"用户账号\",\"paraType\":\"String\",\"required\":\"否(用户账号和用户ID必须设置一个)\"},{\"headerName\":\"userId\",\"headerDesc\":\"用户ID\",\"paraType\":\"String\",\"required\":\"否(用户账号和用户ID必须设置一个)\"}]', '{\n    \"pageNo\": 1(当前页码),\n    \"pageSize\": 10(分页大小),\n    \"params\": {}(条件，例：Q_SUBJECT__S_LK	),\n    \"sortField\": \"排序字段\",\n    \"sortOrder\": \"排序（asc/desc）\"\n}', 'default', '1', '1', '1', '2022-05-16 11:02:44', '1', '2022-05-16 11:12:36', '0');
INSERT INTO `jpaas_system`.`sys_external_api`(`ID_`, `SERVICE_`, `API_NAME_`, `PATH_`, `METHOD_`, `PARAMS_`, `HEADERS_`, `BODY_`, `TYPE_`, `TENANT_ID_`, `CREATE_DEP_ID_`, `CREATE_BY_`, `CREATE_TIME_`, `UPDATE_BY_`, `UPDATE_TIME_`, `COMPANY_ID_`) VALUES ('1526035655713099777', 'jpaas-bpm', '根据任务ID获取后续节点', '/api/api-bpm/restApi/bpm/getTaskOutNodes', 'POST', '[{\"headerName\":\"taskId\",\"headerDesc\":\"任务ID\",\"paraType\":\"String\",\"required\":\"是\",\"serial\":1,\"idx_\":\"mmfmeuq694463\"},{\"headerName\":\"checkType\",\"headerDesc\":\"意见类型\",\"paraType\":\"String\",\"required\":\"否\",\"serial\":2,\"idx_\":\"hvaqgdp714770\"}]', '[{\"headerName\":\"token\",\"headerDesc\":\"令牌\",\"paraType\":\"String\",\"required\":\"是\",\"serial\":1,\"idx_\":\"xmxmcff371367\"}]', NULL, 'default', '1', '1', '1', '2022-05-16 11:03:47', '1', '2022-05-16 15:16:18', '0');
INSERT INTO `jpaas_system`.`sys_external_api`(`ID_`, `SERVICE_`, `API_NAME_`, `PATH_`, `METHOD_`, `PARAMS_`, `HEADERS_`, `BODY_`, `TYPE_`, `TENANT_ID_`, `CREATE_DEP_ID_`, `CREATE_BY_`, `CREATE_TIME_`, `UPDATE_BY_`, `UPDATE_TIME_`, `COMPANY_ID_`) VALUES ('1526038083774713857', 'jpaas-bpm', '根据流程实例ID获取流程相关任务', '/api/api-bpm/restApi/bpm/getTasksByInstId', 'POST', '[{\"headerName\":\"instId\",\"headerDesc\":\"实例ID\",\"paraType\":\"String\",\"required\":\"是\",\"serial\":1,\"idx_\":\"jrkhdlz781185\"}]', '[{\"headerName\":\"token\",\"headerDesc\":\"令牌\",\"paraType\":\"String\",\"required\":\"是\",\"serial\":1,\"idx_\":\"fmeqfll392938\"}]', NULL, 'default', '1', '1', '1', '2022-05-16 11:13:26', '1', '2022-05-16 15:16:36', '0');
INSERT INTO `jpaas_system`.`sys_external_api`(`ID_`, `SERVICE_`, `API_NAME_`, `PATH_`, `METHOD_`, `PARAMS_`, `HEADERS_`, `BODY_`, `TYPE_`, `TENANT_ID_`, `CREATE_DEP_ID_`, `CREATE_BY_`, `CREATE_TIME_`, `UPDATE_BY_`, `UPDATE_TIME_`, `COMPANY_ID_`) VALUES ('1526038274284195841', 'jpaas-bpm', '根据流程实例ID获取审批历史', '/api/api-bpm/restApi/bpm/getCheckHistorys', 'POST', '[{\"headerName\":\"instId\",\"headerDesc\":\"实例ID\",\"paraType\":\"String\",\"required\":\"是\",\"serial\":1,\"idx_\":\"tylzhem835203\"}]', '[{\"headerName\":\"token\",\"headerDesc\":\"令牌\",\"paraType\":\"String\",\"required\":\"是\",\"serial\":1,\"idx_\":\"xnreqav409142\"}]', NULL, 'default', '1', '1', '1', '2022-05-16 11:14:12', '1', '2022-05-16 15:16:56', '0');
INSERT INTO `jpaas_system`.`sys_external_api`(`ID_`, `SERVICE_`, `API_NAME_`, `PATH_`, `METHOD_`, `PARAMS_`, `HEADERS_`, `BODY_`, `TYPE_`, `TENANT_ID_`, `CREATE_DEP_ID_`, `CREATE_BY_`, `CREATE_TIME_`, `UPDATE_BY_`, `UPDATE_TIME_`, `COMPANY_ID_`) VALUES ('1526038735380811778', 'jpaas-bpm', '任务沟通', '/api/api-bpm/restApi/bpm/linkup', 'POST', '[{\"headerName\":\"apiId\",\"headerDesc\":\"接口ID\",\"paraType\":\"String\",\"required\":\"是\",\"serial\":1,\"idx_\":\"jfrvekr898641\"}]', '[{\"headerName\":\"token\",\"headerDesc\":\"令牌\",\"paraType\":\"String\",\"required\":\"是\"},{\"headerName\":\"account\",\"headerDesc\":\"用户账号\",\"paraType\":\"String\",\"required\":\"否(用户账号和用户ID必须设置一个)\"},{\"headerName\":\"userId\",\"headerDesc\":\"用户ID\",\"paraType\":\"String\",\"required\":\"否(用户账号和用户ID必须设置一个)\"}]', '{\n    \"taskId\": \"任务ID(必要)\",\n    \"toUserAccounts\": \"沟通用户账号，用‘，’隔开(必要)\"\n    \"msgTypes\": \"消息通知\",\n    \"opFiles\": \"[](附件)\",\n    \"opinion\": \"沟通意见\",\n}', 'default', '1', '1', '1', '2022-05-16 11:16:02', NULL, '2022-05-16 11:16:02', '0');
INSERT INTO `jpaas_system`.`sys_external_api`(`ID_`, `SERVICE_`, `API_NAME_`, `PATH_`, `METHOD_`, `PARAMS_`, `HEADERS_`, `BODY_`, `TYPE_`, `TENANT_ID_`, `CREATE_DEP_ID_`, `CREATE_BY_`, `CREATE_TIME_`, `UPDATE_BY_`, `UPDATE_TIME_`, `COMPANY_ID_`) VALUES ('1526038945981009922', 'jpaas-bpm', '回复任务沟通', '/api/api-bpm/restApi/bpm/replyLinkupTask', 'POST', '[{\"headerName\":\"apiId\",\"headerDesc\":\"接口ID\",\"paraType\":\"String\",\"required\":\"是\",\"serial\":1,\"idx_\":\"jfrvekr898641\"}]', '[{\"headerName\":\"token\",\"headerDesc\":\"令牌\",\"paraType\":\"String\",\"required\":\"是\"},{\"headerName\":\"account\",\"headerDesc\":\"用户账号\",\"paraType\":\"String\",\"required\":\"否(用户账号和用户ID必须设置一个)\"},{\"headerName\":\"userId\",\"headerDesc\":\"用户ID\",\"paraType\":\"String\",\"required\":\"否(用户账号和用户ID必须设置一个)\"}]', '{\n    \"taskId\": \"任务ID(必要)\",\n	\"msgTypes\": \"消息通知\",\n    \"opFiles\": \"[](附件)\",\n    \"opinion\": \"沟通意见\",\n}', 'default', '1', '1', '1', '2022-05-16 11:16:52', NULL, '2022-05-16 11:16:52', '0');
INSERT INTO `jpaas_system`.`sys_external_api`(`ID_`, `SERVICE_`, `API_NAME_`, `PATH_`, `METHOD_`, `PARAMS_`, `HEADERS_`, `BODY_`, `TYPE_`, `TENANT_ID_`, `CREATE_DEP_ID_`, `CREATE_BY_`, `CREATE_TIME_`, `UPDATE_BY_`, `UPDATE_TIME_`, `COMPANY_ID_`) VALUES ('1526039252010012674', 'jpaas-bpm', '撤销沟通任务', '/api/api-bpm/restApi/bpm/revokeCmTask', 'POST', '[{\"headerName\":\"instId\",\"headerDesc\":\"实例ID\",\"paraType\":\"String\",\"required\":\"是\",\"serial\":1,\"idx_\":\"gcrqlsl041270\"},{\"headerName\":\"delOpinion\",\"headerDesc\":\"是否删除沟通意见\",\"paraType\":\"boolean\",\"required\":\"是\",\"serial\":2,\"idx_\":\"nhhlvms058070\"}]', '[{\"headerName\":\"token\",\"headerDesc\":\"令牌\",\"paraType\":\"String\",\"required\":\"是\",\"serial\":1,\"idx_\":\"htmvpte441898\"}]', NULL, 'default', '1', '1', '1', '2022-05-16 11:18:05', '1', '2022-05-16 15:17:25', '0');
INSERT INTO `jpaas_system`.`sys_external_api`(`ID_`, `SERVICE_`, `API_NAME_`, `PATH_`, `METHOD_`, `PARAMS_`, `HEADERS_`, `BODY_`, `TYPE_`, `TENANT_ID_`, `CREATE_DEP_ID_`, `CREATE_BY_`, `CREATE_TIME_`, `UPDATE_BY_`, `UPDATE_TIME_`, `COMPANY_ID_`) VALUES ('1526039474853384194', 'jpaas-bpm', '根据任务ID获取表单数据', '/api/api-bpm/restApi/bpm/getFormDataByTaskId', 'POST', '[{\"headerName\":\"taskId\",\"headerDesc\":\"任务ID\",\"paraType\":\"String\",\"required\":\"是\",\"serial\":1,\"idx_\":\"tvxfyjq119945\"}]', '[{\"headerName\":\"token\",\"headerDesc\":\"令牌\",\"paraType\":\"String\",\"required\":\"是\"},{\"headerName\":\"account\",\"headerDesc\":\"用户账号\",\"paraType\":\"String\",\"required\":\"否(用户账号和用户ID必须设置一个)\"},{\"headerName\":\"userId\",\"headerDesc\":\"用户ID\",\"paraType\":\"String\",\"required\":\"否(用户账号和用户ID必须设置一个)\"}]', NULL, 'default', '1', '1', '1', '2022-05-16 11:18:58', NULL, '2022-05-16 11:18:58', '0');
INSERT INTO `jpaas_system`.`sys_external_api`(`ID_`, `SERVICE_`, `API_NAME_`, `PATH_`, `METHOD_`, `PARAMS_`, `HEADERS_`, `BODY_`, `TYPE_`, `TENANT_ID_`, `CREATE_DEP_ID_`, `CREATE_BY_`, `CREATE_TIME_`, `UPDATE_BY_`, `UPDATE_TIME_`, `COMPANY_ID_`) VALUES ('1526039646337503233', 'jpaas-bpm', '根据实例ID获取表单数据', '/api/api-bpm/restApi/bpm/getFormDataByInstId', 'POST', '[{\"headerName\":\"instId\",\"headerDesc\":\"实例ID\",\"paraType\":\"String\",\"required\":\"是\",\"serial\":1,\"idx_\":\"czhbiaf165007\"}]', '[{\"headerName\":\"token\",\"headerDesc\":\"令牌\",\"paraType\":\"String\",\"required\":\"是\"},{\"headerName\":\"account\",\"headerDesc\":\"用户账号\",\"paraType\":\"String\",\"required\":\"否(用户账号和用户ID必须设置一个)\"},{\"headerName\":\"userId\",\"headerDesc\":\"用户ID\",\"paraType\":\"String\",\"required\":\"否(用户账号和用户ID必须设置一个)\"}]', NULL, 'default', '1', '1', '1', '2022-05-16 11:19:39', NULL, '2022-05-16 11:19:39', '0');
INSERT INTO `jpaas_system`.`sys_external_api`(`ID_`, `SERVICE_`, `API_NAME_`, `PATH_`, `METHOD_`, `PARAMS_`, `HEADERS_`, `BODY_`, `TYPE_`, `TENANT_ID_`, `CREATE_DEP_ID_`, `CREATE_BY_`, `CREATE_TIME_`, `UPDATE_BY_`, `UPDATE_TIME_`, `COMPANY_ID_`) VALUES ('1526039910364745729', 'jpaas-bpm', '获取我发起的流程实例', '/api/api-bpm/restApi/bpm/getMyStartInsts', 'POST', '[{\"headerName\":\"apiId\",\"headerDesc\":\"接口ID\",\"paraType\":\"String\",\"required\":\"是\",\"serial\":1,\"idx_\":\"jfrvekr898641\"}]', '[{\"headerName\":\"token\",\"headerDesc\":\"令牌\",\"paraType\":\"String\",\"required\":\"是\"},{\"headerName\":\"account\",\"headerDesc\":\"用户账号\",\"paraType\":\"String\",\"required\":\"否(用户账号和用户ID必须设置一个)\"},{\"headerName\":\"userId\",\"headerDesc\":\"用户ID\",\"paraType\":\"String\",\"required\":\"否(用户账号和用户ID必须设置一个)\"}]', '{\n    \"pageNo\": 1(当前页码),\n    \"pageSize\": 10(分页大小),\n    \"params\": {}(条件，例：Q_SUBJECT__S_LK	),\n    \"sortField\": \"排序字段\",\n    \"sortOrder\": \"排序（asc/desc）\"\n}', 'default', '1', '1', '1', '2022-05-16 11:20:42', NULL, '2022-05-16 11:20:42', '0');
INSERT INTO `jpaas_system`.`sys_external_api`(`ID_`, `SERVICE_`, `API_NAME_`, `PATH_`, `METHOD_`, `PARAMS_`, `HEADERS_`, `BODY_`, `TYPE_`, `TENANT_ID_`, `CREATE_DEP_ID_`, `CREATE_BY_`, `CREATE_TIME_`, `UPDATE_BY_`, `UPDATE_TIME_`, `COMPANY_ID_`) VALUES ('1526040140174856193', 'jpaas-bpm', '取回任务接口', '/api/api-bpm/restApi/bpm/takeBackTask', 'POST', '[{\"headerName\":\"instId\",\"headerDesc\":\"实例ID\",\"paraType\":\"String\",\"required\":\"是\",\"serial\":1,\"idx_\":\"snbrvof269052\"},{\"headerName\":\"nodeId\",\"headerDesc\":\"节点ID\",\"paraType\":\"String\",\"required\":\"是\",\"serial\":2,\"idx_\":\"jwbazke280660\"}]', '[{\"headerName\":\"token\",\"headerDesc\":\"令牌\",\"paraType\":\"String\",\"required\":\"是\",\"serial\":1,\"idx_\":\"ldwjicg457274\"}]', NULL, 'default', '1', '1', '1', '2022-05-16 11:21:37', '1', '2022-05-16 15:17:41', '0');
INSERT INTO `jpaas_system`.`sys_external_api`(`ID_`, `SERVICE_`, `API_NAME_`, `PATH_`, `METHOD_`, `PARAMS_`, `HEADERS_`, `BODY_`, `TYPE_`, `TENANT_ID_`, `CREATE_DEP_ID_`, `CREATE_BY_`, `CREATE_TIME_`, `UPDATE_BY_`, `UPDATE_TIME_`, `COMPANY_ID_`) VALUES ('1526040235955982338', 'jpaas-bpm', '获取代理给我的任务', '/api/api-bpm/restApi/bpm/getMyReceiveTask', 'POST', '[{\"headerName\":\"apiId\",\"headerDesc\":\"接口ID\",\"paraType\":\"String\",\"required\":\"是\",\"serial\":1,\"idx_\":\"jfrvekr898641\"}]', '[{\"headerName\":\"token\",\"headerDesc\":\"令牌\",\"paraType\":\"String\",\"required\":\"是\"},{\"headerName\":\"account\",\"headerDesc\":\"用户账号\",\"paraType\":\"String\",\"required\":\"否(用户账号和用户ID必须设置一个)\"},{\"headerName\":\"userId\",\"headerDesc\":\"用户ID\",\"paraType\":\"String\",\"required\":\"否(用户账号和用户ID必须设置一个)\"}]', '{\n    \"pageNo\": 1(当前页码),\n    \"pageSize\": 10(分页大小),\n    \"params\": {}(条件，例：Q_SUBJECT__S_LK	),\n    \"sortField\": \"排序字段\",\n    \"sortOrder\": \"排序（asc/desc）\"\n}', 'default', '1', '1', '1', '2022-05-16 11:21:59', NULL, '2022-05-16 11:21:59', '0');
INSERT INTO `jpaas_system`.`sys_external_api`(`ID_`, `SERVICE_`, `API_NAME_`, `PATH_`, `METHOD_`, `PARAMS_`, `HEADERS_`, `BODY_`, `TYPE_`, `TENANT_ID_`, `CREATE_DEP_ID_`, `CREATE_BY_`, `CREATE_TIME_`, `UPDATE_BY_`, `UPDATE_TIME_`, `COMPANY_ID_`) VALUES ('1526040418320125954', 'jpaas-bpm', '获取实例明细', '/api/api-bpm/restApi/bpm/getInstDetail', 'POST', '[{\"headerName\":\"instId\",\"headerDesc\":\"实例ID\",\"paraType\":\"String\",\"required\":\"是\",\"serial\":1,\"idx_\":\"lzpywpq343812\"}]', '[{\"headerName\":\"token\",\"headerDesc\":\"令牌\",\"paraType\":\"String\",\"required\":\"是\"},{\"headerName\":\"account\",\"headerDesc\":\"用户账号\",\"paraType\":\"String\",\"required\":\"否(用户账号和用户ID必须设置一个)\"},{\"headerName\":\"userId\",\"headerDesc\":\"用户ID\",\"paraType\":\"String\",\"required\":\"否(用户账号和用户ID必须设置一个)\"}]', NULL, 'default', '1', '1', '1', '2022-05-16 11:22:43', NULL, '2022-05-16 11:22:43', '0');
INSERT INTO `jpaas_system`.`sys_external_api`(`ID_`, `SERVICE_`, `API_NAME_`, `PATH_`, `METHOD_`, `PARAMS_`, `HEADERS_`, `BODY_`, `TYPE_`, `TENANT_ID_`, `CREATE_DEP_ID_`, `CREATE_BY_`, `CREATE_TIME_`, `UPDATE_BY_`, `UPDATE_TIME_`, `COMPANY_ID_`) VALUES ('1526040514470350850', 'jpaas-bpm', '返回我已审批的流程实例列表', '/api/api-bpm/restApi/bpm/getMyApproved', 'POST', '[{\"headerName\":\"apiId\",\"headerDesc\":\"接口ID\",\"paraType\":\"String\",\"required\":\"是\",\"serial\":1,\"idx_\":\"jfrvekr898641\"}]', '[{\"headerName\":\"token\",\"headerDesc\":\"令牌\",\"paraType\":\"String\",\"required\":\"是\"},{\"headerName\":\"account\",\"headerDesc\":\"用户账号\",\"paraType\":\"String\",\"required\":\"否(用户账号和用户ID必须设置一个)\"},{\"headerName\":\"userId\",\"headerDesc\":\"用户ID\",\"paraType\":\"String\",\"required\":\"否(用户账号和用户ID必须设置一个)\"}]', '{\n    \"pageNo\": 1(当前页码),\n    \"pageSize\": 10(分页大小),\n    \"params\": {}(条件，例：Q_SUBJECT__S_LK	),\n    \"sortField\": \"排序字段\",\n    \"sortOrder\": \"排序（asc/desc）\"\n}', 'default', '1', '1', '1', '2022-05-16 11:23:06', NULL, '2022-05-16 11:23:06', '0');
INSERT INTO `jpaas_system`.`sys_external_api`(`ID_`, `SERVICE_`, `API_NAME_`, `PATH_`, `METHOD_`, `PARAMS_`, `HEADERS_`, `BODY_`, `TYPE_`, `TENANT_ID_`, `CREATE_DEP_ID_`, `CREATE_BY_`, `CREATE_TIME_`, `UPDATE_BY_`, `UPDATE_TIME_`, `COMPANY_ID_`) VALUES ('1526040751528218625', 'jpaas-bpm', '删除流程实例', '/api/api-bpm/restApi/bpm/delInstById', 'POST', '[{\"headerName\":\"instId\",\"headerDesc\":\"实例ID，多个用‘，’分割\",\"paraType\":\"String\",\"required\":\"是\",\"serial\":1,\"idx_\":\"eljffxb407145\"}]', '[{\"headerName\":\"token\",\"headerDesc\":\"令牌\",\"paraType\":\"String\",\"required\":\"是\",\"serial\":1,\"idx_\":\"xciglfj487800\"}]', '', 'default', '1', '1', '1', '2022-05-16 11:24:02', '1', '2022-05-16 15:18:10', '0');
INSERT INTO `jpaas_system`.`sys_external_api`(`ID_`, `SERVICE_`, `API_NAME_`, `PATH_`, `METHOD_`, `PARAMS_`, `HEADERS_`, `BODY_`, `TYPE_`, `TENANT_ID_`, `CREATE_DEP_ID_`, `CREATE_BY_`, `CREATE_TIME_`, `UPDATE_BY_`, `UPDATE_TIME_`, `COMPANY_ID_`) VALUES ('1526041388802379777', 'jpaas-bpm', '保存流程草稿', '/api/api-bpm/restApi/bpm/saveDraft', 'POST', '[{\"headerName\":\"apiId\",\"headerDesc\":\"接口ID\",\"paraType\":\"String\",\"required\":\"是\",\"serial\":1,\"idx_\":\"jfrvekr898641\"}]', '[{\"headerName\":\"token\",\"headerDesc\":\"令牌\",\"paraType\":\"String\",\"required\":\"是\"},{\"headerName\":\"account\",\"headerDesc\":\"用户账号\",\"paraType\":\"String\",\"required\":\"否(用户账号和用户ID必须设置一个)\"},{\"headerName\":\"userId\",\"headerDesc\":\"用户ID\",\"paraType\":\"String\",\"required\":\"否(用户账号和用户ID必须设置一个)\"}]', '{\n    \"defId\": \"流程定义ID(必要)\",\n   \"defKey\": \"流程定义KEY，如果指定了key，优先使用key\",\n    \"formJson\": \"表单数据({\"表单别名\":{\"字段别名\":\"\"}})(必要)\"\n}', 'default', '1', '1', '1', '2022-05-16 11:26:34', NULL, '2022-05-16 11:26:34', '0');
INSERT INTO `jpaas_system`.`sys_external_api`(`ID_`, `SERVICE_`, `API_NAME_`, `PATH_`, `METHOD_`, `PARAMS_`, `HEADERS_`, `BODY_`, `TYPE_`, `TENANT_ID_`, `CREATE_DEP_ID_`, `CREATE_BY_`, `CREATE_TIME_`, `UPDATE_BY_`, `UPDATE_TIME_`, `COMPANY_ID_`) VALUES ('1526041527323463682', 'jpaas-bpm', '获取我的草稿列表', '/api/api-bpm/restApi/bpm/getMyDraft', 'POST', '[{\"headerName\":\"apiId\",\"headerDesc\":\"接口ID\",\"paraType\":\"String\",\"required\":\"是\",\"serial\":1,\"idx_\":\"jfrvekr898641\"}]', '[{\"headerName\":\"token\",\"headerDesc\":\"令牌\",\"paraType\":\"String\",\"required\":\"是\"},{\"headerName\":\"account\",\"headerDesc\":\"用户账号\",\"paraType\":\"String\",\"required\":\"否(用户账号和用户ID必须设置一个)\"},{\"headerName\":\"userId\",\"headerDesc\":\"用户ID\",\"paraType\":\"String\",\"required\":\"否(用户账号和用户ID必须设置一个)\"}]', '{\n    \"pageNo\": 1(当前页码),\n    \"pageSize\": 10(分页大小),\n    \"params\": {}(条件，例：Q_SUBJECT__S_LK	),\n    \"sortField\": \"排序字段\",\n    \"sortOrder\": \"排序（asc/desc）\"\n}', 'default', '1', '1', '1', '2022-05-16 11:27:07', NULL, '2022-05-16 11:27:07', '0');
INSERT INTO `jpaas_system`.`sys_external_api`(`ID_`, `SERVICE_`, `API_NAME_`, `PATH_`, `METHOD_`, `PARAMS_`, `HEADERS_`, `BODY_`, `TYPE_`, `TENANT_ID_`, `CREATE_DEP_ID_`, `CREATE_BY_`, `CREATE_TIME_`, `UPDATE_BY_`, `UPDATE_TIME_`, `COMPANY_ID_`) VALUES ('1526042250530525185', 'jpaas-bpm', '从草稿中启动流程', '/api/api-bpm/restApi/bpm/startProcess', 'POST', '[{\"headerName\":\"apiId\",\"headerDesc\":\"接口ID\",\"paraType\":\"String\",\"required\":\"是\",\"serial\":1,\"idx_\":\"jfrvekr898641\"}]', '[{\"headerName\":\"token\",\"headerDesc\":\"令牌\",\"paraType\":\"String\",\"required\":\"是\"},{\"headerName\":\"account\",\"headerDesc\":\"用户账号\",\"paraType\":\"String\",\"required\":\"否(用户账号和用户ID必须设置一个)\"},{\"headerName\":\"userId\",\"headerDesc\":\"用户ID\",\"paraType\":\"String\",\"required\":\"否(用户账号和用户ID必须设置一个)\"}]', '{\n    \"checkType\": \"审批类型(必要)\",\n    \"defId\": \"流程定义ID\",\n    \"defKey\": \"流程定义KEY，如果指定了key，优先使用key\",\n    \"formJson\": \"表单数据{\'表单别名\':{\'字段标识\':\'\'}}\",\n    \"instId\":\"实例ID，草稿保存后生成的实例ID\"\n}', 'default', '1', '1', '1', '2022-05-16 11:30:00', NULL, '2022-05-16 11:30:00', '0');
INSERT INTO `jpaas_system`.`sys_external_api`(`ID_`, `SERVICE_`, `API_NAME_`, `PATH_`, `METHOD_`, `PARAMS_`, `HEADERS_`, `BODY_`, `TYPE_`, `TENANT_ID_`, `CREATE_DEP_ID_`, `CREATE_BY_`, `CREATE_TIME_`, `UPDATE_BY_`, `UPDATE_TIME_`, `COMPANY_ID_`) VALUES ('1526042425969872898', 'jpaas-bpm', '根据任务ID获取任务信息详细信息', '/api/api-bpm/restApi/bpm/getTaskInfo', 'POST', '[{\"headerName\":\"taskId\",\"headerDesc\":\"任务ID\",\"paraType\":\"String\",\"required\":\"是\",\"serial\":1,\"idx_\":\"iusohnr827953\"}]', '[{\"headerName\":\"token\",\"headerDesc\":\"令牌\",\"paraType\":\"String\",\"required\":\"是\",\"serial\":1,\"idx_\":\"qcojsnu500948\"}]', NULL, 'default', '1', '1', '1', '2022-05-16 11:30:42', '1', '2022-05-16 15:18:24', '0');
INSERT INTO `jpaas_system`.`sys_external_api`(`ID_`, `SERVICE_`, `API_NAME_`, `PATH_`, `METHOD_`, `PARAMS_`, `HEADERS_`, `BODY_`, `TYPE_`, `TENANT_ID_`, `CREATE_DEP_ID_`, `CREATE_BY_`, `CREATE_TIME_`, `UPDATE_BY_`, `UPDATE_TIME_`, `COMPANY_ID_`) VALUES ('1526042591951065090', 'jpaas-bpm', '通过流程定义ID获取流程图', '/api/api-bpm/restApi/bpm/getImageByDefId', 'POST', '[{\"headerName\":\"defId\",\"headerDesc\":\"流程定义ID\",\"paraType\":\"String\",\"required\":\"是\",\"serial\":1,\"idx_\":\"impgmxv866768\"}]', '[{\"headerName\":\"token\",\"headerDesc\":\"令牌\",\"paraType\":\"String\",\"required\":\"是\",\"serial\":1,\"idx_\":\"nakukkg514606\"}]', NULL, 'default', '1', '1', '1', '2022-05-16 11:31:21', '1', '2022-05-16 15:18:38', '0');
INSERT INTO `jpaas_system`.`sys_external_api`(`ID_`, `SERVICE_`, `API_NAME_`, `PATH_`, `METHOD_`, `PARAMS_`, `HEADERS_`, `BODY_`, `TYPE_`, `TENANT_ID_`, `CREATE_DEP_ID_`, `CREATE_BY_`, `CREATE_TIME_`, `UPDATE_BY_`, `UPDATE_TIME_`, `COMPANY_ID_`) VALUES ('1526042732456054785', 'jpaas-bpm', '通过流程实例ID获取流程图', '/api/api-bpm/restApi/bpm/getImageByInstId', 'POST', '[{\"headerName\":\"instId\",\"headerDesc\":\"流程实例ID\",\"paraType\":\"String\",\"required\":\"是\",\"serial\":1,\"idx_\":\"lviejer901179\"}]', '[{\"headerName\":\"token\",\"headerDesc\":\"令牌\",\"paraType\":\"String\",\"required\":\"是\",\"serial\":1,\"idx_\":\"ubxhvwq526341\"}]', NULL, 'default', '1', '1', '1', '2022-05-16 11:31:55', '1', '2022-05-16 15:18:51', '0');
INSERT INTO `jpaas_system`.`sys_external_api`(`ID_`, `SERVICE_`, `API_NAME_`, `PATH_`, `METHOD_`, `PARAMS_`, `HEADERS_`, `BODY_`, `TYPE_`, `TENANT_ID_`, `CREATE_DEP_ID_`, `CREATE_BY_`, `CREATE_TIME_`, `UPDATE_BY_`, `UPDATE_TIME_`, `COMPANY_ID_`) VALUES ('1526042843760300034', 'jpaas-bpm', '获取所有的待办列表', '/api/api-bpm/restApi/bpm/getAllTasks', 'POST', '[{\"headerName\":\"apiId\",\"headerDesc\":\"接口ID\",\"paraType\":\"String\",\"required\":\"是\",\"serial\":1,\"idx_\":\"jfrvekr898641\"}]', '[{\"headerName\":\"token\",\"headerDesc\":\"令牌\",\"paraType\":\"String\",\"required\":\"是\",\"serial\":1,\"idx_\":\"pkrrqya542522\"}]', '{\n    \"pageNo\": 1(当前页码),\n    \"pageSize\": 10(分页大小),\n    \"params\": {}(条件，例：Q_SUBJECT__S_LK	),\n    \"sortField\": \"排序字段\",\n    \"sortOrder\": \"排序（asc/desc）\"\n}', 'default', '1', '1', '1', '2022-05-16 11:32:21', '1', '2022-05-16 15:19:06', '0');
INSERT INTO `jpaas_system`.`sys_external_api`(`ID_`, `SERVICE_`, `API_NAME_`, `PATH_`, `METHOD_`, `PARAMS_`, `HEADERS_`, `BODY_`, `TYPE_`, `TENANT_ID_`, `CREATE_DEP_ID_`, `CREATE_BY_`, `CREATE_TIME_`, `UPDATE_BY_`, `UPDATE_TIME_`, `COMPANY_ID_`) VALUES ('1526043021665898498', 'jpaas-bpm', '获取所有的已办列表', '/api/api-bpm/restApi/bpm/getAllApproved', 'POST', '[{\"headerName\":\"apiId\",\"headerDesc\":\"接口ID\",\"paraType\":\"String\",\"required\":\"是\",\"serial\":1,\"idx_\":\"jfrvekr898641\"}]', '[{\"headerName\":\"token\",\"headerDesc\":\"令牌\",\"paraType\":\"String\",\"required\":\"是\",\"serial\":1,\"idx_\":\"rhmpftg547710\"}]', '{\n    \"pageNo\": 1(当前页码),\n    \"pageSize\": 10(分页大小),\n    \"params\": {}(条件，例：Q_SUBJECT__S_LK	),\n    \"sortField\": \"排序字段\",\n    \"sortOrder\": \"排序（asc/desc）\"\n}', 'default', '1', '1', '1', '2022-05-16 11:33:04', '1', '2022-05-16 15:19:11', '0');
INSERT INTO `jpaas_system`.`sys_external_api`(`ID_`, `SERVICE_`, `API_NAME_`, `PATH_`, `METHOD_`, `PARAMS_`, `HEADERS_`, `BODY_`, `TYPE_`, `TENANT_ID_`, `CREATE_DEP_ID_`, `CREATE_BY_`, `CREATE_TIME_`, `UPDATE_BY_`, `UPDATE_TIME_`, `COMPANY_ID_`) VALUES ('1526043114083192834', 'jpaas-bpm', '获取所有的申请列表', '/api/api-bpm/restApi/bpm/getAllInsts', 'POST', '[{\"headerName\":\"apiId\",\"headerDesc\":\"接口ID\",\"paraType\":\"String\",\"required\":\"是\",\"serial\":1,\"idx_\":\"jfrvekr898641\"}]', '[{\"headerName\":\"token\",\"headerDesc\":\"令牌\",\"paraType\":\"String\",\"required\":\"是\",\"serial\":1,\"idx_\":\"wxzxhzo553438\"}]', '{\n    \"pageNo\": 1(当前页码),\n    \"pageSize\": 10(分页大小),\n    \"params\": {}(条件，例：Q_SUBJECT__S_LK	),\n    \"sortField\": \"排序字段\",\n    \"sortOrder\": \"排序（asc/desc）\"\n}', 'default', '1', '1', '1', '2022-05-16 11:33:26', '1', '2022-05-16 15:19:16', '0');
INSERT INTO `jpaas_system`.`sys_external_api`(`ID_`, `SERVICE_`, `API_NAME_`, `PATH_`, `METHOD_`, `PARAMS_`, `HEADERS_`, `BODY_`, `TYPE_`, `TENANT_ID_`, `CREATE_DEP_ID_`, `CREATE_BY_`, `CREATE_TIME_`, `UPDATE_BY_`, `UPDATE_TIME_`, `COMPANY_ID_`) VALUES ('1526043568934490113', 'jpaas-bpm', '根据流程实例ID作废流程', '/api/api-bpm/restApi/bpm/cancelProcess', 'POST', '[{\"headerName\":\"instId\",\"headerDesc\":\"流程实例ID\",\"paraType\":\"String\",\"required\":\"是\",\"serial\":1,\"idx_\":\"wulozsx028448\"},{\"headerName\":\"reason\",\"headerDesc\":\"作废原因\",\"paraType\":\"String\",\"required\":\"是\",\"serial\":2,\"idx_\":\"hjpeqra043480\"},{\"headerName\":\"opFiles\",\"headerDesc\":\"附件\",\"paraType\":\"String\",\"required\":\"否\",\"serial\":3,\"idx_\":\"aathewh087716\"}]', '[{\"headerName\":\"token\",\"headerDesc\":\"令牌\",\"paraType\":\"String\",\"required\":\"是\",\"serial\":1,\"idx_\":\"czfzixr583367\"}]', NULL, 'default', '1', '1', '1', '2022-05-16 11:35:14', '1', '2022-05-16 15:19:51', '0');
INSERT INTO `jpaas_system`.`sys_external_api`(`ID_`, `SERVICE_`, `API_NAME_`, `PATH_`, `METHOD_`, `PARAMS_`, `HEADERS_`, `BODY_`, `TYPE_`, `TENANT_ID_`, `CREATE_DEP_ID_`, `CREATE_BY_`, `CREATE_TIME_`, `UPDATE_BY_`, `UPDATE_TIME_`, `COMPANY_ID_`) VALUES ('1526043870442033153', 'jpaas-bpm', '根据流程实例ID结束流程', '/api/api-bpm/restApi/bpm/endProcess', 'POST', '[{\"headerName\":\"instId\",\"headerDesc\":\"流程实例ID\",\"paraType\":\"String\",\"required\":\"是\",\"serial\":1,\"idx_\":\"awmigjm148727\"},{\"headerName\":\"reason\",\"headerDesc\":\"结束原因\",\"paraType\":\"String\",\"required\":\"是\",\"serial\":2,\"idx_\":\"aekwjvl150806\"},{\"headerName\":\"opFiles\",\"headerDesc\":\"附件\",\"paraType\":\"String\",\"required\":\"否\",\"serial\":3,\"idx_\":\"zmiraxe151071\"}]', '[{\"headerName\":\"token\",\"headerDesc\":\"令牌\",\"paraType\":\"String\",\"required\":\"是\",\"serial\":1,\"idx_\":\"isaqaty603324\"}]', NULL, 'default', '1', '1', '1', '2022-05-16 11:36:26', '1', '2022-05-16 15:20:09', '0');
INSERT INTO `jpaas_system`.`sys_external_api`(`ID_`, `SERVICE_`, `API_NAME_`, `PATH_`, `METHOD_`, `PARAMS_`, `HEADERS_`, `BODY_`, `TYPE_`, `TENANT_ID_`, `CREATE_DEP_ID_`, `CREATE_BY_`, `CREATE_TIME_`, `UPDATE_BY_`, `UPDATE_TIME_`, `COMPANY_ID_`) VALUES ('1526044099425865729', 'jpaas-bpm', '设置流程变量', '/api/api-bpm/restApi/bpm/setVariables', 'POST', '[{\"headerName\":\"apiId\",\"headerDesc\":\"接口ID\",\"paraType\":\"String\",\"required\":\"是\",\"serial\":1,\"idx_\":\"jfrvekr898641\"}]', '[{\"headerName\":\"token\",\"headerDesc\":\"令牌\",\"paraType\":\"String\",\"required\":\"是\",\"serial\":1,\"idx_\":\"wzzlbrl611155\"}]', '{\n    \"instId\": \"流程示例ID\",\n    \"vars\": {\'变量\':\'变量值\'}\n}', 'default', '1', '1', '1', '2022-05-16 11:37:20', '1', '2022-05-16 15:20:18', '0');
INSERT INTO `jpaas_system`.`sys_external_api`(`ID_`, `SERVICE_`, `API_NAME_`, `PATH_`, `METHOD_`, `PARAMS_`, `HEADERS_`, `BODY_`, `TYPE_`, `TENANT_ID_`, `CREATE_DEP_ID_`, `CREATE_BY_`, `CREATE_TIME_`, `UPDATE_BY_`, `UPDATE_TIME_`, `COMPANY_ID_`) VALUES ('1526101344641617921', 'jpaas-user', '同步用户组', '/api/api-user/restApi/user/addOrUpdGroups', 'POST', '[{\"headerName\":\"apiId\",\"headerDesc\":\"接口ID\",\"paraType\":\"String\",\"required\":\"是\",\"serial\":1,\"idx_\":\"jfrvekr898641\"}]', '[]', ' {\n    \"tenantId\":\"租户编码(必要)\",\n    \"account\":\"同步账号(必要)\",\n    \"groups\":[{\n         \"id\":\"\",---用户组id,唯一标识(必要)\n         \"code\":\"\",----用户组业务主键(必要)\n         \"name\":\"\",---用户组名称(必要)\n         \"parentId\":\"\"--上级id(必要),\n         \"dimId\":\"\"(非必要，不填则默认1),\n         \"status\":\"\"(非必要，不填则默认ENABLED)\n    }]\n}', 'default', '1', '1', '1', '2022-05-16 15:24:49', NULL, '2022-05-16 15:24:49', '0');
INSERT INTO `jpaas_system`.`sys_external_api`(`ID_`, `SERVICE_`, `API_NAME_`, `PATH_`, `METHOD_`, `PARAMS_`, `HEADERS_`, `BODY_`, `TYPE_`, `TENANT_ID_`, `CREATE_DEP_ID_`, `CREATE_BY_`, `CREATE_TIME_`, `UPDATE_BY_`, `UPDATE_TIME_`, `COMPANY_ID_`) VALUES ('1526127165993390082', 'jpaas-user', '删除用户组', '/api/api-user/restApi/user/deleteGroups', 'POST', '[{\"headerName\":\"apiId\",\"headerDesc\":\"接口ID\",\"paraType\":\"String\",\"required\":\"是\",\"serial\":1,\"idx_\":\"jfrvekr898641\"}]', '[]', '{\n    \"tenantId\": \"租户id,默认webApp(必要)\",\n    \"account\":\"操作人员账号(必要)\",\n    \"groupIds\":\"1,2(用户组ID列表)(必要)\"\n}', 'default', '1', '1', '1', '2022-05-16 17:07:25', NULL, '2022-05-16 17:07:25', '0');
INSERT INTO `jpaas_system`.`sys_external_api`(`ID_`, `SERVICE_`, `API_NAME_`, `PATH_`, `METHOD_`, `PARAMS_`, `HEADERS_`, `BODY_`, `TYPE_`, `TENANT_ID_`, `CREATE_DEP_ID_`, `CREATE_BY_`, `CREATE_TIME_`, `UPDATE_BY_`, `UPDATE_TIME_`, `COMPANY_ID_`) VALUES ('1526128198899470337', 'jpaas-user', '同步用户', '/api/api-user/restApi/user/addOrUpdUsers', 'POST', '[{\"headerName\":\"apiId\",\"headerDesc\":\"接口ID\",\"paraType\":\"String\",\"required\":\"是\",\"serial\":1,\"idx_\":\"jfrvekr898641\"}]', '[]', '{\n    \"tenantId\": \"租户id(必要)\",\n    \"account\":\"操作人员账号(必要)\",\n    \"users\":[\n            {\n            \"userId\":\"用户ID\",\n            \"account\":\"用户账号，唯一标识(必要)\",\n            \"fullname\":\"用户名称(必要)\",\n            \"sex\":\"性别：male（男）、female（女）(必要)\",\n            \"email\":\"电子邮箱(必要)\"，\n            \"mobile\":\"手机号(必要)\",\n            \"qq\":\"qq\",\n            \"address\":\"通讯地址\",\n            \"wxOpenId\":\"用户所在企业微信账号\",\n            \"depId\":\"用户主部门(必要)\",\n            \"status\":\"用户状态：1(在职) 0（离职）(必要)\",\n			\"relations\":[\n			{{\"relationType\":\"3(关系类型Id)(必要)\",\"dimId\":\"用户组维度Id\",\"part1\":\"当前方Id(必要)\"}]\n            }\n    ]\n}', 'default', '1', '1', '1', '2022-05-16 17:11:31', NULL, '2022-05-16 17:11:31', '0');
INSERT INTO `jpaas_system`.`sys_external_api`(`ID_`, `SERVICE_`, `API_NAME_`, `PATH_`, `METHOD_`, `PARAMS_`, `HEADERS_`, `BODY_`, `TYPE_`, `TENANT_ID_`, `CREATE_DEP_ID_`, `CREATE_BY_`, `CREATE_TIME_`, `UPDATE_BY_`, `UPDATE_TIME_`, `COMPANY_ID_`) VALUES ('1526128610662682626', 'jpaas-user', '删除用户', '/api/api-user/restApi/user/deleteUsers', 'POST', '[{\"headerName\":\"apiId\",\"headerDesc\":\"接口ID\",\"paraType\":\"String\",\"required\":\"是\",\"serial\":1,\"idx_\":\"jfrvekr898641\"}]', '[]', '{\n    \"tenantId\": \"租户id,默认webApp(必要)\",\n    \"account\":\"操作人员账号(必要)\",\n    \"userAccounts\":\"用户账号列表，,隔开(必要)\"\n}', 'default', '1', '1', '1', '2022-05-16 17:13:10', NULL, '2022-05-16 17:13:10', '0');
INSERT INTO `jpaas_system`.`sys_external_api`(`ID_`, `SERVICE_`, `API_NAME_`, `PATH_`, `METHOD_`, `PARAMS_`, `HEADERS_`, `BODY_`, `TYPE_`, `TENANT_ID_`, `CREATE_DEP_ID_`, `CREATE_BY_`, `CREATE_TIME_`, `UPDATE_BY_`, `UPDATE_TIME_`, `COMPANY_ID_`) VALUES ('1526128845157830658', 'jpaas-user', '查询所有用户', '/api/api-user/restApi/user/getUsers', 'POST', '[{\"headerName\":\"apiId\",\"headerDesc\":\"接口ID\",\"paraType\":\"String\",\"required\":\"是\",\"serial\":1,\"idx_\":\"jfrvekr898641\"}]', '[]', '{\n    \"pageNo\": 1(当前页码),\n    \"pageSize\": 10(分页大小),\n    \"params\": {}(条件，例：Q_FULLNAME__S_LK),\n    \"sortField\": \"排序字段\",\n    \"sortOrder\": \"排序（asc/desc）\"\n}', 'default', '1', '1', '1', '2022-05-16 17:14:05', NULL, '2022-05-16 17:14:05', '0');
INSERT INTO `jpaas_system`.`sys_external_api`(`ID_`, `SERVICE_`, `API_NAME_`, `PATH_`, `METHOD_`, `PARAMS_`, `HEADERS_`, `BODY_`, `TYPE_`, `TENANT_ID_`, `CREATE_DEP_ID_`, `CREATE_BY_`, `CREATE_TIME_`, `UPDATE_BY_`, `UPDATE_TIME_`, `COMPANY_ID_`) VALUES ('1526129038997590017', 'jpaas-user', '查询所有用户组', '/api/api-user/restApi/user/getGroups', 'POST', '[{\"headerName\":\"apiId\",\"headerDesc\":\"接口ID\",\"paraType\":\"String\",\"required\":\"是\",\"serial\":1,\"idx_\":\"jfrvekr898641\"}]', '[]', '{\n    \"pageNo\": 1(当前页码),\n    \"pageSize\": 10(分页大小),\n    \"params\": {}(条件，例：Q_NAME__S_LK),\n    \"sortField\": \"排序字段\",\n    \"sortOrder\": \"排序（asc/desc）\"\n}', 'default', '1', '1', '1', '2022-05-16 17:14:52', NULL, '2022-05-16 17:14:52', '0');
INSERT INTO `jpaas_system`.`sys_external_api`(`ID_`, `SERVICE_`, `API_NAME_`, `PATH_`, `METHOD_`, `PARAMS_`, `HEADERS_`, `BODY_`, `TYPE_`, `TENANT_ID_`, `CREATE_DEP_ID_`, `CREATE_BY_`, `CREATE_TIME_`, `UPDATE_BY_`, `UPDATE_TIME_`, `COMPANY_ID_`) VALUES ('1526129533740912641', 'jpaas-user', '同步租户', '/api/api-user/restApi/user/syncTenant', 'POST', '[{\"headerName\":\"apiId\",\"headerDesc\":\"接口ID\",\"paraType\":\"String\",\"required\":\"是\",\"serial\":1,\"idx_\":\"jfrvekr898641\"}]', '[]', '[\n    {\n        \"instId\": \"租户ID(必要)\",\n        \"nameCn\": \"租户名称(必要)\",\n        \"instNo\": \"租户实例编码(必要)\",\n        \"parentId\": \"上级租户ID(必要)\",\n        \"status\": \"ENABLED, DISABLED ,可用，不可用,这个可以不传，默认为 ENABLED(必要)\"\n    }\n]', 'default', '1', '1', '1', '2022-05-16 17:16:50', NULL, '2022-05-16 17:16:50', '0');
INSERT INTO `jpaas_system`.`sys_external_api`(`ID_`, `SERVICE_`, `API_NAME_`, `PATH_`, `METHOD_`, `PARAMS_`, `HEADERS_`, `BODY_`, `TYPE_`, `TENANT_ID_`, `CREATE_DEP_ID_`, `CREATE_BY_`, `CREATE_TIME_`, `UPDATE_BY_`, `UPDATE_TIME_`, `COMPANY_ID_`) VALUES ('1526135160978542594', 'jpaas-system', '调用第三方接口接口', '/api/api-system/restApi/system/executeApi', 'POST', '[{\"headerName\":\"apiId\",\"headerDesc\":\"接口ID\",\"paraType\":\"String\",\"required\":\"是\",\"serial\":1,\"idx_\":\"jfrvekr898641\"}]', '[{\"headerName\":\"token\",\"headerDesc\":\"令牌\",\"paraType\":\"String\",\"required\":\"是\",\"serial\":1,\"idx_\":\"wknjfwk937089\"}]', '{}', 'default', '1', '1', '1', '2022-05-16 17:39:11', NULL, '2022-05-16 17:39:11', '0');
INSERT INTO `jpaas_system`.`sys_external_api`(`ID_`, `SERVICE_`, `API_NAME_`, `PATH_`, `METHOD_`, `PARAMS_`, `HEADERS_`, `BODY_`, `TYPE_`, `TENANT_ID_`, `CREATE_DEP_ID_`, `CREATE_BY_`, `CREATE_TIME_`, `UPDATE_BY_`, `UPDATE_TIME_`, `COMPANY_ID_`) VALUES ('1526137157161062402', 'jpaas-system', '统一消息发送接口', '/api/api-system/restApi/system/sendMessageApi', 'POST', '[{\"headerName\":\"apiId\",\"headerDesc\":\"接口ID\",\"paraType\":\"String\",\"required\":\"是\",\"serial\":1,\"idx_\":\"jfrvekr898641\"}]', '[]', '{\n	\'checkType\':\'审批类型数据\',\n	\'boDataMap\':\'表单数据\',\n	\'sender\':\'OsUserDto 消息发送者\',\n	\'msgType\':\'消息类型\',\n	\'receivers\':\'List<OsUserDto> 接收人\',\n	\'subject\':\'主题\',\n	\'content\':\'内容\',\n	\'vars\':\'变量\',\n	\'templateType\':\'模板类型\',\n	\'template\':\'模板内容\',\n	\'tenantId\':\'租户ID\',\n	\'url\':\'消息跳转url\',\n	\'btntxt\':\'消息按钮名称\',\n	\'templateVars\':\'Map<String, String> 模板字符串变量\',\n}', 'default', '1', '1', '1', '2022-05-16 17:47:07', NULL, '2022-05-16 17:47:07', '0');
