package com.negongal.hummingbird.service;

import com.negongal.hummingbird.repository.ArtistRepository;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.specification.Artist;
import com.wrapper.spotify.requests.data.artists.GetSeveralArtistsRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Arrays;

@Slf4j
@RequiredArgsConstructor
@Service
public class ArtistService {

    private final ArtistRepository artistRepository;

    private final SpotifyApi spotifyApi;

    private final String[] ids = new String[]{"0LcJLqbBmaGUft1e9Mm8HV"};

    private final GetSeveralArtistsRequest getSeveralArtistsRequest = spotifyApi.getSeveralArtists(ids).build();

    /*
    SpotifyApi Artist 정보를 CustomArtist 객체로 변환 후 DB에 저장
     */
    @Transactional
    public void initArtists() {
        try {
            Artist[] artists = getSeveralArtistsRequest.execute();

            Arrays.stream(artists).forEach(artist -> {
                com.negongal.hummingbird.domain.Artist customArtist = convertToArtist(artist);
                artistRepository.save(customArtist);
            });
            log.info("Length: {}", artists.length);

        } catch (IOException | SpotifyWebApiException | ParseException e) {
            log.error("Error: {}", e.getMessage());
        }

    }

    private com.negongal.hummingbird.domain.Artist convertToArtist(Artist spotifyArtist) {
        com.negongal.hummingbird.domain.Artist customArtist = com.negongal.hummingbird.domain.Artist.builder()
                .name(spotifyArtist.getName())
                .genres(String.valueOf(Arrays.stream(spotifyArtist.getGenres()).findFirst()))
                .popularity(spotifyArtist.getPopularity())
                .images(String.valueOf(Arrays.stream(spotifyArtist.getImages()).findFirst()))
                .build();

        return customArtist;
    }
}
