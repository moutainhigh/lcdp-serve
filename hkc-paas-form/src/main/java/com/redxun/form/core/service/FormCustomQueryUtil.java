package com.redxun.form.core.service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.SpringUtil;
import com.redxun.db.DbUtil;
import com.redxun.form.core.entity.FormCustomQuery;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author hujun
 */
public class FormCustomQueryUtil {

    protected static Logger logger= LogManager.getLogger(FormCustomQueryUtil.class);

    public static JsonResult queryForJson(String alias,String params,String deploy) throws Exception{
        FormCustomQueryServiceImpl formCustomQueryServiceImpl = SpringUtil.getBean(FormCustomQueryServiceImpl.class);
        if(StringUtils.isEmpty(alias)) {
            return JsonResult.getFailResult("请输入别名");
        }
        Map<String,Object> paramsMap=jsonToMap(params);

        FormCustomQuery formCustomQuery= formCustomQueryServiceImpl.getByKey(alias);

        if(formCustomQuery==null){
            logger.error("自定义SQL("+alias+")不存在！");
            return JsonResult.getFailResult("自定义SQL("+alias+")不存在！");
        }

        Page page=null;
        if(formCustomQuery.getIsPage()==1){
            Object pageObj=paramsMap.get("pageIndex");
            if(pageObj!=null){
                page = new Page(Integer.parseInt(pageObj.toString()),formCustomQuery.getPageSize());
            }
            if(page==null){
                page = new Page(0,formCustomQuery.getPageSize());
            }
        }
        List result= formCustomQueryServiceImpl.getData(formCustomQuery,paramsMap,page,deploy);

        return new JsonResult(true,result,"");

    }

    public static Map<String,Object> jsonToMap(String jsonStr){
        if(StringUtils.isEmpty(jsonStr)){
            return new HashMap<>(16);
        }
        try{
            JSONObject jsonObject= JSONObject.parseObject(jsonStr);
            Set<String> set=jsonObject.keySet();
            Map<String,Object> params=new HashMap<>(set.size());
            for(String key:set){
                String val=jsonObject.getString(key);
                if(StringUtils.isEmpty(val)){
                    continue;
                }
                String str= DbUtil.encodeSql(val);
                params.put(key,str);
            }
            return params;
        }catch (Exception e){
            return new HashMap<>(16);
        }
    }
}
