package com.yukgaejang.inflearnclone.domain.comment.domain;

import com.yukgaejang.inflearnclone.domain.board.domain.Board;
import com.yukgaejang.inflearnclone.domain.user.domain.User;
import com.yukgaejang.inflearnclone.domain.user.dto.LoginType;
import com.yukgaejang.inflearnclone.global.config.QueryDslConfig;
import jakarta.persistence.EntityManager;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({QueryDslConfig.class})
@DisplayName("Comment 엔티티 테스트")
class CommentTest {

    @Autowired
    EntityManager em;

    User user;

    Board board;

    @BeforeEach
    public void setUp() {
        user = User.builder()
            .email("테스트 이메일")
            .nickname("테스트 닉네임")
            .loginType(LoginType.DEFAULT)
            .build();

        board = Board.builder()
            .title("테스트 제목")
            .content("테스트 내용")
            .category("테스트 카테고리")
            .user(user)
            .build();
    }


    @Test
    @DisplayName("저장 성공 테스트")
    public void save() {
        // given
        Comment comment = Comment.builder()
            .content("테스트 내용")
            .board(board)
            .user(user)
            .build();

        // when
        em.persist(comment);

        // then
        Assertions.assertTrue(em.contains(comment));

    }

    @Test
    @DisplayName("저장 실패 테스트 - 내용이 null인 경우")
    public void shouldThrowExceptionContentNegative() {
        // given
        Comment comment = Comment.builder()
            .content(null)
            .board(board)
            .user(user)
            .build();

        // when

        // then
        Assertions.assertThrowsExactly(ConstraintViolationException.class,
            () -> em.persist(comment));
    }

}