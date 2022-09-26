package com.redxun.form.core.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.constant.MBoolean;
import com.redxun.common.engine.FtlEngine;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.DateUtils;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ContextUtil;
import com.redxun.common.utils.ExceptionUtil;
import com.redxun.common.utils.FileUtil;
import com.redxun.common.utils.ZipUtil;
import com.redxun.form.bo.entity.FormBoAttr;
import com.redxun.form.bo.entity.FormBoEntity;
import com.redxun.form.bo.service.FormBoEntityServiceImpl;
import com.redxun.form.core.entity.CodeGenTemplate;
import com.redxun.form.core.entity.FormPc;
import com.redxun.form.core.entity.FormSolution;
import com.redxun.form.core.entity.GlobalVar;
import com.redxun.form.core.service.CodeGenTemplateServiceImpl;
import com.redxun.form.core.service.FormPcServiceImpl;
import com.redxun.form.core.service.FormSolutionServiceImpl;
import com.redxun.form.core.service.GlobalVarServiceImpl;
import com.redxun.log.annotation.AuditLog;
import freemarker.template.TemplateException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 代码生成器功能。
 * @date 2021-8-24
 * @company 广州红迅软件有限公司
 * @author ray
 */
@Slf4j
@RestController
@RequestMapping("/form/core/codegen")
@ClassDefine(title = "代码生成",alias = "formCodeGenController",path = "/form/core/formCodeGen",packages = "core",packageName = "表单管理")
@Api(tags = "表单方案")
public class FormCodeGenController {

    @Autowired
    private GlobalVarServiceImpl globalVarService;
    @Autowired
    FormSolutionServiceImpl formSolutionService;
    @Autowired
    FormPcServiceImpl formPcService;
    @Autowired
    FormBoEntityServiceImpl formBoEntityServiceImpl;
    @Autowired
    CodeGenTemplateServiceImpl codeGenTemplateService;

    private static final Pattern REGEX = Pattern.compile("\\{(.*?)\\}");


    private static final Pattern NAME_ACTION_REGEX = Pattern.compile("\\\"?name\\\"?\\s*?:\\s*?\"(.*?)\",\\\"?action\\\"?\\s*?:\\s*?(.*)", Pattern.DOTALL | Pattern.MULTILINE);


    @Autowired
    private FtlEngine ftlEngine;

    @Autowired
    ConfigService configService;

    private static final String GLOBAL_VAR="codegenGlobalVar";
    private static final String FORM_VAR="codegenFormVar";
    private static final String DEFAULT_GROUP="DEFAULT_GROUP";


    @GetMapping(value = "getGlobalVar",produces = { "application/json"})
    public String getGlobalVar() throws IOException, NacosException {
        String userId= ContextUtil.getCurrentUserId();
        GlobalVar globalVar=globalVarService.getByUserId(userId);
        String config=configService.getConfig(GLOBAL_VAR,DEFAULT_GROUP,0);
        if(globalVar!=null){
            config=globalVar.getConfig();
        }
        return config;
    }

    /**
     * 根据表单方案获取变量。
     *
     * <pre>
     * {
     *  alias:{
     *      name:"",alias:"",vars:[{"name":"package","comment":"包名","value":"core"}]
     *      }
     * }
     * </pre>
     *
     * @param solutionId
     * @return
     * @throws IOException
     * @throws NacosException
     */
    @GetMapping(value = "getFormVars",produces = { "application/json"})
    public JSONObject getFormVars(@RequestParam(value = "solutionId") String solutionId) throws IOException, NacosException {
        FormSolution formSolution=formSolutionService.get(solutionId);
        FormBoEntity entity=formBoEntityServiceImpl.getByDefId(formSolution.getBodefId(),false);
        JSONObject jsonRtn=new JSONObject();

        String formVar=configService.getConfig(FORM_VAR,DEFAULT_GROUP,0);
        JSONArray ary = JSONArray.parseArray(formVar);

        JSONObject mainJson= getVarsByEnt(entity,ary);
        jsonRtn.put(entity.getAlias(),mainJson);

        if(BeanUtil.isNotEmpty( entity.getBoEntityList())){
            for(FormBoEntity subEnt:entity.getBoEntityList()){
                JSONObject subJson= getVarsByEnt(subEnt,ary);
                jsonRtn.put(subEnt.getAlias(),subJson);
            }
        }
        return jsonRtn;
    }

    /**
     * 获取实体变量。
     * @param boEntity
     * @param ary
     * @return
     */
    private JSONObject getVarsByEnt(FormBoEntity boEntity,JSONArray ary){
        JSONObject json=new JSONObject();
        json.put("name",boEntity.getName());
        json.put("alias",boEntity.getAlias());

        JSONArray vars=new JSONArray();

        for(int i=0;i<ary.size();i++){
            JSONObject var=ary.getJSONObject(i);
            JSONObject newVar= (JSONObject) var.clone();

            String name=newVar.getString("name");
            if("class".equals(name)){
                newVar.put("value",StringUtils.makeFirstLetterUpperCase(boEntity.getAlias()));
            }
            vars.add(newVar);
        }
        json.put("vars",vars);
        return  json;
    }

    /**
     * 获取变量定义。
     * @return
     * @throws IOException
     * @throws NacosException
     */
    @GetMapping(value = "getVars",produces = { "application/json"})
    public JSONArray getVars() throws IOException, NacosException {
        String globalVar=configService.getConfig(GLOBAL_VAR,DEFAULT_GROUP,0);
        String formVar=configService.getConfig(FORM_VAR,DEFAULT_GROUP,0);

        JSONArray ary=new JSONArray();
        JSONObject globalJson=new JSONObject();
        globalJson.put("name","全局变量");
        globalJson.put("children",JSONArray.parse(globalVar));

        JSONObject formJson=new JSONObject();
        formJson.put("name","表单变量");
        formJson.put("children",JSONArray.parse(formVar));

        ary.add(globalJson);
        ary.add(formJson);

        return ary;
    }

    /**
     * 生成代码
     * @param json:{
     *    global:[],
     *    formVar:{},
     *    files:[],
     *    solutionId:"表单方案ID"
     * }
     * @return
     * @throws IOException
     */
    @ApiOperation("代码生成")
    @MethodDefine(title = "代码生成", path = "/genCode", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "表单ID", varName = "formId")})
    @AuditLog(operation = "代码生成")
    @PostMapping("genCode")
    public void genCode(@RequestBody String json) throws IOException, TemplateException {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletResponse response=attributes.getResponse();

            JSONObject jsonObj=JSONObject.parseObject(json);
            String solutionId=jsonObj.getString("solutionId");
            JSONArray globalAry=jsonObj.getJSONArray("global");
            JSONObject formVar=jsonObj.getJSONObject("formVar");
            JSONArray files=jsonObj.getJSONArray("files");

            //保存全局变量
            saveGlobalVar(globalAry.toJSONString());


            FormSolution solution= formSolutionService.get(solutionId);
            FormPc formPc=formPcService.get(solution.getFormId());

            FormBoEntity formBoEntity = formBoEntityServiceImpl.getByDefId(solution.getBodefId(), true);

            Map<String,String> vars= getVars(globalAry);

            Map<String,Object> model=new HashMap<>();

            vars.put("date", DateUtils.getTime());
            model.put("vars",vars);


            String basePath=getBasePath(globalAry);


            /**
             * 设置ENT变量。
             */
            setVars(formBoEntity,formVar);

            //编辑页面
            String vueTemplate=getVueTemplate(formPc,formBoEntity,vars);
            model.put("vueTemplate",vueTemplate);

            String vueJs=getVueJs(formPc,formBoEntity,vars);
            model.put("vueJs",vueJs);

            model.put("formAlias",formPc.getAlias());

            for(int i=0;i<files.size();i++){
                JSONObject file=files.getJSONObject(i);
                CodeGenTemplate codeGenTemplate=codeGenTemplateService.get(file.getString("id"));
                boolean single= MBoolean.YES.val.equals( codeGenTemplate.getSingle());

                model.put("model",formBoEntity);
                //对变量进行处理。
                vars.putAll(formBoEntity.getVars());
                genFile(model,codeGenTemplate,vars,basePath);
                //如果是单个文件模式，则每个都生成一次。
                boolean hasSub=BeanUtil.isNotEmpty( formBoEntity.getBoEntityList());
                if(!single && hasSub){
                    for(FormBoEntity entity :formBoEntity.getBoEntityList()){
                        model.put("model",entity);
                        vars.putAll(entity.getVars());
                        genFile(model,codeGenTemplate,vars,basePath);
                    }
                }
            }

            response.setContentType("application/zip");
            response.addHeader("Content-Disposition", "attachment; filename=\""+formBoEntity.getAlias() +".zip\"");
            response.addHeader("blob", "true");

            //下载文件
            ZipUtil.toZip(basePath, response.getOutputStream(), true);
            FileUtil.deleteDir(new File( basePath));
        }catch (Exception e){
            log.error("***** is error: message={}", ExceptionUtil.getExceptionMessage(e));
        }
    }

    /**
     * 保存全局变量。
     * @param config
     */
    private void saveGlobalVar(String config){
        //保存全局变量
        String userId= ContextUtil.getCurrentUserId();
        GlobalVar globalVar=globalVarService.getByUserId(userId);
        if(globalVar==null){
            globalVar=new GlobalVar();
            globalVar.setConfig(config);
            globalVarService.insert(globalVar);
        }
        else{
            globalVar.setConfig(config);
            globalVarService.update(globalVar);
        }
    }

    /**
     * 生成VUEJS页面。
     * @param formPc
     * @param formBoEntity
     * @param vars
     * @return
     * @throws TemplateException
     * @throws IOException
     */
    private String getVueJs(FormPc formPc,FormBoEntity formBoEntity,Map<String,String> vars) throws TemplateException, IOException {
        String js=formPc.getJavascript();

        Map<String,Object> model=new HashMap<>();

        //formulas
        String formulas= getBracesJson(js,"formulas");
        model.put("formulas",formulas);

        String rules= getBracesJson(js,"rules");
        model.put("rules",rules);

        String calcAreas= getBracesJson(js,"calcAreas");
        model.put("calcAreas",calcAreas);

        String validRules= getBracesJson(js,"validRules");
        model.put("validRules",validRules);

        String customquery= getBracesJson(js,"customquery");
        model.put("customquery",customquery);

        model.put("model",formBoEntity);
        model.put("vars",vars);
        //元数据。
        model.put("metadata",formPc.getMetadata());
        model.put("data","{}");

        String content=ftlEngine.mergeTemplateIntoString("codegen/vuejs.ftl",model);
        return  content;


    }


    /**
     * 生成VUE页面
     * @param formPc
     * @param formBoEntity
     * @param vars
     * @return
     * @throws TemplateException
     * @throws IOException
     */
    private String getVueTemplate(FormPc formPc,FormBoEntity formBoEntity,Map<String,String> vars) throws TemplateException, IOException {
        Map<String,Object> model=new HashMap<>();

        String vueTemplate=getTemplate(formPc.getTemplate());

        model.put("vueTemplate",vueTemplate);

        String javascript=formPc.getJavascript();


        String _onload=getBracesJson(javascript,"_onload");

        model.put("onload",_onload);

        String _beforeSubmit=getBracesJson(javascript,"_beforeSubmit");
        _beforeSubmit = StringUtils.replace(_beforeSubmit,"\\n","");
        _beforeSubmit = StringUtils.replace(_beforeSubmit,"\\","");
        model.put("_beforeSubmit",_beforeSubmit);

        String _afterSubmit=getBracesJson(javascript,"_afterSubmit");
        _afterSubmit = StringUtils.replace(_afterSubmit,"\\n","");
        _afterSubmit = StringUtils.replace(_afterSubmit,"\\","");
        model.put("_afterSubmit",_afterSubmit);


        String customFuncs=getBracketJson(javascript,"custFuntions");
        List<JSONObject> funcList=getFunctions(customFuncs);
        model.put("funcList",funcList);

        String watchs=getBracketJson(javascript,"custWatchs");
        List<JSONObject> watchList=getFunctions(watchs);
        model.put("watchList",watchList);

        model.put("model",formBoEntity);
        model.put("vars",vars);
        //元数据。
        model.put("metadata",formPc.getMetadata());

        String content=ftlEngine.mergeTemplateIntoString("codegen/vue.ftl",model);
        return  content;
    }


    /**
     * 整理表单模板。
     * @param formTemplate
     * @return
     */
    private String getTemplate(String formTemplate){
        StringBuilder sb=new StringBuilder();

        JSONArray jsonArray=JSONArray.parseArray(formTemplate);
        if(jsonArray.size()==1){
            String template=jsonArray.getJSONObject(0).getString("content");
            template=handHtmlTemplate(template);
            //template=Jsoup.parse(template).html();
            sb.append( template);
        }
        else{
            sb.append("<a-tabs >");

            for(int i=0;i<jsonArray.size();i++){
                JSONObject json=jsonArray.getJSONObject(i);
                String title=json.getString("title");
                String content=json.getString("content");
                content=handHtmlTemplate(content);

                sb.append( "<a-tab-pane key=\"tab_"+i+"\" tab=\""+title+"\">");
                sb.append(content);
                sb.append("</a-tab-pane>");
            }
            sb.append("</a-tabs>");
        }
        return sb.toString();
    }

    /**
     * 整理HTML模板。
     * @param html
     * @return
     */
    private String handHtmlTemplate(String html){
        Document document = Jsoup.parseBodyFragment(html);

        document.select("[:readonly]").attr(":readonly","readonly");

        document.select("[:permission]")
                .attr("permission","w")
                .removeAttr(":permission");

        document.select("[v-if^=getSubTablePermission]").removeAttr("v-if");

        return document.body().html();

    }

    /**
     * 生成文件
     * @param model             模型对象
     * @param codeGenTemplate   文件模板
     * @param vars              变量
     * @param basePath          基础路径
     * @throws IOException
     * @throws TemplateException
     */
    private void genFile(Map<String,Object> model,CodeGenTemplate codeGenTemplate,Map<String,String> vars,String basePath) throws IOException, TemplateException {
        //获取解析后的内容。
        String fileContent= ftlEngine.parseByStringTemplate(model,codeGenTemplate.getContent());
        //生成文件路径。
        String path=getPath(codeGenTemplate,vars);
        String filePath=basePath + path;

        FileUtil.writeFile(filePath,fileContent);
    }

    /**
     * 将 ary 的数据转换为 map 类型的数据。
     * @param ary
     * @return
     */
    private Map<String,String> getByJsonAry(JSONArray ary){
        Map<String,String> vars=new HashMap<>();
        for(int i=0;i<ary.size();i++){
            //{"name":"package","comment":"包名","value":"core"}
            JSONObject json=ary.getJSONObject(i);
            String name=json.getString("name");
            String value=json.getString("value");
            vars.put(name,value);
            if("class".equals(name)){
                vars.put("classVar", StringUtils.makeFirstLetterLowerCase( value));
            }

        }
        return  vars;
    }


    /**
     * 给每个实体设置变量。
     * @param boEntity
     * @param jsonVar
     */
    private void  setVars(FormBoEntity boEntity,JSONObject jsonVar){
        JSONObject mainJson=jsonVar.getJSONObject(boEntity.getAlias());
        JSONArray mainVars= mainJson.getJSONArray("vars");
        Map<String, String> main = getByJsonAry(mainVars);
        boEntity.setVars(main);

        //addBoAttrByForm(boEntity);

        if(BeanUtil.isNotEmpty( boEntity.getBoEntityList())){
            for(FormBoEntity subEnt:boEntity.getBoEntityList()){
                JSONObject subJson=jsonVar.getJSONObject(subEnt.getAlias());
                JSONArray subVars= subJson.getJSONArray("vars");
                Map<String, String> subMap = getByJsonAry(subVars);
                subEnt.setVars(subMap);
            }
        }
    }

    /**
     * 在线表单生成实体方式，双值控件增加
     */
    private void addBoAttrByForm(FormBoEntity boEntity){
        if(!"form".equals(boEntity.getGenMode())){
            return;
        }
        List<FormBoAttr> boAttrList = boEntity.getBoAttrList();
        if(BeanUtil.isEmpty(boAttrList)){
            return;
        }
        for (FormBoAttr attr:boAttrList) {
            if(0!=attr.getIsSingle()){
                continue;
            }
            String refAttrStr=JSONObject.toJSONString(attr);
            FormBoAttr refAttr = JSONObject.parseObject(refAttrStr,FormBoAttr.class);
            refAttr.setControl("rx-ref");
            refAttr.setIsSingle(1);
            refAttr.setName(attr.getName()+"Name");
            refAttr.setFieldName(attr.getFieldName()+"_NAME");
        }

    }



    /**
     * 获取生成文件的路径。
     * @param codeGenTemplate
     * @param vars
     * @return
     */
    private String getPath(CodeGenTemplate codeGenTemplate,Map<String ,String> vars){
        String fileName=codeGenTemplate.getFileName();
        String path=codeGenTemplate.getPath();
        fileName=replace(fileName,vars);
        path=replace(path,vars).replace("\\","/");
        String filePath=path.endsWith("/")?path :path +"/" + fileName;
        return filePath;
    }



    /**
     * 将jsonArray 转成map对象。
     * @param jsonArray
     * @return
     */
    private Map<String,String> getVars(JSONArray jsonArray){
        Map<String,String> vars=new HashMap<>();
        for(int i=0;i<jsonArray.size();i++) {
            JSONObject json = jsonArray.getJSONObject(i);
            String name=json.getString("name");
            String value=json.getString("value");

            vars.put(name,value);
        }
        return vars;
    }

    /**
     * 获取生成路径。
     * @param globalAry
     * @return
     */
    private String getBasePath(JSONArray globalAry){
        for(int i=0;i<globalAry.size();i++){
            JSONObject json=globalAry.getJSONObject(i);
            String name=json.getString("name");
            if("genDir".equals(name)){
                String val=json.getString("value");
                String basePath=val.replace("\\","/");
                if(!basePath.endsWith("/")){
                    basePath+="/";
                }
                basePath+=System.currentTimeMillis() +"/";
                return  basePath;
            }
        }
        return "";
    }

    /**
     * 将字符串中的 {name} 标识符进行替换。
     * @param template
     * @param vars
     * @return
     */
    private String replace(String template,Map<String,String> vars){
        Matcher regexMatcher = REGEX.matcher(template);
        while (regexMatcher.find()) {
            String key=regexMatcher.group(1);
            String val=vars.get(key);
            template=template.replace(regexMatcher.group(0),val);
        }
        return template;
    }

    /**
     * 根据花括号为边界的数据
     * @param searchStr
     * @param beginStr
     * @return
     */
    private static String getBracesJson(String searchStr,String beginStr){
        String str=getJson(searchStr,beginStr,'{','}');
        if(StringUtils.isEmpty(str)){
            return "{}";
        }
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

    /**
     * 找到使用 { } 开头结尾的JSON。
     * [{name:"demo1","action":function(name){}},
     * {name:"demo2","action":function(name){}},
     * {name:"demo3","action":function(name){}}]
     * @param searchStr
     * @param startChar
     * @param endChar
     * @return
     */
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

    /**
     * 处理自定义函数返回JSON
     * { name:"",action:"" }
     * @param str
     * @return
     */
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
     * 获取自定义函数列表，返回JSON列表
     * JSON格式为 [{name:"",body:""}]
     *  [{name:"demo1","action":function(name){}},{name:"demo2","action":function(name){}},{name:"demo3","action":function(name){}}]
     * @param str
     * @return
     */
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




    public static void main(String[] args) {
        String str="var config={a=1;if(aa=bb){" +
                "serr}};";
        str=getBracesJson(str,"configs");
        System.err.println(str);
        str="var _afterSubmit=function(result,formJson){\n" +
                "System.out.println(oo);\n" +
                "System.out.println(oo);\n" +
                "System.out.println(oo);\n" +
                "};";

        str=getBracesJson(str,"_afterSubmit");
        System.err.println(str);

        str="var custFuntions=[{name:\"demo1\",action:function(){}}];";

        str=getBracketJson(str,"custFuntions");

        System.err.println(str);

        str="[{name:\"demo1\",\"action\":function(name){}},{name:\"demo2\",\"action\":function(name){}},{name:\"demo3\",\"action\":function(name){}}]";

        List<JSONObject> list=getFunctions(str);

        System.err.println(list);
    }


}
