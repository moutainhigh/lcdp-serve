package com.redxun.bpm.activiti.img;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;

import java.io.InputStream;
import java.util.List;

/**
 * 改写流程图的字体
 */
public class ActivitiDiagramGenerator extends DefaultProcessDiagramGenerator {

    private final static String FONT_HEITI="黑体";

    @Override
    public InputStream generateDiagram(BpmnModel bpmnModel, List<String> highLightedActivities, List<String> highLightedFlows) {
        return super.generateDiagram(bpmnModel, highLightedActivities, highLightedFlows,FONT_HEITI,FONT_HEITI,FONT_HEITI);
    }
}
