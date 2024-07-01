package com.yukgaejang.inflearnclone.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "USER")
public class User extends BaseEntity {
    @Column(name = "NICKNAME")
    private String nickname;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "PROFILE_IMAGE")
    private String profileImage;

    @Column(name = "SOCIAL_TYPE")
    private String socialType;
}