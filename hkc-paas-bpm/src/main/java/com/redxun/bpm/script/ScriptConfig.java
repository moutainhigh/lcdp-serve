package com.redxun.bpm.script;

import com.redxun.bpm.activiti.config.VariableConfig;
import com.redxun.bpm.core.entity.BpmInstVars;
import com.redxun.bpm.core.enums.DialogEmnu;
import com.redxun.bpm.core.enums.ProcessCmdAttrEnum;
import com.redxun.bpm.script.cls.*;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.SpringUtil;
import com.redxun.constvar.ConstVarContext;
import com.redxun.constvar.ConstVarType;
import com.redxun.dto.form.FormBoAttrDto;
import com.redxun.dto.form.FormBoEntityDto;
import com.redxun.feign.form.FormClient;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 脚本配置总入口
 */
@Component
public class ScriptConfig implements InitializingBean {

    @Resource
    FormClient formClient;

    /**
     * 获取脚本Classes
     * @param boAliases
     * @param vars
     * @return
     */
    public List<ScriptClass> getScriptClasses(String boAliases, List<VariableConfig> vars){
        List<ScriptClass> scriptClasses=new ArrayList<>();
         //1.函数型脚本。
        Collection<IScript> beans = SpringUtil.getBeans(IScript.class);
        for(IScript script:beans){
            ScriptClass scriptClass= initClassScriptType(script.getClass(),IScriptNode.NODE_TYPE_FUN);
            scriptClasses.add(scriptClass);
        }
        //2.处理Bo定义配置
        ScriptClass boClass= getFormVars(boAliases);
        if(boClass!=null){
            scriptClasses.add(boClass);
        }
        //3. 流程变量
        ScriptClass varClass=getVarClass(vars);
        scriptClasses.add(varClass);
        //4. 加上审批动作类
        ScriptClass actionClass=getApproveAction();
        scriptClasses.add(actionClass);
        //5. 弹出对话框
        ScriptClass dialogClass=getDialogClass();
        scriptClasses.add(dialogClass);
        //6. 添加常量
        ScriptClass constVars=getConstVars();
        scriptClasses.add(constVars);

        return scriptClasses;
    }

    private ScriptClass getConstVars(){

        ScriptClass actionClass=new ScriptClass();
        actionClass.setNodeType(IScriptNode.NODE_TYPE_ATTR);
        actionClass.setDescription("常量");
        //加上审批动作
        List<ConstVarType> list = ConstVarContext.getTypes();

        for(ConstVarType en:list){
            ActionScriptNode node = new ActionScriptNode( en.getKey(),en.getName());
            actionClass.getChildren().add(node);
            node.setNodeType(IScriptNode.NODE_TYPE_ATTR);
        }
        return actionClass;
    }

    /**
     * 弹出对话框
     * @return
     */
    private ScriptClass getDialogClass(){
        ScriptClass dialogClass=new ScriptClass();
        dialogClass.setNodeType(IScriptNode.NODE_TYPE_DIALOG);
        dialogClass.setDescription("对话框");
        for(DialogEmnu en:DialogEmnu.values()){
            DialogScriptNode node = new DialogScriptNode(en.getField(),en.getTitle(),en.getType());
            dialogClass.getChildren().add(node);
            node.setNodeType(IScriptNode.NODE_TYPE_DIALOG);
        }
        return dialogClass;
    }

    /**
     * 获取审批动作。
     * @return
     */
    private ScriptClass getApproveAction(){
        ScriptClass actionClass=new ScriptClass();
        actionClass.setNodeType(IScriptNode.NODE_TYPE_ATTR);
        actionClass.setDescription("审批动作");
        //加上审批动作
        for(ProcessCmdAttrEnum en:ProcessCmdAttrEnum.values()){
            ActionScriptNode node = new ActionScriptNode(ActionScriptNode.VAR_PRE + "." + en.getKey(),en.getTitle(),en.getDescription());
            actionClass.getChildren().add(node);
            node.setNodeType(IScriptNode.NODE_TYPE_ATTR);
        }
        return actionClass;
    }

    /**
     * 获取表单数据。
     * @param boAliases
     * @return
     */
    private ScriptClass getFormVars(String   boAliases){
         if(StringUtils.isEmpty(boAliases)) {
             return null;
         }

        ScriptClass formClass=new ScriptClass();
        formClass.setNodeType(IScriptNode.NODE_TYPE_ATTR);
        formClass.setDescription("单据");

        String[] boAliasArr = boAliases.split("[,]");
        for(String boAlis:boAliasArr) {
            FormBoEntityDto formBoEntityDto=null;
            try {
                //初始化boDefId对象，
                formBoEntityDto = formClient.getBoFields(boAlis);
            }catch (Exception ex){
                ex.printStackTrace();
            }
            //找不到表单
            if (formBoEntityDto == null || StringUtils.isEmpty(formBoEntityDto.getAlias())) {
                continue;
            }
            FormItemNode formItemNode=getFormItemNode("",formBoEntityDto);
            List<IScriptNode> children= new ArrayList<>();
            //设置子Bo
            for(FormBoEntityDto subBoEntity:formBoEntityDto.getSubBoList()){
                children.add(getFormItemNode(formBoEntityDto.getAlias()+".sub__",subBoEntity));
            }
            formItemNode.getChildren().addAll(children);


            formClass.getChildren().add(formItemNode);
        }
        return  formClass;
    }

    /**
     * 获取变量。
     * @param vars
     * @return
     */
    private ScriptClass getVarClass(List<VariableConfig> vars){
        ScriptClass varClass=new ScriptClass();

        ProcessVarNode processVarNode=new ProcessVarNode("sysVars","系统变量");
        processVarNode.setLeaf(false);
        //添加默认流程变量
        for(BpmInstVars var:BpmInstVars.values()){
            ProcessVarNode varNode=new ProcessVarNode(ProcessVarNode.VAR_PRE+"." + var.getKey(),var.getName());
            varNode.setLeaf(true);
            processVarNode.getChildren().add(varNode);
        }

        //页面的配置变量
        if(vars!=null) {
            ProcessVarNode customVarNode=new ProcessVarNode("customVars","自定义变量");
            customVarNode.setLeaf(false);
            for (VariableConfig var : vars) {
                ProcessVarNode varNode = new ProcessVarNode(ProcessVarNode.VAR_PRE+"." +var.getKey(), var.getLabel());
                varNode.setLeaf(true);
                customVarNode.getChildren().add(varNode);
            }
            varClass.getChildren().add(customVarNode);
        }

        varClass.setNodeType(IScriptNode.NODE_TYPE_ATTR);
        varClass.setDescription("流程变量");
        varClass.getChildren().add(processVarNode);

        return varClass;
    }

    private FormItemNode getFormItemNode(String preFix,FormBoEntityDto formBoEntityDto){
        FormItemNode formItemNode = new FormItemNode(preFix+formBoEntityDto.getAlias(), formBoEntityDto.getAlias(),
                formBoEntityDto.getName());
        //设置bo子属性显示
        for(FormBoAttrDto dto:formBoEntityDto.getBoAttrs()){
            FormItemNode subAttNode=new FormItemNode(preFix+formBoEntityDto.getAlias(),
                    dto.getName(),dto.getComment());
            subAttNode.setDataType(dto.getDataType());
            subAttNode.setLeaf(true);
            formItemNode.getChildren().add(subAttNode);
        }

        return formItemNode;
    }

    /**
     * 初始化类脚本类型
     * @param cls
     * @return
     */
    public ScriptClass initClassScriptType(Class cls,String nodeType){
        ClassScriptType classScriptType=(ClassScriptType)cls.getAnnotation(ClassScriptType.class);
        if(classScriptType==null){
            return null;
        }

        String preFix=classScriptType.type();
        String description=classScriptType.description();
        ScriptClass scriptClass=new ScriptClass(preFix,nodeType,description);
        scriptClass.setNodeType(nodeType);
        //读取方法名注解
        Method[] classMethods=cls.getMethods();


        //读取该类所有的方法
        for(Method method:classMethods){
            String modify= Modifier.toString(method.getModifiers());
            MethodDefine methDef=method.getAnnotation(MethodDefine.class);
            if(methDef==null || modify.indexOf("public")==-1 ){
                continue;
            }
            //构建方法
            ScriptMethod scriptMethod=new ScriptMethod();

            scriptMethod.setMethodName(method.getName());
            scriptMethod.setTitle(methDef.title());
            scriptMethod.setDescription(methDef.description());
            scriptMethod.setCls(cls);
            scriptMethod.setNodeType(nodeType);
            scriptMethod.setPreFix(preFix);
            //构建方法参数
            Class<?>[] parameterTypes = method.getParameterTypes();
            ParamDefine[] paramDefines=getMethodParameterNamesByAnnotation(method);
            if(parameterTypes!=null && paramDefines!=null
                    && parameterTypes.length==parameterTypes.length) {
                int i = 0;
                for (Class<?> t : parameterTypes) {
                    ParamDefine paramDefine = paramDefines[i++];
                    String varName = paramDefine.varName();
                    String desp = paramDefine.description();
                    ScriptMethodParam param = new ScriptMethodParam(varName, t.getName(), desp);
                    scriptMethod.getParams().add(param);
                }
            }
            //获得其输出参数
            scriptMethod.setReturnType(method.getReturnType().getName());
            //加方法至脚本
            scriptClass.getChildNodes().add(scriptMethod);
        }

        return scriptClass;
    }


    @Override
    public void afterPropertiesSet() throws Exception {

    }

    /**
     * 获取给 "方法参数" 进行注解的值
     *
     * @param method 要获取参数名的方法
     * @return 按参数顺序排列的参数名列表
     */
    public static ParamDefine[] getMethodParameterNamesByAnnotation(Method method) {
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        if (parameterAnnotations == null || parameterAnnotations.length == 0) {
            return null;
        }
        ParamDefine[] parameterNames = new ParamDefine[parameterAnnotations.length];
        int i = 0;
        for (Annotation[] parameterAnnotation : parameterAnnotations) {
            for (Annotation annotation : parameterAnnotation) {
                if (annotation instanceof ParamDefine) {
                    ParamDefine param = (ParamDefine) annotation;
                    parameterNames[i++] = param;
                }
            }
        }
        return parameterNames;
    }
}
