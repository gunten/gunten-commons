package org.tp.zk.serial;

import java.util.Calendar;

/**
 * 分布式流水头部报文
 *
 * 按照月从0开始生成流水号
 *
 */
public class MonthIndividualNode implements IndividualNode {

    /**
     *
     * @return like "201903"
     */
    @Override
    public String createNode() {
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH) + 1;
        return cal.get(Calendar.YEAR) + "" + (month < 10 ? "0" + month : month);
    }

}