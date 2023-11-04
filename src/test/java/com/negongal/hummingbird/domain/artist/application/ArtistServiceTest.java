package com.negongal.hummingbird.domain.artist.application;

import com.negongal.hummingbird.domain.artist.application.ArtistService;
import com.negongal.hummingbird.domain.artist.domain.Artist;
import com.negongal.hummingbird.domain.artist.domain.ArtistHeart;
import com.negongal.hummingbird.domain.artist.dto.ArtistDto;
import com.negongal.hummingbird.domain.artist.dto.ArtistSearchDto;
import com.negongal.hummingbird.domain.artist.dao.ArtistHeartRepository;
import com.negongal.hummingbird.domain.artist.dao.ArtistRepository;
import com.negongal.hummingbird.domain.user.dao.UserRepository;
import com.negongal.hummingbird.domain.user.domain.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
public class ArtistServiceTest {

    @Autowired
    private ArtistService artistService;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private ArtistHeartRepository artistHeartRepository;

    @Autowired
    private UserRepository userRepository;

    @Before
    public void initUser() {
        User user = User.builder().build();
        userRepository.save(user);
    }

    @Test
    public void getArtistListTest() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<ArtistDto> artistList = artistService.findArtists(pageable);

        Assert.assertEquals("Conan Gray", artistList.toList().get(0).getName());
    }

    @Test
    public void getArtistsByNameTest() {
        String searchName = "AB";
        List<ArtistSearchDto> artistSearchByNameList = artistService.findArtistByName(searchName);

        Assert.assertEquals(1, artistSearchByNameList.size());
        Assert.assertEquals("ABBA", artistSearchByNameList.get(0).getName());
    }

    @Test
    public void 좋아요한_아티스트_없을_때_리스트는_0개() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<ArtistDto> LikedArtist = artistService.findLikeArtist(1L, pageable);

        Assert.assertEquals(0, LikedArtist.toList().size());
    }

    @Test
    public void 좋아요한_아티스트_테스트() {
        Artist artist = artistRepository.findById("4Uc8Dsxct0oMqx0P6i60ea").orElseThrow();
        User user = userRepository.findById(1L).orElseThrow();
        ArtistHeart artistHeart = ArtistHeart.builder()
                .artist(artist)
                .user(user)
                .build();
        artistHeartRepository.save(artistHeart);

        Pageable pageable = PageRequest.of(0,10);
        Page<ArtistDto> LikedArtist = artistService.findLikeArtist(1L, pageable);

        Assert.assertEquals(1L, LikedArtist.toList().size());
        Assert.assertEquals("Conan Gray", LikedArtist.toList().get(0).getName());
    }
}