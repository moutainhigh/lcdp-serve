package com.redxun.fieldrender;

import com.redxun.common.utils.SpringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 字段渲染接口
 *
 * @author ASUS
 * @date 2022/5/17 9:59
 */
@Service
public class FieldRenderContext {


    private  static   final Map<String, IFieldRender> STRATEGY_MAP = new ConcurrentHashMap<>();

    public FieldRenderContext(Map<String, IFieldRender> map) {
        FieldRenderContext.STRATEGY_MAP.clear();
        STRATEGY_MAP.putAll(map);

    }

    /**
     * 获取字段渲染对象。
     * @param control
     * @return
     */
    public  static IFieldRender getFieldRender(String control){

        Collection<IFieldRender> values = STRATEGY_MAP.values();
        for (IFieldRender render:values){
            if(render.getControl()==null){
                continue;
            }
            if(render.getControl().contains(control)){
                return render;
            }
        }

        IFieldRender fieldRender= SpringUtil.getBean("defaultFieldRender");
        return  fieldRender;
    }
}
