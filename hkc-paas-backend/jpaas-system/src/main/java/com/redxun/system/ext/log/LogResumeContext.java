package com.redxun.system.ext.log;

import com.redxun.common.utils.SpringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LogResumeContext {
    @Autowired
    private  static   final Map<String,ILogResume> STRATEGY_MAP = new ConcurrentHashMap<>();

    public LogResumeContext(Map<String,ILogResume> map){
        LogResumeContext.STRATEGY_MAP.clear();
        map.forEach((k, v)-> LogResumeContext.STRATEGY_MAP.put(v.getType(), v));
    }
    /**
     * 获取处理器。
     * @param type
     * @return
     */
    public  static ILogResume getLogResume(String type){
        ILogResume logResume=STRATEGY_MAP.get(type);
        if(logResume!=null){
            return logResume;
        }
        return null;
    }
}
