//package org.tp.transactional.annotation.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import org.tp.transactional.annotation.service.ActivityConsumerService;
//
///**
// * 2021/7/7
// */
//@RestController
//@RequestMapping("/activityService")
//public class ActivityController {
//
//    @Autowired
//    private ActivityConsumerService activityConsumerService;
//
//
//    @GetMapping("/startActivityDemo")
//    public boolean startActivityDemo(){
//        return activityConsumerService.startActivityDemo();
//    }
//}
