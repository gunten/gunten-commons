package org.tp;

import com.lowagie.text.pdf.BaseFont;
import lombok.extern.slf4j.Slf4j;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * Html 转 PDF 工具
 * @author <a href="mailto:mm_8023@hotmail.com">gunten<a/>
 * 2019/5/24
 */

@Slf4j
public class Html2PDFUtil {
    private static String font = "simhei.ttf";

    /**
     * 获取html中body
     * @param html
     * @return
     */
    public static String getBody(String html) {
        String start = "<body>";
        String end = "</body>";
        int s = html.indexOf(start) + start.length();
        int e = html.indexOf(end);
        return html.substring(s, e);
    }

    /**
     * html 转 pdf,需要固定的html头
     */
    public static void htmlToPdf(String targetPath, String content) throws Exception{
        ITextRenderer render = new ITextRenderer();
        ITextFontResolver fontResolver = render.getFontResolver();
        try (OutputStream os = new FileOutputStream(targetPath)){
            fontResolver.addFont(font, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            render.setDocumentFromString(content);
            render.layout();
            render.createPDF(os);
        } catch (Exception e) {
            log.warn("html to pdf failed 转换PDF失败");
            throw e;
        }
    }

    public static void main(String[] args) {
        String str = "<!DOCTYPE html[<!ENTITY nbsp '&#160;'><!ENTITY ldquo '&#8220;'><!ENTITY rdquo '&#8221;'><!ENTITY divide '&#247;'><!ENTITY times '&#215;'><!ENTITY mdash '&#8212;'>]><html><head><meta charset=\"UTF-8\"/><style type=\"text/css\"> body{font-family:SimHei;}.title{font-size:24px;font-weight:bold;text-align:center;margin-bottom:10px}}</style></head><body><h1 style=\"text-align: center;\">这就是例子！！！</h1><p>&nbsp;</p></body></html>";
        try {
            Html2PDFUtil.htmlToPdf("D:\\contract\\lizi.pdf",str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}