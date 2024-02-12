package com.negongal.hummingbird.domain.artist.application;


import com.negongal.hummingbird.domain.artist.dao.ArtistHeartRepository;
import com.negongal.hummingbird.domain.artist.dao.ArtistRepositoryCustom;
import com.negongal.hummingbird.domain.artist.domain.ArtistHeart;
import com.negongal.hummingbird.domain.artist.dto.ArtistDetailDto;
import com.negongal.hummingbird.domain.artist.dto.ArtistDto;
import com.negongal.hummingbird.domain.artist.dto.ArtistGenresDto;
import com.negongal.hummingbird.domain.artist.dto.ArtistSearchDto;
import com.negongal.hummingbird.domain.artist.domain.Artist;
import com.negongal.hummingbird.domain.artist.dao.ArtistRepository;
import com.negongal.hummingbird.global.auth.utils.SecurityUtil;
import com.negongal.hummingbird.global.error.exception.NotExistException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

import static com.negongal.hummingbird.global.error.ErrorCode.*;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ArtistService {

    private final ArtistRepository artistRepository;
    private final ArtistHeartRepository artistHeartRepository;
    private final ArtistRepositoryCustom artistRepositoryCustom;

    public Page<ArtistDto> findAllArtist(Pageable pageable) {
        List<ArtistDto> artists = artistRepository.findAll(pageable).stream().map(artist -> {
            List<ArtistGenresDto> artistGenres = getArtistGenres(artist);
            ArtistDto getArtist = ArtistDto.builder()
                    .id(artist.getId())
                    .name(artist.getName())
                    .heartCount(artist.getHeartCount())
                    .genres(artistGenres)
                    .image(artist.getImage())
                    .build();
            return getArtist;
        }).collect(Collectors.toList());

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), artists.size());

        return new PageImpl<>(artists.subList(start, end), pageable, artists.size());
    }

    private List<ArtistGenresDto> getArtistGenres(Artist artist) {
        if (artist == null || artist.getGenres() == null) {
            return Collections.emptyList();
        }
        List<ArtistGenresDto> artistGenres = artist.getGenres().stream().map(genre ->
                ArtistGenresDto.builder()
                        .name(genre.getName())
                        .build()).collect(Collectors.toList());
        return artistGenres;
    }

    /*
    아티스트 이름으로 아티스트 검색
    */
    public List<ArtistSearchDto> findArtistByName(String name) {
        List<ArtistSearchDto> artistList = artistRepository.findAllByNameStartingWithOrderByNameAsc(name).stream()
                .map(a ->
                        ArtistSearchDto.builder()
                                .id(a.getId())
                                .name(a.getName())
                                .build())
                .collect(Collectors.toList());
        return artistList;
    }

    /*
    아티스트 단건 조회
     */
    public ArtistDetailDto findArtist(String artistId) {
        Artist artist = artistRepository.findById(artistId).orElseThrow(() -> new NotExistException(ARTIST_NOT_EXIST));
        Long currentUserId = SecurityUtil.getCurrentUserId().orElse(null);
        ArtistHeart artistHeart = artistHeartRepository.findByUserIdAndArtistId(currentUserId,
                artistId).orElse(null);
        if (currentUserId == null || artistHeart == null) {
            return ArtistDetailDto.of(artist, false, false);
        }
        boolean isHearted = true;
        boolean isAlarmed = artistHeart.getIsAlarmed();
        return ArtistDetailDto.of(artist, isHearted, isAlarmed);
    }

    /*
    좋아요 한 아티스트들 검색
     */
    public Page<ArtistDto> findLikeArtists(Pageable pageable) {
        Long currentUserId = SecurityUtil.getCurrentUserId().orElseThrow(() -> new NotExistException(USER_NOT_EXIST));
        return artistRepositoryCustom.findLikedArtists(currentUserId, pageable);
    }
}
