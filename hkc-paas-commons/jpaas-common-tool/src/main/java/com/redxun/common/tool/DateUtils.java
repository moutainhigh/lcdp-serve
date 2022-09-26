package com.redxun.common.tool;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.management.ManagementFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * 时间工具类
 * 
 * @author yjy
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils
{
    public static final String DATE_FORMAT_FULL = "yyyy-MM-dd HH:mm:ss";

    public static final String DATE_FORMAT_TIME = "HH:mm:ss";

    public static final String DATE_FORMAT_MON = "yyyy-MM";

    public static String YYYY = "yyyy";

    public static String YYYY_MM = "yyyy-MM";

    public static String YYYY_MM_DD = "yyyy-MM-dd";

    public static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    public static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    
    private static String[] parsePatterns = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM", 
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

    /**
     * 获取某月的最后一天
     *
     */
    public static String getLastDayOfMonth(int year,int month,String datyType)
    {
        Calendar cal = Calendar.getInstance();
        //设置年份
        cal.set(Calendar.YEAR,year);
        //设置月份
        cal.set(Calendar.MONTH, month-1);
        //获取某月最大天数
        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        //设置日历中月份的最大天数
        cal.set(Calendar.DAY_OF_MONTH, lastDay);
        //格式化日期
        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf = new SimpleDateFormat(datyType);
        String lastDayOfMonth = sdf.format(cal.getTime());

        return lastDayOfMonth;
    }


    /**
     *获取当前年-月份：2020-05
     */
    public static String getCurrentMonth(){
        int curYear = DateUtils.getCurYear();
        int newCurMonth = DateUtils.getCurMonth()+1;
        return  curYear+"-"+newCurMonth;
    }


    /**
     * 短日期格式
     */
    public static final String DATE_FORMAT_YMD = "yyyy-MM-dd";

    protected Logger logger= LoggerFactory.getLogger(DateUtils.class);

    /**
     * 获得当前日期中的月中的日号
     * @return
     */
    public static int getCurDay(){
        Calendar cal=Calendar.getInstance();
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获得当前日期中的月份
     * @return
     */
    public static int getCurMonth(){
        Calendar cal=Calendar.getInstance();
        return cal.get(Calendar.MONTH);
    }

    /**
     * 获得日期中的月份
     * @param time 传入的日期时间
     * @return
     */
    public static int getMonth(Date time){
        Calendar cal=Calendar.getInstance();
        cal.setTime(time);
        return cal.get(Calendar.MONTH);
    }

    /**
     * 获得当前日期中的年份
     * @return
     */
    public static int getCurYear(){
        Calendar cal=Calendar.getInstance();
        return cal.get(Calendar.YEAR);
    }

    /**
     * 获得日期中的年份
     * @param time 传入的日期时间
     * @return
     */
    public static int getYear(Date time){
        Calendar cal=Calendar.getInstance();
        cal.setTime(time);
        return cal.get(Calendar.YEAR);
    }

    /**
     * 获得当前日期一年的第几周
     * @param time 传入的日期时间
     * @return
     */
    public static int getWeekOfYear(Date time){
        Calendar cal=Calendar.getInstance();
        return cal.get(Calendar.WEEK_OF_YEAR);
    }


    /**
     * 获得当前日期一年的第几周
     * @return
     */
    public static int getCurWeekOfYear(){
        Calendar cal=Calendar.getInstance();
        return cal.get(Calendar.WEEK_OF_YEAR);
    }

    /**
     * 获得日期中的月中的日号
     * @param time 传入的日期时间
     * @return
     */
    public static int getDay(Date time){
        Calendar cal=Calendar.getInstance();
        cal.setTime(time);
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    public static Date parseDate(String dateString) {
        Date date =null;
        if(dateString.indexOf("T")==-1){
            date =parseDate(dateString,"");
        }
        else{
            dateString=dateString.replace("T", " ");
            date =parseDate(dateString,DATE_FORMAT_FULL);
        }
        return date;
    }



    public static void main(String[] args) {
        String val="2021-01-12 09:50:58";
        Date date= parseDate(val,DATE_FORMAT_FULL);

        System.err.println(date);
    }

    public static String getCnDateStr(String value){
        if(value.matches("^((([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29))\\s+([0-1]?[0-9]|2[0-3])-([0-5][0-9])-([0-5][0-9])$")){
            return "yyyy-MM-dd HH-mm-ss";
        }else if(value.matches("^((([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29))\\s+([0-1]?[0-9]|2[0-3])-([0-5][0-9])-([0-5][0-9])$")){
            return "yyyy年MM月dd日 HH时mm分ss秒";
        }else if(value.matches("^\\d{4}(\\-)\\d{1,2}(\\-)\\d{1,2}$")){
            return "yyyy-MM-dd";
        }else if(value.matches("^\\d{4}(年)\\d{1,2}(月)\\d{1,2}日$")){
            return "yyyy年MM月dd日";
        }
        return "";
    }

    //字符串转时间
    public static Date parseDate(String dateString, String format) {

        if(StringUtils.isEmpty(format)){
            format=DATE_FORMAT_YMD;
        }
        //夏时制问题，相差1小时  是从1986年到1991
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+08"));

        SimpleDateFormat sdf =   new SimpleDateFormat( format );
        Date rtn=null;
        try {
            rtn = sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return rtn;
    }


    /**
     * 设置指定时间为当天的最初时间（即00时00分00秒）
     *
     * @param date
     * @return
     */
    public static Date setStartDay(Date date) {
        Calendar cal=Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTime();
    }

    /**
     * 设置指定时间为当天的结束的时间（即23时59分59秒）
     *
     * @param
     * @return
     */
    public static Date setEndDay(Date date) {
        Calendar cal=Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        return cal.getTime();
    }
    /**
     * 获取当前Date型日期
     * 
     * @return Date() 当前日期
     */
    public static Date getNowDate()
    {
        return new Date();
    }

    /**
     * 获取当前日期, 默认格式为yyyy-MM-dd
     * 
     * @return String
     */
    public static String getDate()
    {
        return dateTimeNow(YYYY_MM_DD);
    }

    public static final String getTime()
    {
        return dateTimeNow(YYYY_MM_DD_HH_MM_SS);
    }

    public static final String dateTimeNow()
    {
        return dateTimeNow(YYYYMMDDHHMMSS);
    }

    public static final String dateTimeNow(final String format)
    {
        return parseDateToStr(format, new Date());
    }

    public static final String dateTime(final Date date)
    {
        return parseDateToStr(YYYY_MM_DD, date);
    }

    public static final String parseDateToStr(final String format, final Date date)
    {
        return new SimpleDateFormat(format).format(date);
    }

    public static final Date dateTime(final String format, final String ts)
    {
        try
        {
            return new SimpleDateFormat(format).parse(ts);
        }
        catch (ParseException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * 日期路径 即年/月/日 如2018/08/08
     */
    public static final String datePath()
    {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyy/MM/dd");
    }

    /**
     * 日期路径 即年/月/日 如20180808
     */
    public static final String dateTime()
    {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyyMMdd");
    }

    /**
     * 日期型字符串转化为日期 格式
     */
    public static Date parseDate(Object str)
    {
        if (str == null)
        {
            return null;
        }
        try
        {
            return parseDate(str.toString(), parsePatterns);
        }
        catch (ParseException e)
        {
            return null;
        }
    }
    
    /**
     * 获取服务器启动时间
     */
    public static Date getServerStartDate()
    {
        long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        return new Date(time);
    }

    /**
     * 计算两个时间差
     */
    public static String getDatePoor(Date endDate, Date nowDate)
    {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
        return day + "天" + hour + "小时" + min + "分钟";
    }

    /**
     * 获取显示时间。
     * @param minute
     * @return
     */
    public static String getDisplayTime(int minute){
        int days= (int) (minute / (24 *60));
        int hours=0;
        if(days>0){
            minute=minute -days *(24*60);
            hours=(int) (minute /60);
        }
        if(hours>0){
            minute=minute -hours *60;
        }
        if(days>0){
            return days +"天" + hours + "小时" + minute +"分钟";
        }
        else if (hours>0){
            return hours + "小时" + minute +"分钟";
        }
        return minute +"分钟";
    }

    /**
     * "YYYY-MM-DD"转为"yyyy-MM-dd"
     * @param format
     * @return
     */
    public static String switchFormat(String format){
        String newFormat="";
        String[] formats = format.split(" ");
        String[] dateFormats = formats[0].split("-");
        for (int i=0;i<dateFormats.length;i++){
            if (dateFormats[i].equals("DD")||dateFormats[i].equals("YYYY")){
                dateFormats[i]=dateFormats[i].toLowerCase();
            }
            if (i>0){
                newFormat+="-";
            }
            newFormat+=dateFormats[i];
        }
        for (int i=1;i<formats.length;i++){
            newFormat+=" ";
            newFormat+=formats[i];
        }
        return newFormat;
    }

    /**
     * 将UNIX时间戳转成日期类型。
     * 1492617620
     * @param timestampString
     * @return
     */
    public static Date timeStamp2Date(String timestampString) {
        Long timestamp = Long.parseLong(timestampString) * 1000;
        return new Date(timestamp);
    }

    public static Date localDateTimeToDate(LocalDateTime localDateTime){
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public LocalDateTime dateToLocalDateTime(Date date){
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
}
