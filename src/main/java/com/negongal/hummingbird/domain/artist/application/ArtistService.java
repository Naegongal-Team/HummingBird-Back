package com.negongal.hummingbird.domain.artist.application;

import com.negongal.hummingbird.domain.artist.dao.ArtistHeartRepository;
import com.negongal.hummingbird.domain.artist.dto.ArtistDetailDto;
import com.negongal.hummingbird.domain.artist.dto.ArtistDto;
import com.negongal.hummingbird.domain.artist.dto.ArtistSearchDto;
import com.negongal.hummingbird.domain.artist.domain.Artist;
import com.negongal.hummingbird.domain.artist.dao.ArtistRepository;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ArtistService {

    private final ArtistRepository artistRepository;

    private final ArtistHeartRepository artistHeartRepository;

    /*
    전체 아티스트 검색 시 Artist의 리스트를 가져온다
     */
    public Page<ArtistDto> findArtists(Pageable pageable) {
        return artistRepository.findAll(pageable).map(ArtistDto::of);
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
    public ArtistDetailDto findArtist(String id) {
        Artist artist = artistRepository.findById(id).orElseThrow(NoSuchElementException::new);
        return ArtistDetailDto.of(artist);
    }

    /*
    좋아요 한 아티스트들 검색
     */
    public Page<ArtistDto> findLikeArtist(Long userId, Pageable pageable) {
        return artistHeartRepository.findArtistsByUserId(userId, pageable).map(ArtistDto::of);
    }
}
