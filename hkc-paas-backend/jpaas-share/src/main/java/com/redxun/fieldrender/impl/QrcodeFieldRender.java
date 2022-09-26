package com.redxun.fieldrender.impl;

import com.redxun.common.utils.QrcodeUtil;
import com.redxun.fieldrender.IFieldRender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能: 增加二维码的渲染。
 * @author ray
 * @date 2022/5/18 16:22
 */
@Slf4j
@Component
public class QrcodeFieldRender implements IFieldRender {
    @Override
    public List<String> getControl() {
        List<String> list=new ArrayList<>();
        list.add("rx-qrcode");
        return list;
    }

    @Override
    public String render(String val) {
        try{
            String str= QrcodeUtil.getBase64(val,150);
            return "<img src=\"data:image/png;base64," +str +"\"/>";
        }
        catch (Exception ex){
            ex.printStackTrace();
            log.error("ImageFieldRender获取渲染值出错,val值为：{}",val);
        }
        return "";
    }
}
