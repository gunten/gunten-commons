package org.tp.excel.poi;

import cn.afterturn.easypoi.excel.entity.ExcelToHtmlParams;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.poi.ss.usermodel.*;
import org.tp.excel.easypoi.html.ExcelXorHtmlUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 *
 */
public class PoiUtils {

    /**
     * 暂定：todo占位符以todo_开头
     */
    private final static String TODO_PREFIX = "todo_";

    /**
     * 将excel模板转为新excel，web导出
     *
     * @param inputStream excel文件输入流
     * @param fileName    导出文件名
     * @param dataMap     todo占位符对应的数据，key：占位符，value：占位符要替换的数据
     */
    public static void templateToExcel(InputStream inputStream, String fileName, Map<String, Double> dataMap, HttpServletResponse response) throws IOException {
        //获取模板文件，Workbook，Sheet
        Workbook workbook = WorkbookFactory.create(inputStream);
        Sheet sheet = workbook.getSheetAt(0);

        //处理todo
        for (Row row : sheet) {
            for (Cell cell : row) {
                if (cell.getCellType() == CellType.STRING && cell.getStringCellValue().startsWith(TODO_PREFIX)) {
                    //单元格赋值
                    if (dataMap.get(cell.getStringCellValue()) != null) {
                        cell.setCellValue(dataMap.get(cell.getStringCellValue()));
                    }
                }
            }
        }

        //处理公式
        FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
        for (Row row : sheet) {
            for (Cell cell : row) {
                if (cell.getCellType() == CellType.FORMULA) {
                    evaluator.evaluateFormulaCell(cell);
                }
            }
        }

        // 设置响应头，告诉浏览器返回的是excel文件
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        //防止中文乱码
        fileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        //关闭流
        inputStream.close();

        //excel to web
        workbook.write(response.getOutputStream());
    }

    /**
     * 将excel模板转为新excel，再将新excel转为html，web展示
     *
     * @param inputStream excel文件输入流
     * @param dataMap     todo占位符对应的数据，key：占位符，left：占位符要替换的数据，right：单元格的批注
     * @param exeFormula  是否执行公式
     * @param response
     */
    public static <T> void templateToExcelHtml(InputStream inputStream, Map<String, ImmutablePair<T, String>> dataMap, boolean exeFormula, HttpServletResponse response) throws IOException {
        //获取模板文件，Workbook，Sheet
        Workbook workbook = WorkbookFactory.create(inputStream);
        Sheet sheet = workbook.getSheetAt(0);

        //创建批注
        CreationHelper factory = workbook.getCreationHelper();
        ClientAnchor anchor = factory.createClientAnchor();
        Drawing drawing = sheet.createDrawingPatriarch();

        //添加样式
        CellStyle cellStyle = workbook.createCellStyle();
        //设置背景色
        cellStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
        //必须设置 否则背景色不生效
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        //处理todo
        for (Row row : sheet) {
            for (Cell cell : row) {
                if (cell.getCellType() == CellType.STRING && cell.getStringCellValue().startsWith(TODO_PREFIX)) {
                    ImmutablePair<T, String> immutablePair = dataMap.get(cell.getStringCellValue());
                    if (immutablePair == null) {
                        continue;
                    }
                    //单元格赋值
                    if (immutablePair.getLeft() != null) {
                        if (immutablePair.getLeft() instanceof Double) {
                            cell.setCellValue(((Double) immutablePair.getLeft()));
                        } else if (immutablePair.getLeft() instanceof String) {
                            cell.setCellValue(((String) immutablePair.getLeft()));
                        } else if (immutablePair.getLeft() instanceof Boolean) {
                            cell.setCellValue(((Boolean) immutablePair.getLeft()));
                        } else if (immutablePair.getLeft() instanceof Date) {
                            cell.setCellValue(((Date) immutablePair.getLeft()));
                        } else if (immutablePair.getLeft() instanceof Calendar) {
                            cell.setCellValue(((Calendar) immutablePair.getLeft()));
                        }
                    } else {
                        cell.setBlank();
                    }
                    //添加批注
                    if (StringUtils.isNotBlank(immutablePair.getRight())) {
                        RichTextString richTextString = factory.createRichTextString(immutablePair.getRight());
                        Comment comment = drawing.createCellComment(anchor);
                        comment.setString(richTextString);
                        cell.setCellComment(comment);
                    }
                    //单元格增加背景色
                    cell.setCellStyle(cellStyle);
                }
            }
        }

        //处理公式
        if (exeFormula) {
            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
            for (Row row : sheet) {
                for (Cell cell : row) {
                    if (cell.getCellType() == CellType.FORMULA) {
                        evaluator.evaluateFormulaCell(cell);
                    }
                }
            }
        }

        //excel to html
        ExcelToHtmlParams params = new ExcelToHtmlParams(workbook, true, "yes");
        String html = ExcelXorHtmlUtil.excelToHtml(params);

        //关闭流
        workbook.close();
        inputStream.close();

        //html to web
        response.getOutputStream().write(html.getBytes());
    }
}
