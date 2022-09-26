
package com.redxun.form.core.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.config.ConfigService;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.GridHeader;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.constant.MBoolean;
import com.redxun.common.engine.FtlEngine;
import com.redxun.common.engine.FtlUtil;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.DateUtils;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.tool.constant.CommonConstant;
import com.redxun.common.utils.ExceptionUtil;
import com.redxun.common.utils.FileUtil;
import com.redxun.common.utils.ZipUtil;
import com.redxun.datasource.DataSourceContextHolder;
import com.redxun.datasource.DataSourceUtil;
import com.redxun.db.DbUtil;
import com.redxun.dto.sys.SysInterfaceApiDto;
import com.redxun.form.bo.entity.FormBoAttr;
import com.redxun.form.bo.entity.FormBoDef;
import com.redxun.form.bo.entity.FormBoEntity;
import com.redxun.form.bo.service.FormBoDefServiceImpl;
import com.redxun.form.bo.service.FormBoEntityServiceImpl;
import com.redxun.form.core.entity.*;
import com.redxun.form.core.service.*;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.tool.StringUtils;
import com.redxun.log.annotation.AuditLog;
import com.redxun.gencode.codegenhander.IFieldContext;
import com.redxun.gencode.codegenhander.IFieldHndler;
import com.redxun.web.controller.BaseController;
import com.redxun.gencode.util.ReaderFileUtil;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateHashModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.redxun.common.annotation.ClassDefine;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@RestController
@RequestMapping("/form/core/codeGenSetting")
@Api(tags = "代码生成配置")
@ClassDefine(title = "代码生成配置",alias = "CodeGenSettingController",path = "/form/core/codeGenSetting",packages = "core",packageName = "子系统名称")

public class CodeGenSettingController extends BaseController<CodeGenSetting> {

    @Autowired
    CodeGenSettingServiceImpl codeGenSettingService;
    @Autowired
    FormTemplateServiceImpl formTemplateService;
    @Autowired
    FormBoDefServiceImpl formBoDefService;
    @Autowired
    FormBoEntityServiceImpl formBoEntityService;
    @Override
    public BaseService getBaseService() {
return codeGenSettingService;
}

    @Override
    public String getComment() {
return "代码生成配置";
}


    /**
     * 生成代码
     * @return
     * @throws IOException
     */
    @ApiOperation("代码生成")
    @MethodDefine(title = "代码生成", path = "/genCode", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "表单ID", varName = "formId")})
    @AuditLog(operation = "代码生成")
    @PostMapping("genCode")
    public void genCode(@RequestBody CodeGenSetting ent) throws IOException, TemplateException {
        codeGenSettingService.genCode(ent);
    }

    @MethodDefine(title = "重新加载表头（模型ID）", path = "/reloadColumns", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "模型ID", varName = "id")})
    @ApiOperation(value = "重新加载表头")
    @GetMapping("/reloadColumns")
    public JsonResult reloadColumns(@RequestParam(value = "boDefId") String boDefId) {
        JsonResult result = new JsonResult(true).setShow(false);

        FormBoEntity boEntity= formBoEntityService.getByDefId(boDefId,true);
        boEntity=formBoEntityService.getFormBoAttrByEnt(boEntity);
        FormBoDef boDef= formBoDefService.getById(boDefId);

        JSONObject resultJson=new JSONObject();

        JSONObject entityJson=new JSONObject();
        entityJson.put("boEnt", boEntity);
        entityJson.put("hasGen", boDef.getSupportDb());

        resultJson.put("entityJson",entityJson);

        String dsAlias = boEntity.getDsAlias();
        if(!DataSourceUtil.LOCAL.equals(dsAlias)){
            DataSourceContextHolder.setDataSource(dsAlias);
        }

        try {
            //JSONArray headerCols = JSONArray.parseArray(formBoList.getColsJson());
            JSONArray headerCols = null;
            String sql = "select * from "+boEntity.getTableName();
            List<GridHeader> headers = DbUtil.getGridHeader(sql);
            JSONArray fieldCols = new JSONArray();
            if (headerCols == null) {
                headerCols = new JSONArray();
            }
            Map<String, JSONObject> jsonFieldMap = null;
            for (GridHeader gh : headers) {
                JSONObject fieldObj = new JSONObject();
                fieldObj.put("fieldLabel", gh.getFieldLabel());
                fieldObj.put("field", gh.getFieldName());
                fieldObj.put("header", gh.getFieldLabel());

                //为创建时间设置默认的格式
                JSONObject dataType = JSONObject.parseObject("{'string':'字符','number':'整型','date':'日期型'}");
                if (gh.getFieldName().equals(FormBoEntity.FIELD_CREATE_TIME) || gh.getFieldName().equals(FormBoEntity.FIELD_UPDATE_TIME)) {
                    fieldObj.put("dataType", "date");
                    fieldObj.put("format", CommonConstant.DATE_FORMAT);
                    fieldObj.put("dataType_name", dataType.getString("date"));
                } else {
                    fieldObj.put("dataType", "number".equals(gh.getDataType()) ? "int" : gh.getDataType());
                    fieldObj.put("dataType_name", dataType.getString(gh.getDataType()));
                }
                fieldObj.put("queryDataType", gh.getQueryDataType());

                setDefaultFieldObj(fieldObj);
                fieldCols.add(fieldObj);
                //是否找到
                boolean isFound = false;
                for (int i = 0; i < headerCols.size(); i++) {
                    String field = headerCols.getJSONObject(i).getString("field");
                    if (field == null) {
                        continue;
                    }
                    if (field.equals(gh.getFieldName())) {
                        isFound = true;
                        break;
                    }
                }
                if (!isFound) {
                    JSONObject cloneObj = (JSONObject) fieldObj.clone();
                    cloneObj.remove("isReturn");
                    cloneObj.remove("visible");
                    headerCols.add(cloneObj);
                }
            }

            resultJson.put("headerCols",headerCols);
            result.setData(resultJson);
            result.setMessage("成功加载数据！");
        } catch (Exception e) {
            e.printStackTrace();
            result.setSuccess(false);
            result.setMessage("加载出错");
        } finally {
            if(!DataSourceUtil.LOCAL.equals(dsAlias)){
                DataSourceContextHolder.setDefaultDataSource();
            }
        }
        return result;
    }

    private void setDefaultFieldObj(JSONObject fieldObj) {
        fieldObj.put("width", 100);
        JSONObject json=new JSONObject();
        json.put("value",fieldObj.getString("field"));
        json.put("label",fieldObj.getString("header"));
        json.put("key",fieldObj.getString("field"));
        fieldObj.put("fieldExt", json);
        fieldObj.put("allowSort", false);
        if (!fieldObj.containsKey("format")) {
            fieldObj.put("format", "");
        }
        json=new JSONObject();
        json.put("value",fieldObj.getString("dataType"));
        json.put("label",fieldObj.getString("dataType_name"));
        fieldObj.put("dataTypeExt", json);
        fieldObj.put("headerAlignExt", "");
        fieldObj.put("fixedExt", "");
        fieldObj.put("url", "");
        fieldObj.put("linkType", "");
        fieldObj.put("renderTypeExt", "");
        fieldObj.put("controlExt", "");
    }

}