package com.yukgaejang.inflearnclone.domain.login.entity;

import com.yukgaejang.inflearnclone.domain.model.BaseEntity;
import java.util.Arrays;
import lombok.*;
import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "`user`")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Users extends BaseEntity {
    @Column(name = "email", length = 50, unique = true)
    private String email;

    @Column(name = "password", length = 100)
    private String password;

    @Column(name = "nickname", length = 50)
    private String nickname;
}