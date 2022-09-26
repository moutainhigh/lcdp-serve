package com.redxun.gencode.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.entity.BaseEntity;
import com.redxun.common.base.entity.JsonPageResult;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.base.entity.QueryData;
import com.redxun.common.base.search.QueryFilter;
import com.redxun.common.base.search.QueryFilterBuilder;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ExceptionUtil;
import com.redxun.gencode.codegenhander.ClassCodeDef;
import com.redxun.gencode.codegenhander.IConvertContext;
import com.redxun.gencode.codegenhander.IConvertHandler;
import com.redxun.log.annotation.AuditLog;
import com.redxun.log.model.Audit;
import com.redxun.log.model.LogContext;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;


@Slf4j
@RestController
public abstract class CodeGenBaseController<E extends BaseEntity<? extends Serializable>> extends BaseController<E> {

    //获取实体
    public abstract E getBaseEntity();


    /**
     * 处理数据。
     * @param json
     */
    protected   void handData(JSONObject json){}


    protected   JsonResult beforeSave(E entity){
        return JsonResult.Success();
    }

    protected   void afterSave(E entity){

    }

    /**
     * 获得所有查询数据列表，不传入条件时，则返回所有的记录
     * @return
     * @throws Exception
     */
    @MethodDefine(title = "根据条件查询业务数据记录", path = "/query", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "查询条件", varName = "queryData")})
    @ApiOperation(value="根据条件查询业务数据记录", notes="根据条件查询业务数据记录")
    @PostMapping(value="/query")
    @Override
    public JsonPageResult query(@RequestBody QueryData queryData) throws Exception{
        JsonPageResult jsonResult=JsonPageResult.getSuccess("返回数据成功!");
        try{
            ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = servletRequestAttributes.getRequest();
            String url=request.getRequestURI();

            long start=System.currentTimeMillis();
            QueryFilter filter= QueryFilterBuilder.createQueryFilter(queryData);
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



    @MethodDefine(title = "保存表单数据", path = "/saveData", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "表单JSON数据", varName = "json")})
    @ApiOperation(value="保存表单数据", notes="保存表单数据")
    @AuditLog(operation = "保存表单数据")
    @PostMapping("/saveData")
    public JsonResult saveData(@RequestBody JSONObject formData){
        String pk=null;
        try{
            JsonResult result=JsonResult.Success();
            Class cls = this.getClass();
            ClassCodeDef classCodeDef =(ClassCodeDef)cls.getAnnotation(ClassCodeDef.class);
            //json转实体
            String formAlias = classCodeDef.alias();
            String genMode = classCodeDef.createType();
            E entity =getBaseEntity();
            IConvertHandler handler = IConvertContext.getByType(genMode);
            handler.handJsonToEntity(formData,entity, formAlias, classCodeDef.classPackage(),genMode);
            //在提交之前保存
            beforeSave(entity);
            pk=(String)entity.getPkId();

            if(StringUtils.isEmpty(pk)){
                this.getBaseService().insert(entity);
                pk=(String)entity.getPkId();
                LogContext.put("PK", pk);
                LogContext.put("action", Audit.ACTION_ADD);
                LogContext.put("detail", "保存表单"+formAlias +"成功" );
            }else{
                this.getBaseService().update(entity);
                LogContext.put("PK", pk);
                LogContext.put("action", Audit.ACTION_UPD);
                LogContext.put("detail", "更新表单"+formAlias +"成功" );
            }

            result.setData(entity);
            result.setMessage("保存表单"+this.getComment()+"成功" );
            afterSave(entity);
            return result;
        }catch (Exception ex){
            JsonResult fail=JsonResult.Fail("保存表单数据失败!");
            String  detail = ExceptionUtil.getExceptionMessage(ex);
            fail.setData(detail);
            String str="";

            if(StringUtils.isEmpty(pk)){
                str = "添加" + this.getComment() + "失败!";
            } else {
                str = "更新" + this.getComment() + "失败!";
            }

            LogContext.put("operation", str);
            LogContext.put("detail", detail);
            fail = JsonResult.getFailResult(str);
            fail.setData(detail);

            return fail;
        }
    }

    /**
     * 根据主键查询记录详细信息
     * @param pkId
     * @return
     */
    @MethodDefine(title = "根据主键查询详细信息", path = "/getById", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "主键", varName = "pkId")})
    @ApiOperation(value="根据主键查询业务数据详细信息", notes="根据主键查询业务数据详细信息")
    @GetMapping("/getById")
    public JSONObject getById(@RequestParam(value="pkId") String pkId){
        JSONObject resultJson=null;
        E result=null;
        if(StringUtils.isNotEmpty(pkId)){
            result=getBaseService().get(pkId);
        }
        Class cls = this.getClass();
        ClassCodeDef classCodeDef =(ClassCodeDef)cls.getAnnotation(ClassCodeDef.class);
        //处理数据--entity转json
        String formAlias = classCodeDef.alias();
        String genMode = classCodeDef.createType();
        IConvertHandler handler = IConvertContext.getByType(genMode);
        resultJson=handler.handEntityToJsonData(result,formAlias,classCodeDef.classPackage(),genMode);
        return resultJson;
    }

}
