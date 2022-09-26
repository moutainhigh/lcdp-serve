package com.redxun.form.core.grid.column;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.redxun.common.base.entity.GridHeader;
import com.redxun.common.base.entity.SqlModel;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.StringUtils;
import com.redxun.db.CommonDao;
import com.redxun.dto.sys.SysDicDto;
import com.redxun.feign.SysDicClient;
import com.redxun.form.core.grid.enums.MiniGridColumnType;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 表格列表中显示标签列的值展示
 * @author mansan
 *
 */
@Component
public class MiniGridColumnRenderSearch implements MiniGridColumnRender{

    @Resource
    CommonDao commonDao;
    @Resource
    SysDicClient sysDicClient;

    @Override
    public String getRenderType() {
        return MiniGridColumnType.SEARCH.name();
    }

    @Override
    public String render(GridHeader gridHeader, Map<String, Object> rowData, Object val, boolean isExport) {
        if(val==null){
            return "";
        }
        JSONObject confObj=gridHeader.getRenderConfObj();
        String type = confObj.getString("type");
        if("table".equals(type)){
            return  handSql(confObj,rowData,val,isExport);
        }
        else{
            return handDic(confObj,val);
        }

    }

    private String handDic(JSONObject confObj,Object val){
        if(val==null || "".equals(val)){
            return "";
        }
        String dicKey = confObj.getString("dicKey");
        List<SysDicDto> sysDicList=sysDicClient.getDicListByTreeIdDicValues(dicKey,val.toString());
        return sysDicList.stream().map(item->item.getName()).collect(Collectors.joining(","));
    }

    private String handSql(JSONObject configObj, Map<String, Object> rowData, Object val, boolean isExport){

        JSONObject tableJson = configObj.getJSONObject("tableName");
        //表名
        String tableName =  tableJson.getString("value");

        String dsAlias="";
        JSONObject dsJson = configObj.getJSONObject("dsName");
        if(dsJson!=null){
            dsAlias=dsJson.getString("value");
        }
        //需要过滤的字段
        String match = configObj.getString("match");

        String params = configObj.getString("params");
        JSONArray paramArray = JSONArray.parseArray(params);
        List<String> fieldList=new ArrayList<>();

        for(int i = 0; i < paramArray.size(); i++){
            JSONObject curObj = paramArray.getJSONObject(i);
            fieldList.add(curObj.getString("target")) ;
        }

        String fields = StringUtils.join(fieldList, ",");
        String sql = "SELECT " + fields + " from " + tableName + " where " + match + "=#{w.val}" ;
        SqlModel sqlModel=new SqlModel(sql);
        return handSqlResult(sqlModel,val,dsAlias,fieldList);
    }

    private String handSqlResult(SqlModel sqlModel,Object val,String dsAlias,List<String> fieldList){
        String[] valAry=val.toString().split(",");
        if(valAry.length>1){
            String valResult="";
            for(int i=0;i<valAry.length;i++) {
                sqlModel.addParam("val", valAry[i]);

                List<Map> resultList;

                if (dsAlias == null) {
                    resultList = commonDao.query(sqlModel);
                } else {
                    resultList = commonDao.query(dsAlias, sqlModel);
                }
                if (BeanUtil.isEmpty(resultList)) {
                    continue;
                }
                Map map = resultList.get(0);

                List<String> results = new ArrayList<>();
                for (String field : fieldList) {
                    String value = (String) map.get(field);
                    results.add(value);
                }
                String result = StringUtils.join(results, " ");
                if(i==valAry.length-1){
                    valResult += result;
                }else{
                    valResult += result + ",";
                }
            }
            return valResult;
        }
        sqlModel.addParam("val",val);

        List<Map> resultList;

        if (dsAlias == null) {
            resultList =  commonDao.query(sqlModel);
        } else {
            resultList =  commonDao.query(dsAlias, sqlModel);
        }
        if(BeanUtil.isEmpty(resultList)){
            return  val.toString();
        }
        Map map=resultList.get(0);

        List<String> results=new ArrayList<>();
        for(String field:fieldList){
            String value=(String)map.get(field);
            results.add(value);
        }
        return StringUtils.join(results," ");
    }



}

