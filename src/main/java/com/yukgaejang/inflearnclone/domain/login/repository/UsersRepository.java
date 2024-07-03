package com.yukgaejang.inflearnclone.domain.login.repository;

import com.yukgaejang.inflearnclone.domain.login.entity.Users;
import com.yukgaejang.inflearnclone.domain.user.domain.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Long> {
    Optional<Users> findOneWithAuthoritiesByEmail(String email);
}
