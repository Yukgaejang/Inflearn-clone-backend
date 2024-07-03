package com.yukgaejang.inflearnclone.domain.board.application;

import com.yukgaejang.inflearnclone.domain.board.dao.HeartDao;
import com.yukgaejang.inflearnclone.domain.board.domain.Board;
import com.yukgaejang.inflearnclone.domain.board.domain.Heart;
import com.yukgaejang.inflearnclone.domain.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class HeartService {

    @Autowired
    private HeartDao heartDao;

    @Transactional
    public boolean toggleHeart(Board board, User user) {
        Optional<Heart> existingHeart = heartDao.findByBoardAndUser(board, user);

        if (existingHeart.isPresent()) {
            heartDao.delete(existingHeart.get());
            board.decrementLikeCount();
            return false;
        } else {
            Heart heart = Heart.builder()
                    .board(board)
                    .user(user)
                    .build();
            heartDao.save(heart);
            board.incrementLikeCount();
            return true;
        }
    }
}
