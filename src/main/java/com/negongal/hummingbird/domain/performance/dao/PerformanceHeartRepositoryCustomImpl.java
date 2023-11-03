package com.negongal.hummingbird.domain.performance.dao;

import static com.negongal.hummingbird.domain.performance.domain.QPerformance.performance;
import static com.negongal.hummingbird.domain.performance.domain.QPerformanceDate.performanceDate;
import static com.negongal.hummingbird.domain.performance.domain.QPerformanceHeart.performanceHeart;
import static com.negongal.hummingbird.domain.performance.domain.QTicketing.ticketing;

import com.negongal.hummingbird.domain.performance.dto.PerformanceDto;
import com.negongal.hummingbird.domain.performance.dto.QPerformanceDto;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Order;

@RequiredArgsConstructor
public class PerformanceHeartRepositoryCustomImpl implements PerformanceHeartRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private static final String START_DATE = "date";
    private static final String TICKETING = "ticketing";
    private static final String HEART_INPUT = "heart-input";

    public String getSort(Pageable pageable) {
        if(pageable.getSort().isSorted()) {
            Order order = pageable.getSort().stream().findAny().get();
            return order.getProperty();
        }
        return START_DATE;
    }

    @Override
    public Page<PerformanceDto> findAllByUserHeart(Pageable pageable, Long userId) {
        long offset = pageable.getOffset();
        int pageSize = pageable.getPageSize();
        String sort = getSort(pageable);

        JPQLQuery<PerformanceDto> dtoQuery = getDtoQuery(sort, userId);
        JPQLQuery<Long> countQuery = countPerformanceList(sort, userId);

        dtoQuery.offset(offset);
        dtoQuery.limit(pageSize);

        return new PageImpl<>(dtoQuery.fetch(), pageable, countQuery.fetchCount());
    }

    public JPQLQuery<Long> countPerformanceList(String sort, Long userId) {
        JPQLQuery<PerformanceDto> dtoQuery = null;
        if(sort.equals(TICKETING)) {
            dtoQuery = getTicketingDtoQuery(userId);
        }
        else {
            dtoQuery = getBasicDtoQuery(userId);
        }
        return dtoQuery.select(performance.count());
    }

    public JPQLQuery<PerformanceDto> getDtoQuery(String sort, Long userId) {
        if(sort.equals(TICKETING)) {
            JPQLQuery<PerformanceDto> dtoQuery = getTicketingDtoQuery(userId);
            dtoQuery.orderBy(ticketing.startDate.min().asc());
            return dtoQuery;
        }

        JPQLQuery<PerformanceDto> dtoQuery = getBasicDtoQuery(userId);
        if (sort.equals(HEART_INPUT)) {
            dtoQuery.orderBy(performanceHeart.id.desc());
        }
        else if(sort.equals(START_DATE)){
            dtoQuery.orderBy(performanceDate.startDate.min().asc());
        }
        return dtoQuery;
    }

    public JPQLQuery<PerformanceDto> getBasicDtoQuery(Long userId) {
        LocalDateTime currentDate = LocalDateTime.now();
        return queryFactory
                .select(new QPerformanceDto(
                        performance.id, performance.name, performance.artist.name, performance.photo,
                        performanceDate.startDate.min()))
                .from(performance)
                .leftJoin(performance.dateList, performanceDate)
                .leftJoin(performance.performanceHeartList, performanceHeart)
                .where(performanceDate.startDate.goe(currentDate)) // 현재 날짜 이후만 join
                .where(performanceHeart.user.userId.eq(userId))
                .groupBy(performance.id, performance.name, performance.photo);
    }

    public JPQLQuery<PerformanceDto> getTicketingDtoQuery(Long userId) {
        LocalDateTime currentDate = LocalDateTime.now();
        return queryFactory
                .select(new QPerformanceDto(
                        performance.id, performance.name, performance.artist.name, performance.photo,
                        ticketing.startDate.min()))
                .from(performance)
                .leftJoin(performance.ticketingList, ticketing)
                .leftJoin(performance.performanceHeartList, performanceHeart)
                .where(ticketing.startDate.goe(currentDate)) // 현재 날짜 이후만 join
                .where(performanceHeart.user.userId.eq(userId))
                .groupBy(performance.id, performance.name, performance.photo);
    }
}
