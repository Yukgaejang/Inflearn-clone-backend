package com.yukgaejang.inflearnclone.domain.board.application;

import com.yukgaejang.inflearnclone.domain.board.dao.BoardDao;
import com.yukgaejang.inflearnclone.domain.board.dto.TopTagDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TopTagService {

    @Autowired
    private BoardDao boardDao;

    public List<TopTagDto> getTopTags() {
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusWeeks(1);

        List<Object[]> topTagsData = boardDao.findTopTagsByCreatedAtBetween(startDate, endDate);

        return topTagsData.stream()
                .map(tagData -> new TopTagDto((String) tagData[0], (Long) tagData[1]))
                .limit(10)
                .collect(Collectors.toList());
    }
}
