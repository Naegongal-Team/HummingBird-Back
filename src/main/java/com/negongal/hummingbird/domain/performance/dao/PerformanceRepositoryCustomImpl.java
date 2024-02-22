package com.negongal.hummingbird.domain.performance.dao;

import static com.negongal.hummingbird.domain.performance.domain.QPerformance.performance;
import static com.negongal.hummingbird.domain.performance.domain.QPerformanceDate.performanceDate;
import static com.negongal.hummingbird.domain.performance.domain.QTicketing.ticketing;

import com.negongal.hummingbird.domain.performance.dto.response.PerformanceDto;
import com.negongal.hummingbird.domain.performance.dto.response.QPerformanceDto;
import com.negongal.hummingbird.domain.performance.dto.request.PerformanceSearchRequestDto;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Order;

@Slf4j
@RequiredArgsConstructor
public class PerformanceRepositoryCustomImpl implements PerformanceRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private static final String START_DATE = "date";
    private static final String TICKETING = "ticketing";
    private static final String HEART_COUNT = "heart";

    @Override
    public Page<PerformanceDto> search(PerformanceSearchRequestDto requestDto, Pageable pageable) {
        long offset = pageable.getOffset();
        int pageSize = pageable.getPageSize();
        String sort = getSort(pageable);

        JPQLQuery<PerformanceDto> dtoQuery = getSortDtoQuery(sort);
        dtoQuery.where(performance.artist.name.containsIgnoreCase(requestDto.getArtistName()));
        Long count = countSearchPerformanceList(sort, requestDto.getArtistName()).stream().count();

        dtoQuery.offset(offset);
        dtoQuery.limit(pageSize);

        return new PageImpl<>(dtoQuery.fetch(), pageable, count);
    }

    @Override
    public Page<PerformanceDto> findAllCustom(Pageable pageable) {
        long offset = pageable.getOffset();
        int pageSize = pageable.getPageSize();
        String sort = getSort(pageable);

        JPQLQuery<PerformanceDto> dtoQuery = getSortDtoQuery(sort);
        Long count = countPerformanceList(sort).stream().count();

        dtoQuery.offset(offset);
        dtoQuery.limit(pageSize);

        return new PageImpl<>(dtoQuery.fetch(), pageable, count);
    }

    @Override
    public List<PerformanceDto> findSeveral(int size, String sort) {
        JPQLQuery<PerformanceDto> query = getSortDtoQuery(sort);
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
                .innerJoin(performance.performanceDates, performanceDate)
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

    public JPQLQuery<Long> countSearchPerformanceList(String sort, String search) {
        JPQLQuery<PerformanceDto> dtoQuery = null;
        if(sort.equals(TICKETING)) {
            dtoQuery = getTicketingDtoQuery();
        }
        else {
            dtoQuery = getBasicDtoQuery();
        }
        dtoQuery.where(performance.artist.name.containsIgnoreCase(search));
        return dtoQuery.select(performance.count());
    }

    public JPQLQuery<PerformanceDto> getSortDtoQuery(String sort) {
        if(sort.equals(TICKETING)) {
            JPQLQuery<PerformanceDto> dtoQuery = getTicketingDtoQuery();
            dtoQuery.orderBy(ticketing.startDate.min().asc());
            return dtoQuery;
        }

        JPQLQuery<PerformanceDto> dtoQuery = getBasicDtoQuery();
        if (sort.equals(HEART_COUNT)) {
            dtoQuery.orderBy(performance.performanceHearts.size().desc());
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
                .leftJoin(performance.performanceDates, performanceDate)
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
                .leftJoin(performance.ticketings, ticketing)
                .where(ticketing.startDate.goe(currentDate)) // 현재 날짜 이후만 join
                .groupBy(performance.id, performance.name, performance.artist.name, performance.photo);
    }

}
