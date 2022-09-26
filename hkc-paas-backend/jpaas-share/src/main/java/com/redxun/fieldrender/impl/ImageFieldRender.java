package com.redxun.fieldrender.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ImageUtil;
import com.redxun.common.utils.SysPropertiesUtil;
import com.redxun.fieldrender.IFieldRender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * 功能: 图片控件显示
 *
 * @author ASUS
 * @date 2022/5/17 23:42
 */
@Component
@Slf4j
public class ImageFieldRender implements IFieldRender {
    @Override
    public List<String> getControl() {
        List<String> list=new ArrayList<>();
        //[{"fileId":"1527192884531720194","fileName":"avatar2.jpg","size":7489}]"
        list.add("rx-image");
        return list;
    }

    @Override
    public String render(String val) {
        StringBuffer stringBuffer = new StringBuffer();
        try {
            if(StringUtils.isEmpty(val)){
                return "";
            }
            //http://localhost:8000/api/api-system/system/core/sysFile/previewImg?fileId=1529003005658382338&time=1653380815281&isScale=true
            JSONArray jsonArray = JSONArray.parseArray(val);
            String serverUrl= SysPropertiesUtil.getString("serverAddress");
            for (Object obj : jsonArray) {
                JSONObject jsonObject = (JSONObject) JSONObject.toJSON(obj);
                String url = serverUrl+"/api/api-system/system/core/sysFile/previewImg?fileId="+jsonObject.getString("fileId");
                String base64 = ImageUtil.getBase64ByImageUrl(new URL(url));
               // stringBuffer.append("<a target=\"_blank\" href=\""+serverUrl+"/api/api-system/system/core/sysFile/public/download/" +jsonObject.getString("fileId") +"\">"+jsonObject.getString("fileName")+"</a><br/>");
                stringBuffer.append("<img style=\"width: 102px; height: 102px;\" src=\"data:image/png;base64," +base64 +"\"/>");
            }
        }catch (Exception e){
            e.printStackTrace();
            log.error("ImageFieldRender获取渲染值出错,val值为：{}",val);
        }
        return stringBuffer.toString();
    }
}
