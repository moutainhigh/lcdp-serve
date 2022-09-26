package com.redxun.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.redxun.common.annotation.ContextQuerySupport;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.BaseEntity;
import com.redxun.common.base.entity.JsonPageResult;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.base.entity.QueryData;
import com.redxun.common.base.search.QueryFilter;
import com.redxun.common.base.search.QueryFilterBuilder;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ExceptionUtil;
import com.redxun.common.utils.FileUtil;
import com.redxun.log.annotation.AuditLog;
import com.redxun.log.model.Audit;
import com.redxun.log.model.LogContext;
import com.redxun.log.util.EntityUtil;
import com.redxun.web.util.WebUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 模块：jpaas
 * 包名：com.redxun.common.web.controller
 * 功能描述：提供控制器一些基础方法，如获得i18n国际化的方法等
 *
 * @author：csx
 * @date:2019/2/21
 */
@Api("BaseController")
@RestController
@ContextQuerySupport
public  abstract class BaseController<E extends BaseEntity<? extends Serializable>>  {
    protected Logger logger=LoggerFactory.getLogger(BaseController.class);

    @Autowired
    private  MessageSource messageSource;

    /**
     * 根据键返回国际化消息。
     * @param key  键
     * @return
     */
    public String getMessage(String key){
        return getMessage(key,null);
    }


    /**
     * 根据键返回国际化消息。
     * @param key  键
     * @param args  比如国际化的消息 为如下形式  A {0} B {1}.
     *              调用方法为 getMessage("abc","AA","BB");
     * @return
     */
    public String getMessage(String key,Object[] args){
        //MessageSource messageSource= SpringUtil.getBean(MessageSource.class);
        return messageSource.getMessage(key,args, LocaleContextHolder.getLocale());
    }


    /**
     *
     * @return
     */
    public abstract BaseService<E> getBaseService();

    /**
     * 导出接口。
     * @return
     */
    protected   IExport getExport(){
        return null;
    }

    /**
     * 获取注释。
     * @return
     */
    public abstract String  getComment();


    protected ContextQuerySupport getContextQuerySupport(){
        ContextQuerySupport annotation = this.getClass().getAnnotation(ContextQuerySupport.class);
        return annotation;
    }

    /**
     * 子类可以添加对这个过滤器添加条件。
     * @param filter
     */
    protected void handleFilter(QueryFilter filter){
        ContextQuerySupport annotation = this.getClass().getAnnotation(ContextQuerySupport.class);
        WebUtil.handFilter(filter,annotation);
    }

    /**
     * 获取上下文查询的 QueryWrapper对象。
     * @return
     */
    protected QueryWrapper getContextWrapper(){
        QueryWrapper wrapper=new QueryWrapper();
        handContextWrapper(wrapper);
        return wrapper;
    }

    /**
     * 处理上下文查询对象。
     * @param wrapper
     */
    protected void handContextWrapper(QueryWrapper wrapper){
        ContextQuerySupport annotation = this.getClass().getAnnotation(ContextQuerySupport.class);
        WebUtil.handContextWrapper(annotation,wrapper);
    }



    /**
     * 返回当前上下文的租户ID
     * @return
     */
    protected String getCurrentTenantId(){
        return WebUtil.getCurrentTenantId();
    }

    /**
     * 子类可以增加这个过滤器处理数据
     * @param page
     */
    protected void handlePage(IPage page){

    }

    /**
     * 查询列表时，可以复写这个方法对列表进行更改。
     * @param list
     */
    protected void handleList(List list){

    }

    /**
     * 子类可以增加这个过滤器处理数据
     * @param ent
     */
    protected void handleData(E ent){

    }

    protected JsonResult beforeSave(E ent) {
        return  JsonResult.Success();
    }

    protected JsonResult beforeRemove(List<String> list){
        return  JsonResult.Success();
    }

    @Resource
    protected HttpServletRequest request;

    @MethodDefine(title = "删除记录", path = "/del", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "实体主键ID", varName = "ids")})
    @ApiOperation(value="ID删除记录", notes="根据主键ID删除记录,parameters is {ids:'1,2'}")
    @AuditLog(operation = "删除记录")
    @PostMapping("del")
    public JsonResult del(@RequestParam(value = "ids") String ids){

        if(StringUtils.isEmpty(ids)){
            return new JsonResult(false,"");
        }
        String[] aryId=ids.split(",");
        List list= Arrays.asList(aryId);

        JsonResult rtn= beforeRemove(list);
        if(!rtn.isSuccess()){
            return rtn;
        }
        JsonResult result=JsonResult.getSuccessResult("删除"+getComment()+"成功!");
        // 写入日志
        LogContext.put(Audit.OPERATION,"删除"+getComment());

        String detail="";
        LogContext.put(Audit.ACTION,Audit.ACTION_DEL);

        for (Object id: list) {
            String idStr=id.toString();
            E ent=getBaseService().get(idStr);
            if(ent==null){
                continue;
            }
            String fieldInfo=EntityUtil.getInfo(ent,false);
            detail+=fieldInfo +"\r\n";
        }
        LogContext.put(Audit.DETAIL,detail);

        getBaseService().delete(list);
        return result;
    }

    @MethodDefine(title = "查询所有数据", path = "/getAll", method = HttpMethodConstants.GET, params = {})
    @ApiOperation(value="查询所有数据", notes = "查询当前实体表的所有数据。")
    @GetMapping("getAll")
    public JsonPageResult getAll() throws Exception {
        List data= getBaseService().getAll();
        JsonPageResult result=new JsonPageResult();
        result.setData(data);
        result.setShow(false);
        return result;
    }

    @MethodDefine(title = "保存数据", path = "/save", method = HttpMethodConstants.POST, params = {@ParamDefine(title = "实体数据JSON", varName = "entity")})
    @ApiOperation(value="保存数据", notes="根据提交的业务JSON数据保存业务数据记录")
    @AuditLog(operation = "保存数据")
    @PostMapping("/save")
    public JsonResult save(@Validated @ApiParam @RequestBody E entity, BindingResult validResult) throws Exception{
        JsonResult result=handValid(validResult);
        if(!result.isSuccess()){
            return result;
        }

        Serializable pkId= entity.getPkId();
        String str="";
        String operation="";
        try{
            result= beforeSave(entity);
            if(!result.isSuccess()){
                return result;
            }
            if(BeanUtil.isEmpty(pkId)){
                String fieldInfo="添加"+getComment()+":"+ EntityUtil.getInfo(entity,true);

                getBaseService().insert(entity);
                //处理日志相关。
                LogContext.put(Audit.PK,entity.getPkId());
                LogContext.put(Audit.ACTION,Audit.ACTION_ADD);
                LogContext.put(Audit.DETAIL, fieldInfo);

                str="添加" + getComment() + "成功";
                operation="添加" + getComment();

                LogContext.put(Audit.OPERATION,operation);
            }else{
                E oldEnt=getBaseService().get(pkId);

                //处理日志相关。
                String fieldInfo= EntityUtil.getUpdInfo(oldEnt,entity);
                LogContext.put(Audit.ACTION,Audit.ACTION_UPD);
                LogContext.put(Audit.PK,pkId);
                LogContext.put(Audit.DETAIL,"更新"+getComment()+":" +fieldInfo);

                getBaseService().update(entity);
                str="更新" + getComment() + "成功";

                operation="更新" + getComment();

                LogContext.put(Audit.OPERATION,operation);
            }
            result=JsonResult.getSuccessResult(str);
            result.setData(entity);
        } catch(Exception ex){
            String errMsg= ExceptionUtil.getExceptionMessage(ex);

            if(BeanUtil.isEmpty(pkId)){
                str="添加" + getComment() +"失败!";
            }else{
                str="更新" + getComment() +"失败!";
            }
            //处理日志相关。
            LogContext.put(Audit.OPERATION,operation);
            LogContext.put(Audit.DETAIL,errMsg);

            result=JsonResult.getFailResult(str);
            result.setData(errMsg);
        }
        return result;
    }

    /**
     * 检查数据是否合法
     * @param validResult
     * @return
     */
    private JsonResult handValid(BindingResult validResult){
        if(!validResult.hasErrors()){
            return  JsonResult.Success();
        }
        JsonResult result=JsonResult.Fail("表单验证失败");
        List<ObjectError> allErrors = validResult.getAllErrors();
        StringBuilder sb=new StringBuilder();
        for(ObjectError error:allErrors){
            if(error instanceof FieldError){
                sb.append("["+((FieldError)error).getField()+"]");
            }
            sb.append( error.getDefaultMessage());
            sb.append("\r\n");
        }
        result.setData( sb.toString());

        return  result;

    }

    /**
     * 获得所有查询数据列表，不传入条件时，则返回所有的记录
     * @return
     * @throws Exception
     */
    @MethodDefine(title = "查询数据", path = "/query", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "查询条件", varName = "queryData")})
    @ApiOperation(value="查询数据", notes="根据条件查询业务数据记录")
    @PostMapping(value="/query")
    public JsonPageResult query(@RequestBody QueryData queryData) throws Exception{
        JsonPageResult jsonResult=JsonPageResult.getSuccess("返回数据成功!");
        try{
            ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = servletRequestAttributes.getRequest();
            String url=request.getRequestURI();

            long start=System.currentTimeMillis();
            QueryFilter filter=QueryFilterBuilder.createQueryFilter(queryData);
            handleFilter(filter);
            IPage page= getBaseService().query(filter);
            handlePage(page);
            jsonResult.setPageData(page);

            logger.info("url:" +url +",escape time:" + (System.currentTimeMillis()-start) +"ms");



        }catch (Exception ex){
            jsonResult.setSuccess(Boolean.FALSE);
            logger.error(ExceptionUtil.getExceptionMessage(ex));
            jsonResult.setMessage(ExceptionUtil.getExceptionMessage(ex));
        }
        return jsonResult;
    }


    @MethodDefine(title = "查询数据（分页）", path = "/queryList", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "查询条件", varName = "queryData")})
    @ApiOperation(value="查询数据（分页）", notes="根据条件查询业务数据记录")
    @PostMapping(value="/queryList")
    public JsonResult queryList(@RequestBody QueryData queryData) throws Exception{
        JsonPageResult jsonResult=JsonPageResult.getSuccess("返回数据成功!");
        try{
            ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = servletRequestAttributes.getRequest();
            String url=request.getRequestURI();

            long start=System.currentTimeMillis();
            QueryFilter filter=QueryFilterBuilder.createQueryFilter(queryData);
            handleFilter(filter);
            List list= getBaseService().queryList(filter);
            handleList(list);
            jsonResult.setData(list);

            logger.info("url:" +url +",escape time:" + (System.currentTimeMillis()-start) +"ms");

        }catch (Exception ex){
            jsonResult.setSuccess(Boolean.FALSE);
            logger.error(ExceptionUtil.getExceptionMessage(ex));
            jsonResult.setMessage(ExceptionUtil.getExceptionMessage(ex));
        }
        return jsonResult;
    }

    /**
     * 根据主键查询记录详细信息
     * @param pkId
     * @return
     */
    @MethodDefine(title = "获取记录数据", path = "/get", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "主键", varName = "pkId")})
    @ApiOperation(value="获取记录数据", notes="根据主键查询业务数据详细信息")
    @GetMapping("/get")
    public JsonResult<E> get(@RequestParam (value="pkId") String pkId){
        JsonResult result=JsonResult.Success();
        result.setShow(false);
        if(ObjectUtils.isEmpty(pkId)){
            return result.setData(new Object());
        }
        E ent=getBaseService().get(pkId);
        handleData(ent);
        return result.setData(ent);
    }

    @MethodDefine(title = "获取多条记录数据", path = "/getByIds", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "主键", varName = "ids")})
    @ApiOperation(value="获取多条记录数据", notes="根据主键IDS查询业务数据详细信息")
    @GetMapping("/getByIds")
    public JsonResult<E> getByIds(@RequestParam (value="ids") String ids){
        JsonResult result=JsonResult.Success();
        result.setShow(false);
        if(ObjectUtils.isEmpty(ids)){
            return result.setData(new Object());
        }
        String[] aryId=ids.split(",");
        List<String> idList = Arrays.asList(aryId);
        List<E> byIds = getBaseService().getByIds(idList);
        return result.setData(byIds);
    }



    @MethodDefine(title = "导出列表数据", path = "/listExport", method = HttpMethodConstants.POST,params = {@ParamDefine(title = "查询参数", varName = "param")})
    @ApiOperation("查询结果导出")
    @AuditLog(operation = "查找结果导出")
    @PostMapping("/listExport")
    public void listExport(@RequestBody QueryData param){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response=attributes.getResponse();
        try {
            String[] all=null;
            if(param.getParams().containsKey("isQuery")){
                QueryFilter filter = QueryFilterBuilder.createQueryFilter(param);
                List<? extends BaseEntity> lists= getBaseService().queryList(filter);
                if(lists==null){
                    throw new Exception("导出失败，没有查询结果。");
                }
                List<String> ids=new ArrayList<>();
                for (BaseEntity list : lists) {
                    ids.add((String) list.getPkId());
                }
                all = ids.toArray(new String[ids.size()]);
            }
            else{
                String ids = param.getParams().get("ids");
                if(StringUtils.isEmpty(ids)){
                    new Exception("导出失败，请选择要导出的记录。");
                    return;
                }
                all = ids.split(",");
            }
            IExport export=getExport();

            StringBuilder sb=new StringBuilder();
            sb.append("导出 "+getComment()+ " :");


            Map<String,String> map=new HashMap<>();
            for(String id : all) {

                if(export==null){
                    logger.debug("请实现IExport接口!");
                    continue;
                }
                JSONObject json=getExport().doExportById(id,sb);
                String fileName =id+".json";
                String defStr = JSONObject.toJSONString(json);
                map.put(fileName,defStr);
            }

            LogContext.put(Audit.DETAIL,sb.toString());

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            String downFileName = getComment() +"_" + sdf.format(new Date());
            FileUtil.downloadZip(response,downFileName,map);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
