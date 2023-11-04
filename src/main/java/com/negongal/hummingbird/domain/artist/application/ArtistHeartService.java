package com.negongal.hummingbird.domain.artist.application;

import com.negongal.hummingbird.domain.artist.domain.Artist;
import com.negongal.hummingbird.domain.artist.domain.ArtistHeart;
import com.negongal.hummingbird.domain.artist.dao.ArtistHeartRepository;
import com.negongal.hummingbird.domain.artist.dao.ArtistRepository;
import com.negongal.hummingbird.domain.user.dao.UserRepository;
import com.negongal.hummingbird.domain.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Slf4j
@RequiredArgsConstructor
@Service
public class ArtistHeartService {

    private final ArtistHeartRepository artistHeartRepository;

    private final ArtistRepository artistRepository;

    private final UserRepository userRepository;

    @Transactional
    public void save(Long userId, String artistId) {
        Artist artist = artistRepository.findById(artistId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 아티스트입니다."));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 유저입니다."));

        ArtistHeart artistHeart = ArtistHeart.builder()
                .artist(artist)
                .user(user)
                .build();

        artistHeartRepository.save(artistHeart);
    }

    @Transactional
    public void delete(Long userId, String artistId) {
        ArtistHeart artistHeart = artistHeartRepository.findByUserUserIdAndArtistId(userId, artistId);

        artistHeartRepository.delete(artistHeart);
    }
}
