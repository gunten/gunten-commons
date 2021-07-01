package org.tp.mix.controler;

import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.*;
import org.tp.SmsSender;
import org.tp.annotation.autolog.AutoLog;
import org.tp.annotation.autolog.LogLevelEnum;
import org.tp.mix.event.BillOverdueEvent;
import org.tp.mix.dto.NotPayBillDTO;
import org.tp.mix.vo.User;
import org.tp.mix.UserDao;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Date;
import java.util.List;

/**
 * {@link UserController}
 *
 * @author <a href="mailto:mm_8023@hotmail.com">gunten<a/>
 * @version 1.0.0
 * @see UserController
 * 2018/11/17
 */
@RestController
@Api(tags="用户管理")
@Slf4j
public class UserController implements UserControllerApi{

    @Autowired
    private UserDao userDao;
    @Autowired
    private ApplicationContext applicationContext;

    @Qualifier("aliYunSmsSender")
    @Autowired
    private SmsSender smsSender1;

    @Qualifier("tencentSmsSender")
    @Autowired
    private SmsSender smsSender2;

    @ApiOperation(value="查询用户", notes="测试无请求参数的swagger-ui")
    @RequestMapping(method=RequestMethod.GET)
    public List<User> findAll() {
        return userDao.findAll();
    }


    @Override
    @AutoLog(level = LogLevelEnum.INFO)
    public User add( User user) {
        log.debug("trying add User ;" + user);
        NotPayBillDTO dto = new NotPayBillDTO()
                .setBillDate(new Date())
                .setBillNo("billNo001")
                ;
        applicationContext.publishEvent(new BillOverdueEvent(this,dto));
        return userDao.save(user);
    }

    @ApiOperation(value="修改用户-测试1", notes="测试application/json形式提交参数")
    @RequestMapping(path="/{id}", method=RequestMethod.PUT)
    public User update1(@ApiParam(value="用户id") @PathVariable Long id, @RequestBody User user) {
        User temp = userDao.findOne(id);
        if(temp != null) {
            temp.setName(user.getName());
            temp.setAge(user.getAge());
            return userDao.save(temp);
        }
        return null;
    }

    @ApiOperation(value="修改用户-测试2", notes="测试application/x-www-form-urlencoded形式提交参数")
    @RequestMapping(path="update2", method=RequestMethod.PUT)
    @ApiImplicitParams({
            @ApiImplicitParam(value="用户id", name="id", required=false, dataType="Integer", paramType="form"),
            @ApiImplicitParam(value="用户名", name="name", required=true, dataType="String", paramType="form"),
            @ApiImplicitParam(value="年龄", name="age", required=false, dataType="Integer", paramType="form"),
    })
    public User update2(@ApiIgnore @ModelAttribute User user) {
        User temp = userDao.findOne(user.getId());
        if(temp != null) {
            temp.setName(user.getName());
            temp.setAge(user.getAge());
            return userDao.save(temp);
        }
        return null;
    }


    @ApiOperation(value="根据id查询用户", notes="测试path参数提交")
    @GetMapping(path="/{id}")
    public User findById(@ApiParam(value="用户id") @PathVariable Long id) {
        return userDao.findOne(id);
    }

    @ApiOperation(value="根据id删除用户", notes="测试path参数提交")
    @DeleteMapping(path="/{id}")
    public Boolean del(@ApiParam(value="用户id") @PathVariable Long id) {
        userDao.delete(id);
        return Boolean.TRUE;
    }

    @GetMapping("/sendSms/{context}")
    public String sendSms(@PathVariable(value = "context") String context) {
        smsSender1.send(context);
        smsSender2.send(context);
        return "send " + context;
    }


}
