package com.negongal.hummingbird.domain.artist.application;

import static org.junit.jupiter.api.Assertions.*;

import com.negongal.hummingbird.domain.artist.dto.ArtistDto;
import com.negongal.hummingbird.domain.artist.dto.ArtistSearchDto;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import java.io.IOException;
import java.util.List;
import org.apache.hc.core5.http.ParseException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
public class SpotifyServiceTest {

    @Autowired
    private SpotifyService spotifyService;

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
}