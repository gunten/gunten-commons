package org.tp.mix.flink.table.api.example;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author gunten
 * 2022/5/9
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {

    private String name;

    private Long score;
}
