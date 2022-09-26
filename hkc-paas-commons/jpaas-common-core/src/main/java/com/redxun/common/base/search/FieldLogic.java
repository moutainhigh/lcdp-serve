package com.redxun.common.base.search;

import java.util.ArrayList;
import java.util.List;

/**
 * @description 用于动态的参数查询进行各种字段的与或非的组合查询时构建的条件
 * @author csx
 */
public class FieldLogic implements  WhereParam{

    public static String FieldLogic_AND="AND";
    public static String FieldLogic_OR="OR";
    public static String FieldLogic_NOT="NOT";

    public  FieldLogic(){
    }

    public  FieldLogic(String logic_){
        this.logic=logic_;
    }

    private String logic=FieldLogic_AND;

    private List<WhereParam> whereParams=new ArrayList<>();

    public String getLogic() {
        return logic;
    }

    public void setLogic(String logic) {
        this.logic = logic;
    }

    public List<WhereParam> getWhereParams() {
        return whereParams;
    }

    public void setWhereParams(List<WhereParam> whereParams) {
        this.whereParams = whereParams;
    }

    public void addParams(WhereParam whereParam) {
        this.whereParams.add(whereParam);
    }

    @Override
    public String getSql(){
        if(whereParams.size()==0){
            return "";
        }
        if(whereParams.size()==1 && !FieldLogic_NOT.equals(logic)){
            return whereParams.get(0).getSql();
        }
        StringBuffer sqlBuf=new StringBuffer("(");
        int i=0;
        if(whereParams.size()>0 && FieldLogic_NOT.equals(logic)){
            sqlBuf.append(" NOT (");
            for(WhereParam clause:whereParams){
                if(i++>0){
                    sqlBuf.append(" ").append(FieldLogic_AND).append(" ");
                }
                sqlBuf.append(clause.getSql());
            }
            sqlBuf.append(")");

            return sqlBuf.toString();
        }
        for(WhereParam clause:whereParams){
            if(i++>0){
                sqlBuf.append(" ").append( this.logic ).append(" ");
            }
            sqlBuf.append(clause.getSql());
        }
        sqlBuf.append(")");

        return sqlBuf.toString();
    }
}
