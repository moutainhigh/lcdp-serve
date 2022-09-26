
package com.redxun.form.core.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.nacos.api.config.ConfigService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.constant.MBoolean;
import com.redxun.common.engine.FtlEngine;
import com.redxun.common.engine.FtlUtil;
import com.redxun.common.tool.*;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.common.utils.ExceptionUtil;
import com.redxun.common.utils.FileUtil;
import com.redxun.common.utils.ZipUtil;
import com.redxun.form.bo.entity.FormBoAttr;
import com.redxun.form.bo.entity.FormBoEntity;
import com.redxun.form.bo.entity.FormBoRelation;
import com.redxun.form.bo.service.FormBoAttrServiceImpl;
import com.redxun.form.bo.service.FormBoEntityServiceImpl;
import com.redxun.form.core.entity.CodeGenSetting;
import com.redxun.form.core.entity.FormTemplate;
import com.redxun.form.core.mapper.CodeGenSettingMapper;
import com.redxun.gencode.util.ReaderFileUtil;
import freemarker.template.TemplateException;
import freemarker.template.TemplateHashModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
* [代码生成配置]业务服务类
*/
@Slf4j
@Service
public class CodeGenSettingServiceImpl extends SuperServiceImpl<CodeGenSettingMapper, CodeGenSetting> implements BaseService<CodeGenSetting> {

    @Resource
    private CodeGenSettingMapper codeGenSettingMapper;

    @Autowired
    FormSolutionServiceImpl formSolutionService;
    @Autowired
    FormPcServiceImpl formPcService;
    @Autowired
    FormBoEntityServiceImpl formBoEntityServiceImpl;
    @Autowired
    CodeGenTemplateServiceImpl codeGenTemplateService;
    @Autowired
    FormTemplateServiceImpl formTemplateService;
    @Autowired
    FormBoAttrServiceImpl formBoAttrService;
    @Resource
    private FormBoAttrServiceImpl formBoAttrServiceImpl;


    @Autowired
    private FtlEngine ftlEngine;

    @Autowired
    ConfigService configService;
    @Autowired
    FtlEngine freemarkEngine;

    private static final Pattern REGEX = Pattern.compile("\\{(.*?)\\}");


    private static final Pattern NAME_ACTION_REGEX = Pattern.compile("\\\"?name\\\"?\\s*?:\\s*?\"(.*?)\",\\\"?action\\\"?\\s*?:\\s*?(.*)", Pattern.DOTALL | Pattern.MULTILINE);



    @Override
    public BaseDao<CodeGenSetting> getRepository() {
        return codeGenSettingMapper;
    }

    public void genCode(CodeGenSetting ent){
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletResponse response=attributes.getResponse();

            String settingStr=ent.getSettingJson();
            JSONObject settingJson=JSONObject.parseObject(settingStr);

            //业务模型主键
            String bodefId= settingJson.getString("bodefId");
            //表单相关配置
            JSONObject formDataSetting= settingJson.getJSONObject("formDataSetting");
            //全局变量
            JSONObject globaValJson= formDataSetting.getJSONObject("globaValJson");
            //表单业务实体对象
            JSONObject mainEntityJson = settingJson.getJSONObject("mainEntity");
            //表单查询配置信息
            JSONArray searchFields = mainEntityJson.getJSONArray("searchFields");
            //需要生成的文件模板
            JSONArray files=formDataSetting.getJSONArray("fileList");

            //保存全局变量
            if(StringUtils.isEmpty(ent.getId())){
                insert(ent);
            }else {
                update(ent);
            }

            FormBoEntity formBoEntity =getFormBoEntity(mainEntityJson);
            //FormBoEntity formBoEntity = formBoEntityServiceImpl.getByDefId(bodefId, true);

            //获取生成文件的变量
            Map<String,String> vars= getVars(globaValJson);

            Map<String,Object> model=new HashMap<>();

            TemplateHashModel fileUtil = FtlUtil.generateStaticModel(ReaderFileUtil.class);
            model.put("fileUtil", fileUtil);
            TemplateHashModel util = FtlUtil.generateStaticModel(StringUtils.class);
            model.put("util", util);


            vars.put("date", DateUtils.getTime());
            //主表Id
            String mainClassId= IdGenerator.getIdStr();
            vars.put("mainClassId", mainClassId);
            model.put("vars",vars);

            //获取生成文件路径
            String basePath=getBasePath(vars);

            /**
             * 设置ENT变量。
             */
            setVars(formBoEntity,vars,mainEntityJson);

            //获取引用字段列表
            String boEntId=formBoEntity.getId();
            Map<String,List<FormBoAttr>> resAttrs=new HashMap<>();
            setResAttrs(formBoEntity,resAttrs);
            //获取当前实体的所有引用属性
            formBoEntity.setBoAttrRefList(resAttrs.get(boEntId));

            //获取主从表对应关系（存在从表）
            JSONObject boRelations=new JSONObject();
            setBoRelations(formBoEntity,resAttrs,boRelations);
            model.put("boRelations",boRelations);
            model.put("boRelationsStr",formatJsonToStr(boRelations));


            setVueJs(formBoEntity,vars,model,settingJson);
            //需要修改
            model.put("formAlias",formBoEntity.getAlias());

            //最后生成***.json文件
            JSONObject jsonFile=createJsonFileForm(true,settingJson,formBoEntity,globaValJson);
            model.put("jsonFile",formatJsonToStr(jsonFile));

            model.put("settingJson",settingJson);


            String mainSubType = globaValJson.getString("mainSubType");
            for(int i=0;i<files.size();i++){
                JSONObject file=files.getJSONObject(i);
                //主从分离模式
                model.put("mainSubType",mainSubType);

                List<FormTemplate> templateList = formTemplateService.getCodeGenByParams(
                        "genCode",file.getString("fileName"),mainSubType);
                FormTemplate template = templateList.get(0);
                boolean single= MBoolean.YES.val.equals( template.getSingle());

                model.put("model",formBoEntity);

                //对变量进行处理。
                vars.putAll(formBoEntity.getVars());
                genFile(model,template,vars,basePath);
                //如果是单个文件模式，则每个都生成一次。
                if("mainAloneSub".equals(mainSubType)){
                    continue;
                }
                boolean hasSub= BeanUtil.isNotEmpty(formBoEntity.getBoEntityList());
                if(!single && hasSub){
                    for(FormBoEntity subEnt :formBoEntity.getBoEntityList()){
                        subEnt.setBoAttrRefList(resAttrs.get(subEnt.getId()));
                        model.put("model",subEnt);
                        vars.putAll(subEnt.getVars());
                        genFile(model,template,vars,basePath);
                    }
                }
            }

            if("mainAloneSub".equals(mainSubType)){
                JSONObject vueJsObj = new JSONObject();
                JSONObject jsonFileObj = new JSONObject();
                for(FormBoEntity subEnt :formBoEntity.getBoEntityList()){
                    String subEntId = subEnt.getId();
                    //主从分离模式，从表生成***Data.js文件
                    JSONObject subVueJs =vueJsObj.getJSONObject(subEntId);
                    if(BeanUtil.isEmpty(subVueJs)){
                        subVueJs = setVueJs(subEnt, vars, model, settingJson);
                        subVueJs.put("treeJson","{}");
                        vueJsObj.put(subEntId,subVueJs);
                    }
                    model.put("vueJs",subVueJs);

                    //主从分离模式，从表生成***.json文件
                    JSONObject subjsonFile =jsonFileObj.getJSONObject(subEntId);
                    if(BeanUtil.isEmpty(subjsonFile)){
                        subjsonFile = createJsonFileForm(false,settingJson,subEnt,globaValJson);
                        jsonFileObj.put(subEntId,subjsonFile);
                    }
                    model.put("jsonFile",formatJsonToStr(subjsonFile));

                    for(int i=0;i<files.size();i++){
                        JSONObject file=files.getJSONObject(i);
                        //主从模式
                        model.put("mainSubType","mainSub");

                        List<FormTemplate> templateList = formTemplateService.getCodeGenByParams(
                                "genCode",file.getString("fileName"),mainSubType);
                        FormTemplate template = templateList.get(0);
                        boolean single= MBoolean.YES.val.equals( template.getSingle());
                        Map<String, String> subVars = subEnt.getVars();
                        subVars.put("isSub","NO");
                        subVars.put("isSubMain","YES");
                        subEnt.setVars(subVars);
                        model.put("model",subEnt);
                        //对变量进行处理。
                        vars.putAll(subVars);
                        genFile(model,template,vars,basePath);
                        //如果是单个文件模式，则每个都生成一次。
                        boolean hasSub= BeanUtil.isNotEmpty(subEnt.getBoEntityList());
                        if(!single && hasSub){
                            for(FormBoEntity sunEnt :subEnt.getBoEntityList()){
                                subEnt.setBoAttrRefList(resAttrs.get(subEnt.getId()));
                                Map<String, String> sunVars = sunEnt.getVars();
                                sunVars.put("mainSubClassVar",subVars.get("classVar"));
                                sunVars.put("isSub","YES");
                                sunVars.put("isSubMain","NO");
                                sunEnt.setVars(sunVars);
                                model.put("model",sunEnt);
                                vars.putAll(sunVars);
                                genFile(model,template,vars,basePath);
                            }
                        }
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
            log.error("***** is error: message={}",ExceptionUtil.getExceptionMessage(e));
        }
    }

    private FormBoEntity getFormBoEntity(JSONObject mainEntityJson){
        String formBoEntityStr = JSONObject.toJSONString(mainEntityJson);
        FormBoEntity entity=JSONObject.parseObject(formBoEntityStr,FormBoEntity.class);
        JSONArray childrens = mainEntityJson.getJSONArray("childrens");
        for(FormBoEntity subEnt :entity.getBoEntityList()){
            if(BeanUtil.isNotEmpty(subEnt.getChildrens())){
                setSubEntity(subEnt,childrens);
            }
        }
        return entity;
    };

    private void setSubEntity(FormBoEntity subEnt,JSONArray childrens){
        List<Tree> childrens1 =new ArrayList<>();
        List<FormBoEntity> boEntityList = new ArrayList<>();
        String id = subEnt.getId();
        for(int i=0;i<childrens.size();i++){
            JSONObject sub = childrens.getJSONObject(i);
            if(!sub.getString("id").equals(id)){
                continue;
            }
            JSONArray sunChildrens = sub.getJSONArray("childrens");
            for(int sunNum=0;sunNum<sunChildrens.size();sunNum++){
                JSONObject sun = sunChildrens.getJSONObject(sunNum);
                String sunStr = JSONObject.toJSONString(sun);
                FormBoEntity sunEntity=JSONObject.parseObject(sunStr,FormBoEntity.class);
                childrens1.add(sunEntity);
                boEntityList.add(sunEntity);
            }
        }

        subEnt.setChildrens(childrens1);
        subEnt.setBoEntityList(boEntityList);
    }

    /**
     * 获取引用字段列表
     * @param mainEnt
     * @return
     */
    private void setResAttrs(FormBoEntity mainEnt,Map<String,List<FormBoAttr>> resAttrs){
        String boEntId=mainEnt.getId();
        formBoEntityServiceImpl.setIdField(mainEnt);
        List<FormBoAttr> refsAttr = formBoAttrService.getAllRefByEntId(boEntId);
        if(BeanUtil.isEmpty(refsAttr)){
            refsAttr=new ArrayList<>();
        }
        resAttrs.put(boEntId,refsAttr);
        List<FormBoEntity> subEnts = mainEnt.getBoEntityList();
        if(BeanUtil.isNotEmpty(mainEnt.getBoEntityList())){
            for(FormBoEntity subEnt :subEnts){
                formBoEntityServiceImpl.setIdField(subEnt);
                String subEntId = subEnt.getId();
                List<FormBoAttr> subRefAttrs = resAttrs.get(subEntId);
                if(BeanUtil.isEmpty(subRefAttrs)){
                    subRefAttrs = formBoAttrService.getAllRefByEntId(subEntId);
                }
                if(BeanUtil.isEmpty(subRefAttrs)){
                    subRefAttrs=new ArrayList<>();
                }
                resAttrs.put(subEntId,subRefAttrs);
            }
        }
        mainEnt.setBoEntityList(subEnts);
    }

    public JSONObject setBoRelations(FormBoEntity mainEnt,Map<String,List<FormBoAttr>> resAttrs,JSONObject boRelations){
        if(BeanUtil.isEmpty( mainEnt.getBoEntityList())){
            return boRelations;
        }

        for(FormBoEntity subEnt :mainEnt.getBoEntityList()){
            String genMode = subEnt.getGenMode();
            JSONObject relation=new JSONObject();
            Map<String, String> vars = subEnt.getVars();
            String classVar = vars.get("classVar");
            relation.put("alias",classVar);
            FormBoRelation boRelation = subEnt.getBoRelation();
            String fkField = boRelation.getFkField();
            String pkField = boRelation.getPkField();


            //主键设置
            relation.put("fkMainId","id");
            if("db".equals(genMode)){
                relation.put("fkMainId",subEnt.getIdFieldName());
            }
            relation.put("fkMainIdName",subEnt.getIdField());

            
            String fkFieldName=null;
            relation.put("fkField",fkField);
            if(subEnt.getIdField().equals(fkField)){
                fkFieldName = subEnt.getIdFieldName();
                relation.put("fkFieldName",fkFieldName);
            }else {
                fkFieldName = getFiledName(subEnt.getGenMode(),subEnt.getBoAttrList(),resAttrs.get(subEnt.getId()),fkField);
                relation.put("fkFieldName",fkFieldName);
            }
            if(StringUtils.isNotEmpty(fkFieldName)){
                relation.put("fkFieldFun",StringUtils.makeFirstLetterUpperCase(fkFieldName));
            }

            String pkFieldName=null;
            relation.put("pkField",pkField);
            if(mainEnt.getIdField().equals(pkField)){
                pkFieldName = mainEnt.getIdFieldName();
                relation.put("pkFieldName",pkFieldName);
            }else {
                pkFieldName=getFiledName(mainEnt.getGenMode(),mainEnt.getBoAttrList(),resAttrs.get(mainEnt.getId()),pkField);
                relation.put("pkFieldName",pkFieldName);
            }

            if(StringUtils.isNotEmpty(pkFieldName)){
                relation.put("pkFieldFun",StringUtils.makeFirstLetterUpperCase(pkFieldName));
            }
            List<FormBoEntity> sunEnts = subEnt.getBoEntityList();
            if(BeanUtil.isNotEmpty(sunEnts)){
                setBoRelations(subEnt,resAttrs,relation);
            }
            boRelations.put(classVar,relation);
        }
        return boRelations;
    }

    public String getFiledName(String genMode,List<FormBoAttr> boAttrList,List<FormBoAttr> boAttrRefList,String key){
        String filedName="";
        if(StringUtils.isEmpty(key)){
            return filedName;
        }
        if(!"db".equals(genMode)){
            if("ID_".equals(key)){
                return "id";
            }
            if("REF_ID_".equals(key)){
                return "refId";
            }
            if("PARENT_ID_".equals(key)){
                return "parentId";
            }
        }

        if(BeanUtil.isNotEmpty(boAttrList)){
            for (FormBoAttr attr:boAttrList) {
                if(key.equals(attr.getFieldName())){
                    return attr.getName();
                }
            }
        }
        if(BeanUtil.isNotEmpty(boAttrRefList)){
            for (FormBoAttr attr:boAttrRefList) {
                if(key.equals(attr.getFieldName())){
                    return attr.getName();
                }
            }
        }
        return filedName;
    }


    /**
     * 生成json映射文件
     */
    public JSONObject createJsonFileForm(boolean isMain,JSONObject settingJson, FormBoEntity formBoEntity, JSONObject globaValJson){
        String mainKey = StringUtils.makeFirstLetterLowerCase(globaValJson.getString("class")) ;
        if(!isMain){
            Map<String, String> vars = formBoEntity.getVars();
            mainKey=vars.get("classVar");
        }
        JSONObject feldJson=new JSONObject();
        JSONObject modeJson=new JSONObject();
        JSONObject tablesJson=new JSONObject();

        createTablesJson(1,true,mainKey,settingJson,formBoEntity,formBoEntity,globaValJson,tablesJson);
        feldJson.put("tables",tablesJson);
        return feldJson;
    }

    public void createInitDataJson(JSONObject feldJson,FormBoEntity formBoEntity){
        JSONObject initData =new JSONObject();
        //从表fileJson
        List<FormBoEntity> subList = formBoEntity.getBoEntityList();
        if(BeanUtil.isNotEmpty(subList)){
            for (FormBoEntity sub:subList) {
                JSONObject subAttrJson=new JSONObject();
                for (FormBoAttr attr:sub.getBoAttrList()) {
                    subAttrJson.put(attr.getName(),"");
                }
                String alias = sub.getAlias();
                Map<String, String> subVars = sub.getVars();
                if(BeanUtil.isNotEmpty(subVars) && subVars.containsKey("classVar")){
                    alias =subVars.get("classVar");
                }
                initData.put(alias,subAttrJson);
            }
        }
        feldJson.put("initData",initData);
    }




    public void createTablesJson(int level,boolean isMain,String mainKey,JSONObject settingJson,FormBoEntity mainBoEnt,
                                 FormBoEntity formBoEntity,JSONObject globaValJson,JSONObject tablesJson){
        //最多两层
        if(level>2){
            return;
        }
        String genMode = formBoEntity.getGenMode();
        String alias = formBoEntity.getAlias();
        String classAlias = alias;
        Map<String, String> vars = formBoEntity.getVars();
        if(!isMain && BeanUtil.isNotEmpty(vars) && vars.containsKey("classVar")){
            classAlias =vars.get("classVar");
        }
        JSONObject tableJson=new JSONObject();
        tableJson.put("genMode",genMode);
        tableJson.put("isMain","true");
        tableJson.put("pkId","id");
        if("db".equals(genMode)){
            tableJson.put("pkId",formBoEntity.getIdFieldName());
        }
        tableJson.put("class",StringUtils.makeFirstLetterUpperCase(mainKey));
        //从表属性
        if(!isMain){
            tableJson.put("isMain","false");
            tableJson.put("class",StringUtils.makeFirstLetterUpperCase(classAlias));
            tableJson.put("mainId","id");
            tableJson.put("refId","refId");
            tableJson.put("type","onetomany");
        }

        JSONObject fileds=new JSONObject();
        creataBaseColsFromFormAndCreate(mainBoEnt,formBoEntity,isMain,fileds,mainKey);

        List<FormBoAttr> mainAttrs = formBoEntity.getBoAttrList();
        List<FormBoAttr> refsAttr = formBoAttrService.getAllRefByEntId(formBoEntity.getId());
        JSONObject model=new JSONObject();
        model.put("boAttrRefList",refsAttr);
        createAttrJson(genMode,fileds,mainAttrs,model);

        //从表fileJson
        List<FormBoEntity> subList = formBoEntity.getBoEntityList();
        if(BeanUtil.isNotEmpty(subList)){
            for (FormBoEntity sub:subList) {
                JSONObject subAttrJson=new JSONObject();
                String subClassAlias = sub.getAlias();;
                Map<String, String> subVars = sub.getVars();
                if(BeanUtil.isNotEmpty(subVars) && subVars.containsKey("classVar")){
                    subClassAlias =subVars.get("classVar");
                }
                subAttrJson.put("genMode",sub.getGenMode());
                subAttrJson.put("subTableAlias",StringUtils.makeFirstLetterUpperCase(subClassAlias));
                subAttrJson.put("name",sub.getName());
                subAttrJson.put("fieldName",subClassAlias);
                subAttrJson.put("comment","主键");
                subAttrJson.put("control","rx-sub");
                subAttrJson.put("ctr","subTablesVal");
                subAttrJson.put("isSingle",0);
                subAttrJson.put("valType","varchar");
                fileds.put(subClassAlias+"List",subAttrJson);
                //递归从表
                createTablesJson(level+1,false,mainKey,settingJson,mainBoEnt,sub,globaValJson,tablesJson);
            }
        }

        tableJson.put("fileds",fileds);
        tablesJson.put(mainKey,tableJson);
        //从表属性
        if(!isMain){
            tablesJson.put(classAlias,tableJson);
        }
        createInitDataJson(tableJson,formBoEntity);
    }

    /**
     * 设置主键，副键，父键值映射json
     * @param foEnt
     * @param isMain
     * @param fileds
     * @param mainKey
     */
    public void creataBaseColsFromFormAndCreate(FormBoEntity mainBoEnt,FormBoEntity foEnt,boolean isMain,JSONObject fileds,String mainKey){
        String genMode = foEnt.getGenMode();
        JSONObject defaultCtlConfigJson = ReaderFileUtil.getDefaultCtlConfigJson();

        JSONObject idJson = defaultCtlConfigJson.getJSONObject("ID_");
        JSONObject fefIdJson = defaultCtlConfigJson.getJSONObject("REF_ID_");
        if(!isMain){
            fefIdJson.put("mainAlias",mainKey);
        }
        JSONObject parentIdJson = defaultCtlConfigJson.getJSONObject("PARENT_ID_");
        if("db".equals(genMode)){
            //主键
            idJson.put("name",foEnt.getIdFieldName());
            idJson.put("fieldName",foEnt.getIdField());
            idJson.put("comment",foEnt.getIdFieldComment());
            fileds.put(foEnt.getIdFieldName(),idJson);
        }else {
            fileds.put("id",idJson);
            fileds.put("refId",fefIdJson);
            fileds.put("parentId",parentIdJson);
        }
    }


    public void createAttrJson(String genMode, JSONObject fileds,List<FormBoAttr> attrs,JSONObject model){
        for (FormBoAttr attr:attrs) {
            JSONObject newJson=new JSONObject();
            String control = attr.getControl();
            Integer isSingle = attr.getIsSingle();
            newJson.put("name",attr.getName());
            newJson.put("fieldName",attr.getFieldName());
            newJson.put("comment",attr.getComment());
            newJson.put("control",control);
            newJson.put("isSingle",isSingle);
            newJson.put("valType",attr.getDataType());

            if(isSingle==0){
                if("rx-address".equals(control)){
                    newJson.put("ctr","rx-address");
                }else {
                    newJson.put("ctr","doubleVal");
                }
            }else{
                newJson.put("ctr","singleVal");
            }
            JSONObject extJson =new JSONObject();
            //地址控件，默认包括省市区、详细地址
            if("rx-address".equals(control)){
                int contains=0;
                String extJsonStr = attr.getExtJson();
                if(StringUtils.isEmpty(extJsonStr)){
                    newJson.put("contains","3");
                }else {
                    extJsonStr = StringUtils.replace(extJsonStr,"[\\]","");
                    extJson =JSONObject.parseObject(extJsonStr);
                    JSONObject setting = extJson.getJSONObject("setting");
                    if(setting.getBoolean("isAddress")){
                        contains++;
                    }
                    if(setting.getBoolean("isCity")){
                        contains++;
                    }
                    if(setting.getBoolean("isCounty")){
                        contains++;
                    }
                    newJson.put("contains",contains);
                }
                if("db".equals(genMode)){
                    newJson.put("extJson",extJson);
                }
            }else if(isSingle==0 && "db".equals(genMode)){
                String extJsonStr = attr.getExtJson();
                if(StringUtils.isNotEmpty(extJsonStr)){
                    extJsonStr = StringUtils.replace(extJsonStr,"[\\]","");
                    extJson = JSONObject.parseObject(extJsonStr);
                }
                JSONObject refJson =new JSONObject();
                String refKey = extJson.getString("ref");
                refJson.put("ref",refKey);
                JSONObject refCol =null;
                if(StringUtils.isNotEmpty(refKey)){
                   // refCol=ReaderFileUtil.getColByColList(model, refKey, "fieldName");
                }
                if(BeanUtil.isNotEmpty(refCol)){
                    refJson.put("refName",refCol.getString("name"));
                }
                newJson.put("extJson",refJson);
            }

            fileds.put(attr.getName(),newJson);
        }
    }

    /**
     * 生成VUEJS页面。
     * @param formBoEntity
     * @param vars
     * @return
     * @throws TemplateException
     * @throws IOException
     */
    public JSONObject setVueJs(FormBoEntity formBoEntity, Map<String,String> vars,Map<String,Object> model,JSONObject settingJson) throws TemplateException, IOException {

        JSONObject vueJs = new JSONObject();
        JSONObject defaultCtlConfigJson = ReaderFileUtil.getDefaultCtlConfigJson();
        //从表
        List<FormBoEntity> subList = formBoEntity.getBoEntityList();
        //formulas
        JSONObject formulas = new JSONObject();
        JSONObject mainFormulaJson = new JSONObject();
        formulas.put("main",mainFormulaJson);
        if(BeanUtil.isNotEmpty(subList)){
            for (FormBoEntity subBo:subList) {
                String subBoAlias = subBo.getAlias();
                Map<String, String> subVars = subBo.getVars();
                if(BeanUtil.isNotEmpty(subVars) && subVars.containsKey("classVar")){
                    subBoAlias =subVars.get("classVar");
                }
                JSONObject subJson = new JSONObject();
                formulas.put(subBoAlias,subJson);
            }
        }
        vueJs.put("formulas",formatJsonToStr(formulas));
        vueJs.put("rules","{}");
        vueJs.put("calcAreas","{}");

        //左树配置信息
        String listType = settingJson.getString("listType");
        JSONObject treeObj = new JSONObject();
        treeObj.put("listSearchType",listType);

        JSONObject treeJson=settingJson.getJSONObject("treeJson");
        JSONObject searchTreeCol =null;
        if(BeanUtil.isNotEmpty(treeJson)){
            treeObj.put("searchTreeColName",treeJson.getString("searchTreeColName"));
            treeObj.put("treeType",treeJson.getString("treeType"));
            searchTreeCol =treeJson.getJSONObject("searchTreeCol");
        }
        if(BeanUtil.isNotEmpty(searchTreeCol)){
            treeObj.put("isSingle",searchTreeCol.getIntValue("isSingle"));
        }

        vueJs.put("treeJson",formatJsonToStr(treeObj));

        //校验规则
        JSONObject validRulesJson = new JSONObject();
        JSONObject mainvalidRuleJson = new JSONObject();
        List<FormBoAttr> mainAttrs = formBoEntity.getBoAttrList();
        createRule(mainvalidRuleJson,mainAttrs,defaultCtlConfigJson);
        validRulesJson.put("main",mainvalidRuleJson);

        if(com.redxun.common.tool.BeanUtil.isNotEmpty(subList)){
            for (FormBoEntity subBo:subList) {
                String subBoAlias = subBo.getAlias();
                Map<String, String> subVars = subBo.getVars();
                if(BeanUtil.isNotEmpty(subVars) && subVars.containsKey("classVar")){
                    subBoAlias =subVars.get("classVar");
                }
                JSONObject subJson = new JSONObject();
                subJson.put("comment",subBoAlias);
                subJson.put("required",false);
                JSONObject fieldsJson = new JSONObject();
                List<FormBoAttr> subBoAttrs = subBo.getBoAttrList();
                createRule(fieldsJson,subBoAttrs,defaultCtlConfigJson);
                subJson.put("fields",fieldsJson);
                validRulesJson.put(subBoAlias,subJson);
            }
        }

        vueJs.put("validRules",formatJsonToStr(validRulesJson));
        vueJs.put("customquery","{}");
        vueJs.put("model",formBoEntity);
        vueJs.put("vars",vars);

        JSONObject dataJson = new JSONObject();
        createInitDataJson(dataJson,formBoEntity);
        vueJs.put("data",formatJsonToStr(dataJson));
        vueJs.put("initData",formatJsonToStr(dataJson.getJSONObject("initData")));

        //元数据
        JSONObject metadata = new JSONObject();
        //主表数据
        for (FormBoAttr attr:mainAttrs) {
            JSONObject attrJson = getContrelJson(attr,defaultCtlConfigJson);
            metadata.put(attr.getId(),attrJson);
        }
        //从表
        if(BeanUtil.isNotEmpty(subList)){
            for (FormBoEntity subBo:subList) {
                String subBoAlias = subBo.getAlias();
                Map<String, String> subVars = subBo.getVars();
                if(BeanUtil.isNotEmpty(subVars) && subVars.containsKey("classVar")){
                    subBoAlias =subVars.get("classVar");
                }
                JSONObject subJson = defaultCtlConfigJson.getJSONObject("rx-table");
                subJson.put("comment",subBoAlias);
                subJson.put("name",subBo.getName());
                subJson.put("tableId",subBo.getId());

                JSONArray fieldsList = new JSONArray();
                List<FormBoAttr> subBoAttrs = subBo.getBoAttrList();
                for (FormBoAttr attr:subBoAttrs) {
                    JSONObject attrJson = getContrelJson(attr,defaultCtlConfigJson);
                    attrJson.put("idx_",attr.getId());
                    attrJson.put("id",attr.getId());
                    fieldsList.add(attrJson);
                }
                subJson.put("fields",fieldsList);

                subJson.put("buttons",createDefaultSubButtons());

                metadata.put(subBo.getId(),subJson);
            }
        }
        vueJs.put("metadata",formatJsonToStr(metadata));

        model.put("vueJs",vueJs);
        return vueJs;
    }

    private String formatJsonToStr(JSONObject json){
        if(BeanUtil.isEmpty(json)){
            return "{}";
        }
        return  JSON.toJSONString(json, SerializerFeature.PrettyFormat,
                SerializerFeature.WriteMapNullValue,SerializerFeature.WriteDateUseDateFormat);
    }

    public void createRule(JSONObject ruleJson,List<FormBoAttr> boAttrs,JSONObject defaultCtlConfigJson){
        for (FormBoAttr attr:boAttrs) {
            JSONObject rule = new JSONObject();

            Integer lengthVal = attr.getLength();
            if(lengthVal==null){
                rule.put("vtype",getCtlLengthVal(attr.getControl(),"length",defaultCtlConfigJson));
            }else {
                rule.put("vtype","length:"+lengthVal);
            }
            List<String> dateList = new ArrayList<>(Arrays.asList("rx-date","rx-month","rx-date-range","rx-week","rx-time"));
            if(dateList.contains(attr.getControl())){
                rule.put("vtype","length:100");
            }

            if("rx-address".equals(attr.getControl())){
                rule.put("vtype","");
            }
            rule.put("comment",attr.getComment());
            rule.put("required",getBooleanCtlValByKey(attr.getControl(),"required",defaultCtlConfigJson));
            rule.put("valmode",getCtlValByKey(attr.getControl(),"valmode",defaultCtlConfigJson));
            if(0==attr.getIsSingle() && !"rx-address".equals(attr.getControl())){
                rule.put("valmode","double");
            }
            ruleJson.put(attr.getName(),rule);
        }
    }

    public JSONArray createDefaultSubButtons(){
        JSONArray buttonsList = new JSONArray();

        return buttonsList;
    }

    public JSONObject getContrelJson(FormBoAttr attr,JSONObject defaultCtlConfigJson){
        JSONObject  attrJson=new JSONObject();
        attrJson.put("ctltype",attr.getControl());
        attrJson.put("comment",attr.getComment());
        attrJson.put("name",attr.getName());
        JSONObject defaultAttrJson = defaultCtlConfigJson.getJSONObject(attr.getControl());
        if(BeanUtil.isEmpty(defaultAttrJson)){
            return attrJson;
        }else {
            if("rx-radio".equals(attr.getControl())){
                setValMode(attrJson,attr.getIsSingle());
            }
            if("rx-switch".equals(attr.getControl())){
                attrJson.put("check","Y");
                attrJson.put("uncheck","N");
                setValMode(attrJson,attr.getIsSingle());
            }
            if("rx-tree-select".equals(attr.getControl())){
                setSingle(attrJson,attr.getIsSingle());
                setValMode(attrJson,attr.getIsSingle());
            }

            if("rx-form-select".equals(attr.getControl())){
                setValMode(attrJson,attr.getIsSingle());
            }

            if("rx-upload".equals(attr.getControl())){
                setValMode(attrJson,attr.getIsSingle());
            }

            if("rx-date".equals(attr.getControl())){
                attrJson.put("format","YYYY-MM-DD HH:mm:ss");
                attrJson.put("type","date");
            }

            if(!"rx-address".equals(attr.getControl())){
                setExtJson(attrJson,attr.getExtJson());
            }

        }
        return attrJson;
    }

    private void setSingle(JSONObject attrJson,Integer isSingle){
        if(0==isSingle){
            attrJson.put("single",false);
        }else {
            attrJson.put("single",true);
        }
    }

    private void setValMode(JSONObject attrJson,Integer isSingle){
        if(0==isSingle){
            attrJson.put("valmode","double");
        }else {
            attrJson.put("valmode","single");
        }
        attrJson.put("mode","default");
    }

    private void setExtJson(JSONObject attrJson,String extJsonStr){
        JSONObject extJson = null;
        if(StringUtils.isEmpty(extJsonStr)){
            return;
        }
        extJson=JSONObject.parseObject(extJsonStr);
        if(BeanUtil.isEmpty(extJson)){
            return;
        }
        JSONObject dataSource = extJson.getJSONObject("dataSource");
        if(BeanUtil.isEmpty(dataSource)){
            return;
        }

        String fromVal =dataSource.getString("from");
        if("sql".equals(fromVal)){
            String sqlDef = dataSource.getString("sqlDef");
            if(StringUtils.isNotEmpty(sqlDef)){
                dataSource.put("sqlDef",JSONObject.parseObject(sqlDef));
            }
        }else if("custom".equals(fromVal)){
            String options = dataSource.getString("options");
            if(StringUtils.isNotEmpty(options)){
                dataSource.put("options",JSONArray.parseArray(options));
            }
        }
        attrJson.put("datasource",dataSource);
    }


    public String getCtlLengthVal(String ctlName,String key,JSONObject defaultCtlConfigJson){
        String val=getCtlValByKey(ctlName,key,defaultCtlConfigJson);
        if(StringUtils.isEmpty(val)){
            return "";
        }
        return key+":"+val;
    }

    public boolean getBooleanCtlValByKey(String ctlName,String key,JSONObject defaultCtlConfigJson){
        boolean val=false;
        JSONObject ctlJson = defaultCtlConfigJson.getJSONObject(ctlName);
        if(com.redxun.common.tool.BeanUtil.isEmpty(ctlJson)){
            return val;
        }
        val=ctlJson.getBoolean(key);
        if(!val){
            return false;
        }
        return val;
    }

    public String getCtlValByKey(String ctlName,String key,JSONObject defaultCtlConfigJson){
        String val="";
        JSONObject ctlJson = defaultCtlConfigJson.getJSONObject(ctlName);
        if(com.redxun.common.tool.BeanUtil.isEmpty(ctlJson)){
            return val;
        }
        val=ctlJson.getString(key);
        if(StringUtils.isEmpty(val)){
            return val;
        }
        return val;
    }


    /**
     * 生成VUE页面
     * @param formBoEntity
     * @param vars
     * @return
     * @throws TemplateException
     * @throws IOException
     */
    public String getVueTemplate(FormBoEntity formBoEntity,Map<String,String> vars) throws TemplateException, IOException {
        Map<String,Object> model=new HashMap<>();
        model.put("model",formBoEntity);
        model.put("vars",vars);

        TemplateHashModel fileUtil = FtlUtil.generateStaticModel(ReaderFileUtil.class);
        model.put("fileUtil", fileUtil);

        //主从，主从分离
        FormTemplate template = formTemplateService.getById("1526473452484186113");
        String content=freemarkEngine.parseByStringTemplate(model, template.getTemplate());

        return  content;
    }


    /**
     * 生成文件
     * @param model             模型对象
     * @param template   文件模板
     * @param vars              变量
     * @param basePath          基础路径
     * @throws IOException
     * @throws TemplateException
     */
    public void genFile(Map<String,Object> model,FormTemplate template,Map<String,String> vars,String basePath) throws IOException, TemplateException {
        //获取解析后的内容。
        String fileContent= ftlEngine.parseByStringTemplate(model,template.getTemplate());

        //生成文件路径。
        String path=getPath(template,vars);
        String filePath=basePath + path;

        FileUtil.writeFile(filePath,fileContent);
    }

    /**
     * 获取生成文件的路径。
     * @param template
     * @param vars
     * @return
     */
    public String getPath(FormTemplate template,Map<String ,String> vars){
        String fileName=template.getFileName();
        String path=template.getPath();
        fileName=replace(fileName,vars);
        path=replace(path,vars).replace("\\","/");
        String filePath=path.endsWith("/")?path :path +"/" + fileName;
        return filePath;
    }

    /**
     * 将字符串中的 {name} 标识符进行替换。
     * @param template
     * @param vars
     * @return
     */
    public String replace(String template,Map<String,String> vars){
        Matcher regexMatcher = REGEX.matcher(template);
        while (regexMatcher.find()) {
            String key=regexMatcher.group(1);
            String val=vars.get(key);
            template=template.replace(regexMatcher.group(0),val);
        }
        return template;
    }

    /**
     * 给每个实体设置变量。
     * @param boEntity
     * @param vars
     */
    public void  setVars(FormBoEntity boEntity,Map<String,String> vars,JSONObject mainEntityJson){
        Map<String, String> main= new HashMap<>();
        String packageName = vars.get("package");
        String className=null;
        String alias=boEntity.getAlias();
        if(vars.containsKey("class")){
            className=vars.get("class");
        }
        String classVar=alias;
        if(StringUtils.isEmpty(className)){
            className=alias;
        }else {
            classVar=StringUtils.makeFirstLetterLowerCase(className);
        }
        className=StringUtils.makeFirstLetterUpperCase(className);
        main.put("classVar",classVar);
        main.put("class",className);
        main.put("package",packageName);
        main.put("entAlias",alias);
        boEntity.setVars(main);

        JSONArray childrens =mainEntityJson.getJSONArray("childrens");
        if(BeanUtil.isEmpty(childrens)){
            return;
        }

        setSubEntityVars(boEntity,childrens,packageName,true);
    }

    private void setSubEntityVars(FormBoEntity boEntity,JSONArray childrens,String packageName,boolean isMain){
        List<FormBoEntity>  subList = new ArrayList<>();
        for(Object subObj:childrens){
            JSONObject subJson=(JSONObject)subObj;
            String subAlias=subJson.getString("alias");
            String isSelect=subJson.getString("select");
            if(!"YES".equals(isSelect)){
                continue;
            }
            String subClass= subJson.getString("subClass");
            String subClassVar=subAlias;
            if(StringUtils.isEmpty(subClass)){
                subClass=StringUtils.makeFirstLetterUpperCase(subAlias);
            }else {
                subClassVar=StringUtils.makeFirstLetterLowerCase(subClass);
            }
            Map<String, String> subMap = new HashMap<>();
            subMap.put("classVar",subClassVar);
            subMap.put("class",subClass);
            subMap.put("package",packageName);
            subMap.put("entAlias",subAlias);
            subMap.put("isSub","YES");
            for(FormBoEntity subEnt:boEntity.getBoEntityList()){
                if(subEnt.getAlias().equals(subAlias)){
                    subEnt.setVars(subMap);
                    JSONArray subChildrens = subJson.getJSONArray("children");
                    if(isMain &&BeanUtil.isNotEmpty(subChildrens)){
                        for (Object sunObj:subChildrens) {
                            setSubEntityVars(subEnt,subChildrens,packageName,false);
                        }
                    }
                    subList.add(subEnt);
                    break;
                }
            }
        }
        boEntity.setBoEntityList(subList);
    }

    /**
     * 获取生成路径。
     * @param vars
     * @return
     */
    public String getBasePath(Map<String,String> vars){
        if(vars.containsKey("genDir")){
            String val=vars.get("genDir");
            String basePath=val.replace("\\","/");
            if(!basePath.endsWith("/")){
                basePath+="/";
            }
            basePath+=System.currentTimeMillis() +"/";
            return  basePath;
        }
        return "";
    }

    /**
     * globaValJson 转成map对象。
     * @param globaValJson
     * @return
     */
    public Map<String,String> getVars(JSONObject globaValJson){
        Map<String,String> vars=new HashMap<>();
        Iterator<String> iterator = globaValJson.keySet().iterator();
        while(iterator.hasNext()){
            String key = iterator.next();
            String value= globaValJson.getString(key);
            vars.put(key,value);
        }
        return vars;
    }
}
