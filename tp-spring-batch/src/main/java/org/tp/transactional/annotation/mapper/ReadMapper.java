package org.tp.transactional.annotation.mapper;

import org.tp.transactional.annotation.entity.ReadData;

import java.util.List;

/**
 * @Date 2020/10/28 3:14 下午
 */
public interface ReadMapper {

    List<ReadData> getResourceData();
}
