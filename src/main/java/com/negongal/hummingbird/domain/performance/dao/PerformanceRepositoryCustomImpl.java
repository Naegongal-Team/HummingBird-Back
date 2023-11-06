package com.negongal.hummingbird.domain.performance.dao;

import static com.negongal.hummingbird.domain.performance.domain.QPerformance.performance;
import static com.negongal.hummingbird.domain.performance.domain.QPerformanceDate.performanceDate;
import static com.negongal.hummingbird.domain.performance.domain.QTicketing.ticketing;

import com.negongal.hummingbird.domain.performance.dto.PerformanceDto;
import com.negongal.hummingbird.domain.performance.dto.PerformanceSearchRequestDto;
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
    private static final String START_DATE = "date";
    private static final String TICKETING = "ticketing";
    private static final String HEART_COUNT = "heart";

    @Override
    public List<PerformanceDto> search(PerformanceSearchRequestDto requestDto) {
        return queryFactory
                .select(new QPerformanceDto(
                        performance.id, performance.name, performance.artist.name, performance.photo))
                .from(performance)
                .where(performance.artist.name.containsIgnoreCase(requestDto.getArtistName()))
                .fetch();
    }

    @Override
    public Page<PerformanceDto> findAllCustom(Pageable pageable) {
        long offset = pageable.getOffset();
        int pageSize = pageable.getPageSize();
        String sort = getSort(pageable);

        JPQLQuery<PerformanceDto> dtoQuery = getDtoQuery(sort);
        Long count = countPerformanceList(sort).fetchCount();

        dtoQuery.offset(offset);
        dtoQuery.limit(pageSize);

        return new PageImpl<>(dtoQuery.fetch(), pageable, count);
    }

    @Override
    public List<PerformanceDto> findSeveral(int size, String sort) {
        JPQLQuery<PerformanceDto> query = getDtoQuery(sort);
        return query.limit(size).fetch();
    }

    @Override
    public List<PerformanceDto> findByArtist(String artistId, boolean scheduled) {
        LocalDateTime currentDate = LocalDateTime.now();
        return queryFactory
                .select(new QPerformanceDto(
                        performance.id, performance.name, performance.artist.name, performance.photo,
                        performanceDate.startDate.min()))
                .from(performance)
                .innerJoin(performance.dateList, performanceDate)
                .where(performance.artist.id.eq(artistId))
                .groupBy(performance.id, performance.name, performance.artist.name, performance.photo)
                .having(
                        scheduled ? performanceDate.startDate.max().after(currentDate) :
                                performanceDate.startDate.max().before(currentDate)
                ).fetch();
    }

    public String getSort(Pageable pageable) {
        if(pageable.getSort().isSorted()) {
            Order order = pageable.getSort().stream().findAny().get();
            return order.getProperty();
        }
        return START_DATE;
    }

    public JPQLQuery<Long> countPerformanceList(String sort) {
        JPQLQuery<PerformanceDto> dtoQuery = null;
        if(sort.equals(TICKETING)) {
            dtoQuery = getTicketingDtoQuery();
        }
        else {
            dtoQuery = getBasicDtoQuery();
        }
        return dtoQuery.select(performance.count());
    }

    public JPQLQuery<PerformanceDto> getDtoQuery(String sort) {
        if(sort.equals(TICKETING)) {
            JPQLQuery<PerformanceDto> dtoQuery = getTicketingDtoQuery();
            dtoQuery.orderBy(ticketing.startDate.min().asc());
            return dtoQuery;
        }

        JPQLQuery<PerformanceDto> dtoQuery = getBasicDtoQuery();
        if (sort.equals(HEART_COUNT)) {
            dtoQuery.orderBy(performance.performanceHeartList.size().desc());
        }
        else if(sort.equals(START_DATE)){
            dtoQuery.orderBy(performanceDate.startDate.min().asc());
        }
        return dtoQuery;
    }

    public JPQLQuery<PerformanceDto> getBasicDtoQuery() {
        LocalDateTime currentDate = LocalDateTime.now();
        return queryFactory
                .select(new QPerformanceDto(
                        performance.id, performance.name, performance.artist.name, performance.photo,
                        performanceDate.startDate.min()))
                .from(performance)
                .leftJoin(performance.dateList, performanceDate)
                .where(performanceDate.startDate.goe(currentDate)) // 현재 날짜 이후만 join
                .groupBy(performance.id, performance.name, performance.artist.name, performance.photo);
    }

    public JPQLQuery<PerformanceDto> getTicketingDtoQuery() {
        LocalDateTime currentDate = LocalDateTime.now();
        return queryFactory
                .select(new QPerformanceDto(
                        performance.id, performance.name, performance.artist.name, performance.photo,
                        ticketing.startDate.min()))
                .from(performance)
                .leftJoin(performance.ticketingList, ticketing)
                .where(ticketing.startDate.goe(currentDate)) // 현재 날짜 이후만 join
                .groupBy(performance.id, performance.name, performance.artist.name, performance.photo);
    }

}
