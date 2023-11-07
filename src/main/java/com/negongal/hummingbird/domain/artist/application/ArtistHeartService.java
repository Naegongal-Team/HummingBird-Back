package com.negongal.hummingbird.domain.artist.application;

import com.negongal.hummingbird.domain.artist.domain.Artist;
import com.negongal.hummingbird.domain.artist.domain.ArtistHeart;
import com.negongal.hummingbird.domain.artist.dao.ArtistHeartRepository;
import com.negongal.hummingbird.domain.artist.dao.ArtistRepository;
import com.negongal.hummingbird.domain.user.dao.UserRepository;
import com.negongal.hummingbird.domain.user.domain.User;
import com.negongal.hummingbird.global.error.exception.NotExistException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.negongal.hummingbird.global.error.ErrorCode.*;

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
                .orElseThrow(() -> new NotExistException(ARTIST_NOT_EXIST));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotExistException(USER_NOT_EXIST));

        ArtistHeart artistHeart = ArtistHeart.builder()
                .artist(artist)
                .user(user)
                .build();

        artistHeartRepository.save(artistHeart);
    }

    @Transactional
    public void delete(Long userId, String artistId) {
        ArtistHeart artistHeart = artistHeartRepository.findByUserIdAndArtistId(userId, artistId);

        artistHeartRepository.delete(artistHeart);
    }
}
