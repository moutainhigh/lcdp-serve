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
 * @date 2022/5/17 23:42
 */
@Slf4j
@Component
public class MapFieldRender implements IFieldRender {
    @Override
    public List<String> getControl() {
        List<String> list=new ArrayList<>();
        //"{"value":{"longitude":113.244269,"latitude":23.130499,"address":"广东省广州市荔湾区恩洲大巷143号-5"},"label":"广东省广州市荔湾区恩洲大巷143号-5"}"
        list.add("rx-map");

        return list;
    }

    @Override
    public String render(String val) {
        String address = "";
        try {
            if(StringUtils.isEmpty(val)){
                return "";
            }
            JSONObject json=JSONObject.parseObject(val);
            address = json.getString("label");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("MapFieldRender获取渲染值出错,val值为：{}",val);
        }
        return address;
    }
}
