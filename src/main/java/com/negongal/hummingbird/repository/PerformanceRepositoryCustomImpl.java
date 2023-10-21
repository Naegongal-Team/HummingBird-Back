package com.negongal.hummingbird.repository;

import static com.negongal.hummingbird.domain.QPerformance.performance;
import static com.negongal.hummingbird.domain.QPerformanceDate.performanceDate;
import static com.negongal.hummingbird.domain.QPerformanceHeart.performanceHeart;
import static com.negongal.hummingbird.domain.QTicketing.ticketing;

import com.negongal.hummingbird.api.dto.PerformanceDto;
import com.negongal.hummingbird.api.dto.QPerformanceDto;
import com.negongal.hummingbird.domain.PerformanceHeart;
import com.negongal.hummingbird.domain.QPerformanceHeart;
import com.querydsl.core.types.Path;
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

        if(pageable.getSort().isSorted()) {
            Order order = pageable.getSort().stream().findAny().get();
            if(order.getProperty().equals("ticketing")) {
                content = findAllOrderByTicketingStartDate(pageable);
            }
            else if(order.getProperty().equals("date")) {
                content = findAllOrderByStartDate(pageable);
            }
            else if(order.getProperty().equals("heart")) { // 인기 있는 공연순 추가 예정
                content = findAllOrderByHeart(pageable);
            }
        }
        else content = findAllOrderByStartDate(pageable);

        LocalDateTime currentDate = LocalDateTime.now();
        Long count = queryFactory
                .select(performance.count())
                .from(performance)
                .leftJoin(performance.dateList, performanceDate)
                .where(performanceDate.startDate.gt(currentDate))
                .fetchOne();

        return new PageImpl<>(content, pageable, count);
    }

    @Override
    public List<PerformanceDto> findSeveral(int size) {
        LocalDateTime currentDate = LocalDateTime.now();
        return queryFactory
                .select(new QPerformanceDto(
                        performance.id, performance.name, performance.artistName, performance.photo, performanceDate.startDate.min()))
                .from(performance)
                .leftJoin(performance.dateList, performanceDate)
                .where(performanceDate.startDate.gt(currentDate)) // 현재 날짜 이후만 join
                .groupBy(performance)
                .orderBy(performanceDate.startDate.min().asc())
                .limit(size)
                .fetch();
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
}
