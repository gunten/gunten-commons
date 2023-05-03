package org.tp.excel.easypoi.html;

import cn.afterturn.easypoi.excel.entity.ExcelToHtmlParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.excel.html.HtmlToExcelService;
import cn.afterturn.easypoi.exception.excel.ExcelExportException;
import cn.afterturn.easypoi.exception.excel.enums.ExcelExportEnum;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;

/**
 * Excel 变成界面
 */
public class ExcelXorHtmlUtil {

    private ExcelXorHtmlUtil() {
    }

    /**
     * 转换成为Table
     * @param wb Excel
     * @return
     */
    public static String toTableHtml(Workbook wb) {
        return HtmlCache.getHtml(new ExcelToHtmlParams(wb, false, 0, null));
    }

    /**
     * 转换成为完整界面,显示图片
     * @param wb Excel
     * @return
     */
    public static String toAllHtml(Workbook wb) {
        return HtmlCache.getHtml(new ExcelToHtmlParams(wb, true, 0, null));
    }

    /**
     * 转换成为Table
     *
     * 源码改造点：
     * 1、转html时，增加对单元格是公式的支持。CellValueHelper.getHtmlValue()
     * 2、拼接html时，增加对自定义属性的支持。ExcelToHtmlService.printSheetContent()
     *
     * @param params
     * @return
     */
    public static String excelToHtml(ExcelToHtmlParams params) {
        return HtmlCache.getHtml(params);
    }

    /**
     * Html 读取Excel
     * @param html
     * @param type
     * @return
     */
    public static Workbook htmlToExcel(String html, ExcelType type) {
        Workbook workbook = null;
        if (ExcelType.HSSF.equals(type)) {
            workbook = new HSSFWorkbook();
        } else {
            workbook = new XSSFWorkbook();
        }
        new HtmlToExcelService().createSheet(html, workbook);
        return workbook;
    }

    /**
     * Html 读取Excel
     * @param is
     * @param type
     * @return
     */
    public static Workbook htmlToExcel(InputStream is, ExcelType type) {
        try {
            return htmlToExcel(new String(IOUtils.toByteArray(is)), type);
        } catch (IOException e) {
            throw new ExcelExportException(ExcelExportEnum.HTML_ERROR, e);
        }
    }

}
