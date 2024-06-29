package com.yukgaejang.inflearnclone.domain.board.domain;

import com.yukgaejang.inflearnclone.domain.model.BaseEntity;
import com.yukgaejang.inflearnclone.domain.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board extends BaseEntity {

    @Column(nullable = false, length = 255)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false, length = 100)
    private String category;

    @Column(nullable = false)
    private Long likeCount = 0L;

    @Column(nullable = false)
    private Long viewCount = 0L;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder
    public Board(String title, String content, String category, User user) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.user = user;
    }

}
