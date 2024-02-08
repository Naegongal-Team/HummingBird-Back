package com.negongal.hummingbird.domain.artist.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;

import com.negongal.hummingbird.domain.artist.dao.ArtistHeartRepository;
import com.negongal.hummingbird.domain.artist.dao.ArtistRepository;
import com.negongal.hummingbird.domain.artist.domain.Artist;
import com.negongal.hummingbird.domain.artist.domain.ArtistHeart;
import com.negongal.hummingbird.domain.user.dao.UserRepository;
import com.negongal.hummingbird.domain.user.domain.User;
import com.negongal.hummingbird.global.auth.utils.SecurityUtil;
import com.negongal.hummingbird.global.error.exception.NotExistException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.BDDMockito;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(properties = "spring.profiles.active=test")
class ArtistHeartServiceTest {
    @Autowired
    private ArtistHeartService artistHeartService;
    @Autowired
    private ArtistHeartRepository artistHeartRepository;
    @Autowired
    private ArtistRepository artistRepository;
    @Autowired
    private UserRepository userRepository;
    private static MockedStatic<SecurityUtil> mockedSecurityUtil;

    @BeforeEach
    void setUp() {
        Artist artist = Artist.builder()
                .id("159125")
                .name("artist1")
                .build();
        artistRepository.save(artist);
        User user = User.builder()
                .oauth2Id("test")
                .provider("test")
                .build();
        userRepository.save(user);
    }

    @DisplayName("로그인 하지 않고 아티스트 좋아요 등록")
    @Transactional
    @Order(1)
    @Test
    void notLoginLikeArtistTest() {
        // Given
        String artistId = "159125";
        mockedSecurityUtil = mockStatic(SecurityUtil.class);
        BDDMockito.given(SecurityUtil.getCurrentUserId()).willReturn(Optional.empty());

        Throwable notExistException = assertThrows(NotExistException.class, () ->
                artistHeartService.save(artistId));
        assertEquals("USER_NOT_EXIST", notExistException.getMessage());
    }

    @DisplayName("존재하지 않는 아티스트 좋아요 등록")
    @Transactional
    @Order(2)
    @Test
    void NotExistArtistLikeTest() {
        // Given
        String notExistArtistId = "159123";
        mockedSecurityUtil = mockStatic(SecurityUtil.class);
        BDDMockito.given(SecurityUtil.getCurrentUserId()).willReturn(Optional.of(1L));

        Throwable notExistException = assertThrows(NotExistException.class, () ->
                artistHeartService.save(notExistArtistId));
        assertEquals("ARTIST_NOT_EXIST", notExistException.getMessage());
    }

    @DisplayName("아티스트 좋아요 등록")
    @Transactional
    @Order(3)
    @Test
    void artistLikeTest() {
        // Given
        String artistId = "159125";
        Long userId = 1L;
        mockedSecurityUtil = mockStatic(SecurityUtil.class);
        BDDMockito.given(SecurityUtil.getCurrentUserId()).willReturn(Optional.of(userId));

        // When
        artistHeartService.save(artistId);
        ArtistHeart artistHeart = artistHeartRepository.findByUserIdAndArtistId(userId, artistId).get();

        // Then
        assertEquals(artistHeart.getArtist().getId(), artistId);
        assertEquals(artistHeart.getUser().getUserId(), userId);
    }

    @DisplayName("로그인 하지 않고 아티스트 좋아요 삭제 시도")
    @Transactional
    @Order(4)
    @Test
    void notLoginDislikeArtistTest() {
        // Given
        String artistId = "159125";
        mockedSecurityUtil = mockStatic(SecurityUtil.class);
        BDDMockito.given(SecurityUtil.getCurrentUserId()).willReturn(Optional.empty());

        Throwable notExistException = assertThrows(NotExistException.class, () ->
                artistHeartService.delete(artistId));
        assertEquals("USER_NOT_EXIST", notExistException.getMessage());
    }

    @DisplayName("좋아하지 않는 아티스트 좋아요 삭제 시도")
    @Transactional
    @Order(5)
    @Test
    void notLikeArtistDislikeTest() {
        // Given
        String artistId = "159125";
        Long userId = 1L;
        mockedSecurityUtil = mockStatic(SecurityUtil.class);
        BDDMockito.given(SecurityUtil.getCurrentUserId()).willReturn(Optional.of(userId));

        Throwable notExistException = assertThrows(NotExistException.class, () ->
                artistHeartService.delete(artistId));
        assertEquals("ARTIST_NOT_LIKE", notExistException.getMessage());
    }

    @DisplayName("좋아하는 아티스트 취소")
    @Transactional
    @Order(6)
    @Test
    void dislikeLikeArtistTest() {
        // Given
        String artistId = "159125";
        Long userId = 1L;
        mockedSecurityUtil = mockStatic(SecurityUtil.class);
        BDDMockito.given(SecurityUtil.getCurrentUserId()).willReturn(Optional.of(userId));
        artistHeartService.save(artistId);

        // When
        artistHeartService.delete(artistId);

        // Then
        Optional<ArtistHeart> findArtistHeart = artistHeartRepository.findByUserIdAndArtistId(userId, artistId);
        assertEquals(Optional.empty(), findArtistHeart);
    }
}