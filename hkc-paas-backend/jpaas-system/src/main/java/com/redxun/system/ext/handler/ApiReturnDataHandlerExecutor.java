package com.redxun.system.ext.handler;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.SpringUtil;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * @author hujun
 */
@Service
public class ApiReturnDataHandlerExecutor {

    /**
     * 获取handler实现。
     * @return
     */
    public JSONArray getHandlers(){
        Collection<ApiReturnDataHandler> beans = SpringUtil.getBeans(ApiReturnDataHandler.class);
        JSONArray jsonArray=new JSONArray();
        for(ApiReturnDataHandler o:beans){
            JSONObject obj=new JSONObject();
            String name= StringUtils.makeFirstLetterLowerCase(o.getClass().getSimpleName());
            obj.put("label",o.getName());
            obj.put("value",name);
            jsonArray.add(obj);
        }
        return jsonArray;
    }

}
