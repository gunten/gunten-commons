package org.tp.mix.dal.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.tp.mix.dal.model.TOrderBoundaryRange;

import java.sql.SQLException;

/**
 * <p>
 *  服务类
 * </p>
 *
 */
public interface ITOrderBoundaryRangeService extends IService<TOrderBoundaryRange> {

    void initEnvironment() throws SQLException;

    void processSuccess() throws SQLException;
}
