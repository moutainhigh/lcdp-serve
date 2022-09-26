package com.redxun.form.core.controller;


import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.config.ConfigService;
import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.entity.GridHeader;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.engine.FtlEngine;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.RequestUtil;
import com.redxun.datasource.DataSourceConstant;
import com.redxun.datasource.DataSourceContextHolder;
import com.redxun.datasource.DataSourceUtil;
import com.redxun.db.DbUtil;
import com.redxun.dboperator.ITableOperator;
import com.redxun.dboperator.OperatorContext;
import com.redxun.dboperator.model.Table;
import com.redxun.dboperator.operatorimpl.DBMetaImpl;
import com.redxun.form.bo.entity.FormBoEntity;
import com.redxun.form.bo.service.FormBoEntityServiceImpl;
import com.redxun.log.annotation.AuditLog;
import com.redxun.util.SysUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 *
 * <pre>
 * 描述：通过jdbc元数据的api查找表名或列名
 * @author cjx
 * @Email: chshxuan@163.com
 * @Copyright (c) 2014-2016 使用范围：
 * 本源代码受软件著作法保护，请在授权允许范围内使用。
 * </pre>
 */
@Slf4j
@RestController
@RequestMapping("/form/core/sysDb")
@ClassDefine(title = "数据库查找表名或列名接口",alias = "formDbController",path = "/form/core/sysDb",packages = "core",packageName = "表单管理")
@Api(tags = "数据库查找表名或列名接口")
public class FormDbController {

	@Resource
	FtlEngine freemarkEngine;
	@Resource
	DBMetaImpl dbMetaImpl;
	@Resource
    FormBoEntityServiceImpl formBoEntityServiceImpl;

	@Autowired
	ConfigService configService;

	/**
	 * 通过jdbc元数据的api查找列名(返回json用于表格)
	 *
	 * @param request
	 * @throws Exception
	 */
	@MethodDefine(title = "通过jdbc元数据的api查找列名", path = "/findColumnList", method = HttpMethodConstants.POST,
			params = {@ParamDefine(title = "请求数据", varName = "request")})
	@ApiOperation(value="查找列名", notes="通过jdbc元数据的api查找列名(返回json用于表格)")
	@PostMapping("/findColumnList")
	public List<GridHeader> findColumnList(HttpServletRequest request) throws Exception {

		String query= RequestUtil.getString(request,"query");
		String queryType=RequestUtil.getString(request,"queryType");
		String ds=RequestUtil.getString(request,"ds");

		List<GridHeader> list = findColumnListMethod(query, queryType, ds);
		return list;
	}

	private List<GridHeader> findColumnListMethod(String query, String queryType, String ds) throws Exception {
		if(StringUtils.isNotEmpty(ds)){
			DataSourceContextHolder.setDataSource(ds);
		}
		String sql=query;
		String table="table";
		String freeMarkerSql="freeMarkerSql";
		if(table.equals(queryType)) {
			sql="select * from " + query;
		}
		else if(freeMarkerSql.equals(queryType)) {
			sql = SysUtil.replaceConstant(sql);
			sql=freemarkEngine.parseByStringTemplate(new HashMap<>(16), sql);
		}

		List<GridHeader> list= DbUtil.getGridHeader(sql);
		DataSourceContextHolder.setDefaultDataSource();
		return list;
	}

	/**
	 * 通过jdbc元数据的api查找表名
	 *
	 * @param request
	 * @throws Exception
	 */
	@MethodDefine(title = "通过jdbc元数据的api查找表名", path = "/findTableList", method = HttpMethodConstants.POST,
			params = {@ParamDefine(title = "请求数据", varName = "request")})
	@ApiOperation(value="查找表名", notes="通过jdbc元数据的api查找表名")
	@PostMapping("/findTableList")
	public List<Table> findTableList(HttpServletRequest request) throws Exception {
		// 查找数据源
		String ds=RequestUtil.getString(request,"ds");
		if(StringUtils.isNotEmpty(ds)){
			DataSourceContextHolder.setDataSource(ds);
		}
		String tableName=RequestUtil.getString(request,"tableName");
		List<Table> tables=dbMetaImpl.getObjectsByName(tableName);
		DataSourceContextHolder.setDefaultDataSource();
		return tables;
	}

	@MethodDefine(title = "获取表详细信息", path = "/getByName", method = HttpMethodConstants.POST,
			params = {@ParamDefine(title = "请求数据", varName = "request")})
	@ApiOperation(value="获取表详细信息")
	@PostMapping("/getByName")
	public Table getByName(HttpServletRequest request) throws Exception{
		String dsName=RequestUtil.getString(request,"dsName");
		String tbName=RequestUtil.getString(request,"tbName");
		if(StringUtils.isNotEmpty(dsName)){
			DataSourceContextHolder.setDataSource(dsName);
		}
		Table table=dbMetaImpl.getModelByName(tbName);
		DataSourceContextHolder.setDefaultDataSource();
		return table;
	}

	@MethodDefine(title = "通过entId获取表详细信息", path = "/getBoEntId", method = HttpMethodConstants.GET,
			params = {@ParamDefine(title = "业务实体ID", varName = "entId")})
	@ApiOperation(value="通过entId获取表详细信息")
	@GetMapping("/getBoEntId")
	public Table getBoEntId(@RequestParam(value="entId")String entId) throws Exception{
		FormBoEntity boEnt = formBoEntityServiceImpl.getById(entId);
		if(StringUtils.isNotEmpty(boEnt.getDsAlias())){
			DataSourceContextHolder.setDataSource(boEnt.getDsAlias());
		}
		Table table=dbMetaImpl.getModelByName(boEnt.getTableName());
		DataSourceContextHolder.setDefaultDataSource();
		return table;
	}

	@MethodDefine(title = "根据sql获取字段", path = "/getFields", method = HttpMethodConstants.POST,
			params = {@ParamDefine(title = "sql", varName = "sql")})
	@ApiOperation(value = "根据sql获取字段", notes = "根据sql获取字段")
	@AuditLog(operation = "根据sql获取字段")
	@PostMapping("/getFields")
	public List<GridHeader> getFields(@ApiParam(required = true, name = "sql") String sql, @ApiParam(required = true, name = "ds") String ds) throws IllegalAccessException, NoSuchFieldException, SQLException {
		List<GridHeader> field = new ArrayList<>();

		if(StringUtils.isNotEmpty(ds)){
			DataSourceContextHolder.setDataSource(ds);
		}
		field = DbUtil.getGridHeader(sql);
		DataSourceContextHolder.setDefaultDataSource();

		return field;
	}


	@MethodDefine(title = "根据数据源获取数据库字段类型", path = "/getDbFieldType", method = HttpMethodConstants.GET,
			params = {@ParamDefine(title = "数据源别名", varName = "dsAlias")})
	@ApiOperation(value="根据数据源获取数据库字段类型")
	@GetMapping("/getDbFieldType")
	public JsonResult getDbFieldType(@RequestParam(value="dsAlias")String dsAlias) throws Exception{
		if(StringUtils.isNotEmpty(dsAlias)){
			DataSourceContextHolder.setDataSource(dsAlias);
		}
		String dbType = DataSourceUtil.getCurrentDbType();
		DataSourceContextHolder.setDefaultDataSource();
		String dbFieldType = configService.getConfig("dbFieldType", DataSourceConstant.DEFAULT_GROUP,0);
		if(StringUtils.isNotEmpty(dbFieldType)){
			JSONObject jsonObject = JSONObject.parseObject(dbFieldType);
			return new JsonResult().setShow(false).setData(jsonObject.getJSONObject(dbType)).setSuccess(true);
		}else {
			return new JsonResult().setShow(true).setSuccess(false).setMessage("Nacos缺少dbFieldType配置!");
		}
	}

}
