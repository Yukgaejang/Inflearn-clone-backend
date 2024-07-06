package com.yukgaejang.inflearnclone.domain.board.config;

import com.yukgaejang.inflearnclone.domain.board.application.TopWriterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TopScheduler {

    @Autowired
    private TopWriterService topWriterService;

    @Scheduled(cron = "0 0 0 * * SUN") // 매주 일요일 00:00에 실행
    public void scheduleWeeklyTopWritersCalculation() {
        topWriterService.calculateWeeklyTopWriters();
    }
}
