package com.redxun.gencode.codegenhander.impl.attr;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.redxun.common.base.entity.BaseEntity;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ExceptionUtil;
import com.redxun.gencode.codegenhander.IFieldHndler;
import com.redxun.gencode.util.ReaderFileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.Serializable;


/**
 *  地址组件实现。
 *
 */
@Slf4j
@Component
public class AddressValHandler<E extends BaseEntity<? extends Serializable>> implements IFieldHndler {



    @Override
    public void handJsonToEntity(BaseEntity entity,JSONObject tableJsonList,JSONObject entJson, JSONObject formData,String key,String createType) {
        String keyVal = formData.getString(key);
        if(StringUtils.isEmpty(keyVal)){
            return;
        }
        JSONObject keyJson = JSONObject.parseObject(keyVal);
        if(BeanUtil.isEmpty(keyJson)){
            return;
        }

        if("db".equals(createType)){
            JSONObject extJson = entJson.getJSONObject("extJson");
            if(BeanUtil.isEmpty(extJson)){
                return;
            }
            JSONObject setting = extJson.getJSONObject("setting");
            if(BeanUtil.isEmpty(setting)){
                return;
            }

            //省级
            String province= extJson.getString("province");
            String pCode= extJson.getString("p_code");

            BeanUtil.setFieldValue(entity, province,keyJson.getString(ReaderFileUtil.ADDRESS_PROVINCE));
            BeanUtil.setFieldValue(entity, pCode,keyJson.getString(ReaderFileUtil.ADDRESS_PROVINCE+ReaderFileUtil.CODE));


            if(setting.getBoolean("isCity")){
                //市
                String city= extJson.getString("city");
                String cityCode= extJson.getString("city_code");
                setFieldValue(entity, city,keyJson.getString("city"));
                setFieldValue(entity, cityCode,keyJson.getString("city_code"));
            }

            if(setting.getBoolean("isCounty")){
                //县
                String county= extJson.getString("county");
                String countyCode= extJson.getString("county_code");
                setFieldValue(entity, county,keyJson.getString("county"));
                setFieldValue(entity, countyCode,keyJson.getString("county_code"));
            }

            if(setting.getBoolean("isAddress")){
                //详细地址
                String address= extJson.getString("address");
                setFieldValue(entity, address,keyJson.getString("address"));
            }
        }else {
            //省级
            BeanUtil.setFieldValue(entity, key+"Province",keyJson.getString(ReaderFileUtil.ADDRESS_PROVINCE));
            BeanUtil.setFieldValue(entity, key+"Pcode",keyJson.getString(ReaderFileUtil.ADDRESS_PROVINCE+ReaderFileUtil.CODE));
            //地址控件特殊处理。需要改成handFun
            if("3".equals(entJson.getString("contains"))){
                //市/县/详细地址
                setFieldValue(entity, key+"City",keyJson.getString("city"));
                setFieldValue(entity, key+"CityCode",keyJson.getString("city_code"));
                setFieldValue(entity, key+"County",keyJson.getString("county"));
                setFieldValue(entity, key+"CountyCode",keyJson.getString("county_code"));
                setFieldValue(entity, key+"Address",keyJson.getString("address"));
            }else if("2".equals(entJson.getString("contains"))){
                //市/县
                setFieldValue(entity, key+"City",keyJson.getString("city"));
                setFieldValue(entity, key+"CityCode",keyJson.getString("city_code"));
                setFieldValue(entity, key+"County",keyJson.getString("county"));
                setFieldValue(entity, key+"CountyCode",keyJson.getString("county_code"));
            }else if("1".equals(entJson.getString("contains"))){
                //市
                setFieldValue(entity, key+"City",keyJson.getString("city"));
                setFieldValue(entity, key+"CityCode",keyJson.getString("city_code"));
            }
        }
    }

    private void setFieldValue(BaseEntity entity, String key,String val){
        if(StringUtils.isEmpty(val)){
            return;
        }
        try {
            BeanUtil.setFieldValue(entity, key,val);
        }catch (Exception e){
            log.error("setFieldValue is error : message ={}", ExceptionUtil.getExceptionMessage(e));
        }

    }

    @Override
    public void handEntityToJsonData(BaseEntity entity, JSONObject tableJsonList, JSONObject fileds, JSONObject entJson, JSONObject formData, String key, String createType) {
        JSONObject json = new JSONObject();
        if("db".equals(createType)){
            JSONObject extJson = entJson.getJSONObject("extJson");
            if(BeanUtil.isEmpty(extJson)){
                formData.put(key,json.toJSONString());
                return;
            }
            JSONObject setting = extJson.getJSONObject("setting");
            if(BeanUtil.isEmpty(setting)){
                formData.put(key,json.toJSONString());
                return;
            }

            //{"province":"浙江省","province_code":"330000000000","city":"温州市","city_code":"330300000000","county":"龙湾区","county_code":"330303000000","address":"sss"}"
            //省级
            String province= extJson.getString("province");
            String p_code= extJson.getString("p_code");
            json.put("province",getAttrVal(entity, province));
            json.put("province_code",getAttrVal(entity, p_code));


            if(setting.getBoolean("isCity")){
                //市
                String city= extJson.getString("city");
                String city_code= extJson.getString("city_code");
                json.put("city",getAttrVal(entity, city));
                json.put("city_code",getAttrVal(entity, city_code));
            }

            if(setting.getBoolean("isCounty")){
                //县
                String county= extJson.getString("county");
                String county_code= extJson.getString("county_code");
                json.put("county",getAttrVal(entity, county));
                json.put("county_code",getAttrVal(entity, county_code));
            }

            if(setting.getBoolean("isAddress")){
                //详细地址
                String address= extJson.getString("address");
                json.put("address",getAttrVal(entity, address));
            }
        }else {
            //省级
            json.put("province",getAttrVal(entity, key+"Province"));
            json.put("province_code",getAttrVal(entity, key+"Pcode"));

            //地址控件特殊处理。需要改成handFun
            if("3".equals(entJson.getString("contains"))){
                //市/县/详细地址
                json.put("city",getAttrVal(entity, key+"City"));
                json.put("city_code",getAttrVal(entity, key+"CityCode"));
                json.put("county",getAttrVal(entity, key+"County"));
                json.put("county_code",getAttrVal(entity, key+"CountyCode"));
                json.put("address",getAttrVal(entity, key+"Address"));
            }else if("2".equals(entJson.getString("contains"))){
                //市/县
                json.put("city",getAttrVal(entity, key+"City"));
                json.put("city_code",getAttrVal(entity, key+"CityCode"));
                json.put("county",getAttrVal(entity, key+"County"));
                json.put("county_code",getAttrVal(entity, key+"CountyCode"));
            }else if("1".equals(entJson.getString("contains"))){
                //市
                json.put("city",getAttrVal(entity, key+"City"));
                json.put("city_code",getAttrVal(entity, key+"CityCode"));
            }
        }
        formData.put(key,json.toJSONString());
    }

    private String getAttrVal(BaseEntity entity,String key){
        Object object = BeanUtil.getFieldValueFromObject(entity, key);
        if(BeanUtil.isEmpty(object)){
            return "";
        }
        return  object.toString();
    }

    @Override
    public String getAttrName() {
        return "rx-address";
    }

    @Override
    public String getDescription() {
        return "地址控件";
    }
}
