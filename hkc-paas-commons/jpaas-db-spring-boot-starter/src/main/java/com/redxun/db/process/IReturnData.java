package com.redxun.db.process;

import java.sql.CallableStatement;
import java.sql.SQLException;

/**
 * 存储过程返回值处理。
 */
public interface IReturnData  {

    Object handStatement(CallableStatement statement) throws SQLException;
}
