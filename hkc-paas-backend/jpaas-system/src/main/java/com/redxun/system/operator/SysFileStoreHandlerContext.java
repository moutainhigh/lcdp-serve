package com.redxun.system.operator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SysFileStoreHandlerContext {

    @Autowired
    private  static   final Map<String, IFileOperator> SYS_FILE_DATA_MAP  = new ConcurrentHashMap<>();
    @Autowired
    private  static   final Map<String, String> SYS_FILE_MAP  = new ConcurrentHashMap<>();

    public SysFileStoreHandlerContext(Map<String, IFileOperator> map) {
        SysFileStoreHandlerContext.SYS_FILE_DATA_MAP.clear();
        map.forEach((k, v)-> SysFileStoreHandlerContext.SYS_FILE_DATA_MAP.put(v.getType(), v));
        SysFileStoreHandlerContext.SYS_FILE_MAP.clear();
        map.forEach((k, v)-> SysFileStoreHandlerContext.SYS_FILE_MAP.put(v.getType(), v.getTitle()));
    }

    /**
     * 获取处理器列表。
     * @return
     */
    public  static Map<String, IFileOperator> getHandlers(){
        return  SYS_FILE_DATA_MAP;
    }

    /**
     * 获取处理器。
     * @param type
     * @return
     */
    public  static IFileOperator getHandler(String type){
        IFileOperator attrHandler=SYS_FILE_DATA_MAP.get(type);
        return  attrHandler;
    }

    public static  Map<String, String> getSysFileStoreMapMap(){
        return  SYS_FILE_MAP;
    }

}
