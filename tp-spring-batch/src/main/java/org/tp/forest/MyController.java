package org.tp.forest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author
 * 2022/4/22
 */
@RestController
public class MyController {

    @Autowired
    private MyService service;

    @GetMapping("forestCall")
    public String forestCall(){
        return service.testClient();
    }
}
