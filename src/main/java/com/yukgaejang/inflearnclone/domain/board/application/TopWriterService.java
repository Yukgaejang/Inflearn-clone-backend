package com.yukgaejang.inflearnclone.domain.board.application;

import com.yukgaejang.inflearnclone.domain.board.dao.BoardDao;
import com.yukgaejang.inflearnclone.domain.board.dao.TopWriterDao;
import com.yukgaejang.inflearnclone.domain.board.dto.TopWriterDto;
import com.yukgaejang.inflearnclone.domain.board.domain.Board;
import com.yukgaejang.inflearnclone.domain.board.domain.TopWriter;
import com.yukgaejang.inflearnclone.domain.user.dao.UserDao;
import com.yukgaejang.inflearnclone.domain.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TopWriterService {

    @Autowired
    private BoardDao boardDao;

    @Autowired
    private TopWriterDao topWriterDao;

    @Autowired
    private UserDao userDao;

    public void calculateWeeklyTopWriters() {

        // 기존 TopWriter 데이터 삭제
        topWriterDao.deleteAll();

        LocalDateTime now = LocalDateTime.now();
        // 현재 기준 지난 주 날짜 계산
        LocalDateTime startOfLastWeek = now.minusWeeks(1).with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDateTime endOfLastWeek = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY)).toLocalDate().atTime(23, 59, 59);

        List<User> users = userDao.findAll();

        List<TopWriter> topWriters = users.stream().map(user -> {
                    List<Board> boards = boardDao.findBoardsByUserAndCreatedAtBetween(user.getId(), startOfLastWeek, endOfLastWeek);
                    long score = boards.stream().mapToLong(board -> 1 + board.getViewCount() + board.getLikeCount()+board.getCommentCount()).sum();
                    return TopWriter.builder().score(score).user(user).build();
                }).sorted((tw1, tw2) -> Long.compare(tw2.getScore(), tw1.getScore()))
                .limit(7)
                .collect(Collectors.toList());

        topWriterDao.saveAll(topWriters);
    }

    public List<TopWriterDto> getTopWriters() {
        return topWriterDao.findAllByOrderByScoreDesc().stream()
                .map(writer -> new TopWriterDto(writer.getScore(), writer.getUser().getNickname()))
                .collect(Collectors.toList());
    }
}
