package com.redxun.bpm.activiti.img;

import org.activiti.bpmn.model.*;
import org.activiti.bpmn.model.Process;
import org.activiti.image.impl.DefaultProcessDiagramCanvas;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

/**
 * 流程图产生器扩展
 */
@Service
@Primary
public class ActivitiDiagramGeneratorExt extends DefaultProcessDiagramGenerator {

    private final static String FONT_HEITI="黑体";

    public InputStream generateDiagram(BpmnModel bpmnModel, List<String> flowsSkipIdlist, List<String> flowsAgreeIdlist
            ,List<String> flowsRefuseIdlist, List<String> flowsBackIdlist, List<String> flowsHandleIdlist) {
        return this.generateProcessDiagram(bpmnModel, flowsSkipIdlist, flowsAgreeIdlist,
                flowsRefuseIdlist,flowsBackIdlist,flowsHandleIdlist,FONT_HEITI,FONT_HEITI,FONT_HEITI).generateImage();
    }


    protected DefaultProcessDiagramCanvas generateProcessDiagram(BpmnModel bpmnModel,
                                                                 List<String> flowsSkipIdlist,
                                                                 List<String> flowsAgreeIdlist,
                                                                 List<String> flowsRefuseIdlist,
                                                                 List<String> flowsBackIdlist,
                                                                 List<String> flowsHandleIdlist,
                                                                 String activityFontName,
                                                                 String labelFontName,
                                                                 String annotationFontName) {

        prepareBpmnModel(bpmnModel);

        ActivitiDiagramCanvasExt processDiagramCanvas = initProcessDiagramCanvas(bpmnModel,
                activityFontName,
                labelFontName,
                annotationFontName);

        // Draw pool shape, if process is participant in collaboration
        for (Pool pool : bpmnModel.getPools()) {
            GraphicInfo graphicInfo = bpmnModel.getGraphicInfo(pool.getId());
            processDiagramCanvas.drawPoolOrLane(pool.getId(),
                    pool.getName(),
                    graphicInfo);
        }

        // Draw lanes
        for (Process process : bpmnModel.getProcesses()) {
            for (Lane lane : process.getLanes()) {
                GraphicInfo graphicInfo = bpmnModel.getGraphicInfo(lane.getId());
                processDiagramCanvas.drawPoolOrLane(lane.getId(),
                        lane.getName(),
                        graphicInfo);
            }
        }

        // Draw activities and their sequence-flows
        for (Process process : bpmnModel.getProcesses()) {
            for (FlowNode flowNode : process.findFlowElementsOfType(FlowNode.class)) {
                drawActivity(processDiagramCanvas,
                        bpmnModel,
                        flowNode,
                        flowsSkipIdlist,
                        flowsAgreeIdlist,
                        flowsRefuseIdlist,
                        flowsBackIdlist,
                        flowsHandleIdlist);
            }
        }

        // Draw artifacts
        for (Process process : bpmnModel.getProcesses()) {

            for (Artifact artifact : process.getArtifacts()) {
                drawArtifact(processDiagramCanvas,
                        bpmnModel,
                        artifact);
            }

            List<SubProcess> subProcesses = process.findFlowElementsOfType(SubProcess.class,
                    true);
            if (subProcesses != null) {
                for (SubProcess subProcess : subProcesses) {
                    for (Artifact subProcessArtifact : subProcess.getArtifacts()) {
                        drawArtifact(processDiagramCanvas,
                                bpmnModel,
                                subProcessArtifact);
                    }
                }
            }
        }

        return processDiagramCanvas;
    }

    protected void drawActivity(ActivitiDiagramCanvasExt processDiagramCanvas,
                                BpmnModel bpmnModel,
                                FlowNode flowNode,
                                List<String> flowsSkipIdlist,
                                List<String> flowsAgreeIdlist,
                                List<String> flowsRefuseIdlist,
                                List<String> flowsBackIdlist,
                                List<String> flowsHandleIdlist) {

        ActivityDrawInstruction drawInstruction = activityDrawInstructions.get(flowNode.getClass());
        if (drawInstruction != null) {

            drawInstruction.draw(processDiagramCanvas,
                    bpmnModel,
                    flowNode);

            // Gather info on the multi instance marker
            boolean multiInstanceSequential = false;
            boolean multiInstanceParallel = false;
            boolean collapsed = false;
            if (flowNode instanceof Activity) {
                Activity activity = (Activity) flowNode;
                MultiInstanceLoopCharacteristics multiInstanceLoopCharacteristics = activity.getLoopCharacteristics();
                if (multiInstanceLoopCharacteristics != null) {
                    multiInstanceSequential = multiInstanceLoopCharacteristics.isSequential();
                    multiInstanceParallel = !multiInstanceSequential;
                }
            }

            // Gather info on the collapsed marker
            GraphicInfo graphicInfo = bpmnModel.getGraphicInfo(flowNode.getId());
            if (flowNode instanceof SubProcess) {
                collapsed = graphicInfo.getExpanded() != null && !graphicInfo.getExpanded();
            } else if (flowNode instanceof CallActivity) {
                collapsed = true;
            }

            // Actually draw the markers
            processDiagramCanvas.drawActivityMarkers((int) graphicInfo.getX(),
                    (int) graphicInfo.getY(),
                    (int) graphicInfo.getWidth(),
                    (int) graphicInfo.getHeight(),
                    multiInstanceSequential,
                    multiInstanceParallel,
                    collapsed);

            // Draw flowsSkip activities
            if (flowsSkipIdlist.contains(flowNode.getId())) {
                drawHighLight(1, processDiagramCanvas,
                        bpmnModel.getGraphicInfo(flowNode.getId()));
            }

            // Draw flowsAgree activities
            if (flowsAgreeIdlist.contains(flowNode.getId())) {
                drawHighLight(2, processDiagramCanvas,
                        bpmnModel.getGraphicInfo(flowNode.getId()));
            }

            // Draw flowsRefuse activities
            if (flowsRefuseIdlist.contains(flowNode.getId())) {
                drawHighLight(3, processDiagramCanvas,
                        bpmnModel.getGraphicInfo(flowNode.getId()));
            }

            // Draw flowsBack activities
            if (flowsBackIdlist.contains(flowNode.getId())) {
                drawHighLight(4, processDiagramCanvas,
                        bpmnModel.getGraphicInfo(flowNode.getId()));
            }
            // Draw flowsHandle activities
            if (flowsHandleIdlist.contains(flowNode.getId())) {
                drawHighLight(5, processDiagramCanvas,
                        bpmnModel.getGraphicInfo(flowNode.getId()));
            }


        }

        // Outgoing transitions of activity
        for (SequenceFlow sequenceFlow : flowNode.getOutgoingFlows()) {
//            boolean highLighted = (highLightedFlows.contains(sequenceFlow.getId()));
            boolean highLighted = (flowsHandleIdlist.contains(sequenceFlow.getId()));
            String defaultFlow = null;
            if (flowNode instanceof Activity) {
                defaultFlow = ((Activity) flowNode).getDefaultFlow();
            } else if (flowNode instanceof Gateway) {
                defaultFlow = ((Gateway) flowNode).getDefaultFlow();
            }

            boolean isDefault = false;
            if (defaultFlow != null && defaultFlow.equalsIgnoreCase(sequenceFlow.getId())) {
                isDefault = true;
            }
            boolean drawConditionalIndicator = sequenceFlow.getConditionExpression() != null && !(flowNode instanceof Gateway);

            String sourceRef = sequenceFlow.getSourceRef();
            String targetRef = sequenceFlow.getTargetRef();
            FlowElement sourceElement = bpmnModel.getFlowElement(sourceRef);
            FlowElement targetElement = bpmnModel.getFlowElement(targetRef);
            List<GraphicInfo> graphicInfoList = bpmnModel.getFlowLocationGraphicInfo(sequenceFlow.getId());
            if (graphicInfoList != null && graphicInfoList.size() > 0) {
                graphicInfoList = connectionPerfectionizer(processDiagramCanvas,
                        bpmnModel,
                        sourceElement,
                        targetElement,
                        graphicInfoList);
                int xPoints[] = new int[graphicInfoList.size()];
                int yPoints[] = new int[graphicInfoList.size()];

                for (int i = 1; i < graphicInfoList.size(); i++) {
                    GraphicInfo graphicInfo = graphicInfoList.get(i);
                    GraphicInfo previousGraphicInfo = graphicInfoList.get(i - 1);

                    if (i == 1) {
                        xPoints[0] = (int) previousGraphicInfo.getX();
                        yPoints[0] = (int) previousGraphicInfo.getY();
                    }
                    xPoints[i] = (int) graphicInfo.getX();
                    yPoints[i] = (int) graphicInfo.getY();
                }

                processDiagramCanvas.drawSequenceflow(xPoints,
                        yPoints,
                        drawConditionalIndicator,
                        isDefault,
                        highLighted);

                // Draw sequenceflow label
                GraphicInfo labelGraphicInfo = bpmnModel.getLabelGraphicInfo(sequenceFlow.getId());
                if (labelGraphicInfo != null) {
                    processDiagramCanvas.drawLabel(sequenceFlow.getName(),
                            labelGraphicInfo,
                            false);
                }
            }
        }

        // Nested elements
        if (flowNode instanceof FlowElementsContainer) {
            for (FlowElement nestedFlowElement : ((FlowElementsContainer) flowNode).getFlowElements()) {
                if (nestedFlowElement instanceof FlowNode) {
                    drawActivity(processDiagramCanvas,
                            bpmnModel,
                            (FlowNode) nestedFlowElement,
                            flowsSkipIdlist,
                            flowsAgreeIdlist,
                            flowsRefuseIdlist,
                            flowsBackIdlist,
                            flowsHandleIdlist);
                }
            }
        }
    }


    private static void drawHighLight(int type, ActivitiDiagramCanvasExt processDiagramCanvas,
                                      GraphicInfo graphicInfo) {
        processDiagramCanvas.drawHighLight(type, (int) graphicInfo.getX(),
                (int) graphicInfo.getY(),
                (int) graphicInfo.getWidth(),
                (int) graphicInfo.getHeight());
    }


    protected static ActivitiDiagramCanvasExt initProcessDiagramCanvas(BpmnModel bpmnModel,
                                                                          String activityFontName,
                                                                          String labelFontName,
                                                                          String annotationFontName) {

        // We need to calculate maximum values to know how big the image will be in its entirety
        double minX = Double.MAX_VALUE;
        double maxX = 0;
        double minY = Double.MAX_VALUE;
        double maxY = 0;

        for (Pool pool : bpmnModel.getPools()) {
            GraphicInfo graphicInfo = bpmnModel.getGraphicInfo(pool.getId());
            minX = graphicInfo.getX();
            maxX = graphicInfo.getX() + graphicInfo.getWidth();
            minY = graphicInfo.getY();
            maxY = graphicInfo.getY() + graphicInfo.getHeight();
        }

        List<FlowNode> flowNodes = gatherAllFlowNodes(bpmnModel);
        for (FlowNode flowNode : flowNodes) {

            GraphicInfo flowNodeGraphicInfo = bpmnModel.getGraphicInfo(flowNode.getId());

            if (flowNodeGraphicInfo == null) {
                continue;
            }

            // width
            if (flowNodeGraphicInfo.getX() + flowNodeGraphicInfo.getWidth() > maxX) {
                maxX = flowNodeGraphicInfo.getX() + flowNodeGraphicInfo.getWidth();
            }
            if (flowNodeGraphicInfo.getX() < minX) {
                minX = flowNodeGraphicInfo.getX();
            }
            // height
            if (flowNodeGraphicInfo.getY() + flowNodeGraphicInfo.getHeight() > maxY) {
                maxY = flowNodeGraphicInfo.getY() + flowNodeGraphicInfo.getHeight();
            }
            if (flowNodeGraphicInfo.getY() < minY) {
                minY = flowNodeGraphicInfo.getY();
            }

            for (SequenceFlow sequenceFlow : flowNode.getOutgoingFlows()) {
                List<GraphicInfo> graphicInfoList = bpmnModel.getFlowLocationGraphicInfo(sequenceFlow.getId());
                if (graphicInfoList != null) {
                    for (GraphicInfo graphicInfo : graphicInfoList) {
                        // width
                        if (graphicInfo.getX() > maxX) {
                            maxX = graphicInfo.getX();
                        }
                        if (graphicInfo.getX() < minX) {
                            minX = graphicInfo.getX();
                        }
                        // height
                        if (graphicInfo.getY() > maxY) {
                            maxY = graphicInfo.getY();
                        }
                        if (graphicInfo.getY() < minY) {
                            minY = graphicInfo.getY();
                        }
                    }
                }
            }
        }

        List<Artifact> artifacts = gatherAllArtifacts(bpmnModel);
        for (Artifact artifact : artifacts) {

            GraphicInfo artifactGraphicInfo = bpmnModel.getGraphicInfo(artifact.getId());

            if (artifactGraphicInfo != null) {
                // width
                if (artifactGraphicInfo.getX() + artifactGraphicInfo.getWidth() > maxX) {
                    maxX = artifactGraphicInfo.getX() + artifactGraphicInfo.getWidth();
                }
                if (artifactGraphicInfo.getX() < minX) {
                    minX = artifactGraphicInfo.getX();
                }
                // height
                if (artifactGraphicInfo.getY() + artifactGraphicInfo.getHeight() > maxY) {
                    maxY = artifactGraphicInfo.getY() + artifactGraphicInfo.getHeight();
                }
                if (artifactGraphicInfo.getY() < minY) {
                    minY = artifactGraphicInfo.getY();
                }
            }

            List<GraphicInfo> graphicInfoList = bpmnModel.getFlowLocationGraphicInfo(artifact.getId());
            if (graphicInfoList != null) {
                for (GraphicInfo graphicInfo : graphicInfoList) {
                    // width
                    if (graphicInfo.getX() > maxX) {
                        maxX = graphicInfo.getX();
                    }
                    if (graphicInfo.getX() < minX) {
                        minX = graphicInfo.getX();
                    }
                    // height
                    if (graphicInfo.getY() > maxY) {
                        maxY = graphicInfo.getY();
                    }
                    if (graphicInfo.getY() < minY) {
                        minY = graphicInfo.getY();
                    }
                }
            }
        }

        int nrOfLanes = 0;
        for (Process process : bpmnModel.getProcesses()) {
            for (Lane l : process.getLanes()) {

                nrOfLanes++;

                GraphicInfo graphicInfo = bpmnModel.getGraphicInfo(l.getId());
                if (graphicInfo != null) {
                    // width
                    if (graphicInfo.getX() + graphicInfo.getWidth() > maxX) {
                        maxX = graphicInfo.getX() + graphicInfo.getWidth();
                    }
                    if (graphicInfo.getX() < minX) {
                        minX = graphicInfo.getX();
                    }
                    // height
                    if (graphicInfo.getY() + graphicInfo.getHeight() > maxY) {
                        maxY = graphicInfo.getY() + graphicInfo.getHeight();
                    }
                    if (graphicInfo.getY() < minY) {
                        minY = graphicInfo.getY();
                    }
                }
            }
        }

        // Special case, see https://activiti.atlassian.net/browse/ACT-1431
        if (flowNodes.isEmpty() && bpmnModel.getPools().isEmpty() && nrOfLanes == 0) {
            // Nothing to show
            minX = 0;
            minY = 0;
        }

        return new ActivitiDiagramCanvasExt((int) maxX + 10,
                (int) maxY + 10,
                (int) minX,
                (int) minY,
                activityFontName,
                labelFontName,
                annotationFontName);
    }


}
