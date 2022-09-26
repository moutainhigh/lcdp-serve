package com.redxun.fieldrender;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Iterator;

/**
 * 功能: 对字段进行渲染
 * <pre>
 *     使用在表单模板上。
 * </pre>
 * @author ray
 * @date 2022/5/17 10:04
 */
public class RenderUtil {

    /**
     * 对控件进行渲染。
     * @param val
     * @param control
     * @return
     */
    public static String render(Object val,String control){
        IFieldRender fieldRender= FieldRenderContext.getFieldRender(control);
        String str = "";
        if(val!=null){
            str = String.valueOf(val);
        }
        return  fieldRender.render(str);
    }



    /**
     * 替换rx-list 标签。
     * @param pdfTemp
     * @return
     */
    public static String constructPDFTemp(String pdfTemp){

        pdfTemp=pdfTemp.replaceAll("&#39;","\"");

        Document doc = Jsoup.parse(pdfTemp);
        Elements listELs = doc.getElementsByAttribute("rx-list");
        Iterator<Element> listIt=listELs.iterator();
        while(listIt.hasNext()){
            Element el = listIt.next();
            String value = el.attr("rx-list");
            el.removeAttr("rx-list");

            el.before("<#list "+value+" as item>");

            el.after("</#list>");
        }
        Elements ifEls=doc.getElementsByAttribute("rx-if");
        Iterator<Element> ifIt=ifEls.iterator();
        while(ifIt.hasNext()){
            Element el = ifIt.next();
            String value = el.attr("rx-if");
            el.removeAttr("rx-if");

            el.before("<#if "+value+">");

            el.after("</#if>");
        }

        String html = doc.body().html();
        html = html.replaceAll("&lt;", "<");
        html = html.replaceAll("&gt;", ">");
        html = html.replaceAll("<!--#list-->","</#list>");
        html = html.replaceAll("<!--#if-->","</#if>");
        html = html.replaceAll("<!--#assign-->","</#assign>");
        html = html.replaceAll("<!--#function-->","</#function>");
        html = html.replaceAll("&amp;&amp;","&&");

        return html;
    }
}
