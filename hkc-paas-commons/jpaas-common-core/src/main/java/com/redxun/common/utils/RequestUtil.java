package com.redxun.common.utils;

import com.redxun.common.tool.DateUtils;
import com.redxun.common.tool.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * HttpRequest工具类
 *
 */
public class RequestUtil {

    public static String getString(HttpServletRequest   request, String key){
        return getString(request,  key, "");
    }

    /**
     * 获取请求中的参数值，并且返回字符串
     * @param request
     * @param key
     * @param defaultValue
     * @return
     */
    public static String getString(HttpServletRequest request, String key,String defaultValue){
        String val=request.getParameter(key);
        if(StringUtils.isNotEmpty(val)){
            return val;
        }
        return defaultValue;
    }


    /**
     * 从request中取得int值
     *
     * @param request
     * @param key
     * @return
     * @throws Exception
     */
    public static int getInt(HttpServletRequest request, String key) {
        return getInt(request, key, 0);
    }
    /**
     * 从request中取得int值,如果无值则返回缺省值
     *
     * @param request
     * @param key
     * @return
     * @throws Exception
     */
    public static int getInt(HttpServletRequest request, String key, int defaultValue) {
        String str = request.getParameter(key);
        if (StringUtils.isEmpty(str)) {
            return defaultValue;
        }
        return Integer.parseInt(str);
    }
    /**
     * 从Request中取得long值
     *
     * @param request
     * @param key
     * @return
     * @throws Exception
     */
    public static long getLong(HttpServletRequest request, String key) {
        return getLong(request, key, 0);
    }
    /**
     * 取得长整形数组
     *
     * @param request
     * @param key
     * @return
     */
    public static Long[] getLongAry(HttpServletRequest request, String key) {
        String[] aryKeys = request.getParameterValues(key);
        if (aryKeys == null || aryKeys.length == 0) {
            return null;
        }
        Long[] aryLong = new Long[aryKeys.length];
        for (int i = 0; i < aryKeys.length; i++) {
            aryLong[i] = Long.parseLong(aryKeys[i]);
        }
        return aryLong;
    }
    /**
     * 根据一串逗号分隔的长整型字符串取得长整形数组
     *
     * @param request
     * @param key
     * @return
     */
    public static Long[] getLongAryByStr(HttpServletRequest request, String key) {
        String sysUserId = request.getParameter(key);
        String[] aryId = sysUserId.split(",");
        Long[] lAryId = new Long[aryId.length];
        for (int i = 0; i < aryId.length; i++) {
            lAryId[i] = Long.parseLong(aryId[i]);
        }
        return lAryId;
    }
    /**
     * 根据一串逗号分隔的长整型字符串取得长整形数组
     *
     * @param request
     * @param key
     * @return
     */
    public static String[] getStringAryByStr(HttpServletRequest request, String key) {
        String ids = request.getParameter(key);
        if(StringUtils.isEmpty(ids)){
            return new String[0];
        }
        String[] aryId = ids.split(",");
        return aryId;
    }
    /**
     * 根据键值取得整形数组
     *
     * @param request
     * @param key
     * @return
     */
    public static Integer[] getIntAry(HttpServletRequest request, String key) {
        String[] aryKey = request.getParameterValues(key);
        Integer[] aryInt = new Integer[aryKey.length];
        for (int i = 0; i < aryKey.length; i++) {
            aryInt[i] = Integer.parseInt(aryKey[i]);
        }
        return aryInt;
    }
    public static Float[] getFloatAry(HttpServletRequest request, String key) {
        String[] aryKey = request.getParameterValues(key);
        Float[] fAryId = new Float[aryKey.length];
        for (int i = 0; i < aryKey.length; i++) {
            fAryId[i] = Float.parseFloat(aryKey[i]);
        }
        return fAryId;
    }
    /**
     * 从Request中取得long值,如果无值则返回缺省值
     *
     * @param request
     * @param key
     * @return
     * @throws Exception
     */
    public static long getLong(HttpServletRequest request, String key, long defaultValue) {
        String str = request.getParameter(key);
        if (StringUtils.isEmpty(str)) {
            return defaultValue;
        }
        return Long.parseLong(str);
    }
    /**
     * 从Request中取得float值
     *
     * @param request
     * @param key
     * @return
     * @throws Exception
     */
    public static float getFloat(HttpServletRequest request, String key) {
        return getFloat(request, key, 0);
    }
    /**
     * 从Request中取得float值,如无值则返回缺省值
     *
     * @param request
     * @param key
     * @return
     * @throws Exception
     */
    public static float getFloat(HttpServletRequest request, String key, float defaultValue) {
        String str = request.getParameter(key);
        if (StringUtils.isEmpty(str)) {
            return defaultValue;
        }
        return Float.parseFloat(request.getParameter(key));
    }
    /**
     * 从Request中取得boolean值,如无值则返回缺省值 false, 如值为数字1，则返回true
     *
     * @param request
     * @param key
     * @return
     */
    public static boolean getBoolean(HttpServletRequest request, String key) {
        return getBoolean(request, key, false);
    }
    /**
     * 从Request中取得boolean值 对字符串,如无值则返回缺省值, 如值为数字1，则返回true
     *
     * @param request
     * @param key
     * @param defaultValue
     * @return
     */
    public static boolean getBoolean(HttpServletRequest request, String key, boolean defaultValue) {
        String str = request.getParameter(key);
        if (StringUtils.isEmpty(str)) {
            return defaultValue;
        }
        if (StringUtils.isNumeric(str)) {
            return (Integer.parseInt(str) == 1);
        }
        return Boolean.parseBoolean(str);
    }
    /**
     * 从Request中取得boolean值,如无值则返回缺省值 0
     *
     * @param request
     * @param key
     * @return
     */
    public static Short getShort(HttpServletRequest request, String key) {
        return getShort(request, key, (short) 0);
    }
    /**
     * 从Request中取得Short值 对字符串,如无值则返回缺省值
     *
     * @param request
     * @param key
     * @param defaultValue
     * @return
     */
    public static Short getShort(HttpServletRequest request, String key, Short defaultValue) {
        String str = request.getParameter(key);
        if (StringUtils.isEmpty(str)) {
            return defaultValue;
        }
        return Short.parseShort(str);
    }
    /**
     * 从Request中取得Date值,如无值则返回缺省值null, 如果有值则返回 yyyy-MM-dd 格式的日期
     *
     * @param request
     * @param key
     * @return
     */
    public static Date getDate(HttpServletRequest request, String key)  {
        String str = request.getParameter(key);
        if (StringUtils.isEmpty(str)) {
            return null;
        }
        return DateUtils.parseDate(str);
    }

    /**
     * 获取上下文参数。
     * @param request
     * @param remainArray
     * @return
     */
    public static Map<String,Object> getParameterValueMap(HttpServletRequest request, boolean remainArray){
        Map<String,Object> params=getParameterValueMap(request,remainArray,true);
        return  params;
    }

    public static Map<String,Object> getParameterValueMap(HttpServletRequest request, boolean remainArray,boolean checkSecurity){
        Map<String,Object> map = new HashMap<>();
        Enumeration params = request.getParameterNames();
        while(params.hasMoreElements()){
            String key=params.nextElement().toString();
            String[] values=request.getParameterValues(key);
            if(values==null){
                continue;
            }
            if(values.length==1){
                String tmpValue=values[0];

                if(checkSecurity && StringUtils.isSqlInject(tmpValue)){
                    continue;
                }
                if(tmpValue==null){
                    continue;
                }
                tmpValue=tmpValue.trim();
                if(tmpValue.equals("")){
                    continue;
                }
                map.put(key,tmpValue);
            }else{
                String rtn=getByAry(values);
                if(checkSecurity && StringUtils.isSqlInject(rtn)){
                    continue;
                }
                if(rtn.length() >0){
                    if(remainArray){
                        map.put(key,rtn.split(","));
                    }else{
                        map.put(key,rtn);
                    }
                }
            }
        }
        return map;
    }

    private static String getByAry(String[] aryTmp){
        String rtn="";
        for(int i=0;i<aryTmp.length;i++){
            String str=aryTmp[i].trim();

            if(!str.equals("")){
                rtn+=str+",";
            }
        }
        if(rtn.length()>0){
            rtn=rtn.substring(0,rtn.length()-1);
        }
        return rtn;
    }

    /**
     * 获取客户端的IP。
     * @param request
     * @return
     */
    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
