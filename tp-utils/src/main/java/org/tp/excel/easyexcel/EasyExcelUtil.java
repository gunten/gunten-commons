package org.tp.excel.easyexcel;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.handler.WriteHandler;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.WriteTable;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * easyExcel导出工具类
 *
 * @date 2023年02月23日 16:28
 */
public class EasyExcelUtil {

    /**
     * 单sheet多table导出excel
     *
     * @param excelData        表格sheet名 表头 表数据
     * @param writeHandlerList 自定义策略
     * @throws IOException
     */
    public static void write(EasyExcelData excelData, List<WriteHandler> writeHandlerList) throws IOException {
        buildExcel(new FileOutputStream(excelData.getFileName()), excelData, writeHandlerList);
    }

    /**
     * web，单sheet多table导出excel
     *
     * @param response
     * @param excelData        表格sheet名 表头 表数据
     * @param writeHandlerList 自定义策略
     * @throws IOException
     */
    public static void download(HttpServletResponse response, EasyExcelData excelData, List<WriteHandler> writeHandlerList) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        //防止中文乱码
        String fileName = URLEncoder.encode(excelData.getFileName(), "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        buildExcel(response.getOutputStream(), excelData, writeHandlerList);
    }

    /**
     * 构建 easyExcel
     *
     * @param out
     * @param excelData
     * @param writeHandlerList
     * @throws IOException
     */
    private static void buildExcel(OutputStream out, EasyExcelData excelData, List<WriteHandler> writeHandlerList) throws IOException {
        validData(excelData);
        ExcelWriterBuilder builder = EasyExcelFactory.write(out);
        //注入自定义策略
        if (CollectionUtils.isNotEmpty(writeHandlerList)) {
            writeHandlerList.forEach(builder::registerWriteHandler);
        }
        ExcelWriter writer = builder.build();
        WriteSheet sheet = new WriteSheet();
        sheet.setSheetName(excelData.getSheetName());
        sheet.setSheetNo(0);
        List<EasyExcelTable> tableList = excelData.getTable();
        //创建表格，用于 Sheet 中使用
        for (int i = 0; i < tableList.size(); i++) {
            WriteTable table = new WriteTable();
            table.setTableNo(i + 1);
            table.setHead(tableList.get(i).getHeadList());
            //写数据
            writer.write(tableList.get(i).getDataList(), sheet, table);
        }
        writer.finish();
        out.close();
    }

    /**
     * 数据校验
     *
     * @param excelData
     */
    private static void validData(EasyExcelData excelData) {
        if (Objects.isNull(excelData)) {
            excelData = EasyExcelData.defaultValue();
        }
        if (CollectionUtils.isEmpty(excelData.getTable())) {
            excelData.setTable(Collections.emptyList());
        }
        if (StringUtils.isBlank(excelData.getFileName())) {
            excelData.setFileName("excel");
        }
        if (StringUtils.isBlank(excelData.getSheetName())) {
            excelData.setSheetName("sheet1");
        }
    }
}
