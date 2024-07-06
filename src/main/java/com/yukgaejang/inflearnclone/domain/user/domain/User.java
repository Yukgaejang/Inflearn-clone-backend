package com.yukgaejang.inflearnclone.domain.user.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yukgaejang.inflearnclone.domain.board.domain.Board;
import com.yukgaejang.inflearnclone.domain.model.BaseEntity;
import com.yukgaejang.inflearnclone.domain.user.dto.LoginType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class User extends BaseEntity {

    @Column(unique = true)
    private String nickname;

    @Column(name = "password", length = 100)
    private String password;

    @Column(unique = true)
    private String email;

    private LoginType loginType;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Board> boards;

    @Builder
    public User(String nickname, String email, LoginType loginType) {
        this.nickname = nickname;
        this.email = email;
        this.loginType = loginType;
    }

    public String getNickname() {
        return nickname;
    }
}
