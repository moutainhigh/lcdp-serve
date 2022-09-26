package com.redxun.common.utils;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Excel工具类
 *
 * @author yjy
 * @date 2019/1/6
 */
public class ExcelUtil {
    private ExcelUtil() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 导出
     *
     * @param list           数据列表
     * @param title          标题
     * @param sheetName      sheet名称
     * @param pojoClass      元素类型
     * @param fileName       文件名
     * @param isCreateHeader 是否创建列头
     * @param response
     * @throws IOException
     */
    public static void exportExcel(List<?> list, String title, String sheetName, Class<?> pojoClass, String fileName
            , boolean isCreateHeader, HttpServletResponse response) throws IOException {
        ExportParams exportParams = new ExportParams(title, sheetName, ExcelType.XSSF);
        exportParams.setCreateHeadRows(isCreateHeader);
        defaultExport(list, pojoClass, fileName, response, exportParams);

    }

    /**
     * 导出
     *
     * @param list      数据列表
     * @param title     标题
     * @param sheetName sheet名称
     * @param pojoClass 元素类型
     * @param fileName  文件名
     * @param response
     * @throws IOException
     */
    public static void exportExcel(List<?> list, String title, String sheetName, Class<?> pojoClass, String fileName
            , HttpServletResponse response) throws IOException {
        defaultExport(list, pojoClass, fileName, response, new ExportParams(title, sheetName, ExcelType.XSSF));
    }

    /**
     * 导出 EXCEL
     * @param list
     * @param title
     * @param sheetName
     * @param gridColumns
     * @param fileName
     * @param response
     * @throws IOException
     */
    public static void exportExcel(List<?> list, String title, String sheetName, List<ExcelExportEntity> gridColumns, String fileName
            , HttpServletResponse response) throws IOException {
        defaultExport(list, gridColumns, fileName, response, new ExportParams(title, sheetName, ExcelType.XSSF));
    }

    /**
     * 导出
     *
     * @param list     数据列表(元素是Map)
     * @param fileName 文件名
     * @param response
     * @throws IOException
     */
    public static void exportExcel(List<Map<String, Object>> list, String fileName, HttpServletResponse response) throws IOException {
        defaultExport(list, fileName, response);
    }

    /**
     * 缺省导出
     * @param list
     * @param pojoClass
     * @param fileName
     * @param response
     * @param exportParams
     * @throws IOException
     */
    private static void defaultExport(List<?> list, Class<?> pojoClass, String fileName
            , HttpServletResponse response, ExportParams exportParams) throws IOException {
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, pojoClass, list);
        if (workbook != null) {
            downLoadExcel(fileName, response, workbook);
        }
    }

    private static void defaultExport(List<?> list, List<ExcelExportEntity> gridColumns, String fileName
            , HttpServletResponse response, ExportParams exportParams) throws IOException {
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, gridColumns, list);
        if (workbook != null) {
            downLoadExcel(fileName, response, workbook);
        }
    }

    private static void defaultExport(List<Map<String, Object>> list, String fileName, HttpServletResponse response) throws IOException {
        Workbook workbook = ExcelExportUtil.exportExcel(list, ExcelType.XSSF);
        if (workbook != null) {
            downLoadExcel(fileName, response, workbook);
        }
    }

    /**
     * 下载EXCEL
     * @param fileName
     * @param response
     * @param workbook
     * @throws IOException
     */
    private static void downLoadExcel(String fileName, HttpServletResponse response, Workbook workbook) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        workbook.write(response.getOutputStream());
    }

    /**
     * 导入EXCEL
     * @param filePath
     * @param titleRows
     * @param headerRows
     * @param pojoClass
     * @param <T>
     * @return
     */
    public static <T> List<T> importExcel(String filePath, Integer titleRows, Integer headerRows, Class<T> pojoClass) {
        if (StringUtils.isBlank(filePath)) {
            return Collections.emptyList();
        }
        ImportParams params = new ImportParams();
        params.setTitleRows(titleRows);
        params.setHeadRows(headerRows);
        return ExcelImportUtil.importExcel(new File(filePath), pojoClass, params);
    }

    /**
     * 在线导入EXCEL
     * @param file
     * @param titleRows
     * @param headerRows
     * @param pojoClass
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> List<T> importExcel(MultipartFile file, Integer titleRows, Integer headerRows, Class<T> pojoClass) throws Exception {
        if (file == null) {
            return Collections.emptyList();
        }
        ImportParams params = new ImportParams();
        params.setTitleRows(titleRows);
        params.setHeadRows(headerRows);
        return ExcelImportUtil.importExcel(file.getInputStream(), pojoClass, params);
    }

    /**
     * 验证EXCEL文件
     *
     * @param filePath
     * @return
     */
    public static boolean validateExcel(String filePath) {
        if (filePath == null || !(isExcel2003(filePath) || isExcel2007(filePath))) {
            return false;
        }
        return true;
    }

    public static boolean getExcelInfo(String fileName) {
        // 根据文件名判断文件是2003版本还是2007版本
        boolean isXls = true;
        if (isExcel2007(fileName)) {
            isXls = false;
        }
        return isXls;
    }

    // @描述：是否是2003的excel，返回true是2003
    public static boolean isExcel2003(String filePath)  {
        return filePath.matches("^.+\\.(?i)(xls)$");
    }

    //@描述：是否是2007的excel，返回true是2007
    public static boolean isExcel2007(String filePath)  {
        return filePath.matches("^.+\\.(?i)(xlsx)$");
    }

    /**
     * Excel 单元格类型
     * @param cell
     * @return
     */
    public static String getValueByCellType(Cell cell){
        int cellType=cell.getCellType().getCode();
        String val="";
        switch (cellType) {
            case 1:
                val = cell.getStringCellValue();
                break;
            case 2:
                try {
                    val = cell.getStringCellValue();
                } catch (IllegalStateException e) {
                    val = String.valueOf(cell.getNumericCellValue());
                }
                break;
            case 4:
                Boolean val1 = cell.getBooleanCellValue();
                val = val1.toString();
                break;
            case 0:
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    Date theDate = cell.getDateCellValue();
                    SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    val = dff.format(theDate);
                }else{
                    DecimalFormat df = new DecimalFormat("0");
                    val = df.format(cell.getNumericCellValue());
                }
                break;
            case 3:
                break;
            default:
        }

        return val;
    }

    /**
     * 获取行Span
     * @param childColumns
     * @param colspan
     * @return
     */
    private static int getRowSpan(List<ExcelExportEntity> childColumns,int colspan){
        for(ExcelExportEntity childColumn:childColumns){
            if(childColumn.getList().size()>0){
                List<ExcelExportEntity> subChildColumns = childColumn.getList();
                colspan = getRowSpan(subChildColumns, colspan);
            }else{
                colspan +=1;
            }
        }
        return colspan;
    }

    /**
     * 通过Json构建多表头Field列
     * @param jsonColumns
     * @return
     */
    public static List<ExcelExportEntity> constructMutiExportFieldColumns(String jsonColumns){
        JSONArray colArr= JSONArray.parseArray(jsonColumns);
        List<ExcelExportEntity> columns=new ArrayList<>();
        for(int i=0;i<colArr.size();i++){
            List<ExcelExportEntity> childColumn = new ArrayList<>();
            JSONObject obj=colArr.getJSONObject(i);
            String header=obj.getString("header");
            String fieldName=obj.getString("field");
            String allowSort=obj.getString("allowSort");
            String sWidth=obj.getString("width");
            String visible=obj.getString("visible");
            String childColumns=obj.getString("children");
            int index=sWidth.lastIndexOf("px");
            int width=100;
            int colspan = 1;
            if(index!=-1){
                sWidth=sWidth.substring(0, index);
                width=new Integer(sWidth);
            }
            if(StringUtils.isNotEmpty(childColumns)&&!("[]".equals(childColumns))){
                childColumn = constructMutiExportFieldColumns(childColumns);
                colspan = getRowSpan(childColumn,0);
            }
            ExcelExportEntity fc=new ExcelExportEntity();
            fc.setKey(fieldName);
            fc.setName(header);
            //fc.setWidth(width);
            //fc.setList(childColumn);
            columns.add(fc);
        }
        return columns;
    }

    /**
     * 通过Json构建单表头Field列
     * @param jsonColumns
     * @return
     */
    public static List<ExcelExportEntity> constructSingleExportFieldColumns(String jsonColumns,List<ExcelExportEntity> columns){
        JSONArray colArr=JSONArray.parseArray(jsonColumns);
        for(int i=0;i<colArr.size();i++){
            List<ExcelExportEntity> childColumn = new ArrayList<>();
            JSONObject obj=colArr.getJSONObject(i);
            String header=obj.getString("header");
            String fieldName=obj.getString("field");
            String allowSort=obj.getString("allowSort");
            String sWidth=obj.getString("width");
            String visible=obj.getString("visible");
            String childColumns=obj.getString("children");
            int index=sWidth.lastIndexOf("px");
            int width=100;
            int colspan = 1;
            if(index!=-1){
                sWidth=sWidth.substring(0, index);
                width=new Integer(sWidth);
            }
            if(StringUtils.isNotEmpty(childColumns)&&!("[]".equals(childColumns))){
                childColumn = constructSingleExportFieldColumns(childColumns,columns);
            }else{
                ExcelExportEntity fc=new ExcelExportEntity();
                fc.setKey(fieldName);
                fc.setName(header);
                //fc.setWidth(width);
                //fc.setList(childColumn);
                columns.add(fc);
            }
        }
        return columns;
    }
}
