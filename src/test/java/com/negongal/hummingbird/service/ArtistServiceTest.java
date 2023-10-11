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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
public class ArtistServiceTest {

    @Autowired
    private ArtistService artistService;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private ArtistLikeRepository artistLikeRepository;

    @Before
    public void artistEntitySetup() {
        createAndSaveArtist("mock1", 94, "POP");
        Artist mockArtist2 = createAndSaveArtist("mock2", 92, "DEATH METAL");
        Artist mockArtist3 = createAndSaveArtist("mock3", 92, "ROCK");
        createAndSaveArtist("mock4", 90, "BALLAD");

        createAndSaveArtistLike(mockArtist2);
        createAndSaveArtistLike(mockArtist2);
        createAndSaveArtistLike(mockArtist3);
    }

    @After
    public void clearRepository() {
        artistLikeRepository.deleteAll();
        artistRepository.deleteAll();
    }

    @Test
    public void getArtistListTest() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Artist> artistList = artistService.getArtists(pageable);

        Assert.assertEquals((Long)3L, (Long)artistList.toList().get(0).getId());
    }

    private Artist createAndSaveArtist(String name, int popularity, String genres) {
        Artist artist = Artist.builder()
                .name(name)
                .popularity(popularity)
                .genres(genres)
                .build();
        artistRepository.save(artist);
        return artist;
    }

    private void createAndSaveArtistLike(Artist artist) {
        ArtistLike artistLike = ArtistLike.builder()
                .artist(artist)
                .build();
        artistLikeRepository.save(artistLike);
    }

}