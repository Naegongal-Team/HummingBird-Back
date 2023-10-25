package com.negongal.hummingbird.domain.performance.dao;

import static com.negongal.hummingbird.domain.performance.domain.QPerformance.performance;
import static com.negongal.hummingbird.domain.performance.domain.QPerformanceDate.performanceDate;
import static com.negongal.hummingbird.domain.performance.domain.QTicketing.ticketing;

import com.negongal.hummingbird.domain.performance.dto.PerformanceDto;
import com.negongal.hummingbird.domain.performance.dto.QPerformanceDto;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
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
    public Page<PerformanceDto> findAllCustom(Pageable pageable) {
        List<PerformanceDto> content = null;

        Long count = -1L;

        if(pageable.getSort().isSorted()) {
            Order order = pageable.getSort().stream().findAny().get();
            if(order.getProperty().equals("ticketing")) {
                content = findAllOrderByTicketingStartDate(pageable);
                count = countTicketingPerformanceList();
            }
            else if(order.getProperty().equals("date")) {
                content = findAllOrderByStartDate(pageable);
            }
            else if(order.getProperty().equals("heart")) { // 인기 있는 공연순 추가 예정
                content = findAllOrderByHeart(pageable);
            }
        }
        else content = findAllOrderByStartDate(pageable);

        if(count == -1L) count = countDatePerformanceList();

        return new PageImpl<>(content, pageable, count);
    }

    @Override
    public List<PerformanceDto> findSeveral(int size, String sort) {
        LocalDateTime currentDate = LocalDateTime.now();

        JPQLQuery<PerformanceDto> query = queryFactory
                .select(new QPerformanceDto(
                        performance.id, performance.name, performance.artistName, performance.photo, performanceDate.startDate.min()))
                .from(performance)
                .leftJoin(performance.dateList, performanceDate)
                .where(performanceDate.startDate.gt(currentDate))
                .groupBy(performance);

        if ("heart".equals(sort)) {
            query.orderBy(performance.heartList.size().desc());
        } else {
            query.orderBy(performanceDate.startDate.min().asc());
        }

        return query.limit(size).fetch();
    }


    public List<PerformanceDto> findAllOrderByStartDate(Pageable pageable) {
        LocalDateTime currentDate = LocalDateTime.now();
        return queryFactory
                .select(new QPerformanceDto(
                        performance.id, performance.name, performance.artistName, performance.photo, performanceDate.startDate.min()))
                .from(performance)
                .leftJoin(performance.dateList, performanceDate)
                .where(performanceDate.startDate.gt(currentDate)) // 현재 날짜 이후만 join
                .groupBy(performance)
                .orderBy(performanceDate.startDate.min().asc())
                .offset(pageable.getOffset())   // 페이지 번호
                .limit(pageable.getPageSize())  // 페이지 사이즈
                .fetch();
    }

    public List<PerformanceDto> findAllOrderByTicketingStartDate(Pageable pageable) {
        LocalDateTime currentDate = LocalDateTime.now();
        return queryFactory
                .select(new QPerformanceDto(
                        performance.id, performance.name, performance.artistName, performance.photo, ticketing.startDate.min()))
                .from(performance)
                .leftJoin(performance.ticketingList, ticketing)
                .where(ticketing.startDate.gt(currentDate)) // 현재 날짜 이후만 join
                .groupBy(performance)
                .orderBy(ticketing.startDate.min().asc())
                .offset(pageable.getOffset())   // 페이지 번호
                .limit(pageable.getPageSize())  // 페이지 사이즈
                .fetch();
    }

    public List<PerformanceDto> findAllOrderByHeart(Pageable pageable) {
        LocalDateTime currentDate = LocalDateTime.now();
        return queryFactory
                .select(new QPerformanceDto(
                        performance.id, performance.name, performance.artistName, performance.photo, performanceDate.startDate.min()))
                .from(performance)
                .leftJoin(performance.dateList, performanceDate)
                .where(performanceDate.startDate.gt(currentDate)) // 현재 날짜 이후만 join
                .groupBy(performance)
                .orderBy(performance.heartList.size().desc())
                .offset(pageable.getOffset())   // 페이지 번호
                .limit(pageable.getPageSize())  // 페이지 사이즈
                .fetch();
    }

    public Long countDatePerformanceList() {
        LocalDateTime currentDate = LocalDateTime.now();
        return queryFactory
                .select(performance.count())
                .from(performance)
                .leftJoin(performance.dateList, performanceDate)
                .where(performanceDate.startDate.gt(currentDate))
                .groupBy(performance)
                .fetchCount();
    }

    public Long countTicketingPerformanceList() {
        LocalDateTime currentDate = LocalDateTime.now();
        return queryFactory
                .select(performance.count())
                .from(performance)
                .leftJoin(performance.ticketingList, ticketing)
                .where(ticketing.startDate.gt(currentDate))
                .groupBy(performance)
                .fetchCount();
    }
}
