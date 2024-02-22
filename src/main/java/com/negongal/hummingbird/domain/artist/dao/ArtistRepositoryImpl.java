package com.negongal.hummingbird.domain.artist.dao;

import static com.negongal.hummingbird.domain.artist.domain.QArtist.artist;
import static com.negongal.hummingbird.domain.artist.domain.QArtistHeart.artistHeart;
import static com.negongal.hummingbird.domain.artist.domain.QGenre.genre;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

import com.negongal.hummingbird.domain.artist.dto.ArtistDto;
import com.negongal.hummingbird.domain.artist.dto.ArtistGenresDto;
import com.negongal.hummingbird.global.error.ErrorCode;
import com.negongal.hummingbird.global.error.exception.InvalidException;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
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
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional
public class ArtistRepositoryImpl implements ArtistRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<ArtistDto> findLikedArtists(Long userId, Pageable pageable) {
        List<ArtistDto> results = findAllArtistsQuery(userId, pageable);

        return new PageImpl<>(results, pageable, results.size());
    }

    private List<ArtistDto> findAllArtistsQuery(Long userId, Pageable pageable) {
        List<OrderSpecifier> orders = getAllOrderSpecifiers(pageable);
        return jpaQueryFactory
                .select(artist)
                .from(artist)
                .leftJoin(genre).on(artist.id.eq(genre.artist.id))
                .join(artist.hearts, artistHeart)
                .where(artistHeart.user.id.eq(userId))
                .orderBy(orders.toArray(OrderSpecifier[]::new))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .transform(groupBy(artist.id).list(Projections.constructor(ArtistDto.class,
                        artist.id, artist.name, artist.image, artist.heartCount,
                        list(Projections.constructor(ArtistGenresDto.class, genre.name)
                        ))));
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
            String property = order.getProperty();

            OrderSpecifier<?> orderSpecifier;

            switch (property) {
                case "name":
                    orderSpecifier = new OrderSpecifier<>(direction, artist.name);
                    break;
                case "heartCount":
                    orderSpecifier = new OrderSpecifier<>(direction, artist.heartCount);
                    break;
                default:
                    throw new InvalidException(ErrorCode.ARTIST_CAN_NOT_SORT_THIS_TYPE);
            }

            orders.add(orderSpecifier);
        }
        return orders;
    }
}
