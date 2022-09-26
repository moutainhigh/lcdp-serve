package com.redxun.portal.context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 栏目定义数据处理上下文
 */
@Service
public class InsColumnDefHandlerContext {

    @Autowired
    private  static   final Map<String, IColumnDataService> INS_COLUMN_DEF_DATA_MAP  = new ConcurrentHashMap<>();
    @Autowired
    private  static   final Map<String, String> INS_COLUMN_DEF_MAP  = new ConcurrentHashMap<>();

    /**
     * 初始化 栏目类型 与 栏目服务类
     * @param map
     */
    public InsColumnDefHandlerContext(Map<String, IColumnDataService> map) {
        InsColumnDefHandlerContext.INS_COLUMN_DEF_DATA_MAP.clear();
        map.forEach((k, v)-> InsColumnDefHandlerContext.INS_COLUMN_DEF_DATA_MAP.put(v.getType(), v));
        InsColumnDefHandlerContext.INS_COLUMN_DEF_MAP.clear();
        map.forEach((k, v)-> InsColumnDefHandlerContext.INS_COLUMN_DEF_MAP.put(v.getType(), v.getName()));
    }

    /**
     * 获取处理器列表。
     * @return
     */
    public  static Map<String, IColumnDataService> getHandlers(){
        return  INS_COLUMN_DEF_DATA_MAP;
    }

    /**
     * 获取处理器。
     * @param type
     * @return
     */
    public  static IColumnDataService getHandler(String type){
        IColumnDataService attrHandler=INS_COLUMN_DEF_DATA_MAP.get(type);
        return  attrHandler;
    }

    public static  Map<String, String> getInsColumnDefMapMap(){
        return  INS_COLUMN_DEF_MAP;
    }

}
