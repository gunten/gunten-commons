package org.tp.easy.excel;

import lombok.Data;

import java.util.Date;

/**
 * 导出实体
 * @author gunten
 * 2018/2/10.
 */
@Data
public class User extends People{

    private int id;

    @Excel(name = "姓名",index = 1)
    private String name;
    @Excel(name = "性别",index = 2)
    private String sex;
    @Excel(name = "生日",index = 3)
    private Date birthDay;
    @Excel(isDIYClass = true,index = 4)
    private Student student;



    public User(int id, String name, String sex, Date birthDay) {
        this.id = id;
        this.name = name;
        this.sex = sex;
        this.birthDay = birthDay;
    }

}

