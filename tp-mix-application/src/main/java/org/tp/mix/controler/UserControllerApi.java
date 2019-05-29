package org.tp.mix.controler;

import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.tp.mix.vo.User;

import javax.validation.Valid;

/**
 * @author <a href="mailto:mm_8023@hotmail.com">gunten<a/>
 * 2019/1/10
 */
@RequestMapping(path="user")
@Validated
public interface UserControllerApi {

    @ApiOperation(value="增加用户-测试1", notes="测试application/x-www-form-urlencoded形式提交参数")
    @PostMapping("/add")
    User add(@Valid @RequestBody User user);
}
