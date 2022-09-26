package com.redxun.logicdel;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.redxun.common.utils.DbLogicDelete;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

/**
 * 功能: TODO
 *
 * @author ASUS
 * @date 2022/7/19 18:14
 */
public class RxDelete extends AbstractMethod {

    public RxDelete() {

    }

    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        SqlMethod sqlMethod = SqlMethod.LOGIC_DELETE;
        String sql;
        SqlSource sqlSource;
        //逻辑删除
        if (DbLogicDelete.getLogicDelete()) {
            sql = String.format(sqlMethod.getSql(), tableInfo.getTableName(), " SET DELETED_=1 ", this.sqlWhereEntityWrapper(true, tableInfo), this.sqlComment());
            sqlSource = this.languageDriver.createSqlSource(this.configuration, sql, modelClass);
            return this.addUpdateMappedStatement(mapperClass, modelClass, this.getMethod(sqlMethod), sqlSource);
        } else {
            sqlMethod = SqlMethod.DELETE;
            sql = String.format(sqlMethod.getSql(), tableInfo.getTableName(), this.sqlWhereEntityWrapper(true, tableInfo), this.sqlComment());
            sqlSource = this.languageDriver.createSqlSource(this.configuration, sql, modelClass);
            return this.addDeleteMappedStatement(mapperClass, this.getMethod(sqlMethod), sqlSource);
        }
    }
}
