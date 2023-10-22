package com.negongal.hummingbird.service;

import com.negongal.hummingbird.domain.artist.application.ArtistService;
import com.negongal.hummingbird.domain.artist.dto.ArtistDto;
import com.negongal.hummingbird.domain.artist.dto.ArtistSearchDto;
import com.negongal.hummingbird.domain.artist.dao.ArtistHeartRepository;
import com.negongal.hummingbird.domain.artist.dao.ArtistRepository;
import org.junit.Assert;
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
}