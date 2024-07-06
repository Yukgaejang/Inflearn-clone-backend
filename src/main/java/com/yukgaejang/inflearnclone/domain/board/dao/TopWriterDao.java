package com.yukgaejang.inflearnclone.domain.board.dao;

import com.yukgaejang.inflearnclone.domain.board.domain.TopWriter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TopWriterDao extends JpaRepository<TopWriter, Long> {
    List<TopWriter> findAllByOrderByScoreDesc();
}
