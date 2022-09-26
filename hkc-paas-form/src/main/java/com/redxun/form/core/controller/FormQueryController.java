package com.redxun.form.core.controller;

import com.alibaba.fastjson.JSONObject;
import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.base.entity.SqlModel;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.tool.StringUtils;
import com.redxun.db.CommonDao;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 功能: 表单数据查询
 *
 * @author ASUS
 * @date 2022/6/6 9:51
 */
@Slf4j
@RestController
@RequestMapping("/form/core/formquery")
@ClassDefine(title = "表单数据查询",alias = "formQueryController",path = "/form/core/formquery",packages = "core",packageName = "表单管理")
@Api(tags = "表单数据查询")
public class FormQueryController {

    @Resource
    CommonDao commonDao;

    /**
     * 查询数据
     * @param json
     * {
     *     //数据源
     *     datasource:"",
     *     //表名
     *     tablename:"",
     *     //字段
     *     field:"使用逗号分隔",
     *     format:"{name}",
     *     //条件
     *     conditionField:{type:'number,string',name:''},
     *     //查询条件值
     *     value:""
     * }
     * @return
     * @throws Exception
     */
    @MethodDefine(title = "查询数据", path = "/doQuery", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "查询数据", varName = "json")})
    @ApiOperation("根据配置查询数据")
    @PostMapping("/doQuery")
    public JsonResult doQuery(@RequestBody JSONObject json) throws Exception {
        String datasource=json.getString("datasource");
        String tableName=json.getString("tablename");
        String field=json.getString("field").toUpperCase();
        JSONObject conditionField=json.getJSONObject("conditionField");
        String type=conditionField.getString("type");
        String name=conditionField.getString("name");
        String value=json.getString("value");
        String format=json.getString("format");
        if(format==null){
            format="";
        }

        if(StringUtils.isEmpty(value)){
            return JsonResult.Success().setData("");
        }

        if("string".equals(type) && value.indexOf(",")!=-1){
            value=convertInStr(value);
        }

        SqlModel model=new SqlModel();
        if(value.indexOf(",")!=-1){
            String sql="select " + field + " from " + tableName + " where "+name +" in ("+value+")";
            model.setSql(sql);
        }
        else{
            String sql="select " + field + " from " + tableName + " where "+name +" =#{w.value} ";
            model.setSql(sql);
            model.addParam("value",value);
        }

        StringBuilder sb=new StringBuilder();
        List list = commonDao.query(datasource, model);
        int i=0;
        for(Object obj :list){

            Map row=(Map) obj;
            Iterator<Map.Entry<String,Object>> it = row.entrySet().iterator();
            String tmp=format;

            while(it.hasNext()){
                Map.Entry<String,Object> ent= it.next();
                if(StringUtils.isEmpty(format)){
                    tmp += ent.getValue();
                }
                else{
                    tmp = tmp.replace("{"+ent.getKey().toUpperCase() +"}",ent.getValue().toString() );
                }
            }
            if(i>0){
                sb.append(",");
            }
            sb.append(tmp );

            i++;

        }
        return JsonResult.Success().setData(sb.toString());
    }


    public static void main(String[] args) {
        String str="{name}A{address}";
        str=str.replace("{name}","老王");
        System.err.println(str);
    }

    private String convertInStr(String value){
        String[] vals=value.split(",");
        String tmp = "";
        for(int i = 0; i < vals.length; ++i) {
            String str = vals[i];
            if (i == 0) {
                tmp = tmp + "'" + str + "'";
            } else {
                tmp = tmp + ",'" + str + "'";
            }
        }
        return  tmp;
    }
}
