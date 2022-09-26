package com.redxun.system.core.service.job;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Joiner;
import com.redxun.common.tool.DateUtils;
import com.redxun.common.tool.StringUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class JobUtil {

    /**
     * {
     *    mode:"",
     *    config:{
     *         value:"",
     *         time:""
     *    }
     *  }
     * @param cfg
     * @return
     */
    public static String getJobCron(String cfg){
        if(StringUtils.isEmpty(cfg)){
            return "";
        }

        /**
         * {
         *     mode:"",
         *     config:{
         *         value:"",
         *         time:""
         *     }
         * }
         */

        String cronStr="";

        JSONObject strategyObj = JSONObject.parseObject(cfg);
        String mode = strategyObj.getString("mode");
        JSONObject config = strategyObj.getJSONObject("config");
        String value = config.getString("value");
        //一次 指定日期
        if("one".equals(mode)){
            //2022-01-02 22:52:48
            Date date = DateUtils.parseDate(value, "yyyy-MM-dd HH:mm:ss");
            Calendar calendar=Calendar.getInstance();
            calendar.setTime(date);


            String year=calendar.get(Calendar.YEAR)+"" ;
            String month=calendar.get(Calendar.MONTH) +1 +"" ;
            String day=calendar.get(Calendar.DAY_OF_MONTH) +"" ;

            String hour=calendar.get(Calendar.HOUR_OF_DAY) +"" ;
            String minute=calendar.get(Calendar.MINUTE) +"" ;
            String second=calendar.get(Calendar.SECOND) +"" ;

            cronStr=String.format("%s %s %s %s %s ? %s ",second ,minute,hour,day,month,year);
        }
        //每天(分)
        else if("day_minute".equals(mode)){
            cronStr="0 0/"+value+" * * * ?";
        }
        //每天指定时间(时分)
        else if("day_hour".equals(mode)){
            String[] ary = value.split(":");
            cronStr="0 "+ary[1]+" "+ary[0]+" * * ?";
        }
        //每周
        else if("week".equals(mode)){
            List list = JSON.parseObject(value, List.class);
            String week = Joiner.on(",").join(list);
            String time = config.getString("time");
            String[] ary = time.split(":");
            cronStr="0 "+ary[1]+" "+ary[0]+" ? * "+week;
        }
        //每月
        else if("month".equals(mode)){
            List list = JSON.parseObject(value, List.class);
            String month = Joiner.on(",").join(list);
            String time = config.getString("time");
            String[] ary = time.split(":");
            cronStr="0 "+ary[1]+" "+ary[0]+" "+month+" * ?" ;
        }else {
            cronStr=value;
        }

        return cronStr;
    }

    public static void main(String[] args) {
        Date date = DateUtils.parseDate("2022-01-02 22:52:48", "yyyy-MM-dd HH:mm:ss");
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);


        String year=calendar.get(Calendar.YEAR)+"" ;
        String month=calendar.get(Calendar.MONTH) +1 +"" ;
        String day=calendar.get(Calendar.DAY_OF_MONTH) +"" ;

        String hour=calendar.get(Calendar.HOUR_OF_DAY) +"" ;
        String minute=calendar.get(Calendar.MINUTE) +"" ;
        String second=calendar.get(Calendar.SECOND) +"" ;



        String str=String.format("%s %s %s %s %s ? %s ",second ,minute,hour,day,month,year);

        System.err.println(str);
    }
}
