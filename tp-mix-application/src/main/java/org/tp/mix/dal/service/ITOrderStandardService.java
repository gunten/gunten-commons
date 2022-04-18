package org.tp.mix.dal.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.tp.mix.dal.model.TOrderStandard;

import java.sql.SQLException;

/**
 * <p>
 *  服务类
 * </p>
 *
 */
public interface ITOrderStandardService extends IService<TOrderStandard> {
    void initEnvironment() throws SQLException;

    void processSuccess() throws SQLException;
}
