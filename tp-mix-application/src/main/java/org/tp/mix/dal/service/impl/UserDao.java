package org.tp.mix.dal.service.impl;

import org.springframework.stereotype.Component;
import org.tp.mix.vo.User;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link UserDao}
 *
 * @author <a href="mailto:mm_8023@hotmail.com">gunten<a/>
 * @version 1.0.0
 * @see UserDao
 * 2018/11/17
 */
@Component
public class UserDao {
    public List<User> findAll() {
        List list =new ArrayList<>();
        list.add(new User(3,"saveuser", 12,"@163.com"));
        return list ;
    }

    public User save(User user) {
//        return new User("saveuser",12);
        return user;
    }

    public User findOne(Long id) {
        return new User(1L,"finduser",12,"abc@163.com");
    }

    public void  delete(Long id) {
        System.out.println("delete user");
    }
}
