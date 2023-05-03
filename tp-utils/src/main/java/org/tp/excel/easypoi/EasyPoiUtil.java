package org.tp.excel.easypoi;

import cn.afterturn.easypoi.cache.manager.POICacheManager;
import cn.afterturn.easypoi.excel.ExcelXorHtmlUtil;
import cn.afterturn.easypoi.excel.entity.ExcelToHtmlParams;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * @date 2023/2/27 10:50
 */
public class EasyPoiUtil {

    /**
     * easypoi to html
     *
     * @param path     excel文件路径
     * @param response
     * @throws IOException
     */
    public static void easyPoiToHtml(String path, HttpServletResponse response) throws IOException {
        if (StringUtils.isBlank(path)) {
            return;
        }
        ExcelToHtmlParams params = new ExcelToHtmlParams(WorkbookFactory.create(POICacheManager.getFile(path)), true, "yes");
        response.getOutputStream().write(ExcelXorHtmlUtil.excelToHtml(params).getBytes());
    }

    /**
     * easypoi to html
     *
     * @param inputStream excel输入流
     * @param response
     * @throws IOException
     */
    public static void easyPoiToHtml(InputStream inputStream, HttpServletResponse response) throws IOException {
        if (Objects.isNull(inputStream)) {
            return;
        }
        ExcelToHtmlParams params = new ExcelToHtmlParams(WorkbookFactory.create(inputStream), true, "yes");
        response.getOutputStream().write(ExcelXorHtmlUtil.excelToHtml(params).getBytes());
    }
}
