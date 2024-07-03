package com.yukgaejang.inflearnclone.domain.board.dao;

import com.yukgaejang.inflearnclone.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoardUserDao extends JpaRepository<User, Long> {
}
