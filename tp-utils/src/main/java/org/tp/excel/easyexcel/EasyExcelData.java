package org.tp.excel.easyexcel;

import lombok.Data;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * 一个 sheet 多个表格
 */
@Data
public class EasyExcelData implements Serializable {

    private static final long serialVersionUID = 3061860445616544487L;

    /**
     * 文件名，如果导出到本地，需提供完整路径，如果web导出，需提供文件名
     */
    private String fileName;

    /**
     * sheet名
     */
    private String sheetName;

    /**
     * table数据
     */
    private List<EasyExcelTable> table;

    /**
     * 默认值
     */
    public static EasyExcelData defaultValue() {
        EasyExcelData excelData = new EasyExcelData();
        excelData.setFileName("excel");
        excelData.setSheetName("Sheet1");
        excelData.setTable(Collections.emptyList());
        return excelData;
    }
}
