package org.tp.mix.dal.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.tp.mix.dal.model.TOrder;

import java.sql.SQLException;

/**
 * <p>
 *  服务类
 * </p>
 *
 */
public interface ITOrderService extends IService<TOrder> {

    void initEnvironment() throws SQLException;

    void processSuccess() throws SQLException;

}
