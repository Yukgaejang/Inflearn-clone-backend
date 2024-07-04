package com.yukgaejang.inflearnclone.domain.login.repository;

import com.yukgaejang.inflearnclone.domain.login.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityDao extends JpaRepository<Authority, String> {
}