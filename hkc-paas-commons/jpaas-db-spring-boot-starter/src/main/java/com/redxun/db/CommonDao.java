package com.redxun.db;

import cn.hutool.core.collection.ConcurrentHashSet;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.proxy.jdbc.ClobProxyImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInterceptor;
import com.redxun.common.base.entity.SqlModel;
import com.redxun.common.base.search.QueryFilter;
import com.redxun.common.base.search.SortParam;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.DateUtils;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ExceptionUtil;
import com.redxun.common.utils.FileUtil;
import com.redxun.common.utils.MessageUtil;
import com.redxun.common.utils.SpringUtil;
import com.redxun.datasource.DataSourceContextHolder;
import com.redxun.datasource.DataSourceUtil;
import com.redxun.datasource.MyBatisSessionFactoryBean;
import com.redxun.listener.DataSourceUpdEvent;
import io.seata.rm.datasource.DataSourceProxy;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 通用查询操作。
 *
 * @param
 */
@Component
@Slf4j
public class CommonDao implements ApplicationListener<DataSourceUpdEvent> {

	/**
	 * 条件标志位
	 */
	public static String CONDITION_TAG="/*CONDITION*/";


	/**
	 * 排序标志位
	 */
	public static String ORDER_TAG="/*ORDERBY*/";
	public final static String WHERE="WHERE";
	public final static String ORDERBY="ORDER BY";

	/**
	 * sessiontemplate 集合
	 */
	private static  Map<String,SqlSessionTemplate> sessionTemplateMap=new ConcurrentHashMap<>();

	private static Set<String> dataSourceKey=new ConcurrentHashSet<>();


	private static final String NAME_SPACE = "com.redxun.sql.common"; // mybatis命名空间

	private static final String dateFormat="yyyy-MM-dd HH:mm:ss";

	/**
	 * 获取当前上下文的数据源。
	 * @return
	 */
	private String getCurDataSource(){
		String ds=DataSourceContextHolder.getDataSource();
		if(StringUtils.isEmpty(ds)){
			ds=DataSourceUtil.LOCAL;
		}
		return ds;
	}

	/**
	 * 执行sql语句
	 * @param sql
	 */
	public int execute(String sql) {
		int rtn= execute(getCurDataSource(), sql);
		return  rtn;
	}

	/**
	 * 根据别名数据源执行SQL语句。
	 * @param alias	数据源源别名
	 * @param sql	需要执行的sql
	 */
	public int execute(String alias,String sql) {
		if(StringUtils.isEmpty(alias)){
			alias=getCurDataSource();
		}
		SqlSessionTemplate sessionTemplate=buildSqlSessionTemplate(alias);
		Map<String, String> map = new HashMap<String, String>();
		map.put("sql", sql);
		String key = getNameSpace("execute");
		int rtn= sessionTemplate.update(key, map);
		return  rtn;
	}


	/**
	 * 根据数据源别 构造 SqlSessionTemplate。
	 * @param alias		数据源别名
	 * @return
	 * @throws Exception
	 */
	private synchronized static SqlSessionTemplate buildTemplate(String alias) throws Exception {
		DataSource dataSource= DataSourceUtil.getDataSourcesByAlias(alias);
		DataSourceProxy dsProxy=(DataSourceProxy)dataSource;
		MyBatisSessionFactoryBean factoryBean=new MyBatisSessionFactoryBean();
		String url= "/mybatis/mybatis-config.xml";
		org.springframework.core.io.Resource configRes=new ClassPathResource(url);
		factoryBean.setConfigLocation(configRes);
		factoryBean.setDataSource(dsProxy);
		Configuration configuration= new Configuration();
		configuration.setCallSettersOnNulls(true);
		configuration.setJdbcTypeForNull(JdbcType.NULL);
		factoryBean.setConfiguration(configuration);
		InputStream[] maps=new InputStream[1];
		org.springframework.core.io.Resource mapRes=new ClassPathResource("/mybatis/mybatis-mappings.xml");
		maps[0]=mapRes.getInputStream();
		factoryBean.setMappingAllLocation(maps);

		String dbType=dsProxy.getDbType();
		Interceptor interceptor=getPageInterceptor(dbType);
		factoryBean.setPlugins(new Interceptor[] {interceptor});
		SqlSessionFactory sqlSessionFactory=factoryBean.buildSqlSessionFactory();

		SqlSessionTemplate sessionTemplate=new SqlSessionTemplate(sqlSessionFactory);

		return  sessionTemplate;
	}

	/**
	 * 构造分页拦截。
	 * @param dialect
	 * @return
	 */
	private    static Interceptor getPageInterceptor(String dialect){
		Interceptor interceptor = new PageInterceptor();
		Properties properties = new Properties();
		//数据库
		properties.setProperty("helperDialect", dialect);
		//是否将参数offset作为PageNum使用
		properties.setProperty("offsetAsPageNum", "true");
		//是否进行count查询
		properties.setProperty("rowBoundsWithCount", "true");
		//是否分页合理化
		properties.setProperty("reasonable", "false");
		interceptor.setProperties(properties);

		return interceptor;
	}

	private static  SqlSessionTemplate buildSqlSessionTemplate(String alias){
		try{
			if(sessionTemplateMap.containsKey(alias)){
				//当数据源发生变化时,重建SqlSessionTemplate对象。
				if(dataSourceKey.contains(alias)){
					SqlSessionTemplate sessionTemplate=buildTemplate(alias);
					sessionTemplateMap.put(alias,sessionTemplate);
					dataSourceKey.remove(alias);
					return  sessionTemplate;
				}

				DataSourceProxy dsProxy=(DataSourceProxy) DataSourceUtil.getDataSourcesByAlias(alias);
				if(dsProxy==null){
					MessageUtil.triggerException("数据源为空","数据源[" +alias +"]为空，请检查数据源是否正确!" );
				}
				DruidDataSource  dataSource= (DruidDataSource) dsProxy .getTargetDataSource();
				if(!dataSource.isClosed()){
					SqlSessionTemplate sessionTemplate=  sessionTemplateMap.get(alias);
					return sessionTemplate;
				}
			}
			SqlSessionTemplate sessionTemplate=buildTemplate(alias);
			sessionTemplateMap.put(alias,sessionTemplate);
			return  sessionTemplate;
		}
		catch (Exception ex){
			String errorMsg=ExceptionUtil.getExceptionMessage(ex);
			log.error(errorMsg);
			MessageUtil.triggerException("数据源为空",errorMsg );
			return null;
		}
	}

	/**
	 * 根据数据源别名获取数据源。
	 * @param alias
	 * @return
	 * @throws Exception
	 */
	public static DataSource getDataSource(String alias) throws Exception{
		DataSourceProxy dsProxy=(DataSourceProxy) DataSourceUtil.getDataSourcesByAlias(alias);
		DruidDataSource  dataSource= (DruidDataSource) dsProxy .getTargetDataSource();
		return dataSource;
	}


	/**
	 * 获取数据库类型
	 * @return
	 */
	public String getDbType(String alias){
		try {
			DataSource dataSource=DataSourceUtil.getDataSourcesByAlias(alias);
			DataSourceProxy druidDataSource=(DataSourceProxy)dataSource;
			String dbType=druidDataSource.getDbType();
			return dbType;
		}
		catch (Exception ex){
			log.error(ExceptionUtil.getExceptionMessage(ex));
			return DataSourceUtil.getDefaultDbType();
		}

	}


	/**
	 * 执行JDBC。
	 * @param model
	 * @throws IllegalAccessException
	 * @throws NoSuchFieldException
	 */
	public int executeNamedSql(SqlModel model) throws IllegalAccessException, NoSuchFieldException{
		String dsName=model.getDsName();

		NamedParameterJdbcTemplate namedParameterJdbcTemplate=null;
		if(StringUtils.isEmpty(dsName)){
			namedParameterJdbcTemplate=SpringUtil.getBean(NamedParameterJdbcTemplate.class);
		}
		else{
			DataSource dataSource= DataSourceUtil.getDataSourcesByAlias(dsName);
			namedParameterJdbcTemplate=new NamedParameterJdbcTemplate(dataSource);
		}
		int rtn= namedParameterJdbcTemplate.update(model.getSql(), model.getParams());
		return rtn;
	}


	/**
	 * 执行sql语句。
	 * <pre>
	 * 调用示例代码:
	 * String sql="insert into orders (ID_,NAME_,ALIAS_) values (#{id},#{name},#{alias})";
	 *	Map<String,Object> params=new HashMap<String, Object>();
	 *	params.put("id", "2");
	 *	params.put("name", "红迅采购JSAAS合同");
	 *	params.put("alias", "redxun");
	 *	commonDao.execute(sql, params);
	 * </pre>
	 * @param sql
	 * @param params
	 */
	public int execute(String sql,Map<String,Object> params)  {
		return execute(getCurDataSource(), sql,params);
	}

	/**
	 * 执行sql
	 * @param alias	数据源别名
	 * @param sql		sql语句
	 * @param params	参数
	 */
	public int  execute(String alias, String sql,Map<String,Object> params) {
		SqlSessionTemplate sessionTemplate=buildSqlSessionTemplate(alias);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sql", sql);
		if(BeanUtil.isNotEmpty(params)){
			map.putAll(params);
		}
		String key = getNameSpace("execute");
		int rtn=sessionTemplate.update(key, map);
		return rtn;
	}



	/**
	 * 根据sqlmodel 执行sql操作。
	 * @param sqlModel
	 */
	public int execute(SqlModel sqlModel){
		int rtn=execute(getCurDataSource(), sqlModel );
		return rtn;
	}

	/**
	 * 执行SQL语句
	 * @param alias		数据源别名
	 * @param sqlModel	需要执行的sqlModel对象
	 */
	public int execute(String alias, SqlModel sqlModel) {
		SqlSessionTemplate sessionTemplate=buildSqlSessionTemplate(alias);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sql", sqlModel.getSql());
		if(BeanUtil.isNotEmpty(sqlModel.getParams())){
			map.putAll(sqlModel.getParams());
		}
		String key = getNameSpace("execute");
		int rtn=sessionTemplate.update(key, map);
		return rtn;
	}


	/**
	 * 查询列表，返回数据为List，列表的数据结构为Map对象实例。
	 *
	 * @param sql
	 * @return List
	 */
	public List query(String sql) {
		return query(getCurDataSource(), sql);
	}

	/**
	 * 执行查询语句
	 * @param alias	数据源别名
	 * @param sql		需要执行的SQL
	 * @return
	 */
	public List query(String alias,String sql) {
		SqlSessionTemplate sessionTemplate=buildSqlSessionTemplate(alias);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sql", sql);
		String key = getNameSpace("query");
		Map<String,Object> tmp=this.handParams(map);
		List pageList=sessionTemplate.selectList(key, tmp);
		handList(pageList);
		return pageList;
	}




	/**
	 * 根据sql 和参数查询数据，返回数据为List，中间的数据结构为Map。
	 * <pre>
	 * String sql="select * from orders where id_=#{id}";
	 *	Map<String,Object> params=new HashMap<String, Object>();
	 *	params.put("id", "2");
	 *	List list= commonDao.query(sql, params);
	 *	System.out.println(list);
	 * </pre>
	 * @param sql
	 * @param params
	 * @return
	 */
	public List query(String sql,Map<String,Object> params) {
		return query(getCurDataSource(), sql,params);
	}

	/**
	 * 执行查询
	 * @param alias		数据源别名
	 * @param sql			sql语句
	 * @param params		查询参数
	 * @return
	 */
	public List query(String alias,String sql,Map<String,Object> params) {
		SqlSessionTemplate sessionTemplate=buildSqlSessionTemplate(alias);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sql", sql);
		if(BeanUtil.isNotEmpty(params)){
			map.putAll(params);
		}
		String key = getNameSpace("query");
		Map<String,Object> tmp=this.handParams(map);
		List pageList=sessionTemplate.selectList(key, tmp);
		handList(pageList);
		return pageList;
	}




	/**
	 * 根据sqlModel查询列表数据。
	 * @param sqlModel
	 * @return
	 */
	public List query(SqlModel sqlModel){
		return query(getCurDataSource(), sqlModel);
	}

	/**
	 * 执行sql查询
	 * @param alias		数据源别名
	 * @param sqlModel	sql查询对象
	 * @return
	 */
	public List query(String  alias, SqlModel sqlModel){
		SqlSessionTemplate sessionTemplate=buildSqlSessionTemplate(alias);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sql", sqlModel.getSql());
		if(BeanUtil.isNotEmpty(sqlModel.getParams())){
			map.putAll(sqlModel.getParams());
		}
		String key = getNameSpace("query");
		Map<String,Object> tmp=this.handParams(map);
		List pageList=sessionTemplate.selectList(key, tmp);
		handList(pageList);
		return pageList;

	}

	/**
	 * 根据sqlmodel 查询单条记录或者单个值。
	 * 这个值可以是map，或者 为单个值，比如汇总等。
	 * @param sqlModel
	 * @return
	 */
	public Object queryOne(SqlModel sqlModel){
		return  queryOne(getCurDataSource(), sqlModel);
	}

	/**
	 * 执行sql查询。
	 * @param alias		数据源别名
	 * @param sqlModel	查询对象
	 * @return
	 */
	public Object queryOne(String  alias, SqlModel sqlModel){
		if(StringUtils.isEmpty(alias)){
			alias=DataSourceUtil.LOCAL;
		}
		SqlSessionTemplate sessionTemplate=buildSqlSessionTemplate(alias);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sql", sqlModel.getSql());
		if(BeanUtil.isNotEmpty(sqlModel.getParams())){
			map.putAll(sqlModel.getParams());
		}
		String key = getNameSpace("queryObject");
		return sessionTemplate.selectOne(key, map);
	}




	/**
	 * 根据 sql 查询单条记录或者单个值。
	 * 这个值可以是map，或者 为单个值，比如汇总等。
	 * @param sql
	 * @return
	 */
	public Object queryOne(String sql){
		return  queryOne(getCurDataSource(), sql);
	}

	/**
	 * 执行sql查询。
	 * @param alias		数据源别名
	 * @param sql	查询对象
	 * @return
	 */
	public Object queryOne(String  alias, String sql){

		SqlSessionTemplate sessionTemplate=buildSqlSessionTemplate(alias);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sql",sql);

		String key = getNameSpace("queryObject");
		return sessionTemplate.selectOne(key, map);
	}


	/**
	 * 返回记录。
	 * @param sql
	 * @param queryFilter
	 * @param params
	 * @return
	 */
	public Object queryOne( String sql, QueryFilter queryFilter, Map<String, Object> params) {
		Object rtn=queryOne(getCurDataSource(),sql,queryFilter,params);
		return rtn;
	}

	public Object queryOne(String alias, String sql, QueryFilter queryFilter, Map<String, Object> params) {
		SqlSessionTemplate sessionTemplate=buildSqlSessionTemplate(alias);
		if (CollectionUtils.isEmpty(params)) {
			params = new HashMap();
		}

		Map<String, Object> filter = parseQueryFilter(queryFilter);
		params.putAll(filter);
		//构建SQL的各部分
		buildSql(sql,params);

		sql= (String) params.get("sql");

		sql="select count(*) from (" + sql +") tmp";

		queryFilter.getParams().putAll(params);
		Map<String,Object> tmp=this.handParams(params);

		tmp.put("sql",sql);

		return sessionTemplate.selectOne(getNameSpace("queryObject"), tmp);
	}

	/**
	 * 返回一行数据。
	 * @param sql
	 * @return
	 */
	public Map queryForMap(String sql){
		return queryForMap(getCurDataSource(), sql);
	}

	/**
	 * 返回一行数据。
	 * @param alias		数据源别名
	 * @param sql   sql查询对象
	 * @return
	 */
	public Map queryForMap(String alias,String sql){

		SqlSessionTemplate sessionTemplate=buildSqlSessionTemplate(alias);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sql", sql);
		String key = getNameSpace("query");
		Map<String,Object> tmp=this.handParams(map);
		return sessionTemplate.selectOne(key, tmp);
	}



	/**
	 * 返回一行数据。
	 * @param sqlModel
	 * @return
	 */
	public Map queryForMap(SqlModel sqlModel){
		return queryForMap(getCurDataSource(), sqlModel);
	}

	/**
	 * 返回一行数据。
	 * @param alias		数据源别名
	 * @param sqlModel   sql查询对象
	 * @return
	 */
	public Map queryForMap(String alias,SqlModel sqlModel){

		SqlSessionTemplate sessionTemplate=buildSqlSessionTemplate(alias);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sql", sqlModel.getSql());
		if(BeanUtil.isNotEmpty(sqlModel.getParams())){
			map.putAll(sqlModel.getParams());
		}
		String key = getNameSpace("query");
		Map<String,Object> tmp=this.handParams(map);
		return sessionTemplate.selectOne(key, tmp);
	}


	/**
	 * 查询分页列表
     * 分页示例：
     * <pre>
     * String sql="select * from demo where name like #{w.name}";
     * Map<String,Object> params=new HashMap<>();
     * params.put("name","A%");
     * Page page=new Page();
     * page.pageNum(1);
     * page.pageSize(3);
     * Page p= commonDao.query(sql,params,page);
     * </pre>
	 * @param sql       sql 语句
	 * @param params    参数MAP
	 * @param page      分页对象
	 * @return
	 */
	public Page query(String sql, Map<String,Object> params, Page page) {
		return  query(getCurDataSource(), sql,params,page);
	}

    /**
     * 分页查询
     * @param alias         数据源名称
     * @param sql           sql语句
     * @param params        参数MAP
     * @param page          分页对象
     * @return
     */
	public Page query(String alias, String sql, Map<String,Object> params, Page page) {
		SqlSessionTemplate sessionTemplate=buildSqlSessionTemplate(alias);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sql", sql);
		if(BeanUtil.isNotEmpty(params)){
			map.putAll(params);
		}
		String key = getNameSpace("query");
		Map<String,Object> tmp=this.handParams(map);
		Page pageList=(Page)sessionTemplate.selectList(key, tmp, new RowBounds(page.getPageNum(),  page.getPageSize()));
		handList(pageList);
		return pageList;

	}


	/**
	 * 合并mybatis 命名空间
	 *
	 * @param sqlKey
	 * @return
	 */
	private String getNameSpace(String sqlKey) {
		return NAME_SPACE + "." + sqlKey;
	}

	/**
	 * 自定义SQL,分页由系统,可自动过滤排序grid规则，详细运用请看：com.hotent.demo.querysql.controller
	 *
	 * <pre>
	 * QueryFilter queryFilter = getQuerFilter(request);
	 * String sql = &quot;SELECT * FROM xog_user WHERE 1=#{test} order by user_id_ &quot;;
	 * Map&lt;String, Object&gt; params = new HashMap&lt;String, Object&gt;();
	 * params.put(&quot;test&quot;, 1);
	 * List list = commonDao.queryForList(sql, queryFilter, params);
	 * return new PageJson(list);
	 * </pre>
	 *
	 * @param sql
	 * @param queryFilter
	 * @param params
	 * @return
	 */
	public List queryForList(String sql, QueryFilter queryFilter, Map<String, Object> params) {
		return  queryForList(getCurDataSource(), sql,queryFilter,params);
	}

	public List queryForList(String alias, String sql, QueryFilter queryFilter, Map<String, Object> params) {
		SqlSessionTemplate sessionTemplate=buildSqlSessionTemplate(alias);
		if (CollectionUtils.isEmpty(params)) {
			params = new HashMap();
		}
		params.put("sql", sql);
		Map<String, Object> filter = parseQueryFilter(queryFilter);
		params.putAll(filter);
		//构建SQL的各部分
		buildSql(sql,params);
		queryFilter.getParams().putAll(params);
		Map<String,Object> tmp=this.handParams(params);

		List pageList=sessionTemplate.selectList(getNameSpace("query"), tmp);
		handList(pageList);

		return pageList;
	}

	/**
	 * 动态查询列表
	 * @param sql
	 * @param filter
	 * @param params
	 * @return
	 */
	public Page queryDynamicList(String sql, QueryFilter filter, Map<String,Object> params){
		return  queryDynamicList(getCurDataSource(), sql,filter,params);
	}

	public Page queryDynamicList(String alias, String sql, QueryFilter filter, Map<String,Object> params){
		SqlSessionTemplate sessionTemplate=buildSqlSessionTemplate(alias);
		if (CollectionUtils.isEmpty(params)) {
			params = new HashMap();
		}


		Map<String, Object> newParams = this.parseQueryFilter(filter);
		params.putAll(newParams);
		//构建SQL的各部分
		buildSql(sql,params);
		filter.getParams().putAll(params);
		Map<String,Object> tmp=this.handParams(params);

		Page pageList=(Page)sessionTemplate.selectList(this.getNameSpace("query"), tmp,
				new RowBounds(Long.valueOf( filter.getPage().getCurrent()).intValue(), Long.valueOf(filter.getPage().getSize()).intValue()));

		handList(pageList);

		return pageList;
	}

	private Map<String,Object> handParams(Map<String,Object> params){
		Map<String,Object> tmp=new HashMap<>();
		tmp.put("w",params);
		return tmp;
	}

	private void handList(List pageList){
		for(Object obj:pageList){
			Map<String,Object> row=(Map<String,Object>)obj;
			for (Map.Entry<String, Object> ent : row.entrySet()) {
				Object val=ent.getValue();
				if(val instanceof ClobProxyImpl){
					ClobProxyImpl clob=(ClobProxyImpl)val;
					String str= FileUtil.clobToString(clob);
					row.put(ent.getKey(), str);
				}
				else if(val instanceof LocalDateTime){
					row.put(ent.getKey(), DateUtils.localDateTimeToDate((LocalDateTime) val));
			   }
			}
		}
	}

	private void buildSql(String sql,Map<String,Object> params){

		sql = sql.replaceAll("(?i)\\s+where\\s+", " WHERE ");
		sql = sql.replaceAll("(?i)\\s+order\\s+by\\s+", " ORDER BY ");



		if(sql.indexOf(CommonDao.CONDITION_TAG)!=-1 || sql.indexOf(CommonDao.ORDER_TAG)!=-1){
			if(params.containsKey("whereSql")){
				sql=sql.replace(CONDITION_TAG,"AND (" + params.get("whereSql").toString() +")");
			}

			if(params.containsKey("orderBySql")){
				String orderSql=params.get("orderBySql").toString();
				//有order 标志
				if(sql.indexOf(CommonDao.ORDER_TAG)!=-1){
					sql=sql.replace(ORDER_TAG," ORDER BY " +  orderSql);
				}
				else {
					//是否有ORDER BY 子句
					int orderByIndex=sql.lastIndexOf(" ORDER BY ");
					if(orderByIndex==-1){
						sql+= " ORDER BY " + orderSql;
					}
					else{
						sql=sql.substring(0,orderByIndex) + " ORDER BY " + orderSql;
					}
				}
			}
			sql=sql.replaceFirst("1\\s*=\\s*1\\s*AND"," ");
			params.put("sql",sql);
		}
		else{
			buildSqlClause(sql,params);
		}
	}


	/**
	 * 构建动态的SQL查询参数结构
	 * @param nSql
	 * @param params
	 */
	private void buildSqlClause(String nSql,Map<String,Object> params){

		nSql=nSql.replaceAll("\r", " ");
		nSql=nSql.replaceAll("\n", " ");
		nSql=nSql.replaceAll("(?is)\\s+", " ");
		int lWhereIndex=nSql.lastIndexOf(" WHERE ");
		int orderByIndex=nSql.lastIndexOf(" ORDER BY ");

		String whereClause=null;
		String orderClause=null;
		String selectClause=null;

		if(lWhereIndex!=-1 && orderByIndex!=-1){
			whereClause=nSql.substring(lWhereIndex, orderByIndex);
			selectClause=nSql.substring(0,lWhereIndex);
		}else if(lWhereIndex!=-1 && orderByIndex==-1){
			whereClause=nSql.substring(lWhereIndex);
			selectClause=nSql.substring(0,lWhereIndex);
		}else if(lWhereIndex==-1 && orderByIndex!=-1){
			whereClause=" WHERE 1=1 ";
			selectClause=nSql.substring(0,orderByIndex);
		}else{
			whereClause=" WHERE 1=1 ";
			selectClause=nSql;
		}

		if(params.containsKey("whereSql")){
			whereClause=whereClause + " AND (" + params.get("whereSql") + ")";
		}
		String orderSql="";
		//若外部没有传入该排序参数，并且原SQL中带有order by ，则加入Order by 参数
		if(!params.containsKey("orderBySql") && orderByIndex!=-1){
			orderClause=nSql.substring(orderByIndex);
			orderSql=orderClause;
		}else if(params.containsKey("orderBySql")){
			orderSql=" ORDER BY " + params.get("orderBySql");

		}
		nSql=selectClause +" " + whereClause +" " + orderSql;
		nSql=nSql.replaceFirst("(?i)WHERE\\s+1=1\\s*AND"," WHERE ");

		params.put("sql", nSql);

	}

	/**
	 * 自定义SQL,分页由系统,可自动过滤排序grid规则，详细运用请看：com.hotent.demo.querysql.controller
	 *
	 * <pre>
	 * QueryFilter queryFilter = getQuerFilter(request);
	 * String sql = &quot;SELECT * FROM xog_user WHERE 1=#{test} order by user_id_ &quot;;
	 * Map&lt;String, Object&gt; params = new HashMap&lt;String, Object&gt;();
	 * params.put(&quot;test&quot;, 1);
	 * PageList list = commonDao.queryForListPage(sql, queryFilter, params);
	 * return new PageJson(list);
	 * </pre>
	 *
	 * @param sql
	 * @param queryFilter
	 * @param params
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Page queryForListPage(String sql, QueryFilter queryFilter, Map<String, Object> params) {
		return  queryForListPage(getCurDataSource(), sql,queryFilter,params);
	}

	public Page queryForListPage(String  alias, String sql, QueryFilter queryFilter, Map<String, Object> params) {

		SqlSessionTemplate sessionTemplate=buildSqlSessionTemplate(alias);
		if (CollectionUtils.isEmpty(params)) {
			params = new HashMap<String, Object>();
		}
		params.put("sql", sql);

		Map<String, Object> filter = this.parseQueryFilter(queryFilter);
		params.putAll(filter);
		Map<String,Object> tmp=this.handParams(params);
		Page pageList=(Page)sessionTemplate.selectList(this.getNameSpace("queryList"), tmp,
				new RowBounds(Long.valueOf( queryFilter.getPage().getCurrent()).intValue(), Long.valueOf(queryFilter.getPage().getSize()).intValue()));
		handList(pageList);
		return pageList;
	}




	/**
	 *
	 *
	 * @param queryFilter
	 * @return
	 */
	public Map<String, Object> parseQueryFilter(QueryFilter queryFilter) {
		queryFilter.setParams();
		Map<String, Object> params = queryFilter.getParams();
		// 构建动态条件SQL
		String dynamicWhereSql = queryFilter.getFieldLogic().getSql();
		if (StringUtils.isNotEmpty(dynamicWhereSql)) {
			params.put("whereSql", dynamicWhereSql);
		}

		if(queryFilter.getSortParams().size()>0){
	    	StringBuffer sb=new StringBuffer();
			for(SortParam sortParam: queryFilter.getSortParams()){
				sb.append(DbUtil.encodeSql(sortParam.getSql())).append(",");
			}
			sb.deleteCharAt(sb.length()-1);
			params.put("orderBySql", sb.toString());
    	}

		return params;
	}

	/**
	 * select * from OS_USER
	 * select * from OS_USER order by AAA
	 *
	 * select * from OS_USER WHERE userId=""
	 * select * from OS_USER WHERE userId="" ORDER BY AA
	 * @param sql-se
	 * @param whereSql
	 * @return
	 */
	public static String insertWhereSql(String sql,String whereSql){
		sql=sql.replaceAll("(?is)\\s+", " ");
		//判断语句中有没有 where
		if(sql.indexOf(CommonDao.WHERE)==-1){
			//没有order by
			if(sql.indexOf(CommonDao.ORDERBY)==-1){
				sql += " WHERE " +whereSql ;
			}
			else{
				String[] arySql= sql.split(ORDERBY);
				sql=arySql[0] +" WHERE " + whereSql +arySql[1];
			}
		}
		else{
			//判断有没有order by
			if(sql.indexOf(ORDERBY)==-1){
				sql+=" AND (" +whereSql +")";
			}
			else{
				String[] arySql= sql.split(ORDERBY);
				sql=arySql[0] +" AND ("+whereSql +")" + ORDERBY + " " + arySql[1];
			}
		}
		sql=sql.replaceFirst("(?i)where\\s+1=1\\s*and"," where ");
		return  sql;
	}


	@Override
	public void onApplicationEvent(DataSourceUpdEvent dataSourceUpdEvent) {
		String key=(String)dataSourceUpdEvent.getSource();
		dataSourceKey.add(key);
	}
}
