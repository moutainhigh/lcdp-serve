package com.redxun.logicdel;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.redxun.common.utils.DbLogicDelete;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

import java.util.Map;

public class RxDeleteByMap extends AbstractMethod {
    public RxDeleteByMap() {
    }

    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        SqlMethod sqlMethod = SqlMethod.LOGIC_DELETE_BY_MAP;
        String sql;
        SqlSource sqlSource;
        //逻辑删除
        if (DbLogicDelete.getLogicDelete()) {
            sql = String.format(sqlMethod.getSql(), tableInfo.getTableName(), " SET DELETED_=1 ", this.sqlWhereByMap(tableInfo));
            sqlSource = this.languageDriver.createSqlSource(this.configuration, sql, Map.class);
            return this.addUpdateMappedStatement(mapperClass, Map.class, this.getMethod(sqlMethod), sqlSource);
        } else {
            sqlMethod = SqlMethod.DELETE_BY_MAP;
            sql = String.format(sqlMethod.getSql(), tableInfo.getTableName(), this.sqlWhereByMap(tableInfo));
            sqlSource = this.languageDriver.createSqlSource(this.configuration, sql, Map.class);
            return this.addDeleteMappedStatement(mapperClass, this.getMethod(sqlMethod), sqlSource);
        }
    }
}
