package com.redxun.db.process;

import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.StringUtils;
import com.redxun.db.CommonDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.StringReader;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 存储过程工具类
 */
@Component
@Slf4j
public class ProcedureHepler {


    /**
     * 调用存储过程，这种是无参数的存储过程。
     * @param procName
     * @throws Exception
     */
    public void callProc(String procName) throws Exception {
        callProc(  procName, null);
    }

    /**
     * 调用存储过程，这种只有输入参数的存储过程。
     * @param procName
     * @param parameterList
     * @throws Exception
     */
    public void callProc(String procName, List<ProcParameter> parameterList) throws Exception {
        callProc( "",  procName, parameterList);
    }

    /**
     * 调用存储过程，这种只有输入参数的存储过程。
     * @param dbAlias			数据源
     * @param procName			存储过程名称
     * @param parameterList		存储过程参数
     * @throws Exception
     */
    public void callProc(String dbAlias, String procName,List<ProcParameter> parameterList) throws Exception {
        callProc( dbAlias,  procName, parameterList,null);
    }


    /**
     * 调用存储过程有返回值的情况。
     * @param procName
     * @param parameterList
     * @param returnData
     * @throws Exception
     * @return
     */
    public Object callProc(String procName, List<ProcParameter> parameterList, IReturnData returnData) throws Exception {
        Object rtn= callProc( "",  procName, parameterList,returnData);
        return rtn;
    }

    /**
     * 调用存储过程方法，这种支持返回值。
     * @param dbAlias		数据源别名
     * @param procName		存储过程名称
     * @param parameterList	参数定义，这个参数定义是有顺序的。
     * @param returnData	返回值处理。
     * @return
     * @throws Exception
     */
    public Object callProc(String dbAlias, String procName, List<ProcParameter> parameterList, IReturnData  returnData) throws Exception {

        DataSource dataSource=CommonDao.getDataSource(dbAlias);
        JdbcTemplate jdbcTemplate=new JdbcTemplate(dataSource);
        Object rtn =  jdbcTemplate.execute(new CallableStatementCreator() {

            @Override
            public CallableStatement createCallableStatement(Connection connection) throws SQLException {
                String paramter=getParameter(parameterList);
                String process="{ call "+procName+paramter+ "}";
                CallableStatement statement = connection.prepareCall(process);
                int index=0;
                for(ProcParameter parameter:parameterList){
                    index++;
                    //设置参数
                    setParameter(statement,parameter,index);
                }
                return statement;
            }
        }, new CallableStatementCallback() {
            @Override
            public Object doInCallableStatement(CallableStatement callableStatement) throws SQLException, DataAccessException {
                callableStatement.execute();
                Object rtn=null;
                if(returnData!=null){
                    rtn=returnData.handStatement(callableStatement);
                }
                return rtn;
            }
        });
        return rtn;
    }

    /**
     * 设置请求参数。
     * @param statement
     * @param parameter
     * @param index
     * @throws SQLException
     */
    private void setParameter(CallableStatement statement, ProcParameter parameter, int index) throws SQLException {

        if(parameter.isInput()){
            if(parameter.getParameterType().equals(ParameterType.INT)){
                statement.setInt(index,((Integer)parameter.getValue()).intValue());
            }
            else if(parameter.getParameterType().equals(ParameterType.DATE)){
                Date date= (Date)parameter.getValue();
                java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                statement.setDate(index,sqlDate);
            }
            else if(parameter.getParameterType().equals(ParameterType.DOUBLE)){
                Double date= (Double)parameter.getValue();

                statement.setDouble(index,date);
            }
            else if(parameter.getParameterType().equals(ParameterType.STRING)){
                String str= (String)parameter.getValue();
                statement.setString(index,str);
            }
            else if(parameter.getParameterType().equals(ParameterType.TEXT)){
                String str= (String)parameter.getValue();
                StringReader reader=new StringReader(str);
                statement.setCharacterStream(index,reader);
            }
        }
        else{
            statement.registerOutParameter(index, parameter.getSqlType());
        }
    }

    /**
     * 构造参数。
     * @param parameterList
     * @return
     */
    private String getParameter(List<ProcParameter> parameterList){
        if(BeanUtil.isEmpty(parameterList)){
            return "()";
        }
        List<String> list=new ArrayList<>();
        for(ProcParameter proc:parameterList){
            list.add("?");
        }
        return "(" + StringUtils.join(list,",") +")";
    }
}
