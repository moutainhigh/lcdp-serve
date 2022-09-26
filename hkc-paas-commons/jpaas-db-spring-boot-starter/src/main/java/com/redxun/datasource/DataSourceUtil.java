package com.redxun.datasource;


import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ExceptionUtil;
import com.redxun.common.utils.SpringUtil;
import io.seata.rm.datasource.DataSourceProxy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.support.JdbcUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.*;

/**
 * 数据源操作类。
 *  <pre>
 *      1.构建 配置文件中的数据源。
 *      2.删除数据源
 *      3.添加数据源
 *      4.获取配置的数据源
 *      5.根据配置构建数据源
 *  </pre>
 * @author ray
 */
@Slf4j
public class DataSourceUtil {

    public static final String GLOBAL_DATASOURCE="dataSource";

    public static final String TARGET_DATASOURCES = "targetDataSources";

    public static final String DEFAULT_DATASOURCE = "defaultTargetDataSource";

    public static final String LOCAL = "LOCAL";

    public static final Map<String,String> driverMap = new HashMap<>();

    static {
        driverMap.put("mysql","com.mysql.jdbc.Driver");
        driverMap.put("oracle","oracle.jdbc.driver.OracleDriver");
        driverMap.put("sqlserver","com.microsoft.sqlserver.jdbc.SQLServerDriver");
        driverMap.put("dm","dm.jdbc.driver.DmDriver");
        driverMap.put("kingbase","com.kingbase8.Driver");
        driverMap.put("postgresql","org.postgresql.Driver");
    }

    /**
     * 创建数据源。
     * @return
     */
    public static DataSource buildDataSource(Environment env,String dataSourcePre,String dsName){
        // 获取对象
        String[] stringParams=new String[]{"url","username","password","driverClassName","validationQuery","filters"};
        String[] intParams=new String[]{"initialSize","minIdle","maxActive","maxPoolPreparedStatementPerConnectionSize"};
        String[] longParams=new String[]{"maxWait","timeBetweenEvictionRunsMillis","minEvictableIdleTimeMillis"};
        String[] boolParams=new String[]{"testWhileIdle","testOnBorrow","testOnReturn","poolPreparedStatements"};
        String[] properitesParams=new String[]{"connectProperties"};
        DruidDataSource dataSource=null;
        try{
            dataSource = new DruidDataSource();// 初始化对象
            //设置属性。
            setInitSql(env,dataSourcePre,dataSource);

            setPropties(dataSource,stringParams,"string",dataSourcePre,env);
            setPropties(dataSource,intParams,"int",dataSourcePre,env);
            setPropties(dataSource,longParams,"long",dataSourcePre,env);
            setPropties(dataSource,boolParams,"boolean",dataSourcePre,env);
            setPropties(dataSource,properitesParams,"Properites",dataSourcePre,env);

            dataSource.setName("druid-" +dsName );
            dataSource.init();

        }
        catch (Exception ex){
            log.error(ex.getMessage());
        }
        return dataSource;
    }

    /**
     * 设置初始化的SQL
     * @param env
     * @param dataSourcePre
     * @param dataSource
     */
    private static void setInitSql(Environment env,String dataSourcePre,DruidDataSource dataSource){
        String initSql= env.getProperty(dataSourcePre+".string.connection-init-sqls");

        if(StringUtils.isEmpty(initSql)){
            return;
        }

        String[] arySql=initSql.split(";");
        List<String> listInitSql = Arrays.asList(arySql);
        dataSource.setConnectionInitSqls(listInitSql);
    }

    /**
     * 验证数据源连接是否有效
     * @param dbType
     * @param url
     * @param user
     * @param pwd
     * @return
     */
    public static JsonResult validConn(String dbType,String url,String user,String pwd){
        String className = driverMap.get(dbType.toLowerCase());
        JsonResult result = JsonResult.getSuccessResult("数据连接成功!");


        Connection conn=null;
        try{
            Class.forName(className);
            conn= DriverManager.getConnection(url,user,pwd);
        }catch (Exception e){
            e.printStackTrace();
            result = JsonResult.getFailResult(e.getMessage());
        }finally {
            JdbcUtils.closeConnection(conn);
        }
        return result;
    }

    /**
     * 设置数据源属性。
     * @param dataSource
     * @param params
     * @param type
     * @param dataSourcePre
     * @param env
     */
    private static void setPropties(DruidDataSource dataSource,String[] params,String type,String dataSourcePre,Environment env){
        for(int i=0;i<params.length;i++) {
            String name = params[i];
            String param = dataSourcePre + "." + type + "." + name;
            String value = env.getProperty(param);
            Object val = "string".equals(type) ? value : BeanUtil.convertByActType(type, value);
            BeanUtil.setFieldValue(dataSource, name, val);
        }
    }


    public static DataSource getDefaultDatasource(){
        DynamicDataSource dataSource= SpringUtil.getBean(DataSourceUtil.GLOBAL_DATASOURCE);
        return dataSource.getDefaultSource();
    }

    /**
     * 获取默认的数据源类型
     * @return
     */
    public static String getDefaultDbType(){
        DataSourceProxy dataSource=(DataSourceProxy)getDefaultDatasource();
        return dataSource.getDbType();
    }

    /**
     * 获取当前数据库类型
     * @return
     */
    public static  String getCurrentDbType(){
        try{
            String alias= DataSourceContextHolder.getDataSource();
            DataSourceProxy dataSource=(DataSourceProxy) getDataSourcesByAlias(alias);
            return  dataSource.getDbType();
        }
        catch (Exception ex){
            log.error(ExceptionUtil.getExceptionMessage(ex));
            return  "";
        }

    }

    /**
     * 获取数据源
     * @return
     * @throws IllegalAccessException
     * @throws NoSuchFieldException
     */
    public static Map<Object, Object> getDataSources() throws IllegalAccessException, NoSuchFieldException {
        DynamicDataSource dynamicDataSource = SpringUtil.getBean(GLOBAL_DATASOURCE);
        return dynamicDataSource.getDataSource();
    }

    /**
     * 根据别名获取数据源
     * @param alias
     * @return
     * @throws IllegalAccessException
     * @throws NoSuchFieldException
     */
    public static DataSource getDataSourcesByAlias(String alias) throws IllegalAccessException, NoSuchFieldException {
        if(LOCAL.equals(alias) || StringUtils.isEmpty(alias) || "null".equals(alias)){
            alias=DEFAULT_DATASOURCE;
        }
        Map<Object,Object> map= getDataSources();
        for(Object key : map.keySet()){
            if(alias.equalsIgnoreCase(key.toString())){
                return (DataSource)map.get(key);
            }
        }
        return null;
    }


    /**
     * 根据配置来动态创建数据源。
     * @param alias
     * @param jsonAry [{name:"url",comment:"连接字符串",type:"String",val:"jdbc:mysql://localhost:3306/dbName?useUnicode=true&characterEncoding=UTF-8&serverTimezone=GMT%2B8"},
     * {name:"username",comment:"用户名",type:"String",val:""},
     * {name:"password",comment:"密码",type:"String",val:""},
     * {name:"initialSize",comment:"初始大小",type:"int",val:"5"},
     * {name:"minIdle",comment:"最小空闲连接数",type:"int",val:"5"},
     * {name:"maxActive",comment:"连接池最大数",type:"int",val:"10"},
     * {name:"maxWait",comment:"获取连接超时的时间(毫秒)",type:"long",val:"60000"},
     * {name:"timeBetweenEvictionRunsMillis",comment:"检测间隔时间(毫秒)",type:"long",val:"60000"},
     * {name:"minEvictableIdleTimeMillis",comment:"连接最小生存时间(毫秒)",type:"long",val:"300000"},
     * {name:"validationQuery",comment:"连接校验语句",type:"String",val:"SELECT 1 from act_ge_property"},
     * {name:"testWhileIdle",comment:"空闲时检测连接",type:"boolean",val:"true"},
     * {name:"testOnBorrow",comment:"获取连接时检测连接",type:"boolean",val:"false"},
     * {name:"testOnReturn",comment:"连接返回连接池时检测连接",type:"boolean",val:"false"},
     * {name:"poolPreparedStatements",comment:"打开PSCACHE",type:"boolean",val:"true"},
     * {name:"maxPoolPreparedStatementPerConnectionSize",comment:"PSCACHE大小",type:"int",val:"20"},
     * {name:"filters",comment:"插件",type:"String",val:"stat"}]
     * @return
     */
    public static DataSource buildDataSource(String alias,String jsonAry){
        try {
            // 获取对象
            DruidDataSource dataSource = new DruidDataSource();// 初始化对象
            // 开始set它的属性
            JSONArray ja = JSONArray.parseArray(jsonAry);
            for (int i = 0; i < ja.size(); i++) {
                JSONObject jo = ja.getJSONObject(i);
                Object value = BeanUtil.convertByActType(jo.getString("type"), jo.getString("val"));
                String name=jo.getString("name");
                BeanUtil.setFieldValue(dataSource,name, value);
            }
            dataSource.setName("druid-" + alias);
            dataSource.init();
            return dataSource;
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
        return null;
    }

    /**
     * 添加数据源。
     * @param key           数据源名称
     * @param dataSource    数据源
     * @param replace       是否替换
     * @throws IllegalAccessException
     * @throws NoSuchFieldException
     */
    public static void addDataSource(String key, DataSource dataSource, boolean replace)
            throws IllegalAccessException, NoSuchFieldException {
        DynamicDataSource dynamicDataSource = SpringUtil.getBean(GLOBAL_DATASOURCE);
        if (dynamicDataSource.isDataSourceExist(key)) {
            if (!replace){
                return;
            }
            dynamicDataSource.removeDataSource(key);
        }
        dynamicDataSource.addDataSource(key, dataSource);
    }

    /**
     * 删除数据源
     * @param key
     */
    public static void removeDataSource(String key)
            throws IllegalAccessException, NoSuchFieldException{
        DynamicDataSource dynamicDataSource = SpringUtil.getBean(GLOBAL_DATASOURCE);
        dynamicDataSource.removeDataSource(key);
    }
}
