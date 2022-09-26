package com.redxun.dboperator.operatorimpl.kingbase;


import com.redxun.dboperator.operatorimpl.oracle.OracleTableOperator;
import org.springframework.stereotype.Component;

/**
 * oracle 数据库表操作的接口实现。
 *
 * <pre>
 * 作者：redxun
 * 版权：广州红迅软件有限公司版权所有
 * </pre>
 *
 */
@Component(value="kingbase_TableOperator")
public class KingbaseTableOperator extends OracleTableOperator {


    @Override
    public String getDropTableSql(String tableName) {

        String sql = "drop table if exists " + tableName ;
        return sql;
    }

}
