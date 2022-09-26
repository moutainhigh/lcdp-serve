package com.redxun.fieldrender.impl;

import com.alibaba.fastjson.JSONObject;
import com.redxun.common.tool.StringUtils;
import com.redxun.fieldrender.IFieldRender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能: TODO
 *
 * @author ASUS
 * @date 2022/5/17 23:15
 */
@Component
@Slf4j
public class DoubleFieldRender implements IFieldRender {
    @Override
    public List<String> getControl() {
        List<String> list=new ArrayList<>();
        list.add("rx-user");
        list.add("rx-group");
        //复选框"{"value":"1,2","label":"数据1,数据2"}"
        list.add("rx-checkbox-list");
        //下拉选择""{"label":"下拉1","value":"1"}"
        list.add("rx-form-select");
        //单选框{"value":"1","label":"数据1"}
        list.add("rx-radio");
        //下拉树{"value":"1","label":"数据1"}
        list.add("rx-tree-select");


        return list;
    }

    @Override
    public String render(String val) {
        try {
            if(StringUtils.isEmpty(val)){
                return "";
            }
            //判断是否是json对象结构
            if (!val.startsWith("{") || !val.endsWith("}")) {
                return val;
            }
            JSONObject obj= JSONObject.parseObject(val);
            if(obj.containsKey("label")){
                return obj.getString("label");
            }
        } catch (Exception e){
            e.printStackTrace();
            log.error("DoubleFieldRender获取渲染值出错,val值为：{}",val);
        }
        return val;
    }
}
