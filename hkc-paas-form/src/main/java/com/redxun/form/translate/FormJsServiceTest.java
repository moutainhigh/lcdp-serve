package com.redxun.form.translate;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.tool.StringUtils;
import com.redxun.datasource.DataSourceUtil;
import com.redxun.form.core.entity.FormMobile;
import com.redxun.form.core.entity.FormPc;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormJsServiceTest {

    private static final Pattern NAME_ACTION_REGEX = Pattern.compile("\\\"?name\\\"?\\s*?:\\s*?\"(.*?)\",\\\"?action\\\"?\\s*?:\\s*?(.*)", Pattern.DOTALL | Pattern.MULTILINE);
    /**
     * 升级到6.5版本后，需要执行的方法，解析新的表单js结构
     */
    public static void main(String[] args) {
        try {
            String[] params = new String[]{"driver-class-name", "url", "username", "password","connectionInitSqls"};
            JSONArray jsonAry = new JSONArray();
            String arg="";
            String driver=args[0];
            for (int i = 0; i < params.length; i++) {
                if ("driver-class-name".equals(params[i])) {
                    if("mysql".equals(driver)) {
                        arg = "com.mysql.cj.jdbc.Driver";
                    }else{
                        arg = DataSourceUtil.driverMap.get(driver);
                    }
                }
                if("url".equals(params[i])){
                    if("mysql".equals(driver)) {
                        arg = "jdbc:mysql://" + args[1]+"/"+args[2] + "?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&useSSL=false&zeroDateTimeBehavior=convertToNull&serverTimezone=GMT%2B8";
                    }else if("sqlserver".equals(driver)) {
                        arg = "jdbc:sqlserver://"+args[1]+";DatabaseName="+args[2]+";integratedSecurity=false";
                    } else if ("oracle".equals(driver)) {
                        arg = "jdbc:oracle:thin:@"+args[1]+":"+args[2];
                    } else if ("dm".equals(driver)) {
                        arg = "jdbc:dm://"+args[1]+"/"+args[2]+"?zeroDateTimeBehavior=convertToNull&useUnicode=true&characterEncoding=utf-8";
                    } else if ("postgresql".equals(driver)) {
                        arg = "jdbc:postgresql://"+args[1]+"/"+args[2];
                    }
                }
                if("username".equals(params[i])){
                    arg = args[3];
                }
                if("password".equals(params[i])){
                    arg = args[4];
                }
                if("connectionInitSqls".equals(params[i])){
                    if("dm".equals(driver)){
                        arg = "SET SCHEMA "+args[2];
                    }else{
                        arg = "";
                    }
                }
                System.out.println(params[i]+"====="+arg);
                JSONObject json = new JSONObject();
                json.put("type", "String");
                json.put("name", params[i]);
                json.put("val", arg);
                jsonAry.add(json);
            }
            DataSource dataSource = DataSourceUtil.buildDataSource("formJs", jsonAry.toJSONString());
            Connection connection = dataSource.getConnection();
            Statement statement=connection.createStatement();
            ResultSet resultSet=statement.executeQuery("select * from form_pc");
            while (resultSet.next()){
                String type=resultSet.getString("TYPE_");
                if(FormPc.FORM_TYPE_EASY_DESIGN.equals(type)){
                    continue;
                }
                String id=resultSet.getString("ID_");
                String javascript=resultSet.getString("JAVASCRIPT_");
                String javascriptTmp=resultSet.getString("JAVASCRIPT_TEMP_");
                String script = StringUtils.isEmpty(javascript) ? javascriptTmp : javascript;
                JsonResult result = parseJs(script);
                if (result.isSuccess()) {
                    String updateSql="update form_pc set JAVASCRIPT_=?,JAVASCRIPT_TEMP_=\"\",JAVASCRIPT_KEY_=\"\" where ID_=?";
                    PreparedStatement preparedStatement=connection.prepareStatement(updateSql);
                    preparedStatement.setString(1,(String)result.getData());
                    preparedStatement.setString(2,id);
                    preparedStatement.executeUpdate();
                }
            }

            resultSet=statement.executeQuery("select * from form_mobile");
            while (resultSet.next()){
                String type=resultSet.getString("TYPE_");
                if(FormMobile.TYPE_EASY.equals(type)){
                    continue;
                }
                String id=resultSet.getString("ID_");
                String script=resultSet.getString("SCRIPT_");
                if(StringUtils.isEmpty(script)){
                    continue;
                }
                JsonResult result = parseJs(script);
                if (result.isSuccess()) {
                    String updateSql="update form_mobile set SCRIPT_=? where ID_=?";
                    PreparedStatement preparedStatement=connection.prepareStatement(updateSql);
                    preparedStatement.setString(1,(String)result.getData());
                    preparedStatement.setString(2,id);
                    preparedStatement.executeUpdate();
                }
            }
        } catch (Exception e) {
            System.out.println("执行错误："+e.getMessage());
        }
        System.out.println("执行成功！");
    }

    private static JsonResult parseJs(String script){
        String _onload=getBracesJson(script,"_onload");
        if(StringUtils.isEmpty(_onload)){
            return new JsonResult(false,"不需要解析");
        }
        _onload = _onload.substring(1,_onload.length()-1);
        String _beforeSubmit="async _beforeSubmit()"+getBracesJson(script,"_beforeSubmit");
        String _afterSubmit="_afterSubmit(result,formJson)"+getBracesJson(script,"_afterSubmit");
        String customFuncs=getBracketJson(script,"custFuntions");
        List<JSONObject> funcList=getFunctions(customFuncs);
        String formulas=getBracesJson(script,"formulas");
        String rules=getBracesJson(script,"rules");
        String calcAreas=getBracesJson(script,"calcAreas");
        String validRules=getBracesJson(script,"validRules");
        String customquery=getBracesJson(script,"customquery");
        String custWatchs=getBracketJson(script,"custWatchs");
        List<JSONObject> watchList=getFunctions(custWatchs);
        String formJs="{\n" +
                "  //数据定义\n" +
                "  data(){\n" +
                "    return {\n" +
                "\n" +
                "    }\n" +
                "  },\n" +
                "  //初始化时加载\n" +
                "  created(){" +
                _onload +
                "  },\n" +
                "  //页面自定义函数\n" +
                "  methods:{\n" +
                "    //在提交之前执行进行验证返回验证结果信息{success:true,msg:\"\"}\n" +
                "       " + _beforeSubmit + ",\n" +
                "    //在提交之后执行返回表单提交数据\n" +
                "       " + _afterSubmit + ",\n";
        for(int i=0;i<funcList.size();i++){
            JSONObject func=funcList.get(i);
            String name=func.getString("name");
            String action=func.getString("action");
            formJs+="       "+name+action+",\n";
        }
        formJs+=
                "  },\n" +
                        "  //页面自定义观察函数\n" +
                        "  watch:{\n";
        for(int i=0;i<watchList.size();i++){
            JSONObject watch=watchList.get(i);
            String name=watch.getString("name");
            String action=watch.getString("action");
            formJs+="       \""+name+"\":function"+action+",\n";
        }
        formJs+=
                "  }\n" +
                        "}";
        JSONObject extJs=new JSONObject();
        extJs.put("calcAreas",JSONObject.parseObject(calcAreas));
        extJs.put("formulas",JSONObject.parseObject(formulas));
        extJs.put("rules",JSONObject.parseObject(rules));
        extJs.put("customquery",JSONObject.parseObject(customquery));
        extJs.put("validRules",JSONObject.parseObject(validRules));
        JSONObject javascript=new JSONObject();
        javascript.put("formJs","export default "+formJs);
        javascript.put("extJs",extJs);
        return new JsonResult(true,javascript.toJSONString(),"解析成功");
    }

    private static List<JSONObject> getFunctions(String str){
        List<JSONObject> funcs=new ArrayList<>();
        if(StringUtils.isEmpty(str)){
            return funcs;
        }
        str=StringUtils.trimPrefix(str,"[");
        str=StringUtils.trimSuffix(str,"]");

        List<String> list=getJsonAry(str,'{','}');

        for(int i=0;i<list.size();i++){
            JSONObject json= handFunc(list.get(i));
            funcs.add(json);
        }
        return funcs;
    }

    private static List<String> getJsonAry(String searchStr,char startChar,char endChar){
        char[] searchAry=searchStr.toCharArray();
        boolean flag=false;
        int begin=0;
        List<String> ary=new ArrayList<>();
        Stack<Integer> stack=new Stack<>();
        for(int  i=0;i<searchAry.length;i++) {
            if (searchAry[i] == startChar) {
                stack.push(i);
                flag = true;
            }

            if (searchAry[i] == endChar) {
                begin = stack.pop();
            }

            if (flag && stack.empty()) {
                String tmp=searchStr.substring(begin, i + 1);
                ary.add(tmp);
                flag = false;
            }

        }
        return ary;
    }

    private static JSONObject handFunc(String str){
        str=str.trim();
        str=str.substring(1,str.length()-1);
        JSONObject json=new JSONObject();
        Matcher regexMatcher =  NAME_ACTION_REGEX.matcher(str);
        if(regexMatcher.matches()){
            //json.put(regexMatcher.group(1),regexMatcher.group(2));
            json.put("name",regexMatcher.group(1));
            json.put("action",regexMatcher.group(2).substring(8));
        }
        return json;
    }

    /**
     * 根据花括号为边界的数据
     * @param searchStr
     * @param beginStr
     * @return
     */
    private static String getBracesJson(String searchStr,String beginStr){
        String str=getJson(searchStr,beginStr,'{','}');
        return str;
    }

    /**
     * 根据中括号为边界的数据
     * @param searchStr
     * @param beginStr
     * @return
     */
    private static String getBracketJson(String searchStr,String beginStr){
        String str=getJson(searchStr,beginStr,'[',']');
        return str;
    }

    /**
     * 在字符串中找使用某个字符串开头的变量值。
     * 比如 var config={a=1;};
     * 我们需要找到 {a=1;}
     * @param searchStr
     * @param beginStr
     * @return
     */
    private static String getJson(String searchStr,String beginStr,char startChar,char endChar){
        if(searchStr.indexOf(beginStr)==-1){
            return "";
        }
        int start=searchStr.indexOf(beginStr) + beginStr.length();
        char eq='=';
        char[] searchAry=searchStr.toCharArray();
        int begin=0;
        for(int i=start;i<searchAry.length;i++){
            if(searchAry[i]== eq){
                begin=i+1;
                break;
            }
        }

        boolean flag=false;
        Stack<Integer> stack=new Stack<>();
        for(int  i=start;i<searchAry.length;i++) {
            if (searchAry[i] == startChar) {
                stack.push(i);
                flag = true;
            }

            if (searchAry[i] == endChar) {
                begin = stack.pop();
            }

            if (flag && stack.empty()) {
                return searchStr.substring(begin, i + 1);
            }

        }
        return "";
    }
}