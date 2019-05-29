package org.tp.poi.excel;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;

import java.io.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class ExcelExportUtilTest {

    @Test
    public void testExportExcel() throws ParseException, IOException {
        User user1 = new User(1, "盖伦", "男", DateUtils.parseDate("2018-2-11", new String[]{"yyyy-MM-dd"}));
        User user2 = new User(2, "德邦", "男", DateUtils.parseDate("2018-2-11", new String[]{"yyyy-MM-dd"}));
        User user3 = new User(3, "拉克丝", "女", DateUtils.parseDate("2018-2-11", new String[]{"yyyy-MM-dd"}));
        User user4 = new User(4, "寒冰", "女", DateUtils.parseDate("2018-2-11", new String[]{"yyyy-MM-dd"}));
        Student student1 = new Student();
        student1.setAge(18);

        user2.setHeight(174.5);
        user2.setWeight(130.4);

        List<User> users = new ArrayList();
        user1.setStudent(student1);
        users.add(user1);
        users.add(user2);
        users.add(user3);
        users.add(user4);

        OutputStream os = new FileOutputStream(new File("D:\\testExportExcel.xlsx"));
        ExportExcelUtil.exportExcel("2007-title", users, os, "2007", true);
        os.flush();
        os = new FileOutputStream(new File("D:\\testExportExcel.xls"));
        ExportExcelUtil.exportExcel("2003-title", users, os, "2003", false);
    }
}



