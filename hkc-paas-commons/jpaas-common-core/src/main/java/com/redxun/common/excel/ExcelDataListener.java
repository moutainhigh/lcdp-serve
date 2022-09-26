package com.redxun.common.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Excel数据监听器
 */
public class ExcelDataListener extends AnalysisEventListener<Map<Integer, String>> {
    //存放读取的数据
    private List<Map<Integer, String>> list = new ArrayList<Map<Integer, String>>();
    //从第几行开始读数据
    private int beginRow=0;

    @Override
    public void invoke(Map<Integer, String> data, AnalysisContext analysisContext) {
        list.add(data);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }

    public List<Map<Integer, String>> getList() {
        List<Map<Integer, String>> newList=new ArrayList<>();
        if(beginRow>0){
            for (int i=beginRow-2;i<list.size();i++){
                newList.add(list.get(i));
            }
            return newList;
        }
        return list;
    }

    public void setList(List<Map<Integer, String>> list) {
        this.list = list;
    }

    public int getBeginRow() {
        return beginRow;
    }

    public void setBeginRow(int beginRow) {
        this.beginRow = beginRow;
    }
}
