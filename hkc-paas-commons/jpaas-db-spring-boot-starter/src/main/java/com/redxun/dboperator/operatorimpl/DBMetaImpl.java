package com.redxun.dboperator.operatorimpl;

import com.redxun.dboperator.*;
import com.redxun.dboperator.model.DefaultTable;
import com.redxun.dboperator.model.Table;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class DBMetaImpl implements IDbMeta {

    @Override
    public List<Table> getObjectsByName(String name) throws Exception {
        ITableMeta iTableMeta=TableMetaContext.getCurrentTableMeta();
        Map<String,String> tables=iTableMeta.getTablesByName(name);
        IViewOperator iViewOperator= OperatorContext.getCurrentViewOperator();
        List<String> views=iViewOperator.getViews(name);
        List<Table> tableList=new ArrayList<>();

        //处理表
        for(Map.Entry<String,String> table : tables.entrySet()){
            Table tmpTable=new DefaultTable();
            tmpTable.setTableName(table.getKey());
            tmpTable.setType(Table.TYPE_TABLE);
            tmpTable.setComment(table.getValue());
            tableList.add(tmpTable);
        }

        //处理视图
        for(String viewName : views){
            Table tmpTable=new DefaultTable();
            tmpTable.setTableName(viewName);
            tmpTable.setType(Table.TYPE_VIEW);
            tmpTable.setComment(viewName);
            tableList.add(tmpTable);
        }
        return tableList;
    }

    @Override
    public Table getByName(String name) {
        return null;
    }

    @Override
    public Table getModelByName(String name) throws Exception {
        ITableMeta iTableMeta=TableMetaContext.getCurrentTableMeta();
        Table table=iTableMeta.getTableByName(name);
        if(table!=null){
            return table;
        }
        IViewOperator iViewOperator=OperatorContext.getCurrentViewOperator();
        Table view=iViewOperator.getModelByViewName(name);
        return view;
    }
}
