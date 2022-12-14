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
 * ?????????jpaas
 * ?????????com.redxun.common.web.controller
 * ????????????????????????????????????????????????????????????i18n?????????????????????
 *
 * @author???csx
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
     * ?????????????????????????????????
     * @param key  ???
     * @return
     */
    public String getMessage(String key){
        return getMessage(key,null);
    }


    /**
     * ?????????????????????????????????
     * @param key  ???
     * @param args  ???????????????????????? ???????????????  A {0} B {1}.
     *              ??????????????? getMessage("abc","AA","BB");
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
     * ???????????????
     * @return
     */
    protected   IExport getExport(){
        return null;
    }

    /**
     * ???????????????
     * @return
     */
    public abstract String  getComment();


    protected ContextQuerySupport getContextQuerySupport(){
        ContextQuerySupport annotation = this.getClass().getAnnotation(ContextQuerySupport.class);
        return annotation;
    }

    /**
     * ???????????????????????????????????????????????????
     * @param filter
     */
    protected void handleFilter(QueryFilter filter){
        ContextQuerySupport annotation = this.getClass().getAnnotation(ContextQuerySupport.class);
        WebUtil.handFilter(filter,annotation);
    }

    /**
     * ???????????????????????? QueryWrapper?????????
     * @return
     */
    protected QueryWrapper getContextWrapper(){
        QueryWrapper wrapper=new QueryWrapper();
        handContextWrapper(wrapper);
        return wrapper;
    }

    /**
     * ??????????????????????????????
     * @param wrapper
     */
    protected void handContextWrapper(QueryWrapper wrapper){
        ContextQuerySupport annotation = this.getClass().getAnnotation(ContextQuerySupport.class);
        WebUtil.handContextWrapper(annotation,wrapper);
    }



    /**
     * ??????????????????????????????ID
     * @return
     */
    protected String getCurrentTenantId(){
        return WebUtil.getCurrentTenantId();
    }

    /**
     * ?????????????????????????????????????????????
     * @param page
     */
    protected void handlePage(IPage page){

    }

    /**
     * ??????????????????????????????????????????????????????????????????
     * @param list
     */
    protected void handleList(List list){

    }

    /**
     * ?????????????????????????????????????????????
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

    @MethodDefine(title = "????????????", path = "/del", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "????????????ID", varName = "ids")})
    @ApiOperation(value="ID????????????", notes="????????????ID????????????,parameters is {ids:'1,2'}")
    @AuditLog(operation = "????????????")
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
        JsonResult result=JsonResult.getSuccessResult("??????"+getComment()+"??????!");
        // ????????????
        LogContext.put(Audit.OPERATION,"??????"+getComment());

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

    @MethodDefine(title = "??????????????????", path = "/getAll", method = HttpMethodConstants.GET, params = {})
    @ApiOperation(value="??????????????????", notes = "???????????????????????????????????????")
    @GetMapping("getAll")
    public JsonPageResult getAll() throws Exception {
        List data= getBaseService().getAll();
        JsonPageResult result=new JsonPageResult();
        result.setData(data);
        result.setShow(false);
        return result;
    }

    @MethodDefine(title = "????????????", path = "/save", method = HttpMethodConstants.POST, params = {@ParamDefine(title = "????????????JSON", varName = "entity")})
    @ApiOperation(value="????????????", notes="?????????????????????JSON??????????????????????????????")
    @AuditLog(operation = "????????????")
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
                String fieldInfo="??????"+getComment()+":"+ EntityUtil.getInfo(entity,true);

                getBaseService().insert(entity);
                //?????????????????????
                LogContext.put(Audit.PK,entity.getPkId());
                LogContext.put(Audit.ACTION,Audit.ACTION_ADD);
                LogContext.put(Audit.DETAIL, fieldInfo);

                str="??????" + getComment() + "??????";
                operation="??????" + getComment();

                LogContext.put(Audit.OPERATION,operation);
            }else{
                E oldEnt=getBaseService().get(pkId);

                //?????????????????????
                String fieldInfo= EntityUtil.getUpdInfo(oldEnt,entity);
                LogContext.put(Audit.ACTION,Audit.ACTION_UPD);
                LogContext.put(Audit.PK,pkId);
                LogContext.put(Audit.DETAIL,"??????"+getComment()+":" +fieldInfo);

                getBaseService().update(entity);
                str="??????" + getComment() + "??????";

                operation="??????" + getComment();

                LogContext.put(Audit.OPERATION,operation);
            }
            result=JsonResult.getSuccessResult(str);
            result.setData(entity);
        } catch(Exception ex){
            String errMsg= ExceptionUtil.getExceptionMessage(ex);

            if(BeanUtil.isEmpty(pkId)){
                str="??????" + getComment() +"??????!";
            }else{
                str="??????" + getComment() +"??????!";
            }
            //?????????????????????
            LogContext.put(Audit.OPERATION,operation);
            LogContext.put(Audit.DETAIL,errMsg);

            result=JsonResult.getFailResult(str);
            result.setData(errMsg);
        }
        return result;
    }

    /**
     * ????????????????????????
     * @param validResult
     * @return
     */
    private JsonResult handValid(BindingResult validResult){
        if(!validResult.hasErrors()){
            return  JsonResult.Success();
        }
        JsonResult result=JsonResult.Fail("??????????????????");
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
     * ??????????????????????????????????????????????????????????????????????????????
     * @return
     * @throws Exception
     */
    @MethodDefine(title = "????????????", path = "/query", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "????????????", varName = "queryData")})
    @ApiOperation(value="????????????", notes="????????????????????????????????????")
    @PostMapping(value="/query")
    public JsonPageResult query(@RequestBody QueryData queryData) throws Exception{
        JsonPageResult jsonResult=JsonPageResult.getSuccess("??????????????????!");
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


    @MethodDefine(title = "????????????????????????", path = "/queryList", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "????????????", varName = "queryData")})
    @ApiOperation(value="????????????????????????", notes="????????????????????????????????????")
    @PostMapping(value="/queryList")
    public JsonResult queryList(@RequestBody QueryData queryData) throws Exception{
        JsonPageResult jsonResult=JsonPageResult.getSuccess("??????????????????!");
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
     * ????????????????????????????????????
     * @param pkId
     * @return
     */
    @MethodDefine(title = "??????????????????", path = "/get", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "??????", varName = "pkId")})
    @ApiOperation(value="??????????????????", notes="??????????????????????????????????????????")
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

    @MethodDefine(title = "????????????????????????", path = "/getByIds", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "??????", varName = "ids")})
    @ApiOperation(value="????????????????????????", notes="????????????IDS??????????????????????????????")
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



    @MethodDefine(title = "??????????????????", path = "/listExport", method = HttpMethodConstants.POST,params = {@ParamDefine(title = "????????????", varName = "param")})
    @ApiOperation("??????????????????")
    @AuditLog(operation = "??????????????????")
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
                    throw new Exception("????????????????????????????????????");
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
                    new Exception("?????????????????????????????????????????????");
                    return;
                }
                all = ids.split(",");
            }
            IExport export=getExport();

            StringBuilder sb=new StringBuilder();
            sb.append("?????? "+getComment()+ " :");


            Map<String,String> map=new HashMap<>();
            for(String id : all) {

                if(export==null){
                    logger.debug("?????????IExport??????!");
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
