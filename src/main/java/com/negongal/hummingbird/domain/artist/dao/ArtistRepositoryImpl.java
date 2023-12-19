package com.negongal.hummingbird.domain.artist.dao;

import static com.negongal.hummingbird.domain.artist.domain.QArtist.artist;
import static com.negongal.hummingbird.domain.artist.domain.QArtistHeart.artistHeart;

import com.negongal.hummingbird.domain.artist.dto.ArtistDto;
import com.negongal.hummingbird.domain.artist.dto.QArtistDto;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ArtistRepositoryImpl implements ArtistRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<ArtistDto> findAllArtists(Long userId, Pageable pageable) {
        JPAQuery<ArtistDto> results = findAllArtistsQuery(userId, pageable);

        return new PageImpl<>(results.fetch(), pageable, results.fetch().size());
    }

    private JPAQuery<ArtistDto> findAllArtistsQuery(Long userId, Pageable pageable) {
        List<OrderSpecifier> orders = getAllOrderSpecifiers(pageable);
        return jpaQueryFactory
                .select(new QArtistDto(artist.id, artist.name, artist.image, artist.heartCount))
                .from(artist)
                .leftJoin(artist.artistHeartList, artistHeart)
                .where(artistHeart.user.userId.eq(userId)
                        .or(artistHeart.user.isNull()))
                .orderBy(orders.stream().toArray(OrderSpecifier[]::new))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());
    }

    private List<OrderSpecifier> getAllOrderSpecifiers(Pageable pageable) {
        List<OrderSpecifier> orders = new ArrayList<>();
        OrderSpecifier<LocalDateTime> defaultOrderSpecifier = artistHeart.createdDate.desc().nullsLast();
        if (pageable.getSort().isEmpty()) {
            orders.add(defaultOrderSpecifier);
            return orders;
        }

        for (Sort.Order order : pageable.getSort()) {
            Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
            if (order.getProperty().equals("name")) {
                OrderSpecifier<String> orderSpecifier = new OrderSpecifier(direction, artist.name);
                orders.add(orderSpecifier);
            }
            else if (order.getProperty().equals("heartCount")) {
                OrderSpecifier<String> orderSpecifier = new OrderSpecifier(direction, artist.heartCount);
                orders.add(orderSpecifier);
            } else {
                throw new IllegalArgumentException("can't sort");
            }
        }
        return orders;
    }
}
