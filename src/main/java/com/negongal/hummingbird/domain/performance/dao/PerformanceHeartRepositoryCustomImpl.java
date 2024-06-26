package com.negongal.hummingbird.domain.performance.dao;

import static com.negongal.hummingbird.domain.performance.domain.QPerformance.*;
import static com.negongal.hummingbird.domain.performance.domain.QPerformanceDate.*;
import static com.negongal.hummingbird.domain.performance.domain.QPerformanceHeart.*;
import static com.negongal.hummingbird.domain.performance.domain.QTicketing.*;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Order;

import com.negongal.hummingbird.domain.performance.dto.response.PerformanceDto;
import com.negongal.hummingbird.domain.performance.dto.response.QPerformanceDto;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PerformanceHeartRepositoryCustomImpl implements PerformanceHeartRepositoryCustom {

	private final JPAQueryFactory queryFactory;
	private static final String START_DATE = "date";
	private static final String TICKETING = "ticketing";
	private static final String HEART_INPUT = "heart-input";

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

	public String getSort(Pageable pageable) {
		if (pageable.getSort().isSorted()) {
			Order order = pageable.getSort().stream().findAny().get();
			return order.getProperty();
		}
		return START_DATE;
	}

	public JPQLQuery<Long> countPerformanceList(String sort, Long userId) {
		JPQLQuery<PerformanceDto> dtoQuery = null;
		if (sort.equals(TICKETING)) {
			dtoQuery = getTicketingDtoQuery(userId);
		} else {
			dtoQuery = getBasicDtoQuery(userId);
		}
		return dtoQuery.select(performance.count());
	}

	public JPQLQuery<PerformanceDto> getDtoQuery(String sort, Long userId) {
		if (sort.equals(TICKETING)) {
			JPQLQuery<PerformanceDto> dtoQuery = getTicketingDtoQuery(userId);
			dtoQuery.orderBy(ticketing.startDate.min().asc());
			return dtoQuery;
		}

		JPQLQuery<PerformanceDto> dtoQuery = getBasicDtoQuery(userId);
		if (sort.equals(HEART_INPUT)) {
			dtoQuery.orderBy(performanceHeart.id.desc());
		} else if (sort.equals(START_DATE)) {
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
			.leftJoin(performance.performanceDates, performanceDate)
			.leftJoin(performance.performanceHearts, performanceHeart)
			.where(performanceDate.startDate.goe(currentDate)) // 현재 날짜 이후만 join
			.where(performanceHeart.user.id.eq(userId))
			.groupBy(performance.id, performance.name, performance.photo);
	}

	public JPQLQuery<PerformanceDto> getTicketingDtoQuery(Long userId) {
		LocalDateTime currentDate = LocalDateTime.now();
		return queryFactory
			.select(new QPerformanceDto(
				performance.id, performance.name, performance.artist.name, performance.photo,
				ticketing.startDate.min()))
			.from(performance)
			.leftJoin(performance.ticketings, ticketing)
			.leftJoin(performance.performanceHearts, performanceHeart)
			.where(ticketing.startDate.goe(currentDate)) // 현재 날짜 이후만 join
			.where(performanceHeart.user.id.eq(userId))
			.groupBy(performance.id, performance.name, performance.photo);
	}
}
