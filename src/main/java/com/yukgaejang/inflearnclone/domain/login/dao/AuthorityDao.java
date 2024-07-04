package com.yukgaejang.inflearnclone.domain.login.dao;

import com.yukgaejang.inflearnclone.domain.login.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityDao extends JpaRepository<Authority, String> {
}