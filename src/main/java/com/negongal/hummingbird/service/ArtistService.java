package com.negongal.hummingbird.service;

import com.negongal.hummingbird.domain.Artist;
import com.negongal.hummingbird.repository.ArtistRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ArtistService {

    private final ArtistRepository artistRepository;

    /*
    전체 아티스트 검색 시 Artist의 리스트를 가져온다
     */
    public Page<Artist> getArtists(Pageable pageable) {
        return artistRepository.findAll(pageable);
    }

    public List<Artist> getArtistByName(String name) {
        return artistRepository.findByNameContainingOrderByName(name);
    }
}
