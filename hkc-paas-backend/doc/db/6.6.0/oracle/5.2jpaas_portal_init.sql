INSERT INTO ins_column_def
  (COL_ID_,
   NAME_,
   KEY_,
   IS_DEFAULT_,
   TEMPLET_,
   SET_TING_,
   IS_PUBLIC_,
   TYPE_,
   ICON_,
   IS_MOBILE_,
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
  ('1239834260509945858',
   '待办事项',
   'BpmTaskColumn',
   '0',
   '',
   '',
   '0',
   '1230771994887495681',
   '',
   '0',
   0,
   '0',
   '0',
   '2',
   '1',
   NULL,
   '1',
   TO_DATE('2021-6-24 18:13:38', 'yyyy-MM-dd HH24:mi:ss'),
   '');

DECLARE
  content_4 CLOB := '<div class="gridLayoutClass">    <div class="headPClass">        <span style="display: inline-block;font-size: 16px;">{{insColumnDef.name}}</span>        <div style="float:right;">            <div class="journalism_span">                <span @click="moreUrl()">更多</span>                <span @click="refresh">刷新</span>            </div>        </div>    </div>    <div class="bodyDivClass">        <ul>            <li class="itmelist" v-for="(obj,index) of data" :key="obj.taskId" v-if="index <=8">                <p @click="handleTask(obj)">{{obj.subject}}</p>                <span>{{obj.createTime}}</span>            </li>        </ul>    </div></div>';
  content_5 CLOB := '{"dataUrl":"/webApp/home/MyBpmTaskList","function":"portalScript.getPortalBpmTask()","isNews":"","newType":"","tabgroups":"","functions":"portalScript.getPortalBpmTask(colId)","funcType":"func","sqlName":"省市"}';
BEGIN
  UPDATE ins_column_def
     SET TEMPLET_ = content_4
   WHERE COL_ID_ = '1239834260509945858';
  UPDATE ins_column_def
     SET SET_TING_ = content_5
   WHERE COL_ID_ = '1239834260509945858';
  COMMIT;
END;
/

INSERT
  INTO ins_column_def(COL_ID_,
                      NAME_,
                      KEY_,
                      IS_DEFAULT_,
                      TEMPLET_,
                      SET_TING_,
                      IS_PUBLIC_,
                      TYPE_,
                      ICON_,
                      IS_MOBILE_,
                      DELETED_,
                      COMPANY_ID_,
                      TENANT_ID_,
                      CREATE_DEP_ID_,
                      CREATE_BY_,
                      CREATE_TIME_,
                      UPDATE_BY_,
                      UPDATE_TIME_,
                      APP_ID_) VALUES('1239837443886018562',
                                      '集团新闻',
                                      'insNews',
                                      '0',
                                      '',
                                      '',
                                      '0',
                                      '1230775094247464962',
                                      '',
                                      '0',
                                      0,
                                      '0',
                                      '0',
                                      '2',
                                      '1',
                                      NULL,
                                      '1',
                                      TO_DATE('2021-6-26 15:52:28',
                                              'yyyy-MM-dd HH24:mi:ss'),
                                      '');

DECLARE
  content_4 CLOB := '<div class="gridLayoutClass">    <div class="headPClass">        <div style="display:inline-block;" >{{insColumnDef.name}}</div>        <div style="float: right">            <div class="journalism_span">             <span @click="moreUrl()">更多</span>             <span @click="refresh">刷新</span>            </div>        </div>    </div>    <div class="bodyDivClass">        <div class="aclsty" v-if="hasImage(data)">            <a-carousel arrows autoplay >                <div  slot="prevArrow"                    slot-scope="props"                    class="custom-slick-arrow"                    style="left: 10px;zIndex: 1"                    >                    <a-icon type="left-circle" />                </div>                <div slot="nextArrow" slot-scope="props" class="custom-slick-arrow" style="right: 10px">                    <a-icon type="right-circle" />                </div>                <div v-for="obj of data" v-if="obj.imgFileId"  class="inglist">                    <img :src="getImgPath(obj.imgFileId)"></img>                </div>            </a-carousel>        </div>        <div  >            <ul>                <li class="itmelist" v-for="(obj,index) of data" :key="obj.pkId" v-if="index <=5">                  <p @click="getInsNews(obj.pkId)">{{obj.subject}}</p>                  <span>{{obj.createTime}}</span>                </li>            </ul>        </div>    </div></div>';
  content_5 CLOB := '{"dataUrl":"/portal/InsNewsIssueds","function":"portalScript.getPortalNews(colId)","isNews":"YES","newType":"wordsList","tabgroups":"","newDIc":"新闻公告"}';
BEGIN
  UPDATE ins_column_def
     SET TEMPLET_ = content_4
   WHERE COL_ID_ = '1239837443886018562';
  UPDATE ins_column_def
     SET SET_TING_ = content_5
   WHERE COL_ID_ = '1239837443886018562';
  COMMIT;
END;
/

INSERT
  INTO ins_column_def(COL_ID_,
                      NAME_,
                      KEY_,
                      IS_DEFAULT_,
                      TEMPLET_,
                      SET_TING_,
                      IS_PUBLIC_,
                      TYPE_,
                      ICON_,
                      IS_MOBILE_,
                      DELETED_,
                      COMPANY_ID_,
                      TENANT_ID_,
                      CREATE_DEP_ID_,
                      CREATE_BY_,
                      CREATE_TIME_,
                      UPDATE_BY_,
                      UPDATE_TIME_,
                      APP_ID_) VALUES('1239838889297702914',
                                      '消息盒子',
                                      'newsColumn',
                                      '0',
                                      '',
                                      '',
                                      '0',
                                      '1230774411821621249',
                                      'iconxinjian',
                                      '0',
                                      0,
                                      '0',
                                      '0',
                                      '2',
                                      '1',
                                      NULL,
                                      '1',
                                      TO_DATE('2021-9-12 19:49:52',
                                              'yyyy-MM-dd HH24:mi:ss'),
                                      '');

DECLARE
  content_4 CLOB := '<div class="msgBoxs">    <ul class="msgUl">        <li class="msgItme"            :style="{width:listWidthFn}"            v-for="(obj,index) of data"          :key="index">        <div @click="moreUrl(obj.url)" class="contentBox" :style="{''background'': obj.color != ''''?obj.color :''red''}">            <div class="msgContent">            <b>{{obj.count}}</b>            <p>{{obj.content}}</p >          </div>          <div class="header_icon">                    <a-icon v-if="!obj.icon.includes(''type'')" :type="obj.icon" />                    <my-icon v-else-if="[''customIcon'',''userCustomIcon''].includes(JSON.parse(obj.icon).type)" :type="JSON.parse(obj.icon).icon"></my-icon>                    <a-icon v-else="JSON.parse(obj.icon).type" :type="JSON.parse(obj.icon).icon"></a-icon>          </div>        </div>    </li>    </ul></div>';
  content_5 CLOB := '{"dataUrl":"","function":"1235758510261506049","newType":"","tabgroups":"","listCount":4,"newDIc":"","funcType":"","sqlName":"","service":"","calendarMode":""}';
BEGIN
  UPDATE ins_column_def
     SET TEMPLET_ = content_4
   WHERE COL_ID_ = '1239838889297702914';
  UPDATE ins_column_def
     SET SET_TING_ = content_5
   WHERE COL_ID_ = '1239838889297702914';
  COMMIT;
END;
/

INSERT
  INTO ins_column_def(COL_ID_,
                      NAME_,
                      KEY_,
                      IS_DEFAULT_,
                      TEMPLET_,
                      SET_TING_,
                      IS_PUBLIC_,
                      TYPE_,
                      ICON_,
                      IS_MOBILE_,
                      DELETED_,
                      COMPANY_ID_,
                      TENANT_ID_,
                      CREATE_DEP_ID_,
                      CREATE_BY_,
                      CREATE_TIME_,
                      UPDATE_BY_,
                      UPDATE_TIME_,
                      APP_ID_) VALUES('1239839327900266497',
                                      '公司公告',
                                      'newsPortColumn',
                                      '0',
                                      '',
                                      '',
                                      '0',
                                      '1230775094247464962',
                                      'iconbaobiao1',
                                      '0',
                                      0,
                                      '0',
                                      '0',
                                      '2',
                                      '1',
                                      NULL,
                                      '1',
                                      TO_DATE('2021-6-29 18:39:53',
                                              'yyyy-MM-dd HH24:mi:ss'),
                                      '');

DECLARE
  content_4 CLOB := '<div class="gridLayoutClass">    <div class="headPClass">        <div style="display:inline-block;" >{{insColumnDef.name}}</div>        <div style="float: right">            <div class="journalism_span">             <span @click="moreUrl()">更多</span>             <span @click="refresh">刷新</span>            </div>        </div>    </div>    <div class="bodyDivClass">        <div class="aclsty" v-if="hasImage(data)">            <a-carousel arrows autoplay >                <div  slot="prevArrow"                    slot-scope="props"                    class="custom-slick-arrow"                    style="left: 10px;zIndex: 1"                    >                    <a-icon type="left-circle" />                </div>                <div slot="nextArrow" slot-scope="props" class="custom-slick-arrow" style="right: 10px">                    <a-icon type="right-circle" />                </div>                <div v-for="obj of data"  v-if="obj.imgFileId" class="inglist">                    <img v-if="obj.imgFileId" :src="getImgPath(obj.imgFileId)"></img>                </div>            </a-carousel>        </div>        <div  >            <ul>                <li class="itmelist" v-for="(obj,index) of data" :key="obj.pkId" v-if="index <=5">                  <p @click="getInsNews(obj.pkId)">{{obj.subject}}</p>                  <span>{{obj.createTime}}</span>                </li>            </ul>        </div>    </div></div>';
  content_5 CLOB := '{"dataUrl":"/portal/InsNewsIssueds","function":"","newType":"wordsList","tabgroups":"","newDIc":"公司公告"}';
BEGIN
  UPDATE ins_column_def
     SET TEMPLET_ = content_4
   WHERE COL_ID_ = '1239839327900266497';
  UPDATE ins_column_def
     SET SET_TING_ = content_5
   WHERE COL_ID_ = '1239839327900266497';
  COMMIT;
END;
/

INSERT
  INTO ins_column_def(COL_ID_,
                      NAME_,
                      KEY_,
                      IS_DEFAULT_,
                      TEMPLET_,
                      SET_TING_,
                      IS_PUBLIC_,
                      TYPE_,
                      ICON_,
                      IS_MOBILE_,
                      DELETED_,
                      COMPANY_ID_,
                      TENANT_ID_,
                      CREATE_DEP_ID_,
                      CREATE_BY_,
                      CREATE_TIME_,
                      UPDATE_BY_,
                      UPDATE_TIME_,
                      APP_ID_) VALUES('1239839763151581185',
                                      '待办已办',
                                      'tabColumn',
                                      '0',
                                      '',
                                      '',
                                      '0',
                                      '1230774801220804610',
                                      '',
                                      '0',
                                      0,
                                      '0',
                                      '0',
                                      '2',
                                      '1',
                                      NULL,
                                      '1',
                                      TO_DATE('2021-6-29 14:48:32',
                                              'yyyy-MM-dd HH24:mi:ss'),
                                      '');

DECLARE
  content_4 CLOB := '<div class="page-header-index-wide">      <div class="salesCard">        <a-tabs default-active-key=0 size="large"  @change="changeKey" style="height:100%;" >          <div class="extra-wrapper" slot="tabBarExtraContent">              <div class="journalism_span">             <span @click="moreUrl()">更多</span>             <span @click="refresh">刷新</span>           </div>          </div>          <a-tab-pane class="hetlist" v-for="(obj,index) of data"  :tab="obj.name" :key="index">             <portal-layoutview ref="innerLayout" :insColumnDef="obj.insColumnDef" > </portal-layoutview>          </a-tab-pane>        </a-tabs>      </div>  </div>';
  content_5 CLOB := '{"dataUrl":"","function":"","isNews":"","newType":"","tabgroups":{"value":"1239834260509945858,1361616576253440002","text":"待办事项,已办事项"}}';
BEGIN
  UPDATE ins_column_def
     SET TEMPLET_ = content_4
   WHERE COL_ID_ = '1239839763151581185';
  UPDATE ins_column_def
     SET SET_TING_ = content_5
   WHERE COL_ID_ = '1239839763151581185';
  COMMIT;
END;
/

INSERT
  INTO ins_column_def(COL_ID_,
                      NAME_,
                      KEY_,
                      IS_DEFAULT_,
                      TEMPLET_,
                      SET_TING_,
                      IS_PUBLIC_,
                      TYPE_,
                      ICON_,
                      IS_MOBILE_,
                      DELETED_,
                      COMPANY_ID_,
                      TENANT_ID_,
                      CREATE_DEP_ID_,
                      CREATE_BY_,
                      CREATE_TIME_,
                      UPDATE_BY_,
                      UPDATE_TIME_,
                      APP_ID_) VALUES('1259742027670732801',
                                      '常用应用',
                                      'application',
                                      '0',
                                      '',
                                      '',
                                      '0',
                                      '1230771994887495681',
                                      '',
                                      '0',
                                      0,
                                      '0',
                                      '0',
                                      '1',
                                      '1',
                                      NULL,
                                      '1',
                                      TO_DATE('2021-6-29 10:51:36',
                                              'yyyy-MM-dd HH24:mi:ss'),
                                      '');

DECLARE
  content_4 CLOB := '<div class="gridLayoutClass"><div class="headPClass">常用应用</div><div class="bodyDivClass"><ul class="application_ul">        <li v-for="obj in data">      <div v-if="obj.type ===''interior''" @click="moreUrl(obj.url)">            <a-icon v-if="!obj.icon.includes(''type'')" :type="obj.icon" />            <my-icon v-else-if="[''customIcon'',''userCustomIcon''].includes(JSON.parse(obj.icon).type)" :type="JSON.parse(obj.icon).icon"></my-icon>            <a-icon v-else="JSON.parse(obj.icon).type" :type="JSON.parse(obj.icon).icon"></a-icon>            <span>{{obj.name}}</span>      </div>      <div v-if="obj.type ===''outside''" @click="moreOutUrl(obj.url)">                             <a-icon v-if="!obj.icon.includes(''type'')" :type="obj.icon" />                <my-icon v-else-if="[''customIcon'',''userCustomIcon''].includes(JSON.parse(obj.icon).type)" :type="JSON.parse(obj.icon).icon"></my-icon>                <a-icon v-else="JSON.parse(obj.icon).type" :type="JSON.parse(obj.icon).icon"></a-icon>                <span>{{obj.name}}</span>         </div>        </li>      </ul></div></div>';
  content_5 CLOB := '{"dataUrl":"","function":"portalScript.getMyAppList()","newType":"","tabgroups":"","listCount":6,"newDIc":"","funcType":"","sqlName":"","service":"","calendarMode":""}';
BEGIN
  UPDATE ins_column_def
     SET TEMPLET_ = content_4
   WHERE COL_ID_ = '1259742027670732801';
  UPDATE ins_column_def
     SET SET_TING_ = content_5
   WHERE COL_ID_ = '1259742027670732801';
  COMMIT;
END;
/

INSERT
  INTO ins_column_def(COL_ID_,
                      NAME_,
                      KEY_,
                      IS_DEFAULT_,
                      TEMPLET_,
                      SET_TING_,
                      IS_PUBLIC_,
                      TYPE_,
                      ICON_,
                      IS_MOBILE_,
                      DELETED_,
                      COMPANY_ID_,
                      TENANT_ID_,
                      CREATE_DEP_ID_,
                      CREATE_BY_,
                      CREATE_TIME_,
                      UPDATE_BY_,
                      UPDATE_TIME_,
                      APP_ID_) VALUES('1259762922225254402',
                                      '提醒',
                                      'remind',
                                      '0',
                                      '',
                                      '',
                                      '0',
                                      '1230771994887495681',
                                      '',
                                      '0',
                                      0,
                                      '0',
                                      '0',
                                      '1',
                                      '1',
                                      NULL,
                                      '1',
                                      TO_DATE('2021-5-17 15:53:09',
                                              'yyyy-MM-dd HH24:mi:ss'),
                                      '');

DECLARE
  content_4 CLOB := '<div class="gridLayoutClass">    <div class="headPClass">        <span style="display: inline-block;font-size: 16px;">我的提醒</span>    </div>    <div class="bodyDivClass">        <ul class="remind_ul">     <li v-for="obj in data">          <span><a-icon :type="obj.icon" /></span>                <div class="remind_a" @click="moreUrl(obj.url)" v-html="obj.description"></div>      </li>        </ul>    </div></div>';
  content_5 CLOB := '{"dataUrl":"","function":"portalScript.getRemindList()","newType":"","tabgroups":"","listCount":6,"newDIc":"","funcType":"","sqlName":""}';
BEGIN
  UPDATE ins_column_def
     SET TEMPLET_ = content_4
   WHERE COL_ID_ = '1259762922225254402';
  UPDATE ins_column_def
     SET SET_TING_ = content_5
   WHERE COL_ID_ = '1259762922225254402';
  COMMIT;
END;
/

INSERT
  INTO ins_column_def(COL_ID_,
                      NAME_,
                      KEY_,
                      IS_DEFAULT_,
                      TEMPLET_,
                      SET_TING_,
                      IS_PUBLIC_,
                      TYPE_,
                      ICON_,
                      IS_MOBILE_,
                      DELETED_,
                      COMPANY_ID_,
                      TENANT_ID_,
                      CREATE_DEP_ID_,
                      CREATE_BY_,
                      CREATE_TIME_,
                      UPDATE_BY_,
                      UPDATE_TIME_,
                      APP_ID_) VALUES('1361616576253440002',
                                      '已办事项',
                                      'hadHandleInsts',
                                      '0',
                                      '',
                                      '',
                                      '0',
                                      '1230771994887495681',
                                      '',
                                      '0',
                                      0,
                                      '0',
                                      '0',
                                      '',
                                      '1',
                                      NULL,
                                      '1',
                                      TO_DATE('2021-5-18 15:12:35',
                                              'yyyy-MM-dd HH24:mi:ss'),
                                      '');

DECLARE
  content_4 CLOB := '<div class="gridLayoutClass">    <div class="headPClass">        <span style="display: inline-block;font-size: 16px;">{{insColumnDef.name}}</span>        <div style="float:right;">           <div class="journalism_span"> <span @click="moreUrl()">更多</span> <span @click="refresh">刷新</span> </div>        </div>    </div>    <div class="bodyDivClass">        <ul>            <li class="itmelist" v-for="(obj,index) of data" :key="obj.instId" v-if="index <=5">                <p @click="handleBpmInst(obj)">{{obj.subject}}</p>                <span>{{obj.createTime}}</span>            </li>        </ul>    </div></div>';
  content_5 CLOB := '{"dataUrl":"/webApp/home/MyApprovedInsts","function":"portalScript.getMyApprovedInsts()","newType":"","tabgroups":"","listCount":6,"newDIc":"","funcType":"","sqlName":"","service":"","calendarMode":""}';
BEGIN
  UPDATE ins_column_def
     SET TEMPLET_ = content_4
   WHERE COL_ID_ = '1361616576253440002';
  UPDATE ins_column_def
     SET SET_TING_ = content_5
   WHERE COL_ID_ = '1361616576253440002';
  COMMIT;
END;
/

INSERT
  INTO ins_column_def(COL_ID_,
                      NAME_,
                      KEY_,
                      IS_DEFAULT_,
                      TEMPLET_,
                      SET_TING_,
                      IS_PUBLIC_,
                      TYPE_,
                      ICON_,
                      IS_MOBILE_,
                      DELETED_,
                      COMPANY_ID_,
                      TENANT_ID_,
                      CREATE_DEP_ID_,
                      CREATE_BY_,
                      CREATE_TIME_,
                      UPDATE_BY_,
                      UPDATE_TIME_,
                      APP_ID_) VALUES('1363419382463524865',
                                      '新闻公告Tab',
                                      'NewsNoticeTab',
                                      '0',
                                      '',
                                      '',
                                      '0',
                                      '1230774801220804610',
                                      '',
                                      '0',
                                      0,
                                      '0',
                                      '0',
                                      '',
                                      '1',
                                      NULL,
                                      '1',
                                      TO_DATE('2021-6-29 14:46:46',
                                              'yyyy-MM-dd HH24:mi:ss'),
                                      '');

DECLARE
  content_4 CLOB := '<div class="page-header-index-wide">      <div class="salesCard">        <a-tabs default-active-key=0 size="large"  @change="changeKey" style="height:100%;" >          <div class="extra-wrapper" slot="tabBarExtraContent">              <div class="journalism_span">             <span @click="moreUrl()">更多</span>             <span @click="refresh">刷新</span>           </div>          </div>          <a-tab-pane class="hetlist" v-for="(obj,index) of data"  :tab="obj.name" :key="index">             <portal-layoutview ref="innerLayout" :insColumnDef="obj.insColumnDef" > </portal-layoutview>          </a-tab-pane>        </a-tabs>      </div>  </div>';
  content_5 CLOB := '{"dataUrl":"","function":"","newType":"","tabgroups":{"value":"1239837443886018562,1239839327900266497","text":"集团新闻,公司公告"},"listCount":6,"newDIc":"","funcType":"","sqlName":"","service":"","calendarMode":""}';
BEGIN
  UPDATE ins_column_def
     SET TEMPLET_ = content_4
   WHERE COL_ID_ = '1363419382463524865';
  UPDATE ins_column_def
     SET SET_TING_ = content_5
   WHERE COL_ID_ = '1363419382463524865';
  COMMIT;
END;
/

INSERT
  INTO ins_column_def(COL_ID_,
                      NAME_,
                      KEY_,
                      IS_DEFAULT_,
                      TEMPLET_,
                      SET_TING_,
                      IS_PUBLIC_,
                      TYPE_,
                      ICON_,
                      IS_MOBILE_,
                      DELETED_,
                      COMPANY_ID_,
                      TENANT_ID_,
                      CREATE_DEP_ID_,
                      CREATE_BY_,
                      CREATE_TIME_,
                      UPDATE_BY_,
                      UPDATE_TIME_,
                      APP_ID_) VALUES('1392727548903243778',
                                      '饼图',
                                      'piecharts',
                                      '0',
                                      '',
                                      '',
                                      '0',
                                      '1230771994887495674',
                                      '',
                                      '0',
                                      0,
                                      '0',
                                      '1',
                                      '1',
                                      '1',
                                      TO_DATE('2021-5-13 14:24:58',
                                              'yyyy-MM-dd HH24:mi:ss'),
                                      '',
                                      TO_DATE('2021-5-13 14:24:58',
                                              'yyyy-MM-dd HH24:mi:ss'),
                                      '');

DECLARE
  content_4 CLOB := '';
  content_5 CLOB := '{"dataUrl":"","function":"","newType":"","tabgroups":"","listCount":6,"newDIc":"","funcType":"","sqlName":"","service":"","calendarMode":"","component":"modules/portal/core/charts/PieCharts.vue"}';
BEGIN
  UPDATE ins_column_def
     SET TEMPLET_ = content_4
   WHERE COL_ID_ = '1392727548903243778';
  UPDATE ins_column_def
     SET SET_TING_ = content_5
   WHERE COL_ID_ = '1392727548903243778';
  COMMIT;
END;
/

INSERT
  INTO ins_column_def(COL_ID_,
                      NAME_,
                      KEY_,
                      IS_DEFAULT_,
                      TEMPLET_,
                      SET_TING_,
                      IS_PUBLIC_,
                      TYPE_,
                      ICON_,
                      IS_MOBILE_,
                      DELETED_,
                      COMPANY_ID_,
                      TENANT_ID_,
                      CREATE_DEP_ID_,
                      CREATE_BY_,
                      CREATE_TIME_,
                      UPDATE_BY_,
                      UPDATE_TIME_,
                      APP_ID_) VALUES('1392738215320346625',
                                      '柱状图',
                                      'barCharts',
                                      '0',
                                      '',
                                      '',
                                      '0',
                                      '1230771994887495674',
                                      '',
                                      '0',
                                      0,
                                      '0',
                                      '1',
                                      '1',
                                      '1',
                                      TO_DATE('2021-5-13 15:07:21',
                                              'yyyy-MM-dd HH24:mi:ss'),
                                      '',
                                      TO_DATE('2021-5-13 15:07:21',
                                              'yyyy-MM-dd HH24:mi:ss'),
                                      '');

DECLARE
  content_4 CLOB := '';
  content_5 CLOB := '{"dataUrl":"","function":"","newType":"","tabgroups":"","listCount":6,"newDIc":"","funcType":"","sqlName":"","service":"","calendarMode":"","component":"modules/portal/core/charts/BarCharts.vue"}';
BEGIN
  UPDATE ins_column_def
     SET TEMPLET_ = content_4
   WHERE COL_ID_ = '1392738215320346625';
  UPDATE ins_column_def
     SET SET_TING_ = content_5
   WHERE COL_ID_ = '1392738215320346625';
  COMMIT;
END;
/

INSERT
  INTO ins_column_def(COL_ID_,
                      NAME_,
                      KEY_,
                      IS_DEFAULT_,
                      TEMPLET_,
                      SET_TING_,
                      IS_PUBLIC_,
                      TYPE_,
                      ICON_,
                      IS_MOBILE_,
                      DELETED_,
                      COMPANY_ID_,
                      TENANT_ID_,
                      CREATE_DEP_ID_,
                      CREATE_BY_,
                      CREATE_TIME_,
                      UPDATE_BY_,
                      UPDATE_TIME_,
                      APP_ID_) VALUES('1392739711713796097',
                                      '漏斗图',
                                      'FunnelCharts',
                                      '0',
                                      '',
                                      '',
                                      '0',
                                      '1230771994887495674',
                                      '',
                                      '0',
                                      0,
                                      '0',
                                      '1',
                                      '1',
                                      '1',
                                      TO_DATE('2021-5-13 15:13:18',
                                              'yyyy-MM-dd HH24:mi:ss'),
                                      '',
                                      TO_DATE('2021-5-13 15:13:18',
                                              'yyyy-MM-dd HH24:mi:ss'),
                                      '');

DECLARE
  content_4 CLOB := '';
  content_5 CLOB := '{"dataUrl":"","function":"","newType":"","tabgroups":"","listCount":6,"newDIc":"","funcType":"","sqlName":"","service":"","calendarMode":"","component":"modules/portal/core/charts/FunnelCharts.vue"}';
BEGIN
  UPDATE ins_column_def
     SET TEMPLET_ = content_4
   WHERE COL_ID_ = '1392739711713796097';
  UPDATE ins_column_def
     SET SET_TING_ = content_5
   WHERE COL_ID_ = '1392739711713796097';
  COMMIT;
END;
/

INSERT
  INTO ins_column_def(COL_ID_,
                      NAME_,
                      KEY_,
                      IS_DEFAULT_,
                      TEMPLET_,
                      SET_TING_,
                      IS_PUBLIC_,
                      TYPE_,
                      ICON_,
                      IS_MOBILE_,
                      DELETED_,
                      COMPANY_ID_,
                      TENANT_ID_,
                      CREATE_DEP_ID_,
                      CREATE_BY_,
                      CREATE_TIME_,
                      UPDATE_BY_,
                      UPDATE_TIME_,
                      APP_ID_) VALUES('1392741135541592065',
                                      '仪表盘',
                                      'gaugeCharts',
                                      '0',
                                      '',
                                      '',
                                      '0',
                                      '1230771994887495674',
                                      '',
                                      '0',
                                      0,
                                      '0',
                                      '1',
                                      '1',
                                      '1',
                                      TO_DATE('2021-5-13 15:18:57',
                                              'yyyy-MM-dd HH24:mi:ss'),
                                      '',
                                      TO_DATE('2021-5-13 15:18:57',
                                              'yyyy-MM-dd HH24:mi:ss'),
                                      '');

DECLARE
  content_4 CLOB := '';
  content_5 CLOB := '{"dataUrl":"","function":"","newType":"","tabgroups":"","listCount":6,"newDIc":"","funcType":"","sqlName":"","service":"","calendarMode":"","component":"modules/portal/core/charts/GaugeCharts.vue"}';
BEGIN
  UPDATE ins_column_def
     SET TEMPLET_ = content_4
   WHERE COL_ID_ = '1392741135541592065';
  UPDATE ins_column_def
     SET SET_TING_ = content_5
   WHERE COL_ID_ = '1392741135541592065';
  COMMIT;
END;
/

INSERT
  INTO ins_column_def(COL_ID_,
                      NAME_,
                      KEY_,
                      IS_DEFAULT_,
                      TEMPLET_,
                      SET_TING_,
                      IS_PUBLIC_,
                      TYPE_,
                      ICON_,
                      IS_MOBILE_,
                      DELETED_,
                      COMPANY_ID_,
                      TENANT_ID_,
                      CREATE_DEP_ID_,
                      CREATE_BY_,
                      CREATE_TIME_,
                      UPDATE_BY_,
                      UPDATE_TIME_,
                      APP_ID_) VALUES('1392742738608775169',
                                      '曲线图',
                                      'linecharts',
                                      '0',
                                      '',
                                      '',
                                      '0',
                                      '1230771994887495674',
                                      '',
                                      '0',
                                      0,
                                      '0',
                                      '1',
                                      '1',
                                      '1',
                                      TO_DATE('2021-5-13 15:25:19',
                                              'yyyy-MM-dd HH24:mi:ss'),
                                      '',
                                      TO_DATE('2021-5-13 15:25:19',
                                              'yyyy-MM-dd HH24:mi:ss'),
                                      '');

DECLARE
  content_4 CLOB := '';
  content_5 CLOB := '{"dataUrl":"","function":"","newType":"","tabgroups":"","listCount":6,"newDIc":"","funcType":"","sqlName":"","service":"","calendarMode":"","component":"modules/portal/core/charts/LineCharts.vue"}';
BEGIN
  UPDATE ins_column_def
     SET TEMPLET_ = content_4
   WHERE COL_ID_ = '1392742738608775169';
  UPDATE ins_column_def
     SET SET_TING_ = content_5
   WHERE COL_ID_ = '1392742738608775169';
  COMMIT;
END;
/

INSERT
  INTO ins_column_def(COL_ID_,
                      NAME_,
                      KEY_,
                      IS_DEFAULT_,
                      TEMPLET_,
                      SET_TING_,
                      IS_PUBLIC_,
                      TYPE_,
                      ICON_,
                      IS_MOBILE_,
                      DELETED_,
                      COMPANY_ID_,
                      TENANT_ID_,
                      CREATE_DEP_ID_,
                      CREATE_BY_,
                      CREATE_TIME_,
                      UPDATE_BY_,
                      UPDATE_TIME_,
                      APP_ID_) VALUES('1392746879276015617',
                                      '混合图形',
                                      'mixCharts',
                                      '0',
                                      '',
                                      '',
                                      '0',
                                      '1230771994887495674',
                                      '',
                                      '0',
                                      0,
                                      '0',
                                      '1',
                                      '1',
                                      '1',
                                      TO_DATE('2021-5-13 15:41:47',
                                              'yyyy-MM-dd HH24:mi:ss'),
                                      '1',
                                      TO_DATE('2022-7-6 16:36:16',
                                              'yyyy-MM-dd HH24:mi:ss'),
                                      '');

DECLARE
  content_4 CLOB := '';
  content_5 CLOB := '{"dataUrl":"","function":"","newType":"","tabgroups":"","listCount":6,"newDIc":"","funcType":"","sqlName":"","service":"","calendarMode":"","component":"modules/portal/core/charts/MixCharts.vue","vueType":"vue"}';
BEGIN
  UPDATE ins_column_def
     SET TEMPLET_ = content_4
   WHERE COL_ID_ = '1392746879276015617';
  UPDATE ins_column_def
     SET SET_TING_ = content_5
   WHERE COL_ID_ = '1392746879276015617';
  COMMIT;
END;
/

INSERT
  INTO ins_column_temp(ID_,
                       NAME_,
                       KEY_,
                       TEMPLET_,
                       IS_SYS_,
                       STATUS_,
                       TEMP_TYPE_,
                       DELETED_,
                       COMPANY_ID_,
                       TENANT_ID_,
                       CREATE_DEP_ID_,
                       CREATE_BY_,
                       CREATE_TIME_,
                       UPDATE_BY_,
                       UPDATE_TIME_) VALUES('1230771994887495674',
                                            'VUE组件',
                                            'vue',
                                            '',
                                            '1',
                                            '1',
                                            'VueBel',
                                            0,
                                            '0',
                                            '0',
                                            '2',
                                            '1',
                                            TO_DATE('2020-2-21 8:31:25',
                                                    'yyyy-MM-dd HH24:mi:ss'),
                                            '1',
                                            TO_DATE('2021-5-11 12:41:38',
                                                    'yyyy-MM-dd HH24:mi:ss'));

DECLARE
  content_3 CLOB := '';
BEGIN
  UPDATE ins_column_temp
     SET TEMPLET_ = content_3
   WHERE ID_ = '1230771994887495674';
  COMMIT;
END;
/

INSERT
  INTO ins_column_temp(ID_,
                       NAME_,
                       KEY_,
                       TEMPLET_,
                       IS_SYS_,
                       STATUS_,
                       TEMP_TYPE_,
                       DELETED_,
                       COMPANY_ID_,
                       TENANT_ID_,
                       CREATE_DEP_ID_,
                       CREATE_BY_,
                       CREATE_TIME_,
                       UPDATE_BY_,
                       UPDATE_TIME_) VALUES('1230771994887495681',
                                            '列表栏目',
                                            'list',
                                            '',
                                            '1',
                                            '1',
                                            'List',
                                            0,
                                            '0',
                                            '0',
                                            '2',
                                            '1',
                                            TO_DATE('2020-2-21 8:31:25',
                                                    'yyyy-MM-dd HH24:mi:ss'),
                                            '1',
                                            TO_DATE('2022-7-6 18:11:16',
                                                    'yyyy-MM-dd HH24:mi:ss'));

DECLARE
  content_3 CLOB := '<div class="gridLayoutClass">    <div class="headPClass">        <div style="display:inline-block;" >{{insColumnDef.name}}</div>        <div style="float: right">          <div style="float: right">            <div class="journalism_span"> <span @click="moreUrl()">更多</span> <span @click="refresh">刷新</span> </div>        </div>        </div>    </div>    <div class="bodyDivClass">        <ul>          <li class="itmelist" v-for="(item,index) of data" :key="item.pkId" >             <p @click="handleBpmInst(item)">{{item.SUBJECT_}</p>                <span>{{getDate(item.CREATE_TIME_)}}</span>            </li>        </ul>    </div></div>';
BEGIN
  UPDATE ins_column_temp
     SET TEMPLET_ = content_3
   WHERE ID_ = '1230771994887495681';
  COMMIT;
END;
/

INSERT
  INTO ins_column_temp(ID_,
                       NAME_,
                       KEY_,
                       TEMPLET_,
                       IS_SYS_,
                       STATUS_,
                       TEMP_TYPE_,
                       DELETED_,
                       COMPANY_ID_,
                       TENANT_ID_,
                       CREATE_DEP_ID_,
                       CREATE_BY_,
                       CREATE_TIME_,
                       UPDATE_BY_,
                       UPDATE_TIME_) VALUES('1230774411821621249',
                                            '消息盒子',
                                            'msgbox',
                                            '',
                                            '1',
                                            '1',
                                            'MessageBel',
                                            0,
                                            '0',
                                            '0',
                                            '2',
                                            '1',
                                            TO_DATE('2020-2-21 8:41:01',
                                                    'yyyy-MM-dd HH24:mi:ss'),
                                            '1',
                                            TO_DATE('2021-9-12 19:52:06',
                                                    'yyyy-MM-dd HH24:mi:ss'));

DECLARE
  content_3 CLOB := '<div class="msgBoxs">    <ul class="msgUl">        <li class="msgItme"            :style="{width:listWidthFn}"            v-for="(obj,index) of data"          :key="index">        <div @click="moreUrl(obj.url)" class="contentBox" >            <div class="msgContent">            <b>{{obj.count}}</b>            <p>{{obj.content}}</p >          </div>          <div class="header_icon" :style="{''color'''': obj.color != ''''?obj.color :''red''}">                    <a-icon v-if="!obj.icon.includes(''type'')" :type="obj.icon" />                    <my-icon v-else-if="[''customIcon'',''userCustomIcon''].includes(JSON.parse(obj.icon).type)" :type="JSON.parse(obj.icon).icon"></my-icon>                    <a-icon v-else="JSON.parse(obj.icon).type" :type="JSON.parse(obj.icon).icon"></a-icon>          </div>        </div>    </li>    </ul></div>';
BEGIN
  UPDATE ins_column_temp
     SET TEMPLET_ = content_3
   WHERE ID_ = '1230774411821621249';
  COMMIT;
END;
/

INSERT
  INTO ins_column_temp(ID_,
                       NAME_,
                       KEY_,
                       TEMPLET_,
                       IS_SYS_,
                       STATUS_,
                       TEMP_TYPE_,
                       DELETED_,
                       COMPANY_ID_,
                       TENANT_ID_,
                       CREATE_DEP_ID_,
                       CREATE_BY_,
                       CREATE_TIME_,
                       UPDATE_BY_,
                       UPDATE_TIME_) VALUES('1230774801220804610',
                                            'Tab栏目',
                                            'tabcolumn',
                                            '',
                                            '1',
                                            '1',
                                            'TabBel',
                                            0,
                                            '0',
                                            '0',
                                            '2',
                                            '1',
                                            TO_DATE('2020-2-21 8:42:34',
                                                    'yyyy-MM-dd HH24:mi:ss'),
                                            '1',
                                            TO_DATE('2022-7-6 18:11:00',
                                                    'yyyy-MM-dd HH24:mi:ss'));

DECLARE
  content_3 CLOB := '<div class="page-header-index-wide">         <div class="salesCard" headPClass>        <a-tabs default-active-key=0 size="large"  @change="changeKey" :tab-bar-style="{marginBottom: ''24px'', paddingLeft: ''16px''}">          <div class="extra-wrapper" slot="tabBarExtraContent">            <div class="extra-item">              <span class="btnSpan" @click="moreUrl()">                <a-icon type="dash"></a-icon>              </span>              <span class="btnSpan" @click="refresh">                <a-icon type="redo"></a-icon>              </span>            </div>          </div>          <a-tab-pane class="hetlist" v-for="(obj,index) of data"  :tab="obj.name" :key="index">            <portal-layoutview ref="innerLayout"  :insColumnDef="obj.insColumnDef"></portal-layoutview>          </a-tab-pane>        </a-tabs>      </div>     </div>';
BEGIN
  UPDATE ins_column_temp
     SET TEMPLET_ = content_3
   WHERE ID_ = '1230774801220804610';
  COMMIT;
END;
/

INSERT
  INTO ins_column_temp(ID_,
                       NAME_,
                       KEY_,
                       TEMPLET_,
                       IS_SYS_,
                       STATUS_,
                       TEMP_TYPE_,
                       DELETED_,
                       COMPANY_ID_,
                       TENANT_ID_,
                       CREATE_DEP_ID_,
                       CREATE_BY_,
                       CREATE_TIME_,
                       UPDATE_BY_,
                       UPDATE_TIME_) VALUES('1230775094247464962',
                                            '新闻公告',
                                            'newsNotice',
                                            '',
                                            '1',
                                            '1',
                                            'NewsBel',
                                            0,
                                            '0',
                                            '0',
                                            '2',
                                            '1',
                                            TO_DATE('2020-2-21 8:43:44',
                                                    'yyyy-MM-dd HH24:mi:ss'),
                                            '1',
                                            TO_DATE('2021-7-28 16:34:33',
                                                    'yyyy-MM-dd HH24:mi:ss'));

DECLARE
  content_3 CLOB := '<div class="gridLayoutClass">    <div class="headPClass">        <div style="display:inline-block;" >{{insColumnDef.name}}</div>        <div style="float: right">            <div class="journalism_span"> <span @click="moreUrl()">更多</span> <span @click="refresh">刷新</span> </div>        </div>    </div>    <div class="bodyDivClass">        <div class="aclsty" v-if="newType ==''img'' || newType ==''imgAndFont''">            <a-carousel arrows autoplay>                <div slot="prevArrow" slot-scope="props" class="custom-slick-arrow" style="left: 10px;zIndex: 1">                    <a-icon type="left-circle" />                </div>                <div slot="nextArrow" slot-scope="props" class="custom-slick-arrow" style="right: 10px">                    <a-icon type="right-circle" />                </div>                <div v-for="lising of data" class="inglist"><img :src="getImgPath(lising.imgFileId)"></img></div>            </a-carousel>        </div>        <div v-if="newType !=''img''" :class="[newType !=''wordsList''? ''listing'':'''' ]">            <ul>                <li class="itmelist" v-for="(obj,index) of data" :key="obj.pkId" v-if="index <=5">                    <p @click="getInsNews(obj.pkId)">{{obj.subject}}</p> <span>{{obj.createTime}}</span>                </li>            </ul>        </div>    </div></div>';
BEGIN
  UPDATE ins_column_temp
     SET TEMPLET_ = content_3
   WHERE ID_ = '1230775094247464962';
  COMMIT;
END;
/

INSERT
  INTO ins_column_temp(ID_,
                       NAME_,
                       KEY_,
                       TEMPLET_,
                       IS_SYS_,
                       STATUS_,
                       TEMP_TYPE_,
                       DELETED_,
                       COMPANY_ID_,
                       TENANT_ID_,
                       CREATE_DEP_ID_,
                       CREATE_BY_,
                       CREATE_TIME_,
                       UPDATE_BY_,
                       UPDATE_TIME_) VALUES('1262328598619750402',
                                            '日程栏目',
                                            'calendar',
                                            '',
                                            '1',
                                            '1',
                                            'Calendar',
                                            0,
                                            '0',
                                            '0',
                                            '1',
                                            '1',
                                            TO_DATE('2020-5-18 10:26:06',
                                                    'yyyy-MM-dd HH24:mi:ss'),
                                            '1',
                                            TO_DATE('2022-7-6 18:11:32',
                                                    'yyyy-MM-dd HH24:mi:ss'));

DECLARE
  content_3 CLOB := '<div class="gridLayoutClass">    <div class="headPClass">            <span style="display: inline-block;font-size: 16px;">我的日程</span>    </div>    <div class="bodyDivClass schstyle" style="overflow-y: auto;overflow-x: hidden;">       <div :style="{ width: ''100%'', borderRadius: ''4px'' }">            <a-calendar :fullscreen="false" :mode="calendarMode" @select="onCalendarSelect" @panelChange="onPanelChange" >                <ul slot="dateCellRender" slot-scope="value" class="events">                    <li class="relisdian" v-if="isxunran" v-for="item in getCalendarDayListData(value)" :key="item.content">                        <a-badge :status="item.type" :text="item.content" />                    </li>                </ul>            </a-calendar>    </div>    <div class="relist">{{calendarValue}}日程</div>    <a-steps  progress-dot :current="1" direction="vertical" :class="[ calendarMode ==''year''? ''calenlist'':'''']">        <a-step v-for="item in data" :key="item.index" :title="item.title">            <span v-if="calendarMode ===''year''" class="shijian_year" slot="description">{{item.day}} {{item.startTime}}</span>            <span v-if="calendarMode ===''month''" class="shijian" slot="description">{{item.startTime}}</span>            <template slot="title">{{item.title}} </template>            <span slot="description">{{item.describe}}</span>        </a-step>    </a-steps>    <a-empty v-if="data.length ==0" />    </div></div>';
BEGIN
  UPDATE ins_column_temp
     SET TEMPLET_ = content_3
   WHERE ID_ = '1262328598619750402';
  COMMIT;
END;
/

INSERT
  INTO ins_column_temp(ID_,
                       NAME_,
                       KEY_,
                       TEMPLET_,
                       IS_SYS_,
                       STATUS_,
                       TEMP_TYPE_,
                       DELETED_,
                       COMPANY_ID_,
                       TENANT_ID_,
                       CREATE_DEP_ID_,
                       CREATE_BY_,
                       CREATE_TIME_,
                       UPDATE_BY_,
                       UPDATE_TIME_) VALUES('1393012721066573826',
                                            '提醒模板',
                                            'remind',
                                            '',
                                            '1',
                                            '1',
                                            'List',
                                            0,
                                            '0',
                                            '1',
                                            '1',
                                            '1',
                                            TO_DATE('2021-5-14 9:18:08',
                                                    'yyyy-MM-dd HH24:mi:ss'),
                                            '1',
                                            TO_DATE('2021-5-14 11:02:25',
                                                    'yyyy-MM-dd HH24:mi:ss'));

DECLARE
  content_3 CLOB := '<div class="gridLayoutClass">
    <div class="headPClass">
        <span style="display: inline-block;font-size: 16px;">我的提醒</span>
    </div>
    <div class="bodyDivClass">
        <ul class="remind_ul">
      <li v-for="obj in data">
          <span><a-icon :type="obj.icon" /></span>
                <div class="remind_a" @click="moreUrl(obj.url)" v-html="obj.description"></div>
       </li>
        </ul>
    </div>
</div>';
BEGIN
  UPDATE ins_column_temp
     SET TEMPLET_ = content_3
   WHERE ID_ = '1393012721066573826';
  COMMIT;
END;
/

INSERT
  INTO ins_msg_def(MSG_ID_,
                   COLOR_,
                   URL_,
                   ICON_,
                   CONTENT_,
                   DS_NAME_,
                   DS_ALIAS_,
                   SQL_FUNC_,
                   TYPE_,
                   COUNT_TYPE_,
                   DELETED_,
                   COMPANY_ID_,
                   TENANT_ID_,
                   CREATE_DEP_ID_,
                   CREATE_BY_,
                   CREATE_TIME_,
                   UPDATE_BY_,
                   UPDATE_TIME_,
                   APP_ID_) VALUES('1259773507849256961',
                                   '#ff5757',
                                   '/webApp/home/MyBpmInstList',
                                   '{"type":"brand_logo","icon":"inbox"}',
                                   '我的流程草稿',
                                   '',
                                   'jpaas_bpm',
                                   'portalScript.getMyAllDraftBpmInst()',
                                   'function',
                                   '',
                                   0,
                                   '0',
                                   '0',
                                   '1',
                                   '1',
                                   TO_DATE('2020-5-11 9:13:04',
                                           'yyyy-MM-dd HH24:mi:ss'),
                                   '1',
                                   TO_DATE('2021-5-13 17:11:29',
                                           'yyyy-MM-dd HH24:mi:ss'),
                                   '');

INSERT INTO ins_msg_def
  (MSG_ID_,
   COLOR_,
   URL_,
   ICON_,
   CONTENT_,
   DS_NAME_,
   DS_ALIAS_,
   SQL_FUNC_,
   TYPE_,
   COUNT_TYPE_,
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
  ('1260089706780913666',
   '#2ba0fc',
   '/webApp/home/MyApprovedTasks',
   '{"type":"brand_logo","icon":"file-done"}',
   '我的已办',
   '',
   'jpaas_bpm',
   'portalScript.getMyApprovedTaskCount()',
   'function',
   '',
   0,
   '0',
   '0',
   '1',
   '1',
   TO_DATE('2020-5-12 6:09:32', 'yyyy-MM-dd HH24:mi:ss'),
   '1',
   TO_DATE('2021-5-13 17:11:31', 'yyyy-MM-dd HH24:mi:ss'),
   '');

INSERT INTO ins_msg_def
  (MSG_ID_,
   COLOR_,
   URL_,
   ICON_,
   CONTENT_,
   DS_NAME_,
   DS_ALIAS_,
   SQL_FUNC_,
   TYPE_,
   COUNT_TYPE_,
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
  ('1260090213217955841',
   '#feaf00',
   '/webApp/home/MyBpmTaskList',
   '{"type":"brand_logo","icon":"audit"}',
   '我的待办',
   '',
   '',
   'portalScript.getMyTaskCount()',
   'function',
   '',
   0,
   '0',
   '0',
   '1',
   '1',
   TO_DATE('2020-5-12 6:11:33', 'yyyy-MM-dd HH24:mi:ss'),
   '1',
   TO_DATE('2021-5-13 17:11:34', 'yyyy-MM-dd HH24:mi:ss'),
   '');

INSERT INTO ins_msg_def
  (MSG_ID_,
   COLOR_,
   URL_,
   ICON_,
   CONTENT_,
   DS_NAME_,
   DS_ALIAS_,
   SQL_FUNC_,
   TYPE_,
   COUNT_TYPE_,
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
  ('1260090447218176002',
   '#1bd895',
   '/webApp/home/innerMsgs/InfInnerMsgList',
   '{"type":"brand_logo","icon":"message"}',
   '我的消息',
   '',
   '',
   'portalScript.getMyInnerMsgCount()    ',
   'function',
   '',
   0,
   '0',
   '0',
   '1',
   '1',
   TO_DATE('2020-5-12 6:12:29', 'yyyy-MM-dd HH24:mi:ss'),
   '1',
   TO_DATE('2021-5-13 17:11:41', 'yyyy-MM-dd HH24:mi:ss'),
   '');

INSERT INTO ins_msgbox_box_def
  (ID_,
   SN_,
   MSG_ID_,
   BOX_ID_,
   APP_ID_,
   DELETED_,
   COMPANY_ID_,
   TENANT_ID_,
   CREATE_DEP_ID_,
   CREATE_BY_,
   CREATE_TIME_,
   UPDATE_BY_,
   UPDATE_TIME_)
VALUES
  ('1409778506415890434',
   0,
   '1260089706780913666',
   '1235758510261506049',
   '',
   0,
   '0',
   '1',
   '',
   '1',
   TO_DATE('2021-6-29 15:39:23', 'yyyy-MM-dd HH24:mi:ss'),
   '',
   TO_DATE('2021-6-29 15:39:23', 'yyyy-MM-dd HH24:mi:ss'));

INSERT INTO ins_msgbox_box_def
  (ID_,
   SN_,
   MSG_ID_,
   BOX_ID_,
   APP_ID_,
   DELETED_,
   COMPANY_ID_,
   TENANT_ID_,
   CREATE_DEP_ID_,
   CREATE_BY_,
   CREATE_TIME_,
   UPDATE_BY_,
   UPDATE_TIME_)
VALUES
  ('1409778506696908801',
   1,
   '1260090447218176002',
   '1235758510261506049',
   '',
   0,
   '0',
   '1',
   '',
   '1',
   TO_DATE('2021-6-29 15:39:23', 'yyyy-MM-dd HH24:mi:ss'),
   '',
   TO_DATE('2021-6-29 15:39:23', 'yyyy-MM-dd HH24:mi:ss'));

INSERT INTO ins_msgbox_box_def
  (ID_,
   SN_,
   MSG_ID_,
   BOX_ID_,
   APP_ID_,
   DELETED_,
   COMPANY_ID_,
   TENANT_ID_,
   CREATE_DEP_ID_,
   CREATE_BY_,
   CREATE_TIME_,
   UPDATE_BY_,
   UPDATE_TIME_)
VALUES
  ('1409778506709491713',
   2,
   '1260090213217955841',
   '1235758510261506049',
   '',
   0,
   '0',
   '1',
   '',
   '1',
   TO_DATE('2021-6-29 15:39:23', 'yyyy-MM-dd HH24:mi:ss'),
   '',
   TO_DATE('2021-6-29 15:39:23', 'yyyy-MM-dd HH24:mi:ss'));

INSERT INTO ins_msgbox_box_def
  (ID_,
   SN_,
   MSG_ID_,
   BOX_ID_,
   APP_ID_,
   DELETED_,
   COMPANY_ID_,
   TENANT_ID_,
   CREATE_DEP_ID_,
   CREATE_BY_,
   CREATE_TIME_,
   UPDATE_BY_,
   UPDATE_TIME_)
VALUES
  ('1409778506722074625',
   3,
   '1259773507849256961',
   '1235758510261506049',
   '',
   0,
   '0',
   '1',
   '',
   '1',
   TO_DATE('2021-6-29 15:39:23', 'yyyy-MM-dd HH24:mi:ss'),
   '',
   TO_DATE('2021-6-29 15:39:23', 'yyyy-MM-dd HH24:mi:ss'));

INSERT INTO ins_msgbox_def
  (BOX_ID_,
   KEY_,
   NAME_,
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
  ('1235758510261506049',
   'msgbox',
   '消息盒',
   0,
   '0',
   '0',
   '2',
   '1',
   TO_DATE('2020-3-6 2:46:03', 'yyyy-MM-dd HH24:mi:ss'),
   '1',
   TO_DATE('2021-6-29 15:39:23', 'yyyy-MM-dd HH24:mi:ss'),
   '');

INSERT INTO ins_portal_def
  (PORT_ID_,
   NAME_,
   KEY_,
   IS_DEFAULT_,
   LAYOUT_HTML_,
   PRIORITY_,
   IS_MOBILE_,
   LAYOUT_JSON_,
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
  ('1232486079769382914',
   '全局门户',
   'GLOBAL-PERSONAL',
   '1',
   '',
   2,
   'NO',
   '',
   0,
   '0',
   '0',
   '2',
   '1',
   TO_DATE('2020-2-26 2:02:35', 'yyyy-MM-dd HH24:mi:ss'),
   '1',
   TO_DATE('2022-7-6 11:41:58', 'yyyy-MM-dd HH24:mi:ss'),
   '');

DECLARE
  content_4 CLOB := '';
  content_7 CLOB := '[{"x":0,"y":0,"w":24,"h":12,"i":1621236255245,"colId":"1239838889297702914","layoutName":"消息盒子","moved":false,"config":{"colId":"1239838889297702914","name":"消息盒子"},"defConf":"column","id":"byafwbl140672"},{"x":9,"y":12,"w":15,"h":33,"i":1622021399023,"colId":"1239839763151581185","layoutName":"待办已办","moved":false,"config":{"colId":"1239839763151581185","name":"待办已办"},"defConf":"column","id":"fzykbly140672"},{"x":0,"y":45,"w":24,"h":15,"i":1624247108516,"colId":"1259742027670732801","layoutName":"常用应用","moved":false,"config":{"colId":"1259742027670732801","name":"常用应用"},"defConf":"column","id":"etngszh140672"},{"x":0,"y":12,"w":9,"h":33,"i":1625017634304,"colId":"1363419382463524865","layoutName":"新闻公告Tab","moved":false,"config":{"colId":"1363419382463524865","name":"新闻公告Tab"},"defConf":"column","id":"bekexdo140672"}]';
BEGIN
  UPDATE ins_portal_def
     SET LAYOUT_HTML_ = content_4
   WHERE PORT_ID_ = '1232486079769382914';
  UPDATE ins_portal_def
     SET LAYOUT_JSON_ = content_7
   WHERE PORT_ID_ = '1232486079769382914';
  COMMIT;
END;
/

INSERT
  INTO ins_portal_def(PORT_ID_,
                      NAME_,
                      KEY_,
                      IS_DEFAULT_,
                      LAYOUT_HTML_,
                      PRIORITY_,
                      IS_MOBILE_,
                      LAYOUT_JSON_,
                      DELETED_,
                      COMPANY_ID_,
                      TENANT_ID_,
                      CREATE_DEP_ID_,
                      CREATE_BY_,
                      CREATE_TIME_,
                      UPDATE_BY_,
                      UPDATE_TIME_,
                      APP_ID_) VALUES('1265199891743682562',
                                      '移动端门户',
                                      'mobile',
                                      '1',
                                      '',
                                      1,
                                      'YES',
                                      '',
                                      0,
                                      '0',
                                      '0',
                                      '1',
                                      '1',
                                      TO_DATE('2020-5-26 8:35:35',
                                              'yyyy-MM-dd HH24:mi:ss'),
                                      '1',
                                      TO_DATE('2021-9-26 17:11:33',
                                              'yyyy-MM-dd HH24:mi:ss'),
                                      '');

DECLARE
  content_4 CLOB := '[{"type":"process","title":"流程管理","titleflg":true,"items":[{"title":"我的待办","routeName":"myTasks","icon":"icon-clock","bgColor":"#FFB400","iconfontcolor":"#fafafa","module":"bpm"},{"title":"新建流程","routeName":"mySolutions","icon":"icon-addsupply","bgColor":"#ea5570","iconfontcolor":"#fafafa","module":"bpm"},{"title":"我的消息","routeName":"news","icon":"icon-addsupply","bgColor":"#ea5570","iconfontcolor":"#fafafa","module":"bpm"},{"title":"我的流程","routeName":"myInst","icon":"icon-order","bgColor":"#7e7cf9","iconfontcolor":"#fafafa","module":"bpm"},{"title":"我的已办","routeName":"HasHandle","icon":"icon-caogao","bgColor":"#05c392","iconfontcolor":"#fafafa","module":"bpm"},{"title":"我的转办","routeName":"ReceiveTask","icon":"icon-order","bgColor":"#7e7cf9","iconfontcolor":"#fafafa","module":"bpm"},{"title":"转出代办","routeName":"TransOutTask","icon":"icon-addsupply","iconfontcolor":"#fafafa","bgColor":"#ea5570","module":"bpm"}]}]';
  content_7 CLOB := '';
BEGIN
  UPDATE ins_portal_def
     SET LAYOUT_HTML_ = content_4
   WHERE PORT_ID_ = '1265199891743682562';
  UPDATE ins_portal_def
     SET LAYOUT_JSON_ = content_7
   WHERE PORT_ID_ = '1265199891743682562';
  COMMIT;
END;
/

INSERT
  INTO ins_remind_def(ID_,
                      SUBJECT_,
                      URL_,
                      TYPE_,
                      SETTING_,
                      DESCRIPTION_,
                      SN_,
                      ENABLED_,
                      ICON_,
                      DS_NAME_,
                      DS_ALIAS_,
                      DELETED_,
                      COMPANY_ID_,
                      TENANT_ID_,
                      CREATE_BY_,
                      CREATE_TIME_,
                      UPDATE_BY_,
                      UPDATE_TIME_,
                      CREATE_DEP_ID_) VALUES('1394232369885880322',
                                             '待办通知',
                                             '/webApp/home/MyBpmTaskList',
                                             'function',
                                             '',
                                             '你有[count]待办',
                                             1,
                                             'YES',
                                             'smile',
                                             '',
                                             '',
                                             0,
                                             '0',
                                             '1',
                                             '1',
                                             TO_DATE('2021-5-17 18:04:35',
                                                     'yyyy-MM-dd HH24:mi:ss'),
                                             '1',
                                             TO_DATE('2021-6-29 14:57:42',
                                                     'yyyy-MM-dd HH24:mi:ss'),
                                             '');

DECLARE
  content_4 CLOB := 'portalScript.getMyTaskCount()';
BEGIN
  UPDATE ins_remind_def
     SET SETTING_ = content_4
   WHERE ID_ = '1394232369885880322';
  COMMIT;
END;
/

INSERT
  INTO ins_remind_def(ID_,
                      SUBJECT_,
                      URL_,
                      TYPE_,
                      SETTING_,
                      DESCRIPTION_,
                      SN_,
                      ENABLED_,
                      ICON_,
                      DS_NAME_,
                      DS_ALIAS_,
                      DELETED_,
                      COMPANY_ID_,
                      TENANT_ID_,
                      CREATE_BY_,
                      CREATE_TIME_,
                      UPDATE_BY_,
                      UPDATE_TIME_,
                      CREATE_DEP_ID_) VALUES('1399568779375443970',
                                             '表单设计统计',
                                             '/form/home/FormPcList',
                                             'sql',
                                             '',
                                             '你的表单设计[count]条。',
                                             1,
                                             'YES',
                                             'iconxinwen',
                                             '',
                                             'paas_form',
                                             0,
                                             '0',
                                             '1',
                                             '1',
                                             TO_DATE('2021-6-1 11:29:34',
                                                     'yyyy-MM-dd HH24:mi:ss'),
                                             '1',
                                             TO_DATE('2021-7-22 17:42:39',
                                                     'yyyy-MM-dd HH24:mi:ss'),
                                             '1385123428631506945');

DECLARE
  content_4 CLOB := 'String sql="select count(*) from form_pc where create_by_=''[USERID]''";
return sql;';
BEGIN
  UPDATE ins_remind_def
     SET SETTING_ = content_4
   WHERE ID_ = '1399568779375443970';
  COMMIT;
END;
/

INSERT
  INTO ins_portal_permission(ID_,
                             LAYOUT_ID_,
                             TYPE_,
                             OWNER_ID_,
                             OWNER_NAME_,
                             MENU_TYPE_,
                             DELETED_,
                             COMPANY_ID_,
                             TENANT_ID_,
                             CREATE_DEP_ID_,
                             CREATE_BY_,
                             CREATE_TIME_,
                             UPDATE_BY_,
                             UPDATE_TIME_) VALUES('1493825769258401794',
                                                  '1394232369885880322',
                                                  'ALL',
                                                  'ALL',
                                                  '',
                                                  'remind',
                                                  0,
                                                  '0',
                                                  '1',
                                                  '1',
                                                  '1',
                                                  TO_DATE('2022-2-16 13:53:12',
                                                          'yyyy-MM-dd HH24:mi:ss'),
                                                  '',
                                                  TO_DATE('2022-2-16 13:53:12',
                                                          'yyyy-MM-dd HH24:mi:ss'));

COMMIT;
