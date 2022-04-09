package org.tp.transactional.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Date 2020/10/28 2:16 下午
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WriteData {

    private Integer id;

    private String no;

    private String name;
}
