package com.redxun.common.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.redxun.common.tool.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * EasyExcel工具类
 */
public class EasyExcelUtil {

    //获取多表头
    public static List<List<String>> constructMutiExportFieldColumns(String title,String columns,String field) {
        JSONArray colArr = JSONArray.parseArray(columns);
        List<List<String>> list = new ArrayList<>();
        for (int i = 0; i < colArr.size(); i++) {
            JSONObject obj=colArr.getJSONObject(i);
            String name = obj.getString(field);
            String childColumns = obj.getString("children");
            List<String> fieldsList = new ArrayList<>();
            fieldsList.add(title);
            fieldsList.add(name);
            if (StringUtils.isNotEmpty(childColumns) && !"[]".equals(childColumns)) {
                list.addAll(constructMutiExport(childColumns,fieldsList,field));
            }else{
                list.add(fieldsList);
            }
        }
        return list;
    }

    private static List<List<String>> constructMutiExport(String columns,List<String> fields,String field){
        JSONArray colArr = JSONArray.parseArray(columns);
        List<List<String>> tmpFeidls=new ArrayList<>();
        for (int i = 0; i < colArr.size(); i++) {
            List<String> tmpField=new ArrayList<>();
            tmpField.addAll(fields);
            JSONObject obj = colArr.getJSONObject(i);
            String name = obj.getString(field);
            String childColumns = obj.getString("children");
            tmpField.add(name);
            if (StringUtils.isNotEmpty(childColumns) && !"[]".equals(childColumns)) {
                tmpFeidls.addAll(constructMutiExport(childColumns, tmpField,field));
            }else {
                tmpFeidls.add(tmpField);
            }
        }
        return tmpFeidls;
    }

    //获取单表头
    public static List<List<String>> constructSingleExportFieldColumns(String title,String columns,List<List<String>> head,String field) {
        JSONArray colArr = JSONArray.parseArray(columns);
        for (int i = 0; i < colArr.size(); i++) {
            JSONObject obj = colArr.getJSONObject(i);
            String name = obj.getString(field);
            String childColumns = obj.getString("children");
            if (StringUtils.isNotEmpty(childColumns) && !"[]".equals(childColumns)) {
                constructSingleExportFieldColumns(title, childColumns, head, field);
            } else {
                List<String> fieldsList = new ArrayList<>();
                fieldsList.add(title);
                fieldsList.add(name);
                head.add(fieldsList);
            }
        }
        return head;
    }

    public static void writeExcel(String fileName, List<List<String>> head, List<List<Object>> data, HttpServletResponse response )throws IOException {
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            fileName = URLEncoder.encode(fileName, "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            EasyExcel.write(response.getOutputStream()).head(head).sheet("模板").doWrite(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取EXCEL数据。
     * @param multipartFile     文件
     * @param beginRow          开始行
     * @param sheetNo           第几个表格
     * @return
     * @throws IOException
     */
    public static List<Map<Integer, String>> readExcel(MultipartFile multipartFile, String beginRow,Integer sheetNo)throws IOException {
        ExcelDataListener listener = new ExcelDataListener();
        EasyExcel.read(multipartFile.getInputStream(), listener).sheet(sheetNo).doRead();
        if (!"".equals(beginRow)){
            listener.setBeginRow(Integer.parseInt(beginRow));
        }
         return listener.getList();
    }
}
