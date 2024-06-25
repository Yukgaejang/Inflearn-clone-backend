package com.yukgaejang.inflearnclone.domain.sample.dao;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SampleDaoImpl implements SampleDaoCustom {

    private final JPAQueryFactory queryFactory;

}
