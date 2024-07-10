package com.yukgaejang.inflearnclone.domain.board.application;

import com.yukgaejang.inflearnclone.domain.board.dao.BoardDao;
import com.yukgaejang.inflearnclone.domain.board.dto.TopPostDto;
import com.yukgaejang.inflearnclone.domain.board.domain.Board;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TopPostService {

    @Autowired
    private BoardDao boardDao;

    public List<TopPostDto> getTopPost() {
        List<Board> boards = boardDao.findAll();

        return boards.stream()
                .map(board -> {
                    Long viewCount = boardDao.countViewsForBoard(board.getId());
                    Long heartCount = boardDao.countHeartsForBoard(board.getId());
                    Long commentCount = boardDao.countCommentsForBoard(board.getId());
                    Long totalScore = viewCount + heartCount + commentCount;
                    return new TopPostDto(board.getId(), board.getTitle(), board.getUser().getNickname(), totalScore);
                })
                .sorted((post1, post2) -> post2.getTotalScore().compareTo(post1.getTotalScore()))
                .limit(5)
                .collect(Collectors.toList());
    }
}
