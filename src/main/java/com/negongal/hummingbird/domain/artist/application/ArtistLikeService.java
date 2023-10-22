package com.negongal.hummingbird.domain.artist.application;

import com.negongal.hummingbird.domain.artist.domain.Artist;
import com.negongal.hummingbird.domain.artist.domain.ArtistLike;
import com.negongal.hummingbird.domain.artist.dao.ArtistLikeRepository;
import com.negongal.hummingbird.domain.artist.dao.ArtistRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Slf4j
@RequiredArgsConstructor
@Service
public class ArtistLikeService {

    private final ArtistLikeRepository artistLikeRepository;

    private final ArtistRepository artistRepository;

    @Transactional
    public void save(String artistId) {
        Artist artist = artistRepository.findById(artistId).orElseThrow(() -> new NoSuchElementException("존재하지 않는 아티스트입니다."));

        /*
        사용자가 생성될 경우 사용자와 관련된 로직 생성, 우선 그대로 저장
         */
        ArtistLike artistLike = ArtistLike.builder()
                .artist(artist)
                .build();

        artistLikeRepository.save(artistLike);
    }
}
