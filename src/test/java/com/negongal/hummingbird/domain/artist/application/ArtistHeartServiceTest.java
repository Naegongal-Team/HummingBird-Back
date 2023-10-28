package com.negongal.hummingbird.domain.artist.application;

import com.negongal.hummingbird.domain.artist.domain.Artist;
import com.negongal.hummingbird.domain.artist.domain.ArtistHeart;
import com.negongal.hummingbird.domain.artist.application.ArtistHeartService;
import com.negongal.hummingbird.domain.artist.dao.ArtistHeartRepository;
import com.negongal.hummingbird.domain.artist.dao.ArtistRepository;
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
    private ArtistHeartRepository artistHeartRepository;


    @Test
    public void submitLikeTest() {
        //given
        Artist mockArtist = artistRepository.findById("1").orElseThrow(NoSuchElementException::new);
        artistHeartService.save(mockArtist.getId());

        //when
        ArtistHeart mockArtistHeart = artistHeartRepository.findById(1L).orElseThrow(NoSuchElementException::new);

        //then
        Assert.assertEquals(1L, artistHeartRepository.count());
        Assert.assertEquals("1", mockArtistHeart.getArtist().getId());
    }
}