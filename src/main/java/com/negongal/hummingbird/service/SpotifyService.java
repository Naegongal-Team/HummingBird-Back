package com.negongal.hummingbird.service;

import com.negongal.hummingbird.repository.ArtistRepository;
import com.negongal.hummingbird.repository.TrackRepository;
import com.neovisionaries.i18n.CountryCode;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.specification.Artist;
import com.wrapper.spotify.model_objects.specification.Image;
import com.wrapper.spotify.model_objects.specification.Track;
import com.wrapper.spotify.requests.data.artists.GetArtistsTopTracksRequest;
import com.wrapper.spotify.requests.data.artists.GetSeveralArtistsRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

import static com.negongal.hummingbird.config.SpotifyConfig.spotifyApi;

@PropertySource("classpath:config.properties")
@DependsOn("spotifyConfig")
@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class SpotifyService {

    private final ArtistRepository artistRepository;

    private final TrackRepository trackRepository;

    //@Value("${spotifyArtists.ids.array}")
    private String[] artistIds = {"4Uc8Dsxct0oMqx0P6i60ea", "3zmfs9cQwzJl575W1ZYXeT"};

    private static final CountryCode countryCode = CountryCode.KR;

    private final GetSeveralArtistsRequest getSeveralArtistsRequest = spotifyApi.getSeveralArtists(artistIds).build();

    /*
    SpotifyApi Artist 정보를 CustomArtist 객체로 변환 후 DB에 저장
    */
    @PostConstruct
    public void initArtists() throws IOException, ParseException, SpotifyWebApiException {
        log.info("array = {}", (Object)artistIds);
        Artist[] artists = getSeveralArtistsRequest.execute();
        Arrays.stream(artists).forEach(artist -> {
            com.negongal.hummingbird.domain.Artist customArtist = convertToArtist(artist);
            artistRepository.save(customArtist);
        });
        getArtistSpotifyTrack();
    }

    private void getArtistSpotifyTrack() throws IOException, ParseException, SpotifyWebApiException {
        for (String artist : artistIds) {
            GetArtistsTopTracksRequest getArtistsTopTracksRequest = spotifyApi
                    .getArtistsTopTracks(artist, countryCode)
                    .build();

            Stream<Track> tracks = Arrays.stream(getArtistsTopTracksRequest.execute()).limit(3);
            convertToTrack(tracks, artist);
        }
    }

    private void convertToTrack(Stream<Track> spotifyTracks, String artistId) {
        spotifyTracks.forEach(track -> {
            com.negongal.hummingbird.domain.Artist artist = artistRepository.findById(artistId).orElseThrow(NoSuchElementException::new);
            com.negongal.hummingbird.domain.Track customTrack = com.negongal.hummingbird.domain.Track.builder()
                    .albumName(track.getAlbum().getName())
                    .artist(artist)
                    .releaseDate(track.getAlbum().getReleaseDate())
                    .name(track.getName())
                    .build();

            trackRepository.save(customTrack);
        });
    }

    private com.negongal.hummingbird.domain.Artist convertToArtist(Artist spotifyArtist) {
        String spotifyArtistId = spotifyArtist.getId();
        String spotifyArtistGenre = Arrays.stream(spotifyArtist.getGenres()).findFirst().orElseThrow(NoSuchElementException::new);
        Image spotifyArtistImage = Arrays.stream(spotifyArtist.getImages()).findFirst().orElseThrow(NoSuchElementException::new);
        String spotifyArtistUrl = spotifyArtistImage.getUrl();

        return com.negongal.hummingbird.domain.Artist.builder()
                .id(spotifyArtistId)
                .name(spotifyArtist.getName())
                .genres(spotifyArtistGenre)
                .popularity(spotifyArtist.getPopularity())
                .image(spotifyArtistUrl)
                .build();
    }
}
