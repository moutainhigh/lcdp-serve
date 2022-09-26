INSERT INTO ins_column_def(COL_ID_,NAME_,KEY_,IS_DEFAULT_,TEMPLET_,IS_PUBLIC_,TYPE_,IS_MOBILE_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_,SET_TING_,ICON_)
         values(         '1239834260509945858',         '待办事项',         'BpmTaskColumn',         '0',         '<div class="gridLayoutClass">    <div class="headPClass">        <span style="display: inline-block;font-size: 16px;">{{insColumnDef.name}}</span>        <div style="float:right;">            <div class="journalism_span">                <span @click="moreUrl()">更多</span>                <span @click="refresh">刷新</span>            </div>        </div>    </div>    <div class="bodyDivClass">        <ul>            <li class="itmelist" v-for="(obj,index) of data" :key="obj.taskId" v-if="index <=8">                <p @click="handleTask(obj)">{{obj.subject}}</p>                <span>{{obj.createTime}}</span>            </li>        </ul>    </div></div>',         '0',         '1230771994887495681',         '0',         '0',         '2',         '1',         null,         '1',         to_date('2021-6-24 18:13:38','yyyy-MM-dd HH24:mi:ss'),         '{"dataUrl":"/webApp/home/MyBpmTaskList","function":"portalScript.getPortalBpmTask()","isNews":"","newType":"","tabgroups":"","functions":"portalScript.getPortalBpmTask(colId)","funcType":"func","sqlName":"省市"}',   '');

INSERT INTO ins_column_def(COL_ID_,NAME_,KEY_,IS_DEFAULT_,TEMPLET_,IS_PUBLIC_,TYPE_,IS_MOBILE_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_,SET_TING_,ICON_)
         values(         '1239837443886018562',         '集团新闻',         'insNews',         '0',         '<div class="gridLayoutClass">    <div class="headPClass">        <div style="display:inline-block;" >{{insColumnDef.name}}</div>        <div style="float: right">            <div class="journalism_span">             <span @click="moreUrl()">更多</span>             <span @click="refresh">刷新</span>            </div>        </div>    </div>    <div class="bodyDivClass">        <div class="aclsty" v-if="hasImage(data)">            <a-carousel arrows autoplay >                <div  slot="prevArrow"                    slot-scope="props"                    class="custom-slick-arrow"                    style="left: 10px;zIndex: 1"                    >                    <a-icon type="left-circle" />                </div>                <div slot="nextArrow" slot-scope="props" class="custom-slick-arrow" style="right: 10px">                    <a-icon type="right-circle" />                </div>                <div v-for="obj of data" v-if="obj.imgFileId"  class="inglist">                    <img :src="getImgPath(obj.imgFileId)"></img>                </div>            </a-carousel>        </div>        <div  >            <ul>                <li class="itmelist" v-for="(obj,index) of data" :key="obj.pkId" v-if="index <=5">                  <p @click="getInsNews(obj.pkId)">{{obj.subject}}</p>                  <span>{{obj.createTime}}</span>                </li>            </ul>        </div>    </div></div>',         '0',         '1230775094247464962',         '0',         '0',         '2',         '1',         null,         '1',         to_date('2021-7-28 16:28:58','yyyy-MM-dd HH24:mi:ss'),         '{"dataUrl":"/portal/InsNewsIssueds","function":"portalScript.getPortalNews(colId)","isNews":"YES","newType":"imgAndFont","tabgroups":"","newDIc":"companyNews"}',   '');

INSERT INTO ins_column_def(COL_ID_,NAME_,KEY_,IS_DEFAULT_,TEMPLET_,IS_PUBLIC_,TYPE_,IS_MOBILE_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_,SET_TING_,ICON_)
         values(         '1239838889297702914',         '消息盒子',         'newsColumn',         '0',         '<div class="msgBoxs">    <ul class="msgUl">        <li class="msgItme"            :style="{width:listWidthFn}"            v-for="(obj,index) of data"          :key="index">        <div @click="moreUrl(obj.url)" class="contentBox" :style="{''background'': obj.color != ''''?obj.color :''red''}">            <div class="msgContent">            <b>{{obj.count}}</b>            <p>{{obj.content}}</p >          </div>          <div class="header_icon">                    <a-icon v-if="!obj.icon.includes(''type'')" :type="obj.icon" />                    <my-icon v-else-if="[''customIcon'',''userCustomIcon''].includes(JSON.parse(obj.icon).type)" :type="JSON.parse(obj.icon).icon"></my-icon>                    <a-icon v-else="JSON.parse(obj.icon).type" :type="JSON.parse(obj.icon).icon"></a-icon>          </div>        </div>    </li>    </ul></div>',         '0',         '1230774411821621249',         '0',         '0',         '2',         '1',         null,         '1',         to_date('2021-7-28 16:11:00','yyyy-MM-dd HH24:mi:ss'),         '{"dataUrl":"","function":"1235758510261506049","newType":"","tabgroups":"","listCount":4,"newDIc":"","funcType":"","sqlName":"","service":"","calendarMode":""}',   'iconxinjian');

INSERT INTO ins_column_def(COL_ID_,NAME_,KEY_,IS_DEFAULT_,TEMPLET_,IS_PUBLIC_,TYPE_,IS_MOBILE_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_,SET_TING_,ICON_)
         values(         '1239839327900266497',         '公司公告',         'newsPortColumn',         '0',         '<div class="gridLayoutClass">    <div class="headPClass">        <div style="display:inline-block;" >{{insColumnDef.name}}</div>        <div style="float: right">            <div class="journalism_span">             <span @click="moreUrl()">更多</span>             <span @click="refresh">刷新</span>            </div>        </div>    </div>    <div class="bodyDivClass">        <div class="aclsty" v-if="hasImage(data)">            <a-carousel arrows autoplay >                <div  slot="prevArrow"                    slot-scope="props"                    class="custom-slick-arrow"                    style="left: 10px;zIndex: 1"                    >                    <a-icon type="left-circle" />                </div>                <div slot="nextArrow" slot-scope="props" class="custom-slick-arrow" style="right: 10px">                    <a-icon type="right-circle" />                </div>                <div v-for="obj of data"  v-if="obj.imgFileId" class="inglist">                    <img v-if="obj.imgFileId" :src="getImgPath(obj.imgFileId)"></img>                </div>            </a-carousel>        </div>        <div  >            <ul>                <li class="itmelist" v-for="(obj,index) of data" :key="obj.pkId" v-if="index <=5">                  <p @click="getInsNews(obj.pkId)">{{obj.subject}}</p>                  <span>{{obj.createTime}}</span>                </li>            </ul>        </div>    </div></div>',         '0',         '1230775094247464962',         '0',         '0',         '2',         '1',         null,         '1',         to_date('2021-7-28 16:28:43','yyyy-MM-dd HH24:mi:ss'),         '{"dataUrl":"/portal/InsNewsIssueds","function":"","newType":"imgAndFont","tabgroups":"","newDIc":"companyNews"}',   'iconbaobiao1');

INSERT INTO ins_column_def(COL_ID_,NAME_,KEY_,IS_DEFAULT_,TEMPLET_,IS_PUBLIC_,TYPE_,IS_MOBILE_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_,SET_TING_,ICON_)
         values(         '1239839763151581185',         '待办已办',         'tabColumn',         '0',         '<div class="page-header-index-wide">      <div class="salesCard">        <a-tabs default-active-key=0 size="large"  @change="changeKey" style="height:100%;" >          <div class="extra-wrapper" slot="tabBarExtraContent">              <div class="journalism_span">             <span @click="moreUrl()">更多</span>             <span @click="refresh">刷新</span>           </div>          </div>          <a-tab-pane class="hetlist" v-for="(obj,index) of data"  :tab="obj.name" :key="index">             <portal-layoutview ref="innerLayout" :insColumnDef="obj.insColumnDef" > </portal-layoutview>          </a-tab-pane>        </a-tabs>      </div>  </div>',         '0',         '1230774801220804610',         '0',         '0',         '2',         '1',         null,         '1',         to_date('2021-6-29 14:48:32','yyyy-MM-dd HH24:mi:ss'),         '{"dataUrl":"","function":"","isNews":"","newType":"","tabgroups":{"value":"1239834260509945858,1361616576253440002","text":"待办事项,已办事项"}}',   '');

INSERT INTO ins_column_def(COL_ID_,NAME_,KEY_,IS_DEFAULT_,TEMPLET_,IS_PUBLIC_,TYPE_,IS_MOBILE_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_,SET_TING_,ICON_)
         values(         '1259742027670732801',         '常用应用',         'application',         '0',         '<div class="gridLayoutClass"><div class="headPClass">常用应用</div><div class="bodyDivClass"><ul class="application_ul">        <li v-for="obj in data">		  <div v-if="obj.type ===''interior''" @click="moreUrl(obj.url)">            <a-icon v-if="!obj.icon.includes(''type'')" :type="obj.icon" />            <my-icon v-else-if="[''customIcon'',''userCustomIcon''].includes(JSON.parse(obj.icon).type)" :type="JSON.parse(obj.icon).icon"></my-icon>            <a-icon v-else="JSON.parse(obj.icon).type" :type="JSON.parse(obj.icon).icon"></a-icon>            <span>{{obj.name}}</span>		  </div>		  <div v-if="obj.type ===''outside''" @click="moreOutUrl(obj.url)">                             <a-icon v-if="!obj.icon.includes(''type'')" :type="obj.icon" />                <my-icon v-else-if="[''customIcon'',''userCustomIcon''].includes(JSON.parse(obj.icon).type)" :type="JSON.parse(obj.icon).icon"></my-icon>                <a-icon v-else="JSON.parse(obj.icon).type" :type="JSON.parse(obj.icon).icon"></a-icon>                <span>{{obj.name}}</span>		  </div>        </li>      </ul></div></div>',         '0',         '1230771994887495681',         '0',         '0',         '1',         '1',         null,         '1',         to_date('2021-6-29 10:51:36','yyyy-MM-dd HH24:mi:ss'),         '{"dataUrl":"","function":"portalScript.getMyAppList()","newType":"","tabgroups":"","listCount":6,"newDIc":"","funcType":"","sqlName":"","service":"","calendarMode":""}',   '');

INSERT INTO ins_column_def(COL_ID_,NAME_,KEY_,IS_DEFAULT_,TEMPLET_,IS_PUBLIC_,TYPE_,IS_MOBILE_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_,SET_TING_,ICON_)
         values(         '1259762922225254402',         '提醒',         'remind',         '0',         '<div class="gridLayoutClass">    <div class="headPClass">        <span style="display: inline-block;font-size: 16px;">我的提醒</span>    </div>    <div class="bodyDivClass">        <ul class="remind_ul">			<li v-for="obj in data">			    <span><a-icon :type="obj.icon" /></span>                <div class="remind_a" @click="moreUrl(obj.url)" v-html="obj.description"></div>			 </li>        </ul>    </div></div>',         '0',         '1230771994887495681',         '0',         '0',         '1',         '1',         null,         '1',         to_date('2021-5-17 15:53:09','yyyy-MM-dd HH24:mi:ss'),         '{"dataUrl":"","function":"portalScript.getRemindList()","newType":"","tabgroups":"","listCount":6,"newDIc":"","funcType":"","sqlName":""}',   '');

INSERT INTO ins_column_def(COL_ID_,NAME_,KEY_,IS_DEFAULT_,TEMPLET_,IS_PUBLIC_,TYPE_,IS_MOBILE_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_,SET_TING_,ICON_)
         values(         '1262555793443348482',         '我的日程',         'calendarTest001',         '0',         '<div class="gridLayoutClass portal-calendar">    <div class="headPClass">        <span style="display: inline-block;font-size: 16px;">我的日程</span>    </div>    <div class="bodyDivClass schstyle" style="overflow-y: auto;overflow-x: hidden;">        <div :style="{ width: ''100%'', borderRadius: ''4px'' }">            <a-calendar :fullscreen="false" :mode="calendarMode" @select="onCalendarSelect" @panelChange="onPanelChange" >                <ul slot="dateCellRender" slot-scope="value" class="events">                    <li class="relisdian" v-if="hasLoadMonthData" v-for="item in getCalendarDayListData(value)" :key="item.content">                        <a-badge :status="item.type" :text="item.content" />                    </li>                </ul>            </a-calendar>        </div>        <div class="relist">{{calendarValue}}日程</div>        <a-steps  progress-dot :current="1" direction="vertical" :class="[ calendarMode ==''year''? ''calenlist'':'''']">		        <a-step v-for="item in data" :key="item.index" :title="item.title">                    <span v-if="calendarMode ===''year''" class="shijian_year" slot="description">{{item.day}} {{item.startTime}}</span>		            <span v-if="calendarMode ===''month''" class="shijian" slot="description">{{item.startTime}}</span>		            <template slot="title">			            {{item.title}}		            </template>		            <span slot="description">{{item.describe}}</span>		        </a-step>	    </a-steps>        <a-empty v-if="data.length ==0" />    </div></div>',         '0',         '1262328598619750402',         '0',         '0',         '1',         '1',         null,         '1',         to_date('2021-6-21 15:38:05','yyyy-MM-dd HH24:mi:ss'),         '{"dataUrl":"","function":"","newType":"","tabgroups":"","listCount":6,"newDIc":"","funcType":"","sqlName":"","service":"default","calendarMode":"month"}',   '');

INSERT INTO ins_column_def(COL_ID_,NAME_,KEY_,IS_DEFAULT_,TEMPLET_,IS_PUBLIC_,TYPE_,IS_MOBILE_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_,SET_TING_,ICON_)
         values(         '1361616576253440002',         '已办事项',         'hadHandleInsts',         '0',         '<div class="gridLayoutClass">    <div class="headPClass">        <span style="display: inline-block;font-size: 16px;">{{insColumnDef.name}}</span>        <div style="float:right;">           <div class="journalism_span"> <span @click="moreUrl()">更多</span> <span @click="refresh">刷新</span> </div>        </div>    </div>    <div class="bodyDivClass">        <ul>            <li class="itmelist" v-for="(obj,index) of data" :key="obj.instId" v-if="index <=5">                <p @click="handleBpmInst(obj)">{{obj.subject}}</p>                <span>{{obj.createTime}}</span>            </li>        </ul>    </div></div>',         '0',         '1230771994887495681',         '0',         '0',         '',         '1',         null,         '1',         to_date('2021-5-18 15:12:35','yyyy-MM-dd HH24:mi:ss'),         '{"dataUrl":"/webApp/home/MyApprovedInsts","function":"portalScript.getMyApprovedInsts()","newType":"","tabgroups":"","listCount":6,"newDIc":"","funcType":"","sqlName":"","service":"","calendarMode":""}',   '');

INSERT INTO ins_column_def(COL_ID_,NAME_,KEY_,IS_DEFAULT_,TEMPLET_,IS_PUBLIC_,TYPE_,IS_MOBILE_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_,SET_TING_,ICON_)
         values(         '1363419382463524865',         '新闻公告Tab',         'NewsNoticeTab',         '0',         '<div class="page-header-index-wide">      <div class="salesCard">        <a-tabs default-active-key=0 size="large"  @change="changeKey" style="height:100%;" >          <div class="extra-wrapper" slot="tabBarExtraContent">              <div class="journalism_span">             <span @click="moreUrl()">更多</span>             <span @click="refresh">刷新</span>           </div>          </div>          <a-tab-pane class="hetlist" v-for="(obj,index) of data"  :tab="obj.name" :key="index">             <portal-layoutview ref="innerLayout" :insColumnDef="obj.insColumnDef" > </portal-layoutview>          </a-tab-pane>        </a-tabs>      </div>  </div>',         '0',         '1230774801220804610',         '0',         '0',         '',         '1',         null,         '1',         to_date('2021-6-29 14:46:46','yyyy-MM-dd HH24:mi:ss'),         '{"dataUrl":"","function":"","newType":"","tabgroups":{"value":"1239837443886018562,1239839327900266497","text":"集团新闻,公司公告"},"listCount":6,"newDIc":"","funcType":"","sqlName":"","service":"","calendarMode":""}',   '');

INSERT INTO ins_column_def(COL_ID_,NAME_,KEY_,IS_DEFAULT_,TEMPLET_,IS_PUBLIC_,TYPE_,IS_MOBILE_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_,SET_TING_,ICON_)
         values(         '1392727548903243778',         '饼图',         'piecharts',         '0',         '',         '0',         '1230771994887495674',         '0',         '1',         '1',         '1',         to_date('2021-5-13 14:24:58','yyyy-MM-dd HH24:mi:ss'),         '',         to_date('2021-5-13 14:24:58','yyyy-MM-dd HH24:mi:ss'),         '{"dataUrl":"","function":"","newType":"","tabgroups":"","listCount":6,"newDIc":"","funcType":"","sqlName":"","service":"","calendarMode":"","component":"modules/portal/core/charts/PieCharts.vue"}',   '');

INSERT INTO ins_column_def(COL_ID_,NAME_,KEY_,IS_DEFAULT_,TEMPLET_,IS_PUBLIC_,TYPE_,IS_MOBILE_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_,SET_TING_,ICON_)
         values(         '1392738215320346625',         '柱状图',         'barCharts',         '0',         '',         '0',         '1230771994887495674',         '0',         '1',         '1',         '1',         to_date('2021-5-13 15:07:21','yyyy-MM-dd HH24:mi:ss'),         '',         to_date('2021-5-13 15:07:21','yyyy-MM-dd HH24:mi:ss'),         '{"dataUrl":"","function":"","newType":"","tabgroups":"","listCount":6,"newDIc":"","funcType":"","sqlName":"","service":"","calendarMode":"","component":"modules/portal/core/charts/BarCharts.vue"}',   '');

INSERT INTO ins_column_def(COL_ID_,NAME_,KEY_,IS_DEFAULT_,TEMPLET_,IS_PUBLIC_,TYPE_,IS_MOBILE_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_,SET_TING_,ICON_)
         values(         '1392739711713796097',         '漏斗图',         'FunnelCharts',         '0',         '',         '0',         '1230771994887495674',         '0',         '1',         '1',         '1',         to_date('2021-5-13 15:13:18','yyyy-MM-dd HH24:mi:ss'),         '',         to_date('2021-5-13 15:13:18','yyyy-MM-dd HH24:mi:ss'),         '{"dataUrl":"","function":"","newType":"","tabgroups":"","listCount":6,"newDIc":"","funcType":"","sqlName":"","service":"","calendarMode":"","component":"modules/portal/core/charts/FunnelCharts.vue"}',   '');

INSERT INTO ins_column_def(COL_ID_,NAME_,KEY_,IS_DEFAULT_,TEMPLET_,IS_PUBLIC_,TYPE_,IS_MOBILE_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_,SET_TING_,ICON_)
         values(         '1392741135541592065',         '仪表盘',         'gaugeCharts',         '0',         '',         '0',         '1230771994887495674',         '0',         '1',         '1',         '1',         to_date('2021-5-13 15:18:57','yyyy-MM-dd HH24:mi:ss'),         '',         to_date('2021-5-13 15:18:57','yyyy-MM-dd HH24:mi:ss'),         '{"dataUrl":"","function":"","newType":"","tabgroups":"","listCount":6,"newDIc":"","funcType":"","sqlName":"","service":"","calendarMode":"","component":"modules/portal/core/charts/GaugeCharts.vue"}',   '');

INSERT INTO ins_column_def(COL_ID_,NAME_,KEY_,IS_DEFAULT_,TEMPLET_,IS_PUBLIC_,TYPE_,IS_MOBILE_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_,SET_TING_,ICON_)
         values(         '1392742738608775169',         '曲线图',         'linecharts',         '0',         '',         '0',         '1230771994887495674',         '0',         '1',         '1',         '1',         to_date('2021-5-13 15:25:19','yyyy-MM-dd HH24:mi:ss'),         '',         to_date('2021-5-13 15:25:19','yyyy-MM-dd HH24:mi:ss'),         '{"dataUrl":"","function":"","newType":"","tabgroups":"","listCount":6,"newDIc":"","funcType":"","sqlName":"","service":"","calendarMode":"","component":"modules/portal/core/charts/LineCharts.vue"}',   '');

INSERT INTO ins_column_def(COL_ID_,NAME_,KEY_,IS_DEFAULT_,TEMPLET_,IS_PUBLIC_,TYPE_,IS_MOBILE_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_,SET_TING_,ICON_)
         values(         '1392746879276015617',         '混合图形',         'mixCharts',         '0',         '',         '0',         '1230771994887495674',         '0',         '1',         '1',         '1',         to_date('2021-5-13 15:41:47','yyyy-MM-dd HH24:mi:ss'),         '',         to_date('2021-5-13 15:41:47','yyyy-MM-dd HH24:mi:ss'),         '{"dataUrl":"","function":"","newType":"","tabgroups":"","listCount":6,"newDIc":"","funcType":"","sqlName":"","service":"","calendarMode":"","component":"modules/portal/core/charts/MixCharts.vue"}',   '');

INSERT INTO ins_column_def(COL_ID_,NAME_,KEY_,IS_DEFAULT_,TEMPLET_,IS_PUBLIC_,TYPE_,IS_MOBILE_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_,SET_TING_,ICON_)
         values(         '1406061558150025218',         '待办事项-门户',         'bpmTaskColumn_portal',         '0',         '<div v-if="insColumnDef" class="insColumnDef">	<div class="headerTitle">		<header>{{ insColumnDef.name }}</header>		<span class="more"  @click="moreUrl(''/webApp/home/MyBpmTaskList'',true)">更多<a-icon type="caret-right" /></span>	</div>	<div class="portalContent">		<div class="portalContentBox">            <div class="portalList">                <ul>                    <li class="list-li"  v-for="(obj,index) of data" :key="obj.taskId" v-if="index <=6">                        <span class="icon">                           <a-icon type="form" />                        </span>                        <div class="text">                                                 <span class="itemText" :title="obj.subject" @click="handleTaskByUrl(insColumnDef,obj)">{{obj.subject}}</span>                        </div>                                              <div class="listColumn column-user">                            {{obj.name}}                        </div>                        <div class="listColumn time">                          {{obj.createTime}}                        </div>                    </li>                </ul>            </div>		</div>	</div></div>',         '0',         '1230771994887495681',         '0',         '1',         '1',         '1',         to_date('2021-6-19 9:29:33','yyyy-MM-dd HH24:mi:ss'),         '1',         to_date('2021-8-13 16:04:57','yyyy-MM-dd HH24:mi:ss'),         '{"dataUrl":"/bpmTask/start","function":"portalScript.getPortalBpmTask()","newType":"","tabgroups":"","listCount":6,"newDIc":"","funcType":"","sqlName":"","service":"","calendarMode":""}',   'iconhomepage');

INSERT INTO ins_column_def(COL_ID_,NAME_,KEY_,IS_DEFAULT_,TEMPLET_,IS_PUBLIC_,TYPE_,IS_MOBILE_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_,SET_TING_,ICON_)
         values(         '1406144016241942530',         '消息盒子-门户',         'msg',         '0',         '<div class="portal-list-box">	<div class="headerTitle">		<header>{{ insColumnDef.name }}</header>	</div>	<div class="listContent">		<div class="listContentBox">			<ul class="portal-msg-box">                <li  class="portal-msg-item" :style="{width:listWidthFn}" v-for="(obj,index) of data" :key="index">                    <div  @click="moreUrl(obj.url,true)" class="portal-msg-div" >                       <div class="msg_icon" :style="{''background'': obj.color != ''''?obj.color :''red''}">                    <a-icon v-if="!obj.icon.includes(''type'')" :type="obj.icon" />                    <my-icon v-else-if="[''customIcon'',''userCustomIcon''].includes(JSON.parse(obj.icon).type)" :type="JSON.parse(obj.icon).icon"></my-icon>                    <a-icon v-else="JSON.parse(obj.icon).type" :type="JSON.parse(obj.icon).icon"></a-icon>          </div>                        <div class="portal-msg-content">                            <b>{{obj.count}}</b>                            <p>{{obj.content}}</p >                        </div>                    </div>                </li>            </ul>		</div>	</div></div>',         '0',         '1230774411821621249',         '0',         '1',         '1385123428631506945',         '1',         to_date('2021-6-19 14:57:13','yyyy-MM-dd HH24:mi:ss'),         '1',         to_date('2021-6-21 18:23:12','yyyy-MM-dd HH24:mi:ss'),         '{"dataUrl":"","function":"1235758510261506049","newType":"","tabgroups":{"value":"1406061558150025218,1361616576253440002,1239834260509945858","text":"待办事项-门户,已办事项,待办事项"},"listCount":4,"newDIc":"","funcType":"","sqlName":"","service":"","calendarMode":""}',   'iconcaidan-');

INSERT INTO ins_column_def(COL_ID_,NAME_,KEY_,IS_DEFAULT_,TEMPLET_,IS_PUBLIC_,TYPE_,IS_MOBILE_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_,SET_TING_,ICON_)
         values(         '1406791032122966017',         '轮播图-门户',         'carousels',         '0',         '<cariusel>轮播图</cariusel>',         '0',         '1230771994887495681',         '0',         '1',         '1385123428631506945',         '1',         to_date('2021-6-21 9:48:14','yyyy-MM-dd HH24:mi:ss'),         '',         to_date('2021-6-21 9:48:14','yyyy-MM-dd HH24:mi:ss'),         '{"dataUrl":"","function":"get()","newType":"","tabgroups":"","listCount":6,"newDIc":"","funcType":"","sqlName":"","service":"","calendarMode":""}',   '');

INSERT INTO ins_column_def(COL_ID_,NAME_,KEY_,IS_DEFAULT_,TEMPLET_,IS_PUBLIC_,TYPE_,IS_MOBILE_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_,SET_TING_,ICON_)
         values(         '1406817035213930498',         '通讯录',         'maillist',         '0',         '<rx-list :name="''通讯录''">	<mail-list></mail-list></rx-list>',         '0',         '1230771994887495681',         '0',         '1',         '1385123428631506945',         '1',         to_date('2021-6-21 11:31:33','yyyy-MM-dd HH24:mi:ss'),         '1',         to_date('2021-6-21 11:31:40','yyyy-MM-dd HH24:mi:ss'),         '{"dataUrl":"","function":"get()","newType":"","tabgroups":"","listCount":6,"newDIc":"","funcType":"","sqlName":"","service":"","calendarMode":""}',   '');

INSERT INTO ins_column_def(COL_ID_,NAME_,KEY_,IS_DEFAULT_,TEMPLET_,IS_PUBLIC_,TYPE_,IS_MOBILE_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_,SET_TING_,ICON_)
         values(         '1407177489941549058',         '文件管理',         'filemanage',         '0',         '<rx-list :name="''文件管理''">    <enclosure></enclosure></rx-list>',         '0',         '1230771994887495681',         '0',         '1',         '1385123428631506945',         '1',         to_date('2021-6-22 11:23:52','yyyy-MM-dd HH24:mi:ss'),         '',         to_date('2021-6-22 11:23:52','yyyy-MM-dd HH24:mi:ss'),         '{"dataUrl":"","function":"get()","newType":"","tabgroups":"","listCount":6,"newDIc":"","funcType":"","sqlName":"","service":"","calendarMode":""}',   '');

INSERT INTO ins_column_def(COL_ID_,NAME_,KEY_,IS_DEFAULT_,TEMPLET_,IS_PUBLIC_,TYPE_,IS_MOBILE_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_,SET_TING_,ICON_)
         values(         '1407547641765888002',         '排行列表',         'rankingList',         '0',         '<rx-list :name="''排行列表''">
<ranking-list></ranking-list>
</rx-list>',         '0',         '1230771994887495681',         '0',         '1',         '1385123428631506945',         '1',         to_date('2021-6-23 11:54:43','yyyy-MM-dd HH24:mi:ss'),         '1',         to_date('2021-6-23 12:02:31','yyyy-MM-dd HH24:mi:ss'),         '{"dataUrl":"","function":"get()","newType":"","tabgroups":"","listCount":6,"newDIc":"","funcType":"","sqlName":"","service":"","calendarMode":""}',   '');

INSERT INTO ins_column_def(COL_ID_,NAME_,KEY_,IS_DEFAULT_,TEMPLET_,IS_PUBLIC_,TYPE_,IS_MOBILE_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_,SET_TING_,ICON_)
         values(         '1409824567578746881',         '基础栏目',         'jclm',         '0',         '<div class="page-header-index-wide">         <div class="salesCard" headPClass>        <a-tabs default-active-key=0 size="large"  @change="changeKey" :tab-bar-style="{marginBottom: ''24px'', paddingLeft: ''16px''}">          <div class="extra-wrapper" slot="tabBarExtraContent">            <div class="extra-item">              <span class="btnSpan" @click="moreUrl()">                <a-icon type="dash"></a-icon>              </span>              <span class="btnSpan" @click="refresh">                <a-icon type="redo"></a-icon>              </span>            </div>          </div>          <a-tab-pane class="hetlist" v-for="(obj,index) of data"  :tab="obj.name" :key="index">            <portal-layoutview ref="innerLayout"  :insColumnDef="obj.insColumnDef"></portal-layoutview>          </a-tab-pane>        </a-tabs>      </div>     </div>',         '0',         '1230774801220804610',         '0',         '1',         '',         '1',         to_date('2021-6-29 18:42:25','yyyy-MM-dd HH24:mi:ss'),         '',         to_date('2021-6-29 18:42:25','yyyy-MM-dd HH24:mi:ss'),         '{"dataUrl":"","function":"","newType":"","tabgroups":{"value":"1259742027670732801,1259762922225254402","text":"常用应用,提醒"},"listCount":6,"newDIc":"","funcType":"","sqlName":"","service":"","calendarMode":""}',   '');

INSERT INTO ins_column_def(COL_ID_,NAME_,KEY_,IS_DEFAULT_,TEMPLET_,IS_PUBLIC_,TYPE_,IS_MOBILE_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_,SET_TING_,ICON_)
         values(         '1426090204717772802',         '已办事项-门户',         'hadHandleInsts_portal',         '0',         '<div v-if="insColumnDef" class="insColumnDef">	<div class="headerTitle">		<header>{{ insColumnDef.name }}</header>		<span class="more"  @click="moreUrl(''/webApp/home/MyApprovedTasks'',true)">更多<a-icon type="caret-right" /></span>	</div>	<div class="portalContent">		<div class="portalContentBox">            <div class="portalList">                <ul>                    <li class="list-li"  v-for="(obj,index) of data" :key="obj.instId" v-if="index <=6">                        <span class="icon">                           <a-icon type="form" />                        </span>                        <div class="text">                                                 <span class="itemText" :title="obj.subject" @click="handleTaskByUrl(insColumnDef,obj)">{{obj.subject}}</span>                        </div>                                              <div class="listColumn column-user">                            {{obj.name}}                        </div>                        <div class="listColumn time">                          {{obj.createTime}}                        </div>                    </li>                </ul>            </div>		</div>	</div></div>',         '0',         '1230771994887495681',         '0',         '1',         '1',         '1',         to_date('2021-8-13 15:56:15','yyyy-MM-dd HH24:mi:ss'),         '1',         to_date('2021-8-13 16:13:20','yyyy-MM-dd HH24:mi:ss'),         '{"dataUrl":"/bpmInst/detail","function":"portalScript.getMyApprovedInsts()","newType":"wordsList","tabgroups":"","listCount":6,"newDIc":"","funcType":"","sqlName":"","service":"","calendarMode":""}',   '');

INSERT INTO ins_column_def(COL_ID_,NAME_,KEY_,IS_DEFAULT_,TEMPLET_,IS_PUBLIC_,TYPE_,IS_MOBILE_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_,SET_TING_,ICON_,APP_ID_)
         values(         '1438703971263635457',         '消息盒子(应用)',         'MessageBox_app',         '0',         '<div class=\"msgBoxs\">\n    <ul class=\"msgUl\">\n        <li class=\"msgItme\"\n            :style=\"{width:listWidthFn}\"\n            v-for=\"(obj,index) of data\"\n          :key=\"index\">\n        <div @click=\"moreUrl(obj.url,true)\" class=\"contentBox\" :style=\"{''background'': obj.color != ''''?obj.color :''red''}\">\n            <div class=\"msgContent\">\n            <b>{{obj.count}}</b>\n            <p>{{obj.content}}</p >\n          </div>\n          <div class=\"header_icon\">\n                    <a-icon v-if=\"!obj.icon.includes(''type'')\" :type=\"obj.icon\" />\n                    <my-icon v-else-if=\"[''customIcon'',''userCustomIcon''].includes(JSON.parse(obj.icon).type)\" :type=\"JSON.parse(obj.icon).icon\"></my-icon>\n                    <a-icon v-else=\"JSON.parse(obj.icon).type\" :type=\"JSON.parse(obj.icon).icon\"></a-icon>\n          </div>\n        </div>\n    </li>\n    </ul>\n</div>',         '0',         '1230774411821621249',         '0',         '1',         '1',         '1',         to_date('2021-9-17 11:18:51','yyyy-MM-dd HH24:mi:ss'),         '1',         to_date('2021-9-17 11:21:58','yyyy-MM-dd HH24:mi:ss'),         '{"dataUrl":"","function":"1235758510261506049","newType":"","tabgroups":"","listCount":4,"newDIc":"","funcType":"","sqlName":"","service":"","calendarMode":""}',   '',         '');


INSERT INTO ins_column_temp(ID_,NAME_,KEY_,TEMPLET_,IS_SYS_,STATUS_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_,TEMP_TYPE_)
         values(         '1230771994887495674',         'VUE组件',         'vue',         '',         '1',         '1',         '0',         '2',         '1',         to_date('2020-2-21 8:31:25','yyyy-MM-dd HH24:mi:ss'),         '1',         to_date('2021-5-11 12:41:38','yyyy-MM-dd HH24:mi:ss'),         'VueBel');

INSERT INTO ins_column_temp(ID_,NAME_,KEY_,TEMPLET_,IS_SYS_,STATUS_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_,TEMP_TYPE_)
        values(         '1230771994887495681',         '列表',         'list',         '<div class="gridLayoutClass">
    <div class="headPClass">
        <div style="display:inline-block;" >{{insColumnDef.name}}</div>
        <div style="float: right">
          <div style="float: right">
            <div class="journalism_span"> <span @click="moreUrl()">更多</span> <span @click="refresh">刷新</span> </div>
        </div>
        </div>
    </div>
    <div class="bodyDivClass">
        <ul>
        	<li class="itmelist" v-for="(item,index) of data" :key="item.pkId" >
          	 <p @click="handleBpmInst(item)">{{item.SUBJECT_}</p>
                <span>{{getDate(item.CREATE_TIME_)}}</span>
            </li>
        </ul>
    </div>
</div>',         '1',         '1',         '0',         '2',         '1',         to_date('2020-2-21 8:31:25','yyyy-MM-dd HH24:mi:ss'),         '1',         to_date('2021-10-26 17:43:58','yyyy-MM-dd HH24:mi:ss'),         'List');

INSERT INTO ins_column_temp(ID_,NAME_,KEY_,TEMPLET_,IS_SYS_,STATUS_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_,TEMP_TYPE_)
         values(         '1230774411821621249',         '消息盒子',         'msgbox',         '<div class="msgBoxs">
    <ul class="msgUl">
        <li class="msgItme"
            :style="{width:listWidthFn}"
            v-for="(obj,index) of data"
          :key="index">
        <div @click="moreUrl(obj.url)" class="contentBox" >
            <div class="msgContent">
            <b>{{obj.count}}</b>
            <p>{{obj.content}}</p >
          </div>
          <div class="header_icon" :style="{''color'': obj.color != ''''?obj.color :''red''}">
                    <a-icon v-if="!obj.icon.includes(''type'')" :type="obj.icon" />
                    <my-icon v-else-if="[''customIcon'',''userCustomIcon''].includes(JSON.parse(obj.icon).type)" :type="JSON.parse(obj.icon).icon"></my-icon>
                    <a-icon v-else="JSON.parse(obj.icon).type" :type="JSON.parse(obj.icon).icon"></a-icon>
          </div>
        </div>
    </li>
    </ul>
</div>',         '1',         '1',         '0',         '2',         '1',         to_date('2020-2-21 8:41:01','yyyy-MM-dd HH24:mi:ss'),         '1',         to_date('2021-9-14 17:09:11','yyyy-MM-dd HH24:mi:ss'),         'MessageBel');


INSERT INTO ins_column_temp(ID_,NAME_,KEY_,TEMPLET_,IS_SYS_,STATUS_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_,TEMP_TYPE_)
         values(         '1230774801220804610',         'tab标签页',         'tabcolumn',         '<div class="page-header-index-wide">

      <div class="salesCard" headPClass>
        <a-tabs default-active-key=0 size="large"  @change="changeKey" :tab-bar-style="{marginBottom: ''24px'', paddingLeft: ''16px''}">
          <div class="extra-wrapper" slot="tabBarExtraContent">
            <div class="extra-item">
              <span class="btnSpan" @click="moreUrl()">
                <a-icon type="dash"></a-icon>
              </span>
              <span class="btnSpan" @click="refresh">
                <a-icon type="redo"></a-icon>
              </span>
            </div>
          </div>
          <a-tab-pane class="hetlist" v-for="(obj,index) of data"  :tab="obj.name" :key="index">
            <portal-layoutview ref="innerLayout"  :insColumnDef="obj.insColumnDef"></portal-layoutview>
          </a-tab-pane>
        </a-tabs>
      </div>

  </div>',         '1',         '1',         '0',         '2',         '1',         to_date('2020-2-21 8:42:34','yyyy-MM-dd HH24:mi:ss'),         '1',         to_date('2021-5-14 10:08:40','yyyy-MM-dd HH24:mi:ss'),         'TabBel');

INSERT INTO ins_column_temp(ID_,NAME_,KEY_,TEMPLET_,IS_SYS_,STATUS_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_,TEMP_TYPE_)
         values(         '1230775094247464962',         '新闻公告',         'newsNotice',         '<div class="gridLayoutClass">
    <div class="headPClass">
        <div style="display:inline-block;" >{{insColumnDef.name}}</div>
        <div style="float: right">
            <div class="journalism_span"> <span @click="moreUrl()">更多</span> <span @click="refresh">刷新</span> </div>
        </div>
    </div>
    <div class="bodyDivClass">
        <div class="aclsty" v-if="newType ==''img'' || newType ==''imgAndFont''">
            <a-carousel arrows autoplay>
                <div slot="prevArrow" slot-scope="props" class="custom-slick-arrow" style="left: 10px;zIndex: 1">
                    <a-icon type="left-circle" />
                </div>
                <div slot="nextArrow" slot-scope="props" class="custom-slick-arrow" style="right: 10px">
                    <a-icon type="right-circle" />
                </div>
                <div v-for="lising of data" class="inglist"><img :src="getImgPath(lising.imgFileId)"></img></div>
            </a-carousel>
        </div>
        <div v-if="newType !=''img''" :class="[newType !=''wordsList''? ''listing'':'''' ]">
            <ul>
                <li class="itmelist" v-for="(obj,index) of data" :key="obj.pkId" v-if="index <=5">
                    <p @click="getInsNews(obj.pkId)">{{obj.subject}}</p> <span>{{obj.createTime}}</span>
                </li>
            </ul>
        </div>
    </div>
</div>',         '1',         '1',         '0',         '2',         '1',         to_date('2020-2-21 8:43:44','yyyy-MM-dd HH24:mi:ss'),         '1',         to_date('2021-5-17 15:47:27','yyyy-MM-dd HH24:mi:ss'),         'NewsBel');

INSERT INTO ins_column_temp(ID_,NAME_,KEY_,TEMPLET_,IS_SYS_,STATUS_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_,TEMP_TYPE_)
         values(         '1262328598619750402',         '日程',         'calendar',         '<div class="gridLayoutClass">
    <div class="headPClass">
            <span style="display: inline-block;font-size: 16px;">我的日程</span>
    </div>
    <div class="bodyDivClass schstyle" style="overflow-y: auto;overflow-x: hidden;">
       <div :style="{ width: ''100%'', borderRadius: ''4px'' }">
            <a-calendar :fullscreen="false" :mode="calendarMode" @select="onCalendarSelect" @panelChange="onPanelChange" >
                <ul slot="dateCellRender" slot-scope="value" class="events">
                    <li class="relisdian" v-if="isxunran" v-for="item in getCalendarDayListData(value)" :key="item.content">
                        <a-badge :status="item.type" :text="item.content" />
                    </li>
                </ul>
            </a-calendar>
    </div>
    <div class="relist">{{calendarValue}}日程</div>
    <a-steps  progress-dot :current="1" direction="vertical" :class="[ calendarMode ==''year''? ''calenlist'':'''']">
        <a-step v-for="item in data" :key="item.index" :title="item.title">
            <span v-if="calendarMode ===''year''" class="shijian_year" slot="description">{{item.day}} {{item.startTime}}</span>
            <span v-if="calendarMode ===''month''" class="shijian" slot="description">{{item.startTime}}</span>
            <template slot="title">{{item.title}} </template>
            <span slot="description">{{item.describe}}</span>
        </a-step>
    </a-steps>
    <a-empty v-if="data.length ==0" />
    </div>
</div>',         '1',         '1',         '0',         '1',         '1',         to_date('2020-5-18 10:26:06','yyyy-MM-dd HH24:mi:ss'),         '1',         to_date('2021-5-11 11:54:44','yyyy-MM-dd HH24:mi:ss'),         'Calendar');

INSERT INTO ins_column_temp(ID_,NAME_,KEY_,TEMPLET_,IS_SYS_,STATUS_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_,TEMP_TYPE_)
         values(         '1393012721066573826',         '提醒模板',         'remind',         '<div class="gridLayoutClass">
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
</div>',         '1',         '1',         '1',         '1',         '1',         to_date('2021-5-14 9:18:08','yyyy-MM-dd HH24:mi:ss'),         '1',         to_date('2021-5-14 11:02:25','yyyy-MM-dd HH24:mi:ss'),         'List');


INSERT INTO ins_msg_def(MSG_ID_,COLOR_,URL_,ICON_,CONTENT_,DS_NAME_,DS_ALIAS_,SQL_FUNC_,TYPE_,TENANT_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_,COUNT_TYPE_,CREATE_DEP_ID_)
         values(         '1259773507849256961',         '#ff5757',         '/webApp/home/MyBpmInstList',         '{"type":"brand_logo","icon":"inbox"}',         '我的流程草稿',         '',         'jpaas_bpm',         'portalScript.getMyAllDraftBpmInst()',         'function',         '0',         '1',         to_date('2020-5-11 9:13:04','yyyy-MM-dd HH24:mi:ss'),         '1',         to_date('2021-5-13 17:11:29','yyyy-MM-dd HH24:mi:ss'),         '',         '1');

INSERT INTO ins_msg_def(MSG_ID_,COLOR_,URL_,ICON_,CONTENT_,DS_NAME_,DS_ALIAS_,SQL_FUNC_,TYPE_,TENANT_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_,COUNT_TYPE_,CREATE_DEP_ID_)
         values(         '1260089706780913666',         '#2ba0fc',         '/webApp/home/MyApprovedTasks',         '{"type":"brand_logo","icon":"file-done"}',         '我的已办',         '',         'jpaas_bpm',         'portalScript.getMyApprovedTaskCount()',         'function',         '0',         '1',         to_date('2020-5-12 6:09:32','yyyy-MM-dd HH24:mi:ss'),         '1',         to_date('2021-5-13 17:11:31','yyyy-MM-dd HH24:mi:ss'),         '',         '1');

INSERT INTO ins_msg_def(MSG_ID_,COLOR_,URL_,ICON_,CONTENT_,DS_NAME_,DS_ALIAS_,SQL_FUNC_,TYPE_,TENANT_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_,COUNT_TYPE_,CREATE_DEP_ID_)
         values(         '1260090213217955841',         '#feaf00',         '/webApp/home/MyBpmTaskList',         '{"type":"brand_logo","icon":"audit"}',         '我的待办',         '',         '',         'portalScript.getMyTaskCount()',         'function',         '0',         '1',         to_date('2020-5-12 6:11:33','yyyy-MM-dd HH24:mi:ss'),         '1',         to_date('2021-5-13 17:11:34','yyyy-MM-dd HH24:mi:ss'),         '',         '1');

INSERT INTO ins_msg_def(MSG_ID_,COLOR_,URL_,ICON_,CONTENT_,DS_NAME_,DS_ALIAS_,SQL_FUNC_,TYPE_,TENANT_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_,COUNT_TYPE_,CREATE_DEP_ID_)
         values(         '1260090447218176002',         '#1bd895',         '/webApp/home/innerMsgs/InfInnerMsgList',         '{"type":"brand_logo","icon":"message"}',         '我的消息',         '',         '',         'portalScript.getMyInnerMsgCount()    ',         'function',         '0',         '1',         to_date('2020-5-12 6:12:29','yyyy-MM-dd HH24:mi:ss'),         '1',         to_date('2021-5-13 17:11:41','yyyy-MM-dd HH24:mi:ss'),         '',         '1');

INSERT INTO ins_msgbox_box_def(SN_,UPDATE_TIME_,UPDATE_BY_,CREATE_TIME_,CREATE_BY_,TENANT_ID_,MSG_ID_,BOX_ID_,ID_,CREATE_DEP_ID_)
         values(         0,         to_date('2021-6-29 15:39:23','yyyy-MM-dd HH24:mi:ss'),         '',         to_date('2021-6-29 15:39:23','yyyy-MM-dd HH24:mi:ss'),         '1',         '1',         '1260089706780913666',         '1235758510261506049',         '1409778506415890434',         '');

INSERT INTO ins_msgbox_box_def(SN_,UPDATE_TIME_,UPDATE_BY_,CREATE_TIME_,CREATE_BY_,TENANT_ID_,MSG_ID_,BOX_ID_,ID_,CREATE_DEP_ID_)
         values(         1,         to_date('2021-6-29 15:39:23','yyyy-MM-dd HH24:mi:ss'),         '',         to_date('2021-6-29 15:39:23','yyyy-MM-dd HH24:mi:ss'),         '1',         '1',         '1260090447218176002',         '1235758510261506049',         '1409778506696908801',         '');

INSERT INTO ins_msgbox_box_def(SN_,UPDATE_TIME_,UPDATE_BY_,CREATE_TIME_,CREATE_BY_,TENANT_ID_,MSG_ID_,BOX_ID_,ID_,CREATE_DEP_ID_)
         values(         2,         to_date('2021-6-29 15:39:23','yyyy-MM-dd HH24:mi:ss'),         '',         to_date('2021-6-29 15:39:23','yyyy-MM-dd HH24:mi:ss'),         '1',         '1',         '1260090213217955841',         '1235758510261506049',         '1409778506709491713',         '');

INSERT INTO ins_msgbox_box_def(SN_,UPDATE_TIME_,UPDATE_BY_,CREATE_TIME_,CREATE_BY_,TENANT_ID_,MSG_ID_,BOX_ID_,ID_,CREATE_DEP_ID_)
         values(         3,         to_date('2021-6-29 15:39:23','yyyy-MM-dd HH24:mi:ss'),         '',         to_date('2021-6-29 15:39:23','yyyy-MM-dd HH24:mi:ss'),         '1',         '1',         '1259773507849256961',         '1235758510261506049',         '1409778506722074625',         '');

INSERT INTO ins_msgbox_def(BOX_ID_,KEY_,NAME_,TENANT_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_,CREATE_DEP_ID_)
         values(         '1235758510261506049',         'msgbox',         '消息盒',         '0',         '1',         to_date('2020-3-6 2:46:03','yyyy-MM-dd HH24:mi:ss'),         '1',         to_date('2021-6-29 15:39:23','yyyy-MM-dd HH24:mi:ss'),         '2');

INSERT INTO ins_portal_def(PORT_ID_,NAME_,KEY_,IS_DEFAULT_,LAYOUT_HTML_,PRIORITY_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_,IS_MOBILE_,LAYOUT_JSON_)
         values(         '1232486079769382914',         '全局门户',         'GLOBAL-PERSONAL',         '1','',         2,         '0',         '2',         '1',         to_date('2020-2-26 2:02:35','yyyy-MM-dd HH24:mi:ss'),         '1',         to_date('2021-6-30 9:47:23','yyyy-MM-dd HH24:mi:ss'),         'NO',         '[{"x":0,"y":0,"w":24,"h":12,"i":1621236255245,"colId":"1239838889297702914","layoutName":"消息盒子","moved":false},{"x":19,"y":12,"w":5,"h":72,"i":1621237747167,"colId":"1262555793443348482","layoutName":"我的日程","moved":false},{"x":8,"y":12,"w":6,"h":35,"i":1622021399023,"colId":"1239839763151581185","layoutName":"待办已办","moved":false},{"x":0,"y":47,"w":10,"h":37,"i":1623894607397,"colId":"1392739711713796097","layoutName":"漏斗图","moved":false},{"x":10,"y":47,"w":9,"h":37,"i":1623894607398,"colId":"1392741135541592065","layoutName":"仪表盘","moved":false},{"x":0,"y":84,"w":24,"h":15,"i":1624247108516,"colId":"1259742027670732801","layoutName":"常用应用","moved":false},{"x":14,"y":12,"w":5,"h":35,"i":1624596484088,"colId":"1239834260509945858","layoutName":"待办事项","moved":false},{"x":0,"y":12,"w":8,"h":35,"i":1625017634304,"colId":"1363419382463524865","layoutName":"新闻公告Tab","moved":false}]');

INSERT INTO ins_portal_def(PORT_ID_,NAME_,KEY_,IS_DEFAULT_,LAYOUT_HTML_,PRIORITY_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_,IS_MOBILE_,LAYOUT_JSON_)
         values(         '1232552596452241409',         '个人门户',         'PERSONAL',         '0','',         0,         '0',         '2',         '1',         to_date('2020-2-26 6:26:53','yyyy-MM-dd HH24:mi:ss'),         '1',         to_date('2021-6-17 10:26:48','yyyy-MM-dd HH24:mi:ss'),         'NO',         '[{"x":3,"y":0,"w":13,"h":23,"i":1591062260072,"colId":"1239838889297702914","layoutName":"消息盒子","moved":false},{"x":0,"y":0,"w":3,"h":17,"i":1591170952239,"colId":"1262555793443348482","layoutName":"我的日程","moved":false},{"x":16,"y":0,"w":4,"h":16,"i":1591170952248,"colId":"1260384379431047169","layoutName":"新闻栏目","moved":false},{"x":20,"y":0,"w":4,"h":16,"i":1591170952254,"colId":"1260154293419307009","layoutName":"饼状图","moved":false},{"x":20,"y":16,"w":4,"h":16,"i":1621583260913,"colId":"1394584155565989889","layoutName":"待办已办事项tab","moved":false},{"x":20,"y":32,"w":4,"h":16,"i":1621583260914,"colId":"1394578718980448258","layoutName":"线索消息","moved":false},{"x":20,"y":48,"w":4,"h":16,"i":1622533798019,"colId":"1394584155565989889","layoutName":"待办已办事项tab","moved":false},{"x":20,"y":64,"w":4,"h":16,"i":1622533798021,"colId":"1394578718980448258","layoutName":"线索消息","moved":false},{"x":16,"y":16,"w":4,"h":16,"i":1623896556304,"colId":"1392746879276015617","layoutName":"混合图形","moved":false},{"x":16,"y":32,"w":4,"h":16,"i":1623896567681,"colId":"1392727548903243778","layoutName":"饼图","moved":false},{"x":20,"y":80,"w":4,"h":16,"i":1623896801613,"colId":"1259762922225254402","layoutName":"提醒","moved":false},{"x":8,"y":39,"w":4,"h":16,"i":1623896801614,"colId":"1392739711713796097","layoutName":"漏斗图","moved":false},{"x":10,"y":55,"w":4,"h":16,"i":1623896801615,"colId":"1392741135541592065","layoutName":"仪表盘","moved":false},{"x":6,"y":23,"w":4,"h":16,"i":1623896801616,"colId":"1392742738608775169","layoutName":"曲线图","moved":false}]');

INSERT INTO ins_portal_def(PORT_ID_,NAME_,KEY_,IS_DEFAULT_,LAYOUT_HTML_,PRIORITY_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_,IS_MOBILE_,LAYOUT_JSON_,APP_ID_)
         values(         '1265199891743682562',         '移动端门户',         'mobile',         '1','[{"type":"process","title":"流程管理","titleflg":true,"items":[{"title":"我的待办","routeName":"myTasks","icon":"icon-clock","bgColor":"#FFB400","iconfontcolor":"#fafafa","module":"bpm"},{"title":"新建流程","routeName":"mySolutions","icon":"icon-addsupply","bgColor":"#ea5570","iconfontcolor":"#fafafa","module":"bpm"},{"title":"我的消息","routeName":"news","icon":"icon-addsupply","bgColor":"#ea5570","iconfontcolor":"#fafafa","module":"bpm"},{"title":"我的流程","routeName":"myInst","icon":"icon-order","bgColor":"#7e7cf9","iconfontcolor":"#fafafa","module":"bpm"},{"title":"我的已办","routeName":"HasHandle","icon":"icon-caogao","bgColor":"#05c392","iconfontcolor":"#fafafa","module":"bpm"},{"title":"我的转办","routeName":"ReceiveTask","icon":"icon-order","bgColor":"#7e7cf9","iconfontcolor":"#fafafa","module":"bpm"},{"title":"转出代办","routeName":"TransOutTask","icon":"icon-addsupply","iconfontcolor":"#fafafa","bgColor":"#ea5570","module":"bpm"}]}]',         1,         '0',         '1',         '1',         to_date('2020-5-26 8:35:35','yyyy-MM-dd HH24:mi:ss'),         '1',         to_date('2021-9-26 17:11:33','yyyy-MM-dd HH24:mi:ss'),         'YES',         '',         '');


INSERT INTO ins_remind_def(ID_,SUBJECT_,URL_,TYPE_,SETTING_,DESCRIPTION_,SN_,ENABLED_,ICON_,DS_NAME_,DS_ALIAS_,TENANT_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_,CREATE_DEP_ID_)
         values(         '1394232369885880322',         '待办通知',         '/webApp/home/MyBpmTaskList',         'function',         'portalScript.getMyTaskCount()',         '你有[count]待办',         1,         'YES',         'smile',         '',         '',         '1',         '1',         to_date('2021-5-17 18:04:35','yyyy-MM-dd HH24:mi:ss'),         '1',         to_date('2021-6-29 14:57:42','yyyy-MM-dd HH24:mi:ss'),         '');

INSERT INTO ins_remind_def(ID_,SUBJECT_,URL_,TYPE_,SETTING_,DESCRIPTION_,SN_,ENABLED_,ICON_,DS_NAME_,DS_ALIAS_,TENANT_ID_,CREATE_BY_,CREATE_TIME_,UPDATE_BY_,UPDATE_TIME_,CREATE_DEP_ID_)
         values(         '1399568779375443970',         '表单设计统计',         '/form/home/FormPcList',         'sql',         'String sql="select count(*) from form_pc where create_by_=''[USERID]''";
return sql;',         '你的表单设计[count]条。',         1,         'YES',         'iconxinwen',         '',         'form',         '1',         '1',         to_date('2021-6-1 11:29:34','yyyy-MM-dd HH24:mi:ss'),         '1',         to_date('2021-6-24 10:48:42','yyyy-MM-dd HH24:mi:ss'),         '1385123428631506945');


commit;