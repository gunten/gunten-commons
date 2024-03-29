package org.tp.mix.controler;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tp.mix.dal.service.ITOrderVolumeRangeService;

import java.sql.SQLException;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 */
@RestController
@RequestMapping("/t-order-volume-range")
public class TOrderVolumeRangeController {
    @Autowired
    ITOrderVolumeRangeService volumeRangeService;
    @GetMapping
    public void init() throws SQLException {
        volumeRangeService.initEnvironment();
        volumeRangeService.processSuccess();
    }
}
