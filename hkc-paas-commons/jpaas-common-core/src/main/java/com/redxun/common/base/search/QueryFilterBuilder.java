package com.redxun.common.base.search;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.redxun.common.base.entity.QueryData;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.RequestUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;

/**
 * 根据QueryFilter构建查询条件
 */
public class QueryFilterBuilder {



    /**
     * json 格式
     * {
     *     page:1,
     *     pageSize:20,
     *     sortField:"",
     *     sortOrder:"desc",
     *     params:{
     *         Q_NAME_S_EQ:"RAY",
     *         AGE:20
     *     }
     * }
     * @param queryData
     * @return
     */
    public static QueryFilter createQueryFilter(QueryData queryData){
        QueryFilter queryFilter=new QueryFilter();
        if(queryData==null){
            return queryFilter;
        }
        Integer pageIndex = queryData.getPageNo()!=null ? new Integer(queryData.getPageNo()) : 0;
        Integer pageSize = queryData.getPageSize()!=null ? new Integer(queryData.getPageSize()) : QueryFilter.DEFAULT_PAGE_SIZE;
        IPage page=new Page(pageIndex,pageSize);
        queryFilter.setPage(page);
        //设置查询条件
        getSqlQueryParams(queryData,queryFilter);

        if(BeanUtil.isNotEmpty(queryData.getSortField())){
            if(queryData.getSortField().length()>30){
                throw new RuntimeException("排序字段过长");
            }

            if(queryData.getSortOrder().length()>20){
                throw new RuntimeException("排序过长");
            }
            if(!StringUtils.isSqlInject(queryData.getSortField())
                    && !StringUtils.isSqlInject(queryData.getSortOrder()) ){
                SortParam sortParam=new SortParam(queryData.getSortField(),queryData.getSortOrder());
                queryFilter.addSortParam(sortParam);
            }

        }
        return  queryFilter;
    }


    /**
     * 根据请求构建QueryFilter
     * @param request
     * @return
     */
    public static QueryFilter createQueryFilter(HttpServletRequest request){
        QueryFilter queryFilter=new QueryFilter();

        Integer pageIndex = RequestUtil.getInt(request, "pageNo", 0);
        Integer pageSize = RequestUtil.getInt(request, "pageSize", QueryFilter.DEFAULT_PAGE_SIZE);

        IPage page=null;
        if(pageIndex>0){
            page=new Page(pageIndex,pageSize);
        }
        else{
            page=new Page(1,pageSize);
        }
        queryFilter.setPage(page);

        //设置查询条件
        getSqlQueryParams(request,queryFilter);

        //排序
        String sortField = RequestUtil.getString(request,"sortField","");
        String sortOrder = RequestUtil.getString(request,"sortOrder","desc");
        if(sortField.length()>30){
            throw new RuntimeException("排序字段过长");
        }

        if(sortOrder.length()>20){
            throw new RuntimeException("排序过长");
        }

        if(StringUtils.isSqlInject(sortField)){
            throw new RuntimeException("排序字段有SQL注入");
        }

        if(StringUtils.isSqlInject(sortOrder)){
            throw new RuntimeException("排序有SQL注入");
        }

        if(StringUtils.isNotEmpty(sortField)){
            SortParam sortParam=new SortParam(sortField,sortOrder);
            queryFilter.addSortParam(sortParam);
        }
        return  queryFilter;
    }

    /**
     * 创建自定义的SQL查询参数
     * @param queryData
     * @param queryFilter
     */
    private static void getSqlQueryParams(QueryData queryData,QueryFilter queryFilter){

        if(BeanUtil.isEmpty(queryData.getParams())){
            return;
        }

       Iterator<Map.Entry<String,String>> paramsIt=queryData.getParams().entrySet().iterator();
        while (paramsIt.hasNext()){
            Map.Entry<String,String> keyVals=paramsIt.next();
            if(BeanUtil.isEmpty(keyVals.getValue())){
                continue;
            }
            String key=keyVals.getKey();
            if(!key.startsWith("Q_")){
                if(!StringUtils.isSqlInject(keyVals.getValue()) ){
                    queryFilter.addParam(keyVals.getKey(),keyVals.getValue());
                }
            }else{
                if(key.indexOf("_display")!=-1){
                    continue;
                }
                QueryParam queryParam= QueryFilter.getQueryParams(keyVals.getKey(),keyVals.getValue());
                queryFilter.addQueryParam(queryParam);
            }
        }
    }

    /**
     * 获取上下文参数
     * @param request
     * @param queryFilter
     */
    private static void getSqlQueryParams(HttpServletRequest request,QueryFilter queryFilter){

        Enumeration<?> params = request.getParameterNames();
        while (params.hasMoreElements()) {
            String key = params.nextElement().toString();
            String[] values = request.getParameterValues(key);
            if(!key.startsWith("Q_") ){
                if(!"sortField".equals(key) &&  !"sortOrder".equals(key)){
                    if(values.length > 0 && StringUtils.isNotEmpty(values[0]) && !StringUtils.isSqlInject(values[0])){
                        queryFilter.addParam(key,values[0]);
                    }
                }
            }
            else if(values.length > 0 && StringUtils.isNotEmpty(values[0])) {
                if(key.indexOf("_display")==-1 && !StringUtils.isSqlInject(values[0])){
                    QueryParam queryParam= QueryFilter.getQueryParams(key, values[0]);
                    queryFilter.addQueryParam(queryParam);
                }
            }
        }
    }

    public static QueryFilter buildQueryFilter(){
        return new QueryFilter();
    }


}
