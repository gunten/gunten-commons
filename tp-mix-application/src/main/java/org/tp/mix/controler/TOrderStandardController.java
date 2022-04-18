package org.tp.mix.controler;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tp.mix.dal.service.ITOrderStandardService;

import java.sql.SQLException;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 */
@RestController
@RequestMapping("/t-order-standard")
public class TOrderStandardController {

    @Autowired
    ITOrderStandardService orderService;

    @GetMapping
    public void init() throws SQLException {
        orderService.initEnvironment();
        orderService.processSuccess();
    }
}
