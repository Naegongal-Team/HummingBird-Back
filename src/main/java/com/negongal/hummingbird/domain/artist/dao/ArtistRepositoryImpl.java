package com.negongal.hummingbird.domain.artist.dao;

import static com.negongal.hummingbird.domain.artist.domain.QArtist.artist;
import static com.negongal.hummingbird.domain.artist.domain.QArtistHeart.artistHeart;

import com.negongal.hummingbird.domain.artist.dto.ArtistDto;
import com.negongal.hummingbird.domain.artist.dto.QArtistDto;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ArtistRepositoryImpl implements ArtistRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<ArtistDto> findAllArtists(Long userId, Pageable pageable) {
        JPAQuery<ArtistDto> results = findAllArtistsQuery(userId);

        return new PageImpl<>(results.fetch(), pageable, results.fetch().size());
    }

    private JPAQuery<ArtistDto> findAllArtistsQuery(Long userId) {
        return jpaQueryFactory.select(new QArtistDto(
                artist.id, artist.name, artist.image, artist.genreList, artist.artistHeartList))
                .from(artist)
                .leftJoin(artist.artistHeartList, artistHeart)
                .where(artistHeart.user.userId.eq(userId).or(artistHeart.user.userId.isNull()))
                .orderBy(artistHeart.createdDate.asc());
    }
}
