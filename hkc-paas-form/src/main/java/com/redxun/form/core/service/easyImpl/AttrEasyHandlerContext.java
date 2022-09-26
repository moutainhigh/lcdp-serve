package com.redxun.form.core.service.easyImpl;

import com.redxun.common.utils.SpringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author hujun
 */
@Service("easyAttrHandlerContext")
public class AttrEasyHandlerContext {

    @Autowired
    private  static   final Map<String, IAttrEasyHandler> STRATEGY_MAP = new ConcurrentHashMap<>();

    public AttrEasyHandlerContext(Map<String, IAttrEasyHandler> map) {
        AttrEasyHandlerContext.STRATEGY_MAP.clear();
        map.forEach((k, v)-> AttrEasyHandlerContext.STRATEGY_MAP.put(v.getPluginName(), v));
    }

    /**
     * 获取处理器。
     * @param plugin
     * @return
     */
    public  static IAttrEasyHandler getAttrHandler(String plugin){
        IAttrEasyHandler attrHandler=STRATEGY_MAP.get(plugin);
        if(attrHandler!=null){
            return attrHandler;
        }
        IAttrEasyHandler defaultAttrHander= SpringUtil.getBean("easyDefaultAttrHandler");
        return  defaultAttrHander;
    }
}
