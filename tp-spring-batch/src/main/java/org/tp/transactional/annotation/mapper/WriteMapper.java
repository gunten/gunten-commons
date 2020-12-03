package org.tp.transactional.annotation.mapper;

import org.tp.transactional.annotation.entity.WriteData;

/**
 * @Date 2020/10/28 3:14 下午
 */
public interface WriteMapper {

    int setTargetData(WriteData data);
}
