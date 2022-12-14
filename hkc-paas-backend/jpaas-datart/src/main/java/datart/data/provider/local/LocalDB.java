/*
 * Datart
 * <p>
 * Copyright 2021
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package datart.data.provider.local;

import datart.core.base.PageInfo;
import datart.core.base.consts.Const;
import datart.core.base.exception.Exceptions;
import datart.core.common.Application;
import datart.core.common.UUIDGenerator;
import datart.core.data.provider.*;
import datart.data.provider.calcite.dialect.H2Dialect;
import datart.data.provider.jdbc.DataTypeUtils;
import datart.data.provider.jdbc.ResultSetMapper;
import datart.data.provider.jdbc.SqlScriptRender;
import lombok.extern.slf4j.Slf4j;
import org.apache.calcite.sql.SqlDialect;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.h2.jdbc.JdbcSQLNonTransientException;
import org.h2.tools.DeleteDbFiles;
import org.h2.tools.SimpleResultSet;

import java.sql.*;
import java.util.Date;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class LocalDB {

    private static final String MEM_URL = "jdbc:h2:mem:/";

    private static final String H2_PARAM = ";LOG=0;DATABASE_TO_UPPER=false;MODE=MySQL;CASE_INSENSITIVE_IDENTIFIERS=TRUE;CACHE_SIZE=65536;LOCK_MODE=0;UNDO_LOG=0";


    public static final SqlDialect SQL_DIALECT = new H2Dialect();

    private static final String SELECT_START_SQL = "SELECT * FROM `%s` ";

    private static final String CREATE_TEMP_TABLE = "CREATE TABLE IF NOT EXISTS `%s` AS (SELECT * FROM FUNCTION_TABLE('%s'))";

    private static final String CACHE_EXPIRE_TABLE_SQL = "CREATE TABLE IF NOT EXISTS `cache_expire` ( `source_id` VARCHAR(128),`expire_time` DATETIME )";

    private static final String SET_EXPIRE_SQL = "INSERT INTO `cache_expire` VALUES( '%s', PARSEDATETIME('%s','%s')) ";

    private static final String DELETE_EXPIRE_SQL = "DELETE FROM `cache_expire` WHERE `source_id`='%s' ";

    private static final Map<String, Dataframe> TEMP_RS_CACHE = new ConcurrentHashMap<>();

    static {
        init();
    }

    private static void init() {
        try {
            Class.forName("org.h2.Driver");
            try (Connection connection = getConnection(true, null)) {
                Statement statement = connection.createStatement();
                statement.execute(CACHE_EXPIRE_TABLE_SQL);
            }
        } catch (Exception e) {
            log.error("H2 init error", e);
        }
    }

    /**
     * ?????????????????????????????????Dataframe ???????????? ResultSet.
     *
     * @param conn   ResultSet ????????????
     * @param dataId ResultSet ?????? Dataframe
     */
    public static ResultSet dataframeTable(Connection conn, String dataId) throws SQLException {
        Dataframe dataframe = TEMP_RS_CACHE.get(dataId);
        if (dataframe == null) {
            Exceptions.msg("The dataframe " + dataId + " does not exist");
        }
        SimpleResultSet rs = new SimpleResultSet();
        if (!CollectionUtils.isEmpty(dataframe.getColumns())) {
            // add columns
            for (Column column : dataframe.getColumns()) {
                rs.addColumn(column.columnName(), DataTypeUtils.valueType2SqlTypes(column.getType()), -1, -1);
            }
        }
        if (conn.getMetaData().getURL().equals("jdbc:columnlist:connection")) {
            return rs;
        }
        // add rows
        if (!CollectionUtils.isEmpty(dataframe.getRows())) {
            for (List<Object> row : dataframe.getRows()) {
                rs.addRow(row.toArray());
            }
        }
        return rs;
    }

    /**
     * ??????????????????????????????????????????SQL??????
     *
     * @param dataframe ???????????????
     */
    private static void registerDataAsTable(Dataframe dataframe, Connection connection) throws SQLException {
        if (Objects.isNull(dataframe)) {
            Exceptions.msg("Empty data cannot be registered as a temporary table");
        }

        // ???????????????
        dataframe.getRows().parallelStream().forEach(row -> {
            for (int i = 0; i < row.size(); i++) {
                Object val = row.get(i);
                if (val instanceof String && StringUtils.isBlank(val.toString())) {
                    row.set(i, null);
                }
            }
        });

        createFunctionTableIfNotExists(connection);

        TEMP_RS_CACHE.put(dataframe.getId(), dataframe);
        // register temporary table
        String sql = String.format(CREATE_TEMP_TABLE, dataframe.getName(), dataframe.getId());
        try {
            connection.prepareStatement(sql).execute();
        } catch (JdbcSQLNonTransientException e) {
            //????????????????????????????????????
        }
    }

    /**
     * ??????????????????
     *
     * @param dataId data id
     */
    private static void unregisterData(String dataId) {
        TEMP_RS_CACHE.remove(dataId);
    }

    private static void createFunctionTableIfNotExists(Connection connection) {
        try {
            Statement statement = connection.createStatement();
            statement.execute("CREATE ALIAS FUNCTION_TABLE  FOR \"datart.data.provider.local.LocalDB.dataframeTable\"");
        } catch (SQLException ignored) {
        }
    }

    public static Dataframe executeLocalQuery(QueryScript queryScript, ExecuteParam executeParam, Dataframes dataframes) throws Exception {
        return executeLocalQuery(queryScript, executeParam, dataframes, false, null);
    }

    /**
     * ???????????????????????????????????????????????????????????????H2?????????????????????H2??????????????????SQL??????????????????
     *
     * @param queryScript  ????????????
     * @param executeParam ????????????
     * @param dataframes   ????????????
     * @param persistent   ????????????????????????
     * @return ????????????+???????????? ???????????????
     */
    public static Dataframe executeLocalQuery(QueryScript queryScript, ExecuteParam executeParam, Dataframes dataframes, boolean persistent, Date expire) throws Exception {
        if (queryScript == null || (dataframes.size() == 1 && dataframes.getDataframes().get(0).getName() == null)) {
            // ??????????????????????????????????????????????????????????????????SQL??????????????????
            queryScript = new QueryScript();
            if (dataframes.getDataframes().get(0).getName() == null) {
                dataframes.getDataframes().get(0).setName("Q" + UUIDGenerator.generate());
            }
            queryScript.setScript(String.format(SELECT_START_SQL, dataframes.getDataframes().get(0).getName()));
            queryScript.setVariables(Collections.emptyList());
            queryScript.setSourceId(dataframes.getKey());
            queryScript.setScriptType(ScriptType.SQL);
        }

        String url = getConnectionUrl(persistent, dataframes.getKey());
        synchronized (url.intern()) {
            return persistent ? executeInLocalDB(queryScript, executeParam, dataframes, expire) : executeInMemDB(queryScript, executeParam, dataframes);
        }
    }

    /**
     * ????????????????????????????????????????????????????????????????????????????????????????????????
     */
    private static Dataframe executeInMemDB(QueryScript queryScript, ExecuteParam executeParam, Dataframes dataframes) throws Exception {
        Connection connection = getConnection(false, dataframes.getKey());
        try {
            for (Dataframe dataframe : dataframes.getDataframes()) {
                registerDataAsTable(dataframe, connection);
            }
            return execute(connection, queryScript, executeParam);
        } finally {
            try {
                connection.close();
            } catch (Exception e) {
                log.error("connection close error ", e);
            }
            for (Dataframe df : dataframes.getDataframes()) {
                unregisterData(df.getId());
            }
        }

    }

    /**
     * ????????????????????????????????????H2????????????????????????
     */
    private static Dataframe executeInLocalDB(QueryScript queryScript, ExecuteParam executeParam, Dataframes dataframes, Date expire) throws Exception {
        try (Connection connection = getConnection(true, dataframes.getKey())) {
            if (!dataframes.isEmpty()) {

                for (Dataframe dataframe : dataframes.getDataframes()) {
                    registerDataAsTable(dataframe, connection);
                }

                if (expire != null) {
                    setCacheExpire(dataframes.getKey(), expire);
                }

            }
            return execute(connection, queryScript, executeParam);
        }
    }

    /**
     * ????????????????????????????????????????????????,????????????
     *
     * @param cacheKey source ????????????
     */
    public static boolean checkCacheExpired(String cacheKey) throws SQLException {
        try (Connection connection = getConnection(true, null)) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM `cache_expire` WHERE `source_id`='" + cacheKey + "'");
            if (resultSet.next()) {
                Timestamp cacheExpire = resultSet.getTimestamp("expire_time");
                if (cacheExpire.after(new java.util.Date())) {
                    return false;
                }
                clearCache(cacheKey);
            }
        }
        return true;
    }

    private static void setCacheExpire(String sourceId, java.util.Date date) throws SQLException {
        try (Connection connection = getConnection(true, null)) {
            Statement statement = connection.createStatement();
            // delete first
            statement.execute(String.format(DELETE_EXPIRE_SQL, statement));
            // insert expire
            String sql = String.format(SET_EXPIRE_SQL, sourceId, DateFormatUtils.format(date, Const.DEFAULT_DATE_FORMAT), Const.DEFAULT_DATE_FORMAT);
            statement.execute(sql);
        }
    }

    public static void clearCache(String cacheKey) throws SQLException {
        try (Connection connection = getConnection(true, null)) {
            connection.createStatement().execute(String.format(DELETE_EXPIRE_SQL, cacheKey));
            DeleteDbFiles.execute(getDbFileBasePath(), cacheKey, false);
        }
    }

    private static Dataframe execute(Connection connection, QueryScript queryScript, ExecuteParam executeParam) throws Exception {

        SqlScriptRender render = new SqlScriptRender(queryScript
                , executeParam
                , SQL_DIALECT);

        String sql = render.render(true, false, false);

        log.debug(sql);

        ResultSet resultSet = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY).executeQuery(sql);
        PageInfo pageInfo = executeParam.getPageInfo();
        resultSet.last();
        pageInfo.setTotal(resultSet.getRow());
        resultSet.first();

        resultSet.absolute((int) Math.min(pageInfo.getTotal(), (pageInfo.getPageNo() - 1) * pageInfo.getPageSize()));
        Dataframe dataframe = ResultSetMapper.mapToTableData(resultSet, pageInfo.getPageSize());
        dataframe.setPageInfo(pageInfo);
        dataframe.setScript(sql);
        return dataframe;

    }

    private static Connection getConnection(boolean persistent, String database) throws SQLException {
        return DriverManager.getConnection(getConnectionUrl(persistent, database));
    }


    private static String getConnectionUrl(boolean persistent, String database) {
        return persistent ? getDatabaseUrl(database) : MEM_URL + "DB" + database + H2_PARAM;
    }

    private static String getDatabaseUrl(String database) {
        if (database == null) {
            database = "datart_meta";
        }
        return String.format("jdbc:h2:file:%s/%s" + H2_PARAM, getDbFileBasePath(), database);
    }

    private static String getDbFileBasePath() {
        return Application.getFileBasePath() + "h2/dbs";
    }

}
