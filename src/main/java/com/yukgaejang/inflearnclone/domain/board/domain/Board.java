package com.yukgaejang.inflearnclone.domain.board.domain;

import com.yukgaejang.inflearnclone.domain.model.BaseEntity;
import com.yukgaejang.inflearnclone.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
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

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "tag_post",
            joinColumns = @JoinColumn(name = "board_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Builder
    public Board(String title, String content, String category, User user, Set<Tag> tags) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.user = user;
        this.tags = tags;
        this.likeCount = 0L;
        this.viewCount = 0L;
        this.updatedAt = null;
    }

    // Post 생성
    public void createPost(User user, String title, String content, String category, Set<Tag> tags) {
        this.user = user;
        this.title = title;
        this.content = content;
        this.category = category;
        this.tags = tags;
        this.likeCount = 0L;
        this.viewCount = 0L;
        this.updatedAt = LocalDateTime.now();
    }

    // Post 수정
    public void updatePost(String title, String content, String category, Set<Tag> tags) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.tags = tags;
        this.updatedAt = LocalDateTime.now();
    }
}
