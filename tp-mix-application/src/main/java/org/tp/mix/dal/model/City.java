package org.tp.mix.dal.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 *
 * </p>
 *
 * @author
 * @since 2021-06-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class City extends Model {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    @TableField("Name")
    private String name;

    @TableField("CountryCode")
    private String countrycode;

    @TableField("District")
    private String district;

    @TableField("Population")
    private Integer population;


}
