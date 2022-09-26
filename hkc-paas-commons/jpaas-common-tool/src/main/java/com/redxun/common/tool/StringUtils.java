package com.redxun.common.tool;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 *
 * @author yjy
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils
{
    /** 空字符串 */
    private static final String NULLSTR = "";

    /** 下划线 */
    private static final char SEPARATOR = '_';

    /**
     * 去空格
     */
    public static String trim(String str)
    {
        return (str == null ? "" : str.trim());
    }

    /**
     * 截取字符串
     *
     * @param str 字符串
     * @param start 开始
     * @return 结果
     */
    public static String substring(final String str, int start)
    {
        if (str == null)
        {
            return NULLSTR;
        }

        if (start < 0)
        {
            start = str.length() + start;
        }

        if (start < 0)
        {
            start = 0;
        }
        if (start > str.length())
        {
            return NULLSTR;
        }

        return str.substring(start);
    }

    /**
     * 截取字符串
     *
     * @param str 字符串
     * @param start 开始
     * @param end 结束
     * @return 结果
     */
    public static String substring(final String str, int start, int end)
    {
        if (str == null)
        {
            return NULLSTR;
        }

        if (end < 0)
        {
            end = str.length() + end;
        }
        if (start < 0)
        {
            start = str.length() + start;
        }

        if (end > str.length())
        {
            end = str.length();
        }

        if (start > end)
        {
            return NULLSTR;
        }

        if (start < 0)
        {
            start = 0;
        }
        if (end < 0)
        {
            end = 0;
        }

        return str.substring(start, end);
    }

    /**
     * 格式化文本, {} 表示占位符<br>
     * 此方法只是简单将占位符 {} 按照顺序替换为参数<br>
     * 如果想输出 {} 使用 \\转义 { 即可，如果想输出 {} 之前的 \ 使用双转义符 \\\\ 即可<br>
     * 例：<br>
     * 通常使用：format("this is {} for {}", "a", "b") -> this is a for b<br>
     * 转义{}： format("this is \\{} for {}", "a", "b") -> this is \{} for a<br>
     * 转义\： format("this is \\\\{} for {}", "a", "b") -> this is \a for b<br>
     *
     * @param template 文本模板，被替换的部分用 {} 表示
     * @param params 参数值
     * @return 格式化后的文本
     */
    public static String format(String template, Object... params) {
        if (BeanUtil.isEmpty(params) || isEmpty(template)) {
            return template;
        }
        return StrFormatter.format(template, params);
    }


    /**
     * 格式化如下字符串 http://www.bac.com?a=${a}&b=${b}
     *
     * @param message
     * @param params
     * @return
     */
    public static String format(String message, Map<String, Object> params) {
        String result = message;
        if (params == null || params.isEmpty()) {
            return result;
        }
        Iterator<String> keyIts = params.keySet().iterator();
        while (keyIts.hasNext()) {
            String key = keyIts.next();
            Object value = params.get(key);
            if (value != null) {
                result = result.replace("${" + key + "}", value.toString());
            }
        }
        return result;
    }

    /**
     * 下划线转驼峰命名
     */
    public static String toUnderScoreCase(String str) {
        if (str == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        // 前置字符是否大写
        boolean preCharIsUpperCase = true;
        // 当前字符是否大写
        boolean curreCharIsUpperCase = true;
        // 下一字符是否大写
        boolean nexteCharIsUpperCase = true;
        for (int i = 0; i < str.length(); i++)
        {
            char c = str.charAt(i);
            if (i > 0)
            {
                preCharIsUpperCase = Character.isUpperCase(str.charAt(i - 1));
            }
            else
            {
                preCharIsUpperCase = false;
            }

            curreCharIsUpperCase = Character.isUpperCase(c);

            if (i < (str.length() - 1))
            {
                nexteCharIsUpperCase = Character.isUpperCase(str.charAt(i + 1));
            }

            if (preCharIsUpperCase && curreCharIsUpperCase && !nexteCharIsUpperCase)
            {
                sb.append(SEPARATOR);
            }
            else if ((i != 0 && !preCharIsUpperCase) && curreCharIsUpperCase)
            {
                sb.append(SEPARATOR);
            }
            sb.append(Character.toLowerCase(c));
        }

        return sb.toString();
    }

    /**
     * 是否包含字符串
     *
     * @param str 验证字符串
     * @param strs 字符串组
     * @return 包含返回true
     */
    public static boolean inStringIgnoreCase(String str, String... strs)
    {
        if (str != null && strs != null)
        {
            for (String s : strs)
            {
                if (str.equalsIgnoreCase(trim(s)))
                {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 将下划线大写方式命名的字符串转换为驼峰式。如果转换前的下划线大写方式命名的字符串为空，则返回空字符串。 例如：HELLO_WORLD->HelloWorld
     *
     * @param name 转换前的下划线大写方式命名的字符串
     * @return 转换后的驼峰式命名的字符串
     */
    public static String convertToCamelCase(String name)
    {
        StringBuilder result = new StringBuilder();
        // 快速检查
        if (name == null || name.isEmpty())
        {
            // 没必要转换
            return "";
        }
        else if (!name.contains("_"))
        {
            // 不含下划线，仅将首字母大写
            return name.substring(0, 1).toUpperCase() + name.substring(1);
        }
        // 用下划线将原始字符串分割
        String[] camels = name.split("_");
        for (String camel : camels)
        {
            // 跳过原始字符串中开头、结尾的下换线或双重下划线
            if (camel.isEmpty())
            {
                continue;
            }
            // 首字母大写
            result.append(camel.substring(0, 1).toUpperCase());
            result.append(camel.substring(1).toLowerCase());
        }
        return result.toString();
    }

    /**
     * 驼峰式命名法 例如：user_name->userName
     */
    public static String toCamelCase(String s)
    {
        if (s == null)
        {
            return null;
        }
        s = s.toLowerCase();
        StringBuilder sb = new StringBuilder(s.length());
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++)
        {
            char c = s.charAt(i);
            if (c == SEPARATOR)
            {
                upperCase = true;
            }
            else if (upperCase)
            {
                sb.append(Character.toUpperCase(c));
                upperCase = false;
            }
            else
            {
                sb.append(c);
            }
        }
        return sb.toString();
    }
    /**
     * 首字母大写。
     * @param str 字符串
     * @return 首字母大写
     */
    public static String toUpFirst(String str){
        if(str==null) {return null;}
        if(str.trim().length()==0) {
            return "";
        }
        String first=str.substring(0, 1).toUpperCase();

        return first + str.substring(1);
    }


    /**
     * 判断字符串非空
     *
     * @param str "字符串"
     * @return
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * 判断字符串是否为空
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        if (str == null) {
            return true;
        }
        return "".equals( str.trim());
    }

    /**
     * 获得父路径
     * @param fullPath 如 0.1.2.3.
     * @return
     */
    public static String getParentPath(String fullPath){
        String subString=fullPath.substring(0,fullPath.length()-1);
        int index=subString.lastIndexOf(".");
        if(index!=-1){
            String newStr=subString.substring(0,index+1);
            return newStr;
        }
        return subString;
    }

    /**
     * 获得数组字符串中的字符集
     * @param path 如 1.2.3.4.则返回 '1','2','3','4'
     * @return
     */
    public static String getArrCharString(String path){
        StringBuffer sb=new StringBuffer();
        String[] arr=path.split("[.]");
        for(int i=0;i<arr.length;i++){
            if("0".equals(arr[i])|| "".equals(arr[i])){
                continue;
            }
            if(sb.length()>0){
                sb.append(",");
            }
            sb.append("'").append(arr[i]).append("'");
        }
        return sb.toString();
    }

    /**
     * 判断某个字符串是否为数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        String chars="01234567890.";
        for(int i=0;i<str.length();i++){
            String c=str.substring(i, i+1);
            if(!chars.contains(c)){
                return false;
            }
        }
        return true;
    }


    /**
     * 把字符串的第一个字母转为大写或者小写
     *
     * @param str
     *            字符串
     * @param isUpper
     *            是否大写
     * @return
     */
    public static String toFirst(String str, boolean isUpper) {
        if (StringUtils.isEmpty(str)) {
            return "";
        }
        char first = str.charAt(0);
        String firstChar = new String(new char[] { first });
        firstChar = isUpper ? firstChar.toUpperCase() : firstChar.toLowerCase();
        return firstChar + str.substring(1);
    }

    /**
     * 将字符串首字符小写。
     * @param newStr
     * @return  String
     */
    public static String makeFirstLetterLowerCase(String newStr) {
        return toFirst(newStr, false);
    }
    /**
     * 将字符串首字符大写。
     * @param newStr
     * @return String
     */
    public static String makeFirstLetterUpperCase(String newStr) {
        return toFirst(newStr, true);
    }



    /**
     * 对字符串去掉前面的指定字符
     *
     * @param content
     *            待处理的字符串
     * @param prefix
     *            要去掉前面的指定字符串
     * @return
     */
    public static String trimPrefix(String content, String prefix) {
        if(StringUtils.isEmpty(prefix))  {
            return content;
        }
        String resultStr = content;
        while (resultStr.startsWith(prefix)) {
            resultStr = resultStr.substring(prefix.length());
        }
        return resultStr;
    }

    /**
     * 对字符串去掉前面的指定字符
     *
     * @param content
     *            待处理的字符串
     * @param suffix
     *            要去掉后面的指定字符串
     * @return
     */
    public static String trimSuffix(String content, String suffix) {
        if(StringUtils.isEmpty(suffix)) {
            return content;
        }
        String resultStr = content;
        while (resultStr.endsWith(suffix)) {
            resultStr = resultStr.substring(0,
                    resultStr.length() - suffix.length());
        }
        return resultStr;
    }

    /**
     * 删除结尾的字符串。
     * @param content
     * @param suffix
     * @return
     */
    public static String trimSuffixOnce(String content, String suffix) {
        if(StringUtils.isEmpty(suffix) || !content.endsWith(suffix)) {
            return content;
        }

        return content.substring(0,content.length()-suffix.length());
    }


    /**
     * 对字符串的前后均去掉前面的指定字符
     *
     * @param content
     * @param trimStr
     * @return
     */
    public static String trim(String content, String trimStr) {
        return trimSuffix(trimPrefix(content, trimStr), trimStr);
    }


    /**
     * 将人民币金额数字转成中文大写。
     *
     * @param amount
     * @return
     */
    public static String convertToChineseNumeral(double amount) {
        char[] hunit = { '拾', '佰', '仟' }; // 段内位置表示
        char[] vunit = { '万', '亿' }; // 段名表示
        char[] digit = { '零', '壹', '贰', '叁', '肆', '伍', '陆', '柒', '捌', '玖' }; // 数字表示
        long midVal = (long) (amount * 100); // 转化成整形
        String valStr = String.valueOf(midVal); // 转化成字符串

        String head = valStr.substring(0, valStr.length() - 2); // 取整数部分
        String rail = valStr.substring(valStr.length() - 2); // 取小数部分

        String prefix = ""; // 整数部分转化的结果
        String suffix = ""; // 小数部分转化的结果
        // 处理小数点后面的数
        if ("00".equals( rail)) { // 如果小数部分为0
            suffix = "整";
        } else {
            suffix = digit[rail.charAt(0) - '0'] + "角"
                    + digit[rail.charAt(1) - '0'] + "分"; // 否则把角分转化出来
        }
        // 处理小数点前面的数
        char[] chDig = head.toCharArray(); // 把整数部分转化成字符数组
        char zero = '0'; // 标志'0'表示出现过0
        byte zeroSerNum = 0; // 连续出现0的次数
        for (int i = 0; i < chDig.length; i++) { // 循环处理每个数字
            int idx = (chDig.length - i - 1) % 4; // 取段内位置
            int vidx = (chDig.length - i - 1) / 4; // 取段位置
            if (chDig[i] == '0') { // 如果当前字符是0
                zeroSerNum++; // 连续0次数递增
                if (zero == '0') { // 标志
                    zero = digit[0];
                } else if (idx == 0 && vidx > 0 && zeroSerNum < 4) {
                    prefix += vunit[vidx - 1];
                    zero = '0';
                }
                continue;
            }
            zeroSerNum = 0; // 连续0次数清零
            if (zero != '0') { // 如果标志不为0,则加上,例如万,亿什么的
                prefix += zero;
                zero = '0';
            }
            prefix += digit[chDig[i] - '0']; // 转化该数字表示
            if (idx > 0) {
                prefix += hunit[idx - 1];
            }
            if (idx == 0 && vidx > 0) {
                prefix += vunit[vidx - 1]; // 段结束位置应该加上段名如万,亿
            }
        }

        if (prefix.length() > 0) {
            prefix += '圆'; // 如果整数部分存在,则有圆的字样
        }
        return prefix + suffix; // 返回正确表示
    }

    /**
     * 将list进行join。
     * @param list
     * @param splitor
     * @return
     */
    public  static String join(List<String> list,String splitor){
        String rtnString="";
        for(int i=0;i<list.size();i++){
            if(i==0){
                rtnString=list.get(i);
            }
            else{
                rtnString+=splitor + list.get(i);
            }
        }
        return rtnString;
    }

    /**
     * 数据合并。
     * @param set
     * @param splitor
     * @return
     */
    public  static String join(Set<String> set, String splitor){
        String rtnString="";
        int i=0;
        for(String str:set){
            if(i==0){
                rtnString+=str;
            }
            else{
                rtnString+=splitor + str;
            }
            i++;
        }
        return rtnString;
    }

    public  static String join(Set<String> set){
        return  join(set,",");
    }



    /**
     * 将list进行join。
     * @param list
     * @return
     */
    public  static String join(List<String> list){
       return  join(list,",");
    }

    /**
     * 对传入的字符串（content）进行变量值替换（map） 采用默认的正则表达式：\\{(.*?)\\}
     *
     * @param content
     *            要处理的字符串
     * @param map
     *            替换参数和值的集合
     * @return 替换后的字符串
     * @throws Exception
     */
    public static String replaceVariableMap(String content,
                                            Map<String, Object> map) throws Exception {
        return replaceVariableMap(content, map, "\\{(.*?)\\}");
    }

    /**
     *
     * @param template
     *            要处理的字符串
     * @param map
     *            替换参数和值的集合
     * @param regular
     *            正则表达式
     * @return 替换后的字符串
     * @throws Exception
     *             如果template的某个
     */
    public static String replaceVariableMap(String template,
                                            Map<String, Object> map, String regular) throws Exception {
        Pattern regex = Pattern.compile(regular);
        Matcher regexMatcher = regex.matcher(template);
        while (regexMatcher.find()) {
            String key = regexMatcher.group(1);
            String toReplace = regexMatcher.group(0);
            String value = (String) map.get(key);
            if (value != null) {
                template = template.replace(toReplace, value);
            } else {
                template = template.replace(toReplace, "");
            }
        }

        return template;
    }

    public static String randomId(){
        String chars = "abcdefghijklmnopqrstuvwxyz";
        int maxPos = chars.length();
        String id = "";
        for (int i = 0; i < 7; i++) {
            id += chars.charAt(Double.valueOf(Math.floor(Math.random() * maxPos)).intValue());
        }
        id+=(System.currentTimeMillis() +"").substring(7);
        return id;
    }


    /**
     * 产生指定长度的随机字符串。
     * @param length
     * @return
     */
    public static String genRandomString(int length){
        String str="ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<length;i++){
            int number=random.nextInt(36);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        String str=genRandomString(6);
        System.err.println(str);
    }

    /**
     * 判断是否又Sql注入。
     * @param injectStr
     * @return
     */
    public static boolean isSqlInject(String injectStr) {
        String injStr = "'|and|exec|create|insert|select|delete|update|count|*|%|chr|mid|master|truncate|drop|char|declare|or|while";
        String injStrArr[] = injStr.split("\\|");
        injectStr = injectStr.toLowerCase();
        for (int i = 0; i < injStrArr.length; i++) {
            if (injectStr.indexOf(injStrArr[i]+" ") >= 0) {
                return true;
            }
        }

        return false;

    }

}
