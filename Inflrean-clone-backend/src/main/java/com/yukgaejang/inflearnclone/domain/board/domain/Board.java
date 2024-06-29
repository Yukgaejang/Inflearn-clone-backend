package com.yukgaejang.inflearnclone.domain.board.domain;

import com.yukgaejang.inflearnclone.domain.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

    @Column(nullable = true)
    private LocalDateTime updatedAt;

    // Post 생성
    public void createPost(User user, String title, String content, String category, Set<Tag> tags) {
        this.user = user;
        this.title = title;
        this.content = content;
        this.category = category;
        this.tags = tags;
        this.likeCount = 0L;
        this.viewCount = 0L;
        this.updatedAt = null;
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

