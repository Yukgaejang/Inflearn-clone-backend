package com.yukgaejang.inflearnclone.domain.board.domain;

import com.yukgaejang.inflearnclone.domain.comment.domain.Comment;
import com.yukgaejang.inflearnclone.domain.model.BaseEntity;
import com.yukgaejang.inflearnclone.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
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

    @Column(nullable = false)
    private Long commentCount = 0L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User user;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "tag_post",
            joinColumns = @JoinColumn(name = "board_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    @Builder
    public Board(String title, String content, String category, User user, Set<Tag> tags) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.user = user;
        this.tags = tags;
        this.likeCount = 0L;
        this.viewCount = 0L;
        this.commentCount = 0L;
    }

    public void setUser(User user) {
        this.user = user;
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
        this.commentCount = 0L;
    }

    // Post 수정
    public void updatePost(String title, String content, String category, Set<Tag> tags) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.tags = tags;
    }

    // 좋아요 수 증가
    public void incrementLikeCount() {
        this.likeCount++;
    }

    // 좋아요 수 감소
    public void decrementLikeCount() {
        if (this.likeCount > 0) {
            this.likeCount--;
        }
    }

    //조회수 증가
    public void incrementViewCount() {
        this.viewCount++;
    }

    // 댓글 수 증가
    public void incrementCommentCount() {
        this.commentCount++;
    }

    // 댓글 수 감소
    public void decrementCommentCount() {
        if (this.commentCount > 0) {
            this.commentCount--;
        }
    }

    public void assignUser(User user) {
        this.user = user;
    }
}
