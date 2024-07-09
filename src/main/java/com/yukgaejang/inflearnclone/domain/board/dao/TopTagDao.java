package com.yukgaejang.inflearnclone.domain.board.dao;

import com.yukgaejang.inflearnclone.domain.board.domain.TopTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TopTagDao extends JpaRepository<TopTag, Long> {
    List<TopTag> findAllByOrderByTotalDesc();
}
