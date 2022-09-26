package com.redxun.common.engine;


import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;


@Service
public class FtlEngine {

    @Autowired
    private Configuration configuration;

    /**
     * 把指定的模板生成对应的字符串
     * @param templateName
     * @param model
     * @return
     * @throws IOException
     * @throws TemplateException receiver reveiver
     */
    public String mergeTemplateIntoString(String templateName,Object model) throws IOException, TemplateException {
        Template template=configuration.getTemplate(templateName);
        return FreeMarkerTemplateUtils.processTemplateIntoString(template,model);
    }

    /**
     * 处理模板，把内容放进输出流
     * @param templateName
     * @param model
     * @param writer
     * @throws IOException
     * @throws TemplateException
     */
    public void processTemplate(String templateName, Object model, Writer writer) throws IOException,TemplateException{
        Template template=configuration.getTemplate(templateName);
        template.process(model,writer);
    }

    public String parseByStringTemplate(Object model,String templateSource) throws IOException,TemplateException{
        Configuration cfg = new Configuration();
        StringTemplateLoader loader = new StringTemplateLoader();
        cfg.setTemplateLoader(loader);
        cfg.setClassicCompatible(true);
        loader.putTemplate("freemarker",templateSource);
        Template template=cfg.getTemplate("freemarker");
        StringWriter writer=new StringWriter();
        template.process(model,writer);
        return writer.toString();
    }

}
