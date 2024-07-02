package com.yukgaejang.inflearnclone.domain.board.dao;

import com.yukgaejang.inflearnclone.domain.board.domain.Heart;
import com.yukgaejang.inflearnclone.domain.board.domain.Board;
import com.yukgaejang.inflearnclone.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HeartDao extends JpaRepository<Heart, Long> {
    Optional<Heart> findByBoardAndUser(Board board, User user);
    void deleteByBoard(Board board);
}