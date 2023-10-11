package com.negongal.hummingbird.service;

import com.negongal.hummingbird.domain.Artist;
import com.negongal.hummingbird.domain.ArtistLike;
import com.negongal.hummingbird.repository.ArtistLikeRepository;
import com.negongal.hummingbird.repository.ArtistRepository;
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
public class ArtistLikeServiceTest {

    @Autowired
    private ArtistLikeService artistLikeService;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private ArtistLikeRepository artistLikeRepository;


    @Before
    public void artistEntitySetUp() {
        Artist mockArtist = Artist.builder()
                .name("Mock Popular")
                .genres("POP")
                .popularity(99)
                .build();

        artistRepository.save(mockArtist);
    }

    @After
    public void clearRepository() {
        artistLikeRepository.deleteAll();
        artistRepository.deleteAll();
    }

    @Test
    public void submitLikeTest() {
        //given
        Artist mockArtist = artistRepository.findById(1L).orElseThrow(NoSuchElementException::new);
        artistLikeService.save(mockArtist.getId());

        //when
        ArtistLike mockArtistLike = artistLikeRepository.findById(1L).orElseThrow(NoSuchElementException::new);

        //then
        Assert.assertEquals(1L, artistLikeRepository.count());
        Assert.assertEquals((Long)1L, (Long)mockArtistLike.getArtist().getId());
    }
}