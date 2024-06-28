package com.yukgaejang.inflearnclone.domain.user.domain;

import com.yukgaejang.inflearnclone.domain.model.BaseEntity;
import com.yukgaejang.inflearnclone.domain.user.dto.LoginType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User extends BaseEntity {

    @Column(unique = true)
    private String nickname;

    @Column(unique = true)
    private String email;

    private LoginType loginType;

    @Builder
    public User(String nickname, String email, LoginType loginType) {
        this.nickname = nickname;
        this.email = email;
        this.loginType = loginType;
    }
}
