
package com.redxun.form.core.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.form.core.entity.FormPdfTemplate;
import com.redxun.form.core.mapper.FormPdfTemplateMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Resource;

/**
* [form_pdf_template]业务服务类
*/
@Service
public class FormPdfTemplateServiceImpl extends SuperServiceImpl<FormPdfTemplateMapper, FormPdfTemplate> implements BaseService<FormPdfTemplate> {

    @Resource
    private FormPdfTemplateMapper formPdfTemplateMapper;

    @Override
    public BaseDao<FormPdfTemplate> getRepository() {
        return formPdfTemplateMapper;
    }


    /**
     * 替换rx-list 标签。
     * @param pdfTemp
     * @return
     */
    public  String constructPDFTemp(String pdfTemp){
        Document doc = Jsoup.parse(pdfTemp);
        Elements listELs = doc.getElementsByAttribute("rx-list");
        Iterator<Element> listIt=listELs.iterator();
        while(listIt.hasNext()){
            Element el = listIt.next();
            String value = el.attr("rx-list");
            el.removeAttr("rx-list");
            if("history".equals(value)){
                el.before("<#list "+value+" as item>");
            }else {
                el.before("<#list data."+value+" as item>");
            }
            el.after("</#list>");
        }

        String html = doc.body().html();
        html = html.replaceAll("&lt;", "<");
        html = html.replaceAll("&gt;", ">");
        html = html.replaceAll("<!--#list-->","</#list>");
        html = html.replaceAll("<!--#assign-->","</#assign>");
        html = html.replaceAll("<!--#function-->","</#function>");
        html = html.replaceAll("&amp;&amp;","&&");

        return html;
    }


}
