package com.redxun.fieldrender.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.SysPropertiesUtil;
import com.redxun.fieldrender.IFieldRender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能: TODO
 *
 * @author ASUS
 * @date 2022/5/17 23:42
 */
@Component
@Slf4j
public class FileFieldRender implements IFieldRender {
    @Override
    public List<String> getControl() {
        List<String> list=new ArrayList<>();
        //[{"fileId":"1527192907378094082","size":715,"fileName":"arrow.png","createUser":"管理员","createTime":"2022-05-19 15:42:17"}]"
        list.add("rx-upload");
        return list;
    }

    @Override
    public String render(String val) {
        StringBuffer stringBuffer = new StringBuffer();
        try {
            if(StringUtils.isEmpty(val)){
                return "";
            }
            JSONArray jsonArray = JSONArray.parseArray(val);

            String serverUrl= SysPropertiesUtil.getString("serverAddress");
            for (Object obj : jsonArray) {
                JSONObject jsonObject = (JSONObject) JSONObject.toJSON(obj);
                stringBuffer.append("<a target=\"_blank\" href=\""+serverUrl+"/api/api-system/system/core/sysFile/public/download/" +jsonObject.getString("fileId") +"\">"+jsonObject.getString("fileName")+"</a><br/>");
            }
        }catch (Exception e){
            e.printStackTrace();
            log.error("FileFieldRender获取渲染值出错,val值为：{}",val);
        }
        return stringBuffer.toString();
    }
}
