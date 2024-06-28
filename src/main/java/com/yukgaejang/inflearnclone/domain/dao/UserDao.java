package com.yukgaejang.inflearnclone.domain.dao;

import com.yukgaejang.inflearnclone.domain.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository<UserEntity, Long> {
    UserEntity findByUserName(String username);
}