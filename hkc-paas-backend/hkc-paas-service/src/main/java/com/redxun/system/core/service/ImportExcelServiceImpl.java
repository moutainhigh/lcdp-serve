
package com.redxun.system.core.service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.IUser;
import com.redxun.common.script.GroovyEngine;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ContextUtil;
import com.redxun.system.core.entity.ImportExcel;
import com.redxun.system.core.entity.ImportExcelBat;
import com.redxun.system.core.mapper.ImportExcelMapper;
import com.redxun.system.feign.FormClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * [sys_excel]业务服务类
 */
@Service
public class ImportExcelServiceImpl extends SuperServiceImpl<ImportExcelMapper, ImportExcel> implements BaseService<ImportExcel> {

    @Resource
    private ImportExcelMapper importExcelMapper;

    @Resource
    GroovyEngine groovyEngine;

    @Autowired
    ImportExcelBatServiceImpl importExcelBatService;

    @Resource
    FormClient formClient;


    @Override
    public BaseDao<ImportExcel> getRepository() {
        return importExcelMapper;
    }


    public Map<String,List<List<String>>> writeWithoutHead(InputStream inputStream, boolean part) throws IOException {

        StringExcelListener listener = new StringExcelListener(part);
        ExcelReader excelReader = EasyExcelFactory.read(inputStream, null, listener).headRowNumber(0).build();
        List<ReadSheet> temp = excelReader.excelExecutor().sheetList();
        Map<String,List<List<String>>> datas =new LinkedHashMap<>();

        for(int i=0;i<temp.size();i++){
            ReadSheet readSheet = EasyExcel.readSheet(i).build();
            ReadSheet sheetName = temp.get(i);
            excelReader.read(readSheet);
            List<List<String>> current = new ArrayList<>();
            current.addAll(listener.getDatas());
            datas.put( sheetName.getSheetName(),current);
            listener.getDatas().clear();
        }

        excelReader.finish();
        return datas;
    }

    private static class StringExcelListener extends AnalysisEventListener {

        public StringExcelListener(){
        }

        private boolean part=false;

        Boolean firstReading = true;
        int totalLine = 0;
        int maxsize = 50;

        public StringExcelListener(boolean part){
            this.part=part;
        }

        /**
         * 自定义用于暂时存储data
         * 可以通过实例获取该值
         */
        private List<List<String>> datas = new ArrayList<>();


        @Override
        public boolean hasNext(AnalysisContext context) {
            if(this.part && datas.size() > maxsize){
                return false;
            };
            return true;
        }

        /**
         * 每解析一行都会回调invoke()方法
         *
         * @param object  读取后的数据对象
         * @param context 内容
         */
        @Override
        public void invoke(Object object, AnalysisContext context) {

            Map<Integer, String> stringMap = (HashMap<Integer, String>) object;
            if(firstReading == true) {
                totalLine = ((HashMap<Integer, String>) object).size();
            }

            firstReading = false;

            List<String> result = analyze(stringMap);

            datas.add(result);

        }

        private List<String> analyze(Map<Integer, String> stringMap){
            List<String> listValue = new ArrayList<String>();
            if(stringMap.size()!= totalLine) {
                for (int i = 0; i < totalLine; i++) {
                    if (stringMap.get(i) == null) {
                        stringMap.put(i, null);
                    }
                }
            }

            for(Map.Entry<Integer, String> entry: stringMap.entrySet()){
                listValue.add(entry.getValue());
            }

            return listValue;
        }

        @Override
        public void doAfterAllAnalysed(AnalysisContext context) {
        }

        /**
         * 返回数据
         *
         * @return 返回读取的数据集合
         **/
        public List<List<String>> getDatas() {
            firstReading = true;
            return datas;
        }


    }

    public String execute(String templateId, List<List<String>>list ,  Map<String, Object> importExcel) throws IOException {

        String dsNameJson= (String) importExcel.get("dsName");
        String tableNameJson= (String) importExcel.get("tableName");

        JSONObject dsJson=JSONObject.parseObject(dsNameJson);
        JSONObject tableJson=JSONObject.parseObject(tableNameJson);
        String dsName="";
        if(dsJson!=null){
            dsName=dsJson.getString("value");
        }

        String tableName=tableJson.getString("value");


        int batId = importExcelBatService.getBatId(templateId);

        StringBuilder sbError = new StringBuilder();

        String header[] = ((JSONArray)importExcel.get("headerContent")).toArray(new String[]{});
        int titleLine = (int) importExcel.get("titleLine");

        JSONArray jsonArr =  (JSONArray)importExcel.get("gridData");

        boolean firstError = true;

        for(int i = titleLine; i < list.size(); i++) {
            try {
                List<String> curRow=list.get(i);
                //获取一行数据
                Map<String, Object> resultMap = getRow(tableName, batId, header, jsonArr, curRow);
                //插入一行数据。
                insertRow(dsName, tableName, resultMap);

            }
            catch (Exception ex){
                if(firstError){
                    sbError.append( "第" + batId + "批：" + "\n");
                    firstError = false;
                }
                sbError.append( "第" + i + "行 ：" + ex.toString() + "\n");
            }
        }

        //添加批次
        addBat(dsName,tableName,batId,templateId);

        String error=sbError.toString();
        if(StringUtils.isEmpty(error)){
            return "第" + batId + "批：导入成功" + "\n";
        }

        return error;
    }

    private void addBat(String dsName,String table,int batId,String templateId){
        ImportExcelBat excelBat=new ImportExcelBat();
        excelBat.setId(IdGenerator.getIdStr());
        excelBat.setDsAlias(dsName);
        excelBat.setTable(table);
        excelBat.setBatId(batId);
        excelBat.setTemplateId(templateId);

        importExcelBatService.insert(excelBat);
    }

    /**
     * 插入一行数据。
     * @param dsName        数据源
     * @param tableName     表名
     * @param resultMap     一行数据
     */
    private void insertRow(String dsName, String tableName, Map<String, Object> resultMap) {
        List<String> fields=new ArrayList<>();
        List<String> values=new ArrayList<>();

        resultMap.forEach((key, value) -> {
            fields.add(key);
            values.add("#{" + key +"}");
        });

        String sql="insert into " + tableName + "("+ StringUtils.join(fields ,",")+") values ("+StringUtils.join(values,",")+")";

        formClient.executeSql(dsName, sql,resultMap);
    }

    /**
     * 取得一行数据。
     * @param tableName
     * @param batId
     * @param header
     * @param jsonArr
     * @param curRow
     * @return
     */
    private Map<String, Object> getRow(String tableName, int batId, String[] header, JSONArray jsonArr, List<String> curRow) {
        Map<String, Object> resultMap = new HashMap<>();
        for (int j = 0; j < jsonArr.size(); j++) {
            JSONObject currentRow = jsonArr.getJSONObject(j);
            String mapType = currentRow.getString("mapType");
            if(StringUtils.isEmpty(mapType)){
                continue;
            }


            String mapValue = currentRow.getString("mapValue");
            String fieldName = currentRow.getString("fieldName");
            String value = null;
            try {
                value = calculateValue(mapType, mapValue, header, curRow, batId);
            }
            catch (Exception ex){
                String errorMsg = "[" + fieldName + "]：" +ex.getMessage();
                throw new RuntimeException(errorMsg);
            }
            resultMap.put(fieldName, value);

        }
        return resultMap;
    }

    private String calculateValue(String mapType, String mapValue, String[] header,
                                  List<String> curRow,  int batId) throws Exception {
        String result = "";
        switch (mapType){

            case "fixValue":
                result = mapValue;
                break;

            case "field":
                String[] arr = header;
                for(int i = 0; i < arr.length; i++){
                    if(arr[i].equals(mapValue)){
                        result = curRow.get(i);
                    }
                }
                break;

            case "scriptValue":
                Map<String,Object> params=new HashMap<>();
                String[] scriptArray = header;
//                for(int i=0; i < scriptArray.length; i++){
//                    params.put(scriptArray[i].substring(1,scriptArray[i].length()-1),curRow.get(i));
//                }
                //
                for(int i=0; i < scriptArray.length; i++){
                    params.put(scriptArray[i],curRow.get(i));
                }
                params.put("currentTime",new Date());
                IUser user = ContextUtil.getCurrentUser();
                params.put("currentUser",user.getFullName());
                result =  (String) groovyEngine.executeScripts(mapValue,params);
                break;

            case "pkId":
                result = IdGenerator.getIdStr();
                break;

            case "time":
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                result = formatter.format(new Date());
                break;

            case "curUserId":
                result = ContextUtil.getCurrentUserId();
                break;

            case "curUserName":
                IUser userName = ContextUtil.getCurrentUser();
                result = userName.getFullName();
                break;

            case "batId":
                result = String.valueOf(batId);
                break;

        }
        return result;
    }

}
