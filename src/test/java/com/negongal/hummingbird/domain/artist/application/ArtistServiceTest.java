package com.negongal.hummingbird.domain.artist.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;

import com.negongal.hummingbird.domain.artist.dao.ArtistHeartRepository;
import com.negongal.hummingbird.domain.artist.dao.ArtistRepository;
import com.negongal.hummingbird.domain.artist.domain.Artist;
import com.negongal.hummingbird.domain.artist.domain.ArtistHeart;
import com.negongal.hummingbird.domain.artist.dto.ArtistDetailDto;
import com.negongal.hummingbird.domain.artist.dto.ArtistDto;
import com.negongal.hummingbird.domain.artist.dto.ArtistSearchDto;
import com.negongal.hummingbird.domain.user.dao.UserRepository;
import com.negongal.hummingbird.domain.user.domain.Role;
import com.negongal.hummingbird.domain.user.domain.User;
import com.negongal.hummingbird.global.auth.utils.SecurityUtil;
import com.negongal.hummingbird.global.error.exception.NotExistException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@TestInstance(Lifecycle.PER_CLASS)
@SpringBootTest(properties = "spring.profiles.active=test")
class ArtistServiceTest {
    @Autowired
    private ArtistService artistService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ArtistRepository artistRepository;
    @Autowired
    private ArtistHeartRepository artistHeartRepository;
    private static MockedStatic<SecurityUtil> mockedSecurityUtil;


    @BeforeAll
    void mockInit() {
        mockedSecurityUtil = mockStatic(SecurityUtil.class);
        User user = User.builder().nickname("test").oauth2Id("test").provider("test").role(Role.USER).build();
        userRepository.save(user);
    }

    @AfterEach
    public void deleteRepository() {
        artistRepository.deleteAll();
        artistHeartRepository.deleteAll();
    }


    @DisplayName("아티스트 저장 후 모든 아티스트 조회")
    @Transactional
    @Test
    void findAllArtistsTest() {
        List<Artist> artistList = createRandomArtists();
        artistRepository.saveAll(artistList);
        Pageable pageable = PageRequest.of(0, 10);

        List<ArtistDto> allArtist = artistService.findAllArtist(pageable).toList();
        int artistSize = allArtist.size();

        assertAll(
                () -> assertEquals(allArtist.get(0).getId(), "1"),
                () -> assertEquals(allArtist.get(9).getId(), "50"),
                () -> assertEquals(artistSize, 10),
                () -> assertThrows(IndexOutOfBoundsException.class, () -> {
                    allArtist.get(10).getId();
                })
        );
    }

    @DisplayName("아티스트를 저장하지 않고 모든 아티스트 조회")
    @Transactional
    @Test
    void findEmptyArtistsTest() {
        Pageable pageable = PageRequest.of(0, 10);

        List<ArtistDto> allArtist = artistService.findAllArtist(pageable).toList();
        int artistSize = allArtist.size();

        assertAll(
                () -> assertEquals(artistSize, 0),
                () -> assertThrows(IndexOutOfBoundsException.class, () -> {
                    allArtist.get(0).getId();
                })
        );
    }

    @DisplayName("아티스트 이름을 입력하지 않고 ")
    @Transactional
    @Test
    void findArtistByEmptyNameTest() {
        artistRepository.saveAll(createRandomArtists());
        String searchName = "";

        List<ArtistSearchDto> searchedArtist = artistService.findArtistByName(searchName);
        int artistSize = searchedArtist.size();
        int firstIndex = 0;
        int lastIndex = artistSize - 1;
        String firstName = "artist 1";
        String lastName = "composer 5";

        assertAll(
                () -> assertEquals(artistSize, 10),
                () -> assertEquals(firstName, searchedArtist.get(firstIndex).getName()),
                () -> assertEquals(lastName, searchedArtist.get(lastIndex).getName())
        );
    }

    @DisplayName("특정한 이름을 입력했을 경우 특정한 이름을 포함한 아티스트가 반환되어야 하고 정렬은 이름 순")
    @Test
    void findArtistsBySpecificNameTest() {
        artistRepository.saveAll(createRandomArtists());
        String searchName = "artist";

        List<ArtistSearchDto> searchedArtist = artistService.findArtistByName(searchName);
        int artistSize = searchedArtist.size();
        int firstIndex = 0;
        int lastIndex = artistSize - 1;
        String firstName = "artist 1";
        String lastName = "artist 5";

        assertAll(
                () -> assertEquals(artistSize, 5),
                () -> assertEquals(firstName, searchedArtist.get(firstIndex).getName()),
                () -> assertEquals(lastName, searchedArtist.get(lastIndex).getName())
        );
    }

    @DisplayName("아티스트 단일 조회 시 아이디 값이 없으면 에러 처리")
    @Test
    void findArtistButNotExistIdTest() {
        artistRepository.saveAll(createRandomArtists());
        String findId = "2512";

        assertAll(
                () -> assertThrows(NotExistException.class, () -> {
                    ArtistDetailDto artist = artistService.findArtist(findId);
                })
        );
    }

    @DisplayName("아티스트 단일 조회 시 아이디 값이 존재하지 않을 경우 ishearted, isalarmed는 false")
    @Test
    void findArtistButNotLogInTest() {
        artistRepository.saveAll(createRandomArtists());
        BDDMockito.given(SecurityUtil.getCurrentUserId()).willReturn(Optional.empty());
        String findId = "1";
        ArtistDetailDto artist = artistService.findArtist(findId);
        boolean isHearted = artist.isHearted();
        boolean isAlarmed = artist.isAlarmed();

        assertAll(
                () -> assertFalse(isHearted),
                () -> assertFalse(isAlarmed)
        );
    }

    @DisplayName("아티스트 단일 조회 시 유저는 존재하지만 좋아하지 않는 아티스트일 경우 ishearted, isalarmed는 false")
    @Test
    void findArtistUserNotLikeTest() {
        artistRepository.saveAll(createRandomArtists());
        String findId = "1";
        BDDMockito.given(SecurityUtil.getCurrentUserId()).willReturn(Optional.of(1L));

        ArtistDetailDto artist = artistService.findArtist(findId);
        boolean isHearted = artist.isHearted();
        boolean isAlarmed = artist.isAlarmed();

        assertAll(
                () -> assertEquals(false, isHearted),
                () -> assertEquals(false, isAlarmed)
        );
    }

    @DisplayName("아티스트 단일 조회 시 유저도 존재하고 좋아요도 있지만 알람은 하지 않을 경우 ishearted, isalarmed는 false")
    @Test
    void findArtistUserLikeNotAlarmedTest() {
        // Given
        List<Artist> randomArtists = createRandomArtists();
        artistRepository.saveAll(randomArtists);
        BDDMockito.given(SecurityUtil.getCurrentUserId()).willReturn(Optional.of(1L));
        User user = userRepository.findById(1L).get();
        Artist artist = randomArtists.get(0);

        ArtistHeart artistHeart = ArtistHeart.builder()
                .artist(artist)
                .user(user)
                .isAlarmed(false)
                .build();
        artistHeartRepository.save(artistHeart);

        // When
        ArtistDetailDto artistDetailDto = artistService.findArtist(artist.getId());

        // Then
        assertAll(
                () -> assertTrue(artistDetailDto.isHearted()),
                () -> assertFalse(artistDetailDto.isAlarmed())
        );
    }

    @DisplayName("아티스트 단일 조회 시 유저도 존재하고 좋아요도 눌렀고 알림도 등록했을 경우 ishearted, isalarmed는 true")
    @Test
    void findArtistUserLikeAlarmedTest() {
        // Given
        List<Artist> randomArtists = createRandomArtists();
        artistRepository.saveAll(randomArtists);
        BDDMockito.given(SecurityUtil.getCurrentUserId()).willReturn(Optional.of(1L));
        User user = userRepository.findById(1L).get();

        Artist artist = randomArtists.get(0);

        ArtistHeart artistHeart = ArtistHeart.builder()
                .artist(artist)
                .user(user)
                .isAlarmed(true)
                .build();
        artistHeartRepository.save(artistHeart);
        // When
        ArtistDetailDto artistDetailDto = artistService.findArtist(artist.getId());
        assertAll(
                () -> assertTrue(artistDetailDto.isHearted()),
                () -> assertTrue(artistDetailDto.isAlarmed())
        );
    }

    @DisplayName("좋아하는 가수가 없을 경우 값 반환 x")
    @Test
    void findUserNotLikedAnyoneTest() {
        artistRepository.saveAll(createRandomArtists());
        Pageable pageable = PageRequest.of(0, 10);
        BDDMockito.given(SecurityUtil.getCurrentUserId()).willReturn(Optional.of(1L));

        List<ArtistDto> artists = artistService.findLikeArtists(pageable).toList();
        int artistsSize = artists.size();

        assertAll(
                () -> assertEquals(artistsSize, 0),
                () -> assertThrows(IndexOutOfBoundsException.class, () -> {
                    artists.get(0).getId();
                })
        );
    }

    @DisplayName("좋아하는 가수가 있을 경우 좋아하는 가수 리스트 반환")
    @Test
    void findUserLikedArtistsTest() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        List<Artist> randomArtists = createLikedArtists();
        artistRepository.saveAll(randomArtists);
        BDDMockito.given(SecurityUtil.getCurrentUserId()).willReturn(Optional.of(1L));
        User user = userRepository.findById(1L).get();
        List<ArtistHeart> artistHearts = createArtistHearts(randomArtists, user);
        artistHeartRepository.saveAll(artistHearts);

        // When
        List<ArtistDto> likeArtists = artistService.findLikeArtists(pageable).toList();

        // Then
        assertAll(
                () -> assertEquals(likeArtists.size(), 3)
        );
    }


    private List<Artist> createRandomArtists() {
        List<Artist> artistList = new ArrayList<>();

        for (int i = 1; i <= 5; i++) {
            artistList.add(Artist.builder().id(Integer.toString(i)).name("artist " + Integer.toString(i)).build());
            artistList.add(
                    Artist.builder().id(Integer.toString(i * 10)).name("composer " + Integer.toString(i)).build());
        }
        return artistList;
    }

    private List<Artist> createLikedArtists() {
        List<Artist> artistList = new ArrayList<>();

        Artist likedArtist1 = Artist.builder().id("123").name("example1").build();
        Artist likedArtist2 = Artist.builder().id("124").name("example2").build();
        Artist likedArtist3 = Artist.builder().id("125").name("example3").build();
        Artist notLikedArtist1 = Artist.builder().id("12345").name("example4").build();
        Artist notLikedArtist2 = Artist.builder().id("123455").name("example5").build();

        artistList.addAll(Arrays.asList(likedArtist1, likedArtist2, likedArtist3, notLikedArtist1, notLikedArtist2));

        return artistList;
    }

    private List<ArtistHeart> createArtistHearts(List<Artist> randomArtists, User user) {
        List<ArtistHeart> artistHearts = new ArrayList<>();
        Artist artist1 = randomArtists.get(0);
        Artist artist2 = randomArtists.get(1);
        Artist artist3 = randomArtists.get(2);
        ArtistHeart artistHeart1 = ArtistHeart.builder()
                .artist(artist1)
                .user(user)
                .isAlarmed(true)
                .build();
        ArtistHeart artistHeart2 = ArtistHeart.builder()
                .artist(artist2)
                .user(user)
                .isAlarmed(true)
                .build();
        ArtistHeart artistHeart3 = ArtistHeart.builder()
                .artist(artist3)
                .user(user)
                .isAlarmed(true)
                .build();
        artistHearts.addAll(Arrays.asList(artistHeart1, artistHeart2, artistHeart3));

        return artistHearts;
    }
}