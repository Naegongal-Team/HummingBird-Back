package com.negongal.hummingbird.service;

import com.negongal.hummingbird.domain.Artist;
import com.negongal.hummingbird.repository.ArtistRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ArtistService {

    private final ArtistRepository artistRepository;

    @Transactional(readOnly = true)
    public Page<Artist> getArtists(Pageable pageable) {
        return artistRepository.findAll(pageable);
    }

}
