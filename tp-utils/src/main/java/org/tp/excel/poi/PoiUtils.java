package org.tp.excel.poi;

import cn.afterturn.easypoi.excel.entity.ExcelToHtmlParams;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.ss.formula.functions.FreeRefFunction;
import org.apache.poi.ss.formula.udf.AggregatingUDFFinder;
import org.apache.poi.ss.formula.udf.DefaultUDFFinder;
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
 * @author wuwenyang
 * @date 2023/4/18 16:11
 */
public class PoiUtils {

    /**
     * 将excel模板转为新excel，web导出
     *
     * @param inputStream excel文件输入流
     * @param fileName    导出文件名
     * @param dataMap     todo占位符对应的数据，key：占位符，value：占位符要替换的数据
     */
    public static <T> void templateToExcel(InputStream inputStream, String fileName, Map<String, T> dataMap, HttpServletResponse response) throws IOException {
        //获取模板文件，Workbook，Sheet
        Workbook workbook = WorkbookFactory.create(inputStream);
        //注入对IFS公式的支持
        workbook.addToolPack(new AggregatingUDFFinder(new DefaultUDFFinder(new String[]{IfsFunction.FUNCTION_NAME}, new FreeRefFunction[]{new IfsFunction()})));
        //获取sheet页
        Sheet sheet = workbook.getSheetAt(0);

        //处理todo
        for (Row row : sheet) {
            for (Cell cell : row) {
                if (cell.getCellType() == CellType.STRING && cell.getStringCellValue().startsWith(PoiConstant.TODO_PREFIX)) {
                    String cellHolder = StringUtils.substringBefore(cell.getStringCellValue(), PoiConstant.FORMULA_SEPARATOR);
                    //单元格赋值
                    setCellValue(cell, dataMap.get(cellHolder));
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
     * @param dataMap     todo占位符对应的数据，key：占位符，left：占位符要替换的数据，right：单元格的批注（单元格颜色样式放在批注中，通过自定义属性实现）
     * @param exeFormula  是否执行公式
     * @param response
     */
    public static <T> void templateToExcelHtml(InputStream inputStream, Map<String, ImmutablePair<T, String>> dataMap, boolean exeFormula, HttpServletResponse response) throws IOException {
        //获取模板文件，Workbook，Sheet
        Workbook workbook = WorkbookFactory.create(inputStream);
        //注入对IFS公式的支持
        workbook.addToolPack(new AggregatingUDFFinder(new DefaultUDFFinder(new String[]{IfsFunction.FUNCTION_NAME}, new FreeRefFunction[]{new IfsFunction()})));
        //获取sheet页
        Sheet sheet = workbook.getSheetAt(0);
        //公式执行器
        FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();

        //创建批注
        CreationHelper factory = workbook.getCreationHelper();
        ClientAnchor anchor = factory.createClientAnchor();
        Drawing drawing = sheet.createDrawingPatriarch();

        //处理todo
        for (Row row : sheet) {
            for (Cell cell : row) {
                if (cell.getCellType() == CellType.STRING && cell.getStringCellValue().startsWith(PoiConstant.TODO_PREFIX)) {
                    String cellHolder = StringUtils.substringBefore(cell.getStringCellValue(), PoiConstant.FORMULA_SEPARATOR);
                    ImmutablePair<T, String> pair = dataMap.get(cellHolder);
                    if (pair == null) {
                        cell.setBlank();
                        continue;
                    }
                    //单元格赋值
                    setCellValue(cell, pair.getLeft());
                    //添加批注
                    if (StringUtils.isNotBlank(pair.getRight())) {
                        RichTextString richTextString = factory.createRichTextString(pair.getRight());
                        Comment comment = drawing.createCellComment(anchor);
                        comment.setString(richTextString);
                        cell.setCellComment(comment);
                    }
                } else if (cell.getCellType() == CellType.FORMULA && exeFormula) {
                    CellStyle cellStyle = cell.getCellStyle();
                    short dataFormatIndex = cell.getCellStyle().getDataFormat();
                    //常规格式默认2位
                    if (dataFormatIndex == 0) {//General
                        cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));
                        cell.setCellStyle(cellStyle);
                    }
                    //处理公式
                    evaluator.evaluateFormulaCell(cell);
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

    /**
     * 单元格赋值
     */
    private static <T> void setCellValue(Cell cell, T left) {
        if (left == null) {
            cell.setBlank();
        } else if (left instanceof Double) {
            cell.setCellValue(((Double) left));
        } else if (left instanceof String) {
            cell.setCellValue(((String) left));
        } else if (left instanceof Boolean) {
            cell.setCellValue(((Boolean) left));
        } else if (left instanceof Date) {
            cell.setCellValue(((Date) left));
        } else if (left instanceof Calendar) {
            cell.setCellValue(((Calendar) left));
        } else {
            cell.setCellValue(String.valueOf(left));
        }
    }
}
