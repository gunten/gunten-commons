package org.tp.easy.excel;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.tp.excel.easyexcel.Excel;

import java.util.Date;

/**
 * 导入实体
 * @author gunten
 * 2018/2/10.
 */
@Data
@NoArgsConstructor
public class Student {

    private String name;
    private String sex;
    private Date birthDay;
    private String className;
    private String address;
    private String phone;
    private String love;
    @Excel(name = "年龄",index = 1)
    private int age;
}

