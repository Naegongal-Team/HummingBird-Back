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
import com.negongal.hummingbird.domain.notification.dao.NotificationRepository;
import com.negongal.hummingbird.global.auth.utils.SecurityUtil;
import com.negongal.hummingbird.global.error.exception.NotExistException;
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
    private final NotificationRepository notificationRepository;

    /*
    전체 아티스트 검색 시 Artist의 리스트를 가져온다
     */
    public Page<ArtistDto> findAllArtist(Pageable pageable) {
        List<ArtistDto> artists = artistRepository.findAll(pageable).stream().map(artist -> {
                    List<ArtistGenresDto> artistGenres = artist.getGenreList().stream().map(genre ->
                            ArtistGenresDto.builder()
                                    .genres(genre.getGenreName())
                                    .build()).collect(Collectors.toList());
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
        int end = Math.min((start + pageable.getPageSize()),artists.size());

        return new PageImpl<>(artists.subList(start, end), pageable, artists.size());
    }

    /*
    아티스트 이름으로 아티스트 검색
    */
    public List<ArtistSearchDto> findArtistByName(String name) {
        List<ArtistSearchDto> artistList = artistRepository.findByNameContainingOrderByName(name).stream().map(a ->
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
        Long currentUserId = SecurityUtil.getCurrentUserId().orElseThrow(() -> new NotExistException(USER_NOT_EXIST));
        boolean isHearted = artistHeartRepository.findByUserIdAndArtistId(currentUserId, artistId).isPresent();
        boolean isAlarmed = artistHeartRepository.findByUserIdAndArtistId(currentUserId, artistId).get().getIsAlarmed();

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
