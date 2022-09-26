package com.redxun.form.core.service;

import com.alibaba.fastjson.JSONObject;

/**
 * 打印渲染
 */
public class FormPdfRender {

    /**
     * {"address":"fffff","province":"北京市","city":"市辖区",
     * "county_code":"110101000000","county":"东城区",
     * "city_code":"110100000000","province_code":"110000000000"}
     * @param json
     * @return
     */
    public static String address(String json){
        JSONObject obj=JSONObject.parseObject(json);
        String province=obj.getString("province");
        StringBuilder sb=new StringBuilder();
        sb.append(province);
        sb.append(" ");

        String city=obj.getString("city");
        sb.append(city);
        sb.append(" ");

        String county=obj.getString("county");
        sb.append(county);
        sb.append(" ");

        String address=obj.getString("address");
        sb.append(address);


        return sb.toString();
    }

    /**
     * 显示LABEL.
     * @param json
     * @return
     */
    public static String getLabel(String json){
        JSONObject obj=JSONObject.parseObject(json);
        return obj.getString("label");
    }
}
