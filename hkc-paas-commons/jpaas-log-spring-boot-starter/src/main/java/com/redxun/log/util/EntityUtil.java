package com.redxun.log.util;

import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ExceptionUtil;
import com.redxun.common.utils.SpringUtil;
import com.redxun.log.annotation.FieldDef;
import com.redxun.log.model.Audit;
import com.redxun.log.service.INameService;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 获取实例有FieldDef注解的操作类。
 */
@Slf4j
public class EntityUtil {

    public final String ERROR="error";

    /**
     * 获取数据变更信息。
     * @param obj       对象
     * @param isAdd     是否添加 true：添加 false 删除
     * @return
     * @throws Exception
     */
    public static String getInfo(Object obj,boolean isAdd)  {
        Field[] fields = obj.getClass().getDeclaredFields();

        StringBuilder sb=new StringBuilder();
        boolean tag=false;
        for (Field field:fields ) {
            int i = field.getModifiers();
            if (i != 2) {
                continue;
            }
            String methodName = "get" + StringUtils.makeFirstLetterUpperCase(field.getName());
            FieldDef annotation = field.getDeclaredAnnotation(FieldDef.class);
            if(annotation==null){
                continue;
            }

            String value = getValue(obj, methodName, annotation);
            if(isAdd){
                if(!annotation.add()){
                    continue;
                }
            }
            else {
                if(!annotation.del()){
                    continue;
                }
            }
            String comment=annotation.comment();
            if(tag){
                sb.append(",");
            }
            else{
                tag=true;
            }
            sb.append(comment +": " + value + " "+ annotation.suffix());

        }
        return sb.toString();
    }

    /**
     * 获取两个对象变更的数据。
     * <pre>
     *     如果数据没有变更则不记录。
     * </pre>
     * @param origin
     * @param current
     * @return
     * @throws Exception
     */
    public static String getUpdInfo(Object origin,Object current) {
        if(origin.getClass()!=current.getClass()){
            return  "对象类型不同!";
        }
        Field[] fields = current.getClass().getDeclaredFields();

        StringBuilder sb=new StringBuilder();
        boolean tag=false;
        for (Field field:fields ) {
            int i = field.getModifiers();
            if (i != 2) {
                continue;
            }

            String methodName = "get" + StringUtils.makeFirstLetterUpperCase(field.getName());
            FieldDef annotation = field.getDeclaredAnnotation(FieldDef.class);
            if (annotation != null) {
                if(!annotation.upd()){
                    continue;
                }
                String curVal=getValue(current,methodName,annotation);
                String oldVal=getValue(origin,methodName,annotation);
                if(curVal.equals(oldVal)){
                    continue;
                }
                String comment=annotation.comment();
                if(tag){
                    sb.append(",");
                }
                else{
                    tag=true;
                }
                sb.append(comment +":" + oldVal +" "+annotation.suffix() +" 改为 " + curVal + " "+ annotation.suffix());
            }
        }
        return sb.toString();
    }

    /**
     * 获取字段的值。
     * @param obj
     * @param methodName
     * @param annotation
     * @return
     */
    private static String getValue(Object obj, String methodName, FieldDef annotation) {
        try {
            Method method = obj.getClass().getMethod(methodName);
            Object val = method.invoke(obj);
            String value="";
            if(val!=null){
                value=val.toString();
            }
            value=getName(annotation.nameClass(),value);

            return  value;
        }
        catch (Exception ex){
            log.error(ExceptionUtil.getExceptionMessage(ex));
            return  "error";
        }
    }

    /**
     * 根据nameClass获取名字。
     * @param cls
     * @param value
     * @return
     */
    private static String getName(Class cls,String value){
        if(StringUtils.isEmpty(value)){
            return "";
        }
        if(cls== INameService.class){
            return value;
        }
        INameService nameService=(INameService) SpringUtil.getBean(cls);
        if(nameService!=null){
            value= nameService.getName(value);
        }
        return value;
    }

    public static void main(String[] args) throws Exception {

        Audit old=new Audit();
        old.setMethodName("query");
        old.setOperation("2");

        Audit audit=new Audit();
        audit.setMethodName("getAll");
        audit.setOperation("4");
        //String addInfo=getAddInfo(audit);
        String updInfo= EntityUtil.getUpdInfo(old,audit);
        System.err.println(updInfo);

    }
}
