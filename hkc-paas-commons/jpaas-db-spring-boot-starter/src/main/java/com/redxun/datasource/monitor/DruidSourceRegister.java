package com.redxun.datasource.monitor;

import com.alibaba.druid.pool.DruidDataSource;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;

import java.util.function.ToDoubleFunction;

/**
 * 增加普罗米修斯数据源监控。
 * @author ray
 */
public class DruidSourceRegister {

    private static final String LABEL_NAME = "pool";

    /**
     * 注册数据源。
     * @param druidDataSource
     * @param registry
     */
    public  static void  registerDataSource(DruidDataSource druidDataSource, MeterRegistry registry){

        // basic configurations
        createGauge(druidDataSource, "druid_initial_size", "Initial size", (datasource) -> (double) druidDataSource.getInitialSize(),registry);
        createGauge(druidDataSource, "druid_min_idle", "Min idle", datasource -> (double) druidDataSource.getMinIdle(),registry);
        createGauge(druidDataSource, "druid_max_active", "Max active", datasource -> (double) druidDataSource.getMaxActive(),registry);

        // connection pool core metrics
        createGauge(druidDataSource, "druid_active_count", "Active count", datasource -> (double) druidDataSource.getActiveCount(),registry);
        createGauge(druidDataSource, "druid_active_peak", "Active peak", datasource -> (double) druidDataSource.getActivePeak(),registry);
        createGauge(druidDataSource, "druid_pooling_peak", "Pooling peak", datasource -> (double) druidDataSource.getPoolingPeak(),registry);
        createGauge(druidDataSource, "druid_pooling_count", "Pooling count", datasource -> (double) druidDataSource.getPoolingCount(),registry);
        createGauge(druidDataSource, "druid_wait_thread_count", "Wait thread count", datasource -> (double) druidDataSource.getWaitThreadCount(),registry);

        // connection pool detail metrics
        createGauge(druidDataSource, "druid_not_empty_wait_count", "Not empty wait count", datasource -> (double) druidDataSource.getNotEmptyWaitCount(),registry);
        createGauge(druidDataSource, "druid_not_empty_wait_millis", "Not empty wait millis", datasource -> (double) druidDataSource.getNotEmptyWaitMillis(),registry);
        createGauge(druidDataSource, "druid_not_empty_thread_count", "Not empty thread count", datasource -> (double) druidDataSource.getNotEmptyWaitThreadCount(),registry);

        createGauge(druidDataSource, "druid_logic_connect_count", "Logic connect count", datasource -> (double) druidDataSource.getConnectCount(),registry);
        createGauge(druidDataSource, "druid_logic_close_count", "Logic close count", datasource -> (double) druidDataSource.getCloseCount(),registry);
        createGauge(druidDataSource, "druid_logic_connect_error_count", "Logic connect error count", datasource -> (double) druidDataSource.getConnectErrorCount(),registry);
        createGauge(druidDataSource, "druid_physical_connect_count", "Physical connect count", datasource -> (double) druidDataSource.getCreateCount(),registry);
        createGauge(druidDataSource, "druid_physical_close_count", "Physical close count", datasource -> (double) druidDataSource.getDestroyCount(),registry);
        createGauge(druidDataSource, "druid_physical_connect_error_count", "Physical connect error count", datasource -> (double) druidDataSource.getCreateErrorCount(),registry);

        // sql execution core metrics
        createGauge(druidDataSource, "druid_error_count", "Error count", datasource -> (double) druidDataSource.getErrorCount(),registry);
        createGauge(druidDataSource, "druid_execute_count", "Execute count", datasource -> (double) druidDataSource.getExecuteCount(),registry);
        // transaction metrics
        createGauge(druidDataSource, "druid_start_transaction_count", "Start transaction count", datasource -> (double) druidDataSource.getStartTransactionCount(),registry);
        createGauge(druidDataSource, "druid_commit_count", "Commit count", datasource -> (double) druidDataSource.getCommitCount(),registry);
        createGauge(druidDataSource, "druid_rollback_count", "Rollback count", datasource -> (double) druidDataSource.getRollbackCount(),registry);

        // sql execution detail
        createGauge(druidDataSource, "druid_prepared_statement_open_count", "Prepared statement open count", datasource -> (double) druidDataSource.getPreparedStatementCount(),registry);
        createGauge(druidDataSource, "druid_prepared_statement_closed_count", "Prepared statement closed count", datasource -> (double) druidDataSource.getClosedPreparedStatementCount(),registry);
        createGauge(druidDataSource, "druid_ps_cache_access_count", "PS cache access count", datasource -> (double) druidDataSource.getCachedPreparedStatementAccessCount(),registry);
        createGauge(druidDataSource, "druid_ps_cache_hit_count", "PS cache hit count", datasource -> (double) druidDataSource.getCachedPreparedStatementHitCount(),registry);
        createGauge(druidDataSource, "druid_ps_cache_miss_count", "PS cache miss count", datasource -> (double) druidDataSource.getCachedPreparedStatementMissCount(),registry);
        createGauge(druidDataSource, "druid_execute_query_count", "Execute query count", datasource -> (double) druidDataSource.getExecuteQueryCount(),registry);
        createGauge(druidDataSource, "druid_execute_update_count", "Execute update count", datasource -> (double) druidDataSource.getExecuteUpdateCount(),registry);
        createGauge(druidDataSource, "druid_execute_batch_count", "Execute batch count", datasource -> (double) druidDataSource.getExecuteBatchCount(),registry);

        // none core metrics, some are static configurations
        createGauge(druidDataSource, "druid_max_wait", "Max wait", datasource -> (double) druidDataSource.getMaxWait(),registry);
        createGauge(druidDataSource, "druid_max_wait_thread_count", "Max wait thread count", datasource -> (double) druidDataSource.getMaxWaitThreadCount(),registry);
        createGauge(druidDataSource, "druid_login_timeout", "Login timeout", datasource -> (double) druidDataSource.getLoginTimeout(),registry);
        createGauge(druidDataSource, "druid_query_timeout", "Query timeout", datasource -> (double) druidDataSource.getQueryTimeout(),registry);
        createGauge(druidDataSource, "druid_transaction_query_timeout", "Transaction query timeout", datasource -> (double) druidDataSource.getTransactionQueryTimeout(),registry);
    }


    private static void createGauge(DruidDataSource weakRef, String metric, String help, ToDoubleFunction<DruidDataSource> measure,MeterRegistry registry) {
        Gauge.builder(metric, weakRef, measure)
                .description(help)
                .tag(LABEL_NAME, weakRef.getName())
                .register(registry);
    }
}
