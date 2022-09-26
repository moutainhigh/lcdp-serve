package com.redxun.form.core.listener;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.redxun.common.base.entity.SqlModel;
import com.redxun.common.constant.MBoolean;
import com.redxun.common.constant.SysConstant;
import com.redxun.common.engine.FtlEngine;
import com.redxun.common.script.GroovyEngine;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ExceptionUtil;
import com.redxun.datasource.DataSourceContextHolder;
import com.redxun.db.CommonDao;
import com.redxun.dboperator.model.Column;
import com.redxun.form.bo.entity.FormBoEntity;
import com.redxun.form.core.entity.*;
import com.redxun.form.core.service.FormSqlLogServiceImpl;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * [{
	"isMain": "yes",
	"name": "out_stock",
	"comment": "主表-出库单",
	"settings": {
		"opDescp": "wwww",
		"dsName": "",
		"setting": [{
			"condition": "name==''",
			"tableName": "demofield",
			"operator": "new",
			"filterSql": "NAME='{cur.F_name}'",
			"gridData": [{
				"comment": "主键",
				"fieldName": "ID",
				"mapType": "field",
				"mapTypeName": "从字段获取",
				"mapValue": "name"
			}]
		}, {
			"condition": "",
			"tableName": "demofield",
			"operator": "upd",
			"filterSql": "ID='{cur.F_name}'",
			"gridData": [{
				"comment": "主键",
				"fieldName": "ID",
				"mapType": "mainPkId",
				"mapTypeName": "主表主键字段"
			}]
		}]
	}
	}, {
		"name": "out_stock_item",
		"comment": "从表-出库单明细",
		"settings": {
			"opDescp": "demofield",
			"dsName": "",
			"setting": [{
				"condition": "",
				"tableName": "demofield",
				"operator": "new",
				"filterSql": "",
				"gridData": [{
					"comment": "主键",
					"fieldName": "ID",
					"mapType": "srcPkId",
					"mapTypeName": "原主键字段值"
				}]
			}]
		}
	}
	]
 * @author ray
 *
 */
@Slf4j
@Service
public class FormDataListener implements ApplicationListener<DataHolderEvent> {


	@Resource
	private GroovyEngine groovyEngine;
	@Resource
	CommonDao commonDao;
	@Resource
	FtlEngine freemarkEngine;
	@Resource
	FormSqlLogServiceImpl formSqlLogService;
//    @Resource
//    BpmInstDataManager bpmInstDataManager;



	private static String MAIN_CUR="mainCur";
    private static String MAIN_OLD="mainOld";

    private static String CURRENT="cur";
    private static String OLD="old";

    private static String DATA="data";

	private static String ACTION="action";

	private static String OPERATOR="operator";

	private static String CONDITION= "condition";

	private static String MAP_TYPE="mapType";


	
	
//	Logger

	/**
	 * {
		dsName:"",
		opDescp:"",
		setting:[{
		condition:"条件",
		filterSql:"过滤条件",
		operator:"new,upd,del",
		//字段映射
		gridData:[]
		}]	
	}
	 */
	@Override
	public void onApplicationEvent(DataHolderEvent event) {
		DataHolder holder=(DataHolder) event.getSource();
		FormulaSetting formulaSetting=FormulaSettingContext.getFormulaSetting();

		FormulaSettingContext.clearSetting();
		if(formulaSetting==null){
			return;
		}
		try {
			List<FormTableFormula> list=formulaSetting.getFormulaList();
			if(BeanUtil.isNotEmpty(list)){
				handFormula( holder, list,formulaSetting.getExtParams());
			}
		} catch (Exception e) {
			log.error(ExceptionUtil.getExceptionMessage(e));
			throw new RuntimeException(e.getMessage());
		}
		
	}
	
	
	private void handFormula(DataHolder holder, List<FormTableFormula> list, Map<String,Object> extParams) throws TemplateException, IOException{
		String action=holder.getAction();
		for(FormTableFormula formula:list) {
			List<SqlModel> sqlModels = new ArrayList<>();

			if (formula.getAction().indexOf(action) == -1) {
				continue;
			}
			String setting = formula.getFillConf();

			boolean isTest = MBoolean.YES.name().equals(formula.getIsTest());

			JSONArray jsonAry = JSONArray.parseArray(setting);
			Map<String, List<JSONObject>> formulaMap = getSettingMap(jsonAry);

			//处理主表
			List<JSONObject> mains = formulaMap.get("main");

			if (BeanUtil.isNotEmpty(mains)) {
				for (JSONObject json : mains) {
					JSONObject settings = json.getJSONObject("settings");
					JSONArray jsonArray = settings.getJSONArray("setting");
					for (int i = 0; i < jsonArray.size(); i++) {
						JSONObject tableSetting = jsonArray.getJSONObject(i);
						String dsName = tableSetting.getString("dsName");
						handMainSetting(tableSetting, holder, dsName, sqlModels, extParams);
					}
				}
			}


			//处理子表。
			List<JSONObject> subs = formulaMap.get("sub");
			if (BeanUtil.isNotEmpty(subs)) {
				for (JSONObject json : subs) {
					String entName = json.getString("name");
					JSONObject settings = json.getJSONObject("settings");
					JSONArray jsonArray = settings.getJSONArray("setting");
					for (int i = 0; i < jsonArray.size(); i++) {
						JSONObject tableSetting = jsonArray.getJSONObject(i);
						String dsName = tableSetting.getString("dsName");
						handSubSetting(tableSetting, holder, entName, dsName, sqlModels, extParams);
					}
				}
			}

			/**
			 * 一次性执行。
			 */

			for (SqlModel model : sqlModels) {
				try {
					if (StringUtils.isEmpty(model.getDsName())) {
						commonDao.execute(model);
					} else {
						commonDao.execute(model.getDsName(), model);
					}
					log.debug(model.getSql());
					log.debug(JSONObject.toJSONString(model.getParams()));
					if (isTest) {
						FormSqlLog formSqlLog = new FormSqlLog();
						formSqlLog.setType(FormSqlLog.TYPE_FORM_TABLE_FORMULA);
						formSqlLog.setSql(model.getSql());
						formSqlLog.setParams(JSONObject.toJSONString(model.getParams()));
						formSqlLog.setIsSuccess(MBoolean.YES.name());
						formSqlLog.setRemark("--FormDataListener.handFormula is debug :--" + model.getSql());
						formSqlLogService.insert(formSqlLog);
					}
				} catch (Exception e) {
					log.error("--FormDataListener.handFormula is error :--" + e.getMessage());
					if (isTest) {
						FormSqlLog formSqlLog = new FormSqlLog();
						formSqlLog.setType(FormSqlLog.TYPE_FORM_TABLE_FORMULA);
						formSqlLog.setSql(model.getSql());
						formSqlLog.setParams(JSONObject.toJSONString(model.getParams()));
						formSqlLog.setIsSuccess(MBoolean.NO.name());
						formSqlLog.setRemark("--FormDataListener.handFormula is error :--" + e.getMessage());
						formSqlLogService.insert(formSqlLog);
					}
					throw e;
				}
			}
		}
	}
	
	
	/**
	 * {
			"condition": "name==''",
			"tableName": "demofield",
			"operator": "new",
			"filterSql": "NAME='{cur.F_name}'",
			"gridData": [{
				"comment": "主键",
				"fieldName": "ID",
				"mapType": "field",
				"mapTypeName": "从字段获取",
				"mapValue": "name"
			}]
		}
	 * @param tableSetting
	 * @param holder
	 * @param dsName
	 * @throws IOException
	 * @throws TemplateException
	 */
	private void handMainSetting(JSONObject tableSetting, DataHolder holder, String dsName, List<SqlModel> sqlModels, Map<String,Object> extParams) throws TemplateException, IOException{
		String operator=tableSetting.getString(OPERATOR);
		String condition=tableSetting.getString(CONDITION);
		//不是新增 必须填写filterSql 
		boolean needFilter=needFilterSql( tableSetting);
		if(needFilter) {
			return;
		}
		
		
		
		//处理condition
		boolean rtn= handMainCondition(condition,dsName,holder.getAction(),holder,extParams);
		if(!rtn) {
			return;
		}

		String pk="";

		switch(operator){
			case DataHolder.ACTION_NEW:
				pk=handInsert(tableSetting,holder.getCurMain(),holder.getOriginMain(),holder, "",sqlModels,dsName,extParams);
				holder.setNewPk(pk);
				break;
			case DataHolder.ACTION_UPD:
				pk=holder.getCurMain().getString(FormBoEntity.FIELD_PK);
				handUpd( tableSetting, holder.getCurMain(), holder.getOriginMain(), holder,sqlModels,dsName,extParams);
				break;
			case DataHolder.ACTION_DEL:
				pk=holder.getCurMain().getString(FormBoEntity.FIELD_PK);
				handDel( tableSetting,holder.getCurMain(),holder.getOriginMain(),holder,sqlModels,dsName,extParams);
				break;
			default:
				break;
		}
		handBindBpmInstData(   extParams, tableSetting, pk);

	}

    /**
     * 流程实例绑定新表单的主键。
     * @param extParams
     * @param tableSetting
     * @param pk
     */
	private void handBindBpmInstData(Map<String,Object>   extParams, JSONObject tableSetting, String pk){
//        String instId=(String)extParams.get("instId");
//        String bindInst=(String)extParams.get("bindInst");
//        String boDefId=(String)tableSetting.getString("boDefId");
//        if(instId!=null && bindInst!=null && "yes".equals(bindInst) && StringUtils.isNotEmpty(boDefId)){
//            bpmInstDataManager.addBpmInstData(boDefId,pk,instId);
//        }
    }

	private boolean needFilterSql(JSONObject tableSetting){
		String operator=tableSetting.getString(OPERATOR);
		String filterSql=tableSetting.getString("filterSql");
		
		if(! DataHolder.ACTION_NEW.equals( operator) && StringUtils.isEmpty(filterSql)) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * {
			"condition": "name==''",
			"tableName": "demofield",
			"operator": "new",
			"filterSql": "NAME='{cur.F_name}'",
			"gridData": [{
				"comment": "主键",
				"fieldName": "ID",
				"mapType": "field",
				"mapTypeName": "从字段获取",
				"mapValue": "name"
			}]
		}
	 * @param tableSetting
	 * @param jsonObj
	 * @param oldJsonObj
	 * @param dataHolder
	 * @return
	 * @throws IOException 
	 * @throws TemplateException
	 */
	private void handUpd(JSONObject tableSetting, JSONObject jsonObj, JSONObject oldJsonObj, DataHolder dataHolder, List<SqlModel> sqlModels, String dsName, Map<String,Object> extParams) throws TemplateException, IOException{
		String tableName=tableSetting.getString("tableName");
		String filterSql=tableSetting.getString("filterSql");
		JSONArray data=tableSetting.getJSONArray("gridData");
		JSONArray mapper= getJsonAry(data);

		if(BeanUtil.isEmpty(mapper)){
			return;
		}
		
		String sql="update " +tableName +" set ";
		
		String fields="";
		SqlModel sqlModel=new SqlModel();
		for(int i=0;i<mapper.size();i++){
			JSONObject json=mapper.getJSONObject(i);
			String mapType=json.getString(MAP_TYPE);
			ITableFieldValueHandler valHandler=FieldValueHandlerContext.getValueHandler(mapType);
			if(valHandler==null){
				continue;
			}
			String fieldName=json.getString("fieldName");
			String columnType=json.getString("columnType");
			String mapValue=json.getString("mapValue");
			
			Object obj= valHandler.getFieldValue(columnType, dataHolder.getNewPk(), 
					dataHolder, jsonObj, oldJsonObj, mapValue,extParams);

			boolean isParam=valHandler.isParameterize();

			
			String pre=(i==0)? "" :","; 
			if(isParam){
				fields+=pre + fieldName +"=#{"+fieldName +"}";
				sqlModel.addParam(fieldName, obj);
			}
			else{
				if(Column.COLUMN_TYPE_VARCHAR.equals( columnType)){
					fields+=pre + fieldName +"='" + obj.toString() + "'";
				}
				else{
					fields+=pre + fieldName +"=" + obj.toString() ;
				}
			}
		}
		sql+=fields +" where ";
		
		Map<String,Object> params=new HashMap<>(SysConstant.INIT_CAPACITY_16);
		params.put(MAIN_CUR, dataHolder.getCurMain());
		params.put(MAIN_OLD, dataHolder.getOriginMain());
		params.put(CURRENT, jsonObj);
		params.put(OLD, oldJsonObj);
		
		String whereSql=freemarkEngine.parseByStringTemplate(params, filterSql);
		
		sql+=whereSql;
		
		
		
		sqlModel.setSql(sql);
		sqlModel.setDsName(dsName);
		sqlModels.add(sqlModel);
		
		
	}
	
	private JSONArray getJsonAry(JSONArray mapper){
		JSONArray ary=new JSONArray();
		for(int i=0;i<mapper.size();i++){
			JSONObject json=mapper.getJSONObject(i);
			String mapType=json.getString(MAP_TYPE);
			if(StringUtils.isEmpty(mapType) || "none".equals(mapType)) {
				continue;
			}
			ary.add(json);
		}
		return ary;
	}
	
	private void handDel(JSONObject tableSetting, JSONObject jsonObj, JSONObject oldJsonObj, DataHolder dataHolder, List<SqlModel> sqlModels, String dsName, Map<String,Object> extParams) throws TemplateException, IOException{
		String tableName=tableSetting.getString("tableName");
		String filterSql=tableSetting.getString("filterSql");
		
		String sql="delete from " +tableName +" where  ";
		
	 
		
		Map<String,Object> params=new HashMap<>(SysConstant.INIT_CAPACITY_16);
		params.put(CURRENT, jsonObj);
		params.put(MAIN_CUR, dataHolder.getCurMain());


		String whereSql=freemarkEngine.parseByStringTemplate(params, filterSql);
		
		sql+=whereSql;
		
		
		SqlModel model=new SqlModel(sql);
		model.setDsName(dsName);
		sqlModels.add(model);
		
		
	}
	
	private String handInsert(JSONObject tableSetting, JSONObject jsonObj, JSONObject oldJsonObj, DataHolder dataHolder, String newPkId, List<SqlModel> sqlModels, String dsName, Map<String,Object> extParams){
	
		String tableName=tableSetting.getString("tableName");

		String sql="insert into " + tableName +"(";

		SqlModel sqlModel=new SqlModel();
		
		JSONArray data=tableSetting.getJSONArray("gridData");
		
		JSONArray mapper= getJsonAry(data);
		
		String pkVal="";
		//计数器。
		int k=0;

		List<String> fieldList=new ArrayList<>();
		List<String> valList=new ArrayList<>();

		pkVal=handFields(mapper,newPkId,dataHolder,jsonObj,oldJsonObj,extParams,fieldList,valList,sqlModel,true);

		handFields(mapper,newPkId,dataHolder,jsonObj,oldJsonObj,extParams,fieldList,valList,sqlModel,false);

		sql+=StringUtils.join(fieldList) +") values ("+StringUtils.join(valList)+")";

		sqlModel.setSql(sql);
		sqlModel.setDsName(dsName);
		sqlModels.add(sqlModel);
		return pkVal;
	}

	private String handFields(JSONArray mapper,String newPkId,DataHolder dataHolder,JSONObject jsonObj,
							JSONObject  oldJsonObj,Map<String,Object> extParams,
							List<String> fieldList,
							List<String> valList,SqlModel sqlModel,boolean isPkField){
		String pkVal="";
		for(int i=0;i<mapper.size();i++){
			JSONObject json=mapper.getJSONObject(i);
			boolean isPk=json.getBooleanValue("isPk");

			if(isPk!=isPkField){
				continue;
			}



			String mapType=json.getString(MAP_TYPE);
			ITableFieldValueHandler valHandler=FieldValueHandlerContext.getValueHandler(mapType);
			if(valHandler==null){
				continue;
			}
			String fieldName=json.getString("fieldName");
			String columnType=json.getString("columnType");
			String mapValue=json.getString("mapValue");


			Object obj= valHandler.getFieldValue(columnType, newPkId, dataHolder, jsonObj, oldJsonObj, mapValue,extParams);

			if(BeanUtil.isEmpty(obj)){
				continue;
			}
			boolean isParam=valHandler.isParameterize();

			if(isPk){
				pkVal=obj.toString();
			}


			fieldList.add(fieldName);

			if(isParam){
				sqlModel.addParam(fieldName, obj);
				valList.add("#{"+ fieldName +"}" ) ;
			}
			else{
				if(Column.COLUMN_TYPE_VARCHAR.equals( columnType)){
					valList.add( "'" + obj.toString() +"'");
				}
				else{
					valList.add( "'" + obj+"'");
				}
			}

		}
		return pkVal;
	}
	
	private void handSubSetting(JSONObject tableSetting, DataHolder holder, String entName, String dsName, List<SqlModel> sqlModels, Map<String,Object> extParams) throws TemplateException, IOException{
		
		SubDataHolder dataHolder= holder.getSubData(entName);
		if(dataHolder==null){
			return;
		}
		
		//获取子表增加的数据。
		List<JSONObject> addList= dataHolder.getAddList();
		for(JSONObject jsonObj:addList){
			handSubAdd( tableSetting, jsonObj, holder, sqlModels,dsName,extParams);
		}
		
		//获取子表更新的数据
		List<UpdJsonEnt> updList= dataHolder.getUpdList();
		for(UpdJsonEnt jsonEnt:updList){
			handSubUpd(tableSetting, jsonEnt, holder,sqlModels,dsName,extParams);
		}
		
		//获取子表删除的数据。
		List<JSONObject> delList= dataHolder.getDelList();
		for(JSONObject jsonObj:delList){
			handSubDel( tableSetting, jsonObj, holder,sqlModels,dsName,extParams);
		}
	}
	
	
	private void handSubAdd(JSONObject tableSetting, JSONObject jsonObject, DataHolder holder, List<SqlModel> sqlModels, String dsName, Map<String,Object> extParams) throws TemplateException, IOException{
		String operator=tableSetting.getString(OPERATOR);
		String condition=tableSetting.getString(CONDITION);
		//没有过滤条件直接不处理
		boolean needFilter=needFilterSql(tableSetting);
		if(needFilter) {
			return ;
		}
		
		boolean rtn=handNewSubCondition(condition,dsName, holder, jsonObject,extParams);
		
		if(!rtn){
			return ;
		}
		//根据操作类型判断做何种操作。
		handByOperator( operator, tableSetting,jsonObject,null, holder,  sqlModels,dsName,extParams);
		
		
	}
	
	/**
	 * 处理子表数据更新
	 * @param tableSetting
	 * @param jsonObject
	 * @param holder
	 * @param sqlModels
	 * @throws TemplateException
	 * @throws IOException
	 */
	private void handSubUpd(JSONObject tableSetting, UpdJsonEnt jsonObject, DataHolder holder, List<SqlModel> sqlModels, String dsName, Map<String,Object> extParams) throws TemplateException, IOException{
		String operator=tableSetting.getString(OPERATOR);
		String condition=tableSetting.getString(CONDITION);
		//没有过滤条件直接不处理
		boolean needFilter=needFilterSql( tableSetting);
		if(needFilter) {
			return ;
		}
		
		boolean rtn=handUpdSubCondition(condition,dsName, holder, jsonObject,extParams);
		
		if(!rtn) {
			return ;
		}
		
		handByOperator( operator, tableSetting,jsonObject.getCurJson(),jsonObject.getOriginJson(), holder, sqlModels,dsName,extParams);
		
	}
	
	/**
	 * 根据操作类型调用不同的操作。
	 * @param operator
	 * @param tableSetting
	 * @param jsonObj
	 * @param oldJsonObj
	 * @param holder
	 * @param sqlModels
	 * @throws TemplateException
	 * @throws IOException
	 */
	private void handByOperator(String operator, JSONObject tableSetting, JSONObject jsonObj, JSONObject oldJsonObj, DataHolder holder, List<SqlModel> sqlModels, String dsName, Map<String,Object> extParams) throws TemplateException, IOException{
		switch(operator){
			case DataHolder.ACTION_NEW:
				handInsert(tableSetting, jsonObj, null, holder, holder.getNewPk(),sqlModels,dsName,extParams);
				break;
			case DataHolder.ACTION_UPD:
				
				handUpd(tableSetting, jsonObj, oldJsonObj, holder,sqlModels,dsName,extParams);
				break;
			case DataHolder.ACTION_DEL:
				handDel(tableSetting, jsonObj, null, holder,sqlModels,dsName,extParams);
				break;
			default:
				break;
		}
	}
	
	/**
	 * 处理数据删除
	 * @param tableSetting
	 * @param jsonObject
	 * @param holder
	 * @throws IOException 
	 * @throws TemplateException
	 */
	private void handSubDel(JSONObject tableSetting, JSONObject jsonObject, DataHolder holder, List<SqlModel> sqlModels, String dsName, Map<String,Object> extParams) throws TemplateException, IOException{
		String operator=tableSetting.getString(OPERATOR);
		String condition=tableSetting.getString(CONDITION);
		//没有过滤条件直接不处理
		boolean needFilter=needFilterSql( tableSetting);
		if(needFilter) {
			return ;
		}
		
		boolean rtn=handDelSubCondition(condition,dsName, holder, jsonObject,extParams);
		
		if(!rtn) {
			return ;
		}
		
		handByOperator( operator, tableSetting,jsonObject,null, holder,  sqlModels,dsName,extParams);
		
	}
	

	/**
	 * 处理条件是否满足。
	 * @param condition
	 * @param action
	 * @param holder
	 * @return 
	 */
	private boolean handMainCondition(String condition,String dsName,String action,DataHolder holder,Map<String,Object> extParams){
		if(StringUtils.isEmpty(condition)) {
			return true;
		}
		Map<String,Object> params=new HashMap<>(SysConstant.INIT_CAPACITY_16);
		
		if(action.equals(DataHolder.ACTION_UPD)){
			params.put(MAIN_CUR, holder.getCurMain());
			params.put(MAIN_OLD, holder.getOriginMain());
		}
		else{
			params.put(MAIN_CUR, holder.getCurMain());
		}


		params.put(DATA, holder);
		
		params.put(ACTION,action);
		
		params.putAll(extParams);

		DataSourceContextHolder.setDataSource(dsName);
		params.put("JSONObject",JSONObject.class);
		params.put("FormulaUtil",FormulaUtil.class);
		Boolean rtn= (Boolean) groovyEngine.executeScripts(condition, params);
		DataSourceContextHolder.setDefaultDataSource();
		return rtn;
	}
	
	/**
	 * 子表记录行更新处理
	 * @param condition
	 * @param holder
	 * @param jsonEnt
	 * @return
	 */
	private boolean handUpdSubCondition(String condition,String dsName,DataHolder holder,UpdJsonEnt jsonEnt ,Map<String,Object> extParams){
		if(StringUtils.isEmpty(condition)) {
			return true;
		}
		Map<String,Object> params=new HashMap<>(SysConstant.INIT_CAPACITY_16);
		
		params.put(MAIN_CUR, holder.getCurMain());
		params.put(MAIN_OLD, holder.getOriginMain());
		
		params.put(CURRENT, jsonEnt.getCurJson());
		params.put(OLD, jsonEnt.getOriginJson());
		
		params.put(ACTION, DataHolder.ACTION_UPD);

		params.put(DATA, holder);
		
		params.putAll(extParams);

		DataSourceContextHolder.setDataSource(dsName);
		params.put("JSONObject",JSONObject.class);
		params.put("FormulaUtil",FormulaUtil.class);
		Boolean rtn= (Boolean) groovyEngine.executeScripts(condition, params);
		DataSourceContextHolder.setDefaultDataSource();
		return rtn;
	}
	
	/**
	 * 处理子表数据新增的情况。
	 * 子表 数据新增 也分为两种情况。
	 *  1.新增时新增
	 *  2.更新时新增
	 * @param condition
	 * @param holder
	 * @param jsonObject
	 * @return
	 */
	private boolean handNewSubCondition(String condition, String dsName, DataHolder holder, JSONObject jsonObject, Map<String,Object> extParams ){
		if(StringUtils.isEmpty(condition)) {
			return true;
		}
		
		String action=holder.getAction();
		
		Map<String,Object> params=new HashMap<>(SysConstant.INIT_CAPACITY_16);
		
		if(action.equals(DataHolder.ACTION_UPD)){
			params.put(MAIN_CUR, holder.getCurMain());
			params.put(MAIN_OLD, holder.getOriginMain());
			
			params.put(CURRENT, jsonObject);
			params.put(OLD, null);
		}
		else{
			params.put(MAIN_CUR, holder.getCurMain());
			params.put(CURRENT, jsonObject);
		}
		params.put(DATA, holder);

		params.put(ACTION, DataHolder.ACTION_NEW);
		
		params.putAll(extParams);
		DataSourceContextHolder.setDataSource(dsName);


		params.put("JSONObject",JSONObject.class);
		params.put("FormulaUtil",FormulaUtil.class);

		Boolean rtn= (Boolean) groovyEngine.executeScripts(condition, params);
		DataSourceContextHolder.setDefaultDataSource();
		return rtn;
	}
	
	/**
	 * 处理子表数据删除是的条件。
	 * @param condition
	 * @param holder
	 * @param jsonObject
	 * @return
	 */
	private boolean handDelSubCondition(String condition,String dsName, DataHolder holder, JSONObject jsonObject , Map<String,Object> extParams ){
		if(StringUtils.isEmpty(condition)) {
			return true;
		}
		
		Map<String,Object> params=new HashMap<>();
		
		params.put(MAIN_CUR, holder.getCurMain());
		params.put(CURRENT, jsonObject);
		params.put(ACTION, DataHolder.ACTION_DEL);

		params.put(DATA, holder);
		
		params.putAll(extParams);

		DataSourceContextHolder.setDataSource(dsName);
		params.put("JSONObject",JSONObject.class);
		params.put("FormulaUtil",FormulaUtil.class);
		Boolean rtn= (Boolean) groovyEngine.executeScripts(condition, params);
		DataSourceContextHolder.setDefaultDataSource();
		return rtn;
	}
	
	
	/**
	 * 将主从进行分开处理。
	 * @param jsonAry
	 * @return
	 */
	private Map<String, List<JSONObject>> getSettingMap(JSONArray jsonAry){
		Map<String, List<JSONObject>> map=new HashMap<String, List<JSONObject>>();
		for(int i=0;i<jsonAry.size();i++){
			JSONObject json=jsonAry.getJSONObject(i);
			String isMain="yes".equals( json.getString("isMain"))?"main":"sub";
			if(map.containsKey(isMain)){
				List<JSONObject> list=map.get(isMain);
				list.add(json);
			}
			else{
				List<JSONObject> list=new ArrayList<>();
				list.add(json);
				map.put(isMain, list);
			}
		}
		return map;
		
	}

	
}
