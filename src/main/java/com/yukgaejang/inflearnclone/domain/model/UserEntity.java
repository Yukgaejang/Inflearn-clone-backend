package com.yukgaejang.inflearnclone.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "TBL_MEMBER")
public class UserEntity extends BaseEntity {

    @Column(name = "ROLE")
    private String role;

    @Column(name = "NAME")
    private String name;

    @Column(name = "USER_NAME")
    private String userName;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "PROFILE_IMAGE")
    private String profileImage;
}