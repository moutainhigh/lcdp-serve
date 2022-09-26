use jpaas_bpm;

INSERT INTO bpm_default_template(ID_,TEMPLATE_TYPE_,MESSAGE_TYPE_,TEMPLATE_,DELETED_,COMPANY_ID_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_)
        values(          '1524947548274446338',          'addsign',          'ding',          '${receiver}：
\n您好，您收到一个加签任务：${subject}
\n需要审批,请及时处理!',          0,            '0',          '1',          '1',          '1',          '2022-5-13 11:00:02',          '1',          '2022-5-18 13:52:35');
        
INSERT INTO bpm_default_template(ID_,TEMPLATE_TYPE_,MESSAGE_TYPE_,TEMPLATE_,DELETED_,COMPANY_ID_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_)
        values(          '1524948427518099457',          'addsign',          'inner',          '${receiver}：
\n您好，您收到一个加签任务：<a target=\"_blank\" href=\"${serverUrl}/bpm/workbench/openDoc/${instId}\">${subject}</a>
\n需要审批,请及时处理!
\n
\n',          0,            '0',          '1',          '1',          '1',          '2022-5-13 11:03:32',          null,          '2022-5-13 11:03:32');
        
INSERT INTO bpm_default_template(ID_,TEMPLATE_TYPE_,MESSAGE_TYPE_,TEMPLATE_,DELETED_,COMPANY_ID_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_)
        values(          '1524948577879703553',          'addsign',          'weixin',          '${receiver}：
\n您好，您收到一个加签任务：${subject}
\n需要审批,请及时处理!
\n',          0,            '0',          '1',          '1',          '1',          '2022-5-13 11:04:08',          null,          '2022-5-13 11:04:08');
        
INSERT INTO bpm_default_template(ID_,TEMPLATE_TYPE_,MESSAGE_TYPE_,TEMPLATE_,DELETED_,COMPANY_ID_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_)
        values(          '1524948673547583490',          'addsign',          'mail',          '${receiver}：
\n您好，您收到一个加签任务：<a target=\"_blank\" href=\"${serverUrl}/bpm/workbench/openDoc/${instId}\">${subject}</a>
\n需要审批,请及时处理!
\n',          0,            '0',          '1',          '1',          '1',          '2022-5-13 11:04:31',          null,          '2022-5-13 11:04:31');
        
INSERT INTO bpm_default_template(ID_,TEMPLATE_TYPE_,MESSAGE_TYPE_,TEMPLATE_,DELETED_,COMPANY_ID_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_)
        values(          '1524948737682685954',          'addsign',          'sms',          '${receiver}：您收到一个加签任务：${subject}
\n需要审批,请及时处理!
\n',          0,            '0',          '1',          '1',          '1',          '2022-5-13 11:04:46',          null,          '2022-5-13 11:04:46');
        
INSERT INTO bpm_default_template(ID_,TEMPLATE_TYPE_,MESSAGE_TYPE_,TEMPLATE_,DELETED_,COMPANY_ID_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_)
        values(          '1524949936792256514',          'agent',          'ding',          '${receiver}：
\n您好，您的待办任务：${subject}
\n代理给了${agentReceiver}.
\n',          0,            '0',          '1',          '1',          '1',          '2022-5-13 11:09:32',          null,          '2022-5-13 11:09:32');
        
INSERT INTO bpm_default_template(ID_,TEMPLATE_TYPE_,MESSAGE_TYPE_,TEMPLATE_,DELETED_,COMPANY_ID_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_)
        values(          '1524950087032225793',          'agent',          'weixin',          '${receiver}：
\n您好，您的待办任务：${subject}
\n代理给了${agentReceiver}.
\n',          0,            '0',          '1',          '1',          '1',          '2022-5-13 11:10:08',          null,          '2022-5-13 11:10:08');
        
INSERT INTO bpm_default_template(ID_,TEMPLATE_TYPE_,MESSAGE_TYPE_,TEMPLATE_,DELETED_,COMPANY_ID_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_)
        values(          '1524950223170945026',          'agent',          'mail',          '${receiver}：
\n您好，您的待办任务：<a target=\"_blank\" href=\"${serverUrl}/bpm/workbench/openDoc/${instId}\">${subject}</a>
\n代理给了${agentReceiver}.
\n',          0,            '0',          '1',          '1',          '1',          '2022-5-13 11:10:40',          null,          '2022-5-13 11:10:40');
        
INSERT INTO bpm_default_template(ID_,TEMPLATE_TYPE_,MESSAGE_TYPE_,TEMPLATE_,DELETED_,COMPANY_ID_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_)
        values(          '1524950305769373698',          'agent',          'sms',          '${receiver}：您的待办任务：${subject}
\n代理给了${agentReceiver}.
\n',          0,            '0',          '1',          '1',          '1',          '2022-5-13 11:11:00',          null,          '2022-5-13 11:11:00');
        
INSERT INTO bpm_default_template(ID_,TEMPLATE_TYPE_,MESSAGE_TYPE_,TEMPLATE_,DELETED_,COMPANY_ID_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_)
        values(          '1524950374442713090',          'agent',          'inner',          '${receiver}：
\n您好，您的待办任务：<a target=\"_blank\" href=\"${serverUrl}/bpm/workbench/openDoc/${instId}\">${subject}</a>
\n代理给了${agentReceiver}.
\n',          0,            '0',          '1',          '1',          '1',          '2022-5-13 11:11:16',          null,          '2022-5-13 11:11:16');
        
INSERT INTO bpm_default_template(ID_,TEMPLATE_TYPE_,MESSAGE_TYPE_,TEMPLATE_,DELETED_,COMPANY_ID_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_)
        values(          '1524951376004759554',          'agentTo',          'ding',          '${receiver}：
\n您好，您收到一个代理待办任务：${subject}
\n代理人：${agent},需要审批,请及时处理!
\n',          0,            '0',          '1',          '1',          '1',          '2022-5-13 11:15:15',          null,          '2022-5-13 11:15:15');
        
INSERT INTO bpm_default_template(ID_,TEMPLATE_TYPE_,MESSAGE_TYPE_,TEMPLATE_,DELETED_,COMPANY_ID_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_)
        values(          '1524951601280827394',          'agentTo',          'weixin',          '${receiver}：
\n您好，您收到一个代理待办任务：${subject}
\n代理人：${agent},需要审批,请及时处理!
\n',          0,            '0',          '1',          '1',          '1',          '2022-5-13 11:16:09',          null,          '2022-5-13 11:16:09');
        
INSERT INTO bpm_default_template(ID_,TEMPLATE_TYPE_,MESSAGE_TYPE_,TEMPLATE_,DELETED_,COMPANY_ID_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_)
        values(          '1524951656763080705',          'agentTo',          'mail',          '${receiver}：
\n您好，您收到一个代理待办任务：<a target=\"_blank\" href=\"${serverUrl}/bpm/workbench/openDoc/${instId}\">${subject}</a>
\n代理人：${agent},需要审批,请及时处理!
\n',          0,            '0',          '1',          '1',          '1',          '2022-5-13 11:16:22',          null,          '2022-5-13 11:16:22');
        
INSERT INTO bpm_default_template(ID_,TEMPLATE_TYPE_,MESSAGE_TYPE_,TEMPLATE_,DELETED_,COMPANY_ID_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_)
        values(          '1524951768151212033',          'agentTo',          'sms',          '${receiver}：您收到一个代理待办任务：${subject}
\n代理人：${agent},需要审批,请及时处理!
\n',          0,            '0',          '1',          '1',          '1',          '2022-5-13 11:16:48',          null,          '2022-5-13 11:16:48');
        
INSERT INTO bpm_default_template(ID_,TEMPLATE_TYPE_,MESSAGE_TYPE_,TEMPLATE_,DELETED_,COMPANY_ID_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_)
        values(          '1524951836648390657',          'agentTo',          'inner',          '${receiver}：
\n您好，您收到一个代理待办任务：<a target=\"_blank\" href=\"${serverUrl}/bpm/workbench/openDoc/${instId}\">${subject}</a>
\n代理人：${agent},需要审批,请及时处理!
\n',          0,            '0',          '1',          '1',          '1',          '2022-5-13 11:17:05',          null,          '2022-5-13 11:17:05');
        
INSERT INTO bpm_default_template(ID_,TEMPLATE_TYPE_,MESSAGE_TYPE_,TEMPLATE_,DELETED_,COMPANY_ID_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_)
        values(          '1524952390388793346',          'approve',          'ding',          '${receiver}：
\n您好，您收到一个待办任务：${subject}
\n需要审批,请及时处理!
\n',          0,            '0',          '1',          '1',          '1',          '2022-5-13 11:19:17',          null,          '2022-5-13 11:19:17');
        
INSERT INTO bpm_default_template(ID_,TEMPLATE_TYPE_,MESSAGE_TYPE_,TEMPLATE_,DELETED_,COMPANY_ID_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_)
        values(          '1524952844149571585',          'approve',          'weixin',          '${receiver}：
\n您好，您收到一个待办任务：${subject}
\n需要审批,请及时处理!
\n',          0,            '0',          '1',          '1',          '1',          '2022-5-13 11:21:05',          null,          '2022-5-13 11:21:05');
        
INSERT INTO bpm_default_template(ID_,TEMPLATE_TYPE_,MESSAGE_TYPE_,TEMPLATE_,DELETED_,COMPANY_ID_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_)
        values(          '1524952923082178561',          'approve',          'mail',          '${receiver}：
\n您好，您收到一个待办任务：<a target=\"_blank\" href=\"${serverUrl}/bpm/workbench/openDoc/${instId}\">${subject}</a>
\n需要审批,请及时处理!
\n',          0,            '0',          '1',          '1',          '1',          '2022-5-13 11:21:24',          null,          '2022-5-13 11:21:24');
        
INSERT INTO bpm_default_template(ID_,TEMPLATE_TYPE_,MESSAGE_TYPE_,TEMPLATE_,DELETED_,COMPANY_ID_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_)
        values(          '1524953214506614785',          'approve',          'sms',          '${receiver}：您收到一个待办任务：${subject}
\n需要审批,请及时处理!
\n',          0,            '0',          '1',          '1',          '1',          '2022-5-13 11:22:33',          null,          '2022-5-13 11:22:33');
        
INSERT INTO bpm_default_template(ID_,TEMPLATE_TYPE_,MESSAGE_TYPE_,TEMPLATE_,DELETED_,COMPANY_ID_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_)
        values(          '1524953293225312257',          'approve',          'inner',          '${receiver}：
\n您好，您收到一个待办任务：<a target=\"_blank\" href=\"${serverUrl}/bpm/workbench/openDoc/${instId}\">${subject}</a>
\n需要审批,请及时处理!
\n',          0,            '0',          '1',          '1',          '1',          '2022-5-13 11:22:52',          null,          '2022-5-13 11:22:52');
        
INSERT INTO bpm_default_template(ID_,TEMPLATE_TYPE_,MESSAGE_TYPE_,TEMPLATE_,DELETED_,COMPANY_ID_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_)
        values(          '1524955002794262530',          'back',          'ding',          '${receiver}：
\n您好，您的申请被驳回：${subject}
\n需要办理,请及时处理!
\n',          0,            '0',          '1',          '1',          '1',          '2022-5-13 11:29:40',          null,          '2022-5-13 11:29:40');
        
INSERT INTO bpm_default_template(ID_,TEMPLATE_TYPE_,MESSAGE_TYPE_,TEMPLATE_,DELETED_,COMPANY_ID_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_)
        values(          '1524955151146795010',          'back',          'weixin',          '${receiver}：
\n您好，您的申请被驳回：<a target=\"_blank\" href=\"${serverUrl}/bpm/workbench/openDoc/${instId}\">${subject}</a>
\n需要办理,请及时处理!
\n',          0,            '0',          '1',          '1',          '1',          '2022-5-13 11:30:15',          null,          '2022-5-13 11:30:15');
        
INSERT INTO bpm_default_template(ID_,TEMPLATE_TYPE_,MESSAGE_TYPE_,TEMPLATE_,DELETED_,COMPANY_ID_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_)
        values(          '1524955211649630210',          'back',          'mail',          '${receiver}：
\n您好，您的申请被驳回：<a target=\"_blank\" href=\"${serverUrl}/bpm/workbench/openDoc/${instId}\">${subject}</a>
\n需要办理,请及时处理!
\n',          0,            '0',          '1',          '1',          '1',          '2022-5-13 11:30:29',          null,          '2022-5-13 11:30:29');
        
INSERT INTO bpm_default_template(ID_,TEMPLATE_TYPE_,MESSAGE_TYPE_,TEMPLATE_,DELETED_,COMPANY_ID_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_)
        values(          '1524955276384518145',          'back',          'sms',          '${receiver}：您的申请被驳回：${subject}
\n需要办理,请及时处理!
\n',          0,            '0',          '1',          '1',          '1',          '2022-5-13 11:30:45',          null,          '2022-5-13 11:30:45');
        
INSERT INTO bpm_default_template(ID_,TEMPLATE_TYPE_,MESSAGE_TYPE_,TEMPLATE_,DELETED_,COMPANY_ID_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_)
        values(          '1524955343099117570',          'back',          'inner',          '${receiver}：
\n您好，您的申请被驳回：<a target=\"_blank\" href=\"${serverUrl}/bpm/workbench/openDoc/${instId}\">${subject}</a>
\n需要办理,请及时处理!
\n',          0,            '0',          '1',          '1',          '1',          '2022-5-13 11:31:01',          null,          '2022-5-13 11:31:01');
        
INSERT INTO bpm_default_template(ID_,TEMPLATE_TYPE_,MESSAGE_TYPE_,TEMPLATE_,DELETED_,COMPANY_ID_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_)
        values(          '1524955793663836161',          'backToStart',          'ding',          '${receiver}：
\n您好, 您的申请被驳回：${subject}
\n需要办理, 请及时处理!
\n',          0,            '0',          '1',          '1',          '1',          '2022-5-13 11:32:48',          null,          '2022-5-13 11:32:48');
        
INSERT INTO bpm_default_template(ID_,TEMPLATE_TYPE_,MESSAGE_TYPE_,TEMPLATE_,DELETED_,COMPANY_ID_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_)
        values(          '1524955906675163138',          'backToStart',          'weixin',          '${receiver}：
\n您好, 您的申请被驳回：${subject}
\n需要办理, 请及时处理!
\n',          0,            '0',          '1',          '1',          '1',          '2022-5-13 11:33:15',          null,          '2022-5-13 11:33:15');
        
INSERT INTO bpm_default_template(ID_,TEMPLATE_TYPE_,MESSAGE_TYPE_,TEMPLATE_,DELETED_,COMPANY_ID_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_)
        values(          '1524955971837870082',          'backToStart',          'mail',          '${receiver}：
\n您好，您的申请被驳回：<a target=\"_blank\" href=\"${serverUrl}/bpm/workbench/openDoc/${instId}\">${subject}</a>
\n需要办理,请及时处理!
\n',          0,            '0',          '1',          '1',          '1',          '2022-5-13 11:33:31',          null,          '2022-5-13 11:33:31');
        
INSERT INTO bpm_default_template(ID_,TEMPLATE_TYPE_,MESSAGE_TYPE_,TEMPLATE_,DELETED_,COMPANY_ID_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_)
        values(          '1524956041979215873',          'backToStart',          'sms',          '${receiver}：您的申请被驳回：${subject}
\n需要办理,请及时处理!
\n',          0,            '0',          '1',          '1',          '1',          '2022-5-13 11:33:47',          null,          '2022-5-13 11:33:47');
        
INSERT INTO bpm_default_template(ID_,TEMPLATE_TYPE_,MESSAGE_TYPE_,TEMPLATE_,DELETED_,COMPANY_ID_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_)
        values(          '1524956168571699201',          'backToStart',          'inner',          '${receiver}：
\n您好，您的申请被驳回：<a target=\"_blank\" href=\"${serverUrl}/bpm/workbench/openDoc/${instId}\">${subject}</a>
\n需要办理,请及时处理!
\n',          0,            '0',          '1',          '1',          '1',          '2022-5-13 11:34:18',          null,          '2022-5-13 11:34:18');
        
INSERT INTO bpm_default_template(ID_,TEMPLATE_TYPE_,MESSAGE_TYPE_,TEMPLATE_,DELETED_,COMPANY_ID_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_)
        values(          '1524957026327838721',          'commu',          'ding',          '${receiver}：
\n您好, ${sender}发给您一个沟通消息：${subject}
\n需要审批,请及时处理!
\n',          0,            '0',          '1',          '1',          '1',          '2022-5-13 11:37:42',          null,          '2022-5-13 11:37:42');
        
INSERT INTO bpm_default_template(ID_,TEMPLATE_TYPE_,MESSAGE_TYPE_,TEMPLATE_,DELETED_,COMPANY_ID_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_)
        values(          '1524957115179974658',          'commu',          'weixin',          '${receiver}：
\n您好, ${sender}发给您一个沟通消息：${subject}
\n需要审批,请及时处理!
\n',          0,            '0',          '1',          '1',          '1',          '2022-5-13 11:38:03',          null,          '2022-5-13 11:38:03');
        
INSERT INTO bpm_default_template(ID_,TEMPLATE_TYPE_,MESSAGE_TYPE_,TEMPLATE_,DELETED_,COMPANY_ID_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_)
        values(          '1524957272227299330',          'commu',          'mail',          '${receiver}：
\n您好, ${sender}发给您一个沟通消息：<a target=\"_blank\" href=\"${serverUrl}/bpm/workbench/openDoc/${instId}\">${subject}</a>
\n',          0,            '0',          '1',          '1',          '1',          '2022-5-13 11:38:41',          null,          '2022-5-13 11:38:41');
        
INSERT INTO bpm_default_template(ID_,TEMPLATE_TYPE_,MESSAGE_TYPE_,TEMPLATE_,DELETED_,COMPANY_ID_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_)
        values(          '1524957374140497922',          'commu',          'sms',          '${receiver}：${sender}发给您一个沟通消息：${subject}
\n',          0,            '0',          '1',          '1',          '1',          '2022-5-13 11:39:05',          null,          '2022-5-13 11:39:05');
        
INSERT INTO bpm_default_template(ID_,TEMPLATE_TYPE_,MESSAGE_TYPE_,TEMPLATE_,DELETED_,COMPANY_ID_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_)
        values(          '1524957464376754177',          'commu',          'inner',          '${receiver}：
\n您好, ${sender}发给您一个沟通消息：<a target=\"_blank\" href=\"${serverUrl}/bpm/workbench/openDoc/${instId}\">${subject}</a>
\n',          0,            '0',          '1',          '1',          '1',          '2022-5-13 11:39:27',          null,          '2022-5-13 11:39:27');
        
INSERT INTO bpm_default_template(ID_,TEMPLATE_TYPE_,MESSAGE_TYPE_,TEMPLATE_,DELETED_,COMPANY_ID_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_)
        values(          '1524957642043277314',          'copy',          'ding',          '${receiver}：
\n您好, ${sender}抄送给您一条流程实例信息：${subject},请查收!',          0,            '0',          '1',          '1',          '1',          '2022-5-13 11:40:09',          null,          '2022-5-13 11:40:09');
        
INSERT INTO bpm_default_template(ID_,TEMPLATE_TYPE_,MESSAGE_TYPE_,TEMPLATE_,DELETED_,COMPANY_ID_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_)
        values(          '1524957737153314818',          'copy',          'weixin',          '${receiver}：
\n您好, ${sender}抄送给您一条流程实例信息：${subject},请查收!
\n',          0,            '0',          '1',          '1',          '1',          '2022-5-13 11:40:32',          null,          '2022-5-13 11:40:32');
        
INSERT INTO bpm_default_template(ID_,TEMPLATE_TYPE_,MESSAGE_TYPE_,TEMPLATE_,DELETED_,COMPANY_ID_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_)
        values(          '1524957947367636993',          'copy',          'mail',          '${receiver}：
\n您好, ${sender}抄送给您一条流程实例信息：<a href=\"${serverUrl}/bpm/workbench/openDoc/${instId}\" target=\"_blank\">${subject}</a>,请查收!
\n',          0,            '0',          '1',          '1',          '1',          '2022-5-13 11:41:22',          null,          '2022-5-13 11:41:22');
        
INSERT INTO bpm_default_template(ID_,TEMPLATE_TYPE_,MESSAGE_TYPE_,TEMPLATE_,DELETED_,COMPANY_ID_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_)
        values(          '1524958039982063618',          'copy',          'sms',          '${receiver}：${sender}抄送给您一条流程实例信息：${subject},请查收!
\n',          0,            '0',          '1',          '1',          '1',          '2022-5-13 11:41:44',          null,          '2022-5-13 11:41:44');
        
INSERT INTO bpm_default_template(ID_,TEMPLATE_TYPE_,MESSAGE_TYPE_,TEMPLATE_,DELETED_,COMPANY_ID_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_)
        values(          '1524958101055324161',          'copy',          'inner',          '${receiver}：
\n您好, ${sender}抄送给您一条流程实例信息：<a href=\"${serverUrl}/bpm/workbench/openDoc/${instId}\" target=\"_blank\">${subject}</a>,请查收!
\n',          0,            '0',          '1',          '1',          '1',          '2022-5-13 11:41:58',          null,          '2022-5-13 11:41:58');
        
INSERT INTO bpm_default_template(ID_,TEMPLATE_TYPE_,MESSAGE_TYPE_,TEMPLATE_,DELETED_,COMPANY_ID_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_)
        values(          '1524958655592644610',          'forward',          'ding',          '${receiver}：
\n您好，${sender}转发给您一条流程实例信息：${subject}, 请查收!
\n',          0,            '0',          '1',          '1',          '1',          '2022-5-13 11:44:11',          null,          '2022-5-13 11:44:11');
        
INSERT INTO bpm_default_template(ID_,TEMPLATE_TYPE_,MESSAGE_TYPE_,TEMPLATE_,DELETED_,COMPANY_ID_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_)
        values(          '1524958835641532417',          'forward',          'weixin',          '${receiver}：
\n您好，${sender}转发给您一条流程实例信息：${subject}, 请查收!
\n',          0,            '0',          '1',          '1',          '1',          '2022-5-13 11:44:53',          null,          '2022-5-13 11:44:53');
        
INSERT INTO bpm_default_template(ID_,TEMPLATE_TYPE_,MESSAGE_TYPE_,TEMPLATE_,DELETED_,COMPANY_ID_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_)
        values(          '1524958927433875458',          'forward',          'mail',          '${receiver}：
\n您好，${sender}转发给您一条流程实例信息：<a href=\"${serverUrl}/bpm/workbench/openDoc/${instId}\" target=\"_blank\">${subject}</a>,请查收!
\n',          0,            '0',          '1',          '1',          '1',          '2022-5-13 11:45:15',          null,          '2022-5-13 11:45:15');
        
INSERT INTO bpm_default_template(ID_,TEMPLATE_TYPE_,MESSAGE_TYPE_,TEMPLATE_,DELETED_,COMPANY_ID_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_)
        values(          '1524959012624384002',          'forward',          'sms',          '${receiver}：您好，${sender}转发给您一条流程实例信息：${subject},请查收!
\n',          0,            '0',          '1',          '1',          '1',          '2022-5-13 11:45:36',          null,          '2022-5-13 11:45:36');
        
INSERT INTO bpm_default_template(ID_,TEMPLATE_TYPE_,MESSAGE_TYPE_,TEMPLATE_,DELETED_,COMPANY_ID_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_)
        values(          '1524959075304062977',          'forward',          'inner',          '${receiver}：
\n您好，${sender}转发给您一条流程实例信息：<a href=\"${serverUrl}/bpm/workbench/openDoc/${instId}\" target=\"_blank\">${subject}</a>,请查收!
\n',          0,            '0',          '1',          '1',          '1',          '2022-5-13 11:45:51',          null,          '2022-5-13 11:45:51');
        
INSERT INTO bpm_default_template(ID_,TEMPLATE_TYPE_,MESSAGE_TYPE_,TEMPLATE_,DELETED_,COMPANY_ID_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_)
        values(          '1524959367735132162',          'remind',          'ding',          '${receiver}：
\n您的收到了一个催办信息,任务：<a target=\"_blank\" href=\"${serverUrl}/bpm/workbench/openDoc/${instId}\">${subject}</a>请尽快处理.
\n',          0,            '0',          '1',          '1',          '1',          '2022-5-13 11:47:00',          null,          '2022-5-13 11:47:00');
        
INSERT INTO bpm_default_template(ID_,TEMPLATE_TYPE_,MESSAGE_TYPE_,TEMPLATE_,DELETED_,COMPANY_ID_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_)
        values(          '1524959704600657922',          'remind',          'weixin',          '${receiver}：
\n您好，您的收到了一个催办信息：${subject}, 请查收!
\n',          0,            '0',          '1',          '1',          '1',          '2022-5-13 11:48:21',          null,          '2022-5-13 11:48:21');
        
INSERT INTO bpm_default_template(ID_,TEMPLATE_TYPE_,MESSAGE_TYPE_,TEMPLATE_,DELETED_,COMPANY_ID_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_)
        values(          '1524959775383732225',          'remind',          'mail',          '${receiver}：
\n    您的收到了一个催办信息,任务：<a target=\"_blank\" href=\"${serverUrl}/bpm/workbench/openDoc/${instId}\">${subject}</a>请尽快处理.',          0,            '0',          '1',          '1',          '1',          '2022-5-13 11:48:38',          null,          '2022-5-13 11:48:38');
        
INSERT INTO bpm_default_template(ID_,TEMPLATE_TYPE_,MESSAGE_TYPE_,TEMPLATE_,DELETED_,COMPANY_ID_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_)
        values(          '1524959902898962434',          'remind',          'sms',          '${receiver}：
\n您好，您的收到了一个催办信息：${subject}, 请查收!
\n',          0,            '0',          '1',          '1',          '1',          '2022-5-13 11:49:08',          null,          '2022-5-13 11:49:08');
        
INSERT INTO bpm_default_template(ID_,TEMPLATE_TYPE_,MESSAGE_TYPE_,TEMPLATE_,DELETED_,COMPANY_ID_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_)
        values(          '1524959974462177282',          'remind',          'inner',          '${receiver}：
\n	您的收到了一个催办信息,任务：<a target=\"_blank\" href=\"${serverUrl}/bpm/workbench/openDoc/${instId}\">${subject}</a>请尽快处理.',          0,            '0',          '1',          '1',          '1',          '2022-5-13 11:49:25',          null,          '2022-5-13 11:49:25');
        
INSERT INTO bpm_default_template(ID_,TEMPLATE_TYPE_,MESSAGE_TYPE_,TEMPLATE_,DELETED_,COMPANY_ID_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_)
        values(          '1524960289034977282',          'replycommu',          'ding',          '${receiver}：
\n您好，${sender}回复了您的沟通：${subject}
\n',          0,            '0',          '1',          '1',          '1',          '2022-5-13 11:50:40',          null,          '2022-5-13 11:50:40');
        
INSERT INTO bpm_default_template(ID_,TEMPLATE_TYPE_,MESSAGE_TYPE_,TEMPLATE_,DELETED_,COMPANY_ID_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_)
        values(          '1524960489740812289',          'replycommu',          'weixin',          '${receiver}：
\n您好，${sender}回复了您的沟通：${subject}
\n',          0,            '0',          '1',          '1',          '1',          '2022-5-13 11:51:28',          null,          '2022-5-13 11:51:28');
        
INSERT INTO bpm_default_template(ID_,TEMPLATE_TYPE_,MESSAGE_TYPE_,TEMPLATE_,DELETED_,COMPANY_ID_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_)
        values(          '1524960595357581314',          'replycommu',          'mail',          '${receiver}：
\n您好，${sender}回复了您的沟通：<a target=\"_blank\" href=\"${serverUrl}/bpm/workbench/openDoc/${instId}\">${subject}</a>
\n',          0,            '0',          '1',          '1',          '1',          '2022-5-13 11:51:53',          null,          '2022-5-13 11:51:53');
        
INSERT INTO bpm_default_template(ID_,TEMPLATE_TYPE_,MESSAGE_TYPE_,TEMPLATE_,DELETED_,COMPANY_ID_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_)
        values(          '1524960722138808322',          'replycommu',          'sms',          '${receiver}：${sender}回复了您的沟通：${subject}
\n',          0,            '0',          '1',          '1',          '1',          '2022-5-13 11:52:23',          null,          '2022-5-13 11:52:23');
        
INSERT INTO bpm_default_template(ID_,TEMPLATE_TYPE_,MESSAGE_TYPE_,TEMPLATE_,DELETED_,COMPANY_ID_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_)
        values(          '1524960781538541570',          'replycommu',          'inner',          '${receiver}：
\n您好，${sender}回复了您的沟通：<a target=\"_blank\" href=\"${serverUrl}/bpm/workbench/openDoc/${instId}\">${subject}</a>
\n',          0,            '0',          '1',          '1',          '1',          '2022-5-13 11:52:37',          null,          '2022-5-13 11:52:37');
        
INSERT INTO bpm_default_template(ID_,TEMPLATE_TYPE_,MESSAGE_TYPE_,TEMPLATE_,DELETED_,COMPANY_ID_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_)
        values(          '1524960946588598274',          'revoke',          'ding',          '${receiver}：
\n您好，	您的任务被撤销：${subject}
\n需要办理,请及时处理!
\n',          0,            '0',          '1',          '1',          '1',          '2022-5-13 11:53:17',          null,          '2022-5-13 11:53:17');
        
INSERT INTO bpm_default_template(ID_,TEMPLATE_TYPE_,MESSAGE_TYPE_,TEMPLATE_,DELETED_,COMPANY_ID_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_)
        values(          '1524961043594461186',          'revoke',          'weixin',          '${receiver}：
\n您好，	您的任务被撤销：<a target=\"_blank\" href=\"${serverUrl}/bpm/workbench/openDoc/${instId}\">${subject}</a>
\n',          0,            '0',          '1',          '1',          '1',          '2022-5-13 11:53:40',          null,          '2022-5-13 11:53:40');
        
INSERT INTO bpm_default_template(ID_,TEMPLATE_TYPE_,MESSAGE_TYPE_,TEMPLATE_,DELETED_,COMPANY_ID_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_)
        values(          '1524961152319209474',          'revoke',          'mail',          '${receiver}：
\n您好，	您的任务被撤销：<a target=\"_blank\" href=\"${serverUrl}/bpm/workbench/openDoc/${instId}\">${subject}</a>
\n',          0,            '0',          '1',          '1',          '1',          '2022-5-13 11:54:06',          null,          '2022-5-13 11:54:06');
        
INSERT INTO bpm_default_template(ID_,TEMPLATE_TYPE_,MESSAGE_TYPE_,TEMPLATE_,DELETED_,COMPANY_ID_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_)
        values(          '1524961611742298113',          'revoke',          'sms',          '${receiver}：您的任务被撤销：${subject}
\n',          0,            '0',          '1',          '1',          '1',          '2022-5-13 11:55:55',          null,          '2022-5-13 11:55:55');
        
INSERT INTO bpm_default_template(ID_,TEMPLATE_TYPE_,MESSAGE_TYPE_,TEMPLATE_,DELETED_,COMPANY_ID_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_)
        values(          '1524961685058732033',          'revoke',          'inner',          '${receiver}：
\n您好，	您的任务被撤销：<a target=\"_blank\" href=\"${serverUrl}/bpm/workbench/openDoc/${instId}\">${subject}</a>
\n',          0,            '0',          '1',          '1',          '1',          '2022-5-13 11:56:13',          null,          '2022-5-13 11:56:13');
        
INSERT INTO bpm_default_template(ID_,TEMPLATE_TYPE_,MESSAGE_TYPE_,TEMPLATE_,DELETED_,COMPANY_ID_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_)
        values(          '1524961951749357570',          'transfer',          'ding',          '${receiver}：
\n${sender}转发给您一个任务：${subject},需要审批,请及时处理!
\n发送人意见： ${opinion}
\n',          0,            '0',          '1',          '1',          '1',          '2022-5-13 11:57:16',          null,          '2022-5-13 11:57:16');
        
INSERT INTO bpm_default_template(ID_,TEMPLATE_TYPE_,MESSAGE_TYPE_,TEMPLATE_,DELETED_,COMPANY_ID_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_)
        values(          '1524962487244537858',          'transfer',          'weixin',          '${receiver}：
\n${sender}转发给您一个任务：${subject},需要审批,请及时处理!
\n发送人意见： ${opinion}
\n',          0,            '0',          '1',          '1',          '1',          '2022-5-13 11:59:24',          null,          '2022-5-13 11:59:24');
        
INSERT INTO bpm_default_template(ID_,TEMPLATE_TYPE_,MESSAGE_TYPE_,TEMPLATE_,DELETED_,COMPANY_ID_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_)
        values(          '1524962596728455170',          'transfer',          'mail',          '${receiver}：
\n${sender}转发给您一个任务：<a target=\"_blank\" href=\"${serverUrl}/bpm/workbench/openDoc/${instId}\">${subject}</a>,需要审批,请及时处理!
\n发送人意见： ${opinion}
\n',          0,            '0',          '1',          '1',          '1',          '2022-5-13 11:59:50',          null,          '2022-5-13 11:59:50');
        
INSERT INTO bpm_default_template(ID_,TEMPLATE_TYPE_,MESSAGE_TYPE_,TEMPLATE_,DELETED_,COMPANY_ID_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_)
        values(          '1524962779960819714',          'transfer',          'sms',          '${receiver}：
\n    ${sender}转发给您一个任务：${subject}需要审批,请及时处理 
\n   发送人意见： ${opinion}
\n',          0,            '0',          '1',          '1',          '1',          '2022-5-13 12:00:34',          null,          '2022-5-13 12:00:34');
        
INSERT INTO bpm_default_template(ID_,TEMPLATE_TYPE_,MESSAGE_TYPE_,TEMPLATE_,DELETED_,COMPANY_ID_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_)
        values(          '1524962847275204610',          'transfer',          'inner',          '${receiver}：
\n	${sender}转发给您一个任务：<a target=\"_blank\" href=\"${serverUrl}/bpm/workbench/openDoc/${instId}\">${subject}</a>,需要审批,请及时处理!
\n	发送人意见： ${opinion}
\n',          0,            '0',          '1',          '1',          '1',          '2022-5-13 12:00:50',          null,          '2022-5-13 12:00:50');
        
INSERT INTO bpm_default_template(ID_,TEMPLATE_TYPE_,MESSAGE_TYPE_,TEMPLATE_,DELETED_,COMPANY_ID_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_)
        values(          '1524969814110617602',          'transferRoam',          'ding',          '${receiver}：
\n${sender}流转给您一个任务：${subject},需要审批,请及时处理!
\n发送人意见： ${opinion}
\n',          0,            '0',          '1',          '1',          '1',          '2022-5-13 12:28:31',          null,          '2022-5-13 12:28:31');
        
INSERT INTO bpm_default_template(ID_,TEMPLATE_TYPE_,MESSAGE_TYPE_,TEMPLATE_,DELETED_,COMPANY_ID_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_)
        values(          '1524969920759185409',          'transferRoam',          'weixin',          '${receiver}：
\n	${sender}流转给您一个任务：<a target=\"_blank\" href=\"${serverUrl}/bpm/workbench/openDoc/${instId}\">${subject}</a>,需要审批,请及时处理!
\n	发送人意见： ${opinion}
\n',          0,            '0',          '1',          '1',          '1',          '2022-5-13 12:28:56',          null,          '2022-5-13 12:28:56');
        
INSERT INTO bpm_default_template(ID_,TEMPLATE_TYPE_,MESSAGE_TYPE_,TEMPLATE_,DELETED_,COMPANY_ID_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_)
        values(          '1524970010664091650',          'transferRoam',          'mail',          '${receiver}：
\n${sender}流转给您一个任务：<a target=\"_blank\" href=\"${serverUrl}/bpm/workbench/openDoc/${instId}\">${subject}</a>,需要审批,请及时处理!
\n发送人意见： ${opinion}
\n',          0,            '0',          '1',          '1',          '1',          '2022-5-13 12:29:18',          null,          '2022-5-13 12:29:18');
        
INSERT INTO bpm_default_template(ID_,TEMPLATE_TYPE_,MESSAGE_TYPE_,TEMPLATE_,DELETED_,COMPANY_ID_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_)
        values(          '1524970072655904769',          'transferRoam',          'sms',          '${receiver}：${sender}流转给您一个任务：${subject}需要审批,请及时处理!
\n发送人意见： ${opinion}
\n',          0,            '0',          '1',          '1',          '1',          '2022-5-13 12:29:33',          null,          '2022-5-13 12:29:33');
        
INSERT INTO bpm_default_template(ID_,TEMPLATE_TYPE_,MESSAGE_TYPE_,TEMPLATE_,DELETED_,COMPANY_ID_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_)
        values(          '1524970241375977473',          'transferRoam',          'inner',          '${receiver}：
\n	${sender}流转给您一个任务：<a target=\"_blank\" href=\"${serverUrl}/bpm/workbench/openDoc/${instId}\">${subject}</a>,需要审批,请及时处理!
\n	发送人意见： ${opinion}
\n',          0,            '0',          '1',          '1',          '1',          '2022-5-13 12:30:13',          null,          '2022-5-13 12:30:13');
        
INSERT INTO bpm_default_template(ID_,TEMPLATE_TYPE_,MESSAGE_TYPE_,TEMPLATE_,DELETED_,COMPANY_ID_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_)
        values(          '1524970322711920642',          'urge',          'ding',          '${receiver}：
\n${sender}提交了一个催办：${subject}  ${opinion}
\n需要审批,请及时处理!
\n',          0,            '0',          '1',          '1',          '1',          '2022-5-13 12:30:32',          null,          '2022-5-13 12:30:32');
        
INSERT INTO bpm_default_template(ID_,TEMPLATE_TYPE_,MESSAGE_TYPE_,TEMPLATE_,DELETED_,COMPANY_ID_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_)
        values(          '1524970450239733762',          'urge',          'weixin',          '${receiver}：
\n	${sender}提交了一个催办：${subject}  ${opinion}
\n需要审批,请及时处理!
\n',          0,            '0',          '1',          '1',          '1',          '2022-5-13 12:31:03',          null,          '2022-5-13 12:31:03');
        
INSERT INTO bpm_default_template(ID_,TEMPLATE_TYPE_,MESSAGE_TYPE_,TEMPLATE_,DELETED_,COMPANY_ID_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_)
        values(          '1524970574231748610',          'urge',          'mail',          '${receiver}：
\n${sender}提交了一个催办：<a target=\"_blank\" href=\"${serverUrl}/bpm/workbench/openDoc/${instId}\">${subject} ${opinion}</a>
\n需要审批,请及时处理!
\n',          0,            '0',          '1',          '1',          '1',          '2022-5-13 12:31:32',          null,          '2022-5-13 12:31:32');
        
INSERT INTO bpm_default_template(ID_,TEMPLATE_TYPE_,MESSAGE_TYPE_,TEMPLATE_,DELETED_,COMPANY_ID_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_)
        values(          '1524970634826858497',          'urge',          'sms',          '${receiver}：${sender}提交了一个催办：${subject}  ${opinion}
\n需要审批,请及时处理!',          0,            '0',          '1',          '1',          '1',          '2022-5-13 12:31:47',          null,          '2022-5-13 12:31:47');
        
INSERT INTO bpm_default_template(ID_,TEMPLATE_TYPE_,MESSAGE_TYPE_,TEMPLATE_,DELETED_,COMPANY_ID_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_)
        values(          '1524970703294676993',          'urge',          'inner',          '${receiver}：
\n	${sender}提交了一个催办：${subject}  ${opinion}
\n需要审批,请及时处理!
\n',          0,            '0',          '1',          '1',          '1',          '2022-5-13 12:32:03',          null,          '2022-5-13 12:32:03');
        
INSERT INTO bpm_default_template(ID_,TEMPLATE_TYPE_,MESSAGE_TYPE_,TEMPLATE_,DELETED_,COMPANY_ID_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_)
        values(          '1537316546681069570',          'tracked',          'inner',          '${receiver}:你好
\n
\n你跟踪的流程 <a href=\"${serverUrl}/bpm/workbench/openDoc/${instId}\" target=\"_blank\">${subject}</a> ,
\n<span rx-if=\"''${status}''==''approve''\">
\n    流程步骤 :${nodeName} 已审批完成!
\n</span>
\n<span rx-if=\"''${status}''!=''approve''\">
\n    流程审批完成!
\n</span>
\n',          0,            '0',          '1',          '1',          '1',          '2022-6-16 14:10:01',          '1',          '2022-6-16 17:19:14');
        
INSERT INTO bpm_default_template(ID_,TEMPLATE_TYPE_,MESSAGE_TYPE_,TEMPLATE_,DELETED_,COMPANY_ID_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_)
        values(          '1537316622560223234',          'tracked',          'mail',          '${receiver}:你好
\n
\n你跟踪的流程 <a href=\"${serverUrl}/bpm/workbench/openDoc/${instId}\" target=\"_blank\">${subject}</a> ,
\n<span rx-if=\"''${status}''==''approve''\">
\n    流程步骤 :${nodeName} 已审批完成!
\n</span>
\n<span rx-if=\"''${status}''!=''approve''\">
\n    流程审批完成!
\n</span>
\n',          0,            '0',          '1',          '1',          '1',          '2022-6-16 14:10:19',          '1',          '2022-6-16 17:19:11');
        
INSERT INTO bpm_default_template(ID_,TEMPLATE_TYPE_,MESSAGE_TYPE_,TEMPLATE_,DELETED_,COMPANY_ID_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_)
        values(          '1537316679560814594',          'tracked',          'sms',          '${receiver}:你好
\n
\n你跟踪的流程 <a href=\"${serverUrl}/bpm/workbench/openDoc/${instId}\" target=\"_blank\">${subject}</a> ,
\n<span rx-if=\"''${status}''==''approve''\">
\n    流程步骤 :${nodeName} 已审批完成!
\n</span>
\n<span rx-if=\"''${status}''!=''approve''\">
\n    流程审批完成!
\n</span>
\n',          0,            '0',          '1',          '1',          '1',          '2022-6-16 14:10:33',          '1',          '2022-6-16 17:19:09');
        
INSERT INTO bpm_default_template(ID_,TEMPLATE_TYPE_,MESSAGE_TYPE_,TEMPLATE_,DELETED_,COMPANY_ID_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_)
        values(          '1537316738314625026',          'tracked',          'weixin',          '${receiver}:你好
\n
\n你跟踪的流程 <a href=\"${serverUrl}/bpm/workbench/openDoc/${instId}\" target=\"_blank\">${subject}</a> ,
\n<span rx-if=\"''${status}''==''approve''\">
\n    流程步骤 :${nodeName} 已审批完成!
\n</span>
\n<span rx-if=\"''${status}''!=''approve''\">
\n    流程审批完成!
\n</span>
\n',          0,            '0',          '1',          '1',          '1',          '2022-6-16 14:10:47',          '1',          '2022-6-16 17:19:07');
        
INSERT INTO bpm_default_template(ID_,TEMPLATE_TYPE_,MESSAGE_TYPE_,TEMPLATE_,DELETED_,COMPANY_ID_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_)
        values(          '1537316872326832130',          'tracked',          'ding',          '${receiver}:你好
\n
\n你跟踪的流程 <a href=\"${serverUrl}/bpm/workbench/openDoc/${instId}\" target=\"_blank\">${subject}</a> ,
\n<span rx-if=\"''${status}''==''approve''\">
\n    流程步骤 :${nodeName} 已审批完成!
\n</span>
\n<span rx-if=\"''${status}''!=''approve''\">
\n    流程审批完成!
\n</span>
\n',          0,            '0',          '1',          '1',          '1',          '2022-6-16 14:11:19',          '1',          '2022-6-16 17:19:02');
        
INSERT INTO bpm_caltime_block(SETTING_ID_,SETTING_NAME_,TIME_INTERVALS_,DELETED_,COMPANY_ID_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_)
        values(          '1273798821971140609',          '白班',          '[{"startTime":"08:00","endTime":"12:00","totalTime":"240"},{"startTime":"13:30","endTime":"18:00","totalTime":270}]',          0,            '0',          '0',          '1',          '1',          '2020-6-19 10:04:40',          '',          '2020-6-19 10:04:40');
        

commit;