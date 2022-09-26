package com.redxun.bpm.activiti.img;

import org.activiti.image.impl.DefaultProcessDiagramCanvas;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Activiti的流程图画布扩展
 * 用于实现一些节点的的高亮显示
 */
public class ActivitiDiagramCanvasExt extends DefaultProcessDiagramCanvas {


    public ActivitiDiagramCanvasExt(int width, int height, int minX, int minY, String activityFontName, String labelFontName, String annotationFontName) {
        super(width, height, minX, minY, activityFontName, labelFontName, annotationFontName);
    }

    public ActivitiDiagramCanvasExt(int width, int height, int minX, int minY) {
        super(width, height, minX, minY);
    }


    public void drawHighLight(int type,
                              int x,
                              int y,
                              int width,
                              int height) {
        Paint originalPaint = g.getPaint();
        Stroke originalStroke = g.getStroke();

        if(type == 1){
            g.setPaint(Color.BLUE);
        }else if(type == 2){
            g.setPaint(Color.GREEN);
        }else if(type == 3){
            g.setPaint(Color.PINK);
        }else if(type == 4){
            g.setPaint(Color.YELLOW);
        }else if(type == 5){
            g.setPaint(Color.RED);
        }

        g.setStroke(THICK_TASK_BORDER_STROKE);

        RoundRectangle2D rect = new RoundRectangle2D.Double(x,
                y,
                width,
                height,
                20,
                20);
        g.draw(rect);

        g.setPaint(originalPaint);
        g.setStroke(originalStroke);
    }





}
