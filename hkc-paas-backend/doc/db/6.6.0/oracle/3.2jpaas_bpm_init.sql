INSERT INTO bpm_default_template
  (ID_,
   TEMPLATE_TYPE_,
   MESSAGE_TYPE_,
   TEMPLATE_,
   DELETED_,
   COMPANY_ID_,
   TENANT_ID_,
   CREATE_DEP_ID_,
   CREATE_BY_,
   CREATE_TIME_,
   UPDATE_BY_,
   UPDATE_TIME_)
VALUES
  ('1524947548274446338',
   'addsign',
   'ding',
   '',
   0,
   '0',
   '1',
   '1',
   '1',
   TO_DATE('2022-5-13 11:00:02', 'yyyy-MM-dd HH24:mi:ss'),
   '1',
   TO_DATE('2022-5-18 13:52:35', 'yyyy-MM-dd HH24:mi:ss'));

DECLARE
  content_3 CLOB := '${receiver}：
您好，您收到一个加签任务：${subject}
需要审批,请及时处理!';
BEGIN
  UPDATE bpm_default_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1524947548274446338';
  COMMIT;
END;
/

INSERT
  INTO bpm_default_template(ID_,
                            TEMPLATE_TYPE_,
                            MESSAGE_TYPE_,
                            TEMPLATE_,
                            DELETED_,
                            COMPANY_ID_,
                            TENANT_ID_,
                            CREATE_DEP_ID_,
                            CREATE_BY_,
                            CREATE_TIME_,
                            UPDATE_BY_,
                            UPDATE_TIME_) VALUES('1524948427518099457',
                                                 'addsign',
                                                 'inner',
                                                 '',
                                                 0,
                                                 '0',
                                                 '1',
                                                 '1',
                                                 '1',
                                                 TO_DATE('2022-5-13 11:03:32',
                                                         'yyyy-MM-dd HH24:mi:ss'),
                                                 '',
                                                 TO_DATE('2022-5-13 11:03:32',
                                                         'yyyy-MM-dd HH24:mi:ss'));

DECLARE
  content_3 CLOB := '${receiver}：
您好，您收到一个加签任务：<a target="_blank" href="${serverUrl}/bpm/workbench/openDoc/${instId}">${subject}</a>
需要审批,请及时处理!

';
BEGIN
  UPDATE bpm_default_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1524948427518099457';
  COMMIT;
END;
/

INSERT
  INTO bpm_default_template(ID_,
                            TEMPLATE_TYPE_,
                            MESSAGE_TYPE_,
                            TEMPLATE_,
                            DELETED_,
                            COMPANY_ID_,
                            TENANT_ID_,
                            CREATE_DEP_ID_,
                            CREATE_BY_,
                            CREATE_TIME_,
                            UPDATE_BY_,
                            UPDATE_TIME_) VALUES('1524948577879703553',
                                                 'addsign',
                                                 'weixin',
                                                 '',
                                                 0,
                                                 '0',
                                                 '1',
                                                 '1',
                                                 '1',
                                                 TO_DATE('2022-5-13 11:04:08',
                                                         'yyyy-MM-dd HH24:mi:ss'),
                                                 '',
                                                 TO_DATE('2022-5-13 11:04:08',
                                                         'yyyy-MM-dd HH24:mi:ss'));

DECLARE
  content_3 CLOB := '${receiver}：
您好，您收到一个加签任务：${subject}
需要审批,请及时处理!
';
BEGIN
  UPDATE bpm_default_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1524948577879703553';
  COMMIT;
END;
/

INSERT
  INTO bpm_default_template(ID_,
                            TEMPLATE_TYPE_,
                            MESSAGE_TYPE_,
                            TEMPLATE_,
                            DELETED_,
                            COMPANY_ID_,
                            TENANT_ID_,
                            CREATE_DEP_ID_,
                            CREATE_BY_,
                            CREATE_TIME_,
                            UPDATE_BY_,
                            UPDATE_TIME_) VALUES('1524948673547583490',
                                                 'addsign',
                                                 'mail',
                                                 '',
                                                 0,
                                                 '0',
                                                 '1',
                                                 '1',
                                                 '1',
                                                 TO_DATE('2022-5-13 11:04:31',
                                                         'yyyy-MM-dd HH24:mi:ss'),
                                                 '',
                                                 TO_DATE('2022-5-13 11:04:31',
                                                         'yyyy-MM-dd HH24:mi:ss'));

DECLARE
  content_3 CLOB := '${receiver}：
您好，您收到一个加签任务：<a target="_blank" href="${serverUrl}/bpm/workbench/openDoc/${instId}">${subject}</a>
需要审批,请及时处理!
';
BEGIN
  UPDATE bpm_default_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1524948673547583490';
  COMMIT;
END;
/

INSERT
  INTO bpm_default_template(ID_,
                            TEMPLATE_TYPE_,
                            MESSAGE_TYPE_,
                            TEMPLATE_,
                            DELETED_,
                            COMPANY_ID_,
                            TENANT_ID_,
                            CREATE_DEP_ID_,
                            CREATE_BY_,
                            CREATE_TIME_,
                            UPDATE_BY_,
                            UPDATE_TIME_) VALUES('1524948737682685954',
                                                 'addsign',
                                                 'sms',
                                                 '',
                                                 0,
                                                 '0',
                                                 '1',
                                                 '1',
                                                 '1',
                                                 TO_DATE('2022-5-13 11:04:46',
                                                         'yyyy-MM-dd HH24:mi:ss'),
                                                 '',
                                                 TO_DATE('2022-5-13 11:04:46',
                                                         'yyyy-MM-dd HH24:mi:ss'));

DECLARE
  content_3 CLOB := '${receiver}：您收到一个加签任务：${subject}
需要审批,请及时处理!
';
BEGIN
  UPDATE bpm_default_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1524948737682685954';
  COMMIT;
END;
/

INSERT
  INTO bpm_default_template(ID_,
                            TEMPLATE_TYPE_,
                            MESSAGE_TYPE_,
                            TEMPLATE_,
                            DELETED_,
                            COMPANY_ID_,
                            TENANT_ID_,
                            CREATE_DEP_ID_,
                            CREATE_BY_,
                            CREATE_TIME_,
                            UPDATE_BY_,
                            UPDATE_TIME_) VALUES('1524949936792256514',
                                                 'agent',
                                                 'ding',
                                                 '',
                                                 0,
                                                 '0',
                                                 '1',
                                                 '1',
                                                 '1',
                                                 TO_DATE('2022-5-13 11:09:32',
                                                         'yyyy-MM-dd HH24:mi:ss'),
                                                 '',
                                                 TO_DATE('2022-5-13 11:09:32',
                                                         'yyyy-MM-dd HH24:mi:ss'));

DECLARE
  content_3 CLOB := '${receiver}：
您好，您的待办任务：${subject}
代理给了${agentReceiver}.
';
BEGIN
  UPDATE bpm_default_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1524949936792256514';
  COMMIT;
END;
/

INSERT
  INTO bpm_default_template(ID_,
                            TEMPLATE_TYPE_,
                            MESSAGE_TYPE_,
                            TEMPLATE_,
                            DELETED_,
                            COMPANY_ID_,
                            TENANT_ID_,
                            CREATE_DEP_ID_,
                            CREATE_BY_,
                            CREATE_TIME_,
                            UPDATE_BY_,
                            UPDATE_TIME_) VALUES('1524950087032225793',
                                                 'agent',
                                                 'weixin',
                                                 '',
                                                 0,
                                                 '0',
                                                 '1',
                                                 '1',
                                                 '1',
                                                 TO_DATE('2022-5-13 11:10:08',
                                                         'yyyy-MM-dd HH24:mi:ss'),
                                                 '',
                                                 TO_DATE('2022-5-13 11:10:08',
                                                         'yyyy-MM-dd HH24:mi:ss'));

DECLARE
  content_3 CLOB := '${receiver}：
您好，您的待办任务：${subject}
代理给了${agentReceiver}.
';
BEGIN
  UPDATE bpm_default_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1524950087032225793';
  COMMIT;
END;
/

INSERT
  INTO bpm_default_template(ID_,
                            TEMPLATE_TYPE_,
                            MESSAGE_TYPE_,
                            TEMPLATE_,
                            DELETED_,
                            COMPANY_ID_,
                            TENANT_ID_,
                            CREATE_DEP_ID_,
                            CREATE_BY_,
                            CREATE_TIME_,
                            UPDATE_BY_,
                            UPDATE_TIME_) VALUES('1524950223170945026',
                                                 'agent',
                                                 'mail',
                                                 '',
                                                 0,
                                                 '0',
                                                 '1',
                                                 '1',
                                                 '1',
                                                 TO_DATE('2022-5-13 11:10:40',
                                                         'yyyy-MM-dd HH24:mi:ss'),
                                                 '',
                                                 TO_DATE('2022-5-13 11:10:40',
                                                         'yyyy-MM-dd HH24:mi:ss'));

DECLARE
  content_3 CLOB := '${receiver}：
您好，您的待办任务：<a target="_blank" href="${serverUrl}/bpm/workbench/openDoc/${instId}">${subject}</a>
代理给了${agentReceiver}.
';
BEGIN
  UPDATE bpm_default_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1524950223170945026';
  COMMIT;
END;
/

INSERT
  INTO bpm_default_template(ID_,
                            TEMPLATE_TYPE_,
                            MESSAGE_TYPE_,
                            TEMPLATE_,
                            DELETED_,
                            COMPANY_ID_,
                            TENANT_ID_,
                            CREATE_DEP_ID_,
                            CREATE_BY_,
                            CREATE_TIME_,
                            UPDATE_BY_,
                            UPDATE_TIME_) VALUES('1524950305769373698',
                                                 'agent',
                                                 'sms',
                                                 '',
                                                 0,
                                                 '0',
                                                 '1',
                                                 '1',
                                                 '1',
                                                 TO_DATE('2022-5-13 11:11:00',
                                                         'yyyy-MM-dd HH24:mi:ss'),
                                                 '',
                                                 TO_DATE('2022-5-13 11:11:00',
                                                         'yyyy-MM-dd HH24:mi:ss'));

DECLARE
  content_3 CLOB := '${receiver}：您的待办任务：${subject}
代理给了${agentReceiver}.
';
BEGIN
  UPDATE bpm_default_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1524950305769373698';
  COMMIT;
END;
/

INSERT
  INTO bpm_default_template(ID_,
                            TEMPLATE_TYPE_,
                            MESSAGE_TYPE_,
                            TEMPLATE_,
                            DELETED_,
                            COMPANY_ID_,
                            TENANT_ID_,
                            CREATE_DEP_ID_,
                            CREATE_BY_,
                            CREATE_TIME_,
                            UPDATE_BY_,
                            UPDATE_TIME_) VALUES('1524950374442713090',
                                                 'agent',
                                                 'inner',
                                                 '',
                                                 0,
                                                 '0',
                                                 '1',
                                                 '1',
                                                 '1',
                                                 TO_DATE('2022-5-13 11:11:16',
                                                         'yyyy-MM-dd HH24:mi:ss'),
                                                 '',
                                                 TO_DATE('2022-5-13 11:11:16',
                                                         'yyyy-MM-dd HH24:mi:ss'));

DECLARE
  content_3 CLOB := '${receiver}：
您好，您的待办任务：<a target="_blank" href="${serverUrl}/bpm/workbench/openDoc/${instId}">${subject}</a>
代理给了${agentReceiver}.
';
BEGIN
  UPDATE bpm_default_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1524950374442713090';
  COMMIT;
END;
/

INSERT
  INTO bpm_default_template(ID_,
                            TEMPLATE_TYPE_,
                            MESSAGE_TYPE_,
                            TEMPLATE_,
                            DELETED_,
                            COMPANY_ID_,
                            TENANT_ID_,
                            CREATE_DEP_ID_,
                            CREATE_BY_,
                            CREATE_TIME_,
                            UPDATE_BY_,
                            UPDATE_TIME_) VALUES('1524951376004759554',
                                                 'agentTo',
                                                 'ding',
                                                 '',
                                                 0,
                                                 '0',
                                                 '1',
                                                 '1',
                                                 '1',
                                                 TO_DATE('2022-5-13 11:15:15',
                                                         'yyyy-MM-dd HH24:mi:ss'),
                                                 '',
                                                 TO_DATE('2022-5-13 11:15:15',
                                                         'yyyy-MM-dd HH24:mi:ss'));

DECLARE
  content_3 CLOB := '${receiver}：
您好，您收到一个代理待办任务：${subject}
代理人：${agent},需要审批,请及时处理!
';
BEGIN
  UPDATE bpm_default_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1524951376004759554';
  COMMIT;
END;
/

INSERT
  INTO bpm_default_template(ID_,
                            TEMPLATE_TYPE_,
                            MESSAGE_TYPE_,
                            TEMPLATE_,
                            DELETED_,
                            COMPANY_ID_,
                            TENANT_ID_,
                            CREATE_DEP_ID_,
                            CREATE_BY_,
                            CREATE_TIME_,
                            UPDATE_BY_,
                            UPDATE_TIME_) VALUES('1524951601280827394',
                                                 'agentTo',
                                                 'weixin',
                                                 '',
                                                 0,
                                                 '0',
                                                 '1',
                                                 '1',
                                                 '1',
                                                 TO_DATE('2022-5-13 11:16:09',
                                                         'yyyy-MM-dd HH24:mi:ss'),
                                                 '',
                                                 TO_DATE('2022-5-13 11:16:09',
                                                         'yyyy-MM-dd HH24:mi:ss'));

DECLARE
  content_3 CLOB := '${receiver}：
您好，您收到一个代理待办任务：${subject}
代理人：${agent},需要审批,请及时处理!
';
BEGIN
  UPDATE bpm_default_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1524951601280827394';
  COMMIT;
END;
/

INSERT
  INTO bpm_default_template(ID_,
                            TEMPLATE_TYPE_,
                            MESSAGE_TYPE_,
                            TEMPLATE_,
                            DELETED_,
                            COMPANY_ID_,
                            TENANT_ID_,
                            CREATE_DEP_ID_,
                            CREATE_BY_,
                            CREATE_TIME_,
                            UPDATE_BY_,
                            UPDATE_TIME_) VALUES('1524951656763080705',
                                                 'agentTo',
                                                 'mail',
                                                 '',
                                                 0,
                                                 '0',
                                                 '1',
                                                 '1',
                                                 '1',
                                                 TO_DATE('2022-5-13 11:16:22',
                                                         'yyyy-MM-dd HH24:mi:ss'),
                                                 '',
                                                 TO_DATE('2022-5-13 11:16:22',
                                                         'yyyy-MM-dd HH24:mi:ss'));

DECLARE
  content_3 CLOB := '${receiver}：
您好，您收到一个代理待办任务：<a target="_blank" href="${serverUrl}/bpm/workbench/openDoc/${instId}">${subject}</a>
代理人：${agent},需要审批,请及时处理!
';
BEGIN
  UPDATE bpm_default_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1524951656763080705';
  COMMIT;
END;
/

INSERT
  INTO bpm_default_template(ID_,
                            TEMPLATE_TYPE_,
                            MESSAGE_TYPE_,
                            TEMPLATE_,
                            DELETED_,
                            COMPANY_ID_,
                            TENANT_ID_,
                            CREATE_DEP_ID_,
                            CREATE_BY_,
                            CREATE_TIME_,
                            UPDATE_BY_,
                            UPDATE_TIME_) VALUES('1524951768151212033',
                                                 'agentTo',
                                                 'sms',
                                                 '',
                                                 0,
                                                 '0',
                                                 '1',
                                                 '1',
                                                 '1',
                                                 TO_DATE('2022-5-13 11:16:48',
                                                         'yyyy-MM-dd HH24:mi:ss'),
                                                 '',
                                                 TO_DATE('2022-5-13 11:16:48',
                                                         'yyyy-MM-dd HH24:mi:ss'));

DECLARE
  content_3 CLOB := '${receiver}：您收到一个代理待办任务：${subject}
代理人：${agent},需要审批,请及时处理!
';
BEGIN
  UPDATE bpm_default_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1524951768151212033';
  COMMIT;
END;
/

INSERT
  INTO bpm_default_template(ID_,
                            TEMPLATE_TYPE_,
                            MESSAGE_TYPE_,
                            TEMPLATE_,
                            DELETED_,
                            COMPANY_ID_,
                            TENANT_ID_,
                            CREATE_DEP_ID_,
                            CREATE_BY_,
                            CREATE_TIME_,
                            UPDATE_BY_,
                            UPDATE_TIME_) VALUES('1524951836648390657',
                                                 'agentTo',
                                                 'inner',
                                                 '',
                                                 0,
                                                 '0',
                                                 '1',
                                                 '1',
                                                 '1',
                                                 TO_DATE('2022-5-13 11:17:05',
                                                         'yyyy-MM-dd HH24:mi:ss'),
                                                 '',
                                                 TO_DATE('2022-5-13 11:17:05',
                                                         'yyyy-MM-dd HH24:mi:ss'));

DECLARE
  content_3 CLOB := '${receiver}：
您好，您收到一个代理待办任务：<a target="_blank" href="${serverUrl}/bpm/workbench/openDoc/${instId}">${subject}</a>
代理人：${agent},需要审批,请及时处理!
';
BEGIN
  UPDATE bpm_default_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1524951836648390657';
  COMMIT;
END;
/

INSERT
  INTO bpm_default_template(ID_,
                            TEMPLATE_TYPE_,
                            MESSAGE_TYPE_,
                            TEMPLATE_,
                            DELETED_,
                            COMPANY_ID_,
                            TENANT_ID_,
                            CREATE_DEP_ID_,
                            CREATE_BY_,
                            CREATE_TIME_,
                            UPDATE_BY_,
                            UPDATE_TIME_) VALUES('1524952390388793346',
                                                 'approve',
                                                 'ding',
                                                 '',
                                                 0,
                                                 '0',
                                                 '1',
                                                 '1',
                                                 '1',
                                                 TO_DATE('2022-5-13 11:19:17',
                                                         'yyyy-MM-dd HH24:mi:ss'),
                                                 '',
                                                 TO_DATE('2022-5-13 11:19:17',
                                                         'yyyy-MM-dd HH24:mi:ss'));

DECLARE
  content_3 CLOB := '${receiver}：
您好，您收到一个待办任务：${subject}
需要审批,请及时处理!
';
BEGIN
  UPDATE bpm_default_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1524952390388793346';
  COMMIT;
END;
/

INSERT
  INTO bpm_default_template(ID_,
                            TEMPLATE_TYPE_,
                            MESSAGE_TYPE_,
                            TEMPLATE_,
                            DELETED_,
                            COMPANY_ID_,
                            TENANT_ID_,
                            CREATE_DEP_ID_,
                            CREATE_BY_,
                            CREATE_TIME_,
                            UPDATE_BY_,
                            UPDATE_TIME_) VALUES('1524952844149571585',
                                                 'approve',
                                                 'weixin',
                                                 '',
                                                 0,
                                                 '0',
                                                 '1',
                                                 '1',
                                                 '1',
                                                 TO_DATE('2022-5-13 11:21:05',
                                                         'yyyy-MM-dd HH24:mi:ss'),
                                                 '',
                                                 TO_DATE('2022-5-13 11:21:05',
                                                         'yyyy-MM-dd HH24:mi:ss'));

DECLARE
  content_3 CLOB := '${receiver}：
您好，您收到一个待办任务：${subject}
需要审批,请及时处理!
';
BEGIN
  UPDATE bpm_default_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1524952844149571585';
  COMMIT;
END;
/

INSERT
  INTO bpm_default_template(ID_,
                            TEMPLATE_TYPE_,
                            MESSAGE_TYPE_,
                            TEMPLATE_,
                            DELETED_,
                            COMPANY_ID_,
                            TENANT_ID_,
                            CREATE_DEP_ID_,
                            CREATE_BY_,
                            CREATE_TIME_,
                            UPDATE_BY_,
                            UPDATE_TIME_) VALUES('1524952923082178561',
                                                 'approve',
                                                 'mail',
                                                 '',
                                                 0,
                                                 '0',
                                                 '1',
                                                 '1',
                                                 '1',
                                                 TO_DATE('2022-5-13 11:21:24',
                                                         'yyyy-MM-dd HH24:mi:ss'),
                                                 '',
                                                 TO_DATE('2022-5-13 11:21:24',
                                                         'yyyy-MM-dd HH24:mi:ss'));

DECLARE
  content_3 CLOB := '${receiver}：
您好，您收到一个待办任务：<a target="_blank" href="${serverUrl}/bpm/workbench/openDoc/${instId}">${subject}</a>
需要审批,请及时处理!
';
BEGIN
  UPDATE bpm_default_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1524952923082178561';
  COMMIT;
END;
/

INSERT
  INTO bpm_default_template(ID_,
                            TEMPLATE_TYPE_,
                            MESSAGE_TYPE_,
                            TEMPLATE_,
                            DELETED_,
                            COMPANY_ID_,
                            TENANT_ID_,
                            CREATE_DEP_ID_,
                            CREATE_BY_,
                            CREATE_TIME_,
                            UPDATE_BY_,
                            UPDATE_TIME_) VALUES('1524953214506614785',
                                                 'approve',
                                                 'sms',
                                                 '',
                                                 0,
                                                 '0',
                                                 '1',
                                                 '1',
                                                 '1',
                                                 TO_DATE('2022-5-13 11:22:33',
                                                         'yyyy-MM-dd HH24:mi:ss'),
                                                 '',
                                                 TO_DATE('2022-5-13 11:22:33',
                                                         'yyyy-MM-dd HH24:mi:ss'));

DECLARE
  content_3 CLOB := '${receiver}：您收到一个待办任务：${subject}
需要审批,请及时处理!
';
BEGIN
  UPDATE bpm_default_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1524953214506614785';
  COMMIT;
END;
/

INSERT
  INTO bpm_default_template(ID_,
                            TEMPLATE_TYPE_,
                            MESSAGE_TYPE_,
                            TEMPLATE_,
                            DELETED_,
                            COMPANY_ID_,
                            TENANT_ID_,
                            CREATE_DEP_ID_,
                            CREATE_BY_,
                            CREATE_TIME_,
                            UPDATE_BY_,
                            UPDATE_TIME_) VALUES('1524953293225312257',
                                                 'approve',
                                                 'inner',
                                                 '',
                                                 0,
                                                 '0',
                                                 '1',
                                                 '1',
                                                 '1',
                                                 TO_DATE('2022-5-13 11:22:52',
                                                         'yyyy-MM-dd HH24:mi:ss'),
                                                 '',
                                                 TO_DATE('2022-5-13 11:22:52',
                                                         'yyyy-MM-dd HH24:mi:ss'));

DECLARE
  content_3 CLOB := '${receiver}：
您好，您收到一个待办任务：<a target="_blank" href="${serverUrl}/bpm/workbench/openDoc/${instId}">${subject}</a>
需要审批,请及时处理!
';
BEGIN
  UPDATE bpm_default_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1524953293225312257';
  COMMIT;
END;
/

INSERT
  INTO bpm_default_template(ID_,
                            TEMPLATE_TYPE_,
                            MESSAGE_TYPE_,
                            TEMPLATE_,
                            DELETED_,
                            COMPANY_ID_,
                            TENANT_ID_,
                            CREATE_DEP_ID_,
                            CREATE_BY_,
                            CREATE_TIME_,
                            UPDATE_BY_,
                            UPDATE_TIME_) VALUES('1524955002794262530',
                                                 'back',
                                                 'ding',
                                                 '',
                                                 0,
                                                 '0',
                                                 '1',
                                                 '1',
                                                 '1',
                                                 TO_DATE('2022-5-13 11:29:40',
                                                         'yyyy-MM-dd HH24:mi:ss'),
                                                 '',
                                                 TO_DATE('2022-5-13 11:29:40',
                                                         'yyyy-MM-dd HH24:mi:ss'));

DECLARE
  content_3 CLOB := '${receiver}：
您好，您的申请被驳回：${subject}
需要办理,请及时处理!
';
BEGIN
  UPDATE bpm_default_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1524955002794262530';
  COMMIT;
END;
/

INSERT
  INTO bpm_default_template(ID_,
                            TEMPLATE_TYPE_,
                            MESSAGE_TYPE_,
                            TEMPLATE_,
                            DELETED_,
                            COMPANY_ID_,
                            TENANT_ID_,
                            CREATE_DEP_ID_,
                            CREATE_BY_,
                            CREATE_TIME_,
                            UPDATE_BY_,
                            UPDATE_TIME_) VALUES('1524955151146795010',
                                                 'back',
                                                 'weixin',
                                                 '',
                                                 0,
                                                 '0',
                                                 '1',
                                                 '1',
                                                 '1',
                                                 TO_DATE('2022-5-13 11:30:15',
                                                         'yyyy-MM-dd HH24:mi:ss'),
                                                 '',
                                                 TO_DATE('2022-5-13 11:30:15',
                                                         'yyyy-MM-dd HH24:mi:ss'));

DECLARE
  content_3 CLOB := '${receiver}：
您好，您的申请被驳回：<a target="_blank" href="${serverUrl}/bpm/workbench/openDoc/${instId}">${subject}</a>
需要办理,请及时处理!
';
BEGIN
  UPDATE bpm_default_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1524955151146795010';
  COMMIT;
END;
/

INSERT
  INTO bpm_default_template(ID_,
                            TEMPLATE_TYPE_,
                            MESSAGE_TYPE_,
                            TEMPLATE_,
                            DELETED_,
                            COMPANY_ID_,
                            TENANT_ID_,
                            CREATE_DEP_ID_,
                            CREATE_BY_,
                            CREATE_TIME_,
                            UPDATE_BY_,
                            UPDATE_TIME_) VALUES('1524955211649630210',
                                                 'back',
                                                 'mail',
                                                 '',
                                                 0,
                                                 '0',
                                                 '1',
                                                 '1',
                                                 '1',
                                                 TO_DATE('2022-5-13 11:30:29',
                                                         'yyyy-MM-dd HH24:mi:ss'),
                                                 '',
                                                 TO_DATE('2022-5-13 11:30:29',
                                                         'yyyy-MM-dd HH24:mi:ss'));

DECLARE
  content_3 CLOB := '${receiver}：
您好，您的申请被驳回：<a target="_blank" href="${serverUrl}/bpm/workbench/openDoc/${instId}">${subject}</a>
需要办理,请及时处理!
';
BEGIN
  UPDATE bpm_default_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1524955211649630210';
  COMMIT;
END;
/

INSERT
  INTO bpm_default_template(ID_,
                            TEMPLATE_TYPE_,
                            MESSAGE_TYPE_,
                            TEMPLATE_,
                            DELETED_,
                            COMPANY_ID_,
                            TENANT_ID_,
                            CREATE_DEP_ID_,
                            CREATE_BY_,
                            CREATE_TIME_,
                            UPDATE_BY_,
                            UPDATE_TIME_) VALUES('1524955276384518145',
                                                 'back',
                                                 'sms',
                                                 '',
                                                 0,
                                                 '0',
                                                 '1',
                                                 '1',
                                                 '1',
                                                 TO_DATE('2022-5-13 11:30:45',
                                                         'yyyy-MM-dd HH24:mi:ss'),
                                                 '',
                                                 TO_DATE('2022-5-13 11:30:45',
                                                         'yyyy-MM-dd HH24:mi:ss'));

DECLARE
  content_3 CLOB := '${receiver}：您的申请被驳回：${subject}
需要办理,请及时处理!
';
BEGIN
  UPDATE bpm_default_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1524955276384518145';
  COMMIT;
END;
/

INSERT
  INTO bpm_default_template(ID_,
                            TEMPLATE_TYPE_,
                            MESSAGE_TYPE_,
                            TEMPLATE_,
                            DELETED_,
                            COMPANY_ID_,
                            TENANT_ID_,
                            CREATE_DEP_ID_,
                            CREATE_BY_,
                            CREATE_TIME_,
                            UPDATE_BY_,
                            UPDATE_TIME_) VALUES('1524955343099117570',
                                                 'back',
                                                 'inner',
                                                 '',
                                                 0,
                                                 '0',
                                                 '1',
                                                 '1',
                                                 '1',
                                                 TO_DATE('2022-5-13 11:31:01',
                                                         'yyyy-MM-dd HH24:mi:ss'),
                                                 '',
                                                 TO_DATE('2022-5-13 11:31:01',
                                                         'yyyy-MM-dd HH24:mi:ss'));

DECLARE
  content_3 CLOB := '${receiver}：
您好，您的申请被驳回：<a target="_blank" href="${serverUrl}/bpm/workbench/openDoc/${instId}">${subject}</a>
需要办理,请及时处理!
';
BEGIN
  UPDATE bpm_default_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1524955343099117570';
  COMMIT;
END;
/

INSERT
  INTO bpm_default_template(ID_,
                            TEMPLATE_TYPE_,
                            MESSAGE_TYPE_,
                            TEMPLATE_,
                            DELETED_,
                            COMPANY_ID_,
                            TENANT_ID_,
                            CREATE_DEP_ID_,
                            CREATE_BY_,
                            CREATE_TIME_,
                            UPDATE_BY_,
                            UPDATE_TIME_) VALUES('1524955793663836161',
                                                 'backToStart',
                                                 'ding',
                                                 '',
                                                 0,
                                                 '0',
                                                 '1',
                                                 '1',
                                                 '1',
                                                 TO_DATE('2022-5-13 11:32:48',
                                                         'yyyy-MM-dd HH24:mi:ss'),
                                                 '',
                                                 TO_DATE('2022-5-13 11:32:48',
                                                         'yyyy-MM-dd HH24:mi:ss'));

DECLARE
  content_3 CLOB := '${receiver}：
您好, 您的申请被驳回：${subject}
需要办理, 请及时处理!
';
BEGIN
  UPDATE bpm_default_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1524955793663836161';
  COMMIT;
END;
/

INSERT
  INTO bpm_default_template(ID_,
                            TEMPLATE_TYPE_,
                            MESSAGE_TYPE_,
                            TEMPLATE_,
                            DELETED_,
                            COMPANY_ID_,
                            TENANT_ID_,
                            CREATE_DEP_ID_,
                            CREATE_BY_,
                            CREATE_TIME_,
                            UPDATE_BY_,
                            UPDATE_TIME_) VALUES('1524955906675163138',
                                                 'backToStart',
                                                 'weixin',
                                                 '',
                                                 0,
                                                 '0',
                                                 '1',
                                                 '1',
                                                 '1',
                                                 TO_DATE('2022-5-13 11:33:15',
                                                         'yyyy-MM-dd HH24:mi:ss'),
                                                 '',
                                                 TO_DATE('2022-5-13 11:33:15',
                                                         'yyyy-MM-dd HH24:mi:ss'));

DECLARE
  content_3 CLOB := '${receiver}：
您好, 您的申请被驳回：${subject}
需要办理, 请及时处理!
';
BEGIN
  UPDATE bpm_default_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1524955906675163138';
  COMMIT;
END;
/

INSERT
  INTO bpm_default_template(ID_,
                            TEMPLATE_TYPE_,
                            MESSAGE_TYPE_,
                            TEMPLATE_,
                            DELETED_,
                            COMPANY_ID_,
                            TENANT_ID_,
                            CREATE_DEP_ID_,
                            CREATE_BY_,
                            CREATE_TIME_,
                            UPDATE_BY_,
                            UPDATE_TIME_) VALUES('1524955971837870082',
                                                 'backToStart',
                                                 'mail',
                                                 '',
                                                 0,
                                                 '0',
                                                 '1',
                                                 '1',
                                                 '1',
                                                 TO_DATE('2022-5-13 11:33:31',
                                                         'yyyy-MM-dd HH24:mi:ss'),
                                                 '',
                                                 TO_DATE('2022-5-13 11:33:31',
                                                         'yyyy-MM-dd HH24:mi:ss'));

DECLARE
  content_3 CLOB := '${receiver}：
您好，您的申请被驳回：<a target="_blank" href="${serverUrl}/bpm/workbench/openDoc/${instId}">${subject}</a>
需要办理,请及时处理!
';
BEGIN
  UPDATE bpm_default_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1524955971837870082';
  COMMIT;
END;
/

INSERT
  INTO bpm_default_template(ID_,
                            TEMPLATE_TYPE_,
                            MESSAGE_TYPE_,
                            TEMPLATE_,
                            DELETED_,
                            COMPANY_ID_,
                            TENANT_ID_,
                            CREATE_DEP_ID_,
                            CREATE_BY_,
                            CREATE_TIME_,
                            UPDATE_BY_,
                            UPDATE_TIME_) VALUES('1524956041979215873',
                                                 'backToStart',
                                                 'sms',
                                                 '',
                                                 0,
                                                 '0',
                                                 '1',
                                                 '1',
                                                 '1',
                                                 TO_DATE('2022-5-13 11:33:47',
                                                         'yyyy-MM-dd HH24:mi:ss'),
                                                 '',
                                                 TO_DATE('2022-5-13 11:33:47',
                                                         'yyyy-MM-dd HH24:mi:ss'));

DECLARE
  content_3 CLOB := '${receiver}：您的申请被驳回：${subject}
需要办理,请及时处理!
';
BEGIN
  UPDATE bpm_default_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1524956041979215873';
  COMMIT;
END;
/

INSERT
  INTO bpm_default_template(ID_,
                            TEMPLATE_TYPE_,
                            MESSAGE_TYPE_,
                            TEMPLATE_,
                            DELETED_,
                            COMPANY_ID_,
                            TENANT_ID_,
                            CREATE_DEP_ID_,
                            CREATE_BY_,
                            CREATE_TIME_,
                            UPDATE_BY_,
                            UPDATE_TIME_) VALUES('1524956168571699201',
                                                 'backToStart',
                                                 'inner',
                                                 '',
                                                 0,
                                                 '0',
                                                 '1',
                                                 '1',
                                                 '1',
                                                 TO_DATE('2022-5-13 11:34:18',
                                                         'yyyy-MM-dd HH24:mi:ss'),
                                                 '',
                                                 TO_DATE('2022-5-13 11:34:18',
                                                         'yyyy-MM-dd HH24:mi:ss'));

DECLARE
  content_3 CLOB := '${receiver}：
您好，您的申请被驳回：<a target="_blank" href="${serverUrl}/bpm/workbench/openDoc/${instId}">${subject}</a>
需要办理,请及时处理!
';
BEGIN
  UPDATE bpm_default_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1524956168571699201';
  COMMIT;
END;
/

INSERT
  INTO bpm_default_template(ID_,
                            TEMPLATE_TYPE_,
                            MESSAGE_TYPE_,
                            TEMPLATE_,
                            DELETED_,
                            COMPANY_ID_,
                            TENANT_ID_,
                            CREATE_DEP_ID_,
                            CREATE_BY_,
                            CREATE_TIME_,
                            UPDATE_BY_,
                            UPDATE_TIME_) VALUES('1524957026327838721',
                                                 'commu',
                                                 'ding',
                                                 '',
                                                 0,
                                                 '0',
                                                 '1',
                                                 '1',
                                                 '1',
                                                 TO_DATE('2022-5-13 11:37:42',
                                                         'yyyy-MM-dd HH24:mi:ss'),
                                                 '',
                                                 TO_DATE('2022-5-13 11:37:42',
                                                         'yyyy-MM-dd HH24:mi:ss'));

DECLARE
  content_3 CLOB := '${receiver}：
您好, ${sender}发给您一个沟通消息：${subject}
需要审批,请及时处理!
';
BEGIN
  UPDATE bpm_default_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1524957026327838721';
  COMMIT;
END;
/

INSERT
  INTO bpm_default_template(ID_,
                            TEMPLATE_TYPE_,
                            MESSAGE_TYPE_,
                            TEMPLATE_,
                            DELETED_,
                            COMPANY_ID_,
                            TENANT_ID_,
                            CREATE_DEP_ID_,
                            CREATE_BY_,
                            CREATE_TIME_,
                            UPDATE_BY_,
                            UPDATE_TIME_) VALUES('1524957115179974658',
                                                 'commu',
                                                 'weixin',
                                                 '',
                                                 0,
                                                 '0',
                                                 '1',
                                                 '1',
                                                 '1',
                                                 TO_DATE('2022-5-13 11:38:03',
                                                         'yyyy-MM-dd HH24:mi:ss'),
                                                 '',
                                                 TO_DATE('2022-5-13 11:38:03',
                                                         'yyyy-MM-dd HH24:mi:ss'));

DECLARE
  content_3 CLOB := '${receiver}：
您好, ${sender}发给您一个沟通消息：${subject}
需要审批,请及时处理!
';
BEGIN
  UPDATE bpm_default_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1524957115179974658';
  COMMIT;
END;
/

INSERT
  INTO bpm_default_template(ID_,
                            TEMPLATE_TYPE_,
                            MESSAGE_TYPE_,
                            TEMPLATE_,
                            DELETED_,
                            COMPANY_ID_,
                            TENANT_ID_,
                            CREATE_DEP_ID_,
                            CREATE_BY_,
                            CREATE_TIME_,
                            UPDATE_BY_,
                            UPDATE_TIME_) VALUES('1524957272227299330',
                                                 'commu',
                                                 'mail',
                                                 '',
                                                 0,
                                                 '0',
                                                 '1',
                                                 '1',
                                                 '1',
                                                 TO_DATE('2022-5-13 11:38:41',
                                                         'yyyy-MM-dd HH24:mi:ss'),
                                                 '',
                                                 TO_DATE('2022-5-13 11:38:41',
                                                         'yyyy-MM-dd HH24:mi:ss'));

DECLARE
  content_3 CLOB := '${receiver}：
您好, ${sender}发给您一个沟通消息：<a target="_blank" href="${serverUrl}/bpm/workbench/openDoc/${instId}">${subject}</a>
';
BEGIN
  UPDATE bpm_default_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1524957272227299330';
  COMMIT;
END;
/

INSERT
  INTO bpm_default_template(ID_,
                            TEMPLATE_TYPE_,
                            MESSAGE_TYPE_,
                            TEMPLATE_,
                            DELETED_,
                            COMPANY_ID_,
                            TENANT_ID_,
                            CREATE_DEP_ID_,
                            CREATE_BY_,
                            CREATE_TIME_,
                            UPDATE_BY_,
                            UPDATE_TIME_) VALUES('1524957374140497922',
                                                 'commu',
                                                 'sms',
                                                 '',
                                                 0,
                                                 '0',
                                                 '1',
                                                 '1',
                                                 '1',
                                                 TO_DATE('2022-5-13 11:39:05',
                                                         'yyyy-MM-dd HH24:mi:ss'),
                                                 '',
                                                 TO_DATE('2022-5-13 11:39:05',
                                                         'yyyy-MM-dd HH24:mi:ss'));

DECLARE
  content_3 CLOB := '${receiver}：${sender}发给您一个沟通消息：${subject}
';
BEGIN
  UPDATE bpm_default_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1524957374140497922';
  COMMIT;
END;
/

INSERT
  INTO bpm_default_template(ID_,
                            TEMPLATE_TYPE_,
                            MESSAGE_TYPE_,
                            TEMPLATE_,
                            DELETED_,
                            COMPANY_ID_,
                            TENANT_ID_,
                            CREATE_DEP_ID_,
                            CREATE_BY_,
                            CREATE_TIME_,
                            UPDATE_BY_,
                            UPDATE_TIME_) VALUES('1524957464376754177',
                                                 'commu',
                                                 'inner',
                                                 '',
                                                 0,
                                                 '0',
                                                 '1',
                                                 '1',
                                                 '1',
                                                 TO_DATE('2022-5-13 11:39:27',
                                                         'yyyy-MM-dd HH24:mi:ss'),
                                                 '',
                                                 TO_DATE('2022-5-13 11:39:27',
                                                         'yyyy-MM-dd HH24:mi:ss'));

DECLARE
  content_3 CLOB := '${receiver}：
您好, ${sender}发给您一个沟通消息：<a target="_blank" href="${serverUrl}/bpm/workbench/openDoc/${instId}">${subject}</a>
';
BEGIN
  UPDATE bpm_default_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1524957464376754177';
  COMMIT;
END;
/

INSERT
  INTO bpm_default_template(ID_,
                            TEMPLATE_TYPE_,
                            MESSAGE_TYPE_,
                            TEMPLATE_,
                            DELETED_,
                            COMPANY_ID_,
                            TENANT_ID_,
                            CREATE_DEP_ID_,
                            CREATE_BY_,
                            CREATE_TIME_,
                            UPDATE_BY_,
                            UPDATE_TIME_) VALUES('1524957642043277314',
                                                 'copy',
                                                 'ding',
                                                 '',
                                                 0,
                                                 '0',
                                                 '1',
                                                 '1',
                                                 '1',
                                                 TO_DATE('2022-5-13 11:40:09',
                                                         'yyyy-MM-dd HH24:mi:ss'),
                                                 '',
                                                 TO_DATE('2022-5-13 11:40:09',
                                                         'yyyy-MM-dd HH24:mi:ss'));

DECLARE
  content_3 CLOB := '${receiver}：
您好, ${sender}抄送给您一条流程实例信息：${subject},请查收!';
BEGIN
  UPDATE bpm_default_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1524957642043277314';
  COMMIT;
END;
/

INSERT
  INTO bpm_default_template(ID_,
                            TEMPLATE_TYPE_,
                            MESSAGE_TYPE_,
                            TEMPLATE_,
                            DELETED_,
                            COMPANY_ID_,
                            TENANT_ID_,
                            CREATE_DEP_ID_,
                            CREATE_BY_,
                            CREATE_TIME_,
                            UPDATE_BY_,
                            UPDATE_TIME_) VALUES('1524957737153314818',
                                                 'copy',
                                                 'weixin',
                                                 '',
                                                 0,
                                                 '0',
                                                 '1',
                                                 '1',
                                                 '1',
                                                 TO_DATE('2022-5-13 11:40:32',
                                                         'yyyy-MM-dd HH24:mi:ss'),
                                                 '',
                                                 TO_DATE('2022-5-13 11:40:32',
                                                         'yyyy-MM-dd HH24:mi:ss'));

DECLARE
  content_3 CLOB := '${receiver}：
您好, ${sender}抄送给您一条流程实例信息：${subject},请查收!
';
BEGIN
  UPDATE bpm_default_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1524957737153314818';
  COMMIT;
END;
/

INSERT
  INTO bpm_default_template(ID_,
                            TEMPLATE_TYPE_,
                            MESSAGE_TYPE_,
                            TEMPLATE_,
                            DELETED_,
                            COMPANY_ID_,
                            TENANT_ID_,
                            CREATE_DEP_ID_,
                            CREATE_BY_,
                            CREATE_TIME_,
                            UPDATE_BY_,
                            UPDATE_TIME_) VALUES('1524957947367636993',
                                                 'copy',
                                                 'mail',
                                                 '',
                                                 0,
                                                 '0',
                                                 '1',
                                                 '1',
                                                 '1',
                                                 TO_DATE('2022-5-13 11:41:22',
                                                         'yyyy-MM-dd HH24:mi:ss'),
                                                 '',
                                                 TO_DATE('2022-5-13 11:41:22',
                                                         'yyyy-MM-dd HH24:mi:ss'));

DECLARE
  content_3 CLOB := '${receiver}：
您好, ${sender}抄送给您一条流程实例信息：<a href="${serverUrl}/bpm/workbench/openDoc/${instId}" target="_blank">${subject}</a>,请查收!
';
BEGIN
  UPDATE bpm_default_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1524957947367636993';
  COMMIT;
END;
/

INSERT
  INTO bpm_default_template(ID_,
                            TEMPLATE_TYPE_,
                            MESSAGE_TYPE_,
                            TEMPLATE_,
                            DELETED_,
                            COMPANY_ID_,
                            TENANT_ID_,
                            CREATE_DEP_ID_,
                            CREATE_BY_,
                            CREATE_TIME_,
                            UPDATE_BY_,
                            UPDATE_TIME_) VALUES('1524958039982063618',
                                                 'copy',
                                                 'sms',
                                                 '',
                                                 0,
                                                 '0',
                                                 '1',
                                                 '1',
                                                 '1',
                                                 TO_DATE('2022-5-13 11:41:44',
                                                         'yyyy-MM-dd HH24:mi:ss'),
                                                 '',
                                                 TO_DATE('2022-5-13 11:41:44',
                                                         'yyyy-MM-dd HH24:mi:ss'));

DECLARE
  content_3 CLOB := '${receiver}：${sender}抄送给您一条流程实例信息：${subject},请查收!
';
BEGIN
  UPDATE bpm_default_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1524958039982063618';
  COMMIT;
END;
/

INSERT
  INTO bpm_default_template(ID_,
                            TEMPLATE_TYPE_,
                            MESSAGE_TYPE_,
                            TEMPLATE_,
                            DELETED_,
                            COMPANY_ID_,
                            TENANT_ID_,
                            CREATE_DEP_ID_,
                            CREATE_BY_,
                            CREATE_TIME_,
                            UPDATE_BY_,
                            UPDATE_TIME_) VALUES('1524958101055324161',
                                                 'copy',
                                                 'inner',
                                                 '',
                                                 0,
                                                 '0',
                                                 '1',
                                                 '1',
                                                 '1',
                                                 TO_DATE('2022-5-13 11:41:58',
                                                         'yyyy-MM-dd HH24:mi:ss'),
                                                 '',
                                                 TO_DATE('2022-5-13 11:41:58',
                                                         'yyyy-MM-dd HH24:mi:ss'));

DECLARE
  content_3 CLOB := '${receiver}：
您好, ${sender}抄送给您一条流程实例信息：<a href="${serverUrl}/bpm/workbench/openDoc/${instId}" target="_blank">${subject}</a>,请查收!
';
BEGIN
  UPDATE bpm_default_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1524958101055324161';
  COMMIT;
END;
/

INSERT
  INTO bpm_default_template(ID_,
                            TEMPLATE_TYPE_,
                            MESSAGE_TYPE_,
                            TEMPLATE_,
                            DELETED_,
                            COMPANY_ID_,
                            TENANT_ID_,
                            CREATE_DEP_ID_,
                            CREATE_BY_,
                            CREATE_TIME_,
                            UPDATE_BY_,
                            UPDATE_TIME_) VALUES('1524958655592644610',
                                                 'forward',
                                                 'ding',
                                                 '',
                                                 0,
                                                 '0',
                                                 '1',
                                                 '1',
                                                 '1',
                                                 TO_DATE('2022-5-13 11:44:11',
                                                         'yyyy-MM-dd HH24:mi:ss'),
                                                 '',
                                                 TO_DATE('2022-5-13 11:44:11',
                                                         'yyyy-MM-dd HH24:mi:ss'));

DECLARE
  content_3 CLOB := '${receiver}：
您好，${sender}转发给您一条流程实例信息：${subject}, 请查收!
';
BEGIN
  UPDATE bpm_default_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1524958655592644610';
  COMMIT;
END;
/

INSERT
  INTO bpm_default_template(ID_,
                            TEMPLATE_TYPE_,
                            MESSAGE_TYPE_,
                            TEMPLATE_,
                            DELETED_,
                            COMPANY_ID_,
                            TENANT_ID_,
                            CREATE_DEP_ID_,
                            CREATE_BY_,
                            CREATE_TIME_,
                            UPDATE_BY_,
                            UPDATE_TIME_) VALUES('1524958835641532417',
                                                 'forward',
                                                 'weixin',
                                                 '',
                                                 0,
                                                 '0',
                                                 '1',
                                                 '1',
                                                 '1',
                                                 TO_DATE('2022-5-13 11:44:53',
                                                         'yyyy-MM-dd HH24:mi:ss'),
                                                 '',
                                                 TO_DATE('2022-5-13 11:44:53',
                                                         'yyyy-MM-dd HH24:mi:ss'));

DECLARE
  content_3 CLOB := '${receiver}：
您好，${sender}转发给您一条流程实例信息：${subject}, 请查收!
';
BEGIN
  UPDATE bpm_default_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1524958835641532417';
  COMMIT;
END;
/

INSERT
  INTO bpm_default_template(ID_,
                            TEMPLATE_TYPE_,
                            MESSAGE_TYPE_,
                            TEMPLATE_,
                            DELETED_,
                            COMPANY_ID_,
                            TENANT_ID_,
                            CREATE_DEP_ID_,
                            CREATE_BY_,
                            CREATE_TIME_,
                            UPDATE_BY_,
                            UPDATE_TIME_) VALUES('1524958927433875458',
                                                 'forward',
                                                 'mail',
                                                 '',
                                                 0,
                                                 '0',
                                                 '1',
                                                 '1',
                                                 '1',
                                                 TO_DATE('2022-5-13 11:45:15',
                                                         'yyyy-MM-dd HH24:mi:ss'),
                                                 '',
                                                 TO_DATE('2022-5-13 11:45:15',
                                                         'yyyy-MM-dd HH24:mi:ss'));

DECLARE
  content_3 CLOB := '${receiver}：
您好，${sender}转发给您一条流程实例信息：<a href="${serverUrl}/bpm/workbench/openDoc/${instId}" target="_blank">${subject}</a>,请查收!
';
BEGIN
  UPDATE bpm_default_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1524958927433875458';
  COMMIT;
END;
/

INSERT
  INTO bpm_default_template(ID_,
                            TEMPLATE_TYPE_,
                            MESSAGE_TYPE_,
                            TEMPLATE_,
                            DELETED_,
                            COMPANY_ID_,
                            TENANT_ID_,
                            CREATE_DEP_ID_,
                            CREATE_BY_,
                            CREATE_TIME_,
                            UPDATE_BY_,
                            UPDATE_TIME_) VALUES('1524959012624384002',
                                                 'forward',
                                                 'sms',
                                                 '',
                                                 0,
                                                 '0',
                                                 '1',
                                                 '1',
                                                 '1',
                                                 TO_DATE('2022-5-13 11:45:36',
                                                         'yyyy-MM-dd HH24:mi:ss'),
                                                 '',
                                                 TO_DATE('2022-5-13 11:45:36',
                                                         'yyyy-MM-dd HH24:mi:ss'));

DECLARE
  content_3 CLOB := '${receiver}：您好，${sender}转发给您一条流程实例信息：${subject},请查收!
';
BEGIN
  UPDATE bpm_default_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1524959012624384002';
  COMMIT;
END;
/

INSERT
  INTO bpm_default_template(ID_,
                            TEMPLATE_TYPE_,
                            MESSAGE_TYPE_,
                            TEMPLATE_,
                            DELETED_,
                            COMPANY_ID_,
                            TENANT_ID_,
                            CREATE_DEP_ID_,
                            CREATE_BY_,
                            CREATE_TIME_,
                            UPDATE_BY_,
                            UPDATE_TIME_) VALUES('1524959075304062977',
                                                 'forward',
                                                 'inner',
                                                 '',
                                                 0,
                                                 '0',
                                                 '1',
                                                 '1',
                                                 '1',
                                                 TO_DATE('2022-5-13 11:45:51',
                                                         'yyyy-MM-dd HH24:mi:ss'),
                                                 '',
                                                 TO_DATE('2022-5-13 11:45:51',
                                                         'yyyy-MM-dd HH24:mi:ss'));

DECLARE
  content_3 CLOB := '${receiver}：
您好，${sender}转发给您一条流程实例信息：<a href="${serverUrl}/bpm/workbench/openDoc/${instId}" target="_blank">${subject}</a>,请查收!
';
BEGIN
  UPDATE bpm_default_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1524959075304062977';
  COMMIT;
END;
/

INSERT
  INTO bpm_default_template(ID_,
                            TEMPLATE_TYPE_,
                            MESSAGE_TYPE_,
                            TEMPLATE_,
                            DELETED_,
                            COMPANY_ID_,
                            TENANT_ID_,
                            CREATE_DEP_ID_,
                            CREATE_BY_,
                            CREATE_TIME_,
                            UPDATE_BY_,
                            UPDATE_TIME_) VALUES('1524959367735132162',
                                                 'remind',
                                                 'ding',
                                                 '',
                                                 0,
                                                 '0',
                                                 '1',
                                                 '1',
                                                 '1',
                                                 TO_DATE('2022-5-13 11:47:00',
                                                         'yyyy-MM-dd HH24:mi:ss'),
                                                 '',
                                                 TO_DATE('2022-5-13 11:47:00',
                                                         'yyyy-MM-dd HH24:mi:ss'));

DECLARE
  content_3 CLOB := '${receiver}：
您的收到了一个催办信息,任务：<a target="_blank" href="${serverUrl}/bpm/workbench/openDoc/${instId}">${subject}</a>请尽快处理.
';
BEGIN
  UPDATE bpm_default_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1524959367735132162';
  COMMIT;
END;
/

INSERT
  INTO bpm_default_template(ID_,
                            TEMPLATE_TYPE_,
                            MESSAGE_TYPE_,
                            TEMPLATE_,
                            DELETED_,
                            COMPANY_ID_,
                            TENANT_ID_,
                            CREATE_DEP_ID_,
                            CREATE_BY_,
                            CREATE_TIME_,
                            UPDATE_BY_,
                            UPDATE_TIME_) VALUES('1524959704600657922',
                                                 'remind',
                                                 'weixin',
                                                 '',
                                                 0,
                                                 '0',
                                                 '1',
                                                 '1',
                                                 '1',
                                                 TO_DATE('2022-5-13 11:48:21',
                                                         'yyyy-MM-dd HH24:mi:ss'),
                                                 '',
                                                 TO_DATE('2022-5-13 11:48:21',
                                                         'yyyy-MM-dd HH24:mi:ss'));

DECLARE
  content_3 CLOB := '${receiver}：
您好，您的收到了一个催办信息：${subject}, 请查收!
';
BEGIN
  UPDATE bpm_default_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1524959704600657922';
  COMMIT;
END;
/

INSERT
  INTO bpm_default_template(ID_,
                            TEMPLATE_TYPE_,
                            MESSAGE_TYPE_,
                            TEMPLATE_,
                            DELETED_,
                            COMPANY_ID_,
                            TENANT_ID_,
                            CREATE_DEP_ID_,
                            CREATE_BY_,
                            CREATE_TIME_,
                            UPDATE_BY_,
                            UPDATE_TIME_) VALUES('1524959775383732225',
                                                 'remind',
                                                 'mail',
                                                 '',
                                                 0,
                                                 '0',
                                                 '1',
                                                 '1',
                                                 '1',
                                                 TO_DATE('2022-5-13 11:48:38',
                                                         'yyyy-MM-dd HH24:mi:ss'),
                                                 '',
                                                 TO_DATE('2022-5-13 11:48:38',
                                                         'yyyy-MM-dd HH24:mi:ss'));

DECLARE
  content_3 CLOB := '${receiver}：
    您的收到了一个催办信息,任务：<a target="_blank" href="${serverUrl}/bpm/workbench/openDoc/${instId}">${subject}</a>请尽快处理.';
BEGIN
  UPDATE bpm_default_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1524959775383732225';
  COMMIT;
END;
/

INSERT
  INTO bpm_default_template(ID_,
                            TEMPLATE_TYPE_,
                            MESSAGE_TYPE_,
                            TEMPLATE_,
                            DELETED_,
                            COMPANY_ID_,
                            TENANT_ID_,
                            CREATE_DEP_ID_,
                            CREATE_BY_,
                            CREATE_TIME_,
                            UPDATE_BY_,
                            UPDATE_TIME_) VALUES('1524959902898962434',
                                                 'remind',
                                                 'sms',
                                                 '',
                                                 0,
                                                 '0',
                                                 '1',
                                                 '1',
                                                 '1',
                                                 TO_DATE('2022-5-13 11:49:08',
                                                         'yyyy-MM-dd HH24:mi:ss'),
                                                 '',
                                                 TO_DATE('2022-5-13 11:49:08',
                                                         'yyyy-MM-dd HH24:mi:ss'));

DECLARE
  content_3 CLOB := '${receiver}：
您好，您的收到了一个催办信息：${subject}, 请查收!
';
BEGIN
  UPDATE bpm_default_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1524959902898962434';
  COMMIT;
END;
/

INSERT
  INTO bpm_default_template(ID_,
                            TEMPLATE_TYPE_,
                            MESSAGE_TYPE_,
                            TEMPLATE_,
                            DELETED_,
                            COMPANY_ID_,
                            TENANT_ID_,
                            CREATE_DEP_ID_,
                            CREATE_BY_,
                            CREATE_TIME_,
                            UPDATE_BY_,
                            UPDATE_TIME_) VALUES('1524959974462177282',
                                                 'remind',
                                                 'inner',
                                                 '',
                                                 0,
                                                 '0',
                                                 '1',
                                                 '1',
                                                 '1',
                                                 TO_DATE('2022-5-13 11:49:25',
                                                         'yyyy-MM-dd HH24:mi:ss'),
                                                 '',
                                                 TO_DATE('2022-5-13 11:49:25',
                                                         'yyyy-MM-dd HH24:mi:ss'));

DECLARE
  content_3 CLOB := '${receiver}：
  您的收到了一个催办信息,任务：<a target="_blank" href="${serverUrl}/bpm/workbench/openDoc/${instId}">${subject}</a>请尽快处理.';
BEGIN
  UPDATE bpm_default_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1524959974462177282';
  COMMIT;
END;
/

INSERT
  INTO bpm_default_template(ID_,
                            TEMPLATE_TYPE_,
                            MESSAGE_TYPE_,
                            TEMPLATE_,
                            DELETED_,
                            COMPANY_ID_,
                            TENANT_ID_,
                            CREATE_DEP_ID_,
                            CREATE_BY_,
                            CREATE_TIME_,
                            UPDATE_BY_,
                            UPDATE_TIME_) VALUES('1524960289034977282',
                                                 'replycommu',
                                                 'ding',
                                                 '',
                                                 0,
                                                 '0',
                                                 '1',
                                                 '1',
                                                 '1',
                                                 TO_DATE('2022-5-13 11:50:40',
                                                         'yyyy-MM-dd HH24:mi:ss'),
                                                 '',
                                                 TO_DATE('2022-5-13 11:50:40',
                                                         'yyyy-MM-dd HH24:mi:ss'));

DECLARE
  content_3 CLOB := '${receiver}：
您好，${sender}回复了您的沟通：${subject}
';
BEGIN
  UPDATE bpm_default_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1524960289034977282';
  COMMIT;
END;
/

INSERT
  INTO bpm_default_template(ID_,
                            TEMPLATE_TYPE_,
                            MESSAGE_TYPE_,
                            TEMPLATE_,
                            DELETED_,
                            COMPANY_ID_,
                            TENANT_ID_,
                            CREATE_DEP_ID_,
                            CREATE_BY_,
                            CREATE_TIME_,
                            UPDATE_BY_,
                            UPDATE_TIME_) VALUES('1524960489740812289',
                                                 'replycommu',
                                                 'weixin',
                                                 '',
                                                 0,
                                                 '0',
                                                 '1',
                                                 '1',
                                                 '1',
                                                 TO_DATE('2022-5-13 11:51:28',
                                                         'yyyy-MM-dd HH24:mi:ss'),
                                                 '',
                                                 TO_DATE('2022-5-13 11:51:28',
                                                         'yyyy-MM-dd HH24:mi:ss'));

DECLARE
  content_3 CLOB := '${receiver}：
您好，${sender}回复了您的沟通：${subject}
';
BEGIN
  UPDATE bpm_default_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1524960489740812289';
  COMMIT;
END;
/

INSERT
  INTO bpm_default_template(ID_,
                            TEMPLATE_TYPE_,
                            MESSAGE_TYPE_,
                            TEMPLATE_,
                            DELETED_,
                            COMPANY_ID_,
                            TENANT_ID_,
                            CREATE_DEP_ID_,
                            CREATE_BY_,
                            CREATE_TIME_,
                            UPDATE_BY_,
                            UPDATE_TIME_) VALUES('1524960595357581314',
                                                 'replycommu',
                                                 'mail',
                                                 '',
                                                 0,
                                                 '0',
                                                 '1',
                                                 '1',
                                                 '1',
                                                 TO_DATE('2022-5-13 11:51:53',
                                                         'yyyy-MM-dd HH24:mi:ss'),
                                                 '',
                                                 TO_DATE('2022-5-13 11:51:53',
                                                         'yyyy-MM-dd HH24:mi:ss'));

DECLARE
  content_3 CLOB := '${receiver}：
您好，${sender}回复了您的沟通：<a target="_blank" href="${serverUrl}/bpm/workbench/openDoc/${instId}">${subject}</a>
';
BEGIN
  UPDATE bpm_default_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1524960595357581314';
  COMMIT;
END;
/

INSERT
  INTO bpm_default_template(ID_,
                            TEMPLATE_TYPE_,
                            MESSAGE_TYPE_,
                            TEMPLATE_,
                            DELETED_,
                            COMPANY_ID_,
                            TENANT_ID_,
                            CREATE_DEP_ID_,
                            CREATE_BY_,
                            CREATE_TIME_,
                            UPDATE_BY_,
                            UPDATE_TIME_) VALUES('1524960722138808322',
                                                 'replycommu',
                                                 'sms',
                                                 '',
                                                 0,
                                                 '0',
                                                 '1',
                                                 '1',
                                                 '1',
                                                 TO_DATE('2022-5-13 11:52:23',
                                                         'yyyy-MM-dd HH24:mi:ss'),
                                                 '',
                                                 TO_DATE('2022-5-13 11:52:23',
                                                         'yyyy-MM-dd HH24:mi:ss'));

DECLARE
  content_3 CLOB := '${receiver}：${sender}回复了您的沟通：${subject}
';
BEGIN
  UPDATE bpm_default_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1524960722138808322';
  COMMIT;
END;
/

INSERT
  INTO bpm_default_template(ID_,
                            TEMPLATE_TYPE_,
                            MESSAGE_TYPE_,
                            TEMPLATE_,
                            DELETED_,
                            COMPANY_ID_,
                            TENANT_ID_,
                            CREATE_DEP_ID_,
                            CREATE_BY_,
                            CREATE_TIME_,
                            UPDATE_BY_,
                            UPDATE_TIME_) VALUES('1524960781538541570',
                                                 'replycommu',
                                                 'inner',
                                                 '',
                                                 0,
                                                 '0',
                                                 '1',
                                                 '1',
                                                 '1',
                                                 TO_DATE('2022-5-13 11:52:37',
                                                         'yyyy-MM-dd HH24:mi:ss'),
                                                 '',
                                                 TO_DATE('2022-5-13 11:52:37',
                                                         'yyyy-MM-dd HH24:mi:ss'));

DECLARE
  content_3 CLOB := '${receiver}：
您好，${sender}回复了您的沟通：<a target="_blank" href="${serverUrl}/bpm/workbench/openDoc/${instId}">${subject}</a>
';
BEGIN
  UPDATE bpm_default_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1524960781538541570';
  COMMIT;
END;
/

INSERT
  INTO bpm_default_template(ID_,
                            TEMPLATE_TYPE_,
                            MESSAGE_TYPE_,
                            TEMPLATE_,
                            DELETED_,
                            COMPANY_ID_,
                            TENANT_ID_,
                            CREATE_DEP_ID_,
                            CREATE_BY_,
                            CREATE_TIME_,
                            UPDATE_BY_,
                            UPDATE_TIME_) VALUES('1524960946588598274',
                                                 'revoke',
                                                 'ding',
                                                 '',
                                                 0,
                                                 '0',
                                                 '1',
                                                 '1',
                                                 '1',
                                                 TO_DATE('2022-5-13 11:53:17',
                                                         'yyyy-MM-dd HH24:mi:ss'),
                                                 '',
                                                 TO_DATE('2022-5-13 11:53:17',
                                                         'yyyy-MM-dd HH24:mi:ss'));

DECLARE
  content_3 CLOB := '${receiver}：
您好， 您的任务被撤销：${subject}
需要办理,请及时处理!
';
BEGIN
  UPDATE bpm_default_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1524960946588598274';
  COMMIT;
END;
/

INSERT
  INTO bpm_default_template(ID_,
                            TEMPLATE_TYPE_,
                            MESSAGE_TYPE_,
                            TEMPLATE_,
                            DELETED_,
                            COMPANY_ID_,
                            TENANT_ID_,
                            CREATE_DEP_ID_,
                            CREATE_BY_,
                            CREATE_TIME_,
                            UPDATE_BY_,
                            UPDATE_TIME_) VALUES('1524961043594461186',
                                                 'revoke',
                                                 'weixin',
                                                 '',
                                                 0,
                                                 '0',
                                                 '1',
                                                 '1',
                                                 '1',
                                                 TO_DATE('2022-5-13 11:53:40',
                                                         'yyyy-MM-dd HH24:mi:ss'),
                                                 '',
                                                 TO_DATE('2022-5-13 11:53:40',
                                                         'yyyy-MM-dd HH24:mi:ss'));

DECLARE
  content_3 CLOB := '${receiver}：
您好， 您的任务被撤销：<a target="_blank" href="${serverUrl}/bpm/workbench/openDoc/${instId}">${subject}</a>
';
BEGIN
  UPDATE bpm_default_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1524961043594461186';
  COMMIT;
END;
/

INSERT
  INTO bpm_default_template(ID_,
                            TEMPLATE_TYPE_,
                            MESSAGE_TYPE_,
                            TEMPLATE_,
                            DELETED_,
                            COMPANY_ID_,
                            TENANT_ID_,
                            CREATE_DEP_ID_,
                            CREATE_BY_,
                            CREATE_TIME_,
                            UPDATE_BY_,
                            UPDATE_TIME_) VALUES('1524961152319209474',
                                                 'revoke',
                                                 'mail',
                                                 '',
                                                 0,
                                                 '0',
                                                 '1',
                                                 '1',
                                                 '1',
                                                 TO_DATE('2022-5-13 11:54:06',
                                                         'yyyy-MM-dd HH24:mi:ss'),
                                                 '',
                                                 TO_DATE('2022-5-13 11:54:06',
                                                         'yyyy-MM-dd HH24:mi:ss'));

DECLARE
  content_3 CLOB := '${receiver}：
您好， 您的任务被撤销：<a target="_blank" href="${serverUrl}/bpm/workbench/openDoc/${instId}">${subject}</a>
';
BEGIN
  UPDATE bpm_default_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1524961152319209474';
  COMMIT;
END;
/

INSERT
  INTO bpm_default_template(ID_,
                            TEMPLATE_TYPE_,
                            MESSAGE_TYPE_,
                            TEMPLATE_,
                            DELETED_,
                            COMPANY_ID_,
                            TENANT_ID_,
                            CREATE_DEP_ID_,
                            CREATE_BY_,
                            CREATE_TIME_,
                            UPDATE_BY_,
                            UPDATE_TIME_) VALUES('1524961611742298113',
                                                 'revoke',
                                                 'sms',
                                                 '',
                                                 0,
                                                 '0',
                                                 '1',
                                                 '1',
                                                 '1',
                                                 TO_DATE('2022-5-13 11:55:55',
                                                         'yyyy-MM-dd HH24:mi:ss'),
                                                 '',
                                                 TO_DATE('2022-5-13 11:55:55',
                                                         'yyyy-MM-dd HH24:mi:ss'));

DECLARE
  content_3 CLOB := '${receiver}：您的任务被撤销：${subject}
';
BEGIN
  UPDATE bpm_default_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1524961611742298113';
  COMMIT;
END;
/

INSERT
  INTO bpm_default_template(ID_,
                            TEMPLATE_TYPE_,
                            MESSAGE_TYPE_,
                            TEMPLATE_,
                            DELETED_,
                            COMPANY_ID_,
                            TENANT_ID_,
                            CREATE_DEP_ID_,
                            CREATE_BY_,
                            CREATE_TIME_,
                            UPDATE_BY_,
                            UPDATE_TIME_) VALUES('1524961685058732033',
                                                 'revoke',
                                                 'inner',
                                                 '',
                                                 0,
                                                 '0',
                                                 '1',
                                                 '1',
                                                 '1',
                                                 TO_DATE('2022-5-13 11:56:13',
                                                         'yyyy-MM-dd HH24:mi:ss'),
                                                 '',
                                                 TO_DATE('2022-5-13 11:56:13',
                                                         'yyyy-MM-dd HH24:mi:ss'));

DECLARE
  content_3 CLOB := '${receiver}：
您好， 您的任务被撤销：<a target="_blank" href="${serverUrl}/bpm/workbench/openDoc/${instId}">${subject}</a>
';
BEGIN
  UPDATE bpm_default_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1524961685058732033';
  COMMIT;
END;
/

INSERT
  INTO bpm_default_template(ID_,
                            TEMPLATE_TYPE_,
                            MESSAGE_TYPE_,
                            TEMPLATE_,
                            DELETED_,
                            COMPANY_ID_,
                            TENANT_ID_,
                            CREATE_DEP_ID_,
                            CREATE_BY_,
                            CREATE_TIME_,
                            UPDATE_BY_,
                            UPDATE_TIME_) VALUES('1524961951749357570',
                                                 'transfer',
                                                 'ding',
                                                 '',
                                                 0,
                                                 '0',
                                                 '1',
                                                 '1',
                                                 '1',
                                                 TO_DATE('2022-5-13 11:57:16',
                                                         'yyyy-MM-dd HH24:mi:ss'),
                                                 '',
                                                 TO_DATE('2022-5-13 11:57:16',
                                                         'yyyy-MM-dd HH24:mi:ss'));

DECLARE
  content_3 CLOB := '${receiver}：
${sender}转发给您一个任务：${subject},需要审批,请及时处理!
发送人意见： ${opinion}
';
BEGIN
  UPDATE bpm_default_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1524961951749357570';
  COMMIT;
END;
/

INSERT
  INTO bpm_default_template(ID_,
                            TEMPLATE_TYPE_,
                            MESSAGE_TYPE_,
                            TEMPLATE_,
                            DELETED_,
                            COMPANY_ID_,
                            TENANT_ID_,
                            CREATE_DEP_ID_,
                            CREATE_BY_,
                            CREATE_TIME_,
                            UPDATE_BY_,
                            UPDATE_TIME_) VALUES('1524962487244537858',
                                                 'transfer',
                                                 'weixin',
                                                 '',
                                                 0,
                                                 '0',
                                                 '1',
                                                 '1',
                                                 '1',
                                                 TO_DATE('2022-5-13 11:59:24',
                                                         'yyyy-MM-dd HH24:mi:ss'),
                                                 '',
                                                 TO_DATE('2022-5-13 11:59:24',
                                                         'yyyy-MM-dd HH24:mi:ss'));

DECLARE
  content_3 CLOB := '${receiver}：
${sender}转发给您一个任务：${subject},需要审批,请及时处理!
发送人意见： ${opinion}
';
BEGIN
  UPDATE bpm_default_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1524962487244537858';
  COMMIT;
END;
/

INSERT
  INTO bpm_default_template(ID_,
                            TEMPLATE_TYPE_,
                            MESSAGE_TYPE_,
                            TEMPLATE_,
                            DELETED_,
                            COMPANY_ID_,
                            TENANT_ID_,
                            CREATE_DEP_ID_,
                            CREATE_BY_,
                            CREATE_TIME_,
                            UPDATE_BY_,
                            UPDATE_TIME_) VALUES('1524962596728455170',
                                                 'transfer',
                                                 'mail',
                                                 '',
                                                 0,
                                                 '0',
                                                 '1',
                                                 '1',
                                                 '1',
                                                 TO_DATE('2022-5-13 11:59:50',
                                                         'yyyy-MM-dd HH24:mi:ss'),
                                                 '',
                                                 TO_DATE('2022-5-13 11:59:50',
                                                         'yyyy-MM-dd HH24:mi:ss'));

DECLARE
  content_3 CLOB := '${receiver}：
${sender}转发给您一个任务：<a target="_blank" href="${serverUrl}/bpm/workbench/openDoc/${instId}">${subject}</a>,需要审批,请及时处理!
发送人意见： ${opinion}
';
BEGIN
  UPDATE bpm_default_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1524962596728455170';
  COMMIT;
END;
/

INSERT
  INTO bpm_default_template(ID_,
                            TEMPLATE_TYPE_,
                            MESSAGE_TYPE_,
                            TEMPLATE_,
                            DELETED_,
                            COMPANY_ID_,
                            TENANT_ID_,
                            CREATE_DEP_ID_,
                            CREATE_BY_,
                            CREATE_TIME_,
                            UPDATE_BY_,
                            UPDATE_TIME_) VALUES('1524962779960819714',
                                                 'transfer',
                                                 'sms',
                                                 '',
                                                 0,
                                                 '0',
                                                 '1',
                                                 '1',
                                                 '1',
                                                 TO_DATE('2022-5-13 12:00:34',
                                                         'yyyy-MM-dd HH24:mi:ss'),
                                                 '',
                                                 TO_DATE('2022-5-13 12:00:34',
                                                         'yyyy-MM-dd HH24:mi:ss'));

DECLARE
  content_3 CLOB := '${receiver}：
    ${sender}转发给您一个任务：${subject}需要审批,请及时处理 
   发送人意见： ${opinion}
';
BEGIN
  UPDATE bpm_default_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1524962779960819714';
  COMMIT;
END;
/

INSERT
  INTO bpm_default_template(ID_,
                            TEMPLATE_TYPE_,
                            MESSAGE_TYPE_,
                            TEMPLATE_,
                            DELETED_,
                            COMPANY_ID_,
                            TENANT_ID_,
                            CREATE_DEP_ID_,
                            CREATE_BY_,
                            CREATE_TIME_,
                            UPDATE_BY_,
                            UPDATE_TIME_) VALUES('1524962847275204610',
                                                 'transfer',
                                                 'inner',
                                                 '',
                                                 0,
                                                 '0',
                                                 '1',
                                                 '1',
                                                 '1',
                                                 TO_DATE('2022-5-13 12:00:50',
                                                         'yyyy-MM-dd HH24:mi:ss'),
                                                 '',
                                                 TO_DATE('2022-5-13 12:00:50',
                                                         'yyyy-MM-dd HH24:mi:ss'));

DECLARE
  content_3 CLOB := '${receiver}：
  ${sender}转发给您一个任务：<a target="_blank" href="${serverUrl}/bpm/workbench/openDoc/${instId}">${subject}</a>,需要审批,请及时处理!
  发送人意见： ${opinion}
';
BEGIN
  UPDATE bpm_default_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1524962847275204610';
  COMMIT;
END;
/

INSERT
  INTO bpm_default_template(ID_,
                            TEMPLATE_TYPE_,
                            MESSAGE_TYPE_,
                            TEMPLATE_,
                            DELETED_,
                            COMPANY_ID_,
                            TENANT_ID_,
                            CREATE_DEP_ID_,
                            CREATE_BY_,
                            CREATE_TIME_,
                            UPDATE_BY_,
                            UPDATE_TIME_) VALUES('1524969814110617602',
                                                 'transferRoam',
                                                 'ding',
                                                 '',
                                                 0,
                                                 '0',
                                                 '1',
                                                 '1',
                                                 '1',
                                                 TO_DATE('2022-5-13 12:28:31',
                                                         'yyyy-MM-dd HH24:mi:ss'),
                                                 '',
                                                 TO_DATE('2022-5-13 12:28:31',
                                                         'yyyy-MM-dd HH24:mi:ss'));

DECLARE
  content_3 CLOB := '${receiver}：
${sender}流转给您一个任务：${subject},需要审批,请及时处理!
发送人意见： ${opinion}
';
BEGIN
  UPDATE bpm_default_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1524969814110617602';
  COMMIT;
END;
/

INSERT
  INTO bpm_default_template(ID_,
                            TEMPLATE_TYPE_,
                            MESSAGE_TYPE_,
                            TEMPLATE_,
                            DELETED_,
                            COMPANY_ID_,
                            TENANT_ID_,
                            CREATE_DEP_ID_,
                            CREATE_BY_,
                            CREATE_TIME_,
                            UPDATE_BY_,
                            UPDATE_TIME_) VALUES('1524969920759185409',
                                                 'transferRoam',
                                                 'weixin',
                                                 '',
                                                 0,
                                                 '0',
                                                 '1',
                                                 '1',
                                                 '1',
                                                 TO_DATE('2022-5-13 12:28:56',
                                                         'yyyy-MM-dd HH24:mi:ss'),
                                                 '',
                                                 TO_DATE('2022-5-13 12:28:56',
                                                         'yyyy-MM-dd HH24:mi:ss'));

DECLARE
  content_3 CLOB := '${receiver}：
  ${sender}流转给您一个任务：<a target="_blank" href="${serverUrl}/bpm/workbench/openDoc/${instId}">${subject}</a>,需要审批,请及时处理!
  发送人意见： ${opinion}
';
BEGIN
  UPDATE bpm_default_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1524969920759185409';
  COMMIT;
END;
/

INSERT
  INTO bpm_default_template(ID_,
                            TEMPLATE_TYPE_,
                            MESSAGE_TYPE_,
                            TEMPLATE_,
                            DELETED_,
                            COMPANY_ID_,
                            TENANT_ID_,
                            CREATE_DEP_ID_,
                            CREATE_BY_,
                            CREATE_TIME_,
                            UPDATE_BY_,
                            UPDATE_TIME_) VALUES('1524970010664091650',
                                                 'transferRoam',
                                                 'mail',
                                                 '',
                                                 0,
                                                 '0',
                                                 '1',
                                                 '1',
                                                 '1',
                                                 TO_DATE('2022-5-13 12:29:18',
                                                         'yyyy-MM-dd HH24:mi:ss'),
                                                 '',
                                                 TO_DATE('2022-5-13 12:29:18',
                                                         'yyyy-MM-dd HH24:mi:ss'));

DECLARE
  content_3 CLOB := '${receiver}：
${sender}流转给您一个任务：<a target="_blank" href="${serverUrl}/bpm/workbench/openDoc/${instId}">${subject}</a>,需要审批,请及时处理!
发送人意见： ${opinion}
';
BEGIN
  UPDATE bpm_default_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1524970010664091650';
  COMMIT;
END;
/

INSERT
  INTO bpm_default_template(ID_,
                            TEMPLATE_TYPE_,
                            MESSAGE_TYPE_,
                            TEMPLATE_,
                            DELETED_,
                            COMPANY_ID_,
                            TENANT_ID_,
                            CREATE_DEP_ID_,
                            CREATE_BY_,
                            CREATE_TIME_,
                            UPDATE_BY_,
                            UPDATE_TIME_) VALUES('1524970072655904769',
                                                 'transferRoam',
                                                 'sms',
                                                 '',
                                                 0,
                                                 '0',
                                                 '1',
                                                 '1',
                                                 '1',
                                                 TO_DATE('2022-5-13 12:29:33',
                                                         'yyyy-MM-dd HH24:mi:ss'),
                                                 '',
                                                 TO_DATE('2022-5-13 12:29:33',
                                                         'yyyy-MM-dd HH24:mi:ss'));

DECLARE
  content_3 CLOB := '${receiver}：${sender}流转给您一个任务：${subject}需要审批,请及时处理!
发送人意见： ${opinion}
';
BEGIN
  UPDATE bpm_default_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1524970072655904769';
  COMMIT;
END;
/

INSERT
  INTO bpm_default_template(ID_,
                            TEMPLATE_TYPE_,
                            MESSAGE_TYPE_,
                            TEMPLATE_,
                            DELETED_,
                            COMPANY_ID_,
                            TENANT_ID_,
                            CREATE_DEP_ID_,
                            CREATE_BY_,
                            CREATE_TIME_,
                            UPDATE_BY_,
                            UPDATE_TIME_) VALUES('1524970241375977473',
                                                 'transferRoam',
                                                 'inner',
                                                 '',
                                                 0,
                                                 '0',
                                                 '1',
                                                 '1',
                                                 '1',
                                                 TO_DATE('2022-5-13 12:30:13',
                                                         'yyyy-MM-dd HH24:mi:ss'),
                                                 '',
                                                 TO_DATE('2022-5-13 12:30:13',
                                                         'yyyy-MM-dd HH24:mi:ss'));

DECLARE
  content_3 CLOB := '${receiver}：
  ${sender}流转给您一个任务：<a target="_blank" href="${serverUrl}/bpm/workbench/openDoc/${instId}">${subject}</a>,需要审批,请及时处理!
  发送人意见： ${opinion}
';
BEGIN
  UPDATE bpm_default_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1524970241375977473';
  COMMIT;
END;
/

INSERT
  INTO bpm_default_template(ID_,
                            TEMPLATE_TYPE_,
                            MESSAGE_TYPE_,
                            TEMPLATE_,
                            DELETED_,
                            COMPANY_ID_,
                            TENANT_ID_,
                            CREATE_DEP_ID_,
                            CREATE_BY_,
                            CREATE_TIME_,
                            UPDATE_BY_,
                            UPDATE_TIME_) VALUES('1524970322711920642',
                                                 'urge',
                                                 'ding',
                                                 '',
                                                 0,
                                                 '0',
                                                 '1',
                                                 '1',
                                                 '1',
                                                 TO_DATE('2022-5-13 12:30:32',
                                                         'yyyy-MM-dd HH24:mi:ss'),
                                                 '',
                                                 TO_DATE('2022-5-13 12:30:32',
                                                         'yyyy-MM-dd HH24:mi:ss'));

DECLARE
  content_3 CLOB := '${receiver}：
${sender}提交了一个催办：${subject}  ${opinion}
需要审批,请及时处理!
';
BEGIN
  UPDATE bpm_default_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1524970322711920642';
  COMMIT;
END;
/

INSERT
  INTO bpm_default_template(ID_,
                            TEMPLATE_TYPE_,
                            MESSAGE_TYPE_,
                            TEMPLATE_,
                            DELETED_,
                            COMPANY_ID_,
                            TENANT_ID_,
                            CREATE_DEP_ID_,
                            CREATE_BY_,
                            CREATE_TIME_,
                            UPDATE_BY_,
                            UPDATE_TIME_) VALUES('1524970450239733762',
                                                 'urge',
                                                 'weixin',
                                                 '',
                                                 0,
                                                 '0',
                                                 '1',
                                                 '1',
                                                 '1',
                                                 TO_DATE('2022-5-13 12:31:03',
                                                         'yyyy-MM-dd HH24:mi:ss'),
                                                 '',
                                                 TO_DATE('2022-5-13 12:31:03',
                                                         'yyyy-MM-dd HH24:mi:ss'));

DECLARE
  content_3 CLOB := '${receiver}：
  ${sender}提交了一个催办：${subject}  ${opinion}
需要审批,请及时处理!
';
BEGIN
  UPDATE bpm_default_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1524970450239733762';
  COMMIT;
END;
/

INSERT
  INTO bpm_default_template(ID_,
                            TEMPLATE_TYPE_,
                            MESSAGE_TYPE_,
                            TEMPLATE_,
                            DELETED_,
                            COMPANY_ID_,
                            TENANT_ID_,
                            CREATE_DEP_ID_,
                            CREATE_BY_,
                            CREATE_TIME_,
                            UPDATE_BY_,
                            UPDATE_TIME_) VALUES('1524970574231748610',
                                                 'urge',
                                                 'mail',
                                                 '',
                                                 0,
                                                 '0',
                                                 '1',
                                                 '1',
                                                 '1',
                                                 TO_DATE('2022-5-13 12:31:32',
                                                         'yyyy-MM-dd HH24:mi:ss'),
                                                 '',
                                                 TO_DATE('2022-5-13 12:31:32',
                                                         'yyyy-MM-dd HH24:mi:ss'));

DECLARE
  content_3 CLOB := '${receiver}：
${sender}提交了一个催办：<a target="_blank" href="${serverUrl}/bpm/workbench/openDoc/${instId}">${subject} ${opinion}</a>
需要审批,请及时处理!
';
BEGIN
  UPDATE bpm_default_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1524970574231748610';
  COMMIT;
END;
/

INSERT
  INTO bpm_default_template(ID_,
                            TEMPLATE_TYPE_,
                            MESSAGE_TYPE_,
                            TEMPLATE_,
                            DELETED_,
                            COMPANY_ID_,
                            TENANT_ID_,
                            CREATE_DEP_ID_,
                            CREATE_BY_,
                            CREATE_TIME_,
                            UPDATE_BY_,
                            UPDATE_TIME_) VALUES('1524970634826858497',
                                                 'urge',
                                                 'sms',
                                                 '',
                                                 0,
                                                 '0',
                                                 '1',
                                                 '1',
                                                 '1',
                                                 TO_DATE('2022-5-13 12:31:47',
                                                         'yyyy-MM-dd HH24:mi:ss'),
                                                 '',
                                                 TO_DATE('2022-5-13 12:31:47',
                                                         'yyyy-MM-dd HH24:mi:ss'));

DECLARE
  content_3 CLOB := '${receiver}：${sender}提交了一个催办：${subject}  ${opinion}
需要审批,请及时处理!';
BEGIN
  UPDATE bpm_default_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1524970634826858497';
  COMMIT;
END;
/

INSERT
  INTO bpm_default_template(ID_,
                            TEMPLATE_TYPE_,
                            MESSAGE_TYPE_,
                            TEMPLATE_,
                            DELETED_,
                            COMPANY_ID_,
                            TENANT_ID_,
                            CREATE_DEP_ID_,
                            CREATE_BY_,
                            CREATE_TIME_,
                            UPDATE_BY_,
                            UPDATE_TIME_) VALUES('1524970703294676993',
                                                 'urge',
                                                 'inner',
                                                 '',
                                                 0,
                                                 '0',
                                                 '1',
                                                 '1',
                                                 '1',
                                                 TO_DATE('2022-5-13 12:32:03',
                                                         'yyyy-MM-dd HH24:mi:ss'),
                                                 '',
                                                 TO_DATE('2022-5-13 12:32:03',
                                                         'yyyy-MM-dd HH24:mi:ss'));

DECLARE
  content_3 CLOB := '${receiver}：
  ${sender}提交了一个催办：${subject}  ${opinion}
需要审批,请及时处理!
';
BEGIN
  UPDATE bpm_default_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1524970703294676993';
  COMMIT;
END;
/

INSERT
  INTO bpm_default_template(ID_,
                            TEMPLATE_TYPE_,
                            MESSAGE_TYPE_,
                            TEMPLATE_,
                            DELETED_,
                            COMPANY_ID_,
                            TENANT_ID_,
                            CREATE_DEP_ID_,
                            CREATE_BY_,
                            CREATE_TIME_,
                            UPDATE_BY_,
                            UPDATE_TIME_) VALUES('1537316546681069570',
                                                 'tracked',
                                                 'inner',
                                                 '',
                                                 0,
                                                 '0',
                                                 '1',
                                                 '1',
                                                 '1',
                                                 TO_DATE('2022-6-16 14:10:01',
                                                         'yyyy-MM-dd HH24:mi:ss'),
                                                 '1',
                                                 TO_DATE('2022-6-16 17:19:14',
                                                         'yyyy-MM-dd HH24:mi:ss'));

DECLARE
  content_3 CLOB := '${receiver}:你好

你跟踪的流程 <a href="${serverUrl}/bpm/workbench/openDoc/${instId}" target="_blank">${subject}</a> ,
<span rx-if="''${status}''==''approve''">
    流程步骤 :${nodeName} 已审批完成!
</span>
<span rx-if="''${status}''!=''approve''">
    流程审批完成!
</span>
';
BEGIN
  UPDATE bpm_default_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1537316546681069570';
  COMMIT;
END;
/

INSERT
  INTO bpm_default_template(ID_,
                            TEMPLATE_TYPE_,
                            MESSAGE_TYPE_,
                            TEMPLATE_,
                            DELETED_,
                            COMPANY_ID_,
                            TENANT_ID_,
                            CREATE_DEP_ID_,
                            CREATE_BY_,
                            CREATE_TIME_,
                            UPDATE_BY_,
                            UPDATE_TIME_) VALUES('1537316622560223234',
                                                 'tracked',
                                                 'mail',
                                                 '',
                                                 0,
                                                 '0',
                                                 '1',
                                                 '1',
                                                 '1',
                                                 TO_DATE('2022-6-16 14:10:19',
                                                         'yyyy-MM-dd HH24:mi:ss'),
                                                 '1',
                                                 TO_DATE('2022-6-16 17:19:11',
                                                         'yyyy-MM-dd HH24:mi:ss'));

DECLARE
  content_3 CLOB := '${receiver}:你好

你跟踪的流程 <a href="${serverUrl}/bpm/workbench/openDoc/${instId}" target="_blank">${subject}</a> ,
<span rx-if="''${status}''==''approve''">
    流程步骤 :${nodeName} 已审批完成!
</span>
<span rx-if="''${status}''!=''approve''">
    流程审批完成!
</span>
';
BEGIN
  UPDATE bpm_default_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1537316622560223234';
  COMMIT;
END;
/

INSERT
  INTO bpm_default_template(ID_,
                            TEMPLATE_TYPE_,
                            MESSAGE_TYPE_,
                            TEMPLATE_,
                            DELETED_,
                            COMPANY_ID_,
                            TENANT_ID_,
                            CREATE_DEP_ID_,
                            CREATE_BY_,
                            CREATE_TIME_,
                            UPDATE_BY_,
                            UPDATE_TIME_) VALUES('1537316679560814594',
                                                 'tracked',
                                                 'sms',
                                                 '',
                                                 0,
                                                 '0',
                                                 '1',
                                                 '1',
                                                 '1',
                                                 TO_DATE('2022-6-16 14:10:33',
                                                         'yyyy-MM-dd HH24:mi:ss'),
                                                 '1',
                                                 TO_DATE('2022-6-16 17:19:09',
                                                         'yyyy-MM-dd HH24:mi:ss'));

DECLARE
  content_3 CLOB := '${receiver}:你好

你跟踪的流程 <a href="${serverUrl}/bpm/workbench/openDoc/${instId}" target="_blank">${subject}</a> ,
<span rx-if="''${status}''==''approve''">
    流程步骤 :${nodeName} 已审批完成!
</span>
<span rx-if="''${status}''!=''approve''">
    流程审批完成!
</span>
';
BEGIN
  UPDATE bpm_default_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1537316679560814594';
  COMMIT;
END;
/

INSERT
  INTO bpm_default_template(ID_,
                            TEMPLATE_TYPE_,
                            MESSAGE_TYPE_,
                            TEMPLATE_,
                            DELETED_,
                            COMPANY_ID_,
                            TENANT_ID_,
                            CREATE_DEP_ID_,
                            CREATE_BY_,
                            CREATE_TIME_,
                            UPDATE_BY_,
                            UPDATE_TIME_) VALUES('1537316738314625026',
                                                 'tracked',
                                                 'weixin',
                                                 '',
                                                 0,
                                                 '0',
                                                 '1',
                                                 '1',
                                                 '1',
                                                 TO_DATE('2022-6-16 14:10:47',
                                                         'yyyy-MM-dd HH24:mi:ss'),
                                                 '1',
                                                 TO_DATE('2022-6-16 17:19:07',
                                                         'yyyy-MM-dd HH24:mi:ss'));

DECLARE
  content_3 CLOB := '${receiver}:你好

你跟踪的流程 <a href="${serverUrl}/bpm/workbench/openDoc/${instId}" target="_blank">${subject}</a> ,
<span rx-if="''${status}''==''approve''">
    流程步骤 :${nodeName} 已审批完成!
</span>
<span rx-if="''${status}''!=''approve''">
    流程审批完成!
</span>
';
BEGIN
  UPDATE bpm_default_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1537316738314625026';
  COMMIT;
END;
/

INSERT
  INTO bpm_default_template(ID_,
                            TEMPLATE_TYPE_,
                            MESSAGE_TYPE_,
                            TEMPLATE_,
                            DELETED_,
                            COMPANY_ID_,
                            TENANT_ID_,
                            CREATE_DEP_ID_,
                            CREATE_BY_,
                            CREATE_TIME_,
                            UPDATE_BY_,
                            UPDATE_TIME_) VALUES('1537316872326832130',
                                                 'tracked',
                                                 'ding',
                                                 '',
                                                 0,
                                                 '0',
                                                 '1',
                                                 '1',
                                                 '1',
                                                 TO_DATE('2022-6-16 14:11:19',
                                                         'yyyy-MM-dd HH24:mi:ss'),
                                                 '1',
                                                 TO_DATE('2022-6-16 17:19:02',
                                                         'yyyy-MM-dd HH24:mi:ss'));

DECLARE
  content_3 CLOB := '${receiver}:你好

你跟踪的流程 <a href="${serverUrl}/bpm/workbench/openDoc/${instId}" target="_blank">${subject}</a> ,
<span rx-if="''${status}''==''approve''">
    流程步骤 :${nodeName} 已审批完成!
</span>
<span rx-if="''${status}''!=''approve''">
    流程审批完成!
</span>
';
BEGIN
  UPDATE bpm_default_template
     SET TEMPLATE_ = content_3
   WHERE ID_ = '1537316872326832130';
  COMMIT;
END;
/

INSERT
  INTO bpm_caltime_block(SETTING_ID_,
                         SETTING_NAME_,
                         TIME_INTERVALS_,
                         DELETED_,
                         COMPANY_ID_,
                         TENANT_ID_,
                         CREATE_DEP_ID_,
                         CREATE_BY_,
                         CREATE_TIME_,
                         UPDATE_BY_,
                         UPDATE_TIME_) VALUES('1273798821971140609',
                                              '白班',
                                              '[{"startTime":"08:00","endTime":"12:00","totalTime":"240"},{"startTime":"13:30","endTime":"18:00","totalTime":270}]',
                                              0,
                                              '0',
                                              '0',
                                              '1',
                                              '1',
                                              TO_DATE('2020-6-19 10:04:40',
                                                      'yyyy-MM-dd HH24:mi:ss'),
                                              '',
                                              TO_DATE('2020-6-19 10:04:40',
                                                      'yyyy-MM-dd HH24:mi:ss'));

COMMIT;
