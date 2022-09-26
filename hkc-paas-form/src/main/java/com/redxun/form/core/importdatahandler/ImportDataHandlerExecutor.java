package com.redxun.form.core.importdatahandler;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.SpringUtil;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 表单数据导入处理器的执行类
 * @author zfh
 */
@Service
public class ImportDataHandlerExecutor {


    /**
     * 对每一行进行判断处理器。
     * @param data
     * @param importDataHandler
     */
    public JsonResult handRowHandler(Map<String,Object> data, String importDataHandler){

        if(StringUtils.isEmpty(importDataHandler)){
            return JsonResult.Success();
        }

        ImportDataHandler handler= SpringUtil.getBean(importDataHandler);
        if(handler==null){
            return JsonResult.Success();
        }
        return  handler.handRow(data);
    }

    /**
     * 对读取的数据进行处理处理器。
     * @param data
     * @param importDataHandler
     */
    public JsonResult beforeInsertHandler(List<Map<String,Object>> data, String importDataHandler){

        if(StringUtils.isEmpty(importDataHandler)){
            return JsonResult.Success();
        }
        ImportDataHandler  handler= SpringUtil.getBean(importDataHandler);
        if(handler==null){
            return JsonResult.Success();
        }
        return handler.beforeInsert(data);
    }

    /**
     * 获取handler实现。
     * @return
     */
    public JSONArray getHandlers(){
        Collection<ImportDataHandler> beans = SpringUtil.getBeans(ImportDataHandler.class);
        JSONArray jsonArray=new JSONArray();
        for(ImportDataHandler o:beans){
            JSONObject obj=new JSONObject();
            String name= StringUtils.makeFirstLetterLowerCase(o.getClass().getSimpleName());
            obj.put("label",o.getName());
            obj.put("value",name);
            jsonArray.add(obj);
        }
        return jsonArray;
    }

}
