package org.tp.mix.temp;

import org.apache.poi.hssf.usermodel.HSSFHyperlink;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFHyperlink;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class MoveSheetExample {

    public static CellStyle cellStyle;

    public static void main(String[] args) throws IOException {
//        String sourceFilePath = "D:\\桌面\\生产日报基础数据-2024年06月02日.xlsx";
        String sourceFilePath = "D:\\桌面\\燃料日报-2024年06月02日.xlsx";
//        String sourceFilePath = "D:\\桌面\\配煤表-2024年06月02日.xlsx";
        String targetFilePath = "D:\\桌面\\copy";

        // 读取源文件
        FileInputStream sourceFile = new FileInputStream(sourceFilePath);
        Workbook sourceWorkbook = WorkbookFactory.create(sourceFile);

        // 获取要移动的Sheet
        Sheet sourceSheet = sourceWorkbook.getSheetAt(0);
        if (sourceSheet == null) {
            throw new RuntimeException("Sheet not found");
        }


        Workbook targetWorkbook;
        if (sourceWorkbook instanceof XSSFWorkbook) {
            targetWorkbook = new XSSFWorkbook();
            targetFilePath += ".xlsx";
        } else {
            targetWorkbook = new HSSFWorkbook();
            targetFilePath += ".xls";
        }
        // 创建目标文件（如果不存在会创建）
        FileOutputStream targetFile = new FileOutputStream(targetFilePath);

        // 复制Sheet到目标Workbook
        Sheet targetSheet = targetWorkbook.createSheet(sourceSheet.getSheetName());

        cellStyle = targetWorkbook.createCellStyle();
        copySheet(sourceSheet, targetSheet, sourceWorkbook, targetWorkbook);

        // 从源Workbook中删除移动的Sheet, 无用
        sourceWorkbook.removeSheetAt(0);

        // 写入目标文件并关闭流
        targetWorkbook.write(targetFile);
        sourceFile.close();
        targetFile.close();
    }

    private static void copySheet(Sheet sourceSheet, Sheet targetSheet, Workbook sourceWorkbook, Workbook targetWorkbook) {

        // 复制列样式
        for (int i = 0; i < sourceSheet.getNumMergedRegions(); i++) {
            CellRangeAddress sourceRegion = sourceSheet.getMergedRegion(i);
            CellRangeAddress targetRegion = new CellRangeAddress(sourceRegion.getFirstRow(), sourceRegion.getLastRow(),
                    sourceRegion.getFirstColumn(), sourceRegion.getLastColumn());
            targetSheet.addMergedRegion(targetRegion);
        }

        //复制列宽
        int lastColIndex = sourceSheet.getRow(0).getLastCellNum();
        for (int i = 0; i <= lastColIndex; i++) {
            targetSheet.setColumnWidth(i, sourceSheet.getColumnWidth(i));
        }

        // 复制行
        for (Row sourceRow : sourceSheet) {
            Row targetRow = targetSheet.createRow(sourceRow.getRowNum());
            copyRow(sourceRow, targetRow, sourceWorkbook, targetWorkbook);
        }

    }

    private static void copyRow(Row sourceRow, Row targetRow, Workbook sourceWorkbook, Workbook targetWorkbook) {

        // 复制行样式
        if (sourceRow.getRowStyle() != null) {
            CellStyle rowCellStyle = targetWorkbook.createCellStyle();
            rowCellStyle.cloneStyleFrom(sourceRow.getRowStyle());
            targetRow.setRowStyle(rowCellStyle);
        }

        // 复制单元格
        for (Cell sourceCell : sourceRow) {
            Cell targetCell = targetRow.createCell(sourceCell.getColumnIndex());
            copyCell(sourceCell, targetCell, sourceWorkbook, targetWorkbook);
        }

    }

    private static void copyCell(Cell sourceCell, Cell targetCell, Workbook sourceWorkbook, Workbook targetWorkbook) {

        // 复制单元格样式
        CellStyle cellStyle = targetWorkbook.createCellStyle();
        cellStyle.cloneStyleFrom(sourceCell.getCellStyle());
        targetCell.setCellStyle(cellStyle);

        //超链接也有两种版本
        Hyperlink sourceHyperlink = sourceCell.getHyperlink();
        if (sourceHyperlink != null) {

            if (sourceHyperlink instanceof HSSFHyperlink) {
                //TODO 会报错
//                HSSFHyperlink link = (HSSFHyperlink) targetWorkbook.getCreationHelper().createHyperlink(sourceHyperlink.getType());
                HSSFHyperlink link = (HSSFHyperlink) targetWorkbook.getCreationHelper().createHyperlink(sourceHyperlink.getType());

                // 设置新超链接的目标地址
                link.setAddress(sourceHyperlink.getAddress());

                // 如果是电子邮件类型，设置邮箱和主题
//                if (sourceHyperlink.getType() == HyperlinkType.EMAIL) {
//                    link.setTextMark(((HSSFHyperlink) sourceHyperlink).getTextMark());
//                }
                targetCell.setHyperlink(link);
            } else if (sourceHyperlink instanceof XSSFHyperlink) {
                targetCell.setHyperlink(new XSSFHyperlink(sourceHyperlink));
            }
        }
        //不生效
//        targetCell.getCellStyle().cloneStyleFrom(sourceCell.getCellStyle());

        switch (sourceCell.getCellType()) {
            case STRING:
                targetCell.setCellValue(sourceCell.getStringCellValue());
                break;
            case NUMERIC:
                targetCell.setCellValue(sourceCell.getNumericCellValue());
                break;
            case BOOLEAN:
                targetCell.setCellValue(sourceCell.getBooleanCellValue());
                break;
            case FORMULA:
                targetCell.setCellFormula(sourceCell.getCellFormula());
                break;
            case BLANK:
                targetCell.setCellType(CellType.BLANK);
                break;
            case ERROR:
                targetCell.setCellErrorValue(sourceCell.getErrorCellValue());
                break;
            default:
                // 其他类型如 _NONE
                break;
        }
    }
}

