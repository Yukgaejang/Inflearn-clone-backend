package com.yukgaejang.inflearnclone.domain.user.dao;

import com.yukgaejang.inflearnclone.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository<User, Long> {
}
