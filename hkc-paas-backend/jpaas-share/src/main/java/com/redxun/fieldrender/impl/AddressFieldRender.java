package com.redxun.fieldrender.impl;

import com.alibaba.fastjson.JSONObject;
import com.redxun.common.tool.StringUtils;
import com.redxun.fieldrender.IFieldRender;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能: TODO
 *
 * @author ASUS
 * @date 2022/5/17 23:42
 */
@Component
public class AddressFieldRender implements IFieldRender {
    @Override
    public List<String> getControl() {
        List<String> list=new ArrayList<>();
        //地址控件"{"province":"北京市","province_code":"110000000000","city":"市辖区","city_code":"110100000000","county":"西城区","county_code":"110102000000","address":"121212"}"
        list.add("rx-address");
        //地区控件"{"province":"江苏省","province_code":"320000000000","city":"南通市","city_code":"320600000000","county":"如东县","county_code":"320623000000"}"
        list.add("rx-district");
        return list;
    }

    @Override
    public String render(String val) {
        if(StringUtils.isEmpty(val)){
            return "";
        }
        //判断是否是json结构
        if (!val.startsWith("{") || !val.endsWith("}")) {
            return val;
        }
        //{"province":"北京市","province_code":"110000000000","city":"市辖区",
        // "city_code":"110100000000","county":"房山区","county_code":"110111000000","address":"dddd"}
        JSONObject json=JSONObject.parseObject(val);

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(json.getString("province"))
                .append(json.getString("city"))
                .append(json.getString("county"));
        //兼容地址控件格式
        if(StringUtils.isNotEmpty(json.getString("address"))){
            stringBuffer.append(json.getString("address"));
        }
        return stringBuffer.toString();
    }
}
