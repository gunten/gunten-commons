package org.tp.controller;

import org.tp.entity.User;
import org.tp.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 */
@RestController
public class UserController {

    @Resource
    private UserService userService;

    @GetMapping("/getAllUser")
    public Object getAllUser() {
        return userService.getAllUser();
    }

    @GetMapping("/getAllGroup")
    public Object getAllGroup() {
        return userService.getAllGroup();
    }

    @GetMapping("/getUserGroup")
    public Object getUserGroup(String groupId) {
        return userService.getUserGroup(groupId);
    }

    @PostMapping("/addUser")
    public Object addUser(@RequestBody User user) {
        return userService.addUser(user);
    }


}
