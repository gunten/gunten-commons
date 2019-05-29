package org.tp.json.fastjson;

import lombok.Data;

import java.util.List;

/**
 * @author <a href="mailto:mm_8023@hotmail.com">gunten<a/>
 * 2018/11/29
 */
@Data
public class Person {
    private String name;
    private int age;
    private boolean verified;
    private List marks;

}
