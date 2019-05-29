package org.tp.poi.excel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author gunten
 * @version 1.0.0
 * @create 2018/7/27 10:20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class People {

    @Excel(name = "身高",index = 5)
    private double height;
    @Excel(name = "体重",index = 6)
    private double weight;

}
