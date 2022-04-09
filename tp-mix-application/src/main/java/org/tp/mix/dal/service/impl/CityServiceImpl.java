package org.tp.mix.dal.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.tp.mix.dal.mapper.CityMapper;
import org.tp.mix.dal.model.City;
import org.tp.mix.dal.service.ICityService;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author
 * @since 2021-06-26
 */
@Service
public class CityServiceImpl extends ServiceImpl<CityMapper, City> implements ICityService {

}
