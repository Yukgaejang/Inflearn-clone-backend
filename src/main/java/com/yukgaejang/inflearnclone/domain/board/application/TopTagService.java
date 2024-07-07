package com.yukgaejang.inflearnclone.domain.board.application;

import com.yukgaejang.inflearnclone.domain.board.dao.BoardDao;
import com.yukgaejang.inflearnclone.domain.board.dao.TopTagDao;
import com.yukgaejang.inflearnclone.domain.board.domain.Tag;
import com.yukgaejang.inflearnclone.domain.board.domain.TopTag;
import com.yukgaejang.inflearnclone.domain.board.dto.TopTagDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TopTagService {

    @Autowired
    private BoardDao boardDao;

    @Autowired
    private TopTagDao topTagDao;

    @Transactional
    public void calculateWeeklyTopTags() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDate = now.minusWeeks(1);
        LocalDateTime endDate = now;

        List<Object[]> tagUsageStats = boardDao.findTagUsageStatsBetween(startDate, endDate);

        topTagDao.deleteAll();

        List<TopTag> topTags = tagUsageStats.stream()
                .map(tagStat -> new TopTag((Long) tagStat[1], (Tag) tagStat[0]))
                .limit(10)
                .collect(Collectors.toList());

        topTagDao.saveAll(topTags);
    }

    public List<TopTagDto> getTopTags() {
        return topTagDao.findAllByOrderByTotalDesc().stream()
                .map(topTag -> new TopTagDto(topTag.getTag().getName(), topTag.getTotal()))
                .collect(Collectors.toList());
    }
}
