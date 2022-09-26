package com.redxun.common.base.search;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.redxun.common.base.entity.SqlModel;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 用于动态构建SQL条件，通过组合各字段的查询，构建SQL中的where条件
 */
public class QueryFilter {

    private static Pattern regex3 = Pattern.compile("Q_(.+)_(.+)_(.+)", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.DOTALL | Pattern.MULTILINE);

    private static Pattern regex2 = Pattern.compile("Q_(.+)_(.+)", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.DOTALL | Pattern.MULTILINE);

    private static Pattern regex1 = Pattern.compile("Q_(.+)", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.DOTALL | Pattern.MULTILINE);


    public static int DEFAULT_PAGE_SIZE=20;

    private  IPage page=new Page(1, DEFAULT_PAGE_SIZE);

    //自动构建查询
    private FieldLogic fieldLogic=new FieldLogic();
    //不是Q_开头的参数
    private Map<String,Object> params=new HashMap<>();
    //排序
    private List<SortParam> sortParams=new ArrayList<>();

    /**
     * 加上查询条件
     * @param param
     */
    public  void addQueryParam(QueryParam param){
        fieldLogic.addParams(param);
    }

    /**
     * 返加SQL Model对象
     * @return
     */
    public SqlModel getSqlModel(){
        return SqlBuilder.build(fieldLogic);
    }
    /**
     * 获取分页对象
     */
    public IPage getPage() {
        return page;
    }

    /**
     * 设置分页对象
     * @param page
     */
    public void setPage(IPage page) {
        this.page = page;
    }

    /**
     * 返回根字段逻辑条件
     * @return
     */
    public FieldLogic getFieldLogic() {
        return fieldLogic;
    }

    /**
     * 设置根字段逻辑条件
     * @param fieldLogic
     */
    public void setFieldLogic(FieldLogic fieldLogic) {
        this.fieldLogic = fieldLogic;
    }

    /**
     * 批量添加字段参数
     * @param list
     */
    public  void addQueryParams(List<QueryParam> list){
        for(QueryParam param:list){
            this.fieldLogic.addParams(param);
        }
    }

    /**
     * 添加单个字段查询
     * @param fieldName 格式如 Q_name__S_LK
     * @param val 值
     */
    public void addQueryParam(String fieldName,String val){
        QueryParam queryParam=getQueryParams(fieldName,val);
        if(queryParam!=null){
            this.fieldLogic.addParams(queryParam);
        }
    }

    /**
     * 获取排序参数
     * @return
     */
    public List<SortParam> getSortParams() {
        return sortParams;
    }

    /**
     * 设置多个排序字段
     * @param sortParams
     */
    public void setSortParams(List<SortParam> sortParams) {
        this.sortParams = sortParams;
    }

    /**
     * 添国排序字段
     * @param sortParam
     */
    public void addSortParam(SortParam sortParam) {
        this.sortParams.add(sortParam);
    }

    /**
     * 根据FieldLogic返回查询参数字段
     * @param logic
     * @return
     */
    private Map<String,QueryParam> getQueryParams(FieldLogic logic){
        Map<String,QueryParam> queryParams=new HashMap<String,QueryParam>();
        for(int i=0;i<logic.getWhereParams().size();i++){
            WhereParam param=logic.getWhereParams().get(i);
            if(param instanceof QueryParam){
                QueryParam p=(QueryParam)param;
                String name=p.getFieldName();
                if(StringUtils.isNotEmpty(p.getParamName())){
                    name=p.getParamName();
                }
                queryParams.put(name, p);
            }else if(param instanceof FieldLogic){
                queryParams.putAll(getQueryParams((FieldLogic)param));
            }
        }
        return queryParams;
    }

    /**
     * 返回查询参数
     * @return
     */
    public Map<String, QueryParam> getQueryParams() {
        return getQueryParams(fieldLogic);
    }

    /**
     * 获取参数
     * @return
     */
    public Map<String, Object> getParams() {
        Map<String,QueryParam>  paramMap=getQueryParams();
        for(QueryParam p:paramMap.values()){
            String name=p.getFieldName();
            if(StringUtils.isNotEmpty(p.getParamName())){
                name=p.getParamName();
            }
            params.put(name, p.getValue());
        }
        return params;
    }

    /**
     * 设置参数
     * @param params
     */
    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    /**
     * 设置参数
     */
    public void setParams(){
        List<WhereParam> list=fieldLogic.getWhereParams();
        if(BeanUtil.isEmpty(list)){
            return;
        }
        for(WhereParam tmp:list){
            if(!(tmp instanceof QueryParam)){
                continue;
            }
            QueryParam param=(QueryParam)tmp;
            String name=param.getFieldName();
            if(StringUtils.isNotEmpty(param.getParamName())){
                name=param.getParamName();
            }
            addParam(name,param.getValue());
        }
    }

    public void addParam(String key,Object val){
        this.params.put(key,val);
    }

    /**
     * 获取MyBatis中的Where传入的各动态参数
     * @return
     */
    public Map<String,Object> getSearchParams(){
        Map<String,Object> params=new HashMap<>();
        SqlModel sqlModel= SqlBuilder.build(this.fieldLogic);
        params.put("whereSql",sqlModel.getSql());
        for (Map.Entry<String, Object> ent : sqlModel.getParams().entrySet()) {
            params.put(ent.getKey(),ent.getValue());
        }
        //其他查询字段
        params.putAll(getParams());
        //排序字段
        String sortSql=getSortSql();
        params.put("orderBySql",sortSql);
        return  params;
    }

    /**
     * 通过在线的字段及值进行构建查询条件
     * @param key
     * @param val
     * @return
     */
    public static QueryParam getQueryParams(String key,String val){
        QueryParam queryParam=new QueryParam();
        if(StringUtils.isEmpty(val) || !key.startsWith("Q_")){
            return null;
        }
        Matcher regexMatcher = regex3.matcher(key);
        Object pVal=null;
        String name="";
        if(regexMatcher.find()){
            name=regexMatcher.group(1);
            String type=regexMatcher.group(2);
            String opType=regexMatcher.group(3);
            pVal=QueryParam.getObjValue(opType,type,val);

            queryParam.setFieldName(name);
            queryParam.setFieldType(type);
            queryParam.setOpType(opType);
            queryParam.setValue(pVal);

        }
        else {
            regexMatcher = regex2.matcher(key);
            if(regexMatcher.find()){
                name=regexMatcher.group(1);
                String opType=regexMatcher.group(2);
                pVal=QueryParam. getObjValue(opType,QueryParam.FIELD_TYPE_STRING,val);

                queryParam.setFieldName(name);
                queryParam.setOpType(opType);
                queryParam.setValue(pVal);
            }
            else{
                regexMatcher = regex1.matcher(key);
                if(regexMatcher.find()){
                    name=regexMatcher.group(1);
                    pVal=QueryParam.getObjValue(QueryParam.OP_EQUAL,QueryParam.FIELD_TYPE_STRING,val);

                    queryParam.setFieldName(name);
                    queryParam.setValue(pVal);
                }
            }
        }
        return  queryParam;
    }
    /**
     * 获取排序SQL
     * @return
     */
    public String getSortSql(){
        String sql="";
        int i=0;
        for(SortParam sortParam:this.sortParams){
            if(i>0){
                sql+=",";
            }
            sql+=sortParam.getProperty() +" " + sortParam.getDirection();
            i++;
        }
        return  sql;
    }
}
