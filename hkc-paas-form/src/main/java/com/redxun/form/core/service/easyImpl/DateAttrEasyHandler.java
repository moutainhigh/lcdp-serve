package com.redxun.form.core.service.easyImpl;

import com.alibaba.fastjson.JSONObject;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.DateUtils;
import com.redxun.common.tool.StringUtils;
import com.redxun.dboperator.model.Column;
import com.redxun.dboperator.model.DefaultColumn;
import com.redxun.form.bo.entity.FieldEntity;
import com.redxun.form.bo.entity.FormBoAttr;
import com.redxun.form.bo.service.TableUtil;
import com.redxun.form.core.entity.ValueResult;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component("easyDateAttrHandler")
public class DateAttrEasyHandler extends BaseAttrEasyHandler {
    private static final String START="_START";
    private static final String END="_END";
    private static final String SPLITOR=",";

    @Override
    public String getPluginName() {
        return "date";
    }

    @Override
    public String getDescription() {
        return "日期控件";
    }

    @Override
    public FormBoAttr parse(JSONObject jsonObject) {
        FormBoAttr attr = super.parse(jsonObject);
        JSONObject options = jsonObject.getJSONObject("options");
        String ctlType = options.getString("ctlType");
        if("date".equals(ctlType)){
            //日期控件
            attr.setDataType(Column.COLUMN_TYPE_DATE);

        }else if("range".equals(ctlType)){
            //日期范围
            attr.setDataType(Column.COLUMN_TYPE_DATE);
            attr.setLength(40);
        }else if("month".equals(ctlType)){
            //月份控件
            attr.setDataType(Column.COLUMN_TYPE_VARCHAR);
            attr.setLength(20);
        }else if("week".equals(ctlType)){
            //周控件
            attr.setDataType(Column.COLUMN_TYPE_VARCHAR);
            attr.setLength(20);
        }
        return attr;
    }

    @Override
    protected void setInitData(FormBoAttr attr, JSONObject setting, JSONObject jsonObject) {

        JSONObject options=setting.getJSONObject("options");
        boolean defaultShow=options.getBoolean("isDefaultValue");
        if(!defaultShow){
            return;
        }
        String format=options.getString("format");
        String ctlType=options.getString("ctlType");
        format=format.replace("YYYY","yyyy").replace("DD","dd");
        if("date".equals(ctlType) || "month".equals(ctlType)){
            String val=DateUtils.dateTimeNow(format);
            jsonObject.put(attr.getName(),val);
        }
        if("range".equals(ctlType)){
            String val= DateUtils.dateTimeNow(DateUtils.switchFormat(format));
            String value=val +SPLITOR +val;
            jsonObject.put(attr.getName(),value);
        }
        if("week".equals(ctlType)){
            int year=DateUtils.getCurYear();
            int week=DateUtils.getCurWeekOfYear();
            String val=year +"-"+ week +"周";
            if(format.indexOf("/")!=-1){
                val=year +"/"+ week +"周";
            }
            jsonObject.put(attr.getName(),val);

        }


    }

    public static void main(String[] args) {
        int week=DateUtils.getCurWeekOfYear();
        System.err.println(week);
    }

    @Override
    public ValueResult getValue(FormBoAttr attr, Map<String, Object> rowData, boolean isExternal) {
        JSONObject obj=JSONObject.parseObject(attr.getExtJson());
        JSONObject options=obj.getJSONObject("options");
        String ctlType = options.getString("ctlType");
        String format = options.getString("format");
        if("date".equals(ctlType)){
            //日期控件
            String fieldName=attr.getFieldName().toUpperCase();
            Object val;
            JSONObject formJson=attr.getFormJson();
            if(rowData.containsKey(fieldName)){
                val=rowData.get(fieldName);
                //如果数据为空直接显示为空。
                if(val==null){
                    return ValueResult.exist("");
                }
                if(BeanUtil.isEmpty(format)){
                    format= DateUtils.DATE_FORMAT_YMD;
                }

                String rtn= DateUtils.parseDateToStr( DateUtils.switchFormat(format),(Date)val);
                return  ValueResult.exist(rtn);
            }
        }else if("range".equals(ctlType)){
            //日期范围
            JSONObject formJson= attr.getFormJson();
            if(BeanUtil.isEmpty(format)){
                format= DateUtils.DATE_FORMAT_YMD;
            }

            String startField=TableUtil.getFieldName(attr.getName() +START);
            String endField=TableUtil.getFieldName(attr.getName() +END);

            if(rowData.containsKey(startField)){
                Date startVal= (Date) rowData.get(startField);
                Date endVal= (Date) rowData.get(endField);

                String start=DateUtils.parseDateToStr(DateUtils.switchFormat(format),startVal);
                String end=DateUtils.parseDateToStr(DateUtils.switchFormat(format),endVal);

                String val=  start +SPLITOR + end;

                return ValueResult.exist(val);
            }
        }
        else{
            String fieldName=attr.getFieldName().toUpperCase();
            Object val;
            if(rowData.containsKey(fieldName)){
                val=rowData.get(fieldName);
                //如果数据为空直接显示为空。
                if(val==null){
                    return ValueResult.exist("");
                }
                return  ValueResult.exist(val);
            }
        }
        return ValueResult.noExist();
    }

    @Override
    public List<FieldEntity> getFieldEntity(FormBoAttr attr, JSONObject json) {
        JSONObject obj=JSONObject.parseObject(attr.getExtJson());
        JSONObject options=obj.getJSONObject("options");
        String ctlType = options.getString("ctlType");
        String format = options.getString("format");
        List<FieldEntity> list=new ArrayList<>();
        if("date".equals(ctlType)){
            //日期控件
            String data=json.getString(attr.getName());
            if(BeanUtil.isEmpty(format)){
                format=DateUtils.DATE_FORMAT_YMD;
            }
            FieldEntity entity=new FieldEntity();
            entity.setName(attr.getName());
            entity.setFieldName(attr.getFieldName());
            String s = DateUtils.switchFormat(format);
            if(StringUtils.isNotEmpty(data)){
                entity.setValue(DateUtils.parseDate(data,s));
            }
            list.add(entity);
        }else if("range".equals(ctlType)){
            //日期范围
            if(BeanUtil.isEmpty(format)){
                format=DateUtils.DATE_FORMAT_YMD;
            }
            String data=json.getString(attr.getName());
            String[] aryDate=data.split(SPLITOR);
            FieldEntity start=getDataEnt(attr,DateUtils.switchFormat(format),true,aryDate[0]);
            FieldEntity end=getDataEnt(attr,DateUtils.switchFormat(format),false,aryDate[1]);
            list.add(start);
            list.add(end);
        }else{
            return super.getFieldEntity(attr,json);
        }
        return list;
    }

    @Override
    public List<Column> getColumns(FormBoAttr attr) {
        JSONObject obj=JSONObject.parseObject(attr.getExtJson());
        JSONObject options=obj.getJSONObject("options");
        String ctlType = options.getString("ctlType");
        if("range".equals(ctlType)){
            //日期范围
            Column colStart = getColumnByAttr(attr, true);
            Column colEnd = getColumnByAttr(attr, false);
            List<Column> columns = new ArrayList<>();
            columns.add(colStart);
            columns.add(colEnd);
            return columns;
        }
        return super.getColumns(attr);
    }

    private FieldEntity getDataEnt(FormBoAttr attr,String format,boolean start,String val){
        String suffix=start?START:END;

        FieldEntity entity=new FieldEntity();
        entity.setName(attr.getName()+suffix);
        entity.setFieldName(TableUtil.getFieldName(attr.getName() +suffix));
        entity.setValue(DateUtils.parseDate(val,DateUtils.switchFormat(format)));
        return entity;
    }

    private static Column getColumnByAttr(FormBoAttr attr, boolean start){
        Column col=new DefaultColumn();
        String suffix=start?START:END;
        String preComment=start?"开始":"结束";

        col.setFieldName(TableUtil.getFieldName(attr.getName() +suffix));
        col.setColumnType(attr.getDataType());
        col.setDbFieldType(attr.getDbFieldType());
        String comment=attr.getComment() + preComment;
        col.setComment(comment);
        return col;
    }
}
