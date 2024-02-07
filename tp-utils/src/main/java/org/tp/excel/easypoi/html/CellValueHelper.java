package org.tp.excel.easypoi.html;

import com.google.common.xml.XmlEscapers;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Cell值帮助类
 *
 */
public class CellValueHelper {
    /**
     * Excel 格式
     */
    private boolean is07;

    private int cssRandom;

    private Map<String, String> fontCache = new HashMap<String, String>();

    public CellValueHelper(Workbook wb, int cssRandom) {
        this.cssRandom = cssRandom;
        if (wb instanceof HSSFWorkbook) {
            is07 = false;
        } else if (wb instanceof XSSFWorkbook) {
            is07 = true;
            cacheFontInfo(wb);
        } else {
            throw new IllegalArgumentException(
                    "unknown workbook type: " + wb.getClass().getSimpleName());
        }
    }

    /**
     * O7 版本坑爹bug
     *
     * @param wb
     */
    private void cacheFontInfo(Workbook wb) {
        for (int i = 0, le = wb.getNumberOfFonts(); i < le; i++) {
            Font font = wb.getFontAt(i);
            fontCache.put(font.getBold() + "_" + font.getItalic() + "_" + font.getFontName()
                            + "_" + font.getFontHeightInPoints() + "_" + font.getColor(),
                    font.getIndex() + "");
        }

    }

    public String getHtmlValue(Cell cell) {
        if (CellType.BOOLEAN == cell.getCellType()
                || CellType.NUMERIC == cell.getCellType()) {
            cell.setCellType(CellType.STRING);
            return cell.getStringCellValue();
        } else if (CellType.STRING == cell.getCellType()) {
            if (cell.getRichStringCellValue().numFormattingRuns() == 0) {
                return XmlEscapers.xmlContentEscaper().escape(cell.getStringCellValue());
            } else if (is07) {
                return getXSSFRichString((XSSFRichTextString) cell.getRichStringCellValue());
            } else {
                return getHSSFRichString((HSSFRichTextString) cell.getRichStringCellValue());
            }
        } else if (CellType.FORMULA == cell.getCellType()) {
            //单元格公式处理
            if (CellType.NUMERIC == cell.getCachedFormulaResultType()) {
                double value = cell.getNumericCellValue();
                CellStyle style = cell.getCellStyle();
                DecimalFormat format = new DecimalFormat();
                String dataFormatString = style.getDataFormatString();
                if (StringUtils.isBlank(dataFormatString)) {
                    return String.valueOf(value);
                }
                // 单元格设置成常规
                switch (dataFormatString) {
                    case "General":
                        format.applyPattern("#");
                        break;
                    case "0.00":
                    case "0.00_ ":
                    case "#,##0.00":
                        format.applyPattern("0.##");
                        break;
                    case "0.000":
                    case "0.000_ ":
                        format.applyPattern("0.###");
                        break;
                    case "0%":
                        format.applyPattern("#%");
                        break;
                    case "0.00%":
                        format.applyPattern("#.##%");
                        break;
                    case "0.000%":
                        format.applyPattern("#.###%");
                        break;
                    default:
                        break;
                }
                return format.format(value);
            } else if (CellType.STRING == cell.getCellType()) {
                return cell.getRichStringCellValue().toString();
            } else {
                return "";
            }
        }
        return "";
    }

    /**
     * 03版本复杂数据
     *
     * @param rich
     * @return
     */
    private String getHSSFRichString(HSSFRichTextString rich) {
        int nums = rich.numFormattingRuns();
        StringBuilder sb = new StringBuilder();
        String text = rich.toString();
        int currentIndex = 0;
        sb.append(text.substring(0, rich.getIndexOfFormattingRun(0)));
        for (int i = 0; i < nums; i++) {
            sb.append("<span ");
            sb.append("class='font_" + rich.getFontOfFormattingRun(i));
            sb.append("_");
            sb.append(cssRandom);
            sb.append("'>");
            currentIndex = rich.getIndexOfFormattingRun(i);
            if (i < nums - 1) {
                sb.append(XmlEscapers.xmlContentEscaper()
                        .escape(text.substring(currentIndex, rich.getIndexOfFormattingRun(i + 1))));
            } else {
                sb.append(XmlEscapers.xmlContentEscaper()
                        .escape(text.substring(currentIndex, text.length())));
            }
            sb.append("</span>");
        }
        return sb.toString();
    }

    /**
     * 07版本复杂数据
     *
     * @param rich
     * @return
     */
    private String getXSSFRichString(XSSFRichTextString rich) {
        int nums = rich.numFormattingRuns();
        StringBuilder sb = new StringBuilder();
        String text = rich.toString();
        int currentIndex = 0, lastIndex = 0;
        for (int i = 1; i <= nums; i++) {
            sb.append("<span ");
            try {
                sb.append("class='font_" + getFontIndex(rich.getFontOfFormattingRun(i - 1)));
                sb.append("_");
                sb.append(cssRandom);
                sb.append("'");
            } catch (Exception e) {
            }
            sb.append(">");
            currentIndex = rich.getIndexOfFormattingRun(i) == -1 ? text.length()
                    : rich.getIndexOfFormattingRun(i);
            sb.append(
                    XmlEscapers.xmlContentEscaper().escape(text.substring(lastIndex, currentIndex)));
            sb.append("</span>");
            lastIndex = currentIndex;
        }
        return sb.toString();
    }

    private String getFontIndex(XSSFFont font) {
        return fontCache
                .get(font.getBold() + "_" + font.getItalic() + "_" + font.getFontName() + "_"
                        + font.getFontHeightInPoints() + "_" + font.getColor());
    }
}
