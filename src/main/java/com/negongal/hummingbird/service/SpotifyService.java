package com.negongal.hummingbird.service;

import com.negongal.hummingbird.repository.ArtistRepository;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.specification.Artist;
import com.wrapper.spotify.model_objects.specification.Image;
import com.wrapper.spotify.requests.data.artists.GetSeveralArtistsRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.ParseException;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Arrays;
import java.util.NoSuchElementException;

import static com.negongal.hummingbird.config.SpotifyConfig.spotifyApi;

@DependsOn("spotifyConfig")
@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class SpotifyService {

    private final ArtistRepository artistRepository;

    private final String[] ids = new String[]{"0LcJLqbBmaGUft1e9Mm8HV"};

    private final GetSeveralArtistsRequest getSeveralArtistsRequest = spotifyApi.getSeveralArtists(ids).build();

    /*
    SpotifyApi Artist 정보를 CustomArtist 객체로 변환 후 DB에 저장
    */
    @PostConstruct
    public void initArtists() throws IOException, ParseException, SpotifyWebApiException {
        Artist[] artists = getSeveralArtistsRequest.execute();

        Arrays.stream(artists).forEach(artist -> {
            com.negongal.hummingbird.domain.Artist customArtist = convertToArtist(artist);
            artistRepository.save(customArtist);
        });
    }

    private com.negongal.hummingbird.domain.Artist convertToArtist(Artist spotifyArtist) {
        String spotifyArtistGenre = Arrays.stream(spotifyArtist.getGenres()).findFirst().orElseThrow(NoSuchElementException::new);
        Image spotifyArtistImage = Arrays.stream(spotifyArtist.getImages()).findFirst().orElseThrow(NoSuchElementException::new);
        String spotifyArtistUrl = spotifyArtistImage.getUrl();

        return com.negongal.hummingbird.domain.Artist.builder()
                .name(spotifyArtist.getName())
                .genres(spotifyArtistGenre)
                .popularity(spotifyArtist.getPopularity())
                .image(spotifyArtistUrl)
                .build();
    }
}
