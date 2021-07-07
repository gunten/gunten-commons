package org.tp.transactional.annotation.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tp.transactional.annotation.entity.UserEntity;
import org.tp.transactional.annotation.mapper.UserMapper;

import java.util.Date;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job job;

    @GetMapping("/jobLaunche")
    public void jobLaunche() {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addDate("date", new Date())
                    .toJobParameters();
            // 通过调用 JobLauncher 中的 run 方法启动一个批处理
            jobLauncher.run(job, jobParameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/getUsers")
    public List<UserEntity> getUsers() {
        List<UserEntity> users = userMapper.getAll();
        return users;
    }

    @RequestMapping("/dynamicGetUser")
    public void dynamicGetUser() {
       /* userMapper.dynamicGetUser(new ResultHandler<UserEntity>() {
            @Override
            public void handleResult(ResultContext<? extends UserEntity> resultContext) {
                UserEntity repDto = resultContext.getResultObject();
                System.out.println(repDto);
            }
        });*/

        // or
        userMapper.dynamicGetUser(resultContext -> {
            UserEntity repDto = resultContext.getResultObject();
            System.out.println(repDto);
        });

    }

    @RequestMapping("/getUser")
    public UserEntity getUser(Long id) {
        UserEntity user = userMapper.getOne(id);
        return user;
    }

    @RequestMapping("/add")
    public void save(UserEntity user) {
        userMapper.insert(user);
    }

    @RequestMapping(value = "update")
    public void update(UserEntity user) {
        userMapper.update(user);
    }

    @RequestMapping(value = "/delete/{id}")
    public void delete(@PathVariable("id") Long id) {
        userMapper.delete(id);
    }


}