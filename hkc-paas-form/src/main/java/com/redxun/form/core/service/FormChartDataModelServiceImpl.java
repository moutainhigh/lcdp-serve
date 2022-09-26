
package com.redxun.form.core.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.DateUtils;
import com.redxun.common.tool.StringUtils;
import com.redxun.db.CommonDao;
import com.redxun.form.core.entity.FormChartDataModel;
import com.redxun.form.core.mapper.FormChartDataModelMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
* [图表数据模型]业务服务类
*/
@Service
public class FormChartDataModelServiceImpl extends SuperServiceImpl<FormChartDataModelMapper, FormChartDataModel> implements BaseService<FormChartDataModel> {

    @Resource
    private FormChartDataModelMapper formChartDataModelMapper;
    @Resource
    CommonDao commonDao;
    @Resource
    private PermissionSqlService permissionSqlService;

    @Override
    public BaseDao<FormChartDataModel> getRepository() {
        return formChartDataModelMapper;
    }

    /**
     * 根据配置获取数据
     * @param config
     * @return
     */
    public JSONObject getData(JSONObject config) {
        JSONObject jsonObject=new JSONObject();
        String dataModelId = config.getString("dataModel");
        JSONArray dimensions = config.getJSONArray("dimensions");
        JSONArray measures = config.getJSONArray("measures");
        JSONArray colorLegends = config.getJSONArray("colorLegends");
        //发布事件的参数
        JSONObject params = config.getJSONObject("params");
        //图表本身的过滤条件
        JSONArray filterParams = config.getJSONArray("filterParams");

        FormChartDataModel chartDataModel = formChartDataModelMapper.selectById(dataModelId);
        if(measures.size()==0){
            return new JSONObject();
        }
        //获取sql
        String sql = getSql(dimensions, measures, colorLegends, chartDataModel,params,filterParams);
        if(StringUtils.isEmpty(sql)){
            return new JSONObject();
        }
        //根据sql查询数据
        JSONObject dataSource = JSONObject.parseObject(chartDataModel.getDataSource());
        List data=new ArrayList();
        if(BeanUtil.isNotEmpty(dataSource)){
            data = commonDao.query(dataSource.getString("value"),sql);
        }else {
            data = commonDao.query(sql);
        }
        //获取维度的渲染值
        data=getRenderValue(dimensions,data);
        jsonObject.put("sql",sql);
        jsonObject.put("data",data);
        return jsonObject;
    }

    /**
     * 获取sql
     * @param dimensions 维度
     * @param measures  度量
     * @param colorLegends  颜色图例
     * @param chartDataModel 数据模型
     * @param params 条件参数
     * @return
     */
    private String getSql(JSONArray dimensions,JSONArray measures,JSONArray colorLegends,FormChartDataModel chartDataModel,JSONObject params,JSONArray filterParams) {

        String tableStr = chartDataModel.getTables();
        JSONArray tables = JSONObject.parseArray(tableStr);
        String sqlMode = chartDataModel.getSqlMode();
        String modeType = chartDataModel.getType();
        if("sql".equals(modeType) && StringUtils.isEmpty(sqlMode)){
            return "";
        }

        String sql="SELECT ";
        String groupBy=" GROUP BY ";

        List<String> tableList=new ArrayList<>();

        //维度
        for (int i = 0; i < dimensions.size(); i++) {
            JSONObject dimension = dimensions.getJSONObject(i);
            sql+=dimension.getString("tableName")+"."+dimension.getString("fieldName")+
                    " AS `"+dimension.getString("tableName")+"_"+dimension.getString("fieldName")+"`,";
            groupBy+=dimension.getString("tableName")+"."+dimension.getString("fieldName");
            if(i!=dimensions.size()-1 || colorLegends.size()>0){
                groupBy+=",";
            }
            tableList.add(dimension.getString("tableName"));
        }

        //颜色图例
        for (int i = 0; i < colorLegends.size(); i++) {
            JSONObject colorLegend = colorLegends.getJSONObject(i);
            sql+=colorLegend.getString("tableName")+"."+colorLegend.getString("fieldName")+
                    " AS `"+colorLegend.getString("tableName")+"_"+colorLegend.getString("fieldName")+"`,";
            groupBy+=colorLegend.getString("tableName")+"."+colorLegend.getString("fieldName");
            if(i!=colorLegends.size()-1){
                groupBy+=",";
            }
            tableList.add(colorLegend.getString("tableName"));
        }

        //度量
        for (int i = 0; i < measures.size(); i++) {
            JSONObject measure = measures.getJSONObject(i);
            String aggregate=measure.getString("aggregate");
            //聚合方式 默认求和
            if(StringUtils.isEmpty(aggregate)){
                aggregate="SUM";
            }
            sql+=aggregate+"("+measure.getString("tableName")+"."+measure.getString("fieldName")+")" +
                    " AS `"+measure.getString("tableName")+"_"+measure.getString("fieldName")+"`";
            if(i!=measures.size()-1){
                sql+=",";
            }
            tableList.add(measure.getString("tableName"));
        }

        //去重
        tableList=tableList.stream().distinct().collect(Collectors.toList());

        sql+=" FROM ";

        if("sql".equals(modeType)){
            JSONObject sqlModeObj = JSONObject.parseObject(sqlMode);
            String sqlAlias=sqlModeObj.getString("sqlAlias");
            String sql_= sqlModeObj.getString("sql");
            sql+="("+sql_+") "+sqlAlias;
        }else {
            //获取选择的表 除去主表
            JSONArray selectTables=new JSONArray();
            //主表
            String mainTable="";
            if(tableList.size()>1){
                for (int i = 0; i < tableList.size(); i++) {
                    for (int j = 0; j < tables.size(); j++) {
                        String tableName = tables.getJSONObject(j).getString("key");
                        if(tableList.get(i).equals(tableName)){
                            JSONObject join = tables.getJSONObject(j).getJSONObject("join");
                            if(join.size()>0){
                                selectTables.add(tables.getJSONObject(j));
                            }else {
                                mainTable=tableName;
                            }
                        }
                    }
                }
                //拼接主表
                sql+=mainTable;

                for (int i = 0; i < selectTables.size(); i++) {
                    String key = selectTables.getJSONObject(i).getString("key");
                    JSONObject join = selectTables.getJSONObject(i).getJSONObject("join");
                    String type = join.getString("type");
                    String tableName = join.getString("tableName");
                    JSONArray on = join.getJSONArray("on");
                    sql+=" "+type+" JOIN " +key+" ON ";
                    for (int j = 0; j < on.size(); j++) {
                        if(j!=0){
                            sql+="AND ";
                        }
                        JSONObject onObj = on.getJSONObject(j);
                        String rightField = onObj.getString("rightField");
                        String leftField = onObj.getString("leftField");
                        sql+=tableName+"."+leftField+" = "+key+"."+rightField+" ";
                    }
                }
            }else {
                sql+=tableList.get(0);
            }
        }


        //数据模型配置的参数
        String modelConfig = chartDataModel.getModelConfig();
        //拼接条件参数
        sql=setParams(sql,modelConfig,params,filterParams);

        if(dimensions.size()>0){
            sql+=groupBy;
        }
        return sql;
    }

    /**
     * 拼接条件参数
     * @param sql
     * @param modelConfig
     * @param params
     * @param filterParams
     * @return
     */
    private String setParams(String sql,String modelConfig,JSONObject params,JSONArray filterParams){
        String whereSql=" WHERE ";
        Map<String, String> operateMap = FormChartDataModel.operateMap;

        JSONObject configJson = JSONObject.parseObject(modelConfig);
        JSONArray paramsArr = configJson.getJSONArray("params");

        if(params.size()>0){
            for (int i = 0; i < paramsArr.size(); i++) {
                JSONObject param = paramsArr.getJSONObject(i);
                String tableName = param.getString("tableName");
                JSONArray fields = param.getJSONArray("fields");
                if(fields.size()>0){
                    for (int j = 0; j < fields.size(); j++) {
                        if(j!=0){
                            whereSql+="AND ";
                        }
                        JSONObject field = fields.getJSONObject(j);
                        String fieldName = field.getString("fieldName");
                        String op = field.getString("comparison");
                        whereSql+=tableName+"."+fieldName+operateMap.get(op);
                        String value = params.getString(tableName + "." + fieldName);
                        if(value==null){
                            value="";
                        }
                        //模糊查询
                        if("LK".equals(op)){
                            whereSql+="'%"+value+"%' ";
                        }
                        //左模糊查询
                        else if("LEK".equals(op)){
                            whereSql+="'%"+value+"' ";
                        }
                        //右模糊查询
                        else if("RIK".equals(op)){
                            whereSql+="'"+value+"%' ";
                        }else {
                            whereSql+="'"+value+"' ";
                        }
                    }
                    sql+=whereSql;
                }

            }
        }


        //图表本身的过滤的参数
        if(filterParams.size()>0){
            for (int i = 0; i < filterParams.size(); i++) {
                JSONObject filterParam = filterParams.getJSONObject(i);
                String key = filterParam.getString("key");
                String value = filterParam.getString("value");
                String controlType = filterParam.getString("controlType");
                String opType = filterParam.getString("opType");
                if(i!=0){
                    whereSql+="AND ";
                }
                //时间范围
                if("rangepicker".equals(controlType)){
                    JSONArray val = JSONArray.parseArray(value);
                    whereSql+=key+" BETWEEN '"+val.get(0)+"' AND '"+ val.get(1)+"' ";
                }else {
                    String operate = operateMap.get(opType);
                    whereSql+=key+operate;
                    //模糊查询
                    if("LK".equals(opType)){
                        whereSql+="'%"+value+"%' ";
                    }
                    //左模糊查询
                    else if("LEK".equals(opType)){
                        whereSql+="'%"+value+"' ";
                    }
                    //右模糊查询
                    else if("RIK".equals(opType)){
                        whereSql+="'"+value+"%' ";
                    }else {
                        whereSql+="'"+value+"' ";
                    }
                }
            }
            sql+=whereSql;
        }
        return sql;
    }

    /**
     * 根据维度获取渲染值
     * @param dimensions
     * @param data
     * @return
     */
    private List getRenderValue(JSONArray dimensions,List data){
        for (int i = 0; i < dimensions.size(); i++) {
            JSONObject dimension = dimensions.getJSONObject(i);
            String tableName = dimension.getString("tableName");
            String fieldName = dimension.getString("fieldName");
            JSONObject sqlSetting = dimension.getJSONObject("sqlSetting");
            if(sqlSetting!=null){
                String sql = sqlSetting.getString("sql");
                if(StringUtils.isEmpty(sql)){
                    return data;
                }
                String whereField = sqlSetting.getString("whereField");
                for (int j = 0; j < data.size(); j++) {
                    Map dataMap = (Map) data.get(j);
                    String whereValue= (String) dataMap.get(tableName + "_" + whereField);
                    String value=getValueBySqlSetting(whereValue,sqlSetting);
                    JSONObject newValue=new JSONObject();
                    newValue.put("oldValue",whereValue);
                    newValue.put("newValue",value);
                    dataMap.put(tableName + "_" + whereField,newValue);
                }
            }
            //格式化日期类型数据
            String dataType = dimension.getString("dataType");
            String dataFormat = dimension.getString("dataFormat");
            if("date".equals(dataType) && StringUtils.isNotEmpty(dataFormat)){
                SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.switchFormat(dataFormat));
                for (int j = 0; j < data.size(); j++) {
                    Map dataMap = (Map) data.get(j);
                    Date date =null;
                    Object val = dataMap.get(tableName + "_" + fieldName);
                    if(val instanceof LocalDateTime){
                        date = DateUtils.localDateTimeToDate((LocalDateTime) val);
                    }else{
                        date = (Date) val;
                    }
                    String newValue = sdf.format(date);
                    dataMap.put(tableName + "_" + fieldName,newValue);
                }
            }
        }
        return data;
    }

    /**
     * 根据sql配置获取数据
     * @param whereValue
     * @param sqlSetting
     * @return
     */
    public String getValueBySqlSetting(String whereValue, JSONObject sqlSetting) {
        JsonResult result=new JsonResult();
        String value="";
        try {
            String sql = sqlSetting.getString("sql");
            String dataSource = sqlSetting.getString("dataSource");
            String whereField = sqlSetting.getString("whereField");
            String renderingField = sqlSetting.getString("renderingField");
            String newSql = permissionSqlService.insertWhereSql(sql, whereField+"='"+whereValue+"'");
            List data=new ArrayList();
            //是否配置了数据源
            if(StringUtils.isNotEmpty(dataSource)){
                data= commonDao.query(dataSource,newSql);
            }else {
                data= commonDao.query(newSql);
            }
            for (int i = 0; i < data.size(); i++) {
                Map<String,String> map = (Map<String, String>) data.get(i);
                value = map.get(renderingField);
            }
        } catch (Exception e) {
            e.printStackTrace();
            value="";
            result.setMessage(e.getMessage());
        }
        return value;
    }
}
