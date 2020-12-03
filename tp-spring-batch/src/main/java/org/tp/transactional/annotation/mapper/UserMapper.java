package org.tp.transactional.annotation.mapper;

import java.util.List;

import org.tp.transactional.annotation.entity.UserEntity;

public interface UserMapper {

    List<UserEntity> getAll();

    UserEntity getOne(Long id);

    int insert(UserEntity user);

    void update(UserEntity user);

    void delete(Long id);

}