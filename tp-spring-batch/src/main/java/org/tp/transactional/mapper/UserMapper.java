package org.tp.transactional.mapper;

import java.util.List;

import org.apache.ibatis.session.ResultHandler;
import org.tp.transactional.entity.UserEntity;

public interface UserMapper {

    List<UserEntity> getAll();

    UserEntity getOne(Long id);

    int insert(UserEntity user);

    void update(UserEntity user);

    void delete(Long id);

//    @Select("select * from users limit 200")
//    @Options(resultSetType = ResultSetType.FORWARD_ONLY, fetchSize = 10)
//    @ResultType(UserEntity.class)
    void dynamicGetUser(ResultHandler<UserEntity> handler);

}