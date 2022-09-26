package com.redxun.common.base.db;

import com.redxun.common.base.entity.SqlModel;
import com.redxun.common.base.search.QueryFilter;
import com.redxun.common.base.search.SqlBuilder;

import java.util.HashMap;
import java.util.Map;

public class PageHelper {

    /**
     * 根据QueryFilter 构建参数Map。
     * @param queryFilter
     * @return
     */
    public static Map<String,Object> constructParams(QueryFilter queryFilter){
        Map<String,Object> params=new HashMap<>();

        SqlModel sqlModel= SqlBuilder.build(queryFilter.getFieldLogic());
        params.put("whereSql",sqlModel.getSql());
        for (Map.Entry<String, Object> ent : sqlModel.getParams().entrySet()) {
            String key =ent.getKey();
            if(key.indexOf(".")!=-1){
                key=key.replace(".","");
            }
            params.put(key,ent.getValue());
        }
        //其他查询字段
        params.putAll(queryFilter.getParams());
        //排序字段
        String sortSql=queryFilter.getSortSql();
        params.put("orderBySql",sortSql);

        return params;
    }
}
