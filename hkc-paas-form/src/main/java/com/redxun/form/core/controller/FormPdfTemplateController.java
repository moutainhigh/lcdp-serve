
package com.redxun.form.core.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.redxun.common.annotation.ContextQuerySupport;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.search.QueryFilter;
import com.redxun.common.base.search.QueryParam;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.engine.FtlUtil;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.tool.constant.CommonConstant;
import com.redxun.common.utils.ContextUtil;
import com.redxun.dto.user.OsInstDto;
import com.redxun.feign.BpmInstClient;
import com.redxun.feign.OsInstClient;
import com.redxun.fieldrender.RenderUtil;
import com.redxun.form.core.entity.FormPdfTemplate;
import com.redxun.form.core.service.FormPdfRender;
import com.redxun.form.core.service.FormPdfTemplateServiceImpl;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.form.util.QrCodeUtil;
import com.redxun.util.SysUtil;
import com.redxun.web.controller.BaseController;
import freemarker.template.TemplateHashModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/form/core/formPdfTemplate")
@Api(tags = "单据PDF模板")
public class FormPdfTemplateController extends BaseController<FormPdfTemplate> {

    @Autowired
    FormPdfTemplateServiceImpl formPdfTemplateService;

    @Autowired
    BpmInstClient bpmInstClient;

    @Resource
    OsInstClient osInstClient;


    @Override
    public BaseService getBaseService() {
    return formPdfTemplateService;
    }

    @Override
    public String getComment() {
    return "单据PDF模板";
    }

    @Override
    protected void handleFilter(QueryFilter filter) {

        filter.addParam(CommonConstant.TENANT_PREFIX,"a.");
        filter.addParam(CommonConstant.COMPANY_PREFIX,"a.");
        filter.addParam(CommonConstant.DELETED_PREFIX,"a.");
        super.handleFilter(filter);

        QueryParam appIdParam=filter.getQueryParams().get("APP_ID_");
        if(appIdParam!=null) {
            appIdParam.setFieldName("a.APP_ID_");
        }
    }

    @MethodDefine(title = "获取PDF模板", path = "/getPdfTemplate", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "传递参数", varName = "jsonObject")})
    @ApiOperation(value = "获取PDF模板")
    @PostMapping({"/getPdfTemplate"})
    public JsonResult getPdfTemplate(@RequestBody JSONObject jsonObject) throws Exception{
        String pkId = jsonObject.getString( "pkId");
        String data = jsonObject.getString( "data");
        String metadata = jsonObject.getString( "metadata");
        FormPdfTemplate template =formPdfTemplateService.get(pkId);
        String html =  formPdfTemplateService.constructPDFTemp(template.getPdfHtml());
        Map<String, Object> params = new HashMap<>();
        JSONObject json=JSONObject.parseObject(data);
        params.put("data", json);
        JSONObject labelObj=new JSONObject();
        if(StringUtils.isNotEmpty(metadata)){
            String tenantId = ContextUtil.getCurrentTenantId();
            OsInstDto osInstDto = osInstClient.getById(tenantId);
            String label = osInstDto.getLabel();
            JSONArray jsonArray = JSONArray.parseArray(metadata);
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject attr = jsonArray.getJSONObject(i);
                String name = attr.getString("name");
                if(StringUtils.isEmpty(name)){
                    continue;
                }
                String comment = attr.getString("comment");
                String instConfig = attr.getString("instConfig");
                labelObj.put(name,comment);
                //有配置个性化 且 配了租户配置
                if(StringUtils.isNotEmpty(instConfig) && StringUtils.isNotEmpty(label)){
                    JSONArray configs = JSONArray.parseArray(instConfig);
                    for (int j = 0; j < configs.size(); j++) {
                        JSONObject config= configs.getJSONObject(j);
                        if(label.equals(config.getString("instLabel"))){
                            labelObj.put(name,config.getString("label"));
                            break;
                        }
                    }
                }
            }
        }
        params.put("label", labelObj);
        //获取审批历史
        String instId = json.getString("INST_ID_");
        if(StringUtils.isNotEmpty(instId)){
            List history = bpmInstClient.getCheckHistorys(instId);
            for (int i = 0; i < history.size(); i++) {
                Map<String,Object> item = (Map<String, Object>) history.get(i);
                Integer duration = (Integer) item.get("duration");
                item.put("duration",duration/1000);
            }
            params.put("history", history);
        }
        //TemplateHashModel util = FtlUtil.generateStaticModel(FormPdfRender.class);
        // params.put("util", util);
        TemplateHashModel util = FtlUtil.generateStaticModel(RenderUtil.class);
        params.put("util", util);

        String outHtml= SysUtil.parseScript(html, params);
        JsonResult result = new JsonResult(true);
        result.setData(outHtml);
        return result;
    }


    @MethodDefine(title = "获取二维码", path = "/getQrCode", method = HttpMethodConstants.GET)
    @ApiOperation(value = "获取二维码")
    @GetMapping({"/getQrCode"})
    public void getQrCode(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String content = request.getParameter("content");
        int width = 100;
        int height = 100;
        if(StringUtils.isEmpty(content)){
            return;
        }
        if(StringUtils.isNotEmpty(request.getParameter("width"))){
            width=Integer.parseInt(request.getParameter("width"));
        }
        if(StringUtils.isNotEmpty(request.getParameter("height"))){
            height=Integer.parseInt(request.getParameter("height"));
        }
        byte[] qrCode = QrCodeUtil.createQRCode(width, height, content);
        response.setContentType("application/vnd.ms-word;charset=utf-8");
        response.setHeader("Content-Disposition",
                "attachment;filename=\"" + new String("文件名包含文件后缀".getBytes("gb2312"), "ISO8859-1"));
        OutputStream out = response.getOutputStream();
        out.write(qrCode);
        out.flush();
    }





}

