package com.negongal.hummingbird.repository;

import static com.negongal.hummingbird.domain.QPerformance.performance;
import static com.negongal.hummingbird.domain.QPerformanceDate.performanceDate;
import static com.negongal.hummingbird.domain.QTicketing.ticketing;

import com.negongal.hummingbird.domain.Performance;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Order;

@RequiredArgsConstructor
public class PerformanceRepositoryCustomImpl implements PerformanceRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Performance> findAll(Pageable pageable) {
        List<Performance> content = null;

        if(pageable.getSort().isSorted()) {
            Order order = pageable.getSort().stream().findAny().get();
            if(order.getProperty().equals("ticketing")) {
                content = findAllOrderByTicketingStartDate(pageable);
            }
            else if(order.getProperty().equals("date")) {
                content = findAllOrderByStartDate(pageable);
            }
//            else if(order.getProperty().equals("like")) { // 인기 있는 공연순 추가 예정
//
//            }
        }
        else content = findAllOrderByStartDate(pageable);

        Long count = queryFactory
                .select(performance.count())
                .from(performance)
                .fetchOne();

        return new PageImpl<>(content, pageable, count);
    }

    @Override
    public List<Performance> findSeveral(int size) {
        return queryFactory
                .select(performance)
                .from(performance)
                .leftJoin(performance.dateList, performanceDate)
                .groupBy(performance)
                .orderBy(performanceDate.startDate.min().asc())
                .limit(size)
                .fetch();
    }

    public List<Performance> findAllOrderByStartDate(Pageable pageable) {
        return queryFactory
                .select(performance)
                .from(performance)
                .leftJoin(performance.dateList, performanceDate)
                .groupBy(performance)
                .orderBy(performanceDate.startDate.min().asc())
                .offset(pageable.getOffset())   // 페이지 번호
                .limit(pageable.getPageSize())  // 페이지 사이즈
                .fetch();
    }

    public List<Performance> findAllOrderByTicketingStartDate(Pageable pageable) {
        return queryFactory
                .select(performance)
                .from(performance)
                .leftJoin(performance.ticketing, ticketing)
                .groupBy(performance)
                .orderBy(ticketing.startDate.min().asc())
                .offset(pageable.getOffset())   // 페이지 번호
                .limit(pageable.getPageSize())  // 페이지 사이즈
                .fetch();
    }
}
