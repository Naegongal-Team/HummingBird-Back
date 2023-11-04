package com.negongal.hummingbird.domain.artist.application;

import static org.junit.jupiter.api.Assertions.*;

import com.negongal.hummingbird.domain.artist.dao.ArtistRepository;
import com.negongal.hummingbird.domain.artist.domain.Artist;
import com.negongal.hummingbird.domain.artist.dto.ArtistSearchDto;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import org.apache.hc.core5.http.ParseException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;


@SpringBootTest
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
public class SpotifyServiceTest {

    @Autowired
    private SpotifyService spotifyService;

    @Autowired
    private ArtistRepository artistRepository;

    @Test
    public void a를_입력할_시_10개_검색() throws IOException, ParseException, SpotifyWebApiException {
        String searchName = "a";
        List<ArtistSearchDto> searchDto = spotifyService.searchArtists(searchName);

        Assert.assertEquals(10, searchDto.size());
    }

    @Test
    public void Elliott_Smith를_입력할_시_올바른_값() throws IOException, ParseException, SpotifyWebApiException {
        String searchName = "Elliott Smith";
        List<ArtistSearchDto> searchDto = spotifyService.searchArtists(searchName);
        String elliotName = searchDto.get(0).getName();
        String elliotId = searchDto.get(0).getId();

        Assert.assertEquals("Elliott Smith", elliotName);
        Assert.assertEquals("2ApaG60P4r0yhBoDCGD8YG", elliotId);
    }

    @Test(expected = SpotifyWebApiException.class)
    public void 존재하지_않는_아이디_값_입력시_에러() throws IOException, ParseException, SpotifyWebApiException {
        String artistId = "dkanrjsk";

        spotifyService.saveArtist(artistId);
    }

    @Test
    public void 김수영_아이디_값_입력시_저장() throws IOException, ParseException, SpotifyWebApiException {
        String artistId = "7nj9JLgGDx7CRNUKzptaCj";
        spotifyService.saveArtist(artistId);

        Artist kim = artistRepository.findById(artistId).orElseThrow(NoSuchElementException::new);
        String kimId = kim.getId();
        String kimName = kim.getName();

        Assert.assertEquals(artistId, kimId);
        Assert.assertEquals("김수영 Kim Suyoung", kimName);
    }

    @Test
    public void radiohead_검색_후_저장() throws IOException, ParseException, SpotifyWebApiException {
        String searchArtistName = "Radiohead";
        List<ArtistSearchDto> searchArtists = spotifyService.searchArtists(searchArtistName);

        ArtistSearchDto resultArtist = searchArtists.get(0);
        String radioheadId = resultArtist.getId();
        spotifyService.saveArtist(radioheadId);

        Artist radiohead = artistRepository.findById(radioheadId).orElseThrow(NoSuchElementException::new);

        Assert.assertEquals(radioheadId, radiohead.getId());
        Assert.assertEquals(searchArtistName, radiohead.getName());
    }
}