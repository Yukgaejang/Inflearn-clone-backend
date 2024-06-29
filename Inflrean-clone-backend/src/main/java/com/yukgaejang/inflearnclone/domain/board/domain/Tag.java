package com.yukgaejang.inflearnclone.domain.board.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToMany(mappedBy = "tags")
    private Set<Board> boards = new HashSet<>();

    private Tag(String name) {
        this.name = name;
    }

    // Tag 생성
    public static Tag createTag(String name) {
        return new Tag(name);
    }
}
