package com.yukgaejang.inflearnclone.domain.dao;

import com.yukgaejang.inflearnclone.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository<User, Long> {
    User findByEmail(String email);
}