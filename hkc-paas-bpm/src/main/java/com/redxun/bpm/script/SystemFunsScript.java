package com.redxun.bpm.script;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.redxun.bpm.script.cls.ClassScriptType;
import com.redxun.bpm.script.cls.IScript;
import com.redxun.bpm.script.cls.MethodDefine;
import com.redxun.bpm.script.cls.ParamDefine;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.DateUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 系统函数脚本
 */
@ClassScriptType(type = "SysFunApi",description = "系统函数")
@Component("SysFunApi")
public class SystemFunsScript implements IScript {

    @MethodDefine(title="解析json对象")
    public <T> T parseObj(@ParamDefine(varName = "obj",description = "json对象")Object obj,
                          @ParamDefine(varName = "clazz",description = "字节码")Class<T> clazz){
        return JSONObject.toJavaObject((JSON)JSON.toJSON(obj), clazz);
    }

    @MethodDefine(title="解析List对象")
    public <T> List<T> parseList(@ParamDefine(varName = "list",description = "list对象") List list,
                          @ParamDefine(varName = "clazz",description = "字节码")Class<T> clazz){
        List<T> arr=new ArrayList<>();
        for(Object obj:list){
            arr.add(JSONObject.toJavaObject((JSON)JSON.toJSON(obj), clazz));
        }
        return arr;
    }

    /**
     * 获取当前系统时间
     * @return
     */
    @MethodDefine(title="获取当前系统时间")
    public Date getCurDate(){
        return new Date();
    }

    @MethodDefine(title="日期天数比较",description = "返回{日期1}减去{日期2}的天数")
    public int compareDays(@ParamDefine(varName = "date1",description = "日期1") Date date1,
                           @ParamDefine(varName = "date2",description = "日期2") Date date2){

        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        cal1.set(Calendar.HOUR_OF_DAY, 0);
        cal1.set(Calendar.MINUTE, 0);
        cal1.set(Calendar.SECOND, 0);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        cal2.set(Calendar.HOUR_OF_DAY, 0);
        cal2.set(Calendar.MINUTE, 0);
        cal2.set(Calendar.SECOND, 0);
        long intervalMilli = cal1.getTimeInMillis() - cal2.getTimeInMillis();
        int days = (int) (intervalMilli / (24 * 60 * 60 * 1000));
        return days;
    }

    @MethodDefine(title="日期（字符）天数比较",description = "返回{日期1(字符)}减去{日期2}的天数(字符)")
    public int compareDays(@ParamDefine(varName = "date1",description = "字符日期1") String date1,
                           @ParamDefine(varName = "date2",description = "字符日期2") String date2){

        Date tmpDate1= DateUtils.parseDate(date1);

        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(tmpDate1);
        cal1.set(Calendar.HOUR_OF_DAY, 0);
        cal1.set(Calendar.MINUTE, 0);
        cal1.set(Calendar.SECOND, 0);

        Date tmpDate2= DateUtils.parseDate(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(tmpDate2);
        cal2.set(Calendar.HOUR_OF_DAY, 0);
        cal2.set(Calendar.MINUTE, 0);
        cal2.set(Calendar.SECOND, 0);
        long intervalMilli = cal1.getTimeInMillis() - cal2.getTimeInMillis();

        int days = (int) (intervalMilli / (24 * 60 * 60 * 1000));
        return days;
    }



    @MethodDefine(title="日期比较",description = "返回{日期1}减去{日期2}的时间毫秒数")
    public long subDate(@ParamDefine(varName = "date1",description = "日期1") Date date1,
                            @ParamDefine(varName = "date2",description = "日期2") Date date2){
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);

        long intervalMilli = cal1.getTimeInMillis() - cal2.getTimeInMillis();

        return intervalMilli;
    }


    @MethodDefine(title="字符串包含函数",description = "第一个参数是否包含第二个参数")
    public boolean contain(@ParamDefine(varName = "findIn",description = "被查找字符串")String findIn,
                           @ParamDefine(varName = "find",description = "目标字符串")String find){

        return findIn.indexOf(find)!=-1;
    }

    /**
     * sqyh -> {label:"管理员",value:"1"}
     * @param field
     * @return
     */
    @MethodDefine(title="获取JSON中的label属性值",description = "参数为一个格式为 {label:\"管理员\",value:\"1\"}JSON 数据，我们需要获取value的值")
    public String getLabel(@ParamDefine(varName = "field",description = "参数为一个格式为 {label:\"管理员\",value:\"1\"}JSON 数据") String field){
        JSONObject json=JSONObject.parseObject(field);
        return  json.getString("label");
    }


    /**
     * sqyh -> {label:"管理员",value:"1"}
     * @param field
     * @return
     */
    @MethodDefine(title="获取JSON中的value属性值",description = "参数为一个格式为 {label:\"管理员\",value:\"1\"}JSON 数据，我们需要获取value的值")
    public String getValue(@ParamDefine(varName = "field",description = "参数为一个格式为 {label:\"管理员\",value:\"1\"}JSON 数据")String field){
        JSONObject json=JSONObject.parseObject(field);
        return  json.getString("value");
    }

    /**
     *
     * @param obj
     * @return
     */
    @MethodDefine(title="判断对象为空",description = "判断对象是否为空")
    public boolean isEmpty(@ParamDefine(varName = "obj",description = "任意对象") Object obj){
        return BeanUtil.isEmpty(obj);
    }

    @MethodDefine(title="判断对象不为空",description = "判断对象不为空")
    public boolean isNotEmpty(@ParamDefine(varName = "obj",description = "任意对象") Object obj){
        return BeanUtil.isNotEmpty(obj);
    }





}
