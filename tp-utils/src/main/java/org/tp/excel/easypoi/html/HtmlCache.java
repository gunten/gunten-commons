package org.tp.excel.easypoi.html;

import cn.afterturn.easypoi.excel.entity.ExcelToHtmlParams;

/**
 * Excel 转变成为Html 的缓存
 *
 */
public class HtmlCache {

    public static String getHtml(ExcelToHtmlParams params) {
        try {
            return new ExcelToHtmlService(params).printPage();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
