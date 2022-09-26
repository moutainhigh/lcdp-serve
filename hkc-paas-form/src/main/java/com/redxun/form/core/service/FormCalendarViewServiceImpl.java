
package com.redxun.form.core.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.GridHeader;
import com.redxun.common.base.entity.IUser;
import com.redxun.common.base.search.FieldLogic;
import com.redxun.common.base.search.QueryFilter;
import com.redxun.common.base.search.QueryParam;
import com.redxun.common.base.search.WhereParam;
import com.redxun.common.engine.FtlEngine;
import com.redxun.common.script.GroovyEngine;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.DateUtils;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ContextUtil;
import com.redxun.common.utils.FileUtil;
import com.redxun.datasource.DataSourceContextHolder;
import com.redxun.db.CommonDao;
import com.redxun.db.DbUtil;
import com.redxun.form.bo.entity.FormBoEntity;
import com.redxun.form.core.entity.FormBoPmt;
import com.redxun.form.core.entity.FormCalendarView;
import com.redxun.form.core.mapper.FormCalendarViewMapper;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


/**
* [表单日历视图]业务服务类
*/
@Service
public class FormCalendarViewServiceImpl extends SuperServiceImpl<FormCalendarViewMapper, FormCalendarView> implements BaseService<FormCalendarView> {

    @Resource
    private FormCalendarViewMapper formCalendarViewMapper;

    @Resource
    FtlEngine freemarkEngine;
    @Resource
    GroovyEngine groovyEngine;
    @Resource
    CommonDao commonDao;
    @Resource
    PermissionSqlService permissionSqlService;
    @Resource
    FormBoPmtServiceImpl formBoPmtService;

    private final String FREEMARKER_="freeMarker";
    private final String GROOVY_="groovy";
    private final String FORMAT_="yyyy-MM-dd HH:mm";

    @Override
    public BaseDao<FormCalendarView> getRepository() {
        return formCalendarViewMapper;
    }


    public Map<String, Object> getParams(HttpServletRequest request) {
        Map<String, Object> params = getParameterValueMap(request, false);
        IUser curUser = ContextUtil.getCurrentUser();
        //加上上下文的Context变量
        if (curUser != null) {
            params.put(FormBoEntity.FIELD_CREATE_BY, curUser.getUserId());
            params.put(FormBoEntity.FIELD_TENANT, curUser.getTenantId());
            params.put("DEP_ID_", curUser.getDeptId());
        }
        return params;
    }
    /**
     * 获取上下文参数。
     *
     * @param request
     * @param remainArray
     * @return
     */
    public static Map<String, Object> getParameterValueMap(HttpServletRequest request, boolean remainArray) {
        Map<String, Object> map = new HashMap<>();
        Enumeration params = request.getParameterNames();
        while (params.hasMoreElements()) {
            String key = params.nextElement().toString();
            String[] values = request.getParameterValues(key);
            if (values == null) {
                continue;
            }
            if (values.length == 1) {
                String tmpValue = values[0];
                if (tmpValue == null) {
                    continue;
                }
                tmpValue = tmpValue.trim();
                if (tmpValue.equals("")) {
                    continue;
                }
                map.put(key, tmpValue);
            } else {
                String rtn = getByAry(values);
                if (rtn.length() > 0) {
                    if (remainArray) {
                        map.put(key, rtn.split(","));
                    } else {
                        map.put(key, rtn);
                    }
                }
            }
        }
        return map;
    }

    private static String getByAry(String[] aryTmp) {
        String rtn = "";

        for (int i = 0; i < aryTmp.length; ++i) {
            String str = aryTmp[i].trim();
            if (!str.equals("")) {
                rtn = rtn + str + ",";
            }
        }

        if (rtn.length() > 0) {
            rtn = rtn.substring(0, rtn.length() - 1);
        }

        return rtn;
    }

    public void getColumns(HttpServletRequest request, FormCalendarView formCalendarView) throws Exception {
        Map<String, Object> params = getParams(request);
        String newSql = parseFreeMarkSql(formCalendarView.getUseCondSql(),formCalendarView.getSql(), params );
        newSql = DbUtil.preHandleSql(newSql);

        List<GridHeader> headers = DbUtil.getGridHeader(newSql);
        if(BeanUtil.isEmpty(headers)){
            return;
        }
        formCalendarView.setFieldColumns(headers);
    }

    private String parseFreeMarkSql(String useCondSql,String sql,Map<String,Object> params) throws Exception{
        String newSql="";
        Pattern pattern = Pattern.compile("#\\{([\\w]+)\\}");
        Matcher matcher = pattern.matcher(sql);
        while(matcher.find()) {
            String key = matcher.group(1);
            sql=sql.replace("#{"+key+"}","<#noparse>#{w."+key+"}</#noparse>");
        }
        if (FREEMARKER_.equals(useCondSql)) {
            newSql = freemarkEngine.parseByStringTemplate(params, sql);
        } else if(GROOVY_.equals(useCondSql)) {
            sql = freemarkEngine.parseByStringTemplate(params, sql);
            newSql = (String) groovyEngine.executeScripts(sql, params);
        }
        return newSql;
    }

    /**
     * 判断是否存在。
     *
     * @param formCalendarView
     * @return
     */
    public boolean isExist(FormCalendarView formCalendarView) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("KEY_", formCalendarView.getKey());
        if (StringUtils.isNotEmpty(formCalendarView.getId())) {
            wrapper.ne("ID_", formCalendarView.getId());
        }
        Integer rtn = formCalendarViewMapper.selectCount(wrapper);
        return rtn > 0;
    }

    /**
     * 根据key获取日历视图
     * @param key
     * @return
     */
    public FormCalendarView getByKey(String key){
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("KEY_", key);
        return formCalendarViewMapper.selectOne(wrapper);
    }

    /**
     * 根据日历视图获取数据
     * @param formCalendarView
     * @param request
     * @param paramStr 参数(当有参数时会根据参数的配置获取对应的数据)
     * @return
     */
    public JSONObject getData(FormCalendarView formCalendarView ,HttpServletRequest request,String paramStr,FormBoPmt formBoPmt) {
        JSONObject dataObj=new JSONObject();
        try {
            //设置数据源
            String dbAlias = formCalendarView.getDbAlias();
            DataSourceContextHolder.setDataSource(dbAlias);
            //根据配置获取数据
            JSONObject columnConf = formCalendarView.getColumnConf();
            JSONObject monthConf = columnConf.getJSONObject("monthConf");
            JSONObject weekConf = columnConf.getJSONObject("weekConf");
            JSONObject dayConf = columnConf.getJSONObject("dayConf");
            if(StringUtils.isEmpty(formCalendarView.getSql())){
                return dataObj;
            }
            Map<String, Object> params = getParams(request);
            String sql = parseFreeMarkSql(formCalendarView.getUseCondSql(),formCalendarView.getSql(), params);
            if(BeanUtil.isNotEmpty(params.get("menuId"))){
                String permissionSql=permissionSqlService.parsePermissionSql(formCalendarView.getId(),(String) params.get("menuId"),null);
                sql=dealSql(sql, permissionSql);
            }
            if(StringUtils.isNotEmpty(paramStr)){
                JSONObject paramsObject = JSONObject.parseObject(paramStr);
                String day = paramsObject.getString("day");
                JSONArray months  = paramsObject.getJSONArray("months");
                JSONArray weeks = paramsObject.getJSONArray("weeks");
                if(BeanUtil.isNotEmpty(months) || BeanUtil.isNotEmpty(weeks) || BeanUtil.isNotEmpty(day)){
                    if(BeanUtil.isNotEmpty(months)){
                        List monthData = getMonthData(sql, monthConf, params,months,columnConf,formBoPmt);
                        dataObj.put("monthData",monthData);
                    }
                    if(BeanUtil.isNotEmpty(weeks)){
                        List weekData = getWeekData(sql, weekConf, params,weeks,columnConf);
                        dataObj.put("weekData",weekData);
                    }
                    if(StringUtils.isNotEmpty(day)){
                        List dayData = getDayData(sql, dayConf, params,day,columnConf);
                        dataObj.put("dayData",dayData);
                    }
                    return dataObj;
                }
            }
            if(monthConf.getBoolean("show")){
                List monthData = getMonthData(sql, monthConf, params,null,columnConf,formBoPmt);
                dataObj.put("monthData",monthData);
            }
            if(weekConf.getBoolean("show") ){
                List weekData = getWeekData(sql, weekConf, params,null,columnConf);
                dataObj.put("weekData",weekData);
            }
            if(dayConf.getBoolean("show")){
                List dayData = getDayData(sql, dayConf, params,"",columnConf);
                dataObj.put("dayData",dayData);
            }
        }catch (Exception ex){
            DataSourceContextHolder.setDefaultDataSource();
        }finally {
            DataSourceContextHolder.setDefaultDataSource();
        }
        return dataObj;
    }

    /**
     * 获取月份的数据
     * @param monthConf
     * @param monthDays
     */
    public List<Map<String,Object>> getMonthData(String sql,JSONObject monthConf,Map<String, Object> params,
                                                 JSONArray monthDays,JSONObject columnConf,FormBoPmt formBoPmt) throws Exception {
        List<Map<String,Object>> monthData=new ArrayList();
        int year=DateUtils.getCurYear();
        int month=DateUtils.getCurMonth();
        QueryFilter queryFilter=new QueryFilter();
        SimpleDateFormat format= new SimpleDateFormat("yyyy-MM-dd");
        FieldLogic fieldLogic=new FieldLogic();
        fieldLogic.setLogic(FieldLogic.FieldLogic_OR);
        Boolean timeQuantum = columnConf.getBoolean("timeQuantum");
        String start = "";
        String end ="";
        String startField = columnConf.getString("startField");
        String endField = columnConf.getString("endField");
        List<WhereParam> whereParams=new ArrayList<>();
        if(BeanUtil.isNotEmpty(monthDays)){
            start= (String) monthDays.get(0);
            end= (String) monthDays.get(1);
        }else {
            //月开始
            Calendar startCalendar=Calendar.getInstance();
            startCalendar.set(year, month, 1);
            start = format.format(startCalendar.getTime());
            //月结束
            Calendar endCalendar=Calendar.getInstance();
            endCalendar.set(year, month+1, 1);//这里先设置要获取月份的下月的第一天
            endCalendar.add(Calendar.DATE, -1);//这里将日期值减去一天，从而获取到要求的月份最后一天
            end = format.format(endCalendar.getTime());
        }
        if(!timeQuantum){
            QueryFilter filter=new QueryFilter();
            if(StringUtils.isNotEmpty(columnConf.getString("startField"))){
                QueryParam startParam= QueryFilter.getQueryParams("Q_"+columnConf.getString("startField")+"_D_GE",start+" 00:00");
                QueryParam endParam= QueryFilter.getQueryParams("Q_"+columnConf.getString("startField")+"_D_LE",end+" 23:59");
                filter.addQueryParam(startParam);
                filter.addQueryParam(endParam);
            }
            //查询数据
            monthData = getDataBySql(sql, filter);
        }else {
            if(StringUtils.isNotEmpty(columnConf.getString("startField")) && StringUtils.isNotEmpty(columnConf.getString("endField"))){
                params.put(startField,start);
                params.put(endField,end);
                //大于等于开始 小于等于结束
                FieldLogic fieldLogic1 = buildFieldLogic(FieldLogic.FieldLogic_AND, startField, start, endField, end, QueryParam.OP_GREAT_EQUAL, QueryParam.OP_LESS_EQUAL);
                whereParams.add(fieldLogic1);
                //大于等于开始 大于等于结束
                FieldLogic fieldLogic2 = buildFieldLogic(FieldLogic.FieldLogic_AND, startField, start, endField, end, QueryParam.OP_GREAT_EQUAL, QueryParam.OP_GREAT_EQUAL);
                whereParams.add(fieldLogic2);
                //小于等于开始 小于等于结束
                FieldLogic fieldLogic3 = buildFieldLogic(FieldLogic.FieldLogic_AND, startField, start, endField, end, QueryParam.OP_LESS_EQUAL, QueryParam.OP_LESS_EQUAL);
                whereParams.add(fieldLogic3);
                //小于等于开始 大于等于结束
                FieldLogic fieldLogic4 = buildFieldLogic(FieldLogic.FieldLogic_AND, startField, start, endField, end, QueryParam.OP_LESS_EQUAL, QueryParam.OP_GREAT_EQUAL);
                whereParams.add(fieldLogic4);
                fieldLogic.setWhereParams(whereParams);
            }
            queryFilter.setFieldLogic(fieldLogic);
            queryFilter.setParams(params);
            //查询数据
            monthData = getDataBySql(sql, queryFilter);
        }
        handDate(monthData,FORMAT_);
        JSONArray pmtFields=new JSONArray();
        if(BeanUtil.isNotEmpty(formBoPmt) && StringUtils.isNotEmpty(formBoPmt.getFields())){
            pmtFields = JSON.parseArray(formBoPmt.getFields());
        }
        for (Map<String, Object> data : monthData) {
            Map<String, Object> newData=new HashMap<>();
            //计算字段权限
            if(pmtFields.size()>0){
                for (int i = 0; i < pmtFields.size(); i++) {
                    JSONObject jsonObject = pmtFields.getJSONObject(i);
                    String field = jsonObject.getString("field");
                    newData.put(field,data.get(field));
                }
            }else {
                newData=data;
            }
            String monthTitle = freemarkEngine.parseByStringTemplate(newData, monthConf.getString("monthTitle"));
            data.put("monthTitle",monthTitle);
        }
        return monthData;
    }
    /**
     * 获取周的数据
     * @param weekConf
     * @param weeks 参数
     */
    public List<Map<String,Object>> getWeekData(String sql,JSONObject weekConf,Map<String, Object> params,JSONArray weeks,JSONObject columnConf) throws Exception {
        List<Map<String,Object>> weekData=new ArrayList();
        String start="";
        String end="";
        if(BeanUtil.isNotEmpty(weeks)){
            start= (String) weeks.get(0);
            end= (String) weeks.get(1);
        }else {
            Date date = new Date();
            Map<String, Long> map= getTimeInterval(date);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            start = simpleDateFormat.format(map.get("start"));
            end = simpleDateFormat.format(map.get("end"));
        }
        Boolean timeQuantum = columnConf.getBoolean("timeQuantum");
        String startField = columnConf.getString("startField");
        String endField = columnConf.getString("endField");
        if(timeQuantum){
            QueryFilter filter=new QueryFilter();
            if(StringUtils.isNotEmpty(columnConf.getString("startField"))){
                QueryParam startParam= QueryFilter.getQueryParams("Q_"+columnConf.getString("startField")+"_D_GE",start+" 00:00");
                QueryParam endParam= QueryFilter.getQueryParams("Q_"+columnConf.getString("startField")+"_D_LE",end+" 23:59");
                filter.addQueryParam(startParam);
                filter.addQueryParam(endParam);
            }
            //查询数据
            weekData = getDataBySql(sql, filter);
        }else {
            QueryFilter queryFilter=new QueryFilter();
            FieldLogic fieldLogic=new FieldLogic();
            fieldLogic.setLogic(FieldLogic.FieldLogic_OR);
            if(StringUtils.isNotEmpty(startField) && StringUtils.isNotEmpty(endField)){
                params.put(startField,start);
                params.put(endField,end);
                List<WhereParam> whereParams=new ArrayList<>();
                //大于等于开始 小于等于结束
                FieldLogic fieldLogic1 = buildFieldLogic(FieldLogic.FieldLogic_AND, startField, start, endField, end, QueryParam.OP_GREAT_EQUAL, QueryParam.OP_LESS_EQUAL);
                whereParams.add(fieldLogic1);
                //大于等于开始 大于等于结束
                FieldLogic fieldLogic2 = buildFieldLogic(FieldLogic.FieldLogic_AND, startField, start, endField, end, QueryParam.OP_GREAT_EQUAL, QueryParam.OP_GREAT_EQUAL);
                whereParams.add(fieldLogic2);
                //小于等于开始 小于等于结束
                FieldLogic fieldLogic3 = buildFieldLogic(FieldLogic.FieldLogic_AND, startField, start, endField, end, QueryParam.OP_LESS_EQUAL, QueryParam.OP_LESS_EQUAL);
                whereParams.add(fieldLogic3);
                //小于等于开始 大于等于结束
                FieldLogic fieldLogic4 = buildFieldLogic(FieldLogic.FieldLogic_AND, startField, start, endField, end, QueryParam.OP_LESS_EQUAL, QueryParam.OP_GREAT_EQUAL);
                whereParams.add(fieldLogic4);
                fieldLogic.setWhereParams(whereParams);
            }
            queryFilter.setFieldLogic(fieldLogic);
            queryFilter.setParams(params);
            //查询数据
            weekData = getDataBySql(sql, queryFilter);
        }
        handDate(weekData,FORMAT_);
        return weekData;
    }
    /**
     * 根据当前日期获得所在周的日期区间（周一和周日日期）
     */
    public static Map<String, Long> getTimeInterval(Date date){
        Map<String, Long> map = new HashMap<>();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        // 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
        if(1 == dayWeek){
            cal.add(Calendar.DAY_OF_MONTH,-1);
        }
        // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        // 获得当前日期是一个星期的第几天
        int day = cal.get(Calendar.DAY_OF_WEEK);
        // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);
        Long imptimeBegin = cal.getTime().getTime();
        cal.add(Calendar.DATE,6);
        Long imptimeEnd = cal.getTime().getTime();
        map.put("start", imptimeBegin);
        map.put("end", imptimeEnd);
        return map;
    }
    /**
     * 获取日的数据
     * @param dayConf
     * @param day 参数
     */
    public List<Map<String,Object>> getDayData(String sql,JSONObject dayConf,Map<String, Object> params,String day,JSONObject columnConf) throws Exception {
        List<Map<String,Object>> dayData=new ArrayList();
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = "";
        if(StringUtils.isNotEmpty(day)){
            dateStr=day;
        }else {
            dateStr=simpleDateFormat.format(date);
        }
        Boolean timeQuantum = columnConf.getBoolean("timeQuantum");
        //时间点
        if(!timeQuantum){
            QueryFilter filter=new QueryFilter();
            if(StringUtils.isNotEmpty(columnConf.getString("startField"))){
                QueryParam startParam= QueryFilter.getQueryParams("Q_"+columnConf.getString("startField")+"_D_GE",dateStr+" 00:00");
                QueryParam endParam= QueryFilter.getQueryParams("Q_"+columnConf.getString("startField")+"_D_LE",dateStr+" 23:59");
                filter.addQueryParam(startParam);
                filter.addQueryParam(endParam);
            }
            //查询数据
            dayData = getDataBySql(sql, filter);
        }else{ //时间段
            QueryFilter queryFilter=new QueryFilter();
            FieldLogic fieldLogic=new FieldLogic();
            fieldLogic.setLogic(FieldLogic.FieldLogic_OR);
            if(StringUtils.isNotEmpty(columnConf.getString("startField")) && StringUtils.isNotEmpty(columnConf.getString("endField"))){
                String startField = columnConf.getString("startField");
                String endField = columnConf.getString("endField");
                String start=dateStr+" 00:00";
                String end=dateStr+" 23:59";
                params.put(startField,start);
                params.put(endField,end);
                List<WhereParam> whereParams=new ArrayList<>();
                //大于等于开始 小于等于结束
                FieldLogic fieldLogic1 = buildFieldLogic(FieldLogic.FieldLogic_AND, startField, start, endField, end, QueryParam.OP_GREAT_EQUAL, QueryParam.OP_LESS_EQUAL);
                whereParams.add(fieldLogic1);
                //大于等于开始 大于等于结束
                FieldLogic fieldLogic2 = buildFieldLogic(FieldLogic.FieldLogic_AND, startField, start, endField, end, QueryParam.OP_GREAT_EQUAL, QueryParam.OP_GREAT_EQUAL);
                whereParams.add(fieldLogic2);
                //小于等于开始 小于等于结束
                FieldLogic fieldLogic3 = buildFieldLogic(FieldLogic.FieldLogic_AND, startField, start, endField, end, QueryParam.OP_LESS_EQUAL, QueryParam.OP_LESS_EQUAL);
                whereParams.add(fieldLogic3);
                //小于等于开始 大于等于结束
                FieldLogic fieldLogic4 = buildFieldLogic(FieldLogic.FieldLogic_AND, startField, start, endField, end, QueryParam.OP_LESS_EQUAL, QueryParam.OP_GREAT_EQUAL);
                whereParams.add(fieldLogic4);
                fieldLogic.setWhereParams(whereParams);
            }
            queryFilter.setFieldLogic(fieldLogic);
            queryFilter.setParams(params);
            //查询数据
            dayData = getDataBySql(sql, queryFilter);
        }
        handDate(dayData,FORMAT_);
        return dayData;
    }

    private List getDataBySql(String sql, QueryFilter queryFilter) {
        List list = commonDao.queryForList(sql, queryFilter, null);
        return list;
    }

    /**
     * 日期
     * @param data
     * @param format
     */
    private void handDate(List data,String format){
        SimpleDateFormat sdf  = new SimpleDateFormat(format);
        for(Object obj:data){
            Map<String,Object> row=(Map<String,Object>)obj;
            for (Map.Entry<String, Object> ent : row.entrySet()) {
                Object val=ent.getValue();
                if(BeanUtil.isNotEmpty(val) && val instanceof Date){
                    row.put(ent.getKey(),sdf.format(val));
                }
            }
        }
    }

    private FieldLogic buildFieldLogic(String logic,String startField,String startDate,String endField,String endDate,String startOpType,String endOpType){
        FieldLogic fieldLogic=new FieldLogic();
        fieldLogic.setLogic(logic);
        List<WhereParam> whereParams=new ArrayList<>();
        QueryParam startParam=new QueryParam();
        startParam.setFieldName(startField);
        startParam.setOpType(startOpType);
        startParam.setFieldType(QueryParam.FIELD_TYPE_DATE);
        startParam.setValue(startDate);
        whereParams.add(startParam);

        QueryParam endParam=new QueryParam();
        endParam.setFieldName(endField);
        endParam.setOpType(endOpType);
        endParam.setFieldType(QueryParam.FIELD_TYPE_DATE);
        endParam.setValue(endDate);
        whereParams.add(endParam);

        //都是大于等于
        if(QueryParam.OP_GREAT_EQUAL.equals(startOpType) && QueryParam.OP_GREAT_EQUAL.equals(endOpType)){
            QueryParam startParam2=new QueryParam();
            startParam2.setFieldName(startField);
            startParam2.setParamName(startField+"_"+QueryParam.OP_LESS_EQUAL);
            startParam2.setOpType(QueryParam.OP_LESS_EQUAL);
            startParam2.setFieldType(QueryParam.FIELD_TYPE_DATE);
            startParam2.setValue(endDate);
            whereParams.add(startParam2);
        }
        //都是小于等于
        else if(QueryParam.OP_LESS_EQUAL.equals(startOpType) && QueryParam.OP_LESS_EQUAL.equals(endOpType)){
            QueryParam endParam2=new QueryParam();
            endParam2.setFieldName(endField);
            endParam2.setParamName(endField+"_"+QueryParam.OP_GREAT_EQUAL);
            endParam2.setOpType(QueryParam.OP_GREAT_EQUAL);
            endParam2.setFieldType(QueryParam.FIELD_TYPE_DATE);
            endParam2.setValue(startDate);
            whereParams.add(endParam2);
        }
        fieldLogic.setWhereParams(whereParams);
        return fieldLogic;
    }


    private String dealSql(String sql, String permissionSql){
        if(StringUtils.isEmpty(permissionSql) || "()".equals(permissionSql.trim())){
            return sql;
        }
        //表示sql中存在 /*condition*/
        if(sql.indexOf(CommonDao.CONDITION_TAG)!=-1){
            int len=2;
            String[] arySql=sql.split("/\\*CONDITION\\*/");
            String sqlreturn =  arySql[0] + CommonDao.CONDITION_TAG + " and (" +permissionSql +")" ;
            if(arySql.length==len){
                sqlreturn+=" " +arySql[1];
            }
            return  sqlreturn;
        }
        else{
            sql=permissionSqlService.insertWhereSql(sql,permissionSql);
        }
        return  sql;
    }

    /**
     * 获取菜单权限中的按钮
     * @param formCalendarView
     * @param formCalendarView
     * @param menuId
     * @param formBoPmt
     * @return
     */
    public JSONArray getPmtButtons(FormCalendarView formCalendarView, String menuId,FormBoPmt formBoPmt) {
        if(formCalendarView.getButtonConf().size()==0 || BeanUtil.isEmpty(menuId)){
            return formCalendarView.getButtonConf();
        }
        JSONArray buttonConfs = formCalendarView.getButtonConf();
        if(formBoPmt==null){
            return buttonConfs;
        }
        JSONArray buttons=new JSONArray();
        if(StringUtils.isNotEmpty(formBoPmt.getButtons())){
            JSONArray pmtButtons = JSONArray.parseArray(formBoPmt.getButtons());
            for (int i = 0; i < pmtButtons.size(); i++) {
                JSONObject btn = pmtButtons.getJSONObject(i);
                for (int j = 0; j < buttonConfs.size(); j++) {
                    JSONObject btnConf= buttonConfs.getJSONObject(j);
                    if(btn.getString("btnName").equals(btnConf.getString("btnName"))){
                        buttons.add(btnConf);
                        break;
                    }
                }
            }
        }
        return buttons;
    }
}
