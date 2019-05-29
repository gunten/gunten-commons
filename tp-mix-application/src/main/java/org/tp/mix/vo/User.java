package org.tp.mix.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.tp.annotation.sensitive.SensitiveInfo;
import org.tp.annotation.sensitive.SensitiveType;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;


/**
 * {@link User}
 *
 * @author <a href="mailto:mm_8023@hotmail.com">gunten<a/>
 * @version 1.0.0
 * @see User
 * 2018/11/17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

    private static final long serialVersionUID = 8661171727017290255L;

    @ApiModelProperty(hidden=true)
    private long id;

    @SensitiveInfo(type = SensitiveType.CHINESE_NAME)
    @ApiModelProperty(value = "用户名称", required=true)
    @NotBlank(message = "姓名不能为空")
    private String name;

    @ApiModelProperty(value = "年龄")
    private Integer age;

    @SensitiveInfo(type = SensitiveType.EMAIL)
    @ApiModelProperty(value = "邮箱")
    private String email;

}
