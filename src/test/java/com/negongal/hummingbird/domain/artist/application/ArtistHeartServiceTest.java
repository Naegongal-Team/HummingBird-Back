package com.negongal.hummingbird.domain.artist.application;

import com.negongal.hummingbird.domain.artist.domain.Artist;
import com.negongal.hummingbird.domain.artist.domain.ArtistHeart;
import com.negongal.hummingbird.domain.artist.application.ArtistHeartService;
import com.negongal.hummingbird.domain.artist.dao.ArtistHeartRepository;
import com.negongal.hummingbird.domain.artist.dao.ArtistRepository;
import com.negongal.hummingbird.domain.user.dao.UserRepository;
import com.negongal.hummingbird.domain.user.domain.User;
import java.util.Optional;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@SpringBootTest
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
public class ArtistHeartServiceTest {

    @Autowired
    private ArtistHeartService artistHeartService;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArtistHeartRepository artistHeartRepository;

    @Before
    public void initUser() {
        User user = User.builder()
                .build();
    }
    @Test
    public void submitLikeTest() {
        //given
        Artist mockArtist = artistRepository.findById("4Uc8Dsxct0oMqx0P6i60ea").orElseThrow(NoSuchElementException::new);
        User user = userRepository.findById(1L).orElseThrow(NoSuchElementException::new);

        artistHeartService.save(user.getUserId(), mockArtist.getId());

        //when
        ArtistHeart mockArtistHeart = artistHeartRepository.findById(1L).orElseThrow(NoSuchElementException::new);

        //then
        Assert.assertEquals(1L, artistHeartRepository.count());
        Assert.assertEquals("4Uc8Dsxct0oMqx0P6i60ea", mockArtistHeart.getArtist().getId());
        Assert.assertEquals(Optional.of(1L), mockArtistHeart.getUser().getUserId());
    }
}