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
@Slf4j
@Component
public class SwitchFieldRender implements IFieldRender {
    @Override
    public List<String> getControl() {
        List<String> list=new ArrayList<>();
        //开关：true,false
        list.add("rx-switch");

        return list;
    }

    @Override
    public String render(String val) {
        if(StringUtils.isEmpty(val)){
            return "";
        }
        if("true".equals(val)){
            return "是";
        } else {
            return "否";
        }
    }
}
