package com.redxun.form.core.service;

import com.redxun.common.utils.SpringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author hujun
 */
@Service
public class AttrHandlerContext {

    @Autowired
    private  static   final Map<String, IAttrHandler> STRATEGY_MAP = new ConcurrentHashMap<>();

    public AttrHandlerContext(Map<String, IAttrHandler> map) {
        AttrHandlerContext.STRATEGY_MAP.clear();
        map.forEach((k, v)-> AttrHandlerContext.STRATEGY_MAP.put(v.getPluginName(), v));
    }

    /**
     * 获取处理器。
     * @param plugin
     * @return
     */
    public  static IAttrHandler getAttrHandler(String plugin){
        IAttrHandler attrHandler=STRATEGY_MAP.get(plugin);
        if(attrHandler!=null){
            return attrHandler;
        }
        IAttrHandler defaultAttrHander= SpringUtil.getBean("defaultAttrHandler");
        return  defaultAttrHander;
    }
}
