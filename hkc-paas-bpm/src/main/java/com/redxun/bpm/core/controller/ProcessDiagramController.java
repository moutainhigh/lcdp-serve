package com.redxun.bpm.core.controller;


import com.redxun.bpm.activiti.ActInstService;
import com.redxun.bpm.activiti.ActRepService;
import com.redxun.bpm.activiti.img.ActivitiDiagramGeneratorExt;
import com.redxun.bpm.core.entity.BpmCheckHistory;
import com.redxun.bpm.core.entity.BpmDef;
import com.redxun.bpm.core.entity.BpmInst;
import com.redxun.bpm.core.service.BpmDefService;
import com.redxun.bpm.core.service.BpmInstServiceImpl;
import com.redxun.bpm.core.service.BpmRuPathServiceImpl;
import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.utils.FileUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.image.ProcessDiagramGenerator;
import org.dom4j.DocumentException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/bpm/core/bpmDef")
@ClassDefine(title = "流程图接口",alias = "processDiagramController",path = "/bpm/core/bpmDef",packages = "core",packageName = "流程管理")
@Api(tags = "流程图接口")
public class ProcessDiagramController {

    private static final String activityFontName="黑体";
    private static final String labelFontName="黑体";

    @Resource
    BpmDefService bpmDefService;
    @Resource
    BpmInstServiceImpl bpmInstService;
    @Resource
    RepositoryService repositoryService;
    @Resource
    ProcessDiagramGenerator diagramGenerator;


    @Resource
    ActInstService actInstService;



    @Resource
    ActivitiDiagramGeneratorExt activitiDiagramGeneratorExt;

    /**
     * 根据流程定义ID获取流程图。
     * @param defId
     * @throws IOException
     */
    @MethodDefine(title = "根据流程定义ID获取流程图", path = "/getImageByDefId/*", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "流程定义ID", varName = "defId")})
    @ApiOperation("根据流程定义ID获取流程图")
    @GetMapping("getImageByDefId/{defId}")
    public void getImageByDefId(@PathVariable(value = "defId") String defId) throws IOException {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response=attributes.getResponse();
        response.setContentType("image/svg+xml");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        BpmDef bpmDef=bpmDefService.getById(defId);
        String actAefId=bpmDef.getActDefId();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(actAefId);

        List<String> list=new ArrayList<>();
        List<String> flowlist=new ArrayList<>();
        InputStream is=diagramGenerator.generateDiagram( bpmnModel,
                list,
                flowlist);

        FileUtil.writeInput(is,response.getOutputStream());
    }

    @MethodDefine(title = "根据流程实例ID获取流程图", path = "/getImageByInstId/*", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "流程实例ID", varName = "instId")})
    @ApiOperation("根据流程实例ID获取流程图")
    @GetMapping("getImageByInstId/{instId}")
    public void getImageByInstId(@PathVariable(value = "instId") String instId) throws IOException, DocumentException {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response=attributes.getResponse();
        response.setContentType("image/svg+xml");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        BpmInst bpmInst=bpmInstService.getById(instId);
        String actDefId=bpmInst.getActDefId();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(actDefId);

        List<String> flowsSkipIdlist=new ArrayList<>();
        List<String> flowsAgreeIdlist=new ArrayList<>();
        List<String> flowsRefuseIdlist=new ArrayList<>();
        List<String> flowsBackIdlist=new ArrayList<String>();
        List<String> flowsHandleIdlist=new ArrayList<String>();

        actInstService.getFlowsByInstId(instId, flowsSkipIdlist,flowsAgreeIdlist,flowsRefuseIdlist,flowsBackIdlist,flowsHandleIdlist);


        InputStream is=activitiDiagramGeneratorExt.generateDiagram(bpmnModel,
                flowsSkipIdlist,flowsAgreeIdlist,flowsRefuseIdlist,flowsBackIdlist,flowsHandleIdlist);

        FileUtil.writeInput(is,response.getOutputStream());
    }


}
