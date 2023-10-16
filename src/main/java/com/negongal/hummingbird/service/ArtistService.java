package com.negongal.hummingbird.service;

import com.negongal.hummingbird.api.dto.ArtistDto;
import com.negongal.hummingbird.api.dto.ArtistSearchDto;
import com.negongal.hummingbird.domain.Artist;
import com.negongal.hummingbird.repository.ArtistRepository;
import com.wrapper.spotify.exceptions.detailed.NotFoundException;
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

    /*
    전체 아티스트 검색 시 Artist의 리스트를 가져온다
     */
    public Page<Artist> findArtists(Pageable pageable) {
        return artistRepository.findAll(pageable);
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

    public ArtistDto findArtist(Long id) throws NotFoundException {
        Artist artist = artistRepository.findById(id).orElseThrow(NotFoundException::new);
        return ArtistDto.of(artist);
    }
}
