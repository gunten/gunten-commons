package org.tp.excel.easyexcel;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @date 2023年02月24日 9:36
 */
@Data
public class EasyExcelTable implements Serializable {

    private static final long serialVersionUID = -7586135087850866599L;

    /**
     * 表头
     */
    private List<List<String>> headList;

    /**
     * 数据
     */
    private List<List<String>> dataList;
}
