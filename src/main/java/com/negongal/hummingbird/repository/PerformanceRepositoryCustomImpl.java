package com.negongal.hummingbird.repository;



import com.negongal.hummingbird.domain.Performance;

import com.querydsl.jpa.impl.JPAQueryFactory;

import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PerformanceRepositoryCustomImpl implements PerformanceRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    
    @Override
    public List<Performance> findAllOrderByStartDate() {
        return null;
    }
}
